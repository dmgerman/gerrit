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

begin_comment
comment|// CGI environment and execution management portions are:
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// ========================================================================
end_comment

begin_comment
comment|// Copyright (c) 2006-2009 Mort Bay Consulting Pty. Ltd.
end_comment

begin_comment
comment|// ------------------------------------------------------------------------
end_comment

begin_comment
comment|// All rights reserved. This program and the accompanying materials
end_comment

begin_comment
comment|// are made available under the terms of the Eclipse Public License v1.0
end_comment

begin_comment
comment|// and Apache License v2.0 which accompanies this distribution.
end_comment

begin_comment
comment|// The Eclipse Public License is available at
end_comment

begin_comment
comment|// http://www.eclipse.org/legal/epl-v10.html
end_comment

begin_comment
comment|// The Apache License v2.0 is available at
end_comment

begin_comment
comment|// http://www.opensource.org/licenses/apache2.0.php
end_comment

begin_comment
comment|// You may elect to redistribute this code under either of these licenses.
end_comment

begin_comment
comment|// ========================================================================
end_comment

begin_package
DECL|package|com.google.gerrit.httpd.gitweb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|gitweb
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
name|httpd
operator|.
name|GitWebConfig
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
name|Singleton
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
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
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

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|GitWebCssServlet
specifier|abstract
class|class
name|GitWebCssServlet
extends|extends
name|HttpServlet
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Singleton
DECL|class|Site
specifier|static
class|class
name|Site
extends|extends
name|GitWebCssServlet
block|{
annotation|@
name|Inject
DECL|method|Site (SitePaths paths, GitWebConfig gwc)
name|Site
parameter_list|(
name|SitePaths
name|paths
parameter_list|,
name|GitWebConfig
name|gwc
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|paths
operator|.
name|site_css
argument_list|,
name|gwc
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Singleton
DECL|class|Default
specifier|static
class|class
name|Default
extends|extends
name|GitWebCssServlet
block|{
annotation|@
name|Inject
DECL|method|Default (GitWebConfig gwc)
name|Default
parameter_list|(
name|GitWebConfig
name|gwc
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|gwc
operator|.
name|getGitwebCSS
argument_list|()
argument_list|,
name|gwc
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|ENC
specifier|private
specifier|static
specifier|final
name|String
name|ENC
init|=
literal|"UTF-8"
decl_stmt|;
DECL|field|raw_css
specifier|private
specifier|final
name|byte
index|[]
name|raw_css
decl_stmt|;
DECL|field|gz_css
specifier|private
specifier|final
name|byte
index|[]
name|gz_css
decl_stmt|;
DECL|method|GitWebCssServlet (final File src, final GitWebConfig gitWebConfig)
name|GitWebCssServlet
parameter_list|(
specifier|final
name|File
name|src
parameter_list|,
specifier|final
name|GitWebConfig
name|gitWebConfig
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|File
name|dir
init|=
name|src
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|src
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|String
name|raw
init|=
name|HtmlDomUtil
operator|.
name|readFile
argument_list|(
name|dir
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|raw
operator|!=
literal|null
condition|)
block|{
name|raw_css
operator|=
name|raw
operator|.
name|getBytes
argument_list|(
name|ENC
argument_list|)
expr_stmt|;
name|gz_css
operator|=
name|HtmlDomUtil
operator|.
name|compress
argument_list|(
name|raw_css
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|raw_css
operator|=
literal|null
expr_stmt|;
name|gz_css
operator|=
literal|null
expr_stmt|;
block|}
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
if|if
condition|(
name|raw_css
operator|!=
literal|null
condition|)
block|{
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/css"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
name|ENC
argument_list|)
expr_stmt|;
specifier|final
name|byte
index|[]
name|toSend
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
name|toSend
operator|=
name|gz_css
expr_stmt|;
block|}
else|else
block|{
name|toSend
operator|=
name|raw_css
expr_stmt|;
block|}
name|rsp
operator|.
name|setContentLength
argument_list|(
name|toSend
operator|.
name|length
argument_list|)
expr_stmt|;
specifier|final
name|ServletOutputStream
name|os
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|os
operator|.
name|write
argument_list|(
name|toSend
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

