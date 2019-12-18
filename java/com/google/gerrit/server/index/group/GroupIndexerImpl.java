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
DECL|package|com.google.gerrit.server.index.group
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
name|group
package|;
end_package

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
name|entities
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
name|extensions
operator|.
name|events
operator|.
name|GroupIndexedListener
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
name|index
operator|.
name|Index
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
name|GroupCache
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
name|InternalGroup
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
name|StalenessCheckResult
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
name|Metadata
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
name|logging
operator|.
name|TraceContext
operator|.
name|TraceTimer
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
name|PluginSetContext
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
name|assistedinject
operator|.
name|Assisted
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
name|assistedinject
operator|.
name|AssistedInject
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
name|Collection
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
name|Optional
import|;
end_import

begin_comment
comment|/**  * Implementation for indexing an internal Gerrit group. The group will be loaded from {@link  * GroupCache}.  */
end_comment

begin_class
DECL|class|GroupIndexerImpl
specifier|public
class|class
name|GroupIndexerImpl
implements|implements
name|GroupIndexer
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
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (GroupIndexCollection indexes)
name|GroupIndexerImpl
name|create
parameter_list|(
name|GroupIndexCollection
name|indexes
parameter_list|)
function_decl|;
DECL|method|create (@ullable GroupIndex index)
name|GroupIndexerImpl
name|create
parameter_list|(
annotation|@
name|Nullable
name|GroupIndex
name|index
parameter_list|)
function_decl|;
block|}
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|indexedListener
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|GroupIndexedListener
argument_list|>
name|indexedListener
decl_stmt|;
DECL|field|stalenessChecker
specifier|private
specifier|final
name|StalenessChecker
name|stalenessChecker
decl_stmt|;
DECL|field|indexes
annotation|@
name|Nullable
specifier|private
specifier|final
name|GroupIndexCollection
name|indexes
decl_stmt|;
DECL|field|index
annotation|@
name|Nullable
specifier|private
specifier|final
name|GroupIndex
name|index
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|GroupIndexerImpl ( GroupCache groupCache, PluginSetContext<GroupIndexedListener> indexedListener, StalenessChecker stalenessChecker, @Assisted GroupIndexCollection indexes)
name|GroupIndexerImpl
parameter_list|(
name|GroupCache
name|groupCache
parameter_list|,
name|PluginSetContext
argument_list|<
name|GroupIndexedListener
argument_list|>
name|indexedListener
parameter_list|,
name|StalenessChecker
name|stalenessChecker
parameter_list|,
annotation|@
name|Assisted
name|GroupIndexCollection
name|indexes
parameter_list|)
block|{
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|indexedListener
operator|=
name|indexedListener
expr_stmt|;
name|this
operator|.
name|stalenessChecker
operator|=
name|stalenessChecker
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|index
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|GroupIndexerImpl ( GroupCache groupCache, PluginSetContext<GroupIndexedListener> indexedListener, StalenessChecker stalenessChecker, @Assisted @Nullable GroupIndex index)
name|GroupIndexerImpl
parameter_list|(
name|GroupCache
name|groupCache
parameter_list|,
name|PluginSetContext
argument_list|<
name|GroupIndexedListener
argument_list|>
name|indexedListener
parameter_list|,
name|StalenessChecker
name|stalenessChecker
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
name|GroupIndex
name|index
parameter_list|)
block|{
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|indexedListener
operator|=
name|indexedListener
expr_stmt|;
name|this
operator|.
name|stalenessChecker
operator|=
name|stalenessChecker
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|index (AccountGroup.UUID uuid)
specifier|public
name|void
name|index
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
comment|// Evict the cache to get an up-to-date value for sure.
name|groupCache
operator|.
name|evict
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|internalGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|internalGroup
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Replace group %s in index"
argument_list|,
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Delete group %s from index"
argument_list|,
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Index
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|InternalGroup
argument_list|>
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
block|{
if|if
condition|(
name|internalGroup
operator|.
name|isPresent
argument_list|()
condition|)
block|{
try|try
init|(
name|TraceTimer
name|traceTimer
init|=
name|TraceContext
operator|.
name|newTimer
argument_list|(
literal|"Replacing group"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|groupUuid
argument_list|(
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|indexVersion
argument_list|(
name|i
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
init|)
block|{
name|i
operator|.
name|replace
argument_list|(
name|internalGroup
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to replace group %s in index version %d"
argument_list|,
name|uuid
operator|.
name|get
argument_list|()
argument_list|,
name|i
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
init|(
name|TraceTimer
name|traceTimer
init|=
name|TraceContext
operator|.
name|newTimer
argument_list|(
literal|"Deleting group"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|groupUuid
argument_list|(
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|indexVersion
argument_list|(
name|i
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
init|)
block|{
name|i
operator|.
name|delete
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to delete group %s from index version %d"
argument_list|,
name|uuid
operator|.
name|get
argument_list|()
argument_list|,
name|i
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|fireGroupIndexedEvent
argument_list|(
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reindexIfStale (AccountGroup.UUID uuid)
specifier|public
name|boolean
name|reindexIfStale
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
try|try
block|{
name|StalenessCheckResult
name|stalenessCheckResult
init|=
name|stalenessChecker
operator|.
name|check
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|stalenessCheckResult
operator|.
name|isStale
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Reindexing stale document %s"
argument_list|,
name|stalenessCheckResult
argument_list|)
expr_stmt|;
name|index
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
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
name|StorageException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|fireGroupIndexedEvent (String uuid)
specifier|private
name|void
name|fireGroupIndexedEvent
parameter_list|(
name|String
name|uuid
parameter_list|)
block|{
name|indexedListener
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onGroupIndexed
argument_list|(
name|uuid
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getWriteIndexes ()
specifier|private
name|Collection
argument_list|<
name|GroupIndex
argument_list|>
name|getWriteIndexes
parameter_list|()
block|{
if|if
condition|(
name|indexes
operator|!=
literal|null
condition|)
block|{
return|return
name|indexes
operator|.
name|getWriteIndexes
argument_list|()
return|;
block|}
return|return
name|index
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|singleton
argument_list|(
name|index
argument_list|)
else|:
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
block|}
end_class

end_unit

