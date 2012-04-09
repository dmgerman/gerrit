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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|ui
operator|.
name|ListenableValue
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
name|common
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|FocusWidget
import|;
end_import

begin_class
DECL|class|ChangeDetailCache
specifier|public
class|class
name|ChangeDetailCache
extends|extends
name|ListenableValue
argument_list|<
name|ChangeDetail
argument_list|>
block|{
DECL|class|GerritCallback
specifier|public
specifier|static
class|class
name|GerritCallback
extends|extends
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
name|GerritCallback
argument_list|<
name|ChangeDetail
argument_list|>
block|{
annotation|@
name|Override
DECL|method|onSuccess (ChangeDetail detail)
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeDetail
name|detail
parameter_list|)
block|{
name|setChangeDetail
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*    * GerritCallback which will re-enable a FocusWidget    * {@link com.google.gwt.user.client.ui.FocusWidget} if we are returning    * with a failed result.    *    * It is up to the caller to handle the original disabling of the Widget.    */
DECL|class|GerritWidgetCallback
specifier|public
specifier|static
class|class
name|GerritWidgetCallback
extends|extends
name|GerritCallback
block|{
DECL|field|widget
specifier|private
name|FocusWidget
name|widget
decl_stmt|;
DECL|method|GerritWidgetCallback (FocusWidget widget)
specifier|public
name|GerritWidgetCallback
parameter_list|(
name|FocusWidget
name|widget
parameter_list|)
block|{
name|this
operator|.
name|widget
operator|=
name|widget
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable caught)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|widget
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|IgnoreErrorCallback
specifier|public
specifier|static
class|class
name|IgnoreErrorCallback
implements|implements
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
block|{
annotation|@
name|Override
DECL|method|onSuccess (ChangeDetail detail)
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeDetail
name|detail
parameter_list|)
block|{
name|setChangeDetail
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable caught)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{     }
block|}
DECL|method|setChangeDetail (ChangeDetail detail)
specifier|public
specifier|static
name|void
name|setChangeDetail
parameter_list|(
name|ChangeDetail
name|detail
parameter_list|)
block|{
name|Change
operator|.
name|Id
name|chgId
init|=
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|ChangeCache
operator|.
name|get
argument_list|(
name|chgId
argument_list|)
operator|.
name|getChangeDetailCache
argument_list|()
operator|.
name|set
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|method|ChangeDetailCache (final Change.Id chg)
specifier|public
name|ChangeDetailCache
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|chg
parameter_list|)
block|{
name|changeId
operator|=
name|chg
expr_stmt|;
block|}
DECL|method|refresh ()
specifier|public
name|void
name|refresh
parameter_list|()
block|{
name|Util
operator|.
name|DETAIL_SVC
operator|.
name|changeDetail
argument_list|(
name|changeId
argument_list|,
operator|new
name|GerritCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

