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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
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
name|entities
operator|.
name|Account
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
name|entities
operator|.
name|AccountGroup
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
name|AuditEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_interface
DECL|interface|GroupAuditService
specifier|public
interface|interface
name|GroupAuditService
block|{
DECL|method|dispatch (AuditEvent action)
name|void
name|dispatch
parameter_list|(
name|AuditEvent
name|action
parameter_list|)
function_decl|;
DECL|method|dispatchAddMembers ( Account.Id actor, AccountGroup.UUID updatedGroup, ImmutableSet<Account.Id> addedMembers, Timestamp addedOn)
name|void
name|dispatchAddMembers
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|updatedGroup
parameter_list|,
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|addedMembers
parameter_list|,
name|Timestamp
name|addedOn
parameter_list|)
function_decl|;
DECL|method|dispatchDeleteMembers ( Account.Id actor, AccountGroup.UUID updatedGroup, ImmutableSet<Account.Id> deletedMembers, Timestamp deletedOn)
name|void
name|dispatchDeleteMembers
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|updatedGroup
parameter_list|,
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|deletedMembers
parameter_list|,
name|Timestamp
name|deletedOn
parameter_list|)
function_decl|;
DECL|method|dispatchAddSubgroups ( Account.Id actor, AccountGroup.UUID updatedGroup, ImmutableSet<AccountGroup.UUID> addedSubgroups, Timestamp addedOn)
name|void
name|dispatchAddSubgroups
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|updatedGroup
parameter_list|,
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|addedSubgroups
parameter_list|,
name|Timestamp
name|addedOn
parameter_list|)
function_decl|;
DECL|method|dispatchDeleteSubgroups ( Account.Id actor, AccountGroup.UUID updatedGroup, ImmutableSet<AccountGroup.UUID> deletedSubgroups, Timestamp deletedOn)
name|void
name|dispatchDeleteSubgroups
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|updatedGroup
parameter_list|,
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|deletedSubgroups
parameter_list|,
name|Timestamp
name|deletedOn
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

