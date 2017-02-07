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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|VIEW_CACHES
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|CommandMetaData
operator|.
name|Mode
operator|.
name|MASTER_OR_SLAVE
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
name|base
operator|.
name|Strings
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
name|common
operator|.
name|Version
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
name|annotations
operator|.
name|RequiresAnyCapability
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
name|config
operator|.
name|ConfigResource
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
name|GetSummary
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
name|GetSummary
operator|.
name|JvmSummaryInfo
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
name|GetSummary
operator|.
name|MemSummaryInfo
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
name|GetSummary
operator|.
name|SummaryInfo
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
name|GetSummary
operator|.
name|TaskSummaryInfo
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
name|GetSummary
operator|.
name|ThreadSummaryInfo
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
name|ListCaches
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
name|ListCaches
operator|.
name|CacheInfo
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
name|ListCaches
operator|.
name|CacheType
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
name|CommandMetaData
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
name|SshCommand
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
name|SshDaemon
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
name|Collection
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|common
operator|.
name|io
operator|.
name|IoAcceptor
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
name|common
operator|.
name|io
operator|.
name|IoSession
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
name|common
operator|.
name|io
operator|.
name|mina
operator|.
name|MinaSession
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

begin_comment
comment|/** Show the current cache states. */
end_comment

begin_class
annotation|@
name|RequiresAnyCapability
argument_list|(
block|{
name|VIEW_CACHES
block|,
name|MAINTAIN_SERVER
block|}
argument_list|)
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"show-caches"
argument_list|,
name|description
operator|=
literal|"Display current cache statistics"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|ShowCaches
specifier|final
class|class
name|ShowCaches
extends|extends
name|SshCommand
block|{
DECL|field|serverStarted
specifier|private
specifier|static
specifier|volatile
name|long
name|serverStarted
decl_stmt|;
DECL|class|StartupListener
specifier|static
class|class
name|StartupListener
implements|implements
name|LifecycleListener
block|{
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|serverStarted
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{}
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--gc"
argument_list|,
name|usage
operator|=
literal|"perform Java GC before printing memory stats"
argument_list|)
DECL|field|gc
specifier|private
name|boolean
name|gc
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--show-jvm"
argument_list|,
name|usage
operator|=
literal|"show details about the JVM"
argument_list|)
DECL|field|showJVM
specifier|private
name|boolean
name|showJVM
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--show-threads"
argument_list|,
name|usage
operator|=
literal|"show detailed thread counts"
argument_list|)
DECL|field|showThreads
specifier|private
name|boolean
name|showThreads
decl_stmt|;
DECL|field|daemon
annotation|@
name|Inject
specifier|private
name|SshDaemon
name|daemon
decl_stmt|;
DECL|field|listCaches
annotation|@
name|Inject
specifier|private
name|ListCaches
name|listCaches
decl_stmt|;
DECL|field|getSummary
annotation|@
name|Inject
specifier|private
name|GetSummary
name|getSummary
decl_stmt|;
DECL|field|self
annotation|@
name|Inject
specifier|private
name|CurrentUser
name|self
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--width"
argument_list|,
name|aliases
operator|=
block|{
literal|"-w"
block|}
argument_list|,
name|metaVar
operator|=
literal|"COLS"
argument_list|,
name|usage
operator|=
literal|"width of output table"
argument_list|)
DECL|field|columns
specifier|private
name|int
name|columns
init|=
literal|80
decl_stmt|;
DECL|field|nw
specifier|private
name|int
name|nw
decl_stmt|;
annotation|@
name|Override
DECL|method|start (Environment env)
specifier|public
name|void
name|start
parameter_list|(
name|Environment
name|env
parameter_list|)
throws|throws
name|IOException
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
name|super
operator|.
name|start
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|UnloggedFailure
block|{
name|nw
operator|=
name|columns
operator|-
literal|50
expr_stmt|;
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"%-25s %-20s      now  %16s\n"
argument_list|,
literal|"Gerrit Code Review"
argument_list|,
name|Version
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|?
name|Version
operator|.
name|getVersion
argument_list|()
else|:
literal|""
argument_list|,
operator|new
name|SimpleDateFormat
argument_list|(
literal|"HH:mm:ss   zzz"
argument_list|)
operator|.
name|format
argument_list|(
name|now
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"%-25s %-20s   uptime %16s\n"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
name|uptime
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
operator|-
name|serverStarted
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
comment|//
literal|"%1s %-"
operator|+
name|nw
operator|+
literal|"s|%-21s|  %-5s |%-9s|\n"
comment|//
argument_list|,
literal|""
comment|//
argument_list|,
literal|"Name"
comment|//
argument_list|,
literal|"Entries"
comment|//
argument_list|,
literal|"AvgGet"
comment|//
argument_list|,
literal|"Hit Ratio"
comment|//
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
comment|//
literal|"%1s %-"
operator|+
name|nw
operator|+
literal|"s|%6s %6s %7s|  %-5s  |%-4s %-4s|\n"
comment|//
argument_list|,
literal|""
comment|//
argument_list|,
literal|""
comment|//
argument_list|,
literal|"Mem"
comment|//
argument_list|,
literal|"Disk"
comment|//
argument_list|,
literal|"Space"
comment|//
argument_list|,
literal|""
comment|//
argument_list|,
literal|"Mem"
comment|//
argument_list|,
literal|"Disk"
comment|//
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|"--"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nw
condition|;
name|i
operator|++
control|)
block|{
name|stdout
operator|.
name|print
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
name|stdout
operator|.
name|print
argument_list|(
literal|"+---------------------+---------+---------+\n"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|CacheInfo
argument_list|>
name|caches
init|=
name|getCaches
argument_list|()
decl_stmt|;
name|printMemoryCoreCaches
argument_list|(
name|caches
argument_list|)
expr_stmt|;
name|printMemoryPluginCaches
argument_list|(
name|caches
argument_list|)
expr_stmt|;
name|printDiskCaches
argument_list|(
name|caches
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
if|if
condition|(
name|self
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canMaintainServer
argument_list|()
condition|)
block|{
name|sshSummary
argument_list|()
expr_stmt|;
name|SummaryInfo
name|summary
init|=
name|getSummary
operator|.
name|setGc
argument_list|(
name|gc
argument_list|)
operator|.
name|setJvm
argument_list|(
name|showJVM
argument_list|)
operator|.
name|apply
argument_list|(
operator|new
name|ConfigResource
argument_list|()
argument_list|)
decl_stmt|;
name|taskSummary
argument_list|(
name|summary
operator|.
name|taskSummary
argument_list|)
expr_stmt|;
name|memSummary
argument_list|(
name|summary
operator|.
name|memSummary
argument_list|)
expr_stmt|;
name|threadSummary
argument_list|(
name|summary
operator|.
name|threadSummary
argument_list|)
expr_stmt|;
if|if
condition|(
name|showJVM
operator|&&
name|summary
operator|.
name|jvmSummary
operator|!=
literal|null
condition|)
block|{
name|jvmSummary
argument_list|(
name|summary
operator|.
name|jvmSummary
argument_list|)
expr_stmt|;
block|}
block|}
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|getCaches ()
specifier|private
name|Collection
argument_list|<
name|CacheInfo
argument_list|>
name|getCaches
parameter_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|CacheInfo
argument_list|>
name|caches
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|CacheInfo
argument_list|>
operator|)
name|listCaches
operator|.
name|apply
argument_list|(
operator|new
name|ConfigResource
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|CacheInfo
argument_list|>
name|entry
range|:
name|caches
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|CacheInfo
name|cache
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|cache
operator|.
name|name
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
block|}
return|return
name|caches
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|printMemoryCoreCaches (Collection<CacheInfo> caches)
specifier|private
name|void
name|printMemoryCoreCaches
parameter_list|(
name|Collection
argument_list|<
name|CacheInfo
argument_list|>
name|caches
parameter_list|)
block|{
for|for
control|(
name|CacheInfo
name|cache
range|:
name|caches
control|)
block|{
if|if
condition|(
operator|!
name|cache
operator|.
name|name
operator|.
name|contains
argument_list|(
literal|"-"
argument_list|)
operator|&&
name|CacheType
operator|.
name|MEM
operator|.
name|equals
argument_list|(
name|cache
operator|.
name|type
argument_list|)
condition|)
block|{
name|printCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|printMemoryPluginCaches (Collection<CacheInfo> caches)
specifier|private
name|void
name|printMemoryPluginCaches
parameter_list|(
name|Collection
argument_list|<
name|CacheInfo
argument_list|>
name|caches
parameter_list|)
block|{
for|for
control|(
name|CacheInfo
name|cache
range|:
name|caches
control|)
block|{
if|if
condition|(
name|cache
operator|.
name|name
operator|.
name|contains
argument_list|(
literal|"-"
argument_list|)
operator|&&
name|CacheType
operator|.
name|MEM
operator|.
name|equals
argument_list|(
name|cache
operator|.
name|type
argument_list|)
condition|)
block|{
name|printCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|printDiskCaches (Collection<CacheInfo> caches)
specifier|private
name|void
name|printDiskCaches
parameter_list|(
name|Collection
argument_list|<
name|CacheInfo
argument_list|>
name|caches
parameter_list|)
block|{
for|for
control|(
name|CacheInfo
name|cache
range|:
name|caches
control|)
block|{
if|if
condition|(
name|CacheType
operator|.
name|DISK
operator|.
name|equals
argument_list|(
name|cache
operator|.
name|type
argument_list|)
condition|)
block|{
name|printCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|printCache (CacheInfo cache)
specifier|private
name|void
name|printCache
parameter_list|(
name|CacheInfo
name|cache
parameter_list|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%1s %-"
operator|+
name|nw
operator|+
literal|"s|%6s %6s %7s| %7s |%4s %4s|\n"
argument_list|,
name|CacheType
operator|.
name|DISK
operator|.
name|equals
argument_list|(
name|cache
operator|.
name|type
argument_list|)
condition|?
literal|"D"
else|:
literal|""
argument_list|,
name|cache
operator|.
name|name
argument_list|,
name|nullToEmpty
argument_list|(
name|cache
operator|.
name|entries
operator|.
name|mem
argument_list|)
argument_list|,
name|nullToEmpty
argument_list|(
name|cache
operator|.
name|entries
operator|.
name|disk
argument_list|)
argument_list|,
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|cache
operator|.
name|entries
operator|.
name|space
argument_list|)
argument_list|,
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|cache
operator|.
name|averageGet
argument_list|)
argument_list|,
name|formatAsPercent
argument_list|(
name|cache
operator|.
name|hitRatio
operator|.
name|mem
argument_list|)
argument_list|,
name|formatAsPercent
argument_list|(
name|cache
operator|.
name|hitRatio
operator|.
name|disk
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|nullToEmpty (Long l)
specifier|private
specifier|static
name|String
name|nullToEmpty
parameter_list|(
name|Long
name|l
parameter_list|)
block|{
return|return
name|l
operator|!=
literal|null
condition|?
name|String
operator|.
name|valueOf
argument_list|(
name|l
argument_list|)
else|:
literal|""
return|;
block|}
DECL|method|formatAsPercent (Integer i)
specifier|private
specifier|static
name|String
name|formatAsPercent
parameter_list|(
name|Integer
name|i
parameter_list|)
block|{
return|return
name|i
operator|!=
literal|null
condition|?
name|String
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
operator|+
literal|"%"
else|:
literal|""
return|;
block|}
DECL|method|memSummary (MemSummaryInfo memSummary)
specifier|private
name|void
name|memSummary
parameter_list|(
name|MemSummaryInfo
name|memSummary
parameter_list|)
block|{
name|stdout
operator|.
name|format
argument_list|(
literal|"Mem: %s total = %s used + %s free + %s buffers\n"
argument_list|,
name|memSummary
operator|.
name|total
argument_list|,
name|memSummary
operator|.
name|used
argument_list|,
name|memSummary
operator|.
name|free
argument_list|,
name|memSummary
operator|.
name|buffers
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"     %s max\n"
argument_list|,
name|memSummary
operator|.
name|max
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"    %8d open files\n"
argument_list|,
name|nullToZero
argument_list|(
name|memSummary
operator|.
name|openFiles
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
DECL|method|threadSummary (ThreadSummaryInfo threadSummary)
specifier|private
name|void
name|threadSummary
parameter_list|(
name|ThreadSummaryInfo
name|threadSummary
parameter_list|)
block|{
name|stdout
operator|.
name|format
argument_list|(
literal|"Threads: %d CPUs available, %d threads\n"
argument_list|,
name|threadSummary
operator|.
name|cpus
argument_list|,
name|threadSummary
operator|.
name|threads
argument_list|)
expr_stmt|;
if|if
condition|(
name|showThreads
condition|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %22s"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Thread
operator|.
name|State
name|s
range|:
name|Thread
operator|.
name|State
operator|.
name|values
argument_list|()
control|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|" %14s"
argument_list|,
name|s
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Thread
operator|.
name|State
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|e
range|:
name|threadSummary
operator|.
name|counts
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %-22s"
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Thread
operator|.
name|State
name|s
range|:
name|Thread
operator|.
name|State
operator|.
name|values
argument_list|()
control|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|" %14d"
argument_list|,
name|nullToZero
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
name|s
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
DECL|method|taskSummary (TaskSummaryInfo taskSummary)
specifier|private
name|void
name|taskSummary
parameter_list|(
name|TaskSummaryInfo
name|taskSummary
parameter_list|)
block|{
name|stdout
operator|.
name|format
argument_list|(
literal|"Tasks: %4d  total = %4d running +   %4d ready + %4d sleeping\n"
argument_list|,
name|nullToZero
argument_list|(
name|taskSummary
operator|.
name|total
argument_list|)
argument_list|,
name|nullToZero
argument_list|(
name|taskSummary
operator|.
name|running
argument_list|)
argument_list|,
name|nullToZero
argument_list|(
name|taskSummary
operator|.
name|ready
argument_list|)
argument_list|,
name|nullToZero
argument_list|(
name|taskSummary
operator|.
name|sleeping
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|nullToZero (Integer i)
specifier|private
specifier|static
name|int
name|nullToZero
parameter_list|(
name|Integer
name|i
parameter_list|)
block|{
return|return
name|i
operator|!=
literal|null
condition|?
name|i
else|:
literal|0
return|;
block|}
DECL|method|sshSummary ()
specifier|private
name|void
name|sshSummary
parameter_list|()
block|{
name|IoAcceptor
name|acceptor
init|=
name|daemon
operator|.
name|getIoAcceptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|acceptor
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|long
name|now
init|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|IoSession
argument_list|>
name|list
init|=
name|acceptor
operator|.
name|getManagedSessions
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|long
name|oldest
init|=
name|now
decl_stmt|;
for|for
control|(
name|IoSession
name|s
range|:
name|list
control|)
block|{
if|if
condition|(
name|s
operator|instanceof
name|MinaSession
condition|)
block|{
name|MinaSession
name|minaSession
init|=
operator|(
name|MinaSession
operator|)
name|s
decl_stmt|;
name|oldest
operator|=
name|Math
operator|.
name|min
argument_list|(
name|oldest
argument_list|,
name|minaSession
operator|.
name|getSession
argument_list|()
operator|.
name|getCreationTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|stdout
operator|.
name|format
argument_list|(
literal|"SSH:   %4d  users, oldest session started %s ago\n"
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|,
name|uptime
argument_list|(
name|now
operator|-
name|oldest
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|jvmSummary (JvmSummaryInfo jvmSummary)
specifier|private
name|void
name|jvmSummary
parameter_list|(
name|JvmSummaryInfo
name|jvmSummary
parameter_list|)
block|{
name|stdout
operator|.
name|format
argument_list|(
literal|"JVM: %s %s %s\n"
argument_list|,
name|jvmSummary
operator|.
name|vmVendor
argument_list|,
name|jvmSummary
operator|.
name|vmName
argument_list|,
name|jvmSummary
operator|.
name|vmVersion
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"  on %s %s %s\n"
argument_list|,
name|jvmSummary
operator|.
name|osName
argument_list|,
name|jvmSummary
operator|.
name|osVersion
argument_list|,
name|jvmSummary
operator|.
name|osArch
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"  running as %s on %s\n"
argument_list|,
name|jvmSummary
operator|.
name|user
argument_list|,
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|jvmSummary
operator|.
name|host
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"  cwd  %s\n"
argument_list|,
name|jvmSummary
operator|.
name|currentWorkingDirectory
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"  site %s\n"
argument_list|,
name|jvmSummary
operator|.
name|site
argument_list|)
expr_stmt|;
block|}
DECL|method|uptime (long uptimeMillis)
specifier|private
name|String
name|uptime
parameter_list|(
name|long
name|uptimeMillis
parameter_list|)
block|{
if|if
condition|(
name|uptimeMillis
operator|<
literal|1000
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%3d ms"
argument_list|,
name|uptimeMillis
argument_list|)
return|;
block|}
name|long
name|uptime
init|=
name|uptimeMillis
operator|/
literal|1000L
decl_stmt|;
name|long
name|min
init|=
name|uptime
operator|/
literal|60
decl_stmt|;
if|if
condition|(
name|min
operator|<
literal|60
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%2d min %2d sec"
argument_list|,
name|min
argument_list|,
name|uptime
operator|-
name|min
operator|*
literal|60
argument_list|)
return|;
block|}
name|long
name|hr
init|=
name|uptime
operator|/
literal|3600
decl_stmt|;
if|if
condition|(
name|hr
operator|<
literal|24
condition|)
block|{
name|min
operator|=
operator|(
name|uptime
operator|-
name|hr
operator|*
literal|3600
operator|)
operator|/
literal|60
expr_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%2d hrs %2d min"
argument_list|,
name|hr
argument_list|,
name|min
argument_list|)
return|;
block|}
name|long
name|days
init|=
name|uptime
operator|/
operator|(
literal|24
operator|*
literal|3600
operator|)
decl_stmt|;
name|hr
operator|=
operator|(
name|uptime
operator|-
operator|(
name|days
operator|*
literal|24
operator|*
literal|3600
operator|)
operator|)
operator|/
literal|3600
expr_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%4d days %2d hrs"
argument_list|,
name|days
argument_list|,
name|hr
argument_list|)
return|;
block|}
block|}
end_class

end_unit

