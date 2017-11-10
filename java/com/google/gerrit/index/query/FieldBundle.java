begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|ImmutableListMultimap
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
name|ListMultimap
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

begin_comment
comment|/** FieldBundle is an abstraction that allows retrieval of raw values from different sources. */
end_comment

begin_class
DECL|class|FieldBundle
specifier|public
class|class
name|FieldBundle
block|{
comment|// Map String => List{Integer, Long, Timestamp, String, byte[]}
DECL|field|fields
specifier|private
name|ImmutableListMultimap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fields
decl_stmt|;
DECL|method|FieldBundle (ListMultimap<String, Object> fields)
specifier|public
name|FieldBundle
parameter_list|(
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fields
operator|=
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|fields
argument_list|)
expr_stmt|;
block|}
comment|/**    * Get a field's value based on the field definition.    *    * @param fieldDef the definition of the field of which the value should be retrieved. The field    *     must be stored and contained in the result set as specified by {@link    *     com.google.gerrit.index.QueryOptions}.    * @param<T> Data type of the returned object based on the field definition    * @return Either a single element or an Iterable based on the field definition. An empty list is    *     returned for repeated fields that are not contained in the result.    * @throws IllegalArgumentException if the requested field is not stored or not present. This    *     check is only enforced on non-repeatable fields.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getValue (FieldDef<?, T> fieldDef)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getValue
parameter_list|(
name|FieldDef
argument_list|<
name|?
argument_list|,
name|T
argument_list|>
name|fieldDef
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|fieldDef
operator|.
name|isStored
argument_list|()
argument_list|,
literal|"Field must be stored"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|fields
operator|.
name|containsKey
argument_list|(
name|fieldDef
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|fieldDef
operator|.
name|isRepeatable
argument_list|()
argument_list|,
literal|"Field %s is not in result set %s"
argument_list|,
name|fieldDef
operator|.
name|getName
argument_list|()
argument_list|,
name|fields
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|Iterable
argument_list|<
name|Object
argument_list|>
name|result
init|=
name|fields
operator|.
name|get
argument_list|(
name|fieldDef
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|fieldDef
operator|.
name|isRepeatable
argument_list|()
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|result
return|;
block|}
else|else
block|{
return|return
operator|(
name|T
operator|)
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

