begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
package|;
end_package

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
name|Timer
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|CodeMirror
import|;
end_import

begin_comment
comment|/**  * LineWidget attached to a CodeMirror container.  *  * When a comment is placed on a line a CommentWidget is created.  * The group tracks all comment boxes on a line in unified diff view.  */
end_comment

begin_class
DECL|class|UnifiedCommentGroup
class|class
name|UnifiedCommentGroup
extends|extends
name|CommentGroup
block|{
DECL|method|UnifiedCommentGroup (UnifiedCommentManager manager, CodeMirror cm, DisplaySide side, int line)
name|UnifiedCommentGroup
parameter_list|(
name|UnifiedCommentManager
name|manager
parameter_list|,
name|CodeMirror
name|cm
parameter_list|,
name|DisplaySide
name|side
parameter_list|,
name|int
name|line
parameter_list|)
block|{
name|super
argument_list|(
name|manager
argument_list|,
name|cm
argument_list|,
name|side
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (DraftBox box)
name|void
name|remove
parameter_list|(
name|DraftBox
name|box
parameter_list|)
block|{
name|super
operator|.
name|remove
argument_list|(
name|box
argument_list|)
expr_stmt|;
if|if
condition|(
literal|0
operator|<
name|getBoxCount
argument_list|()
condition|)
block|{
name|resize
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|detach
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|init (DiffTable parent)
name|void
name|init
parameter_list|(
name|DiffTable
name|parent
parameter_list|)
block|{
if|if
condition|(
name|getLineWidget
argument_list|()
operator|==
literal|null
condition|)
block|{
name|attach
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|handleRedraw ()
name|void
name|handleRedraw
parameter_list|()
block|{
name|getLineWidget
argument_list|()
operator|.
name|onRedraw
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|canComputeHeight
argument_list|()
condition|)
block|{
if|if
condition|(
name|getResizeTimer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getResizeTimer
argument_list|()
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|setResizeTimer
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|reportHeightChange
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getResizeTimer
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setResizeTimer
argument_list|(
operator|new
name|Timer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|canComputeHeight
argument_list|()
condition|)
block|{
name|cancel
argument_list|()
expr_stmt|;
name|setResizeTimer
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|reportHeightChange
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|getResizeTimer
argument_list|()
operator|.
name|scheduleRepeating
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resize ()
name|void
name|resize
parameter_list|()
block|{
if|if
condition|(
name|getLineWidget
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reportHeightChange
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|reportHeightChange ()
specifier|private
name|void
name|reportHeightChange
parameter_list|()
block|{
name|getLineWidget
argument_list|()
operator|.
name|changed
argument_list|()
expr_stmt|;
name|updateSelection
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

