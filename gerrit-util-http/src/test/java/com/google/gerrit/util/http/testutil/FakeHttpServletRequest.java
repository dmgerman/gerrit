begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.util.http.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|http
operator|.
name|testutil
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Splitter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|LinkedListMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|restapi
operator|.
name|Url
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Enumeration
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
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|AsyncContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|DispatcherType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|RequestDispatcher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletInputStream
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
name|Cookie
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSession
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
name|HttpUpgradeHandler
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
name|Part
import|;
end_import

begin_comment
comment|/** Simple fake implementation of {@link HttpServletRequest}. */
end_comment

begin_class
DECL|class|FakeHttpServletRequest
specifier|public
class|class
name|FakeHttpServletRequest
implements|implements
name|HttpServletRequest
block|{
DECL|field|SERVLET_PATH
specifier|public
specifier|static
specifier|final
name|String
name|SERVLET_PATH
init|=
literal|"/b"
decl_stmt|;
DECL|field|rfcDateformatter
specifier|public
specifier|static
specifier|final
name|DateTimeFormatter
name|rfcDateformatter
init|=
name|DateTimeFormatter
operator|.
name|ofPattern
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss ZZZ"
argument_list|)
decl_stmt|;
DECL|field|attributes
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
decl_stmt|;
DECL|field|headers
specifier|private
specifier|final
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
decl_stmt|;
DECL|field|parameters
specifier|private
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
decl_stmt|;
DECL|field|hostName
specifier|private
name|String
name|hostName
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
DECL|field|servletPath
specifier|private
name|String
name|servletPath
decl_stmt|;
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|method|FakeHttpServletRequest ()
specifier|public
name|FakeHttpServletRequest
parameter_list|()
block|{
name|this
argument_list|(
literal|"gerrit.example.com"
argument_list|,
literal|80
argument_list|,
literal|""
argument_list|,
name|SERVLET_PATH
argument_list|)
expr_stmt|;
block|}
DECL|method|FakeHttpServletRequest (String hostName, int port, String contextPath, String servletPath)
specifier|public
name|FakeHttpServletRequest
parameter_list|(
name|String
name|hostName
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|String
name|servletPath
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|checkNotNull
argument_list|(
name|hostName
argument_list|,
literal|"hostName"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|port
operator|>
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|contextPath
operator|=
name|checkNotNull
argument_list|(
name|contextPath
argument_list|,
literal|"contextPath"
argument_list|)
expr_stmt|;
name|this
operator|.
name|servletPath
operator|=
name|checkNotNull
argument_list|(
name|servletPath
argument_list|,
literal|"servletPath"
argument_list|)
expr_stmt|;
name|attributes
operator|=
name|Maps
operator|.
name|newConcurrentMap
argument_list|()
expr_stmt|;
name|parameters
operator|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
expr_stmt|;
name|headers
operator|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAttribute (String name)
specifier|public
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getAttributeNames ()
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getAttributeNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|attributes
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCharacterEncoding ()
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
return|return
name|UTF_8
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getContentLength ()
specifier|public
name|int
name|getContentLength
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getInputStream ()
specifier|public
name|ServletInputStream
name|getInputStream
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getLocalAddr ()
specifier|public
name|String
name|getLocalAddr
parameter_list|()
block|{
return|return
literal|"1.2.3.4"
return|;
block|}
annotation|@
name|Override
DECL|method|getLocalName ()
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
annotation|@
name|Override
DECL|method|getLocalPort ()
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
annotation|@
name|Override
DECL|method|getLocale ()
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
return|return
name|Locale
operator|.
name|US
return|;
block|}
annotation|@
name|Override
DECL|method|getLocales ()
specifier|public
name|Enumeration
argument_list|<
name|Locale
argument_list|>
name|getLocales
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getParameter (String name)
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|getFirst
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getParameterMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getParameterMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|Maps
operator|.
name|transformValues
argument_list|(
name|parameters
operator|.
name|asMap
argument_list|()
argument_list|,
name|vs
lambda|->
name|vs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getParameterNames ()
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getParameterNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|parameters
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getParameterValues (String name)
specifier|public
name|String
index|[]
name|getParameterValues
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|parameters
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
return|;
block|}
DECL|method|setQueryString (String qs)
specifier|public
name|void
name|setQueryString
parameter_list|(
name|String
name|qs
parameter_list|)
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|entry
range|:
name|Splitter
operator|.
name|on
argument_list|(
literal|'&'
argument_list|)
operator|.
name|split
argument_list|(
name|qs
argument_list|)
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|kv
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|'='
argument_list|)
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|splitToList
argument_list|(
name|entry
argument_list|)
decl_stmt|;
try|try
block|{
name|params
operator|.
name|put
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|kv
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|kv
operator|.
name|size
argument_list|()
operator|==
literal|2
condition|?
name|URLDecoder
operator|.
name|decode
argument_list|(
name|kv
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|parameters
operator|=
name|params
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
literal|"HTTP/1.1"
return|;
block|}
annotation|@
name|Override
DECL|method|getReader ()
specifier|public
name|BufferedReader
name|getReader
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|getRealPath (String path)
specifier|public
name|String
name|getRealPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getRemoteAddr ()
specifier|public
name|String
name|getRemoteAddr
parameter_list|()
block|{
return|return
literal|"5.6.7.8"
return|;
block|}
annotation|@
name|Override
DECL|method|getRemoteHost ()
specifier|public
name|String
name|getRemoteHost
parameter_list|()
block|{
return|return
literal|"remotehost"
return|;
block|}
annotation|@
name|Override
DECL|method|getRemotePort ()
specifier|public
name|int
name|getRemotePort
parameter_list|()
block|{
return|return
literal|1234
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestDispatcher (String path)
specifier|public
name|RequestDispatcher
name|getRequestDispatcher
parameter_list|(
name|String
name|path
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|port
operator|==
literal|443
condition|?
literal|"https"
else|:
literal|"http"
return|;
block|}
annotation|@
name|Override
DECL|method|getServerName ()
specifier|public
name|String
name|getServerName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
annotation|@
name|Override
DECL|method|getServerPort ()
specifier|public
name|int
name|getServerPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
annotation|@
name|Override
DECL|method|isSecure ()
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
return|return
name|port
operator|==
literal|443
return|;
block|}
annotation|@
name|Override
DECL|method|removeAttribute (String name)
specifier|public
name|void
name|removeAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|attributes
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setAttribute (String name, Object value)
specifier|public
name|void
name|setAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setCharacterEncoding (String env)
specifier|public
name|void
name|setCharacterEncoding
parameter_list|(
name|String
name|env
parameter_list|)
throws|throws
name|UnsupportedOperationException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getAuthType ()
specifier|public
name|String
name|getAuthType
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
return|;
block|}
annotation|@
name|Override
DECL|method|getCookies ()
specifier|public
name|Cookie
index|[]
name|getCookies
parameter_list|()
block|{
return|return
operator|new
name|Cookie
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|getDateHeader (String name)
specifier|public
name|long
name|getDateHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|v
init|=
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|v
operator|==
literal|null
condition|?
literal|0
else|:
name|rfcDateformatter
operator|.
name|parse
argument_list|(
name|v
argument_list|,
name|Instant
operator|::
name|from
argument_list|)
operator|.
name|getEpochSecond
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getHeader (String name)
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|getFirst
argument_list|(
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getHeaderNames ()
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getHeaderNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|headers
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getHeaders (String name)
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getHeaders
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getIntHeader (String name)
specifier|public
name|int
name|getIntHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|getHeader
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
literal|"GET"
return|;
block|}
annotation|@
name|Override
DECL|method|getPathInfo ()
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|setPathInfo (String path)
specifier|public
name|FakeHttpServletRequest
name|setPathInfo
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getPathTranslated ()
specifier|public
name|String
name|getPathTranslated
parameter_list|()
block|{
return|return
name|path
return|;
block|}
annotation|@
name|Override
DECL|method|getQueryString ()
specifier|public
name|String
name|getQueryString
parameter_list|()
block|{
return|return
name|paramsToString
argument_list|(
name|parameters
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRemoteUser ()
specifier|public
name|String
name|getRemoteUser
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestURI ()
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
name|String
name|uri
init|=
name|contextPath
operator|+
name|servletPath
operator|+
name|path
decl_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|uri
operator|+=
literal|"?"
operator|+
name|paramsToString
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestURL ()
specifier|public
name|StringBuffer
name|getRequestURL
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestedSessionId ()
specifier|public
name|String
name|getRequestedSessionId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getServletPath ()
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
return|return
name|servletPath
return|;
block|}
annotation|@
name|Override
DECL|method|getSession ()
specifier|public
name|HttpSession
name|getSession
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getSession (boolean create)
specifier|public
name|HttpSession
name|getSession
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getUserPrincipal ()
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isRequestedSessionIdFromCookie ()
specifier|public
name|boolean
name|isRequestedSessionIdFromCookie
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isRequestedSessionIdFromURL ()
specifier|public
name|boolean
name|isRequestedSessionIdFromURL
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|isRequestedSessionIdFromUrl ()
specifier|public
name|boolean
name|isRequestedSessionIdFromUrl
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isRequestedSessionIdValid ()
specifier|public
name|boolean
name|isRequestedSessionIdValid
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isUserInRole (String role)
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|paramsToString (ListMultimap<String, String> params)
specifier|private
specifier|static
name|String
name|paramsToString
parameter_list|(
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|params
operator|.
name|entries
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'&'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|Url
operator|.
name|encode
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|Url
operator|.
name|encode
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getAsyncContext ()
specifier|public
name|AsyncContext
name|getAsyncContext
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getDispatcherType ()
specifier|public
name|DispatcherType
name|getDispatcherType
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getServletContext ()
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isAsyncStarted ()
specifier|public
name|boolean
name|isAsyncStarted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isAsyncSupported ()
specifier|public
name|boolean
name|isAsyncSupported
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|startAsync ()
specifier|public
name|AsyncContext
name|startAsync
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|startAsync (ServletRequest req, ServletResponse res)
specifier|public
name|AsyncContext
name|startAsync
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|authenticate (HttpServletResponse res)
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|HttpServletResponse
name|res
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getPart (String name)
specifier|public
name|Part
name|getPart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getParts ()
specifier|public
name|Collection
argument_list|<
name|Part
argument_list|>
name|getParts
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|login (String username, String password)
specifier|public
name|void
name|login
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|logout ()
specifier|public
name|void
name|logout
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getContentLengthLong ()
specifier|public
name|long
name|getContentLengthLong
parameter_list|()
block|{
return|return
name|getContentLength
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|changeSessionId ()
specifier|public
name|String
name|changeSessionId
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|upgrade (Class<T> httpUpgradeHandlerClass)
specifier|public
parameter_list|<
name|T
extends|extends
name|HttpUpgradeHandler
parameter_list|>
name|T
name|upgrade
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|httpUpgradeHandlerClass
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|addHeader (String name, String value)
specifier|public
name|FakeHttpServletRequest
name|addHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

