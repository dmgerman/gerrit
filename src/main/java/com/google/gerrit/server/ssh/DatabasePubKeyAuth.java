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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|PublickeyAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|session
operator|.
name|ServerSession
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_comment
comment|/**  * Authenticates by public key through {@link AccountSshKey} entities.  *<p>  * The username supplied by the client must be the user's preferred email  * address, as listed in their Account entity. Only keys listed under that  * account as authorized keys are permitted to access the account.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|DatabasePubKeyAuth
class|class
name|DatabasePubKeyAuth
implements|implements
name|PublickeyAuthenticator
block|{
DECL|field|sshKeyCache
specifier|private
specifier|final
name|SshKeyCache
name|sshKeyCache
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Inject
DECL|method|DatabasePubKeyAuth (final SshKeyCache skc, final SchemaFactory<ReviewDb> sf)
name|DatabasePubKeyAuth
parameter_list|(
specifier|final
name|SshKeyCache
name|skc
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|)
block|{
name|sshKeyCache
operator|=
name|skc
expr_stmt|;
name|schema
operator|=
name|sf
expr_stmt|;
block|}
DECL|method|hasKey (final String username, final PublicKey suppliedKey, final ServerSession session)
specifier|public
name|boolean
name|hasKey
parameter_list|(
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|PublicKey
name|suppliedKey
parameter_list|,
specifier|final
name|ServerSession
name|session
parameter_list|)
block|{
specifier|final
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|keyList
init|=
name|sshKeyCache
operator|.
name|get
argument_list|(
name|username
argument_list|)
decl_stmt|;
specifier|final
name|SshKeyCacheEntry
name|key
init|=
name|find
argument_list|(
name|keyList
argument_list|,
name|suppliedKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Double check that all of the keys are for the same user account.
comment|// This should have been true when the cache factory method loaded
comment|// the list into memory, but we want to be extra paranoid about our
comment|// security check to ensure there aren't two users sharing the same
comment|// user name on the server.
comment|//
for|for
control|(
specifier|final
name|SshKeyCacheEntry
name|otherKey
range|:
name|keyList
control|)
block|{
if|if
condition|(
operator|!
name|key
operator|.
name|getAccount
argument_list|()
operator|.
name|equals
argument_list|(
name|otherKey
operator|.
name|getAccount
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|key
operator|.
name|updateLastUsed
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|SshUtil
operator|.
name|CURRENT_ACCOUNT
argument_list|,
name|key
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|find (final Iterable<SshKeyCacheEntry> keyList, final PublicKey suppliedKey)
specifier|private
name|SshKeyCacheEntry
name|find
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|keyList
parameter_list|,
specifier|final
name|PublicKey
name|suppliedKey
parameter_list|)
block|{
for|for
control|(
specifier|final
name|SshKeyCacheEntry
name|k
range|:
name|keyList
control|)
block|{
if|if
condition|(
name|k
operator|.
name|match
argument_list|(
name|suppliedKey
argument_list|)
condition|)
block|{
return|return
name|k
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

