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
name|ProvisionException
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
name|servlet
operator|.
name|RequestScoped
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

begin_class
annotation|@
name|RequestScoped
DECL|class|HttpRemotePeerProvider
class|class
name|HttpRemotePeerProvider
implements|implements
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
block|{
DECL|field|req
specifier|private
specifier|final
name|HttpServletRequest
name|req
decl_stmt|;
annotation|@
name|Inject
DECL|method|HttpRemotePeerProvider (final HttpServletRequest r)
name|HttpRemotePeerProvider
parameter_list|(
specifier|final
name|HttpServletRequest
name|r
parameter_list|)
block|{
name|req
operator|=
name|r
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|SocketAddress
name|get
parameter_list|()
block|{
specifier|final
name|String
name|addr
init|=
name|req
operator|.
name|getRemoteAddr
argument_list|()
decl_stmt|;
specifier|final
name|int
name|port
init|=
name|req
operator|.
name|getRemotePort
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|new
name|InetSocketAddress
argument_list|(
name|InetAddress
operator|.
name|getByName
argument_list|(
name|addr
argument_list|)
argument_list|,
name|port
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
name|ProvisionException
argument_list|(
literal|"Cannot get @RemotePeer"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

