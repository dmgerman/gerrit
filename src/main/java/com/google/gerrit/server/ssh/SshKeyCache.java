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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|net
operator|.
name|sf
operator|.
name|ehcache
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
name|constructs
operator|.
name|blocking
operator|.
name|SelfPopulatingCache
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
comment|/** Provides the {@link SshKeyCacheEntry}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|SshKeyCache
specifier|public
class|class
name|SshKeyCache
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|SelfPopulatingCache
name|self
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshKeyCache (final CacheManager mgr, final SchemaFactory<ReviewDb> db)
name|SshKeyCache
parameter_list|(
specifier|final
name|CacheManager
name|mgr
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
block|{
specifier|final
name|Cache
name|dc
init|=
name|mgr
operator|.
name|getCache
argument_list|(
literal|"sshkeys"
argument_list|)
decl_stmt|;
name|self
operator|=
operator|new
name|SelfPopulatingCache
argument_list|(
name|dc
argument_list|,
operator|new
name|SshKeyCacheEntryFactory
argument_list|(
name|db
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|replaceCacheWithDecoratedCache
argument_list|(
name|dc
argument_list|,
name|self
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|get (String username)
specifier|public
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|get
parameter_list|(
name|String
name|username
parameter_list|)
block|{
try|try
block|{
specifier|final
name|Element
name|e
init|=
name|self
operator|.
name|get
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
operator|||
name|e
operator|.
name|getObjectValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Can't get SSH keys for \""
operator|+
name|username
operator|+
literal|"\" from cache."
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
operator|(
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
operator|)
name|e
operator|.
name|getObjectValue
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Can't get SSH keys for \""
operator|+
name|username
operator|+
literal|"\" from cache."
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
DECL|method|evict (String username)
specifier|public
name|void
name|evict
parameter_list|(
name|String
name|username
parameter_list|)
block|{
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|self
operator|.
name|remove
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

