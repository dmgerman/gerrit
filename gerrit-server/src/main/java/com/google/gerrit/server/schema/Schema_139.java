begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2016 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|//you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|//You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|//distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|//See the License for the specific language governing permissions and
end_comment

begin_comment
comment|//limitations under the License.
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
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|ArrayListMultimap
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
name|ListMultimap
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
name|WatchConfig
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
name|WatchConfig
operator|.
name|NotifyType
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
name|WatchConfig
operator|.
name|ProjectWatchKey
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
name|git
operator|.
name|MetaDataUpdate
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
name|jdbc
operator|.
name|JdbcSchema
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
name|OrmDuplicateKeyException
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
name|NullProgressMonitor
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
name|revwalk
operator|.
name|RevWalk
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
DECL|class|Schema_139
specifier|public
class|class
name|Schema_139
extends|extends
name|SchemaVersion
block|{
DECL|field|MSG
specifier|private
specifier|static
specifier|final
name|String
name|MSG
init|=
literal|"Migrate project watches to git"
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
DECL|field|serverUser
specifier|private
specifier|final
name|PersonIdent
name|serverUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_139 (Provider<Schema_138> prior, GitRepositoryManager repoManager, AllUsersName allUsersName, @GerritPersonIdent PersonIdent serverUser)
name|Schema_139
parameter_list|(
name|Provider
argument_list|<
name|Schema_138
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
name|GerritPersonIdent
name|PersonIdent
name|serverUser
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
name|serverUser
operator|=
name|serverUser
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
name|ListMultimap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ProjectWatch
argument_list|>
name|imports
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
init|(
name|Statement
name|stmt
init|=
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
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
literal|"SELECT "
operator|+
literal|"account_id, "
operator|+
literal|"project_name, "
operator|+
literal|"filter, "
operator|+
literal|"notify_abandoned_changes, "
operator|+
literal|"notify_all_comments, "
operator|+
literal|"notify_new_changes, "
operator|+
literal|"notify_new_patch_sets, "
operator|+
literal|"notify_submitted_changes "
operator|+
literal|"FROM account_project_watches"
argument_list|)
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ProjectWatch
operator|.
name|Builder
name|b
init|=
name|ProjectWatch
operator|.
name|builder
argument_list|()
operator|.
name|project
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|notifyAbandonedChanges
argument_list|(
name|rs
operator|.
name|getBoolean
argument_list|(
literal|4
argument_list|)
argument_list|)
operator|.
name|notifyAllComments
argument_list|(
name|rs
operator|.
name|getBoolean
argument_list|(
literal|5
argument_list|)
argument_list|)
operator|.
name|notifyNewChanges
argument_list|(
name|rs
operator|.
name|getBoolean
argument_list|(
literal|6
argument_list|)
argument_list|)
operator|.
name|notifyNewPatchSets
argument_list|(
name|rs
operator|.
name|getBoolean
argument_list|(
literal|7
argument_list|)
argument_list|)
operator|.
name|notifySubmittedChanges
argument_list|(
name|rs
operator|.
name|getBoolean
argument_list|(
literal|8
argument_list|)
argument_list|)
decl_stmt|;
name|imports
operator|.
name|put
argument_list|(
name|accountId
argument_list|,
name|b
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|imports
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
init|(
name|Repository
name|git
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
name|git
argument_list|)
init|)
block|{
name|BatchRefUpdate
name|bru
init|=
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
name|bru
operator|.
name|setRefLogIdent
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|bru
operator|.
name|setRefLogMessage
argument_list|(
name|MSG
argument_list|,
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectWatch
argument_list|>
argument_list|>
name|e
range|:
name|imports
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
argument_list|<
name|ProjectWatchKey
argument_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|projectWatches
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProjectWatch
name|projectWatch
range|:
name|e
operator|.
name|getValue
argument_list|()
control|)
block|{
name|ProjectWatchKey
name|key
init|=
name|ProjectWatchKey
operator|.
name|create
argument_list|(
name|projectWatch
operator|.
name|project
argument_list|()
argument_list|,
name|projectWatch
operator|.
name|filter
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectWatches
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OrmDuplicateKeyException
argument_list|(
literal|"Duplicate key for watched project: "
operator|+
name|key
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|NotifyType
argument_list|>
name|notifyValues
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|NotifyType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectWatch
operator|.
name|notifyAbandonedChanges
argument_list|()
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|ABANDONED_CHANGES
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectWatch
operator|.
name|notifyAllComments
argument_list|()
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectWatch
operator|.
name|notifyNewChanges
argument_list|()
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|NEW_CHANGES
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectWatch
operator|.
name|notifyNewPatchSets
argument_list|()
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectWatch
operator|.
name|notifySubmittedChanges
argument_list|()
condition|)
block|{
name|notifyValues
operator|.
name|add
argument_list|(
name|NotifyType
operator|.
name|SUBMITTED_CHANGES
argument_list|)
expr_stmt|;
block|}
name|projectWatches
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|notifyValues
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allUsersName
argument_list|,
name|git
argument_list|,
name|bru
argument_list|)
init|)
block|{
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
name|MSG
argument_list|)
expr_stmt|;
name|WatchConfig
name|watchConfig
init|=
operator|new
name|WatchConfig
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|watchConfig
operator|.
name|load
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|watchConfig
operator|.
name|setProjectWatches
argument_list|(
name|projectWatches
argument_list|)
expr_stmt|;
name|watchConfig
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
block|}
name|bru
operator|.
name|execute
argument_list|(
name|rw
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|AutoValue
DECL|class|ProjectWatch
specifier|abstract
specifier|static
class|class
name|ProjectWatch
block|{
DECL|method|project ()
specifier|abstract
name|Project
operator|.
name|NameKey
name|project
parameter_list|()
function_decl|;
DECL|method|filter ()
specifier|abstract
annotation|@
name|Nullable
name|String
name|filter
parameter_list|()
function_decl|;
DECL|method|notifyAbandonedChanges ()
specifier|abstract
name|boolean
name|notifyAbandonedChanges
parameter_list|()
function_decl|;
DECL|method|notifyAllComments ()
specifier|abstract
name|boolean
name|notifyAllComments
parameter_list|()
function_decl|;
DECL|method|notifyNewChanges ()
specifier|abstract
name|boolean
name|notifyNewChanges
parameter_list|()
function_decl|;
DECL|method|notifyNewPatchSets ()
specifier|abstract
name|boolean
name|notifyNewPatchSets
parameter_list|()
function_decl|;
DECL|method|notifySubmittedChanges ()
specifier|abstract
name|boolean
name|notifySubmittedChanges
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_Schema_139_ProjectWatch
operator|.
name|Builder
argument_list|()
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|project (Project.NameKey project)
specifier|abstract
name|Builder
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
function_decl|;
DECL|method|filter (@ullable String filter)
specifier|abstract
name|Builder
name|filter
parameter_list|(
annotation|@
name|Nullable
name|String
name|filter
parameter_list|)
function_decl|;
DECL|method|notifyAbandonedChanges (boolean notifyAbandonedChanges)
specifier|abstract
name|Builder
name|notifyAbandonedChanges
parameter_list|(
name|boolean
name|notifyAbandonedChanges
parameter_list|)
function_decl|;
DECL|method|notifyAllComments (boolean notifyAllComments)
specifier|abstract
name|Builder
name|notifyAllComments
parameter_list|(
name|boolean
name|notifyAllComments
parameter_list|)
function_decl|;
DECL|method|notifyNewChanges (boolean notifyNewChanges)
specifier|abstract
name|Builder
name|notifyNewChanges
parameter_list|(
name|boolean
name|notifyNewChanges
parameter_list|)
function_decl|;
DECL|method|notifyNewPatchSets (boolean notifyNewPatchSets)
specifier|abstract
name|Builder
name|notifyNewPatchSets
parameter_list|(
name|boolean
name|notifyNewPatchSets
parameter_list|)
function_decl|;
DECL|method|notifySubmittedChanges (boolean notifySubmittedChanges)
specifier|abstract
name|Builder
name|notifySubmittedChanges
parameter_list|(
name|boolean
name|notifySubmittedChanges
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|abstract
name|ProjectWatch
name|build
parameter_list|()
function_decl|;
block|}
block|}
block|}
end_class

end_unit

