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
name|checkState
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
name|MergeException
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
name|index
operator|.
name|ChangeIndexer
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
name|RefUpdate
operator|.
name|Result
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
DECL|class|Arguments
specifier|static
class|class
name|Arguments
block|{
DECL|field|identifiedUserFactory
specifier|protected
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|serverIdent
specifier|protected
specifier|final
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
DECL|field|db
specifier|protected
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|batchUpdateFactory
specifier|protected
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|field|changeControlFactory
specifier|protected
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
decl_stmt|;
DECL|field|repo
specifier|protected
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|rw
specifier|protected
specifier|final
name|CodeReviewRevWalk
name|rw
decl_stmt|;
DECL|field|inserter
specifier|protected
specifier|final
name|ObjectInserter
name|inserter
decl_stmt|;
DECL|field|canMergeFlag
specifier|protected
specifier|final
name|RevFlag
name|canMergeFlag
decl_stmt|;
DECL|field|alreadyAccepted
specifier|protected
specifier|final
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|alreadyAccepted
decl_stmt|;
DECL|field|destBranch
specifier|protected
specifier|final
name|Branch
operator|.
name|NameKey
name|destBranch
decl_stmt|;
DECL|field|approvalsUtil
specifier|protected
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|mergeUtil
specifier|protected
specifier|final
name|MergeUtil
name|mergeUtil
decl_stmt|;
DECL|field|indexer
specifier|protected
specifier|final
name|ChangeIndexer
name|indexer
decl_stmt|;
DECL|field|mergeSorter
specifier|protected
specifier|final
name|MergeSorter
name|mergeSorter
decl_stmt|;
DECL|field|caller
specifier|protected
specifier|final
name|IdentifiedUser
name|caller
decl_stmt|;
DECL|method|Arguments (IdentifiedUser.GenericFactory identifiedUserFactory, Provider<PersonIdent> serverIdent, ReviewDb db, BatchUpdate.Factory batchUpdateFactory, ChangeControl.GenericFactory changeControlFactory, Repository repo, CodeReviewRevWalk rw, ObjectInserter inserter, RevFlag canMergeFlag, Set<RevCommit> alreadyAccepted, Branch.NameKey destBranch, ApprovalsUtil approvalsUtil, MergeUtil mergeUtil, ChangeIndexer indexer, IdentifiedUser caller)
name|Arguments
parameter_list|(
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
parameter_list|,
name|ReviewDb
name|db
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
name|Repository
name|repo
parameter_list|,
name|CodeReviewRevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|,
name|RevFlag
name|canMergeFlag
parameter_list|,
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|alreadyAccepted
parameter_list|,
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|MergeUtil
name|mergeUtil
parameter_list|,
name|ChangeIndexer
name|indexer
parameter_list|,
name|IdentifiedUser
name|caller
parameter_list|)
block|{
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
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
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|rw
operator|=
name|rw
expr_stmt|;
name|this
operator|.
name|inserter
operator|=
name|inserter
expr_stmt|;
name|this
operator|.
name|canMergeFlag
operator|=
name|canMergeFlag
expr_stmt|;
name|this
operator|.
name|alreadyAccepted
operator|=
name|alreadyAccepted
expr_stmt|;
name|this
operator|.
name|destBranch
operator|=
name|destBranch
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|mergeUtil
operator|=
name|mergeUtil
expr_stmt|;
name|this
operator|.
name|indexer
operator|=
name|indexer
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
name|caller
operator|=
name|caller
expr_stmt|;
block|}
DECL|method|newBatchUpdate (Timestamp when)
name|BatchUpdate
name|newBatchUpdate
parameter_list|(
name|Timestamp
name|when
parameter_list|)
block|{
return|return
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|destBranch
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|when
argument_list|)
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|inserter
argument_list|)
return|;
block|}
block|}
DECL|field|args
specifier|protected
specifier|final
name|Arguments
name|args
decl_stmt|;
DECL|field|refLogIdent
specifier|private
name|PersonIdent
name|refLogIdent
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
name|args
expr_stmt|;
block|}
comment|/**    * Runs this submit strategy.    *<p>    * If possible, the provided commits will be merged with this submit strategy.    *    * @param currentTip the mergeTip    * @param toMerge the list of submitted commits that should be merged using    *        this submit strategy. Implementations are responsible for ordering    *        of commits, and should not modify the input in place.    * @return the new merge tip.    * @throws MergeException    */
DECL|method|run (final CodeReviewCommit currentTip, final Collection<CodeReviewCommit> toMerge)
specifier|public
specifier|final
name|MergeTip
name|run
parameter_list|(
specifier|final
name|CodeReviewCommit
name|currentTip
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
throws|throws
name|MergeException
block|{
name|refLogIdent
operator|=
literal|null
expr_stmt|;
name|checkState
argument_list|(
name|args
operator|.
name|caller
operator|!=
literal|null
argument_list|)
expr_stmt|;
return|return
name|_run
argument_list|(
name|currentTip
argument_list|,
name|toMerge
argument_list|)
return|;
block|}
comment|/** @see #run(CodeReviewCommit, Collection) */
DECL|method|_run (CodeReviewCommit currentTip, Collection<CodeReviewCommit> toMerge)
specifier|protected
specifier|abstract
name|MergeTip
name|_run
parameter_list|(
name|CodeReviewCommit
name|currentTip
parameter_list|,
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
throws|throws
name|MergeException
function_decl|;
comment|/**    * Checks whether the given commit can be merged.    *<p>    * Implementations must ensure that invoking this method modifies neither the    * git repository nor the Gerrit database.    *    * @param mergeTip the merge tip.    * @param toMerge the commit that should be checked.    * @return {@code true} if the given commit can be merged, otherwise    *         {@code false}    * @throws MergeException    */
DECL|method|dryRun (CodeReviewCommit mergeTip, CodeReviewCommit toMerge)
specifier|public
specifier|abstract
name|boolean
name|dryRun
parameter_list|(
name|CodeReviewCommit
name|mergeTip
parameter_list|,
name|CodeReviewCommit
name|toMerge
parameter_list|)
throws|throws
name|MergeException
function_decl|;
comment|/**    * Returns the identity that should be used for reflog entries when updating    * the destination branch.    *<p>    * The reflog identity may only be set during {@link #run(CodeReviewCommit,    * Collection)}, and this method is invalid to call beforehand.    *    * @return the ref log identity, which may be {@code null}.    */
DECL|method|getRefLogIdent ()
specifier|public
specifier|final
name|PersonIdent
name|getRefLogIdent
parameter_list|()
block|{
return|return
name|refLogIdent
return|;
block|}
comment|/**    * Returns all commits that have been newly created for the changes that are    * getting merged.    *<p>    * By default this method returns an empty map, but subclasses may override    * this method to provide any newly created commits.    *<p>    * This method may only be called after {@link #run(CodeReviewCommit,    * Collection)}.    *    * @return new commits created for changes that were merged.    */
DECL|method|getNewCommits ()
specifier|public
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|CodeReviewCommit
argument_list|>
name|getNewCommits
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/**    * Returns whether a merge that failed with {@link Result#LOCK_FAILURE} should    * be retried.    *<p>    * May be overridden by subclasses.    *    * @return {@code true} if a merge that failed with    *         {@link Result#LOCK_FAILURE} should be retried, otherwise    *         {@code false}    */
DECL|method|retryOnLockFailure ()
specifier|public
name|boolean
name|retryOnLockFailure
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**    * Set the ref log identity if it wasn't set yet.    */
DECL|method|setRefLogIdent ()
specifier|protected
specifier|final
name|void
name|setRefLogIdent
parameter_list|()
block|{
if|if
condition|(
name|refLogIdent
operator|==
literal|null
condition|)
block|{
name|refLogIdent
operator|=
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|args
operator|.
name|caller
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|newRefLogIdent
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

