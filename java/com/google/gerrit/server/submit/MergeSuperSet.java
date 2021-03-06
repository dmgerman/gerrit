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
DECL|package|com.google.gerrit.server.submit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|submit
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
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|Strings
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
name|entities
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicItem
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
name|AuthException
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
name|CurrentUser
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
name|logging
operator|.
name|TraceContext
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
name|ChangeNotes
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|plugincontext
operator|.
name|PluginContext
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
name|project
operator|.
name|NoSuchChangeException
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectState
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|HashSet
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
name|Set
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

begin_comment
comment|/**  * Calculates the minimal superset of changes required to be merged.  *  *<p>This includes all parents between a change and the tip of its target branch for the  * merging/rebasing submit strategies. For the cherry-pick strategy no additional changes are  * included.  *  *<p>If change.submitWholeTopic is enabled, also all changes of the topic and their parents are  * included.  */
end_comment

begin_class
DECL|class|MergeSuperSet
specifier|public
class|class
name|MergeSuperSet
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
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
DECL|field|repoManagerProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|MergeOpRepoManager
argument_list|>
name|repoManagerProvider
decl_stmt|;
DECL|field|mergeSuperSetComputation
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|MergeSuperSetComputation
argument_list|>
name|mergeSuperSetComputation
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|notesFactory
specifier|private
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
decl_stmt|;
DECL|field|orm
specifier|private
name|MergeOpRepoManager
name|orm
decl_stmt|;
DECL|field|closeOrm
specifier|private
name|boolean
name|closeOrm
decl_stmt|;
annotation|@
name|Inject
DECL|method|MergeSuperSet ( @erritServerConfig Config cfg, Provider<InternalChangeQuery> queryProvider, Provider<MergeOpRepoManager> repoManagerProvider, DynamicItem<MergeSuperSetComputation> mergeSuperSetComputation, PermissionBackend permissionBackend, ProjectCache projectCache, ChangeNotes.Factory notesFactory)
name|MergeSuperSet
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|Provider
argument_list|<
name|MergeOpRepoManager
argument_list|>
name|repoManagerProvider
parameter_list|,
name|DynamicItem
argument_list|<
name|MergeSuperSetComputation
argument_list|>
name|mergeSuperSetComputation
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
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
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
name|this
operator|.
name|repoManagerProvider
operator|=
name|repoManagerProvider
expr_stmt|;
name|this
operator|.
name|mergeSuperSetComputation
operator|=
name|mergeSuperSetComputation
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|notesFactory
operator|=
name|notesFactory
expr_stmt|;
block|}
DECL|method|wholeTopicEnabled (Config config)
specifier|public
specifier|static
name|boolean
name|wholeTopicEnabled
parameter_list|(
name|Config
name|config
parameter_list|)
block|{
return|return
name|config
operator|.
name|getBoolean
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"submitWholeTopic"
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|setMergeOpRepoManager (MergeOpRepoManager orm)
specifier|public
name|MergeSuperSet
name|setMergeOpRepoManager
parameter_list|(
name|MergeOpRepoManager
name|orm
parameter_list|)
block|{
name|checkState
argument_list|(
name|this
operator|.
name|orm
operator|==
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|orm
operator|=
name|requireNonNull
argument_list|(
name|orm
argument_list|)
expr_stmt|;
name|closeOrm
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|completeChangeSet (Change change, CurrentUser user)
specifier|public
name|ChangeSet
name|completeChangeSet
parameter_list|(
name|Change
name|change
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|IOException
throws|,
name|PermissionBackendException
block|{
try|try
block|{
if|if
condition|(
name|orm
operator|==
literal|null
condition|)
block|{
name|orm
operator|=
name|repoManagerProvider
operator|.
name|get
argument_list|()
expr_stmt|;
name|closeOrm
operator|=
literal|true
expr_stmt|;
block|}
name|List
argument_list|<
name|ChangeData
argument_list|>
name|cds
init|=
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byLegacyChangeId
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|cds
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|,
literal|"Expected exactly one ChangeData for change ID %s, got %s"
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|cds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeData
name|cd
init|=
name|Iterables
operator|.
name|getFirst
argument_list|(
name|cds
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|boolean
name|visible
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|cd
operator|!=
literal|null
condition|)
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|cd
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|.
name|statePermitsRead
argument_list|()
condition|)
block|{
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|change
argument_list|(
name|cd
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
name|visible
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
comment|// Do nothing.
block|}
block|}
block|}
name|ChangeSet
name|changeSet
init|=
operator|new
name|ChangeSet
argument_list|(
name|cd
argument_list|,
name|visible
argument_list|)
decl_stmt|;
if|if
condition|(
name|wholeTopicEnabled
argument_list|(
name|cfg
argument_list|)
condition|)
block|{
return|return
name|completeChangeSetIncludingTopics
argument_list|(
name|changeSet
argument_list|,
name|user
argument_list|)
return|;
block|}
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|PluginContext
operator|.
name|newTrace
argument_list|(
name|mergeSuperSetComputation
argument_list|)
init|)
block|{
return|return
name|mergeSuperSetComputation
operator|.
name|get
argument_list|()
operator|.
name|completeWithoutTopic
argument_list|(
name|orm
argument_list|,
name|changeSet
argument_list|,
name|user
argument_list|)
return|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|closeOrm
operator|&&
name|orm
operator|!=
literal|null
condition|)
block|{
name|orm
operator|.
name|close
argument_list|()
expr_stmt|;
name|orm
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Completes {@code changeSet} with any additional changes from its topics    *    *<p>{@link #completeChangeSetIncludingTopics} calls this repeatedly, alternating with {@link    * MergeSuperSetComputation#completeWithoutTopic(MergeOpRepoManager, ChangeSet, CurrentUser)}, to    * discover what additional changes should be submitted with a change until the set stops growing.    *    *<p>{@code topicsSeen} and {@code visibleTopicsSeen} keep track of topics already explored to    * avoid wasted work.    *    * @return the resulting larger {@link ChangeSet}    */
DECL|method|topicClosure ( ChangeSet changeSet, CurrentUser user, Set<String> topicsSeen, Set<String> visibleTopicsSeen)
specifier|private
name|ChangeSet
name|topicClosure
parameter_list|(
name|ChangeSet
name|changeSet
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|topicsSeen
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|visibleTopicsSeen
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|IOException
block|{
name|List
argument_list|<
name|ChangeData
argument_list|>
name|visibleChanges
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ChangeData
argument_list|>
name|nonVisibleChanges
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changeSet
operator|.
name|changes
argument_list|()
control|)
block|{
name|visibleChanges
operator|.
name|add
argument_list|(
name|cd
argument_list|)
expr_stmt|;
name|String
name|topic
init|=
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|topic
argument_list|)
operator|||
name|visibleTopicsSeen
operator|.
name|contains
argument_list|(
name|topic
argument_list|)
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|ChangeData
name|topicCd
range|:
name|byTopicOpen
argument_list|(
name|topic
argument_list|)
control|)
block|{
if|if
condition|(
name|canRead
argument_list|(
name|user
argument_list|,
name|topicCd
argument_list|)
condition|)
block|{
name|visibleChanges
operator|.
name|add
argument_list|(
name|topicCd
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|nonVisibleChanges
operator|.
name|add
argument_list|(
name|topicCd
argument_list|)
expr_stmt|;
block|}
block|}
name|topicsSeen
operator|.
name|add
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|visibleTopicsSeen
operator|.
name|add
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ChangeData
name|cd
range|:
name|changeSet
operator|.
name|nonVisibleChanges
argument_list|()
control|)
block|{
name|nonVisibleChanges
operator|.
name|add
argument_list|(
name|cd
argument_list|)
expr_stmt|;
name|String
name|topic
init|=
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|topic
argument_list|)
operator|||
name|topicsSeen
operator|.
name|contains
argument_list|(
name|topic
argument_list|)
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|ChangeData
name|topicCd
range|:
name|byTopicOpen
argument_list|(
name|topic
argument_list|)
control|)
block|{
name|nonVisibleChanges
operator|.
name|add
argument_list|(
name|topicCd
argument_list|)
expr_stmt|;
block|}
name|topicsSeen
operator|.
name|add
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ChangeSet
argument_list|(
name|visibleChanges
argument_list|,
name|nonVisibleChanges
argument_list|)
return|;
block|}
DECL|method|completeChangeSetIncludingTopics (ChangeSet changeSet, CurrentUser user)
specifier|private
name|ChangeSet
name|completeChangeSetIncludingTopics
parameter_list|(
name|ChangeSet
name|changeSet
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|IOException
throws|,
name|PermissionBackendException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|topicsSeen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|visibleTopicsSeen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|oldSeen
decl_stmt|;
name|int
name|seen
decl_stmt|;
name|changeSet
operator|=
name|topicClosure
argument_list|(
name|changeSet
argument_list|,
name|user
argument_list|,
name|topicsSeen
argument_list|,
name|visibleTopicsSeen
argument_list|)
expr_stmt|;
name|seen
operator|=
name|topicsSeen
operator|.
name|size
argument_list|()
operator|+
name|visibleTopicsSeen
operator|.
name|size
argument_list|()
expr_stmt|;
do|do
block|{
name|oldSeen
operator|=
name|seen
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|PluginContext
operator|.
name|newTrace
argument_list|(
name|mergeSuperSetComputation
argument_list|)
init|)
block|{
name|changeSet
operator|=
name|mergeSuperSetComputation
operator|.
name|get
argument_list|()
operator|.
name|completeWithoutTopic
argument_list|(
name|orm
argument_list|,
name|changeSet
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
name|changeSet
operator|=
name|topicClosure
argument_list|(
name|changeSet
argument_list|,
name|user
argument_list|,
name|topicsSeen
argument_list|,
name|visibleTopicsSeen
argument_list|)
expr_stmt|;
name|seen
operator|=
name|topicsSeen
operator|.
name|size
argument_list|()
operator|+
name|visibleTopicsSeen
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|seen
operator|!=
name|oldSeen
condition|)
do|;
return|return
name|changeSet
return|;
block|}
DECL|method|byTopicOpen (String topic)
specifier|private
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byTopicOpen
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
return|return
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byTopicOpen
argument_list|(
name|topic
argument_list|)
return|;
block|}
DECL|method|canRead (CurrentUser user, ChangeData cd)
specifier|private
name|boolean
name|canRead
parameter_list|(
name|CurrentUser
name|user
parameter_list|,
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|IOException
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|cd
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
operator|||
operator|!
name|projectState
operator|.
name|statePermitsRead
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ChangeNotes
name|notes
decl_stmt|;
try|try
block|{
name|notes
operator|=
name|cd
operator|.
name|notes
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
comment|// The change was found in the index but is missing in NoteDb.
comment|// This can happen in systems with multiple primary nodes when the replication of the index
comment|// documents is faster than the replication of the Git data.
comment|// Instead of failing, create the change notes from the index data so that the read permission
comment|// check can be performed successfully.
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Got change %d of project %s from index, but couldn't find it in NoteDb"
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|cd
operator|.
name|project
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|notes
operator|=
name|notesFactory
operator|.
name|createFromIndexedChange
argument_list|(
name|cd
operator|.
name|change
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|change
argument_list|(
name|notes
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

