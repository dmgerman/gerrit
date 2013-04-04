begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|regexp
operator|.
name|shared
operator|.
name|RegExp
import|;
end_import

begin_comment
comment|/** A Find/Replace pair used against the {@link SafeHtml} block of text. */
end_comment

begin_interface
DECL|interface|FindReplace
specifier|public
interface|interface
name|FindReplace
block|{
comment|/**    * @return regular expression to match substrings with; should be treated as    *     immutable.    */
DECL|method|pattern ()
specifier|public
name|RegExp
name|pattern
parameter_list|()
function_decl|;
comment|/**    * Find and replace a single instance of this pattern in an input.    *<p>    *<b>WARNING:</b> No XSS sanitization is done on the return value of this    * method, e.g. this value may be passed directly to    * {@link SafeHtml#replaceAll(String, String)}. Implementations must sanitize output    * appropriately.    *    * @param input input string.    * @return result of regular expression replacement.    * @throws IllegalArgumentException if the input could not be safely sanitized.    */
DECL|method|replace (String input)
specifier|public
name|String
name|replace
parameter_list|(
name|String
name|input
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

