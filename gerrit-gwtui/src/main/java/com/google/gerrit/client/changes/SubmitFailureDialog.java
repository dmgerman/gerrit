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
name|ChangeMessage
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickHandler
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
name|Button
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
name|Widget
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|user
operator|.
name|client
operator|.
name|AutoCenterDialogBox
import|;
end_import

begin_class
DECL|class|SubmitFailureDialog
class|class
name|SubmitFailureDialog
extends|extends
name|AutoCenterDialogBox
block|{
DECL|method|SubmitFailureDialog (final ChangeDetail result, final ChangeMessage msg)
name|SubmitFailureDialog
parameter_list|(
specifier|final
name|ChangeDetail
name|result
parameter_list|,
specifier|final
name|ChangeMessage
name|msg
parameter_list|)
block|{
name|setText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|submitFailed
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|body
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
specifier|final
name|Widget
name|msgText
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|msg
operator|.
name|getMessage
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|wikify
argument_list|()
operator|.
name|toBlockWidget
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
name|msgText
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|buttonPanel
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|buttonPanel
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-CommentEditor-Buttons"
argument_list|)
expr_stmt|;
name|Button
name|close
init|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonClose
argument_list|()
argument_list|)
decl_stmt|;
name|close
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|close
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|buttonPanel
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

