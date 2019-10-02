begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|data
operator|.
name|PatchScript
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
name|data
operator|.
name|PatchScript
operator|.
name|DisplayMethod
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
name|data
operator|.
name|PatchScript
operator|.
name|PatchScriptFileInfo
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
name|entities
operator|.
name|Patch
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
name|extensions
operator|.
name|common
operator|.
name|ChangeType
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
name|extensions
operator|.
name|common
operator|.
name|DiffInfo
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
name|extensions
operator|.
name|common
operator|.
name|DiffInfo
operator|.
name|ContentEntry
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
name|extensions
operator|.
name|common
operator|.
name|DiffInfo
operator|.
name|FileMeta
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
name|extensions
operator|.
name|common
operator|.
name|DiffInfo
operator|.
name|IntraLineStatus
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
name|extensions
operator|.
name|common
operator|.
name|DiffWebLinkInfo
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
name|extensions
operator|.
name|common
operator|.
name|WebLinkInfo
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
name|jgit
operator|.
name|diff
operator|.
name|ReplaceEdit
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
name|prettify
operator|.
name|common
operator|.
name|SparseFileContent
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
name|server
operator|.
name|change
operator|.
name|FileContentUtil
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
name|server
operator|.
name|project
operator|.
name|ProjectState
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
name|Optional
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
import|;
end_import

begin_comment
comment|/** Creates and fills a new {@link DiffInfo} object based on diff between files. */
end_comment

