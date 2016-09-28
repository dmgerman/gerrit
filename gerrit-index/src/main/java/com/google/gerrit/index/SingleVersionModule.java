begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|Schema
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
name|ProvisionException
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
name|Singleton
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
name|TypeLiteral
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Names
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
annotation|@
name|Singleton
DECL|class|SingleVersionModule
specifier|public
class|class
name|SingleVersionModule
extends|extends
name|LifecycleModule
block|{
DECL|field|SINGLE_VERSIONS
specifier|static
specifier|final
name|String
name|SINGLE_VERSIONS
init|=
literal|"LuceneIndexModule/SingleVersions"
decl_stmt|;
DECL|field|singleVersions
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
decl_stmt|;
DECL|method|SingleVersionModule (Map<String, Integer> singleVersions)
specifier|public
name|SingleVersionModule
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
parameter_list|)
block|{
name|this
operator|.
name|singleVersions
operator|=
name|singleVersions
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|SingleVersionListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
name|SINGLE_VERSIONS
argument_list|)
argument_list|)
operator|.
name|toInstance
argument_list|(
name|singleVersions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Singleton
DECL|class|SingleVersionListener
specifier|static
class|class
name|SingleVersionListener
implements|implements
name|LifecycleListener
block|{
DECL|field|disabled
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|disabled
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
DECL|field|singleVersions
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
decl_stmt|;
annotation|@
name|Inject
DECL|method|SingleVersionListener ( @erritServerConfig Config cfg, Collection<IndexDefinition<?, ?, ?>> defs, @Named(SINGLE_VERSIONS) Map<String, Integer> singleVersions)
name|SingleVersionListener
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
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
parameter_list|,
annotation|@
name|Named
argument_list|(
name|SINGLE_VERSIONS
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
parameter_list|)
block|{
name|this
operator|.
name|defs
operator|=
name|defs
expr_stmt|;
name|this
operator|.
name|singleVersions
operator|=
name|singleVersions
expr_stmt|;
name|disabled
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"testDisable"
argument_list|)
argument_list|)
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
name|start
argument_list|(
name|def
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|start ( IndexDefinition<K, V, I> def)
specifier|private
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|,
name|I
extends|extends
name|Index
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
parameter_list|>
name|void
name|start
parameter_list|(
name|IndexDefinition
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|def
parameter_list|)
block|{
if|if
condition|(
name|disabled
operator|.
name|contains
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
decl_stmt|;
name|Integer
name|v
init|=
name|singleVersions
operator|.
name|get
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|schema
operator|=
name|def
operator|.
name|getLatest
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|schema
operator|=
name|def
operator|.
name|getSchemas
argument_list|()
operator|.
name|get
argument_list|(
name|v
argument_list|)
expr_stmt|;
if|if
condition|(
name|schema
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unrecognized %s schema version: %s"
argument_list|,
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|v
argument_list|)
argument_list|)
throw|;
block|}
block|}
name|I
name|index
init|=
name|def
operator|.
name|getIndexFactory
argument_list|()
operator|.
name|create
argument_list|(
name|schema
argument_list|)
decl_stmt|;
name|def
operator|.
name|getIndexCollection
argument_list|()
operator|.
name|setSearchIndex
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|def
operator|.
name|getIndexCollection
argument_list|()
operator|.
name|addWriteIndex
argument_list|(
name|index
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
comment|// Do nothing; indexes are closed by IndexCollection.
block|}
block|}
block|}
end_class

end_unit

