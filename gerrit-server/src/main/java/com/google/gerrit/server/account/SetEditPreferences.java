begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|account
operator|.
name|GetEditPreferences
operator|.
name|readFromGit
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
name|storeSection
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
name|EditPreferencesInfo
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
name|BadRequestException
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
name|RestModifyView
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

begin_class
annotation|@
name|Singleton
DECL|class|SetEditPreferences
specifier|public
class|class
name|SetEditPreferences
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
argument_list|,
name|EditPreferencesInfo
argument_list|>
block|{
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|Provider
argument_list|<
name|MetaDataUpdate
operator|.
name|User
argument_list|>
name|metaDataUpdateFactory
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
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
annotation|@
name|Inject
DECL|method|SetEditPreferences ( Provider<CurrentUser> self, Provider<MetaDataUpdate.User> metaDataUpdateFactory, PermissionBackend permissionBackend, GitRepositoryManager gitMgr, AllUsersName allUsersName)
name|SetEditPreferences
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|Provider
argument_list|<
name|MetaDataUpdate
operator|.
name|User
argument_list|>
name|metaDataUpdateFactory
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GitRepositoryManager
name|gitMgr
parameter_list|,
name|AllUsersName
name|allUsersName
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
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
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
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc, EditPreferencesInfo in)
specifier|public
name|EditPreferencesInfo
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|EditPreferencesInfo
name|in
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|hasSameAccountId
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
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
name|MODIFY_ACCOUNT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"input must be provided"
argument_list|)
throw|;
block|}
name|Account
operator|.
name|Id
name|accountId
init|=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|VersionedAccountPreferences
name|prefs
decl_stmt|;
name|EditPreferencesInfo
name|out
init|=
operator|new
name|EditPreferencesInfo
argument_list|()
decl_stmt|;
try|try
init|(
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|get
argument_list|()
operator|.
name|create
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|prefs
operator|=
name|VersionedAccountPreferences
operator|.
name|forUser
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|load
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|storeSection
argument_list|(
name|prefs
operator|.
name|getConfig
argument_list|()
argument_list|,
name|UserConfigSections
operator|.
name|EDIT
argument_list|,
literal|null
argument_list|,
name|readFromGit
argument_list|(
name|accountId
argument_list|,
name|gitMgr
argument_list|,
name|allUsersName
argument_list|,
name|in
argument_list|)
argument_list|,
name|EditPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|out
operator|=
name|loadSection
argument_list|(
name|prefs
operator|.
name|getConfig
argument_list|()
argument_list|,
name|UserConfigSections
operator|.
name|EDIT
argument_list|,
literal|null
argument_list|,
name|out
argument_list|,
name|EditPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
block|}
end_class

end_unit

