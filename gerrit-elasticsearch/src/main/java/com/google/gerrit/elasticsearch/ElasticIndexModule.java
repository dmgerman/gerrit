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
DECL|package|com.google.gerrit.elasticsearch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
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
name|AbstractVersionManager
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
name|SingleVersionModule
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
name|AccountIndex
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
name|change
operator|.
name|ChangeIndex
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
name|GroupIndex
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|FactoryModuleBuilder
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
DECL|class|ElasticIndexModule
specifier|public
class|class
name|ElasticIndexModule
extends|extends
name|LifecycleModule
block|{
DECL|field|threads
specifier|private
specifier|final
name|int
name|threads
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
DECL|method|singleVersionWithExplicitVersions ( Map<String, Integer> versions, int threads)
specifier|public
specifier|static
name|ElasticIndexModule
name|singleVersionWithExplicitVersions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|versions
parameter_list|,
name|int
name|threads
parameter_list|)
block|{
return|return
operator|new
name|ElasticIndexModule
argument_list|(
name|versions
argument_list|,
name|threads
argument_list|)
return|;
block|}
DECL|method|latestVersionWithOnlineUpgrade ()
specifier|public
specifier|static
name|ElasticIndexModule
name|latestVersionWithOnlineUpgrade
parameter_list|()
block|{
return|return
operator|new
name|ElasticIndexModule
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|ElasticIndexModule (Map<String, Integer> singleVersions, int threads)
specifier|private
name|ElasticIndexModule
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
parameter_list|,
name|int
name|threads
parameter_list|)
block|{
name|this
operator|.
name|singleVersions
operator|=
name|singleVersions
expr_stmt|;
name|this
operator|.
name|threads
operator|=
name|threads
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
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|implement
argument_list|(
name|AccountIndex
operator|.
name|class
argument_list|,
name|ElasticAccountIndex
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|(
name|AccountIndex
operator|.
name|Factory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|implement
argument_list|(
name|ChangeIndex
operator|.
name|class
argument_list|,
name|ElasticChangeIndex
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|(
name|ChangeIndex
operator|.
name|Factory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|implement
argument_list|(
name|GroupIndex
operator|.
name|class
argument_list|,
name|ElasticGroupIndex
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|(
name|GroupIndex
operator|.
name|Factory
operator|.
name|class
argument_list|)
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
name|singleVersions
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
argument_list|(
name|singleVersions
argument_list|)
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
name|bind
argument_list|(
name|AbstractVersionManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ElasticVersionManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|ElasticVersionManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

