begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|KeyPressEvent
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
name|KeyPressHandler
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
name|SelectionEvent
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
name|SelectionHandler
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
name|SuggestBox
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
name|SuggestBox
operator|.
name|DefaultSuggestionDisplay
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
name|SuggestOracle
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
name|SuggestOracle
operator|.
name|Suggestion
import|;
end_import

begin_class
DECL|class|AddMemberBox
specifier|public
class|class
name|AddMemberBox
extends|extends
name|Composite
block|{
DECL|field|addPanel
specifier|private
specifier|final
name|FlowPanel
name|addPanel
decl_stmt|;
DECL|field|addMember
specifier|private
specifier|final
name|Button
name|addMember
decl_stmt|;
DECL|field|nameTxtBox
specifier|private
specifier|final
name|HintTextBox
name|nameTxtBox
decl_stmt|;
DECL|field|nameTxt
specifier|private
specifier|final
name|SuggestBox
name|nameTxt
decl_stmt|;
DECL|field|submitOnSelection
specifier|private
name|boolean
name|submitOnSelection
decl_stmt|;
DECL|method|AddMemberBox (final String buttonLabel, final String hint, final SuggestOracle suggestOracle)
specifier|public
name|AddMemberBox
parameter_list|(
specifier|final
name|String
name|buttonLabel
parameter_list|,
specifier|final
name|String
name|hint
parameter_list|,
specifier|final
name|SuggestOracle
name|suggestOracle
parameter_list|)
block|{
name|addPanel
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|addMember
operator|=
operator|new
name|Button
argument_list|(
name|buttonLabel
argument_list|)
expr_stmt|;
name|nameTxtBox
operator|=
operator|new
name|HintTextBox
argument_list|()
expr_stmt|;
name|nameTxt
operator|=
operator|new
name|SuggestBox
argument_list|(
operator|new
name|RemoteSuggestOracle
argument_list|(
name|suggestOracle
argument_list|)
argument_list|,
name|nameTxtBox
argument_list|)
expr_stmt|;
name|nameTxt
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
name|addMemberTextBox
argument_list|()
argument_list|)
expr_stmt|;
name|nameTxtBox
operator|.
name|setVisibleLength
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|nameTxtBox
operator|.
name|setHintText
argument_list|(
name|hint
argument_list|)
expr_stmt|;
name|nameTxtBox
operator|.
name|addKeyPressHandler
argument_list|(
operator|new
name|KeyPressHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|submitOnSelection
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|event
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
if|if
condition|(
operator|(
operator|(
name|DefaultSuggestionDisplay
operator|)
name|nameTxt
operator|.
name|getSuggestionDisplay
argument_list|()
operator|)
operator|.
name|isSuggestionListShowing
argument_list|()
condition|)
block|{
name|submitOnSelection
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|doAdd
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|nameTxt
operator|.
name|addSelectionHandler
argument_list|(
operator|new
name|SelectionHandler
argument_list|<
name|Suggestion
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSelection
parameter_list|(
name|SelectionEvent
argument_list|<
name|Suggestion
argument_list|>
name|event
parameter_list|)
block|{
name|nameTxtBox
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|submitOnSelection
condition|)
block|{
name|submitOnSelection
operator|=
literal|false
expr_stmt|;
name|doAdd
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addPanel
operator|.
name|add
argument_list|(
name|nameTxt
argument_list|)
expr_stmt|;
name|addPanel
operator|.
name|add
argument_list|(
name|addMember
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|addPanel
argument_list|)
expr_stmt|;
block|}
DECL|method|addClickHandler (final ClickHandler handler)
specifier|public
name|void
name|addClickHandler
parameter_list|(
specifier|final
name|ClickHandler
name|handler
parameter_list|)
block|{
name|addMember
operator|.
name|addClickHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
name|String
name|s
init|=
name|nameTxtBox
operator|.
name|getText
argument_list|()
decl_stmt|;
return|return
name|s
operator|==
literal|null
condition|?
literal|""
else|:
name|s
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|addMember
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|nameTxtBox
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
DECL|method|setText (String text)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|nameTxtBox
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
DECL|method|doAdd ()
specifier|private
name|void
name|doAdd
parameter_list|()
block|{
name|addMember
operator|.
name|fireEvent
argument_list|(
operator|new
name|ClickEvent
argument_list|()
block|{}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

