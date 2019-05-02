begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|Truth
operator|.
name|assert_
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
name|server
operator|.
name|schema
operator|.
name|NoteDbSchemaUpdater
operator|.
name|requiredUpgrades
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
name|ImmutableSortedMap
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
name|ImmutableSortedSet
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
name|exceptions
operator|.
name|StorageException
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
name|server
operator|.
name|config
operator|.
name|AllProjectsName
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|notedb
operator|.
name|IntBlob
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
name|RepoSequence
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
name|Sequences
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
name|InMemoryRepositoryManager
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
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
name|Repository
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
DECL|class|NoteDbSchemaUpdaterTest
specifier|public
class|class
name|NoteDbSchemaUpdaterTest
block|{
annotation|@
name|Test
DECL|method|requiredUpgradesFromNoVersion ()
specifier|public
name|void
name|requiredUpgradesFromNoVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|requiredUpgrades
argument_list|(
literal|0
argument_list|,
name|versions
argument_list|(
literal|10
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|10
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|requiredUpgrades
argument_list|(
literal|0
argument_list|,
name|versions
argument_list|(
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|requiredUpgradesFromExistingVersion ()
specifier|public
name|void
name|requiredUpgradesFromExistingVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|ImmutableSortedSet
argument_list|<
name|Integer
argument_list|>
name|versions
init|=
name|versions
argument_list|(
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|requiredUpgrades
argument_list|(
literal|10
argument_list|,
name|versions
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|requiredUpgrades
argument_list|(
literal|11
argument_list|,
name|versions
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|12
argument_list|,
literal|13
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|requiredUpgrades
argument_list|(
literal|12
argument_list|,
name|versions
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|13
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|requiredUpgrades
argument_list|(
literal|13
argument_list|,
name|versions
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|downgradeNotSupported ()
specifier|public
name|void
name|downgradeNotSupported
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|requiredUpgrades
argument_list|(
literal|14
argument_list|,
name|versions
argument_list|(
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"expected StorageException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Cannot downgrade NoteDb schema from version 14 to 13"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|skipToFirstVersionNotSupported ()
specifier|public
name|void
name|skipToFirstVersionNotSupported
parameter_list|()
throws|throws
name|Exception
block|{
name|ImmutableSortedSet
argument_list|<
name|Integer
argument_list|>
name|versions
init|=
name|versions
argument_list|(
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|requiredUpgrades
argument_list|(
literal|9
argument_list|,
name|versions
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
try|try
block|{
name|requiredUpgrades
argument_list|(
literal|8
argument_list|,
name|versions
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"expected StorageException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Cannot skip NoteDb schema from version 8 to 10"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TestUpdate
specifier|private
specifier|static
class|class
name|TestUpdate
block|{
DECL|field|cfg
specifier|protected
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|allProjectsName
specifier|protected
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|allUsersName
specifier|protected
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|updater
specifier|protected
specifier|final
name|NoteDbSchemaUpdater
name|updater
decl_stmt|;
DECL|field|repoManager
specifier|protected
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|args
specifier|protected
specifier|final
name|NoteDbSchemaVersion
operator|.
name|Arguments
name|args
decl_stmt|;
DECL|field|messages
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|messages
decl_stmt|;
DECL|method|TestUpdate (Optional<Integer> initialVersion)
name|TestUpdate
parameter_list|(
name|Optional
argument_list|<
name|Integer
argument_list|>
name|initialVersion
parameter_list|)
block|{
name|cfg
operator|=
operator|new
name|Config
argument_list|()
expr_stmt|;
name|allProjectsName
operator|=
operator|new
name|AllProjectsName
argument_list|(
literal|"The-Projects"
argument_list|)
expr_stmt|;
name|allUsersName
operator|=
operator|new
name|AllUsersName
argument_list|(
literal|"The-Users"
argument_list|)
expr_stmt|;
name|repoManager
operator|=
operator|new
name|InMemoryRepositoryManager
argument_list|()
expr_stmt|;
name|args
operator|=
operator|new
name|NoteDbSchemaVersion
operator|.
name|Arguments
argument_list|(
name|repoManager
argument_list|,
name|allProjectsName
argument_list|,
name|allUsersName
argument_list|)
expr_stmt|;
name|NoteDbSchemaVersionManager
name|versionManager
init|=
operator|new
name|NoteDbSchemaVersionManager
argument_list|(
name|allProjectsName
argument_list|,
name|repoManager
argument_list|)
decl_stmt|;
name|updater
operator|=
operator|new
name|NoteDbSchemaUpdater
argument_list|(
name|cfg
argument_list|,
name|allUsersName
argument_list|,
name|repoManager
argument_list|,
operator|new
name|TestSchemaCreator
argument_list|(
name|initialVersion
argument_list|)
argument_list|,
name|versionManager
argument_list|,
name|args
argument_list|,
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|10
argument_list|,
name|TestSchema_10
operator|.
name|class
argument_list|,
literal|11
argument_list|,
name|TestSchema_11
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|messages
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|class|TestSchemaCreator
specifier|private
class|class
name|TestSchemaCreator
implements|implements
name|SchemaCreator
block|{
DECL|field|initialVersion
specifier|private
specifier|final
name|Optional
argument_list|<
name|Integer
argument_list|>
name|initialVersion
decl_stmt|;
DECL|method|TestSchemaCreator (Optional<Integer> initialVersion)
name|TestSchemaCreator
parameter_list|(
name|Optional
argument_list|<
name|Integer
argument_list|>
name|initialVersion
parameter_list|)
block|{
name|this
operator|.
name|initialVersion
operator|=
name|initialVersion
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|void
name|create
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|allProjectsName
argument_list|)
init|)
block|{
if|if
condition|(
name|initialVersion
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|tr
init|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|tr
operator|.
name|update
argument_list|(
name|RefNames
operator|.
name|REFS_VERSION
argument_list|,
name|tr
operator|.
name|blob
argument_list|(
name|initialVersion
operator|.
name|get
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|repoManager
operator|.
name|createRepository
argument_list|(
name|allUsersName
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|ensureCreated ()
specifier|public
name|void
name|ensureCreated
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allProjectsName
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|create
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|setNotesMigrationConfig ()
specifier|protected
name|void
name|setNotesMigrationConfig
parameter_list|()
block|{
name|cfg
operator|.
name|setString
argument_list|(
literal|"noteDb"
argument_list|,
literal|"changes"
argument_list|,
literal|"write"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"noteDb"
argument_list|,
literal|"changes"
argument_list|,
literal|"read"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"noteDb"
argument_list|,
literal|"changes"
argument_list|,
literal|"primaryStorage"
argument_list|,
literal|"NOTE_DB"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"noteDb"
argument_list|,
literal|"changes"
argument_list|,
literal|"disableReviewDb"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
DECL|method|seedGroupSequenceRef ()
specifier|protected
name|void
name|seedGroupSequenceRef
parameter_list|()
block|{
operator|new
name|RepoSequence
argument_list|(
name|repoManager
argument_list|,
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allUsersName
argument_list|,
name|Sequences
operator|.
name|NAME_GROUPS
argument_list|,
parameter_list|()
lambda|->
literal|1
argument_list|,
literal|1
argument_list|)
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
comment|/** Test-specific setup. */
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
block|{}
DECL|method|update ()
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|update
parameter_list|()
throws|throws
name|Exception
block|{
name|updater
operator|.
name|update
argument_list|(
operator|new
name|TestUpdateUI
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|message
parameter_list|(
name|String
name|m
parameter_list|)
block|{
name|messages
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|getMessages
argument_list|()
return|;
block|}
DECL|method|getMessages ()
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|getMessages
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|messages
argument_list|)
return|;
block|}
DECL|method|readVersion ()
name|Optional
argument_list|<
name|Integer
argument_list|>
name|readVersion
parameter_list|()
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
name|allProjectsName
argument_list|)
init|)
block|{
return|return
name|IntBlob
operator|.
name|parse
argument_list|(
name|repo
argument_list|,
name|RefNames
operator|.
name|REFS_VERSION
argument_list|)
operator|.
name|map
argument_list|(
name|IntBlob
operator|::
name|value
argument_list|)
return|;
block|}
block|}
DECL|class|TestSchema_10
specifier|static
class|class
name|TestSchema_10
implements|implements
name|NoteDbSchemaVersion
block|{
annotation|@
name|Override
DECL|method|upgrade (Arguments args, UpdateUI ui)
specifier|public
name|void
name|upgrade
parameter_list|(
name|Arguments
name|args
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"body of 10"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TestSchema_11
specifier|static
class|class
name|TestSchema_11
implements|implements
name|NoteDbSchemaVersion
block|{
annotation|@
name|Override
DECL|method|upgrade (Arguments args, UpdateUI ui)
specifier|public
name|void
name|upgrade
parameter_list|(
name|Arguments
name|args
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"BODY OF 11"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|bootstrapUpdateWith216Prerequisites ()
specifier|public
name|void
name|bootstrapUpdateWith216Prerequisites
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUpdate
name|u
init|=
operator|new
name|TestUpdate
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|setNotesMigrationConfig
argument_list|()
expr_stmt|;
name|seedGroupSequenceRef
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|update
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"Migrating data to schema 10 ..."
argument_list|,
literal|"body of 10"
argument_list|,
literal|"Migrating data to schema 11 ..."
argument_list|,
literal|"BODY OF 11"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|readVersion
argument_list|()
argument_list|)
operator|.
name|hasValue
argument_list|(
literal|11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|bootstrapUpdateFailsWithoutNotesMigrationConfig ()
specifier|public
name|void
name|bootstrapUpdateFailsWithoutNotesMigrationConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUpdate
name|u
init|=
operator|new
name|TestUpdate
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|seedGroupSequenceRef
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|u
operator|.
name|update
argument_list|()
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"expected StorageException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"NoteDb change migration was not completed"
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|u
operator|.
name|getMessages
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|readVersion
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|bootstrapUpdateFailsWithoutGroupSequenceRef ()
specifier|public
name|void
name|bootstrapUpdateFailsWithoutGroupSequenceRef
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUpdate
name|u
init|=
operator|new
name|TestUpdate
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|setNotesMigrationConfig
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|u
operator|.
name|update
argument_list|()
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"expected StorageException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"upgrade to 2.16.x first"
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|u
operator|.
name|getMessages
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|readVersion
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateTwoVersions ()
specifier|public
name|void
name|updateTwoVersions
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUpdate
name|u
init|=
operator|new
name|TestUpdate
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|9
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|update
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"Migrating data to schema 10 ..."
argument_list|,
literal|"body of 10"
argument_list|,
literal|"Migrating data to schema 11 ..."
argument_list|,
literal|"BODY OF 11"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|readVersion
argument_list|()
argument_list|)
operator|.
name|hasValue
argument_list|(
literal|11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateOneVersion ()
specifier|public
name|void
name|updateOneVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUpdate
name|u
init|=
operator|new
name|TestUpdate
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|update
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"Migrating data to schema 11 ..."
argument_list|,
literal|"BODY OF 11"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|readVersion
argument_list|()
argument_list|)
operator|.
name|hasValue
argument_list|(
literal|11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateNoOp ()
specifier|public
name|void
name|updateNoOp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This test covers the state when running the updater after initializing a new 3.x site, which
comment|// seeds the schema version ref with the latest version.
name|TestUpdate
name|u
init|=
operator|new
name|TestUpdate
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|11
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|update
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|readVersion
argument_list|()
argument_list|)
operator|.
name|hasValue
argument_list|(
literal|11
argument_list|)
expr_stmt|;
block|}
DECL|method|versions (Integer... versions)
specifier|private
specifier|static
name|ImmutableSortedSet
argument_list|<
name|Integer
argument_list|>
name|versions
parameter_list|(
name|Integer
modifier|...
name|versions
parameter_list|)
block|{
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|versions
argument_list|)
return|;
block|}
block|}
end_class

end_unit

