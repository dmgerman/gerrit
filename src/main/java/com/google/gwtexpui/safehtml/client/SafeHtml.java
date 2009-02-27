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
name|HasHTML
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

