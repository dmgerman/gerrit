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
import|import static
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
name|change
operator|.
name|ChangeField
operator|.
name|FUZZY_TOPIC
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|change
operator|.
name|ChangeIndex
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
name|change
operator|.
name|IndexedChangeQuery
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

begin_class
DECL|class|FuzzyTopicPredicate
specifier|public
class|class
name|FuzzyTopicPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|field|index
specifier|protected
specifier|final
name|ChangeIndex
name|index
decl_stmt|;
DECL|method|FuzzyTopicPredicate (String topic, ChangeIndex index)
specifier|public
name|FuzzyTopicPredicate
parameter_list|(
name|String
name|topic
parameter_list|,
name|ChangeIndex
name|index
parameter_list|)
block|{
name|super
argument_list|(
name|FUZZY_TOPIC
argument_list|,
name|topic
argument_list|)
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
name|Change
name|change
init|=
name|cd
operator|.
name|change
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|t
init|=
name|change
operator|.
name|getTopic
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|thisId
init|=
operator|new
name|LegacyChangeIdPredicate
argument_list|(
name|cd
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|results
init|=
name|index
operator|.
name|getSource
argument_list|(
name|and
argument_list|(
name|thisId
argument_list|,
name|this
argument_list|)
argument_list|,
name|IndexedChangeQuery
operator|.
name|oneResult
argument_list|()
argument_list|)
operator|.
name|read
argument_list|()
decl_stmt|;
return|return
operator|!
name|Iterables
operator|.
name|isEmpty
argument_list|(
name|results
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit
