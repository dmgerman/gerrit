begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.strategy
package|package
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
name|strategy
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
name|checkNotNull
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
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
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|SubmitType
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
name|config
operator|.
name|FactoryModule
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
name|Branch
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
name|ApprovalsUtil
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
name|ChangeMessagesUtil
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
name|GerritPersonIdent
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
name|IdentifiedUser
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
name|PatchSetUtil
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
name|account
operator|.
name|AccountCache
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
name|RebaseChangeOp
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
name|extensions
operator|.
name|events
operator|.
name|ChangeMerged
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
name|BatchUpdate
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
name|CodeReviewCommit
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
name|CodeReviewCommit
operator|.
name|CodeReviewRevWalk
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
name|EmailMerge
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
name|IntegrationException
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
name|git
operator|.
name|MergeOp
operator|.
name|CommitStatus
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
name|MergeSorter
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
name|MergeTip
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
name|MergeUtil
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
name|SubmoduleOp
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
name|TagCache
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
name|validators
operator|.
name|OnSubmitValidators
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
name|PatchSetInfoFactory
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
name|util
operator|.
name|RequestId
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
name|Module
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
name|assistedinject
operator|.
name|Assisted
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
name|assistedinject
operator|.
name|AssistedInject
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
name|ObjectInserter
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
name|PersonIdent
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
name|RevFlag
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Base class that submit strategies must extend.  *<p>  * A submit strategy for a certain {@link SubmitType} defines how the submitted  * commits should be merged.  */
end_comment

