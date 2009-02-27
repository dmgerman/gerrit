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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_comment
comment|/** Utilities for dealing with the DOM. */
end_comment

begin_class
DECL|class|DomUtil
specifier|public
specifier|abstract
class|class
name|DomUtil
block|{
comment|/** Escape XML/HTML special characters in the input string. */
DECL|method|escape (final String in)
specifier|public
specifier|static
name|String
name|escape
parameter_list|(
specifier|final
name|String
name|in
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|in
argument_list|)
operator|.
name|asString
argument_list|()
return|;
block|}
comment|/** Convert bare URLs into&lt;a href&gt; tags. */
DECL|method|linkify (final String in)
specifier|public
specifier|static
name|String
name|linkify
parameter_list|(
specifier|final
name|String
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|replaceAll
argument_list|(
literal|"(https?://[^ \n\r\t]*)"
argument_list|,
literal|"<a href=\"$1\">$1</a>"
argument_list|)
return|;
block|}
comment|/** Do wiki style formatting to make it pretty */
DECL|method|wikify (String in)
specifier|public
specifier|static
name|String
name|wikify
parameter_list|(
name|String
name|in
parameter_list|)
block|{
name|in
operator|=
name|escape
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|in
operator|=
name|linkify
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|in
operator|=
name|in
operator|.
name|replaceAll
argument_list|(
literal|"(^|\n)([ \t][^\n]*)"
argument_list|,
literal|"$1<span class=\"gerrit-preformat\">$2</span><br />"
argument_list|)
expr_stmt|;
name|in
operator|=
name|in
operator|.
name|replaceAll
argument_list|(
literal|"\n\n"
argument_list|,
literal|"<p>\n"
argument_list|)
expr_stmt|;
return|return
name|in
return|;
block|}
DECL|method|DomUtil ()
specifier|private
name|DomUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

