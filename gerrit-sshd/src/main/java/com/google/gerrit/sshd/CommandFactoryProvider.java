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
name|util
operator|.
name|concurrent
operator|.
name|Atomics
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
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactoryBuilder
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
name|reviewdb
operator|.
name|server
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
name|gwtorm
operator|.
name|server
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
name|sshd
operator|.
name|server
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|CommandFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Environment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|ExitCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|SessionAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|session
operator|.
name|ServerSession
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|List
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
name|Executors
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|atomic
operator|.
name|AtomicBoolean
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * Creates a CommandFactory using commands registered by {@link CommandModule}.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|CommandFactoryProvider
class|class
name|CommandFactoryProvider
implements|implements
name|Provider
argument_list|<
name|CommandFactory
argument_list|>
implements|,
name|LifecycleListener
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CommandFactoryProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dispatcher
specifier|private
specifier|final
name|DispatchCommandProvider
name|dispatcher
decl_stmt|;
DECL|field|log
specifier|private
specifier|final
name|SshLog
name|log
decl_stmt|;
DECL|field|sshScope
specifier|private
specifier|final
name|SshScope
name|sshScope
decl_stmt|;
DECL|field|startExecutor
specifier|private
specifier|final
name|ScheduledExecutorService
name|startExecutor
decl_stmt|;
DECL|field|destroyExecutor
specifier|private
specifier|final
name|ExecutorService
name|destroyExecutor
decl_stmt|;
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommandFactoryProvider ( @ommandNameCommands.ROOT) final DispatchCommandProvider d, @GerritServerConfig final Config cfg, final WorkQueue workQueue, final SshLog l, final SshScope s, SchemaFactory<ReviewDb> sf)
name|CommandFactoryProvider
parameter_list|(
annotation|@
name|CommandName
argument_list|(
name|Commands
operator|.
name|ROOT
argument_list|)
specifier|final
name|DispatchCommandProvider
name|d
parameter_list|,
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|,
specifier|final
name|WorkQueue
name|workQueue
parameter_list|,
specifier|final
name|SshLog
name|l
parameter_list|,
specifier|final
name|SshScope
name|s
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|)
block|{
name|dispatcher
operator|=
name|d
expr_stmt|;
name|log
operator|=
name|l
expr_stmt|;
name|sshScope
operator|=
name|s
expr_stmt|;
name|schemaFactory
operator|=
name|sf
expr_stmt|;
name|int
name|threads
init|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"sshd"
argument_list|,
literal|"commandStartThreads"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|startExecutor
operator|=
name|workQueue
operator|.
name|createQueue
argument_list|(
name|threads
argument_list|,
literal|"SshCommandStart"
argument_list|)
expr_stmt|;
name|destroyExecutor
operator|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|(
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setNameFormat
argument_list|(
literal|"SshCommandDestroy-%s"
argument_list|)
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
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
block|{   }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|destroyExecutor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|CommandFactory
name|get
parameter_list|()
block|{
return|return
operator|new
name|CommandFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Command
name|createCommand
parameter_list|(
specifier|final
name|String
name|requestCommand
parameter_list|)
block|{
return|return
operator|new
name|Trampoline
argument_list|(
name|requestCommand
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|class|Trampoline
specifier|private
class|class
name|Trampoline
implements|implements
name|Command
implements|,
name|SessionAware
block|{
DECL|field|commandLine
specifier|private
specifier|final
name|String
name|commandLine
decl_stmt|;
DECL|field|argv
specifier|private
specifier|final
name|String
index|[]
name|argv
decl_stmt|;
DECL|field|in
specifier|private
name|InputStream
name|in
decl_stmt|;
DECL|field|out
specifier|private
name|OutputStream
name|out
decl_stmt|;
DECL|field|err
specifier|private
name|OutputStream
name|err
decl_stmt|;
DECL|field|exit
specifier|private
name|ExitCallback
name|exit
decl_stmt|;
DECL|field|env
specifier|private
name|Environment
name|env
decl_stmt|;
DECL|field|ctx
specifier|private
name|Context
name|ctx
decl_stmt|;
DECL|field|cmd
specifier|private
name|DispatchCommand
name|cmd
decl_stmt|;
DECL|field|logged
specifier|private
specifier|final
name|AtomicBoolean
name|logged
decl_stmt|;
DECL|field|task
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|Future
argument_list|<
name|?
argument_list|>
argument_list|>
name|task
decl_stmt|;
DECL|method|Trampoline (final String cmdLine)
name|Trampoline
parameter_list|(
specifier|final
name|String
name|cmdLine
parameter_list|)
block|{
name|commandLine
operator|=
name|cmdLine
expr_stmt|;
name|argv
operator|=
name|split
argument_list|(
name|cmdLine
argument_list|)
expr_stmt|;
name|logged
operator|=
operator|new
name|AtomicBoolean
argument_list|()
expr_stmt|;
name|task
operator|=
name|Atomics
operator|.
name|newReference
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setInputStream (final InputStream in)
specifier|public
name|void
name|setInputStream
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setOutputStream (final OutputStream out)
specifier|public
name|void
name|setOutputStream
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setErrorStream (final OutputStream err)
specifier|public
name|void
name|setErrorStream
parameter_list|(
specifier|final
name|OutputStream
name|err
parameter_list|)
block|{
name|this
operator|.
name|err
operator|=
name|err
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setExitCallback (final ExitCallback callback)
specifier|public
name|void
name|setExitCallback
parameter_list|(
specifier|final
name|ExitCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|exit
operator|=
name|callback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSession (final ServerSession session)
specifier|public
name|void
name|setSession
parameter_list|(
specifier|final
name|ServerSession
name|session
parameter_list|)
block|{
specifier|final
name|SshSession
name|s
init|=
name|session
operator|.
name|getAttribute
argument_list|(
name|SshSession
operator|.
name|KEY
argument_list|)
decl_stmt|;
name|this
operator|.
name|ctx
operator|=
name|sshScope
operator|.
name|newContext
argument_list|(
name|schemaFactory
argument_list|,
name|s
argument_list|,
name|commandLine
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start (final Environment env)
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|Environment
name|env
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|env
operator|=
name|env
expr_stmt|;
specifier|final
name|Context
name|ctx
init|=
name|this
operator|.
name|ctx
decl_stmt|;
name|task
operator|.
name|set
argument_list|(
name|startExecutor
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|onStart
argument_list|()
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
name|warn
argument_list|(
literal|"Cannot start command \""
operator|+
name|ctx
operator|.
name|getCommandLine
argument_list|()
operator|+
literal|"\" for user "
operator|+
name|ctx
operator|.
name|getSession
argument_list|()
operator|.
name|getUsername
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"start (user "
operator|+
name|ctx
operator|.
name|getSession
argument_list|()
operator|.
name|getUsername
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|onStart ()
specifier|private
name|void
name|onStart
parameter_list|()
throws|throws
name|IOException
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
specifier|final
name|Context
name|old
init|=
name|sshScope
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
try|try
block|{
name|cmd
operator|=
name|dispatcher
operator|.
name|get
argument_list|()
expr_stmt|;
name|cmd
operator|.
name|setArguments
argument_list|(
name|argv
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setInputStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setOutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setErrorStream
argument_list|(
name|err
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setExitCallback
argument_list|(
operator|new
name|ExitCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onExit
parameter_list|(
name|int
name|rc
parameter_list|,
name|String
name|exitMessage
parameter_list|)
block|{
name|exit
operator|.
name|onExit
argument_list|(
name|translateExit
argument_list|(
name|rc
argument_list|)
argument_list|,
name|exitMessage
argument_list|)
expr_stmt|;
name|log
argument_list|(
name|rc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onExit
parameter_list|(
name|int
name|rc
parameter_list|)
block|{
name|exit
operator|.
name|onExit
argument_list|(
name|translateExit
argument_list|(
name|rc
argument_list|)
argument_list|)
expr_stmt|;
name|log
argument_list|(
name|rc
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|start
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|sshScope
operator|.
name|set
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|translateExit (final int rc)
specifier|private
name|int
name|translateExit
parameter_list|(
specifier|final
name|int
name|rc
parameter_list|)
block|{
switch|switch
condition|(
name|rc
condition|)
block|{
case|case
name|BaseCommand
operator|.
name|STATUS_NOT_ADMIN
case|:
return|return
literal|1
return|;
case|case
name|BaseCommand
operator|.
name|STATUS_CANCEL
case|:
return|return
literal|15
comment|/* SIGKILL */
return|;
case|case
name|BaseCommand
operator|.
name|STATUS_NOT_FOUND
case|:
return|return
literal|127
comment|/* POSIX not found */
return|;
default|default:
return|return
name|rc
return|;
block|}
block|}
DECL|method|log (final int rc)
specifier|private
name|void
name|log
parameter_list|(
specifier|final
name|int
name|rc
parameter_list|)
block|{
if|if
condition|(
name|logged
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|log
operator|.
name|onExecute
argument_list|(
name|cmd
argument_list|,
name|rc
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|task
operator|.
name|getAndSet
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|destroyExecutor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|onDestroy
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onDestroy ()
specifier|private
name|void
name|onDestroy
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|cmd
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Context
name|old
init|=
name|sshScope
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
try|try
block|{
name|cmd
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|log
argument_list|(
name|BaseCommand
operator|.
name|STATUS_CANCEL
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ctx
operator|=
literal|null
expr_stmt|;
name|cmd
operator|=
literal|null
expr_stmt|;
name|sshScope
operator|.
name|set
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/** Split a command line into a string array. */
DECL|method|split (String commandLine)
specifier|static
specifier|public
name|String
index|[]
name|split
parameter_list|(
name|String
name|commandLine
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|inquote
init|=
literal|false
decl_stmt|;
name|boolean
name|inDblQuote
init|=
literal|false
decl_stmt|;
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|ip
init|=
literal|0
init|;
name|ip
operator|<
name|commandLine
operator|.
name|length
argument_list|()
condition|;
control|)
block|{
specifier|final
name|char
name|b
init|=
name|commandLine
operator|.
name|charAt
argument_list|(
name|ip
operator|++
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|b
condition|)
block|{
case|case
literal|'\t'
case|:
case|case
literal|' '
case|:
if|if
condition|(
name|inquote
operator|||
name|inDblQuote
condition|)
name|r
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
elseif|else
if|if
condition|(
name|r
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
block|}
continue|continue;
case|case
literal|'\"'
case|:
if|if
condition|(
name|inquote
condition|)
name|r
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
else|else
name|inDblQuote
operator|=
operator|!
name|inDblQuote
expr_stmt|;
continue|continue;
case|case
literal|'\''
case|:
if|if
condition|(
name|inDblQuote
condition|)
name|r
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
else|else
name|inquote
operator|=
operator|!
name|inquote
expr_stmt|;
continue|continue;
case|case
literal|'\\'
case|:
if|if
condition|(
name|inquote
operator|||
name|ip
operator|==
name|commandLine
operator|.
name|length
argument_list|()
condition|)
name|r
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
comment|// literal within a quote
else|else
name|r
operator|.
name|append
argument_list|(
name|commandLine
operator|.
name|charAt
argument_list|(
name|ip
operator|++
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
default|default:
name|r
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
if|if
condition|(
name|r
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

