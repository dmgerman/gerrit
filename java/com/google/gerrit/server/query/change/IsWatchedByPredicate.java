begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
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
name|change
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
name|gerrit
operator|.
name|index
operator|.
name|query
operator|.
name|AndPredicate
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|index
operator|.
name|query
operator|.
name|QueryBuilder
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
name|index
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
name|WatchConfig
operator|.
name|ProjectWatchKey
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
name|List
import|;
end_import

begin_class
DECL|class|IsWatchedByPredicate
specifier|public
class|class
name|IsWatchedByPredicate
extends|extends
name|AndPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|method|describe (CurrentUser user)
specifier|protected
specifier|static
name|String
name|describe
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
if|if
condition|(
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
return|return
name|user
operator|.
name|getAccountId
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|user
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|field|user
specifier|protected
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|method|IsWatchedByPredicate (ChangeQueryBuilder.Arguments args, boolean checkIsVisible)
specifier|public
name|IsWatchedByPredicate
parameter_list|(
name|ChangeQueryBuilder
operator|.
name|Arguments
name|args
parameter_list|,
name|boolean
name|checkIsVisible
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|super
argument_list|(
name|filters
argument_list|(
name|args
argument_list|,
name|checkIsVisible
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|args
operator|.
name|getUser
argument_list|()
expr_stmt|;
block|}
DECL|method|filters ( ChangeQueryBuilder.Arguments args, boolean checkIsVisible)
specifier|protected
specifier|static
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|filters
parameter_list|(
name|ChangeQueryBuilder
operator|.
name|Arguments
name|args
parameter_list|,
name|boolean
name|checkIsVisible
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ChangeQueryBuilder
name|builder
init|=
operator|new
name|ChangeQueryBuilder
argument_list|(
name|args
argument_list|)
decl_stmt|;
for|for
control|(
name|ProjectWatchKey
name|w
range|:
name|getWatches
argument_list|(
name|args
argument_list|)
control|)
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|f
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|w
operator|.
name|filter
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|f
operator|=
name|builder
operator|.
name|parse
argument_list|(
name|w
operator|.
name|filter
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|QueryBuilder
operator|.
name|find
argument_list|(
name|f
argument_list|,
name|IsWatchedByPredicate
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// If the query is going to infinite loop, assume it
comment|// will never match and return null. Yes this test
comment|// prevents you from having a filter that matches what
comment|// another user is filtering on. :-)
continue|continue;
block|}
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
continue|continue;
block|}
block|}
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
decl_stmt|;
if|if
condition|(
name|w
operator|.
name|project
argument_list|()
operator|.
name|equals
argument_list|(
name|args
operator|.
name|allProjectsName
argument_list|)
condition|)
block|{
name|p
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|p
operator|=
name|builder
operator|.
name|project
argument_list|(
name|w
operator|.
name|project
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|!=
literal|null
operator|&&
name|f
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|and
argument_list|(
name|p
argument_list|,
name|f
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|add
argument_list|(
name|builder
operator|.
name|status_open
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|r
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|none
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|checkIsVisible
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|or
argument_list|(
name|r
argument_list|)
argument_list|,
name|builder
operator|.
name|is_visible
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|or
argument_list|(
name|r
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|getWatches (ChangeQueryBuilder.Arguments args)
specifier|protected
specifier|static
name|Collection
argument_list|<
name|ProjectWatchKey
argument_list|>
name|getWatches
parameter_list|(
name|ChangeQueryBuilder
operator|.
name|Arguments
name|args
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|CurrentUser
name|user
init|=
name|args
operator|.
name|getUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
return|return
name|args
operator|.
name|accountCache
operator|.
name|get
argument_list|(
name|args
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|getProjectWatches
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
expr|<
name|ProjectWatchKey
operator|>
name|emptySet
argument_list|()
return|;
block|}
DECL|method|none ()
specifier|protected
specifier|static
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|none
parameter_list|()
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|any
init|=
name|any
argument_list|()
decl_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|not
argument_list|(
name|any
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|val
init|=
name|describe
argument_list|(
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return
name|ChangeQueryBuilder
operator|.
name|FIELD_WATCHEDBY
operator|+
literal|":"
operator|+
name|val
return|;
block|}
return|return
name|ChangeQueryBuilder
operator|.
name|FIELD_WATCHEDBY
operator|+
literal|":\""
operator|+
name|val
operator|+
literal|"\""
return|;
block|}
block|}
end_class

end_unit
