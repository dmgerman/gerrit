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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|ApprovalCategory
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
name|ApprovalCategoryValue
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
name|Branch
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
name|ContributorAgreement
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
name|server
operator|.
name|ReviewDb
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
name|IdentifiedUser
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefUpdate
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
comment|/** Invokes hooks on server actions. */
end_comment

begin_interface
DECL|interface|ChangeHooks
specifier|public
interface|interface
name|ChangeHooks
block|{
DECL|method|addChangeListener (ChangeListener listener, IdentifiedUser user)
specifier|public
name|void
name|addChangeListener
parameter_list|(
name|ChangeListener
name|listener
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
function_decl|;
DECL|method|removeChangeListener (ChangeListener listener)
specifier|public
name|void
name|removeChangeListener
parameter_list|(
name|ChangeListener
name|listener
parameter_list|)
function_decl|;
comment|/**    * Fire the Patchset Created Hook.    *    * @param change The change itself.    * @param patchSet The Patchset that was created.    * @throws OrmException    */
DECL|method|doPatchsetCreatedHook (Change change, PatchSet patchSet, ReviewDb db)
specifier|public
name|void
name|doPatchsetCreatedHook
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/**    * Fire the Comment Added Hook.    *    * @param change The change itself.    * @param patchSet The patchset this comment is related to.    * @param account The gerrit user who commited the change.    * @param comment The comment given.    * @param approvals Map of Approval Categories and Scores    * @throws OrmException    */
DECL|method|doCommentAddedHook (Change change, Account account, PatchSet patchSet, String comment, Map<ApprovalCategory.Id, ApprovalCategoryValue.Id> approvals, ReviewDb db)
specifier|public
name|void
name|doCommentAddedHook
parameter_list|(
name|Change
name|change
parameter_list|,
name|Account
name|account
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|String
name|comment
parameter_list|,
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|approvals
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/**    * Fire the Change Merged Hook.    *    * @param change The change itself.    * @param account The gerrit user who commited the change.    * @param patchSet The patchset that was merged.    * @throws OrmException    */
DECL|method|doChangeMergedHook (Change change, Account account, PatchSet patchSet, ReviewDb db)
specifier|public
name|void
name|doChangeMergedHook
parameter_list|(
name|Change
name|change
parameter_list|,
name|Account
name|account
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/**    * Fire the Change Abandoned Hook.    *    * @param change The change itself.    * @param account The gerrit user who abandoned the change.    * @param reason Reason for abandoning the change.    * @throws OrmException    */
DECL|method|doChangeAbandonedHook (Change change, Account account, String reason, ReviewDb db)
specifier|public
name|void
name|doChangeAbandonedHook
parameter_list|(
name|Change
name|change
parameter_list|,
name|Account
name|account
parameter_list|,
name|String
name|reason
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/**    * Fire the Change Restored Hook.    *    * @param change The change itself.    * @param account The gerrit user who restored the change.    * @param reason Reason for restoring the change.    * @throws OrmException    */
DECL|method|doChangeRestoreHook (Change change, Account account, String reason, ReviewDb db)
specifier|public
name|void
name|doChangeRestoreHook
parameter_list|(
name|Change
name|change
parameter_list|,
name|Account
name|account
parameter_list|,
name|String
name|reason
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/**    * Fire the Ref Updated Hook    *    * @param refName The updated project and branch.    * @param refUpdate An actual RefUpdate object    * @param account The gerrit user who moved the ref    */
DECL|method|doRefUpdatedHook (Branch.NameKey refName, RefUpdate refUpdate, Account account)
specifier|public
name|void
name|doRefUpdatedHook
parameter_list|(
name|Branch
operator|.
name|NameKey
name|refName
parameter_list|,
name|RefUpdate
name|refUpdate
parameter_list|,
name|Account
name|account
parameter_list|)
function_decl|;
comment|/**    * Fire the Ref Updated Hook    *    * @param refName The Branch.NameKey of the ref that was updated    * @param oldId The ref's old id    * @param newId The ref's new id    * @param account The gerrit user who moved the ref    */
DECL|method|doRefUpdatedHook (Branch.NameKey refName, ObjectId oldId, ObjectId newId, Account account)
specifier|public
name|void
name|doRefUpdatedHook
parameter_list|(
name|Branch
operator|.
name|NameKey
name|refName
parameter_list|,
name|ObjectId
name|oldId
parameter_list|,
name|ObjectId
name|newId
parameter_list|,
name|Account
name|account
parameter_list|)
function_decl|;
DECL|method|doClaSignupHook (Account account, ContributorAgreement cla)
specifier|public
name|void
name|doClaSignupHook
parameter_list|(
name|Account
name|account
parameter_list|,
name|ContributorAgreement
name|cla
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

