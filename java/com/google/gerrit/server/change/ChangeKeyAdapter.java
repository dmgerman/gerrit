begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
operator|.
name|Key
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
name|JsonObject
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

begin_comment
comment|/**  * Adapter that serializes {@link Change.Key}'s {@code key} field as {@code id}, for backwards  * compatibility in stream-events.  */
end_comment

begin_comment
comment|// TODO(dborowitz): auto-value-gson should support this directly using @SerializedName on the
end_comment

begin_comment
comment|// AutoValue method.
end_comment

begin_class
DECL|class|ChangeKeyAdapter
specifier|public
class|class
name|ChangeKeyAdapter
implements|implements
name|JsonSerializer
argument_list|<
name|Change
operator|.
name|Key
argument_list|>
implements|,
name|JsonDeserializer
argument_list|<
name|Change
operator|.
name|Key
argument_list|>
block|{
annotation|@
name|Override
DECL|method|serialize (Change.Key src, Type typeOfSrc, JsonSerializationContext context)
specifier|public
name|JsonElement
name|serialize
parameter_list|(
name|Change
operator|.
name|Key
name|src
parameter_list|,
name|Type
name|typeOfSrc
parameter_list|,
name|JsonSerializationContext
name|context
parameter_list|)
block|{
name|JsonObject
name|obj
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|obj
operator|.
name|addProperty
argument_list|(
literal|"id"
argument_list|,
name|src
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|obj
return|;
block|}
annotation|@
name|Override
DECL|method|deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context)
specifier|public
name|Key
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
name|JsonElement
name|keyJson
init|=
name|json
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyJson
operator|==
literal|null
operator|||
operator|!
name|keyJson
operator|.
name|isJsonPrimitive
argument_list|()
operator|||
operator|!
name|keyJson
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
name|keyJson
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|keyJson
operator|.
name|getAsJsonPrimitive
argument_list|()
operator|.
name|getAsString
argument_list|()
decl_stmt|;
return|return
name|Change
operator|.
name|key
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
end_class

end_unit

