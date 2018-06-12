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
name|primitives
operator|.
name|Ints
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
name|DynamicSet
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
name|SitePaths
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
name|GerritIndexStatus
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
name|OnlineUpgradeListener
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
name|VersionManager
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
name|Singleton
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
name|TreeMap
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
annotation|@
name|Singleton
DECL|class|ElasticIndexVersionManager
specifier|public
class|class
name|ElasticIndexVersionManager
extends|extends
name|VersionManager
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
DECL|field|prefix
specifier|private
specifier|final
name|String
name|prefix
decl_stmt|;
DECL|field|versionDiscovery
specifier|private
specifier|final
name|ElasticIndexVersionDiscovery
name|versionDiscovery
decl_stmt|;
annotation|@
name|Inject
DECL|method|ElasticIndexVersionManager ( @erritServerConfig Config cfg, SitePaths sitePaths, DynamicSet<OnlineUpgradeListener> listeners, Collection<IndexDefinition<?, ?, ?>> defs, ElasticIndexVersionDiscovery versionDiscovery)
name|ElasticIndexVersionManager
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|DynamicSet
argument_list|<
name|OnlineUpgradeListener
argument_list|>
name|listeners
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
name|ElasticIndexVersionDiscovery
name|versionDiscovery
parameter_list|)
block|{
name|super
argument_list|(
name|sitePaths
argument_list|,
name|listeners
argument_list|,
name|defs
argument_list|,
name|VersionManager
operator|.
name|getOnlineUpgrade
argument_list|(
name|cfg
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|versionDiscovery
operator|=
name|versionDiscovery
expr_stmt|;
name|prefix
operator|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"elasticsearch"
argument_list|,
literal|null
argument_list|,
literal|"prefix"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|scanVersions ( IndexDefinition<K, V, I> def, GerritIndexStatus cfg)
specifier|protected
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
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|Version
argument_list|<
name|V
argument_list|>
argument_list|>
name|scanVersions
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
parameter_list|,
name|GerritIndexStatus
name|cfg
parameter_list|)
block|{
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|Version
argument_list|<
name|V
argument_list|>
argument_list|>
name|versions
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|version
range|:
name|versionDiscovery
operator|.
name|discover
argument_list|(
name|prefix
argument_list|,
name|def
operator|.
name|getName
argument_list|()
argument_list|)
control|)
block|{
name|Integer
name|v
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|version
operator|.
name|length
argument_list|()
operator|!=
literal|4
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Unrecognized version in index %s: %s"
argument_list|,
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|version
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|versions
operator|.
name|put
argument_list|(
name|v
argument_list|,
operator|new
name|Version
argument_list|<
name|V
argument_list|>
argument_list|(
literal|null
argument_list|,
name|v
argument_list|,
literal|true
argument_list|,
name|cfg
operator|.
name|getReady
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
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
literal|"Error scanning index: %s"
argument_list|,
name|def
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
range|:
name|def
operator|.
name|getSchemas
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|int
name|v
init|=
name|schema
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|boolean
name|exists
init|=
name|versions
operator|.
name|containsKey
argument_list|(
name|v
argument_list|)
decl_stmt|;
name|versions
operator|.
name|put
argument_list|(
name|v
argument_list|,
operator|new
name|Version
argument_list|<>
argument_list|(
name|schema
argument_list|,
name|v
argument_list|,
name|exists
argument_list|,
name|cfg
operator|.
name|getReady
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|versions
return|;
block|}
block|}
end_class

end_unit
