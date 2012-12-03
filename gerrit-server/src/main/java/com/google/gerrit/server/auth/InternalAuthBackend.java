begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.auth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
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
name|server
operator|.
name|account
operator|.
name|AccountCache
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
name|AccountState
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|InternalAuthBackend
specifier|public
class|class
name|InternalAuthBackend
implements|implements
name|AuthBackend
block|{
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|InternalAuthBackend (AccountCache accountCache, AuthConfig authConfig)
name|InternalAuthBackend
parameter_list|(
name|AccountCache
name|accountCache
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
operator|.
name|accountCache
operator|=
name|accountCache
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
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
literal|"gerrit"
return|;
block|}
annotation|@
name|Override
DECL|method|authenticate (AuthRequest req)
specifier|public
name|AuthUser
name|authenticate
parameter_list|(
name|AuthRequest
name|req
parameter_list|)
throws|throws
name|MissingCredentialsException
throws|,
name|InvalidCredentialsException
throws|,
name|UnknownUserException
throws|,
name|UserNotAllowedException
throws|,
name|AuthException
block|{
if|if
condition|(
name|req
operator|.
name|getUsername
argument_list|()
operator|==
literal|null
operator|||
name|req
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MissingCredentialsException
argument_list|()
throw|;
block|}
name|String
name|username
decl_stmt|;
if|if
condition|(
name|authConfig
operator|.
name|isUserNameToLowerCase
argument_list|()
condition|)
block|{
name|username
operator|=
name|req
operator|.
name|getUsername
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|username
operator|=
name|req
operator|.
name|getUsername
argument_list|()
expr_stmt|;
block|}
specifier|final
name|AccountState
name|who
init|=
name|accountCache
operator|.
name|getByUsername
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|who
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnknownUserException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|who
operator|.
name|getAccount
argument_list|()
operator|.
name|isActive
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UserNotAllowedException
argument_list|(
literal|"Authentication failed for "
operator|+
name|username
operator|+
literal|": account inactive or not provisioned in Gerrit"
argument_list|)
throw|;
block|}
name|req
operator|.
name|checkPassword
argument_list|(
name|who
operator|.
name|getPassword
argument_list|(
name|username
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|AuthUser
argument_list|(
operator|new
name|AuthUser
operator|.
name|UUID
argument_list|(
name|username
argument_list|)
argument_list|,
name|username
argument_list|)
return|;
block|}
block|}
end_class

end_unit

