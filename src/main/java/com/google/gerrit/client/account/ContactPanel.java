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
name|reviewdb
operator|.
name|Account
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
name|AccountExternalId
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
name|ContactInformation
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
name|Common
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
name|ui
operator|.
name|TextSaveButtonListener
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
name|ChangeEvent
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
name|ChangeHandler
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
name|i18n
operator|.
name|client
operator|.
name|LocaleInfo
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
name|FormPanel
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
name|Grid
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
name|Label
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
name|ListBox
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
name|Widget
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
name|FormPanel
operator|.
name|SubmitEvent
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
name|client
operator|.
name|VoidResult
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|Date
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|ContactPanel
class|class
name|ContactPanel
extends|extends
name|Composite
block|{
DECL|field|parentScreen
specifier|private
specifier|final
name|AccountSettings
name|parentScreen
decl_stmt|;
DECL|field|labelIdx
DECL|field|fieldIdx
specifier|private
name|int
name|labelIdx
decl_stmt|,
name|fieldIdx
decl_stmt|;
DECL|field|currentEmail
specifier|private
name|String
name|currentEmail
decl_stmt|;
DECL|field|haveAccount
specifier|private
name|boolean
name|haveAccount
decl_stmt|;
DECL|field|haveEmails
specifier|private
name|boolean
name|haveEmails
decl_stmt|;
DECL|field|nameTxt
specifier|private
name|NpTextBox
name|nameTxt
decl_stmt|;
DECL|field|emailPick
specifier|private
name|ListBox
name|emailPick
decl_stmt|;
DECL|field|registerNewEmail
specifier|private
name|Button
name|registerNewEmail
decl_stmt|;
DECL|field|hasContact
specifier|private
name|Label
name|hasContact
decl_stmt|;
DECL|field|addressTxt
specifier|private
name|NpTextArea
name|addressTxt
decl_stmt|;
DECL|field|countryTxt
specifier|private
name|NpTextBox
name|countryTxt
decl_stmt|;
DECL|field|phoneTxt
specifier|private
name|NpTextBox
name|phoneTxt
decl_stmt|;
DECL|field|faxTxt
specifier|private
name|NpTextBox
name|faxTxt
decl_stmt|;
DECL|field|save
specifier|private
name|Button
name|save
decl_stmt|;
DECL|method|ContactPanel ()
name|ContactPanel
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ContactPanel (final AccountSettings parent)
name|ContactPanel
parameter_list|(
specifier|final
name|AccountSettings
name|parent
parameter_list|)
block|{
name|parentScreen
operator|=
name|parent
expr_stmt|;
if|if
condition|(
name|LocaleInfo
operator|.
name|getCurrentLocale
argument_list|()
operator|.
name|isRTL
argument_list|()
condition|)
block|{
name|labelIdx
operator|=
literal|1
expr_stmt|;
name|fieldIdx
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|labelIdx
operator|=
literal|0
expr_stmt|;
name|fieldIdx
operator|=
literal|1
expr_stmt|;
block|}
name|nameTxt
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|nameTxt
operator|.
name|setVisibleLength
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|emailPick
operator|=
operator|new
name|ListBox
argument_list|()
expr_stmt|;
name|addressTxt
operator|=
operator|new
name|NpTextArea
argument_list|()
expr_stmt|;
name|addressTxt
operator|.
name|setVisibleLines
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|addressTxt
operator|.
name|setCharacterWidth
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|countryTxt
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|countryTxt
operator|.
name|setVisibleLength
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|countryTxt
operator|.
name|setMaxLength
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|phoneTxt
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|phoneTxt
operator|.
name|setVisibleLength
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|phoneTxt
operator|.
name|setMaxLength
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|faxTxt
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|faxTxt
operator|.
name|setVisibleLength
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|faxTxt
operator|.
name|setMaxLength
argument_list|(
literal|30
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|body
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
specifier|final
name|Grid
name|infoPlainText
init|=
operator|new
name|Grid
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|infoPlainText
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-InfoBlock"
argument_list|)
expr_stmt|;
name|infoPlainText
operator|.
name|addStyleName
argument_list|(
literal|"gerrit-AccountInfoBlock"
argument_list|)
expr_stmt|;
specifier|final
name|Grid
name|infoSecure
init|=
operator|new
name|Grid
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|infoSecure
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-InfoBlock"
argument_list|)
expr_stmt|;
name|infoSecure
operator|.
name|addStyleName
argument_list|(
literal|"gerrit-AccountInfoBlock"
argument_list|)
expr_stmt|;
specifier|final
name|HTML
name|privhtml
init|=
operator|new
name|HTML
argument_list|(
name|Util
operator|.
name|C
operator|.
name|contactPrivacyDetailsHtml
argument_list|()
argument_list|)
decl_stmt|;
name|privhtml
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-AccountContactPrivacyDetails"
argument_list|)
expr_stmt|;
name|hasContact
operator|=
operator|new
name|Label
argument_list|()
expr_stmt|;
name|hasContact
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-AccountContactOnFile"
argument_list|)
expr_stmt|;
name|hasContact
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|infoPlainText
argument_list|)
expr_stmt|;
if|if
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|isUseContactInfo
argument_list|()
condition|)
block|{
name|body
operator|.
name|add
argument_list|(
name|privhtml
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|hasContact
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|infoSecure
argument_list|)
expr_stmt|;
block|}
name|registerNewEmail
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonOpenRegisterNewEmail
argument_list|()
argument_list|)
expr_stmt|;
name|registerNewEmail
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|registerNewEmail
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
name|doRegisterNewEmail
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|emailLine
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|emailLine
operator|.
name|add
argument_list|(
name|emailPick
argument_list|)
expr_stmt|;
name|emailLine
operator|.
name|add
argument_list|(
name|registerNewEmail
argument_list|)
expr_stmt|;
name|row
argument_list|(
name|infoPlainText
argument_list|,
literal|0
argument_list|,
name|Util
operator|.
name|C
operator|.
name|contactFieldFullName
argument_list|()
argument_list|,
name|nameTxt
argument_list|)
expr_stmt|;
name|row
argument_list|(
name|infoPlainText
argument_list|,
literal|1
argument_list|,
name|Util
operator|.
name|C
operator|.
name|contactFieldEmail
argument_list|()
argument_list|,
name|emailLine
argument_list|)
expr_stmt|;
name|row
argument_list|(
name|infoSecure
argument_list|,
literal|0
argument_list|,
name|Util
operator|.
name|C
operator|.
name|contactFieldAddress
argument_list|()
argument_list|,
name|addressTxt
argument_list|)
expr_stmt|;
name|row
argument_list|(
name|infoSecure
argument_list|,
literal|1
argument_list|,
name|Util
operator|.
name|C
operator|.
name|contactFieldCountry
argument_list|()
argument_list|,
name|countryTxt
argument_list|)
expr_stmt|;
name|row
argument_list|(
name|infoSecure
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|contactFieldPhone
argument_list|()
argument_list|,
name|phoneTxt
argument_list|)
expr_stmt|;
name|row
argument_list|(
name|infoSecure
argument_list|,
literal|3
argument_list|,
name|Util
operator|.
name|C
operator|.
name|contactFieldFax
argument_list|()
argument_list|,
name|faxTxt
argument_list|)
expr_stmt|;
name|infoPlainText
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|"topmost"
argument_list|)
expr_stmt|;
name|infoPlainText
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|"topmost"
argument_list|)
expr_stmt|;
name|infoPlainText
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|"bottomheader"
argument_list|)
expr_stmt|;
name|infoSecure
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|"topmost"
argument_list|)
expr_stmt|;
name|infoSecure
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|"topmost"
argument_list|)
expr_stmt|;
name|infoSecure
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|3
argument_list|,
literal|0
argument_list|,
literal|"bottomheader"
argument_list|)
expr_stmt|;
name|save
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonSaveContact
argument_list|()
argument_list|)
expr_stmt|;
name|save
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|save
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
name|doSave
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
name|save
argument_list|)
expr_stmt|;
specifier|final
name|TextSaveButtonListener
name|sbl
init|=
operator|new
name|TextSaveButtonListener
argument_list|(
name|save
argument_list|)
decl_stmt|;
name|nameTxt
operator|.
name|addKeyPressHandler
argument_list|(
name|sbl
argument_list|)
expr_stmt|;
name|emailPick
operator|.
name|addChangeHandler
argument_list|(
operator|new
name|ChangeHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onChange
parameter_list|(
specifier|final
name|ChangeEvent
name|event
parameter_list|)
block|{
specifier|final
name|int
name|idx
init|=
name|emailPick
operator|.
name|getSelectedIndex
argument_list|()
decl_stmt|;
specifier|final
name|String
name|v
init|=
literal|0
operator|<=
name|idx
condition|?
name|emailPick
operator|.
name|getValue
argument_list|(
name|idx
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|C
operator|.
name|buttonOpenRegisterNewEmail
argument_list|()
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
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
name|emailPick
operator|.
name|getItemCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|currentEmail
operator|.
name|equals
argument_list|(
name|emailPick
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|emailPick
operator|.
name|setSelectedIndex
argument_list|(
name|i
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|doRegisterNewEmail
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|save
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addressTxt
operator|.
name|addKeyPressHandler
argument_list|(
name|sbl
argument_list|)
expr_stmt|;
name|countryTxt
operator|.
name|addKeyPressHandler
argument_list|(
name|sbl
argument_list|)
expr_stmt|;
name|phoneTxt
operator|.
name|addKeyPressHandler
argument_list|(
name|sbl
argument_list|)
expr_stmt|;
name|faxTxt
operator|.
name|addKeyPressHandler
argument_list|(
name|sbl
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|hideSaveButton ()
name|void
name|hideSaveButton
parameter_list|()
block|{
name|save
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|display
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
argument_list|)
expr_stmt|;
name|emailPick
operator|.
name|clear
argument_list|()
expr_stmt|;
name|emailPick
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|registerNewEmail
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|haveAccount
operator|=
literal|false
expr_stmt|;
name|haveEmails
operator|=
literal|false
expr_stmt|;
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|myAccount
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|Account
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|Account
name|result
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isAttached
argument_list|()
condition|)
block|{
return|return;
block|}
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|haveAccount
operator|=
literal|true
expr_stmt|;
name|postLoad
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Util
operator|.
name|ACCOUNT_SEC
operator|.
name|myExternalIds
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|ExternalIdDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|ExternalIdDetail
name|detail
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isAttached
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|result
init|=
name|detail
operator|.
name|ids
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|emails
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountExternalId
name|i
range|:
name|result
control|)
block|{
if|if
condition|(
name|i
operator|.
name|getEmailAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|i
operator|.
name|getEmailAddress
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|emails
operator|.
name|add
argument_list|(
name|i
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|addrs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|emails
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|addrs
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s
range|:
name|addrs
control|)
block|{
name|emailPick
operator|.
name|addItem
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|haveEmails
operator|=
literal|true
expr_stmt|;
name|postLoad
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|postLoad ()
specifier|private
name|void
name|postLoad
parameter_list|()
block|{
if|if
condition|(
name|haveAccount
operator|&&
name|haveEmails
condition|)
block|{
if|if
condition|(
name|currentEmail
operator|!=
literal|null
condition|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|emailPick
operator|.
name|getItemCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|currentEmail
operator|.
name|equals
argument_list|(
name|emailPick
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|emailPick
operator|.
name|setSelectedIndex
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|emailPick
operator|.
name|addItem
argument_list|(
name|currentEmail
argument_list|)
expr_stmt|;
name|emailPick
operator|.
name|setSelectedIndex
argument_list|(
name|emailPick
operator|.
name|getItemCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|emailPick
operator|.
name|getItemCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|emailPick
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|emailPick
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|emailPick
operator|.
name|addItem
argument_list|(
literal|"... "
operator|+
name|Util
operator|.
name|C
operator|.
name|buttonOpenRegisterNewEmail
argument_list|()
operator|+
literal|"  "
argument_list|,
name|Util
operator|.
name|C
operator|.
name|buttonOpenRegisterNewEmail
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|emailPick
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|registerNewEmail
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|row (final Grid info, final int row, final String name, final Widget field)
specifier|private
name|void
name|row
parameter_list|(
specifier|final
name|Grid
name|info
parameter_list|,
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Widget
name|field
parameter_list|)
block|{
name|info
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|info
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|field
argument_list|)
expr_stmt|;
name|info
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
literal|"header"
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final Account userAccount)
specifier|private
name|void
name|display
parameter_list|(
specifier|final
name|Account
name|userAccount
parameter_list|)
block|{
name|currentEmail
operator|=
name|userAccount
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
name|nameTxt
operator|.
name|setText
argument_list|(
name|userAccount
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
name|displayHasContact
argument_list|(
name|userAccount
argument_list|)
expr_stmt|;
name|addressTxt
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|countryTxt
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|phoneTxt
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|faxTxt
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|save
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|displayHasContact (final Account userAccount)
specifier|private
name|void
name|displayHasContact
parameter_list|(
specifier|final
name|Account
name|userAccount
parameter_list|)
block|{
if|if
condition|(
name|userAccount
operator|.
name|isContactFiled
argument_list|()
condition|)
block|{
specifier|final
name|Timestamp
name|dt
init|=
name|userAccount
operator|.
name|getContactFiledOn
argument_list|()
decl_stmt|;
name|hasContact
operator|.
name|setText
argument_list|(
name|Util
operator|.
name|M
operator|.
name|contactOnFile
argument_list|(
operator|new
name|Date
argument_list|(
name|dt
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|hasContact
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hasContact
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doRegisterNewEmail ()
specifier|private
name|void
name|doRegisterNewEmail
parameter_list|()
block|{
specifier|final
name|AutoCenterDialogBox
name|box
init|=
operator|new
name|AutoCenterDialogBox
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|VerticalPanel
name|body
init|=
operator|new
name|VerticalPanel
argument_list|()
decl_stmt|;
specifier|final
name|NpTextBox
name|inEmail
init|=
operator|new
name|NpTextBox
argument_list|()
decl_stmt|;
name|inEmail
operator|.
name|setVisibleLength
argument_list|(
literal|60
argument_list|)
expr_stmt|;
specifier|final
name|Button
name|register
init|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonSendRegisterNewEmail
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|FormPanel
name|form
init|=
operator|new
name|FormPanel
argument_list|()
decl_stmt|;
name|form
operator|.
name|addSubmitHandler
argument_list|(
operator|new
name|FormPanel
operator|.
name|SubmitHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSubmit
parameter_list|(
specifier|final
name|SubmitEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|cancel
argument_list|()
expr_stmt|;
specifier|final
name|String
name|addr
init|=
name|inEmail
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|addr
operator|.
name|contains
argument_list|(
literal|"@"
argument_list|)
condition|)
block|{
return|return;
block|}
name|inEmail
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|register
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
name|registerEmail
argument_list|(
name|addr
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
name|VoidResult
name|result
parameter_list|)
block|{
name|box
operator|.
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
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
name|inEmail
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|register
operator|.
name|setEnabled
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
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|form
operator|.
name|setWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|register
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
name|form
operator|.
name|submit
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
operator|new
name|HTML
argument_list|(
name|Util
operator|.
name|C
operator|.
name|descRegisterNewEmail
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|inEmail
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|register
argument_list|)
expr_stmt|;
name|box
operator|.
name|setText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|titleRegisterNewEmail
argument_list|()
argument_list|)
expr_stmt|;
name|box
operator|.
name|setWidget
argument_list|(
name|form
argument_list|)
expr_stmt|;
name|box
operator|.
name|center
argument_list|()
expr_stmt|;
name|inEmail
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|doSave ()
name|void
name|doSave
parameter_list|()
block|{
specifier|final
name|String
name|newName
init|=
name|nameTxt
operator|.
name|getText
argument_list|()
decl_stmt|;
specifier|final
name|String
name|newEmail
decl_stmt|;
if|if
condition|(
name|emailPick
operator|.
name|isEnabled
argument_list|()
operator|&&
name|emailPick
operator|.
name|getSelectedIndex
argument_list|()
operator|>=
literal|0
condition|)
block|{
specifier|final
name|String
name|v
init|=
name|emailPick
operator|.
name|getValue
argument_list|(
name|emailPick
operator|.
name|getSelectedIndex
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|C
operator|.
name|buttonOpenRegisterNewEmail
argument_list|()
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
block|{
name|newEmail
operator|=
name|currentEmail
expr_stmt|;
block|}
else|else
block|{
name|newEmail
operator|=
name|v
expr_stmt|;
block|}
block|}
else|else
block|{
name|newEmail
operator|=
name|currentEmail
expr_stmt|;
block|}
specifier|final
name|ContactInformation
name|info
decl_stmt|;
if|if
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|isUseContactInfo
argument_list|()
condition|)
block|{
name|info
operator|=
operator|new
name|ContactInformation
argument_list|()
expr_stmt|;
name|info
operator|.
name|setAddress
argument_list|(
name|addressTxt
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setCountry
argument_list|(
name|countryTxt
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setPhoneNumber
argument_list|(
name|phoneTxt
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setFaxNumber
argument_list|(
name|faxTxt
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|info
operator|=
literal|null
expr_stmt|;
block|}
name|save
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|registerNewEmail
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
name|updateContact
argument_list|(
name|newName
argument_list|,
name|newEmail
argument_list|,
name|info
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|Account
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|Account
name|result
parameter_list|)
block|{
name|registerNewEmail
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|Account
name|me
init|=
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
decl_stmt|;
name|me
operator|.
name|setFullName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|me
operator|.
name|setPreferredEmail
argument_list|(
name|newEmail
argument_list|)
expr_stmt|;
name|displayHasContact
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|Gerrit
operator|.
name|refreshMenuBar
argument_list|()
expr_stmt|;
if|if
condition|(
name|parentScreen
operator|!=
literal|null
condition|)
block|{
name|parentScreen
operator|.
name|display
argument_list|(
name|me
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
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
name|save
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerNewEmail
operator|.
name|setEnabled
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
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

