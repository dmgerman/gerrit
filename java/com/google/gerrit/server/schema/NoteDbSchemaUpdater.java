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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|notedb
operator|.
name|NoteDbTable
operator|.
name|CHANGES
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
name|notedb
operator|.
name|NotesMigration
operator|.
name|DISABLE_REVIEW_DB
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
name|notedb
operator|.
name|NotesMigration
operator|.
name|PRIMARY_STORAGE
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
name|notedb
operator|.
name|NotesMigration
operator|.
name|READ
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
name|notedb
operator|.
name|NotesMigration
operator|.
name|SECTION_NOTE_DB
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
name|notedb
operator|.
name|NotesMigration
operator|.
name|WRITE
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
name|annotations
operator|.
name|VisibleForTesting
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
name|NoteDbSchemaVersionManager
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
name|NotesMigration
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
name|stream
operator|.
name|IntStream
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
name|ConfigInvalidException
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

begin_class
DECL|class|NoteDbSchemaUpdater
specifier|public
class|class
name|NoteDbSchemaUpdater
block|{
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|schemaCreator
specifier|private
specifier|final
name|SchemaCreator
name|schemaCreator
decl_stmt|;
DECL|field|notesMigration
specifier|private
specifier|final
name|NotesMigration
name|notesMigration
decl_stmt|;
DECL|field|versionManager
specifier|private
specifier|final
name|NoteDbSchemaVersionManager
name|versionManager
decl_stmt|;
DECL|field|args
specifier|private
specifier|final
name|NoteDbSchemaVersion
operator|.
name|Arguments
name|args
decl_stmt|;
DECL|field|schemaVersions
specifier|private
specifier|final
name|ImmutableSortedMap
argument_list|<
name|Integer
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|NoteDbSchemaVersion
argument_list|>
argument_list|>
name|schemaVersions
decl_stmt|;
annotation|@
name|Inject
DECL|method|NoteDbSchemaUpdater ( @erritServerConfig Config cfg, AllUsersName allUsersName, AllProjectsName allProjectsName, GitRepositoryManager repoManager, SchemaCreator schemaCreator, NotesMigration notesMigration, NoteDbSchemaVersionManager versionManager, NoteDbSchemaVersion.Arguments args)
name|NoteDbSchemaUpdater
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|SchemaCreator
name|schemaCreator
parameter_list|,
name|NotesMigration
name|notesMigration
parameter_list|,
name|NoteDbSchemaVersionManager
name|versionManager
parameter_list|,
name|NoteDbSchemaVersion
operator|.
name|Arguments
name|args
parameter_list|)
block|{
name|this
argument_list|(
name|cfg
argument_list|,
name|allProjectsName
argument_list|,
name|allUsersName
argument_list|,
name|repoManager
argument_list|,
name|schemaCreator
argument_list|,
name|notesMigration
argument_list|,
name|versionManager
argument_list|,
name|args
argument_list|,
name|NoteDbSchemaVersions
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
DECL|method|NoteDbSchemaUpdater ( Config cfg, AllProjectsName allProjectsName, AllUsersName allUsersName, GitRepositoryManager repoManager, SchemaCreator schemaCreator, NotesMigration notesMigration, NoteDbSchemaVersionManager versionManager, NoteDbSchemaVersion.Arguments args, ImmutableSortedMap<Integer, Class<? extends NoteDbSchemaVersion>> schemaVersions)
name|NoteDbSchemaUpdater
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|SchemaCreator
name|schemaCreator
parameter_list|,
name|NotesMigration
name|notesMigration
parameter_list|,
name|NoteDbSchemaVersionManager
name|versionManager
parameter_list|,
name|NoteDbSchemaVersion
operator|.
name|Arguments
name|args
parameter_list|,
name|ImmutableSortedMap
argument_list|<
name|Integer
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|NoteDbSchemaVersion
argument_list|>
argument_list|>
name|schemaVersions
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|schemaCreator
operator|=
name|schemaCreator
expr_stmt|;
name|this
operator|.
name|notesMigration
operator|=
name|notesMigration
expr_stmt|;
name|this
operator|.
name|versionManager
operator|=
name|versionManager
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
name|this
operator|.
name|schemaVersions
operator|=
name|schemaVersions
expr_stmt|;
block|}
DECL|method|update (UpdateUI ui)
specifier|public
name|void
name|update
parameter_list|(
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|notesMigration
operator|.
name|commitChangeWrites
argument_list|()
condition|)
block|{
comment|// TODO(dborowitz): Only necessary to make migration tests pass; remove when NoteDb is the
comment|// only option.
return|return;
block|}
name|ensureSchemaCreated
argument_list|()
expr_stmt|;
name|int
name|currentVersion
init|=
name|versionManager
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentVersion
operator|==
literal|0
condition|)
block|{
comment|// The only valid case where there is no refs/meta/version is when running 3.x init for the
comment|// first time on a site that previously ran init on 2.16. A freshly created 3.x site will have
comment|// seeded refs/meta/version during AllProjectsCreator, so it won't hit this block.
name|checkNoteDbConfigFor216
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|nextVersion
range|:
name|requiredUpgrades
argument_list|(
name|currentVersion
argument_list|,
name|schemaVersions
operator|.
name|keySet
argument_list|()
argument_list|)
control|)
block|{
try|try
block|{
name|ui
operator|.
name|message
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Migrating data to schema %d ..."
argument_list|,
name|nextVersion
argument_list|)
argument_list|)
expr_stmt|;
name|NoteDbSchemaVersions
operator|.
name|get
argument_list|(
name|schemaVersions
argument_list|,
name|nextVersion
argument_list|,
name|args
argument_list|)
operator|.
name|upgrade
argument_list|(
name|ui
argument_list|)
expr_stmt|;
name|versionManager
operator|.
name|increment
argument_list|(
name|nextVersion
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to upgrade to schema version %d"
argument_list|,
name|nextVersion
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|ensureSchemaCreated ()
specifier|private
name|void
name|ensureSchemaCreated
parameter_list|()
throws|throws
name|OrmException
block|{
try|try
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
name|schemaCreator
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot initialize Gerrit site"
argument_list|)
throw|;
block|}
block|}
DECL|method|checkNoteDbConfigFor216 ()
specifier|private
name|void
name|checkNoteDbConfigFor216
parameter_list|()
throws|throws
name|OrmException
block|{
comment|// Check that the NoteDb migration config matches what we expect from a site that both:
comment|// * Completed the change migration to NoteDB.
comment|// * Ran schema upgrades from a 2.16 final release.
if|if
condition|(
operator|!
name|cfg
operator|.
name|getBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|WRITE
argument_list|,
literal|false
argument_list|)
operator|||
operator|!
name|cfg
operator|.
name|getBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|READ
argument_list|,
literal|false
argument_list|)
operator|||
name|cfg
operator|.
name|getEnum
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|PRIMARY_STORAGE
argument_list|,
name|PrimaryStorage
operator|.
name|REVIEW_DB
argument_list|)
operator|!=
name|PrimaryStorage
operator|.
name|NOTE_DB
operator|||
operator|!
name|cfg
operator|.
name|getBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|DISABLE_REVIEW_DB
argument_list|,
literal|false
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"You appear to be upgrading from a 2.x site, but the NoteDb change migration was"
operator|+
literal|" not completed. See documentation:\n"
operator|+
literal|"https://gerrit-review.googlesource.com/Documentation/note-db.html#migration"
argument_list|)
throw|;
block|}
comment|// We don't have a direct way to check that 2.16 init was run; the most obvious side effect
comment|// would be upgrading the *ReviewDb* schema to the latest 2.16 schema version. But in 3.x we can
comment|// no longer access ReviewDb, so we can't check that directly.
comment|//
comment|// Instead, check for a NoteDb-specific side effect of the migration process: the presence of
comment|// the NoteDb group sequence ref. This is created by the schema 163 migration, which was part of
comment|// 2.16 and not 2.15.
comment|//
comment|// There are a few corner cases where we will proceed even if the schema is not fully up to
comment|// date:
comment|//  * If a user happened to run init from master after schema 163 was added but before 2.16
comment|//    final. We assume that someone savvy enough to do that has followed the documented
comment|//    requirement of upgrading to 2.16 final before 3.0.
comment|//  * If a user ran init in 2.16.x and the upgrade to 163 succeeded but a later update failed.
comment|//    In this case the server literally will not start under 2.16. We assume the user will fix
comment|//    this and get 2.16 running rather than abandoning 2.16 and jumping to 3.0 at this point.
try|try
init|(
name|Repository
name|allUsers
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
if|if
condition|(
name|allUsers
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|REFS_SEQUENCES
operator|+
name|Sequences
operator|.
name|NAME_GROUPS
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"You appear to be upgrading to 3.x from a version prior to 2.16; you must upgrade to"
operator|+
literal|" 2.16.x first"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Failed to check NoteDb migration state"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|VisibleForTesting
DECL|method|requiredUpgrades ( int currentVersion, ImmutableSortedSet<Integer> allVersions)
specifier|static
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
name|requiredUpgrades
parameter_list|(
name|int
name|currentVersion
parameter_list|,
name|ImmutableSortedSet
argument_list|<
name|Integer
argument_list|>
name|allVersions
parameter_list|)
throws|throws
name|OrmException
block|{
name|int
name|firstVersion
init|=
name|allVersions
operator|.
name|first
argument_list|()
decl_stmt|;
name|int
name|latestVersion
init|=
name|allVersions
operator|.
name|last
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentVersion
operator|==
name|latestVersion
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|currentVersion
operator|>
name|latestVersion
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot downgrade NoteDb schema from version %d to %d"
argument_list|,
name|currentVersion
argument_list|,
name|latestVersion
argument_list|)
argument_list|)
throw|;
block|}
name|int
name|firstUpgradeVersion
decl_stmt|;
if|if
condition|(
name|currentVersion
operator|==
literal|0
condition|)
block|{
comment|// Bootstrap NoteDb version to minimum supported schema number.
name|firstUpgradeVersion
operator|=
name|firstVersion
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|currentVersion
operator|<
name|firstVersion
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot skip NoteDb schema from version %d to %d"
argument_list|,
name|currentVersion
argument_list|,
name|firstVersion
argument_list|)
argument_list|)
throw|;
block|}
name|firstUpgradeVersion
operator|=
name|currentVersion
operator|+
literal|1
expr_stmt|;
block|}
return|return
name|IntStream
operator|.
name|rangeClosed
argument_list|(
name|firstUpgradeVersion
argument_list|,
name|latestVersion
argument_list|)
operator|.
name|boxed
argument_list|()
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit
