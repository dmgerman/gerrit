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
DECL|package|com.google.gerrit.server.index
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
name|checkArgument
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
name|Lists
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
name|Maps
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
name|IndexDefinition
operator|.
name|IndexFactory
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
name|List
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
name|errors
operator|.
name|ConfigInvalidException
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
DECL|class|AbstractVersionManager
specifier|public
specifier|abstract
class|class
name|AbstractVersionManager
implements|implements
name|LifecycleListener
block|{
DECL|class|Version
specifier|public
specifier|static
class|class
name|Version
parameter_list|<
name|V
parameter_list|>
block|{
DECL|field|schema
specifier|public
specifier|final
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
decl_stmt|;
DECL|field|version
specifier|public
specifier|final
name|int
name|version
decl_stmt|;
DECL|field|exists
specifier|public
specifier|final
name|boolean
name|exists
decl_stmt|;
DECL|field|ready
specifier|public
specifier|final
name|boolean
name|ready
decl_stmt|;
DECL|method|Version (Schema<V> schema, int version, boolean exists, boolean ready)
specifier|public
name|Version
parameter_list|(
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
parameter_list|,
name|int
name|version
parameter_list|,
name|boolean
name|exists
parameter_list|,
name|boolean
name|ready
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|schema
operator|==
literal|null
operator|||
name|schema
operator|.
name|getVersion
argument_list|()
operator|==
name|version
argument_list|)
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|exists
operator|=
name|exists
expr_stmt|;
name|this
operator|.
name|ready
operator|=
name|ready
expr_stmt|;
block|}
block|}
DECL|field|onlineUpgrade
specifier|protected
specifier|final
name|boolean
name|onlineUpgrade
decl_stmt|;
DECL|field|runReindexMsg
specifier|protected
specifier|final
name|String
name|runReindexMsg
decl_stmt|;
DECL|field|sitePaths
specifier|protected
specifier|final
name|SitePaths
name|sitePaths
decl_stmt|;
DECL|field|defs
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
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
DECL|field|reindexers
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|OnlineReindexer
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|reindexers
decl_stmt|;
DECL|method|AbstractVersionManager ( @erritServerConfig Config cfg, SitePaths sitePaths, Collection<IndexDefinition<?, ?, ?>> defs)
specifier|protected
name|AbstractVersionManager
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
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
name|sitePaths
operator|=
name|sitePaths
expr_stmt|;
name|this
operator|.
name|defs
operator|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|defs
operator|.
name|size
argument_list|()
argument_list|)
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
name|this
operator|.
name|defs
operator|.
name|put
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
name|reindexers
operator|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|defs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|onlineUpgrade
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"onlineUpgrade"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|runReindexMsg
operator|=
literal|"No index versions ready; run java -jar "
operator|+
name|sitePaths
operator|.
name|gerrit_war
operator|.
name|toAbsolutePath
argument_list|()
operator|+
literal|" reindex"
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
name|GerritIndexStatus
name|cfg
init|=
name|createIndexStatus
argument_list|()
decl_stmt|;
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
operator|.
name|values
argument_list|()
control|)
block|{
name|initIndex
argument_list|(
name|def
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
comment|// Do nothing; indexes are closed on demand by IndexCollection.
block|}
comment|/**    * Start the online reindexer if the current index is not already the latest.    *    * @param name index name    * @param force start re-index    * @return true if started, otherwise false.    * @throws ReindexerAlreadyRunningException    */
DECL|method|startReindexer (String name, boolean force)
specifier|public
specifier|synchronized
name|boolean
name|startReindexer
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|force
parameter_list|)
throws|throws
name|ReindexerAlreadyRunningException
block|{
name|OnlineReindexer
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|reindexer
init|=
name|reindexers
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|validateReindexerNotRunning
argument_list|(
name|reindexer
argument_list|)
expr_stmt|;
if|if
condition|(
name|force
operator|||
operator|!
name|isLatestIndexVersion
argument_list|(
name|name
argument_list|,
name|reindexer
argument_list|)
condition|)
block|{
name|reindexer
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Activate the latest index if the current index is not already the latest.    *    * @param name index name    * @return true if index was activated, otherwise false.    * @throws ReindexerAlreadyRunningException    */
DECL|method|activateLatestIndex (String name)
specifier|public
specifier|synchronized
name|boolean
name|activateLatestIndex
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ReindexerAlreadyRunningException
block|{
name|OnlineReindexer
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|reindexer
init|=
name|reindexers
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|validateReindexerNotRunning
argument_list|(
name|reindexer
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isLatestIndexVersion
argument_list|(
name|name
argument_list|,
name|reindexer
argument_list|)
condition|)
block|{
name|reindexer
operator|.
name|activateIndex
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Tells if an index with this name is currently known or not.    *    * @param name index name    * @return true if index is known and can be used, otherwise false.    */
DECL|method|isKnownIndex (String name)
specifier|public
name|boolean
name|isKnownIndex
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|defs
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|initIndex ( IndexDefinition<K, V, I> def, GerritIndexStatus cfg)
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
name|void
name|initIndex
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
name|scanVersions
argument_list|(
name|def
argument_list|,
name|cfg
argument_list|)
decl_stmt|;
comment|// Search from the most recent ready version.
comment|// Write to the most recent ready version and the most recent version.
name|Version
argument_list|<
name|V
argument_list|>
name|search
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|Version
argument_list|<
name|V
argument_list|>
argument_list|>
name|write
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|Version
argument_list|<
name|V
argument_list|>
name|v
range|:
name|versions
operator|.
name|descendingMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|v
operator|.
name|schema
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|write
operator|.
name|isEmpty
argument_list|()
operator|&&
name|onlineUpgrade
condition|)
block|{
name|write
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|v
operator|.
name|ready
condition|)
block|{
name|search
operator|=
name|v
expr_stmt|;
if|if
condition|(
operator|!
name|write
operator|.
name|contains
argument_list|(
name|v
argument_list|)
condition|)
block|{
name|write
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|search
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|runReindexMsg
argument_list|)
throw|;
block|}
name|IndexFactory
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|factory
init|=
name|def
operator|.
name|getIndexFactory
argument_list|()
decl_stmt|;
name|I
name|searchIndex
init|=
name|factory
operator|.
name|create
argument_list|(
name|search
operator|.
name|schema
argument_list|)
decl_stmt|;
name|IndexCollection
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|indexes
init|=
name|def
operator|.
name|getIndexCollection
argument_list|()
decl_stmt|;
name|indexes
operator|.
name|setSearchIndex
argument_list|(
name|searchIndex
argument_list|)
expr_stmt|;
for|for
control|(
name|Version
argument_list|<
name|V
argument_list|>
name|v
range|:
name|write
control|)
block|{
if|if
condition|(
name|v
operator|.
name|version
operator|!=
name|search
operator|.
name|version
condition|)
block|{
name|indexes
operator|.
name|addWriteIndex
argument_list|(
name|factory
operator|.
name|create
argument_list|(
name|v
operator|.
name|schema
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|indexes
operator|.
name|addWriteIndex
argument_list|(
name|searchIndex
argument_list|)
expr_stmt|;
block|}
block|}
name|markNotReady
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|versions
operator|.
name|values
argument_list|()
argument_list|,
name|write
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|reindexers
operator|.
name|containsKey
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|latest
init|=
name|write
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|version
decl_stmt|;
name|OnlineReindexer
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|reindexer
init|=
operator|new
name|OnlineReindexer
argument_list|<>
argument_list|(
name|def
argument_list|,
name|latest
argument_list|)
decl_stmt|;
name|reindexers
operator|.
name|put
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|reindexer
argument_list|)
expr_stmt|;
if|if
condition|(
name|onlineUpgrade
operator|&&
name|latest
operator|!=
name|search
operator|.
name|version
condition|)
block|{
name|reindexer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|createIndexStatus ()
specifier|protected
name|GerritIndexStatus
name|createIndexStatus
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|GerritIndexStatus
argument_list|(
name|sitePaths
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|fail
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|scanVersions ( IndexDefinition<K, V, I> def, GerritIndexStatus cfg)
specifier|protected
specifier|abstract
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
function_decl|;
DECL|method|isDirty (Collection<Version<V>> inUse, Version<V> v)
specifier|private
parameter_list|<
name|V
parameter_list|>
name|boolean
name|isDirty
parameter_list|(
name|Collection
argument_list|<
name|Version
argument_list|<
name|V
argument_list|>
argument_list|>
name|inUse
parameter_list|,
name|Version
argument_list|<
name|V
argument_list|>
name|v
parameter_list|)
block|{
return|return
operator|!
name|inUse
operator|.
name|contains
argument_list|(
name|v
argument_list|)
operator|&&
name|v
operator|.
name|exists
return|;
block|}
DECL|method|isLatestIndexVersion (String name, OnlineReindexer<?, ?, ?> reindexer)
specifier|private
name|boolean
name|isLatestIndexVersion
parameter_list|(
name|String
name|name
parameter_list|,
name|OnlineReindexer
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|reindexer
parameter_list|)
block|{
name|int
name|readVersion
init|=
name|defs
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|getIndexCollection
argument_list|()
operator|.
name|getSearchIndex
argument_list|()
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
return|return
name|reindexer
operator|==
literal|null
operator|||
name|reindexer
operator|.
name|getVersion
argument_list|()
operator|==
name|readVersion
return|;
block|}
DECL|method|validateReindexerNotRunning (OnlineReindexer<?, ?, ?> reindexer)
specifier|private
specifier|static
name|void
name|validateReindexerNotRunning
parameter_list|(
name|OnlineReindexer
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|reindexer
parameter_list|)
throws|throws
name|ReindexerAlreadyRunningException
block|{
if|if
condition|(
name|reindexer
operator|!=
literal|null
operator|&&
name|reindexer
operator|.
name|isRunning
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ReindexerAlreadyRunningException
argument_list|()
throw|;
block|}
block|}
DECL|method|markNotReady ( String name, Iterable<Version<V>> versions, Collection<Version<V>> inUse)
specifier|private
parameter_list|<
name|V
parameter_list|>
name|void
name|markNotReady
parameter_list|(
name|String
name|name
parameter_list|,
name|Iterable
argument_list|<
name|Version
argument_list|<
name|V
argument_list|>
argument_list|>
name|versions
parameter_list|,
name|Collection
argument_list|<
name|Version
argument_list|<
name|V
argument_list|>
argument_list|>
name|inUse
parameter_list|)
block|{
name|GerritIndexStatus
name|cfg
init|=
name|createIndexStatus
argument_list|()
decl_stmt|;
name|boolean
name|dirty
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Version
argument_list|<
name|V
argument_list|>
name|v
range|:
name|versions
control|)
block|{
if|if
condition|(
name|isDirty
argument_list|(
name|inUse
argument_list|,
name|v
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setReady
argument_list|(
name|name
argument_list|,
name|v
operator|.
name|version
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dirty
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dirty
condition|)
block|{
try|try
block|{
name|cfg
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|fail
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|fail (Throwable t)
specifier|private
name|ProvisionException
name|fail
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|ProvisionException
name|e
init|=
operator|new
name|ProvisionException
argument_list|(
literal|"Error scanning indexes"
argument_list|)
decl_stmt|;
name|e
operator|.
name|initCause
argument_list|(
name|t
argument_list|)
expr_stmt|;
return|return
name|e
return|;
block|}
block|}
end_class

end_unit

