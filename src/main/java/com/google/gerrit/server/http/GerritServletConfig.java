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
DECL|package|com.google.gerrit.server.http
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|http
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Stage
operator|.
name|PRODUCTION
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
name|git
operator|.
name|PushAllProjectsOp
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
name|git
operator|.
name|ReloadSubmitQueueOp
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
name|cache
operator|.
name|CachePool
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
name|DatabaseModule
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
name|ssh
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
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|inject
operator|.
name|ConfigurationException
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
name|CreationException
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
name|Guice
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
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
name|servlet
operator|.
name|GuiceServletContextListener
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
name|spi
operator|.
name|Message
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
name|Collections
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextEvent
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

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_comment
comment|/** Configures the web application environment for Gerrit Code Review. */
end_comment

begin_class
DECL|class|GerritServletConfig
specifier|public
class|class
name|GerritServletConfig
extends|extends
name|GuiceServletContextListener
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
name|GerritServletConfig
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dbInjector
specifier|private
name|Injector
name|dbInjector
decl_stmt|;
DECL|field|sysInjector
specifier|private
name|Injector
name|sysInjector
decl_stmt|;
DECL|field|webInjector
specifier|private
name|Injector
name|webInjector
decl_stmt|;
DECL|field|sshInjector
specifier|private
name|Injector
name|sshInjector
decl_stmt|;
DECL|method|init ()
specifier|private
specifier|synchronized
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|sysInjector
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|dbInjector
operator|=
name|Guice
operator|.
name|createInjector
argument_list|(
name|PRODUCTION
argument_list|,
operator|new
name|DatabaseModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CreationException
name|ce
parameter_list|)
block|{
specifier|final
name|Message
name|first
init|=
name|ce
operator|.
name|getErrorMessages
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|first
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|Throwable
name|why
init|=
name|first
operator|.
name|getCause
argument_list|()
decl_stmt|;
while|while
condition|(
name|why
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n  caused by "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|why
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|why
operator|=
name|why
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|first
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\nResolve above errors before continuing."
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\nComplete stack trace follows:"
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|error
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|,
name|first
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CreationException
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|first
argument_list|)
argument_list|)
throw|;
block|}
name|sysInjector
operator|=
name|GerritGlobalModule
operator|.
name|createInjector
argument_list|(
name|dbInjector
argument_list|,
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
name|sshInjector
operator|=
name|createSshInjector
argument_list|()
expr_stmt|;
name|webInjector
operator|=
name|createWebInjector
argument_list|()
expr_stmt|;
comment|// Push the Provider<HttpServletRequest> down into the canonical
comment|// URL provider. Its optional for that provider, but since we can
comment|// supply one we should do so, in case the administrator has not
comment|// setup the canonical URL in the configuration file.
comment|//
comment|// Note we have to do this manually as Guice failed to do the
comment|// injection here because the HTTP environment is not visible
comment|// to the core server modules.
comment|//
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
block|}
block|}
DECL|method|createSshInjector ()
specifier|private
name|Injector
name|createSshInjector
parameter_list|()
block|{
return|return
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
operator|new
name|SshModule
argument_list|()
argument_list|)
return|;
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
annotation|@
name|Override
DECL|method|getInjector ()
specifier|protected
name|Injector
name|getInjector
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
return|return
name|webInjector
return|;
block|}
annotation|@
name|Override
DECL|method|contextInitialized (final ServletContextEvent event)
specifier|public
name|void
name|contextInitialized
parameter_list|(
specifier|final
name|ServletContextEvent
name|event
parameter_list|)
block|{
name|super
operator|.
name|contextInitialized
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
try|try
block|{
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|CachePool
operator|.
name|class
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to start CachePool"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProvisionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to start CachePool"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|PushAllProjectsOp
operator|.
name|Factory
operator|.
name|class
argument_list|)
operator|.
name|create
argument_list|(
literal|null
argument_list|)
operator|.
name|start
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to restart replication queue"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProvisionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to restart replication queue"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|ReloadSubmitQueueOp
operator|.
name|Factory
operator|.
name|class
argument_list|)
operator|.
name|create
argument_list|()
operator|.
name|start
argument_list|(
literal|15
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to restart merge queue"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProvisionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to restart merge queue"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|sshInjector
operator|.
name|getInstance
argument_list|(
name|SshDaemon
operator|.
name|class
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to start SSHD"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProvisionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to start SSHD"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to start SSHD"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|contextDestroyed (final ServletContextEvent event)
specifier|public
name|void
name|contextDestroyed
parameter_list|(
specifier|final
name|ServletContextEvent
name|event
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|sshInjector
operator|!=
literal|null
condition|)
block|{
name|sshInjector
operator|.
name|getInstance
argument_list|(
name|SshDaemon
operator|.
name|class
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{     }
catch|catch
parameter_list|(
name|ProvisionException
name|e
parameter_list|)
block|{     }
try|try
block|{
if|if
condition|(
name|sysInjector
operator|!=
literal|null
condition|)
block|{
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|WorkQueue
operator|.
name|class
argument_list|)
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{     }
catch|catch
parameter_list|(
name|ProvisionException
name|e
parameter_list|)
block|{     }
try|try
block|{
if|if
condition|(
name|sysInjector
operator|!=
literal|null
condition|)
block|{
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|CachePool
operator|.
name|class
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{     }
catch|catch
parameter_list|(
name|ProvisionException
name|e
parameter_list|)
block|{     }
try|try
block|{
if|if
condition|(
name|dbInjector
operator|!=
literal|null
condition|)
block|{
name|closeDataSource
argument_list|(
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|DatabaseModule
operator|.
name|DS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|ce
parameter_list|)
block|{     }
catch|catch
parameter_list|(
name|ProvisionException
name|ce
parameter_list|)
block|{     }
name|super
operator|.
name|contextDestroyed
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|closeDataSource (final DataSource ds)
specifier|private
name|void
name|closeDataSource
parameter_list|(
specifier|final
name|DataSource
name|ds
parameter_list|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.commons.dbcp.BasicDataSource"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|ds
argument_list|)
condition|)
block|{
name|type
operator|.
name|getMethod
argument_list|(
literal|"close"
argument_list|)
operator|.
name|invoke
argument_list|(
name|ds
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|bad
parameter_list|)
block|{
comment|// Oh well, its not a Commons DBCP pooled connection.
block|}
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.mchange.v2.c3p0.DataSources"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|ds
argument_list|)
condition|)
block|{
name|type
operator|.
name|getMethod
argument_list|(
literal|"destroy"
argument_list|,
name|DataSource
operator|.
name|class
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|ds
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|bad
parameter_list|)
block|{
comment|// Oh well, its not a c3p0 pooled connection.
block|}
block|}
block|}
end_class

end_unit

