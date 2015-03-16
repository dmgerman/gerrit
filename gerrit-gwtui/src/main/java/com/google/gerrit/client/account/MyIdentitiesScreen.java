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
name|auth
operator|.
name|openid
operator|.
name|OpenIdUtil
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
name|common
operator|.
name|auth
operator|.
name|openid
operator|.
name|OpenIdUrls
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
name|reviewdb
operator|.
name|client
operator|.
name|AuthType
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
name|Window
operator|.
name|Location
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
name|FlexTable
operator|.
name|FlexCellFormatter
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
name|Comparator
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
DECL|class|MyIdentitiesScreen
specifier|public
class|class
name|MyIdentitiesScreen
extends|extends
name|SettingsScreen
block|{
DECL|field|identites
specifier|private
name|IdTable
name|identites
decl_stmt|;
DECL|field|deleteIdentity
specifier|private
name|Button
name|deleteIdentity
decl_stmt|;
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
name|identites
operator|=
operator|new
name|IdTable
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|identites
argument_list|)
expr_stmt|;
name|deleteIdentity
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonDeleteIdentity
argument_list|()
argument_list|)
expr_stmt|;
name|deleteIdentity
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|deleteIdentity
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
name|identites
operator|.
name|deleteChecked
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|deleteIdentity
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getAuthType
argument_list|()
operator|==
name|AuthType
operator|.
name|OPENID
condition|)
block|{
name|Button
name|linkIdentity
init|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonLinkIdentity
argument_list|()
argument_list|)
decl_stmt|;
name|linkIdentity
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
name|Location
operator|.
name|assign
argument_list|(
name|Gerrit
operator|.
name|loginRedirect
argument_list|(
name|History
operator|.
name|getToken
argument_list|()
argument_list|)
operator|+
literal|"?link"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|linkIdentity
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
name|Util
operator|.
name|ACCOUNT_SEC
operator|.
name|myExternalIds
argument_list|(
operator|new
name|ScreenLoadCallback
argument_list|<
name|List
argument_list|<
name|AccountExternalId
argument_list|>
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|preDisplay
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|result
parameter_list|)
block|{
name|identites
operator|.
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
DECL|class|IdTable
specifier|private
class|class
name|IdTable
extends|extends
name|FancyFlexTable
argument_list|<
name|AccountExternalId
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
DECL|method|IdTable ()
name|IdTable
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
name|webIdStatus
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
name|webIdEmail
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
name|webIdIdentity
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
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|keys
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
name|AccountExternalId
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
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
specifier|final
name|CheckBox
name|cb
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
name|cb
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|cb
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|keys
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
name|keys
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
name|deleteIdentity
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
name|deleteExternalIds
argument_list|(
name|keys
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
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
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|removed
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
name|AccountExternalId
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
name|removed
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
name|updateDeleteButton
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
DECL|method|updateDeleteButton ()
name|void
name|updateDeleteButton
parameter_list|()
block|{
name|int
name|off
init|=
literal|0
decl_stmt|;
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
if|if
condition|(
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|)
operator|==
literal|null
condition|)
block|{
name|off
operator|++
expr_stmt|;
block|}
else|else
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
block|}
name|deleteIdentity
operator|.
name|setVisible
argument_list|(
name|off
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|deleteIdentity
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final List<AccountExternalId> result)
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|result
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|result
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AccountExternalId
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|AccountExternalId
name|a
parameter_list|,
name|AccountExternalId
name|b
parameter_list|)
block|{
return|return
name|emailOf
argument_list|(
name|a
argument_list|)
operator|.
name|compareTo
argument_list|(
name|emailOf
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|emailOf
parameter_list|(
specifier|final
name|AccountExternalId
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|getEmailAddress
argument_list|()
operator|!=
literal|null
condition|?
name|a
operator|.
name|getEmailAddress
argument_list|()
else|:
literal|""
return|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|AccountExternalId
name|k
range|:
name|result
control|)
block|{
name|addOneId
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
name|updateDeleteButton
argument_list|()
expr_stmt|;
block|}
DECL|method|addOneId (final AccountExternalId k)
name|void
name|addOneId
parameter_list|(
specifier|final
name|AccountExternalId
name|k
parameter_list|)
block|{
if|if
condition|(
name|k
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
comment|// Don't display the username as an identity here.
return|return;
block|}
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
if|if
condition|(
name|k
operator|.
name|canDelete
argument_list|()
condition|)
block|{
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
block|}
else|else
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|k
operator|.
name|isTrusted
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
name|untrustedProvider
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
name|identityUntrustedExternalId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|k
operator|.
name|getEmailAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|k
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
name|getEmailAddress
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
literal|3
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
name|describe
argument_list|(
name|k
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
literal|2
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
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
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
name|dataCell
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
name|dataCell
argument_list|()
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
DECL|method|describe (final AccountExternalId k)
specifier|private
name|String
name|describe
parameter_list|(
specifier|final
name|AccountExternalId
name|k
parameter_list|)
block|{
if|if
condition|(
name|k
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_GERRIT
argument_list|)
condition|)
block|{
comment|// A local user identity should just be itself.
comment|//
return|return
name|k
operator|.
name|getSchemeRest
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
comment|// A local user identity should just be itself.
comment|//
return|return
name|k
operator|.
name|getSchemeRest
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_MAILTO
argument_list|)
condition|)
block|{
comment|// Describe a mailto address as just its email address, which
comment|// is already shown in the email address field.
comment|//
return|return
literal|""
return|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|isScheme
argument_list|(
literal|"https://www.google.com/accounts/o8/id"
argument_list|)
condition|)
block|{
return|return
name|OpenIdUtil
operator|.
name|C
operator|.
name|nameGoogle
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|isScheme
argument_list|(
name|OpenIdUrls
operator|.
name|URL_LAUNCHPAD
argument_list|)
condition|)
block|{
return|return
name|OpenIdUtil
operator|.
name|C
operator|.
name|nameLaunchpad
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|isScheme
argument_list|(
name|OpenIdUrls
operator|.
name|URL_YAHOO
argument_list|)
condition|)
block|{
return|return
name|OpenIdUtil
operator|.
name|C
operator|.
name|nameYahoo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|k
operator|.
name|getExternalId
argument_list|()
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

