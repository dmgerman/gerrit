begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|DynamicSet
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
name|IdentifiedUser
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
name|plugincontext
operator|.
name|PluginItemContext
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
name|plugincontext
operator|.
name|PluginSetContext
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
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_class
DECL|class|UniversalWebLoginFilter
specifier|public
class|class
name|UniversalWebLoginFilter
implements|implements
name|Filter
block|{
DECL|field|session
specifier|private
specifier|final
name|PluginItemContext
argument_list|<
name|WebSession
argument_list|>
name|session
decl_stmt|;
DECL|field|webLoginListeners
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|WebLoginListener
argument_list|>
name|webLoginListeners
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|ServletModule
name|module
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
literal|"/login*"
argument_list|,
literal|"/logout*"
argument_list|)
operator|.
name|through
argument_list|(
name|UniversalWebLoginFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|UniversalWebLoginFilter
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|Singleton
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicSet
operator|.
name|setOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|WebLoginListener
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Inject
DECL|method|UniversalWebLoginFilter ( PluginItemContext<WebSession> session, PluginSetContext<WebLoginListener> webLoginListeners, Provider<CurrentUser> userProvider)
specifier|public
name|UniversalWebLoginFilter
parameter_list|(
name|PluginItemContext
argument_list|<
name|WebSession
argument_list|>
name|session
parameter_list|,
name|PluginSetContext
argument_list|<
name|WebLoginListener
argument_list|>
name|webLoginListeners
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|webLoginListeners
operator|=
name|webLoginListeners
expr_stmt|;
name|this
operator|.
name|userProvider
operator|=
name|userProvider
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
throws|throws
name|ServletException
block|{}
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
name|httpRequest
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
name|HttpServletResponseRecorder
name|wrappedResponse
init|=
operator|new
name|HttpServletResponseRecorder
argument_list|(
operator|(
name|HttpServletResponse
operator|)
name|response
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|IdentifiedUser
argument_list|>
name|loggedInUserBefore
init|=
name|loggedInUser
argument_list|()
decl_stmt|;
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|wrappedResponse
argument_list|)
expr_stmt|;
name|Optional
argument_list|<
name|IdentifiedUser
argument_list|>
name|loggedInUserAfter
init|=
name|loggedInUser
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|loggedInUserBefore
operator|.
name|isPresent
argument_list|()
operator|&&
name|loggedInUserAfter
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|webLoginListeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onLogin
argument_list|(
name|loggedInUserAfter
operator|.
name|get
argument_list|()
argument_list|,
name|httpRequest
argument_list|,
name|wrappedResponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|loggedInUserBefore
operator|.
name|isPresent
argument_list|()
operator|&&
operator|!
name|loggedInUserAfter
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|webLoginListeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onLogout
argument_list|(
name|loggedInUserBefore
operator|.
name|get
argument_list|()
argument_list|,
name|httpRequest
argument_list|,
name|wrappedResponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|wrappedResponse
operator|.
name|play
argument_list|()
expr_stmt|;
block|}
DECL|method|loggedInUser ()
specifier|private
name|Optional
argument_list|<
name|IdentifiedUser
argument_list|>
name|loggedInUser
parameter_list|()
block|{
return|return
name|session
operator|.
name|call
argument_list|(
name|WebSession
operator|::
name|isSignedIn
argument_list|)
condition|?
name|Optional
operator|.
name|of
argument_list|(
name|userProvider
operator|.
name|get
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
argument_list|)
else|:
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
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

