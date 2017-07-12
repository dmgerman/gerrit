begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|pgm
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
name|gerrit
operator|.
name|acceptance
operator|.
name|NoHttpd
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
name|acceptance
operator|.
name|StandaloneSiteTest
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
name|common
operator|.
name|ChangeInput
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
name|Change
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
name|Project
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
name|GerritIndexStatus
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
name|change
operator|.
name|ChangeIndexCollection
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
name|change
operator|.
name|ChangeSchemaDefinitions
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
name|ConfigNotesMigration
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
name|NoteDbChangeState
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
name|NoteDbChangeState
operator|.
name|PrimaryStorage
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
name|NoteDbChangeState
operator|.
name|RefState
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
name|NotesMigrationState
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
name|ReviewDbFactory
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
name|testutil
operator|.
name|NoteDbMode
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
name|google
operator|.
name|inject
operator|.
name|Key
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
name|TypeLiteral
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|StoredConfig
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
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
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
name|util
operator|.
name|FS
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
comment|/**  * Tests for NoteDb migrations where the entry point is through a program, {@code  * migrate-to-note-db} or {@code daemon}.  *  *<p><strong>Note:</strong> These tests are very slow due to the repeated daemon startup. Prefer  * adding tests to {@link com.google.gerrit.acceptance.server.notedb.OnlineNoteDbMigrationIT} if  * possible.  */
end_comment

