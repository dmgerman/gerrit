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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|reviewdb
operator|.
name|Project
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
name|git
operator|.
name|WorkQueue
operator|.
name|ProjectTask
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
operator|.
name|Task
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectState
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
name|IdGenerator
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
name|AdminHighPriorityCommand
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
name|BaseCommand
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/** Display the current work queue. */
end_comment

begin_class
annotation|@
name|AdminHighPriorityCommand
DECL|class|ShowQueue
specifier|final
class|class
name|ShowQueue
extends|extends
name|BaseCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-w"
argument_list|,
name|usage
operator|=
literal|"display without line width truncation"
argument_list|)
DECL|field|wide
specifier|private
name|boolean
name|wide
decl_stmt|;
annotation|@
name|Inject
DECL|field|workQueue
specifier|private
name|WorkQueue
name|workQueue
decl_stmt|;
annotation|@
name|Inject
DECL|field|projectCache
specifier|private
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|field|userProvider
specifier|private
name|CurrentUser
name|userProvider
decl_stmt|;
DECL|field|p
specifier|private
name|PrintWriter
name|p
decl_stmt|;
DECL|field|columns
specifier|private
name|int
name|columns
init|=
literal|80
decl_stmt|;
DECL|field|taskNameWidth
specifier|private
name|int
name|taskNameWidth
decl_stmt|;
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
block|{
name|String
name|s
init|=
name|env
operator|.
name|getEnv
argument_list|()
operator|.
name|get
argument_list|(
name|Environment
operator|.
name|ENV_COLUMNS
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
operator|!
name|s
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|columns
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|err
parameter_list|)
block|{
name|columns
operator|=
literal|80
expr_stmt|;
block|}
block|}
name|startThread
argument_list|(
operator|new
name|CommandRunnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|parseCommandLine
argument_list|()
expr_stmt|;
name|ShowQueue
operator|.
name|this
operator|.
name|display
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|display ()
specifier|private
name|void
name|display
parameter_list|()
block|{
name|p
operator|=
name|toPrintWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Task
argument_list|<
name|?
argument_list|>
argument_list|>
name|pending
init|=
name|workQueue
operator|.
name|getTasks
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|pending
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Task
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Task
argument_list|<
name|?
argument_list|>
name|a
parameter_list|,
name|Task
argument_list|<
name|?
argument_list|>
name|b
parameter_list|)
block|{
specifier|final
name|Task
operator|.
name|State
name|aState
init|=
name|a
operator|.
name|getState
argument_list|()
decl_stmt|;
specifier|final
name|Task
operator|.
name|State
name|bState
init|=
name|b
operator|.
name|getState
argument_list|()
decl_stmt|;
if|if
condition|(
name|aState
operator|!=
name|bState
condition|)
block|{
return|return
name|aState
operator|.
name|ordinal
argument_list|()
operator|-
name|bState
operator|.
name|ordinal
argument_list|()
return|;
block|}
specifier|final
name|long
name|aDelay
init|=
name|a
operator|.
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
specifier|final
name|long
name|bDelay
init|=
name|b
operator|.
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|aDelay
operator|<
name|bDelay
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|aDelay
operator|>
name|bDelay
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
name|format
argument_list|(
name|a
argument_list|)
operator|.
name|compareTo
argument_list|(
name|format
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
block|}
block|)
function|;
name|taskNameWidth
operator|=
name|wide
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|columns
operator|-
literal|8
operator|-
literal|12
operator|-
literal|8
operator|-
literal|4
expr_stmt|;
name|p
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%-8s %-12s %-8s %s\n"
argument_list|,
comment|//
literal|"Task"
argument_list|,
literal|"State"
argument_list|,
literal|""
argument_list|,
literal|"Command"
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|print
argument_list|(
literal|"----------------------------------------------"
operator|+
literal|"--------------------------------\n"
argument_list|)
expr_stmt|;
name|int
name|numberOfPendingTasks
init|=
literal|0
decl_stmt|;
specifier|final
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|isAdministrator
init|=
name|userProvider
operator|.
name|isAdministrator
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Task
argument_list|<
name|?
argument_list|>
name|task
range|:
name|pending
control|)
block|{
specifier|final
name|long
name|delay
init|=
name|task
operator|.
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
specifier|final
name|Task
operator|.
name|State
name|state
init|=
name|task
operator|.
name|getState
argument_list|()
decl_stmt|;
specifier|final
name|String
name|start
decl_stmt|;
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|DONE
case|:
case|case
name|CANCELLED
case|:
case|case
name|RUNNING
case|:
case|case
name|READY
case|:
name|start
operator|=
name|format
argument_list|(
name|state
argument_list|)
expr_stmt|;
break|break;
default|default:
name|start
operator|=
name|time
argument_list|(
name|now
argument_list|,
name|delay
argument_list|)
expr_stmt|;
break|break;
block|}
name|boolean
name|regularUserCanSee
init|=
literal|false
decl_stmt|;
name|boolean
name|hasCustomizedPrint
init|=
literal|true
decl_stmt|;
comment|// If the user is not administrator, check if has rights to see
comment|// the Task
name|Project
operator|.
name|NameKey
name|projectName
init|=
literal|null
decl_stmt|;
name|String
name|remoteName
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|isAdministrator
condition|)
block|{
if|if
condition|(
name|task
operator|instanceof
name|ProjectTask
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|projectName
operator|=
operator|(
operator|(
name|ProjectTask
argument_list|<
name|?
argument_list|>
operator|)
name|task
operator|)
operator|.
name|getProjectNameKey
argument_list|()
expr_stmt|;
name|remoteName
operator|=
operator|(
operator|(
name|ProjectTask
argument_list|<
name|?
argument_list|>
operator|)
name|task
operator|)
operator|.
name|getRemoteName
argument_list|()
expr_stmt|;
name|hasCustomizedPrint
operator|=
operator|(
operator|(
name|ProjectTask
argument_list|<
name|?
argument_list|>
operator|)
name|task
operator|)
operator|.
name|hasCustomizedPrint
argument_list|()
expr_stmt|;
block|}
name|ProjectState
name|e
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|projectName
operator|!=
literal|null
condition|)
block|{
name|e
operator|=
name|projectCache
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
block|}
name|regularUserCanSee
operator|=
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|controlFor
argument_list|(
name|userProvider
argument_list|)
operator|.
name|isVisible
argument_list|()
expr_stmt|;
if|if
condition|(
name|regularUserCanSee
condition|)
block|{
name|numberOfPendingTasks
operator|++
expr_stmt|;
block|}
block|}
comment|// Shows information about tasks depending on the user rights
if|if
condition|(
name|isAdministrator
operator|||
operator|(
operator|!
name|hasCustomizedPrint
operator|&&
name|regularUserCanSee
operator|)
condition|)
block|{
name|p
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%8s %-12s %-8s %s\n"
argument_list|,
comment|//
name|id
argument_list|(
name|task
operator|.
name|getTaskId
argument_list|()
argument_list|)
argument_list|,
name|start
argument_list|,
literal|""
argument_list|,
name|format
argument_list|(
name|task
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|regularUserCanSee
condition|)
block|{
if|if
condition|(
name|remoteName
operator|==
literal|null
condition|)
block|{
name|remoteName
operator|=
name|projectName
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|remoteName
operator|=
name|remoteName
operator|+
literal|"/"
operator|+
name|projectName
expr_stmt|;
block|}
name|p
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%8s %-12s %-8s %s\n"
argument_list|,
comment|//
name|id
argument_list|(
name|task
operator|.
name|getTaskId
argument_list|()
argument_list|)
argument_list|,
name|start
argument_list|,
literal|""
argument_list|,
name|remoteName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|p
operator|.
name|print
argument_list|(
literal|"----------------------------------------------"
operator|+
literal|"--------------------------------\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isAdministrator
condition|)
block|{
name|numberOfPendingTasks
operator|=
name|pending
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
name|p
operator|.
name|print
argument_list|(
literal|"  "
operator|+
name|numberOfPendingTasks
operator|+
literal|" tasks\n"
argument_list|)
expr_stmt|;
name|p
operator|.
name|flush
parameter_list|()
constructor_decl|;
block|}
end_class

begin_function
DECL|method|id (final int id)
specifier|private
specifier|static
name|String
name|id
parameter_list|(
specifier|final
name|int
name|id
parameter_list|)
block|{
return|return
name|IdGenerator
operator|.
name|format
argument_list|(
name|id
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|time (final long now, final long delay)
specifier|private
specifier|static
name|String
name|time
parameter_list|(
specifier|final
name|long
name|now
parameter_list|,
specifier|final
name|long
name|delay
parameter_list|)
block|{
specifier|final
name|Date
name|when
init|=
operator|new
name|Date
argument_list|(
name|now
operator|+
name|delay
argument_list|)
decl_stmt|;
if|if
condition|(
name|delay
operator|<
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
condition|)
block|{
return|return
operator|new
name|SimpleDateFormat
argument_list|(
literal|"HH:mm:ss.SSS"
argument_list|)
operator|.
name|format
argument_list|(
name|when
argument_list|)
return|;
block|}
return|return
operator|new
name|SimpleDateFormat
argument_list|(
literal|"MMM-dd HH:mm"
argument_list|)
operator|.
name|format
argument_list|(
name|when
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|format (final Task<?> task)
specifier|private
name|String
name|format
parameter_list|(
specifier|final
name|Task
argument_list|<
name|?
argument_list|>
name|task
parameter_list|)
block|{
name|String
name|s
init|=
name|task
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|<
name|taskNameWidth
condition|)
block|{
return|return
name|s
return|;
block|}
else|else
block|{
return|return
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|taskNameWidth
argument_list|)
return|;
block|}
block|}
end_function

begin_function
DECL|method|format (final Task.State state)
specifier|private
specifier|static
name|String
name|format
parameter_list|(
specifier|final
name|Task
operator|.
name|State
name|state
parameter_list|)
block|{
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|DONE
case|:
return|return
literal|"....... done"
return|;
case|case
name|CANCELLED
case|:
return|return
literal|"..... killed"
return|;
case|case
name|RUNNING
case|:
return|return
literal|""
return|;
case|case
name|READY
case|:
return|return
literal|"waiting ...."
return|;
case|case
name|SLEEPING
case|:
return|return
literal|"sleeping"
return|;
default|default:
return|return
name|state
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_function

unit|}
end_unit

