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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

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
name|Function
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
name|MoreObjects
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
name|ImmutableList
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
name|Iterables
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
name|common
operator|.
name|collect
operator|.
name|Ordering
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
name|Nullable
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
name|GroupReference
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
name|extensions
operator|.
name|common
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
name|extensions
operator|.
name|common
operator|.
name|GroupBaseInfo
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
name|common
operator|.
name|SuggestedReviewerInfo
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
name|Url
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
name|AccountControl
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
name|AccountLoader
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
name|AccountState
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
name|GroupBackend
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
name|GroupMembers
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
name|AccountDirectory
operator|.
name|FillOptions
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
name|change
operator|.
name|PostReviewers
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
name|change
operator|.
name|SuggestReviewers
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
name|index
operator|.
name|account
operator|.
name|AccountIndex
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
name|index
operator|.
name|account
operator|.
name|AccountIndexCollection
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
name|ProjectControl
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
name|query
operator|.
name|QueryParseException
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
name|query
operator|.
name|QueryResult
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
name|query
operator|.
name|account
operator|.
name|AccountQueryBuilder
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
name|query
operator|.
name|account
operator|.
name|AccountQueryProcessor
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
name|Collection
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
name|EnumSet
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
DECL|class|ReviewersUtil
specifier|public
class|class
name|ReviewersUtil
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
DECL|field|ORDERING
specifier|private
specifier|static
specifier|final
name|Ordering
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|ORDERING
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|onResultOf
argument_list|(
operator|new
name|Function
argument_list|<
name|SuggestedReviewerInfo
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
annotation|@
name|Nullable
name|SuggestedReviewerInfo
name|suggestedReviewerInfo
parameter_list|)
block|{
if|if
condition|(
name|suggestedReviewerInfo
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|suggestedReviewerInfo
operator|.
name|account
operator|!=
literal|null
condition|?
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|suggestedReviewerInfo
operator|.
name|account
operator|.
name|email
argument_list|,
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|suggestedReviewerInfo
operator|.
name|account
operator|.
name|name
argument_list|)
argument_list|)
else|:
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|suggestedReviewerInfo
operator|.
name|group
operator|.
name|name
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
DECL|field|accountLoader
specifier|private
specifier|final
name|AccountLoader
name|accountLoader
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|AccountIndexCollection
name|indexes
decl_stmt|;
DECL|field|queryBuilder
specifier|private
specifier|final
name|AccountQueryBuilder
name|queryBuilder
decl_stmt|;
DECL|field|queryProcessor
specifier|private
specifier|final
name|AccountQueryProcessor
name|queryProcessor
decl_stmt|;
DECL|field|accountControl
specifier|private
specifier|final
name|AccountControl
name|accountControl
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|groupMembersFactory
specifier|private
specifier|final
name|GroupMembers
operator|.
name|Factory
name|groupMembersFactory
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
DECL|method|ReviewersUtil (AccountLoader.Factory accountLoaderFactory, AccountCache accountCache, AccountIndexCollection indexes, AccountQueryBuilder queryBuilder, AccountQueryProcessor queryProcessor, AccountControl.Factory accountControlFactory, Provider<ReviewDb> dbProvider, GroupBackend groupBackend, GroupMembers.Factory groupMembersFactory, Provider<CurrentUser> currentUser)
name|ReviewersUtil
parameter_list|(
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|AccountIndexCollection
name|indexes
parameter_list|,
name|AccountQueryBuilder
name|queryBuilder
parameter_list|,
name|AccountQueryProcessor
name|queryProcessor
parameter_list|,
name|AccountControl
operator|.
name|Factory
name|accountControlFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|GroupMembers
operator|.
name|Factory
name|groupMembersFactory
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
parameter_list|)
block|{
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|fillOptions
init|=
name|EnumSet
operator|.
name|of
argument_list|(
name|FillOptions
operator|.
name|SECONDARY_EMAILS
argument_list|)
decl_stmt|;
name|fillOptions
operator|.
name|addAll
argument_list|(
name|AccountLoader
operator|.
name|DETAILED_OPTIONS
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountLoader
operator|=
name|accountLoaderFactory
operator|.
name|create
argument_list|(
name|fillOptions
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
expr_stmt|;
name|this
operator|.
name|queryProcessor
operator|=
name|queryProcessor
expr_stmt|;
name|this
operator|.
name|accountControl
operator|=
name|accountControlFactory
operator|.
name|get
argument_list|()
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|groupMembersFactory
operator|=
name|groupMembersFactory
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
block|}
DECL|interface|VisibilityControl
specifier|public
interface|interface
name|VisibilityControl
block|{
DECL|method|isVisibleTo (Account.Id account)
name|boolean
name|isVisibleTo
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
throws|throws
name|OrmException
function_decl|;
block|}
DECL|method|suggestReviewers ( SuggestReviewers suggestReviewers, ProjectControl projectControl, VisibilityControl visibilityControl)
specifier|public
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|suggestReviewers
parameter_list|(
name|SuggestReviewers
name|suggestReviewers
parameter_list|,
name|ProjectControl
name|projectControl
parameter_list|,
name|VisibilityControl
name|visibilityControl
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
throws|,
name|BadRequestException
block|{
name|String
name|query
init|=
name|suggestReviewers
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|boolean
name|suggestAccounts
init|=
name|suggestReviewers
operator|.
name|getSuggestAccounts
argument_list|()
decl_stmt|;
name|int
name|suggestFrom
init|=
name|suggestReviewers
operator|.
name|getSuggestFrom
argument_list|()
decl_stmt|;
name|int
name|limit
init|=
name|suggestReviewers
operator|.
name|getLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"missing query field"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|suggestAccounts
operator|||
name|query
operator|.
name|length
argument_list|()
operator|<
name|suggestFrom
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
name|suggestedAccounts
init|=
name|suggestAccounts
argument_list|(
name|suggestReviewers
argument_list|,
name|visibilityControl
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|reviewer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountInfo
name|a
range|:
name|suggestedAccounts
control|)
block|{
name|SuggestedReviewerInfo
name|info
init|=
operator|new
name|SuggestedReviewerInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|account
operator|=
name|a
expr_stmt|;
name|reviewer
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|GroupReference
name|g
range|:
name|suggestAccountGroup
argument_list|(
name|suggestReviewers
argument_list|,
name|projectControl
argument_list|)
control|)
block|{
if|if
condition|(
name|suggestGroupAsReviewer
argument_list|(
name|suggestReviewers
argument_list|,
name|projectControl
operator|.
name|getProject
argument_list|()
argument_list|,
name|g
argument_list|,
name|visibilityControl
argument_list|)
condition|)
block|{
name|GroupBaseInfo
name|info
init|=
operator|new
name|GroupBaseInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|id
operator|=
name|Url
operator|.
name|encode
argument_list|(
name|g
operator|.
name|getUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|name
operator|=
name|g
operator|.
name|getName
argument_list|()
expr_stmt|;
name|SuggestedReviewerInfo
name|suggestedReviewerInfo
init|=
operator|new
name|SuggestedReviewerInfo
argument_list|()
decl_stmt|;
name|suggestedReviewerInfo
operator|.
name|group
operator|=
name|info
expr_stmt|;
name|reviewer
operator|.
name|add
argument_list|(
name|suggestedReviewerInfo
argument_list|)
expr_stmt|;
block|}
block|}
name|reviewer
operator|=
name|ORDERING
operator|.
name|immutableSortedCopy
argument_list|(
name|reviewer
argument_list|)
expr_stmt|;
if|if
condition|(
name|reviewer
operator|.
name|size
argument_list|()
operator|<=
name|limit
condition|)
block|{
return|return
name|reviewer
return|;
block|}
return|return
name|reviewer
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|limit
argument_list|)
return|;
block|}
DECL|method|suggestAccounts (SuggestReviewers suggestReviewers, VisibilityControl visibilityControl)
specifier|private
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
name|suggestAccounts
parameter_list|(
name|SuggestReviewers
name|suggestReviewers
parameter_list|,
name|VisibilityControl
name|visibilityControl
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountIndex
name|searchIndex
init|=
name|indexes
operator|.
name|getSearchIndex
argument_list|()
decl_stmt|;
if|if
condition|(
name|searchIndex
operator|!=
literal|null
condition|)
block|{
return|return
name|suggestAccountsFromIndex
argument_list|(
name|suggestReviewers
argument_list|)
return|;
block|}
return|return
name|suggestAccountsFromDb
argument_list|(
name|suggestReviewers
argument_list|,
name|visibilityControl
argument_list|)
return|;
block|}
DECL|method|suggestAccountsFromIndex ( SuggestReviewers suggestReviewers)
specifier|private
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
name|suggestAccountsFromIndex
parameter_list|(
name|SuggestReviewers
name|suggestReviewers
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
block|{
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountInfo
argument_list|>
name|matches
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|QueryResult
argument_list|<
name|AccountState
argument_list|>
name|result
init|=
name|queryProcessor
operator|.
name|setLimit
argument_list|(
name|suggestReviewers
operator|.
name|getLimit
argument_list|()
argument_list|)
operator|.
name|query
argument_list|(
name|queryBuilder
operator|.
name|defaultQuery
argument_list|(
name|suggestReviewers
operator|.
name|getQuery
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountState
name|accountState
range|:
name|result
operator|.
name|entities
argument_list|()
control|)
block|{
name|Account
operator|.
name|Id
name|id
init|=
name|accountState
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|matches
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|accountLoader
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|accountLoader
operator|.
name|fill
argument_list|()
expr_stmt|;
return|return
name|matches
operator|.
name|values
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
block|}
DECL|method|suggestAccountsFromDb ( SuggestReviewers suggestReviewers, VisibilityControl visibilityControl)
specifier|private
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
name|suggestAccountsFromDb
parameter_list|(
name|SuggestReviewers
name|suggestReviewers
parameter_list|,
name|VisibilityControl
name|visibilityControl
parameter_list|)
throws|throws
name|OrmException
block|{
name|String
name|query
init|=
name|suggestReviewers
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|int
name|limit
init|=
name|suggestReviewers
operator|.
name|getLimit
argument_list|()
decl_stmt|;
name|String
name|a
init|=
name|query
decl_stmt|;
name|String
name|b
init|=
name|a
operator|+
name|MAX_SUFFIX
decl_stmt|;
name|Map
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
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|String
argument_list|>
name|queryEmail
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
name|p
range|:
name|dbProvider
operator|.
name|get
argument_list|()
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
name|limit
argument_list|)
control|)
block|{
if|if
condition|(
name|p
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|addSuggestion
argument_list|(
name|r
argument_list|,
name|p
operator|.
name|getId
argument_list|()
argument_list|,
name|visibilityControl
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
name|limit
condition|)
block|{
for|for
control|(
name|Account
name|p
range|:
name|dbProvider
operator|.
name|get
argument_list|()
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
name|limit
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
name|p
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|addSuggestion
argument_list|(
name|r
argument_list|,
name|p
operator|.
name|getId
argument_list|()
argument_list|,
name|visibilityControl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|r
operator|.
name|size
argument_list|()
operator|<
name|limit
condition|)
block|{
for|for
control|(
name|AccountExternalId
name|e
range|:
name|dbProvider
operator|.
name|get
argument_list|()
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
name|limit
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
if|if
condition|(
name|p
operator|.
name|isActive
argument_list|()
condition|)
block|{
if|if
condition|(
name|addSuggestion
argument_list|(
name|r
argument_list|,
name|p
operator|.
name|getId
argument_list|()
argument_list|,
name|visibilityControl
argument_list|)
condition|)
block|{
name|queryEmail
operator|.
name|put
argument_list|(
name|e
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|e
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
name|accountLoader
operator|.
name|fill
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|String
argument_list|>
name|p
range|:
name|queryEmail
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|AccountInfo
name|info
init|=
name|r
operator|.
name|get
argument_list|(
name|p
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|email
operator|=
name|p
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|r
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addSuggestion (Map<Account.Id, AccountInfo> map, Account.Id account, VisibilityControl visibilityControl)
specifier|private
name|boolean
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
operator|.
name|Id
name|account
parameter_list|,
name|VisibilityControl
name|visibilityControl
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|map
operator|.
name|containsKey
argument_list|(
name|account
argument_list|)
comment|// Can the suggestion see the change?
operator|&&
name|visibilityControl
operator|.
name|isVisibleTo
argument_list|(
name|account
argument_list|)
comment|// Can the current user see the account?
operator|&&
name|accountControl
operator|.
name|canSee
argument_list|(
name|account
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|account
argument_list|,
name|accountLoader
operator|.
name|get
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|suggestAccountGroup ( SuggestReviewers suggestReviewers, ProjectControl ctl)
specifier|private
name|List
argument_list|<
name|GroupReference
argument_list|>
name|suggestAccountGroup
parameter_list|(
name|SuggestReviewers
name|suggestReviewers
parameter_list|,
name|ProjectControl
name|ctl
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Iterables
operator|.
name|limit
argument_list|(
name|groupBackend
operator|.
name|suggest
argument_list|(
name|suggestReviewers
operator|.
name|getQuery
argument_list|()
argument_list|,
name|ctl
argument_list|)
argument_list|,
name|suggestReviewers
operator|.
name|getLimit
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|suggestGroupAsReviewer (SuggestReviewers suggestReviewers, Project project, GroupReference group, VisibilityControl visibilityControl)
specifier|private
name|boolean
name|suggestGroupAsReviewer
parameter_list|(
name|SuggestReviewers
name|suggestReviewers
parameter_list|,
name|Project
name|project
parameter_list|,
name|GroupReference
name|group
parameter_list|,
name|VisibilityControl
name|visibilityControl
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|int
name|maxAllowed
init|=
name|suggestReviewers
operator|.
name|getMaxAllowed
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|PostReviewers
operator|.
name|isLegalReviewerGroup
argument_list|(
name|group
operator|.
name|getUUID
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|Set
argument_list|<
name|Account
argument_list|>
name|members
init|=
name|groupMembersFactory
operator|.
name|create
argument_list|(
name|currentUser
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|listAccounts
argument_list|(
name|group
operator|.
name|getUUID
argument_list|()
argument_list|,
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|members
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|maxAllowed
operator|>
literal|0
operator|&&
name|members
operator|.
name|size
argument_list|()
operator|>
name|maxAllowed
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// require that at least one member in the group can see the change
for|for
control|(
name|Account
name|account
range|:
name|members
control|)
block|{
if|if
condition|(
name|visibilityControl
operator|.
name|isVisibleTo
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

