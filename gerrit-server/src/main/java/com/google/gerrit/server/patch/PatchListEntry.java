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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|reviewdb
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
name|reviewdb
operator|.
name|Patch
operator|.
name|PatchType
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
name|lib
operator|.
name|Constants
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
name|lib
operator|.
name|FileMode
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|List
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
DECL|method|empty (final String fileName)
specifier|static
name|PatchListEntry
name|empty
parameter_list|(
specifier|final
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
name|Collections
operator|.
expr|<
name|Edit
operator|>
name|emptyList
argument_list|()
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
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
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
DECL|method|PatchListEntry (final FileHeader hdr, List<Edit> editList)
name|PatchListEntry
parameter_list|(
specifier|final
name|FileHeader
name|hdr
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|editList
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
comment|//
operator|||
name|hdr
operator|.
name|getOldMode
argument_list|()
operator|==
name|FileMode
operator|.
name|GITLINK
operator|||
name|hdr
operator|.
name|getNewMode
argument_list|()
operator|==
name|FileMode
operator|.
name|GITLINK
condition|)
block|{
name|edits
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|edits
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|editList
argument_list|)
expr_stmt|;
block|}
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
name|insertions
operator|=
name|ins
expr_stmt|;
name|deletions
operator|=
name|del
expr_stmt|;
block|}
DECL|method|PatchListEntry (final ChangeType changeType, final PatchType patchType, final String oldName, final String newName, final byte[] header, final List<Edit> edits, final int insertions, final int deletions)
specifier|private
name|PatchListEntry
parameter_list|(
specifier|final
name|ChangeType
name|changeType
parameter_list|,
specifier|final
name|PatchType
name|patchType
parameter_list|,
specifier|final
name|String
name|oldName
parameter_list|,
specifier|final
name|String
name|newName
parameter_list|,
specifier|final
name|byte
index|[]
name|header
parameter_list|,
specifier|final
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|,
specifier|final
name|int
name|insertions
parameter_list|,
specifier|final
name|int
name|deletions
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
name|List
argument_list|<
name|Edit
argument_list|>
name|getEdits
parameter_list|()
block|{
return|return
name|edits
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
DECL|method|getHeaderLines ()
specifier|public
name|List
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
name|List
argument_list|<
name|String
argument_list|>
name|headerLines
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
name|headerLines
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
return|;
block|}
DECL|method|toPatch (final PatchSet.Id setId)
name|Patch
name|toPatch
parameter_list|(
specifier|final
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
operator|new
name|Patch
operator|.
name|Key
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
DECL|method|writeTo (final OutputStream out)
name|void
name|writeTo
parameter_list|(
specifier|final
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
specifier|final
name|Edit
name|e
range|:
name|edits
control|)
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|e
operator|.
name|getBeginA
argument_list|()
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|e
operator|.
name|getEndA
argument_list|()
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|e
operator|.
name|getBeginB
argument_list|()
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|e
operator|.
name|getEndB
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|readFrom (final InputStream in)
specifier|static
name|PatchListEntry
name|readFrom
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
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
specifier|final
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
specifier|final
name|String
name|oldName
init|=
name|readString
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|newName
init|=
name|readString
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|hdr
init|=
name|readBytes
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|ins
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|del
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|editCount
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|Edit
index|[]
name|editArray
init|=
operator|new
name|Edit
index|[
name|editCount
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
name|editCount
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
name|editArray
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
name|toList
argument_list|(
name|editArray
argument_list|)
argument_list|,
name|ins
argument_list|,
name|del
argument_list|)
return|;
block|}
DECL|method|toList (Edit[] l)
specifier|private
specifier|static
name|List
argument_list|<
name|Edit
argument_list|>
name|toList
parameter_list|(
name|Edit
index|[]
name|l
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|l
argument_list|)
argument_list|)
return|;
block|}
DECL|method|compact (final FileHeader h)
specifier|private
specifier|static
name|byte
index|[]
name|compact
parameter_list|(
specifier|final
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
DECL|method|toChangeType (final FileHeader hdr)
specifier|private
specifier|static
name|ChangeType
name|toChangeType
parameter_list|(
specifier|final
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
DECL|method|toPatchType (final FileHeader hdr)
specifier|private
specifier|static
name|PatchType
name|toPatchType
parameter_list|(
specifier|final
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

