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
name|base
operator|.
name|MoreObjects
operator|.
name|firstNonNull
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
name|collect
operator|.
name|ImmutableMap
operator|.
name|toImmutableMap
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
name|Truth8
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_USERS_DEFAULT
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
name|server
operator|.
name|git
operator|.
name|UserConfigSections
operator|.
name|KEY_URL
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
name|server
operator|.
name|git
operator|.
name|UserConfigSections
operator|.
name|MY
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
name|server
operator|.
name|schema
operator|.
name|Schema_160
operator|.
name|DEFAULT_DRAFT_ITEMS
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
name|server
operator|.
name|schema
operator|.
name|VersionedAccountPreferences
operator|.
name|PREFERENCES
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
name|ImmutableMap
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
name|client
operator|.
name|GeneralPreferencesInfo
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
name|MenuItem
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
name|index
operator|.
name|account
operator|.
name|AccountIndexer
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
name|TestUpdateUI
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|junit
operator|.
name|TestRepository
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
name|BlobBasedConfig
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
name|Repository
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
DECL|class|Schema_159_to_160_Test
specifier|public
class|class
name|Schema_159_to_160_Test
block|{
DECL|field|testEnv
annotation|@
name|Rule
specifier|public
name|InMemoryTestEnvironment
name|testEnv
init|=
operator|new
name|InMemoryTestEnvironment
argument_list|()
decl_stmt|;
DECL|field|accountCache
annotation|@
name|Inject
specifier|private
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|accountIndexer
annotation|@
name|Inject
specifier|private
name|AccountIndexer
name|accountIndexer
decl_stmt|;
DECL|field|allUsersName
annotation|@
name|Inject
specifier|private
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|gApi
annotation|@
name|Inject
specifier|private
name|GerritApi
name|gApi
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|userProvider
annotation|@
name|Inject
specifier|private
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|db
annotation|@
name|Inject
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|schema160
annotation|@
name|Inject
specifier|private
name|Schema_160
name|schema160
decl_stmt|;
DECL|field|accountId
specifier|private
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|accountId
operator|=
name|userProvider
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|skipUnmodified ()
specifier|public
name|void
name|skipUnmodified
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|oldMetaId
init|=
name|metaRef
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|fromNoteDb
init|=
name|myMenusFromNoteDb
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|fromApi
init|=
name|myMenusFromApi
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|item
range|:
name|DEFAULT_DRAFT_ITEMS
control|)
block|{
name|assertThat
argument_list|(
name|fromNoteDb
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|item
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|fromApi
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
name|schema160
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metaRef
argument_list|(
name|accountId
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteItems ()
specifier|public
name|void
name|deleteItems
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|oldMetaId
init|=
name|metaRef
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|defaultNames
init|=
name|myMenusFromApi
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|GeneralPreferencesInfo
name|prefs
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
name|prefs
operator|.
name|my
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|MenuItem
argument_list|(
literal|"Something else"
argument_list|,
name|DEFAULT_DRAFT_ITEMS
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
literal|"+is:mergeable"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|DEFAULT_DRAFT_ITEMS
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|prefs
operator|.
name|my
operator|.
name|add
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Draft entry "
operator|+
name|i
argument_list|,
name|DEFAULT_DRAFT_ITEMS
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|setPreferences
argument_list|(
name|prefs
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|oldNames
init|=
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
operator|.
name|add
argument_list|(
literal|"Something else"
argument_list|)
operator|.
name|addAll
argument_list|(
name|defaultNames
argument_list|)
operator|.
name|add
argument_list|(
literal|"Draft entry 0"
argument_list|)
operator|.
name|add
argument_list|(
literal|"Draft entry 1"
argument_list|)
operator|.
name|add
argument_list|(
literal|"Draft entry 2"
argument_list|)
operator|.
name|add
argument_list|(
literal|"Draft entry 3"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|myMenusFromApi
argument_list|(
name|accountId
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|oldNames
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|schema160
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
name|accountCache
operator|.
name|evict
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
name|accountIndexer
operator|.
name|index
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
name|testEnv
operator|.
name|setApiUser
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metaRef
argument_list|(
name|accountId
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|newNames
init|=
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
operator|.
name|add
argument_list|(
literal|"Something else"
argument_list|)
operator|.
name|addAll
argument_list|(
name|defaultNames
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|myMenusFromNoteDb
argument_list|(
name|accountId
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|newNames
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|myMenusFromApi
argument_list|(
name|accountId
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|newNames
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|skipNonExistentRefsUsersDefault ()
specifier|public
name|void
name|skipNonExistentRefsUsersDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|readRef
argument_list|(
name|REFS_USERS_DEFAULT
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|schema160
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|readRef
argument_list|(
name|REFS_USERS_DEFAULT
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDefaultItem ()
specifier|public
name|void
name|deleteDefaultItem
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|readRef
argument_list|(
name|REFS_USERS_DEFAULT
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|defaultNames
init|=
name|defaultMenusFromApi
argument_list|()
decl_stmt|;
comment|// Setting *any* preference causes preferences.config to contain the full set of "my" sections.
comment|// This mimics real-world behavior prior to the 2.15 upgrade; see Issue 8439 for details.
name|GeneralPreferencesInfo
name|prefs
init|=
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|getDefaultPreferences
argument_list|()
decl_stmt|;
name|prefs
operator|.
name|signedOffBy
operator|=
operator|!
name|firstNonNull
argument_list|(
name|prefs
operator|.
name|signedOffBy
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|setDefaultPreferences
argument_list|(
name|prefs
argument_list|)
expr_stmt|;
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
name|Config
name|cfg
init|=
operator|new
name|BlobBasedConfig
argument_list|(
literal|null
argument_list|,
name|repo
argument_list|,
name|readRef
argument_list|(
name|REFS_USERS_DEFAULT
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
name|PREFERENCES
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cfg
operator|.
name|getSubsections
argument_list|(
literal|"my"
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|defaultNames
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// Add more defaults directly in git, the SetPreferences endpoint doesn't respect the "my"
comment|// field in the input in 2.15 and earlier.
name|cfg
operator|.
name|setString
argument_list|(
literal|"my"
argument_list|,
literal|"Drafts"
argument_list|,
literal|"url"
argument_list|,
literal|"#/q/owner:self+is:draft"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"my"
argument_list|,
literal|"Something else"
argument_list|,
literal|"url"
argument_list|,
literal|"#/q/owner:self+is:draft+is:mergeable"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"my"
argument_list|,
literal|"Totally not drafts"
argument_list|,
literal|"url"
argument_list|,
literal|"#/q/owner:self+is:draft"
argument_list|)
expr_stmt|;
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
operator|.
name|branch
argument_list|(
name|REFS_USERS_DEFAULT
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|add
argument_list|(
name|PREFERENCES
argument_list|,
name|cfg
operator|.
name|toText
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|oldNames
init|=
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
operator|.
name|addAll
argument_list|(
name|defaultNames
argument_list|)
operator|.
name|add
argument_list|(
literal|"Drafts"
argument_list|)
operator|.
name|add
argument_list|(
literal|"Something else"
argument_list|)
operator|.
name|add
argument_list|(
literal|"Totally not drafts"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|defaultMenusFromApi
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|oldNames
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|schema160
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
operator|new
name|TestUpdateUI
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|readRef
argument_list|(
name|REFS_USERS_DEFAULT
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|newNames
init|=
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
operator|.
name|addAll
argument_list|(
name|defaultNames
argument_list|)
operator|.
name|add
argument_list|(
literal|"Something else"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|myMenusFromNoteDb
argument_list|(
name|VersionedAccountPreferences
operator|::
name|forDefault
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|newNames
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|defaultMenusFromApi
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|newNames
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|myMenusFromNoteDb (Account.Id id)
specifier|private
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|myMenusFromNoteDb
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|myMenusFromNoteDb
argument_list|(
parameter_list|()
lambda|->
name|VersionedAccountPreferences
operator|.
name|forUser
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|// Raw config values, bypassing the defaults set by PreferencesConfig.
DECL|method|myMenusFromNoteDb ( Supplier<VersionedAccountPreferences> prefsSupplier)
specifier|private
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|myMenusFromNoteDb
parameter_list|(
name|Supplier
argument_list|<
name|VersionedAccountPreferences
argument_list|>
name|prefsSupplier
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
name|VersionedAccountPreferences
name|prefs
init|=
name|prefsSupplier
operator|.
name|get
argument_list|()
decl_stmt|;
name|prefs
operator|.
name|load
argument_list|(
name|allUsersName
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|Config
name|cfg
init|=
name|prefs
operator|.
name|getConfig
argument_list|()
decl_stmt|;
return|return
name|cfg
operator|.
name|getSubsections
argument_list|(
name|MY
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toImmutableMap
argument_list|(
name|i
lambda|->
name|i
argument_list|,
name|i
lambda|->
name|cfg
operator|.
name|getString
argument_list|(
name|MY
argument_list|,
name|i
argument_list|,
name|KEY_URL
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|myMenusFromApi (Account.Id id)
specifier|private
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|myMenusFromApi
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|myMenus
argument_list|(
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getPreferences
argument_list|()
argument_list|)
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|defaultMenusFromApi ()
specifier|private
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|defaultMenusFromApi
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|myMenus
argument_list|(
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|getDefaultPreferences
argument_list|()
argument_list|)
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|myMenus (GeneralPreferencesInfo prefs)
specifier|private
specifier|static
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|myMenus
parameter_list|(
name|GeneralPreferencesInfo
name|prefs
parameter_list|)
block|{
return|return
name|prefs
operator|.
name|my
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toImmutableMap
argument_list|(
name|i
lambda|->
name|i
operator|.
name|name
argument_list|,
name|i
lambda|->
name|i
operator|.
name|url
argument_list|)
argument_list|)
return|;
block|}
DECL|method|metaRef (Account.Id id)
specifier|private
name|ObjectId
name|metaRef
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|readRef
argument_list|(
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|AssertionError
argument_list|(
literal|"missing ref for account "
operator|+
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|readRef (String ref)
specifier|private
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|readRef
parameter_list|(
name|String
name|ref
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
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|ref
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Ref
operator|::
name|getObjectId
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

