begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
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
name|CommentInfo
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
name|changes
operator|.
name|Side
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
name|FlowPanel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * HTMLPanel to hold file comments.  * TODO: Need to resize CodeMirror if this is resized since we don't have the  * system scrollbar.  */
end_comment

begin_class
DECL|class|FileCommentPanel
class|class
name|FileCommentPanel
extends|extends
name|Composite
block|{
DECL|field|parent
specifier|private
name|SideBySide2
name|parent
decl_stmt|;
DECL|field|table
specifier|private
name|DiffTable
name|table
decl_stmt|;
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|field|side
specifier|private
name|Side
name|side
decl_stmt|;
DECL|field|boxes
specifier|private
name|List
argument_list|<
name|CommentBox
argument_list|>
name|boxes
decl_stmt|;
DECL|field|body
specifier|private
name|FlowPanel
name|body
decl_stmt|;
DECL|method|FileCommentPanel (SideBySide2 host, DiffTable table, String path, Side side)
name|FileCommentPanel
parameter_list|(
name|SideBySide2
name|host
parameter_list|,
name|DiffTable
name|table
parameter_list|,
name|String
name|path
parameter_list|,
name|Side
name|side
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|side
operator|=
name|side
expr_stmt|;
name|boxes
operator|=
operator|new
name|ArrayList
argument_list|<
name|CommentBox
argument_list|>
argument_list|()
expr_stmt|;
name|initWidget
argument_list|(
name|body
operator|=
operator|new
name|FlowPanel
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createOrEditFileComment ()
name|void
name|createOrEditFileComment
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|doSignIn
argument_list|(
name|parent
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|boxes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|CommentInfo
name|info
init|=
name|CommentInfo
operator|.
name|createFile
argument_list|(
name|path
argument_list|,
name|side
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|addFileComment
argument_list|(
name|parent
operator|.
name|addDraftBox
argument_list|(
name|info
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CommentBox
name|box
init|=
name|boxes
operator|.
name|get
argument_list|(
name|boxes
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|box
operator|instanceof
name|DraftBox
condition|)
block|{
operator|(
operator|(
name|DraftBox
operator|)
name|box
operator|)
operator|.
name|setEdit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addFileComment
argument_list|(
operator|(
operator|(
name|PublishedBox
operator|)
name|box
operator|)
operator|.
name|addReplyBox
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getBoxCount ()
name|int
name|getBoxCount
parameter_list|()
block|{
return|return
name|boxes
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|addFileComment (CommentBox box)
name|void
name|addFileComment
parameter_list|(
name|CommentBox
name|box
parameter_list|)
block|{
name|boxes
operator|.
name|add
argument_list|(
name|box
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|box
argument_list|)
expr_stmt|;
name|table
operator|.
name|updateFileCommentVisibility
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|onRemoveDraftBox (DraftBox box)
name|void
name|onRemoveDraftBox
parameter_list|(
name|DraftBox
name|box
parameter_list|)
block|{
name|boxes
operator|.
name|remove
argument_list|(
name|box
argument_list|)
expr_stmt|;
name|table
operator|.
name|updateFileCommentVisibility
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

