begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.audit
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|GroupMemberAuditListener
specifier|public
interface|interface
name|GroupMemberAuditListener
block|{
DECL|method|onAddAccountsToGroup (Account.Id actor, Collection<AccountGroupMember> added)
name|void
name|onAddAccountsToGroup
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
DECL|method|onDeleteAccountsFromGroup (Account.Id actor, Collection<AccountGroupMember> removed)
name|void
name|onDeleteAccountsFromGroup
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
DECL|method|onAddGroupsToGroup (Account.Id actor, Collection<AccountGroupById> added)
name|void
name|onAddGroupsToGroup
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
DECL|method|onDeleteGroupsFromGroup (Account.Id actor, Collection<AccountGroupById> deleted)
name|void
name|onDeleteGroupsFromGroup
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
name|deleted
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
