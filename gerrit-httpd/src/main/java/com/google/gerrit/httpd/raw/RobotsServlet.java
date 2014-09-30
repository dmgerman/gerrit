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
name|common
operator|.
name|io
operator|.
name|ByteStreams
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
comment|/**  * This class provides a mechanism to use a configurable robots.txt file,  * outside of the .war of the application. In order to configure it add the  * following to the {@code httpd} section of the {@code gerrit.conf}  * file:  *  *<pre>  * [httpd]  *         robotsFile = etc/myrobots.txt  *</pre>  *  * If the specified file name is relative it will resolved as a sub directory of  * the site directory, if it is absolute it will be used as is.  *  * If the specified file doesn't exist or isn't readable the servlet will  * default to the {@code robots.txt} file bundled with the .war file of the  * application.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Singleton
DECL|class|RobotsServlet
specifier|public
class|class
name|RobotsServlet
extends|extends
name|HttpServlet
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
name|RobotsServlet
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|robotsFile
specifier|private
specifier|final
name|File
name|robotsFile
decl_stmt|;
annotation|@
name|Inject
DECL|method|RobotsServlet (@erritServerConfig final Config config, final SitePaths sitePaths)
name|RobotsServlet
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|config
parameter_list|,
specifier|final
name|SitePaths
name|sitePaths
parameter_list|)
block|{
name|File
name|file
init|=
name|sitePaths
operator|.
name|resolve
argument_list|(
name|config
operator|.
name|getString
argument_list|(
literal|"httpd"
argument_list|,
literal|null
argument_list|,
literal|"robotsFile"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|file
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|file
operator|.
name|canRead
argument_list|()
operator|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot read httpd.robotsFile, using default"
argument_list|)
expr_stmt|;
name|file
operator|=
literal|null
expr_stmt|;
block|}
name|robotsFile
operator|=
name|file
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
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|openRobotsFile
argument_list|()
decl_stmt|;
try|try
block|{
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
name|ByteStreams
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
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
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|openRobotsFile ()
specifier|private
name|InputStream
name|openRobotsFile
parameter_list|()
block|{
if|if
condition|(
name|robotsFile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|new
name|FileInputStream
argument_list|(
name|robotsFile
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot read "
operator|+
name|robotsFile
operator|+
literal|"; using default"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|getServletContext
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/robots.txt"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

