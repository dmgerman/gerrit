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
DECL|package|com.google.gerrit.server.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rpc
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
name|changes
operator|.
name|ChangeDetailService
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
name|changes
operator|.
name|PatchSetPublishDetail
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
name|ChangeDetail
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
name|PatchSetDetail
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
DECL|class|ChangeDetailServiceImpl
class|class
name|ChangeDetailServiceImpl
implements|implements
name|ChangeDetailService
block|{
DECL|field|changeDetail
specifier|private
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetail
decl_stmt|;
DECL|field|patchSetDetail
specifier|private
specifier|final
name|PatchSetDetailFactory
operator|.
name|Factory
name|patchSetDetail
decl_stmt|;
DECL|field|patchSetPublishDetail
specifier|private
specifier|final
name|PatchSetPublishDetailFactory
operator|.
name|Factory
name|patchSetPublishDetail
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeDetailServiceImpl (final ChangeDetailFactory.Factory changeDetail, final PatchSetDetailFactory.Factory patchSetDetail, final PatchSetPublishDetailFactory.Factory patchSetPublishDetail)
name|ChangeDetailServiceImpl
parameter_list|(
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetail
parameter_list|,
specifier|final
name|PatchSetDetailFactory
operator|.
name|Factory
name|patchSetDetail
parameter_list|,
specifier|final
name|PatchSetPublishDetailFactory
operator|.
name|Factory
name|patchSetPublishDetail
parameter_list|)
block|{
name|this
operator|.
name|changeDetail
operator|=
name|changeDetail
expr_stmt|;
name|this
operator|.
name|patchSetDetail
operator|=
name|patchSetDetail
expr_stmt|;
name|this
operator|.
name|patchSetPublishDetail
operator|=
name|patchSetPublishDetail
expr_stmt|;
block|}
DECL|method|changeDetail (final Change.Id id, final AsyncCallback<ChangeDetail> callback)
specifier|public
name|void
name|changeDetail
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|changeDetail
operator|.
name|create
argument_list|(
name|id
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|patchSetDetail (final PatchSet.Id id, final AsyncCallback<PatchSetDetail> callback)
specifier|public
name|void
name|patchSetDetail
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|PatchSetDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|patchSetDetail
operator|.
name|create
argument_list|(
name|id
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|patchSetPublishDetail (final PatchSet.Id id, final AsyncCallback<PatchSetPublishDetail> callback)
specifier|public
name|void
name|patchSetPublishDetail
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|PatchSetPublishDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|patchSetPublishDetail
operator|.
name|create
argument_list|(
name|id
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

