begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|AccessSection
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
operator|.
name|Action
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
name|AccountGroup
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
name|AccountGroupName
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|Schema_86
specifier|public
class|class
name|Schema_86
extends|extends
name|SchemaVersion
block|{
DECL|field|allProjects
specifier|private
specifier|final
name|AllProjectsName
name|allProjects
decl_stmt|;
DECL|field|mgr
specifier|private
specifier|final
name|GitRepositoryManager
name|mgr
decl_stmt|;
DECL|field|serverUser
specifier|private
specifier|final
name|PersonIdent
name|serverUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_86 (Provider<Schema_85> prior, AllProjectsName allProjects, GitRepositoryManager mgr, @GerritPersonIdent PersonIdent serverUser)
name|Schema_86
parameter_list|(
name|Provider
argument_list|<
name|Schema_85
argument_list|>
name|prior
parameter_list|,
name|AllProjectsName
name|allProjects
parameter_list|,
name|GitRepositoryManager
name|mgr
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
name|allProjects
operator|=
name|allProjects
expr_stmt|;
name|this
operator|.
name|mgr
operator|=
name|mgr
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
name|Repository
name|git
decl_stmt|;
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|openRepository
argument_list|(
name|allProjects
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
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
try|try
block|{
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
name|allProjects
argument_list|,
name|git
argument_list|)
decl_stmt|;
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
comment|// Create the CHANGE OWNER group.
name|AccountGroup
operator|.
name|UUID
name|adminGroupUUID
init|=
name|findAdminGroup
argument_list|(
name|db
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|createGroup
argument_list|(
name|db
argument_list|,
literal|"Change Owner"
argument_list|,
name|adminGroupUUID
argument_list|,
literal|"The owner of a change"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
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
catch|catch
parameter_list|(
name|ConfigInvalidException
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
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createGroup (ReviewDb db, String groupName, AccountGroup.UUID adminGroupUUID, String description)
specifier|private
name|AccountGroup
name|createGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|String
name|groupName
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|adminGroupUUID
parameter_list|,
name|String
name|description
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|db
operator|.
name|nextAccountGroupId
argument_list|()
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|groupName
argument_list|)
decl_stmt|;
name|AccountGroup
name|group
init|=
operator|new
name|AccountGroup
argument_list|(
name|nameKey
argument_list|,
name|groupId
argument_list|,
name|AccountGroup
operator|.
name|CHANGE_OWNER
argument_list|)
decl_stmt|;
name|group
operator|.
name|setOwnerGroupUUID
argument_list|(
name|adminGroupUUID
argument_list|)
expr_stmt|;
name|group
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|group
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|SYSTEM
argument_list|)
expr_stmt|;
name|AccountGroupName
name|gn
init|=
operator|new
name|AccountGroupName
argument_list|(
name|group
argument_list|)
decl_stmt|;
comment|// first insert the group name to validate that the group name hasn't
comment|// already been used to create another group
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|gn
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|group
return|;
block|}
DECL|method|findAdminGroup ( ReviewDb db, ProjectConfig cfg)
specifier|private
specifier|static
name|AccountGroup
operator|.
name|UUID
name|findAdminGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ProjectConfig
name|cfg
parameter_list|)
block|{
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|rules
init|=
name|cfg
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|)
operator|.
name|getPermission
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
operator|.
name|getRules
argument_list|()
decl_stmt|;
for|for
control|(
name|PermissionRule
name|rule
range|:
name|rules
control|)
block|{
if|if
condition|(
name|rule
operator|.
name|getAction
argument_list|()
operator|==
name|Action
operator|.
name|ALLOW
condition|)
block|{
return|return
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no administrator group found"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

