begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
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
name|Gerrit
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
name|ChangeApi
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
name|Util
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
name|info
operator|.
name|ChangeInfo
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
name|GerritCallback
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
name|ui
operator|.
name|MoveDialog
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
name|PageLinks
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
name|Project
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
name|logical
operator|.
name|shared
operator|.
name|CloseEvent
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
name|PopupPanel
import|;
end_import

begin_class
DECL|class|MoveAction
class|class
name|MoveAction
block|{
DECL|method|call (Button b, ChangeInfo info, Project.NameKey project)
specifier|static
name|void
name|call
parameter_list|(
name|Button
name|b
parameter_list|,
name|ChangeInfo
name|info
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|b
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
operator|new
name|MoveDialog
argument_list|(
name|project
argument_list|)
block|{
block|{
name|sendButton
operator|.
name|setText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|moveChangeSend
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSend
parameter_list|()
block|{
name|ChangeApi
operator|.
name|move
argument_list|(
name|info
operator|.
name|project
argument_list|()
argument_list|,
name|info
operator|.
name|legacyId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|getDestinationBranch
argument_list|()
argument_list|,
name|getMessageText
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeInfo
name|result
parameter_list|)
block|{
name|sent
operator|=
literal|true
expr_stmt|;
name|hide
argument_list|()
expr_stmt|;
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|project
argument_list|,
name|result
operator|.
name|legacyId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|enableButtons
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|CloseEvent
argument_list|<
name|PopupPanel
argument_list|>
name|event
parameter_list|)
block|{
name|super
operator|.
name|onClose
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|b
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

