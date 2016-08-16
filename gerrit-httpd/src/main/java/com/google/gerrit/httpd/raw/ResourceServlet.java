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
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
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
name|checkNotNull
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
name|net
operator|.
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
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
name|net
operator|.
name|HttpHeaders
operator|.
name|ETAG
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
name|net
operator|.
name|HttpHeaders
operator|.
name|IF_MODIFIED_SINCE
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
name|net
operator|.
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
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
name|net
operator|.
name|HttpHeaders
operator|.
name|LAST_MODIFIED
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
name|MINUTES
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
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
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
name|SC_NOT_MODIFIED
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
name|annotations
operator|.
name|VisibleForTesting
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
name|CharMatcher
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
name|cache
operator|.
name|Cache
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
name|ImmutableMap
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
name|hash
operator|.
name|Hashing
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
name|FileUtil
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
name|Nullable
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
name|HtmlDomUtil
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
name|CacheHeaders
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
name|RPCServletUtils
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|NoSuchFileException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|FileTime
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
name|Callable
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
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
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
name|HttpServlet
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
comment|/**  * Base class for serving static resources.  *<p>  * Supports caching, ETags, basic content type detection, and limited gzip  * compression.  */
end_comment

begin_class
DECL|class|ResourceServlet
specifier|public
specifier|abstract
class|class
name|ResourceServlet
extends|extends
name|HttpServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
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
name|ResourceServlet
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CACHE_FILE_SIZE_LIMIT_BYTES
specifier|private
specifier|static
specifier|final
name|int
name|CACHE_FILE_SIZE_LIMIT_BYTES
init|=
literal|100
operator|<<
literal|10
decl_stmt|;
DECL|field|JS
specifier|private
specifier|static
specifier|final
name|String
name|JS
init|=
literal|"application/x-javascript"
decl_stmt|;
DECL|field|MIME_TYPES
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|MIME_TYPES
init|=
name|ImmutableMap
operator|.
expr|<
name|String
decl_stmt|,
name|String
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
literal|"css"
argument_list|,
literal|"text/css"
argument_list|)
decl|.
name|put
argument_list|(
literal|"gif"
argument_list|,
literal|"image/gif"
argument_list|)
decl|.
name|put
argument_list|(
literal|"htm"
argument_list|,
literal|"text/html"
argument_list|)
decl|.
name|put
argument_list|(
literal|"html"
argument_list|,
literal|"text/html"
argument_list|)
decl|.
name|put
argument_list|(
literal|"ico"
argument_list|,
literal|"image/x-icon"
argument_list|)
decl|.
name|put
argument_list|(
literal|"jpeg"
argument_list|,
literal|"image/jpeg"
argument_list|)
decl|.
name|put
argument_list|(
literal|"jpg"
argument_list|,
literal|"image/jpeg"
argument_list|)
decl|.
name|put
argument_list|(
literal|"js"
argument_list|,
name|JS
argument_list|)
decl|.
name|put
argument_list|(
literal|"pdf"
argument_list|,
literal|"application/pdf"
argument_list|)
decl|.
name|put
argument_list|(
literal|"png"
argument_list|,
literal|"image/png"
argument_list|)
decl|.
name|put
argument_list|(
literal|"rtf"
argument_list|,
literal|"text/rtf"
argument_list|)
decl|.
name|put
argument_list|(
literal|"svg"
argument_list|,
literal|"image/svg+xml"
argument_list|)
decl|.
name|put
argument_list|(
literal|"text"
argument_list|,
literal|"text/plain"
argument_list|)
decl|.
name|put
argument_list|(
literal|"tif"
argument_list|,
literal|"image/tiff"
argument_list|)
decl|.
name|put
argument_list|(
literal|"tiff"
argument_list|,
literal|"image/tiff"
argument_list|)
decl|.
name|put
argument_list|(
literal|"txt"
argument_list|,
literal|"text/plain"
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
DECL|method|contentType (String name)
specifier|protected
specifier|static
name|String
name|contentType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|dot
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|String
name|ext
init|=
literal|0
operator|<
name|dot
condition|?
name|name
operator|.
name|substring
argument_list|(
name|dot
operator|+
literal|1
argument_list|)
else|:
literal|""
decl_stmt|;
name|String
name|type
init|=
name|MIME_TYPES
operator|.
name|get
argument_list|(
name|ext
argument_list|)
decl_stmt|;
return|return
name|type
operator|!=
literal|null
condition|?
name|type
else|:
literal|"application/octet-stream"
return|;
block|}
DECL|field|cache
specifier|private
specifier|final
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
decl_stmt|;
DECL|field|refresh
specifier|private
specifier|final
name|boolean
name|refresh
decl_stmt|;
DECL|field|cacheOnClient
specifier|private
specifier|final
name|boolean
name|cacheOnClient
decl_stmt|;
DECL|field|cacheFileSizeLimitBytes
specifier|private
specifier|final
name|int
name|cacheFileSizeLimitBytes
decl_stmt|;
DECL|method|ResourceServlet (Cache<Path, Resource> cache, boolean refresh)
specifier|protected
name|ResourceServlet
parameter_list|(
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|,
name|boolean
name|refresh
parameter_list|)
block|{
name|this
argument_list|(
name|cache
argument_list|,
name|refresh
argument_list|,
literal|true
argument_list|,
name|CACHE_FILE_SIZE_LIMIT_BYTES
argument_list|)
expr_stmt|;
block|}
DECL|method|ResourceServlet (Cache<Path, Resource> cache, boolean refresh, boolean cacheOnClient)
specifier|protected
name|ResourceServlet
parameter_list|(
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|,
name|boolean
name|refresh
parameter_list|,
name|boolean
name|cacheOnClient
parameter_list|)
block|{
name|this
argument_list|(
name|cache
argument_list|,
name|refresh
argument_list|,
name|cacheOnClient
argument_list|,
name|CACHE_FILE_SIZE_LIMIT_BYTES
argument_list|)
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|ResourceServlet (Cache<Path, Resource> cache, boolean refresh, boolean cacheOnClient, int cacheFileSizeLimitBytes)
name|ResourceServlet
parameter_list|(
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|,
name|boolean
name|refresh
parameter_list|,
name|boolean
name|cacheOnClient
parameter_list|,
name|int
name|cacheFileSizeLimitBytes
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|checkNotNull
argument_list|(
name|cache
argument_list|,
literal|"cache"
argument_list|)
expr_stmt|;
name|this
operator|.
name|refresh
operator|=
name|refresh
expr_stmt|;
name|this
operator|.
name|cacheOnClient
operator|=
name|cacheOnClient
expr_stmt|;
name|this
operator|.
name|cacheFileSizeLimitBytes
operator|=
name|cacheFileSizeLimitBytes
expr_stmt|;
block|}
comment|/**    * Get the resource path on the filesystem that should be served for this    * request.    *    * @param pathInfo result of {@link HttpServletRequest#getPathInfo()}.    * @return path where static content can be found.    * @throws IOException if an error occurred resolving the resource.    */
DECL|method|getResourcePath (String pathInfo)
specifier|protected
specifier|abstract
name|Path
name|getResourcePath
parameter_list|(
name|String
name|pathInfo
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|getLastModifiedTime (Path p)
specifier|protected
name|FileTime
name|getLastModifiedTime
parameter_list|(
name|Path
name|p
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|p
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest req, HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
decl_stmt|;
if|if
condition|(
name|req
operator|.
name|getPathInfo
argument_list|()
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|"/"
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'/'
argument_list|)
operator|.
name|trimFrom
argument_list|(
name|req
operator|.
name|getPathInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isUnreasonableName
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|notFound
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
return|return;
block|}
name|Path
name|p
init|=
name|getResourcePath
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|notFound
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
return|return;
block|}
name|Resource
name|r
init|=
name|cache
operator|.
name|getIfPresent
argument_list|(
name|p
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|maybeStream
argument_list|(
name|p
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
condition|)
block|{
return|return;
comment|// Bypass cache for large resource.
block|}
name|r
operator|=
name|cache
operator|.
name|get
argument_list|(
name|p
argument_list|,
name|newLoader
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|refresh
operator|&&
name|r
operator|.
name|isStale
argument_list|(
name|p
argument_list|,
name|this
argument_list|)
condition|)
block|{
name|cache
operator|.
name|invalidate
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|r
operator|=
name|cache
operator|.
name|get
argument_list|(
name|p
argument_list|,
name|newLoader
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load static resource "
operator|+
name|req
operator|.
name|getPathInfo
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|CacheHeaders
operator|.
name|setNotCacheable
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setStatus
argument_list|(
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|r
operator|==
name|Resource
operator|.
name|NOT_FOUND
condition|)
block|{
name|notFound
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
comment|// Cached not found response.
return|return;
block|}
name|String
name|e
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"e"
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
operator|!
name|r
operator|.
name|etag
operator|.
name|equals
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|CacheHeaders
operator|.
name|setNotCacheable
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setStatus
argument_list|(
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|cacheOnClient
operator|&&
name|r
operator|.
name|etag
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getHeader
argument_list|(
name|IF_NONE_MATCH
argument_list|)
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|setStatus
argument_list|(
name|SC_NOT_MODIFIED
argument_list|)
expr_stmt|;
return|return;
block|}
name|byte
index|[]
name|tosend
init|=
name|r
operator|.
name|raw
decl_stmt|;
if|if
condition|(
operator|!
name|r
operator|.
name|contentType
operator|.
name|equals
argument_list|(
name|JS
argument_list|)
operator|&&
name|RPCServletUtils
operator|.
name|acceptsGzipEncoding
argument_list|(
name|req
argument_list|)
condition|)
block|{
name|byte
index|[]
name|gz
init|=
name|HtmlDomUtil
operator|.
name|compress
argument_list|(
name|tosend
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|gz
operator|.
name|length
operator|+
literal|24
operator|)
operator|<
name|tosend
operator|.
name|length
condition|)
block|{
name|rsp
operator|.
name|setHeader
argument_list|(
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|tosend
operator|=
name|gz
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cacheOnClient
condition|)
block|{
name|rsp
operator|.
name|setHeader
argument_list|(
name|ETAG
argument_list|,
name|r
operator|.
name|etag
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CacheHeaders
operator|.
name|setNotCacheable
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|CacheHeaders
operator|.
name|hasCacheHeader
argument_list|(
name|rsp
argument_list|)
condition|)
block|{
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|r
operator|.
name|etag
operator|.
name|equals
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|CacheHeaders
operator|.
name|setCacheable
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|,
literal|360
argument_list|,
name|DAYS
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CacheHeaders
operator|.
name|setCacheable
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|,
literal|15
argument_list|,
name|MINUTES
argument_list|,
name|refresh
argument_list|)
expr_stmt|;
block|}
block|}
name|rsp
operator|.
name|setContentType
argument_list|(
name|r
operator|.
name|contentType
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setContentLength
argument_list|(
name|tosend
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|out
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|out
operator|.
name|write
argument_list|(
name|tosend
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Nullable
DECL|method|getResource (String name)
name|Resource
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Path
name|p
init|=
name|getResourcePath
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Path doesn't exist %s"
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|cache
operator|.
name|get
argument_list|(
name|p
argument_list|,
name|newLoader
argument_list|(
name|p
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load static resource %s"
argument_list|,
name|name
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|notFound (HttpServletResponse rsp)
specifier|private
specifier|static
name|void
name|notFound
parameter_list|(
name|HttpServletResponse
name|rsp
parameter_list|)
block|{
name|rsp
operator|.
name|setStatus
argument_list|(
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
name|CacheHeaders
operator|.
name|setNotCacheable
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
block|}
comment|/**    * Maybe stream a path to the response, depending on the properties of the    * file and cache headers in the request.    *    * @param p path to stream    * @param req HTTP request.    * @param rsp HTTP response.    * @return true if the response was written (either the file contents or an    *     error); false if the path is too small to stream and should be cached.    */
DECL|method|maybeStream (Path p, HttpServletRequest req, HttpServletResponse rsp)
specifier|private
name|boolean
name|maybeStream
parameter_list|(
name|Path
name|p
parameter_list|,
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
if|if
condition|(
name|Files
operator|.
name|size
argument_list|(
name|p
argument_list|)
operator|<
name|cacheFileSizeLimitBytes
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|e
parameter_list|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|Resource
operator|.
name|NOT_FOUND
argument_list|)
expr_stmt|;
name|notFound
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|long
name|lastModified
init|=
name|FileUtil
operator|.
name|lastModified
argument_list|(
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|req
operator|.
name|getDateHeader
argument_list|(
name|IF_MODIFIED_SINCE
argument_list|)
operator|>=
name|lastModified
condition|)
block|{
name|rsp
operator|.
name|setStatus
argument_list|(
name|SC_NOT_MODIFIED
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|lastModified
operator|>
literal|0
condition|)
block|{
name|rsp
operator|.
name|setDateHeader
argument_list|(
name|LAST_MODIFIED
argument_list|,
name|lastModified
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|CacheHeaders
operator|.
name|hasCacheHeader
argument_list|(
name|rsp
argument_list|)
condition|)
block|{
name|CacheHeaders
operator|.
name|setCacheable
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|,
literal|15
argument_list|,
name|MINUTES
argument_list|,
name|refresh
argument_list|)
expr_stmt|;
block|}
name|rsp
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|(
name|p
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|GZIPOutputStream
name|gz
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|RPCServletUtils
operator|.
name|acceptsGzipEncoding
argument_list|(
name|req
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|setHeader
argument_list|(
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|gz
operator|=
operator|new
name|GZIPOutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|gz
expr_stmt|;
block|}
name|Files
operator|.
name|copy
argument_list|(
name|p
argument_list|,
name|out
argument_list|)
expr_stmt|;
if|if
condition|(
name|gz
operator|!=
literal|null
condition|)
block|{
name|gz
operator|.
name|finish
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|isUnreasonableName (String name)
specifier|private
specifier|static
name|boolean
name|isUnreasonableName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|length
argument_list|()
operator|<
literal|1
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"\\"
argument_list|)
comment|// no windows/dos style paths
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"../"
argument_list|)
comment|// no "../etc/passwd"
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"/../"
argument_list|)
comment|// no "foo/../etc/passwd"
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"/./"
argument_list|)
comment|// "foo/./foo" is insane to ask
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"//"
argument_list|)
return|;
comment|// windows UNC path can be "//..."
block|}
DECL|method|newLoader (final Path p)
specifier|private
name|Callable
argument_list|<
name|Resource
argument_list|>
name|newLoader
parameter_list|(
specifier|final
name|Path
name|p
parameter_list|)
block|{
return|return
operator|new
name|Callable
argument_list|<
name|Resource
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Resource
name|call
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
return|return
operator|new
name|Resource
argument_list|(
name|getLastModifiedTime
argument_list|(
name|p
argument_list|)
argument_list|,
name|contentType
argument_list|(
name|p
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|Files
operator|.
name|readAllBytes
argument_list|(
name|p
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|e
parameter_list|)
block|{
return|return
name|Resource
operator|.
name|NOT_FOUND
return|;
block|}
block|}
block|}
return|;
block|}
DECL|class|Resource
specifier|public
specifier|static
class|class
name|Resource
block|{
DECL|field|NOT_FOUND
specifier|static
specifier|final
name|Resource
name|NOT_FOUND
init|=
operator|new
name|Resource
argument_list|(
name|FileTime
operator|.
name|fromMillis
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|""
argument_list|,
operator|new
name|byte
index|[]
block|{}
argument_list|)
decl_stmt|;
DECL|field|lastModified
specifier|final
name|FileTime
name|lastModified
decl_stmt|;
DECL|field|contentType
specifier|final
name|String
name|contentType
decl_stmt|;
DECL|field|etag
specifier|final
name|String
name|etag
decl_stmt|;
DECL|field|raw
specifier|final
name|byte
index|[]
name|raw
decl_stmt|;
DECL|method|Resource (FileTime lastModified, String contentType, byte[] raw)
name|Resource
parameter_list|(
name|FileTime
name|lastModified
parameter_list|,
name|String
name|contentType
parameter_list|,
name|byte
index|[]
name|raw
parameter_list|)
block|{
name|this
operator|.
name|lastModified
operator|=
name|checkNotNull
argument_list|(
name|lastModified
argument_list|,
literal|"lastModified"
argument_list|)
expr_stmt|;
name|this
operator|.
name|contentType
operator|=
name|checkNotNull
argument_list|(
name|contentType
argument_list|,
literal|"contentType"
argument_list|)
expr_stmt|;
name|this
operator|.
name|raw
operator|=
name|checkNotNull
argument_list|(
name|raw
argument_list|,
literal|"raw"
argument_list|)
expr_stmt|;
name|this
operator|.
name|etag
operator|=
name|Hashing
operator|.
name|md5
argument_list|()
operator|.
name|hashBytes
argument_list|(
name|raw
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
DECL|method|isStale (Path p, ResourceServlet rs)
name|boolean
name|isStale
parameter_list|(
name|Path
name|p
parameter_list|,
name|ResourceServlet
name|rs
parameter_list|)
throws|throws
name|IOException
block|{
name|FileTime
name|t
decl_stmt|;
try|try
block|{
name|t
operator|=
name|rs
operator|.
name|getLastModifiedTime
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|e
parameter_list|)
block|{
return|return
name|this
operator|!=
name|NOT_FOUND
return|;
block|}
return|return
name|t
operator|.
name|toMillis
argument_list|()
operator|==
literal|0
operator|||
name|lastModified
operator|.
name|toMillis
argument_list|()
operator|==
literal|0
operator|||
operator|!
name|lastModified
operator|.
name|equals
argument_list|(
name|t
argument_list|)
return|;
block|}
block|}
DECL|class|Weigher
specifier|public
specifier|static
class|class
name|Weigher
implements|implements
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|Weigher
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
block|{
annotation|@
name|Override
DECL|method|weigh (Path p, Resource r)
specifier|public
name|int
name|weigh
parameter_list|(
name|Path
name|p
parameter_list|,
name|Resource
name|r
parameter_list|)
block|{
return|return
literal|2
operator|*
name|p
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
operator|+
name|r
operator|.
name|raw
operator|.
name|length
return|;
block|}
block|}
block|}
end_class

end_unit

