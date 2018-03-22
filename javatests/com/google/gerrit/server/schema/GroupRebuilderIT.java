begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|common
operator|.
name|truth
operator|.
name|TruthJUnit
operator|.
name|assume
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
name|extensions
operator|.
name|common
operator|.
name|testing
operator|.
name|CommitInfoSubject
operator|.
name|assertThat
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
name|gerrit
operator|.
name|common
operator|.
name|TimeUtil
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
name|GerritApi
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
name|AccountInput
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
name|AccountInfo
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
name|CommitInfo
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
name|GroupInfo
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
name|RestApiException
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
name|AccountGroupById
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
name|AccountGroupByIdAud
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
name|RefNames
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
name|GroupBackend
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
name|AllUsersName
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
name|GerritServerId
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
name|GerritServerIdProvider
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
name|git
operator|.
name|CommitUtil
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
name|git
operator|.
name|GitRepositoryManager
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
name|group
operator|.
name|SystemGroupBackend
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
name|group
operator|.
name|db
operator|.
name|AuditLogFormatter
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
name|notedb
operator|.
name|GroupsMigration
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
name|GerritBaseTests
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
name|InMemoryTestEnvironment
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
name|TestTimeUtil
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
name|TestTimeUtil
operator|.
name|TempClockStep
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|concurrent
operator|.
name|TimeUnit
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
name|ObjectId
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
name|Ref
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevSort
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
name|revwalk
operator|.
name|RevWalk
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
name|Rule
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

