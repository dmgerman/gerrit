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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|common
operator|.
name|auth
operator|.
name|SignInMode
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|user
operator|.
name|client
operator|.
name|AutoCenterDialogBox
import|;
end_import

begin_comment
comment|/** Prompts the user to sign in to their account. */
end_comment

begin_class
DECL|class|SignInDialog
specifier|public
specifier|abstract
class|class
name|SignInDialog
extends|extends
name|AutoCenterDialogBox
block|{
DECL|field|mode
specifier|protected
specifier|final
name|SignInMode
name|mode
decl_stmt|;
DECL|field|token
specifier|protected
specifier|final
name|String
name|token
decl_stmt|;
comment|/**    * Create a new dialog to handle user sign in.    *    * @param signInMode type of mode the login will perform.    * @param token the token to jump to after sign-in is complete.    */
DECL|method|SignInDialog (final SignInMode signInMode, final String token)
specifier|protected
name|SignInDialog
parameter_list|(
specifier|final
name|SignInMode
name|signInMode
parameter_list|,
specifier|final
name|String
name|token
parameter_list|)
block|{
name|super
argument_list|(
comment|/* auto hide */
literal|true
argument_list|,
comment|/* modal */
literal|true
argument_list|)
expr_stmt|;
name|setGlassEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|mode
operator|=
name|signInMode
expr_stmt|;
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
switch|switch
condition|(
name|signInMode
condition|)
block|{
case|case
name|LINK_IDENTIY
case|:
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|linkIdentityDialogTitle
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REGISTER
case|:
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|registerDialogTitle
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|signInDialogTitle
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

