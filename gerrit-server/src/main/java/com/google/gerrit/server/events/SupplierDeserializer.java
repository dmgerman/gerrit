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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
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
name|base
operator|.
name|Supplier
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
name|Suppliers
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonDeserializationContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonDeserializer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonElement
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonParseException
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
name|ParameterizedType
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
name|Type
import|;
end_import

begin_class
DECL|class|SupplierDeserializer
specifier|public
class|class
name|SupplierDeserializer
implements|implements
name|JsonDeserializer
argument_list|<
name|Supplier
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context)
specifier|public
name|Supplier
argument_list|<
name|?
argument_list|>
name|deserialize
parameter_list|(
name|JsonElement
name|json
parameter_list|,
name|Type
name|typeOfT
parameter_list|,
name|JsonDeserializationContext
name|context
parameter_list|)
throws|throws
name|JsonParseException
block|{
name|checkArgument
argument_list|(
name|typeOfT
operator|instanceof
name|ParameterizedType
argument_list|)
expr_stmt|;
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
name|typeOfT
decl_stmt|;
if|if
condition|(
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|JsonParseException
argument_list|(
literal|"Expected one parameter type in Supplier interface."
argument_list|)
throw|;
block|}
name|Type
name|supplierOf
init|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
return|return
name|Suppliers
operator|.
name|ofInstance
argument_list|(
name|context
operator|.
name|deserialize
argument_list|(
name|json
argument_list|,
name|supplierOf
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

