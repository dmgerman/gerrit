begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|core
operator|.
name|client
operator|.
name|GWT
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
name|dom
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
name|regexp
operator|.
name|shared
operator|.
name|MatchResult
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
name|regexp
operator|.
name|shared
operator|.
name|RegExp
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Immutable string safely placed as HTML without further escaping. */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|SafeHtml
specifier|public
specifier|abstract
class|class
name|SafeHtml
implements|implements
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|safehtml
operator|.
name|shared
operator|.
name|SafeHtml
block|{
DECL|field|RESOURCES
specifier|public
specifier|static
specifier|final
name|SafeHtmlResources
name|RESOURCES
decl_stmt|;
static|static
block|{
if|if
condition|(
name|GWT
operator|.
name|isClient
argument_list|()
condition|)
block|{
name|RESOURCES
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|SafeHtmlResources
operator|.
name|class
argument_list|)
expr_stmt|;
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|RESOURCES
operator|=
operator|new
name|SafeHtmlResources
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SafeHtmlCss
name|css
parameter_list|()
block|{
return|return
operator|new
name|SafeHtmlCss
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|wikiList
parameter_list|()
block|{
return|return
literal|"wikiList"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|wikiPreFormat
parameter_list|()
block|{
return|return
literal|"wikiPreFormat"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|wikiQuote
parameter_list|()
block|{
return|return
literal|"wikiQuote"
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|ensureInjected
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
block|}
expr_stmt|;
block|}
block|}
comment|/** @return the existing HTML property of a widget. */
DECL|method|get (HasHTML t)
specifier|public
specifier|static
name|SafeHtml
name|get
parameter_list|(
name|HasHTML
name|t
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlString
argument_list|(
name|t
operator|.
name|getHTML
argument_list|()
argument_list|)
return|;
block|}
comment|/** @return the existing HTML text, wrapped in a safe buffer. */
DECL|method|asis (String htmlText)
specifier|public
specifier|static
name|SafeHtml
name|asis
parameter_list|(
name|String
name|htmlText
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlString
argument_list|(
name|htmlText
argument_list|)
return|;
block|}
comment|/** Set the HTML property of a widget. */
DECL|method|set (T e, SafeHtml str)
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
name|T
name|e
parameter_list|,
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
comment|/** @return the existing inner HTML of any element. */
DECL|method|get (Element e)
specifier|public
specifier|static
name|SafeHtml
name|get
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlString
argument_list|(
name|e
operator|.
name|getInnerHTML
argument_list|()
argument_list|)
return|;
block|}
comment|/** Set the inner HTML of any element. */
DECL|method|setInnerHTML (Element e, SafeHtml str)
specifier|public
specifier|static
name|Element
name|setInnerHTML
parameter_list|(
name|Element
name|e
parameter_list|,
name|SafeHtml
name|str
parameter_list|)
block|{
name|e
operator|.
name|setInnerHTML
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
comment|/** @return the existing inner HTML of a table cell. */
DECL|method|get (HTMLTable t, int row, int col)
specifier|public
specifier|static
name|SafeHtml
name|get
parameter_list|(
name|HTMLTable
name|t
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlString
argument_list|(
name|t
operator|.
name|getHTML
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
argument_list|)
return|;
block|}
comment|/** Set the inner HTML of a table cell. */
DECL|method|set (final T t, int row, int col, SafeHtml str)
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
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|,
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
DECL|method|parse (SafeHtml html)
specifier|public
specifier|static
name|Element
name|parse
parameter_list|(
name|SafeHtml
name|html
parameter_list|)
block|{
name|Element
name|e
init|=
name|DOM
operator|.
name|createDiv
argument_list|()
decl_stmt|;
name|setInnerHTML
argument_list|(
name|e
argument_list|,
name|html
argument_list|)
expr_stmt|;
return|return
name|DOM
operator|.
name|getFirstChild
argument_list|(
name|e
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
specifier|final
name|String
name|part
init|=
literal|"(?:[a-zA-Z0-9$_+!*'%;:@=?#/~-]|&(?!lt;|gt;)|[.,](?!(?:\\s|$)))"
decl_stmt|;
return|return
name|replaceAll
argument_list|(
literal|"(https?://"
operator|+
name|part
operator|+
literal|"{2,}(?:[(]"
operator|+
name|part
operator|+
literal|"*[)])*"
operator|+
name|part
operator|+
literal|"*)"
argument_list|,
literal|"<a href=\"$1\" target=\"_blank\" rel=\"nofollow\">$1</a>"
argument_list|)
return|;
block|}
comment|/**    * Apply {@link #linkify()}, and "\n\n" to&lt;p&gt;.    *    *<p>Lines that start with whitespace are assumed to be preformatted, and are formatted by the    * {@link SafeHtmlCss#wikiPreFormat()} CSS class.    */
DECL|method|wikify ()
specifier|public
name|SafeHtml
name|wikify
parameter_list|()
block|{
specifier|final
name|SafeHtmlBuilder
name|r
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|p
range|:
name|linkify
argument_list|()
operator|.
name|asString
argument_list|()
operator|.
name|split
argument_list|(
literal|"\n\n"
argument_list|)
control|)
block|{
if|if
condition|(
name|isQuote
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|wikifyQuote
argument_list|(
name|r
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isPreFormat
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|r
operator|.
name|openElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|line
range|:
name|p
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
control|)
block|{
name|r
operator|.
name|openSpan
argument_list|()
expr_stmt|;
name|r
operator|.
name|setStyleName
argument_list|(
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|wikiPreFormat
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|asis
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|closeSpan
argument_list|()
expr_stmt|;
name|r
operator|.
name|br
argument_list|()
expr_stmt|;
block|}
name|r
operator|.
name|closeElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isList
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|wikifyList
argument_list|(
name|r
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|openElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|asis
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|closeElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
operator|.
name|toSafeHtml
argument_list|()
return|;
block|}
DECL|method|wikifyList (SafeHtmlBuilder r, String p)
specifier|private
name|void
name|wikifyList
parameter_list|(
name|SafeHtmlBuilder
name|r
parameter_list|,
name|String
name|p
parameter_list|)
block|{
name|boolean
name|in_ul
init|=
literal|false
decl_stmt|;
name|boolean
name|in_p
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|p
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
control|)
block|{
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
operator|||
name|line
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|in_ul
condition|)
block|{
if|if
condition|(
name|in_p
condition|)
block|{
name|in_p
operator|=
literal|false
expr_stmt|;
name|r
operator|.
name|closeElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
block|}
name|in_ul
operator|=
literal|true
expr_stmt|;
name|r
operator|.
name|openElement
argument_list|(
literal|"ul"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setStyleName
argument_list|(
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|wikiList
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|in_ul
condition|)
block|{
if|if
condition|(
operator|!
name|in_p
condition|)
block|{
name|in_p
operator|=
literal|true
expr_stmt|;
name|r
operator|.
name|openElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|asis
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|r
operator|.
name|openElement
argument_list|(
literal|"li"
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|asis
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|closeElement
argument_list|(
literal|"li"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in_ul
condition|)
block|{
name|r
operator|.
name|closeElement
argument_list|(
literal|"ul"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|in_p
condition|)
block|{
name|r
operator|.
name|closeElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|wikifyQuote (SafeHtmlBuilder r, String p)
specifier|private
name|void
name|wikifyQuote
parameter_list|(
name|SafeHtmlBuilder
name|r
parameter_list|,
name|String
name|p
parameter_list|)
block|{
name|r
operator|.
name|openElement
argument_list|(
literal|"blockquote"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setStyleName
argument_list|(
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|wikiQuote
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|startsWith
argument_list|(
literal|"&gt; "
argument_list|)
condition|)
block|{
name|p
operator|=
name|p
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p
operator|.
name|startsWith
argument_list|(
literal|"&gt; "
argument_list|)
condition|)
block|{
name|p
operator|=
name|p
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|p
operator|.
name|replaceAll
argument_list|(
literal|"\\n ?&gt; "
argument_list|,
literal|"\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|e
range|:
name|p
operator|.
name|split
argument_list|(
literal|"\n\n"
argument_list|)
control|)
block|{
if|if
condition|(
name|isQuote
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|SafeHtmlBuilder
name|b
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
name|wikifyQuote
argument_list|(
name|b
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|append
argument_list|(
name|asis
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|r
operator|.
name|closeElement
argument_list|(
literal|"blockquote"
argument_list|)
expr_stmt|;
block|}
DECL|method|isQuote (String p)
specifier|private
specifier|static
name|boolean
name|isQuote
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|p
operator|.
name|startsWith
argument_list|(
literal|"&gt; "
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"&gt; "
argument_list|)
return|;
block|}
DECL|method|isPreFormat (String p)
specifier|private
specifier|static
name|boolean
name|isPreFormat
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|p
operator|.
name|contains
argument_list|(
literal|"\n "
argument_list|)
operator|||
name|p
operator|.
name|contains
argument_list|(
literal|"\n\t"
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|" "
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"\t"
argument_list|)
return|;
block|}
DECL|method|isList (String p)
specifier|private
specifier|static
name|boolean
name|isList
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|p
operator|.
name|contains
argument_list|(
literal|"\n- "
argument_list|)
operator|||
name|p
operator|.
name|contains
argument_list|(
literal|"\n* "
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"- "
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"* "
argument_list|)
return|;
block|}
comment|/**    * Replace first occurrence of {@code regex} with {@code repl} .    *    *<p><b>WARNING:</b> This replacement is being performed against an otherwise safe HTML string.    * The caller must ensure that the replacement does not introduce cross-site scripting attack    * entry points.    *    * @param regex regular expression pattern to match the substring with.    * @param repl replacement expression. Capture groups within {@code regex} can be referenced with    *     {@code $<i>n</i>}.    * @return a new string, after the replacement has been made.    */
DECL|method|replaceFirst (String regex, String repl)
specifier|public
name|SafeHtml
name|replaceFirst
parameter_list|(
name|String
name|regex
parameter_list|,
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
comment|/**    * Replace each occurrence of {@code regex} with {@code repl} .    *    *<p><b>WARNING:</b> This replacement is being performed against an otherwise safe HTML string.    * The caller must ensure that the replacement does not introduce cross-site scripting attack    * entry points.    *    * @param regex regular expression pattern to match substrings with.    * @param repl replacement expression. Capture groups within {@code regex} can be referenced with    *     {@code $<i>n</i>}.    * @return a new string, after the replacements have been made.    */
DECL|method|replaceAll (String regex, String repl)
specifier|public
name|SafeHtml
name|replaceAll
parameter_list|(
name|String
name|regex
parameter_list|,
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
comment|/**    * Replace all find/replace pairs in the list in a single pass.    *    * @param findReplaceList find/replace pairs to use.    * @return a new string, after the replacements have been made.    */
DECL|method|replaceAll (List<? extends FindReplace> findReplaceList)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SafeHtml
name|replaceAll
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|FindReplace
argument_list|>
name|findReplaceList
parameter_list|)
block|{
if|if
condition|(
name|findReplaceList
operator|==
literal|null
operator|||
name|findReplaceList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|StringBuilder
name|pat
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
extends|extends
name|FindReplace
argument_list|>
name|it
init|=
name|findReplaceList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|FindReplace
name|fr
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|pat
operator|.
name|append
argument_list|(
name|fr
operator|.
name|pattern
argument_list|()
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|pat
operator|.
name|append
argument_list|(
literal|'|'
argument_list|)
expr_stmt|;
block|}
block|}
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|RegExp
name|re
init|=
name|RegExp
operator|.
name|compile
argument_list|(
name|pat
operator|.
name|toString
argument_list|()
argument_list|,
literal|"g"
argument_list|)
decl_stmt|;
name|String
name|orig
init|=
name|asString
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
name|MatchResult
name|mat
decl_stmt|;
while|while
condition|(
operator|(
name|mat
operator|=
name|re
operator|.
name|exec
argument_list|(
name|orig
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
name|String
name|g
init|=
name|mat
operator|.
name|getGroup
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// Re-run each candidate to find which one matched.
for|for
control|(
name|FindReplace
name|fr
range|:
name|findReplaceList
control|)
block|{
if|if
condition|(
name|fr
operator|.
name|pattern
argument_list|()
operator|.
name|test
argument_list|(
name|g
argument_list|)
condition|)
block|{
try|try
block|{
name|String
name|repl
init|=
name|fr
operator|.
name|replace
argument_list|(
name|g
argument_list|)
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
name|orig
operator|.
name|substring
argument_list|(
name|index
argument_list|,
name|mat
operator|.
name|getIndex
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|repl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
continue|continue;
block|}
name|index
operator|=
name|mat
operator|.
name|getIndex
argument_list|()
operator|+
name|g
operator|.
name|length
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
name|result
operator|.
name|append
argument_list|(
name|orig
operator|.
name|substring
argument_list|(
name|index
argument_list|,
name|orig
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|asis
argument_list|(
name|result
operator|.
name|toString
argument_list|()
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
annotation|@
name|Override
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
