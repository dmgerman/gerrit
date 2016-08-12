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
DECL|package|com.google.gerrit.server.args4j
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|args4j
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|account
operator|.
name|AccountManager
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
name|AccountResolver
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|OptionDef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|OptionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|Parameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|Setter
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

begin_class
DECL|class|AccountIdHandler
specifier|public
class|class
name|AccountIdHandler
extends|extends
name|OptionHandler
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
block|{
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|accountResolver
specifier|private
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
DECL|field|accountManager
specifier|private
specifier|final
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|authType
specifier|private
specifier|final
name|AuthType
name|authType
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountIdHandler ( Provider<ReviewDb> db, AccountResolver accountResolver, AccountManager accountManager, AuthConfig authConfig, @Assisted CmdLineParser parser, @Assisted OptionDef option, @Assisted Setter<Account.Id> setter)
specifier|public
name|AccountIdHandler
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|AccountResolver
name|accountResolver
parameter_list|,
name|AccountManager
name|accountManager
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
annotation|@
name|Assisted
name|CmdLineParser
name|parser
parameter_list|,
annotation|@
name|Assisted
name|OptionDef
name|option
parameter_list|,
annotation|@
name|Assisted
name|Setter
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|setter
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|option
argument_list|,
name|setter
argument_list|)
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
name|this
operator|.
name|accountManager
operator|=
name|accountManager
expr_stmt|;
name|this
operator|.
name|authType
operator|=
name|authConfig
operator|.
name|getAuthType
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parseArguments (Parameters params)
specifier|public
name|int
name|parseArguments
parameter_list|(
name|Parameters
name|params
parameter_list|)
throws|throws
name|CmdLineException
block|{
name|String
name|token
init|=
name|params
operator|.
name|getParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
try|try
block|{
name|Account
name|a
init|=
name|accountResolver
operator|.
name|find
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|accountId
operator|=
name|a
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
switch|switch
condition|(
name|authType
condition|)
block|{
case|case
name|HTTP_LDAP
case|:
case|case
name|CLIENT_SSL_CERT_LDAP
case|:
case|case
name|LDAP
case|:
name|accountId
operator|=
name|createAccountByLdap
argument_list|(
name|token
argument_list|)
expr_stmt|;
break|break;
case|case
name|CUSTOM_EXTENSION
case|:
case|case
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
case|:
case|case
name|HTTP
case|:
case|case
name|LDAP_BIND
case|:
case|case
name|OAUTH
case|:
case|case
name|OPENID
case|:
case|case
name|OPENID_SSO
case|:
default|default:
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
literal|"user \""
operator|+
name|token
operator|+
literal|"\" not found"
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
literal|"database is down"
argument_list|)
throw|;
block|}
name|setter
operator|.
name|addValue
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
DECL|method|createAccountByLdap (String user)
specifier|private
name|Account
operator|.
name|Id
name|createAccountByLdap
parameter_list|(
name|String
name|user
parameter_list|)
throws|throws
name|CmdLineException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|user
operator|.
name|matches
argument_list|(
name|Account
operator|.
name|USER_NAME_PATTERN
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
literal|"user \""
operator|+
name|user
operator|+
literal|"\" not found"
argument_list|)
throw|;
block|}
try|try
block|{
name|AuthRequest
name|req
init|=
name|AuthRequest
operator|.
name|forUser
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|req
operator|.
name|setSkipAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|accountManager
operator|.
name|authenticate
argument_list|(
name|req
argument_list|)
operator|.
name|getAccountId
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|AccountException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
literal|"user \""
operator|+
name|user
operator|+
literal|"\" not found"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getDefaultMetaVariable ()
specifier|public
specifier|final
name|String
name|getDefaultMetaVariable
parameter_list|()
block|{
return|return
literal|"EMAIL"
return|;
block|}
block|}
end_class

end_unit

