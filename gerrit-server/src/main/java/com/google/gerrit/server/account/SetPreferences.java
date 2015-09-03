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
name|account
operator|.
name|GetPreferences
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
name|account
operator|.
name|GetPreferences
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
name|account
operator|.
name|GetPreferences
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
name|account
operator|.
name|GetPreferences
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
name|account
operator|.
name|GetPreferences
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
name|account
operator|.
name|GetPreferences
operator|.
name|MY
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
name|account
operator|.
name|GetPreferences
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
name|ResourceNotFoundException
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
name|extensions
operator|.
name|webui
operator|.
name|TopMenu
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
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
name|AccountGeneralPreferences
operator|.
name|DateFormat
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
name|AccountGeneralPreferences
operator|.
name|DiffView
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
name|AccountGeneralPreferences
operator|.
name|DownloadCommand
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
name|AccountGeneralPreferences
operator|.
name|ReviewCategoryStrategy
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
name|AccountGeneralPreferences
operator|.
name|TimeFormat
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
name|account
operator|.
name|SetPreferences
operator|.
name|Input
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
name|Config
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
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|changesPerPage
specifier|public
name|Short
name|changesPerPage
decl_stmt|;
DECL|field|showSiteHeader
specifier|public
name|Boolean
name|showSiteHeader
decl_stmt|;
DECL|field|useFlashClipboard
specifier|public
name|Boolean
name|useFlashClipboard
decl_stmt|;
DECL|field|downloadScheme
specifier|public
name|String
name|downloadScheme
decl_stmt|;
DECL|field|downloadCommand
specifier|public
name|DownloadCommand
name|downloadCommand
decl_stmt|;
DECL|field|copySelfOnEmail
specifier|public
name|Boolean
name|copySelfOnEmail
decl_stmt|;
DECL|field|dateFormat
specifier|public
name|DateFormat
name|dateFormat
decl_stmt|;
DECL|field|timeFormat
specifier|public
name|TimeFormat
name|timeFormat
decl_stmt|;
DECL|field|relativeDateInChangeTable
specifier|public
name|Boolean
name|relativeDateInChangeTable
decl_stmt|;
DECL|field|sizeBarInChangeTable
specifier|public
name|Boolean
name|sizeBarInChangeTable
decl_stmt|;
DECL|field|legacycidInChangeTable
specifier|public
name|Boolean
name|legacycidInChangeTable
decl_stmt|;
DECL|field|muteCommonPathPrefixes
specifier|public
name|Boolean
name|muteCommonPathPrefixes
decl_stmt|;
DECL|field|reviewCategoryStrategy
specifier|public
name|ReviewCategoryStrategy
name|reviewCategoryStrategy
decl_stmt|;
DECL|field|diffView
specifier|public
name|DiffView
name|diffView
decl_stmt|;
DECL|field|my
specifier|public
name|List
argument_list|<
name|TopMenu
operator|.
name|MenuItem
argument_list|>
name|my
decl_stmt|;
DECL|field|urlAliases
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|urlAliases
decl_stmt|;
block|}
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
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
annotation|@
name|Inject
DECL|method|SetPreferences (Provider<CurrentUser> self, AccountCache cache, Provider<ReviewDb> db, MetaDataUpdate.User metaDataUpdateFactory, AllUsersName allUsersName)
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
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
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
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
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
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc, Input i)
specifier|public
name|GetPreferences
operator|.
name|PreferenceInfo
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|Input
name|i
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
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
operator|&&
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canModifyAccount
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"restricted to members of Modify Accounts"
argument_list|)
throw|;
block|}
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
name|i
operator|=
operator|new
name|Input
argument_list|()
expr_stmt|;
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
name|AccountGeneralPreferences
name|p
decl_stmt|;
name|VersionedAccountPreferences
name|versionedPrefs
decl_stmt|;
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|allUsersName
argument_list|)
decl_stmt|;
name|db
operator|.
name|get
argument_list|()
operator|.
name|accounts
argument_list|()
operator|.
name|beginTransaction
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
try|try
block|{
name|Account
name|a
init|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|accounts
argument_list|()
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
name|versionedPrefs
operator|=
name|VersionedAccountPreferences
operator|.
name|forUser
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
name|versionedPrefs
operator|.
name|load
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|p
operator|=
name|a
operator|.
name|getGeneralPreferences
argument_list|()
expr_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|p
operator|=
operator|new
name|AccountGeneralPreferences
argument_list|()
expr_stmt|;
name|a
operator|.
name|setGeneralPreferences
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|changesPerPage
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setMaximumPageSize
argument_list|(
name|i
operator|.
name|changesPerPage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|showSiteHeader
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setShowSiteHeader
argument_list|(
name|i
operator|.
name|showSiteHeader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|useFlashClipboard
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setUseFlashClipboard
argument_list|(
name|i
operator|.
name|useFlashClipboard
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|downloadScheme
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setDownloadUrl
argument_list|(
name|i
operator|.
name|downloadScheme
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|downloadCommand
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setDownloadCommand
argument_list|(
name|i
operator|.
name|downloadCommand
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|copySelfOnEmail
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setCopySelfOnEmails
argument_list|(
name|i
operator|.
name|copySelfOnEmail
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|dateFormat
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setDateFormat
argument_list|(
name|i
operator|.
name|dateFormat
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|timeFormat
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setTimeFormat
argument_list|(
name|i
operator|.
name|timeFormat
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|relativeDateInChangeTable
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setRelativeDateInChangeTable
argument_list|(
name|i
operator|.
name|relativeDateInChangeTable
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|sizeBarInChangeTable
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setSizeBarInChangeTable
argument_list|(
name|i
operator|.
name|sizeBarInChangeTable
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|legacycidInChangeTable
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setLegacycidInChangeTable
argument_list|(
name|i
operator|.
name|legacycidInChangeTable
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|muteCommonPathPrefixes
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setMuteCommonPathPrefixes
argument_list|(
name|i
operator|.
name|muteCommonPathPrefixes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|reviewCategoryStrategy
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setReviewCategoryStrategy
argument_list|(
name|i
operator|.
name|reviewCategoryStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|.
name|diffView
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setDiffView
argument_list|(
name|i
operator|.
name|diffView
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|get
argument_list|()
operator|.
name|accounts
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|get
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
name|storeMyMenus
argument_list|(
name|versionedPrefs
argument_list|,
name|i
operator|.
name|my
argument_list|)
expr_stmt|;
name|storeUrlAliases
argument_list|(
name|versionedPrefs
argument_list|,
name|i
operator|.
name|urlAliases
argument_list|)
expr_stmt|;
name|versionedPrefs
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
name|accountId
argument_list|)
expr_stmt|;
return|return
operator|new
name|GetPreferences
operator|.
name|PreferenceInfo
argument_list|(
name|p
argument_list|,
name|versionedPrefs
argument_list|,
name|md
operator|.
name|getRepository
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
name|db
operator|.
name|get
argument_list|()
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|storeMyMenus (VersionedAccountPreferences prefs, List<TopMenu.MenuItem> my)
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
name|TopMenu
operator|.
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
name|MY
argument_list|)
expr_stmt|;
for|for
control|(
name|TopMenu
operator|.
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
DECL|method|storeUrlAliases (VersionedAccountPreferences prefs, Map<String, String> urlAliases)
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
block|}
end_class

end_unit

