begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.audit.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|audit
operator|.
name|group
package|;
end_package

begin_interface
DECL|interface|GroupAuditListener
specifier|public
interface|interface
name|GroupAuditListener
block|{
DECL|method|onAddMembers (GroupMemberAuditEvent groupMemberAuditEvent)
name|void
name|onAddMembers
parameter_list|(
name|GroupMemberAuditEvent
name|groupMemberAuditEvent
parameter_list|)
function_decl|;
DECL|method|onDeleteMembers (GroupMemberAuditEvent groupMemberAuditEvent)
name|void
name|onDeleteMembers
parameter_list|(
name|GroupMemberAuditEvent
name|groupMemberAuditEvent
parameter_list|)
function_decl|;
DECL|method|onAddSubgroups (GroupSubgroupAuditEvent groupSubgroupAuditEvent)
name|void
name|onAddSubgroups
parameter_list|(
name|GroupSubgroupAuditEvent
name|groupSubgroupAuditEvent
parameter_list|)
function_decl|;
DECL|method|onDeleteSubgroups (GroupSubgroupAuditEvent groupSubgroupAuditEvent)
name|void
name|onDeleteSubgroups
parameter_list|(
name|GroupSubgroupAuditEvent
name|groupSubgroupAuditEvent
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

