begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|IdentifiedUser
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
comment|/**  * Allows to listen and override the reponse to login/logout web actions.  *  *<p>Allows to intercept and act when a Gerrit user logs in or logs out of the Web interface to  * perform actions or to override the output response status code.  *  *<p>Typical use can be multi-factor authentication (on login) or global sign-out from SSO systems  * (on logout).  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|WebLoginListener
specifier|public
interface|interface
name|WebLoginListener
block|{
comment|/**    * Invoked after a user's web login.    *    * @param userId logged in user    * @param request request of the latest login action    * @param response response of the latest login action    */
DECL|method|onLogin (IdentifiedUser userId, HttpServletRequest request, HttpServletResponse response)
name|void
name|onLogin
parameter_list|(
name|IdentifiedUser
name|userId
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Invoked after a user's web logout.    *    * @param userId logged out user    * @param request request of the latest logout action    * @param response response of the latest logout action    */
DECL|method|onLogout (IdentifiedUser userId, HttpServletRequest request, HttpServletResponse response)
name|void
name|onLogout
parameter_list|(
name|IdentifiedUser
name|userId
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

