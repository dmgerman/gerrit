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
DECL|package|com.google.gerrit.metrics
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|logging
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiConsumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * Describes a bucketing field used by a metric.  *  * @param<T> type of field  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|Field
specifier|public
specifier|abstract
class|class
name|Field
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|ignoreMetadata ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|BiConsumer
argument_list|<
name|Metadata
operator|.
name|Builder
argument_list|,
name|T
argument_list|>
name|ignoreMetadata
parameter_list|()
block|{
return|return
parameter_list|(
name|metadataBuilder
parameter_list|,
name|fieldValue
parameter_list|)
lambda|->
block|{}
return|;
block|}
comment|/**    * Break down metrics by boolean true/false.    *    * @param name field name    * @return builder for the boolean field    */
DECL|method|ofBoolean ( String name, BiConsumer<Metadata.Builder, Boolean> metadataMapper)
specifier|public
specifier|static
name|Field
operator|.
name|Builder
argument_list|<
name|Boolean
argument_list|>
name|ofBoolean
parameter_list|(
name|String
name|name
parameter_list|,
name|BiConsumer
argument_list|<
name|Metadata
operator|.
name|Builder
argument_list|,
name|Boolean
argument_list|>
name|metadataMapper
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_Field
operator|.
name|Builder
argument_list|<
name|Boolean
argument_list|>
argument_list|()
operator|.
name|valueType
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
operator|.
name|formatter
argument_list|(
name|Object
operator|::
name|toString
argument_list|)
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|metadataMapper
argument_list|(
name|metadataMapper
argument_list|)
return|;
block|}
comment|/**    * Break down metrics by cases of an enum.    *    * @param enumType type of enum    * @param name field name    * @return builder for the enum field    */
DECL|method|ofEnum ( Class<E> enumType, String name, BiConsumer<Metadata.Builder, String> metadataMapper)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Field
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
name|ofEnum
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|enumType
parameter_list|,
name|String
name|name
parameter_list|,
name|BiConsumer
argument_list|<
name|Metadata
operator|.
name|Builder
argument_list|,
name|String
argument_list|>
name|metadataMapper
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_Field
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|()
operator|.
name|valueType
argument_list|(
name|enumType
argument_list|)
operator|.
name|formatter
argument_list|(
name|Enum
operator|::
name|name
argument_list|)
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|metadataMapper
argument_list|(
parameter_list|(
name|metadataBuilder
parameter_list|,
name|fieldValue
parameter_list|)
lambda|->
name|metadataMapper
operator|.
name|accept
argument_list|(
name|metadataBuilder
argument_list|,
name|fieldValue
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Break down metrics by integer.    *    *<p>Each unique integer will allocate a new submetric.<b>Do not use user content as a field    * value</b> as field values are never reclaimed.    *    * @param name field name    * @return builder for the integer field    */
DECL|method|ofInteger ( String name, BiConsumer<Metadata.Builder, Integer> metadataMapper)
specifier|public
specifier|static
name|Field
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|>
name|ofInteger
parameter_list|(
name|String
name|name
parameter_list|,
name|BiConsumer
argument_list|<
name|Metadata
operator|.
name|Builder
argument_list|,
name|Integer
argument_list|>
name|metadataMapper
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_Field
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|>
argument_list|()
operator|.
name|valueType
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|formatter
argument_list|(
name|Object
operator|::
name|toString
argument_list|)
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|metadataMapper
argument_list|(
name|metadataMapper
argument_list|)
return|;
block|}
comment|/**    * Break down metrics by string.    *    *<p>Each unique string will allocate a new submetric.<b>Do not use user content as a field    * value</b> as field values are never reclaimed.    *    * @param name field name    * @return builder for the string field    */
DECL|method|ofString ( String name, BiConsumer<Metadata.Builder, String> metadataMapper)
specifier|public
specifier|static
name|Field
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|ofString
parameter_list|(
name|String
name|name
parameter_list|,
name|BiConsumer
argument_list|<
name|Metadata
operator|.
name|Builder
argument_list|,
name|String
argument_list|>
name|metadataMapper
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_Field
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
argument_list|()
operator|.
name|valueType
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|formatter
argument_list|(
name|s
lambda|->
name|s
argument_list|)
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|metadataMapper
argument_list|(
name|metadataMapper
argument_list|)
return|;
block|}
comment|/** @return name of this field within the metric. */
DECL|method|name ()
specifier|public
specifier|abstract
name|String
name|name
parameter_list|()
function_decl|;
comment|/** @return type of value used within the field. */
DECL|method|valueType ()
specifier|public
specifier|abstract
name|Class
argument_list|<
name|T
argument_list|>
name|valueType
parameter_list|()
function_decl|;
comment|/** @return mapper that maps a field value to a field in the {@link Metadata} class. */
DECL|method|metadataMapper ()
specifier|public
specifier|abstract
name|BiConsumer
argument_list|<
name|Metadata
operator|.
name|Builder
argument_list|,
name|T
argument_list|>
name|metadataMapper
parameter_list|()
function_decl|;
comment|/** @return description text for the field explaining its range of values. */
DECL|method|description ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|description
parameter_list|()
function_decl|;
comment|/** @return formatter to format field values. */
DECL|method|formatter ()
specifier|public
specifier|abstract
name|Function
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|formatter
parameter_list|()
function_decl|;
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|name (String name)
specifier|abstract
name|Builder
argument_list|<
name|T
argument_list|>
name|name
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|valueType (Class<T> type)
specifier|abstract
name|Builder
argument_list|<
name|T
argument_list|>
name|valueType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
DECL|method|formatter (Function<T, String> formatter)
specifier|abstract
name|Builder
argument_list|<
name|T
argument_list|>
name|formatter
parameter_list|(
name|Function
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|formatter
parameter_list|)
function_decl|;
DECL|method|metadataMapper (BiConsumer<Metadata.Builder, T> metadataMapper)
specifier|abstract
name|Builder
argument_list|<
name|T
argument_list|>
name|metadataMapper
parameter_list|(
name|BiConsumer
argument_list|<
name|Metadata
operator|.
name|Builder
argument_list|,
name|T
argument_list|>
name|metadataMapper
parameter_list|)
function_decl|;
DECL|method|description (String description)
specifier|public
specifier|abstract
name|Builder
argument_list|<
name|T
argument_list|>
name|description
parameter_list|(
name|String
name|description
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|Field
argument_list|<
name|T
argument_list|>
name|autoBuild
parameter_list|()
function_decl|;
DECL|method|build ()
specifier|public
name|Field
argument_list|<
name|T
argument_list|>
name|build
parameter_list|()
block|{
name|Field
argument_list|<
name|T
argument_list|>
name|field
init|=
name|autoBuild
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|field
operator|.
name|name
argument_list|()
operator|.
name|matches
argument_list|(
literal|"^[a-z_]+$"
argument_list|)
argument_list|,
literal|"name must match [a-z_]"
argument_list|)
expr_stmt|;
return|return
name|field
return|;
block|}
block|}
block|}
end_class

end_unit

