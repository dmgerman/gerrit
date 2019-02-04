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
DECL|package|com.google.gerrit.audit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|audit
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroupById
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
name|client
operator|.
name|AccountGroupMember
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
name|ImplementedBy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_interface
annotation|@
name|ImplementedBy
argument_list|(
name|AuditServiceImpl
operator|.
name|class
argument_list|)
DECL|interface|AuditService
specifier|public
interface|interface
name|AuditService
block|{
DECL|method|dispatch (AuditEvent action)
name|void
name|dispatch
parameter_list|(
name|AuditEvent
name|action
parameter_list|)
function_decl|;
DECL|method|dispatchAddAccountsToGroup (Account.Id actor, Collection<AccountGroupMember> added)
name|void
name|dispatchAddAccountsToGroup
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|Collection
argument_list|<
name|AccountGroupMember
argument_list|>
name|added
parameter_list|)
function_decl|;
DECL|method|dispatchDeleteAccountsFromGroup (Account.Id actor, Collection<AccountGroupMember> removed)
name|void
name|dispatchDeleteAccountsFromGroup
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|Collection
argument_list|<
name|AccountGroupMember
argument_list|>
name|removed
parameter_list|)
function_decl|;
DECL|method|dispatchAddGroupsToGroup (Account.Id actor, Collection<AccountGroupById> added)
name|void
name|dispatchAddGroupsToGroup
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|Collection
argument_list|<
name|AccountGroupById
argument_list|>
name|added
parameter_list|)
function_decl|;
DECL|method|dispatchDeleteGroupsFromGroup (Account.Id actor, Collection<AccountGroupById> removed)
name|void
name|dispatchDeleteGroupsFromGroup
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|Collection
argument_list|<
name|AccountGroupById
argument_list|>
name|removed
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

