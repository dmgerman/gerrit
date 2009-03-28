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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|SuggestOracle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_comment
comment|/**  * A suggestion oracle that tries to highlight the matched text.  *<p>  * Suggestions supplied by the implementation of  * {@link #onRequestSuggestions(Request, Callback)} are modified to wrap all  * occurrences of the {@link SuggestOracle.Request#getQuery()} substring in HTML  *<code>&lt;strong&gt;</code> tags, so they can be emphasized to the user.  */
end_comment

begin_class
DECL|class|HighlightSuggestOracle
specifier|public
specifier|abstract
class|class
name|HighlightSuggestOracle
extends|extends
name|SuggestOracle
block|{
DECL|method|escape (String ds)
specifier|private
specifier|static
name|String
name|escape
parameter_list|(
name|String
name|ds
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|ds
argument_list|)
operator|.
name|asString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isDisplayStringHTML ()
specifier|public
specifier|final
name|boolean
name|isDisplayStringHTML
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|requestSuggestions (final Request request, final Callback cb)
specifier|public
specifier|final
name|void
name|requestSuggestions
parameter_list|(
specifier|final
name|Request
name|request
parameter_list|,
specifier|final
name|Callback
name|cb
parameter_list|)
block|{
name|onRequestSuggestions
argument_list|(
name|request
argument_list|,
operator|new
name|Callback
argument_list|()
block|{
specifier|public
name|void
name|onSuggestionsReady
parameter_list|(
specifier|final
name|Request
name|request
parameter_list|,
specifier|final
name|Response
name|response
parameter_list|)
block|{
specifier|final
name|String
name|qpat
init|=
literal|"("
operator|+
name|escape
argument_list|(
name|request
operator|.
name|getQuery
argument_list|()
argument_list|)
operator|+
literal|")"
decl_stmt|;
specifier|final
name|boolean
name|html
init|=
name|isHTML
argument_list|()
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|Suggestion
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|Suggestion
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Suggestion
name|s
range|:
name|response
operator|.
name|getSuggestions
argument_list|()
control|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|BoldSuggestion
argument_list|(
name|qpat
argument_list|,
name|s
argument_list|,
name|html
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|cb
operator|.
name|onSuggestionsReady
argument_list|(
name|request
argument_list|,
operator|new
name|Response
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**    * @return true if {@link SuggestOracle.Suggestion#getDisplayString()} returns    *         HTML; false if the text must be escaped before evaluating in an    *         HTML like context.    */
DECL|method|isHTML ()
specifier|protected
name|boolean
name|isHTML
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** Compute the suggestions and return them for display. */
DECL|method|onRequestSuggestions (Request request, Callback done)
specifier|protected
specifier|abstract
name|void
name|onRequestSuggestions
parameter_list|(
name|Request
name|request
parameter_list|,
name|Callback
name|done
parameter_list|)
function_decl|;
DECL|class|BoldSuggestion
specifier|private
specifier|static
class|class
name|BoldSuggestion
implements|implements
name|Suggestion
block|{
DECL|field|suggestion
specifier|private
specifier|final
name|Suggestion
name|suggestion
decl_stmt|;
DECL|field|displayString
specifier|private
specifier|final
name|String
name|displayString
decl_stmt|;
DECL|method|BoldSuggestion (final String qstr, final Suggestion s, final boolean html)
name|BoldSuggestion
parameter_list|(
specifier|final
name|String
name|qstr
parameter_list|,
specifier|final
name|Suggestion
name|s
parameter_list|,
specifier|final
name|boolean
name|html
parameter_list|)
block|{
name|suggestion
operator|=
name|s
expr_stmt|;
name|String
name|ds
init|=
name|s
operator|.
name|getDisplayString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|html
condition|)
block|{
name|ds
operator|=
name|escape
argument_list|(
name|ds
argument_list|)
expr_stmt|;
block|}
name|displayString
operator|=
name|sgi
argument_list|(
name|ds
argument_list|,
name|qstr
argument_list|,
literal|"<strong>$1</strong>"
argument_list|)
expr_stmt|;
block|}
DECL|method|sgi (String inString, String pat, String newHtml)
specifier|private
specifier|static
specifier|native
name|String
name|sgi
parameter_list|(
name|String
name|inString
parameter_list|,
name|String
name|pat
parameter_list|,
name|String
name|newHtml
parameter_list|)
comment|/*-{ return inString.replace(RegExp(pat, 'gi'), newHtml); }-*/
function_decl|;
DECL|method|getDisplayString ()
specifier|public
name|String
name|getDisplayString
parameter_list|()
block|{
return|return
name|displayString
return|;
block|}
DECL|method|getReplacementString ()
specifier|public
name|String
name|getReplacementString
parameter_list|()
block|{
return|return
name|suggestion
operator|.
name|getReplacementString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

