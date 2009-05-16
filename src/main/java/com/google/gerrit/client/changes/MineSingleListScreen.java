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
name|data
operator|.
name|SingleListChangeInfo
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
name|gerrit
operator|.
name|client
operator|.
name|ui
operator|.
name|AccountScreen
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

begin_class
DECL|class|MineSingleListScreen
specifier|public
specifier|abstract
class|class
name|MineSingleListScreen
extends|extends
name|AccountScreen
block|{
DECL|field|anchor
specifier|private
specifier|final
name|String
name|anchor
decl_stmt|;
DECL|field|table
specifier|private
name|ChangeTable
name|table
decl_stmt|;
DECL|field|drafts
specifier|private
name|ChangeTable
operator|.
name|Section
name|drafts
decl_stmt|;
DECL|method|MineSingleListScreen (final String historyToken)
specifier|protected
name|MineSingleListScreen
parameter_list|(
specifier|final
name|String
name|historyToken
parameter_list|)
block|{
name|anchor
operator|=
name|historyToken
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|table
operator|=
operator|new
name|ChangeTable
argument_list|()
expr_stmt|;
name|drafts
operator|=
operator|new
name|ChangeTable
operator|.
name|Section
argument_list|()
expr_stmt|;
name|table
operator|.
name|addSection
argument_list|(
name|drafts
argument_list|)
expr_stmt|;
name|table
operator|.
name|setSavePointerId
argument_list|(
name|anchor
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerKeys ()
specifier|public
name|void
name|registerKeys
parameter_list|()
block|{
name|super
operator|.
name|registerKeys
argument_list|()
expr_stmt|;
name|table
operator|.
name|setRegisterKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|loadCallback ()
specifier|protected
name|AsyncCallback
argument_list|<
name|SingleListChangeInfo
argument_list|>
name|loadCallback
parameter_list|()
block|{
return|return
operator|new
name|ScreenLoadCallback
argument_list|<
name|SingleListChangeInfo
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
specifier|final
name|SingleListChangeInfo
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|display (final SingleListChangeInfo result)
specifier|private
name|void
name|display
parameter_list|(
specifier|final
name|SingleListChangeInfo
name|result
parameter_list|)
block|{
name|table
operator|.
name|setAccountInfoCache
argument_list|(
name|result
operator|.
name|getAccounts
argument_list|()
argument_list|)
expr_stmt|;
name|drafts
operator|.
name|display
argument_list|(
name|result
operator|.
name|getChanges
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|finishDisplay
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

