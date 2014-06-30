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
name|common
operator|.
name|collect
operator|.
name|HashBasedTable
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
name|Table
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|RequiresCapability
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
name|server
operator|.
name|config
operator|.
name|SitePath
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
name|util
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
name|eclipse
operator|.
name|jgit
operator|.
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|WindowCacheStatAccessor
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
name|File
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
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|OperatingSystemMXBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|RuntimeMXBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ThreadInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ThreadMXBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
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
name|Arrays
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

begin_comment
comment|/** Show the current cache states. */
end_comment

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_CACHES
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
block|{     }
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
annotation|@
name|Inject
DECL|field|workQueue
specifier|private
name|WorkQueue
name|workQueue
decl_stmt|;
annotation|@
name|Inject
DECL|field|daemon
specifier|private
name|SshDaemon
name|daemon
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|SitePath
DECL|field|sitePath
specifier|private
name|File
name|sitePath
decl_stmt|;
annotation|@
name|Inject
DECL|field|listCaches
specifier|private
name|Provider
argument_list|<
name|ListCaches
argument_list|>
name|listCaches
decl_stmt|;
annotation|@
name|Inject
DECL|field|self
specifier|private
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
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
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
condition|)
block|{
if|if
condition|(
name|gc
condition|)
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
block|}
name|sshSummary
argument_list|()
expr_stmt|;
name|taskSummary
argument_list|()
expr_stmt|;
name|memSummary
argument_list|()
expr_stmt|;
name|threadSummary
argument_list|()
expr_stmt|;
if|if
condition|(
name|showJVM
condition|)
block|{
name|jvmSummary
argument_list|()
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
name|get
argument_list|()
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
DECL|method|memSummary ()
specifier|private
name|void
name|memSummary
parameter_list|()
block|{
specifier|final
name|Runtime
name|r
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mMax
init|=
name|r
operator|.
name|maxMemory
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mFree
init|=
name|r
operator|.
name|freeMemory
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mTotal
init|=
name|r
operator|.
name|totalMemory
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mInuse
init|=
name|mTotal
operator|-
name|mFree
decl_stmt|;
specifier|final
name|int
name|jgitOpen
init|=
name|WindowCacheStatAccessor
operator|.
name|getOpenFiles
argument_list|()
decl_stmt|;
specifier|final
name|long
name|jgitBytes
init|=
name|WindowCacheStatAccessor
operator|.
name|getOpenBytes
argument_list|()
decl_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"Mem: %s total = %s used + %s free + %s buffers\n"
argument_list|,
name|bytes
argument_list|(
name|mTotal
argument_list|)
argument_list|,
name|bytes
argument_list|(
name|mInuse
operator|-
name|jgitBytes
argument_list|)
argument_list|,
name|bytes
argument_list|(
name|mFree
argument_list|)
argument_list|,
name|bytes
argument_list|(
name|jgitBytes
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"     %s max\n"
argument_list|,
name|bytes
argument_list|(
name|mMax
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"    %8d open files\n"
argument_list|,
name|jgitOpen
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
DECL|method|threadSummary ()
specifier|private
name|void
name|threadSummary
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"HTTP"
argument_list|,
literal|"IntraLineDiff"
argument_list|,
literal|"ReceiveCommits"
argument_list|,
literal|"SSH git-receive-pack"
argument_list|,
literal|"SSH git-upload-pack"
argument_list|,
literal|"SSH-Interactive-Worker"
argument_list|,
literal|"SSH-Stream-Worker"
argument_list|,
literal|"SshCommandStart"
argument_list|)
decl_stmt|;
name|String
name|other
init|=
literal|"Other"
decl_stmt|;
name|Runtime
name|r
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
decl_stmt|;
name|ThreadMXBean
name|threadMXBean
init|=
name|ManagementFactory
operator|.
name|getThreadMXBean
argument_list|()
decl_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"Threads: %d CPUs available, %d threads\n"
argument_list|,
name|r
operator|.
name|availableProcessors
argument_list|()
argument_list|,
name|threadMXBean
operator|.
name|getThreadCount
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|showThreads
condition|)
block|{
name|Table
argument_list|<
name|String
argument_list|,
name|Thread
operator|.
name|State
argument_list|,
name|Integer
argument_list|>
name|count
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|id
range|:
name|threadMXBean
operator|.
name|getAllThreadIds
argument_list|()
control|)
block|{
name|ThreadInfo
name|info
init|=
name|threadMXBean
operator|.
name|getThreadInfo
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|name
init|=
name|info
operator|.
name|getThreadName
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|State
name|state
init|=
name|info
operator|.
name|getThreadState
argument_list|()
decl_stmt|;
name|String
name|group
init|=
name|other
decl_stmt|;
for|for
control|(
name|String
name|p
range|:
name|prefixes
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|group
operator|=
name|p
expr_stmt|;
break|break;
block|}
block|}
name|Integer
name|c
init|=
name|count
operator|.
name|get
argument_list|(
name|group
argument_list|,
name|state
argument_list|)
decl_stmt|;
name|count
operator|.
name|put
argument_list|(
name|group
argument_list|,
name|state
argument_list|,
name|c
operator|!=
literal|null
condition|?
name|c
operator|+
literal|1
else|:
literal|1
argument_list|)
expr_stmt|;
block|}
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
name|String
name|p
range|:
name|prefixes
control|)
block|{
name|printThreadCounts
argument_list|(
name|p
argument_list|,
name|count
operator|.
name|row
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|printThreadCounts
argument_list|(
name|other
argument_list|,
name|count
operator|.
name|row
argument_list|(
name|other
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
DECL|method|printThreadCounts (String title, Map<Thread.State, Integer> counts)
specifier|private
name|void
name|printThreadCounts
parameter_list|(
name|String
name|title
parameter_list|,
name|Map
argument_list|<
name|Thread
operator|.
name|State
argument_list|,
name|Integer
argument_list|>
name|counts
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
literal|"  %-22s"
argument_list|,
name|title
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
name|counts
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
operator|.
name|intValue
argument_list|()
else|:
literal|0
return|;
block|}
DECL|method|taskSummary ()
specifier|private
name|void
name|taskSummary
parameter_list|()
block|{
name|Collection
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
name|int
name|tasksTotal
init|=
name|pending
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|tasksRunning
init|=
literal|0
decl_stmt|,
name|tasksReady
init|=
literal|0
decl_stmt|,
name|tasksSleeping
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Task
argument_list|<
name|?
argument_list|>
name|task
range|:
name|pending
control|)
block|{
switch|switch
condition|(
name|task
operator|.
name|getState
argument_list|()
condition|)
block|{
case|case
name|RUNNING
case|:
name|tasksRunning
operator|++
expr_stmt|;
break|break;
case|case
name|READY
case|:
name|tasksReady
operator|++
expr_stmt|;
break|break;
case|case
name|SLEEPING
case|:
name|tasksSleeping
operator|++
expr_stmt|;
break|break;
case|case
name|CANCELLED
case|:
case|case
name|DONE
case|:
case|case
name|OTHER
case|:
break|break;
block|}
block|}
name|stdout
operator|.
name|format
argument_list|(
literal|"Tasks: %4d  total = %4d running +   %4d ready + %4d sleeping\n"
argument_list|,
name|tasksTotal
argument_list|,
name|tasksRunning
argument_list|,
name|tasksReady
argument_list|,
name|tasksSleeping
argument_list|)
expr_stmt|;
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
DECL|method|jvmSummary ()
specifier|private
name|void
name|jvmSummary
parameter_list|()
block|{
name|OperatingSystemMXBean
name|osBean
init|=
name|ManagementFactory
operator|.
name|getOperatingSystemMXBean
argument_list|()
decl_stmt|;
name|RuntimeMXBean
name|runtimeBean
init|=
name|ManagementFactory
operator|.
name|getRuntimeMXBean
argument_list|()
decl_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"JVM: %s %s %s\n"
argument_list|,
name|runtimeBean
operator|.
name|getVmVendor
argument_list|()
argument_list|,
name|runtimeBean
operator|.
name|getVmName
argument_list|()
argument_list|,
name|runtimeBean
operator|.
name|getVmVersion
argument_list|()
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"  on %s %s %s\n"
argument_list|,
name|osBean
operator|.
name|getName
argument_list|()
argument_list|,
name|osBean
operator|.
name|getVersion
argument_list|()
argument_list|,
name|osBean
operator|.
name|getArch
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|stdout
operator|.
name|format
argument_list|(
literal|"  running as %s on %s\n"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
argument_list|,
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getHostName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{     }
name|stdout
operator|.
name|format
argument_list|(
literal|"  cwd  %s\n"
argument_list|,
name|path
argument_list|(
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|format
argument_list|(
literal|"  site %s\n"
argument_list|,
name|path
argument_list|(
name|sitePath
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|path (File file)
specifier|private
name|String
name|path
parameter_list|(
name|File
name|file
parameter_list|)
block|{
try|try
block|{
return|return
name|file
operator|.
name|getCanonicalPath
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
return|return
name|file
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
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
DECL|method|bytes (double value)
specifier|private
name|String
name|bytes
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|value
operator|/=
literal|1024
expr_stmt|;
name|String
name|suffix
init|=
literal|"k"
decl_stmt|;
if|if
condition|(
name|value
operator|>
literal|1024
condition|)
block|{
name|value
operator|/=
literal|1024
expr_stmt|;
name|suffix
operator|=
literal|"m"
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|>
literal|1024
condition|)
block|{
name|value
operator|/=
literal|1024
expr_stmt|;
name|suffix
operator|=
literal|"g"
expr_stmt|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%1$6.2f%2$s"
argument_list|,
name|value
argument_list|,
name|suffix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

