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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|Schema_130
specifier|public
class|class
name|Schema_130
extends|extends
name|SchemaVersion
block|{
DECL|field|COMMIT_MSG
specifier|private
specifier|static
specifier|final
name|String
name|COMMIT_MSG
init|=
literal|"Remove force option from 'Push Annotated Tag' permission\n"
operator|+
literal|"\n"
operator|+
literal|"The force option on 'Push Annotated Tag' had no effect and is no longer\n"
operator|+
literal|"supported."
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|serverUser
specifier|private
specifier|final
name|PersonIdent
name|serverUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_130 (Provider<Schema_129> prior, GitRepositoryManager repoManager, @GerritPersonIdent PersonIdent serverUser)
name|Schema_130
parameter_list|(
name|Provider
argument_list|<
name|Schema_129
argument_list|>
name|prior
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverUser
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|serverUser
operator|=
name|serverUser
expr_stmt|;
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
block|{
for|for
control|(
name|Project
operator|.
name|NameKey
name|projectName
range|:
name|repoManager
operator|.
name|list
argument_list|()
control|)
block|{
try|try
init|(
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectName
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
name|projectName
argument_list|,
name|git
argument_list|)
init|)
block|{
name|ProjectConfigSchemaUpdate
name|cfg
init|=
name|ProjectConfigSchemaUpdate
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|removeForceFromPermission
argument_list|(
literal|"pushTag"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|save
argument_list|(
name|serverUser
argument_list|,
name|COMMIT_MSG
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
literal|"Cannot migrate project "
operator|+
name|projectName
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

