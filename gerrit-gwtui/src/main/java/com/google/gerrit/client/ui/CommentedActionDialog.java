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
name|ClickHandler
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
name|logical
operator|.
name|shared
operator|.
name|CloseEvent
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
name|logical
operator|.
name|shared
operator|.
name|CloseHandler
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
name|FlowPanel
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
name|FocusWidget
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
name|PopupPanel
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
name|GlobalKey
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
name|NpTextArea
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
name|user
operator|.
name|client
operator|.
name|AutoCenterDialogBox
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|CommentedActionDialog
specifier|public
specifier|abstract
class|class
name|CommentedActionDialog
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AutoCenterDialogBox
implements|implements
name|CloseHandler
argument_list|<
name|PopupPanel
argument_list|>
block|{
DECL|field|panel
specifier|protected
specifier|final
name|FlowPanel
name|panel
decl_stmt|;
DECL|field|message
specifier|protected
specifier|final
name|NpTextArea
name|message
decl_stmt|;
DECL|field|sendButton
specifier|protected
specifier|final
name|Button
name|sendButton
decl_stmt|;
DECL|field|cancelButton
specifier|protected
specifier|final
name|Button
name|cancelButton
decl_stmt|;
DECL|field|buttonPanel
specifier|protected
specifier|final
name|FlowPanel
name|buttonPanel
decl_stmt|;
DECL|field|callback
specifier|protected
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|callback
decl_stmt|;
DECL|field|focusOn
specifier|protected
name|FocusWidget
name|focusOn
decl_stmt|;
DECL|field|sent
specifier|protected
name|boolean
name|sent
init|=
literal|false
decl_stmt|;
DECL|method|CommentedActionDialog (final String title, final String heading, AsyncCallback<T> callback)
specifier|public
name|CommentedActionDialog
parameter_list|(
specifier|final
name|String
name|title
parameter_list|,
specifier|final
name|String
name|heading
parameter_list|,
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
block|{
name|super
argument_list|(
comment|/* auto hide */
literal|false
argument_list|,
comment|/* modal */
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|setGlassEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setText
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentedActionDialog
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|NpTextArea
argument_list|()
expr_stmt|;
name|message
operator|.
name|setCharacterWidth
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|message
operator|.
name|setVisibleLines
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|message
operator|.
name|getElement
argument_list|()
operator|.
name|setPropertyBoolean
argument_list|(
literal|"spellcheck"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|setFocusOn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|sendButton
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|commentedActionButtonSend
argument_list|()
argument_list|)
expr_stmt|;
name|sendButton
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClick
parameter_list|(
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
name|enableButtons
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|onSend
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|cancelButton
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|commentedActionButtonCancel
argument_list|()
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|getElement
argument_list|()
operator|.
name|getStyle
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"float"
argument_list|,
literal|"right"
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClick
parameter_list|(
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|mwrap
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|mwrap
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentedActionMessage
argument_list|()
argument_list|)
expr_stmt|;
name|mwrap
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|buttonPanel
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|sendButton
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|cancelButton
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|getElement
argument_list|()
operator|.
name|getStyle
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"marginTop"
argument_list|,
literal|"4px"
argument_list|)
expr_stmt|;
name|panel
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
operator|new
name|SmallHeading
argument_list|(
name|heading
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|mwrap
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|buttonPanel
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|addCloseHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|setFocusOn (FocusWidget focusWidget)
specifier|public
name|void
name|setFocusOn
parameter_list|(
name|FocusWidget
name|focusWidget
parameter_list|)
block|{
name|focusOn
operator|=
name|focusWidget
expr_stmt|;
block|}
DECL|method|enableButtons (boolean enable)
specifier|public
name|void
name|enableButtons
parameter_list|(
name|boolean
name|enable
parameter_list|)
block|{
name|sendButton
operator|.
name|setEnabled
argument_list|(
name|enable
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|setEnabled
argument_list|(
name|enable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|center ()
specifier|public
name|void
name|center
parameter_list|()
block|{
name|super
operator|.
name|center
argument_list|()
expr_stmt|;
name|GlobalKey
operator|.
name|dialog
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|focusOn
operator|!=
literal|null
condition|)
block|{
name|focusOn
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onClose (CloseEvent<PopupPanel> event)
specifier|public
name|void
name|onClose
parameter_list|(
name|CloseEvent
argument_list|<
name|PopupPanel
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|sent
condition|)
block|{
comment|// the dialog was closed without the send button being pressed
comment|// e.g. the user pressed Cancel or ESC to close the dialog
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|sent
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|onSend ()
specifier|public
specifier|abstract
name|void
name|onSend
parameter_list|()
function_decl|;
DECL|method|getMessageText ()
specifier|public
name|String
name|getMessageText
parameter_list|()
block|{
return|return
name|message
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
return|;
block|}
DECL|method|createCallback ()
specifier|public
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|createCallback
parameter_list|()
block|{
return|return
operator|new
name|GerritCallback
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|sent
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
name|hide
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|enableButtons
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

