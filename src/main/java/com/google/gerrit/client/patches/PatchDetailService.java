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
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|client
operator|.
name|data
operator|.
name|SideBySidePatchDetail
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
name|client
operator|.
name|data
operator|.
name|UnifiedPatchDetail
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
name|client
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
name|client
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
name|client
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
name|client
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
name|client
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
name|client
operator|.
name|rpc
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
name|VoidResult
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
DECL|interface|PatchDetailService
specifier|public
interface|interface
name|PatchDetailService
extends|extends
name|RemoteJsonService
block|{
DECL|method|sideBySidePatchDetail (Patch.Key key, List<PatchSet.Id> versions, AsyncCallback<SideBySidePatchDetail> callback)
name|void
name|sideBySidePatchDetail
parameter_list|(
name|Patch
operator|.
name|Key
name|key
parameter_list|,
name|List
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|versions
parameter_list|,
name|AsyncCallback
argument_list|<
name|SideBySidePatchDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
DECL|method|unifiedPatchDetail (Patch.Key key, AsyncCallback<UnifiedPatchDetail> callback)
name|void
name|unifiedPatchDetail
parameter_list|(
name|Patch
operator|.
name|Key
name|key
parameter_list|,
name|AsyncCallback
argument_list|<
name|UnifiedPatchDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|myDrafts (Patch.Key key, AsyncCallback<List<PatchLineComment>> callback)
name|void
name|myDrafts
parameter_list|(
name|Patch
operator|.
name|Key
name|key
parameter_list|,
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|PatchLineComment
argument_list|>
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
DECL|method|addReviewers (Change.Id id, List<String> reviewers, AsyncCallback<VoidResult> callback)
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
name|VoidResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

