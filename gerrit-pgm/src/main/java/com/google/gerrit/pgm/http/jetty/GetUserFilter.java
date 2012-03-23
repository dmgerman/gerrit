begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.http.jetty
package|package
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
name|GerritServerConfig
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
name|URI
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

begin_comment
comment|/**  * Stores as a request attribute, so the {@link HttpLog} can include the the  * user for the request outside of the request scope.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GetUserFilter
specifier|public
class|class
name|GetUserFilter
implements|implements
name|Filter
block|{
DECL|field|REQ_ATTR_KEY
specifier|static
specifier|final
name|String
name|REQ_ATTR_KEY
init|=
name|CurrentUser
operator|.
name|class
operator|.
name|toString
argument_list|()
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|ServletModule
block|{
DECL|field|loggingEnabled
specifier|private
name|boolean
name|loggingEnabled
decl_stmt|;
annotation|@
name|Inject
DECL|method|Module (@erritServerConfig final Config cfg)
name|Module
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
block|{
name|URI
index|[]
name|urls
init|=
name|JettyServer
operator|.
name|listenURLs
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|boolean
name|reverseProxy
init|=
name|JettyServer
operator|.
name|isReverseProxied
argument_list|(
name|urls
argument_list|)
decl_stmt|;
name|this
operator|.
name|loggingEnabled
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"httpd"
argument_list|,
literal|"requestlog"
argument_list|,
operator|!
name|reverseProxy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
if|if
condition|(
name|loggingEnabled
condition|)
block|{
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|GetUserFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetUserFilter (final Provider<CurrentUser> userProvider)
name|GetUserFilter
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|)
block|{
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter ( ServletRequest req, ServletResponse resp, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|resp
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|req
operator|.
name|setAttribute
argument_list|(
name|REQ_ATTR_KEY
argument_list|,
name|userProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{   }
annotation|@
name|Override
DECL|method|init (FilterConfig arg0)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|arg0
parameter_list|)
block|{   }
block|}
end_class

end_unit

