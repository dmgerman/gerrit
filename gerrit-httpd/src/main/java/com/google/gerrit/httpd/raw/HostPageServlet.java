begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GerritConfig
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
name|data
operator|.
name|HostPageData
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
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|server
operator|.
name|rpc
operator|.
name|RPCServletUtils
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
name|JsonServlet
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
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
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|FileNotFoundException
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
name|InputStream
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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
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
comment|/** Sends the Gerrit host page to clients. */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Singleton
DECL|class|HostPageServlet
specifier|public
class|class
name|HostPageServlet
extends|extends
name|HttpServlet
block|{
DECL|field|IS_DEV
specifier|private
specifier|static
specifier|final
name|boolean
name|IS_DEV
init|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"Gerrit.GwtDevMode"
argument_list|)
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|GerritConfig
name|config
decl_stmt|;
DECL|field|hostDoc
specifier|private
specifier|final
name|Document
name|hostDoc
decl_stmt|;
annotation|@
name|Inject
DECL|method|HostPageServlet (final Provider<CurrentUser> cu, final SitePaths site, final GerritConfig gc, @GerritServerConfig final Config cfg, final ServletContext servletContext)
name|HostPageServlet
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|cu
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|GerritConfig
name|gc
parameter_list|,
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|,
specifier|final
name|ServletContext
name|servletContext
parameter_list|)
throws|throws
name|IOException
block|{
name|currentUser
operator|=
name|cu
expr_stmt|;
name|config
operator|=
name|gc
expr_stmt|;
specifier|final
name|String
name|pageName
init|=
literal|"HostPage.html"
decl_stmt|;
name|hostDoc
operator|=
name|HtmlDomUtil
operator|.
name|parseFile
argument_list|(
name|getClass
argument_list|()
argument_list|,
name|pageName
argument_list|)
expr_stmt|;
if|if
condition|(
name|hostDoc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"No "
operator|+
name|pageName
operator|+
literal|" in webapp"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|IS_DEV
condition|)
block|{
specifier|final
name|Element
name|devmode
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|hostDoc
argument_list|,
literal|"gerrit_gwtdevmode"
argument_list|)
decl_stmt|;
if|if
condition|(
name|devmode
operator|!=
literal|null
condition|)
block|{
name|devmode
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|devmode
argument_list|)
expr_stmt|;
block|}
block|}
name|fixModuleReference
argument_list|(
name|hostDoc
argument_list|,
name|servletContext
argument_list|)
expr_stmt|;
name|injectCssFile
argument_list|(
name|hostDoc
argument_list|,
literal|"gerrit_sitecss"
argument_list|,
name|site
operator|.
name|site_css
argument_list|)
expr_stmt|;
name|injectXmlFile
argument_list|(
name|hostDoc
argument_list|,
literal|"gerrit_header"
argument_list|,
name|site
operator|.
name|site_header
argument_list|)
expr_stmt|;
name|injectXmlFile
argument_list|(
name|hostDoc
argument_list|,
literal|"gerrit_footer"
argument_list|,
name|site
operator|.
name|site_footer
argument_list|)
expr_stmt|;
block|}
DECL|method|injectXmlFile (final Document hostDoc, final String id, final File src)
specifier|private
name|void
name|injectXmlFile
parameter_list|(
specifier|final
name|Document
name|hostDoc
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|File
name|src
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Element
name|banner
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|hostDoc
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|banner
operator|==
literal|null
condition|)
block|{
return|return;
block|}
while|while
condition|(
name|banner
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|banner
operator|.
name|removeChild
argument_list|(
name|banner
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Document
name|html
init|=
name|HtmlDomUtil
operator|.
name|parseFile
argument_list|(
name|src
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|src
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|html
operator|==
literal|null
condition|)
block|{
name|banner
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|banner
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|Element
name|content
init|=
name|html
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|banner
operator|.
name|appendChild
argument_list|(
name|hostDoc
operator|.
name|importNode
argument_list|(
name|content
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|injectCssFile (final Document hostDoc, final String id, final File src)
specifier|private
name|void
name|injectCssFile
parameter_list|(
specifier|final
name|Document
name|hostDoc
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|File
name|src
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Element
name|banner
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|hostDoc
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|banner
operator|==
literal|null
condition|)
block|{
return|return;
block|}
while|while
condition|(
name|banner
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|banner
operator|.
name|removeChild
argument_list|(
name|banner
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|css
init|=
name|HtmlDomUtil
operator|.
name|readFile
argument_list|(
name|src
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|src
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|css
operator|==
literal|null
condition|)
block|{
name|banner
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|banner
argument_list|)
expr_stmt|;
return|return;
block|}
name|banner
operator|.
name|removeAttribute
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|banner
operator|.
name|appendChild
argument_list|(
name|hostDoc
operator|.
name|createCDATASection
argument_list|(
literal|"\n"
operator|+
name|css
operator|+
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|injectJson (final Document hostDoc, final String id, final Object obj)
specifier|private
name|void
name|injectJson
parameter_list|(
specifier|final
name|Document
name|hostDoc
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|Object
name|obj
parameter_list|)
block|{
specifier|final
name|Element
name|scriptNode
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|hostDoc
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|scriptNode
operator|==
literal|null
condition|)
block|{
return|return;
block|}
while|while
condition|(
name|scriptNode
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scriptNode
operator|.
name|removeChild
argument_list|(
name|scriptNode
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
name|scriptNode
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|scriptNode
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"<!--\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"var "
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"_obj="
argument_list|)
expr_stmt|;
name|JsonServlet
operator|.
name|defaultGsonBuilder
argument_list|()
operator|.
name|create
argument_list|()
operator|.
name|toJson
argument_list|(
name|obj
argument_list|,
name|w
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|";\n// -->\n"
argument_list|)
expr_stmt|;
name|asScript
argument_list|(
name|scriptNode
argument_list|)
expr_stmt|;
name|scriptNode
operator|.
name|appendChild
argument_list|(
name|hostDoc
operator|.
name|createCDATASection
argument_list|(
name|w
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|asScript (final Element scriptNode)
specifier|private
name|void
name|asScript
parameter_list|(
specifier|final
name|Element
name|scriptNode
parameter_list|)
block|{
name|scriptNode
operator|.
name|removeAttribute
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|scriptNode
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
literal|"text/javascript"
argument_list|)
expr_stmt|;
name|scriptNode
operator|.
name|setAttribute
argument_list|(
literal|"language"
argument_list|,
literal|"javascript"
argument_list|)
expr_stmt|;
block|}
DECL|method|fixModuleReference (final Document hostDoc, final ServletContext servletContext)
specifier|private
name|void
name|fixModuleReference
parameter_list|(
specifier|final
name|Document
name|hostDoc
parameter_list|,
specifier|final
name|ServletContext
name|servletContext
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Element
name|scriptNode
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|hostDoc
argument_list|,
literal|"gerrit_module"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scriptNode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No gerrit_module to rewrite in host document"
argument_list|)
throw|;
block|}
name|String
name|src
init|=
literal|"gerrit/gerrit.nocache.js"
decl_stmt|;
if|if
condition|(
operator|!
name|IS_DEV
condition|)
block|{
name|InputStream
name|in
init|=
name|servletContext
operator|.
name|getResourceAsStream
argument_list|(
literal|"/"
operator|+
name|src
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No "
operator|+
name|src
operator|+
literal|" in webapp root"
argument_list|)
throw|;
block|}
specifier|final
name|MessageDigest
name|md
init|=
name|Constants
operator|.
name|newMessageDigest
argument_list|()
decl_stmt|;
try|try
block|{
try|try
block|{
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|md
operator|.
name|update
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed reading "
operator|+
name|src
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|src
operator|+=
literal|"?content="
operator|+
name|ObjectId
operator|.
name|fromRaw
argument_list|(
name|md
operator|.
name|digest
argument_list|()
argument_list|)
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|scriptNode
operator|.
name|setAttribute
argument_list|(
literal|"src"
argument_list|,
name|src
argument_list|)
expr_stmt|;
name|asScript
argument_list|(
name|scriptNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (final HttpServletRequest req, final HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|HostPageData
name|pageData
init|=
operator|new
name|HostPageData
argument_list|()
decl_stmt|;
name|pageData
operator|.
name|config
operator|=
name|config
expr_stmt|;
specifier|final
name|CurrentUser
name|user
init|=
name|currentUser
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|instanceof
name|IdentifiedUser
condition|)
block|{
name|pageData
operator|.
name|userAccount
operator|=
operator|(
operator|(
name|IdentifiedUser
operator|)
name|user
operator|)
operator|.
name|getAccount
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Document
name|peruser
init|=
name|HtmlDomUtil
operator|.
name|clone
argument_list|(
name|hostDoc
argument_list|)
decl_stmt|;
name|injectJson
argument_list|(
name|peruser
argument_list|,
literal|"gerrit_hostpagedata"
argument_list|,
name|pageData
argument_list|)
expr_stmt|;
specifier|final
name|byte
index|[]
name|raw
init|=
name|HtmlDomUtil
operator|.
name|toUTF8
argument_list|(
name|peruser
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|tosend
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
literal|"Content-Encoding"
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|tosend
operator|=
name|HtmlDomUtil
operator|.
name|compress
argument_list|(
name|raw
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tosend
operator|=
name|raw
expr_stmt|;
block|}
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
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
name|HtmlDomUtil
operator|.
name|ENC
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
specifier|final
name|OutputStream
name|out
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|tosend
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

