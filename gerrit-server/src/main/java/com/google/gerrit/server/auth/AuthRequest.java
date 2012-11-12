begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.auth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
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
name|base
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Defines an abstract request for user authentication to Gerrit.  */
end_comment

begin_class
DECL|class|AuthRequest
specifier|public
specifier|abstract
class|class
name|AuthRequest
block|{
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
specifier|final
name|String
name|password
decl_stmt|;
DECL|method|AuthRequest (String username, String password)
specifier|protected
name|AuthRequest
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**    * Returns the username to be authenticated.    *    * @return username for authentication or null for anonymous access.    */
annotation|@
name|Nullable
DECL|method|getUsername ()
specifier|public
specifier|final
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**    * Returns the user's credentials    *    * @return user's credentials or null    */
annotation|@
name|Nullable
DECL|method|getPassword ()
specifier|public
specifier|final
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|checkPassword (String pwd)
specifier|public
name|void
name|checkPassword
parameter_list|(
name|String
name|pwd
parameter_list|)
throws|throws
name|AuthException
block|{
if|if
condition|(
operator|!
name|Objects
operator|.
name|equal
argument_list|(
name|getPassword
argument_list|()
argument_list|,
name|pwd
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InvalidCredentialsException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

