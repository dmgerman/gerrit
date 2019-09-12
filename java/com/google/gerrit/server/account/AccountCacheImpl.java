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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|collect
operator|.
name|ImmutableMap
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
name|common
operator|.
name|Nullable
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|FanOutExecutor
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
name|util
operator|.
name|time
operator|.
name|TimeUtil
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
name|HashMap
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
name|Optional
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|Future
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
comment|/** Caches important (but small) account state to avoid database hits. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|AccountCacheImpl
specifier|public
class|class
name|AccountCacheImpl
implements|implements
name|AccountCache
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
DECL|field|BYID_NAME
specifier|private
specifier|static
specifier|final
name|String
name|BYID_NAME
init|=
literal|"accounts"
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
name|BYID_NAME
argument_list|,
name|Account
operator|.
name|Id
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|AccountState
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|loader
argument_list|(
name|ByIdLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AccountCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AccountCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|AccountCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|externalIds
specifier|private
specifier|final
name|ExternalIds
name|externalIds
decl_stmt|;
DECL|field|byId
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
name|byId
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountCacheImpl ( ExternalIds externalIds, @Named(BYID_NAME) LoadingCache<Account.Id, AccountState> byId, @FanOutExecutor ExecutorService executor)
name|AccountCacheImpl
parameter_list|(
name|ExternalIds
name|externalIds
parameter_list|,
annotation|@
name|Named
argument_list|(
name|BYID_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
name|byId
parameter_list|,
annotation|@
name|FanOutExecutor
name|ExecutorService
name|executor
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
name|byId
operator|=
name|byId
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEvenIfMissing (Account.Id accountId)
specifier|public
name|AccountState
name|getEvenIfMissing
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
try|try
block|{
return|return
name|byId
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|AccountNotFoundException
operator|)
condition|)
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
literal|"Cannot load AccountState for %s"
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
block|}
return|return
name|missing
argument_list|(
name|accountId
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (Account.Id accountId)
specifier|public
name|Optional
argument_list|<
name|AccountState
argument_list|>
name|get
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
try|try
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|byId
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|AccountNotFoundException
operator|)
condition|)
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
literal|"Cannot load AccountState for %s"
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (Set<Account.Id> accountIds)
specifier|public
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
name|get
parameter_list|(
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
parameter_list|)
block|{
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
name|accountStates
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|accountIds
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Callable
argument_list|<
name|Optional
argument_list|<
name|AccountState
argument_list|>
argument_list|>
argument_list|>
name|callables
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|accountId
range|:
name|accountIds
control|)
block|{
name|AccountState
name|state
init|=
name|byId
operator|.
name|getIfPresent
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
comment|// The value is in-memory, so we just get the state
name|accountStates
operator|.
name|put
argument_list|(
name|accountId
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Queue up a callable so that we can load accounts in parallel
name|callables
operator|.
name|add
argument_list|(
parameter_list|()
lambda|->
name|get
argument_list|(
name|accountId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|callables
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|accountStates
return|;
block|}
name|List
argument_list|<
name|Future
argument_list|<
name|Optional
argument_list|<
name|AccountState
argument_list|>
argument_list|>
argument_list|>
name|futures
decl_stmt|;
try|try
block|{
name|futures
operator|=
name|executor
operator|.
name|invokeAll
argument_list|(
name|callables
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
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
literal|"Cannot load AccountStates"
argument_list|)
expr_stmt|;
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
for|for
control|(
name|Future
argument_list|<
name|Optional
argument_list|<
name|AccountState
argument_list|>
argument_list|>
name|f
range|:
name|futures
control|)
block|{
try|try
block|{
name|f
operator|.
name|get
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|s
lambda|->
name|accountStates
operator|.
name|put
argument_list|(
name|s
operator|.
name|getAccount
argument_list|()
operator|.
name|id
argument_list|()
argument_list|,
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
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
literal|"Cannot load AccountState"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|accountStates
return|;
block|}
annotation|@
name|Override
DECL|method|getByUsername (String username)
specifier|public
name|Optional
argument_list|<
name|AccountState
argument_list|>
name|getByUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
try|try
block|{
return|return
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
operator|.
name|map
argument_list|(
name|e
lambda|->
name|get
argument_list|(
name|e
operator|.
name|accountId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|Optional
operator|::
name|empty
argument_list|)
return|;
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
literal|"Cannot load AccountState for username %s"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|evict (@ullable Account.Id accountId)
specifier|public
name|void
name|evict
parameter_list|(
annotation|@
name|Nullable
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
if|if
condition|(
name|accountId
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
literal|"Evict account %d"
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|byId
operator|.
name|invalidate
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|evictAll ()
specifier|public
name|void
name|evictAll
parameter_list|()
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Evict all accounts"
argument_list|)
expr_stmt|;
name|byId
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
block|}
DECL|method|missing (Account.Id accountId)
specifier|private
name|AccountState
name|missing
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|Account
operator|.
name|Builder
name|account
init|=
name|Account
operator|.
name|builder
argument_list|(
name|accountId
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|account
operator|.
name|setActive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|AccountState
operator|.
name|forAccount
argument_list|(
name|account
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|class|ByIdLoader
specifier|static
class|class
name|ByIdLoader
extends|extends
name|CacheLoader
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
block|{
DECL|field|accounts
specifier|private
specifier|final
name|Accounts
name|accounts
decl_stmt|;
annotation|@
name|Inject
DECL|method|ByIdLoader (Accounts accounts)
name|ByIdLoader
parameter_list|(
name|Accounts
name|accounts
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (Account.Id who)
specifier|public
name|AccountState
name|load
parameter_list|(
name|Account
operator|.
name|Id
name|who
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
literal|"Loading account"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|accountId
argument_list|(
name|who
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
init|)
block|{
return|return
name|accounts
operator|.
name|get
argument_list|(
name|who
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|AccountNotFoundException
argument_list|(
name|who
operator|+
literal|" not found"
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
comment|/** Signals that the account was not found in the primary storage. */
DECL|class|AccountNotFoundException
specifier|private
specifier|static
class|class
name|AccountNotFoundException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|AccountNotFoundException (String message)
specifier|public
name|AccountNotFoundException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

