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
name|rpc
operator|.
name|ScreenLoadCallback
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
name|ui
operator|.
name|FlowPanel
import|;
end_import

begin_class
DECL|class|PatchSideBySideScreen
specifier|public
class|class
name|PatchSideBySideScreen
extends|extends
name|PatchScreen
block|{
DECL|field|sbsTable
specifier|private
name|SideBySideTable
name|sbsTable
decl_stmt|;
DECL|method|PatchSideBySideScreen (final Patch.Id id)
specifier|public
name|PatchSideBySideScreen
parameter_list|(
specifier|final
name|Patch
operator|.
name|Id
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|public
name|void
name|onLoad
parameter_list|()
block|{
if|if
condition|(
name|sbsTable
operator|==
literal|null
condition|)
block|{
name|initUI
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|PatchUtil
operator|.
name|DETAIL_SVC
operator|.
name|sideBySidePatchDetail
argument_list|(
name|patchId
argument_list|,
operator|new
name|ScreenLoadCallback
argument_list|<
name|SideBySidePatchDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|SideBySidePatchDetail
name|r
parameter_list|)
block|{
comment|// TODO Actually we want to cancel the RPC if detached.
if|if
condition|(
name|isAttached
argument_list|()
condition|)
block|{
name|display
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|initUI ()
specifier|private
name|void
name|initUI
parameter_list|()
block|{
specifier|final
name|FlowPanel
name|sbsPanel
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|sbsPanel
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-SideBySideScreen-SideBySideTable"
argument_list|)
expr_stmt|;
name|sbsTable
operator|=
operator|new
name|SideBySideTable
argument_list|()
expr_stmt|;
name|sbsPanel
operator|.
name|add
argument_list|(
name|sbsTable
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|sbsPanel
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final SideBySidePatchDetail detail)
specifier|private
name|void
name|display
parameter_list|(
specifier|final
name|SideBySidePatchDetail
name|detail
parameter_list|)
block|{
name|sbsTable
operator|.
name|setAccountInfoCache
argument_list|(
name|detail
operator|.
name|getAccounts
argument_list|()
argument_list|)
expr_stmt|;
name|sbsTable
operator|.
name|display
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

