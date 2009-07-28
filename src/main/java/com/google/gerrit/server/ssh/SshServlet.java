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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
package|;
end_package

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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Inet6Address
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
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
comment|/**  * Servlet hosting an SSH daemon on another port. During a standard HTTP GET  * request the servlet returns the hostname and port number back to the client  * in the form<code>${host} ${port}</code>.  *<p>  * Use a Git URL such as<code>ssh://${email}@${host}:${port}/${path}</code>,  * e.g.<code>ssh://sop@google.com@gerrit.com:8010/tools/gerrit.git</code> to  * access the SSH daemon itself.  *<p>  * Versions of Git before 1.5.3 may require setting the username and port  * properties in the user's<code>~/.ssh/config</code> file, and using a host  * alias through a URL such as<code>gerrit-alias:/tools/gerrit.git:  *<pre>  * Host gerrit-alias  *  User sop@google.com  *  Hostname gerrit.com  *  Port 8010  *</pre>  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Singleton
DECL|class|SshServlet
specifier|public
class|class
name|SshServlet
extends|extends
name|HttpServlet
block|{
DECL|field|sshd
specifier|private
specifier|final
name|GerritSshDaemon
name|sshd
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshServlet (final GerritSshDaemon daemon)
name|SshServlet
parameter_list|(
specifier|final
name|GerritSshDaemon
name|daemon
parameter_list|)
block|{
name|sshd
operator|=
name|daemon
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
specifier|final
name|InetSocketAddress
name|addr
init|=
name|sshd
operator|.
name|getAddress
argument_list|()
decl_stmt|;
specifier|final
name|String
name|out
decl_stmt|;
if|if
condition|(
name|addr
operator|!=
literal|null
condition|)
block|{
specifier|final
name|InetAddress
name|ip
init|=
name|addr
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|String
name|host
decl_stmt|;
if|if
condition|(
name|ip
operator|!=
literal|null
operator|&&
name|ip
operator|.
name|isAnyLocalAddress
argument_list|()
condition|)
block|{
name|host
operator|=
name|req
operator|.
name|getServerName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ip
operator|instanceof
name|Inet6Address
condition|)
block|{
name|host
operator|=
literal|"["
operator|+
name|addr
operator|.
name|getHostName
argument_list|()
operator|+
literal|"]"
expr_stmt|;
block|}
else|else
block|{
name|host
operator|=
name|addr
operator|.
name|getHostName
argument_list|()
expr_stmt|;
block|}
name|out
operator|=
name|host
operator|+
literal|" "
operator|+
name|addr
operator|.
name|getPort
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|out
operator|=
literal|"NOT_AVAILABLE"
expr_stmt|;
block|}
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
specifier|final
name|PrintWriter
name|w
init|=
name|rsp
operator|.
name|getWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|w
operator|.
name|write
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|w
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

