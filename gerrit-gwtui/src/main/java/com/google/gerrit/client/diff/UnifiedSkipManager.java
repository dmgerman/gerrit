begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|patches
operator|.
name|SkippedLine
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Collapses common regions with {@link UnifiedSkipBar} for {@link Unified}. */
end_comment

begin_class
DECL|class|UnifiedSkipManager
class|class
name|UnifiedSkipManager
extends|extends
name|SkipManager
block|{
DECL|field|host
specifier|private
name|Unified
name|host
decl_stmt|;
DECL|method|UnifiedSkipManager (Unified host, UnifiedCommentManager commentManager)
name|UnifiedSkipManager
parameter_list|(
name|Unified
name|host
parameter_list|,
name|UnifiedCommentManager
name|commentManager
parameter_list|)
block|{
name|super
argument_list|(
name|commentManager
argument_list|)
expr_stmt|;
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|render (int context, DiffInfo diff)
name|void
name|render
parameter_list|(
name|int
name|context
parameter_list|,
name|DiffInfo
name|diff
parameter_list|)
block|{
name|List
argument_list|<
name|SkippedLine
argument_list|>
name|skips
init|=
name|getSkippedLines
argument_list|(
name|context
argument_list|,
name|diff
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|skips
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|CodeMirror
name|cm
init|=
name|host
operator|.
name|getCm
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|SkipBar
argument_list|>
name|skipBars
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|setSkipBars
argument_list|(
name|skipBars
argument_list|)
expr_stmt|;
for|for
control|(
name|SkippedLine
name|skip
range|:
name|skips
control|)
block|{
name|UnifiedSkipBar
name|bar
init|=
name|newSkipBar
argument_list|(
name|cm
argument_list|,
name|skip
argument_list|)
decl_stmt|;
name|skipBars
operator|.
name|add
argument_list|(
name|bar
argument_list|)
expr_stmt|;
if|if
condition|(
name|skip
operator|.
name|getStartA
argument_list|()
operator|==
literal|0
operator|||
name|skip
operator|.
name|getStartB
argument_list|()
operator|==
literal|0
condition|)
block|{
name|bar
operator|.
name|upArrow
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setLine0
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|skip
operator|.
name|getStartA
argument_list|()
operator|+
name|skip
operator|.
name|getSize
argument_list|()
operator|==
name|getLineA
argument_list|()
operator|||
name|skip
operator|.
name|getStartB
argument_list|()
operator|+
name|skip
operator|.
name|getSize
argument_list|()
operator|==
name|getLineB
argument_list|()
condition|)
block|{
name|bar
operator|.
name|downArrow
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|remove (UnifiedSkipBar bar)
name|void
name|remove
parameter_list|(
name|UnifiedSkipBar
name|bar
parameter_list|)
block|{
name|Set
argument_list|<
name|SkipBar
argument_list|>
name|skipBars
init|=
name|getSkipBars
argument_list|()
decl_stmt|;
name|skipBars
operator|.
name|remove
argument_list|(
name|bar
argument_list|)
expr_stmt|;
if|if
condition|(
name|getLine0
argument_list|()
operator|==
name|bar
condition|)
block|{
name|setLine0
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipBars
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setSkipBars
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|newSkipBar (CodeMirror cm, SkippedLine skip)
specifier|private
name|UnifiedSkipBar
name|newSkipBar
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|SkippedLine
name|skip
parameter_list|)
block|{
name|int
name|start
init|=
name|host
operator|.
name|getCmLine
argument_list|(
name|skip
operator|.
name|getStartA
argument_list|()
argument_list|,
name|DisplaySide
operator|.
name|A
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|start
operator|+
name|skip
operator|.
name|getSize
argument_list|()
operator|-
literal|1
decl_stmt|;
name|UnifiedSkipBar
name|bar
init|=
operator|new
name|UnifiedSkipBar
argument_list|(
name|this
argument_list|,
name|cm
argument_list|)
decl_stmt|;
name|host
operator|.
name|getDiffTable
argument_list|()
operator|.
name|add
argument_list|(
name|bar
argument_list|)
expr_stmt|;
name|bar
operator|.
name|collapse
argument_list|(
name|start
argument_list|,
name|end
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|bar
return|;
block|}
block|}
end_class

end_unit

