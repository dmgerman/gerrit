begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|client
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
comment|/**  * Result from performing a review (comment, abandon, etc.)  */
end_comment

begin_class
DECL|class|ReviewResult
specifier|public
class|class
name|ReviewResult
block|{
DECL|field|errors
specifier|protected
name|List
argument_list|<
name|Error
argument_list|>
name|errors
decl_stmt|;
DECL|field|changeId
specifier|protected
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|method|ReviewResult ()
specifier|public
name|ReviewResult
parameter_list|()
block|{
name|errors
operator|=
operator|new
name|ArrayList
argument_list|<
name|Error
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|addError (final Error e)
specifier|public
name|void
name|addError
parameter_list|(
specifier|final
name|Error
name|e
parameter_list|)
block|{
name|errors
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|Error
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
DECL|method|getChangeId ()
specifier|public
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
DECL|method|setChangeId (Change.Id changeId)
specifier|public
name|void
name|setChangeId
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
block|}
DECL|class|Error
specifier|public
specifier|static
class|class
name|Error
block|{
DECL|enum|Type
specifier|public
specifier|static
enum|enum
name|Type
block|{
comment|/** Not permitted to abandon this change. */
DECL|enumConstant|ABANDON_NOT_PERMITTED
name|ABANDON_NOT_PERMITTED
block|,
comment|/** Not permitted to restore this change. */
DECL|enumConstant|RESTORE_NOT_PERMITTED
name|RESTORE_NOT_PERMITTED
block|,
comment|/** Not permitted to submit this change. */
DECL|enumConstant|SUBMIT_NOT_PERMITTED
name|SUBMIT_NOT_PERMITTED
block|,
comment|/** Approvals or dependencies are lacking for submission. */
DECL|enumConstant|SUBMIT_NOT_READY
name|SUBMIT_NOT_READY
block|,
comment|/** Review operation invalid because change is closed. */
DECL|enumConstant|CHANGE_IS_CLOSED
name|CHANGE_IS_CLOSED
block|,
comment|/** Not permitted to publish this draft patch set */
DECL|enumConstant|PUBLISH_NOT_PERMITTED
name|PUBLISH_NOT_PERMITTED
block|,
comment|/** Not permitted to delete this draft patch set */
DECL|enumConstant|DELETE_NOT_PERMITTED
name|DELETE_NOT_PERMITTED
block|,
comment|/** Review operation not permitted by rule. */
DECL|enumConstant|RULE_ERROR
name|RULE_ERROR
block|,
comment|/** Review operation invalid because patch set is not a draft. */
DECL|enumConstant|NOT_A_DRAFT
name|NOT_A_DRAFT
block|,
comment|/** Error writing change to git repository */
DECL|enumConstant|GIT_ERROR
name|GIT_ERROR
block|,
comment|/** The destination branch does not exist */
DECL|enumConstant|DEST_BRANCH_NOT_FOUND
name|DEST_BRANCH_NOT_FOUND
block|}
DECL|field|type
specifier|protected
name|Type
name|type
decl_stmt|;
DECL|field|message
specifier|protected
name|String
name|message
decl_stmt|;
DECL|method|Error ()
specifier|protected
name|Error
parameter_list|()
block|{     }
DECL|method|Error (final Type type)
specifier|public
name|Error
parameter_list|(
specifier|final
name|Type
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|message
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|Error (final Type type, final String message)
specifier|public
name|Error
parameter_list|(
specifier|final
name|Type
name|type
parameter_list|,
specifier|final
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|getMessageOrType ()
specifier|public
name|String
name|getMessageOrType
parameter_list|()
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
return|return
name|message
return|;
block|}
return|return
literal|""
operator|+
name|type
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
name|String
name|ret
init|=
name|type
operator|+
literal|""
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|ret
operator|+=
literal|" "
operator|+
name|message
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
block|}
end_class

end_unit

