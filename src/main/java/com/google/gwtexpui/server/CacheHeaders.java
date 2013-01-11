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
DECL|package|com.google.gwtexpui.server
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|server
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|DAYS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
comment|/** Utilities to manage HTTP caching directives in responses. */
end_comment

begin_class
DECL|class|CacheHeaders
specifier|public
class|class
name|CacheHeaders
block|{
DECL|field|MAX_CACHE_DURATION
specifier|private
specifier|static
specifier|final
name|long
name|MAX_CACHE_DURATION
init|=
name|DAYS
operator|.
name|toSeconds
argument_list|(
literal|365
argument_list|)
decl_stmt|;
comment|/**    * Do not cache the response, anywhere.    *    * @param res response being returned.    */
DECL|method|setNotCacheable (HttpServletResponse res)
specifier|public
specifier|static
name|void
name|setNotCacheable
parameter_list|(
name|HttpServletResponse
name|res
parameter_list|)
block|{
name|String
name|cc
init|=
literal|"no-cache, no-store, max-age=0, must-revalidate"
decl_stmt|;
name|res
operator|.
name|setHeader
argument_list|(
literal|"Cache-Control"
argument_list|,
name|cc
argument_list|)
expr_stmt|;
name|res
operator|.
name|setHeader
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setHeader
argument_list|(
literal|"Expires"
argument_list|,
literal|"Fri, 01 Jan 1990 00:00:00 GMT"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setDateHeader
argument_list|(
literal|"Date"
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Permit caching the response for up to the age specified.    *<p>    * If the request is on a secure connection (e.g. SSL) private caching is    * used. This allows the user-agent to cache the response, but requests    * intermediate proxies to not cache. This may offer better protection for    * Set-Cookie headers.    *<p>    * If the request is on plaintext (insecure), public caching is used. This may    * allow an intermediate proxy to cache the response, including any Set-Cookie    * header that may have also been included.    *    * @param req current request.    * @param res response being returned.    * @param age how long the response can be cached.    * @param unit time unit for age, usually {@link TimeUnit#SECONDS}.    */
DECL|method|setCacheable ( HttpServletRequest req, HttpServletResponse res, long age, TimeUnit unit)
specifier|public
specifier|static
name|void
name|setCacheable
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|,
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
if|if
condition|(
name|req
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|setCacheablePrivate
argument_list|(
name|res
argument_list|,
name|age
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setCacheablePublic
argument_list|(
name|res
argument_list|,
name|age
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Allow the response to be cached by proxies and user-agents.    *<p>    * If the response includes a Set-Cookie header the cookie may be cached by a    * proxy and returned to multiple browsers behind the same proxy. This is    * insecure for authenticated connections.    *    * @param res response being returned.    * @param age how long the response can be cached.    * @param unit time unit for age, usually {@link TimeUnit#SECONDS}.    */
DECL|method|setCacheablePublic (HttpServletResponse res, long age, TimeUnit unit)
specifier|public
specifier|static
name|void
name|setCacheablePublic
parameter_list|(
name|HttpServletResponse
name|res
parameter_list|,
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|sec
init|=
name|maxAgeSeconds
argument_list|(
name|age
argument_list|,
name|unit
argument_list|)
decl_stmt|;
name|res
operator|.
name|setDateHeader
argument_list|(
literal|"Expires"
argument_list|,
name|now
operator|+
name|SECONDS
operator|.
name|toMillis
argument_list|(
name|sec
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|setDateHeader
argument_list|(
literal|"Date"
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|res
argument_list|,
literal|"public"
argument_list|,
name|age
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
comment|/**    * Allow the response to be cached only by the user-agent.    *    * @param res response being returned.    * @param age how long the response can be cached.    * @param unit time unit for age, usually {@link TimeUnit#SECONDS}.    */
DECL|method|setCacheablePrivate (HttpServletResponse res, long age, TimeUnit unit)
specifier|public
specifier|static
name|void
name|setCacheablePrivate
parameter_list|(
name|HttpServletResponse
name|res
parameter_list|,
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|res
operator|.
name|setDateHeader
argument_list|(
literal|"Expires"
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|res
operator|.
name|setDateHeader
argument_list|(
literal|"Date"
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|res
argument_list|,
literal|"private"
argument_list|,
name|age
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
DECL|method|cache (HttpServletResponse res, String type, long age, TimeUnit unit)
specifier|private
specifier|static
name|void
name|cache
parameter_list|(
name|HttpServletResponse
name|res
parameter_list|,
name|String
name|type
parameter_list|,
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|res
operator|.
name|setHeader
argument_list|(
literal|"Cache-Control"
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"%s, max-age=%d"
argument_list|,
name|type
argument_list|,
name|maxAgeSeconds
argument_list|(
name|age
argument_list|,
name|unit
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|maxAgeSeconds (long age, TimeUnit unit)
specifier|private
specifier|static
name|long
name|maxAgeSeconds
parameter_list|(
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|Math
operator|.
name|min
argument_list|(
name|unit
operator|.
name|toSeconds
argument_list|(
name|age
argument_list|)
argument_list|,
name|MAX_CACHE_DURATION
argument_list|)
return|;
block|}
DECL|method|CacheHeaders ()
specifier|private
name|CacheHeaders
parameter_list|()
block|{   }
block|}
end_class

end_unit

