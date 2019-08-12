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
name|Strings
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
name|common
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** Defines an abstract request for user authentication to Gerrit. */
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
name|Optional
argument_list|<
name|String
argument_list|>
name|username
decl_stmt|;
DECL|field|password
specifier|private
specifier|final
name|Optional
argument_list|<
name|String
argument_list|>
name|password
decl_stmt|;
DECL|method|AuthRequest (@ullable String username, @Nullable String password)
specifier|protected
name|AuthRequest
parameter_list|(
annotation|@
name|Nullable
name|String
name|username
parameter_list|,
annotation|@
name|Nullable
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|username
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the username to be authenticated.    *    * @return username for authentication or {@code empty} for anonymous access.    */
DECL|method|getUsername ()
specifier|public
specifier|final
name|Optional
argument_list|<
name|String
argument_list|>
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**    * Returns the user's credentials    *    * @return user's credentials or {@code empty}.    */
DECL|method|getPassword ()
specifier|public
specifier|final
name|Optional
argument_list|<
name|String
argument_list|>
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
block|}
end_class

end_unit

