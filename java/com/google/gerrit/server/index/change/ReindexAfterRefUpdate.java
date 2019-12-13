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
DECL|package|com.google.gerrit.server.index.change
package|package
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|directExecutor
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
name|query
operator|.
name|change
operator|.
name|ChangeData
operator|.
name|asChanges
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
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FutureCallback
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
name|util
operator|.
name|concurrent
operator|.
name|Futures
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningExecutorService
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
name|entities
operator|.
name|BranchNameKey
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
name|entities
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
name|entities
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdatedListener
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
name|QueueProvider
operator|.
name|QueueType
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
name|IndexExecutor
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
name|gerrit
operator|.
name|server
operator|.
name|util
operator|.
name|ManualRequestContext
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
name|OneOffRequestContext
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
name|RequestContext
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
name|concurrent
operator|.
name|Callable
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
name|Future
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
comment|/**  * Listener for ref update events that reindexes entities in case the updated Git reference was used  * to compute contents of an index document.  *  *<p>Reindexes any open changes that has a destination branch that was updated to ensure that  * 'mergeable' is still current.  *  *<p>Will reindex accounts when the account's NoteDb ref changes.  */
end_comment

begin_class
DECL|class|ReindexAfterRefUpdate
specifier|public
class|class
name|ReindexAfterRefUpdate
implements|implements
name|GitReferenceUpdatedListener
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
DECL|field|requestContext
specifier|private
specifier|final
name|OneOffRequestContext
name|requestContext
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
DECL|field|indexerFactory
specifier|private
specifier|final
name|ChangeIndexer
operator|.
name|Factory
name|indexerFactory
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|ChangeIndexCollection
name|indexes
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|indexer
specifier|private
specifier|final
name|Provider
argument_list|<
name|AccountIndexer
argument_list|>
name|indexer
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ListeningExecutorService
name|executor
decl_stmt|;
DECL|field|enabled
specifier|private
specifier|final
name|boolean
name|enabled
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReindexAfterRefUpdate ( @erritServerConfig Config cfg, OneOffRequestContext requestContext, Provider<InternalChangeQuery> queryProvider, ChangeIndexer.Factory indexerFactory, ChangeIndexCollection indexes, AllUsersName allUsersName, AccountCache accountCache, Provider<AccountIndexer> indexer, @IndexExecutor(QueueType.BATCH) ListeningExecutorService executor)
name|ReindexAfterRefUpdate
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|OneOffRequestContext
name|requestContext
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|ChangeIndexer
operator|.
name|Factory
name|indexerFactory
parameter_list|,
name|ChangeIndexCollection
name|indexes
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|Provider
argument_list|<
name|AccountIndexer
argument_list|>
name|indexer
parameter_list|,
annotation|@
name|IndexExecutor
argument_list|(
name|QueueType
operator|.
name|BATCH
argument_list|)
name|ListeningExecutorService
name|executor
parameter_list|)
block|{
name|this
operator|.
name|requestContext
operator|=
name|requestContext
expr_stmt|;
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
name|this
operator|.
name|indexerFactory
operator|=
name|indexerFactory
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|enabled
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"change"
argument_list|,
literal|"indexMergeable"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onGitReferenceUpdated (Event event)
specifier|public
name|void
name|onGitReferenceUpdated
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
if|if
condition|(
name|allUsersName
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|event
operator|.
name|getProjectName
argument_list|()
argument_list|)
condition|)
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
name|Account
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|event
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|accountId
operator|!=
literal|null
operator|&&
operator|!
name|event
operator|.
name|getRefName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_STARRED_CHANGES
argument_list|)
condition|)
block|{
name|accountCache
operator|.
name|evict
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|get
argument_list|()
operator|.
name|index
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|enabled
operator|||
name|event
operator|.
name|getRefName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_CHANGES
argument_list|)
operator|||
name|event
operator|.
name|getRefName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_DRAFT_COMMENTS
argument_list|)
operator|||
name|event
operator|.
name|getRefName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_USERS
argument_list|)
condition|)
block|{
return|return;
block|}
name|Futures
operator|.
name|addCallback
argument_list|(
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|GetChanges
argument_list|(
name|event
argument_list|)
argument_list|)
argument_list|,
operator|new
name|FutureCallback
argument_list|<
name|List
argument_list|<
name|Change
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|List
argument_list|<
name|Change
argument_list|>
name|changes
parameter_list|)
block|{
for|for
control|(
name|Change
name|c
range|:
name|changes
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|indexerFactory
operator|.
name|create
argument_list|(
name|executor
argument_list|,
name|indexes
argument_list|)
operator|.
name|indexAsync
argument_list|(
name|c
operator|.
name|getProject
argument_list|()
argument_list|,
name|c
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|ignored
parameter_list|)
block|{
comment|// Logged by {@link GetChanges#call()}.
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|Task
specifier|private
specifier|abstract
class|class
name|Task
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Callable
argument_list|<
name|V
argument_list|>
block|{
DECL|field|event
specifier|protected
name|Event
name|event
decl_stmt|;
DECL|method|Task (Event event)
specifier|protected
name|Task
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|this
operator|.
name|event
operator|=
name|event
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
specifier|final
name|V
name|call
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|ManualRequestContext
name|ctx
init|=
name|requestContext
operator|.
name|open
argument_list|()
init|)
block|{
return|return
name|impl
argument_list|(
name|ctx
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to reindex changes after %s"
argument_list|,
name|event
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|impl (RequestContext ctx)
specifier|protected
specifier|abstract
name|V
name|impl
parameter_list|(
name|RequestContext
name|ctx
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|remove ()
specifier|protected
specifier|abstract
name|void
name|remove
parameter_list|()
function_decl|;
block|}
DECL|class|GetChanges
specifier|private
class|class
name|GetChanges
extends|extends
name|Task
argument_list|<
name|List
argument_list|<
name|Change
argument_list|>
argument_list|>
block|{
DECL|method|GetChanges (Event event)
specifier|private
name|GetChanges
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|super
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|impl (RequestContext ctx)
specifier|protected
name|List
argument_list|<
name|Change
argument_list|>
name|impl
parameter_list|(
name|RequestContext
name|ctx
parameter_list|)
block|{
name|String
name|ref
init|=
name|event
operator|.
name|getRefName
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|project
init|=
name|Project
operator|.
name|nameKey
argument_list|(
name|event
operator|.
name|getProjectName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|equals
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
condition|)
block|{
return|return
name|asChanges
argument_list|(
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byProjectOpen
argument_list|(
name|project
argument_list|)
argument_list|)
return|;
block|}
return|return
name|asChanges
argument_list|(
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byBranchNew
argument_list|(
name|BranchNameKey
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|ref
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Get changes to reindex caused by "
operator|+
name|event
operator|.
name|getRefName
argument_list|()
operator|+
literal|" update of project "
operator|+
name|event
operator|.
name|getProjectName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|protected
name|void
name|remove
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit

