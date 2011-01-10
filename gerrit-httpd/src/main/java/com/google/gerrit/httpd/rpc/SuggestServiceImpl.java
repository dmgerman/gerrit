begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
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
name|AccountInfo
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
name|SuggestService
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
name|errors
operator|.
name|NoSuchGroupException
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
name|AccountExternalId
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
name|AccountGroup
operator|.
name|Id
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
name|IdentifiedUser
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
name|AccountCache
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
name|GroupControl
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
name|AuthConfig
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
name|GerritServerConfig
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
name|NoSuchProjectException
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
name|ProjectCache
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
name|ProjectControl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
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
name|client
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
name|lib
operator|.
name|Config
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Set
import|;
end_import

begin_class
DECL|class|SuggestServiceImpl
class|class
name|SuggestServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|SuggestService
block|{
DECL|field|MAX_SUFFIX
specifier|private
specifier|static
specifier|final
name|String
name|MAX_SUFFIX
init|=
literal|"\u9fa5"
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
decl_stmt|;
DECL|field|suggestAccounts
specifier|private
specifier|final
name|SuggestAccountsEnum
name|suggestAccounts
decl_stmt|;
annotation|@
name|Inject
DECL|method|SuggestServiceImpl (final Provider<ReviewDb> schema, final AuthConfig authConfig, final ProjectControl.Factory projectControlFactory, final ProjectCache projectCache, final AccountCache accountCache, final GroupControl.Factory groupControlFactory, final IdentifiedUser.GenericFactory userFactory, final Provider<CurrentUser> currentUser, @GerritServerConfig final Config cfg)
name|SuggestServiceImpl
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
specifier|final
name|AuthConfig
name|authConfig
parameter_list|,
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
specifier|final
name|ProjectCache
name|projectCache
parameter_list|,
specifier|final
name|AccountCache
name|accountCache
parameter_list|,
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
parameter_list|,
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|currentUser
argument_list|)
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
name|this
operator|.
name|suggestAccounts
operator|=
name|cfg
operator|.
name|getEnum
argument_list|(
literal|"suggest"
argument_list|,
literal|null
argument_list|,
literal|"accounts"
argument_list|,
name|SuggestAccountsEnum
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
DECL|method|suggestProjectNameKey (final String query, final int limit, final AsyncCallback<List<Project.NameKey>> callback)
specifier|public
name|void
name|suggestProjectNameKey
parameter_list|(
specifier|final
name|String
name|query
parameter_list|,
specifier|final
name|int
name|limit
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
specifier|final
name|int
name|max
init|=
literal|10
decl_stmt|;
specifier|final
name|int
name|n
init|=
name|limit
operator|<=
literal|0
condition|?
name|max
else|:
name|Math
operator|.
name|min
argument_list|(
name|limit
argument_list|,
name|max
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|(
name|n
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
range|:
name|projectCache
operator|.
name|byName
argument_list|(
name|query
argument_list|)
control|)
block|{
specifier|final
name|ProjectControl
name|ctl
decl_stmt|;
try|try
block|{
name|ctl
operator|=
name|projectControlFactory
operator|.
name|validateFor
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
continue|continue;
block|}
name|r
operator|.
name|add
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|size
argument_list|()
operator|==
name|n
condition|)
block|{
break|break;
block|}
block|}
name|callback
operator|.
name|onSuccess
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|suggestAccount (final String query, final Boolean active, final int limit, final AsyncCallback<List<AccountInfo>> callback)
specifier|public
name|void
name|suggestAccount
parameter_list|(
specifier|final
name|String
name|query
parameter_list|,
specifier|final
name|Boolean
name|active
parameter_list|,
specifier|final
name|int
name|limit
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
if|if
condition|(
name|suggestAccounts
operator|==
name|SuggestAccountsEnum
operator|.
name|OFF
condition|)
block|{
name|callback
operator|.
name|onSuccess
argument_list|(
name|Collections
operator|.
expr|<
name|AccountInfo
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|List
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|String
name|a
init|=
name|query
decl_stmt|;
specifier|final
name|String
name|b
init|=
name|a
operator|+
name|MAX_SUFFIX
decl_stmt|;
specifier|final
name|int
name|max
init|=
literal|10
decl_stmt|;
specifier|final
name|int
name|n
init|=
name|limit
operator|<=
literal|0
condition|?
name|max
else|:
name|Math
operator|.
name|min
argument_list|(
name|limit
argument_list|,
name|max
argument_list|)
decl_stmt|;
specifier|final
name|LinkedHashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountInfo
argument_list|>
name|r
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Account
name|p
range|:
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|suggestByFullName
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|n
argument_list|)
control|)
block|{
name|addSuggestion
argument_list|(
name|r
argument_list|,
name|p
argument_list|,
operator|new
name|AccountInfo
argument_list|(
name|p
argument_list|)
argument_list|,
name|active
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|.
name|size
argument_list|()
operator|<
name|n
condition|)
block|{
for|for
control|(
specifier|final
name|Account
name|p
range|:
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|suggestByPreferredEmail
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|n
operator|-
name|r
operator|.
name|size
argument_list|()
argument_list|)
control|)
block|{
name|addSuggestion
argument_list|(
name|r
argument_list|,
name|p
argument_list|,
operator|new
name|AccountInfo
argument_list|(
name|p
argument_list|)
argument_list|,
name|active
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|r
operator|.
name|size
argument_list|()
operator|<
name|n
condition|)
block|{
for|for
control|(
specifier|final
name|AccountExternalId
name|e
range|:
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|suggestByEmailAddress
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|n
operator|-
name|r
operator|.
name|size
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|r
operator|.
name|containsKey
argument_list|(
name|e
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
specifier|final
name|Account
name|p
init|=
name|accountCache
operator|.
name|get
argument_list|(
name|e
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
specifier|final
name|AccountInfo
name|info
init|=
operator|new
name|AccountInfo
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|info
operator|.
name|setPreferredEmail
argument_list|(
name|e
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|addSuggestion
argument_list|(
name|r
argument_list|,
name|p
argument_list|,
name|info
argument_list|,
name|active
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|ArrayList
argument_list|<
name|AccountInfo
argument_list|>
argument_list|(
name|r
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|addSuggestion (Map<Account.Id, AccountInfo> map, Account account, AccountInfo info, Boolean active)
specifier|private
name|void
name|addSuggestion
parameter_list|(
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountInfo
argument_list|>
name|map
parameter_list|,
name|Account
name|account
parameter_list|,
name|AccountInfo
name|info
parameter_list|,
name|Boolean
name|active
parameter_list|)
block|{
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|active
operator|!=
literal|null
operator|&&
name|active
operator|!=
name|account
operator|.
name|isActive
argument_list|()
condition|)
block|{
return|return;
block|}
switch|switch
condition|(
name|suggestAccounts
condition|)
block|{
case|case
name|ALL
case|:
name|map
operator|.
name|put
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|,
name|info
argument_list|)
expr_stmt|;
break|break;
case|case
name|SAME_GROUP
case|:
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|usersGroups
init|=
name|groupsOf
argument_list|(
name|account
argument_list|)
decl_stmt|;
name|usersGroups
operator|.
name|removeAll
argument_list|(
name|authConfig
operator|.
name|getRegisteredGroups
argument_list|()
argument_list|)
expr_stmt|;
name|usersGroups
operator|.
name|remove
argument_list|(
name|authConfig
operator|.
name|getBatchUsersGroup
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|Id
name|myGroup
range|:
name|currentUser
operator|.
name|get
argument_list|()
operator|.
name|getEffectiveGroups
argument_list|()
control|)
block|{
if|if
condition|(
name|usersGroups
operator|.
name|contains
argument_list|(
name|myGroup
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|,
name|info
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
break|break;
block|}
case|case
name|OFF
case|:
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Bad SuggestAccounts "
operator|+
name|suggestAccounts
argument_list|)
throw|;
block|}
block|}
DECL|method|groupsOf (Account account)
specifier|private
name|Set
argument_list|<
name|Id
argument_list|>
name|groupsOf
parameter_list|(
name|Account
name|account
parameter_list|)
block|{
name|IdentifiedUser
name|user
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|(
name|user
operator|.
name|getEffectiveGroups
argument_list|()
argument_list|)
return|;
block|}
DECL|method|suggestAccountGroup (final String query, final int limit, final AsyncCallback<List<AccountGroupName>> callback)
specifier|public
name|void
name|suggestAccountGroup
parameter_list|(
specifier|final
name|String
name|query
parameter_list|,
specifier|final
name|int
name|limit
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountGroupName
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|List
argument_list|<
name|AccountGroupName
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|AccountGroupName
argument_list|>
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|String
name|a
init|=
name|query
decl_stmt|;
specifier|final
name|String
name|b
init|=
name|a
operator|+
name|MAX_SUFFIX
decl_stmt|;
specifier|final
name|int
name|max
init|=
literal|10
decl_stmt|;
specifier|final
name|int
name|n
init|=
name|limit
operator|<=
literal|0
condition|?
name|max
else|:
name|Math
operator|.
name|min
argument_list|(
name|limit
argument_list|,
name|max
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|memberOf
init|=
name|currentUser
operator|.
name|get
argument_list|()
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AccountGroupName
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroupName
argument_list|>
argument_list|(
name|n
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroupName
name|group
range|:
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|suggestByName
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|n
argument_list|)
control|)
block|{
try|try
block|{
if|if
condition|(
name|memberOf
operator|.
name|contains
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|||
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
continue|continue;
block|}
block|}
return|return
name|names
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

