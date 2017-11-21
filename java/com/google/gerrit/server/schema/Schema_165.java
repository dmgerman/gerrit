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
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
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
name|Permission
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
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
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
name|project
operator|.
name|RefPattern
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
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_comment
comment|/** Make default Label-Code-Review permission on user branches exclusive. */
end_comment

begin_class
DECL|class|Schema_165
specifier|public
class|class
name|Schema_165
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
literal|"Make default Label-Code-Review permission on user branches exclusive"
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|systemGroupBackend
specifier|private
specifier|final
name|SystemGroupBackend
name|systemGroupBackend
decl_stmt|;
DECL|field|serverUser
specifier|private
specifier|final
name|PersonIdent
name|serverUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_165 ( Provider<Schema_164> prior, GitRepositoryManager repoManager, AllUsersName allUsersName, SystemGroupBackend systemGroupBackend, @GerritPersonIdent PersonIdent serverUser)
name|Schema_165
parameter_list|(
name|Provider
argument_list|<
name|Schema_164
argument_list|>
name|prior
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|SystemGroupBackend
name|systemGroupBackend
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
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|systemGroupBackend
operator|=
name|systemGroupBackend
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
throws|,
name|SQLException
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
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|Permission
argument_list|>
name|permission
init|=
name|findDefaultPermission
argument_list|(
name|config
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|permission
operator|.
name|isPresent
argument_list|()
condition|)
block|{
comment|// the default permission was not found, hence it cannot be fixed
return|return;
block|}
name|permission
operator|.
name|get
argument_list|()
operator|.
name|setExclusiveGroup
argument_list|(
literal|true
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
name|setMessage
argument_list|(
name|COMMIT_MSG
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Failed to make default Label-Code-Review permission on user branches exclusive"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Searches for the default "Label-Code-Review" permission on the user branch and returns it if it    * was found. If it was not found (e.g. because it was removed or modified) {@link    * Optional#empty()} is returned.    */
DECL|method|findDefaultPermission (ProjectConfig config)
specifier|private
name|Optional
argument_list|<
name|Permission
argument_list|>
name|findDefaultPermission
parameter_list|(
name|ProjectConfig
name|config
parameter_list|)
block|{
name|AccessSection
name|users
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|RefNames
operator|.
name|REFS_USERS
operator|+
literal|"${"
operator|+
name|RefPattern
operator|.
name|USERID_SHARDED
operator|+
literal|"}"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|users
operator|==
literal|null
condition|)
block|{
comment|// default permission was removed
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|Permission
name|permission
init|=
name|users
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|LABEL
operator|+
literal|"Code-Review"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|isDefaultPermissionUntouched
argument_list|(
name|permission
argument_list|)
condition|?
name|Optional
operator|.
name|of
argument_list|(
name|permission
argument_list|)
else|:
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/**    * Checks whether the given permission matches the default "Label-Code-Review" permission on the    * user branch that was initially setup by {@link AllUsersCreator}.    */
DECL|method|isDefaultPermissionUntouched (Permission permission)
specifier|private
name|boolean
name|isDefaultPermissionUntouched
parameter_list|(
name|Permission
name|permission
parameter_list|)
block|{
if|if
condition|(
name|permission
operator|==
literal|null
condition|)
block|{
comment|// default permission was removed
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|permission
operator|.
name|getExclusiveGroup
argument_list|()
condition|)
block|{
comment|// default permission was modified
return|return
literal|false
return|;
block|}
if|if
condition|(
name|permission
operator|.
name|getRules
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
comment|// default permission was modified
return|return
literal|false
return|;
block|}
name|PermissionRule
name|rule
init|=
name|permission
operator|.
name|getRule
argument_list|(
name|systemGroupBackend
operator|.
name|getGroup
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rule
operator|==
literal|null
condition|)
block|{
comment|// default permission was removed
return|return
literal|false
return|;
block|}
if|if
condition|(
name|rule
operator|.
name|getAction
argument_list|()
operator|!=
name|Action
operator|.
name|ALLOW
operator|||
name|rule
operator|.
name|getForce
argument_list|()
operator|||
name|rule
operator|.
name|getMin
argument_list|()
operator|!=
operator|-
literal|2
operator|||
name|rule
operator|.
name|getMax
argument_list|()
operator|!=
literal|2
condition|)
block|{
comment|// default permission was modified
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

