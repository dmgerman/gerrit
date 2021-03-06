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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalId
operator|.
name|SCHEME_USERNAME
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
name|cache
operator|.
name|CacheLoader
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
name|cache
operator|.
name|LoadingCache
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
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|AccountSshKey
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
name|account
operator|.
name|VersionedAuthorizedKeys
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
name|account
operator|.
name|externalids
operator|.
name|ExternalId
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
name|account
operator|.
name|externalids
operator|.
name|ExternalIds
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
name|cache
operator|.
name|CacheModule
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
name|logging
operator|.
name|Metadata
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
name|logging
operator|.
name|TraceContext
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
name|logging
operator|.
name|TraceContext
operator|.
name|TraceTimer
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
name|ssh
operator|.
name|SshKeyCache
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
name|ssh
operator|.
name|SshKeyCreator
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
name|Module
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Optional
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
name|ExecutionException
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

begin_comment
comment|/** Provides the {@link SshKeyCacheEntry}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|SshKeyCacheImpl
specifier|public
class|class
name|SshKeyCacheImpl
implements|implements
name|SshKeyCache
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
DECL|field|CACHE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CACHE_NAME
init|=
literal|"sshkeys"
decl_stmt|;
DECL|field|NO_SUCH_USER
specifier|static
specifier|final
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|NO_SUCH_USER
init|=
name|none
argument_list|()
decl_stmt|;
DECL|field|NO_KEYS
specifier|static
specifier|final
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|NO_KEYS
init|=
name|none
argument_list|()
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|cache
argument_list|(
name|CACHE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|loader
argument_list|(
name|Loader
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshKeyCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshKeyCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SshKeyCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshKeyCreator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SshKeyCreatorImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|none ()
specifier|private
specifier|static
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|none
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|SshKeyCacheEntry
index|[
literal|0
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|field|cache
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshKeyCacheImpl (@amedCACHE_NAME) LoadingCache<String, Iterable<SshKeyCacheEntry>> cache)
name|SshKeyCacheImpl
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|get (String username)
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
return|return
name|cache
operator|.
name|get
argument_list|(
name|username
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot load SSH keys for %s"
argument_list|,
name|username
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
annotation|@
name|Override
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
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Evict SSH key for username %s"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|cache
operator|.
name|invalidate
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Loader
specifier|static
class|class
name|Loader
extends|extends
name|CacheLoader
argument_list|<
name|String
argument_list|,
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
argument_list|>
block|{
DECL|field|externalIds
specifier|private
specifier|final
name|ExternalIds
name|externalIds
decl_stmt|;
DECL|field|authorizedKeys
specifier|private
specifier|final
name|VersionedAuthorizedKeys
operator|.
name|Accessor
name|authorizedKeys
decl_stmt|;
annotation|@
name|Inject
DECL|method|Loader (ExternalIds externalIds, VersionedAuthorizedKeys.Accessor authorizedKeys)
name|Loader
parameter_list|(
name|ExternalIds
name|externalIds
parameter_list|,
name|VersionedAuthorizedKeys
operator|.
name|Accessor
name|authorizedKeys
parameter_list|)
block|{
name|this
operator|.
name|externalIds
operator|=
name|externalIds
expr_stmt|;
name|this
operator|.
name|authorizedKeys
operator|=
name|authorizedKeys
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (String username)
specifier|public
name|Iterable
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|load
parameter_list|(
name|String
name|username
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|TraceTimer
name|timer
init|=
name|TraceContext
operator|.
name|newTimer
argument_list|(
literal|"Loading SSH keys for account with username"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|username
argument_list|(
name|username
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
init|)
block|{
name|Optional
argument_list|<
name|ExternalId
argument_list|>
name|user
init|=
name|externalIds
operator|.
name|get
argument_list|(
name|ExternalId
operator|.
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_USERNAME
argument_list|,
name|username
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|user
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|NO_SUCH_USER
return|;
block|}
name|List
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|kl
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountSshKey
name|k
range|:
name|authorizedKeys
operator|.
name|getKeys
argument_list|(
name|user
operator|.
name|get
argument_list|()
operator|.
name|accountId
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|k
operator|.
name|valid
argument_list|()
condition|)
block|{
name|add
argument_list|(
name|kl
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|kl
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|NO_KEYS
return|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|kl
argument_list|)
return|;
block|}
block|}
DECL|method|add (List<SshKeyCacheEntry> kl, AccountSshKey k)
specifier|private
name|void
name|add
parameter_list|(
name|List
argument_list|<
name|SshKeyCacheEntry
argument_list|>
name|kl
parameter_list|,
name|AccountSshKey
name|k
parameter_list|)
block|{
try|try
block|{
name|kl
operator|.
name|add
argument_list|(
operator|new
name|SshKeyCacheEntry
argument_list|(
name|k
operator|.
name|accountId
argument_list|()
argument_list|,
name|SshUtil
operator|.
name|parse
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OutOfMemoryError
name|e
parameter_list|)
block|{
comment|// This is the only case where we assume the problem has nothing
comment|// to do with the key object, and instead we must abort this load.
comment|//
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|markInvalid
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|markInvalid (AccountSshKey k)
specifier|private
name|void
name|markInvalid
parameter_list|(
name|AccountSshKey
name|k
parameter_list|)
block|{
try|try
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Flagging SSH key %d of account %s invalid"
argument_list|,
name|k
operator|.
name|seq
argument_list|()
argument_list|,
name|k
operator|.
name|accountId
argument_list|()
argument_list|)
expr_stmt|;
name|authorizedKeys
operator|.
name|markKeyInvalid
argument_list|(
name|k
operator|.
name|accountId
argument_list|()
argument_list|,
name|k
operator|.
name|seq
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
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
literal|"Failed to mark SSH key %d of account %s invalid"
argument_list|,
name|k
operator|.
name|seq
argument_list|()
argument_list|,
name|k
operator|.
name|accountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

