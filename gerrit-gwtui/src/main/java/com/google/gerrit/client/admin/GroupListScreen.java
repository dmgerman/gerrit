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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
name|Dispatcher
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
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|ScreenLoadCallback
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
name|ui
operator|.
name|AccountScreen
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
name|ui
operator|.
name|OnEditEnabler
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
name|ui
operator|.
name|SmallHeading
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
name|common
operator|.
name|data
operator|.
name|GroupList
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
name|AccountGroup
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
name|user
operator|.
name|client
operator|.
name|History
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
name|VerticalPanel
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
DECL|class|GroupListScreen
specifier|public
class|class
name|GroupListScreen
extends|extends
name|AccountScreen
block|{
DECL|field|groups
specifier|private
name|GroupTable
name|groups
decl_stmt|;
DECL|field|addPanel
specifier|private
name|VerticalPanel
name|addPanel
decl_stmt|;
DECL|field|addTxt
specifier|private
name|NpTextBox
name|addTxt
decl_stmt|;
DECL|field|addNew
specifier|private
name|Button
name|addNew
decl_stmt|;
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
name|Util
operator|.
name|GROUP_SVC
operator|.
name|visibleGroups
argument_list|(
operator|new
name|ScreenLoadCallback
argument_list|<
name|GroupList
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
name|GroupList
name|result
parameter_list|)
block|{
name|addPanel
operator|.
name|setVisible
argument_list|(
name|result
operator|.
name|isCanCreateGroup
argument_list|()
argument_list|)
expr_stmt|;
name|groups
operator|.
name|display
argument_list|(
name|result
operator|.
name|getGroups
argument_list|()
argument_list|)
expr_stmt|;
name|groups
operator|.
name|finishDisplay
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|setPageTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|groupListTitle
argument_list|()
argument_list|)
expr_stmt|;
name|groups
operator|=
operator|new
name|GroupTable
argument_list|(
literal|true
comment|/* hyperlink to admin */
argument_list|,
name|PageLinks
operator|.
name|ADMIN_GROUPS
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|groups
argument_list|)
expr_stmt|;
name|addPanel
operator|=
operator|new
name|VerticalPanel
argument_list|()
expr_stmt|;
name|addPanel
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
name|addSshKeyPanel
argument_list|()
argument_list|)
expr_stmt|;
name|addPanel
operator|.
name|add
argument_list|(
operator|new
name|SmallHeading
argument_list|(
name|Util
operator|.
name|C
operator|.
name|headingCreateGroup
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|addTxt
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|addTxt
operator|.
name|setVisibleLength
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|addTxt
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
name|doCreateGroup
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
name|addTxt
argument_list|)
expr_stmt|;
name|addNew
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonCreateGroup
argument_list|()
argument_list|)
expr_stmt|;
name|addNew
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addNew
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
name|doCreateGroup
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addNew
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
comment|// unregister the keys for the 'groups' table so that pressing ENTER
comment|// when the 'addNew' button has the focus triggers the button (if the
comment|// keys for the 'groups' table would not be unregistered the 'addNew'
comment|// button would not be triggered on ENTER but the group which is
comment|// selected in the 'groups' table would be opened)
name|groups
operator|.
name|setRegisterKeys
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addNew
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
comment|// re-register the keys for the 'groups' table when the 'addNew' button
comment|// gets blurred
name|groups
operator|.
name|setRegisterKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addPanel
operator|.
name|add
argument_list|(
name|addNew
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|addPanel
argument_list|)
expr_stmt|;
operator|new
name|OnEditEnabler
argument_list|(
name|addNew
argument_list|,
name|addTxt
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerKeys ()
specifier|public
name|void
name|registerKeys
parameter_list|()
block|{
name|super
operator|.
name|registerKeys
argument_list|()
expr_stmt|;
name|groups
operator|.
name|setRegisterKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|doCreateGroup ()
specifier|private
name|void
name|doCreateGroup
parameter_list|()
block|{
specifier|final
name|String
name|newName
init|=
name|addTxt
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|newName
operator|==
literal|null
operator|||
name|newName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|addNew
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Util
operator|.
name|GROUP_SVC
operator|.
name|createGroup
argument_list|(
name|newName
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|result
parameter_list|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|Dispatcher
operator|.
name|toGroup
argument_list|(
name|result
argument_list|)
argument_list|)
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
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
name|addNew
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
block|}
end_class

end_unit

