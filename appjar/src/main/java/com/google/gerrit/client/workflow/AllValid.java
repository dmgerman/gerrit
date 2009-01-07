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
name|rpc
operator|.
name|Common
import|;
end_import

begin_comment
comment|/**  * Computes an {@link ApprovalCategory} by requiring all others to be valid.  *<p>  * In order to be considered "approved" this function requires that all approval  * categories with a position>= 0 (that is any whose  * {@link ApprovalCategory#isAction()} method returns false) is valid in the  * state.  *<p>  * This is mostly useful for actions, like {@link ApprovalCategory#SUBMIT}.  */
end_comment

begin_class
DECL|class|AllValid
specifier|public
class|class
name|AllValid
extends|extends
name|CategoryFunction
block|{
DECL|field|NAME
specifier|public
specifier|static
name|String
name|NAME
init|=
literal|"AllValid"
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
for|for
control|(
specifier|final
name|ApprovalType
name|t
range|:
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getApprovalTypes
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|state
operator|.
name|isValid
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|state
operator|.
name|valid
argument_list|(
name|at
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|state
operator|.
name|valid
argument_list|(
name|at
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

