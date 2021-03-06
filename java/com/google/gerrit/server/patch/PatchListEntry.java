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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
operator|.
name|toImmutableSet
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readBytes
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readEnum
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readFixInt64
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readString
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readVarInt32
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeBytes
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeEnum
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeFixInt64
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeString
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeVarInt32
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|ImmutableSet
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
name|entities
operator|.
name|Patch
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
name|entities
operator|.
name|Patch
operator|.
name|PatchType
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
name|PatchSet
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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

begin_import
import|import
name|org
operator|.
name|eclipse
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
name|eclipse
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
name|eclipse
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
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_class
DECL|class|PatchListEntry
specifier|public
class|class
name|PatchListEntry
block|{
DECL|field|EMPTY_HEADER
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|EMPTY_HEADER
init|=
block|{}
decl_stmt|;
DECL|method|empty (String fileName)
specifier|static
name|PatchListEntry
name|empty
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
return|return
operator|new
name|PatchListEntry
argument_list|(
name|ChangeType
operator|.
name|MODIFIED
argument_list|,
name|PatchType
operator|.
name|UNIFIED
argument_list|,
literal|null
argument_list|,
name|fileName
argument_list|,
name|EMPTY_HEADER
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|field|changeType
specifier|private
specifier|final
name|ChangeType
name|changeType
decl_stmt|;
DECL|field|patchType
specifier|private
specifier|final
name|PatchType
name|patchType
decl_stmt|;
DECL|field|oldName
specifier|private
specifier|final
name|String
name|oldName
decl_stmt|;
DECL|field|newName
specifier|private
specifier|final
name|String
name|newName
decl_stmt|;
DECL|field|header
specifier|private
specifier|final
name|byte
index|[]
name|header
decl_stmt|;
DECL|field|edits
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|Edit
argument_list|>
name|edits
decl_stmt|;
DECL|field|editsDueToRebase
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|Edit
argument_list|>
name|editsDueToRebase
decl_stmt|;
DECL|field|insertions
specifier|private
specifier|final
name|int
name|insertions
decl_stmt|;
DECL|field|deletions
specifier|private
specifier|final
name|int
name|deletions
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
name|long
name|size
decl_stmt|;
DECL|field|sizeDelta
specifier|private
specifier|final
name|long
name|sizeDelta
decl_stmt|;
comment|// Note: When adding new fields, the serialVersionUID in PatchListKey must be
comment|// incremented so that entries from the cache are automatically invalidated.
DECL|method|PatchListEntry ( FileHeader hdr, List<Edit> editList, Set<Edit> editsDueToRebase, long size, long sizeDelta)
name|PatchListEntry
parameter_list|(
name|FileHeader
name|hdr
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|editList
parameter_list|,
name|Set
argument_list|<
name|Edit
argument_list|>
name|editsDueToRebase
parameter_list|,
name|long
name|size
parameter_list|,
name|long
name|sizeDelta
parameter_list|)
block|{
name|changeType
operator|=
name|toChangeType
argument_list|(
name|hdr
argument_list|)
expr_stmt|;
name|patchType
operator|=
name|toPatchType
argument_list|(
name|hdr
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|changeType
condition|)
block|{
case|case
name|DELETED
case|:
name|oldName
operator|=
literal|null
expr_stmt|;
name|newName
operator|=
name|hdr
operator|.
name|getOldPath
argument_list|()
expr_stmt|;
break|break;
case|case
name|ADDED
case|:
case|case
name|MODIFIED
case|:
case|case
name|REWRITE
case|:
name|oldName
operator|=
literal|null
expr_stmt|;
name|newName
operator|=
name|hdr
operator|.
name|getNewPath
argument_list|()
expr_stmt|;
break|break;
case|case
name|COPIED
case|:
case|case
name|RENAMED
case|:
name|oldName
operator|=
name|hdr
operator|.
name|getOldPath
argument_list|()
expr_stmt|;
name|newName
operator|=
name|hdr
operator|.
name|getNewPath
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type "
operator|+
name|changeType
argument_list|)
throw|;
block|}
name|header
operator|=
name|compact
argument_list|(
name|hdr
argument_list|)
expr_stmt|;
if|if
condition|(
name|hdr
operator|instanceof
name|CombinedFileHeader
operator|||
name|hdr
operator|.
name|getHunks
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|edits
operator|=
name|ImmutableList
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|edits
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|editList
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|editsDueToRebase
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|editsDueToRebase
argument_list|)
expr_stmt|;
name|int
name|ins
init|=
literal|0
decl_stmt|;
name|int
name|del
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Edit
name|e
range|:
name|editList
control|)
block|{
if|if
condition|(
operator|!
name|editsDueToRebase
operator|.
name|contains
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|del
operator|+=
name|e
operator|.
name|getEndA
argument_list|()
operator|-
name|e
operator|.
name|getBeginA
argument_list|()
expr_stmt|;
name|ins
operator|+=
name|e
operator|.
name|getEndB
argument_list|()
operator|-
name|e
operator|.
name|getBeginB
argument_list|()
expr_stmt|;
block|}
block|}
name|insertions
operator|=
name|ins
expr_stmt|;
name|deletions
operator|=
name|del
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|sizeDelta
operator|=
name|sizeDelta
expr_stmt|;
block|}
DECL|method|PatchListEntry ( ChangeType changeType, PatchType patchType, String oldName, String newName, byte[] header, ImmutableList<Edit> edits, ImmutableSet<Edit> editsDueToRebase, int insertions, int deletions, long size, long sizeDelta)
specifier|private
name|PatchListEntry
parameter_list|(
name|ChangeType
name|changeType
parameter_list|,
name|PatchType
name|patchType
parameter_list|,
name|String
name|oldName
parameter_list|,
name|String
name|newName
parameter_list|,
name|byte
index|[]
name|header
parameter_list|,
name|ImmutableList
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|,
name|ImmutableSet
argument_list|<
name|Edit
argument_list|>
name|editsDueToRebase
parameter_list|,
name|int
name|insertions
parameter_list|,
name|int
name|deletions
parameter_list|,
name|long
name|size
parameter_list|,
name|long
name|sizeDelta
parameter_list|)
block|{
name|this
operator|.
name|changeType
operator|=
name|changeType
expr_stmt|;
name|this
operator|.
name|patchType
operator|=
name|patchType
expr_stmt|;
name|this
operator|.
name|oldName
operator|=
name|oldName
expr_stmt|;
name|this
operator|.
name|newName
operator|=
name|newName
expr_stmt|;
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
name|this
operator|.
name|edits
operator|=
name|edits
expr_stmt|;
name|this
operator|.
name|editsDueToRebase
operator|=
name|editsDueToRebase
expr_stmt|;
name|this
operator|.
name|insertions
operator|=
name|insertions
expr_stmt|;
name|this
operator|.
name|deletions
operator|=
name|deletions
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|sizeDelta
operator|=
name|sizeDelta
expr_stmt|;
block|}
DECL|method|weigh ()
name|int
name|weigh
parameter_list|()
block|{
name|int
name|size
init|=
literal|16
operator|+
literal|6
operator|*
literal|8
operator|+
literal|2
operator|*
literal|4
operator|+
literal|20
operator|+
literal|16
operator|+
literal|8
operator|+
literal|4
operator|+
literal|20
decl_stmt|;
name|size
operator|+=
name|stringSize
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|size
operator|+=
name|stringSize
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|size
operator|+=
name|header
operator|.
name|length
expr_stmt|;
name|size
operator|+=
operator|(
literal|8
operator|+
literal|16
operator|+
literal|4
operator|*
literal|4
operator|)
operator|*
name|edits
operator|.
name|size
argument_list|()
expr_stmt|;
name|size
operator|+=
operator|(
literal|8
operator|+
literal|16
operator|+
literal|4
operator|*
literal|4
operator|)
operator|*
name|editsDueToRebase
operator|.
name|size
argument_list|()
expr_stmt|;
return|return
name|size
return|;
block|}
DECL|method|stringSize (String str)
specifier|private
specifier|static
name|int
name|stringSize
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|!=
literal|null
condition|)
block|{
return|return
literal|16
operator|+
literal|3
operator|*
literal|4
operator|+
literal|16
operator|+
name|str
operator|.
name|length
argument_list|()
operator|*
literal|2
return|;
block|}
return|return
literal|0
return|;
block|}
DECL|method|getChangeType ()
specifier|public
name|ChangeType
name|getChangeType
parameter_list|()
block|{
return|return
name|changeType
return|;
block|}
DECL|method|getPatchType ()
specifier|public
name|PatchType
name|getPatchType
parameter_list|()
block|{
return|return
name|patchType
return|;
block|}
DECL|method|getOldName ()
specifier|public
name|String
name|getOldName
parameter_list|()
block|{
return|return
name|oldName
return|;
block|}
DECL|method|getNewName ()
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
name|newName
return|;
block|}
DECL|method|getEdits ()
specifier|public
name|ImmutableList
argument_list|<
name|Edit
argument_list|>
name|getEdits
parameter_list|()
block|{
comment|// Edits are mutable objects. As we serialize PatchListEntry asynchronously in H2CacheImpl, we
comment|// must ensure that its state isn't modified until it was properly stored in the cache.
return|return
name|deepCopyEdits
argument_list|(
name|edits
argument_list|)
return|;
block|}
DECL|method|getEditsDueToRebase ()
specifier|public
name|ImmutableSet
argument_list|<
name|Edit
argument_list|>
name|getEditsDueToRebase
parameter_list|()
block|{
return|return
name|deepCopyEdits
argument_list|(
name|editsDueToRebase
argument_list|)
return|;
block|}
DECL|method|getInsertions ()
specifier|public
name|int
name|getInsertions
parameter_list|()
block|{
return|return
name|insertions
return|;
block|}
DECL|method|getDeletions ()
specifier|public
name|int
name|getDeletions
parameter_list|()
block|{
return|return
name|deletions
return|;
block|}
DECL|method|getSize ()
specifier|public
name|long
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|getSizeDelta ()
specifier|public
name|long
name|getSizeDelta
parameter_list|()
block|{
return|return
name|sizeDelta
return|;
block|}
DECL|method|getHeaderLines ()
specifier|public
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|getHeaderLines
parameter_list|()
block|{
specifier|final
name|IntList
name|m
init|=
name|RawParseUtils
operator|.
name|lineMap
argument_list|(
name|header
argument_list|,
literal|0
argument_list|,
name|header
operator|.
name|length
argument_list|)
decl_stmt|;
specifier|final
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|headerLines
init|=
name|ImmutableList
operator|.
name|builderWithExpectedSize
argument_list|(
name|m
operator|.
name|size
argument_list|()
operator|-
literal|1
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
if|if
condition|(
name|header
index|[
name|e
operator|-
literal|1
index|]
operator|==
literal|'\n'
condition|)
block|{
name|e
operator|--
expr_stmt|;
block|}
name|headerLines
operator|.
name|add
argument_list|(
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|UTF_8
argument_list|,
name|header
argument_list|,
name|b
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|headerLines
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|toPatch (PatchSet.Id setId)
name|Patch
name|toPatch
parameter_list|(
name|PatchSet
operator|.
name|Id
name|setId
parameter_list|)
block|{
specifier|final
name|Patch
name|p
init|=
operator|new
name|Patch
argument_list|(
name|Patch
operator|.
name|key
argument_list|(
name|setId
argument_list|,
name|getNewName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|p
operator|.
name|setChangeType
argument_list|(
name|getChangeType
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setPatchType
argument_list|(
name|getPatchType
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setSourceFileName
argument_list|(
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setInsertions
argument_list|(
name|insertions
argument_list|)
expr_stmt|;
name|p
operator|.
name|setDeletions
argument_list|(
name|deletions
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
DECL|method|deepCopyEdits (List<Edit> edits)
specifier|private
specifier|static
name|ImmutableList
argument_list|<
name|Edit
argument_list|>
name|deepCopyEdits
parameter_list|(
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|)
block|{
return|return
name|edits
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|PatchListEntry
operator|::
name|copy
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|deepCopyEdits (Set<Edit> edits)
specifier|private
specifier|static
name|ImmutableSet
argument_list|<
name|Edit
argument_list|>
name|deepCopyEdits
parameter_list|(
name|Set
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|)
block|{
return|return
name|edits
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|PatchListEntry
operator|::
name|copy
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|copy (Edit edit)
specifier|private
specifier|static
name|Edit
name|copy
parameter_list|(
name|Edit
name|edit
parameter_list|)
block|{
return|return
operator|new
name|Edit
argument_list|(
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
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
return|;
block|}
DECL|method|writeTo (OutputStream out)
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeEnum
argument_list|(
name|out
argument_list|,
name|changeType
argument_list|)
expr_stmt|;
name|writeEnum
argument_list|(
name|out
argument_list|,
name|patchType
argument_list|)
expr_stmt|;
name|writeString
argument_list|(
name|out
argument_list|,
name|oldName
argument_list|)
expr_stmt|;
name|writeString
argument_list|(
name|out
argument_list|,
name|newName
argument_list|)
expr_stmt|;
name|writeBytes
argument_list|(
name|out
argument_list|,
name|header
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|insertions
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|deletions
argument_list|)
expr_stmt|;
name|writeFixInt64
argument_list|(
name|out
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|writeFixInt64
argument_list|(
name|out
argument_list|,
name|sizeDelta
argument_list|)
expr_stmt|;
name|writeEditArray
argument_list|(
name|out
argument_list|,
name|edits
argument_list|)
expr_stmt|;
name|writeEditArray
argument_list|(
name|out
argument_list|,
name|editsDueToRebase
argument_list|)
expr_stmt|;
block|}
DECL|method|writeEditArray (OutputStream out, Collection<Edit> edits)
specifier|private
specifier|static
name|void
name|writeEditArray
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|Collection
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|)
throws|throws
name|IOException
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|edits
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Edit
name|edit
range|:
name|edits
control|)
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|readFrom (InputStream in)
specifier|static
name|PatchListEntry
name|readFrom
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|ChangeType
name|changeType
init|=
name|readEnum
argument_list|(
name|in
argument_list|,
name|ChangeType
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|PatchType
name|patchType
init|=
name|readEnum
argument_list|(
name|in
argument_list|,
name|PatchType
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|oldName
init|=
name|readString
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|String
name|newName
init|=
name|readString
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|byte
index|[]
name|hdr
init|=
name|readBytes
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|int
name|ins
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|int
name|del
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|long
name|size
init|=
name|readFixInt64
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|long
name|sizeDelta
init|=
name|readFixInt64
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Edit
index|[]
name|editArray
init|=
name|readEditArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Edit
index|[]
name|editsDueToRebase
init|=
name|readEditArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
return|return
operator|new
name|PatchListEntry
argument_list|(
name|changeType
argument_list|,
name|patchType
argument_list|,
name|oldName
argument_list|,
name|newName
argument_list|,
name|hdr
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|editArray
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|editsDueToRebase
argument_list|)
argument_list|,
name|ins
argument_list|,
name|del
argument_list|,
name|size
argument_list|,
name|sizeDelta
argument_list|)
return|;
block|}
DECL|method|readEditArray (InputStream in)
specifier|private
specifier|static
name|Edit
index|[]
name|readEditArray
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|numEdits
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Edit
index|[]
name|edits
init|=
operator|new
name|Edit
index|[
name|numEdits
index|]
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
name|numEdits
condition|;
name|i
operator|++
control|)
block|{
name|int
name|beginA
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|int
name|endA
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|int
name|beginB
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|int
name|endB
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|edits
index|[
name|i
index|]
operator|=
operator|new
name|Edit
argument_list|(
name|beginA
argument_list|,
name|endA
argument_list|,
name|beginB
argument_list|,
name|endB
argument_list|)
expr_stmt|;
block|}
return|return
name|edits
return|;
block|}
DECL|method|compact (FileHeader h)
specifier|private
specifier|static
name|byte
index|[]
name|compact
parameter_list|(
name|FileHeader
name|h
parameter_list|)
block|{
specifier|final
name|int
name|end
init|=
name|end
argument_list|(
name|h
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|.
name|getStartOffset
argument_list|()
operator|==
literal|0
operator|&&
name|end
operator|==
name|h
operator|.
name|getBuffer
argument_list|()
operator|.
name|length
condition|)
block|{
return|return
name|h
operator|.
name|getBuffer
argument_list|()
return|;
block|}
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|end
operator|-
name|h
operator|.
name|getStartOffset
argument_list|()
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|h
operator|.
name|getBuffer
argument_list|()
argument_list|,
name|h
operator|.
name|getStartOffset
argument_list|()
argument_list|,
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
DECL|method|end (FileHeader h)
specifier|private
specifier|static
name|int
name|end
parameter_list|(
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
DECL|method|toChangeType (FileHeader hdr)
specifier|private
specifier|static
name|ChangeType
name|toChangeType
parameter_list|(
name|FileHeader
name|hdr
parameter_list|)
block|{
switch|switch
condition|(
name|hdr
operator|.
name|getChangeType
argument_list|()
condition|)
block|{
case|case
name|ADD
case|:
return|return
name|Patch
operator|.
name|ChangeType
operator|.
name|ADDED
return|;
case|case
name|MODIFY
case|:
return|return
name|Patch
operator|.
name|ChangeType
operator|.
name|MODIFIED
return|;
case|case
name|DELETE
case|:
return|return
name|Patch
operator|.
name|ChangeType
operator|.
name|DELETED
return|;
case|case
name|RENAME
case|:
return|return
name|Patch
operator|.
name|ChangeType
operator|.
name|RENAMED
return|;
case|case
name|COPY
case|:
return|return
name|Patch
operator|.
name|ChangeType
operator|.
name|COPIED
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type "
operator|+
name|hdr
operator|.
name|getChangeType
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|toPatchType (FileHeader hdr)
specifier|private
specifier|static
name|PatchType
name|toPatchType
parameter_list|(
name|FileHeader
name|hdr
parameter_list|)
block|{
name|PatchType
name|pt
decl_stmt|;
switch|switch
condition|(
name|hdr
operator|.
name|getPatchType
argument_list|()
condition|)
block|{
case|case
name|UNIFIED
case|:
name|pt
operator|=
name|Patch
operator|.
name|PatchType
operator|.
name|UNIFIED
expr_stmt|;
break|break;
case|case
name|GIT_BINARY
case|:
case|case
name|BINARY
case|:
name|pt
operator|=
name|Patch
operator|.
name|PatchType
operator|.
name|BINARY
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type "
operator|+
name|hdr
operator|.
name|getPatchType
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|pt
operator|!=
name|PatchType
operator|.
name|BINARY
condition|)
block|{
specifier|final
name|byte
index|[]
name|buf
init|=
name|hdr
operator|.
name|getBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|ptr
init|=
name|hdr
operator|.
name|getStartOffset
argument_list|()
init|;
name|ptr
operator|<
name|hdr
operator|.
name|getEndOffset
argument_list|()
condition|;
name|ptr
operator|++
control|)
block|{
if|if
condition|(
name|buf
index|[
name|ptr
index|]
operator|==
literal|'\0'
condition|)
block|{
comment|// Its really binary, but Git couldn't see the nul early enough
comment|// to realize its binary, and instead produced the diff.
comment|//
comment|// Force it to be a binary; it really should have been that.
comment|//
name|pt
operator|=
name|PatchType
operator|.
name|BINARY
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|pt
return|;
block|}
block|}
end_class

end_unit

