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
name|common
operator|.
name|auth
operator|.
name|SignInRequired
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
name|Patch
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
name|PatchLineComment
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
name|Patch
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RpcImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RpcImpl
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_interface
annotation|@
name|RpcImpl
argument_list|(
name|version
operator|=
name|Version
operator|.
name|V2_0
argument_list|)
DECL|interface|PatchDetailService
specifier|public
interface|interface
name|PatchDetailService
extends|extends
name|RemoteJsonService
block|{
DECL|method|patchScript (Patch.Key key, PatchSet.Id a, PatchSet.Id b, PatchScriptSettings settings, AsyncCallback<PatchScript> callback)
name|void
name|patchScript
parameter_list|(
name|Patch
operator|.
name|Key
name|key
parameter_list|,
name|PatchSet
operator|.
name|Id
name|a
parameter_list|,
name|PatchSet
operator|.
name|Id
name|b
parameter_list|,
name|PatchScriptSettings
name|settings
parameter_list|,
name|AsyncCallback
argument_list|<
name|PatchScript
argument_list|>
name|callback
parameter_list|)
function_decl|;
DECL|method|patchComments (Patch.Key key, PatchSet.Id a, PatchSet.Id b, AsyncCallback<CommentDetail> callback)
name|void
name|patchComments
parameter_list|(
name|Patch
operator|.
name|Key
name|key
parameter_list|,
name|PatchSet
operator|.
name|Id
name|a
parameter_list|,
name|PatchSet
operator|.
name|Id
name|b
parameter_list|,
name|AsyncCallback
argument_list|<
name|CommentDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|saveDraft (PatchLineComment comment, AsyncCallback<PatchLineComment> callback)
name|void
name|saveDraft
parameter_list|(
name|PatchLineComment
name|comment
parameter_list|,
name|AsyncCallback
argument_list|<
name|PatchLineComment
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|deleteDraft (PatchLineComment.Key key, AsyncCallback<VoidResult> callback)
name|void
name|deleteDraft
parameter_list|(
name|PatchLineComment
operator|.
name|Key
name|key
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|publishComments (PatchSet.Id psid, String message, Set<ApprovalCategoryValue.Id> approvals, AsyncCallback<VoidResult> callback)
name|void
name|publishComments
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psid
parameter_list|,
name|String
name|message
parameter_list|,
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|approvals
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|addReviewers (Change.Id id, List<String> reviewers, AsyncCallback<AddReviewerResult> callback)
name|void
name|addReviewers
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|reviewers
parameter_list|,
name|AsyncCallback
argument_list|<
name|AddReviewerResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
DECL|method|userApprovals (Set<Change.Id> cids, Account.Id aid, AsyncCallback<ApprovalSummarySet> callback)
name|void
name|userApprovals
parameter_list|(
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|cids
parameter_list|,
name|Account
operator|.
name|Id
name|aid
parameter_list|,
name|AsyncCallback
argument_list|<
name|ApprovalSummarySet
argument_list|>
name|callback
parameter_list|)
function_decl|;
DECL|method|strongestApprovals (Set<Change.Id> cids, AsyncCallback<ApprovalSummarySet> callback)
name|void
name|strongestApprovals
parameter_list|(
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|cids
parameter_list|,
name|AsyncCallback
argument_list|<
name|ApprovalSummarySet
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/**    * Update the reviewed status for the patch.    */
annotation|@
name|SignInRequired
DECL|method|setReviewedByCurrentUser (Key patchKey, boolean reviewed, AsyncCallback<VoidResult> callback)
name|void
name|setReviewedByCurrentUser
parameter_list|(
name|Key
name|patchKey
parameter_list|,
name|boolean
name|reviewed
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

