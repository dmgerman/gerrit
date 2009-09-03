begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
DECL|class|CacheProvider
specifier|final
class|class
name|CacheProvider
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Provider
argument_list|<
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
implements|,
name|NamedCacheBinding
implements|,
name|UnnamedCacheBinding
block|{
DECL|field|disk
specifier|private
specifier|final
name|boolean
name|disk
decl_stmt|;
DECL|field|memoryLimit
specifier|private
name|int
name|memoryLimit
init|=
literal|1024
decl_stmt|;
DECL|field|diskLimit
specifier|private
name|int
name|diskLimit
init|=
literal|16384
decl_stmt|;
DECL|field|timeToIdle
specifier|private
name|long
name|timeToIdle
init|=
name|DEFAULT_TIME
decl_stmt|;
DECL|field|timeToLive
specifier|private
name|long
name|timeToLive
init|=
name|DEFAULT_TIME
decl_stmt|;
DECL|field|evictionPolicy
specifier|private
name|EvictionPolicy
name|evictionPolicy
init|=
name|EvictionPolicy
operator|.
name|LFU
decl_stmt|;
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
DECL|field|cache
specifier|private
name|ProxyEhcache
name|cache
decl_stmt|;
DECL|method|CacheProvider (final boolean disk)
name|CacheProvider
parameter_list|(
specifier|final
name|boolean
name|disk
parameter_list|)
block|{
name|this
operator|.
name|disk
operator|=
name|disk
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|setCachePool (final CachePool pool)
name|void
name|setCachePool
parameter_list|(
specifier|final
name|CachePool
name|pool
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|pool
operator|.
name|register
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|bind (final Ehcache ehcache)
name|void
name|bind
parameter_list|(
specifier|final
name|Ehcache
name|ehcache
parameter_list|)
block|{
name|cache
operator|.
name|bind
argument_list|(
name|ehcache
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|cacheName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cache has no name"
argument_list|)
throw|;
block|}
return|return
name|cacheName
return|;
block|}
DECL|method|disk ()
name|boolean
name|disk
parameter_list|()
block|{
return|return
name|disk
return|;
block|}
DECL|method|memoryLimit ()
name|int
name|memoryLimit
parameter_list|()
block|{
return|return
name|memoryLimit
return|;
block|}
DECL|method|diskLimit ()
name|int
name|diskLimit
parameter_list|()
block|{
return|return
name|diskLimit
return|;
block|}
DECL|method|timeToIdle ()
name|long
name|timeToIdle
parameter_list|()
block|{
return|return
name|timeToIdle
return|;
block|}
DECL|method|timeToLive ()
name|long
name|timeToLive
parameter_list|()
block|{
return|return
name|timeToLive
return|;
block|}
DECL|method|evictionPolicy ()
name|EvictionPolicy
name|evictionPolicy
parameter_list|()
block|{
return|return
name|evictionPolicy
return|;
block|}
DECL|method|name (final String name)
specifier|public
name|NamedCacheBinding
name|name
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|cacheName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cache name already set"
argument_list|)
throw|;
block|}
name|cacheName
operator|=
name|name
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|memoryLimit (final int objects)
specifier|public
name|NamedCacheBinding
name|memoryLimit
parameter_list|(
specifier|final
name|int
name|objects
parameter_list|)
block|{
name|memoryLimit
operator|=
name|objects
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|diskLimit (final int objects)
specifier|public
name|NamedCacheBinding
name|diskLimit
parameter_list|(
specifier|final
name|int
name|objects
parameter_list|)
block|{
if|if
condition|(
operator|!
name|disk
condition|)
block|{
comment|// TODO This should really be a compile time type error, but I'm
comment|// too lazy to create the mess of permutations required to setup
comment|// type safe returns for bindings in our little DSL.
comment|//
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cache is not disk based"
argument_list|)
throw|;
block|}
name|diskLimit
operator|=
name|objects
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|timeToIdle (final long duration, final TimeUnit unit)
specifier|public
name|NamedCacheBinding
name|timeToIdle
parameter_list|(
specifier|final
name|long
name|duration
parameter_list|,
specifier|final
name|TimeUnit
name|unit
parameter_list|)
block|{
if|if
condition|(
name|timeToIdle
operator|>=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cache timeToIdle already set"
argument_list|)
throw|;
block|}
name|timeToIdle
operator|=
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|convert
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|timeToLive (final long duration, final TimeUnit unit)
specifier|public
name|NamedCacheBinding
name|timeToLive
parameter_list|(
specifier|final
name|long
name|duration
parameter_list|,
specifier|final
name|TimeUnit
name|unit
parameter_list|)
block|{
if|if
condition|(
name|timeToLive
operator|>=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cache timeToLive already set"
argument_list|)
throw|;
block|}
name|timeToLive
operator|=
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|convert
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|evictionPolicy (final EvictionPolicy policy)
specifier|public
name|NamedCacheBinding
name|evictionPolicy
parameter_list|(
specifier|final
name|EvictionPolicy
name|policy
parameter_list|)
block|{
name|evictionPolicy
operator|=
name|policy
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|get ()
specifier|public
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|get
parameter_list|()
block|{
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cache \""
operator|+
name|cacheName
operator|+
literal|"\" not available"
argument_list|)
throw|;
block|}
return|return
operator|new
name|SimpleCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|cache
argument_list|)
return|;
block|}
block|}
end_class

end_unit

