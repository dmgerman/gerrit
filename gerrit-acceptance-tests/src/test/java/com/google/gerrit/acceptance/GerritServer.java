begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|ImmutableList
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
name|lucene
operator|.
name|LuceneIndexModule
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
name|pgm
operator|.
name|Daemon
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
name|pgm
operator|.
name|Init
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
name|FactoryModule
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
name|index
operator|.
name|ChangeSchemas
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
name|SocketUtil
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
name|Injector
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
name|Key
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RepositoryCache
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
name|util
operator|.
name|FS
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
name|reflect
operator|.
name|Field
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
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|util
operator|.
name|concurrent
operator|.
name|BrokenBarrierException
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
name|CyclicBarrier
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
name|TimeUnit
import|;
end_import

begin_class
DECL|class|GerritServer
specifier|public
class|class
name|GerritServer
block|{
comment|/** Returns fully started Gerrit server */
DECL|method|start (Config cfg, boolean memory, boolean enableHttpd)
specifier|static
name|GerritServer
name|start
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|boolean
name|memory
parameter_list|,
name|boolean
name|enableHttpd
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CyclicBarrier
name|serverStarted
init|=
operator|new
name|CyclicBarrier
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|final
name|Daemon
name|daemon
init|=
operator|new
name|Daemon
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|serverStarted
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|BrokenBarrierException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
specifier|final
name|File
name|site
decl_stmt|;
name|ExecutorService
name|daemonService
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|memory
condition|)
block|{
name|site
operator|=
literal|null
expr_stmt|;
name|mergeTestConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"httpd"
argument_list|,
literal|null
argument_list|,
literal|"requestLog"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|,
literal|"requestLog"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"lucene"
argument_list|,
literal|"testInmemory"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"gitweb"
argument_list|,
literal|null
argument_list|,
literal|"cgi"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|daemon
operator|.
name|setEnableHttpd
argument_list|(
name|enableHttpd
argument_list|)
expr_stmt|;
name|daemon
operator|.
name|setLuceneModule
argument_list|(
operator|new
name|LuceneIndexModule
argument_list|(
name|ChangeSchemas
operator|.
name|getLatest
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|,
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|daemon
operator|.
name|setDatabaseForTesting
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Module
operator|>
name|of
argument_list|(
operator|new
name|InMemoryTestingDatabaseModule
argument_list|(
name|cfg
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|daemon
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|site
operator|=
name|initSite
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|daemonService
operator|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
expr_stmt|;
name|daemonService
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|rc
init|=
name|daemon
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-d"
block|,
name|site
operator|.
name|getPath
argument_list|()
block|,
literal|"--headless"
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Failed to start Gerrit daemon. Check "
operator|+
name|site
operator|.
name|getPath
argument_list|()
operator|+
literal|"/logs/error_log"
argument_list|)
expr_stmt|;
name|serverStarted
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|serverStarted
operator|.
name|await
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Gerrit Server Started"
argument_list|)
expr_stmt|;
block|}
name|Injector
name|i
init|=
name|createTestInjector
argument_list|(
name|daemon
argument_list|)
decl_stmt|;
return|return
operator|new
name|GerritServer
argument_list|(
name|site
argument_list|,
name|i
argument_list|,
name|daemon
argument_list|,
name|daemonService
argument_list|)
return|;
block|}
DECL|method|initSite (Config base)
specifier|private
specifier|static
name|File
name|initSite
parameter_list|(
name|Config
name|base
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|tmp
init|=
name|TempFileUtil
operator|.
name|createTempDirectory
argument_list|()
decl_stmt|;
name|Init
name|init
init|=
operator|new
name|Init
argument_list|()
decl_stmt|;
name|int
name|rc
init|=
name|init
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-d"
block|,
name|tmp
operator|.
name|getPath
argument_list|()
block|,
literal|"--batch"
block|,
literal|"--no-auto-start"
block|,
literal|"--skip-plugins"
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Couldn't initialize site"
argument_list|)
throw|;
block|}
name|MergeableFileBasedConfig
name|cfg
init|=
operator|new
name|MergeableFileBasedConfig
argument_list|(
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|tmp
argument_list|,
literal|"etc"
argument_list|)
argument_list|,
literal|"gerrit.config"
argument_list|)
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
name|cfg
operator|.
name|merge
argument_list|(
name|base
argument_list|)
expr_stmt|;
name|mergeTestConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|save
argument_list|()
expr_stmt|;
return|return
name|tmp
return|;
block|}
DECL|method|mergeTestConfig (Config cfg)
specifier|private
specifier|static
name|void
name|mergeTestConfig
parameter_list|(
name|Config
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|forceEphemeralPort
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s:0"
argument_list|,
name|getLocalHost
argument_list|()
operator|.
name|getHostName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|url
init|=
literal|"http://"
operator|+
name|forceEphemeralPort
operator|+
literal|"/"
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"canonicalWebUrl"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"httpd"
argument_list|,
literal|null
argument_list|,
literal|"listenUrl"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|,
literal|"listenAddress"
argument_list|,
name|forceEphemeralPort
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"cache"
argument_list|,
literal|null
argument_list|,
literal|"directory"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"enable"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
literal|"cache"
argument_list|,
literal|"projects"
argument_list|,
literal|"checkFrequency"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
literal|"plugins"
argument_list|,
literal|null
argument_list|,
literal|"checkFrequency"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|createTestInjector (Daemon daemon)
specifier|private
specifier|static
name|Injector
name|createTestInjector
parameter_list|(
name|Daemon
name|daemon
parameter_list|)
throws|throws
name|Exception
block|{
name|Injector
name|sysInjector
init|=
name|get
argument_list|(
name|daemon
argument_list|,
literal|"sysInjector"
argument_list|)
decl_stmt|;
name|Module
name|module
init|=
operator|new
name|FactoryModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|AccountCreator
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|PushOneCommit
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|module
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|get (Object obj, String field)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Object
name|obj
parameter_list|,
name|String
name|field
parameter_list|)
throws|throws
name|SecurityException
throws|,
name|NoSuchFieldException
throws|,
name|IllegalArgumentException
throws|,
name|IllegalAccessException
block|{
name|Field
name|f
init|=
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|f
operator|.
name|get
argument_list|(
name|obj
argument_list|)
return|;
block|}
DECL|method|getLocalHost ()
specifier|private
specifier|static
name|InetAddress
name|getLocalHost
parameter_list|()
throws|throws
name|UnknownHostException
block|{
return|return
name|InetAddress
operator|.
name|getLoopbackAddress
argument_list|()
return|;
block|}
DECL|field|sitePath
specifier|private
name|File
name|sitePath
decl_stmt|;
DECL|field|daemon
specifier|private
name|Daemon
name|daemon
decl_stmt|;
DECL|field|daemonService
specifier|private
name|ExecutorService
name|daemonService
decl_stmt|;
DECL|field|testInjector
specifier|private
name|Injector
name|testInjector
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|field|sshdAddress
specifier|private
name|InetSocketAddress
name|sshdAddress
decl_stmt|;
DECL|field|httpAddress
specifier|private
name|InetSocketAddress
name|httpAddress
decl_stmt|;
DECL|method|GerritServer (File sitePath, Injector testInjector, Daemon daemon, ExecutorService daemonService)
specifier|private
name|GerritServer
parameter_list|(
name|File
name|sitePath
parameter_list|,
name|Injector
name|testInjector
parameter_list|,
name|Daemon
name|daemon
parameter_list|,
name|ExecutorService
name|daemonService
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|this
operator|.
name|sitePath
operator|=
name|sitePath
expr_stmt|;
name|this
operator|.
name|testInjector
operator|=
name|testInjector
expr_stmt|;
name|this
operator|.
name|daemon
operator|=
name|daemon
expr_stmt|;
name|this
operator|.
name|daemonService
operator|=
name|daemonService
expr_stmt|;
name|Config
name|cfg
init|=
name|testInjector
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|Config
operator|.
name|class
argument_list|,
name|GerritServerConfig
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|url
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"canonicalWebUrl"
argument_list|)
expr_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|sshdAddress
operator|=
name|SocketUtil
operator|.
name|resolve
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|,
literal|"listenAddress"
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|httpAddress
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getUrl ()
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|getSshdAddress ()
name|InetSocketAddress
name|getSshdAddress
parameter_list|()
block|{
return|return
name|sshdAddress
return|;
block|}
DECL|method|getHttpAddress ()
name|InetSocketAddress
name|getHttpAddress
parameter_list|()
block|{
return|return
name|httpAddress
return|;
block|}
DECL|method|getTestInjector ()
name|Injector
name|getTestInjector
parameter_list|()
block|{
return|return
name|testInjector
return|;
block|}
DECL|method|stop ()
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|daemon
operator|.
name|getLifecycleManager
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|daemonService
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Gerrit Server Shutdown"
argument_list|)
expr_stmt|;
name|daemonService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|daemonService
operator|.
name|awaitTermination
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sitePath
operator|!=
literal|null
condition|)
block|{
name|TempFileUtil
operator|.
name|recursivelyDelete
argument_list|(
name|sitePath
argument_list|)
expr_stmt|;
block|}
name|RepositoryCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

