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

begin_comment
comment|/** Safely constructs a {@link SafeHtml}, escaping user provided content. */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|SafeHtmlBuilder
specifier|public
class|class
name|SafeHtmlBuilder
extends|extends
name|SafeHtml
block|{
DECL|field|impl
specifier|private
specifier|static
specifier|final
name|Impl
name|impl
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
name|impl
operator|=
operator|new
name|ClientImpl
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|impl
operator|=
operator|new
name|ServerImpl
argument_list|()
expr_stmt|;
block|}
block|}
DECL|field|dBuf
specifier|private
specifier|final
name|BufferDirect
name|dBuf
decl_stmt|;
DECL|field|cb
specifier|private
name|Buffer
name|cb
decl_stmt|;
DECL|field|sBuf
specifier|private
name|BufferSealElement
name|sBuf
decl_stmt|;
DECL|field|att
specifier|private
name|AttMap
name|att
decl_stmt|;
DECL|method|SafeHtmlBuilder ()
specifier|public
name|SafeHtmlBuilder
parameter_list|()
block|{
name|cb
operator|=
name|dBuf
operator|=
operator|new
name|BufferDirect
argument_list|()
expr_stmt|;
block|}
comment|/** @return true if this builder has not had an append occur yet. */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|dBuf
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/** @return true if this builder has content appended into it. */
DECL|method|hasContent ()
specifier|public
name|boolean
name|hasContent
parameter_list|()
block|{
return|return
operator|!
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|append (boolean in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|boolean
name|in
parameter_list|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (char in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|char
name|in
parameter_list|)
block|{
switch|switch
condition|(
name|in
condition|)
block|{
case|case
literal|'&'
case|:
name|cb
operator|.
name|append
argument_list|(
literal|"&amp;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'>'
case|:
name|cb
operator|.
name|append
argument_list|(
literal|"&gt;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'<'
case|:
name|cb
operator|.
name|append
argument_list|(
literal|"&lt;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'"'
case|:
name|cb
operator|.
name|append
argument_list|(
literal|"&quot;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\''
case|:
name|cb
operator|.
name|append
argument_list|(
literal|"&#39;"
argument_list|)
expr_stmt|;
break|break;
default|default:
name|cb
operator|.
name|append
argument_list|(
name|in
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|this
return|;
block|}
DECL|method|append (int in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|int
name|in
parameter_list|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (long in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|long
name|in
parameter_list|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (float in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|float
name|in
parameter_list|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (double in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|double
name|in
parameter_list|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Append already safe HTML as-is, avoiding double escaping. */
DECL|method|append (com.google.gwt.safehtml.shared.SafeHtml in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
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
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|in
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** Append already safe HTML as-is, avoiding double escaping. */
DECL|method|append (SafeHtml in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|SafeHtml
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|in
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** Append the string, escaping unsafe characters. */
DECL|method|append (String in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|String
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|impl
operator|.
name|escapeStr
argument_list|(
name|this
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** Append the string, escaping unsafe characters. */
DECL|method|append (StringBuilder in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|StringBuilder
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|in
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** Append the string, escaping unsafe characters. */
DECL|method|append (StringBuffer in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|StringBuffer
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|in
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** Append the result of toString(), escaping unsafe characters. */
DECL|method|append (Object in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|Object
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|append
argument_list|(
name|in
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** Append the string, escaping unsafe characters. */
DECL|method|append (CharSequence in)
specifier|public
name|SafeHtmlBuilder
name|append
parameter_list|(
name|CharSequence
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|escapeCS
argument_list|(
name|this
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**    * Open an element, appending "{@code<tagName>}" to the buffer.    *    *<p>After the element is open the attributes may be manipulated until the next {@code append},    * {@code openElement}, {@code closeSelf} or {@code closeElement} call.    *    * @param tagName name of the HTML element to open.    */
DECL|method|openElement (String tagName)
specifier|public
name|SafeHtmlBuilder
name|openElement
parameter_list|(
name|String
name|tagName
parameter_list|)
block|{
assert|assert
name|isElementName
argument_list|(
name|tagName
argument_list|)
assert|;
name|cb
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|tagName
argument_list|)
expr_stmt|;
if|if
condition|(
name|sBuf
operator|==
literal|null
condition|)
block|{
name|att
operator|=
operator|new
name|AttMap
argument_list|()
expr_stmt|;
name|sBuf
operator|=
operator|new
name|BufferSealElement
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|att
operator|.
name|reset
argument_list|(
name|tagName
argument_list|)
expr_stmt|;
name|cb
operator|=
name|sBuf
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Get an attribute of the last opened element.    *    * @param name name of the attribute to read.    * @return the attribute value, as a string. The empty string if the attribute has not been    *     assigned a value. The returned string is the raw (unescaped) value.    */
DECL|method|getAttribute (String name)
specifier|public
name|String
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
assert|assert
name|isAttributeName
argument_list|(
name|name
argument_list|)
assert|;
assert|assert
name|cb
operator|==
name|sBuf
assert|;
return|return
name|att
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**    * Set an attribute of the last opened element.    *    * @param name name of the attribute to set.    * @param value value to assign; any existing value is replaced. The value is escaped (if    *     necessary) during the assignment.    */
DECL|method|setAttribute (String name, String value)
specifier|public
name|SafeHtmlBuilder
name|setAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
assert|assert
name|isAttributeName
argument_list|(
name|name
argument_list|)
assert|;
assert|assert
name|cb
operator|==
name|sBuf
assert|;
name|att
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|value
operator|!=
literal|null
condition|?
name|value
else|:
literal|""
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Set an attribute of the last opened element.    *    * @param name name of the attribute to set.    * @param value value to assign, any existing value is replaced.    */
DECL|method|setAttribute (String name, int value)
specifier|public
name|SafeHtmlBuilder
name|setAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
name|setAttribute
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Append a new value into a whitespace delimited attribute.    *    *<p>If the attribute is not yet assigned, this method sets the attribute. If the attribute is    * already assigned, the new value is appended onto the end, after appending a single space to    * delimit the values.    *    * @param name name of the attribute to append onto.    * @param value additional value to append.    */
DECL|method|appendAttribute (String name, String value)
specifier|public
name|SafeHtmlBuilder
name|appendAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|String
name|e
init|=
name|getAttribute
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|setAttribute
argument_list|(
name|name
argument_list|,
name|e
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|e
operator|+
literal|" "
operator|+
name|value
else|:
name|value
argument_list|)
return|;
block|}
return|return
name|this
return|;
block|}
comment|/** Set the height attribute of the current element. */
DECL|method|setHeight (int height)
specifier|public
name|SafeHtmlBuilder
name|setHeight
parameter_list|(
name|int
name|height
parameter_list|)
block|{
return|return
name|setAttribute
argument_list|(
literal|"height"
argument_list|,
name|height
argument_list|)
return|;
block|}
comment|/** Set the width attribute of the current element. */
DECL|method|setWidth (int width)
specifier|public
name|SafeHtmlBuilder
name|setWidth
parameter_list|(
name|int
name|width
parameter_list|)
block|{
return|return
name|setAttribute
argument_list|(
literal|"width"
argument_list|,
name|width
argument_list|)
return|;
block|}
comment|/** Set the CSS class name for this element. */
DECL|method|setStyleName (String style)
specifier|public
name|SafeHtmlBuilder
name|setStyleName
parameter_list|(
name|String
name|style
parameter_list|)
block|{
assert|assert
name|isCssName
argument_list|(
name|style
argument_list|)
assert|;
return|return
name|setAttribute
argument_list|(
literal|"class"
argument_list|,
name|style
argument_list|)
return|;
block|}
comment|/**    * Add an additional CSS class name to this element.    *    *<p>If no CSS class name has been specified yet, this method initializes it to the single name.    */
DECL|method|addStyleName (String style)
specifier|public
name|SafeHtmlBuilder
name|addStyleName
parameter_list|(
name|String
name|style
parameter_list|)
block|{
assert|assert
name|isCssName
argument_list|(
name|style
argument_list|)
assert|;
return|return
name|appendAttribute
argument_list|(
literal|"class"
argument_list|,
name|style
argument_list|)
return|;
block|}
DECL|method|sealElement0 ()
specifier|private
name|void
name|sealElement0
parameter_list|()
block|{
assert|assert
name|cb
operator|==
name|sBuf
assert|;
name|cb
operator|=
name|dBuf
expr_stmt|;
name|att
operator|.
name|onto
argument_list|(
name|cb
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|sealElement ()
name|Buffer
name|sealElement
parameter_list|()
block|{
name|sealElement0
argument_list|()
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
return|return
name|cb
return|;
block|}
comment|/** Close the current element with a self closing suffix ("/&gt;"). */
DECL|method|closeSelf ()
specifier|public
name|SafeHtmlBuilder
name|closeSelf
parameter_list|()
block|{
name|sealElement0
argument_list|()
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
literal|" />"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Append a closing tag for the named element. */
DECL|method|closeElement (String name)
specifier|public
name|SafeHtmlBuilder
name|closeElement
parameter_list|(
name|String
name|name
parameter_list|)
block|{
assert|assert
name|isElementName
argument_list|(
name|name
argument_list|)
assert|;
name|cb
operator|.
name|append
argument_list|(
literal|"</"
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Append "&amp;nbsp;" - a non-breaking space, useful in empty table cells. */
DECL|method|nbsp ()
specifier|public
name|SafeHtmlBuilder
name|nbsp
parameter_list|()
block|{
name|cb
operator|.
name|append
argument_list|(
literal|"&nbsp;"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Append "&lt;br /&gt;" - a line break with no attributes */
DECL|method|br ()
specifier|public
name|SafeHtmlBuilder
name|br
parameter_list|()
block|{
name|cb
operator|.
name|append
argument_list|(
literal|"<br />"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Append "&lt;tr&gt;"; attributes may be set if needed */
DECL|method|openTr ()
specifier|public
name|SafeHtmlBuilder
name|openTr
parameter_list|()
block|{
return|return
name|openElement
argument_list|(
literal|"tr"
argument_list|)
return|;
block|}
comment|/** Append "&lt;/tr&gt;" */
DECL|method|closeTr ()
specifier|public
name|SafeHtmlBuilder
name|closeTr
parameter_list|()
block|{
return|return
name|closeElement
argument_list|(
literal|"tr"
argument_list|)
return|;
block|}
comment|/** Append "&lt;td&gt;"; attributes may be set if needed */
DECL|method|openTd ()
specifier|public
name|SafeHtmlBuilder
name|openTd
parameter_list|()
block|{
return|return
name|openElement
argument_list|(
literal|"td"
argument_list|)
return|;
block|}
comment|/** Append "&lt;/td&gt;" */
DECL|method|closeTd ()
specifier|public
name|SafeHtmlBuilder
name|closeTd
parameter_list|()
block|{
return|return
name|closeElement
argument_list|(
literal|"td"
argument_list|)
return|;
block|}
comment|/** Append "&lt;th&gt;"; attributes may be set if needed */
DECL|method|openTh ()
specifier|public
name|SafeHtmlBuilder
name|openTh
parameter_list|()
block|{
return|return
name|openElement
argument_list|(
literal|"th"
argument_list|)
return|;
block|}
comment|/** Append "&lt;/th&gt;" */
DECL|method|closeTh ()
specifier|public
name|SafeHtmlBuilder
name|closeTh
parameter_list|()
block|{
return|return
name|closeElement
argument_list|(
literal|"th"
argument_list|)
return|;
block|}
comment|/** Append "&lt;div&gt;"; attributes may be set if needed */
DECL|method|openDiv ()
specifier|public
name|SafeHtmlBuilder
name|openDiv
parameter_list|()
block|{
return|return
name|openElement
argument_list|(
literal|"div"
argument_list|)
return|;
block|}
comment|/** Append "&lt;/div&gt;" */
DECL|method|closeDiv ()
specifier|public
name|SafeHtmlBuilder
name|closeDiv
parameter_list|()
block|{
return|return
name|closeElement
argument_list|(
literal|"div"
argument_list|)
return|;
block|}
comment|/** Append "&lt;span&gt;"; attributes may be set if needed */
DECL|method|openSpan ()
specifier|public
name|SafeHtmlBuilder
name|openSpan
parameter_list|()
block|{
return|return
name|openElement
argument_list|(
literal|"span"
argument_list|)
return|;
block|}
comment|/** Append "&lt;/span&gt;" */
DECL|method|closeSpan ()
specifier|public
name|SafeHtmlBuilder
name|closeSpan
parameter_list|()
block|{
return|return
name|closeElement
argument_list|(
literal|"span"
argument_list|)
return|;
block|}
comment|/** Append "&lt;a&gt;"; attributes may be set if needed */
DECL|method|openAnchor ()
specifier|public
name|SafeHtmlBuilder
name|openAnchor
parameter_list|()
block|{
return|return
name|openElement
argument_list|(
literal|"a"
argument_list|)
return|;
block|}
comment|/** Append "&lt;/a&gt;" */
DECL|method|closeAnchor ()
specifier|public
name|SafeHtmlBuilder
name|closeAnchor
parameter_list|()
block|{
return|return
name|closeElement
argument_list|(
literal|"a"
argument_list|)
return|;
block|}
comment|/** Append "&lt;param name=... value=... /&gt;". */
DECL|method|paramElement (String name, String value)
specifier|public
name|SafeHtmlBuilder
name|paramElement
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|openElement
argument_list|(
literal|"param"
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|closeSelf
argument_list|()
return|;
block|}
comment|/** @return an immutable {@link SafeHtml} representation of the buffer. */
DECL|method|toSafeHtml ()
specifier|public
name|SafeHtml
name|toSafeHtml
parameter_list|()
block|{
return|return
operator|new
name|SafeHtmlString
argument_list|(
name|asString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|asString ()
specifier|public
name|String
name|asString
parameter_list|()
block|{
return|return
name|cb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|escapeCS (SafeHtmlBuilder b, CharSequence in)
specifier|private
specifier|static
name|void
name|escapeCS
parameter_list|(
name|SafeHtmlBuilder
name|b
parameter_list|,
name|CharSequence
name|in
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|in
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|in
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isElementName (String name)
specifier|private
specifier|static
name|boolean
name|isElementName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|matches
argument_list|(
literal|"^[a-zA-Z][a-zA-Z0-9_-]*$"
argument_list|)
return|;
block|}
DECL|method|isAttributeName (String name)
specifier|private
specifier|static
name|boolean
name|isAttributeName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|isElementName
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|isCssName (String name)
specifier|private
specifier|static
name|boolean
name|isCssName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|isElementName
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|class|Impl
specifier|private
specifier|abstract
specifier|static
class|class
name|Impl
block|{
DECL|method|escapeStr (SafeHtmlBuilder b, String in)
specifier|abstract
name|void
name|escapeStr
parameter_list|(
name|SafeHtmlBuilder
name|b
parameter_list|,
name|String
name|in
parameter_list|)
function_decl|;
block|}
DECL|class|ServerImpl
specifier|private
specifier|static
class|class
name|ServerImpl
extends|extends
name|Impl
block|{
annotation|@
name|Override
DECL|method|escapeStr (SafeHtmlBuilder b, String in)
name|void
name|escapeStr
parameter_list|(
name|SafeHtmlBuilder
name|b
parameter_list|,
name|String
name|in
parameter_list|)
block|{
name|SafeHtmlBuilder
operator|.
name|escapeCS
argument_list|(
name|b
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ClientImpl
specifier|private
specifier|static
class|class
name|ClientImpl
extends|extends
name|Impl
block|{
annotation|@
name|Override
DECL|method|escapeStr (SafeHtmlBuilder b, String in)
name|void
name|escapeStr
parameter_list|(
name|SafeHtmlBuilder
name|b
parameter_list|,
name|String
name|in
parameter_list|)
block|{
name|b
operator|.
name|cb
operator|.
name|append
argument_list|(
name|escape
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|escape (String src)
specifier|private
specifier|static
specifier|native
name|String
name|escape
parameter_list|(
name|String
name|src
parameter_list|)
comment|/*-{ return src.replace(/&/g,'&amp;')                    .replace(/>/g,'&gt;')                    .replace(/</g,'&lt;')                    .replace(/"/g,'&quot;')                    .replace(/'/g,'&#39;');      }-*/
function_decl|;
block|}
block|}
end_class

end_unit