begin_class
DECL|class|GroupRebuilderIT
specifier|public
class|class
name|GroupRebuilderIT
extends|extends
name|GerritBaseTests
block|{
DECL|method|createConfigWithServerId ()
specifier|private
specifier|static
name|Config
name|createConfigWithServerId
parameter_list|()
block|{
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|config
operator|.
name|setString
argument_list|(
name|GerritServerIdProvider
operator|.
name|SECTION
argument_list|,
literal|null
argument_list|,
name|GerritServerIdProvider
operator|.
name|KEY
argument_list|,
literal|"1234567"
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
annotation|@
name|Rule
DECL|field|testEnv
specifier|public
name|InMemoryTestEnvironment
name|testEnv
init|=
operator|new
name|InMemoryTestEnvironment
argument_list|(
name|GroupRebuilderIT
operator|::
name|createConfigWithServerId
argument_list|)
decl_stmt|;
DECL|field|migration
annotation|@
name|Inject
specifier|private
name|GroupsMigration
name|migration
decl_stmt|;
DECL|field|gApi
annotation|@
name|Inject
specifier|private
name|GerritApi
name|gApi
decl_stmt|;
DECL|field|db
annotation|@
name|Inject
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
annotation|@
name|Inject
specifier|private
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|currentUser
annotation|@
name|Inject
specifier|private
name|IdentifiedUser
name|currentUser
decl_stmt|;
DECL|field|serverId
annotation|@
name|Inject
specifier|private
annotation|@
name|GerritServerId
name|String
name|serverId
decl_stmt|;
DECL|field|accountCache
annotation|@
name|Inject
specifier|private
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|accountsUpdate
annotation|@
name|Inject
specifier|private
annotation|@
name|ServerInitiated
name|AccountsUpdate
name|accountsUpdate
decl_stmt|;
DECL|field|groupBackend
annotation|@
name|Inject
specifier|private
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|bundleFactory
annotation|@
name|Inject
specifier|private
name|GroupBundle
operator|.
name|Factory
name|bundleFactory
decl_stmt|;
DECL|field|serverIdent
annotation|@
name|Inject
specifier|private
annotation|@
name|GerritPersonIdent
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
DECL|field|rebuilder
specifier|private
name|GroupRebuilder
name|rebuilder
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This test is explicitly testing the migration from ReviewDb to NoteDb, and handles reading
comment|// from NoteDb manually. It should work regardless of the value of noteDb.groups.write, however.
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|migration
operator|.
name|readFromNoteDb
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|accountsUpdate
operator|.
name|update
argument_list|(
literal|"Set Name for CurrentUser"
argument_list|,
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|u
lambda|->
name|u
operator|.
name|setFullName
argument_list|(
literal|"current"
argument_list|)
argument_list|)
expr_stmt|;
name|AuditLogFormatter
name|auditLogFormatter
init|=
name|AuditLogFormatter
operator|.
name|createBackedBy
argument_list|(
name|accountCache
argument_list|,
name|groupBackend
argument_list|,
name|serverId
argument_list|)
decl_stmt|;
name|rebuilder
operator|=
operator|new
name|GroupRebuilder
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|allUsersName
argument_list|,
name|auditLogFormatter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setTimeForTesting ()
specifier|public
name|void
name|setTimeForTesting
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|resetTime ()
specifier|public
name|void
name|resetTime
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|useSystemTime
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|basicGroupProperties ()
specifier|public
name|void
name|basicGroupProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|GroupInfo
name|createdGroup
init|=
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|create
argument_list|(
literal|"group"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|GroupBundle
name|reviewDbBundle
init|=
name|GroupBundle
operator|.
name|Factory
operator|.
name|fromReviewDb
argument_list|(
name|db
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|createdGroup
operator|.
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|deleteGroupRefs
argument_list|(
name|reviewDbBundle
argument_list|)
expr_stmt|;
name|assertMigratedCleanly
argument_list|(
name|rebuild
argument_list|(
name|reviewDbBundle
argument_list|)
argument_list|,
name|reviewDbBundle
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|logFormat ()
specifier|public
name|void
name|logFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountInfo
name|user1
init|=
name|createAccount
argument_list|(
literal|"user1"
argument_list|)
decl_stmt|;
name|AccountInfo
name|user2
init|=
name|createAccount
argument_list|(
literal|"user2"
argument_list|)
decl_stmt|;
name|GroupInfo
name|group1
init|=
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|create
argument_list|(
literal|"group1"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|GroupInfo
name|group2
init|=
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|create
argument_list|(
literal|"group2"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
init|(
name|TempClockStep
name|step
init|=
name|TestTimeUtil
operator|.
name|freezeClock
argument_list|()
init|)
block|{
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|group1
operator|.
name|id
argument_list|)
operator|.
name|addMembers
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|user1
operator|.
name|_accountId
argument_list|)
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|user2
operator|.
name|_accountId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|TimeUtil
operator|.
name|nowTs
argument_list|()
expr_stmt|;
try|try
init|(
name|TempClockStep
name|step
init|=
name|TestTimeUtil
operator|.
name|freezeClock
argument_list|()
init|)
block|{
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|group1
operator|.
name|id
argument_list|)
operator|.
name|addGroups
argument_list|(
name|group2
operator|.
name|id
argument_list|,
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|GroupBundle
name|reviewDbBundle
init|=
name|GroupBundle
operator|.
name|Factory
operator|.
name|fromReviewDb
argument_list|(
name|db
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|group1
operator|.
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|deleteGroupRefs
argument_list|(
name|reviewDbBundle
argument_list|)
expr_stmt|;
name|GroupBundle
name|noteDbBundle
init|=
name|rebuild
argument_list|(
name|reviewDbBundle
argument_list|)
decl_stmt|;
name|assertMigratedCleanly
argument_list|(
name|noteDbBundle
argument_list|,
name|reviewDbBundle
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|CommitInfo
argument_list|>
name|log
init|=
name|log
argument_list|(
name|group1
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|log
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Create group"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|email
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|date
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|noteDbBundle
operator|.
name|group
argument_list|()
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|tz
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
operator|.
name|getTimeZoneOffset
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|author
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Update group\n\nAdd: "
operator|+
name|currentUser
operator|.
name|getName
argument_list|()
operator|+
literal|"<"
operator|+
name|currentUser
operator|.
name|getAccountId
argument_list|()
operator|+
literal|"@"
operator|+
name|serverId
operator|+
literal|">"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|currentUser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|email
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
operator|+
literal|"@"
operator|+
name|serverId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|hasSameDateAs
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|author
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Update group\n"
operator|+
literal|"\n"
operator|+
operator|(
literal|"Add: user1<"
operator|+
name|user1
operator|.
name|_accountId
operator|+
literal|"@"
operator|+
name|serverId
operator|+
literal|">\n"
operator|)
operator|+
operator|(
literal|"Add: user2<"
operator|+
name|user2
operator|.
name|_accountId
operator|+
literal|"@"
operator|+
name|serverId
operator|+
literal|">"
operator|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|currentUser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|email
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
operator|+
literal|"@"
operator|+
name|serverId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|hasSameDateAs
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|author
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Update group\n"
operator|+
literal|"\n"
operator|+
operator|(
literal|"Add-group: Registered Users<global:Registered-Users>\n"
operator|)
operator|+
operator|(
literal|"Add-group: "
operator|+
name|group2
operator|.
name|name
operator|+
literal|"<"
operator|+
name|group2
operator|.
name|id
operator|+
literal|">"
operator|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|currentUser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|email
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
operator|+
literal|"@"
operator|+
name|serverId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|hasSameDateAs
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|author
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|unknownGroupUuid ()
specifier|public
name|void
name|unknownGroupUuid
parameter_list|()
throws|throws
name|Exception
block|{
name|GroupInfo
name|group
init|=
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|create
argument_list|(
literal|"group"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|AccountGroup
operator|.
name|UUID
name|subgroupUuid
init|=
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"mybackend:foo"
argument_list|)
decl_stmt|;
name|AccountGroupById
name|byId
init|=
operator|new
name|AccountGroupById
argument_list|(
operator|new
name|AccountGroupById
operator|.
name|Key
argument_list|(
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|group
operator|.
name|groupId
argument_list|)
argument_list|,
name|subgroupUuid
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|groupBackend
operator|.
name|handles
argument_list|(
name|byId
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|db
operator|.
name|accountGroupById
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|byId
argument_list|)
argument_list|)
expr_stmt|;
name|AccountGroupByIdAud
name|audit
init|=
operator|new
name|AccountGroupByIdAud
argument_list|(
name|byId
argument_list|,
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|db
operator|.
name|accountGroupByIdAud
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|audit
argument_list|)
argument_list|)
expr_stmt|;
name|GroupBundle
name|reviewDbBundle
init|=
name|GroupBundle
operator|.
name|Factory
operator|.
name|fromReviewDb
argument_list|(
name|db
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|group
operator|.
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|deleteGroupRefs
argument_list|(
name|reviewDbBundle
argument_list|)
expr_stmt|;
name|GroupBundle
name|noteDbBundle
init|=
name|rebuild
argument_list|(
name|reviewDbBundle
argument_list|)
decl_stmt|;
name|assertMigratedCleanly
argument_list|(
name|noteDbBundle
argument_list|,
name|reviewDbBundle
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|CommitInfo
argument_list|>
name|log
init|=
name|log
argument_list|(
name|group
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|log
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Create group"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Update group\n\nAdd: "
operator|+
name|currentUser
operator|.
name|getName
argument_list|()
operator|+
literal|"<"
operator|+
name|currentUser
operator|.
name|getAccountId
argument_list|()
operator|+
literal|"@"
operator|+
name|serverId
operator|+
literal|">"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Update group\n\nAdd-group: mybackend:foo<mybackend:foo>"
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteGroupRefs (GroupBundle bundle)
specifier|private
name|void
name|deleteGroupRefs
parameter_list|(
name|GroupBundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|String
name|refName
init|=
name|RefNames
operator|.
name|refsGroups
argument_list|(
name|bundle
operator|.
name|uuid
argument_list|()
argument_list|)
decl_stmt|;
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Ref
name|oldRef
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldRef
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|oldRef
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ru
operator|.
name|delete
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|RefUpdate
operator|.
name|Result
operator|.
name|FORCED
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|rebuild (GroupBundle reviewDbBundle)
specifier|private
name|GroupBundle
name|rebuild
parameter_list|(
name|GroupBundle
name|reviewDbBundle
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|rebuilder
operator|.
name|rebuild
argument_list|(
name|repo
argument_list|,
name|reviewDbBundle
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|bundleFactory
operator|.
name|fromNoteDb
argument_list|(
name|repo
argument_list|,
name|reviewDbBundle
operator|.
name|uuid
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|assertMigratedCleanly (GroupBundle noteDbBundle, GroupBundle expectedReviewDbBundle)
specifier|private
name|void
name|assertMigratedCleanly
parameter_list|(
name|GroupBundle
name|noteDbBundle
parameter_list|,
name|GroupBundle
name|expectedReviewDbBundle
parameter_list|)
block|{
name|assertThat
argument_list|(
name|GroupBundle
operator|.
name|compareWithAudits
argument_list|(
name|expectedReviewDbBundle
argument_list|,
name|noteDbBundle
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|createAccount (String name)
specifier|private
name|AccountInfo
name|createAccount
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RestApiException
block|{
name|AccountInput
name|accountInput
init|=
operator|new
name|AccountInput
argument_list|()
decl_stmt|;
name|accountInput
operator|.
name|username
operator|=
name|name
expr_stmt|;
name|accountInput
operator|.
name|name
operator|=
name|name
expr_stmt|;
return|return
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|create
argument_list|(
name|accountInput
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|log (GroupInfo g)
specifier|private
name|ImmutableList
argument_list|<
name|CommitInfo
argument_list|>
name|log
parameter_list|(
name|GroupInfo
name|g
parameter_list|)
throws|throws
name|Exception
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|CommitInfo
argument_list|>
name|result
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Date
argument_list|>
name|commitDates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|Ref
name|ref
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|refsGroups
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|g
operator|.
name|id
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|rw
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|REVERSE
argument_list|)
expr_stmt|;
name|rw
operator|.
name|setRetainBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|RevCommit
name|c
range|:
name|rw
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|CommitUtil
operator|.
name|toCommitInfo
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|commitDates
operator|.
name|add
argument_list|(
name|c
operator|.
name|getCommitterIdent
argument_list|()
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|assertThat
argument_list|(
name|commitDates
argument_list|)
operator|.
name|named
argument_list|(
literal|"commit timestamps for %s"
argument_list|,
name|result
argument_list|)
operator|.
name|isOrdered
argument_list|()
expr_stmt|;
return|return
name|result
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

