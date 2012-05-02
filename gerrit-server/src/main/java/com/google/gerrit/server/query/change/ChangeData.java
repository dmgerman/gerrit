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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
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
name|ChangeMessage
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
name|PatchSetApproval
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
name|TrackingId
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
name|server
operator|.
name|ReviewDb
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
name|CurrentUser
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
name|git
operator|.
name|GitRepositoryManager
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
name|patch
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
name|patch
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
name|patch
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
name|project
operator|.
name|ChangeControl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|Provider
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
name|Collection
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
name|HashMap
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
name|Map
import|;
end_import

begin_class
DECL|class|ChangeData
specifier|public
class|class
name|ChangeData
block|{
DECL|method|ensureChangeLoaded ( Provider<ReviewDb> db, List<ChangeData> changes)
specifier|public
specifier|static
name|void
name|ensureChangeLoaded
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|)
throws|throws
name|OrmException
block|{
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|missing
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changes
control|)
block|{
if|if
condition|(
name|cd
operator|.
name|change
operator|==
literal|null
condition|)
block|{
name|missing
operator|.
name|put
argument_list|(
name|cd
operator|.
name|getId
argument_list|()
argument_list|,
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Change
name|change
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|missing
operator|.
name|keySet
argument_list|()
argument_list|)
control|)
block|{
name|missing
operator|.
name|get
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|change
operator|=
name|change
expr_stmt|;
block|}
block|}
block|}
DECL|method|ensureCurrentPatchSetLoaded ( Provider<ReviewDb> db, List<ChangeData> changes)
specifier|public
specifier|static
name|void
name|ensureCurrentPatchSetLoaded
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|)
throws|throws
name|OrmException
block|{
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|missing
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changes
control|)
block|{
if|if
condition|(
name|cd
operator|.
name|currentPatchSet
operator|==
literal|null
operator|&&
name|cd
operator|.
name|patches
operator|==
literal|null
condition|)
block|{
name|missing
operator|.
name|put
argument_list|(
name|cd
operator|.
name|change
argument_list|(
name|db
argument_list|)
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|PatchSet
name|ps
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|missing
operator|.
name|keySet
argument_list|()
argument_list|)
control|)
block|{
name|ChangeData
name|cd
init|=
name|missing
operator|.
name|get
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|cd
operator|.
name|currentPatchSet
operator|=
name|ps
expr_stmt|;
name|cd
operator|.
name|patches
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|field|legacyId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|legacyId
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|commitMessage
specifier|private
name|String
name|commitMessage
decl_stmt|;
DECL|field|currentPatchSet
specifier|private
name|PatchSet
name|currentPatchSet
decl_stmt|;
DECL|field|patches
specifier|private
name|Collection
argument_list|<
name|PatchSet
argument_list|>
name|patches
decl_stmt|;
DECL|field|approvals
specifier|private
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
decl_stmt|;
DECL|field|approvalsMap
specifier|private
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
argument_list|>
name|approvalsMap
decl_stmt|;
DECL|field|currentApprovals
specifier|private
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|currentApprovals
decl_stmt|;
DECL|field|currentFiles
specifier|private
name|String
index|[]
name|currentFiles
decl_stmt|;
DECL|field|comments
specifier|private
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
decl_stmt|;
DECL|field|trackingIds
specifier|private
name|Collection
argument_list|<
name|TrackingId
argument_list|>
name|trackingIds
decl_stmt|;
DECL|field|visibleTo
specifier|private
name|CurrentUser
name|visibleTo
decl_stmt|;
DECL|field|changeControl
specifier|private
name|ChangeControl
name|changeControl
decl_stmt|;
DECL|field|messages
specifier|private
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|messages
decl_stmt|;
DECL|method|ChangeData (final Change.Id id)
specifier|public
name|ChangeData
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
name|legacyId
operator|=
name|id
expr_stmt|;
block|}
DECL|method|ChangeData (final Change c)
specifier|public
name|ChangeData
parameter_list|(
specifier|final
name|Change
name|c
parameter_list|)
block|{
name|legacyId
operator|=
name|c
operator|.
name|getId
argument_list|()
expr_stmt|;
name|change
operator|=
name|c
expr_stmt|;
block|}
DECL|method|setCurrentFilePaths (String[] filePaths)
specifier|public
name|void
name|setCurrentFilePaths
parameter_list|(
name|String
index|[]
name|filePaths
parameter_list|)
block|{
name|currentFiles
operator|=
name|filePaths
expr_stmt|;
block|}
DECL|method|currentFilePaths (Provider<ReviewDb> db, PatchListCache cache)
specifier|public
name|String
index|[]
name|currentFilePaths
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|PatchListCache
name|cache
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|currentFiles
operator|==
literal|null
condition|)
block|{
name|Change
name|c
init|=
name|change
argument_list|(
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PatchSet
name|ps
init|=
name|currentPatchSet
argument_list|(
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|ps
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PatchList
name|p
init|=
name|cache
operator|.
name|get
argument_list|(
name|c
argument_list|,
name|ps
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|p
operator|.
name|getPatches
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PatchListEntry
name|e
range|:
name|p
operator|.
name|getPatches
argument_list|()
control|)
block|{
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
switch|switch
condition|(
name|e
operator|.
name|getChangeType
argument_list|()
condition|)
block|{
case|case
name|ADDED
case|:
case|case
name|MODIFIED
case|:
case|case
name|DELETED
case|:
case|case
name|COPIED
case|:
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|RENAMED
case|:
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|currentFiles
operator|=
name|r
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|r
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|currentFiles
argument_list|)
expr_stmt|;
block|}
return|return
name|currentFiles
return|;
block|}
DECL|method|getId ()
specifier|public
name|Change
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|legacyId
return|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
return|;
block|}
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
return|return
name|change
operator|!=
literal|null
return|;
block|}
DECL|method|fastIsVisibleTo (CurrentUser user)
name|boolean
name|fastIsVisibleTo
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
name|visibleTo
operator|==
name|user
return|;
block|}
DECL|method|changeControl ()
name|ChangeControl
name|changeControl
parameter_list|()
block|{
return|return
name|changeControl
return|;
block|}
DECL|method|cacheVisibleTo (ChangeControl ctl)
name|void
name|cacheVisibleTo
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|)
block|{
name|visibleTo
operator|=
name|ctl
operator|.
name|getCurrentUser
argument_list|()
expr_stmt|;
name|changeControl
operator|=
name|ctl
expr_stmt|;
block|}
DECL|method|change (Provider<ReviewDb> db)
specifier|public
name|Change
name|change
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
name|change
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|legacyId
argument_list|)
expr_stmt|;
block|}
return|return
name|change
return|;
block|}
DECL|method|currentPatchSet (Provider<ReviewDb> db)
specifier|public
name|PatchSet
name|currentPatchSet
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|currentPatchSet
operator|==
literal|null
condition|)
block|{
name|Change
name|c
init|=
name|change
argument_list|(
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|PatchSet
name|p
range|:
name|patches
argument_list|(
name|db
argument_list|)
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
condition|)
block|{
name|currentPatchSet
operator|=
name|p
expr_stmt|;
return|return
name|p
return|;
block|}
block|}
block|}
return|return
name|currentPatchSet
return|;
block|}
DECL|method|currentApprovals (Provider<ReviewDb> db)
specifier|public
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|currentApprovals
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|currentApprovals
operator|==
literal|null
condition|)
block|{
name|Change
name|c
init|=
name|change
argument_list|(
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|currentApprovals
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|approvals
operator|!=
literal|null
condition|)
block|{
name|currentApprovals
operator|=
name|approvalsMap
argument_list|(
name|db
argument_list|)
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentApprovals
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byPatchSet
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|currentApprovals
return|;
block|}
DECL|method|commitMessage (GitRepositoryManager repoManager, Provider<ReviewDb> db)
specifier|public
name|String
name|commitMessage
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
if|if
condition|(
name|commitMessage
operator|==
literal|null
condition|)
block|{
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|change
argument_list|(
name|db
argument_list|)
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|String
name|sha1
init|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|name
init|=
name|change
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|name
argument_list|)
decl_stmt|;
try|try
block|{
name|RevWalk
name|walk
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|RevCommit
name|c
init|=
name|walk
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|sha1
argument_list|)
argument_list|)
decl_stmt|;
name|commitMessage
operator|=
name|c
operator|.
name|getFullMessage
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|walk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|commitMessage
return|;
block|}
DECL|method|patches (Provider<ReviewDb> db)
specifier|public
name|Collection
argument_list|<
name|PatchSet
argument_list|>
name|patches
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|patches
operator|==
literal|null
condition|)
block|{
name|patches
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSets
argument_list|()
operator|.
name|byChange
argument_list|(
name|legacyId
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
return|return
name|patches
return|;
block|}
DECL|method|approvals (Provider<ReviewDb> db)
specifier|public
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|approvals
operator|==
literal|null
condition|)
block|{
name|approvals
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|legacyId
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
return|return
name|approvals
return|;
block|}
DECL|method|approvalsMap (Provider<ReviewDb> db)
specifier|public
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
argument_list|>
name|approvalsMap
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|approvalsMap
operator|==
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|all
init|=
name|approvals
argument_list|(
name|db
argument_list|)
decl_stmt|;
name|approvalsMap
operator|=
operator|new
name|HashMap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
argument_list|>
argument_list|(
name|all
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|all
control|)
block|{
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|c
init|=
name|approvalsMap
operator|.
name|get
argument_list|(
name|psa
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|c
operator|=
operator|new
name|ArrayList
argument_list|<
name|PatchSetApproval
argument_list|>
argument_list|()
expr_stmt|;
name|approvalsMap
operator|.
name|put
argument_list|(
name|psa
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|add
argument_list|(
name|psa
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|approvalsMap
return|;
block|}
DECL|method|comments (Provider<ReviewDb> db)
specifier|public
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|comments
operator|==
literal|null
condition|)
block|{
name|comments
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchComments
argument_list|()
operator|.
name|byChange
argument_list|(
name|legacyId
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
return|return
name|comments
return|;
block|}
DECL|method|trackingIds (Provider<ReviewDb> db)
specifier|public
name|Collection
argument_list|<
name|TrackingId
argument_list|>
name|trackingIds
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|trackingIds
operator|==
literal|null
condition|)
block|{
name|trackingIds
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|trackingIds
argument_list|()
operator|.
name|byChange
argument_list|(
name|legacyId
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
return|return
name|trackingIds
return|;
block|}
DECL|method|messages (Provider<ReviewDb> db)
specifier|public
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|messages
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|messages
operator|==
literal|null
condition|)
block|{
name|messages
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|changeMessages
argument_list|()
operator|.
name|byChange
argument_list|(
name|legacyId
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
return|return
name|messages
return|;
block|}
block|}
end_class

end_unit

