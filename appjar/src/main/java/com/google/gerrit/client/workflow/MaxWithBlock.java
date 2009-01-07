begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.workflow
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|workflow
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
name|client
operator|.
name|data
operator|.
name|ApprovalType
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
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
name|client
operator|.
name|reviewdb
operator|.
name|ChangeApproval
import|;
end_import

begin_comment
comment|/**  * Computes an {@link ApprovalCategory} by looking at maximum values.  *<p>  * In order to be considered "approved" this function requires that:  *<ul>  *<li>The maximum negative value is never used;</li>  *<li>The maximum positive value is used at least once;</li>  *<li>The user approving the maximum positive has been granted that.</li>  *</ul>  *<p>  * This function is primarily useful for review fields, with values such as:  *<ul>  *<li>+2: Approved change.</li>  *<li>+1: Looks ok, but get another approval from someone with more depth.</li>  *<li>-1: Soft reject, it isn't a great change but its OK if approved.</li>  *<li>-2: Rejected, must not be submitted.  *</ul>  *<p>  * Note that projects using this function would typically want to assign out the  * middle range (-1 .. +1) to almost everyone, so people can indicate how they  * feel about a change, but the extremes of -2 and +2 should be reserved for the  * project's long-term maintainers, those who are most familiar with its code.  */
end_comment

begin_class
DECL|class|MaxWithBlock
specifier|public
class|class
name|MaxWithBlock
extends|extends
name|CategoryFunction
block|{
DECL|field|NAME
specifier|public
specifier|static
name|String
name|NAME
init|=
literal|"MaxWithBlock"
decl_stmt|;
annotation|@
name|Override
DECL|method|run (final ApprovalType at, final FunctionState state)
specifier|public
name|void
name|run
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|,
specifier|final
name|FunctionState
name|state
parameter_list|)
block|{
name|boolean
name|rejected
init|=
literal|false
decl_stmt|;
name|boolean
name|passed
init|=
literal|false
decl_stmt|;
for|for
control|(
specifier|final
name|ChangeApproval
name|a
range|:
name|state
operator|.
name|getApprovals
argument_list|(
name|at
argument_list|)
control|)
block|{
name|state
operator|.
name|normalize
argument_list|(
name|at
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|rejected
operator||=
name|at
operator|.
name|isMaxNegative
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|passed
operator||=
name|at
operator|.
name|isMaxPositive
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|// The type must not have had its max negative (a forceful reject)
comment|// and must have at least one max positive (a full accept).
comment|//
name|state
operator|.
name|valid
argument_list|(
name|at
argument_list|,
operator|!
name|rejected
operator|&&
name|passed
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

