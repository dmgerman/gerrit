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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|checkArgument
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
name|change
operator|.
name|ChangeKind
operator|.
name|NO_CHANGE
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
name|change
operator|.
name|ChangeKind
operator|.
name|NO_CODE_CHANGE
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
name|change
operator|.
name|ChangeKind
operator|.
name|TRIVIAL_REBASE
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
name|HashBasedTable
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
name|Table
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
name|LabelType
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
name|Account
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
name|change
operator|.
name|ChangeKind
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
name|ChangeKindCache
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
name|git
operator|.
name|LabelNormalizer
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
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|ProjectCache
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
operator|.
name|ChangeData
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
name|NavigableSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
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

begin_comment
comment|/**  * Copies approvals between patch sets.  *<p>  * The result of a copy may either be stored, as when stamping approvals in the  * database at submit time, or refreshed on demand, as when reading approvals  * from the notedb.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ApprovalCopier
specifier|public
class|class
name|ApprovalCopier
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|changeKindCache
specifier|private
specifier|final
name|ChangeKindCache
name|changeKindCache
decl_stmt|;
DECL|field|labelNormalizer
specifier|private
specifier|final
name|LabelNormalizer
name|labelNormalizer
decl_stmt|;
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ApprovalCopier (GitRepositoryManager repoManager, ProjectCache projectCache, ChangeKindCache changeKindCache, LabelNormalizer labelNormalizer, ChangeData.Factory changeDataFactory)
name|ApprovalCopier
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ChangeKindCache
name|changeKindCache
parameter_list|,
name|LabelNormalizer
name|labelNormalizer
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|changeKindCache
operator|=
name|changeKindCache
expr_stmt|;
name|this
operator|.
name|labelNormalizer
operator|=
name|labelNormalizer
expr_stmt|;
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
block|}
DECL|method|copy (ReviewDb db, ChangeControl ctl, PatchSet ps)
specifier|public
name|void
name|copy
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeControl
name|ctl
parameter_list|,
name|PatchSet
name|ps
parameter_list|)
throws|throws
name|OrmException
block|{
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|insert
argument_list|(
name|getForPatchSet
argument_list|(
name|db
argument_list|,
name|ctl
argument_list|,
name|ps
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getForPatchSet (ReviewDb db, ChangeControl ctl, PatchSet.Id psId)
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|getForPatchSet
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeControl
name|ctl
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|getForPatchSet
argument_list|(
name|db
argument_list|,
name|ctl
argument_list|,
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getForPatchSet (ReviewDb db, ChangeControl ctl, PatchSet ps)
specifier|private
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|getForPatchSet
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeControl
name|ctl
parameter_list|,
name|PatchSet
name|ps
parameter_list|)
throws|throws
name|OrmException
block|{
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|ctl
argument_list|)
decl_stmt|;
try|try
block|{
name|ProjectState
name|project
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|all
init|=
name|cd
operator|.
name|approvals
argument_list|()
decl_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Account
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|byUser
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|all
operator|.
name|get
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
name|byUser
operator|.
name|put
argument_list|(
name|psa
operator|.
name|getLabel
argument_list|()
argument_list|,
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|psa
argument_list|)
expr_stmt|;
block|}
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|PatchSet
argument_list|>
name|patchSets
init|=
name|getPatchSets
argument_list|(
name|cd
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|allPsIds
init|=
name|patchSets
operator|.
name|navigableKeySet
argument_list|()
decl_stmt|;
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// Walk patch sets strictly less than current in descending order.
name|Collection
argument_list|<
name|PatchSet
argument_list|>
name|allPrior
init|=
name|patchSets
operator|.
name|descendingMap
argument_list|()
operator|.
name|tailMap
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSet
name|priorPs
range|:
name|allPrior
control|)
block|{
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|priorApprovals
init|=
name|all
operator|.
name|get
argument_list|(
name|priorPs
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|priorApprovals
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|ChangeKind
name|kind
init|=
name|changeKindCache
operator|.
name|getChangeKind
argument_list|(
name|project
argument_list|,
name|repo
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
name|priorPs
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|priorApprovals
control|)
block|{
if|if
condition|(
operator|!
name|byUser
operator|.
name|contains
argument_list|(
name|psa
operator|.
name|getLabel
argument_list|()
argument_list|,
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|&&
name|canCopy
argument_list|(
name|project
argument_list|,
name|psa
argument_list|,
name|ps
operator|.
name|getId
argument_list|()
argument_list|,
name|allPsIds
argument_list|,
name|kind
argument_list|)
condition|)
block|{
name|byUser
operator|.
name|put
argument_list|(
name|psa
operator|.
name|getLabel
argument_list|()
argument_list|,
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|copy
argument_list|(
name|psa
argument_list|,
name|ps
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|labelNormalizer
operator|.
name|normalize
argument_list|(
name|ctl
argument_list|,
name|byUser
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|getNormalized
argument_list|()
return|;
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getPatchSets (ChangeData cd)
specifier|private
specifier|static
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|PatchSet
argument_list|>
name|getPatchSets
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
name|Collection
argument_list|<
name|PatchSet
argument_list|>
name|patchSets
init|=
name|cd
operator|.
name|patches
argument_list|()
decl_stmt|;
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|PatchSet
argument_list|>
name|result
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSet
name|ps
range|:
name|patchSets
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|ps
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|canCopy (ProjectState project, PatchSetApproval psa, PatchSet.Id psId, NavigableSet<Integer> allPsIds, ChangeKind kind)
specifier|private
specifier|static
name|boolean
name|canCopy
parameter_list|(
name|ProjectState
name|project
parameter_list|,
name|PatchSetApproval
name|psa
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|allPsIds
parameter_list|,
name|ChangeKind
name|kind
parameter_list|)
block|{
name|int
name|n
init|=
name|psa
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|n
operator|!=
name|psId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|LabelType
name|type
init|=
name|project
operator|.
name|getLabelTypes
argument_list|()
operator|.
name|byLabel
argument_list|(
name|psa
operator|.
name|getLabelId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|n
argument_list|,
name|previous
argument_list|(
name|allPsIds
argument_list|,
name|psId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|&&
operator|(
name|type
operator|.
name|isCopyMinScore
argument_list|()
operator|&&
name|type
operator|.
name|isMaxNegative
argument_list|(
name|psa
argument_list|)
operator|||
name|type
operator|.
name|isCopyMaxScore
argument_list|()
operator|&&
name|type
operator|.
name|isMaxPositive
argument_list|(
name|psa
argument_list|)
operator|)
condition|)
block|{
comment|// Copy min/max score only from the immediately preceding patch set (which
comment|// may not be psId.get() - 1).
return|return
literal|true
return|;
block|}
return|return
operator|(
name|type
operator|.
name|isCopyAllScoresOnTrivialRebase
argument_list|()
operator|&&
name|kind
operator|==
name|TRIVIAL_REBASE
operator|)
operator|||
operator|(
name|type
operator|.
name|isCopyAllScoresIfNoCodeChange
argument_list|()
operator|&&
name|kind
operator|==
name|NO_CODE_CHANGE
operator|)
operator|||
operator|(
name|type
operator|.
name|isCopyAllScoresIfNoChange
argument_list|()
operator|&&
name|kind
operator|==
name|NO_CHANGE
operator|)
return|;
block|}
DECL|method|copy (PatchSetApproval src, PatchSet.Id psId)
specifier|private
specifier|static
name|PatchSetApproval
name|copy
parameter_list|(
name|PatchSetApproval
name|src
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
if|if
condition|(
name|src
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|equals
argument_list|(
name|psId
argument_list|)
condition|)
block|{
return|return
name|src
return|;
block|}
return|return
operator|new
name|PatchSetApproval
argument_list|(
name|psId
argument_list|,
name|src
argument_list|)
return|;
block|}
DECL|method|previous (NavigableSet<T> s, T v)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|previous
parameter_list|(
name|NavigableSet
argument_list|<
name|T
argument_list|>
name|s
parameter_list|,
name|T
name|v
parameter_list|)
block|{
name|SortedSet
argument_list|<
name|T
argument_list|>
name|head
init|=
name|s
operator|.
name|headSet
argument_list|(
name|v
argument_list|)
decl_stmt|;
return|return
operator|!
name|head
operator|.
name|isEmpty
argument_list|()
condition|?
name|head
operator|.
name|last
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

