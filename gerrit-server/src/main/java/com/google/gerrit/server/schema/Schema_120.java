begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|SubscribeSection
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
name|Branch
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
name|Project
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
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcSchema
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|BatchRefUpdate
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
name|NullProgressMonitor
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
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
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
name|transport
operator|.
name|RefSpec
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
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

begin_class
DECL|class|Schema_120
specifier|public
class|class
name|Schema_120
extends|extends
name|SchemaVersion
block|{
DECL|field|mgr
specifier|private
specifier|final
name|GitRepositoryManager
name|mgr
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_120 (Provider<Schema_119> prior, GitRepositoryManager mgr)
name|Schema_120
parameter_list|(
name|Provider
argument_list|<
name|Schema_119
argument_list|>
name|prior
parameter_list|,
name|GitRepositoryManager
name|mgr
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
name|this
operator|.
name|mgr
operator|=
name|mgr
expr_stmt|;
block|}
DECL|method|allowSubmoduleSubscription (Branch.NameKey subbranch, Branch.NameKey superBranch)
specifier|private
name|void
name|allowSubmoduleSubscription
parameter_list|(
name|Branch
operator|.
name|NameKey
name|subbranch
parameter_list|,
name|Branch
operator|.
name|NameKey
name|superBranch
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
init|(
name|Repository
name|git
init|=
name|mgr
operator|.
name|openRepository
argument_list|(
name|subbranch
operator|.
name|getParentKey
argument_list|()
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|BatchRefUpdate
name|bru
init|=
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
try|try
init|(
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|subbranch
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|git
argument_list|,
name|bru
argument_list|)
init|)
block|{
name|md
operator|.
name|setMessage
argument_list|(
literal|"Added superproject subscription during upgrade"
argument_list|)
expr_stmt|;
name|ProjectConfig
name|pc
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|SubscribeSection
name|s
init|=
literal|null
decl_stmt|;
for|for
control|(
name|SubscribeSection
name|s1
range|:
name|pc
operator|.
name|getSubscribeSections
argument_list|(
name|subbranch
argument_list|)
control|)
block|{
if|if
condition|(
name|s
operator|.
name|getProject
argument_list|()
operator|==
name|superBranch
operator|.
name|getParentKey
argument_list|()
condition|)
block|{
name|s
operator|=
name|s1
expr_stmt|;
block|}
block|}
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
operator|new
name|SubscribeSection
argument_list|(
name|superBranch
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|pc
operator|.
name|addSubscribeSection
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|RefSpec
name|newRefSpec
init|=
operator|new
name|RefSpec
argument_list|(
name|subbranch
operator|.
name|get
argument_list|()
operator|+
literal|":"
operator|+
name|superBranch
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|s
operator|.
name|getRefSpecs
argument_list|()
operator|.
name|contains
argument_list|(
name|newRefSpec
argument_list|)
condition|)
block|{
comment|// For the migration we use only exact RefSpecs, we're not trying to
comment|// generalize it.
name|s
operator|.
name|addRefSpec
argument_list|(
name|newRefSpec
argument_list|)
expr_stmt|;
block|}
name|pc
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
name|bru
operator|.
name|execute
argument_list|(
name|rw
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|migrateData (ReviewDb db, UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Generating Superproject subscriptions table to submodule ACLs"
argument_list|)
expr_stmt|;
try|try
init|(
name|Statement
name|stmt
init|=
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT "
operator|+
literal|"key.super_project.project_name, "
operator|+
literal|"key.super_project.branch_name, "
operator|+
literal|"submodule.project_name "
operator|+
literal|"submodule.branch_name "
operator|+
literal|"FROM submodule_subscriptions"
argument_list|)
init|;
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|Project
operator|.
name|NameKey
name|superproject
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|superbranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|superproject
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|submodule
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|4
argument_list|)
argument_list|)
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|subbranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|submodule
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|5
argument_list|)
argument_list|)
decl_stmt|;
name|allowSubmoduleSubscription
argument_list|(
name|subbranch
argument_list|,
name|superbranch
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

