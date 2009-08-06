begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
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
name|data
operator|.
name|EditList
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
name|client
operator|.
name|data
operator|.
name|PatchScriptSettings
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
name|data
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
name|client
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
name|client
operator|.
name|patches
operator|.
name|CommentDetail
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
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchLineComment
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
name|rpc
operator|.
name|CorruptEntityException
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
name|FileTypeRegistry
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeType
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeUtil2
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|errors
operator|.
name|CorruptObjectException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|errors
operator|.
name|IncorrectObjectTypeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|errors
operator|.
name|MissingObjectException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|FileMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|CombinedFileHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|patch
operator|.
name|FileHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevTree
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|treewalk
operator|.
name|TreeWalk
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|IntList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_class
DECL|class|PatchScriptBuilder
class|class
name|PatchScriptBuilder
block|{
DECL|field|MAX_CONTEXT
specifier|static
specifier|final
name|int
name|MAX_CONTEXT
init|=
literal|5000000
decl_stmt|;
DECL|field|BIG_FILE
specifier|static
specifier|final
name|int
name|BIG_FILE
init|=
literal|9000
decl_stmt|;
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PatchScriptBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|EDIT_SORT
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Edit
argument_list|>
name|EDIT_SORT
init|=
operator|new
name|Comparator
argument_list|<
name|Edit
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|Edit
name|o1
parameter_list|,
specifier|final
name|Edit
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getBeginA
argument_list|()
operator|-
name|o2
operator|.
name|getBeginA
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|header
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|header
decl_stmt|;
DECL|field|db
specifier|private
name|Repository
name|db
decl_stmt|;
DECL|field|patch
specifier|private
name|Patch
name|patch
decl_stmt|;
DECL|field|patchKey
specifier|private
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
DECL|field|settings
specifier|private
name|PatchScriptSettings
name|settings
decl_stmt|;
DECL|field|a
specifier|private
specifier|final
name|Side
name|a
decl_stmt|;
DECL|field|b
specifier|private
specifier|final
name|Side
name|b
decl_stmt|;
DECL|field|edits
specifier|private
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|FileTypeRegistry
name|registry
decl_stmt|;
DECL|method|PatchScriptBuilder ()
name|PatchScriptBuilder
parameter_list|()
block|{
name|header
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|a
operator|=
operator|new
name|Side
argument_list|()
expr_stmt|;
name|b
operator|=
operator|new
name|Side
argument_list|()
expr_stmt|;
name|registry
operator|=
name|FileTypeRegistry
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
DECL|method|setRepository (final Repository r)
name|void
name|setRepository
parameter_list|(
specifier|final
name|Repository
name|r
parameter_list|)
block|{
name|db
operator|=
name|r
expr_stmt|;
block|}
DECL|method|setPatch (final Patch p)
name|void
name|setPatch
parameter_list|(
specifier|final
name|Patch
name|p
parameter_list|)
block|{
name|patch
operator|=
name|p
expr_stmt|;
name|patchKey
operator|=
name|patch
operator|.
name|getKey
argument_list|()
expr_stmt|;
block|}
DECL|method|setSettings (final PatchScriptSettings s)
name|void
name|setSettings
parameter_list|(
specifier|final
name|PatchScriptSettings
name|s
parameter_list|)
block|{
name|settings
operator|=
name|s
expr_stmt|;
block|}
DECL|method|context ()
specifier|private
name|int
name|context
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getContext
argument_list|()
return|;
block|}
DECL|method|toPatchScript (final DiffCacheKey key, final DiffCacheContent contentWS, final CommentDetail comments, final DiffCacheContent contentAct)
name|PatchScript
name|toPatchScript
parameter_list|(
specifier|final
name|DiffCacheKey
name|key
parameter_list|,
specifier|final
name|DiffCacheContent
name|contentWS
parameter_list|,
specifier|final
name|CommentDetail
name|comments
parameter_list|,
specifier|final
name|DiffCacheContent
name|contentAct
parameter_list|)
throws|throws
name|CorruptEntityException
block|{
if|if
condition|(
name|contentAct
operator|.
name|getFileHeader
argument_list|()
operator|instanceof
name|CombinedFileHeader
condition|)
block|{
comment|// For a diff --cc format we don't support converting it into
comment|// a patch script. Instead treat everything as a file header.
comment|//
name|edits
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
name|packHeader
argument_list|(
name|contentAct
operator|.
name|getFileHeader
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|PatchScript
argument_list|(
name|header
argument_list|,
name|settings
argument_list|,
name|a
operator|.
name|dst
argument_list|,
name|b
operator|.
name|dst
argument_list|,
name|edits
argument_list|,
name|a
operator|.
name|displayMethod
argument_list|,
name|b
operator|.
name|displayMethod
argument_list|)
return|;
block|}
name|a
operator|.
name|path
operator|=
name|oldFileName
argument_list|(
name|key
argument_list|,
name|contentAct
operator|.
name|getFileHeader
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|path
operator|=
name|newFileName
argument_list|(
name|key
argument_list|,
name|contentAct
operator|.
name|getFileHeader
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|resolve
argument_list|(
literal|null
argument_list|,
name|key
operator|.
name|getOldId
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|resolve
argument_list|(
name|a
argument_list|,
name|key
operator|.
name|getNewId
argument_list|()
argument_list|)
expr_stmt|;
name|edits
operator|=
operator|new
name|ArrayList
argument_list|<
name|Edit
argument_list|>
argument_list|(
name|contentAct
operator|.
name|getEdits
argument_list|()
argument_list|)
expr_stmt|;
name|ensureCommentsVisible
argument_list|(
name|comments
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentAct
operator|.
name|getFileHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|packHeader
argument_list|(
name|contentAct
operator|.
name|getFileHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|a
operator|.
name|mode
operator|==
name|FileMode
operator|.
name|GITLINK
operator|||
name|b
operator|.
name|mode
operator|==
name|FileMode
operator|.
name|GITLINK
condition|)
block|{      }
elseif|else
if|if
condition|(
name|a
operator|.
name|src
operator|==
name|b
operator|.
name|src
operator|&&
name|a
operator|.
name|src
operator|.
name|size
argument_list|()
operator|<=
name|context
argument_list|()
operator|&&
name|contentAct
operator|.
name|getEdits
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Odd special case; the files are identical (100% rename or copy)
comment|// and the user has asked for context that is larger than the file.
comment|// Send them the entire file, with an empty edit after the last line.
comment|//
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|a
operator|.
name|src
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|a
operator|.
name|src
operator|.
name|addLineTo
argument_list|(
name|a
operator|.
name|dst
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|edits
operator|=
operator|new
name|ArrayList
argument_list|<
name|Edit
argument_list|>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|edits
operator|.
name|add
argument_list|(
operator|new
name|Edit
argument_list|(
name|a
operator|.
name|src
operator|.
name|size
argument_list|()
argument_list|,
name|a
operator|.
name|src
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|BIG_FILE
operator|<
name|Math
operator|.
name|max
argument_list|(
name|a
operator|.
name|src
operator|.
name|size
argument_list|()
argument_list|,
name|b
operator|.
name|src
operator|.
name|size
argument_list|()
argument_list|)
operator|&&
literal|25
operator|<
name|context
argument_list|()
condition|)
block|{
name|settings
operator|.
name|setContext
argument_list|(
literal|25
argument_list|)
expr_stmt|;
block|}
name|packContent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|contentWS
operator|!=
name|contentAct
condition|)
block|{
comment|// The edit list we used to pack the file contents doesn't honor the
comment|// whitespace settings requested. Instead we must rebuild our edit
comment|// list around the whitespace edit list.
comment|//
name|edits
operator|=
operator|new
name|ArrayList
argument_list|<
name|Edit
argument_list|>
argument_list|(
name|contentWS
operator|.
name|getEdits
argument_list|()
argument_list|)
expr_stmt|;
name|ensureCommentsVisible
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PatchScript
argument_list|(
name|header
argument_list|,
name|settings
argument_list|,
name|a
operator|.
name|dst
argument_list|,
name|b
operator|.
name|dst
argument_list|,
name|edits
argument_list|,
name|a
operator|.
name|displayMethod
argument_list|,
name|b
operator|.
name|displayMethod
argument_list|)
return|;
block|}
DECL|method|oldFileName (final DiffCacheKey key, final FileHeader fh)
specifier|private
specifier|static
name|String
name|oldFileName
parameter_list|(
specifier|final
name|DiffCacheKey
name|key
parameter_list|,
specifier|final
name|FileHeader
name|fh
parameter_list|)
block|{
if|if
condition|(
name|fh
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|FileMode
operator|.
name|MISSING
operator|==
name|fh
operator|.
name|getOldMode
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|FileHeader
operator|.
name|DEV_NULL
operator|.
name|equals
argument_list|(
name|fh
operator|.
name|getOldName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|fh
operator|.
name|getOldName
argument_list|()
return|;
block|}
if|if
condition|(
name|key
operator|.
name|getSourceFileName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|key
operator|.
name|getSourceFileName
argument_list|()
return|;
block|}
return|return
name|key
operator|.
name|getFileName
argument_list|()
return|;
block|}
DECL|method|newFileName (final DiffCacheKey key, final FileHeader fh)
specifier|private
specifier|static
name|String
name|newFileName
parameter_list|(
specifier|final
name|DiffCacheKey
name|key
parameter_list|,
specifier|final
name|FileHeader
name|fh
parameter_list|)
block|{
if|if
condition|(
name|fh
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|FileMode
operator|.
name|MISSING
operator|==
name|fh
operator|.
name|getNewMode
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|FileHeader
operator|.
name|DEV_NULL
operator|.
name|equals
argument_list|(
name|fh
operator|.
name|getNewName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|fh
operator|.
name|getNewName
argument_list|()
return|;
block|}
return|return
name|key
operator|.
name|getFileName
argument_list|()
return|;
block|}
DECL|method|ensureCommentsVisible (final CommentDetail comments)
specifier|private
name|void
name|ensureCommentsVisible
parameter_list|(
specifier|final
name|CommentDetail
name|comments
parameter_list|)
block|{
if|if
condition|(
name|comments
operator|.
name|getCommentsA
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|comments
operator|.
name|getCommentsB
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// No comments, no additional dummy edits are required.
comment|//
return|return;
block|}
comment|// Construct empty Edit blocks around each location where a comment is.
comment|// This will force the later packContent method to include the regions
comment|// containing comments, potentially combining those regions together if
comment|// they have overlapping contexts. UI renders will also be able to make
comment|// correct hunks from this, but because the Edit is empty they will not
comment|// style it specially.
comment|//
specifier|final
name|List
argument_list|<
name|Edit
argument_list|>
name|empty
init|=
operator|new
name|ArrayList
argument_list|<
name|Edit
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|lastLine
decl_stmt|;
name|lastLine
operator|=
operator|-
literal|1
expr_stmt|;
for|for
control|(
name|PatchLineComment
name|plc
range|:
name|comments
operator|.
name|getCommentsA
argument_list|()
control|)
block|{
specifier|final
name|int
name|a
init|=
name|plc
operator|.
name|getLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastLine
operator|!=
name|a
condition|)
block|{
specifier|final
name|int
name|b
init|=
name|mapA2B
argument_list|(
name|a
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|b
condition|)
block|{
name|safeAdd
argument_list|(
name|empty
argument_list|,
operator|new
name|Edit
argument_list|(
name|a
operator|-
literal|1
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|lastLine
operator|=
name|a
expr_stmt|;
block|}
block|}
name|lastLine
operator|=
operator|-
literal|1
expr_stmt|;
for|for
control|(
name|PatchLineComment
name|plc
range|:
name|comments
operator|.
name|getCommentsB
argument_list|()
control|)
block|{
specifier|final
name|int
name|b
init|=
name|plc
operator|.
name|getLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastLine
operator|!=
name|b
condition|)
block|{
specifier|final
name|int
name|a
init|=
name|mapB2A
argument_list|(
name|b
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|a
condition|)
block|{
name|safeAdd
argument_list|(
name|empty
argument_list|,
operator|new
name|Edit
argument_list|(
name|a
argument_list|,
name|b
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|lastLine
operator|=
name|b
expr_stmt|;
block|}
block|}
comment|// Sort the final list by the index in A, so packContent can combine
comment|// them correctly later.
comment|//
name|edits
operator|.
name|addAll
argument_list|(
name|empty
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|edits
argument_list|,
name|EDIT_SORT
argument_list|)
expr_stmt|;
block|}
DECL|method|safeAdd (final List<Edit> empty, final Edit toAdd)
specifier|private
name|void
name|safeAdd
parameter_list|(
specifier|final
name|List
argument_list|<
name|Edit
argument_list|>
name|empty
parameter_list|,
specifier|final
name|Edit
name|toAdd
parameter_list|)
block|{
specifier|final
name|int
name|a
init|=
name|toAdd
operator|.
name|getBeginA
argument_list|()
decl_stmt|;
specifier|final
name|int
name|b
init|=
name|toAdd
operator|.
name|getBeginB
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Edit
name|e
range|:
name|edits
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getBeginA
argument_list|()
operator|<=
name|a
operator|&&
name|a
operator|<=
name|e
operator|.
name|getEndA
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|e
operator|.
name|getBeginB
argument_list|()
operator|<=
name|b
operator|&&
name|b
operator|<=
name|e
operator|.
name|getEndB
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
name|empty
operator|.
name|add
argument_list|(
name|toAdd
argument_list|)
expr_stmt|;
block|}
DECL|method|mapA2B (final int a)
specifier|private
name|int
name|mapA2B
parameter_list|(
specifier|final
name|int
name|a
parameter_list|)
block|{
if|if
condition|(
name|edits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Magic special case of an unmodified file.
comment|//
return|return
name|a
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|edits
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Edit
name|e
init|=
name|edits
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|<
name|e
operator|.
name|getBeginA
argument_list|()
condition|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
comment|// Special case of context at start of file.
comment|//
return|return
name|a
return|;
block|}
return|return
name|edits
operator|.
name|get
argument_list|(
name|i
operator|-
name|i
argument_list|)
operator|.
name|getEndB
argument_list|()
operator|+
operator|(
name|e
operator|.
name|getBeginA
argument_list|()
operator|-
name|a
operator|)
return|;
block|}
if|if
condition|(
name|e
operator|.
name|getBeginA
argument_list|()
operator|<=
name|a
operator|&&
name|a
operator|<=
name|e
operator|.
name|getEndA
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
specifier|final
name|Edit
name|last
init|=
name|edits
operator|.
name|get
argument_list|(
name|edits
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|last
operator|.
name|getBeginB
argument_list|()
operator|+
operator|(
name|a
operator|-
name|last
operator|.
name|getEndA
argument_list|()
operator|)
return|;
block|}
DECL|method|mapB2A (final int b)
specifier|private
name|int
name|mapB2A
parameter_list|(
specifier|final
name|int
name|b
parameter_list|)
block|{
if|if
condition|(
name|edits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Magic special case of an unmodified file.
comment|//
return|return
name|b
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|edits
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Edit
name|e
init|=
name|edits
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|<
name|e
operator|.
name|getBeginB
argument_list|()
condition|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
comment|// Special case of context at start of file.
comment|//
return|return
name|b
return|;
block|}
return|return
name|edits
operator|.
name|get
argument_list|(
name|i
operator|-
name|i
argument_list|)
operator|.
name|getEndA
argument_list|()
operator|+
operator|(
name|e
operator|.
name|getBeginB
argument_list|()
operator|-
name|b
operator|)
return|;
block|}
if|if
condition|(
name|e
operator|.
name|getBeginB
argument_list|()
operator|<=
name|b
operator|&&
name|b
operator|<=
name|e
operator|.
name|getEndB
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
specifier|final
name|Edit
name|last
init|=
name|edits
operator|.
name|get
argument_list|(
name|edits
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|last
operator|.
name|getBeginA
argument_list|()
operator|+
operator|(
name|b
operator|-
name|last
operator|.
name|getEndB
argument_list|()
operator|)
return|;
block|}
DECL|method|packHeader (final FileHeader fh)
specifier|private
name|void
name|packHeader
parameter_list|(
specifier|final
name|FileHeader
name|fh
parameter_list|)
block|{
specifier|final
name|byte
index|[]
name|buf
init|=
name|fh
operator|.
name|getBuffer
argument_list|()
decl_stmt|;
specifier|final
name|IntList
name|m
init|=
name|RawParseUtils
operator|.
name|lineMap
argument_list|(
name|buf
argument_list|,
name|fh
operator|.
name|getStartOffset
argument_list|()
argument_list|,
name|end
argument_list|(
name|fh
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|m
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|b
init|=
name|m
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|int
name|e
init|=
name|m
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|header
operator|.
name|add
argument_list|(
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|Constants
operator|.
name|CHARSET
argument_list|,
name|buf
argument_list|,
name|b
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|end (final FileHeader h)
specifier|private
specifier|static
name|int
name|end
parameter_list|(
specifier|final
name|FileHeader
name|h
parameter_list|)
block|{
if|if
condition|(
name|h
operator|instanceof
name|CombinedFileHeader
condition|)
block|{
return|return
name|h
operator|.
name|getEndOffset
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|h
operator|.
name|getHunks
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|h
operator|.
name|getHunks
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStartOffset
argument_list|()
return|;
block|}
return|return
name|h
operator|.
name|getEndOffset
argument_list|()
return|;
block|}
DECL|method|packContent ()
specifier|private
name|void
name|packContent
parameter_list|()
block|{
name|EditList
name|list
init|=
operator|new
name|EditList
argument_list|(
name|edits
argument_list|,
name|context
argument_list|()
argument_list|,
name|a
operator|.
name|src
operator|.
name|size
argument_list|()
argument_list|,
name|b
operator|.
name|src
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|EditList
operator|.
name|Hunk
name|hunk
range|:
name|list
operator|.
name|getHunks
argument_list|()
control|)
block|{
while|while
condition|(
name|hunk
operator|.
name|next
argument_list|()
condition|)
block|{
if|if
condition|(
name|hunk
operator|.
name|isContextLine
argument_list|()
condition|)
block|{
name|a
operator|.
name|src
operator|.
name|addLineTo
argument_list|(
name|a
operator|.
name|dst
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incBoth
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hunk
operator|.
name|isDeletedA
argument_list|()
condition|)
block|{
name|a
operator|.
name|src
operator|.
name|addLineTo
argument_list|(
name|a
operator|.
name|dst
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incA
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hunk
operator|.
name|isInsertedB
argument_list|()
condition|)
block|{
name|b
operator|.
name|src
operator|.
name|addLineTo
argument_list|(
name|b
operator|.
name|dst
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incB
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|class|Side
specifier|private
class|class
name|Side
block|{
DECL|field|path
name|String
name|path
decl_stmt|;
DECL|field|id
name|ObjectId
name|id
decl_stmt|;
DECL|field|mode
name|FileMode
name|mode
decl_stmt|;
DECL|field|src
name|Text
name|src
decl_stmt|;
DECL|field|mimeType
name|MimeType
name|mimeType
init|=
name|MimeUtil2
operator|.
name|UNKNOWN_MIME_TYPE
decl_stmt|;
DECL|field|displayMethod
name|DisplayMethod
name|displayMethod
init|=
name|DisplayMethod
operator|.
name|DIFF
decl_stmt|;
DECL|field|dst
specifier|final
name|SparseFileContent
name|dst
init|=
operator|new
name|SparseFileContent
argument_list|()
decl_stmt|;
DECL|method|resolve (final Side other, final ObjectId within)
name|void
name|resolve
parameter_list|(
specifier|final
name|Side
name|other
parameter_list|,
specifier|final
name|ObjectId
name|within
parameter_list|)
throws|throws
name|CorruptEntityException
block|{
try|try
block|{
specifier|final
name|TreeWalk
name|tw
init|=
name|find
argument_list|(
name|within
argument_list|)
decl_stmt|;
name|id
operator|=
name|tw
operator|!=
literal|null
condition|?
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
else|:
name|ObjectId
operator|.
name|zeroId
argument_list|()
expr_stmt|;
name|mode
operator|=
name|tw
operator|!=
literal|null
condition|?
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
else|:
name|FileMode
operator|.
name|MISSING
expr_stmt|;
specifier|final
name|boolean
name|reuse
init|=
name|other
operator|!=
literal|null
operator|&&
name|other
operator|.
name|id
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|&&
name|other
operator|.
name|mode
operator|==
name|mode
decl_stmt|;
if|if
condition|(
name|reuse
condition|)
block|{
name|src
operator|=
name|other
operator|.
name|src
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mode
operator|.
name|getObjectType
argument_list|()
operator|==
name|Constants
operator|.
name|OBJ_BLOB
condition|)
block|{
specifier|final
name|ObjectLoader
name|ldr
init|=
name|db
operator|.
name|openObject
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|ldr
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MissingObjectException
argument_list|(
name|id
argument_list|,
name|Constants
operator|.
name|TYPE_BLOB
argument_list|)
throw|;
block|}
specifier|final
name|byte
index|[]
name|data
init|=
name|ldr
operator|.
name|getCachedBytes
argument_list|()
decl_stmt|;
if|if
condition|(
name|ldr
operator|.
name|getType
argument_list|()
operator|!=
name|Constants
operator|.
name|OBJ_BLOB
condition|)
block|{
throw|throw
operator|new
name|IncorrectObjectTypeException
argument_list|(
name|id
argument_list|,
name|Constants
operator|.
name|TYPE_BLOB
argument_list|)
throw|;
block|}
name|src
operator|=
operator|new
name|Text
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|src
operator|=
name|Text
operator|.
name|EMPTY
expr_stmt|;
block|}
if|if
condition|(
name|reuse
condition|)
block|{
name|mimeType
operator|=
name|other
operator|.
name|mimeType
expr_stmt|;
name|displayMethod
operator|=
name|other
operator|.
name|displayMethod
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|src
operator|.
name|getContent
argument_list|()
operator|.
name|length
operator|>
literal|0
operator|&&
name|FileMode
operator|.
name|SYMLINK
operator|!=
name|mode
condition|)
block|{
name|mimeType
operator|=
name|registry
operator|.
name|getMimeType
argument_list|(
name|path
argument_list|,
name|src
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"image"
operator|.
name|equals
argument_list|(
name|mimeType
operator|.
name|getMediaType
argument_list|()
argument_list|)
operator|&&
name|registry
operator|.
name|isSafeInline
argument_list|(
name|mimeType
argument_list|)
condition|)
block|{
name|displayMethod
operator|=
name|DisplayMethod
operator|.
name|IMG
expr_stmt|;
block|}
block|}
if|if
condition|(
name|mode
operator|==
name|FileMode
operator|.
name|MISSING
condition|)
block|{
name|displayMethod
operator|=
name|DisplayMethod
operator|.
name|NONE
expr_stmt|;
block|}
name|dst
operator|.
name|setMissingNewlineAtEnd
argument_list|(
name|src
operator|.
name|isMissingNewlineAtEnd
argument_list|()
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setSize
argument_list|(
name|src
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot read "
operator|+
name|within
operator|.
name|name
argument_list|()
operator|+
literal|":"
operator|+
name|path
argument_list|,
name|err
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CorruptEntityException
argument_list|(
name|patchKey
argument_list|)
throw|;
block|}
block|}
DECL|method|find (final ObjectId within)
specifier|private
name|TreeWalk
name|find
parameter_list|(
specifier|final
name|ObjectId
name|within
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|CorruptObjectException
throws|,
name|IOException
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|RevTree
name|tree
init|=
name|rw
operator|.
name|parseTree
argument_list|(
name|within
argument_list|)
decl_stmt|;
return|return
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|db
argument_list|,
name|path
argument_list|,
name|tree
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

