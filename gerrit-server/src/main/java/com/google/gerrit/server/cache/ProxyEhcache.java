begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.cache
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Statistics
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|bootstrap
operator|.
name|BootstrapCacheLoader
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|CacheConfiguration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|event
operator|.
name|RegisteredEventListeners
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|exceptionhandler
operator|.
name|CacheExceptionHandler
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|extension
operator|.
name|CacheExtension
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|loader
operator|.
name|CacheLoader
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|statistics
operator|.
name|CacheUsageListener
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|statistics
operator|.
name|LiveCacheStatistics
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|statistics
operator|.
name|sampled
operator|.
name|SampledCacheStatistics
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|transaction
operator|.
name|manager
operator|.
name|TransactionManagerLookup
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|writer
operator|.
name|CacheWriter
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|writer
operator|.
name|CacheWriterManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyChangeListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_comment
comment|/** Proxy around a cache which has not yet been created. */
end_comment

begin_class
DECL|class|ProxyEhcache
specifier|final
class|class
name|ProxyEhcache
implements|implements
name|Ehcache
block|{
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
DECL|field|self
specifier|private
specifier|volatile
name|Ehcache
name|self
decl_stmt|;
DECL|method|ProxyEhcache (final String cacheName)
name|ProxyEhcache
parameter_list|(
specifier|final
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
DECL|method|bind (final Ehcache self)
name|void
name|bind
parameter_list|(
specifier|final
name|Ehcache
name|self
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
block|}
DECL|method|self ()
specifier|private
name|Ehcache
name|self
parameter_list|()
block|{
return|return
name|self
return|;
block|}
annotation|@
name|Override
DECL|method|clone ()
specifier|public
name|Object
name|clone
parameter_list|()
throws|throws
name|CloneNotSupportedException
block|{
throw|throw
operator|new
name|CloneNotSupportedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
annotation|@
name|Override
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|//
comment|// Everything else delegates through self.
comment|//
annotation|@
name|Override
DECL|method|bootstrap ()
specifier|public
name|void
name|bootstrap
parameter_list|()
block|{
name|self
argument_list|()
operator|.
name|bootstrap
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|calculateInMemorySize ()
specifier|public
name|long
name|calculateInMemorySize
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|calculateInMemorySize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|clearStatistics ()
specifier|public
name|void
name|clearStatistics
parameter_list|()
block|{
name|self
argument_list|()
operator|.
name|clearStatistics
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|dispose ()
specifier|public
name|void
name|dispose
parameter_list|()
throws|throws
name|IllegalStateException
block|{
name|self
argument_list|()
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|evictExpiredElements ()
specifier|public
name|void
name|evictExpiredElements
parameter_list|()
block|{
name|self
argument_list|()
operator|.
name|evictExpiredElements
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (Object key)
specifier|public
name|Element
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (Serializable key)
specifier|public
name|Element
name|get
parameter_list|(
name|Serializable
name|key
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|getAllWithLoader (Collection keys, Object loaderArgument)
specifier|public
name|Map
name|getAllWithLoader
parameter_list|(
name|Collection
name|keys
parameter_list|,
name|Object
name|loaderArgument
parameter_list|)
throws|throws
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getAllWithLoader
argument_list|(
name|keys
argument_list|,
name|loaderArgument
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getAverageGetTime ()
specifier|public
name|float
name|getAverageGetTime
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getAverageGetTime
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getBootstrapCacheLoader ()
specifier|public
name|BootstrapCacheLoader
name|getBootstrapCacheLoader
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getBootstrapCacheLoader
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCacheConfiguration ()
specifier|public
name|CacheConfiguration
name|getCacheConfiguration
parameter_list|()
block|{
if|if
condition|(
name|self
operator|==
literal|null
condition|)
block|{
comment|// In Ehcache 1.7, BlockingCache wants to ask us if we are
comment|// clustered using Terracotta. Unfortunately it is too early
comment|// to know for certain as the caches have not actually been
comment|// created or configured.
comment|//
return|return
operator|new
name|CacheConfiguration
argument_list|()
return|;
block|}
return|return
name|self
argument_list|()
operator|.
name|getCacheConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCacheEventNotificationService ()
specifier|public
name|RegisteredEventListeners
name|getCacheEventNotificationService
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getCacheEventNotificationService
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCacheExceptionHandler ()
specifier|public
name|CacheExceptionHandler
name|getCacheExceptionHandler
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getCacheExceptionHandler
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCacheManager ()
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getCacheManager
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDiskStoreSize ()
specifier|public
name|int
name|getDiskStoreSize
parameter_list|()
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|getDiskStoreSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getGuid ()
specifier|public
name|String
name|getGuid
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getGuid
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|getKeys ()
specifier|public
name|List
name|getKeys
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getKeys
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|getKeysNoDuplicateCheck ()
specifier|public
name|List
name|getKeysNoDuplicateCheck
parameter_list|()
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|getKeysNoDuplicateCheck
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|getKeysWithExpiryCheck ()
specifier|public
name|List
name|getKeysWithExpiryCheck
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getKeysWithExpiryCheck
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMemoryStoreSize ()
specifier|public
name|long
name|getMemoryStoreSize
parameter_list|()
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|getMemoryStoreSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getQuiet (Object key)
specifier|public
name|Element
name|getQuiet
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getQuiet
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getQuiet (Serializable key)
specifier|public
name|Element
name|getQuiet
parameter_list|(
name|Serializable
name|key
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getQuiet
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRegisteredCacheExtensions ()
specifier|public
name|List
argument_list|<
name|CacheExtension
argument_list|>
name|getRegisteredCacheExtensions
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getRegisteredCacheExtensions
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRegisteredCacheLoaders ()
specifier|public
name|List
argument_list|<
name|CacheLoader
argument_list|>
name|getRegisteredCacheLoaders
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getRegisteredCacheLoaders
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSize ()
specifier|public
name|int
name|getSize
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getStatistics ()
specifier|public
name|Statistics
name|getStatistics
parameter_list|()
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|getStatistics
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getStatisticsAccuracy ()
specifier|public
name|int
name|getStatisticsAccuracy
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getStatisticsAccuracy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getStatus
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getWithLoader (Object key, CacheLoader loader, Object loaderArgument)
specifier|public
name|Element
name|getWithLoader
parameter_list|(
name|Object
name|key
parameter_list|,
name|CacheLoader
name|loader
parameter_list|,
name|Object
name|loaderArgument
parameter_list|)
throws|throws
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getWithLoader
argument_list|(
name|key
argument_list|,
name|loader
argument_list|,
name|loaderArgument
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|initialise ()
specifier|public
name|void
name|initialise
parameter_list|()
block|{
name|self
argument_list|()
operator|.
name|initialise
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isDisabled ()
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|isDisabled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isElementInMemory (Object key)
specifier|public
name|boolean
name|isElementInMemory
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|self
argument_list|()
operator|.
name|isElementInMemory
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isElementInMemory (Serializable key)
specifier|public
name|boolean
name|isElementInMemory
parameter_list|(
name|Serializable
name|key
parameter_list|)
block|{
return|return
name|self
argument_list|()
operator|.
name|isElementInMemory
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isElementOnDisk (Object key)
specifier|public
name|boolean
name|isElementOnDisk
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|self
argument_list|()
operator|.
name|isElementOnDisk
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isElementOnDisk (Serializable key)
specifier|public
name|boolean
name|isElementOnDisk
parameter_list|(
name|Serializable
name|key
parameter_list|)
block|{
return|return
name|self
argument_list|()
operator|.
name|isElementOnDisk
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isExpired (Element element)
specifier|public
name|boolean
name|isExpired
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|NullPointerException
block|{
return|return
name|self
argument_list|()
operator|.
name|isExpired
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isKeyInCache (Object key)
specifier|public
name|boolean
name|isKeyInCache
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|self
argument_list|()
operator|.
name|isKeyInCache
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isValueInCache (Object value)
specifier|public
name|boolean
name|isValueInCache
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|self
argument_list|()
operator|.
name|isValueInCache
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|load (Object key)
specifier|public
name|void
name|load
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|load
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|loadAll (Collection keys, Object argument)
specifier|public
name|void
name|loadAll
parameter_list|(
name|Collection
name|keys
parameter_list|,
name|Object
name|argument
parameter_list|)
throws|throws
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|loadAll
argument_list|(
name|keys
argument_list|,
name|argument
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|put (Element element, boolean doNotNotifyCacheReplicators)
specifier|public
name|void
name|put
parameter_list|(
name|Element
name|element
parameter_list|,
name|boolean
name|doNotNotifyCacheReplicators
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|doNotNotifyCacheReplicators
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|put (Element element)
specifier|public
name|void
name|put
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|put
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|putQuiet (Element element)
specifier|public
name|void
name|putQuiet
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|putQuiet
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerCacheExtension (CacheExtension cacheExtension)
specifier|public
name|void
name|registerCacheExtension
parameter_list|(
name|CacheExtension
name|cacheExtension
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|registerCacheExtension
argument_list|(
name|cacheExtension
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerCacheLoader (CacheLoader cacheLoader)
specifier|public
name|void
name|registerCacheLoader
parameter_list|(
name|CacheLoader
name|cacheLoader
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|registerCacheLoader
argument_list|(
name|cacheLoader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (Object key, boolean doNotNotifyCacheReplicators)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|key
parameter_list|,
name|boolean
name|doNotNotifyCacheReplicators
parameter_list|)
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|doNotNotifyCacheReplicators
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Object key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Serializable key, boolean doNotNotifyCacheReplicators)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Serializable
name|key
parameter_list|,
name|boolean
name|doNotNotifyCacheReplicators
parameter_list|)
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|doNotNotifyCacheReplicators
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Serializable key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Serializable
name|key
parameter_list|)
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll ()
specifier|public
name|void
name|removeAll
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeAll (boolean doNotNotifyCacheReplicators)
specifier|public
name|void
name|removeAll
parameter_list|(
name|boolean
name|doNotNotifyCacheReplicators
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|removeAll
argument_list|(
name|doNotNotifyCacheReplicators
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeQuiet (Object key)
specifier|public
name|boolean
name|removeQuiet
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|removeQuiet
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeQuiet (Serializable key)
specifier|public
name|boolean
name|removeQuiet
parameter_list|(
name|Serializable
name|key
parameter_list|)
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|removeQuiet
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setBootstrapCacheLoader (BootstrapCacheLoader bootstrapCacheLoader)
specifier|public
name|void
name|setBootstrapCacheLoader
parameter_list|(
name|BootstrapCacheLoader
name|bootstrapCacheLoader
parameter_list|)
throws|throws
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|setBootstrapCacheLoader
argument_list|(
name|bootstrapCacheLoader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setCacheExceptionHandler ( CacheExceptionHandler cacheExceptionHandler)
specifier|public
name|void
name|setCacheExceptionHandler
parameter_list|(
name|CacheExceptionHandler
name|cacheExceptionHandler
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|setCacheExceptionHandler
argument_list|(
name|cacheExceptionHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setCacheManager (CacheManager cacheManager)
specifier|public
name|void
name|setCacheManager
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setDisabled (boolean disabled)
specifier|public
name|void
name|setDisabled
parameter_list|(
name|boolean
name|disabled
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|setDisabled
argument_list|(
name|disabled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setDiskStorePath (String diskStorePath)
specifier|public
name|void
name|setDiskStorePath
parameter_list|(
name|String
name|diskStorePath
parameter_list|)
throws|throws
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|setDiskStorePath
argument_list|(
name|diskStorePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setStatisticsAccuracy (int statisticsAccuracy)
specifier|public
name|void
name|setStatisticsAccuracy
parameter_list|(
name|int
name|statisticsAccuracy
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|setStatisticsAccuracy
argument_list|(
name|statisticsAccuracy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unregisterCacheExtension (CacheExtension cacheExtension)
specifier|public
name|void
name|unregisterCacheExtension
parameter_list|(
name|CacheExtension
name|cacheExtension
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|unregisterCacheExtension
argument_list|(
name|cacheExtension
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unregisterCacheLoader (CacheLoader cacheLoader)
specifier|public
name|void
name|unregisterCacheLoader
parameter_list|(
name|CacheLoader
name|cacheLoader
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|unregisterCacheLoader
argument_list|(
name|cacheLoader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getInternalContext ()
specifier|public
name|Object
name|getInternalContext
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getInternalContext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLiveCacheStatistics ()
specifier|public
name|LiveCacheStatistics
name|getLiveCacheStatistics
parameter_list|()
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|getLiveCacheStatistics
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSampledCacheStatistics ()
specifier|public
name|SampledCacheStatistics
name|getSampledCacheStatistics
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getSampledCacheStatistics
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSizeBasedOnAccuracy (int statisticsAccuracy)
specifier|public
name|int
name|getSizeBasedOnAccuracy
parameter_list|(
name|int
name|statisticsAccuracy
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|getSizeBasedOnAccuracy
argument_list|(
name|statisticsAccuracy
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSampledStatisticsEnabled ()
specifier|public
name|boolean
name|isSampledStatisticsEnabled
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|isSampledStatisticsEnabled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|isStatisticsEnabled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|registerCacheUsageListener (CacheUsageListener cacheUsageListener)
specifier|public
name|void
name|registerCacheUsageListener
parameter_list|(
name|CacheUsageListener
name|cacheUsageListener
parameter_list|)
throws|throws
name|IllegalStateException
block|{
name|self
argument_list|()
operator|.
name|registerCacheUsageListener
argument_list|(
name|cacheUsageListener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeCacheUsageListener (CacheUsageListener cacheUsageListener)
specifier|public
name|void
name|removeCacheUsageListener
parameter_list|(
name|CacheUsageListener
name|cacheUsageListener
parameter_list|)
throws|throws
name|IllegalStateException
block|{
name|self
argument_list|()
operator|.
name|removeCacheUsageListener
argument_list|(
name|cacheUsageListener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSampledStatisticsEnabled (boolean enableStatistics)
specifier|public
name|void
name|setSampledStatisticsEnabled
parameter_list|(
name|boolean
name|enableStatistics
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|setSampledStatisticsEnabled
argument_list|(
name|enableStatistics
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setStatisticsEnabled (boolean enableStatistics)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|enableStatistics
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|setStatisticsEnabled
argument_list|(
name|enableStatistics
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|putWithWriter (Element element)
specifier|public
name|void
name|putWithWriter
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
argument_list|()
operator|.
name|putWithWriter
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|putIfAbsent (Element element)
specifier|public
name|Element
name|putIfAbsent
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|NullPointerException
block|{
return|return
name|self
argument_list|()
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeElement (Element element)
specifier|public
name|boolean
name|removeElement
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|NullPointerException
block|{
return|return
name|self
argument_list|()
operator|.
name|removeElement
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|replace (Element element, Element element1)
specifier|public
name|boolean
name|replace
parameter_list|(
name|Element
name|element
parameter_list|,
name|Element
name|element1
parameter_list|)
throws|throws
name|NullPointerException
throws|,
name|IllegalArgumentException
block|{
return|return
name|self
argument_list|()
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|element1
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|replace (Element element)
specifier|public
name|Element
name|replace
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|NullPointerException
block|{
return|return
name|self
argument_list|()
operator|.
name|replace
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeWithWriter (Object o)
specifier|public
name|boolean
name|removeWithWriter
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|removeWithWriter
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|calculateOffHeapSize ()
specifier|public
name|long
name|calculateOffHeapSize
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
return|return
name|self
argument_list|()
operator|.
name|calculateOffHeapSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOffHeapStoreSize ()
specifier|public
name|long
name|getOffHeapStoreSize
parameter_list|()
throws|throws
name|IllegalStateException
block|{
return|return
name|self
argument_list|()
operator|.
name|getOffHeapStoreSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|registerCacheWriter (CacheWriter cacheWriter)
specifier|public
name|void
name|registerCacheWriter
parameter_list|(
name|CacheWriter
name|cacheWriter
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|registerCacheWriter
argument_list|(
name|cacheWriter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unregisterCacheWriter ()
specifier|public
name|void
name|unregisterCacheWriter
parameter_list|()
block|{
name|self
argument_list|()
operator|.
name|unregisterCacheWriter
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRegisteredCacheWriter ()
specifier|public
name|CacheWriter
name|getRegisteredCacheWriter
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getRegisteredCacheWriter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|disableDynamicFeatures ()
specifier|public
name|void
name|disableDynamicFeatures
parameter_list|()
block|{
name|self
argument_list|()
operator|.
name|disableDynamicFeatures
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getWriterManager ()
specifier|public
name|CacheWriterManager
name|getWriterManager
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|getWriterManager
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isClusterCoherent ()
specifier|public
name|boolean
name|isClusterCoherent
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|isClusterCoherent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isNodeCoherent ()
specifier|public
name|boolean
name|isNodeCoherent
parameter_list|()
block|{
return|return
name|self
argument_list|()
operator|.
name|isNodeCoherent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setNodeCoherent (boolean b)
specifier|public
name|void
name|setNodeCoherent
parameter_list|(
name|boolean
name|b
parameter_list|)
throws|throws
name|UnsupportedOperationException
block|{
name|self
argument_list|()
operator|.
name|setNodeCoherent
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|waitUntilClusterCoherent ()
specifier|public
name|void
name|waitUntilClusterCoherent
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
name|self
argument_list|()
operator|.
name|waitUntilClusterCoherent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setTransactionManagerLookup (TransactionManagerLookup transactionManagerLookup)
specifier|public
name|void
name|setTransactionManagerLookup
parameter_list|(
name|TransactionManagerLookup
name|transactionManagerLookup
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|setTransactionManagerLookup
argument_list|(
name|transactionManagerLookup
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addPropertyChangeListener (PropertyChangeListener propertyChangeListener)
specifier|public
name|void
name|addPropertyChangeListener
parameter_list|(
name|PropertyChangeListener
name|propertyChangeListener
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|addPropertyChangeListener
argument_list|(
name|propertyChangeListener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removePropertyChangeListener (PropertyChangeListener propertyChangeListener)
specifier|public
name|void
name|removePropertyChangeListener
parameter_list|(
name|PropertyChangeListener
name|propertyChangeListener
parameter_list|)
block|{
name|self
argument_list|()
operator|.
name|removePropertyChangeListener
argument_list|(
name|propertyChangeListener
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

