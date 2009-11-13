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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|httpd
operator|.
name|HttpCanonicalWebUrlProvider
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
name|httpd
operator|.
name|WebModule
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
name|LifecycleManager
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
name|http
operator|.
name|jetty
operator|.
name|JettyEnv
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
name|http
operator|.
name|jetty
operator|.
name|JettyModule
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
name|AuthConfigModule
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
name|CanonicalWebUrlModule
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
name|CanonicalWebUrlProvider
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
name|GerritGlobalModule
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
name|MasterNodeStartup
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
name|SshModule
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
name|commands
operator|.
name|MasterCommandModule
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
name|commands
operator|.
name|SlaveCommandModule
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
name|Provider
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_comment
comment|/** Run SSH daemon portions of Gerrit. */
end_comment

begin_class
DECL|class|Daemon
specifier|public
class|class
name|Daemon
extends|extends
name|SiteProgram
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Daemon
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--enable-httpd"
argument_list|,
name|usage
operator|=
literal|"Enable the internal HTTP daemon"
argument_list|)
DECL|field|httpd
specifier|private
name|Boolean
name|httpd
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--disable-httpd"
argument_list|,
name|usage
operator|=
literal|"Disable the internal HTTP daemon"
argument_list|)
DECL|method|setDisableHttpd (final boolean arg)
name|void
name|setDisableHttpd
parameter_list|(
specifier|final
name|boolean
name|arg
parameter_list|)
block|{
name|httpd
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--enable-sshd"
argument_list|,
name|usage
operator|=
literal|"Enable the internal SSH daemon"
argument_list|)
DECL|field|sshd
specifier|private
name|boolean
name|sshd
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--disable-sshd"
argument_list|,
name|usage
operator|=
literal|"Disable the internal SSH daemon"
argument_list|)
DECL|method|setDisableSshd (final boolean arg)
name|void
name|setDisableSshd
parameter_list|(
specifier|final
name|boolean
name|arg
parameter_list|)
block|{
name|sshd
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--slave"
argument_list|,
name|usage
operator|=
literal|"Support fetch only; implies --disable-httpd"
argument_list|)
DECL|field|slave
specifier|private
name|boolean
name|slave
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|LifecycleManager
name|manager
init|=
operator|new
name|LifecycleManager
argument_list|()
decl_stmt|;
DECL|field|dbInjector
specifier|private
name|Injector
name|dbInjector
decl_stmt|;
DECL|field|cfgInjector
specifier|private
name|Injector
name|cfgInjector
decl_stmt|;
DECL|field|sysInjector
specifier|private
name|Injector
name|sysInjector
decl_stmt|;
DECL|field|sshInjector
specifier|private
name|Injector
name|sshInjector
decl_stmt|;
DECL|field|webInjector
specifier|private
name|Injector
name|webInjector
decl_stmt|;
DECL|field|httpdInjector
specifier|private
name|Injector
name|httpdInjector
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|httpd
operator|==
literal|null
condition|)
block|{
name|httpd
operator|=
operator|!
name|slave
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|httpd
operator|&&
operator|!
name|sshd
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"No services enabled, nothing to do"
argument_list|)
throw|;
block|}
if|if
condition|(
name|slave
operator|&&
name|httpd
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot combine --slave and --enable-httpd"
argument_list|)
throw|;
block|}
if|if
condition|(
name|httpd
operator|&&
operator|!
name|sshd
condition|)
block|{
comment|// TODO Support HTTP without SSH.
throw|throw
name|die
argument_list|(
literal|"--enable-httpd currently requires --enable-sshd"
argument_list|)
throw|;
block|}
name|dbInjector
operator|=
name|createDbInjector
argument_list|()
expr_stmt|;
name|cfgInjector
operator|=
name|createCfgInjector
argument_list|()
expr_stmt|;
name|sysInjector
operator|=
name|createSysInjector
argument_list|()
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|dbInjector
argument_list|,
name|cfgInjector
argument_list|,
name|sysInjector
argument_list|)
expr_stmt|;
if|if
condition|(
name|sshd
condition|)
block|{
name|initSshd
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|httpd
condition|)
block|{
name|initHttpd
argument_list|()
expr_stmt|;
block|}
name|manager
operator|.
name|start
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Gerrit Code Review "
operator|+
name|myVersion
argument_list|()
operator|+
literal|" ready"
argument_list|)
expr_stmt|;
name|RuntimeShutdown
operator|.
name|add
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
name|log
operator|.
name|info
argument_list|(
literal|"caught shutdown, cleaning up"
argument_list|)
expr_stmt|;
name|manager
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|RuntimeShutdown
operator|.
name|waitFor
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
DECL|method|myVersion ()
specifier|private
name|String
name|myVersion
parameter_list|()
block|{
return|return
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Version
operator|.
name|getVersion
argument_list|()
return|;
block|}
DECL|method|createCfgInjector ()
specifier|private
name|Injector
name|createCfgInjector
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|AuthConfigModule
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dbInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
DECL|method|createSysInjector ()
specifier|private
name|Injector
name|createSysInjector
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|cfgInjector
operator|.
name|getInstance
argument_list|(
name|GerritGlobalModule
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|httpd
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|CanonicalWebUrlModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|String
argument_list|>
argument_list|>
name|provider
parameter_list|()
block|{
return|return
name|HttpCanonicalWebUrlProvider
operator|.
name|class
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|CanonicalWebUrlModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|String
argument_list|>
argument_list|>
name|provider
parameter_list|()
block|{
return|return
name|CanonicalWebUrlProvider
operator|.
name|class
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|slave
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|MasterNodeStartup
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|cfgInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
DECL|method|initSshd ()
specifier|private
name|void
name|initSshd
parameter_list|()
block|{
name|sshInjector
operator|=
name|createSshInjector
argument_list|()
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|sshInjector
argument_list|)
expr_stmt|;
block|}
DECL|method|createSshInjector ()
specifier|private
name|Injector
name|createSshInjector
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SshModule
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|slave
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SlaveCommandModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|MasterCommandModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
DECL|method|initHttpd ()
specifier|private
name|void
name|initHttpd
parameter_list|()
block|{
name|webInjector
operator|=
name|createWebInjector
argument_list|()
expr_stmt|;
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|HttpCanonicalWebUrlProvider
operator|.
name|class
argument_list|)
operator|.
name|setHttpServletRequest
argument_list|(
name|webInjector
operator|.
name|getProvider
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|httpdInjector
operator|=
name|createHttpdInjector
argument_list|()
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|webInjector
argument_list|,
name|httpdInjector
argument_list|)
expr_stmt|;
block|}
DECL|method|createWebInjector ()
specifier|private
name|Injector
name|createWebInjector
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|sshInjector
operator|.
name|getInstance
argument_list|(
name|WebModule
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
DECL|method|createHttpdInjector ()
specifier|private
name|Injector
name|createHttpdInjector
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|JettyModule
argument_list|(
operator|new
name|JettyEnv
argument_list|(
name|webInjector
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
block|}
end_class

end_unit

