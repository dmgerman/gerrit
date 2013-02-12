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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|javax
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
name|AccountGroup
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
name|AccountGroupMember
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
name|AccountByEmailCache
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
name|GroupCache
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
name|ssh
operator|.
name|SshKeyCache
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
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSch
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|KeyPair
import|;
end_import

begin_class
DECL|class|AccountCreator
specifier|public
class|class
name|AccountCreator
block|{
DECL|field|reviewDbProvider
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|reviewDbProvider
decl_stmt|;
DECL|field|groupCache
specifier|private
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|sshKeyCache
specifier|private
name|SshKeyCache
name|sshKeyCache
decl_stmt|;
DECL|field|accountCache
specifier|private
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|byEmailCache
specifier|private
name|AccountByEmailCache
name|byEmailCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountCreator (SchemaFactory<ReviewDb> schema, GroupCache groupCache, SshKeyCache sshKeyCache, AccountCache accountCache, AccountByEmailCache byEmailCache)
name|AccountCreator
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|,
name|SshKeyCache
name|sshKeyCache
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|AccountByEmailCache
name|byEmailCache
parameter_list|)
block|{
name|reviewDbProvider
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|sshKeyCache
operator|=
name|sshKeyCache
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|byEmailCache
operator|=
name|byEmailCache
expr_stmt|;
block|}
DECL|method|create (String username, String email, String fullName, String... groups)
specifier|public
name|TestAccount
name|create
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|email
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
modifier|...
name|groups
parameter_list|)
throws|throws
name|OrmException
throws|,
name|UnsupportedEncodingException
throws|,
name|JSchException
block|{
name|ReviewDb
name|db
init|=
name|reviewDbProvider
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
name|Account
operator|.
name|Id
name|id
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|db
operator|.
name|nextAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|KeyPair
name|sshKey
init|=
name|genSshKey
argument_list|()
decl_stmt|;
name|AccountSshKey
name|key
init|=
operator|new
name|AccountSshKey
argument_list|(
operator|new
name|AccountSshKey
operator|.
name|Id
argument_list|(
name|id
argument_list|,
literal|1
argument_list|)
argument_list|,
name|publicKey
argument_list|(
name|sshKey
argument_list|,
name|email
argument_list|)
argument_list|)
decl_stmt|;
name|AccountExternalId
name|extUser
init|=
operator|new
name|AccountExternalId
argument_list|(
name|id
argument_list|,
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
argument_list|,
name|username
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|httpPass
init|=
literal|"http-pass"
decl_stmt|;
name|extUser
operator|.
name|setPassword
argument_list|(
name|httpPass
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|extUser
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|email
operator|!=
literal|null
condition|)
block|{
name|AccountExternalId
name|extMailto
init|=
operator|new
name|AccountExternalId
argument_list|(
name|id
argument_list|,
name|getEmailKey
argument_list|(
name|email
argument_list|)
argument_list|)
decl_stmt|;
name|extMailto
operator|.
name|setEmailAddress
argument_list|(
name|email
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|extMailto
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Account
name|a
init|=
operator|new
name|Account
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|a
operator|.
name|setFullName
argument_list|(
name|fullName
argument_list|)
expr_stmt|;
name|a
operator|.
name|setPreferredEmail
argument_list|(
name|email
argument_list|)
expr_stmt|;
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|n
range|:
name|groups
control|)
block|{
name|AccountGroup
operator|.
name|NameKey
name|k
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|AccountGroup
name|g
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
name|AccountGroupMember
name|m
init|=
operator|new
name|AccountGroupMember
argument_list|(
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|id
argument_list|,
name|g
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sshKeyCache
operator|.
name|evict
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|accountCache
operator|.
name|evictByUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|byEmailCache
operator|.
name|evict
argument_list|(
name|email
argument_list|)
expr_stmt|;
return|return
operator|new
name|TestAccount
argument_list|(
name|username
argument_list|,
name|email
argument_list|,
name|fullName
argument_list|,
name|sshKey
argument_list|,
name|httpPass
argument_list|)
return|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getEmailKey (String email)
specifier|private
name|AccountExternalId
operator|.
name|Key
name|getEmailKey
parameter_list|(
name|String
name|email
parameter_list|)
block|{
return|return
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_MAILTO
argument_list|,
name|email
argument_list|)
return|;
block|}
DECL|method|genSshKey ()
specifier|private
specifier|static
name|KeyPair
name|genSshKey
parameter_list|()
throws|throws
name|JSchException
block|{
name|JSch
name|jsch
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
return|return
name|KeyPair
operator|.
name|genKeyPair
argument_list|(
name|jsch
argument_list|,
name|KeyPair
operator|.
name|RSA
argument_list|)
return|;
block|}
DECL|method|publicKey (KeyPair sshKey, String comment)
specifier|private
specifier|static
name|String
name|publicKey
parameter_list|(
name|KeyPair
name|sshKey
parameter_list|,
name|String
name|comment
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|sshKey
operator|.
name|writePublicKey
argument_list|(
name|out
argument_list|,
name|comment
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|(
literal|"ASCII"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

