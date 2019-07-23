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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|config
operator|.
name|ScheduleConfig
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
name|ScheduleConfig
operator|.
name|Schedule
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
name|git
operator|.
name|WorkQueue
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
name|account
operator|.
name|AccountPredicates
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
name|account
operator|.
name|InternalAccountQuery
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
name|Provider
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

begin_comment
comment|/** Runnable to enable scheduling account deactivations to run periodically */
end_comment

begin_class
DECL|class|AccountDeactivator
specifier|public
class|class
name|AccountDeactivator
implements|implements
name|Runnable
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
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|Lifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Lifecycle
specifier|static
class|class
name|Lifecycle
implements|implements
name|LifecycleListener
block|{
DECL|field|queue
specifier|private
specifier|final
name|WorkQueue
name|queue
decl_stmt|;
DECL|field|deactivator
specifier|private
specifier|final
name|AccountDeactivator
name|deactivator
decl_stmt|;
DECL|field|supportAutomaticAccountActivityUpdate
specifier|private
specifier|final
name|boolean
name|supportAutomaticAccountActivityUpdate
decl_stmt|;
DECL|field|schedule
specifier|private
specifier|final
name|Optional
argument_list|<
name|Schedule
argument_list|>
name|schedule
decl_stmt|;
annotation|@
name|Inject
DECL|method|Lifecycle (WorkQueue queue, AccountDeactivator deactivator, @GerritServerConfig Config cfg)
name|Lifecycle
parameter_list|(
name|WorkQueue
name|queue
parameter_list|,
name|AccountDeactivator
name|deactivator
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|deactivator
operator|=
name|deactivator
expr_stmt|;
name|schedule
operator|=
name|ScheduleConfig
operator|.
name|createSchedule
argument_list|(
name|cfg
argument_list|,
literal|"accountDeactivation"
argument_list|)
expr_stmt|;
name|supportAutomaticAccountActivityUpdate
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"autoUpdateAccountActiveStatus"
argument_list|,
literal|false
argument_list|)
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
if|if
condition|(
operator|!
name|supportAutomaticAccountActivityUpdate
condition|)
block|{
return|return;
block|}
name|schedule
operator|.
name|ifPresent
argument_list|(
name|s
lambda|->
name|queue
operator|.
name|scheduleAtFixedRate
argument_list|(
name|deactivator
argument_list|,
name|s
argument_list|)
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
comment|// handled by WorkQueue.stop() already
block|}
block|}
DECL|field|accountQueryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalAccountQuery
argument_list|>
name|accountQueryProvider
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|sif
specifier|private
specifier|final
name|SetInactiveFlag
name|sif
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountDeactivator ( Provider<InternalAccountQuery> accountQueryProvider, SetInactiveFlag sif, Realm realm)
name|AccountDeactivator
parameter_list|(
name|Provider
argument_list|<
name|InternalAccountQuery
argument_list|>
name|accountQueryProvider
parameter_list|,
name|SetInactiveFlag
name|sif
parameter_list|,
name|Realm
name|realm
parameter_list|)
block|{
name|this
operator|.
name|accountQueryProvider
operator|=
name|accountQueryProvider
expr_stmt|;
name|this
operator|.
name|sif
operator|=
name|sif
expr_stmt|;
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Running account deactivations"
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|numberOfAccountsDeactivated
init|=
literal|0
decl_stmt|;
for|for
control|(
name|AccountState
name|acc
range|:
name|accountQueryProvider
operator|.
name|get
argument_list|()
operator|.
name|query
argument_list|(
name|AccountPredicates
operator|.
name|isActive
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|processAccount
argument_list|(
name|acc
argument_list|)
condition|)
block|{
name|numberOfAccountsDeactivated
operator|++
expr_stmt|;
block|}
block|}
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Deactivations complete, %d account(s) were deactivated"
argument_list|,
name|numberOfAccountsDeactivated
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Failed to complete deactivation of accounts: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processAccount (AccountState accountState)
specifier|private
name|boolean
name|processAccount
parameter_list|(
name|AccountState
name|accountState
parameter_list|)
block|{
if|if
condition|(
operator|!
name|accountState
operator|.
name|getUserName
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|userName
init|=
name|accountState
operator|.
name|getUserName
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"processing account %s"
argument_list|,
name|userName
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|realm
operator|.
name|accountBelongsToRealm
argument_list|(
name|accountState
operator|.
name|getExternalIds
argument_list|()
argument_list|)
operator|&&
operator|!
name|realm
operator|.
name|isActive
argument_list|(
name|userName
argument_list|)
condition|)
block|{
name|sif
operator|.
name|deactivate
argument_list|(
name|accountState
operator|.
name|getAccount
argument_list|()
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"deactivated account %s"
argument_list|,
name|userName
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Account %s already deactivated, continuing..."
argument_list|,
name|userName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Error deactivating account: %s (%s) %s"
argument_list|,
name|userName
argument_list|,
name|accountState
operator|.
name|getAccount
argument_list|()
operator|.
name|id
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
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
literal|"account deactivator"
return|;
block|}
block|}
end_class

end_unit