begin_class
DECL|class|DiffInfoCreator
specifier|public
class|class
name|DiffInfoCreator
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|CHANGE_TYPE
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|Patch
operator|.
name|ChangeType
argument_list|,
name|ChangeType
argument_list|>
name|CHANGE_TYPE
init|=
name|Maps
operator|.
name|immutableEnumMap
argument_list|(
operator|new
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Patch
operator|.
name|ChangeType
argument_list|,
name|ChangeType
argument_list|>
argument_list|()
operator|.
name|put
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|ADDED
argument_list|,
name|ChangeType
operator|.
name|ADDED
argument_list|)
operator|.
name|put
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|MODIFIED
argument_list|,
name|ChangeType
operator|.
name|MODIFIED
argument_list|)
operator|.
name|put
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|DELETED
argument_list|,
name|ChangeType
operator|.
name|DELETED
argument_list|)
operator|.
name|put
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|RENAMED
argument_list|,
name|ChangeType
operator|.
name|RENAMED
argument_list|)
operator|.
name|put
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|COPIED
argument_list|,
name|ChangeType
operator|.
name|COPIED
argument_list|)
operator|.
name|put
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|REWRITE
argument_list|,
name|ChangeType
operator|.
name|REWRITE
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|webLinksProvider
specifier|private
specifier|final
name|DiffWebLinksProvider
name|webLinksProvider
decl_stmt|;
DECL|field|intraline
specifier|private
specifier|final
name|boolean
name|intraline
decl_stmt|;
DECL|field|state
specifier|private
specifier|final
name|ProjectState
name|state
decl_stmt|;
DECL|method|DiffInfoCreator ( ProjectState state, DiffWebLinksProvider webLinksProvider, boolean intraline)
specifier|public
name|DiffInfoCreator
parameter_list|(
name|ProjectState
name|state
parameter_list|,
name|DiffWebLinksProvider
name|webLinksProvider
parameter_list|,
name|boolean
name|intraline
parameter_list|)
block|{
name|this
operator|.
name|webLinksProvider
operator|=
name|webLinksProvider
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
name|this
operator|.
name|intraline
operator|=
name|intraline
expr_stmt|;
block|}
comment|/* Returns the {@link DiffInfo} to display for end-users */
DECL|method|create (PatchScript ps, DiffSide sideA, DiffSide sideB)
specifier|public
name|DiffInfo
name|create
parameter_list|(
name|PatchScript
name|ps
parameter_list|,
name|DiffSide
name|sideA
parameter_list|,
name|DiffSide
name|sideB
parameter_list|)
block|{
name|DiffInfo
name|result
init|=
operator|new
name|DiffInfo
argument_list|()
decl_stmt|;
name|ImmutableList
argument_list|<
name|DiffWebLinkInfo
argument_list|>
name|links
init|=
name|webLinksProvider
operator|.
name|getDiffLinks
argument_list|()
decl_stmt|;
name|result
operator|.
name|webLinks
operator|=
name|links
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|links
expr_stmt|;
if|if
condition|(
name|ps
operator|.
name|isBinary
argument_list|()
condition|)
block|{
name|result
operator|.
name|binary
operator|=
literal|true
expr_stmt|;
block|}
name|result
operator|.
name|metaA
operator|=
name|createFileMeta
argument_list|(
name|sideA
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|result
operator|.
name|metaB
operator|=
name|createFileMeta
argument_list|(
name|sideB
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|intraline
condition|)
block|{
if|if
condition|(
name|ps
operator|.
name|hasIntralineTimeout
argument_list|()
condition|)
block|{
name|result
operator|.
name|intralineStatus
operator|=
name|IntraLineStatus
operator|.
name|TIMEOUT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ps
operator|.
name|hasIntralineFailure
argument_list|()
condition|)
block|{
name|result
operator|.
name|intralineStatus
operator|=
name|IntraLineStatus
operator|.
name|FAILURE
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|intralineStatus
operator|=
name|IntraLineStatus
operator|.
name|OK
expr_stmt|;
block|}
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"intralineStatus = %s"
argument_list|,
name|result
operator|.
name|intralineStatus
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|changeType
operator|=
name|CHANGE_TYPE
operator|.
name|get
argument_list|(
name|ps
operator|.
name|getChangeType
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"changeType = %s"
argument_list|,
name|result
operator|.
name|changeType
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|changeType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unknown change type: "
operator|+
name|ps
operator|.
name|getChangeType
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|ps
operator|.
name|getPatchHeader
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|result
operator|.
name|diffHeader
operator|=
name|ps
operator|.
name|getPatchHeader
argument_list|()
expr_stmt|;
block|}
name|result
operator|.
name|content
operator|=
name|calculateDiffContentEntries
argument_list|(
name|ps
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|calculateDiffContentEntries (PatchScript ps)
specifier|private
specifier|static
name|List
argument_list|<
name|ContentEntry
argument_list|>
name|calculateDiffContentEntries
parameter_list|(
name|PatchScript
name|ps
parameter_list|)
block|{
name|ContentCollector
name|contentCollector
init|=
operator|new
name|ContentCollector
argument_list|(
name|ps
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Edit
argument_list|>
name|editsDueToRebase
init|=
name|ps
operator|.
name|getEditsDueToRebase
argument_list|()
decl_stmt|;
for|for
control|(
name|Edit
name|edit
range|:
name|ps
operator|.
name|getEdits
argument_list|()
control|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"next edit = %s"
argument_list|,
name|edit
argument_list|)
expr_stmt|;
if|if
condition|(
name|edit
operator|.
name|getType
argument_list|()
operator|==
name|Edit
operator|.
name|Type
operator|.
name|EMPTY
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"skip empty edit"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|contentCollector
operator|.
name|addCommon
argument_list|(
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|)
expr_stmt|;
name|checkState
argument_list|(
name|contentCollector
operator|.
name|nextA
operator|==
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|,
literal|"nextA = %s; want %s"
argument_list|,
name|contentCollector
operator|.
name|nextA
argument_list|,
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|)
expr_stmt|;
name|checkState
argument_list|(
name|contentCollector
operator|.
name|nextB
operator|==
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|,
literal|"nextB = %s; want %s"
argument_list|,
name|contentCollector
operator|.
name|nextB
argument_list|,
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|edit
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|DELETE
case|:
case|case
name|INSERT
case|:
case|case
name|REPLACE
case|:
name|List
argument_list|<
name|Edit
argument_list|>
name|internalEdit
init|=
name|edit
operator|instanceof
name|ReplaceEdit
condition|?
operator|(
operator|(
name|ReplaceEdit
operator|)
name|edit
operator|)
operator|.
name|getInternalEdits
argument_list|()
else|:
literal|null
decl_stmt|;
name|boolean
name|dueToRebase
init|=
name|editsDueToRebase
operator|.
name|contains
argument_list|(
name|edit
argument_list|)
decl_stmt|;
name|contentCollector
operator|.
name|addDiff
argument_list|(
name|edit
operator|.
name|getEndA
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
argument_list|,
name|internalEdit
argument_list|,
name|dueToRebase
argument_list|)
expr_stmt|;
break|break;
case|case
name|EMPTY
case|:
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
name|contentCollector
operator|.
name|addCommon
argument_list|(
name|ps
operator|.
name|getA
argument_list|()
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|contentCollector
operator|.
name|lines
return|;
block|}
DECL|method|createFileMeta (DiffSide side)
specifier|private
name|Optional
argument_list|<
name|FileMeta
argument_list|>
name|createFileMeta
parameter_list|(
name|DiffSide
name|side
parameter_list|)
block|{
name|PatchScriptFileInfo
name|fileInfo
init|=
name|side
operator|.
name|fileInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileInfo
operator|.
name|displayMethod
operator|==
name|DisplayMethod
operator|.
name|NONE
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|FileMeta
name|result
init|=
operator|new
name|FileMeta
argument_list|()
decl_stmt|;
name|result
operator|.
name|name
operator|=
name|side
operator|.
name|fileName
argument_list|()
expr_stmt|;
name|result
operator|.
name|contentType
operator|=
name|FileContentUtil
operator|.
name|resolveContentType
argument_list|(
name|state
argument_list|,
name|side
operator|.
name|fileName
argument_list|()
argument_list|,
name|fileInfo
operator|.
name|mode
argument_list|,
name|fileInfo
operator|.
name|mimeType
argument_list|)
expr_stmt|;
name|result
operator|.
name|lines
operator|=
name|fileInfo
operator|.
name|content
operator|.
name|getSize
argument_list|()
expr_stmt|;
name|ImmutableList
argument_list|<
name|WebLinkInfo
argument_list|>
name|links
init|=
name|webLinksProvider
operator|.
name|getFileWebLinks
argument_list|(
name|side
operator|.
name|type
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|webLinks
operator|=
name|links
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|links
expr_stmt|;
name|result
operator|.
name|commitId
operator|=
name|fileInfo
operator|.
name|commitId
expr_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|class|ContentCollector
specifier|private
specifier|static
class|class
name|ContentCollector
block|{
DECL|field|lines
specifier|private
specifier|final
name|List
argument_list|<
name|ContentEntry
argument_list|>
name|lines
decl_stmt|;
DECL|field|fileA
specifier|private
specifier|final
name|SparseFileContent
operator|.
name|Accessor
name|fileA
decl_stmt|;
DECL|field|fileB
specifier|private
specifier|final
name|SparseFileContent
operator|.
name|Accessor
name|fileB
decl_stmt|;
DECL|field|ignoreWS
specifier|private
specifier|final
name|boolean
name|ignoreWS
decl_stmt|;
DECL|field|nextA
specifier|private
name|int
name|nextA
decl_stmt|;
DECL|field|nextB
specifier|private
name|int
name|nextB
decl_stmt|;
DECL|method|ContentCollector (PatchScript ps)
name|ContentCollector
parameter_list|(
name|PatchScript
name|ps
parameter_list|)
block|{
name|lines
operator|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|ps
operator|.
name|getEdits
argument_list|()
operator|.
name|size
argument_list|()
operator|+
literal|2
argument_list|)
expr_stmt|;
name|fileA
operator|=
name|ps
operator|.
name|getA
argument_list|()
operator|.
name|createAccessor
argument_list|()
expr_stmt|;
name|fileB
operator|=
name|ps
operator|.
name|getB
argument_list|()
operator|.
name|createAccessor
argument_list|()
expr_stmt|;
name|ignoreWS
operator|=
name|ps
operator|.
name|isIgnoreWhitespace
argument_list|()
expr_stmt|;
block|}
DECL|method|addCommon (int end)
name|void
name|addCommon
parameter_list|(
name|int
name|end
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"addCommon: end = %d"
argument_list|,
name|end
argument_list|)
expr_stmt|;
name|end
operator|=
name|Math
operator|.
name|min
argument_list|(
name|end
argument_list|,
name|fileA
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"end = %d"
argument_list|,
name|end
argument_list|)
expr_stmt|;
if|if
condition|(
name|nextA
operator|>=
name|end
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"nextA>= end: nextA = %d, end = %d"
argument_list|,
name|nextA
argument_list|,
name|end
argument_list|)
expr_stmt|;
return|return;
block|}
while|while
condition|(
name|nextA
operator|<
name|end
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"nextA< end: nextA = %d, end = %d"
argument_list|,
name|nextA
argument_list|,
name|end
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|fileA
operator|.
name|contains
argument_list|(
name|nextA
argument_list|)
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"fileA does not contain nextA: nextA = %d"
argument_list|,
name|nextA
argument_list|)
expr_stmt|;
name|int
name|endRegion
init|=
name|Math
operator|.
name|min
argument_list|(
name|end
argument_list|,
name|nextA
operator|==
literal|0
condition|?
name|fileA
operator|.
name|first
argument_list|()
else|:
name|fileA
operator|.
name|next
argument_list|(
name|nextA
operator|-
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|endRegion
operator|-
name|nextA
decl_stmt|;
name|entry
argument_list|()
operator|.
name|skip
operator|=
name|len
expr_stmt|;
name|nextA
operator|=
name|endRegion
expr_stmt|;
name|nextB
operator|+=
name|len
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"setting: nextA = %d, nextB = %d"
argument_list|,
name|nextA
argument_list|,
name|nextB
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|ContentEntry
name|e
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|nextA
init|;
name|i
operator|==
name|nextA
operator|&&
name|i
operator|<
name|end
condition|;
name|i
operator|=
name|fileA
operator|.
name|next
argument_list|(
name|i
argument_list|)
operator|,
name|nextA
operator|++
operator|,
name|nextB
operator|++
control|)
block|{
if|if
condition|(
name|ignoreWS
operator|&&
name|fileB
operator|.
name|contains
argument_list|(
name|nextB
argument_list|)
condition|)
block|{
if|if
condition|(
name|e
operator|==
literal|null
operator|||
name|e
operator|.
name|common
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"create new common entry: nextA = %d, nextB = %d"
argument_list|,
name|nextA
argument_list|,
name|nextB
argument_list|)
expr_stmt|;
name|e
operator|=
name|entry
argument_list|()
expr_stmt|;
name|e
operator|.
name|a
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|end
operator|-
name|nextA
argument_list|)
expr_stmt|;
name|e
operator|.
name|b
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|end
operator|-
name|nextA
argument_list|)
expr_stmt|;
name|e
operator|.
name|common
operator|=
literal|true
expr_stmt|;
block|}
name|e
operator|.
name|a
operator|.
name|add
argument_list|(
name|fileA
operator|.
name|get
argument_list|(
name|nextA
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|b
operator|.
name|add
argument_list|(
name|fileB
operator|.
name|get
argument_list|(
name|nextB
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|e
operator|==
literal|null
operator|||
name|e
operator|.
name|common
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"create new non-common entry: nextA = %d, nextB = %d"
argument_list|,
name|nextA
argument_list|,
name|nextB
argument_list|)
expr_stmt|;
name|e
operator|=
name|entry
argument_list|()
expr_stmt|;
name|e
operator|.
name|ab
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|end
operator|-
name|nextA
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|ab
operator|.
name|add
argument_list|(
name|fileA
operator|.
name|get
argument_list|(
name|nextA
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|addDiff (int endA, int endB, List<Edit> internalEdit, boolean dueToRebase)
name|void
name|addDiff
parameter_list|(
name|int
name|endA
parameter_list|,
name|int
name|endB
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|internalEdit
parameter_list|,
name|boolean
name|dueToRebase
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"addDiff: endA = %d, endB = %d, numberOfInternalEdits = %d, dueToRebase = %s"
argument_list|,
name|endA
argument_list|,
name|endB
argument_list|,
name|internalEdit
operator|!=
literal|null
condition|?
name|internalEdit
operator|.
name|size
argument_list|()
else|:
literal|0
argument_list|,
name|dueToRebase
argument_list|)
expr_stmt|;
name|int
name|lenA
init|=
name|endA
operator|-
name|nextA
decl_stmt|;
name|int
name|lenB
init|=
name|endB
operator|-
name|nextB
decl_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"lenA = %d, lenB = %d"
argument_list|,
name|lenA
argument_list|,
name|lenB
argument_list|)
expr_stmt|;
name|checkState
argument_list|(
name|lenA
operator|>
literal|0
operator|||
name|lenB
operator|>
literal|0
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"create non-common entry"
argument_list|)
expr_stmt|;
name|ContentEntry
name|e
init|=
name|entry
argument_list|()
decl_stmt|;
if|if
condition|(
name|lenA
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"lenA> 0: lenA = %d"
argument_list|,
name|lenA
argument_list|)
expr_stmt|;
name|e
operator|.
name|a
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|lenA
argument_list|)
expr_stmt|;
for|for
control|(
init|;
name|nextA
operator|<
name|endA
condition|;
name|nextA
operator|++
control|)
block|{
name|e
operator|.
name|a
operator|.
name|add
argument_list|(
name|fileA
operator|.
name|get
argument_list|(
name|nextA
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|lenB
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"lenB> 0: lenB = %d"
argument_list|,
name|lenB
argument_list|)
expr_stmt|;
name|e
operator|.
name|b
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|lenB
argument_list|)
expr_stmt|;
for|for
control|(
init|;
name|nextB
operator|<
name|endB
condition|;
name|nextB
operator|++
control|)
block|{
name|e
operator|.
name|b
operator|.
name|add
argument_list|(
name|fileB
operator|.
name|get
argument_list|(
name|nextB
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|internalEdit
operator|!=
literal|null
operator|&&
operator|!
name|internalEdit
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"processing internal edits"
argument_list|)
expr_stmt|;
name|e
operator|.
name|editA
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|internalEdit
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
name|e
operator|.
name|editB
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|internalEdit
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
name|int
name|lastA
init|=
literal|0
decl_stmt|;
name|int
name|lastB
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Edit
name|edit
range|:
name|internalEdit
control|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"internal edit = %s"
argument_list|,
name|edit
argument_list|)
expr_stmt|;
if|if
condition|(
name|edit
operator|.
name|getBeginA
argument_list|()
operator|!=
name|edit
operator|.
name|getEndA
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"edit.getBeginA() != edit.getEndA(): edit.getBeginA() = %d, edit.getEndA() = %d"
argument_list|,
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|editA
operator|.
name|add
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|edit
operator|.
name|getBeginA
argument_list|()
operator|-
name|lastA
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
operator|-
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|lastA
operator|=
name|edit
operator|.
name|getEndA
argument_list|()
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"lastA = %d"
argument_list|,
name|lastA
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|edit
operator|.
name|getBeginB
argument_list|()
operator|!=
name|edit
operator|.
name|getEndB
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"edit.getBeginB() != edit.getEndB(): edit.getBeginB() = %d, edit.getEndB() = %d"
argument_list|,
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|editB
operator|.
name|add
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|edit
operator|.
name|getBeginB
argument_list|()
operator|-
name|lastB
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
operator|-
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|lastB
operator|=
name|edit
operator|.
name|getEndB
argument_list|()
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"lastB = %d"
argument_list|,
name|lastB
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|e
operator|.
name|dueToRebase
operator|=
name|dueToRebase
condition|?
literal|true
else|:
literal|null
expr_stmt|;
block|}
DECL|method|entry ()
specifier|private
name|ContentEntry
name|entry
parameter_list|()
block|{
name|ContentEntry
name|e
init|=
operator|new
name|ContentEntry
argument_list|()
decl_stmt|;
name|lines
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
name|e
return|;
block|}
block|}
block|}
end_class

end_unit

