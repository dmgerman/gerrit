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
DECL|package|com.google.gwtorm.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
package|;
end_package

begin_comment
comment|/** Common utility functions for {@link Key} implementors. */
end_comment

begin_class
DECL|class|KeyUtil
specifier|public
class|class
name|KeyUtil
block|{
DECL|field|ENCODER_IMPL
specifier|private
specifier|static
name|Encoder
name|ENCODER_IMPL
init|=
operator|new
name|StandardKeyEncoder
argument_list|()
decl_stmt|;
comment|/**    * Set the encoder implementation to a valid implementation.    *    *<p>Server-side code needs to set the encoder to a {@link    * com.google.gwtorm.server.StandardKeyEncoder} instance prior to invoking any methods in this    * class. Typically this is done by the {@link com.google.gwtorm.server.SchemaFactory}    * implementation's static initializer.    */
DECL|method|setEncoderImpl (final Encoder e)
specifier|public
specifier|static
name|void
name|setEncoderImpl
parameter_list|(
specifier|final
name|Encoder
name|e
parameter_list|)
block|{
name|ENCODER_IMPL
operator|=
name|e
expr_stmt|;
block|}
comment|/**    * Determine if two keys are equal, supporting null references.    *    * @param<T> type of the key entity.    * @param a first key to test; may be null.    * @param b second key to test; may be null.    * @return true if both<code>a</code> and<code>b</code> are null, or if both are not-null and    *<code>a.equals(b)</code> is true. Otherwise false.    */
DECL|method|eq (final T a, final T b)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Key
argument_list|<
name|?
argument_list|>
parameter_list|>
name|boolean
name|eq
parameter_list|(
specifier|final
name|T
name|a
parameter_list|,
specifier|final
name|T
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
name|b
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|a
operator|==
literal|null
operator|||
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
comment|/**    * Encode a string to be safe for use within a URL like string.    *    *<p>The returned encoded string has URL component characters escaped with hex escapes (e.g. ' '    * is '+' and '%' is '%25'). The special character '/' is left literal. The comma character (',')    * is always encoded, permitting multiple encoded string values to be joined together safely.    *    * @param e the string to encode, must not be null.    * @return the encoded string.    */
DECL|method|encode (final String e)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
specifier|final
name|String
name|e
parameter_list|)
block|{
return|return
name|ENCODER_IMPL
operator|.
name|encode
argument_list|(
name|e
argument_list|)
return|;
block|}
comment|/**    * Decode a string previously encoded by {@link #encode(String)}.    *    * @param e the string to decode, must not be null.    * @return the decoded string.    */
DECL|method|decode (final String e)
specifier|public
specifier|static
name|String
name|decode
parameter_list|(
specifier|final
name|String
name|e
parameter_list|)
block|{
return|return
name|ENCODER_IMPL
operator|.
name|decode
argument_list|(
name|e
argument_list|)
return|;
block|}
comment|/**    * Split a string along the last comma and parse into the parent.    *    * @param parent parent key;<code>parent.fromString(in[0..comma])</code>.    * @param in the input string.    * @return text (if any) after the last comma in the input.    */
DECL|method|parseFromString (final Key<?> parent, final String in)
specifier|public
specifier|static
name|String
name|parseFromString
parameter_list|(
specifier|final
name|Key
argument_list|<
name|?
argument_list|>
name|parent
parameter_list|,
specifier|final
name|String
name|in
parameter_list|)
block|{
specifier|final
name|int
name|comma
init|=
name|in
operator|.
name|lastIndexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
if|if
condition|(
name|comma
operator|<
literal|0
operator|&&
name|parent
operator|==
literal|null
condition|)
block|{
return|return
name|decode
argument_list|(
name|in
argument_list|)
return|;
block|}
if|if
condition|(
name|comma
operator|<
literal|0
operator|&&
name|parent
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not enough components: "
operator|+
name|in
argument_list|)
throw|;
block|}
assert|assert
operator|(
name|parent
operator|!=
literal|null
operator|)
assert|;
name|parent
operator|.
name|fromString
argument_list|(
name|in
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|comma
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|decode
argument_list|(
name|in
operator|.
name|substring
argument_list|(
name|comma
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
DECL|class|Encoder
specifier|public
specifier|abstract
specifier|static
class|class
name|Encoder
block|{
DECL|method|encode (String e)
specifier|public
specifier|abstract
name|String
name|encode
parameter_list|(
name|String
name|e
parameter_list|)
function_decl|;
DECL|method|decode (String e)
specifier|public
specifier|abstract
name|String
name|decode
parameter_list|(
name|String
name|e
parameter_list|)
function_decl|;
block|}
DECL|method|KeyUtil ()
specifier|private
name|KeyUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

