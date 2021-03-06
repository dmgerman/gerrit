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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
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
name|Multimap
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
name|MultimapBuilder
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
name|CurrentUser
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
name|IdentifiedUser
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
name|PeerDaemonUser
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
name|audit
operator|.
name|SshAuditEvent
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
name|ConfigKey
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
name|ConfigUpdatedEvent
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
name|ConfigUpdatedEvent
operator|.
name|ConfigUpdateEntry
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
name|ConfigUpdatedEvent
operator|.
name|UpdateResult
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
name|GerritConfigListener
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
name|group
operator|.
name|GroupAuditService
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
name|ioutil
operator|.
name|HexFormat
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
name|SystemLog
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
name|gerrit
operator|.
name|sshd
operator|.
name|SshScope
operator|.
name|Context
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
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|AsyncAppender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|spi
operator|.
name|LoggingEvent
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
DECL|class|SshLog
class|class
name|SshLog
implements|implements
name|LifecycleListener
implements|,
name|GerritConfigListener
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|SshLog
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LOG_NAME
specifier|private
specifier|static
specifier|final
name|String
name|LOG_NAME
init|=
literal|"sshd_log"
decl_stmt|;
DECL|field|P_SESSION
specifier|private
specifier|static
specifier|final
name|String
name|P_SESSION
init|=
literal|"session"
decl_stmt|;
DECL|field|P_USER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|P_USER_NAME
init|=
literal|"userName"
decl_stmt|;
DECL|field|P_ACCOUNT_ID
specifier|private
specifier|static
specifier|final
name|String
name|P_ACCOUNT_ID
init|=
literal|"accountId"
decl_stmt|;
DECL|field|P_WAIT
specifier|private
specifier|static
specifier|final
name|String
name|P_WAIT
init|=
literal|"queueWaitTime"
decl_stmt|;
DECL|field|P_EXEC
specifier|private
specifier|static
specifier|final
name|String
name|P_EXEC
init|=
literal|"executionTime"
decl_stmt|;
DECL|field|P_STATUS
specifier|private
specifier|static
specifier|final
name|String
name|P_STATUS
init|=
literal|"status"
decl_stmt|;
DECL|field|P_AGENT
specifier|private
specifier|static
specifier|final
name|String
name|P_AGENT
init|=
literal|"agent"
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|Provider
argument_list|<
name|SshSession
argument_list|>
name|session
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|Provider
argument_list|<
name|Context
argument_list|>
name|context
decl_stmt|;
DECL|field|async
specifier|private
specifier|volatile
name|AsyncAppender
name|async
decl_stmt|;
DECL|field|auditService
specifier|private
specifier|final
name|GroupAuditService
name|auditService
decl_stmt|;
DECL|field|systemLog
specifier|private
specifier|final
name|SystemLog
name|systemLog
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Object
name|lock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshLog ( final Provider<SshSession> session, final Provider<Context> context, SystemLog systemLog, @GerritServerConfig Config config, GroupAuditService auditService)
name|SshLog
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|SshSession
argument_list|>
name|session
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|Context
argument_list|>
name|context
parameter_list|,
name|SystemLog
name|systemLog
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|GroupAuditService
name|auditService
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|auditService
operator|=
name|auditService
expr_stmt|;
name|this
operator|.
name|systemLog
operator|=
name|systemLog
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getBoolean
argument_list|(
literal|"sshd"
argument_list|,
literal|"requestLog"
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|enableLogging
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** @return true if a change in state has occurred */
DECL|method|enableLogging ()
specifier|public
name|boolean
name|enableLogging
parameter_list|()
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
if|if
condition|(
name|async
operator|==
literal|null
condition|)
block|{
name|async
operator|=
name|systemLog
operator|.
name|createAsyncAppender
argument_list|(
name|LOG_NAME
argument_list|,
operator|new
name|SshLogLayout
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
comment|/** @return true if a change in state has occurred */
DECL|method|disableLogging ()
specifier|public
name|boolean
name|disableLogging
parameter_list|()
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
if|if
condition|(
name|async
operator|!=
literal|null
condition|)
block|{
name|async
operator|.
name|close
argument_list|()
expr_stmt|;
name|async
operator|=
literal|null
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|disableLogging
argument_list|()
expr_stmt|;
block|}
DECL|method|onLogin ()
name|void
name|onLogin
parameter_list|()
block|{
name|LoggingEvent
name|entry
init|=
name|log
argument_list|(
literal|"LOGIN FROM "
operator|+
name|session
operator|.
name|get
argument_list|()
operator|.
name|getRemoteAddressAsString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|async
operator|!=
literal|null
condition|)
block|{
name|async
operator|.
name|append
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|audit
argument_list|(
name|context
operator|.
name|get
argument_list|()
argument_list|,
literal|"0"
argument_list|,
literal|"LOGIN"
argument_list|)
expr_stmt|;
block|}
DECL|method|onAuthFail (SshSession sd)
name|void
name|onAuthFail
parameter_list|(
name|SshSession
name|sd
parameter_list|)
block|{
specifier|final
name|LoggingEvent
name|event
init|=
operator|new
name|LoggingEvent
argument_list|(
comment|//
name|Logger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
comment|// fqnOfCategoryClass
name|log
argument_list|,
comment|// logger
name|TimeUtil
operator|.
name|nowMs
argument_list|()
argument_list|,
comment|// when
name|Level
operator|.
name|INFO
argument_list|,
comment|// level
literal|"AUTH FAILURE FROM "
operator|+
name|sd
operator|.
name|getRemoteAddressAsString
argument_list|()
argument_list|,
comment|// message text
literal|"SSHD"
argument_list|,
comment|// thread name
literal|null
argument_list|,
comment|// exception information
literal|null
argument_list|,
comment|// current NDC string
literal|null
argument_list|,
comment|// caller location
literal|null
comment|// MDC properties
argument_list|)
decl_stmt|;
name|event
operator|.
name|setProperty
argument_list|(
name|P_SESSION
argument_list|,
name|id
argument_list|(
name|sd
operator|.
name|getSessionId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setProperty
argument_list|(
name|P_USER_NAME
argument_list|,
name|sd
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|error
init|=
name|sd
operator|.
name|getAuthenticationError
argument_list|()
decl_stmt|;
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|event
operator|.
name|setProperty
argument_list|(
name|P_STATUS
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|async
operator|!=
literal|null
condition|)
block|{
name|async
operator|.
name|append
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
name|audit
argument_list|(
literal|null
argument_list|,
literal|"FAIL"
argument_list|,
literal|"AUTH"
argument_list|)
expr_stmt|;
block|}
DECL|method|onExecute (DispatchCommand dcmd, int exitValue, SshSession sshSession)
name|void
name|onExecute
parameter_list|(
name|DispatchCommand
name|dcmd
parameter_list|,
name|int
name|exitValue
parameter_list|,
name|SshSession
name|sshSession
parameter_list|)
block|{
specifier|final
name|Context
name|ctx
init|=
name|context
operator|.
name|get
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|finished
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
expr_stmt|;
name|String
name|cmd
init|=
name|extractWhat
argument_list|(
name|dcmd
argument_list|)
decl_stmt|;
specifier|final
name|LoggingEvent
name|event
init|=
name|log
argument_list|(
name|cmd
argument_list|)
decl_stmt|;
name|event
operator|.
name|setProperty
argument_list|(
name|P_WAIT
argument_list|,
operator|(
name|ctx
operator|.
name|started
operator|-
name|ctx
operator|.
name|created
operator|)
operator|+
literal|"ms"
argument_list|)
expr_stmt|;
name|event
operator|.
name|setProperty
argument_list|(
name|P_EXEC
argument_list|,
operator|(
name|ctx
operator|.
name|finished
operator|-
name|ctx
operator|.
name|started
operator|)
operator|+
literal|"ms"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|status
decl_stmt|;
switch|switch
condition|(
name|exitValue
condition|)
block|{
case|case
name|BaseCommand
operator|.
name|STATUS_CANCEL
case|:
name|status
operator|=
literal|"killed"
expr_stmt|;
break|break;
case|case
name|BaseCommand
operator|.
name|STATUS_NOT_FOUND
case|:
name|status
operator|=
literal|"not-found"
expr_stmt|;
break|break;
case|case
name|BaseCommand
operator|.
name|STATUS_NOT_ADMIN
case|:
name|status
operator|=
literal|"not-admin"
expr_stmt|;
break|break;
default|default:
name|status
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|exitValue
argument_list|)
expr_stmt|;
break|break;
block|}
name|event
operator|.
name|setProperty
argument_list|(
name|P_STATUS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|String
name|peerAgent
init|=
name|sshSession
operator|.
name|getPeerAgent
argument_list|()
decl_stmt|;
if|if
condition|(
name|peerAgent
operator|!=
literal|null
condition|)
block|{
name|event
operator|.
name|setProperty
argument_list|(
name|P_AGENT
argument_list|,
name|peerAgent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|async
operator|!=
literal|null
condition|)
block|{
name|async
operator|.
name|append
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
name|audit
argument_list|(
name|context
operator|.
name|get
argument_list|()
argument_list|,
name|status
argument_list|,
name|dcmd
argument_list|)
expr_stmt|;
block|}
DECL|method|extractParameters (DispatchCommand dcmd)
specifier|private
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|extractParameters
parameter_list|(
name|DispatchCommand
name|dcmd
parameter_list|)
block|{
if|if
condition|(
name|dcmd
operator|==
literal|null
condition|)
block|{
return|return
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|(
literal|0
argument_list|)
operator|.
name|arrayListValues
argument_list|(
literal|0
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
name|String
index|[]
name|cmdArgs
init|=
name|dcmd
operator|.
name|getArguments
argument_list|()
decl_stmt|;
name|String
name|paramName
init|=
literal|null
decl_stmt|;
name|int
name|argPos
init|=
literal|0
decl_stmt|;
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parms
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<
name|cmdArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|arg
init|=
name|cmdArgs
index|[
name|i
index|]
decl_stmt|;
comment|// -- stop parameters parsing
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
literal|"--"
argument_list|)
condition|)
block|{
for|for
control|(
name|i
operator|++
init|;
name|i
operator|<
name|cmdArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|parms
operator|.
name|put
argument_list|(
literal|"$"
operator|+
name|argPos
operator|++
argument_list|,
name|cmdArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
comment|// --param=value
name|int
name|eqPos
init|=
name|arg
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"--"
argument_list|)
operator|&&
name|eqPos
operator|>
literal|0
condition|)
block|{
name|parms
operator|.
name|put
argument_list|(
name|arg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|eqPos
argument_list|)
argument_list|,
name|arg
operator|.
name|substring
argument_list|(
name|eqPos
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// -p value or --param value
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
if|if
condition|(
name|paramName
operator|!=
literal|null
condition|)
block|{
name|parms
operator|.
name|put
argument_list|(
name|paramName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|paramName
operator|=
name|arg
expr_stmt|;
continue|continue;
block|}
comment|// value
if|if
condition|(
name|paramName
operator|==
literal|null
condition|)
block|{
name|parms
operator|.
name|put
argument_list|(
literal|"$"
operator|+
name|argPos
operator|++
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parms
operator|.
name|put
argument_list|(
name|paramName
argument_list|,
name|arg
argument_list|)
expr_stmt|;
name|paramName
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|paramName
operator|!=
literal|null
condition|)
block|{
name|parms
operator|.
name|put
argument_list|(
name|paramName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|parms
return|;
block|}
DECL|method|onLogout ()
name|void
name|onLogout
parameter_list|()
block|{
name|LoggingEvent
name|entry
init|=
name|log
argument_list|(
literal|"LOGOUT"
argument_list|)
decl_stmt|;
if|if
condition|(
name|async
operator|!=
literal|null
condition|)
block|{
name|async
operator|.
name|append
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|audit
argument_list|(
name|context
operator|.
name|get
argument_list|()
argument_list|,
literal|"0"
argument_list|,
literal|"LOGOUT"
argument_list|)
expr_stmt|;
block|}
DECL|method|log (String msg)
specifier|private
name|LoggingEvent
name|log
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
specifier|final
name|SshSession
name|sd
init|=
name|session
operator|.
name|get
argument_list|()
decl_stmt|;
specifier|final
name|CurrentUser
name|user
init|=
name|sd
operator|.
name|getUser
argument_list|()
decl_stmt|;
specifier|final
name|LoggingEvent
name|event
init|=
operator|new
name|LoggingEvent
argument_list|(
comment|//
name|Logger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
comment|// fqnOfCategoryClass
name|log
argument_list|,
comment|// logger
name|TimeUtil
operator|.
name|nowMs
argument_list|()
argument_list|,
comment|// when
name|Level
operator|.
name|INFO
argument_list|,
comment|// level
name|msg
argument_list|,
comment|// message text
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
comment|// thread name
literal|null
argument_list|,
comment|// exception information
literal|null
argument_list|,
comment|// current NDC string
literal|null
argument_list|,
comment|// caller location
literal|null
comment|// MDC properties
argument_list|)
decl_stmt|;
name|event
operator|.
name|setProperty
argument_list|(
name|P_SESSION
argument_list|,
name|id
argument_list|(
name|sd
operator|.
name|getSessionId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|userName
init|=
literal|"-"
decl_stmt|;
name|String
name|accountId
init|=
literal|"-"
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
operator|&&
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|IdentifiedUser
name|u
init|=
name|user
operator|.
name|asIdentifiedUser
argument_list|()
decl_stmt|;
name|userName
operator|=
name|u
operator|.
name|getUserName
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|accountId
operator|=
literal|"a/"
operator|+
name|u
operator|.
name|getAccountId
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|user
operator|instanceof
name|PeerDaemonUser
condition|)
block|{
name|userName
operator|=
name|PeerDaemonUser
operator|.
name|USER_NAME
expr_stmt|;
block|}
name|event
operator|.
name|setProperty
argument_list|(
name|P_USER_NAME
argument_list|,
name|userName
argument_list|)
expr_stmt|;
name|event
operator|.
name|setProperty
argument_list|(
name|P_ACCOUNT_ID
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
return|return
name|event
return|;
block|}
DECL|method|id (int id)
specifier|private
specifier|static
name|String
name|id
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
name|HexFormat
operator|.
name|fromInt
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|audit (Context ctx, Object result, String cmd)
name|void
name|audit
parameter_list|(
name|Context
name|ctx
parameter_list|,
name|Object
name|result
parameter_list|,
name|String
name|cmd
parameter_list|)
block|{
name|audit
argument_list|(
name|ctx
argument_list|,
name|result
argument_list|,
name|cmd
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|audit (Context ctx, Object result, DispatchCommand cmd)
name|void
name|audit
parameter_list|(
name|Context
name|ctx
parameter_list|,
name|Object
name|result
parameter_list|,
name|DispatchCommand
name|cmd
parameter_list|)
block|{
name|audit
argument_list|(
name|ctx
argument_list|,
name|result
argument_list|,
name|extractWhat
argument_list|(
name|cmd
argument_list|)
argument_list|,
name|extractParameters
argument_list|(
name|cmd
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|audit (Context ctx, Object result, String cmd, ListMultimap<String, ?> params)
specifier|private
name|void
name|audit
parameter_list|(
name|Context
name|ctx
parameter_list|,
name|Object
name|result
parameter_list|,
name|String
name|cmd
parameter_list|,
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|params
parameter_list|)
block|{
name|String
name|sessionId
decl_stmt|;
name|CurrentUser
name|currentUser
decl_stmt|;
name|long
name|created
decl_stmt|;
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
name|sessionId
operator|=
literal|null
expr_stmt|;
name|currentUser
operator|=
literal|null
expr_stmt|;
name|created
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|SshSession
name|session
init|=
name|ctx
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|sessionId
operator|=
name|HexFormat
operator|.
name|fromInt
argument_list|(
name|session
operator|.
name|getSessionId
argument_list|()
argument_list|)
expr_stmt|;
name|currentUser
operator|=
name|session
operator|.
name|getUser
argument_list|()
expr_stmt|;
name|created
operator|=
name|ctx
operator|.
name|created
expr_stmt|;
block|}
name|auditService
operator|.
name|dispatch
argument_list|(
operator|new
name|SshAuditEvent
argument_list|(
name|sessionId
argument_list|,
name|currentUser
argument_list|,
name|cmd
argument_list|,
name|created
argument_list|,
name|params
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|extractWhat (DispatchCommand dcmd)
specifier|private
name|String
name|extractWhat
parameter_list|(
name|DispatchCommand
name|dcmd
parameter_list|)
block|{
if|if
condition|(
name|dcmd
operator|==
literal|null
condition|)
block|{
return|return
literal|"Command was already destroyed"
return|;
block|}
name|StringBuilder
name|commandName
init|=
operator|new
name|StringBuilder
argument_list|(
name|dcmd
operator|.
name|getCommandName
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
name|dcmd
operator|.
name|getArguments
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|commandName
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|commandName
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|configUpdated (ConfigUpdatedEvent event)
specifier|public
name|Multimap
argument_list|<
name|UpdateResult
argument_list|,
name|ConfigUpdateEntry
argument_list|>
name|configUpdated
parameter_list|(
name|ConfigUpdatedEvent
name|event
parameter_list|)
block|{
name|ConfigKey
name|sshdRequestLog
init|=
name|ConfigKey
operator|.
name|create
argument_list|(
literal|"sshd"
argument_list|,
literal|"requestLog"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|event
operator|.
name|isValueUpdated
argument_list|(
name|sshdRequestLog
argument_list|)
condition|)
block|{
return|return
name|ConfigUpdatedEvent
operator|.
name|NO_UPDATES
return|;
block|}
name|boolean
name|stateUpdated
decl_stmt|;
try|try
block|{
name|boolean
name|enabled
init|=
name|event
operator|.
name|getNewConfig
argument_list|()
operator|.
name|getBoolean
argument_list|(
literal|"sshd"
argument_list|,
literal|"requestLog"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|enabled
condition|)
block|{
name|stateUpdated
operator|=
name|enableLogging
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|stateUpdated
operator|=
name|disableLogging
argument_list|()
expr_stmt|;
block|}
return|return
name|stateUpdated
condition|?
name|event
operator|.
name|accept
argument_list|(
name|sshdRequestLog
argument_list|)
else|:
name|ConfigUpdatedEvent
operator|.
name|NO_UPDATES
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
return|return
name|event
operator|.
name|reject
argument_list|(
name|sshdRequestLog
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

