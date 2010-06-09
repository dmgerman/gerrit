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
DECL|package|com.google.gerrit.httpd.auth.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|auth
operator|.
name|openid
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
name|rpc
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
name|inject
operator|.
name|servlet
operator|.
name|ServletModule
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
name|serve
argument_list|(
literal|"/"
operator|+
name|XrdsServlet
operator|.
name|LOCATION
argument_list|)
operator|.
name|with
argument_list|(
name|XrdsServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/"
argument_list|)
operator|.
name|through
argument_list|(
name|XrdsFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|RpcServletModule
argument_list|(
name|RpcServletModule
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

