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
DECL|package|com.google.gerrit.server.git.validators
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|validators
package|;
end_package

begin_comment
comment|/**  * Result of a commit validation from a CommitValidationListener.  *  * Commit validators should return CommitValidationResult.SUCCESS  * in case of valid commit and CommitValidationResult.FAILURE in  * case of rejected commits.  *  * When reason of the failure needs to be displayed on the remote  * client, {@link #newFailure(String)} can be used to return additional  * textual description.  */
end_comment

begin_class
DECL|class|CommitValidationResult
specifier|public
class|class
name|CommitValidationResult
block|{
DECL|field|validated
specifier|public
specifier|final
name|boolean
name|validated
decl_stmt|;
DECL|field|message
specifier|public
specifier|final
name|String
name|message
decl_stmt|;
comment|/**    * Successful commit validation.    */
DECL|field|SUCCESS
specifier|public
specifier|static
specifier|final
name|CommitValidationResult
name|SUCCESS
init|=
operator|new
name|CommitValidationResult
argument_list|(
literal|true
argument_list|,
literal|""
argument_list|)
decl_stmt|;
comment|/**    * Commit validation failed.    */
DECL|field|FAILURE
specifier|public
specifier|static
specifier|final
name|CommitValidationResult
name|FAILURE
init|=
operator|new
name|CommitValidationResult
argument_list|(
literal|false
argument_list|,
literal|"Prohibited by Gerrit, invalid commit"
argument_list|)
decl_stmt|;
comment|/**    * Commit validation failed with a reason.    *    * @param message reason of the commit validation failure.    *    * @return validation failure with reason.    */
DECL|method|newFailure (String message)
specifier|public
specifier|static
name|CommitValidationResult
name|newFailure
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
operator|new
name|CommitValidationResult
argument_list|(
literal|false
argument_list|,
name|message
argument_list|)
return|;
block|}
comment|/**    * Commit validation result and reason.    *    * @param validated true if commit is valid or false if has to be rejected.    * @param message reason of the commit validation failure or warning message when    *            commit has been validated.    */
DECL|method|CommitValidationResult (boolean validated, String message)
specifier|protected
name|CommitValidationResult
parameter_list|(
name|boolean
name|validated
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|validated
operator|=
name|validated
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
comment|/**    * Gets validation status.    *    * @return validation status.    */
DECL|method|isValidated ()
specifier|public
name|boolean
name|isValidated
parameter_list|()
block|{
return|return
name|validated
return|;
block|}
comment|/**    * Gets additional textual description for the validation.    *    * @return textual validation reason.    */
DECL|method|getValidationReason ()
specifier|public
name|String
name|getValidationReason
parameter_list|()
block|{
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

