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
DECL|package|com.google.gerrit.pgm.init.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|index
package|;
end_package

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
name|index
operator|.
name|IndexDefinition
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
name|name
operator|.
name|Named
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

begin_comment
comment|/**  * This class starts/stops the indexes from the init program so that init can write updates the  * indexes.  */
end_comment

begin_class
DECL|class|IndexManagerOnInit
specifier|public
class|class
name|IndexManagerOnInit
block|{
DECL|field|indexManager
specifier|private
specifier|final
name|LifecycleListener
name|indexManager
decl_stmt|;
DECL|field|defs
specifier|private
specifier|final
name|Collection
argument_list|<
name|IndexDefinition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|defs
decl_stmt|;
annotation|@
name|Inject
DECL|method|IndexManagerOnInit ( @amedIndexModuleOnInit.INDEX_MANAGER) LifecycleListener indexManager, Collection<IndexDefinition<?, ?, ?>> defs)
name|IndexManagerOnInit
parameter_list|(
annotation|@
name|Named
argument_list|(
name|IndexModuleOnInit
operator|.
name|INDEX_MANAGER
argument_list|)
name|LifecycleListener
name|indexManager
parameter_list|,
name|Collection
argument_list|<
name|IndexDefinition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|defs
parameter_list|)
block|{
name|this
operator|.
name|indexManager
operator|=
name|indexManager
expr_stmt|;
name|this
operator|.
name|defs
operator|=
name|defs
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|indexManager
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|IndexDefinition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|def
range|:
name|defs
control|)
block|{
name|def
operator|.
name|getIndexCollection
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|indexManager
operator|.
name|stop
argument_list|()
expr_stmt|;
for|for
control|(
name|IndexDefinition
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|def
range|:
name|defs
control|)
block|{
name|def
operator|.
name|getIndexCollection
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

