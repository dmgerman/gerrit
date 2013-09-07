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
name|base
operator|.
name|Function
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
name|ArrayListMultimap
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
name|ListMultimap
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
name|collect
operator|.
name|Ordering
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
name|Sets
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
name|SubmitRecord
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
name|patch
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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
name|sql
operator|.
name|Timestamp
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|ChangeData
specifier|public
class|class
name|ChangeData
block|{
DECL|field|SORT_APPROVALS
specifier|private
specifier|static
name|Ordering
argument_list|<
name|PatchSetApproval
argument_list|>
name|SORT_APPROVALS
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|onResultOf
argument_list|(
operator|new
name|Function
argument_list|<
name|PatchSetApproval
argument_list|,
name|Timestamp
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Timestamp
name|apply
parameter_list|(
name|PatchSetApproval
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|getGranted
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
DECL|method|sortApprovals ( Iterable<PatchSetApproval> approvals)
specifier|public
specifier|static
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|sortApprovals
parameter_list|(
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
parameter_list|)
block|{
return|return
name|SORT_APPROVALS
operator|.
name|sortedCopy
argument_list|(
name|approvals
argument_list|)
return|;
block|}
DECL|method|ensureChangeLoaded (Provider<ReviewDb> db, Iterable<ChangeData> changes)
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
name|Iterable
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
DECL|method|ensureAllPatchSetsLoaded (Provider<ReviewDb> db, Iterable<ChangeData> changes)
specifier|public
specifier|static
name|void
name|ensureAllPatchSetsLoaded
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|ChangeData
name|cd
range|:
name|changes
control|)
block|{
name|cd
operator|.
name|patches
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ensureCurrentPatchSetLoaded (Provider<ReviewDb> db, Iterable<ChangeData> changes)
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
name|Iterable
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
if|if
condition|(
name|cd
operator|.
name|limitedIds
operator|==
literal|null
condition|)
block|{
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
block|}
DECL|method|ensureCurrentApprovalsLoaded (Provider<ReviewDb> db, Iterable<ChangeData> changes)
specifier|public
specifier|static
name|void
name|ensureCurrentApprovalsLoaded
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|)
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
argument_list|>
name|pending
init|=
name|Lists
operator|.
name|newArrayList
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
name|currentApprovals
operator|==
literal|null
operator|&&
name|cd
operator|.
name|limitedApprovals
operator|==
literal|null
condition|)
block|{
name|pending
operator|.
name|add
argument_list|(
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
name|cd
operator|.
name|change
argument_list|(
name|db
argument_list|)
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|pending
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|int
name|idx
init|=
literal|0
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
name|currentApprovals
operator|==
literal|null
operator|&&
name|cd
operator|.
name|limitedApprovals
operator|==
literal|null
condition|)
block|{
name|cd
operator|.
name|currentApprovals
operator|=
name|sortApprovals
argument_list|(
name|pending
operator|.
name|get
argument_list|(
name|idx
operator|++
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|field|returnedBySource
specifier|private
name|ChangeDataSource
name|returnedBySource
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
DECL|field|limitedIds
specifier|private
name|Set
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|limitedIds
decl_stmt|;
DECL|field|patches
specifier|private
name|Collection
argument_list|<
name|PatchSet
argument_list|>
name|patches
decl_stmt|;
DECL|field|limitedApprovals
specifier|private
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|limitedApprovals
decl_stmt|;
DECL|field|allApprovals
specifier|private
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|allApprovals
decl_stmt|;
DECL|field|currentApprovals
specifier|private
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|currentApprovals
decl_stmt|;
DECL|field|currentFiles
specifier|private
name|List
argument_list|<
name|String
argument_list|>
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
DECL|field|submitRecords
specifier|private
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
decl_stmt|;
DECL|field|patchesLoaded
specifier|private
name|boolean
name|patchesLoaded
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
DECL|method|ChangeData (final ChangeControl c)
specifier|public
name|ChangeData
parameter_list|(
specifier|final
name|ChangeControl
name|c
parameter_list|)
block|{
name|legacyId
operator|=
name|c
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
name|change
operator|=
name|c
operator|.
name|getChange
argument_list|()
expr_stmt|;
name|changeControl
operator|=
name|c
expr_stmt|;
block|}
DECL|method|isFromSource (ChangeDataSource s)
specifier|public
name|boolean
name|isFromSource
parameter_list|(
name|ChangeDataSource
name|s
parameter_list|)
block|{
return|return
name|s
operator|==
name|returnedBySource
return|;
block|}
DECL|method|cacheFromSource (ChangeDataSource s)
specifier|public
name|void
name|cacheFromSource
parameter_list|(
name|ChangeDataSource
name|s
parameter_list|)
block|{
name|returnedBySource
operator|=
name|s
expr_stmt|;
block|}
DECL|method|limitToPatchSets (Collection<PatchSet.Id> ids)
specifier|public
name|void
name|limitToPatchSets
parameter_list|(
name|Collection
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|ids
parameter_list|)
block|{
name|limitedIds
operator|=
name|Sets
operator|.
name|newLinkedHashSetWithExpectedSize
argument_list|(
name|ids
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PatchSet
operator|.
name|Id
name|id
range|:
name|ids
control|)
block|{
if|if
condition|(
operator|!
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|equals
argument_list|(
name|legacyId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"invalid patch set %s for change %s"
argument_list|,
name|id
argument_list|,
name|legacyId
argument_list|)
argument_list|)
throw|;
block|}
name|limitedIds
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getLimitedPatchSets ()
specifier|public
name|Collection
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|getLimitedPatchSets
parameter_list|()
block|{
return|return
name|limitedIds
return|;
block|}
DECL|method|setCurrentFilePaths (List<String> filePaths)
specifier|public
name|void
name|setCurrentFilePaths
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
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
name|List
argument_list|<
name|String
argument_list|>
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
decl_stmt|;
try|try
block|{
name|p
operator|=
name|cache
operator|.
name|get
argument_list|(
name|c
argument_list|,
name|ps
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatchListNotAvailableException
name|e
parameter_list|)
block|{
name|currentFiles
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
return|return
name|currentFiles
return|;
block|}
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
case|case
name|REWRITE
case|:
break|break;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|currentFiles
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|r
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
specifier|public
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
DECL|method|setChange (Change c)
name|void
name|setChange
parameter_list|(
name|Change
name|c
parameter_list|)
block|{
name|change
operator|=
name|c
expr_stmt|;
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
name|List
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
name|allApprovals
operator|!=
literal|null
condition|)
block|{
return|return
name|allApprovals
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|limitedApprovals
operator|!=
literal|null
operator|&&
operator|(
name|limitedIds
operator|==
literal|null
operator|||
name|limitedIds
operator|.
name|contains
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return
name|limitedApprovals
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
name|currentApprovals
operator|=
name|sortApprovals
argument_list|(
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
argument_list|)
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
comment|/**    * @param db review database.    * @return patches for the change. If {@link #limitToPatchSets(Collection)}    *     was previously called, only contains patches with the specified IDs.    * @throws OrmException an error occurred reading the database.    */
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
operator|||
operator|!
name|patchesLoaded
condition|)
block|{
if|if
condition|(
name|limitedIds
operator|!=
literal|null
condition|)
block|{
name|patches
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
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
name|byChange
argument_list|(
name|legacyId
argument_list|)
control|)
block|{
if|if
condition|(
name|limitedIds
operator|.
name|contains
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|patches
operator|.
name|add
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
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
name|patchesLoaded
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|patches
return|;
block|}
comment|/**    * @param db review database.    * @return patch set approvals for the change in timestamp order. If    *     {@link #limitToPatchSets(Collection)} was previously called, only contains    *     approvals for the patches with the specified IDs.    * @throws OrmException an error occurred reading the database.    */
DECL|method|approvals (Provider<ReviewDb> db)
specifier|public
name|List
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
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|approvalsMap
argument_list|(
name|db
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * @param db review database.    * @return patch set approvals for the change, keyed by ID, ordered by    *     timestamp within each patch set. If    *     {@link #limitToPatchSets(Collection)} was previously called, only    *     contains approvals for the patches with the specified IDs.    * @throws OrmException an error occurred reading the database.    */
DECL|method|approvalsMap ( Provider<ReviewDb> db)
specifier|public
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
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
name|limitedApprovals
operator|==
literal|null
condition|)
block|{
name|limitedApprovals
operator|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
expr_stmt|;
if|if
condition|(
name|allApprovals
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PatchSet
operator|.
name|Id
name|id
range|:
name|limitedIds
control|)
block|{
name|limitedApprovals
operator|.
name|putAll
argument_list|(
name|id
argument_list|,
name|allApprovals
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|sortApprovals
argument_list|(
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
argument_list|)
control|)
block|{
if|if
condition|(
name|limitedIds
operator|==
literal|null
operator|||
name|limitedIds
operator|.
name|contains
argument_list|(
name|legacyId
argument_list|)
condition|)
block|{
name|limitedApprovals
operator|.
name|put
argument_list|(
name|psa
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|psa
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|limitedApprovals
return|;
block|}
comment|/**    * @param db review database.    * @return all patch set approvals for the change in timestamp order    *     (regardless of whether {@link #limitToPatchSets(Collection)} was    *     previously called).    * @throws OrmException an error occurred reading the database.    */
DECL|method|allApprovals (Provider<ReviewDb> db)
specifier|public
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|allApprovals
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
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|allApprovalsMap
argument_list|(
name|db
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * @param db review database.    * @return all patch set approvals for the change (regardless of whether    *     {@link #limitToPatchSets(Collection)} was previously called), keyed by    *     ID, ordered by timestamp within each patch set.    * @throws OrmException an error occurred reading the database.    */
DECL|method|allApprovalsMap ( Provider<ReviewDb> db)
specifier|public
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|allApprovalsMap
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
name|allApprovals
operator|==
literal|null
condition|)
block|{
name|allApprovals
operator|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
expr_stmt|;
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|sortApprovals
argument_list|(
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
argument_list|)
control|)
block|{
name|allApprovals
operator|.
name|put
argument_list|(
name|psa
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|psa
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|allApprovals
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
DECL|method|setSubmitRecords (List<SubmitRecord> records)
specifier|public
name|void
name|setSubmitRecords
parameter_list|(
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|records
parameter_list|)
block|{
name|submitRecords
operator|=
name|records
expr_stmt|;
block|}
DECL|method|getSubmitRecords ()
specifier|public
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|getSubmitRecords
parameter_list|()
block|{
return|return
name|submitRecords
return|;
block|}
block|}
end_class

end_unit

