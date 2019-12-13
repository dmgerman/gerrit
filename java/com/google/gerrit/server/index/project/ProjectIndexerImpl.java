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
DECL|package|com.google.gerrit.server.index.project
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
name|project
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
name|extensions
operator|.
name|events
operator|.
name|ProjectIndexedListener
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
name|project
operator|.
name|ProjectData
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
name|project
operator|.
name|ProjectIndex
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
name|project
operator|.
name|ProjectIndexCollection
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
name|project
operator|.
name|ProjectIndexer
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

begin_comment
comment|/**  * Implementation for indexing a Gerrit-managed repository (project). The project will be loaded  * from {@link ProjectCache}.  */
end_comment

begin_class
DECL|class|ProjectIndexerImpl
specifier|public
class|class
name|ProjectIndexerImpl
implements|implements
name|ProjectIndexer
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
DECL|method|create (ProjectIndexCollection indexes)
name|ProjectIndexerImpl
name|create
parameter_list|(
name|ProjectIndexCollection
name|indexes
parameter_list|)
function_decl|;
DECL|method|create (@ullable ProjectIndex index)
name|ProjectIndexerImpl
name|create
parameter_list|(
annotation|@
name|Nullable
name|ProjectIndex
name|index
parameter_list|)
function_decl|;
block|}
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|indexedListener
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|ProjectIndexedListener
argument_list|>
name|indexedListener
decl_stmt|;
DECL|field|indexes
annotation|@
name|Nullable
specifier|private
specifier|final
name|ProjectIndexCollection
name|indexes
decl_stmt|;
DECL|field|index
annotation|@
name|Nullable
specifier|private
specifier|final
name|ProjectIndex
name|index
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|ProjectIndexerImpl ( ProjectCache projectCache, PluginSetContext<ProjectIndexedListener> indexedListener, @Assisted ProjectIndexCollection indexes)
name|ProjectIndexerImpl
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|,
name|PluginSetContext
argument_list|<
name|ProjectIndexedListener
argument_list|>
name|indexedListener
parameter_list|,
annotation|@
name|Assisted
name|ProjectIndexCollection
name|indexes
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|indexedListener
operator|=
name|indexedListener
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
DECL|method|ProjectIndexerImpl ( ProjectCache projectCache, PluginSetContext<ProjectIndexedListener> indexedListener, @Assisted @Nullable ProjectIndex index)
name|ProjectIndexerImpl
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|,
name|PluginSetContext
argument_list|<
name|ProjectIndexedListener
argument_list|>
name|indexedListener
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
name|ProjectIndex
name|index
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|indexedListener
operator|=
name|indexedListener
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
DECL|method|index (Project.NameKey nameKey)
specifier|public
name|void
name|index
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Replace project %s in index"
argument_list|,
name|nameKey
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectData
name|projectData
init|=
name|projectState
operator|.
name|toProjectData
argument_list|()
decl_stmt|;
for|for
control|(
name|ProjectIndex
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
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
literal|"Replacing project"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|projectName
argument_list|(
name|nameKey
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
name|projectData
argument_list|)
expr_stmt|;
block|}
block|}
name|fireProjectIndexedEvent
argument_list|(
name|nameKey
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
literal|"Delete project %s from index"
argument_list|,
name|nameKey
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ProjectIndex
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
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
literal|"Deleting project"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|projectName
argument_list|(
name|nameKey
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
name|nameKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|fireProjectIndexedEvent (String name)
specifier|private
name|void
name|fireProjectIndexedEvent
parameter_list|(
name|String
name|name
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
name|onProjectIndexed
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getWriteIndexes ()
specifier|private
name|Collection
argument_list|<
name|ProjectIndex
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

