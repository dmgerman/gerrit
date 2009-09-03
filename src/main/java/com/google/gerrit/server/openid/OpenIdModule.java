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
DECL|package|com.google.gerrit.server.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|openid
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
name|MINUTES
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
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|CacheModule
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
name|http
operator|.
name|RpcServletModule
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
name|rpc
operator|.
name|UiRpcModule
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
name|TypeLiteral
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
name|ServletModule
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

begin_comment
comment|/** Servlets and RPC support related to OpenID authentication. */
end_comment

begin_class
DECL|class|OpenIdModule
specifier|public
class|class
name|OpenIdModule
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
specifier|final
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
argument_list|>
name|type
init|=
operator|new
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|String
argument_list|,
name|List
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|core
argument_list|(
name|type
argument_list|,
literal|"openid"
argument_list|)
comment|//
operator|.
name|timeToIdle
argument_list|(
literal|5
argument_list|,
name|MINUTES
argument_list|)
comment|// don't cache too long, might be stale
operator|.
name|timeToLive
argument_list|(
literal|5
argument_list|,
name|MINUTES
argument_list|)
comment|//
operator|.
name|memoryLimit
argument_list|(
literal|64
argument_list|)
comment|// short TTL means we won't have many entries
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/"
operator|+
name|OpenIdServiceImpl
operator|.
name|RETURN_URL
argument_list|)
operator|.
name|with
argument_list|(
name|OpenIdLoginServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|RpcServletModule
argument_list|(
name|UiRpcModule
operator|.
name|PREFIX
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|rpc
argument_list|(
name|OpenIdServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

