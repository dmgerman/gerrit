begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|events
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
name|extensions
operator|.
name|common
operator|.
name|ApprovalInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Notified whenever a vote is removed from a change. */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|VoteDeletedListener
specifier|public
interface|interface
name|VoteDeletedListener
block|{
DECL|interface|Event
interface|interface
name|Event
extends|extends
name|RevisionEvent
block|{
DECL|method|getOldApprovals ()
name|Map
argument_list|<
name|String
argument_list|,
name|ApprovalInfo
argument_list|>
name|getOldApprovals
parameter_list|()
function_decl|;
DECL|method|getApprovals ()
name|Map
argument_list|<
name|String
argument_list|,
name|ApprovalInfo
argument_list|>
name|getApprovals
parameter_list|()
function_decl|;
DECL|method|getRemoved ()
name|Map
argument_list|<
name|String
argument_list|,
name|ApprovalInfo
argument_list|>
name|getRemoved
parameter_list|()
function_decl|;
DECL|method|getMessage ()
name|String
name|getMessage
parameter_list|()
function_decl|;
block|}
DECL|method|onVoteDeleted (Event event)
name|void
name|onVoteDeleted
parameter_list|(
name|Event
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

