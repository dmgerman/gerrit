begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.codereview
package|package
name|com
operator|.
name|google
operator|.
name|codereview
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|Backend
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|ProjectSync
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|RepositoryCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|merge
operator|.
name|PendingMerger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|prune
operator|.
name|BuildPruner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|prune
operator|.
name|BundlePruner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|unpack
operator|.
name|ReceivedBundleUnpacker
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|rpc
operator|.
name|HttpRpc
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|Appender
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
name|ConsoleAppender
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
name|FileAppender
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
name|Layout
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
name|LogManager
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
name|PatternLayout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RepositoryConfig
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
name|FileNotFoundException
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|ScheduledThreadPoolExecutor
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
comment|/** Server startup, invoked from the command line. */
end_comment

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
literal|"main"
argument_list|)
decl_stmt|;
DECL|field|SEC_CODEREVIEW
specifier|private
specifier|static
specifier|final
name|String
name|SEC_CODEREVIEW
init|=
literal|"codereview"
decl_stmt|;
DECL|field|SEC_LOG
specifier|private
specifier|static
specifier|final
name|String
name|SEC_LOG
init|=
literal|"log"
decl_stmt|;
DECL|field|FOUR_HOURS
specifier|private
specifier|static
specifier|final
name|int
name|FOUR_HOURS
init|=
literal|4
operator|*
literal|60
operator|*
literal|60
decl_stmt|;
comment|// seconds
DECL|field|ONCE_PER_DAY
specifier|private
specifier|static
specifier|final
name|int
name|ONCE_PER_DAY
init|=
literal|24
operator|*
literal|60
operator|*
literal|60
decl_stmt|;
comment|// seconds
DECL|method|main (final String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: "
operator|+
name|Main
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" configfile"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|final
name|File
name|configPath
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
specifier|final
name|RepositoryConfig
name|config
init|=
operator|new
name|RepositoryConfig
argument_list|(
literal|null
argument_list|,
name|configPath
argument_list|)
decl_stmt|;
try|try
block|{
name|config
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: "
operator|+
name|configPath
operator|+
literal|" not found"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: "
operator|+
name|configPath
operator|+
literal|" not readable"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|configureLogging
argument_list|(
name|config
argument_list|,
name|args
operator|.
name|length
operator|>
literal|1
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Read "
operator|+
name|configPath
argument_list|)
expr_stmt|;
specifier|final
name|Main
name|me
init|=
operator|new
name|Main
argument_list|(
name|configPath
argument_list|,
name|config
argument_list|)
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|me
operator|.
name|addShutdownHook
argument_list|()
expr_stmt|;
name|me
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|String
name|cmd
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|cmd
operator|.
name|equals
argument_list|(
literal|"sync"
argument_list|)
condition|)
block|{
operator|new
name|ProjectSync
argument_list|(
name|me
operator|.
name|backend
argument_list|)
operator|.
name|sync
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: "
operator|+
name|cmd
operator|+
literal|" not recognized"
argument_list|)
expr_stmt|;
block|}
name|LogManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|field|config
specifier|private
specifier|final
name|RepositoryConfig
name|config
decl_stmt|;
DECL|field|pool
specifier|private
specifier|final
name|ScheduledThreadPoolExecutor
name|pool
decl_stmt|;
DECL|field|taskSleep
specifier|private
specifier|final
name|int
name|taskSleep
decl_stmt|;
DECL|field|backend
specifier|private
specifier|final
name|Backend
name|backend
decl_stmt|;
DECL|method|Main (final File configPath, final RepositoryConfig rc)
specifier|private
name|Main
parameter_list|(
specifier|final
name|File
name|configPath
parameter_list|,
specifier|final
name|RepositoryConfig
name|rc
parameter_list|)
block|{
name|config
operator|=
name|rc
expr_stmt|;
specifier|final
name|int
name|threads
init|=
name|config
operator|.
name|getInt
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|"threads"
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|taskSleep
operator|=
name|config
operator|.
name|getInt
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|"sleep"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting thread pool with "
operator|+
name|threads
operator|+
literal|" initial threads."
argument_list|)
expr_stmt|;
name|pool
operator|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
name|threads
argument_list|)
expr_stmt|;
specifier|final
name|RepositoryCache
name|repoCache
init|=
name|createRepositoryCache
argument_list|()
decl_stmt|;
specifier|final
name|HttpRpc
name|rpc
init|=
name|createHttpRpc
argument_list|(
name|configPath
operator|.
name|getParentFile
argument_list|()
argument_list|)
decl_stmt|;
name|backend
operator|=
operator|new
name|Backend
argument_list|(
name|repoCache
argument_list|,
name|rpc
argument_list|,
name|pool
argument_list|,
name|createUserPersonIdent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRepositoryCache ()
specifier|private
name|RepositoryCache
name|createRepositoryCache
parameter_list|()
block|{
specifier|final
name|File
name|basedir
init|=
operator|new
name|File
argument_list|(
name|required
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|"basedir"
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|RepositoryCache
argument_list|(
name|basedir
argument_list|)
return|;
block|}
DECL|method|createHttpRpc (final File base)
specifier|private
name|HttpRpc
name|createHttpRpc
parameter_list|(
specifier|final
name|File
name|base
parameter_list|)
throws|throws
name|ThreadDeath
block|{
specifier|final
name|URL
name|serverUrl
decl_stmt|;
try|try
block|{
name|serverUrl
operator|=
operator|new
name|URL
argument_list|(
name|required
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|"server"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|err
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: Bad URL in "
operator|+
name|SEC_CODEREVIEW
operator|+
literal|".server"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ThreadDeath
argument_list|()
throw|;
block|}
specifier|final
name|String
name|roleUser
init|=
name|config
operator|.
name|getString
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|null
argument_list|,
literal|"username"
argument_list|)
decl_stmt|;
name|File
name|pwf
init|=
operator|new
name|File
argument_list|(
name|required
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|"secureconfig"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pwf
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|pwf
operator|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
name|pwf
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|RepositoryConfig
name|pwc
init|=
operator|new
name|RepositoryConfig
argument_list|(
literal|null
argument_list|,
name|pwf
argument_list|)
decl_stmt|;
try|try
block|{
name|pwc
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: Cannot read secureconfig: "
operator|+
name|pwf
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ThreadDeath
argument_list|()
throw|;
block|}
specifier|final
name|String
name|rolePass
init|=
name|pwc
operator|.
name|getString
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|null
argument_list|,
literal|"password"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiKey
init|=
name|pwc
operator|.
name|getString
argument_list|(
name|SEC_CODEREVIEW
argument_list|,
literal|null
argument_list|,
literal|"internalapikey"
argument_list|)
decl_stmt|;
return|return
operator|new
name|HttpRpc
argument_list|(
name|serverUrl
argument_list|,
name|roleUser
argument_list|,
name|rolePass
argument_list|,
name|apiKey
argument_list|)
return|;
block|}
DECL|method|createUserPersonIdent ()
specifier|private
name|PersonIdent
name|createUserPersonIdent
parameter_list|()
block|{
specifier|final
name|String
name|name
init|=
name|required
argument_list|(
literal|"user"
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|email
init|=
name|required
argument_list|(
literal|"user"
argument_list|,
literal|"email"
argument_list|)
decl_stmt|;
return|return
operator|new
name|PersonIdent
argument_list|(
name|name
argument_list|,
name|email
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|addShutdownHook ()
specifier|private
name|void
name|addShutdownHook
parameter_list|()
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down thread pool."
argument_list|)
expr_stmt|;
name|pool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|boolean
name|isTerminated
decl_stmt|;
do|do
block|{
try|try
block|{
name|isTerminated
operator|=
name|pool
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
name|isTerminated
operator|=
literal|false
expr_stmt|;
block|}
block|}
do|while
condition|(
operator|!
name|isTerminated
condition|)
do|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Thread pool shutdown."
argument_list|)
expr_stmt|;
name|LogManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|start ()
specifier|private
name|void
name|start
parameter_list|()
block|{
name|schedule
argument_list|(
operator|new
name|ReceivedBundleUnpacker
argument_list|(
name|backend
argument_list|)
argument_list|)
expr_stmt|;
name|schedule
argument_list|(
operator|new
name|PendingMerger
argument_list|(
name|backend
argument_list|)
argument_list|)
expr_stmt|;
name|schedule
argument_list|(
operator|new
name|BundlePruner
argument_list|(
name|backend
argument_list|)
argument_list|,
name|FOUR_HOURS
argument_list|)
expr_stmt|;
name|schedule
argument_list|(
operator|new
name|BuildPruner
argument_list|(
name|backend
argument_list|)
argument_list|,
name|ONCE_PER_DAY
argument_list|)
expr_stmt|;
block|}
DECL|method|schedule (final Runnable t)
specifier|private
name|void
name|schedule
parameter_list|(
specifier|final
name|Runnable
name|t
parameter_list|)
block|{
name|schedule
argument_list|(
name|t
argument_list|,
name|taskSleep
argument_list|)
expr_stmt|;
block|}
DECL|method|schedule (final Runnable t, final int sleep)
specifier|private
name|void
name|schedule
parameter_list|(
specifier|final
name|Runnable
name|t
parameter_list|,
specifier|final
name|int
name|sleep
parameter_list|)
block|{
name|pool
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|t
argument_list|,
literal|0
argument_list|,
name|sleep
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|required (final String sec, final String key)
specifier|private
name|String
name|required
parameter_list|(
specifier|final
name|String
name|sec
parameter_list|,
specifier|final
name|String
name|key
parameter_list|)
block|{
specifier|final
name|String
name|r
init|=
name|config
operator|.
name|getString
argument_list|(
name|sec
argument_list|,
literal|null
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
operator|||
name|r
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: Missing required config "
operator|+
name|sec
operator|+
literal|"."
operator|+
name|key
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|configureLogging (final RepositoryConfig config, final boolean interactive)
specifier|private
specifier|static
name|void
name|configureLogging
parameter_list|(
specifier|final
name|RepositoryConfig
name|config
parameter_list|,
specifier|final
name|boolean
name|interactive
parameter_list|)
block|{
specifier|final
name|String
name|logfile
init|=
name|config
operator|.
name|getString
argument_list|(
name|SEC_LOG
argument_list|,
literal|null
argument_list|,
literal|"file"
argument_list|)
decl_stmt|;
specifier|final
name|Layout
name|layout
decl_stmt|;
specifier|final
name|Appender
name|out
decl_stmt|;
name|layout
operator|=
operator|new
name|PatternLayout
argument_list|(
literal|"%d{yyyyMMdd.HHmmss} %-5p %c - %m%n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|logfile
operator|!=
literal|null
operator|&&
operator|!
name|interactive
condition|)
block|{
try|try
block|{
name|out
operator|=
operator|new
name|FileAppender
argument_list|(
name|layout
argument_list|,
name|logfile
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"fatal: Cannot open log '"
operator|+
name|logfile
operator|+
literal|"': "
operator|+
name|err
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ThreadDeath
argument_list|()
throw|;
block|}
block|}
else|else
block|{
name|out
operator|=
operator|new
name|ConsoleAppender
argument_list|(
name|layout
argument_list|)
expr_stmt|;
block|}
name|LogManager
operator|.
name|getRootLogger
argument_list|()
operator|.
name|addAppender
argument_list|(
name|out
argument_list|)
expr_stmt|;
specifier|final
name|Level
name|levelObj
decl_stmt|;
specifier|final
name|String
name|levelStr
init|=
name|config
operator|.
name|getString
argument_list|(
name|SEC_LOG
argument_list|,
literal|null
argument_list|,
literal|"level"
argument_list|)
decl_stmt|;
if|if
condition|(
name|levelStr
operator|==
literal|null
operator|||
name|levelStr
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|levelObj
operator|=
name|Level
operator|.
name|INFO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"trace"
operator|.
name|equalsIgnoreCase
argument_list|(
name|levelStr
argument_list|)
condition|)
block|{
name|levelObj
operator|=
name|Level
operator|.
name|TRACE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"debug"
operator|.
name|equalsIgnoreCase
argument_list|(
name|levelStr
argument_list|)
condition|)
block|{
name|levelObj
operator|=
name|Level
operator|.
name|DEBUG
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"info"
operator|.
name|equalsIgnoreCase
argument_list|(
name|levelStr
argument_list|)
condition|)
block|{
name|levelObj
operator|=
name|Level
operator|.
name|INFO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"warning"
operator|.
name|equalsIgnoreCase
argument_list|(
name|levelStr
argument_list|)
operator|||
literal|"warn"
operator|.
name|equalsIgnoreCase
argument_list|(
name|levelStr
argument_list|)
condition|)
block|{
name|levelObj
operator|=
name|Level
operator|.
name|WARN
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"error"
operator|.
name|equalsIgnoreCase
argument_list|(
name|levelStr
argument_list|)
condition|)
block|{
name|levelObj
operator|=
name|Level
operator|.
name|ERROR
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"fatal"
operator|.
name|equalsIgnoreCase
argument_list|(
name|levelStr
argument_list|)
condition|)
block|{
name|levelObj
operator|=
name|Level
operator|.
name|FATAL
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"warning: Bad "
operator|+
name|SEC_LOG
operator|+
literal|".level "
operator|+
name|levelStr
operator|+
literal|"; assuming info"
argument_list|)
expr_stmt|;
name|levelObj
operator|=
name|Level
operator|.
name|INFO
expr_stmt|;
block|}
name|LogManager
operator|.
name|getRootLogger
argument_list|()
operator|.
name|setLevel
argument_list|(
name|levelObj
argument_list|)
expr_stmt|;
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"httpclient"
argument_list|)
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|WARN
argument_list|)
expr_stmt|;
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"org.apache.commons.httpclient"
argument_list|)
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|WARN
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

