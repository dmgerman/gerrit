begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|Joiner
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
name|EmailException
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
name|server
operator|.
name|IdentifiedUser
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
name|assistedinject
operator|.
name|Assisted
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
name|assistedinject
operator|.
name|AssistedInject
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
DECL|class|AddKeySender
specifier|public
class|class
name|AddKeySender
extends|extends
name|OutgoingEmail
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (IdentifiedUser user, AccountSshKey sshKey)
name|AddKeySender
name|create
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|AccountSshKey
name|sshKey
parameter_list|)
function_decl|;
DECL|method|create (IdentifiedUser user, List<String> gpgKey)
name|AddKeySender
name|create
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|gpgKey
parameter_list|)
function_decl|;
block|}
DECL|field|callingUser
specifier|private
specifier|final
name|IdentifiedUser
name|callingUser
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|sshKey
specifier|private
specifier|final
name|AccountSshKey
name|sshKey
decl_stmt|;
DECL|field|gpgKeys
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|gpgKeys
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|AddKeySender (EmailArguments ea, IdentifiedUser callingUser, @Assisted IdentifiedUser user, @Assisted AccountSshKey sshKey)
specifier|public
name|AddKeySender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|IdentifiedUser
name|callingUser
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|user
parameter_list|,
annotation|@
name|Assisted
name|AccountSshKey
name|sshKey
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
literal|"addkey"
argument_list|)
expr_stmt|;
name|this
operator|.
name|callingUser
operator|=
name|callingUser
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|sshKey
operator|=
name|sshKey
expr_stmt|;
name|this
operator|.
name|gpgKeys
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|AddKeySender (EmailArguments ea, IdentifiedUser callingUser, @Assisted IdentifiedUser user, @Assisted List<String> gpgKeys)
specifier|public
name|AddKeySender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|IdentifiedUser
name|callingUser
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|user
parameter_list|,
annotation|@
name|Assisted
name|List
argument_list|<
name|String
argument_list|>
name|gpgKeys
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
literal|"addkey"
argument_list|)
expr_stmt|;
name|this
operator|.
name|callingUser
operator|=
name|callingUser
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|sshKey
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|gpgKeys
operator|=
name|gpgKeys
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|setHeader
argument_list|(
literal|"Subject"
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"[Gerrit Code Review] New %s Keys Added"
argument_list|,
name|getKeyType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
operator|new
name|Address
argument_list|(
name|getEmail
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|shouldSendMessage ()
specifier|protected
name|boolean
name|shouldSendMessage
parameter_list|()
block|{
comment|/*      * Don't send an email if no keys are added, or an admin is adding a key to      * a user.      */
return|return
operator|(
name|sshKey
operator|!=
literal|null
operator|||
name|gpgKeys
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
operator|&&
operator|(
name|user
operator|.
name|equals
argument_list|(
name|callingUser
argument_list|)
operator|||
operator|!
name|callingUser
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|format ()
specifier|protected
name|void
name|format
parameter_list|()
throws|throws
name|EmailException
block|{
name|appendText
argument_list|(
name|textTemplate
argument_list|(
literal|"AddKey"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|useHtml
argument_list|()
condition|)
block|{
name|appendHtml
argument_list|(
name|soyHtmlTemplate
argument_list|(
literal|"AddKeyHtml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getEmail ()
specifier|public
name|String
name|getEmail
parameter_list|()
block|{
return|return
name|user
operator|.
name|getAccount
argument_list|()
operator|.
name|getPreferredEmail
argument_list|()
return|;
block|}
DECL|method|getUserNameEmail ()
specifier|public
name|String
name|getUserNameEmail
parameter_list|()
block|{
return|return
name|getUserNameEmailFor
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getKeyType ()
specifier|public
name|String
name|getKeyType
parameter_list|()
block|{
if|if
condition|(
name|sshKey
operator|!=
literal|null
condition|)
block|{
return|return
literal|"SSH"
return|;
block|}
elseif|else
if|if
condition|(
name|gpgKeys
operator|!=
literal|null
condition|)
block|{
return|return
literal|"GPG"
return|;
block|}
return|return
literal|"Unknown"
return|;
block|}
DECL|method|getSshKey ()
specifier|public
name|String
name|getSshKey
parameter_list|()
block|{
return|return
operator|(
name|sshKey
operator|!=
literal|null
operator|)
condition|?
name|sshKey
operator|.
name|getSshPublicKey
argument_list|()
operator|+
literal|"\n"
else|:
literal|null
return|;
block|}
DECL|method|getGpgKeys ()
specifier|public
name|String
name|getGpgKeys
parameter_list|()
block|{
if|if
condition|(
name|gpgKeys
operator|!=
literal|null
condition|)
block|{
return|return
name|Joiner
operator|.
name|on
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|join
argument_list|(
name|gpgKeys
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setupSoyContext ()
specifier|protected
name|void
name|setupSoyContext
parameter_list|()
block|{
name|super
operator|.
name|setupSoyContext
argument_list|()
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"email"
argument_list|,
name|getEmail
argument_list|()
argument_list|)
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"gpgKeys"
argument_list|,
name|getGpgKeys
argument_list|()
argument_list|)
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"keyType"
argument_list|,
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"sshKey"
argument_list|,
name|getSshKey
argument_list|()
argument_list|)
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"userNameEmail"
argument_list|,
name|getUserNameEmail
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|supportsHtml ()
specifier|protected
name|boolean
name|supportsHtml
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

