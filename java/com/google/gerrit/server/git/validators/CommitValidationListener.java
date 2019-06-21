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
name|events
operator|.
name|CommitReceivedEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Listener to provide validation on received commits.  *  *<p>Invoked by Gerrit when a new commit is received, has passed basic Gerrit validation and can be  * then subject to extra validation checks.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|CommitValidationListener
specifier|public
interface|interface
name|CommitValidationListener
block|{
comment|/**    * Commit validation.    *    * @param receiveEvent commit event details    * @return list of validation messages    * @throws CommitValidationException if validation fails    */
DECL|method|onCommitReceived (CommitReceivedEvent receiveEvent)
name|List
argument_list|<
name|CommitValidationMessage
argument_list|>
name|onCommitReceived
parameter_list|(
name|CommitReceivedEvent
name|receiveEvent
parameter_list|)
throws|throws
name|CommitValidationException
function_decl|;
comment|/**    * Whether this validator should validate all commits.    *    * @return {@code true} if this validator should validate all commits, even when the {@code    *     skip-validation} push option was specified.    */
DECL|method|shouldValidateAllCommits ()
specifier|default
name|boolean
name|shouldValidateAllCommits
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_interface

end_unit

