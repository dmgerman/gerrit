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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|ErrorDialog
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
name|VoidResult
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
name|client
operator|.
name|ui
operator|.
name|ComplexDisclosurePanel
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
name|FancyFlexTable
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
name|data
operator|.
name|SshHostKey
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
name|errors
operator|.
name|InvalidSshKeyException
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
name|JsArray
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
name|ValueChangeEvent
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
name|ValueChangeHandler
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
name|CheckBox
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
name|FlexTable
operator|.
name|FlexCellFormatter
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
name|HasHorizontalAlignment
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
name|HorizontalPanel
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
name|Panel
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
name|clippy
operator|.
name|client
operator|.
name|CopyableLabel
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
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_class
DECL|class|SshPanel
class|class
name|SshPanel
extends|extends
name|Composite
block|{
DECL|field|keys
specifier|private
name|SshKeyTable
name|keys
decl_stmt|;
DECL|field|showAddKeyBlock
specifier|private
name|Button
name|showAddKeyBlock
decl_stmt|;
DECL|field|addKeyBlock
specifier|private
name|Panel
name|addKeyBlock
decl_stmt|;
DECL|field|closeAddKeyBlock
specifier|private
name|Button
name|closeAddKeyBlock
decl_stmt|;
DECL|field|clearNew
specifier|private
name|Button
name|clearNew
decl_stmt|;
DECL|field|addNew
specifier|private
name|Button
name|addNew
decl_stmt|;
DECL|field|addTxt
specifier|private
name|NpTextArea
name|addTxt
decl_stmt|;
DECL|field|deleteKey
specifier|private
name|Button
name|deleteKey
decl_stmt|;
DECL|field|serverKeys
specifier|private
name|Panel
name|serverKeys
decl_stmt|;
DECL|field|loadCount
specifier|private
name|int
name|loadCount
decl_stmt|;
DECL|method|SshPanel ()
name|SshPanel
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
name|showAddKeyBlock
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonShowAddSshKey
argument_list|()
argument_list|)
expr_stmt|;
name|showAddKeyBlock
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
name|showAddKeyBlock
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|keys
operator|=
operator|new
name|SshKeyTable
argument_list|()
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|keys
argument_list|)
expr_stmt|;
block|{
specifier|final
name|FlowPanel
name|fp
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|deleteKey
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonDeleteSshKey
argument_list|()
argument_list|)
expr_stmt|;
name|deleteKey
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|deleteKey
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
name|keys
operator|.
name|deleteChecked
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|deleteKey
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|showAddKeyBlock
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|fp
argument_list|)
expr_stmt|;
block|}
name|addKeyBlock
operator|=
operator|new
name|VerticalPanel
argument_list|()
expr_stmt|;
name|addKeyBlock
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addKeyBlock
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
name|addKeyBlock
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
name|addSshKeyPanelHeader
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|ComplexDisclosurePanel
name|addSshKeyHelp
init|=
operator|new
name|ComplexDisclosurePanel
argument_list|(
name|Util
operator|.
name|C
operator|.
name|addSshKeyHelpTitle
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|addSshKeyHelp
operator|.
name|setContent
argument_list|(
operator|new
name|HTML
argument_list|(
name|Util
operator|.
name|C
operator|.
name|addSshKeyHelp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|addKeyBlock
operator|.
name|add
argument_list|(
name|addSshKeyHelp
argument_list|)
expr_stmt|;
name|addTxt
operator|=
operator|new
name|NpTextArea
argument_list|()
expr_stmt|;
name|addTxt
operator|.
name|setVisibleLines
argument_list|(
literal|12
argument_list|)
expr_stmt|;
name|addTxt
operator|.
name|setCharacterWidth
argument_list|(
literal|80
argument_list|)
expr_stmt|;
name|addTxt
operator|.
name|setSpellCheck
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addKeyBlock
operator|.
name|add
argument_list|(
name|addTxt
argument_list|)
expr_stmt|;
specifier|final
name|HorizontalPanel
name|buttons
init|=
operator|new
name|HorizontalPanel
argument_list|()
decl_stmt|;
name|addKeyBlock
operator|.
name|add
argument_list|(
name|buttons
argument_list|)
expr_stmt|;
name|clearNew
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonClearSshKeyInput
argument_list|()
argument_list|)
expr_stmt|;
name|clearNew
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
name|addTxt
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addTxt
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|clearNew
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
name|buttonAddSshKey
argument_list|()
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
name|doAddNew
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|addNew
argument_list|)
expr_stmt|;
name|closeAddKeyBlock
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonCloseAddSshKey
argument_list|()
argument_list|)
expr_stmt|;
name|closeAddKeyBlock
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
name|showAddKeyBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|closeAddKeyBlock
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|setCellWidth
argument_list|(
name|closeAddKeyBlock
argument_list|,
literal|"100%"
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|setCellHorizontalAlignment
argument_list|(
name|closeAddKeyBlock
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_RIGHT
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|addKeyBlock
argument_list|)
expr_stmt|;
name|serverKeys
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|serverKeys
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|setKeyTableVisible (final boolean on)
name|void
name|setKeyTableVisible
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|keys
operator|.
name|setVisible
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|deleteKey
operator|.
name|setVisible
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|closeAddKeyBlock
operator|.
name|setVisible
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
DECL|method|doAddNew ()
name|void
name|doAddNew
parameter_list|()
block|{
specifier|final
name|String
name|txt
init|=
name|addTxt
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|txt
operator|!=
literal|null
operator|&&
name|txt
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|addNew
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AccountApi
operator|.
name|addSshKey
argument_list|(
literal|"self"
argument_list|,
name|txt
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|SshKeyInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|SshKeyInfo
name|k
parameter_list|)
block|{
name|addNew
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addTxt
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|keys
operator|.
name|addOneKey
argument_list|(
name|k
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|keys
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|showAddKeyBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setKeyTableVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|keys
operator|.
name|updateDeleteButton
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
name|addNew
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|isInvalidSshKey
argument_list|(
name|caught
argument_list|)
condition|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|Util
operator|.
name|C
operator|.
name|invalidSshKeyError
argument_list|()
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isInvalidSshKey
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
name|caught
operator|instanceof
name|InvalidSshKeyException
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|caught
operator|instanceof
name|RemoteJsonException
operator|&&
name|InvalidSshKeyException
operator|.
name|MESSAGE
operator|.
name|equals
argument_list|(
name|caught
operator|.
name|getMessage
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
name|refreshSshKeys
argument_list|()
expr_stmt|;
name|Gerrit
operator|.
name|SYSTEM_SVC
operator|.
name|daemonHostKeys
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|List
argument_list|<
name|SshHostKey
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|List
argument_list|<
name|SshHostKey
argument_list|>
name|result
parameter_list|)
block|{
name|serverKeys
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|SshHostKey
name|keyInfo
range|:
name|result
control|)
block|{
name|serverKeys
operator|.
name|add
argument_list|(
operator|new
name|SshHostKeyPanel
argument_list|(
name|keyInfo
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|++
name|loadCount
operator|==
literal|2
condition|)
block|{
name|display
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|refreshSshKeys ()
specifier|private
name|void
name|refreshSshKeys
parameter_list|()
block|{
name|AccountApi
operator|.
name|getSshKeys
argument_list|(
literal|"self"
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|JsArray
argument_list|<
name|SshKeyInfo
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JsArray
argument_list|<
name|SshKeyInfo
argument_list|>
name|result
parameter_list|)
block|{
name|keys
operator|.
name|display
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|&&
name|keys
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|showAddKeyBlock
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|++
name|loadCount
operator|==
literal|2
condition|)
block|{
name|display
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|display ()
name|void
name|display
parameter_list|()
block|{   }
DECL|method|showAddKeyBlock (final boolean show)
specifier|private
name|void
name|showAddKeyBlock
parameter_list|(
specifier|final
name|boolean
name|show
parameter_list|)
block|{
name|showAddKeyBlock
operator|.
name|setVisible
argument_list|(
operator|!
name|show
argument_list|)
expr_stmt|;
name|addKeyBlock
operator|.
name|setVisible
argument_list|(
name|show
argument_list|)
expr_stmt|;
block|}
DECL|class|SshKeyTable
specifier|private
class|class
name|SshKeyTable
extends|extends
name|FancyFlexTable
argument_list|<
name|SshKeyInfo
argument_list|>
block|{
DECL|field|updateDeleteHandler
specifier|private
name|ValueChangeHandler
argument_list|<
name|Boolean
argument_list|>
name|updateDeleteHandler
decl_stmt|;
DECL|method|SshKeyTable ()
name|SshKeyTable
parameter_list|()
block|{
name|table
operator|.
name|setWidth
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|sshKeyStatus
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|,
name|Util
operator|.
name|C
operator|.
name|sshKeyAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|,
name|Util
operator|.
name|C
operator|.
name|sshKeyKey
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
name|Util
operator|.
name|C
operator|.
name|sshKeyComment
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|FlexCellFormatter
name|fmt
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
decl_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|iconHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|updateDeleteHandler
operator|=
operator|new
name|ValueChangeHandler
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onValueChange
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|event
parameter_list|)
block|{
name|updateDeleteButton
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|deleteChecked ()
name|void
name|deleteChecked
parameter_list|()
block|{
specifier|final
name|HashSet
argument_list|<
name|Integer
argument_list|>
name|sequenceNumbers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|1
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
specifier|final
name|SshKeyInfo
name|k
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
operator|&&
operator|(
operator|(
name|CheckBox
operator|)
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|sequenceNumbers
operator|.
name|add
argument_list|(
name|k
operator|.
name|seq
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sequenceNumbers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|updateDeleteButton
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|deleteKey
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AccountApi
operator|.
name|deleteSshKeys
argument_list|(
literal|"self"
argument_list|,
name|sequenceNumbers
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|VoidResult
name|result
parameter_list|)
block|{
for|for
control|(
name|int
name|row
init|=
literal|1
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
control|)
block|{
specifier|final
name|SshKeyInfo
name|k
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
operator|&&
name|sequenceNumbers
operator|.
name|contains
argument_list|(
name|k
operator|.
name|seq
argument_list|()
argument_list|)
condition|)
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|row
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|table
operator|.
name|getRowCount
argument_list|()
operator|==
literal|1
condition|)
block|{
name|display
argument_list|(
name|Collections
operator|.
expr|<
name|SshKeyInfo
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateDeleteButton
argument_list|()
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
name|caught
parameter_list|)
block|{
name|refreshSshKeys
argument_list|()
expr_stmt|;
name|updateDeleteButton
argument_list|()
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
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final List<SshKeyInfo> result)
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|SshKeyInfo
argument_list|>
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setKeyTableVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|showAddKeyBlock
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
while|while
condition|(
literal|1
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|)
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|table
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|SshKeyInfo
name|k
range|:
name|result
control|)
block|{
name|addOneKey
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
name|setKeyTableVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|deleteKey
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addOneKey (final SshKeyInfo k)
name|void
name|addOneKey
parameter_list|(
specifier|final
name|SshKeyInfo
name|k
parameter_list|)
block|{
specifier|final
name|FlexCellFormatter
name|fmt
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
decl_stmt|;
specifier|final
name|int
name|row
init|=
name|table
operator|.
name|getRowCount
argument_list|()
decl_stmt|;
name|table
operator|.
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|applyDataRowStyle
argument_list|(
name|row
argument_list|)
expr_stmt|;
specifier|final
name|CheckBox
name|sel
init|=
operator|new
name|CheckBox
argument_list|()
decl_stmt|;
name|sel
operator|.
name|addValueChangeHandler
argument_list|(
name|updateDeleteHandler
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|sel
argument_list|)
expr_stmt|;
if|if
condition|(
name|k
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|removeStyleName
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
comment|//
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|sshKeyPanelInvalid
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|sshKeyInvalid
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|sshKeyPanelInvalid
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|3
argument_list|,
name|k
operator|.
name|algorithm
argument_list|()
argument_list|)
expr_stmt|;
name|CopyableLabel
name|keyLabel
init|=
operator|new
name|CopyableLabel
argument_list|(
name|k
operator|.
name|sshPublicKey
argument_list|()
argument_list|)
decl_stmt|;
name|keyLabel
operator|.
name|setPreviewText
argument_list|(
name|elide
argument_list|(
name|k
operator|.
name|encodedKey
argument_list|()
argument_list|,
literal|40
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
name|keyLabel
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|5
argument_list|,
name|k
operator|.
name|comment
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|iconCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|sshKeyPanelEncodedKey
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|2
init|;
name|c
operator|<=
literal|5
condition|;
name|c
operator|++
control|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|c
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataCell
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
DECL|method|updateDeleteButton ()
name|void
name|updateDeleteButton
parameter_list|()
block|{
name|boolean
name|on
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|1
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|CheckBox
name|sel
init|=
operator|(
name|CheckBox
operator|)
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|sel
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|on
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|deleteKey
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|elide (final String s, final int len)
specifier|static
name|String
name|elide
parameter_list|(
specifier|final
name|String
name|s
parameter_list|,
specifier|final
name|int
name|len
parameter_list|)
block|{
if|if
condition|(
name|s
operator|==
literal|null
operator|||
name|s
operator|.
name|length
argument_list|()
operator|<
name|len
operator|||
name|len
operator|<=
literal|10
condition|)
block|{
return|return
name|s
return|;
block|}
return|return
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|len
operator|-
literal|10
argument_list|)
operator|+
literal|"..."
operator|+
name|s
operator|.
name|substring
argument_list|(
name|s
operator|.
name|length
argument_list|()
operator|-
literal|10
argument_list|)
return|;
block|}
block|}
end_class

end_unit

