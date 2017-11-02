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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|config
operator|.
name|ConfigUtil
operator|.
name|loadSection
import|;
end_import

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
name|config
operator|.
name|ConfigUtil
operator|.
name|skipField
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
name|client
operator|.
name|DiffPreferencesInfo
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|RestReadView
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
name|Account
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
name|CurrentUser
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
name|UserConfigSections
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
name|permissions
operator|.
name|GlobalPermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GetDiffPreferences
specifier|public
class|class
name|GetDiffPreferences
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GetDiffPreferences
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|Provider
argument_list|<
name|AllUsersName
argument_list|>
name|allUsersName
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|gitMgr
specifier|private
specifier|final
name|GitRepositoryManager
name|gitMgr
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetDiffPreferences ( Provider<CurrentUser> self, Provider<AllUsersName> allUsersName, PermissionBackend permissionBackend, GitRepositoryManager gitMgr)
name|GetDiffPreferences
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|Provider
argument_list|<
name|AllUsersName
argument_list|>
name|allUsersName
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GitRepositoryManager
name|gitMgr
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|gitMgr
operator|=
name|gitMgr
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc)
specifier|public
name|DiffPreferencesInfo
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ConfigInvalidException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|rsrc
operator|.
name|getUser
argument_list|()
condition|)
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|self
argument_list|)
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
expr_stmt|;
block|}
name|Account
operator|.
name|Id
name|id
init|=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
return|return
name|readFromGit
argument_list|(
name|id
argument_list|,
name|gitMgr
argument_list|,
name|allUsersName
operator|.
name|get
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|readFromGit ( Account.Id id, GitRepositoryManager gitMgr, AllUsersName allUsersName, DiffPreferencesInfo in)
specifier|static
name|DiffPreferencesInfo
name|readFromGit
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|GitRepositoryManager
name|gitMgr
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|DiffPreferencesInfo
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|RepositoryNotFoundException
block|{
try|try
init|(
name|Repository
name|git
init|=
name|gitMgr
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|VersionedAccountPreferences
name|p
init|=
name|VersionedAccountPreferences
operator|.
name|forUser
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|p
operator|.
name|load
argument_list|(
name|git
argument_list|)
expr_stmt|;
name|DiffPreferencesInfo
name|prefs
init|=
operator|new
name|DiffPreferencesInfo
argument_list|()
decl_stmt|;
name|loadSection
argument_list|(
name|p
operator|.
name|getConfig
argument_list|()
argument_list|,
name|UserConfigSections
operator|.
name|DIFF
argument_list|,
literal|null
argument_list|,
name|prefs
argument_list|,
name|readDefaultsFromGit
argument_list|(
name|git
argument_list|,
name|in
argument_list|)
argument_list|,
name|in
argument_list|)
expr_stmt|;
return|return
name|prefs
return|;
block|}
block|}
DECL|method|readDefaultsFromGit (Repository git, DiffPreferencesInfo in)
specifier|static
name|DiffPreferencesInfo
name|readDefaultsFromGit
parameter_list|(
name|Repository
name|git
parameter_list|,
name|DiffPreferencesInfo
name|in
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|VersionedAccountPreferences
name|dp
init|=
name|VersionedAccountPreferences
operator|.
name|forDefault
argument_list|()
decl_stmt|;
name|dp
operator|.
name|load
argument_list|(
name|git
argument_list|)
expr_stmt|;
name|DiffPreferencesInfo
name|allUserPrefs
init|=
operator|new
name|DiffPreferencesInfo
argument_list|()
decl_stmt|;
name|loadSection
argument_list|(
name|dp
operator|.
name|getConfig
argument_list|()
argument_list|,
name|UserConfigSections
operator|.
name|DIFF
argument_list|,
literal|null
argument_list|,
name|allUserPrefs
argument_list|,
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
return|return
name|updateDefaults
argument_list|(
name|allUserPrefs
argument_list|)
return|;
block|}
DECL|method|updateDefaults (DiffPreferencesInfo input)
specifier|private
specifier|static
name|DiffPreferencesInfo
name|updateDefaults
parameter_list|(
name|DiffPreferencesInfo
name|input
parameter_list|)
block|{
name|DiffPreferencesInfo
name|result
init|=
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|Field
name|field
range|:
name|input
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
name|skipField
argument_list|(
name|field
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Object
name|newVal
init|=
name|field
operator|.
name|get
argument_list|(
name|input
argument_list|)
decl_stmt|;
if|if
condition|(
name|newVal
operator|!=
literal|null
condition|)
block|{
name|field
operator|.
name|set
argument_list|(
name|result
argument_list|,
name|newVal
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot get default diff preferences from All-Users"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
return|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit
