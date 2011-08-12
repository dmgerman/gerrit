begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|PatchSet
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
name|RpcImpl
operator|.
name|Version
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
DECL|interface|ChangeManageService
specifier|public
interface|interface
name|ChangeManageService
extends|extends
name|RemoteJsonService
block|{
annotation|@
name|SignInRequired
DECL|method|submit (PatchSet.Id patchSetId, AsyncCallback<ChangeDetail> callback)
name|void
name|submit
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|abandonChange (PatchSet.Id patchSetId, String message, AsyncCallback<ChangeDetail> callback)
name|void
name|abandonChange
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|String
name|message
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|revertChange (PatchSet.Id patchSetId, String message, AsyncCallback<ChangeDetail> callback)
name|void
name|revertChange
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|String
name|message
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|restoreChange (PatchSet.Id patchSetId, String message, AsyncCallback<ChangeDetail> callback)
name|void
name|restoreChange
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|String
name|message
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|publish (PatchSet.Id patchSetId, AsyncCallback<ChangeDetail> callback)
name|void
name|publish
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|deleteDraftChange (PatchSet.Id patchSetId, AsyncCallback<VoidResult> callback)
name|void
name|deleteDraftChange
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
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

