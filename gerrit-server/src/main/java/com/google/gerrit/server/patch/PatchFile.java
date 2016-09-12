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
name|gerrit
operator|.
name|common
operator|.
name|errors
operator|.
name|NoSuchEntityException
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
name|Patch
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
name|errors
operator|.
name|CorruptObjectException
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
name|errors
operator|.
name|IncorrectObjectTypeException
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
name|errors
operator|.
name|MissingObjectException
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
name|ObjectId
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
name|ObjectReader
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
name|Repository
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevObject
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
name|revwalk
operator|.
name|RevTree
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
name|revwalk
operator|.
name|RevWalk
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
name|treewalk
operator|.
name|TreeWalk
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
DECL|field|entry
specifier|private
specifier|final
name|PatchListEntry
name|entry
decl_stmt|;
DECL|field|aTree
specifier|private
specifier|final
name|RevTree
name|aTree
decl_stmt|;
DECL|field|bTree
specifier|private
specifier|final
name|RevTree
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
DECL|method|PatchFile (final Repository repo, final PatchList patchList, final String fileName)
specifier|public
name|PatchFile
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|PatchList
name|patchList
parameter_list|,
specifier|final
name|String
name|fileName
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
name|entry
operator|=
name|patchList
operator|.
name|get
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
try|try
init|(
name|ObjectReader
name|reader
init|=
name|repo
operator|.
name|newObjectReader
argument_list|()
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
specifier|final
name|RevCommit
name|bCommit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|patchList
operator|.
name|getNewId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|fileName
argument_list|)
condition|)
block|{
if|if
condition|(
name|patchList
operator|.
name|getComparisonType
argument_list|()
operator|.
name|isAgainstParentOrAutoMerge
argument_list|()
condition|)
block|{
name|a
operator|=
name|Text
operator|.
name|EMPTY
expr_stmt|;
block|}
else|else
block|{
comment|// For the initial commit, we have an empty tree on Side A
name|RevObject
name|object
init|=
name|rw
operator|.
name|parseAny
argument_list|(
name|patchList
operator|.
name|getOldId
argument_list|()
argument_list|)
decl_stmt|;
name|a
operator|=
name|object
operator|instanceof
name|RevCommit
condition|?
name|Text
operator|.
name|forCommit
argument_list|(
name|reader
argument_list|,
name|object
argument_list|)
else|:
name|Text
operator|.
name|EMPTY
expr_stmt|;
block|}
name|b
operator|=
name|Text
operator|.
name|forCommit
argument_list|(
name|reader
argument_list|,
name|bCommit
argument_list|)
expr_stmt|;
name|aTree
operator|=
literal|null
expr_stmt|;
name|bTree
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|patchList
operator|.
name|getOldId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|aTree
operator|=
name|rw
operator|.
name|parseTree
argument_list|(
name|patchList
operator|.
name|getOldId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|RevCommit
name|p
init|=
name|bCommit
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|rw
operator|.
name|parseHeaders
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|aTree
operator|=
name|p
operator|.
name|getTree
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
block|}
block|}
comment|/**    * Extract a line from the file, as a string.    *    * @param file the file index to extract.    * @param line the line number to extract (1 based; 1 is the first line).    * @return the string version of the file line.    * @throws IOException the patch or complete file content cannot be read.    * @throws NoSuchEntityException    */
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
name|a
operator|=
name|load
argument_list|(
name|aTree
argument_list|,
name|entry
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|a
operator|.
name|getString
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
name|entry
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|getString
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
comment|/**    * Return number of lines in file.    *    * @param file the file index to extract.    * @return number of lines in file.    * @throws IOException the patch or complete file content cannot be read.    * @throws NoSuchEntityException the file is not exist.    */
DECL|method|getLineCount (final int file)
specifier|public
name|int
name|getLineCount
parameter_list|(
specifier|final
name|int
name|file
parameter_list|)
throws|throws
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
name|a
operator|=
name|load
argument_list|(
name|aTree
argument_list|,
name|entry
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|a
operator|.
name|size
argument_list|()
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
name|entry
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|size
argument_list|()
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
if|if
condition|(
name|path
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
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|repo
argument_list|,
name|path
argument_list|,
name|tree
argument_list|)
decl_stmt|;
if|if
condition|(
name|tw
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
if|if
condition|(
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectType
argument_list|()
operator|==
name|Constants
operator|.
name|OBJ_BLOB
condition|)
block|{
return|return
operator|new
name|Text
argument_list|(
name|repo
operator|.
name|open
argument_list|(
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|,
name|Constants
operator|.
name|OBJ_BLOB
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectType
argument_list|()
operator|==
name|Constants
operator|.
name|OBJ_COMMIT
condition|)
block|{
name|String
name|str
init|=
literal|"Subproject commit "
operator|+
name|ObjectId
operator|.
name|toString
argument_list|(
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|Text
argument_list|(
name|str
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Text
operator|.
name|EMPTY
return|;
block|}
block|}
block|}
end_class

end_unit

