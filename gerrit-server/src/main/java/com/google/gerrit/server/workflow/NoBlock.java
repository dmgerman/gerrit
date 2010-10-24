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
name|server
operator|.
name|CurrentUser
import|;
end_import

begin_comment
comment|/** A function that does nothing. */
end_comment

begin_class
DECL|class|NoBlock
specifier|public
class|class
name|NoBlock
extends|extends
name|CategoryFunction
block|{
DECL|field|NAME
specifier|public
specifier|static
name|String
name|NAME
init|=
literal|"NoBlock"
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
annotation|@
name|Override
DECL|method|isValid (final CurrentUser user, final ApprovalType at, final FunctionState state)
specifier|public
name|boolean
name|isValid
parameter_list|(
specifier|final
name|CurrentUser
name|user
parameter_list|,
specifier|final
name|ApprovalType
name|at
parameter_list|,
specifier|final
name|FunctionState
name|state
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

