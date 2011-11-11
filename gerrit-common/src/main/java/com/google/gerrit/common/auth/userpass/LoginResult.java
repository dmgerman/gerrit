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
DECL|package|com.google.gerrit.common.auth.userpass
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|auth
operator|.
name|userpass
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
name|reviewdb
operator|.
name|AuthType
import|;
end_import

begin_class
DECL|class|LoginResult
specifier|public
class|class
name|LoginResult
block|{
DECL|field|success
specifier|public
name|boolean
name|success
decl_stmt|;
DECL|field|isNew
specifier|public
name|boolean
name|isNew
decl_stmt|;
DECL|field|authType
specifier|protected
name|AuthType
name|authType
decl_stmt|;
DECL|field|error
specifier|protected
name|Error
name|error
decl_stmt|;
DECL|method|LoginResult ()
specifier|protected
name|LoginResult
parameter_list|()
block|{   }
DECL|method|LoginResult (final AuthType authType)
specifier|public
name|LoginResult
parameter_list|(
specifier|final
name|AuthType
name|authType
parameter_list|)
block|{
name|this
operator|.
name|authType
operator|=
name|authType
expr_stmt|;
block|}
DECL|method|getAuthType ()
specifier|public
name|AuthType
name|getAuthType
parameter_list|()
block|{
return|return
name|authType
return|;
block|}
DECL|method|setError (final Error error)
specifier|public
name|void
name|setError
parameter_list|(
specifier|final
name|Error
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
name|success
operator|=
name|error
operator|==
literal|null
expr_stmt|;
block|}
DECL|method|getError ()
specifier|public
name|Error
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
DECL|enum|Error
specifier|public
specifier|static
enum|enum
name|Error
block|{
comment|/** Username or password are invalid */
DECL|enumConstant|INVALID_LOGIN
name|INVALID_LOGIN
block|,
comment|/** The authentication server is unavailable or the query to it timed out */
DECL|enumConstant|AUTHENTICATION_UNAVAILABLE
name|AUTHENTICATION_UNAVAILABLE
block|}
block|}
end_class

end_unit

