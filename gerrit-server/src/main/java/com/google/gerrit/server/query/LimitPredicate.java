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
DECL|package|com.google.gerrit.server.query
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
name|index
operator|.
name|query
operator|.
name|QueryParseException
import|;
end_import

begin_class
DECL|class|LimitPredicate
specifier|public
class|class
name|LimitPredicate
parameter_list|<
name|T
parameter_list|>
extends|extends
name|IntPredicate
argument_list|<
name|T
argument_list|>
implements|implements
name|Matchable
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getLimit (String fieldName, Predicate<?> p)
specifier|public
specifier|static
name|Integer
name|getLimit
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|Predicate
argument_list|<
name|?
argument_list|>
name|p
parameter_list|)
block|{
name|IntPredicate
argument_list|<
name|?
argument_list|>
name|ip
init|=
name|QueryBuilder
operator|.
name|find
argument_list|(
name|p
argument_list|,
name|IntPredicate
operator|.
name|class
argument_list|,
name|fieldName
argument_list|)
decl_stmt|;
return|return
name|ip
operator|!=
literal|null
condition|?
name|ip
operator|.
name|intValue
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|LimitPredicate (String fieldName, int limit)
specifier|public
name|LimitPredicate
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|super
argument_list|(
name|fieldName
argument_list|,
name|limit
argument_list|)
expr_stmt|;
if|if
condition|(
name|limit
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"limit must be positive: "
operator|+
name|limit
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|match (T object)
specifier|public
name|boolean
name|match
parameter_list|(
name|T
name|object
parameter_list|)
block|{
return|return
literal|true
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
literal|0
return|;
block|}
block|}
end_class

end_unit

