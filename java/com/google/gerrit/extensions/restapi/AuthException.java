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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** Caller cannot perform the request operation (HTTP 403 Forbidden). */
end_comment

begin_class
DECL|class|AuthException
specifier|public
class|class
name|AuthException
extends|extends
name|RestApiException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|advice
specifier|private
name|Optional
argument_list|<
name|String
argument_list|>
name|advice
init|=
name|Optional
operator|.
name|empty
argument_list|()
decl_stmt|;
comment|/** @param msg message to return to the client. */
DECL|method|AuthException (String msg)
specifier|public
name|AuthException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
comment|/**    * @param msg message to return to the client.    * @param cause cause of this exception.    */
DECL|method|AuthException (String msg, Throwable cause)
specifier|public
name|AuthException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|setAdvice (String advice)
specifier|public
name|void
name|setAdvice
parameter_list|(
name|String
name|advice
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|advice
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|advice
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|advice
argument_list|)
expr_stmt|;
block|}
comment|/**    * Advice that the user can follow to acquire authorization to perform the action.    *    *<p>This may be long-form text with newlines, and may be printed to a terminal, for example in    * the message stream in response to a push.    */
DECL|method|getAdvice ()
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|getAdvice
parameter_list|()
block|{
return|return
name|advice
return|;
block|}
block|}
end_class

end_unit

