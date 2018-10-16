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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|ISO_8859_1
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|FileUtil
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
name|PeerDaemonUser
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
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
name|io
operator|.
name|BufferedReader
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|NoSuchFileException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
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
name|common
operator|.
name|SshException
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
name|common
operator|.
name|keyprovider
operator|.
name|KeyPairProvider
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
name|common
operator|.
name|util
operator|.
name|buffer
operator|.
name|ByteArrayBuffer
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
name|auth
operator|.
name|pubkey
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

begin_comment
comment|/** Authenticates by public key through {@link AccountSshKey} entities. */
end_comment

begin_class
DECL|class|DatabasePubKeyAuth
class|class
name|DatabasePubKeyAuth
implements|implements
name|PublickeyAuthenticator
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|sshKeyCache
specifier|private
specifier|final
name|SshKeyCacheImpl
name|sshKeyCache
decl_stmt|;
DECL|field|sshLog
specifier|private
specifier|final
name|SshLog
name|sshLog
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|peerFactory
specifier|private
specifier|final
name|PeerDaemonUser
operator|.
name|Factory
name|peerFactory
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|sshScope
specifier|private
specifier|final
name|SshScope
name|sshScope
decl_stmt|;
DECL|field|myHostKeys
specifier|private
specifier|final
name|Set
argument_list|<
name|PublicKey
argument_list|>
name|myHostKeys
decl_stmt|;
DECL|field|peerKeyCache
specifier|private
specifier|volatile
name|PeerKeyCache
name|peerKeyCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|DatabasePubKeyAuth ( SshKeyCacheImpl skc, SshLog l, IdentifiedUser.GenericFactory uf, PeerDaemonUser.Factory pf, SitePaths site, KeyPairProvider hostKeyProvider, @GerritServerConfig Config cfg, SshScope s)
name|DatabasePubKeyAuth
parameter_list|(
name|SshKeyCacheImpl
name|skc
parameter_list|,
name|SshLog
name|l
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|uf
parameter_list|,
name|PeerDaemonUser
operator|.
name|Factory
name|pf
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|KeyPairProvider
name|hostKeyProvider
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SshScope
name|s
parameter_list|)
block|{
name|sshKeyCache
operator|=
name|skc
expr_stmt|;
name|sshLog
operator|=
name|l
expr_stmt|;
name|userFactory
operator|=
name|uf
expr_stmt|;
name|peerFactory
operator|=
name|pf
expr_stmt|;
name|config
operator|=
name|cfg
expr_stmt|;
name|sshScope
operator|=
name|s
expr_stmt|;
name|myHostKeys
operator|=
name|myHostKeys
argument_list|(
name|hostKeyProvider
argument_list|)
expr_stmt|;
name|peerKeyCache
operator|=
operator|new
name|PeerKeyCache
argument_list|(
name|site
operator|.
name|peer_keys
argument_list|)
expr_stmt|;
block|}
DECL|method|myHostKeys (KeyPairProvider p)
specifier|private
specifier|static
name|Set
argument_list|<
name|PublicKey
argument_list|>
name|myHostKeys
parameter_list|(
name|KeyPairProvider
name|p
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|PublicKey
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|addPublicKey
argument_list|(
name|keys
argument_list|,
name|p
argument_list|,
name|KeyPairProvider
operator|.
name|SSH_ED25519
argument_list|)
expr_stmt|;
name|addPublicKey
argument_list|(
name|keys
argument_list|,
name|p
argument_list|,
name|KeyPairProvider
operator|.
name|ECDSA_SHA2_NISTP256
argument_list|)
expr_stmt|;
name|addPublicKey
argument_list|(
name|keys
argument_list|,
name|p
argument_list|,
name|KeyPairProvider
operator|.
name|ECDSA_SHA2_NISTP384
argument_list|)
expr_stmt|;
name|addPublicKey
argument_list|(
name|keys
argument_list|,
name|p
argument_list|,
name|KeyPairProvider
operator|.
name|ECDSA_SHA2_NISTP521
argument_list|)
expr_stmt|;
name|addPublicKey
argument_list|(
name|keys
argument_list|,
name|p
argument_list|,
name|KeyPairProvider
operator|.
name|SSH_RSA
argument_list|)
expr_stmt|;
name|addPublicKey
argument_list|(
name|keys
argument_list|,
name|p
argument_list|,
name|KeyPairProvider
operator|.
name|SSH_DSS
argument_list|)
expr_stmt|;
return|return
name|keys
return|;
block|}
DECL|method|addPublicKey ( final Collection<PublicKey> out, KeyPairProvider p, String type)
specifier|private
specifier|static
name|void
name|addPublicKey
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|PublicKey
argument_list|>
name|out
parameter_list|,
name|KeyPairProvider
name|p
parameter_list|,
name|String
name|type
parameter_list|)
block|{
specifier|final
name|KeyPair
name|pair
init|=
name|p
operator|.
name|loadKey
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|pair
operator|!=
literal|null
operator|&&
name|pair
operator|.
name|getPublic
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|add
argument_list|(
name|pair
operator|.
name|getPublic
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|authenticate (String username, PublicKey suppliedKey, ServerSession session)
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|username
parameter_list|,
name|PublicKey
name|suppliedKey
parameter_list|,
name|ServerSession
name|session
parameter_list|)
block|{
name|SshSession
name|sd
init|=
name|session
operator|.
name|getAttribute
argument_list|(
name|SshSession
operator|.
name|KEY
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|sd
operator|.
name|getUser
argument_list|()
operator|==
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|PeerDaemonUser
operator|.
name|USER_NAME
operator|.
name|equals
argument_list|(
name|username
argument_list|)
condition|)
block|{
if|if
condition|(
name|myHostKeys
operator|.
name|contains
argument_list|(
name|suppliedKey
argument_list|)
operator|||
name|getPeerKeys
argument_list|()
operator|.
name|contains
argument_list|(
name|suppliedKey
argument_list|)
condition|)
block|{
name|PeerDaemonUser
name|user
init|=
name|peerFactory
operator|.
name|create
argument_list|(
name|sd
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|SshUtil
operator|.
name|success
argument_list|(
name|username
argument_list|,
name|session
argument_list|,
name|sshScope
argument_list|,
name|sshLog
argument_list|,
name|sd
argument_list|,
name|user
argument_list|)
return|;
block|}
name|sd
operator|.
name|authenticationError
argument_list|(
name|username
argument_list|,
literal|"no-matching-key"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|config
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"userNameToLowerCase"
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|username
operator|=
name|username
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
block|}
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
name|String
name|err
decl_stmt|;
if|if
condition|(
name|keyList
operator|==
name|SshKeyCacheImpl
operator|.
name|NO_SUCH_USER
condition|)
block|{
name|err
operator|=
literal|"user-not-found"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keyList
operator|==
name|SshKeyCacheImpl
operator|.
name|NO_KEYS
condition|)
block|{
name|err
operator|=
literal|"key-list-empty"
expr_stmt|;
block|}
else|else
block|{
name|err
operator|=
literal|"no-matching-key"
expr_stmt|;
block|}
name|sd
operator|.
name|authenticationError
argument_list|(
name|username
argument_list|,
name|err
argument_list|)
expr_stmt|;
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
name|sd
operator|.
name|authenticationError
argument_list|(
name|username
argument_list|,
literal|"keys-cross-accounts"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|IdentifiedUser
name|cu
init|=
name|SshUtil
operator|.
name|createUser
argument_list|(
name|sd
argument_list|,
name|userFactory
argument_list|,
name|key
operator|.
name|getAccount
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cu
operator|.
name|getAccount
argument_list|()
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|sd
operator|.
name|authenticationError
argument_list|(
name|username
argument_list|,
literal|"inactive-account"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
name|SshUtil
operator|.
name|success
argument_list|(
name|username
argument_list|,
name|session
argument_list|,
name|sshScope
argument_list|,
name|sshLog
argument_list|,
name|sd
argument_list|,
name|cu
argument_list|)
return|;
block|}
DECL|method|getPeerKeys ()
specifier|private
name|Set
argument_list|<
name|PublicKey
argument_list|>
name|getPeerKeys
parameter_list|()
block|{
name|PeerKeyCache
name|p
init|=
name|peerKeyCache
decl_stmt|;
if|if
condition|(
operator|!
name|p
operator|.
name|isCurrent
argument_list|()
condition|)
block|{
name|p
operator|=
name|p
operator|.
name|reload
argument_list|()
expr_stmt|;
name|peerKeyCache
operator|=
name|p
expr_stmt|;
block|}
return|return
name|p
operator|.
name|keys
return|;
block|}
DECL|method|find (Iterable<SshKeyCacheEntry> keyList, PublicKey suppliedKey)
specifier|private
name|SshKeyCacheEntry
name|find
parameter_list|(
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|keyList
parameter_list|,
name|PublicKey
name|suppliedKey
parameter_list|)
block|{
for|for
control|(
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
DECL|class|PeerKeyCache
specifier|private
specifier|static
class|class
name|PeerKeyCache
block|{
DECL|field|path
specifier|private
specifier|final
name|Path
name|path
decl_stmt|;
DECL|field|modified
specifier|private
specifier|final
name|long
name|modified
decl_stmt|;
DECL|field|keys
specifier|final
name|Set
argument_list|<
name|PublicKey
argument_list|>
name|keys
decl_stmt|;
DECL|method|PeerKeyCache (Path path)
name|PeerKeyCache
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|modified
operator|=
name|FileUtil
operator|.
name|lastModified
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|keys
operator|=
name|read
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
DECL|method|read (Path path)
specifier|private
specifier|static
name|Set
argument_list|<
name|PublicKey
argument_list|>
name|read
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
try|try
init|(
name|BufferedReader
name|br
init|=
name|Files
operator|.
name|newBufferedReader
argument_list|(
name|path
argument_list|,
name|UTF_8
argument_list|)
init|)
block|{
specifier|final
name|Set
argument_list|<
name|PublicKey
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|||
name|line
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
try|try
block|{
name|byte
index|[]
name|bin
init|=
name|Base64
operator|.
name|decodeBase64
argument_list|(
name|line
operator|.
name|getBytes
argument_list|(
name|ISO_8859_1
argument_list|)
argument_list|)
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
operator|new
name|ByteArrayBuffer
argument_list|(
name|bin
argument_list|)
operator|.
name|getRawPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
decl||
name|SshException
name|e
parameter_list|)
block|{
name|logBadKey
argument_list|(
name|path
argument_list|,
name|line
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|keys
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|noFile
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot read %s"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
DECL|method|logBadKey (Path path, String line, Exception e)
specifier|private
specifier|static
name|void
name|logBadKey
parameter_list|(
name|Path
name|path
parameter_list|,
name|String
name|line
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Invalid key in %s:\n  %s"
argument_list|,
name|path
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
DECL|method|isCurrent ()
name|boolean
name|isCurrent
parameter_list|()
block|{
return|return
name|modified
operator|==
name|FileUtil
operator|.
name|lastModified
argument_list|(
name|path
argument_list|)
return|;
block|}
DECL|method|reload ()
name|PeerKeyCache
name|reload
parameter_list|()
block|{
return|return
operator|new
name|PeerKeyCache
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

