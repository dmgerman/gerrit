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
name|common
operator|.
name|data
operator|.
name|AccountInfoCache
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetInfo
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Composite
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
name|HorizontalPanel
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
name|globalkey
operator|.
name|client
operator|.
name|KeyCommandSet
import|;
end_import

begin_class
DECL|class|ChangeDescriptionBlock
specifier|public
class|class
name|ChangeDescriptionBlock
extends|extends
name|Composite
block|{
DECL|field|infoBlock
specifier|private
specifier|final
name|ChangeInfoBlock
name|infoBlock
decl_stmt|;
DECL|field|messageBlock
specifier|private
specifier|final
name|CommitMessageBlock
name|messageBlock
decl_stmt|;
DECL|method|ChangeDescriptionBlock (KeyCommandSet keysAction)
specifier|public
name|ChangeDescriptionBlock
parameter_list|(
name|KeyCommandSet
name|keysAction
parameter_list|)
block|{
name|infoBlock
operator|=
operator|new
name|ChangeInfoBlock
argument_list|()
expr_stmt|;
name|messageBlock
operator|=
operator|new
name|CommitMessageBlock
argument_list|(
name|keysAction
argument_list|)
expr_stmt|;
specifier|final
name|HorizontalPanel
name|hp
init|=
operator|new
name|HorizontalPanel
argument_list|()
decl_stmt|;
name|hp
operator|.
name|add
argument_list|(
name|infoBlock
argument_list|)
expr_stmt|;
name|hp
operator|.
name|add
argument_list|(
name|messageBlock
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|hp
argument_list|)
expr_stmt|;
block|}
DECL|method|display (Change chg, Boolean starred, PatchSetInfo info, final AccountInfoCache acc, Project.SubmitType submitType)
specifier|public
name|void
name|display
parameter_list|(
name|Change
name|chg
parameter_list|,
name|Boolean
name|starred
parameter_list|,
name|PatchSetInfo
name|info
parameter_list|,
specifier|final
name|AccountInfoCache
name|acc
parameter_list|,
name|Project
operator|.
name|SubmitType
name|submitType
parameter_list|)
block|{
name|infoBlock
operator|.
name|display
argument_list|(
name|chg
argument_list|,
name|acc
argument_list|,
name|submitType
argument_list|)
expr_stmt|;
name|messageBlock
operator|.
name|display
argument_list|(
name|chg
operator|.
name|getId
argument_list|()
argument_list|,
name|starred
argument_list|,
name|info
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

