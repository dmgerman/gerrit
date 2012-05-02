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
name|AccountGroup
import|;
end_import

begin_comment
comment|/**  * Group methods exposed by the GroupBackend.  */
end_comment

begin_class
DECL|class|GroupDescription
specifier|public
class|class
name|GroupDescription
block|{
comment|/**    * The Basic information required to be exposed by any Group.    */
DECL|interface|Basic
specifier|public
interface|interface
name|Basic
block|{
comment|/** @return the non-null UUID of the group. */
DECL|method|getGroupUUID ()
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
function_decl|;
comment|/** @return the non-null name of the group. */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/** @return whether the group is visible to all accounts. */
DECL|method|isVisibleToAll ()
name|boolean
name|isVisibleToAll
parameter_list|()
function_decl|;
block|}
comment|/**    * The extended information exposed by internal groups backed by an    * AccountGroup.    */
DECL|interface|Internal
specifier|public
interface|interface
name|Internal
extends|extends
name|Basic
block|{
comment|/** @return the backing AccountGroup. */
DECL|method|getAccountGroup ()
name|AccountGroup
name|getAccountGroup
parameter_list|()
function_decl|;
block|}
DECL|method|GroupDescription ()
specifier|private
name|GroupDescription
parameter_list|()
block|{   }
block|}
end_class

end_unit

