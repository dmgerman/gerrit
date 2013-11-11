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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|Scopes
operator|.
name|SINGLETON
import|;
end_import

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
name|common
operator|.
name|ChangeHookRunner
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
name|auth
operator|.
name|openid
operator|.
name|OpenIdModule
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
name|plugins
operator|.
name|HttpPluginModule
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
name|reviewdb
operator|.
name|client
operator|.
name|AuthType
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
name|account
operator|.
name|InternalAccountDirectory
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
name|h2
operator|.
name|DefaultCacheFactory
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
name|AuthConfig
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
name|GerritServerConfigModule
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
name|contact
operator|.
name|HttpContactStoreConnection
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
name|LocalDiskRepositoryManager
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
name|ReceiveCommitsExecutorModule
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
name|index
operator|.
name|IndexModule
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
name|NoIndexModule
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
name|mail
operator|.
name|SignedTokenEmailTokenVerifier
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
name|mail
operator|.
name|SmtpEmailSender
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
name|patch
operator|.
name|IntraLineWorkerPool
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
name|plugins
operator|.
name|PluginGuiceEnvironment
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
name|plugins
operator|.
name|PluginRestApiModule
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
name|schema
operator|.
name|DataSourceModule
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
name|schema
operator|.
name|DataSourceProvider
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
name|schema
operator|.
name|DataSourceType
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
name|schema
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
name|schema
operator|.
name|SchemaModule
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
name|schema
operator|.
name|SchemaVersionCheck
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
name|solr
operator|.
name|SolrIndexModule
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
name|SshHostKeyModule
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
name|SshKeyCacheImpl
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
name|inject
operator|.
name|AbstractModule
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
name|name
operator|.
name|Names
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
name|GuiceFilter
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
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
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
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
DECL|class|WebAppInitializer
specifier|public
class|class
name|WebAppInitializer
extends|extends
name|GuiceServletContextListener
implements|implements
name|Filter
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
name|WebAppInitializer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sitePath
specifier|private
name|File
name|sitePath
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
DECL|field|manager
specifier|private
name|LifecycleManager
name|manager
decl_stmt|;
DECL|field|filter
specifier|private
name|GuiceFilter
name|filter
decl_stmt|;
annotation|@
name|Override
DECL|method|doFilter (ServletRequest req, ServletResponse res, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|filter
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|chain
argument_list|)
expr_stmt|;
block|}
DECL|method|init ()
specifier|private
specifier|synchronized
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|manager
operator|==
literal|null
condition|)
block|{
specifier|final
name|String
name|path
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"gerrit.site_path"
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|sitePath
operator|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"gerrit.init"
argument_list|)
operator|!=
literal|null
condition|)
block|{
operator|new
name|SiteInitializer
argument_list|(
name|path
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"gerrit.init_path"
argument_list|)
argument_list|)
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|dbInjector
operator|=
name|createDbInjector
argument_list|()
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
name|PluginGuiceEnvironment
name|env
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|PluginGuiceEnvironment
operator|.
name|class
argument_list|)
decl_stmt|;
name|env
operator|.
name|setCfgInjector
argument_list|(
name|cfgInjector
argument_list|)
expr_stmt|;
name|env
operator|.
name|setSshInjector
argument_list|(
name|sshInjector
argument_list|)
expr_stmt|;
name|env
operator|.
name|setHttpInjector
argument_list|(
name|webInjector
argument_list|)
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
name|filter
operator|=
name|webInjector
operator|.
name|getInstance
argument_list|(
name|GuiceFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|manager
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|dbInjector
argument_list|)
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|cfgInjector
argument_list|)
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|sysInjector
argument_list|)
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|sshInjector
argument_list|)
expr_stmt|;
name|manager
operator|.
name|add
argument_list|(
name|webInjector
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createDbInjector ()
specifier|private
name|Injector
name|createDbInjector
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
if|if
condition|(
name|sitePath
operator|!=
literal|null
condition|)
block|{
name|Module
name|sitePathModule
init|=
operator|new
name|AbstractModule
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
name|File
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|SitePath
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|sitePath
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|sitePathModule
argument_list|)
expr_stmt|;
name|Module
name|configModule
init|=
operator|new
name|GerritServerConfigModule
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|configModule
argument_list|)
expr_stmt|;
name|Injector
name|cfgInjector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
name|sitePathModule
argument_list|,
name|configModule
argument_list|)
decl_stmt|;
name|Config
name|cfg
init|=
name|cfgInjector
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
name|String
name|dbType
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"database"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
specifier|final
name|DataSourceType
name|dst
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|DataSourceModule
argument_list|()
argument_list|,
name|configModule
argument_list|,
name|sitePathModule
argument_list|)
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|dbType
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|LifecycleModule
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
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|dst
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceProvider
operator|.
name|Context
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|DataSourceProvider
operator|.
name|Context
operator|.
name|MULTI_USER
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|DataSource
operator|.
name|class
argument_list|,
name|Names
operator|.
name|named
argument_list|(
literal|"ReviewDb"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|toProvider
argument_list|(
name|DataSourceProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|DataSourceProvider
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|LifecycleModule
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
name|Key
operator|.
name|get
argument_list|(
name|DataSource
operator|.
name|class
argument_list|,
name|Names
operator|.
name|named
argument_list|(
literal|"ReviewDb"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ReviewDbDataSourceProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|ReviewDbDataSourceProvider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|modules
operator|.
name|add
argument_list|(
operator|new
name|DatabaseModule
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Guice
operator|.
name|createInjector
argument_list|(
name|PRODUCTION
argument_list|,
name|modules
argument_list|)
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
if|if
condition|(
name|sitePath
operator|==
literal|null
condition|)
block|{
comment|// If we didn't get the site path from the system property
comment|// we need to get it from the database, as that's our old
comment|// method of locating the site path on disk.
comment|//
name|modules
operator|.
name|add
argument_list|(
operator|new
name|AbstractModule
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
name|File
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|SitePath
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SitePathFromSystemConfigProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|GerritServerConfigModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SchemaModule
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|LocalDiskRepositoryManager
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|SchemaVersionCheck
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
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
operator|new
name|WorkQueue
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|ChangeHookRunner
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommitsExecutorModule
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|IntraLineWorkerPool
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
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
name|modules
operator|.
name|add
argument_list|(
operator|new
name|InternalAccountDirectory
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|DefaultCacheFactory
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SmtpEmailSender
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SignedTokenEmailTokenVerifier
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|PluginRestApiModule
argument_list|()
argument_list|)
expr_stmt|;
name|AbstractModule
name|changeIndexModule
decl_stmt|;
switch|switch
condition|(
name|IndexModule
operator|.
name|getIndexType
argument_list|(
name|cfgInjector
argument_list|)
condition|)
block|{
case|case
name|LUCENE
case|:
name|changeIndexModule
operator|=
operator|new
name|LuceneIndexModule
argument_list|()
expr_stmt|;
break|break;
case|case
name|SOLR
case|:
name|changeIndexModule
operator|=
operator|new
name|SolrIndexModule
argument_list|()
expr_stmt|;
break|break;
default|default:
name|changeIndexModule
operator|=
operator|new
name|NoIndexModule
argument_list|()
expr_stmt|;
block|}
name|modules
operator|.
name|add
argument_list|(
name|changeIndexModule
argument_list|)
expr_stmt|;
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
name|modules
operator|.
name|add
argument_list|(
name|SshKeyCacheImpl
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|MasterNodeStartup
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|AbstractModule
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
name|GerritUiOptions
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|GerritUiOptions
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|cfgInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
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
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|SshModule
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SshHostKeyModule
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|MasterCommandModule
argument_list|()
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
name|RequestContextFilter
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|AllRequestFilter
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|GitOverHttpModule
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
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
name|modules
operator|.
name|add
argument_list|(
name|sshInjector
operator|.
name|getInstance
argument_list|(
name|WebSshGlueModule
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|CacheBasedWebSession
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|HttpContactStoreConnection
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|HttpPluginModule
argument_list|()
argument_list|)
expr_stmt|;
name|AuthConfig
name|authConfig
init|=
name|cfgInjector
operator|.
name|getInstance
argument_list|(
name|AuthConfig
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|==
name|AuthType
operator|.
name|OPENID
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|OpenIdModule
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
DECL|method|init (FilterConfig cfg)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|cfg
parameter_list|)
throws|throws
name|ServletException
block|{
name|contextInitialized
argument_list|(
operator|new
name|ServletContextEvent
argument_list|(
name|cfg
operator|.
name|getServletContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
name|manager
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|manager
operator|.
name|stop
argument_list|()
expr_stmt|;
name|manager
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

