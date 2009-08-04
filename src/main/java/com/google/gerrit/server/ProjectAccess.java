begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|reviewdb
operator|.
name|Project
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
name|project
operator|.
name|ProjectState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_comment
comment|/**  * Access controls for a {@link CurrentUser} within a {@link Project}.  */
end_comment

begin_class
DECL|class|ProjectAccess
specifier|public
class|class
name|ProjectAccess
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (CurrentUser u, ProjectState entry)
name|ProjectAccess
name|create
parameter_list|(
name|CurrentUser
name|u
parameter_list|,
name|ProjectState
name|entry
parameter_list|)
function_decl|;
block|}
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|entry
specifier|private
specifier|final
name|ProjectState
name|entry
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectAccess (@ssisted final CurrentUser u, @Assisted final ProjectState e)
name|ProjectAccess
parameter_list|(
annotation|@
name|Assisted
specifier|final
name|CurrentUser
name|u
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|ProjectState
name|e
parameter_list|)
block|{
name|user
operator|=
name|u
expr_stmt|;
name|entry
operator|=
name|e
expr_stmt|;
block|}
block|}
end_class

end_unit

