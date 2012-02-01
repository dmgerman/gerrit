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
DECL|package|com.google.gerrit.ehcache
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|ehcache
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
name|server
operator|.
name|cache
operator|.
name|Cache
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A fast in-memory and/or on-disk based cache.  *  * @type<K> type of key used to lookup entries in the cache.  * @type<V> type of value stored within each cache entry.  */
end_comment

begin_class
DECL|class|SimpleCache
specifier|final
class|class
name|SimpleCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SimpleCache
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Ehcache
name|self
decl_stmt|;
DECL|method|SimpleCache (final Ehcache self)
name|SimpleCache
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
DECL|method|getEhcache ()
name|Ehcache
name|getEhcache
parameter_list|()
block|{
return|return
name|self
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|get (final K key)
specifier|public
name|V
name|get
parameter_list|(
specifier|final
name|K
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Element
name|m
decl_stmt|;
try|try
block|{
name|m
operator|=
name|self
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot lookup "
operator|+
name|key
operator|+
literal|" in \""
operator|+
name|self
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|CacheException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot lookup "
operator|+
name|key
operator|+
literal|" in \""
operator|+
name|self
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|m
operator|!=
literal|null
condition|?
operator|(
name|V
operator|)
name|m
operator|.
name|getObjectValue
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|put (final K key, final V value)
specifier|public
name|void
name|put
parameter_list|(
specifier|final
name|K
name|key
parameter_list|,
specifier|final
name|V
name|value
parameter_list|)
block|{
name|self
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (final K key)
specifier|public
name|void
name|remove
parameter_list|(
specifier|final
name|K
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|self
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|removeAll ()
specifier|public
name|void
name|removeAll
parameter_list|()
block|{
name|self
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Cache["
operator|+
name|self
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

