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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
package|;
end_package

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
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_class
DECL|class|SocketUtil
specifier|public
specifier|final
class|class
name|SocketUtil
block|{
comment|/** True if this InetAddress is a raw IPv6 in dotted quad notation. */
DECL|method|isIPv6 (final InetAddress ip)
specifier|public
specifier|static
name|boolean
name|isIPv6
parameter_list|(
specifier|final
name|InetAddress
name|ip
parameter_list|)
block|{
return|return
name|ip
operator|instanceof
name|Inet6Address
operator|&&
name|ip
operator|.
name|getHostName
argument_list|()
operator|.
name|equals
argument_list|(
name|ip
operator|.
name|getHostAddress
argument_list|()
argument_list|)
return|;
block|}
comment|/** Get the name or IP address, or {@code *} if this address is a wildcard IP. */
DECL|method|hostname (final InetSocketAddress addr)
specifier|public
specifier|static
name|String
name|hostname
parameter_list|(
specifier|final
name|InetSocketAddress
name|addr
parameter_list|)
block|{
if|if
condition|(
name|addr
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|addr
operator|.
name|getAddress
argument_list|()
operator|.
name|isAnyLocalAddress
argument_list|()
condition|)
block|{
return|return
literal|"*"
return|;
block|}
return|return
name|addr
operator|.
name|getAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
return|;
block|}
return|return
name|addr
operator|.
name|getHostName
argument_list|()
return|;
block|}
comment|/** Format an address string into {@code host:port} or {@code *:port} syntax. */
DECL|method|format (final SocketAddress s, final int defaultPort)
specifier|public
specifier|static
name|String
name|format
parameter_list|(
specifier|final
name|SocketAddress
name|s
parameter_list|,
specifier|final
name|int
name|defaultPort
parameter_list|)
block|{
if|if
condition|(
name|s
operator|instanceof
name|InetSocketAddress
condition|)
block|{
specifier|final
name|InetSocketAddress
name|addr
init|=
operator|(
name|InetSocketAddress
operator|)
name|s
decl_stmt|;
if|if
condition|(
name|addr
operator|.
name|getPort
argument_list|()
operator|==
name|defaultPort
condition|)
block|{
return|return
name|safeHostname
argument_list|(
name|hostname
argument_list|(
name|addr
argument_list|)
argument_list|)
return|;
block|}
return|return
name|format
argument_list|(
name|hostname
argument_list|(
name|addr
argument_list|)
argument_list|,
name|addr
operator|.
name|getPort
argument_list|()
argument_list|)
return|;
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** Format an address string into {@code host:port} or {@code *:port} syntax. */
DECL|method|format (String hostname, int port)
specifier|public
specifier|static
name|String
name|format
parameter_list|(
name|String
name|hostname
parameter_list|,
name|int
name|port
parameter_list|)
block|{
return|return
name|safeHostname
argument_list|(
name|hostname
argument_list|)
operator|+
literal|":"
operator|+
name|port
return|;
block|}
DECL|method|safeHostname (String hostname)
specifier|private
specifier|static
name|String
name|safeHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
if|if
condition|(
literal|0
operator|<=
name|hostname
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
condition|)
block|{
name|hostname
operator|=
literal|"["
operator|+
name|hostname
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|hostname
return|;
block|}
comment|/** Parse an address string such as {@code host:port} or {@code *:port}. */
DECL|method|parse (final String desc, final int defaultPort)
specifier|public
specifier|static
name|InetSocketAddress
name|parse
parameter_list|(
specifier|final
name|String
name|desc
parameter_list|,
specifier|final
name|int
name|defaultPort
parameter_list|)
block|{
name|String
name|hostStr
decl_stmt|;
name|String
name|portStr
decl_stmt|;
if|if
condition|(
name|desc
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
comment|// IPv6, as a raw IP address.
comment|//
specifier|final
name|int
name|hostEnd
init|=
name|desc
operator|.
name|indexOf
argument_list|(
literal|']'
argument_list|)
decl_stmt|;
if|if
condition|(
name|hostEnd
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid IPv6: "
operator|+
name|desc
argument_list|)
throw|;
block|}
name|hostStr
operator|=
name|desc
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|hostEnd
argument_list|)
expr_stmt|;
name|portStr
operator|=
name|desc
operator|.
name|substring
argument_list|(
name|hostEnd
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// IPv4, or a host name.
comment|//
specifier|final
name|int
name|hostEnd
init|=
name|desc
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|hostStr
operator|=
literal|0
operator|<=
name|hostEnd
condition|?
name|desc
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|hostEnd
argument_list|)
else|:
name|desc
expr_stmt|;
name|portStr
operator|=
literal|0
operator|<=
name|hostEnd
condition|?
name|desc
operator|.
name|substring
argument_list|(
name|hostEnd
argument_list|)
else|:
literal|""
expr_stmt|;
block|}
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|hostStr
argument_list|)
condition|)
block|{
name|hostStr
operator|=
literal|"*"
expr_stmt|;
block|}
if|if
condition|(
name|portStr
operator|.
name|startsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|portStr
operator|=
name|portStr
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|port
decl_stmt|;
if|if
condition|(
name|portStr
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portStr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid port: "
operator|+
name|desc
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|port
operator|=
name|defaultPort
expr_stmt|;
block|}
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|hostStr
argument_list|)
condition|)
block|{
return|return
operator|new
name|InetSocketAddress
argument_list|(
name|port
argument_list|)
return|;
block|}
return|return
name|InetSocketAddress
operator|.
name|createUnresolved
argument_list|(
name|hostStr
argument_list|,
name|port
argument_list|)
return|;
block|}
comment|/** Parse and resolve an address string, looking up the IP address. */
DECL|method|resolve (final String desc, final int defaultPort)
specifier|public
specifier|static
name|InetSocketAddress
name|resolve
parameter_list|(
specifier|final
name|String
name|desc
parameter_list|,
specifier|final
name|int
name|defaultPort
parameter_list|)
block|{
specifier|final
name|InetSocketAddress
name|addr
init|=
name|parse
argument_list|(
name|desc
argument_list|,
name|defaultPort
argument_list|)
decl_stmt|;
if|if
condition|(
name|addr
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|addr
operator|.
name|getAddress
argument_list|()
operator|.
name|isAnyLocalAddress
argument_list|()
condition|)
block|{
return|return
name|addr
return|;
block|}
try|try
block|{
specifier|final
name|InetAddress
name|host
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
name|addr
operator|.
name|getHostName
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|InetSocketAddress
argument_list|(
name|host
argument_list|,
name|addr
operator|.
name|getPort
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown host: "
operator|+
name|desc
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|SocketUtil ()
specifier|private
name|SocketUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

