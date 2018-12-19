begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|FieldNamingPolicy
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
name|Gson
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
name|GsonBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|SqlTimestampDeserializer
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

begin_comment
comment|/** Standard output format used by an API call. */
end_comment

begin_enum
DECL|enum|OutputFormat
specifier|public
enum|enum
name|OutputFormat
block|{
comment|/**    * The output is a human readable text format. It may also be regular enough to be machine    * readable. Whether or not the text format is machine readable and will be committed to as a long    * term format that tools can build upon is specific to each API call.    */
DECL|enumConstant|TEXT
name|TEXT
block|,
comment|/**    * Pretty-printed JSON format. This format uses whitespace to make the output readable by a human,    * but is also machine readable with a JSON library. The structure of the output is a long term    * format that tools can rely upon.    */
DECL|enumConstant|JSON
name|JSON
block|,
comment|/**    * Same as {@link #JSON}, but with unnecessary whitespace removed to save generation time and copy    * costs. Typically JSON_COMPACT format is used by a browser based HTML client running over the    * network.    */
DECL|enumConstant|JSON_COMPACT
name|JSON_COMPACT
block|;
comment|/** @return true when the format is either JSON or JSON_COMPACT. */
DECL|method|isJson ()
specifier|public
name|boolean
name|isJson
parameter_list|()
block|{
return|return
name|this
operator|==
name|JSON_COMPACT
operator|||
name|this
operator|==
name|JSON
return|;
block|}
comment|/** @return a new Gson instance configured according to the format. */
DECL|method|newGsonBuilder ()
specifier|public
name|GsonBuilder
name|newGsonBuilder
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isJson
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s is not JSON"
argument_list|,
name|this
argument_list|)
argument_list|)
throw|;
block|}
name|GsonBuilder
name|gb
init|=
operator|new
name|GsonBuilder
argument_list|()
operator|.
name|setFieldNamingPolicy
argument_list|(
name|FieldNamingPolicy
operator|.
name|LOWER_CASE_WITH_UNDERSCORES
argument_list|)
operator|.
name|registerTypeAdapter
argument_list|(
name|Timestamp
operator|.
name|class
argument_list|,
operator|new
name|SqlTimestampDeserializer
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|this
operator|==
name|OutputFormat
operator|.
name|JSON
condition|)
block|{
name|gb
operator|.
name|setPrettyPrinting
argument_list|()
expr_stmt|;
block|}
return|return
name|gb
return|;
block|}
comment|/** @return a new Gson instance configured according to the format. */
DECL|method|newGson ()
specifier|public
name|Gson
name|newGson
parameter_list|()
block|{
return|return
name|newGsonBuilder
argument_list|()
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_enum

end_unit