begin_class
DECL|class|SubmitStrategy
specifier|public
specifier|abstract
class|class
name|SubmitStrategy
block|{
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|FactoryModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|factory
argument_list|(
name|SubmitStrategy
operator|.
name|Arguments
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|Arguments
specifier|static
class|class
name|Arguments
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ( SubmitType submitType, Branch.NameKey destBranch, CommitStatus commitStatus, CodeReviewRevWalk rw, IdentifiedUser caller, MergeTip mergeTip, ObjectInserter inserter, Repository repo, RevFlag canMergeFlag, ReviewDb db, Set<RevCommit> alreadyAccepted, RequestId submissionId, NotifyHandling notifyHandling, ListMultimap<RecipientType, Account.Id> accountsToNotify, SubmoduleOp submoduleOp, boolean dryrun)
name|Arguments
name|create
parameter_list|(
name|SubmitType
name|submitType
parameter_list|,
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
name|CommitStatus
name|commitStatus
parameter_list|,
name|CodeReviewRevWalk
name|rw
parameter_list|,
name|IdentifiedUser
name|caller
parameter_list|,
name|MergeTip
name|mergeTip
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|RevFlag
name|canMergeFlag
parameter_list|,
name|ReviewDb
name|db
parameter_list|,
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|alreadyAccepted
parameter_list|,
name|RequestId
name|submissionId
parameter_list|,
name|NotifyHandling
name|notifyHandling
parameter_list|,
name|ListMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accountsToNotify
parameter_list|,
name|SubmoduleOp
name|submoduleOp
parameter_list|,
name|boolean
name|dryrun
parameter_list|)
function_decl|;
block|}
DECL|field|accountCache
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|approvalsUtil
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|batchUpdateFactory
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|field|changeControlFactory
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
decl_stmt|;
DECL|field|changeMerged
specifier|final
name|ChangeMerged
name|changeMerged
decl_stmt|;
DECL|field|cmUtil
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|mergedSenderFactory
specifier|final
name|EmailMerge
operator|.
name|Factory
name|mergedSenderFactory
decl_stmt|;
DECL|field|repoManager
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|labelNormalizer
specifier|final
name|LabelNormalizer
name|labelNormalizer
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|psUtil
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|projectCache
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|serverIdent
specifier|final
name|PersonIdent
name|serverIdent
decl_stmt|;
DECL|field|rebaseFactory
specifier|final
name|RebaseChangeOp
operator|.
name|Factory
name|rebaseFactory
decl_stmt|;
DECL|field|onSubmitValidatorsFactory
specifier|final
name|OnSubmitValidators
operator|.
name|Factory
name|onSubmitValidatorsFactory
decl_stmt|;
DECL|field|tagCache
specifier|final
name|TagCache
name|tagCache
decl_stmt|;
DECL|field|destBranch
specifier|final
name|Branch
operator|.
name|NameKey
name|destBranch
decl_stmt|;
DECL|field|rw
specifier|final
name|CodeReviewRevWalk
name|rw
decl_stmt|;
DECL|field|commitStatus
specifier|final
name|CommitStatus
name|commitStatus
decl_stmt|;
DECL|field|caller
specifier|final
name|IdentifiedUser
name|caller
decl_stmt|;
DECL|field|mergeTip
specifier|final
name|MergeTip
name|mergeTip
decl_stmt|;
DECL|field|inserter
specifier|final
name|ObjectInserter
name|inserter
decl_stmt|;
DECL|field|repo
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|canMergeFlag
specifier|final
name|RevFlag
name|canMergeFlag
decl_stmt|;
DECL|field|db
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|alreadyAccepted
specifier|final
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|alreadyAccepted
decl_stmt|;
DECL|field|submissionId
specifier|final
name|RequestId
name|submissionId
decl_stmt|;
DECL|field|submitType
specifier|final
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|notifyHandling
specifier|final
name|NotifyHandling
name|notifyHandling
decl_stmt|;
DECL|field|accountsToNotify
specifier|final
name|ListMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accountsToNotify
decl_stmt|;
DECL|field|submoduleOp
specifier|final
name|SubmoduleOp
name|submoduleOp
decl_stmt|;
DECL|field|project
specifier|final
name|ProjectState
name|project
decl_stmt|;
DECL|field|mergeSorter
specifier|final
name|MergeSorter
name|mergeSorter
decl_stmt|;
DECL|field|mergeUtil
specifier|final
name|MergeUtil
name|mergeUtil
decl_stmt|;
DECL|field|dryrun
specifier|final
name|boolean
name|dryrun
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|Arguments ( AccountCache accountCache, ApprovalsUtil approvalsUtil, BatchUpdate.Factory batchUpdateFactory, ChangeControl.GenericFactory changeControlFactory, ChangeMerged changeMerged, ChangeMessagesUtil cmUtil, EmailMerge.Factory mergedSenderFactory, GitRepositoryManager repoManager, LabelNormalizer labelNormalizer, MergeUtil.Factory mergeUtilFactory, PatchSetInfoFactory patchSetInfoFactory, PatchSetUtil psUtil, @GerritPersonIdent PersonIdent serverIdent, ProjectCache projectCache, RebaseChangeOp.Factory rebaseFactory, OnSubmitValidators.Factory onSubmitValidatorsFactory, TagCache tagCache, @Assisted Branch.NameKey destBranch, @Assisted CommitStatus commitStatus, @Assisted CodeReviewRevWalk rw, @Assisted IdentifiedUser caller, @Assisted MergeTip mergeTip, @Assisted ObjectInserter inserter, @Assisted Repository repo, @Assisted RevFlag canMergeFlag, @Assisted ReviewDb db, @Assisted Set<RevCommit> alreadyAccepted, @Assisted RequestId submissionId, @Assisted SubmitType submitType, @Assisted NotifyHandling notifyHandling, @Assisted ListMultimap<RecipientType, Account.Id> accountsToNotify, @Assisted SubmoduleOp submoduleOp, @Assisted boolean dryrun)
name|Arguments
parameter_list|(
name|AccountCache
name|accountCache
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|,
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
parameter_list|,
name|ChangeMerged
name|changeMerged
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|EmailMerge
operator|.
name|Factory
name|mergedSenderFactory
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|LabelNormalizer
name|labelNormalizer
parameter_list|,
name|MergeUtil
operator|.
name|Factory
name|mergeUtilFactory
parameter_list|,
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|RebaseChangeOp
operator|.
name|Factory
name|rebaseFactory
parameter_list|,
name|OnSubmitValidators
operator|.
name|Factory
name|onSubmitValidatorsFactory
parameter_list|,
name|TagCache
name|tagCache
parameter_list|,
annotation|@
name|Assisted
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
annotation|@
name|Assisted
name|CommitStatus
name|commitStatus
parameter_list|,
annotation|@
name|Assisted
name|CodeReviewRevWalk
name|rw
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|caller
parameter_list|,
annotation|@
name|Assisted
name|MergeTip
name|mergeTip
parameter_list|,
annotation|@
name|Assisted
name|ObjectInserter
name|inserter
parameter_list|,
annotation|@
name|Assisted
name|Repository
name|repo
parameter_list|,
annotation|@
name|Assisted
name|RevFlag
name|canMergeFlag
parameter_list|,
annotation|@
name|Assisted
name|ReviewDb
name|db
parameter_list|,
annotation|@
name|Assisted
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|alreadyAccepted
parameter_list|,
annotation|@
name|Assisted
name|RequestId
name|submissionId
parameter_list|,
annotation|@
name|Assisted
name|SubmitType
name|submitType
parameter_list|,
annotation|@
name|Assisted
name|NotifyHandling
name|notifyHandling
parameter_list|,
annotation|@
name|Assisted
name|ListMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accountsToNotify
parameter_list|,
annotation|@
name|Assisted
name|SubmoduleOp
name|submoduleOp
parameter_list|,
annotation|@
name|Assisted
name|boolean
name|dryrun
parameter_list|)
block|{
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|batchUpdateFactory
operator|=
name|batchUpdateFactory
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|changeMerged
operator|=
name|changeMerged
expr_stmt|;
name|this
operator|.
name|mergedSenderFactory
operator|=
name|mergedSenderFactory
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|labelNormalizer
operator|=
name|labelNormalizer
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|rebaseFactory
operator|=
name|rebaseFactory
expr_stmt|;
name|this
operator|.
name|tagCache
operator|=
name|tagCache
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|destBranch
operator|=
name|destBranch
expr_stmt|;
name|this
operator|.
name|commitStatus
operator|=
name|commitStatus
expr_stmt|;
name|this
operator|.
name|rw
operator|=
name|rw
expr_stmt|;
name|this
operator|.
name|caller
operator|=
name|caller
expr_stmt|;
name|this
operator|.
name|mergeTip
operator|=
name|mergeTip
expr_stmt|;
name|this
operator|.
name|inserter
operator|=
name|inserter
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|canMergeFlag
operator|=
name|canMergeFlag
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|alreadyAccepted
operator|=
name|alreadyAccepted
expr_stmt|;
name|this
operator|.
name|submissionId
operator|=
name|submissionId
expr_stmt|;
name|this
operator|.
name|submitType
operator|=
name|submitType
expr_stmt|;
name|this
operator|.
name|notifyHandling
operator|=
name|notifyHandling
expr_stmt|;
name|this
operator|.
name|accountsToNotify
operator|=
name|accountsToNotify
expr_stmt|;
name|this
operator|.
name|submoduleOp
operator|=
name|submoduleOp
expr_stmt|;
name|this
operator|.
name|dryrun
operator|=
name|dryrun
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|checkNotNull
argument_list|(
name|projectCache
operator|.
name|get
argument_list|(
name|destBranch
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|,
literal|"project not found: %s"
argument_list|,
name|destBranch
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|mergeSorter
operator|=
operator|new
name|MergeSorter
argument_list|(
name|rw
argument_list|,
name|alreadyAccepted
argument_list|,
name|canMergeFlag
argument_list|)
expr_stmt|;
name|this
operator|.
name|mergeUtil
operator|=
name|mergeUtilFactory
operator|.
name|create
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|this
operator|.
name|onSubmitValidatorsFactory
operator|=
name|onSubmitValidatorsFactory
expr_stmt|;
block|}
block|}
DECL|field|args
specifier|final
name|Arguments
name|args
decl_stmt|;
DECL|method|SubmitStrategy (Arguments args)
name|SubmitStrategy
parameter_list|(
name|Arguments
name|args
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|checkNotNull
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**    * Add operations to a batch update that execute this submit strategy.    *<p>    * Guarantees exactly one op is added to the update for each change in the    * input set.    *    * @param bu batch update to add operations to.    * @param toMerge the set of submitted commits that should be merged using    *     this submit strategy. Implementations are responsible for ordering of    *     commits, and will not modify the input in place.    * @throws IntegrationException if an error occurred initializing the    *     operations (as opposed to an error during execution, which will be    *     reported only when the batch update executes the operations).    */
DECL|method|addOps (BatchUpdate bu, Set<CodeReviewCommit> toMerge)
specifier|public
specifier|final
name|void
name|addOps
parameter_list|(
name|BatchUpdate
name|bu
parameter_list|,
name|Set
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
throws|throws
name|IntegrationException
block|{
name|List
argument_list|<
name|SubmitStrategyOp
argument_list|>
name|ops
init|=
name|buildOps
argument_list|(
name|toMerge
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|CodeReviewCommit
argument_list|>
name|added
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|ops
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|SubmitStrategyOp
name|op
range|:
name|ops
control|)
block|{
name|added
operator|.
name|add
argument_list|(
name|op
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// First add ops for any implicitly merged changes.
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|difference
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Sets
operator|.
name|difference
argument_list|(
name|toMerge
argument_list|,
name|added
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|difference
argument_list|)
expr_stmt|;
for|for
control|(
name|CodeReviewCommit
name|c
range|:
name|difference
control|)
block|{
name|bu
operator|.
name|addOp
argument_list|(
name|c
operator|.
name|change
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|ImplicitIntegrateOp
argument_list|(
name|args
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Then ops for explicitly merged changes
for|for
control|(
name|SubmitStrategyOp
name|op
range|:
name|ops
control|)
block|{
name|bu
operator|.
name|addOp
argument_list|(
name|op
operator|.
name|getId
argument_list|()
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildOps ( Collection<CodeReviewCommit> toMerge)
specifier|protected
specifier|abstract
name|List
argument_list|<
name|SubmitStrategyOp
argument_list|>
name|buildOps
parameter_list|(
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
throws|throws
name|IntegrationException
function_decl|;
block|}
end_class

end_unit

