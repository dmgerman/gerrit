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
name|diff
operator|.
name|DiffInfo
operator|.
name|Region
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
name|patches
operator|.
name|SkippedLine
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
name|AccountDiffPreference
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
name|JsArray
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
name|ArrayList
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
comment|/** Collapses common regions with {@link SkipBar} for {@link SideBySide2}. */
end_comment

begin_class
DECL|class|SkipManager
class|class
name|SkipManager
block|{
DECL|field|host
specifier|private
specifier|final
name|SideBySide2
name|host
decl_stmt|;
DECL|field|commentManager
specifier|private
specifier|final
name|CommentManager
name|commentManager
decl_stmt|;
DECL|field|skipBars
specifier|private
name|Set
argument_list|<
name|SkipBar
argument_list|>
name|skipBars
decl_stmt|;
DECL|method|SkipManager (SideBySide2 host, CommentManager commentManager)
name|SkipManager
parameter_list|(
name|SideBySide2
name|host
parameter_list|,
name|CommentManager
name|commentManager
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|commentManager
operator|=
name|commentManager
expr_stmt|;
block|}
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
if|if
condition|(
name|context
operator|==
name|AccountDiffPreference
operator|.
name|WHOLE_FILE_CONTEXT
condition|)
block|{
return|return;
block|}
name|JsArray
argument_list|<
name|Region
argument_list|>
name|regions
init|=
name|diff
operator|.
name|content
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|SkippedLine
argument_list|>
name|skips
init|=
operator|new
name|ArrayList
argument_list|<
name|SkippedLine
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|lineA
init|=
literal|0
decl_stmt|,
name|lineB
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|regions
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Region
name|current
init|=
name|regions
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|.
name|ab
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|len
init|=
name|current
operator|.
name|ab
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
name|len
operator|>
name|context
operator|+
literal|1
condition|)
block|{
name|skips
operator|.
name|add
argument_list|(
operator|new
name|SkippedLine
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|len
operator|-
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|==
name|regions
operator|.
name|length
argument_list|()
operator|-
literal|1
operator|&&
name|len
operator|>
name|context
operator|+
literal|1
condition|)
block|{
name|skips
operator|.
name|add
argument_list|(
operator|new
name|SkippedLine
argument_list|(
name|lineA
operator|+
name|context
argument_list|,
name|lineB
operator|+
name|context
argument_list|,
name|len
operator|-
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|len
operator|>
literal|2
operator|*
name|context
operator|+
literal|1
condition|)
block|{
name|skips
operator|.
name|add
argument_list|(
operator|new
name|SkippedLine
argument_list|(
name|lineA
operator|+
name|context
argument_list|,
name|lineB
operator|+
name|context
argument_list|,
name|len
operator|-
literal|2
operator|*
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|lineA
operator|+=
name|len
expr_stmt|;
name|lineB
operator|+=
name|len
expr_stmt|;
block|}
else|else
block|{
name|lineA
operator|+=
name|current
operator|.
name|a
argument_list|()
operator|!=
literal|null
condition|?
name|current
operator|.
name|a
argument_list|()
operator|.
name|length
argument_list|()
else|:
literal|0
expr_stmt|;
name|lineB
operator|+=
name|current
operator|.
name|b
argument_list|()
operator|!=
literal|null
condition|?
name|current
operator|.
name|b
argument_list|()
operator|.
name|length
argument_list|()
else|:
literal|0
expr_stmt|;
block|}
block|}
name|skips
operator|=
name|commentManager
operator|.
name|splitSkips
argument_list|(
name|context
argument_list|,
name|skips
argument_list|)
expr_stmt|;
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
name|cmA
init|=
name|host
operator|.
name|getCmFromSide
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|)
decl_stmt|;
name|CodeMirror
name|cmB
init|=
name|host
operator|.
name|getCmFromSide
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|)
decl_stmt|;
name|skipBars
operator|=
operator|new
name|HashSet
argument_list|<
name|SkipBar
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|SkippedLine
name|skip
range|:
name|skips
control|)
block|{
name|SkipBar
name|barA
init|=
name|newSkipBar
argument_list|(
name|cmA
argument_list|,
name|DisplaySide
operator|.
name|A
argument_list|,
name|skip
argument_list|)
decl_stmt|;
name|SkipBar
name|barB
init|=
name|newSkipBar
argument_list|(
name|cmB
argument_list|,
name|DisplaySide
operator|.
name|B
argument_list|,
name|skip
argument_list|)
decl_stmt|;
name|SkipBar
operator|.
name|link
argument_list|(
name|barA
argument_list|,
name|barB
argument_list|)
expr_stmt|;
name|skipBars
operator|.
name|add
argument_list|(
name|barA
argument_list|)
expr_stmt|;
name|skipBars
operator|.
name|add
argument_list|(
name|barB
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
name|barA
operator|.
name|upArrow
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|barB
operator|.
name|upArrow
operator|.
name|setVisible
argument_list|(
literal|false
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
name|lineA
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
name|lineB
condition|)
block|{
name|barA
operator|.
name|downArrow
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|barB
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
DECL|method|removeAll ()
name|void
name|removeAll
parameter_list|()
block|{
if|if
condition|(
name|skipBars
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SkipBar
name|bar
range|:
name|skipBars
control|)
block|{
name|bar
operator|.
name|expandAll
argument_list|()
expr_stmt|;
block|}
name|skipBars
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|remove (SkipBar a, SkipBar b)
name|void
name|remove
parameter_list|(
name|SkipBar
name|a
parameter_list|,
name|SkipBar
name|b
parameter_list|)
block|{
name|skipBars
operator|.
name|remove
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|skipBars
operator|.
name|remove
argument_list|(
name|b
argument_list|)
expr_stmt|;
if|if
condition|(
name|skipBars
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|skipBars
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|newSkipBar (CodeMirror cm, DisplaySide side, SkippedLine skip)
specifier|private
name|SkipBar
name|newSkipBar
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|DisplaySide
name|side
parameter_list|,
name|SkippedLine
name|skip
parameter_list|)
block|{
name|int
name|start
init|=
name|side
operator|==
name|DisplaySide
operator|.
name|A
condition|?
name|skip
operator|.
name|getStartA
argument_list|()
else|:
name|skip
operator|.
name|getStartB
argument_list|()
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
name|SkipBar
name|bar
init|=
operator|new
name|SkipBar
argument_list|(
name|this
argument_list|,
name|cm
argument_list|)
decl_stmt|;
name|host
operator|.
name|diffTable
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

