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
name|RevId
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
name|client
operator|.
name|rpc
operator|.
name|NoSuchEntityException
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
name|ObjectWriter
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
name|revwalk
operator|.
name|RevCommit
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
name|nio
operator|.
name|charset
operator|.
name|CharacterCodingException
import|;
end_import

begin_comment
comment|/** State supporting processing of a single {@link Patch} instance. */
end_comment

begin_class
DECL|class|PatchFile
specifier|public
class|class
name|PatchFile
block|{
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|patch
specifier|private
specifier|final
name|Patch
name|patch
decl_stmt|;
DECL|field|aTree
specifier|private
specifier|final
name|ObjectId
name|aTree
decl_stmt|;
DECL|field|bTree
specifier|private
specifier|final
name|ObjectId
name|bTree
decl_stmt|;
DECL|field|a
specifier|private
name|Text
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|Text
name|b
decl_stmt|;
DECL|method|PatchFile (final Repository repo, final RevId id, final Patch patch)
specifier|public
name|PatchFile
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|RevId
name|id
parameter_list|,
specifier|final
name|Patch
name|patch
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
block|{
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|patch
operator|=
name|patch
expr_stmt|;
specifier|final
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
specifier|final
name|RevCommit
name|bCommit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|bCommit
operator|.
name|getParentCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|rw
operator|.
name|parse
argument_list|(
name|bCommit
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|aTree
operator|=
name|bCommit
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
operator|.
name|getTree
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|aTree
operator|=
name|emptyTree
argument_list|()
expr_stmt|;
block|}
name|bTree
operator|=
name|bCommit
operator|.
name|getTree
argument_list|()
expr_stmt|;
block|}
comment|/**    * Extract a line from the file, as a string.    *     * @param file the file index to extract.    * @param line the line number to extract (1 based; 1 is the first line).    * @return the string version of the file line.    * @throws CorruptEntityException the patch cannot be read.    * @throws IOException the patch or complete file content cannot be read.    * @throws NoSuchEntityException    * @throws CharacterCodingException the file is not a known character set.    */
DECL|method|getLine (final int file, final int line)
specifier|public
name|String
name|getLine
parameter_list|(
specifier|final
name|int
name|file
parameter_list|,
specifier|final
name|int
name|line
parameter_list|)
throws|throws
name|CorruptEntityException
throws|,
name|IOException
throws|,
name|NoSuchEntityException
block|{
switch|switch
condition|(
name|file
condition|)
block|{
case|case
literal|0
case|:
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
name|String
name|p
init|=
name|patch
operator|.
name|getSourceFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|p
operator|=
name|patch
operator|.
name|getFileName
argument_list|()
expr_stmt|;
block|}
name|a
operator|=
name|load
argument_list|(
name|aTree
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
return|return
name|a
operator|.
name|getLine
argument_list|(
name|line
operator|-
literal|1
argument_list|)
return|;
case|case
literal|1
case|:
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|b
operator|=
name|load
argument_list|(
name|bTree
argument_list|,
name|patch
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|getLine
argument_list|(
name|line
operator|-
literal|1
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|NoSuchEntityException
argument_list|()
throw|;
block|}
block|}
DECL|method|load (final ObjectId tree, final String path)
specifier|private
name|Text
name|load
parameter_list|(
specifier|final
name|ObjectId
name|tree
parameter_list|,
specifier|final
name|String
name|path
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
specifier|final
name|ObjectId
name|id
init|=
name|DiffCacheContent
operator|.
name|find
argument_list|(
name|repo
argument_list|,
name|tree
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
name|Text
operator|.
name|EMPTY
return|;
block|}
specifier|final
name|ObjectLoader
name|ldr
init|=
name|repo
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
return|return
operator|new
name|Text
argument_list|(
name|ldr
operator|.
name|getCachedBytes
argument_list|()
argument_list|)
return|;
block|}
DECL|method|emptyTree ()
specifier|private
name|ObjectId
name|emptyTree
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ObjectWriter
argument_list|(
name|repo
argument_list|)
operator|.
name|writeCanonicalTree
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

