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
name|ProjectState
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
DECL|field|currentUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|SuggestServiceImpl (final Provider<ReviewDb> schema, final ProjectCache projectCache, final AccountCache accountCache, final Provider<CurrentUser> currentUser)
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
name|ProjectCache
name|projectCache
parameter_list|,
specifier|final
name|AccountCache
name|accountCache
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
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
name|currentUser
operator|=
name|currentUser
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
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|Project
operator|.
name|NameKey
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
name|CurrentUser
name|user
init|=
name|currentUser
operator|.
name|get
argument_list|()
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
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Project
name|p
range|:
name|db
operator|.
name|projects
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
specifier|final
name|ProjectState
name|e
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|p
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|p
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
block|}
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
DECL|method|addSuggestion (Map map, Account account, AccountInfo info, Boolean active)
specifier|private
name|void
name|addSuggestion
parameter_list|(
name|Map
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
name|active
operator|==
literal|null
operator|||
name|active
operator|==
name|account
operator|.
name|isActive
argument_list|()
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
block|}
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
return|return
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
operator|.
name|toList
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

