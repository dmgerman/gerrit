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
DECL|package|com.google.gerrit.gpg.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
operator|.
name|server
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
name|gpg
operator|.
name|PublicKeyStore
operator|.
name|keyIdToString
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
operator|.
name|PublicKeyStore
operator|.
name|keyToString
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
name|base
operator|.
name|Function
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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|ImmutableSet
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
name|collect
operator|.
name|Iterables
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
name|collect
operator|.
name|Lists
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
name|collect
operator|.
name|Maps
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
name|collect
operator|.
name|Sets
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
name|io
operator|.
name|BaseEncoding
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
name|common
operator|.
name|GpgKeyInfo
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
name|gpg
operator|.
name|CheckResult
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
name|gpg
operator|.
name|Fingerprint
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
name|gpg
operator|.
name|PublicKeyChecker
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
name|gpg
operator|.
name|PublicKeyStore
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
name|gpg
operator|.
name|server
operator|.
name|PostGpgKeys
operator|.
name|Input
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
name|GerritPersonIdent
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
name|AccountResource
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
name|AddKeySender
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
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|ArmoredInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyRing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|bc
operator|.
name|BcPGPObjectFactory
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
name|CommitBuilder
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
name|PersonIdent
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
name|RefUpdate
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
DECL|class|PostGpgKeys
specifier|public
class|class
name|PostGpgKeys
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|add
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|add
decl_stmt|;
DECL|field|delete
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|delete
decl_stmt|;
block|}
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|storeProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|PublicKeyStore
argument_list|>
name|storeProvider
decl_stmt|;
DECL|field|checker
specifier|private
specifier|final
name|PublicKeyChecker
name|checker
decl_stmt|;
DECL|field|addKeyFactory
specifier|private
specifier|final
name|AddKeySender
operator|.
name|Factory
name|addKeyFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|PostGpgKeys (@erritPersonIdent Provider<PersonIdent> serverIdent, Provider<ReviewDb> db, Provider<PublicKeyStore> storeProvider, PublicKeyChecker checker, AddKeySender.Factory addKeyFactory)
name|PostGpgKeys
parameter_list|(
annotation|@
name|GerritPersonIdent
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|PublicKeyStore
argument_list|>
name|storeProvider
parameter_list|,
name|PublicKeyChecker
name|checker
parameter_list|,
name|AddKeySender
operator|.
name|Factory
name|addKeyFactory
parameter_list|)
block|{
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|storeProvider
operator|=
name|storeProvider
expr_stmt|;
name|this
operator|.
name|checker
operator|=
name|checker
expr_stmt|;
name|this
operator|.
name|addKeyFactory
operator|=
name|addKeyFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc, Input input)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|GpgKeyInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|PGPException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|GpgKeys
operator|.
name|checkEnabled
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|existingExtIds
init|=
name|GpgKeys
operator|.
name|getGpgExtIds
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
try|try
init|(
name|PublicKeyStore
name|store
init|=
name|storeProvider
operator|.
name|get
argument_list|()
init|)
block|{
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|toRemove
init|=
name|readKeysToRemove
argument_list|(
name|input
argument_list|,
name|existingExtIds
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|newKeys
init|=
name|readKeysToAdd
argument_list|(
name|input
argument_list|,
name|toRemove
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|newExtIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|existingExtIds
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PGPPublicKeyRing
name|keyRing
range|:
name|newKeys
control|)
block|{
name|PGPPublicKey
name|key
init|=
name|keyRing
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
name|AccountExternalId
operator|.
name|Key
name|extIdKey
init|=
name|toExtIdKey
argument_list|(
name|key
operator|.
name|getFingerprint
argument_list|()
argument_list|)
decl_stmt|;
name|AccountExternalId
name|existing
init|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|get
argument_list|(
name|extIdKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|existing
operator|.
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"GPG key already associated with another account"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|newExtIds
operator|.
name|add
argument_list|(
operator|new
name|AccountExternalId
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|extIdKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|storeKeys
argument_list|(
name|rsrc
argument_list|,
name|newKeys
argument_list|,
name|toRemove
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|newExtIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|newExtIds
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|deleteKeys
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|toRemove
argument_list|,
operator|new
name|Function
argument_list|<
name|Fingerprint
argument_list|,
name|AccountExternalId
operator|.
name|Key
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AccountExternalId
operator|.
name|Key
name|apply
parameter_list|(
name|Fingerprint
name|fp
parameter_list|)
block|{
return|return
name|toExtIdKey
argument_list|(
name|fp
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|toJson
argument_list|(
name|newKeys
argument_list|,
name|toRemove
argument_list|)
return|;
block|}
block|}
DECL|method|readKeysToRemove (Input input, List<AccountExternalId> existingExtIds)
specifier|private
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|readKeysToRemove
parameter_list|(
name|Input
name|input
parameter_list|,
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|existingExtIds
parameter_list|)
block|{
if|if
condition|(
name|input
operator|.
name|delete
operator|==
literal|null
operator|||
name|input
operator|.
name|delete
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|fingerprints
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|input
operator|.
name|delete
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|input
operator|.
name|delete
control|)
block|{
try|try
block|{
name|fingerprints
operator|.
name|add
argument_list|(
operator|new
name|Fingerprint
argument_list|(
name|GpgKeys
operator|.
name|parseFingerprint
argument_list|(
name|id
argument_list|,
name|existingExtIds
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
comment|// Skip removal.
block|}
block|}
return|return
name|fingerprints
return|;
block|}
DECL|method|readKeysToAdd (Input input, Set<Fingerprint> toRemove)
specifier|private
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|readKeysToAdd
parameter_list|(
name|Input
name|input
parameter_list|,
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|toRemove
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|IOException
block|{
if|if
condition|(
name|input
operator|.
name|add
operator|==
literal|null
operator|||
name|input
operator|.
name|add
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|keyRings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|input
operator|.
name|add
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|armored
range|:
name|input
operator|.
name|add
control|)
block|{
try|try
init|(
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|armored
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
init|;
name|ArmoredInputStream
name|ain
operator|=
operator|new
name|ArmoredInputStream
argument_list|(
name|in
argument_list|)
init|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|Object
argument_list|>
name|objs
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
operator|new
name|BcPGPObjectFactory
argument_list|(
name|ain
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|||
operator|!
operator|(
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|PGPPublicKeyRing
operator|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Expected exactly one PUBLIC KEY BLOCK"
argument_list|)
throw|;
block|}
name|PGPPublicKeyRing
name|keyRing
init|=
operator|(
name|PGPPublicKeyRing
operator|)
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|toRemove
operator|.
name|contains
argument_list|(
operator|new
name|Fingerprint
argument_list|(
name|keyRing
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getFingerprint
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Cannot both add and delete key: "
operator|+
name|keyToString
argument_list|(
name|keyRing
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|keyRings
operator|.
name|add
argument_list|(
name|keyRing
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|keyRings
return|;
block|}
DECL|method|storeKeys (AccountResource rsrc, List<PGPPublicKeyRing> keyRings, Set<Fingerprint> toRemove)
specifier|private
name|void
name|storeKeys
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|keyRings
parameter_list|,
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|toRemove
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|PGPException
throws|,
name|IOException
block|{
try|try
init|(
name|PublicKeyStore
name|store
init|=
name|storeProvider
operator|.
name|get
argument_list|()
init|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|addedKeys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PGPPublicKeyRing
name|keyRing
range|:
name|keyRings
control|)
block|{
name|PGPPublicKey
name|key
init|=
name|keyRing
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
name|CheckResult
name|result
init|=
name|checker
operator|.
name|check
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isOk
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Problems with public key %s:\n%s"
argument_list|,
name|keyToString
argument_list|(
name|key
argument_list|)
argument_list|,
name|Joiner
operator|.
name|on
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|join
argument_list|(
name|result
operator|.
name|getProblems
argument_list|()
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
name|addedKeys
operator|.
name|add
argument_list|(
name|PublicKeyStore
operator|.
name|keyToString
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|keyRing
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Fingerprint
name|fp
range|:
name|toRemove
control|)
block|{
name|store
operator|.
name|remove
argument_list|(
name|fp
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|PersonIdent
name|committer
init|=
name|serverIdent
operator|.
name|get
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|newCommitterIdent
argument_list|(
name|committer
operator|.
name|getWhen
argument_list|()
argument_list|,
name|committer
operator|.
name|getTimeZone
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|committer
argument_list|)
expr_stmt|;
name|RefUpdate
operator|.
name|Result
name|saveResult
init|=
name|store
operator|.
name|save
argument_list|(
name|cb
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|saveResult
condition|)
block|{
case|case
name|NEW
case|:
case|case
name|FAST_FORWARD
case|:
case|case
name|FORCED
case|:
try|try
block|{
name|addKeyFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|addedKeys
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EmailException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send GPG key added message to "
operator|+
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccount
argument_list|()
operator|.
name|getPreferredEmail
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|NO_CHANGE
case|:
break|break;
default|default:
comment|// TODO(dborowitz): Backoff and retry on LOCK_FAILURE.
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Failed to save public keys: "
operator|+
name|saveResult
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|toExtIdKey (byte[] fp)
specifier|private
specifier|final
name|AccountExternalId
operator|.
name|Key
name|toExtIdKey
parameter_list|(
name|byte
index|[]
name|fp
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
name|SCHEME_GPGKEY
argument_list|,
name|BaseEncoding
operator|.
name|base16
argument_list|()
operator|.
name|encode
argument_list|(
name|fp
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toJson ( Collection<PGPPublicKeyRing> keys, Set<Fingerprint> deleted)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|GpgKeyInfo
argument_list|>
name|toJson
parameter_list|(
name|Collection
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|keys
parameter_list|,
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|deleted
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|GpgKeyInfo
argument_list|>
name|infos
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|keys
operator|.
name|size
argument_list|()
operator|+
name|deleted
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PGPPublicKeyRing
name|keyRing
range|:
name|keys
control|)
block|{
name|GpgKeyInfo
name|info
init|=
name|GpgKeys
operator|.
name|toJson
argument_list|(
name|keyRing
argument_list|)
decl_stmt|;
name|infos
operator|.
name|put
argument_list|(
name|info
operator|.
name|id
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|info
operator|.
name|id
operator|=
literal|null
expr_stmt|;
block|}
for|for
control|(
name|Fingerprint
name|fp
range|:
name|deleted
control|)
block|{
name|infos
operator|.
name|put
argument_list|(
name|keyIdToString
argument_list|(
name|fp
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
operator|new
name|GpgKeyInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|infos
return|;
block|}
block|}
end_class

end_unit

