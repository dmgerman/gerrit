begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
package|;
end_package

begin_comment
comment|/**  * Result from {@code ChangeControl.canSubmit()}.  *  * @see ChangeControl#canSubmit(com.google.gerrit.reviewdb.PatchSet.Id,  *      com.google.gerrit.reviewdb.ReviewDb,  *      com.google.gerrit.common.data.ApprovalTypes,  *      com.google.gerrit.server.workflow.FunctionState.Factory)  */
end_comment

begin_class
DECL|class|CanSubmitResult
specifier|public
class|class
name|CanSubmitResult
block|{
comment|/** Magic constant meaning submitting is possible. */
DECL|field|OK
specifier|public
specifier|static
specifier|final
name|CanSubmitResult
name|OK
init|=
operator|new
name|CanSubmitResult
argument_list|(
literal|"OK"
argument_list|)
decl_stmt|;
DECL|field|errorMessage
specifier|private
specifier|final
name|String
name|errorMessage
decl_stmt|;
DECL|method|CanSubmitResult (String error)
name|CanSubmitResult
parameter_list|(
name|String
name|error
parameter_list|)
block|{
name|this
operator|.
name|errorMessage
operator|=
name|error
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|errorMessage
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CanSubmitResult["
operator|+
name|getMessage
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

