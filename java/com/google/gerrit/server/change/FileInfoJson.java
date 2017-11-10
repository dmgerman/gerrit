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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
operator|.
name|Whitespace
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
name|FileInfo
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
name|Change
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
name|server
operator|.
name|diff
operator|.
name|PatchList
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
name|diff
operator|.
name|PatchListCache
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
name|diff
operator|.
name|PatchListEntry
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
name|diff
operator|.
name|PatchListKey
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
name|diff
operator|.
name|PatchListNotAvailableException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|TreeMap
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

begin_class
annotation|@
name|Singleton
DECL|class|FileInfoJson
specifier|public
class|class
name|FileInfoJson
block|{
DECL|field|patchListCache
specifier|private
specifier|final
name|PatchListCache
name|patchListCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|FileInfoJson (PatchListCache patchListCache)
name|FileInfoJson
parameter_list|(
name|PatchListCache
name|patchListCache
parameter_list|)
block|{
name|this
operator|.
name|patchListCache
operator|=
name|patchListCache
expr_stmt|;
block|}
DECL|method|toFileInfoMap (Change change, PatchSet patchSet)
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|toFileInfoMap
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|)
throws|throws
name|PatchListNotAvailableException
block|{
return|return
name|toFileInfoMap
argument_list|(
name|change
argument_list|,
name|patchSet
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|toFileInfoMap (Change change, RevId revision, @Nullable PatchSet base)
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|toFileInfoMap
parameter_list|(
name|Change
name|change
parameter_list|,
name|RevId
name|revision
parameter_list|,
annotation|@
name|Nullable
name|PatchSet
name|base
parameter_list|)
throws|throws
name|PatchListNotAvailableException
block|{
name|ObjectId
name|objectId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|revision
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|toFileInfoMap
argument_list|(
name|change
argument_list|,
name|objectId
argument_list|,
name|base
argument_list|)
return|;
block|}
DECL|method|toFileInfoMap (Change change, ObjectId objectId, @Nullable PatchSet base)
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|toFileInfoMap
parameter_list|(
name|Change
name|change
parameter_list|,
name|ObjectId
name|objectId
parameter_list|,
annotation|@
name|Nullable
name|PatchSet
name|base
parameter_list|)
throws|throws
name|PatchListNotAvailableException
block|{
name|ObjectId
name|a
init|=
operator|(
name|base
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|ObjectId
operator|.
name|fromString
argument_list|(
name|base
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|toFileInfoMap
argument_list|(
name|change
argument_list|,
name|PatchListKey
operator|.
name|againstCommit
argument_list|(
name|a
argument_list|,
name|objectId
argument_list|,
name|Whitespace
operator|.
name|IGNORE_NONE
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toFileInfoMap (Change change, RevId revision, int parent)
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|toFileInfoMap
parameter_list|(
name|Change
name|change
parameter_list|,
name|RevId
name|revision
parameter_list|,
name|int
name|parent
parameter_list|)
throws|throws
name|PatchListNotAvailableException
block|{
name|ObjectId
name|b
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|revision
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|toFileInfoMap
argument_list|(
name|change
argument_list|,
name|PatchListKey
operator|.
name|againstParentNum
argument_list|(
name|parent
operator|+
literal|1
argument_list|,
name|b
argument_list|,
name|Whitespace
operator|.
name|IGNORE_NONE
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toFileInfoMap (Change change, PatchListKey key)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|toFileInfoMap
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchListKey
name|key
parameter_list|)
throws|throws
name|PatchListNotAvailableException
block|{
name|PatchList
name|list
init|=
name|patchListCache
operator|.
name|get
argument_list|(
name|key
argument_list|,
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|files
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchListEntry
name|e
range|:
name|list
operator|.
name|getPatches
argument_list|()
control|)
block|{
name|FileInfo
name|d
init|=
operator|new
name|FileInfo
argument_list|()
decl_stmt|;
name|d
operator|.
name|status
operator|=
name|e
operator|.
name|getChangeType
argument_list|()
operator|!=
name|Patch
operator|.
name|ChangeType
operator|.
name|MODIFIED
condition|?
name|e
operator|.
name|getChangeType
argument_list|()
operator|.
name|getCode
argument_list|()
else|:
literal|null
expr_stmt|;
name|d
operator|.
name|oldPath
operator|=
name|e
operator|.
name|getOldName
argument_list|()
expr_stmt|;
name|d
operator|.
name|sizeDelta
operator|=
name|e
operator|.
name|getSizeDelta
argument_list|()
expr_stmt|;
name|d
operator|.
name|size
operator|=
name|e
operator|.
name|getSize
argument_list|()
expr_stmt|;
if|if
condition|(
name|e
operator|.
name|getPatchType
argument_list|()
operator|==
name|Patch
operator|.
name|PatchType
operator|.
name|BINARY
condition|)
block|{
name|d
operator|.
name|binary
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|d
operator|.
name|linesInserted
operator|=
name|e
operator|.
name|getInsertions
argument_list|()
operator|>
literal|0
condition|?
name|e
operator|.
name|getInsertions
argument_list|()
else|:
literal|null
expr_stmt|;
name|d
operator|.
name|linesDeleted
operator|=
name|e
operator|.
name|getDeletions
argument_list|()
operator|>
literal|0
condition|?
name|e
operator|.
name|getDeletions
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
name|FileInfo
name|o
init|=
name|files
operator|.
name|put
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|,
name|d
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
comment|// This should only happen on a delete-add break created by JGit
comment|// when the file was rewritten and too little content survived. Write
comment|// a single record with data from both sides.
name|d
operator|.
name|status
operator|=
name|Patch
operator|.
name|ChangeType
operator|.
name|REWRITE
operator|.
name|getCode
argument_list|()
expr_stmt|;
name|d
operator|.
name|sizeDelta
operator|=
name|o
operator|.
name|sizeDelta
expr_stmt|;
name|d
operator|.
name|size
operator|=
name|o
operator|.
name|size
expr_stmt|;
if|if
condition|(
name|o
operator|.
name|binary
operator|!=
literal|null
operator|&&
name|o
operator|.
name|binary
condition|)
block|{
name|d
operator|.
name|binary
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|.
name|linesInserted
operator|!=
literal|null
condition|)
block|{
name|d
operator|.
name|linesInserted
operator|=
name|o
operator|.
name|linesInserted
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|.
name|linesDeleted
operator|!=
literal|null
condition|)
block|{
name|d
operator|.
name|linesDeleted
operator|=
name|o
operator|.
name|linesDeleted
expr_stmt|;
block|}
block|}
block|}
return|return
name|files
return|;
block|}
block|}
end_class

end_unit

