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
DECL|package|com.google.gerrit.lucene
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|lucene
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
name|ChangeSchemas
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
name|IndexCollection
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
name|IndexConfig
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
name|IndexModule
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
name|Provides
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

begin_class
DECL|class|LuceneIndexModule
specifier|public
class|class
name|LuceneIndexModule
extends|extends
name|LifecycleModule
block|{
DECL|field|singleVersion
specifier|private
specifier|final
name|Integer
name|singleVersion
decl_stmt|;
DECL|field|threads
specifier|private
specifier|final
name|int
name|threads
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|String
name|base
decl_stmt|;
DECL|method|LuceneIndexModule ()
specifier|public
name|LuceneIndexModule
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|LuceneIndexModule (Integer singleVersion, int threads, String base)
specifier|public
name|LuceneIndexModule
parameter_list|(
name|Integer
name|singleVersion
parameter_list|,
name|int
name|threads
parameter_list|,
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|singleVersion
operator|=
name|singleVersion
expr_stmt|;
name|this
operator|.
name|threads
operator|=
name|threads
expr_stmt|;
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|factory
argument_list|(
name|LuceneChangeIndex
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|OnlineReindexer
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|IndexModule
argument_list|(
name|threads
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|singleVersion
operator|==
literal|null
operator|&&
name|base
operator|==
literal|null
condition|)
block|{
name|install
argument_list|(
operator|new
name|MultiVersionModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|install
argument_list|(
operator|new
name|SingleVersionModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|getIndexConfig (@erritServerConfig Config cfg)
name|IndexConfig
name|getIndexConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
return|return
name|IndexConfig
operator|.
name|fromConfig
argument_list|(
name|cfg
argument_list|)
return|;
block|}
DECL|class|MultiVersionModule
specifier|private
specifier|static
class|class
name|MultiVersionModule
extends|extends
name|LifecycleModule
block|{
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
name|LuceneVersionManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|SingleVersionModule
specifier|private
class|class
name|SingleVersionModule
extends|extends
name|LifecycleModule
block|{
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
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|getIndex (LuceneChangeIndex.Factory factory)
name|LuceneChangeIndex
name|getIndex
parameter_list|(
name|LuceneChangeIndex
operator|.
name|Factory
name|factory
parameter_list|)
block|{
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
init|=
name|singleVersion
operator|!=
literal|null
condition|?
name|ChangeSchemas
operator|.
name|get
argument_list|(
name|singleVersion
argument_list|)
else|:
name|ChangeSchemas
operator|.
name|getLatest
argument_list|()
decl_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|(
name|schema
argument_list|,
name|base
argument_list|)
return|;
block|}
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
DECL|field|indexes
specifier|private
specifier|final
name|IndexCollection
name|indexes
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|LuceneChangeIndex
name|index
decl_stmt|;
annotation|@
name|Inject
DECL|method|SingleVersionListener (IndexCollection indexes, LuceneChangeIndex index)
name|SingleVersionListener
parameter_list|(
name|IndexCollection
name|indexes
parameter_list|,
name|LuceneChangeIndex
name|index
parameter_list|)
block|{
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
name|index
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
name|indexes
operator|.
name|setSearchIndex
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|indexes
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

