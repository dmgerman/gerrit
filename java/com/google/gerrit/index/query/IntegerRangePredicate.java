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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
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
name|exceptions
operator|.
name|StorageException
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
name|FieldDef
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
name|RangeUtil
operator|.
name|Range
import|;
end_import

begin_class
DECL|class|IntegerRangePredicate
specifier|public
specifier|abstract
class|class
name|IntegerRangePredicate
parameter_list|<
name|T
parameter_list|>
extends|extends
name|IndexPredicate
argument_list|<
name|T
argument_list|>
block|{
DECL|field|range
specifier|private
specifier|final
name|Range
name|range
decl_stmt|;
DECL|method|IntegerRangePredicate (FieldDef<T, Integer> type, String value)
specifier|protected
name|IntegerRangePredicate
parameter_list|(
name|FieldDef
argument_list|<
name|T
argument_list|,
name|Integer
argument_list|>
name|type
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|super
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|range
operator|=
name|RangeUtil
operator|.
name|getRange
argument_list|(
name|value
argument_list|,
name|Integer
operator|.
name|MIN_VALUE
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"Invalid range predicate: "
operator|+
name|value
argument_list|)
throw|;
block|}
block|}
DECL|method|getValueInt (T object)
specifier|protected
specifier|abstract
name|Integer
name|getValueInt
parameter_list|(
name|T
name|object
parameter_list|)
throws|throws
name|StorageException
function_decl|;
DECL|method|match (T object)
specifier|public
name|boolean
name|match
parameter_list|(
name|T
name|object
parameter_list|)
throws|throws
name|StorageException
block|{
name|Integer
name|valueInt
init|=
name|getValueInt
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueInt
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|valueInt
operator|>=
name|range
operator|.
name|min
operator|&&
name|valueInt
operator|<=
name|range
operator|.
name|max
return|;
block|}
comment|/** Return the minimum value of this predicate's range, inclusive. */
DECL|method|getMinimumValue ()
specifier|public
name|int
name|getMinimumValue
parameter_list|()
block|{
return|return
name|range
operator|.
name|min
return|;
block|}
comment|/** Return the maximum value of this predicate's range, inclusive. */
DECL|method|getMaximumValue ()
specifier|public
name|int
name|getMaximumValue
parameter_list|()
block|{
return|return
name|range
operator|.
name|max
return|;
block|}
block|}
end_class

end_unit

