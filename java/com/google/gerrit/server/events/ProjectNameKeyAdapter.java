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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
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
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonPrimitive
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
name|JsonSerializationContext
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
name|JsonSerializer
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
DECL|class|ProjectNameKeyAdapter
specifier|public
class|class
name|ProjectNameKeyAdapter
implements|implements
name|JsonSerializer
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
implements|,
name|JsonDeserializer
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
block|{
annotation|@
name|Override
DECL|method|serialize ( Project.NameKey project, Type typeOfSrc, JsonSerializationContext context)
specifier|public
name|JsonElement
name|serialize
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Type
name|typeOfSrc
parameter_list|,
name|JsonSerializationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|JsonPrimitive
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|deserialize ( JsonElement json, Type typeOfT, JsonDeserializationContext context)
specifier|public
name|Project
operator|.
name|NameKey
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
if|if
condition|(
operator|!
name|json
operator|.
name|isJsonPrimitive
argument_list|()
operator|||
operator|!
name|json
operator|.
name|getAsJsonPrimitive
argument_list|()
operator|.
name|isString
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|JsonParseException
argument_list|(
literal|"Key is not a string: "
operator|+
name|json
argument_list|)
throw|;
block|}
return|return
name|Project
operator|.
name|nameKey
argument_list|(
name|json
operator|.
name|getAsString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

