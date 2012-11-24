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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_comment
comment|/** URL related utility functions. */
end_comment

begin_class
DECL|class|Url
specifier|public
specifier|final
class|class
name|Url
block|{
comment|/**    * Encode a path segment, escaping characters not valid for a URL.    *<p>    * The following characters are not escaped:    *<ul>    *<li>{@code a..z, A..Z, 0..9}    *<li>{@code . - * _}    *</ul>    *<p>    * ' ' (space) is encoded as '+'.    *<p>    * All other characters (including '/') are converted to the triplet "%xy"    * where "xy" is the hex representation of the character in UTF-8.    *    * @param component a string containing text to encode.    * @return a string with all invalid URL characters escaped.    */
DECL|method|encode (String component)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|component
parameter_list|)
block|{
try|try
block|{
return|return
name|URLEncoder
operator|.
name|encode
argument_list|(
name|component
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"JVM must support UTF-8"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/** Decode a URL encoded string, e.g. from {@code "%2F"} to {@code "/"}. */
DECL|method|decode (String str)
specifier|public
specifier|static
name|String
name|decode
parameter_list|(
name|String
name|str
parameter_list|)
block|{
try|try
block|{
return|return
name|URLDecoder
operator|.
name|decode
argument_list|(
name|str
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"JVM must support UTF-8"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|Url ()
specifier|private
name|Url
parameter_list|()
block|{   }
block|}
end_class

end_unit

