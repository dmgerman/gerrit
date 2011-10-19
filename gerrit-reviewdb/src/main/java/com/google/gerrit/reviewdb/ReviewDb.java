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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
comment|/**  * The review service database schema.  *<p>  * Root entities that are at the top level of some important data graph:  *<ul>  *<li>{@link Account}: Per-user account registration, preferences, identity.</li>  *<li>{@link Change}: All review information about a single proposed change.</li>  *<li>{@link SystemConfig}: Server-wide settings, managed by administrator.</li>  *</ul>  */
end_comment

begin_interface
DECL|interface|ReviewDb
specifier|public
interface|interface
name|ReviewDb
extends|extends
name|Schema
block|{
comment|/* If you change anything, update SchemaVersion.C to use a new version. */
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|method|schemaVersion ()
name|SchemaVersionAccess
name|schemaVersion
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|method|systemConfig ()
name|SystemConfigAccess
name|systemConfig
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|3
argument_list|)
DECL|method|approvalCategories ()
name|ApprovalCategoryAccess
name|approvalCategories
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|4
argument_list|)
DECL|method|approvalCategoryValues ()
name|ApprovalCategoryValueAccess
name|approvalCategoryValues
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|5
argument_list|)
DECL|method|contributorAgreements ()
name|ContributorAgreementAccess
name|contributorAgreements
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|6
argument_list|)
DECL|method|accounts ()
name|AccountAccess
name|accounts
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|7
argument_list|)
DECL|method|accountExternalIds ()
name|AccountExternalIdAccess
name|accountExternalIds
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|8
argument_list|)
DECL|method|accountSshKeys ()
name|AccountSshKeyAccess
name|accountSshKeys
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|9
argument_list|)
DECL|method|accountAgreements ()
name|AccountAgreementAccess
name|accountAgreements
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|10
argument_list|)
DECL|method|accountGroups ()
name|AccountGroupAccess
name|accountGroups
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|11
argument_list|)
DECL|method|accountGroupNames ()
name|AccountGroupNameAccess
name|accountGroupNames
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|12
argument_list|)
DECL|method|accountGroupMembers ()
name|AccountGroupMemberAccess
name|accountGroupMembers
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|13
argument_list|)
DECL|method|accountGroupMembersAudit ()
name|AccountGroupMemberAuditAccess
name|accountGroupMembersAudit
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|14
argument_list|)
DECL|method|accountGroupIncludes ()
name|AccountGroupIncludeAccess
name|accountGroupIncludes
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|15
argument_list|)
DECL|method|accountGroupIncludesAudit ()
name|AccountGroupIncludeAuditAccess
name|accountGroupIncludesAudit
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|16
argument_list|)
DECL|method|accountGroupAgreements ()
name|AccountGroupAgreementAccess
name|accountGroupAgreements
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|17
argument_list|)
DECL|method|accountDiffPreferences ()
name|AccountDiffPreferenceAccess
name|accountDiffPreferences
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|18
argument_list|)
DECL|method|starredChanges ()
name|StarredChangeAccess
name|starredChanges
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|19
argument_list|)
DECL|method|accountProjectWatches ()
name|AccountProjectWatchAccess
name|accountProjectWatches
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|20
argument_list|)
DECL|method|accountPatchReviews ()
name|AccountPatchReviewAccess
name|accountPatchReviews
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|21
argument_list|)
DECL|method|changes ()
name|ChangeAccess
name|changes
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|22
argument_list|)
DECL|method|patchSetApprovals ()
name|PatchSetApprovalAccess
name|patchSetApprovals
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|23
argument_list|)
DECL|method|changeMessages ()
name|ChangeMessageAccess
name|changeMessages
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|24
argument_list|)
DECL|method|patchSets ()
name|PatchSetAccess
name|patchSets
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|25
argument_list|)
DECL|method|patchSetAncestors ()
name|PatchSetAncestorAccess
name|patchSetAncestors
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|26
argument_list|)
DECL|method|patchComments ()
name|PatchLineCommentAccess
name|patchComments
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|27
argument_list|)
DECL|method|trackingIds ()
name|TrackingIdAccess
name|trackingIds
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
throws|throws
name|OrmException
function_decl|;
comment|/** Create the next unique id for a {@link ContributorAgreement}. */
annotation|@
name|Sequence
DECL|method|nextContributorAgreementId ()
name|int
name|nextContributorAgreementId
parameter_list|()
throws|throws
name|OrmException
function_decl|;
comment|/** Next unique id for a {@link AccountGroup}. */
annotation|@
name|Sequence
DECL|method|nextAccountGroupId ()
name|int
name|nextAccountGroupId
parameter_list|()
throws|throws
name|OrmException
function_decl|;
comment|/** Next unique id for a {@link Change}. */
annotation|@
name|Sequence
DECL|method|nextChangeId ()
name|int
name|nextChangeId
parameter_list|()
throws|throws
name|OrmException
function_decl|;
comment|/**    * Next id for a block of {@link ChangeMessage} records.    *    * @see com.google.gerrit.server.ChangeUtil#messageUUID(ReviewDb)    */
annotation|@
name|Sequence
DECL|method|nextChangeMessageId ()
name|int
name|nextChangeMessageId
parameter_list|()
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

