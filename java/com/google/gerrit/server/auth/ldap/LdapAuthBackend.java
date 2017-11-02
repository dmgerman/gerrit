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
DECL|package|com.google.gerrit.server.auth.ldap
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
operator|.
name|ldap
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
name|server
operator|.
name|account
operator|.
name|AccountException
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
name|auth
operator|.
name|AuthBackend
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
name|auth
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
name|server
operator|.
name|auth
operator|.
name|AuthRequest
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
name|auth
operator|.
name|AuthUser
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
name|auth
operator|.
name|InvalidCredentialsException
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
name|auth
operator|.
name|MissingCredentialsException
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
name|auth
operator|.
name|UnknownUserException
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
name|auth
operator|.
name|UserNotAllowedException
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
name|config
operator|.
name|GerritServerConfig
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
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|DirContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
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
name|lib
operator|.
name|Config
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

begin_comment
comment|/** Implementation of AuthBackend for the LDAP authentication system. */
end_comment

begin_class
DECL|class|LdapAuthBackend
specifier|public
class|class
name|LdapAuthBackend
implements|implements
name|AuthBackend
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
name|LdapAuthBackend
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|helper
specifier|private
specifier|final
name|Helper
name|helper
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|lowerCaseUsername
specifier|private
specifier|final
name|boolean
name|lowerCaseUsername
decl_stmt|;
annotation|@
name|Inject
DECL|method|LdapAuthBackend (Helper helper, AuthConfig authConfig, @GerritServerConfig Config config)
specifier|public
name|LdapAuthBackend
parameter_list|(
name|Helper
name|helper
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|helper
operator|=
name|helper
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|lowerCaseUsername
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"ldap"
argument_list|,
literal|"localUsernameToLowerCase"
argument_list|,
literal|false
argument_list|)
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
literal|"ldap"
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
condition|)
block|{
throw|throw
operator|new
name|MissingCredentialsException
argument_list|()
throw|;
block|}
specifier|final
name|String
name|username
init|=
name|lowerCaseUsername
condition|?
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
else|:
name|req
operator|.
name|getUsername
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|DirContext
name|ctx
decl_stmt|;
if|if
condition|(
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|==
name|AuthType
operator|.
name|LDAP_BIND
condition|)
block|{
name|ctx
operator|=
name|helper
operator|.
name|authenticate
argument_list|(
name|username
argument_list|,
name|req
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ctx
operator|=
name|helper
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|Helper
operator|.
name|LdapSchema
name|schema
init|=
name|helper
operator|.
name|getSchema
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
specifier|final
name|LdapQuery
operator|.
name|Result
name|m
init|=
name|helper
operator|.
name|findAccount
argument_list|(
name|schema
argument_list|,
name|ctx
argument_list|,
name|username
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|==
name|AuthType
operator|.
name|LDAP
condition|)
block|{
comment|// We found the user account, but we need to verify
comment|// the password matches it before we can continue.
comment|//
name|helper
operator|.
name|authenticate
argument_list|(
name|m
operator|.
name|getDN
argument_list|()
argument_list|,
name|req
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|AuthUser
argument_list|(
name|AuthUser
operator|.
name|UUID
operator|.
name|create
argument_list|(
name|username
argument_list|)
argument_list|,
name|username
argument_list|)
return|;
block|}
finally|finally
block|{
try|try
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot close LDAP query handle"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|AccountException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot query LDAP to authenticate user"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|InvalidCredentialsException
argument_list|(
literal|"Cannot query LDAP for account"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot query LDAP to authenticate user"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Cannot query LDAP for account"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot authenticate server via JAAS"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Cannot query LDAP for account"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
