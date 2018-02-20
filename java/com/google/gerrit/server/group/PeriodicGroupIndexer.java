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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|ImmutableSet
operator|.
name|toImmutableSet
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
name|Sets
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
name|extensions
operator|.
name|events
operator|.
name|LifecycleListener
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
name|LifecycleModule
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
name|ScheduleConfig
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
name|ScheduleConfig
operator|.
name|Schedule
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
name|WorkQueue
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
name|index
operator|.
name|group
operator|.
name|GroupIndexer
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
name|Repository
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

begin_comment
comment|/**  * Runnable to schedule periodic group reindexing.  *  *<p>Periodic group indexing is intended to run only on slaves. Replication to slaves happens on  * Git level so that Gerrit is not aware of incoming replication events. But slaves need an updated  * group index to resolve memberships of users for ACL validation. To keep the group index in slaves  * up-to-date this class periodically scans the group refs in the All-Users repository to reindex  * groups if they are stale. The ref states of the group refs are cached so that on each run deleted  * groups can be detected and reindexed. This means callers of slaves may observe outdated group  * information until the next indexing happens. The interval on which group indexing is done is  * configurable by setting {@code index.scheduledIndexer.interval} in {@code gerrit.config}. By  * default group indexing is done every 5 minutes.  *  *<p>This class is not able to detect group deletions that were replicated while the slave was  * offline. This means if group refs are deleted while the slave is offline these groups are not  * removed from the group index when the slave is started. However since group deletion is not  * supported this should never happen and one can always do an offline reindex before starting the  * slave.  */
end_comment

begin_class
DECL|class|PeriodicGroupIndexer
specifier|public
class|class
name|PeriodicGroupIndexer
implements|implements
name|Runnable
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PeriodicGroupIndexer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|Lifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Lifecycle
specifier|private
specifier|static
class|class
name|Lifecycle
implements|implements
name|LifecycleListener
block|{
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|queue
specifier|private
specifier|final
name|WorkQueue
name|queue
decl_stmt|;
DECL|field|runner
specifier|private
specifier|final
name|PeriodicGroupIndexer
name|runner
decl_stmt|;
annotation|@
name|Inject
DECL|method|Lifecycle (@erritServerConfig Config cfg, WorkQueue queue, PeriodicGroupIndexer runner)
name|Lifecycle
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|WorkQueue
name|queue
parameter_list|,
name|PeriodicGroupIndexer
name|runner
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
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|runner
operator|=
name|runner
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|boolean
name|runOnStartup
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"scheduledIndexer"
argument_list|,
literal|"runOnStartup"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|runOnStartup
condition|)
block|{
name|runner
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
name|boolean
name|isEnabled
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"scheduledIndexer"
argument_list|,
literal|"enabled"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isEnabled
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"index.scheduledIndexer is disabled"
argument_list|)
expr_stmt|;
return|return;
block|}
name|Schedule
name|schedule
init|=
name|ScheduleConfig
operator|.
name|builder
argument_list|(
name|cfg
argument_list|,
literal|"index"
argument_list|)
operator|.
name|setSubsection
argument_list|(
literal|"scheduledIndexer"
argument_list|)
operator|.
name|buildSchedule
argument_list|()
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|Schedule
operator|.
name|createOrFail
argument_list|(
name|TimeUnit
operator|.
name|MINUTES
operator|.
name|toMillis
argument_list|(
literal|5
argument_list|)
argument_list|,
literal|"00:00"
argument_list|)
argument_list|)
decl_stmt|;
name|queue
operator|.
name|scheduleAtFixedRate
argument_list|(
name|runner
argument_list|,
name|schedule
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
comment|// handled by WorkQueue.stop() already
block|}
block|}
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
DECL|field|groupIndexerProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupIndexer
argument_list|>
name|groupIndexerProvider
decl_stmt|;
DECL|field|groupUuids
specifier|private
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupUuids
decl_stmt|;
annotation|@
name|Inject
DECL|method|PeriodicGroupIndexer ( AllUsersName allUsersName, GitRepositoryManager repoManager, Provider<GroupIndexer> groupIndexerProvider)
name|PeriodicGroupIndexer
parameter_list|(
name|AllUsersName
name|allUsersName
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Provider
argument_list|<
name|GroupIndexer
argument_list|>
name|groupIndexerProvider
parameter_list|)
block|{
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
name|groupIndexerProvider
operator|=
name|groupIndexerProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
specifier|synchronized
name|void
name|run
parameter_list|()
block|{
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
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|newGroupUuids
init|=
name|GroupNameNotes
operator|.
name|loadAllGroups
argument_list|(
name|allUsers
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|GroupReference
operator|::
name|getUUID
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
decl_stmt|;
name|GroupIndexer
name|groupIndexer
init|=
name|groupIndexerProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|int
name|reindexCounter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
range|:
name|newGroupUuids
control|)
block|{
if|if
condition|(
name|groupIndexer
operator|.
name|reindexIfStale
argument_list|(
name|groupUuid
argument_list|)
condition|)
block|{
name|reindexCounter
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|groupUuids
operator|!=
literal|null
condition|)
block|{
comment|// Check if any group was deleted since the last run and if yes remove these groups from the
comment|// index.
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
range|:
name|Sets
operator|.
name|difference
argument_list|(
name|groupUuids
argument_list|,
name|newGroupUuids
argument_list|)
control|)
block|{
name|groupIndexer
operator|.
name|index
argument_list|(
name|groupUuid
argument_list|)
expr_stmt|;
name|reindexCounter
operator|++
expr_stmt|;
block|}
block|}
name|groupUuids
operator|=
name|newGroupUuids
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Run group indexer, {} groups reindexed"
argument_list|,
name|reindexCounter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to reindex groups"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

