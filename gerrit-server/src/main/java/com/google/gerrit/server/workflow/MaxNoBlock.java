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
DECL|package|com.google.gerrit.server.workflow
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|common
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
name|reviewdb
operator|.
name|PatchSetApproval
import|;
end_import

begin_comment
comment|/**  * Computes an {@link ApprovalCategory} by looking at maximum values.  *<p>  * In order to be considered "approved" this function requires that:  *<ul>  *<li>The maximum positive value is used at least once;</li>  *<li>The user approving the maximum positive has been granted that.</li>  *</ul>  *<p>  * This function is primarily useful for advisory review fields.  */
end_comment

begin_class
DECL|class|MaxNoBlock
specifier|public
class|class
name|MaxNoBlock
extends|extends
name|CategoryFunction
block|{
DECL|field|NAME
specifier|public
specifier|static
name|String
name|NAME
init|=
literal|"MaxNoBlock"
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
name|passed
init|=
literal|false
decl_stmt|;
for|for
control|(
specifier|final
name|PatchSetApproval
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
comment|// The type must have at least one max positive (a full accept).
comment|//
name|state
operator|.
name|valid
argument_list|(
name|at
argument_list|,
name|passed
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

