begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|CHANGE_TABLE
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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_class
annotation|@
name|Singleton
DECL|class|GeneralPreferencesLoader
specifier|public
class|class
name|GeneralPreferencesLoader
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
name|GeneralPreferencesLoader
operator|.
name|class
argument_list|)
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
DECL|method|GeneralPreferencesLoader (GitRepositoryManager gitMgr, AllUsersName allUsersName)
specifier|public
name|GeneralPreferencesLoader
parameter_list|(
name|GitRepositoryManager
name|gitMgr
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|)
block|{
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
DECL|method|load (Account.Id id)
specifier|public
name|GeneralPreferencesInfo
name|load
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|RepositoryNotFoundException
block|{
return|return
name|read
argument_list|(
name|id
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|merge (Account.Id id, GeneralPreferencesInfo in)
specifier|public
name|GeneralPreferencesInfo
name|merge
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|GeneralPreferencesInfo
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|RepositoryNotFoundException
block|{
return|return
name|read
argument_list|(
name|id
argument_list|,
name|in
argument_list|)
return|;
block|}
DECL|method|read (Account.Id id, GeneralPreferencesInfo in)
specifier|private
name|GeneralPreferencesInfo
name|read
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|GeneralPreferencesInfo
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
name|allUsers
init|=
name|gitMgr
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
comment|// Load all users default prefs
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
name|allUsers
argument_list|)
expr_stmt|;
name|GeneralPreferencesInfo
name|allUserPrefs
init|=
operator|new
name|GeneralPreferencesInfo
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
name|GENERAL
argument_list|,
literal|null
argument_list|,
name|allUserPrefs
argument_list|,
name|GeneralPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
comment|// Load user prefs
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
name|allUsers
argument_list|)
expr_stmt|;
name|GeneralPreferencesInfo
name|r
init|=
name|loadSection
argument_list|(
name|p
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
operator|new
name|GeneralPreferencesInfo
argument_list|()
argument_list|,
name|updateDefaults
argument_list|(
name|allUserPrefs
argument_list|)
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|loadChangeTableColumns
argument_list|(
name|r
argument_list|,
name|p
argument_list|,
name|dp
argument_list|)
expr_stmt|;
return|return
name|loadMyMenusAndUrlAliases
argument_list|(
name|r
argument_list|,
name|p
argument_list|,
name|dp
argument_list|)
return|;
block|}
block|}
DECL|method|updateDefaults (GeneralPreferencesInfo input)
specifier|private
name|GeneralPreferencesInfo
name|updateDefaults
parameter_list|(
name|GeneralPreferencesInfo
name|input
parameter_list|)
block|{
name|GeneralPreferencesInfo
name|result
init|=
name|GeneralPreferencesInfo
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
name|error
argument_list|(
literal|"Cannot get default general preferences from "
operator|+
name|allUsersName
operator|.
name|get
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|GeneralPreferencesInfo
operator|.
name|defaults
argument_list|()
return|;
block|}
return|return
name|result
return|;
block|}
DECL|method|loadMyMenusAndUrlAliases ( GeneralPreferencesInfo r, VersionedAccountPreferences v, VersionedAccountPreferences d)
specifier|public
name|GeneralPreferencesInfo
name|loadMyMenusAndUrlAliases
parameter_list|(
name|GeneralPreferencesInfo
name|r
parameter_list|,
name|VersionedAccountPreferences
name|v
parameter_list|,
name|VersionedAccountPreferences
name|d
parameter_list|)
block|{
name|r
operator|.
name|my
operator|=
name|my
argument_list|(
name|v
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|my
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|v
operator|.
name|isDefaults
argument_list|()
condition|)
block|{
name|r
operator|.
name|my
operator|=
name|my
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|.
name|my
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|r
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Changes"
argument_list|,
literal|"#/dashboard/self"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Drafts"
argument_list|,
literal|"#/q/owner:self+is:draft"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Draft Comments"
argument_list|,
literal|"#/q/has:draft"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Edits"
argument_list|,
literal|"#/q/has:edit"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Watched Changes"
argument_list|,
literal|"#/q/is:watched+is:open"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Starred Changes"
argument_list|,
literal|"#/q/is:starred"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Groups"
argument_list|,
literal|"#/groups/self"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|urlAliases
operator|=
name|urlAliases
argument_list|(
name|v
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|urlAliases
operator|==
literal|null
operator|&&
operator|!
name|v
operator|.
name|isDefaults
argument_list|()
condition|)
block|{
name|r
operator|.
name|urlAliases
operator|=
name|urlAliases
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|my (VersionedAccountPreferences v)
specifier|private
specifier|static
name|List
argument_list|<
name|MenuItem
argument_list|>
name|my
parameter_list|(
name|VersionedAccountPreferences
name|v
parameter_list|)
block|{
name|List
argument_list|<
name|MenuItem
argument_list|>
name|my
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Config
name|cfg
init|=
name|v
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
name|UserConfigSections
operator|.
name|MY
argument_list|)
control|)
block|{
name|String
name|url
init|=
name|my
argument_list|(
name|cfg
argument_list|,
name|subsection
argument_list|,
name|KEY_URL
argument_list|,
literal|"#/"
argument_list|)
decl_stmt|;
name|String
name|target
init|=
name|my
argument_list|(
name|cfg
argument_list|,
name|subsection
argument_list|,
name|KEY_TARGET
argument_list|,
name|url
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|?
literal|null
else|:
literal|"_blank"
argument_list|)
decl_stmt|;
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
name|subsection
argument_list|,
name|url
argument_list|,
name|target
argument_list|,
name|my
argument_list|(
name|cfg
argument_list|,
name|subsection
argument_list|,
name|KEY_ID
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|my
return|;
block|}
DECL|method|my (Config cfg, String subsection, String key, String defaultValue)
specifier|private
specifier|static
name|String
name|my
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|val
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|UserConfigSections
operator|.
name|MY
argument_list|,
name|subsection
argument_list|,
name|key
argument_list|)
decl_stmt|;
return|return
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|val
argument_list|)
condition|?
name|val
else|:
name|defaultValue
return|;
block|}
DECL|method|loadChangeTableColumns (GeneralPreferencesInfo r, VersionedAccountPreferences v, VersionedAccountPreferences d)
specifier|public
name|GeneralPreferencesInfo
name|loadChangeTableColumns
parameter_list|(
name|GeneralPreferencesInfo
name|r
parameter_list|,
name|VersionedAccountPreferences
name|v
parameter_list|,
name|VersionedAccountPreferences
name|d
parameter_list|)
block|{
name|r
operator|.
name|changeTable
operator|=
name|changeTable
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|Config
name|cfg
init|=
name|v
operator|.
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|changeTable
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|v
operator|.
name|isDefaults
argument_list|()
condition|)
block|{
name|r
operator|.
name|changeTable
operator|=
name|changeTable
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|changeTable (VersionedAccountPreferences v)
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|changeTable
parameter_list|(
name|VersionedAccountPreferences
name|v
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|v
operator|.
name|getConfig
argument_list|()
operator|.
name|getStringList
argument_list|(
name|CHANGE_TABLE
argument_list|,
literal|null
argument_list|,
name|CHANGE_TABLE_COLUMN
argument_list|)
argument_list|)
return|;
block|}
DECL|method|urlAliases (VersionedAccountPreferences v)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|urlAliases
parameter_list|(
name|VersionedAccountPreferences
name|v
parameter_list|)
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|urlAliases
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Config
name|cfg
init|=
name|v
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
name|urlAliases
operator|.
name|put
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
name|URL_ALIAS
argument_list|,
name|subsection
argument_list|,
name|KEY_MATCH
argument_list|)
argument_list|,
name|cfg
operator|.
name|getString
argument_list|(
name|URL_ALIAS
argument_list|,
name|subsection
argument_list|,
name|KEY_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|!
name|urlAliases
operator|.
name|isEmpty
argument_list|()
condition|?
name|urlAliases
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

