begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc.
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
name|user
operator|.
name|client
operator|.
name|DOM
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|HTML
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|HTMLTable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|HasHTML
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|InlineHTML
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Widget
import|;
end_import

begin_comment
comment|/** Immutable string safely placed as HTML without further escaping. */
end_comment

begin_class
DECL|class|SafeHtml
specifier|public
specifier|abstract
class|class
name|SafeHtml
block|{
comment|/** Set the HTML property of a widget. */
DECL|method|set (final T e, final SafeHtml str)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|HasHTML
parameter_list|>
name|T
name|set
parameter_list|(
specifier|final
name|T
name|e
parameter_list|,
specifier|final
name|SafeHtml
name|str
parameter_list|)
block|{
name|e
operator|.
name|setHTML
argument_list|(
name|str
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|e
return|;
block|}
comment|/** Set the inner HTML of any element. */
DECL|method|set (final Element e, final SafeHtml str)
specifier|public
specifier|static
name|Element
name|set
parameter_list|(
specifier|final
name|Element
name|e
parameter_list|,
specifier|final
name|SafeHtml
name|str
parameter_list|)
block|{
name|DOM
operator|.
name|setInnerHTML
argument_list|(
name|e
argument_list|,
name|str
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|e
return|;
block|}
comment|/** Set the inner HTML of a table cell. */
DECL|method|set (final T t, final int row, final int col, final SafeHtml str)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|HTMLTable
parameter_list|>
name|T
name|set
parameter_list|(
specifier|final
name|T
name|t
parameter_list|,
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|int
name|col
parameter_list|,
specifier|final
name|SafeHtml
name|str
parameter_list|)
block|{
name|t
operator|.
name|setHTML
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|str
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|t
return|;
block|}
comment|/** Parse an HTML block and return the first (typically root) element. */
DECL|method|parse (final SafeHtml str)
specifier|public
specifier|static
name|Element
name|parse
parameter_list|(
specifier|final
name|SafeHtml
name|str
parameter_list|)
block|{
return|return
name|DOM
operator|.
name|getFirstChild
argument_list|(
name|set
argument_list|(
name|DOM
operator|.
name|createDiv
argument_list|()
argument_list|,
name|str
argument_list|)
argument_list|)
return|;
block|}
comment|/** Convert bare http:// and https:// URLs into&lt;a href&gt; tags. */
DECL|method|linkify ()
specifier|public
name|SafeHtml
name|linkify
parameter_list|()
block|{
return|return
name|replaceAll
argument_list|(
literal|"(https?://[^ \n\r\t]*)"
argument_list|,
literal|"<a href=\"$1\">$1</a>"
argument_list|)
return|;
block|}
comment|/**    * Apply {@link #linkify()}, and "\n\n" to&lt;p&gt;.    *<p>    * Lines that start with whitespace are assumed to be preformatted, and are    * formatted by the<code>gwtexpui-SafeHtml-WikiPreFormat</code> CSS class. By    * default this class is:    *     *<pre>    *   white-space: pre;    *   font-family: monospace;    *</pre>    */
DECL|method|wikify ()
specifier|public
name|SafeHtml
name|wikify
parameter_list|()
block|{
name|SafeHtml
name|s
init|=
name|linkify
argument_list|()
decl_stmt|;
name|s
operator|=
name|s
operator|.
name|replaceAll
argument_list|(
literal|"(^|\n)([ \t][^\n]*)"
argument_list|,
literal|"$1<span class=\"gwtexpui-SafeHtml-WikiPreFormat\">$2</span><br />"
argument_list|)
expr_stmt|;
name|s
operator|=
name|s
operator|.
name|replaceAll
argument_list|(
literal|"\n\n"
argument_list|,
literal|"\n<p />\n"
argument_list|)
expr_stmt|;
return|return
name|s
return|;
block|}
comment|/**    * Replace first occurrence of<code>regex</code> with<code>repl</code> .    *<p>    *<b>WARNING:</b> This replacement is being performed against an otherwise    * safe HTML string. The caller must ensure that the replacement does not    * introduce cross-site scripting attack entry points.    *     * @param regex regular expression pattern to match the substring with.    * @param repl replacement expression. Capture groups within    *<code>regex</code> can be referenced with<code>$<i>n</i></code>.    * @return a new string, after the replacement has been made.    */
DECL|method|replaceFirst (final String regex, final String repl)
specifier|public
name|SafeHtml
name|replaceFirst
parameter_list|(
specifier|final
name|String
name|regex
parameter_list|,
specifier|final
name|String
name|repl
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlString
argument_list|(
name|asString
argument_list|()
operator|.
name|replaceFirst
argument_list|(
name|regex
argument_list|,
name|repl
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Replace each occurrence of<code>regex</code> with<code>repl</code> .    *<p>    *<b>WARNING:</b> This replacement is being performed against an otherwise    * safe HTML string. The caller must ensure that the replacement does not    * introduce cross-site scripting attack entry points.    *     * @param regex regular expression pattern to match substrings with.    * @param repl replacement expression. Capture groups within    *<code>regex</code> can be referenced with<code>$<i>n</i></code>.    * @return a new string, after the replacements have been made.    */
DECL|method|replaceAll (final String regex, final String repl)
specifier|public
name|SafeHtml
name|replaceAll
parameter_list|(
specifier|final
name|String
name|regex
parameter_list|,
specifier|final
name|String
name|repl
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlString
argument_list|(
name|asString
argument_list|()
operator|.
name|replaceAll
argument_list|(
name|regex
argument_list|,
name|repl
argument_list|)
argument_list|)
return|;
block|}
comment|/** @return a GWT block display widget displaying this HTML. */
DECL|method|toBlockWidget ()
specifier|public
name|Widget
name|toBlockWidget
parameter_list|()
block|{
return|return
operator|new
name|HTML
argument_list|(
name|asString
argument_list|()
argument_list|)
return|;
block|}
comment|/** @return a GWT inline display widget displaying this HTML. */
DECL|method|toInlineWidget ()
specifier|public
name|Widget
name|toInlineWidget
parameter_list|()
block|{
return|return
operator|new
name|InlineHTML
argument_list|(
name|asString
argument_list|()
argument_list|)
return|;
block|}
comment|/** @return a clean HTML string safe for inclusion in any context. */
DECL|method|asString ()
specifier|public
specifier|abstract
name|String
name|asString
parameter_list|()
function_decl|;
block|}
end_class

end_unit

