begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// (Copied from JGit org.eclipse.jgit.pgm.Main)
end_comment

begin_comment
comment|// Copyright (C) 2006, Robin Rosenberg<robin.rosenberg@dewire.com>
end_comment

begin_comment
comment|// Copyright (C) 2008, Shawn O. Pearce<spearce@spearce.org>
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// All rights reserved.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Redistribution and use in source and binary forms, with or
end_comment

begin_comment
comment|// without modification, are permitted provided that the following
end_comment

begin_comment
comment|// conditions are met:
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// - Redistributions of source code must retain the above copyright
end_comment

begin_comment
comment|// notice, this list of conditions and the following disclaimer.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// - Redistributions in binary form must reproduce the above
end_comment

begin_comment
comment|// copyright notice, this list of conditions and the following
end_comment

begin_comment
comment|// disclaimer in the documentation and/or other materials provided
end_comment

begin_comment
comment|// with the distribution.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// - Neither the name of the Eclipse Foundation, Inc. nor the
end_comment

begin_comment
comment|// names of its contributors may be used to endorse or promote
end_comment

begin_comment
comment|// products derived from this software without specific prior
end_comment

begin_comment
comment|// written permission.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
end_comment

begin_comment
comment|// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
end_comment

begin_comment
comment|// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
end_comment

begin_comment
comment|// OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
end_comment

begin_comment
comment|// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
end_comment

begin_comment
comment|// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
end_comment

begin_comment
comment|// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
end_comment

begin_comment
comment|// NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
end_comment

begin_comment
comment|// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
end_comment

begin_comment
comment|// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
end_comment

begin_comment
comment|// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
end_comment

begin_comment
comment|// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
end_comment

begin_comment
comment|// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
end_comment

begin_package
DECL|package|com.google.gerrit.pgm.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|CachedAuthenticator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_class
DECL|class|ProxyUtil
specifier|final
class|class
name|ProxyUtil
block|{
comment|/**    * Configure the JRE's standard HTTP based on<code>http_proxy</code>.    *<p>    * The popular libcurl library honors the<code>http_proxy</code> environment    * variable as a means of specifying an HTTP proxy for requests made behind a    * firewall. This is not natively recognized by the JRE, so this method can be    * used by command line utilities to configure the JRE before the first    * request is sent.    *    * @throws MalformedURLException the value in<code>http_proxy</code> is    *         unsupportable.    */
DECL|method|configureHttpProxy ()
specifier|static
name|void
name|configureHttpProxy
parameter_list|()
throws|throws
name|MalformedURLException
block|{
specifier|final
name|String
name|s
init|=
name|System
operator|.
name|getenv
argument_list|(
literal|"http_proxy"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
operator|||
name|s
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
return|return;
block|}
specifier|final
name|URL
name|u
init|=
operator|new
name|URL
argument_list|(
operator|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"://"
argument_list|)
operator|==
operator|-
literal|1
operator|)
condition|?
literal|"http://"
operator|+
name|s
else|:
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"http"
operator|.
name|equals
argument_list|(
name|u
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Invalid http_proxy: "
operator|+
name|s
operator|+
literal|": Only http supported."
argument_list|)
throw|;
block|}
specifier|final
name|String
name|proxyHost
init|=
name|u
operator|.
name|getHost
argument_list|()
decl_stmt|;
specifier|final
name|int
name|proxyPort
init|=
name|u
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"http.proxyHost"
argument_list|,
name|proxyHost
argument_list|)
expr_stmt|;
if|if
condition|(
name|proxyPort
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"http.proxyPort"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|proxyPort
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|userpass
init|=
name|u
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|userpass
operator|!=
literal|null
operator|&&
name|userpass
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
specifier|final
name|int
name|c
init|=
name|userpass
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
specifier|final
name|String
name|user
init|=
name|userpass
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
decl_stmt|;
specifier|final
name|String
name|pass
init|=
name|userpass
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
decl_stmt|;
name|CachedAuthenticator
operator|.
name|add
argument_list|(
operator|new
name|CachedAuthenticator
operator|.
name|CachedAuthentication
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|,
name|user
argument_list|,
name|pass
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ProxyUtil ()
name|ProxyUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

