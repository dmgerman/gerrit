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
name|audit
operator|.
name|Audit
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
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
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
name|gwtjsonrpc
operator|.
name|common
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
name|common
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
name|common
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
name|common
operator|.
name|RpcImpl
operator|.
name|Version
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
DECL|interface|ChangeDetailService
specifier|public
interface|interface
name|ChangeDetailService
extends|extends
name|RemoteJsonService
block|{
annotation|@
name|Audit
DECL|method|patchSetDetail (PatchSet.Id key, AsyncCallback<PatchSetDetail> callback)
name|void
name|patchSetDetail
parameter_list|(
name|PatchSet
operator|.
name|Id
name|key
parameter_list|,
name|AsyncCallback
argument_list|<
name|PatchSetDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|Audit
DECL|method|patchSetDetail2 (PatchSet.Id baseId, PatchSet.Id key, DiffPreferencesInfo diffPrefs, AsyncCallback<PatchSetDetail> callback)
name|void
name|patchSetDetail2
parameter_list|(
name|PatchSet
operator|.
name|Id
name|baseId
parameter_list|,
name|PatchSet
operator|.
name|Id
name|key
parameter_list|,
name|DiffPreferencesInfo
name|diffPrefs
parameter_list|,
name|AsyncCallback
argument_list|<
name|PatchSetDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

