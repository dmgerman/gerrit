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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|common
operator|.
name|base
operator|.
name|Strings
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
name|extensions
operator|.
name|client
operator|.
name|AccountFieldName
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
name|extensions
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|server
operator|.
name|config
operator|.
name|AuthConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
annotation|@
name|Singleton
DECL|class|DefaultRealm
specifier|public
class|class
name|DefaultRealm
extends|extends
name|AbstractRealm
block|{
DECL|field|emailExpander
specifier|private
specifier|final
name|EmailExpander
name|emailExpander
decl_stmt|;
DECL|field|emails
specifier|private
specifier|final
name|Provider
argument_list|<
name|Emails
argument_list|>
name|emails
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|DefaultRealm (EmailExpander emailExpander, Provider<Emails> emails, AuthConfig authConfig)
name|DefaultRealm
parameter_list|(
name|EmailExpander
name|emailExpander
parameter_list|,
name|Provider
argument_list|<
name|Emails
argument_list|>
name|emails
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
operator|.
name|emailExpander
operator|=
name|emailExpander
expr_stmt|;
name|this
operator|.
name|emails
operator|=
name|emails
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|allowsEdit (AccountFieldName field)
specifier|public
name|boolean
name|allowsEdit
parameter_list|(
name|AccountFieldName
name|field
parameter_list|)
block|{
if|if
condition|(
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|==
name|AuthType
operator|.
name|HTTP
condition|)
block|{
switch|switch
condition|(
name|field
condition|)
block|{
case|case
name|USER_NAME
case|:
return|return
literal|false
return|;
case|case
name|FULL_NAME
case|:
return|return
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|authConfig
operator|.
name|getHttpDisplaynameHeader
argument_list|()
argument_list|)
operator|==
literal|null
return|;
case|case
name|REGISTER_NEW_EMAIL
case|:
return|return
name|authConfig
operator|.
name|isAllowRegisterNewEmail
argument_list|()
operator|&&
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|authConfig
operator|.
name|getHttpEmailHeader
argument_list|()
argument_list|)
operator|==
literal|null
return|;
default|default:
return|return
literal|true
return|;
block|}
block|}
switch|switch
condition|(
name|field
condition|)
block|{
case|case
name|REGISTER_NEW_EMAIL
case|:
return|return
name|authConfig
operator|.
name|isAllowRegisterNewEmail
argument_list|()
return|;
case|case
name|FULL_NAME
case|:
case|case
name|USER_NAME
case|:
default|default:
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|authenticate (AuthRequest who)
specifier|public
name|AuthRequest
name|authenticate
parameter_list|(
name|AuthRequest
name|who
parameter_list|)
block|{
if|if
condition|(
name|who
operator|.
name|getEmailAddress
argument_list|()
operator|==
literal|null
operator|&&
name|who
operator|.
name|getLocalUser
argument_list|()
operator|!=
literal|null
operator|&&
name|emailExpander
operator|.
name|canExpand
argument_list|(
name|who
operator|.
name|getLocalUser
argument_list|()
argument_list|)
condition|)
block|{
name|who
operator|.
name|setEmailAddress
argument_list|(
name|emailExpander
operator|.
name|expand
argument_list|(
name|who
operator|.
name|getLocalUser
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|who
return|;
block|}
annotation|@
name|Override
DECL|method|onCreateAccount (AuthRequest who, Account account)
specifier|public
name|void
name|onCreateAccount
parameter_list|(
name|AuthRequest
name|who
parameter_list|,
name|Account
name|account
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|lookup (String accountName)
specifier|public
name|Account
operator|.
name|Id
name|lookup
parameter_list|(
name|String
name|accountName
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|emailExpander
operator|.
name|canExpand
argument_list|(
name|accountName
argument_list|)
condition|)
block|{
try|try
block|{
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|c
init|=
name|emails
operator|.
name|get
argument_list|()
operator|.
name|getAccountFor
argument_list|(
name|emailExpander
operator|.
name|expand
argument_list|(
name|accountName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|1
operator|==
name|c
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|c
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to query accounts by email"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
