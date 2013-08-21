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

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|ScrollInfo
import|;
end_import

begin_class
DECL|class|ScrollSynchronizer
class|class
name|ScrollSynchronizer
block|{
DECL|field|diffTable
specifier|private
name|SideBySideTable
name|diffTable
decl_stmt|;
DECL|field|mapper
specifier|private
name|LineMapper
name|mapper
decl_stmt|;
DECL|field|active
specifier|private
name|ScrollCallback
name|active
decl_stmt|;
DECL|field|callbackA
specifier|private
name|ScrollCallback
name|callbackA
decl_stmt|;
DECL|field|callbackB
specifier|private
name|ScrollCallback
name|callbackB
decl_stmt|;
DECL|field|cmB
specifier|private
name|CodeMirror
name|cmB
decl_stmt|;
DECL|field|autoHideDiffTableHeader
specifier|private
name|boolean
name|autoHideDiffTableHeader
decl_stmt|;
DECL|method|ScrollSynchronizer (SideBySideTable diffTable, CodeMirror cmA, CodeMirror cmB, LineMapper mapper)
name|ScrollSynchronizer
parameter_list|(
name|SideBySideTable
name|diffTable
parameter_list|,
name|CodeMirror
name|cmA
parameter_list|,
name|CodeMirror
name|cmB
parameter_list|,
name|LineMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|diffTable
operator|=
name|diffTable
expr_stmt|;
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
name|this
operator|.
name|cmB
operator|=
name|cmB
expr_stmt|;
name|callbackA
operator|=
operator|new
name|ScrollCallback
argument_list|(
name|cmA
argument_list|,
name|cmB
argument_list|,
name|DisplaySide
operator|.
name|A
argument_list|)
expr_stmt|;
name|callbackB
operator|=
operator|new
name|ScrollCallback
argument_list|(
name|cmB
argument_list|,
name|cmA
argument_list|,
name|DisplaySide
operator|.
name|B
argument_list|)
expr_stmt|;
name|cmA
operator|.
name|on
argument_list|(
literal|"scroll"
argument_list|,
name|callbackA
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|on
argument_list|(
literal|"scroll"
argument_list|,
name|callbackB
argument_list|)
expr_stmt|;
block|}
DECL|method|setAutoHideDiffTableHeader (boolean autoHide)
name|void
name|setAutoHideDiffTableHeader
parameter_list|(
name|boolean
name|autoHide
parameter_list|)
block|{
if|if
condition|(
name|autoHide
condition|)
block|{
name|updateDiffTableHeader
argument_list|(
name|cmB
operator|.
name|getScrollInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|diffTable
operator|.
name|setHeaderVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|autoHideDiffTableHeader
operator|=
name|autoHide
expr_stmt|;
block|}
DECL|method|syncScroll (DisplaySide masterSide)
name|void
name|syncScroll
parameter_list|(
name|DisplaySide
name|masterSide
parameter_list|)
block|{
operator|(
name|masterSide
operator|==
name|DisplaySide
operator|.
name|A
condition|?
name|callbackA
else|:
name|callbackB
operator|)
operator|.
name|sync
argument_list|()
expr_stmt|;
block|}
DECL|method|updateDiffTableHeader (ScrollInfo si)
specifier|private
name|void
name|updateDiffTableHeader
parameter_list|(
name|ScrollInfo
name|si
parameter_list|)
block|{
if|if
condition|(
name|si
operator|.
name|top
argument_list|()
operator|==
literal|0
condition|)
block|{
name|diffTable
operator|.
name|setHeaderVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|si
operator|.
name|top
argument_list|()
operator|>
literal|0.5
operator|*
name|si
operator|.
name|clientHeight
argument_list|()
condition|)
block|{
name|diffTable
operator|.
name|setHeaderVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ScrollCallback
class|class
name|ScrollCallback
implements|implements
name|Runnable
block|{
DECL|field|src
specifier|private
specifier|final
name|CodeMirror
name|src
decl_stmt|;
DECL|field|dst
specifier|private
specifier|final
name|CodeMirror
name|dst
decl_stmt|;
DECL|field|srcSide
specifier|private
specifier|final
name|DisplaySide
name|srcSide
decl_stmt|;
DECL|field|fixup
specifier|private
specifier|final
name|Timer
name|fixup
decl_stmt|;
DECL|field|state
specifier|private
name|int
name|state
decl_stmt|;
DECL|method|ScrollCallback (CodeMirror src, CodeMirror dst, DisplaySide srcSide)
name|ScrollCallback
parameter_list|(
name|CodeMirror
name|src
parameter_list|,
name|CodeMirror
name|dst
parameter_list|,
name|DisplaySide
name|srcSide
parameter_list|)
block|{
name|this
operator|.
name|src
operator|=
name|src
expr_stmt|;
name|this
operator|.
name|dst
operator|=
name|dst
expr_stmt|;
name|this
operator|.
name|srcSide
operator|=
name|srcSide
expr_stmt|;
name|this
operator|.
name|fixup
operator|=
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
name|active
operator|==
name|ScrollCallback
operator|.
name|this
condition|)
block|{
name|fixup
argument_list|()
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
DECL|method|sync ()
name|void
name|sync
parameter_list|()
block|{
name|dst
operator|.
name|scrollToY
argument_list|(
name|align
argument_list|(
name|src
operator|.
name|getScrollInfo
argument_list|()
operator|.
name|top
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|active
operator|==
literal|null
condition|)
block|{
name|active
operator|=
name|this
expr_stmt|;
name|fixup
operator|.
name|scheduleRepeating
argument_list|(
literal|20
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|active
operator|==
name|this
condition|)
block|{
name|ScrollInfo
name|si
init|=
name|src
operator|.
name|getScrollInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|autoHideDiffTableHeader
condition|)
block|{
name|updateDiffTableHeader
argument_list|(
name|si
argument_list|)
expr_stmt|;
block|}
name|dst
operator|.
name|scrollTo
argument_list|(
name|si
operator|.
name|left
argument_list|()
argument_list|,
name|align
argument_list|(
name|si
operator|.
name|top
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|=
literal|0
expr_stmt|;
block|}
block|}
DECL|method|fixup ()
specifier|private
name|void
name|fixup
parameter_list|()
block|{
switch|switch
condition|(
name|state
condition|)
block|{
case|case
literal|0
case|:
name|state
operator|=
literal|1
expr_stmt|;
name|dst
operator|.
name|scrollToY
argument_list|(
name|align
argument_list|(
name|src
operator|.
name|getScrollInfo
argument_list|()
operator|.
name|top
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|state
operator|=
literal|2
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|active
operator|=
literal|null
expr_stmt|;
name|fixup
operator|.
name|cancel
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
DECL|method|align (double srcTop)
specifier|private
name|double
name|align
parameter_list|(
name|double
name|srcTop
parameter_list|)
block|{
comment|// Since CM doesn't always take the height of line widgets into
comment|// account when calculating scrollInfo when scrolling too fast (e.g.
comment|// throw scrolling), simply setting scrollTop to be the same doesn't
comment|// guarantee alignment.
name|int
name|line
init|=
name|src
operator|.
name|lineAtHeight
argument_list|(
name|srcTop
argument_list|,
literal|"local"
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|0
condition|)
block|{
comment|// Padding for insert at start of file occurs above line 0,
comment|// and CM3 doesn't always compute heightAtLine correctly.
return|return
name|srcTop
return|;
block|}
comment|// Find a pair of lines that are aligned and near the top of
comment|// the viewport. Use that distance to correct the Y coordinate.
name|LineMapper
operator|.
name|AlignedPair
name|p
init|=
name|mapper
operator|.
name|align
argument_list|(
name|srcSide
argument_list|,
name|line
argument_list|)
decl_stmt|;
name|double
name|sy
init|=
name|src
operator|.
name|heightAtLine
argument_list|(
name|p
operator|.
name|src
argument_list|,
literal|"local"
argument_list|)
decl_stmt|;
name|double
name|dy
init|=
name|dst
operator|.
name|heightAtLine
argument_list|(
name|p
operator|.
name|dst
argument_list|,
literal|"local"
argument_list|)
decl_stmt|;
return|return
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|dy
operator|+
operator|(
name|srcTop
operator|-
name|sy
operator|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

