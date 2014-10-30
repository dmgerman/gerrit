begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|Gerrit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
operator|.
name|ChangeApi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
operator|.
name|ChangeInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|GerritCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|Natives
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
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
name|dom
operator|.
name|client
operator|.
name|NativeEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyCodes
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyDownEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyDownHandler
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
name|uibinder
operator|.
name|client
operator|.
name|UiBinder
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
name|uibinder
operator|.
name|client
operator|.
name|UiField
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
name|uibinder
operator|.
name|client
operator|.
name|UiHandler
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
name|rpc
operator|.
name|StatusCodeException
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
name|Button
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
name|Composite
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
name|HTMLPanel
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
name|ImageResourceRenderer
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
name|UIObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
operator|.
name|NpTextBox
import|;
end_import

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_class
DECL|class|Hashtags
specifier|public
class|class
name|Hashtags
extends|extends
name|Composite
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|Hashtags
argument_list|>
block|{}
DECL|field|VISIBLE_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|VISIBLE_LENGTH
init|=
literal|55
decl_stmt|;
DECL|field|uiBinder
specifier|private
specifier|static
specifier|final
name|Binder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Binder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|REMOVE
specifier|private
specifier|static
specifier|final
name|String
name|REMOVE
decl_stmt|;
DECL|field|DATA_ID
specifier|private
specifier|static
specifier|final
name|String
name|DATA_ID
init|=
literal|"data-id"
decl_stmt|;
static|static
block|{
name|REMOVE
operator|=
name|DOM
operator|.
name|createUniqueId
argument_list|()
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|REMOVE
argument_list|)
expr_stmt|;
block|}
DECL|method|init (String r)
specifier|private
specifier|static
specifier|final
specifier|native
name|void
name|init
parameter_list|(
name|String
name|r
parameter_list|)
comment|/*-{     $wnd[r] = $entry(function(e) {       @com.google.gerrit.client.change.Hashtags::onRemove(Lcom/google/gwt/dom/client/NativeEvent;)(e)     });   }-*/
function_decl|;
DECL|method|onRemove (NativeEvent event)
specifier|private
specifier|static
name|void
name|onRemove
parameter_list|(
name|NativeEvent
name|event
parameter_list|)
block|{
name|String
name|hashtags
init|=
name|getDataId
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|hashtags
operator|!=
literal|null
condition|)
block|{
specifier|final
name|ChangeScreen2
name|screen
init|=
name|ChangeScreen2
operator|.
name|get
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|ChangeApi
operator|.
name|hashtags
argument_list|(
name|screen
operator|.
name|getChangeId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|post
argument_list|(
name|PostInput
operator|.
name|create
argument_list|(
literal|null
argument_list|,
name|hashtags
argument_list|)
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|JavaScriptObject
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JavaScriptObject
name|result
parameter_list|)
block|{
if|if
condition|(
name|screen
operator|.
name|isCurrentView
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|screen
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getDataId (NativeEvent event)
specifier|private
specifier|static
name|String
name|getDataId
parameter_list|(
name|NativeEvent
name|event
parameter_list|)
block|{
name|Element
name|e
init|=
name|event
operator|.
name|getEventTarget
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|String
name|v
init|=
name|e
operator|.
name|getAttribute
argument_list|(
name|DATA_ID
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|v
return|;
block|}
name|e
operator|=
name|e
operator|.
name|getParentElement
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|field|hashtagsText
annotation|@
name|UiField
name|Element
name|hashtagsText
decl_stmt|;
DECL|field|openForm
annotation|@
name|UiField
name|Button
name|openForm
decl_stmt|;
DECL|field|form
annotation|@
name|UiField
name|Element
name|form
decl_stmt|;
DECL|field|error
annotation|@
name|UiField
name|Element
name|error
decl_stmt|;
DECL|field|hashtagTextBox
annotation|@
name|UiField
name|NpTextBox
name|hashtagTextBox
decl_stmt|;
DECL|field|style
specifier|private
name|ChangeScreen2
operator|.
name|Style
name|style
decl_stmt|;
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|method|Hashtags ()
specifier|public
name|Hashtags
parameter_list|()
block|{
name|initWidget
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|hashtagTextBox
operator|.
name|setVisibleLength
argument_list|(
name|VISIBLE_LENGTH
argument_list|)
expr_stmt|;
name|hashtagTextBox
operator|.
name|addKeyDownHandler
argument_list|(
operator|new
name|KeyDownHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyDown
parameter_list|(
name|KeyDownEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getNativeEvent
argument_list|()
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ESCAPE
condition|)
block|{
name|onCancel
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getNativeEvent
argument_list|()
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
condition|)
block|{
name|onAdd
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|init (ChangeScreen2.Style style)
name|void
name|init
parameter_list|(
name|ChangeScreen2
operator|.
name|Style
name|style
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
block|}
DECL|method|set (ChangeInfo info)
name|void
name|set
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|info
operator|.
name|legacy_id
argument_list|()
expr_stmt|;
name|display
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|openForm
operator|.
name|setVisible
argument_list|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"openForm"
argument_list|)
DECL|method|onOpenForm (@uppressWarningsR) ClickEvent e)
name|void
name|onOpenForm
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|onOpenForm
argument_list|()
expr_stmt|;
block|}
DECL|method|onOpenForm ()
name|void
name|onOpenForm
parameter_list|()
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|form
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|error
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|openForm
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|hashtagTextBox
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|display (ChangeInfo info)
specifier|private
name|void
name|display
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
name|hashtagsText
operator|.
name|setInnerSafeHtml
argument_list|(
name|formatHashtags
argument_list|(
name|info
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|display (JsArrayString hashtags)
specifier|private
name|void
name|display
parameter_list|(
name|JsArrayString
name|hashtags
parameter_list|)
block|{
name|hashtagsText
operator|.
name|setInnerSafeHtml
argument_list|(
name|formatHashtags
argument_list|(
name|hashtags
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|formatHashtags (ChangeInfo info)
specifier|private
name|SafeHtmlBuilder
name|formatHashtags
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|info
operator|.
name|hashtags
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|formatHashtags
argument_list|(
name|info
operator|.
name|hashtags
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|new
name|SafeHtmlBuilder
argument_list|()
return|;
block|}
DECL|method|formatHashtags (JsArrayString hashtags)
specifier|private
name|SafeHtmlBuilder
name|formatHashtags
parameter_list|(
name|JsArrayString
name|hashtags
parameter_list|)
block|{
name|SafeHtmlBuilder
name|html
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|itr
init|=
name|Natives
operator|.
name|asList
argument_list|(
name|hashtags
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|hashtagName
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|html
operator|.
name|openSpan
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|DATA_ID
argument_list|,
name|hashtagName
argument_list|)
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|hashtagName
argument_list|()
argument_list|)
operator|.
name|openAnchor
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"href"
argument_list|,
literal|"#"
operator|+
name|PageLinks
operator|.
name|toChangeQuery
argument_list|(
literal|"hashtag:\""
operator|+
name|hashtagName
operator|+
literal|"\""
argument_list|)
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"role"
argument_list|,
literal|"listitem"
argument_list|)
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
operator|.
name|append
argument_list|(
name|hashtagName
argument_list|)
operator|.
name|closeAnchor
argument_list|()
operator|.
name|openElement
argument_list|(
literal|"button"
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"title"
argument_list|,
literal|"Remove hashtag"
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"onclick"
argument_list|,
name|REMOVE
operator|+
literal|"(event)"
argument_list|)
operator|.
name|append
argument_list|(
operator|new
name|ImageResourceRenderer
argument_list|()
operator|.
name|render
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|remove_reviewer
argument_list|()
argument_list|)
argument_list|)
operator|.
name|closeElement
argument_list|(
literal|"button"
argument_list|)
operator|.
name|closeSpan
argument_list|()
expr_stmt|;
if|if
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|html
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|html
return|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"cancel"
argument_list|)
DECL|method|onCancel (@uppressWarningsR) ClickEvent e)
name|void
name|onCancel
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|openForm
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|form
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|hashtagTextBox
operator|.
name|setFocus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"add"
argument_list|)
DECL|method|onAdd (@uppressWarningsR) ClickEvent e)
name|void
name|onAdd
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|String
name|hashtag
init|=
name|hashtagTextBox
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|hashtag
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addHashtag
argument_list|(
name|hashtag
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addHashtag (final String hashtags)
specifier|private
name|void
name|addHashtag
parameter_list|(
specifier|final
name|String
name|hashtags
parameter_list|)
block|{
name|ChangeApi
operator|.
name|hashtags
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|post
argument_list|(
name|PostInput
operator|.
name|create
argument_list|(
name|hashtags
argument_list|,
literal|null
argument_list|)
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|JsArrayString
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JsArrayString
name|result
parameter_list|)
block|{
name|hashtagTextBox
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|error
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|error
operator|.
name|setInnerText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|hashtagTextBox
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
name|result
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|updateHashtagList
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|error
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|error
operator|.
name|setInnerText
argument_list|(
name|err
operator|instanceof
name|StatusCodeException
condition|?
operator|(
operator|(
name|StatusCodeException
operator|)
name|err
operator|)
operator|.
name|getEncodedResponse
argument_list|()
else|:
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|hashtagTextBox
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|updateHashtagList ()
specifier|protected
name|void
name|updateHashtagList
parameter_list|()
block|{
name|ChangeApi
operator|.
name|detail
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeInfo
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|updateHashtagList (JsArrayString hashtags)
specifier|protected
name|void
name|updateHashtagList
parameter_list|(
name|JsArrayString
name|hashtags
parameter_list|)
block|{
name|display
argument_list|(
name|hashtags
argument_list|)
expr_stmt|;
block|}
DECL|class|PostInput
specifier|public
specifier|static
class|class
name|PostInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (String add, String remove)
specifier|public
specifier|static
name|PostInput
name|create
parameter_list|(
name|String
name|add
parameter_list|,
name|String
name|remove
parameter_list|)
block|{
name|PostInput
name|input
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|input
operator|.
name|init
argument_list|(
name|toJsArrayString
argument_list|(
name|add
argument_list|)
argument_list|,
name|toJsArrayString
argument_list|(
name|remove
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|input
return|;
block|}
DECL|method|toJsArrayString (String commaSeparated)
specifier|private
specifier|static
name|JsArrayString
name|toJsArrayString
parameter_list|(
name|String
name|commaSeparated
parameter_list|)
block|{
if|if
condition|(
name|commaSeparated
operator|==
literal|null
operator|||
name|commaSeparated
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|JsArrayString
name|array
init|=
name|JsArrayString
operator|.
name|createArray
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|hashtag
range|:
name|commaSeparated
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|array
operator|.
name|push
argument_list|(
name|hashtag
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
DECL|method|init (JsArrayString add, JsArrayString remove)
specifier|private
specifier|native
name|void
name|init
parameter_list|(
name|JsArrayString
name|add
parameter_list|,
name|JsArrayString
name|remove
parameter_list|)
comment|/*-{       this.add = add;       this.remove = remove;     }-*/
function_decl|;
DECL|method|PostInput ()
specifier|protected
name|PostInput
parameter_list|()
block|{     }
block|}
DECL|class|Result
specifier|public
specifier|static
class|class
name|Result
extends|extends
name|JavaScriptObject
block|{
DECL|method|hashtags ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|hashtags
parameter_list|()
comment|/*-{ return this.hashtags; }-*/
function_decl|;
DECL|method|error ()
specifier|public
specifier|final
specifier|native
name|String
name|error
parameter_list|()
comment|/*-{ return this.error; }-*/
function_decl|;
DECL|method|Result ()
specifier|protected
name|Result
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

