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
name|Type
import|;
end_import

begin_comment
comment|/**  * JSON deserializer for {@link Event}s.  *  *<p>Deserialized objects are of an appropriate subclass based on the value of the top-level "type"  * element.  */
end_comment

begin_class
DECL|class|EventDeserializer
specifier|public
class|class
name|EventDeserializer
implements|implements
name|JsonDeserializer
argument_list|<
name|Event
argument_list|>
block|{
annotation|@
name|Override
DECL|method|deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context)
specifier|public
name|Event
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
name|isJsonObject
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|JsonParseException
argument_list|(
literal|"Not an object"
argument_list|)
throw|;
block|}
name|JsonElement
name|typeJson
init|=
name|json
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeJson
operator|==
literal|null
operator|||
operator|!
name|typeJson
operator|.
name|isJsonPrimitive
argument_list|()
operator|||
operator|!
name|typeJson
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
literal|"Type is not a string: "
operator|+
name|typeJson
argument_list|)
throw|;
block|}
name|String
name|type
init|=
name|typeJson
operator|.
name|getAsJsonPrimitive
argument_list|()
operator|.
name|getAsString
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|EventTypes
operator|.
name|getClass
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JsonParseException
argument_list|(
literal|"Unknown event type: "
operator|+
name|type
argument_list|)
throw|;
block|}
return|return
name|context
operator|.
name|deserialize
argument_list|(
name|json
argument_list|,
name|cls
argument_list|)
return|;
block|}
block|}
end_class

end_unit

