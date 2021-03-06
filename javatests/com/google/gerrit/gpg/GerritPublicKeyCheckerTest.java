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
DECL|package|com.google.gerrit.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|GerritPublicKeyChecker
operator|.
name|toExtIdKey
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
operator|.
name|testing
operator|.
name|TestKeys
operator|.
name|validKeyWithSecondUserId
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
name|testing
operator|.
name|TestTrustKeys
operator|.
name|keyA
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
name|testing
operator|.
name|TestTrustKeys
operator|.
name|keyB
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
name|testing
operator|.
name|TestTrustKeys
operator|.
name|keyC
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
name|testing
operator|.
name|TestTrustKeys
operator|.
name|keyD
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
name|testing
operator|.
name|TestTrustKeys
operator|.
name|keyE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefUpdate
operator|.
name|Result
operator|.
name|FAST_FORWARD
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefUpdate
operator|.
name|Result
operator|.
name|FORCED
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefUpdate
operator|.
name|Result
operator|.
name|NEW
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
name|Iterators
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
name|entities
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
name|extensions
operator|.
name|common
operator|.
name|GpgKeyInfo
operator|.
name|Status
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
name|testing
operator|.
name|TestKey
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
name|lifecycle
operator|.
name|LifecycleManager
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
name|ServerInitiated
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
name|AccountsUpdate
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
name|account
operator|.
name|externalids
operator|.
name|ExternalId
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
name|schema
operator|.
name|SchemaCreator
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|testing
operator|.
name|InMemoryModule
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
name|Guice
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
name|Injector
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
name|Arrays
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
name|eclipse
operator|.
name|jgit
operator|.
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|DfsRepositoryDescription
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
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|InMemoryRepository
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
name|Config
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
name|Repository
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
name|transport
operator|.
name|PushCertificateIdent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/** Unit tests for {@link GerritPublicKeyChecker}. */
end_comment

