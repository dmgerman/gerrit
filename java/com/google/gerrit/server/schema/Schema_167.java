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
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|NoteDbTable
operator|.
name|GROUPS
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
name|SECTION_NOTE_DB
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
name|Nullable
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
name|data
operator|.
name|GroupDescription
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
name|data
operator|.
name|GroupReference
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDbWrapper
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
name|AccountConfig
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
name|group
operator|.
name|db
operator|.
name|GroupNameNotes
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
name|update
operator|.
name|RefUpdateUtil
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
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|HashMap
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
name|lib
operator|.
name|BatchRefUpdate
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
name|ObjectInserter
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

begin_comment
comment|/** Migrate groups from ReviewDb to NoteDb. */
end_comment

begin_class
DECL|class|Schema_167
specifier|public
class|class
name|Schema_167
extends|extends
name|SchemaVersion
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
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|gerritConfig
specifier|private
specifier|final
name|Config
name|gerritConfig
decl_stmt|;
DECL|field|sitePaths
specifier|private
specifier|final
name|SitePaths
name|sitePaths
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|PersonIdent
name|serverIdent
decl_stmt|;
DECL|field|systemGroupBackend
specifier|private
specifier|final
name|SystemGroupBackend
name|systemGroupBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_167 ( Provider<Schema_166> prior, GitRepositoryManager repoManager, AllUsersName allUsersName, @GerritServerConfig Config gerritConfig, SitePaths sitePaths, @GerritPersonIdent PersonIdent serverIdent, SystemGroupBackend systemGroupBackend)
specifier|protected
name|Schema_167
parameter_list|(
name|Provider
argument_list|<
name|Schema_166
argument_list|>
name|prior
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|gerritConfig
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
name|SystemGroupBackend
name|systemGroupBackend
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|gerritConfig
operator|=
name|gerritConfig
expr_stmt|;
name|this
operator|.
name|sitePaths
operator|=
name|sitePaths
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|systemGroupBackend
operator|=
name|systemGroupBackend
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|migrateData (ReviewDb db, UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
if|if
condition|(
name|gerritConfig
operator|.
name|getBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|GROUPS
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
comment|// Groups in ReviewDb have already been disabled, nothing to do.
return|return;
block|}
try|try
init|(
name|Repository
name|allUsersRepo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|List
argument_list|<
name|GroupReference
argument_list|>
name|allGroupReferences
init|=
name|readGroupReferencesFromReviewDb
argument_list|(
name|db
argument_list|)
decl_stmt|;
name|BatchRefUpdate
name|batchRefUpdate
init|=
name|allUsersRepo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
name|writeAllGroupNamesToNoteDb
argument_list|(
name|allUsersRepo
argument_list|,
name|allGroupReferences
argument_list|,
name|batchRefUpdate
argument_list|)
expr_stmt|;
name|GroupRebuilder
name|groupRebuilder
init|=
name|createGroupRebuilder
argument_list|(
name|db
argument_list|,
name|allUsersRepo
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupReference
name|groupReference
range|:
name|allGroupReferences
control|)
block|{
name|migrateOneGroupToNoteDb
argument_list|(
name|db
argument_list|,
name|allUsersRepo
argument_list|,
name|groupRebuilder
argument_list|,
name|groupReference
operator|.
name|getUUID
argument_list|()
argument_list|,
name|batchRefUpdate
argument_list|)
expr_stmt|;
block|}
name|RefUpdateUtil
operator|.
name|executeChecked
argument_list|(
name|batchRefUpdate
argument_list|,
name|allUsersRepo
argument_list|)
expr_stmt|;
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
name|String
operator|.
name|format
argument_list|(
literal|"Failed to migrate groups to NoteDb for %s"
argument_list|,
name|allUsersName
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|readGroupReferencesFromReviewDb (ReviewDb db)
specifier|private
name|List
argument_list|<
name|GroupReference
argument_list|>
name|readGroupReferencesFromReviewDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
init|(
name|Statement
name|stmt
init|=
name|ReviewDbWrapper
operator|.
name|unwrapJbdcSchema
argument_list|(
name|db
argument_list|)
operator|.
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT group_uuid, name FROM account_groups"
argument_list|)
init|)
block|{
name|List
argument_list|<
name|GroupReference
argument_list|>
name|allGroupReferences
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|groupName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|allGroupReferences
operator|.
name|add
argument_list|(
operator|new
name|GroupReference
argument_list|(
name|groupUuid
argument_list|,
name|groupName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|allGroupReferences
return|;
block|}
block|}
DECL|method|writeAllGroupNamesToNoteDb ( Repository allUsersRepo, List<GroupReference> allGroupReferences, BatchRefUpdate batchRefUpdate)
specifier|private
name|void
name|writeAllGroupNamesToNoteDb
parameter_list|(
name|Repository
name|allUsersRepo
parameter_list|,
name|List
argument_list|<
name|GroupReference
argument_list|>
name|allGroupReferences
parameter_list|,
name|BatchRefUpdate
name|batchRefUpdate
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ObjectInserter
name|inserter
init|=
name|allUsersRepo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|GroupNameNotes
operator|.
name|updateAllGroups
argument_list|(
name|allUsersRepo
argument_list|,
name|inserter
argument_list|,
name|batchRefUpdate
argument_list|,
name|allGroupReferences
argument_list|,
name|serverIdent
argument_list|)
expr_stmt|;
name|inserter
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createGroupRebuilder (ReviewDb db, Repository allUsersRepo)
specifier|private
name|GroupRebuilder
name|createGroupRebuilder
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Repository
name|allUsersRepo
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|AuditLogFormatter
name|auditLogFormatter
init|=
name|createAuditLogFormatter
argument_list|(
name|db
argument_list|,
name|allUsersRepo
argument_list|,
name|gerritConfig
argument_list|,
name|sitePaths
argument_list|)
decl_stmt|;
return|return
operator|new
name|GroupRebuilder
argument_list|(
name|serverIdent
argument_list|,
name|allUsersName
argument_list|,
name|auditLogFormatter
argument_list|)
return|;
block|}
DECL|method|createAuditLogFormatter ( ReviewDb db, Repository allUsersRepo, Config gerritConfig, SitePaths sitePaths)
specifier|private
name|AuditLogFormatter
name|createAuditLogFormatter
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Repository
name|allUsersRepo
parameter_list|,
name|Config
name|gerritConfig
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|String
name|serverId
init|=
operator|new
name|GerritServerIdProvider
argument_list|(
name|gerritConfig
argument_list|,
name|sitePaths
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|SimpleInMemoryAccountCache
name|accountCache
init|=
operator|new
name|SimpleInMemoryAccountCache
argument_list|(
name|allUsersRepo
argument_list|)
decl_stmt|;
name|SimpleInMemoryGroupCache
name|groupCache
init|=
operator|new
name|SimpleInMemoryGroupCache
argument_list|(
name|db
argument_list|)
decl_stmt|;
return|return
name|AuditLogFormatter
operator|.
name|create
argument_list|(
name|accountCache
operator|::
name|get
argument_list|,
name|uuid
lambda|->
block|{
if|if
condition|(
name|systemGroupBackend
operator|.
name|handles
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|systemGroupBackend
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
argument_list|)
return|;
block|}
return|return
name|groupCache
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
return|;
block|}
argument_list|,
name|serverId
argument_list|)
return|;
block|}
DECL|method|migrateOneGroupToNoteDb ( ReviewDb db, Repository allUsersRepo, GroupRebuilder rebuilder, AccountGroup.UUID uuid, BatchRefUpdate batchRefUpdate)
specifier|private
specifier|static
name|void
name|migrateOneGroupToNoteDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Repository
name|allUsersRepo
parameter_list|,
name|GroupRebuilder
name|rebuilder
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|,
name|BatchRefUpdate
name|batchRefUpdate
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
throws|,
name|OrmException
block|{
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
name|uuid
argument_list|)
decl_stmt|;
name|RefUpdateUtil
operator|.
name|deleteChecked
argument_list|(
name|allUsersRepo
argument_list|,
name|RefNames
operator|.
name|refsGroups
argument_list|(
name|uuid
argument_list|)
argument_list|)
expr_stmt|;
name|rebuilder
operator|.
name|rebuild
argument_list|(
name|allUsersRepo
argument_list|,
name|reviewDbBundle
argument_list|,
name|batchRefUpdate
argument_list|)
expr_stmt|;
block|}
comment|// The regular account cache isn't available during init. -> Use a simple replacement which tries
comment|// to load every account only once from disk.
DECL|class|SimpleInMemoryAccountCache
specifier|private
specifier|static
class|class
name|SimpleInMemoryAccountCache
block|{
DECL|field|allUsersRepo
specifier|private
specifier|final
name|Repository
name|allUsersRepo
decl_stmt|;
DECL|field|accounts
specifier|private
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|Account
argument_list|>
argument_list|>
name|accounts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|SimpleInMemoryAccountCache (Repository allUsersRepo)
specifier|public
name|SimpleInMemoryAccountCache
parameter_list|(
name|Repository
name|allUsersRepo
parameter_list|)
block|{
name|this
operator|.
name|allUsersRepo
operator|=
name|allUsersRepo
expr_stmt|;
block|}
DECL|method|get (Account.Id accountId)
specifier|public
name|Optional
argument_list|<
name|Account
argument_list|>
name|get
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|accounts
operator|.
name|computeIfAbsent
argument_list|(
name|accountId
argument_list|,
name|this
operator|::
name|load
argument_list|)
expr_stmt|;
return|return
name|accounts
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
return|;
block|}
DECL|method|load (Account.Id accountId)
specifier|private
name|Optional
argument_list|<
name|Account
argument_list|>
name|load
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
try|try
block|{
name|AccountConfig
name|accountConfig
init|=
operator|new
name|AccountConfig
argument_list|(
name|accountId
argument_list|,
name|allUsersRepo
argument_list|)
operator|.
name|load
argument_list|()
decl_stmt|;
return|return
name|accountConfig
operator|.
name|getLoadedAccount
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|ignored
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|ignored
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to load account %s."
operator|+
literal|" Cannot get account name for group audit log commit messages."
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
block|}
comment|// The regular GroupBackends (especially external GroupBackends) and our internal group cache
comment|// aren't available during init. -> Use a simple replacement which tries to look up only internal
comment|// groups and which loads every internal group only once from disc. (There's no way we can look up
comment|// external groups during init. As we need those groups only for cosmetic aspects in
comment|// AuditLogFormatter, it's safe to exclude them.)
DECL|class|SimpleInMemoryGroupCache
specifier|private
specifier|static
class|class
name|SimpleInMemoryGroupCache
block|{
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|groups
specifier|private
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
argument_list|>
name|groups
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|SimpleInMemoryGroupCache (ReviewDb db)
specifier|public
name|SimpleInMemoryGroupCache
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
block|}
DECL|method|get (AccountGroup.UUID groupUuid)
specifier|public
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|get
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
name|groups
operator|.
name|computeIfAbsent
argument_list|(
name|groupUuid
argument_list|,
name|this
operator|::
name|load
argument_list|)
expr_stmt|;
return|return
name|groups
operator|.
name|get
argument_list|(
name|groupUuid
argument_list|)
return|;
block|}
DECL|method|load (AccountGroup.UUID groupUuid)
specifier|private
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|load
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
if|if
condition|(
operator|!
name|AccountGroup
operator|.
name|isInternalGroup
argument_list|(
name|groupUuid
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|List
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|groupDescriptions
init|=
name|getGroupDescriptions
argument_list|(
name|groupUuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupDescriptions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|groupDescriptions
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|getGroupDescriptions (AccountGroup.UUID groupUuid)
specifier|private
name|List
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|getGroupDescriptions
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
try|try
init|(
name|Statement
name|stmt
init|=
name|ReviewDbWrapper
operator|.
name|unwrapJbdcSchema
argument_list|(
name|db
argument_list|)
operator|.
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT name FROM account_groups where group_uuid = '"
operator|+
name|groupUuid
operator|+
literal|"'"
argument_list|)
init|)
block|{
name|List
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|groupDescriptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|groupName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|groupDescriptions
operator|.
name|add
argument_list|(
name|toGroupDescription
argument_list|(
name|groupUuid
argument_list|,
name|groupName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|groupDescriptions
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|ignored
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to load group %s."
operator|+
literal|" Cannot get group name for group audit log commit messages."
argument_list|,
name|groupUuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
block|}
DECL|method|toGroupDescription ( AccountGroup.UUID groupUuid, String groupName)
specifier|private
specifier|static
name|GroupDescription
operator|.
name|Basic
name|toGroupDescription
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|String
name|groupName
parameter_list|)
block|{
return|return
operator|new
name|GroupDescription
operator|.
name|Basic
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
block|{
return|return
name|groupUuid
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|groupName
return|;
block|}
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

