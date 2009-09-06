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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|Command
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
name|Panel
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

begin_comment
comment|/** Expands all {@link CommentPanel} in a parent panel. */
end_comment

begin_class
DECL|class|ExpandAllCommand
specifier|public
class|class
name|ExpandAllCommand
implements|implements
name|Command
block|{
DECL|field|panel
specifier|private
specifier|final
name|Panel
name|panel
decl_stmt|;
DECL|field|open
specifier|protected
specifier|final
name|boolean
name|open
decl_stmt|;
DECL|method|ExpandAllCommand (final Panel p, final boolean isOpen)
specifier|public
name|ExpandAllCommand
parameter_list|(
specifier|final
name|Panel
name|p
parameter_list|,
specifier|final
name|boolean
name|isOpen
parameter_list|)
block|{
name|panel
operator|=
name|p
expr_stmt|;
name|open
operator|=
name|isOpen
expr_stmt|;
block|}
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
for|for
control|(
specifier|final
name|Widget
name|w
range|:
name|panel
control|)
block|{
if|if
condition|(
name|w
operator|instanceof
name|CommentPanel
condition|)
block|{
name|expand
argument_list|(
operator|(
name|CommentPanel
operator|)
name|w
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|expand (final CommentPanel w)
specifier|protected
name|void
name|expand
parameter_list|(
specifier|final
name|CommentPanel
name|w
parameter_list|)
block|{
name|w
operator|.
name|setOpen
argument_list|(
name|open
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

