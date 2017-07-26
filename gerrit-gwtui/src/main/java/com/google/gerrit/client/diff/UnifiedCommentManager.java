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
name|gerrit
operator|.
name|client
operator|.
name|DiffObject
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
name|client
operator|.
name|diff
operator|.
name|LineMapper
operator|.
name|LineOnOtherInfo
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
name|diff
operator|.
name|UnifiedChunkManager
operator|.
name|LineRegionInfo
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
name|diff
operator|.
name|UnifiedChunkManager
operator|.
name|RegionType
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
name|CommentLinkProcessor
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
name|Nullable
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
name|PatchSet
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|Pos
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
name|TextMarker
operator|.
name|FromTo
import|;
end_import

begin_comment
comment|/** Tracks comment widgets for {@link Unified}. */
end_comment

begin_class
DECL|class|UnifiedCommentManager
class|class
name|UnifiedCommentManager
extends|extends
name|CommentManager
block|{
DECL|field|mergedMap
specifier|private
specifier|final
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|CommentGroup
argument_list|>
name|mergedMap
decl_stmt|;
comment|// In Unified, a CodeMirror line can have up to two CommentGroups - one for
comment|// the base side and one for the revision, so we need to keep track of the
comment|// duplicates and replace the entries in mergedMap on draft removal.
DECL|field|duplicates
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|CommentGroup
argument_list|>
name|duplicates
decl_stmt|;
DECL|method|UnifiedCommentManager ( Unified host, @Nullable Project.NameKey project, DiffObject base, PatchSet.Id revision, String path, CommentLinkProcessor clp, boolean open)
name|UnifiedCommentManager
parameter_list|(
name|Unified
name|host
parameter_list|,
annotation|@
name|Nullable
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|DiffObject
name|base
parameter_list|,
name|PatchSet
operator|.
name|Id
name|revision
parameter_list|,
name|String
name|path
parameter_list|,
name|CommentLinkProcessor
name|clp
parameter_list|,
name|boolean
name|open
parameter_list|)
block|{
name|super
argument_list|(
name|host
argument_list|,
name|project
argument_list|,
name|base
argument_list|,
name|revision
argument_list|,
name|path
argument_list|,
name|clp
argument_list|,
name|open
argument_list|)
expr_stmt|;
name|mergedMap
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
expr_stmt|;
name|duplicates
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMapForNav (DisplaySide side)
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|CommentGroup
argument_list|>
name|getMapForNav
parameter_list|(
name|DisplaySide
name|side
parameter_list|)
block|{
return|return
name|mergedMap
return|;
block|}
annotation|@
name|Override
DECL|method|clearLine (DisplaySide side, int line, CommentGroup group)
name|void
name|clearLine
parameter_list|(
name|DisplaySide
name|side
parameter_list|,
name|int
name|line
parameter_list|,
name|CommentGroup
name|group
parameter_list|)
block|{
name|super
operator|.
name|clearLine
argument_list|(
name|side
argument_list|,
name|line
argument_list|,
name|group
argument_list|)
expr_stmt|;
if|if
condition|(
name|mergedMap
operator|.
name|get
argument_list|(
name|line
argument_list|)
operator|==
name|group
condition|)
block|{
name|mergedMap
operator|.
name|remove
argument_list|(
name|line
argument_list|)
expr_stmt|;
if|if
condition|(
name|duplicates
operator|.
name|containsKey
argument_list|(
name|line
argument_list|)
condition|)
block|{
name|mergedMap
operator|.
name|put
argument_list|(
name|line
argument_list|,
name|duplicates
operator|.
name|remove
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|newDraftOnGutterClick (CodeMirror cm, String gutterClass, int cmLinePlusOne)
name|void
name|newDraftOnGutterClick
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|String
name|gutterClass
parameter_list|,
name|int
name|cmLinePlusOne
parameter_list|)
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
name|signInCallback
argument_list|(
name|cm
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LineRegionInfo
name|info
init|=
operator|(
operator|(
name|Unified
operator|)
name|host
operator|)
operator|.
name|getLineRegionInfoFromCmLine
argument_list|(
name|cmLinePlusOne
operator|-
literal|1
argument_list|)
decl_stmt|;
name|DisplaySide
name|side
init|=
name|gutterClass
operator|.
name|equals
argument_list|(
name|UnifiedTable
operator|.
name|style
operator|.
name|lineNumbersLeft
argument_list|()
argument_list|)
condition|?
name|DisplaySide
operator|.
name|A
else|:
name|DisplaySide
operator|.
name|B
decl_stmt|;
name|int
name|line
init|=
name|info
operator|.
name|line
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getSide
argument_list|()
operator|!=
name|side
condition|)
block|{
name|line
operator|=
name|host
operator|.
name|lineOnOther
argument_list|(
name|info
operator|.
name|getSide
argument_list|()
argument_list|,
name|line
argument_list|)
operator|.
name|getLine
argument_list|()
expr_stmt|;
block|}
name|insertNewDraft
argument_list|(
name|side
argument_list|,
name|line
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getCommentGroupOnActiveLine (CodeMirror cm)
name|CommentGroup
name|getCommentGroupOnActiveLine
parameter_list|(
name|CodeMirror
name|cm
parameter_list|)
block|{
name|CommentGroup
name|group
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cm
operator|.
name|extras
argument_list|()
operator|.
name|hasActiveLine
argument_list|()
condition|)
block|{
name|int
name|cmLinePlusOne
init|=
name|cm
operator|.
name|getLineNumber
argument_list|(
name|cm
operator|.
name|extras
argument_list|()
operator|.
name|activeLine
argument_list|()
argument_list|)
operator|+
literal|1
decl_stmt|;
name|LineRegionInfo
name|info
init|=
operator|(
operator|(
name|Unified
operator|)
name|host
operator|)
operator|.
name|getLineRegionInfoFromCmLine
argument_list|(
name|cmLinePlusOne
operator|-
literal|1
argument_list|)
decl_stmt|;
name|CommentGroup
name|forSide
init|=
name|map
argument_list|(
name|info
operator|.
name|getSide
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
name|cmLinePlusOne
argument_list|)
decl_stmt|;
name|group
operator|=
name|forSide
operator|==
literal|null
condition|?
name|map
argument_list|(
name|info
operator|.
name|getSide
argument_list|()
operator|.
name|otherSide
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
name|cmLinePlusOne
argument_list|)
else|:
name|forSide
expr_stmt|;
block|}
return|return
name|group
return|;
block|}
annotation|@
name|Override
DECL|method|getLinesWithCommentGroups ()
name|Collection
argument_list|<
name|Integer
argument_list|>
name|getLinesWithCommentGroups
parameter_list|()
block|{
return|return
name|mergedMap
operator|.
name|tailMap
argument_list|(
literal|1
argument_list|)
operator|.
name|keySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTokenSuffixForActiveLine (CodeMirror cm)
name|String
name|getTokenSuffixForActiveLine
parameter_list|(
name|CodeMirror
name|cm
parameter_list|)
block|{
name|int
name|cmLinePlusOne
init|=
name|cm
operator|.
name|getLineNumber
argument_list|(
name|cm
operator|.
name|extras
argument_list|()
operator|.
name|activeLine
argument_list|()
argument_list|)
operator|+
literal|1
decl_stmt|;
name|LineRegionInfo
name|info
init|=
operator|(
operator|(
name|Unified
operator|)
name|host
operator|)
operator|.
name|getLineRegionInfoFromCmLine
argument_list|(
name|cmLinePlusOne
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
operator|(
name|info
operator|.
name|getSide
argument_list|()
operator|==
name|DisplaySide
operator|.
name|A
condition|?
literal|"a"
else|:
literal|""
operator|)
operator|+
name|cmLinePlusOne
return|;
block|}
annotation|@
name|Override
DECL|method|newDraft (CodeMirror cm)
name|void
name|newDraft
parameter_list|(
name|CodeMirror
name|cm
parameter_list|)
block|{
if|if
condition|(
name|cm
operator|.
name|somethingSelected
argument_list|()
condition|)
block|{
name|FromTo
name|fromTo
init|=
name|adjustSelection
argument_list|(
name|cm
argument_list|)
decl_stmt|;
name|Pos
name|from
init|=
name|fromTo
operator|.
name|from
argument_list|()
decl_stmt|;
name|Pos
name|to
init|=
name|fromTo
operator|.
name|to
argument_list|()
decl_stmt|;
name|Unified
name|unified
init|=
operator|(
name|Unified
operator|)
name|host
decl_stmt|;
name|UnifiedChunkManager
name|manager
init|=
name|unified
operator|.
name|getChunkManager
argument_list|()
decl_stmt|;
name|LineRegionInfo
name|fromInfo
init|=
name|unified
operator|.
name|getLineRegionInfoFromCmLine
argument_list|(
name|from
operator|.
name|line
argument_list|()
argument_list|)
decl_stmt|;
name|LineRegionInfo
name|toInfo
init|=
name|unified
operator|.
name|getLineRegionInfoFromCmLine
argument_list|(
name|to
operator|.
name|line
argument_list|()
argument_list|)
decl_stmt|;
name|DisplaySide
name|side
init|=
name|toInfo
operator|.
name|getSide
argument_list|()
decl_stmt|;
comment|// Handle special cases in selections that span multiple regions. Force
comment|// start line to be on the same side as the end line.
if|if
condition|(
operator|(
name|fromInfo
operator|.
name|type
operator|==
name|RegionType
operator|.
name|INSERT
operator|||
name|fromInfo
operator|.
name|type
operator|==
name|RegionType
operator|.
name|COMMON
operator|)
operator|&&
name|toInfo
operator|.
name|type
operator|==
name|RegionType
operator|.
name|DELETE
condition|)
block|{
name|LineOnOtherInfo
name|infoOnSideA
init|=
name|manager
operator|.
name|lineMapper
operator|.
name|lineOnOther
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|,
name|fromInfo
operator|.
name|line
argument_list|)
decl_stmt|;
name|int
name|startLineOnSideA
init|=
name|infoOnSideA
operator|.
name|getLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|infoOnSideA
operator|.
name|isAligned
argument_list|()
condition|)
block|{
name|from
operator|.
name|line
argument_list|(
name|startLineOnSideA
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|from
operator|.
name|line
argument_list|(
name|startLineOnSideA
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|from
operator|.
name|ch
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|to
operator|.
name|line
argument_list|(
name|toInfo
operator|.
name|line
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fromInfo
operator|.
name|type
operator|==
name|RegionType
operator|.
name|DELETE
operator|&&
name|toInfo
operator|.
name|type
operator|==
name|RegionType
operator|.
name|INSERT
condition|)
block|{
name|LineOnOtherInfo
name|infoOnSideB
init|=
name|manager
operator|.
name|lineMapper
operator|.
name|lineOnOther
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|,
name|fromInfo
operator|.
name|line
argument_list|)
decl_stmt|;
name|int
name|startLineOnSideB
init|=
name|infoOnSideB
operator|.
name|getLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|infoOnSideB
operator|.
name|isAligned
argument_list|()
condition|)
block|{
name|from
operator|.
name|line
argument_list|(
name|startLineOnSideB
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|from
operator|.
name|line
argument_list|(
name|startLineOnSideB
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|from
operator|.
name|ch
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|to
operator|.
name|line
argument_list|(
name|toInfo
operator|.
name|line
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fromInfo
operator|.
name|type
operator|==
name|RegionType
operator|.
name|DELETE
operator|&&
name|toInfo
operator|.
name|type
operator|==
name|RegionType
operator|.
name|COMMON
condition|)
block|{
name|int
name|toLineOnSideA
init|=
name|manager
operator|.
name|lineMapper
operator|.
name|lineOnOther
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|,
name|toInfo
operator|.
name|line
argument_list|)
operator|.
name|getLine
argument_list|()
decl_stmt|;
name|from
operator|.
name|line
argument_list|(
name|fromInfo
operator|.
name|line
argument_list|)
expr_stmt|;
comment|// Force the end line to be on the same side as the start line.
name|to
operator|.
name|line
argument_list|(
name|toLineOnSideA
argument_list|)
expr_stmt|;
name|side
operator|=
name|DisplaySide
operator|.
name|A
expr_stmt|;
block|}
else|else
block|{
comment|// Common case
name|from
operator|.
name|line
argument_list|(
name|fromInfo
operator|.
name|line
argument_list|)
expr_stmt|;
name|to
operator|.
name|line
argument_list|(
name|toInfo
operator|.
name|line
argument_list|)
expr_stmt|;
block|}
name|addDraftBox
argument_list|(
name|side
argument_list|,
name|CommentInfo
operator|.
name|create
argument_list|(
name|getPath
argument_list|()
argument_list|,
name|getStoredSideFromDisplaySide
argument_list|(
name|side
argument_list|)
argument_list|,
name|to
operator|.
name|line
argument_list|()
operator|+
literal|1
argument_list|,
name|CommentRange
operator|.
name|create
argument_list|(
name|fromTo
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
operator|.
name|setEdit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setCursor
argument_list|(
name|Pos
operator|.
name|create
argument_list|(
name|host
operator|.
name|getCmLine
argument_list|(
name|to
operator|.
name|line
argument_list|()
argument_list|,
name|side
argument_list|)
argument_list|,
name|to
operator|.
name|ch
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setSelection
argument_list|(
name|cm
operator|.
name|getCursor
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|cmLine
init|=
name|cm
operator|.
name|getLineNumber
argument_list|(
name|cm
operator|.
name|extras
argument_list|()
operator|.
name|activeLine
argument_list|()
argument_list|)
decl_stmt|;
name|LineRegionInfo
name|info
init|=
operator|(
operator|(
name|Unified
operator|)
name|host
operator|)
operator|.
name|getLineRegionInfoFromCmLine
argument_list|(
name|cmLine
argument_list|)
decl_stmt|;
name|insertNewDraft
argument_list|(
name|info
operator|.
name|getSide
argument_list|()
argument_list|,
name|cmLine
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|group (DisplaySide side, int cmLinePlusOne)
name|CommentGroup
name|group
parameter_list|(
name|DisplaySide
name|side
parameter_list|,
name|int
name|cmLinePlusOne
parameter_list|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|CommentGroup
argument_list|>
name|map
init|=
name|map
argument_list|(
name|side
argument_list|)
decl_stmt|;
name|CommentGroup
name|existing
init|=
name|map
operator|.
name|get
argument_list|(
name|cmLinePlusOne
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
return|return
name|existing
return|;
block|}
name|UnifiedCommentGroup
name|g
init|=
operator|new
name|UnifiedCommentGroup
argument_list|(
name|this
argument_list|,
name|host
operator|.
name|getCmFromSide
argument_list|(
name|side
argument_list|)
argument_list|,
name|side
argument_list|,
name|cmLinePlusOne
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|cmLinePlusOne
argument_list|,
name|g
argument_list|)
expr_stmt|;
if|if
condition|(
name|mergedMap
operator|.
name|containsKey
argument_list|(
name|cmLinePlusOne
argument_list|)
condition|)
block|{
name|duplicates
operator|.
name|put
argument_list|(
name|cmLinePlusOne
argument_list|,
name|mergedMap
operator|.
name|remove
argument_list|(
name|cmLinePlusOne
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mergedMap
operator|.
name|put
argument_list|(
name|cmLinePlusOne
argument_list|,
name|g
argument_list|)
expr_stmt|;
if|if
condition|(
name|isAttached
argument_list|()
condition|)
block|{
name|g
operator|.
name|init
argument_list|(
name|host
operator|.
name|getDiffTable
argument_list|()
argument_list|)
expr_stmt|;
name|g
operator|.
name|handleRedraw
argument_list|()
expr_stmt|;
block|}
return|return
name|g
return|;
block|}
block|}
end_class

end_unit

