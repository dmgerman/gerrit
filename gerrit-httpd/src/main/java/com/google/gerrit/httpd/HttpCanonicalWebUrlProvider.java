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
name|CanonicalWebUrl
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
name|OutOfScopeException
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
comment|/** Sets {@link CanonicalWebUrl} to current HTTP request if not configured. */
end_comment

begin_class
DECL|class|HttpCanonicalWebUrlProvider
specifier|public
class|class
name|HttpCanonicalWebUrlProvider
extends|extends
name|CanonicalWebUrlProvider
block|{
DECL|field|requestProvider
specifier|private
name|Provider
argument_list|<
name|HttpServletRequest
argument_list|>
name|requestProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|HttpCanonicalWebUrlProvider (@erritServerConfig final Config config)
name|HttpCanonicalWebUrlProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setHttpServletRequest (final Provider<HttpServletRequest> hsr)
specifier|public
name|void
name|setHttpServletRequest
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|HttpServletRequest
argument_list|>
name|hsr
parameter_list|)
block|{
name|requestProvider
operator|=
name|hsr
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
name|String
name|canonicalUrl
init|=
name|super
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|canonicalUrl
operator|!=
literal|null
condition|)
block|{
return|return
name|canonicalUrl
return|;
block|}
if|if
condition|(
name|requestProvider
operator|!=
literal|null
condition|)
block|{
comment|// No canonical URL configured? Maybe we can get a reasonable
comment|// guess from the incoming HTTP request, if we are currently
comment|// inside of an HTTP request scope.
comment|//
specifier|final
name|HttpServletRequest
name|req
decl_stmt|;
try|try
block|{
name|req
operator|=
name|requestProvider
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProvisionException
name|noWeb
parameter_list|)
block|{
if|if
condition|(
name|noWeb
operator|.
name|getCause
argument_list|()
operator|instanceof
name|OutOfScopeException
condition|)
block|{
comment|// We can't obtain the request as we are not inside of
comment|// an HTTP request scope. Callers must handle null.
comment|//
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
name|noWeb
throw|;
block|}
block|}
specifier|final
name|StringBuffer
name|url
init|=
name|req
operator|.
name|getRequestURL
argument_list|()
decl_stmt|;
name|url
operator|.
name|setLength
argument_list|(
name|url
operator|.
name|length
argument_list|()
operator|-
name|req
operator|.
name|getServletPath
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|charAt
argument_list|(
name|url
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|!=
literal|'/'
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
return|return
name|url
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// We have no way of guessing our HTTP url.
comment|//
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

