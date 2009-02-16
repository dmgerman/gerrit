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

begin_comment
comment|/**  * Forces GWT resources to cache for a very long time.  *<p>  * GWT compiled JavaScript and ImageBundles can be cached indefinitely by a  * browser and/or an edge proxy, as they never contain user-specific data and  * are named by a unique checksum. If their content is ever modified then the  * URL changes, so user agents would request a different resource. We force  * these resources to have very long expiration times.  */
end_comment

begin_class
DECL|class|CacheControlFilter
specifier|public
class|class
name|CacheControlFilter
implements|implements
name|Filter
block|{
DECL|method|init (final FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
specifier|final
name|FilterConfig
name|config
parameter_list|)
block|{   }
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{   }
DECL|method|doFilter (final ServletRequest sreq, final ServletResponse srsp, final FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
specifier|final
name|ServletRequest
name|sreq
parameter_list|,
specifier|final
name|ServletResponse
name|srsp
parameter_list|,
specifier|final
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
specifier|final
name|HttpServletRequest
name|req
init|=
operator|(
name|HttpServletRequest
operator|)
name|sreq
decl_stmt|;
specifier|final
name|HttpServletResponse
name|rsp
init|=
operator|(
name|HttpServletResponse
operator|)
name|srsp
decl_stmt|;
specifier|final
name|String
name|pathInfo
init|=
name|pathInfo
argument_list|(
name|req
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheForever
argument_list|(
name|pathInfo
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Cache-Control"
argument_list|,
literal|"max-age=31536000,public"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setDateHeader
argument_list|(
literal|"Expires"
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|31536000000L
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nocache
argument_list|(
name|pathInfo
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Expires"
argument_list|,
literal|"Fri, 01 Jan 1980 00:00:00 GMT"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Cache-Control"
argument_list|,
literal|"no-cache, must-revalidate"
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
DECL|method|cacheForever (final String pathInfo)
specifier|private
specifier|static
name|boolean
name|cacheForever
parameter_list|(
specifier|final
name|String
name|pathInfo
parameter_list|)
block|{
if|if
condition|(
name|pathInfo
operator|.
name|endsWith
argument_list|(
literal|".cache.gif"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|pathInfo
operator|.
name|endsWith
argument_list|(
literal|".cache.html"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|pathInfo
operator|.
name|endsWith
argument_list|(
literal|".cache.png"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|pathInfo
operator|.
name|endsWith
argument_list|(
literal|".cache.jar"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|nocache (final String pathInfo)
specifier|private
specifier|static
name|boolean
name|nocache
parameter_list|(
specifier|final
name|String
name|pathInfo
parameter_list|)
block|{
if|if
condition|(
name|pathInfo
operator|.
name|endsWith
argument_list|(
literal|".nocache.js"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|pathInfo (final HttpServletRequest req)
specifier|private
specifier|static
name|String
name|pathInfo
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|)
block|{
specifier|final
name|String
name|uri
init|=
name|req
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
specifier|final
name|String
name|ctx
init|=
name|req
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
return|return
name|uri
operator|.
name|startsWith
argument_list|(
name|ctx
argument_list|)
condition|?
name|uri
operator|.
name|substring
argument_list|(
name|ctx
operator|.
name|length
argument_list|()
argument_list|)
else|:
name|uri
return|;
block|}
block|}
end_class

end_unit

