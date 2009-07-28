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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|patch
operator|.
name|PatchDetailServiceImpl
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
name|SshServlet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|server
operator|.
name|CacheControlFilter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
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
name|client
operator|.
name|OrmException
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
name|BindingAnnotation
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
name|Scopes
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
name|servlet
operator|.
name|ServletModule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|ProviderException
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
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|BindingAnnotation
DECL|annotation|ServletName
specifier|private
specifier|static
annotation_defn|@interface
name|ServletName
block|{
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
block|}
DECL|class|ServletNameImpl
specifier|private
specifier|static
specifier|final
class|class
name|ServletNameImpl
implements|implements
name|ServletName
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|ServletNameImpl (final String name)
name|ServletNameImpl
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|annotationType ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|()
block|{
return|return
name|ServletName
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ServletName["
operator|+
name|value
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
DECL|method|createServletModule ()
specifier|private
specifier|static
name|Module
name|createServletModule
parameter_list|()
block|{
return|return
operator|new
name|ServletModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|UrlRewriteFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|CacheControlFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|CacheControlFilter
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/Gerrit"
argument_list|,
literal|"/Gerrit/*"
argument_list|)
operator|.
name|with
argument_list|(
name|HostPageServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/prettify/*"
argument_list|)
operator|.
name|with
argument_list|(
name|PrettifyServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/login"
argument_list|)
operator|.
name|with
argument_list|(
name|OpenIdLoginServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/ssh_info"
argument_list|)
operator|.
name|with
argument_list|(
name|SshServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/cat/*"
argument_list|)
operator|.
name|with
argument_list|(
name|CatServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/static/*"
argument_list|)
operator|.
name|with
argument_list|(
name|StaticServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|AccountServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|AccountSecurityImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|GroupAdminServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|ChangeDetailServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|ChangeListServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|ChangeManageServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|OpenIdServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|PatchDetailServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|ProjectAdminServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|SuggestServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|SystemInfoServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|BecomeAnyAccountLoginServlet
operator|.
name|isAllowed
argument_list|()
condition|)
block|{
name|serve
argument_list|(
literal|"/become"
argument_list|)
operator|.
name|with
argument_list|(
name|BecomeAnyAccountLoginServlet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|rpc
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|RemoteJsonService
argument_list|>
name|clazz
parameter_list|)
block|{
name|String
name|name
init|=
name|clazz
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"Impl"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
name|rpc
argument_list|(
name|name
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|rpc
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|RemoteJsonService
argument_list|>
name|clazz
parameter_list|)
block|{
specifier|final
name|Key
argument_list|<
name|GerritJsonServlet
argument_list|>
name|srv
init|=
name|Key
operator|.
name|get
argument_list|(
name|GerritJsonServlet
operator|.
name|class
argument_list|,
operator|new
name|ServletNameImpl
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|GerritJsonServletProvider
name|provider
init|=
operator|new
name|GerritJsonServletProvider
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|serve
argument_list|(
literal|"/gerrit/rpc/"
operator|+
name|name
argument_list|)
operator|.
name|with
argument_list|(
name|srv
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|srv
argument_list|)
operator|.
name|toProvider
argument_list|(
name|provider
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createDatabaseModule ()
specifier|private
specifier|static
name|Module
name|createDatabaseModule
parameter_list|()
block|{
return|return
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
try|try
block|{
name|bind
argument_list|(
name|GerritServer
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|GerritServer
operator|.
name|getInstance
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|addError
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
name|addError
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|bind
argument_list|(
name|ContactStore
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|EncryptedContactStoreProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|FileTypeRegistry
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MimeUtilFileTypeRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|injector
specifier|private
specifier|final
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
name|createDatabaseModule
argument_list|()
argument_list|,
name|createServletModule
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getInjector ()
specifier|protected
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|injector
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
specifier|final
name|GerritServer
name|gs
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|GerritServer
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|gs
operator|.
name|closeDataSource
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|ce
parameter_list|)
block|{
comment|// Assume it never started.
block|}
catch|catch
parameter_list|(
name|ProviderException
name|ce
parameter_list|)
block|{
comment|// Assume it never started.
block|}
name|WorkQueue
operator|.
name|terminate
argument_list|()
expr_stmt|;
name|super
operator|.
name|contextDestroyed
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

