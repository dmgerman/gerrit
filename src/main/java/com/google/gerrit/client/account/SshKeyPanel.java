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
name|FormatUtil
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountSshKey
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
name|InvalidSshKeyException
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
name|user
operator|.
name|client
operator|.
name|Command
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
name|DeferredCommand
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
name|RootPanel
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
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
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
DECL|class|SshKeyPanel
class|class
name|SshKeyPanel
extends|extends
name|Composite
block|{
DECL|field|loadedApplet
specifier|private
specifier|static
name|boolean
name|loadedApplet
decl_stmt|;
DECL|field|applet
specifier|private
specifier|static
name|Element
name|applet
decl_stmt|;
DECL|field|appletErrorInvalidKey
specifier|private
specifier|static
name|String
name|appletErrorInvalidKey
decl_stmt|;
DECL|field|appletErrorSecurity
specifier|private
specifier|static
name|String
name|appletErrorSecurity
decl_stmt|;
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
DECL|field|browse
specifier|private
name|Button
name|browse
decl_stmt|;
DECL|field|addTxt
specifier|private
name|NpTextArea
name|addTxt
decl_stmt|;
DECL|field|delSel
specifier|private
name|Button
name|delSel
decl_stmt|;
DECL|field|serverKeys
specifier|private
name|Panel
name|serverKeys
decl_stmt|;
DECL|method|SshKeyPanel ()
name|SshKeyPanel
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
name|delSel
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
name|delSel
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
name|delSel
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
literal|"gerrit-AddSshKeyPanel"
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
name|addKeyBlock
operator|.
name|add
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
name|DOM
operator|.
name|setElementPropertyBoolean
argument_list|(
name|addTxt
operator|.
name|getElement
argument_list|()
argument_list|,
literal|"spellcheck"
argument_list|,
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
name|browse
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonOpenSshKey
argument_list|()
argument_list|)
expr_stmt|;
name|browse
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
name|doBrowse
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|browse
operator|.
name|setVisible
argument_list|(
name|GWT
operator|.
name|isScript
argument_list|()
operator|&&
operator|(
operator|!
name|loadedApplet
operator|||
name|applet
operator|!=
literal|null
operator|)
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|browse
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
DECL|method|doBrowse ()
name|void
name|doBrowse
parameter_list|()
block|{
name|browse
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|loadedApplet
condition|)
block|{
name|applet
operator|=
name|DOM
operator|.
name|createElement
argument_list|(
literal|"applet"
argument_list|)
expr_stmt|;
name|applet
operator|.
name|setAttribute
argument_list|(
literal|"code"
argument_list|,
literal|"com.google.gerrit.keyapplet.ReadPublicKey.class"
argument_list|)
expr_stmt|;
name|applet
operator|.
name|setAttribute
argument_list|(
literal|"archive"
argument_list|,
name|GWT
operator|.
name|getModuleBaseURL
argument_list|()
operator|+
literal|"gerrit-keyapplet.cache.jar?v="
operator|+
name|Gerrit
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|applet
operator|.
name|setAttribute
argument_list|(
literal|"mayscript"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|applet
operator|.
name|setAttribute
argument_list|(
literal|"width"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|applet
operator|.
name|setAttribute
argument_list|(
literal|"height"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|RootPanel
operator|.
name|getBodyElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|applet
argument_list|)
expr_stmt|;
name|loadedApplet
operator|=
literal|true
expr_stmt|;
comment|// We have to defer to allow the event loop time to setup that
comment|// new applet tag we just created above, and actually load the
comment|// applet into the runtime.
comment|//
name|DeferredCommand
operator|.
name|addCommand
argument_list|(
operator|new
name|Command
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|doBrowse
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|applet
operator|==
literal|null
condition|)
block|{
comment|// If the applet element is null, the applet was determined
comment|// to have failed to load, and we are dead. Hide the button.
comment|//
name|noBrowse
argument_list|()
expr_stmt|;
return|return;
block|}
name|String
name|txt
decl_stmt|;
try|try
block|{
name|txt
operator|=
name|openPublicKey
argument_list|(
name|applet
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
comment|// If this call fails, the applet is dead. It is most likely
comment|// not loading due to Java support being disabled.
comment|//
name|noBrowse
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|txt
operator|==
literal|null
condition|)
block|{
name|txt
operator|=
literal|""
expr_stmt|;
block|}
name|browse
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|appletErrorInvalidKey
operator|==
literal|null
condition|)
block|{
name|appletErrorInvalidKey
operator|=
name|getErrorInvalidKey
argument_list|(
name|applet
argument_list|)
expr_stmt|;
name|appletErrorSecurity
operator|=
name|getErrorSecurity
argument_list|(
name|applet
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|appletErrorInvalidKey
operator|.
name|equals
argument_list|(
name|txt
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
return|return;
block|}
if|if
condition|(
name|appletErrorSecurity
operator|.
name|equals
argument_list|(
name|txt
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
return|return;
block|}
name|addTxt
operator|.
name|setText
argument_list|(
name|txt
argument_list|)
expr_stmt|;
name|addNew
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|noBrowse ()
specifier|private
name|void
name|noBrowse
parameter_list|()
block|{
if|if
condition|(
name|applet
operator|!=
literal|null
condition|)
block|{
name|applet
operator|.
name|getParentElement
argument_list|()
operator|.
name|removeChild
argument_list|(
name|applet
argument_list|)
expr_stmt|;
name|applet
operator|=
literal|null
expr_stmt|;
block|}
name|browse
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
operator|new
name|ErrorDialog
argument_list|(
name|Util
operator|.
name|C
operator|.
name|sshJavaAppletNotAvailable
argument_list|()
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
DECL|method|openPublicKey (Element keyapp)
specifier|private
specifier|static
specifier|native
name|String
name|openPublicKey
parameter_list|(
name|Element
name|keyapp
parameter_list|)
comment|/*-{ var r = keyapp.openPublicKey(); return r == null ? null : ''+r; }-*/
function_decl|;
DECL|method|getErrorInvalidKey (Element keyapp)
specifier|private
specifier|static
specifier|native
name|String
name|getErrorInvalidKey
parameter_list|(
name|Element
name|keyapp
parameter_list|)
comment|/*-{ return ''+keyapp.getErrorInvalidKey(); }-*/
function_decl|;
DECL|method|getErrorSecurity (Element keyapp)
specifier|private
specifier|static
specifier|native
name|String
name|getErrorSecurity
parameter_list|(
name|Element
name|keyapp
parameter_list|)
comment|/*-{ return ''+keyapp.getErrorSecurity(); }-*/
function_decl|;
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
name|Util
operator|.
name|ACCOUNT_SEC
operator|.
name|addSshKey
argument_list|(
name|txt
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|AccountSshKey
name|result
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
name|result
argument_list|)
expr_stmt|;
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
specifier|public
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
name|ACCOUNT_SEC
operator|.
name|mySshKeys
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|List
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountSshKey
argument_list|>
name|result
parameter_list|)
block|{
name|keys
operator|.
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|showAddKeyBlock
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|serverKeys
operator|.
name|clear
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
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
name|AccountSshKey
argument_list|>
block|{
DECL|field|S_INVALID
specifier|private
specifier|static
specifier|final
name|String
name|S_INVALID
init|=
literal|"gerrit-SshKeyPanel-Invalid"
decl_stmt|;
DECL|method|SshKeyTable ()
name|SshKeyTable
parameter_list|()
block|{
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
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|,
name|Util
operator|.
name|C
operator|.
name|sshKeyLastUsed
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|7
argument_list|,
name|Util
operator|.
name|C
operator|.
name|sshKeyStored
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
name|S_ICON_HEADER
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
name|S_DATA_HEADER
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
name|S_DATA_HEADER
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
name|S_DATA_HEADER
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
name|S_DATA_HEADER
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|,
name|S_DATA_HEADER
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|7
argument_list|,
name|S_DATA_HEADER
argument_list|)
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
name|AccountSshKey
operator|.
name|Id
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|AccountSshKey
operator|.
name|Id
argument_list|>
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
name|AccountSshKey
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
name|ids
operator|.
name|add
argument_list|(
name|k
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Util
operator|.
name|ACCOUNT_SEC
operator|.
name|deleteSshKeys
argument_list|(
name|ids
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
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
name|AccountSshKey
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
name|ids
operator|.
name|contains
argument_list|(
name|k
operator|.
name|getKey
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
name|showAddKeyBlock
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final List<AccountSshKey> result)
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountSshKey
argument_list|>
name|result
parameter_list|)
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
for|for
control|(
specifier|final
name|AccountSshKey
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
block|}
DECL|method|addOneKey (final AccountSshKey k)
name|void
name|addOneKey
parameter_list|(
specifier|final
name|AccountSshKey
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
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
operator|new
name|CheckBox
argument_list|()
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
name|S_INVALID
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
name|S_INVALID
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
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
name|elide
argument_list|(
name|k
operator|.
name|getEncodedKey
argument_list|()
argument_list|)
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
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|6
argument_list|,
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|k
operator|.
name|getLastUsedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|7
argument_list|,
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|k
operator|.
name|getStoredOn
argument_list|()
argument_list|)
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
name|S_ICON_CELL
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
name|S_ICON_CELL
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
literal|"gerrit-SshKeyPanel-EncodedKey"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|3
init|;
name|c
operator|<=
literal|7
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
name|S_DATA_CELL
argument_list|)
expr_stmt|;
block|}
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|6
argument_list|,
literal|"C_LAST_UPDATE"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|7
argument_list|,
literal|"C_LAST_UPDATE"
argument_list|)
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
DECL|method|elide (final String s)
name|String
name|elide
parameter_list|(
specifier|final
name|String
name|s
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
literal|40
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
literal|30
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
block|}
end_class

end_unit

