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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|Scheduler
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
name|core
operator|.
name|client
operator|.
name|Scheduler
operator|.
name|ScheduledCommand
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
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|LineWidget
import|;
end_import

begin_comment
comment|/** An HtmlPanel for displaying a comment */
end_comment

begin_class
DECL|class|CommentBox
specifier|abstract
class|class
name|CommentBox
extends|extends
name|Composite
block|{
static|static
block|{
name|Resources
operator|.
name|I
operator|.
name|style
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
block|}
DECL|field|widgetManager
specifier|private
name|PaddingManager
name|widgetManager
decl_stmt|;
DECL|field|selfWidget
specifier|private
name|LineWidget
name|selfWidget
decl_stmt|;
DECL|method|resizePaddingWidget ()
name|void
name|resizePaddingWidget
parameter_list|()
block|{
name|Scheduler
operator|.
name|get
argument_list|()
operator|.
name|scheduleDeferred
argument_list|(
operator|new
name|ScheduledCommand
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
assert|assert
name|selfWidget
operator|!=
literal|null
assert|;
assert|assert
name|widgetManager
operator|!=
literal|null
assert|;
name|selfWidget
operator|.
name|changed
argument_list|()
expr_stmt|;
name|widgetManager
operator|.
name|resizePaddingWidget
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getCommentInfo ()
specifier|abstract
name|CommentInfo
name|getCommentInfo
parameter_list|()
function_decl|;
DECL|method|isOpen ()
specifier|abstract
name|boolean
name|isOpen
parameter_list|()
function_decl|;
DECL|method|setOpen (boolean open)
name|void
name|setOpen
parameter_list|(
name|boolean
name|open
parameter_list|)
block|{
name|resizePaddingWidget
argument_list|()
expr_stmt|;
block|}
DECL|method|getPaddingManager ()
name|PaddingManager
name|getPaddingManager
parameter_list|()
block|{
return|return
name|widgetManager
return|;
block|}
DECL|method|setPaddingManager (PaddingManager manager)
name|void
name|setPaddingManager
parameter_list|(
name|PaddingManager
name|manager
parameter_list|)
block|{
name|widgetManager
operator|=
name|manager
expr_stmt|;
block|}
DECL|method|setSelfWidget (LineWidget widget)
name|void
name|setSelfWidget
parameter_list|(
name|LineWidget
name|widget
parameter_list|)
block|{
name|selfWidget
operator|=
name|widget
expr_stmt|;
block|}
DECL|method|getSelfWidget ()
name|LineWidget
name|getSelfWidget
parameter_list|()
block|{
return|return
name|selfWidget
return|;
block|}
block|}
end_class

end_unit

