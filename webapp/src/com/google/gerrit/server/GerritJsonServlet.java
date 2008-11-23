begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|gerrit
operator|.
name|client
operator|.
name|Gerrit
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
name|client
operator|.
name|CookieAccess
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
name|ActiveCall
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
name|gwtjsonrpc
operator|.
name|server
operator|.
name|SignedToken
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

begin_comment
comment|/**  * Base JSON servlet to ensure the current user is not forged.  */
end_comment

begin_class
DECL|class|GerritJsonServlet
specifier|public
specifier|abstract
class|class
name|GerritJsonServlet
extends|extends
name|JsonServlet
block|{
DECL|field|server
specifier|private
name|GerritServer
name|server
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
name|server
operator|=
name|GerritServer
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
literal|"Cannot configure GerritServer"
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
literal|"Cannot configure GerritServer"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createXsrfSignedToken ()
specifier|protected
name|SignedToken
name|createXsrfSignedToken
parameter_list|()
throws|throws
name|XsrfException
block|{
try|try
block|{
return|return
name|GerritServer
operator|.
name|getInstance
argument_list|()
operator|.
name|getXsrfToken
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XsrfException
argument_list|(
literal|"Cannot configure GerritServer"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createServiceHandle ()
specifier|protected
specifier|abstract
name|Object
name|createServiceHandle
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|Override
DECL|method|xsrfUser (final ActiveCall call)
specifier|protected
name|String
name|xsrfUser
parameter_list|(
specifier|final
name|ActiveCall
name|call
parameter_list|)
block|{
specifier|final
name|String
name|idstr
init|=
name|CookieAccess
operator|.
name|getTokenText
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|)
decl_stmt|;
return|return
name|idstr
operator|!=
literal|null
condition|?
literal|"account"
operator|+
name|idstr
else|:
literal|"anonymous"
return|;
block|}
annotation|@
name|Override
DECL|method|xsrfValidate (final ActiveCall call)
specifier|protected
name|boolean
name|xsrfValidate
parameter_list|(
specifier|final
name|ActiveCall
name|call
parameter_list|)
throws|throws
name|XsrfException
block|{
specifier|final
name|String
name|usertok
init|=
name|CookieAccess
operator|.
name|get
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|)
decl_stmt|;
if|if
condition|(
name|usertok
operator|==
literal|null
operator|||
name|server
operator|.
name|getAccountToken
argument_list|()
operator|.
name|checkToken
argument_list|(
name|usertok
argument_list|,
literal|null
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|xsrfValidate
argument_list|(
name|call
argument_list|)
return|;
block|}
else|else
block|{
name|LoginServlet
operator|.
name|forceLogout
argument_list|(
name|call
operator|.
name|getHttpServletResponse
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