begin_class
annotation|@
name|NoHttpd
DECL|class|StandaloneNoteDbMigrationIT
specifier|public
class|class
name|StandaloneNoteDbMigrationIT
extends|extends
name|StandaloneSiteTest
block|{
DECL|field|gerritConfig
specifier|private
name|StoredConfig
name|gerritConfig
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
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
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|NoteDbMode
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|NoteDbMode
operator|.
name|OFF
argument_list|)
expr_stmt|;
name|gerritConfig
operator|=
operator|new
name|FileBasedConfig
argument_list|(
name|sitePaths
operator|.
name|gerrit_config
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|detect
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|rebuildOneChangeTrialMode ()
specifier|public
name|void
name|rebuildOneChangeTrialMode
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|setUpOneChange
argument_list|()
expr_stmt|;
name|migrate
argument_list|()
expr_stmt|;
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|READ_WRITE_NO_SEQUENCE
argument_list|)
expr_stmt|;
try|try
init|(
name|ServerContext
name|ctx
init|=
name|startServer
argument_list|()
init|)
block|{
name|GitRepositoryManager
name|repoManager
init|=
name|ctx
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|GitRepositoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectId
name|metaId
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
name|project
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
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ref
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|metaId
operator|=
name|ref
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|ReviewDb
name|db
init|=
name|openUnderlyingReviewDb
argument_list|(
name|ctx
argument_list|)
init|)
block|{
name|Change
name|c
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|NoteDbChangeState
name|state
init|=
name|NoteDbChangeState
operator|.
name|parse
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|state
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|PrimaryStorage
operator|.
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getRefState
argument_list|()
argument_list|)
operator|.
name|hasValue
argument_list|(
name|RefState
operator|.
name|create
argument_list|(
name|metaId
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|migrateOneChange ()
specifier|public
name|void
name|migrateOneChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|setUpOneChange
argument_list|()
expr_stmt|;
name|migrate
argument_list|(
literal|"--trial"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|NOTE_DB_UNFUSED
argument_list|)
expr_stmt|;
try|try
init|(
name|ServerContext
name|ctx
init|=
name|startServer
argument_list|()
init|)
block|{
name|GitRepositoryManager
name|repoManager
init|=
name|ctx
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|GitRepositoryManager
operator|.
name|class
argument_list|)
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
name|project
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|ReviewDb
name|db
init|=
name|openUnderlyingReviewDb
argument_list|(
name|ctx
argument_list|)
init|)
block|{
name|Change
name|c
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|NoteDbChangeState
name|state
init|=
name|NoteDbChangeState
operator|.
name|parse
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|state
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|PrimaryStorage
operator|.
name|NOTE_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getRefState
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|ChangeInput
name|in
init|=
operator|new
name|ChangeInput
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"NoteDb-only change"
argument_list|)
decl_stmt|;
name|in
operator|.
name|newBranch
operator|=
literal|true
expr_stmt|;
name|GerritApi
name|gApi
init|=
name|ctx
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|GerritApi
operator|.
name|class
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Id
name|id2
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
operator|.
name|info
argument_list|()
operator|.
name|_number
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|id2
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|migrationWithReindex ()
specifier|public
name|void
name|migrationWithReindex
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|setUpOneChange
argument_list|()
expr_stmt|;
name|int
name|version
init|=
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
operator|.
name|getLatest
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|GerritIndexStatus
name|status
init|=
operator|new
name|GerritIndexStatus
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|status
operator|.
name|getReady
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|NAME
argument_list|,
name|version
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|status
operator|.
name|setReady
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|NAME
argument_list|,
name|version
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|status
operator|.
name|save
argument_list|()
expr_stmt|;
name|assertServerStartupFails
argument_list|()
expr_stmt|;
name|migrate
argument_list|(
literal|"--trial"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|NOTE_DB_UNFUSED
argument_list|)
expr_stmt|;
name|status
operator|=
operator|new
name|GerritIndexStatus
argument_list|(
name|sitePaths
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|status
operator|.
name|getReady
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|NAME
argument_list|,
name|version
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|onlineMigrationViaDaemon ()
specifier|public
name|void
name|onlineMigrationViaDaemon
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|int
name|prevVersion
init|=
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
operator|.
name|getPrevious
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|int
name|currVersion
init|=
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
operator|.
name|getLatest
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
comment|// Before storing any changes, switch back to the previous version.
name|GerritIndexStatus
name|status
init|=
operator|new
name|GerritIndexStatus
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
name|status
operator|.
name|setReady
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|NAME
argument_list|,
name|currVersion
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|status
operator|.
name|setReady
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|NAME
argument_list|,
name|prevVersion
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|status
operator|.
name|save
argument_list|()
expr_stmt|;
name|setOnlineUpgradeConfig
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setUpOneChange
argument_list|()
expr_stmt|;
name|setOnlineUpgradeConfig
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|IndexUpgradeController
name|u
init|=
operator|new
name|IndexUpgradeController
argument_list|(
literal|1
argument_list|)
decl_stmt|;
try|try
init|(
name|ServerContext
name|ctx
init|=
name|startServer
argument_list|(
name|u
operator|.
name|module
argument_list|()
argument_list|,
literal|"--migrate-to-note-db"
argument_list|,
literal|"true"
argument_list|)
init|)
block|{
name|ChangeIndexCollection
name|indexes
init|=
name|ctx
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ChangeIndexCollection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|indexes
operator|.
name|getSearchIndex
argument_list|()
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|prevVersion
argument_list|)
expr_stmt|;
comment|// Index schema upgrades happen after NoteDb migration, so waiting for those to complete
comment|// should be sufficient.
name|u
operator|.
name|runUpgrades
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|indexes
operator|.
name|getSearchIndex
argument_list|()
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|currVersion
argument_list|)
expr_stmt|;
name|assertNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|NOTE_DB_UNFUSED
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setUpOneChange ()
specifier|private
name|void
name|setUpOneChange
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project"
argument_list|)
expr_stmt|;
try|try
init|(
name|ServerContext
name|ctx
init|=
name|startServer
argument_list|()
init|)
block|{
name|GerritApi
name|gApi
init|=
name|ctx
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|GerritApi
operator|.
name|class
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
literal|"project"
argument_list|)
expr_stmt|;
name|ChangeInput
name|in
init|=
operator|new
name|ChangeInput
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"Test change"
argument_list|)
decl_stmt|;
name|in
operator|.
name|newBranch
operator|=
literal|true
expr_stmt|;
name|changeId
operator|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
operator|.
name|info
argument_list|()
operator|.
name|_number
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|migrate (String... additionalArgs)
specifier|private
name|void
name|migrate
parameter_list|(
name|String
modifier|...
name|additionalArgs
parameter_list|)
throws|throws
name|Exception
block|{
name|runGerrit
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"migrate-to-note-db"
argument_list|,
literal|"-d"
argument_list|,
name|sitePaths
operator|.
name|site_path
operator|.
name|toString
argument_list|()
argument_list|,
literal|"--show-stack-trace"
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|additionalArgs
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNotesMigrationState (NotesMigrationState expected)
specifier|private
name|void
name|assertNotesMigrationState
parameter_list|(
name|NotesMigrationState
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|gerritConfig
operator|.
name|load
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|NotesMigrationState
operator|.
name|forNotesMigration
argument_list|(
operator|new
name|ConfigNotesMigration
argument_list|(
name|gerritConfig
argument_list|)
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|openUnderlyingReviewDb (ServerContext ctx)
specifier|private
name|ReviewDb
name|openUnderlyingReviewDb
parameter_list|(
name|ServerContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|ctx
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|,
name|ReviewDbFactory
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|open
argument_list|()
return|;
block|}
DECL|method|setOnlineUpgradeConfig (boolean enable)
specifier|private
name|void
name|setOnlineUpgradeConfig
parameter_list|(
name|boolean
name|enable
parameter_list|)
throws|throws
name|Exception
block|{
name|gerritConfig
operator|.
name|load
argument_list|()
expr_stmt|;
name|gerritConfig
operator|.
name|setBoolean
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"onlineUpgrade"
argument_list|,
name|enable
argument_list|)
expr_stmt|;
name|gerritConfig
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

