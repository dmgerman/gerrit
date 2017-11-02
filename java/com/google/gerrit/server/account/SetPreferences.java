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
name|storeSection
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
name|git
operator|.
name|UserConfigSections
operator|.
name|CHANGE_TABLE_COLUMN
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
name|git
operator|.
name|UserConfigSections
operator|.
name|KEY_ID
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
name|git
operator|.
name|UserConfigSections
operator|.
name|KEY_MATCH
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
name|git
operator|.
name|UserConfigSections
operator|.
name|KEY_TARGET
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
name|git
operator|.
name|UserConfigSections
operator|.
name|KEY_TOKEN
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
name|git
operator|.
name|UserConfigSections
operator|.
name|KEY_URL
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
name|git
operator|.
name|UserConfigSections
operator|.
name|URL_ALIAS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|GeneralPreferencesInfo
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
name|MenuItem
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
name|config
operator|.
name|DownloadScheme
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
name|registration
operator|.
name|DynamicMap
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|Config
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|SetPreferences
specifier|public
class|class
name|SetPreferences
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
argument_list|,
name|GeneralPreferencesInfo
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
DECL|field|cache
specifier|private
specifier|final
name|AccountCache
name|cache
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|loader
specifier|private
specifier|final
name|GeneralPreferencesLoader
name|loader
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
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|downloadSchemes
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
decl_stmt|;
annotation|@
name|Inject
DECL|method|SetPreferences ( Provider<CurrentUser> self, AccountCache cache, PermissionBackend permissionBackend, GeneralPreferencesLoader loader, Provider<MetaDataUpdate.User> metaDataUpdateFactory, AllUsersName allUsersName, DynamicMap<DownloadScheme> downloadSchemes)
name|SetPreferences
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|AccountCache
name|cache
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GeneralPreferencesLoader
name|loader
parameter_list|,
name|Provider
argument_list|<
name|MetaDataUpdate
operator|.
name|User
argument_list|>
name|metaDataUpdateFactory
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|DynamicMap
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
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
name|loader
operator|=
name|loader
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|downloadSchemes
operator|=
name|downloadSchemes
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc, GeneralPreferencesInfo i)
specifier|public
name|GeneralPreferencesInfo
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|GeneralPreferencesInfo
name|i
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
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
name|MODIFY_ACCOUNT
argument_list|)
expr_stmt|;
block|}
name|checkDownloadScheme
argument_list|(
name|i
operator|.
name|downloadScheme
argument_list|)
expr_stmt|;
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
name|GeneralPreferencesInfo
name|n
init|=
name|loader
operator|.
name|merge
argument_list|(
name|id
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|n
operator|.
name|changeTable
operator|=
name|i
operator|.
name|changeTable
expr_stmt|;
name|n
operator|.
name|my
operator|=
name|i
operator|.
name|my
expr_stmt|;
name|n
operator|.
name|urlAliases
operator|=
name|i
operator|.
name|urlAliases
expr_stmt|;
name|writeToGit
argument_list|(
name|id
argument_list|,
name|n
argument_list|)
expr_stmt|;
return|return
name|cache
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|getAccount
argument_list|()
operator|.
name|getGeneralPreferencesInfo
argument_list|()
return|;
block|}
DECL|method|writeToGit (Account.Id id, GeneralPreferencesInfo i)
specifier|private
name|void
name|writeToGit
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|GeneralPreferencesInfo
name|i
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|VersionedAccountPreferences
name|prefs
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
name|id
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
name|GENERAL
argument_list|,
literal|null
argument_list|,
name|i
argument_list|,
name|loader
operator|.
name|readDefaultsFromGit
argument_list|(
name|md
operator|.
name|getRepository
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|storeMyChangeTableColumns
argument_list|(
name|prefs
argument_list|,
name|i
operator|.
name|changeTable
argument_list|)
expr_stmt|;
name|storeMyMenus
argument_list|(
name|prefs
argument_list|,
name|i
operator|.
name|my
argument_list|)
expr_stmt|;
name|storeUrlAliases
argument_list|(
name|prefs
argument_list|,
name|i
operator|.
name|urlAliases
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|cache
operator|.
name|evict
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeMyMenus (VersionedAccountPreferences prefs, List<MenuItem> my)
specifier|public
specifier|static
name|void
name|storeMyMenus
parameter_list|(
name|VersionedAccountPreferences
name|prefs
parameter_list|,
name|List
argument_list|<
name|MenuItem
argument_list|>
name|my
parameter_list|)
block|{
name|Config
name|cfg
init|=
name|prefs
operator|.
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|my
operator|!=
literal|null
condition|)
block|{
name|unsetSection
argument_list|(
name|cfg
argument_list|,
name|UserConfigSections
operator|.
name|MY
argument_list|)
expr_stmt|;
for|for
control|(
name|MenuItem
name|item
range|:
name|my
control|)
block|{
name|set
argument_list|(
name|cfg
argument_list|,
name|item
operator|.
name|name
argument_list|,
name|KEY_URL
argument_list|,
name|item
operator|.
name|url
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|cfg
argument_list|,
name|item
operator|.
name|name
argument_list|,
name|KEY_TARGET
argument_list|,
name|item
operator|.
name|target
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|cfg
argument_list|,
name|item
operator|.
name|name
argument_list|,
name|KEY_ID
argument_list|,
name|item
operator|.
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|storeMyChangeTableColumns ( VersionedAccountPreferences prefs, List<String> changeTable)
specifier|public
specifier|static
name|void
name|storeMyChangeTableColumns
parameter_list|(
name|VersionedAccountPreferences
name|prefs
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|changeTable
parameter_list|)
block|{
name|Config
name|cfg
init|=
name|prefs
operator|.
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|changeTable
operator|!=
literal|null
condition|)
block|{
name|unsetSection
argument_list|(
name|cfg
argument_list|,
name|UserConfigSections
operator|.
name|CHANGE_TABLE
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setStringList
argument_list|(
name|UserConfigSections
operator|.
name|CHANGE_TABLE
argument_list|,
literal|null
argument_list|,
name|CHANGE_TABLE_COLUMN
argument_list|,
name|changeTable
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|set (Config cfg, String section, String key, String val)
specifier|private
specifier|static
name|void
name|set
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|val
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|UserConfigSections
operator|.
name|MY
argument_list|,
name|section
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|UserConfigSections
operator|.
name|MY
argument_list|,
name|section
argument_list|,
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unsetSection (Config cfg, String section)
specifier|private
specifier|static
name|void
name|unsetSection
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|section
parameter_list|)
block|{
name|cfg
operator|.
name|unsetSection
argument_list|(
name|section
argument_list|,
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|subsection
range|:
name|cfg
operator|.
name|getSubsections
argument_list|(
name|section
argument_list|)
control|)
block|{
name|cfg
operator|.
name|unsetSection
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeUrlAliases ( VersionedAccountPreferences prefs, Map<String, String> urlAliases)
specifier|public
specifier|static
name|void
name|storeUrlAliases
parameter_list|(
name|VersionedAccountPreferences
name|prefs
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|urlAliases
parameter_list|)
block|{
if|if
condition|(
name|urlAliases
operator|!=
literal|null
condition|)
block|{
name|Config
name|cfg
init|=
name|prefs
operator|.
name|getConfig
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|subsection
range|:
name|cfg
operator|.
name|getSubsections
argument_list|(
name|URL_ALIAS
argument_list|)
control|)
block|{
name|cfg
operator|.
name|unsetSection
argument_list|(
name|URL_ALIAS
argument_list|,
name|subsection
argument_list|)
expr_stmt|;
block|}
name|int
name|i
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|urlAliases
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|URL_ALIAS
argument_list|,
name|URL_ALIAS
operator|+
name|i
argument_list|,
name|KEY_MATCH
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|URL_ALIAS
argument_list|,
name|URL_ALIAS
operator|+
name|i
argument_list|,
name|KEY_TOKEN
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
block|}
block|}
DECL|method|checkDownloadScheme (String downloadScheme)
specifier|private
name|void
name|checkDownloadScheme
parameter_list|(
name|String
name|downloadScheme
parameter_list|)
throws|throws
name|BadRequestException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|downloadScheme
argument_list|)
condition|)
block|{
return|return;
block|}
for|for
control|(
name|DynamicMap
operator|.
name|Entry
argument_list|<
name|DownloadScheme
argument_list|>
name|e
range|:
name|downloadSchemes
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getExportName
argument_list|()
operator|.
name|equals
argument_list|(
name|downloadScheme
argument_list|)
operator|&&
name|e
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Unsupported download scheme: "
operator|+
name|downloadScheme
argument_list|)
throw|;
block|}
block|}
end_class

end_unit
