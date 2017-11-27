begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
import|import static
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
operator|.
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
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
name|extensions
operator|.
name|api
operator|.
name|accounts
operator|.
name|EmailInput
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
name|common
operator|.
name|EmailInfo
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|BadRequestException
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
name|restapi
operator|.
name|MethodNotAllowedException
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
name|restapi
operator|.
name|ResourceConflictException
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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|restapi
operator|.
name|Response
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
name|restapi
operator|.
name|RestModifyView
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
name|CurrentUser
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
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|send
operator|.
name|OutgoingEmailValidator
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
name|mail
operator|.
name|send
operator|.
name|RegisterNewEmailSender
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
name|assistedinject
operator|.
name|Assisted
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CreateEmail
specifier|public
class|class
name|CreateEmail
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
argument_list|,
name|EmailInput
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CreateEmail
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (String email)
name|CreateEmail
name|create
parameter_list|(
name|String
name|email
parameter_list|)
function_decl|;
block|}
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|accountManager
specifier|private
specifier|final
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|registerNewEmailFactory
specifier|private
specifier|final
name|RegisterNewEmailSender
operator|.
name|Factory
name|registerNewEmailFactory
decl_stmt|;
DECL|field|putPreferred
specifier|private
specifier|final
name|PutPreferred
name|putPreferred
decl_stmt|;
DECL|field|email
specifier|private
specifier|final
name|String
name|email
decl_stmt|;
DECL|field|isDevMode
specifier|private
specifier|final
name|boolean
name|isDevMode
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateEmail ( Provider<CurrentUser> self, Realm realm, AuthConfig authConfig, AccountManager accountManager, RegisterNewEmailSender.Factory registerNewEmailFactory, PutPreferred putPreferred, @Assisted String email)
name|CreateEmail
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|Realm
name|realm
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
name|AccountManager
name|accountManager
parameter_list|,
name|RegisterNewEmailSender
operator|.
name|Factory
name|registerNewEmailFactory
parameter_list|,
name|PutPreferred
name|putPreferred
parameter_list|,
annotation|@
name|Assisted
name|String
name|email
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
name|this
operator|.
name|accountManager
operator|=
name|accountManager
expr_stmt|;
name|this
operator|.
name|registerNewEmailFactory
operator|=
name|registerNewEmailFactory
expr_stmt|;
name|this
operator|.
name|putPreferred
operator|=
name|putPreferred
expr_stmt|;
name|this
operator|.
name|email
operator|=
name|email
expr_stmt|;
name|this
operator|.
name|isDevMode
operator|=
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|==
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc, EmailInput input)
specifier|public
name|Response
argument_list|<
name|EmailInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|EmailInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|EmailException
throws|,
name|MethodNotAllowedException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|&&
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canModifyAccount
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to add email address"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|EmailInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|noConfirmation
operator|&&
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canModifyAccount
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to use no_confirmation"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|realm
operator|.
name|allowsEdit
argument_list|(
name|AccountFieldName
operator|.
name|REGISTER_NEW_EMAIL
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"realm does not allow adding emails"
argument_list|)
throw|;
block|}
return|return
name|apply
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|input
argument_list|)
return|;
block|}
comment|/** To be used from plugins that want to create emails without permission checks. */
DECL|method|apply (IdentifiedUser user, EmailInput input)
specifier|public
name|Response
argument_list|<
name|EmailInfo
argument_list|>
name|apply
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|EmailInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|EmailException
throws|,
name|MethodNotAllowedException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|EmailInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|email
operator|!=
literal|null
operator|&&
operator|!
name|email
operator|.
name|equals
argument_list|(
name|input
operator|.
name|email
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"email address must match URL"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|OutgoingEmailValidator
operator|.
name|isValid
argument_list|(
name|email
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"invalid email address"
argument_list|)
throw|;
block|}
name|EmailInfo
name|info
init|=
operator|new
name|EmailInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|email
operator|=
name|email
expr_stmt|;
if|if
condition|(
name|input
operator|.
name|noConfirmation
operator|||
name|isDevMode
condition|)
block|{
if|if
condition|(
name|isDevMode
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skipping email validation in developer mode"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|accountManager
operator|.
name|link
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|AuthRequest
operator|.
name|forEmail
argument_list|(
name|email
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccountException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|preferred
condition|)
block|{
name|putPreferred
operator|.
name|apply
argument_list|(
operator|new
name|AccountResource
operator|.
name|Email
argument_list|(
name|user
argument_list|,
name|email
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|info
operator|.
name|preferred
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
name|RegisterNewEmailSender
name|sender
init|=
name|registerNewEmailFactory
operator|.
name|create
argument_list|(
name|email
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sender
operator|.
name|isAllowed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"Not allowed to add email address "
operator|+
name|email
argument_list|)
throw|;
block|}
name|sender
operator|.
name|send
argument_list|()
expr_stmt|;
name|info
operator|.
name|pendingConfirmation
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EmailException
decl||
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email verification message to "
operator|+
name|email
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
return|return
name|Response
operator|.
name|created
argument_list|(
name|info
argument_list|)
return|;
block|}
block|}
end_class

end_unit

