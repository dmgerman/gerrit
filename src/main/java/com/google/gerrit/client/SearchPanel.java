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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|gerrit
operator|.
name|client
operator|.
name|changes
operator|.
name|ChangeScreen
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
name|reviewdb
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|BlurEvent
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
name|BlurHandler
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
name|FocusEvent
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
name|FocusHandler
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
name|shared
operator|.
name|HandlerRegistration
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
name|KeyCommand
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

begin_class
DECL|class|SearchPanel
class|class
name|SearchPanel
extends|extends
name|Composite
block|{
DECL|field|searchBox
specifier|private
specifier|final
name|NpTextBox
name|searchBox
decl_stmt|;
DECL|field|regFocus
specifier|private
name|HandlerRegistration
name|regFocus
decl_stmt|;
DECL|method|SearchPanel ()
name|SearchPanel
parameter_list|()
block|{
specifier|final
name|FlowPanel
name|body
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|setStyleName
argument_list|(
literal|"gerrit-SearchPanel"
argument_list|)
expr_stmt|;
name|searchBox
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|searchBox
operator|.
name|setVisibleLength
argument_list|(
literal|42
argument_list|)
expr_stmt|;
name|searchBox
operator|.
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|searchHint
argument_list|()
argument_list|)
expr_stmt|;
name|searchBox
operator|.
name|addStyleName
argument_list|(
literal|"gerrit-InputFieldTypeHint"
argument_list|)
expr_stmt|;
name|searchBox
operator|.
name|addFocusHandler
argument_list|(
operator|new
name|FocusHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFocus
parameter_list|(
name|FocusEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|Gerrit
operator|.
name|C
operator|.
name|searchHint
argument_list|()
operator|.
name|equals
argument_list|(
name|searchBox
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
name|searchBox
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|searchBox
operator|.
name|removeStyleName
argument_list|(
literal|"gerrit-InputFieldTypeHint"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|searchBox
operator|.
name|addBlurHandler
argument_list|(
operator|new
name|BlurHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onBlur
parameter_list|(
name|BlurEvent
name|event
parameter_list|)
block|{
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|searchBox
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
name|searchBox
operator|.
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|searchHint
argument_list|()
argument_list|)
expr_stmt|;
name|searchBox
operator|.
name|addStyleName
argument_list|(
literal|"gerrit-InputFieldTypeHint"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|searchBox
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
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getCharCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
condition|)
block|{
name|doSearch
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|Button
name|searchButton
init|=
operator|new
name|Button
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|searchButton
argument_list|()
argument_list|)
decl_stmt|;
name|searchButton
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
name|ClickEvent
name|event
parameter_list|)
block|{
name|doSearch
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|searchBox
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|searchButton
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
if|if
condition|(
name|regFocus
operator|==
literal|null
condition|)
block|{
name|regFocus
operator|=
name|GlobalKey
operator|.
name|addApplication
argument_list|(
name|this
argument_list|,
operator|new
name|KeyCommand
argument_list|(
literal|0
argument_list|,
literal|'/'
argument_list|,
name|Gerrit
operator|.
name|C
operator|.
name|keySearch
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
name|searchBox
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|searchBox
operator|.
name|selectAll
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onUnload ()
specifier|protected
name|void
name|onUnload
parameter_list|()
block|{
if|if
condition|(
name|regFocus
operator|!=
literal|null
condition|)
block|{
name|regFocus
operator|.
name|removeHandler
argument_list|()
expr_stmt|;
name|regFocus
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|doSearch ()
specifier|private
name|void
name|doSearch
parameter_list|()
block|{
specifier|final
name|String
name|query
init|=
name|searchBox
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
name|Gerrit
operator|.
name|C
operator|.
name|searchHint
argument_list|()
operator|.
name|equals
argument_list|(
name|query
argument_list|)
condition|)
block|{
return|return;
block|}
name|searchBox
operator|.
name|setFocus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|query
operator|.
name|matches
argument_list|(
literal|"^[1-9][0-9]*$"
argument_list|)
condition|)
block|{
specifier|final
name|Change
operator|.
name|Id
name|ck
init|=
name|Change
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|Gerrit
operator|.
name|display
argument_list|(
name|Link
operator|.
name|toChange
argument_list|(
name|ck
argument_list|)
argument_list|,
operator|new
name|ChangeScreen
argument_list|(
name|ck
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Gerrit
operator|.
name|display
argument_list|(
operator|new
name|NotFoundScreen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