begin_class
DECL|class|GerritPublicKeyCheckerTest
specifier|public
class|class
name|GerritPublicKeyCheckerTest
block|{
DECL|field|accountsUpdateProvider
annotation|@
name|Inject
annotation|@
name|ServerInitiated
specifier|private
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
decl_stmt|;
DECL|field|accountManager
annotation|@
name|Inject
specifier|private
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|checkerFactory
annotation|@
name|Inject
specifier|private
name|GerritPublicKeyChecker
operator|.
name|Factory
name|checkerFactory
decl_stmt|;
DECL|field|userFactory
annotation|@
name|Inject
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|schemaCreator
annotation|@
name|Inject
specifier|private
name|SchemaCreator
name|schemaCreator
decl_stmt|;
DECL|field|requestContext
annotation|@
name|Inject
specifier|private
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|lifecycle
specifier|private
name|LifecycleManager
name|lifecycle
decl_stmt|;
DECL|field|userId
specifier|private
name|Account
operator|.
name|Id
name|userId
decl_stmt|;
DECL|field|user
specifier|private
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|storeRepo
specifier|private
name|Repository
name|storeRepo
decl_stmt|;
DECL|field|store
specifier|private
name|PublicKeyStore
name|store
decl_stmt|;
annotation|@
name|Before
DECL|method|setUpInjector ()
specifier|public
name|void
name|setUpInjector
parameter_list|()
throws|throws
name|Exception
block|{
name|Config
name|cfg
init|=
name|InMemoryModule
operator|.
name|newDefaultConfig
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"maxTrustDepth"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setStringList
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"trustedKey"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|Fingerprint
operator|.
name|toString
argument_list|(
name|keyB
argument_list|()
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getFingerprint
argument_list|()
argument_list|)
argument_list|,
name|Fingerprint
operator|.
name|toString
argument_list|(
name|keyD
argument_list|()
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getFingerprint
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|InMemoryModule
argument_list|(
name|cfg
argument_list|)
argument_list|)
decl_stmt|;
name|lifecycle
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
name|lifecycle
operator|.
name|add
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|lifecycle
operator|.
name|start
argument_list|()
expr_stmt|;
name|schemaCreator
operator|.
name|create
argument_list|()
expr_stmt|;
name|userId
operator|=
name|accountManager
operator|.
name|authenticate
argument_list|(
name|AuthRequest
operator|.
name|forUser
argument_list|(
literal|"user"
argument_list|)
argument_list|)
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
comment|// Note: does not match any key in TestKeys.
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|update
argument_list|(
literal|"Set Preferred Email"
argument_list|,
name|userId
argument_list|,
name|u
lambda|->
name|u
operator|.
name|setPreferredEmail
argument_list|(
literal|"user@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|user
operator|=
name|reloadUser
argument_list|()
expr_stmt|;
name|requestContext
operator|.
name|setContext
argument_list|(
parameter_list|()
lambda|->
name|user
argument_list|)
expr_stmt|;
name|storeRepo
operator|=
operator|new
name|InMemoryRepository
argument_list|(
operator|new
name|DfsRepositoryDescription
argument_list|(
literal|"repo"
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|=
operator|new
name|PublicKeyStore
argument_list|(
name|storeRepo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|store
operator|.
name|close
argument_list|()
expr_stmt|;
name|storeRepo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|addUser (String name)
specifier|private
name|IdentifiedUser
name|addUser
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|AuthRequest
name|req
init|=
name|AuthRequest
operator|.
name|forUser
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Account
operator|.
name|Id
name|id
init|=
name|accountManager
operator|.
name|authenticate
argument_list|(
name|req
argument_list|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
return|return
name|userFactory
operator|.
name|create
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|reloadUser ()
specifier|private
name|IdentifiedUser
name|reloadUser
parameter_list|()
block|{
name|user
operator|=
name|userFactory
operator|.
name|create
argument_list|(
name|userId
argument_list|)
expr_stmt|;
return|return
name|user
return|;
block|}
annotation|@
name|After
DECL|method|tearDownInjector ()
specifier|public
name|void
name|tearDownInjector
parameter_list|()
block|{
if|if
condition|(
name|lifecycle
operator|!=
literal|null
condition|)
block|{
name|lifecycle
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|defaultGpgCertificationMatchesEmail ()
specifier|public
name|void
name|defaultGpgCertificationMatchesEmail
parameter_list|()
throws|throws
name|Exception
block|{
name|TestKey
name|key
init|=
name|validKeyWithSecondUserId
argument_list|()
decl_stmt|;
name|PublicKeyChecker
name|checker
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
operator|.
name|disableTrust
argument_list|()
decl_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|key
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key must contain a valid certification for one of the following "
operator|+
literal|"identities:\n"
operator|+
literal|"  gerrit:user\n"
operator|+
literal|"  username:user"
argument_list|)
expr_stmt|;
name|addExternalId
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|,
literal|"test5@example.com"
argument_list|)
expr_stmt|;
name|checker
operator|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
operator|.
name|disableTrust
argument_list|()
expr_stmt|;
name|assertNoProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|key
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|defaultGpgCertificationDoesNotMatchEmail ()
specifier|public
name|void
name|defaultGpgCertificationDoesNotMatchEmail
parameter_list|()
throws|throws
name|Exception
block|{
name|addExternalId
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|,
literal|"nobody@example.com"
argument_list|)
expr_stmt|;
name|PublicKeyChecker
name|checker
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
operator|.
name|disableTrust
argument_list|()
decl_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|validKeyWithSecondUserId
argument_list|()
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key must contain a valid certification for one of the following "
operator|+
literal|"identities:\n"
operator|+
literal|"  gerrit:user\n"
operator|+
literal|"  nobody@example.com\n"
operator|+
literal|"  test:test\n"
operator|+
literal|"  username:user"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|manualCertificationMatchesExternalId ()
specifier|public
name|void
name|manualCertificationMatchesExternalId
parameter_list|()
throws|throws
name|Exception
block|{
name|addExternalId
argument_list|(
literal|"foo"
argument_list|,
literal|"myId"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|PublicKeyChecker
name|checker
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
operator|.
name|disableTrust
argument_list|()
decl_stmt|;
name|assertNoProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|validKeyWithSecondUserId
argument_list|()
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|manualCertificationDoesNotMatchExternalId ()
specifier|public
name|void
name|manualCertificationDoesNotMatchExternalId
parameter_list|()
throws|throws
name|Exception
block|{
name|addExternalId
argument_list|(
literal|"foo"
argument_list|,
literal|"otherId"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|PublicKeyChecker
name|checker
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
operator|.
name|disableTrust
argument_list|()
decl_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|validKeyWithSecondUserId
argument_list|()
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key must contain a valid certification for one of the following "
operator|+
literal|"identities:\n"
operator|+
literal|"  foo:otherId\n"
operator|+
literal|"  gerrit:user\n"
operator|+
literal|"  username:user"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noExternalIds ()
specifier|public
name|void
name|noExternalIds
parameter_list|()
throws|throws
name|Exception
block|{
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|update
argument_list|(
literal|"Delete External IDs"
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
parameter_list|(
name|a
parameter_list|,
name|u
parameter_list|)
lambda|->
name|u
operator|.
name|deleteExternalIds
argument_list|(
name|a
operator|.
name|externalIds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|reloadUser
argument_list|()
expr_stmt|;
name|TestKey
name|key
init|=
name|validKeyWithSecondUserId
argument_list|()
decl_stmt|;
name|PublicKeyChecker
name|checker
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
operator|.
name|disableTrust
argument_list|()
decl_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|key
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"No identities found for user; check http://test/settings#Identities"
argument_list|)
expr_stmt|;
name|checker
operator|=
name|checkerFactory
operator|.
name|create
argument_list|()
operator|.
name|setStore
argument_list|(
name|store
argument_list|)
operator|.
name|disableTrust
argument_list|()
expr_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|key
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key is not associated with any users"
argument_list|)
expr_stmt|;
name|insertExtId
argument_list|(
name|ExternalId
operator|.
name|create
argument_list|(
name|toExtIdKey
argument_list|(
name|key
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|key
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"No identities found for user"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkValidTrustChainAndCorrectExternalIds ()
specifier|public
name|void
name|checkValidTrustChainAndCorrectExternalIds
parameter_list|()
throws|throws
name|Exception
block|{
comment|// A---Bx
comment|//  \
comment|//   \---C---D
comment|//        \
comment|//         \---Ex
comment|//
comment|// The server ultimately trusts B and D.
comment|// D and E trust C to be a valid introducer of depth 2.
name|IdentifiedUser
name|userB
init|=
name|addUser
argument_list|(
literal|"userB"
argument_list|)
decl_stmt|;
name|TestKey
name|keyA
init|=
name|add
argument_list|(
name|keyA
argument_list|()
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|TestKey
name|keyB
init|=
name|add
argument_list|(
name|keyB
argument_list|()
argument_list|,
name|userB
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|keyC
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userC"
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|keyD
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userD"
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|keyE
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userE"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Checker for A, checking A.
name|PublicKeyChecker
name|checkerA
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
decl_stmt|;
name|assertNoProblems
argument_list|(
name|checkerA
operator|.
name|check
argument_list|(
name|keyA
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Checker for B, checking B. Trust chain and IDs are correct, so the only
comment|// problem is with the key itself.
name|PublicKeyChecker
name|checkerB
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|userB
argument_list|,
name|store
argument_list|)
decl_stmt|;
name|assertProblems
argument_list|(
name|checkerB
operator|.
name|check
argument_list|(
name|keyB
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key is expired"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkWithValidKeyButWrongExpectedUserInChecker ()
specifier|public
name|void
name|checkWithValidKeyButWrongExpectedUserInChecker
parameter_list|()
throws|throws
name|Exception
block|{
comment|// A---Bx
comment|//  \
comment|//   \---C---D
comment|//        \
comment|//         \---Ex
comment|//
comment|// The server ultimately trusts B and D.
comment|// D and E trust C to be a valid introducer of depth 2.
name|IdentifiedUser
name|userB
init|=
name|addUser
argument_list|(
literal|"userB"
argument_list|)
decl_stmt|;
name|TestKey
name|keyA
init|=
name|add
argument_list|(
name|keyA
argument_list|()
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|TestKey
name|keyB
init|=
name|add
argument_list|(
name|keyB
argument_list|()
argument_list|,
name|userB
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|keyC
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userC"
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|keyD
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userD"
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|keyE
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userE"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Checker for A, checking B.
name|PublicKeyChecker
name|checkerA
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
decl_stmt|;
name|assertProblems
argument_list|(
name|checkerA
operator|.
name|check
argument_list|(
name|keyB
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key is expired"
argument_list|,
literal|"Key must contain a valid certification for one of the following"
operator|+
literal|" identities:\n"
operator|+
literal|"  gerrit:user\n"
operator|+
literal|"  mailto:testa@example.com\n"
operator|+
literal|"  testa@example.com\n"
operator|+
literal|"  username:user"
argument_list|)
expr_stmt|;
comment|// Checker for B, checking A.
name|PublicKeyChecker
name|checkerB
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|userB
argument_list|,
name|store
argument_list|)
decl_stmt|;
name|assertProblems
argument_list|(
name|checkerB
operator|.
name|check
argument_list|(
name|keyA
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key must contain a valid certification for one of the following"
operator|+
literal|" identities:\n"
operator|+
literal|"  gerrit:userB\n"
operator|+
literal|"  mailto:testb@example.com\n"
operator|+
literal|"  testb@example.com\n"
operator|+
literal|"  username:userB"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkTrustChainWithExpiredKey ()
specifier|public
name|void
name|checkTrustChainWithExpiredKey
parameter_list|()
throws|throws
name|Exception
block|{
comment|// A---Bx
comment|//
comment|// The server ultimately trusts B.
name|TestKey
name|keyA
init|=
name|add
argument_list|(
name|keyA
argument_list|()
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|TestKey
name|keyB
init|=
name|add
argument_list|(
name|keyB
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userB"
argument_list|)
argument_list|)
decl_stmt|;
name|PublicKeyChecker
name|checker
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
decl_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|keyA
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|OK
argument_list|,
literal|"No path to a trusted key"
argument_list|,
literal|"Certification by "
operator|+
name|keyToString
argument_list|(
name|keyB
operator|.
name|getPublicKey
argument_list|()
argument_list|)
operator|+
literal|" is valid, but key is not trusted"
argument_list|,
literal|"Key D24FE467 used for certification is not in store"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkTrustChainUsingCheckerWithoutExpectedKey ()
specifier|public
name|void
name|checkTrustChainUsingCheckerWithoutExpectedKey
parameter_list|()
throws|throws
name|Exception
block|{
comment|// A---Bx
comment|//  \
comment|//   \---C---D
comment|//        \
comment|//         \---Ex
comment|//
comment|// The server ultimately trusts B and D.
comment|// D and E trust C to be a valid introducer of depth 2.
name|TestKey
name|keyA
init|=
name|add
argument_list|(
name|keyA
argument_list|()
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|TestKey
name|keyB
init|=
name|add
argument_list|(
name|keyB
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userB"
argument_list|)
argument_list|)
decl_stmt|;
name|TestKey
name|keyC
init|=
name|add
argument_list|(
name|keyC
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userC"
argument_list|)
argument_list|)
decl_stmt|;
name|TestKey
name|keyD
init|=
name|add
argument_list|(
name|keyD
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userD"
argument_list|)
argument_list|)
decl_stmt|;
name|TestKey
name|keyE
init|=
name|add
argument_list|(
name|keyE
argument_list|()
argument_list|,
name|addUser
argument_list|(
literal|"userE"
argument_list|)
argument_list|)
decl_stmt|;
comment|// This checker can check any key, so the only problems come from issues
comment|// with the keys themselves, not having invalid user IDs.
name|PublicKeyChecker
name|checker
init|=
name|checkerFactory
operator|.
name|create
argument_list|()
operator|.
name|setStore
argument_list|(
name|store
argument_list|)
decl_stmt|;
name|assertNoProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|keyA
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|keyB
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key is expired"
argument_list|)
expr_stmt|;
name|assertNoProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|keyC
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNoProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|keyD
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertProblems
argument_list|(
name|checker
operator|.
name|check
argument_list|(
name|keyE
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|BAD
argument_list|,
literal|"Key is expired"
argument_list|,
literal|"No path to a trusted key"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|keyLaterInTrustChainMissingUserId ()
specifier|public
name|void
name|keyLaterInTrustChainMissingUserId
parameter_list|()
throws|throws
name|Exception
block|{
comment|// A---Bx
comment|//  \
comment|//   \---C
comment|//
comment|// The server ultimately trusts B.
comment|// C signed A's key but is not in the store.
name|TestKey
name|keyA
init|=
name|add
argument_list|(
name|keyA
argument_list|()
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|PGPPublicKeyRing
name|keyRingB
init|=
name|keyB
argument_list|()
operator|.
name|getPublicKeyRing
argument_list|()
decl_stmt|;
name|PGPPublicKey
name|keyB
init|=
name|keyRingB
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
name|keyB
operator|=
name|PGPPublicKey
operator|.
name|removeCertification
argument_list|(
name|keyB
argument_list|,
name|keyB
operator|.
name|getUserIDs
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|keyRingB
operator|=
name|PGPPublicKeyRing
operator|.
name|insertPublicKey
argument_list|(
name|keyRingB
argument_list|,
name|keyB
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|keyRingB
argument_list|,
name|addUser
argument_list|(
literal|"userB"
argument_list|)
argument_list|)
expr_stmt|;
name|PublicKeyChecker
name|checkerA
init|=
name|checkerFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|store
argument_list|)
decl_stmt|;
name|assertProblems
argument_list|(
name|checkerA
operator|.
name|check
argument_list|(
name|keyA
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|OK
argument_list|,
literal|"No path to a trusted key"
argument_list|,
literal|"Certification by "
operator|+
name|keyToString
argument_list|(
name|keyB
argument_list|)
operator|+
literal|" is valid, but key is not trusted"
argument_list|,
literal|"Key D24FE467 used for certification is not in store"
argument_list|)
expr_stmt|;
block|}
DECL|method|add (PGPPublicKeyRing kr, IdentifiedUser user)
specifier|private
name|void
name|add
parameter_list|(
name|PGPPublicKeyRing
name|kr
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
throws|throws
name|Exception
block|{
name|Account
operator|.
name|Id
name|id
init|=
name|user
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ExternalId
argument_list|>
name|newExtIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|newExtIds
operator|.
name|add
argument_list|(
name|ExternalId
operator|.
name|create
argument_list|(
name|toExtIdKey
argument_list|(
name|kr
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|userId
init|=
name|Iterators
operator|.
name|getOnlyElement
argument_list|(
name|kr
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getUserIDs
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|userId
operator|!=
literal|null
condition|)
block|{
name|String
name|email
init|=
name|PushCertificateIdent
operator|.
name|parse
argument_list|(
name|userId
argument_list|)
operator|.
name|getEmailAddress
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|email
argument_list|)
operator|.
name|contains
argument_list|(
literal|"@"
argument_list|)
expr_stmt|;
name|newExtIds
operator|.
name|add
argument_list|(
name|ExternalId
operator|.
name|createEmail
argument_list|(
name|id
argument_list|,
name|email
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|store
operator|.
name|add
argument_list|(
name|kr
argument_list|)
expr_stmt|;
name|PersonIdent
name|ident
init|=
operator|new
name|PersonIdent
argument_list|(
literal|"A U Thor"
argument_list|,
literal|"author@example.com"
argument_list|)
decl_stmt|;
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|store
operator|.
name|save
argument_list|(
name|cb
argument_list|)
argument_list|)
operator|.
name|isAnyOf
argument_list|(
name|NEW
argument_list|,
name|FAST_FORWARD
argument_list|,
name|FORCED
argument_list|)
expr_stmt|;
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|update
argument_list|(
literal|"Add External IDs"
argument_list|,
name|id
argument_list|,
name|u
lambda|->
name|u
operator|.
name|addExternalIds
argument_list|(
name|newExtIds
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|add (TestKey k, IdentifiedUser user)
specifier|private
name|TestKey
name|add
parameter_list|(
name|TestKey
name|k
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
throws|throws
name|Exception
block|{
name|add
argument_list|(
name|k
operator|.
name|getPublicKeyRing
argument_list|()
argument_list|,
name|user
argument_list|)
expr_stmt|;
return|return
name|k
return|;
block|}
DECL|method|assertProblems ( CheckResult result, Status expectedStatus, String first, String... rest)
specifier|private
name|void
name|assertProblems
parameter_list|(
name|CheckResult
name|result
parameter_list|,
name|Status
name|expectedStatus
parameter_list|,
name|String
name|first
parameter_list|,
name|String
modifier|...
name|rest
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|expectedProblems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedProblems
operator|.
name|add
argument_list|(
name|first
argument_list|)
expr_stmt|;
name|expectedProblems
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|rest
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedStatus
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|getProblems
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expectedProblems
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|assertNoProblems (CheckResult result)
specifier|private
name|void
name|assertNoProblems
parameter_list|(
name|CheckResult
name|result
parameter_list|)
block|{
name|assertThat
argument_list|(
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|Status
operator|.
name|TRUSTED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|getProblems
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|addExternalId (String scheme, String id, String email)
specifier|private
name|void
name|addExternalId
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|email
parameter_list|)
throws|throws
name|Exception
block|{
name|insertExtId
argument_list|(
name|ExternalId
operator|.
name|createWithEmail
argument_list|(
name|scheme
argument_list|,
name|id
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|email
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|insertExtId (ExternalId extId)
specifier|private
name|void
name|insertExtId
parameter_list|(
name|ExternalId
name|extId
parameter_list|)
throws|throws
name|Exception
block|{
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|update
argument_list|(
literal|"Add External ID"
argument_list|,
name|extId
operator|.
name|accountId
argument_list|()
argument_list|,
name|u
lambda|->
name|u
operator|.
name|addExternalId
argument_list|(
name|extId
argument_list|)
argument_list|)
expr_stmt|;
name|reloadUser
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

