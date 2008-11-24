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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Relation
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
name|client
operator|.
name|Schema
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
name|client
operator|.
name|Sequence
import|;
end_import

begin_comment
comment|/** The review service database schema. */
end_comment

begin_interface
DECL|interface|ReviewDb
specifier|public
interface|interface
name|ReviewDb
extends|extends
name|Schema
block|{
annotation|@
name|Relation
DECL|method|systemConfig ()
name|SystemConfigAccess
name|systemConfig
parameter_list|()
function_decl|;
annotation|@
name|Relation
DECL|method|contributorAgreements ()
name|ContributorAgreementAccess
name|contributorAgreements
parameter_list|()
function_decl|;
annotation|@
name|Relation
DECL|method|accounts ()
name|AccountAccess
name|accounts
parameter_list|()
function_decl|;
annotation|@
name|Relation
DECL|method|accountAgreements ()
name|AccountAgreementAccess
name|accountAgreements
parameter_list|()
function_decl|;
annotation|@
name|Relation
DECL|method|accountGroups ()
name|AccountGroupAccess
name|accountGroups
parameter_list|()
function_decl|;
annotation|@
name|Relation
DECL|method|accountGroupMembers ()
name|AccountGroupMemberAccess
name|accountGroupMembers
parameter_list|()
function_decl|;
annotation|@
name|Relation
DECL|method|projects ()
name|ProjectAccess
name|projects
parameter_list|()
function_decl|;
annotation|@
name|Relation
DECL|method|branches ()
name|BranchAccess
name|branches
parameter_list|()
function_decl|;
comment|/** Create the next unique id for an {@link Account}. */
annotation|@
name|Sequence
argument_list|(
name|startWith
operator|=
literal|1000000
argument_list|)
DECL|method|nextAccountId ()
name|int
name|nextAccountId
parameter_list|()
function_decl|;
comment|/** Create the next unique id for a {@link ContributorAgreement}. */
annotation|@
name|Sequence
DECL|method|nextContributorAgreementId ()
name|int
name|nextContributorAgreementId
parameter_list|()
function_decl|;
comment|/** Next unique id for a {@link AccountGroup}. */
annotation|@
name|Sequence
DECL|method|nextAccountGroupId ()
name|int
name|nextAccountGroupId
parameter_list|()
function_decl|;
comment|/** Next unique id for a {@link Project}. */
annotation|@
name|Sequence
DECL|method|nextProjectId ()
name|int
name|nextProjectId
parameter_list|()
function_decl|;
comment|/** Next unique id for a {@link Branch}. */
annotation|@
name|Sequence
DECL|method|nextBranchId ()
name|int
name|nextBranchId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

