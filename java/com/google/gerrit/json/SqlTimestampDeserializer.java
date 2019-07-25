begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.json
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|json
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
name|JsonNull
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

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_class
DECL|class|SqlTimestampDeserializer
class|class
name|SqlTimestampDeserializer
implements|implements
name|JsonDeserializer
argument_list|<
name|Timestamp
argument_list|>
implements|,
name|JsonSerializer
argument_list|<
name|Timestamp
argument_list|>
block|{
DECL|field|UTC
specifier|private
specifier|static
specifier|final
name|TimeZone
name|UTC
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context)
specifier|public
name|Timestamp
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
name|json
operator|.
name|isJsonNull
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|json
operator|.
name|isJsonPrimitive
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|JsonParseException
argument_list|(
literal|"Expected string for timestamp type"
argument_list|)
throw|;
block|}
name|JsonPrimitive
name|p
init|=
operator|(
name|JsonPrimitive
operator|)
name|json
decl_stmt|;
if|if
condition|(
operator|!
name|p
operator|.
name|isString
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|JsonParseException
argument_list|(
literal|"Expected string for timestamp type"
argument_list|)
throw|;
block|}
name|String
name|input
init|=
name|p
operator|.
name|getAsString
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Magic timestamp to indicate no timestamp. (-> null object)
comment|// Always create a new object as timestamps are mutable. Don't use TimeUtil.never() to not
comment|// introduce an undesired dependency.
return|return
operator|new
name|Timestamp
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
name|JavaSqlTimestampHelper
operator|.
name|parseTimestamp
argument_list|(
name|input
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|serialize (Timestamp src, Type typeOfSrc, JsonSerializationContext context)
specifier|public
name|JsonElement
name|serialize
parameter_list|(
name|Timestamp
name|src
parameter_list|,
name|Type
name|typeOfSrc
parameter_list|,
name|JsonSerializationContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
return|return
name|JsonNull
operator|.
name|INSTANCE
return|;
block|}
return|return
operator|new
name|JsonPrimitive
argument_list|(
name|newFormat
argument_list|()
operator|.
name|format
argument_list|(
name|src
argument_list|)
operator|+
literal|"000000"
argument_list|)
return|;
block|}
DECL|method|newFormat ()
specifier|private
specifier|static
name|SimpleDateFormat
name|newFormat
parameter_list|()
block|{
name|SimpleDateFormat
name|f
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss.SSS"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setTimeZone
argument_list|(
name|UTC
argument_list|)
expr_stmt|;
name|f
operator|.
name|setLenient
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|f
return|;
block|}
block|}
end_class

end_unit

