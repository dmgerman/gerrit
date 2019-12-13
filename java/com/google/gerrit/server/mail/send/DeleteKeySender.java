begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
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
operator|.
name|send
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
name|exceptions
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|mail
operator|.
name|Address
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
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|Collections
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
DECL|class|DeleteKeySender
specifier|public
class|class
name|DeleteKeySender
extends|extends
name|OutgoingEmail
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (IdentifiedUser user, AccountSshKey sshKey)
name|DeleteKeySender
name|create
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|AccountSshKey
name|sshKey
parameter_list|)
function_decl|;
DECL|method|create (IdentifiedUser user, List<String> gpgKeyFingerprints)
name|DeleteKeySender
name|create
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|gpgKeyFingerprints
parameter_list|)
function_decl|;
block|}
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
DECL|field|gpgKeyFingerprints
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|gpgKeyFingerprints
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|DeleteKeySender ( EmailArguments args, @Assisted IdentifiedUser user, @Assisted AccountSshKey sshKey)
specifier|public
name|DeleteKeySender
parameter_list|(
name|EmailArguments
name|args
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
name|args
argument_list|,
literal|"deletekey"
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|gpgKeyFingerprints
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
name|this
operator|.
name|sshKey
operator|=
name|sshKey
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|DeleteKeySender ( EmailArguments args, @Assisted IdentifiedUser user, @Assisted List<String> gpgKeyFingerprints)
specifier|public
name|DeleteKeySender
parameter_list|(
name|EmailArguments
name|args
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
name|gpgKeyFingerprints
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|,
literal|"deletekey"
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|gpgKeyFingerprints
operator|=
name|gpgKeyFingerprints
expr_stmt|;
name|this
operator|.
name|sshKey
operator|=
literal|null
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
literal|"[Gerrit Code Review] %s Keys Deleted"
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
return|return
literal|true
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
literal|"DeleteKey"
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
literal|"DeleteKeyHtml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"gpgKeyFingerprints"
argument_list|,
name|getGpgKeyFingerprints
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
name|getUserNameEmailFor
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
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
DECL|method|getEmail ()
specifier|private
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
name|preferredEmail
argument_list|()
return|;
block|}
DECL|method|getKeyType ()
specifier|private
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
name|gpgKeyFingerprints
operator|!=
literal|null
condition|)
block|{
return|return
literal|"GPG"
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"key type is not SSH or GPG"
argument_list|)
throw|;
block|}
DECL|method|getSshKey ()
specifier|private
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
name|sshPublicKey
argument_list|()
operator|+
literal|"\n"
else|:
literal|null
return|;
block|}
DECL|method|getGpgKeyFingerprints ()
specifier|private
name|String
name|getGpgKeyFingerprints
parameter_list|()
block|{
if|if
condition|(
operator|!
name|gpgKeyFingerprints
operator|.
name|isEmpty
argument_list|()
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
name|gpgKeyFingerprints
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

