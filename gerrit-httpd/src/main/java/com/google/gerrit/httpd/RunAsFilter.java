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
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
operator|.
name|RestApiServlet
operator|.
name|replyError
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_FORBIDDEN
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
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
name|registration
operator|.
name|DynamicItem
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
name|Account
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
name|server
operator|.
name|ReviewDb
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
name|account
operator|.
name|AccountResolver
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
name|gwtorm
operator|.
name|server
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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

begin_comment
comment|/** Allows running a request as another user account. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|RunAsFilter
class|class
name|RunAsFilter
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
name|RunAsFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|RUN_AS
specifier|private
specifier|static
specifier|final
name|String
name|RUN_AS
init|=
literal|"X-Gerrit-RunAs"
decl_stmt|;
DECL|class|Module
specifier|static
class|class
name|Module
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
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
name|RunAsFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|enabled
specifier|private
specifier|final
name|boolean
name|enabled
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|WebSession
argument_list|>
name|session
decl_stmt|;
DECL|field|accountResolver
specifier|private
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
annotation|@
name|Inject
DECL|method|RunAsFilter ( Provider<ReviewDb> db, AuthConfig config, DynamicItem<WebSession> session, AccountResolver accountResolver)
name|RunAsFilter
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|AuthConfig
name|config
parameter_list|,
name|DynamicItem
argument_list|<
name|WebSession
argument_list|>
name|session
parameter_list|,
name|AccountResolver
name|accountResolver
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|enabled
operator|=
name|config
operator|.
name|isRunAsEnabled
argument_list|()
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|HttpServletRequest
name|req
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
name|HttpServletResponse
name|res
init|=
operator|(
name|HttpServletResponse
operator|)
name|response
decl_stmt|;
name|String
name|runas
init|=
name|req
operator|.
name|getHeader
argument_list|(
name|RUN_AS
argument_list|)
decl_stmt|;
if|if
condition|(
name|runas
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
name|replyError
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|SC_FORBIDDEN
argument_list|,
name|RUN_AS
operator|+
literal|" disabled by auth.enableRunAs = false"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
name|CurrentUser
name|self
init|=
name|session
operator|.
name|get
argument_list|()
operator|.
name|getUser
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|self
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canRunAs
argument_list|()
comment|// Always disallow for anonymous users, even if permitted by the ACL,
comment|// because that would be crazy.
operator|||
operator|!
name|self
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|replyError
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|SC_FORBIDDEN
argument_list|,
literal|"not permitted to use "
operator|+
name|RUN_AS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
name|Account
name|target
decl_stmt|;
try|try
block|{
name|target
operator|=
name|accountResolver
operator|.
name|find
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|runas
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"cannot resolve account for "
operator|+
name|RUN_AS
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|replyError
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|SC_INTERNAL_SERVER_ERROR
argument_list|,
literal|"cannot resolve "
operator|+
name|RUN_AS
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
name|replyError
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|SC_FORBIDDEN
argument_list|,
literal|"no account matches "
operator|+
name|RUN_AS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
name|session
operator|.
name|get
argument_list|()
operator|.
name|setUserAccountId
argument_list|(
name|target
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig filterConfig)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|filterConfig
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{}
block|}
end_class

end_unit

