begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|GerritApi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|GerritPersonIdent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|AllProjectsName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|AllUsersName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|GitRepositoryManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|MetaDataUpdate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|ProjectConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
operator|.
name|SchemaUpgradeTestEnvironment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
operator|.
name|TestUpdateUI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|Schema_161_to_162_Test
specifier|public
class|class
name|Schema_161_to_162_Test
block|{
DECL|field|testEnv
annotation|@
name|Rule
specifier|public
name|SchemaUpgradeTestEnvironment
name|testEnv
init|=
operator|new
name|SchemaUpgradeTestEnvironment
argument_list|()
decl_stmt|;
DECL|field|allProjectsName
annotation|@
name|Inject
specifier|private
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|allUsersName
annotation|@
name|Inject
specifier|private
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|gApi
annotation|@
name|Inject
specifier|private
name|GerritApi
name|gApi
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|schema162
annotation|@
name|Inject
specifier|private
name|Schema_162
name|schema162
decl_stmt|;
DECL|field|db
annotation|@
name|Inject
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|serverUser
annotation|@
name|Inject
annotation|@
name|GerritPersonIdent
specifier|private
name|PersonIdent
name|serverUser
decl_stmt|;
annotation|@
name|Test
DECL|method|skipCorrectInheritance ()
specifier|public
name|void
name|skipCorrectInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThatAllUsersInheritsFrom
argument_list|(
name|allProjectsName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectId
name|oldHead
decl_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|oldHead
operator|=
name|git
operator|.
name|findRef
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
name|schema162
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check that the parent remained unchanged and that no commit was made
name|assertThatAllUsersInheritsFrom
argument_list|(
name|allProjectsName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|oldHead
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|git
operator|.
name|findRef
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|fixIncorrectInheritance ()
specifier|public
name|void
name|fixIncorrectInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testProject
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
literal|"test"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|name
decl_stmt|;
name|assertThatAllUsersInheritsFrom
argument_list|(
name|allProjectsName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|;
name|MetaDataUpdate
name|md
operator|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allUsersName
argument_list|,
name|git
argument_list|)
init|)
block|{
name|ProjectConfig
name|cfg
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|getProject
argument_list|()
operator|.
name|setParentName
argument_list|(
name|testProject
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|assertThatAllUsersInheritsFrom
argument_list|(
name|testProject
argument_list|)
expr_stmt|;
name|schema162
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
name|assertThatAllUsersInheritsFrom
argument_list|(
name|allProjectsName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertThatAllUsersInheritsFrom (String parent)
specifier|private
name|void
name|assertThatAllUsersInheritsFrom
parameter_list|(
name|String
name|parent
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|allUsersName
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|access
argument_list|()
operator|.
name|inheritsFrom
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

