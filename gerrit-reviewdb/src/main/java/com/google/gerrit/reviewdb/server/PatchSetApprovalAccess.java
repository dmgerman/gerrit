begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
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
name|Change
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
name|PatchSet
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
name|PatchSetApproval
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
name|Change
operator|.
name|Id
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
name|PatchSetApproval
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|Access
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|PrimaryKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
import|;
end_import

begin_interface
DECL|interface|PatchSetApprovalAccess
specifier|public
interface|interface
name|PatchSetApprovalAccess
extends|extends
name|Access
argument_list|<
name|PatchSetApproval
argument_list|,
name|PatchSetApproval
operator|.
name|Key
argument_list|>
block|{
annotation|@
name|PrimaryKey
argument_list|(
literal|"key"
argument_list|)
DECL|method|get (PatchSetApproval.Key key)
name|PatchSetApproval
name|get
parameter_list|(
name|PatchSetApproval
operator|.
name|Key
name|key
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE key.patchSetId.changeId = ?"
argument_list|)
DECL|method|byChange (Change.Id id)
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|byChange
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE key.patchSetId = ?"
argument_list|)
DECL|method|byPatchSet (PatchSet.Id id)
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSet
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE key.patchSetId = ? AND key.accountId = ?"
argument_list|)
DECL|method|byPatchSetUser (PatchSet.Id patchSet, Account.Id account)
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSetUser
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSet
parameter_list|,
name|Account
operator|.
name|Id
name|account
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE changeOpen = true AND key.accountId = ?"
argument_list|)
DECL|method|openByUser (Account.Id account)
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|openByUser
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE changeOpen = false AND key.accountId = ?"
operator|+
literal|" ORDER BY changeSortKey DESC LIMIT 10"
argument_list|)
DECL|method|closedByUser (Account.Id account)
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|closedByUser
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE changeOpen = false AND key.accountId = ? ORDER BY changeSortKey"
argument_list|)
DECL|method|closedByUserAll (Account.Id account)
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|closedByUserAll
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

