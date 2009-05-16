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
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openid4java
operator|.
name|consumer
operator|.
name|ConsumerException
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
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
comment|/** Handles the<code>/login</code> URL for web based single-sign-on. */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|OpenIdLoginServlet
specifier|public
class|class
name|OpenIdLoginServlet
extends|extends
name|HttpServlet
block|{
DECL|field|impl
specifier|private
name|OpenIdServiceImpl
name|impl
decl_stmt|;
annotation|@
name|Override
DECL|method|init (final ServletConfig config)
specifier|public
name|void
name|init
parameter_list|(
specifier|final
name|ServletConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
try|try
block|{
name|impl
operator|=
name|OpenIdServiceImpl
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Cannot load GerritServer"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Cannot load GerritServer"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ConsumerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Cannot load OpenId consumer"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|doGet (final HttpServletRequest req, final HttpServletResponse rsp)
specifier|public
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
name|doPost
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doPost (final HttpServletRequest req, final HttpServletResponse rsp)
specifier|public
name|void
name|doPost
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
try|try
block|{
name|impl
operator|.
name|doAuth
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
literal|"Unexpected error during authentication"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|reset
argument_list|()
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
literal|500
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

