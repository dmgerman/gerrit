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
DECL|package|com.google.gerrit.server.changedetail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|changedetail
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
name|change
operator|.
name|PatchSetInserter
operator|.
name|ValidatePolicy
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
name|EmailException
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
name|client
operator|.
name|Change
operator|.
name|Status
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
name|PatchSetAncestor
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
name|ChangeUtil
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
name|change
operator|.
name|PatchSetInserter
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
name|RevisionResource
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
name|InvalidChangeOperationException
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
name|NoSuchChangeException
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
name|TimeUtil
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
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
name|CommitBuilder
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
name|Ref
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
name|merge
operator|.
name|ThreeWayMerger
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
name|List
import|;
end_import

begin_class
DECL|class|RebaseChange
specifier|public
class|class
name|RebaseChange
block|{
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|myIdent
specifier|private
specifier|final
name|PersonIdent
name|myIdent
decl_stmt|;
DECL|field|mergeUtilFactory
specifier|private
specifier|final
name|MergeUtil
operator|.
name|Factory
name|mergeUtilFactory
decl_stmt|;
DECL|field|patchSetInserterFactory
specifier|private
specifier|final
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|RebaseChange (final ChangeControl.GenericFactory changeControlFactory, final ReviewDb db, @GerritPersonIdent final PersonIdent myIdent, final GitRepositoryManager gitManager, final MergeUtil.Factory mergeUtilFactory, final PatchSetInserter.Factory patchSetInserterFactory)
name|RebaseChange
parameter_list|(
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
annotation|@
name|GerritPersonIdent
specifier|final
name|PersonIdent
name|myIdent
parameter_list|,
specifier|final
name|GitRepositoryManager
name|gitManager
parameter_list|,
specifier|final
name|MergeUtil
operator|.
name|Factory
name|mergeUtilFactory
parameter_list|,
specifier|final
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
parameter_list|)
block|{
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
name|this
operator|.
name|myIdent
operator|=
name|myIdent
expr_stmt|;
name|this
operator|.
name|mergeUtilFactory
operator|=
name|mergeUtilFactory
expr_stmt|;
name|this
operator|.
name|patchSetInserterFactory
operator|=
name|patchSetInserterFactory
expr_stmt|;
block|}
comment|/**    * Rebases the change of the given patch set.    *    * It is verified that the current user is allowed to do the rebase.    *    * If the patch set has no dependency to an open change, then the change is    * rebased on the tip of the destination branch.    *    * If the patch set depends on an open change, it is rebased on the latest    * patch set of this change.    *    * The rebased commit is added as new patch set to the change.    *    * E-mail notification and triggering of hooks happens for the creation of the    * new patch set.    *    * @param patchSetId the id of the patch set    * @param uploader the user that creates the rebased patch set    * @throws NoSuchChangeException thrown if the change to which the patch set    *         belongs does not exist or is not visible to the user    * @throws EmailException thrown if sending the e-mail to notify about the new    *         patch set fails    * @throws OrmException thrown in case accessing the database fails    * @throws IOException thrown if rebase is not possible or not needed    * @throws InvalidChangeOperationException thrown if rebase is not allowed    */
DECL|method|rebase (final PatchSet.Id patchSetId, final IdentifiedUser uploader)
specifier|public
name|void
name|rebase
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
specifier|final
name|IdentifiedUser
name|uploader
parameter_list|)
throws|throws
name|NoSuchChangeException
throws|,
name|EmailException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|InvalidChangeOperationException
block|{
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
specifier|final
name|ChangeControl
name|changeControl
init|=
name|changeControlFactory
operator|.
name|validateFor
argument_list|(
name|changeId
argument_list|,
name|uploader
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|changeControl
operator|.
name|canRebase
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
literal|"Cannot rebase: New patch sets are not allowed to be added to change: "
operator|+
name|changeId
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|Change
name|change
init|=
name|changeControl
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|Repository
name|git
init|=
literal|null
decl_stmt|;
name|RevWalk
name|rw
init|=
literal|null
decl_stmt|;
name|ObjectInserter
name|inserter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|git
operator|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
expr_stmt|;
name|inserter
operator|=
name|git
operator|.
name|newObjectInserter
argument_list|()
expr_stmt|;
specifier|final
name|String
name|baseRev
init|=
name|findBaseRevision
argument_list|(
name|patchSetId
argument_list|,
name|db
argument_list|,
name|change
operator|.
name|getDest
argument_list|()
argument_list|,
name|git
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|RevCommit
name|baseCommit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|baseRev
argument_list|)
argument_list|)
decl_stmt|;
name|PersonIdent
name|committerIdent
init|=
name|uploader
operator|.
name|newCommitterIdent
argument_list|(
name|myIdent
operator|.
name|getWhen
argument_list|()
argument_list|,
name|myIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
decl_stmt|;
name|rebase
argument_list|(
name|git
argument_list|,
name|rw
argument_list|,
name|inserter
argument_list|,
name|patchSetId
argument_list|,
name|change
argument_list|,
name|uploader
argument_list|,
name|baseCommit
argument_list|,
name|mergeUtilFactory
operator|.
name|create
argument_list|(
name|changeControl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProjectState
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|,
name|committerIdent
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|ValidatePolicy
operator|.
name|GERRIT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PathConflictException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|inserter
operator|!=
literal|null
condition|)
block|{
name|inserter
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|rw
operator|!=
literal|null
condition|)
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|git
operator|!=
literal|null
condition|)
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Finds the revision of commit on which the given patch set should be based.    *    * @param patchSetId the id of the patch set for which the new base commit    *        should be found    * @param db the ReviewDb    * @param destBranch the destination branch    * @param git the repository    * @param patchSetAncestors the original PatchSetAncestor of the given patch    *        set that should be based    * @param depPatchSetList the original patch set list on which the rebased    *        patch set depends    * @param depChangeList the original change list on whose patch set the    *        rebased patch set depends    * @return the revision of commit on which the given patch set should be based    * @throws IOException thrown if rebase is not possible or not needed    * @throws OrmException thrown in case accessing the database fails    */
DECL|method|findBaseRevision (final PatchSet.Id patchSetId, final ReviewDb db, final Branch.NameKey destBranch, final Repository git, List<PatchSetAncestor> patchSetAncestors, List<PatchSet> depPatchSetList, List<Change> depChangeList)
specifier|private
specifier|static
name|String
name|findBaseRevision
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
specifier|final
name|Repository
name|git
parameter_list|,
name|List
argument_list|<
name|PatchSetAncestor
argument_list|>
name|patchSetAncestors
parameter_list|,
name|List
argument_list|<
name|PatchSet
argument_list|>
name|depPatchSetList
parameter_list|,
name|List
argument_list|<
name|Change
argument_list|>
name|depChangeList
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|String
name|baseRev
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|patchSetAncestors
operator|==
literal|null
condition|)
block|{
name|patchSetAncestors
operator|=
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|ancestorsOf
argument_list|(
name|patchSetId
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|patchSetAncestors
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot rebase a change with multiple parents. Parents commits: "
operator|+
name|patchSetAncestors
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|patchSetAncestors
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot rebase a change without any parents (is this the initial commit?)."
argument_list|)
throw|;
block|}
name|RevId
name|ancestorRev
init|=
name|patchSetAncestors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAncestorRevision
argument_list|()
decl_stmt|;
if|if
condition|(
name|depPatchSetList
operator|==
literal|null
operator|||
name|depPatchSetList
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|||
operator|!
name|depPatchSetList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|ancestorRev
argument_list|)
condition|)
block|{
name|depPatchSetList
operator|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byRevision
argument_list|(
name|ancestorRev
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|PatchSet
name|depPatchSet
range|:
name|depPatchSetList
control|)
block|{
name|Change
operator|.
name|Id
name|depChangeId
init|=
name|depPatchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|Change
name|depChange
decl_stmt|;
if|if
condition|(
name|depChangeList
operator|==
literal|null
operator|||
name|depChangeList
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|||
operator|!
name|depChangeList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|depChangeId
argument_list|)
condition|)
block|{
name|depChange
operator|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|depChangeId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|depChange
operator|=
name|depChangeList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|depChange
operator|.
name|getDest
argument_list|()
operator|.
name|equals
argument_list|(
name|destBranch
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|depChange
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|ABANDONED
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot rebase a change with an abandoned parent: "
operator|+
name|depChange
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|depChange
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
if|if
condition|(
name|depPatchSet
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|depChange
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Change is already based on the latest patch set of the dependent change."
argument_list|)
throw|;
block|}
name|PatchSet
name|latestDepPatchSet
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|depChange
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|baseRev
operator|=
name|latestDepPatchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
if|if
condition|(
name|baseRev
operator|==
literal|null
condition|)
block|{
comment|// We are dependent on a merged PatchSet or have no PatchSet
comment|// dependencies at all.
name|Ref
name|destRef
init|=
name|git
operator|.
name|getRef
argument_list|(
name|destBranch
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|destRef
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"The destination branch does not exist: "
operator|+
name|destBranch
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
name|baseRev
operator|=
name|destRef
operator|.
name|getObjectId
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
name|baseRev
operator|.
name|equals
argument_list|(
name|ancestorRev
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Change is already up to date."
argument_list|)
throw|;
block|}
block|}
return|return
name|baseRev
return|;
block|}
comment|/**    * Rebases the change of the given patch set on the given base commit.    *    * The rebased commit is added as new patch set to the change.    *    * E-mail notification and triggering of hooks is only done for the creation of    * the new patch set if `sendEmail` and `runHooks` are set to true.    *    * @param git the repository    * @param revWalk the RevWalk    * @param inserter the object inserter    * @param patchSetId the id of the patch set    * @param change the change that should be rebased    * @param uploader the user that creates the rebased patch set    * @param baseCommit the commit that should be the new base    * @param mergeUtil merge utilities for the destination project    * @param committerIdent the committer's identity    * @param sendMail if a mail notification should be sent for the new patch set    * @param runHooks if hooks should be run for the new patch set    * @param validate if commit validation should be run for the new patch set    * @return the new patch set which is based on the given base commit    * @throws NoSuchChangeException thrown if the change to which the patch set    *         belongs does not exist or is not visible to the user    * @throws OrmException thrown in case accessing the database fails    * @throws IOException thrown if rebase is not possible or not needed    * @throws InvalidChangeOperationException thrown if rebase is not allowed    */
DECL|method|rebase (final Repository git, final RevWalk revWalk, final ObjectInserter inserter, final PatchSet.Id patchSetId, final Change change, final IdentifiedUser uploader, final RevCommit baseCommit, final MergeUtil mergeUtil, PersonIdent committerIdent, boolean sendMail, boolean runHooks, ValidatePolicy validate)
specifier|public
name|PatchSet
name|rebase
parameter_list|(
specifier|final
name|Repository
name|git
parameter_list|,
specifier|final
name|RevWalk
name|revWalk
parameter_list|,
specifier|final
name|ObjectInserter
name|inserter
parameter_list|,
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|IdentifiedUser
name|uploader
parameter_list|,
specifier|final
name|RevCommit
name|baseCommit
parameter_list|,
specifier|final
name|MergeUtil
name|mergeUtil
parameter_list|,
name|PersonIdent
name|committerIdent
parameter_list|,
name|boolean
name|sendMail
parameter_list|,
name|boolean
name|runHooks
parameter_list|,
name|ValidatePolicy
name|validate
parameter_list|)
throws|throws
name|NoSuchChangeException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|InvalidChangeOperationException
throws|,
name|PathConflictException
block|{
if|if
condition|(
operator|!
name|change
operator|.
name|currentPatchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|patchSetId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
literal|"patch set is not current"
argument_list|)
throw|;
block|}
specifier|final
name|PatchSet
name|originalPatchSet
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
specifier|final
name|RevCommit
name|rebasedCommit
decl_stmt|;
name|ObjectId
name|oldId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|originalPatchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectId
name|newId
init|=
name|rebaseCommit
argument_list|(
name|git
argument_list|,
name|inserter
argument_list|,
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|oldId
argument_list|)
argument_list|,
name|baseCommit
argument_list|,
name|mergeUtil
argument_list|,
name|committerIdent
argument_list|)
decl_stmt|;
name|rebasedCommit
operator|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|newId
argument_list|)
expr_stmt|;
specifier|final
name|ChangeControl
name|changeControl
init|=
name|changeControlFactory
operator|.
name|validateFor
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|uploader
argument_list|)
decl_stmt|;
name|PatchSetInserter
name|patchSetInserter
init|=
name|patchSetInserterFactory
operator|.
name|create
argument_list|(
name|git
argument_list|,
name|revWalk
argument_list|,
name|changeControl
operator|.
name|getRefControl
argument_list|()
argument_list|,
name|uploader
argument_list|,
name|change
argument_list|,
name|rebasedCommit
argument_list|)
operator|.
name|setCopyLabels
argument_list|(
literal|true
argument_list|)
operator|.
name|setValidatePolicy
argument_list|(
name|validate
argument_list|)
operator|.
name|setDraft
argument_list|(
name|originalPatchSet
operator|.
name|isDraft
argument_list|()
argument_list|)
operator|.
name|setSendMail
argument_list|(
name|sendMail
argument_list|)
operator|.
name|setRunHooks
argument_list|(
name|runHooks
argument_list|)
decl_stmt|;
specifier|final
name|PatchSet
operator|.
name|Id
name|newPatchSetId
init|=
name|patchSetInserter
operator|.
name|getPatchSetId
argument_list|()
decl_stmt|;
specifier|final
name|ChangeMessage
name|cmsg
init|=
operator|new
name|ChangeMessage
argument_list|(
operator|new
name|ChangeMessage
operator|.
name|Key
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
argument_list|)
argument_list|)
argument_list|,
name|uploader
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|,
name|patchSetId
argument_list|)
decl_stmt|;
name|cmsg
operator|.
name|setMessage
argument_list|(
literal|"Patch Set "
operator|+
name|newPatchSetId
operator|.
name|get
argument_list|()
operator|+
literal|": Patch Set "
operator|+
name|patchSetId
operator|.
name|get
argument_list|()
operator|+
literal|" was rebased"
argument_list|)
expr_stmt|;
name|Change
name|newChange
init|=
name|patchSetInserter
operator|.
name|setMessage
argument_list|(
name|cmsg
argument_list|)
operator|.
name|insert
argument_list|()
decl_stmt|;
return|return
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|newChange
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Rebases a commit.    *    * @param git repository to find commits in    * @param inserter inserter to handle new trees and blobs    * @param original The commit to rebase    * @param base Base to rebase against    * @param mergeUtil merge utilities for the destination project    * @param committerIdent committer identity    * @return the id of the rebased commit    * @throws IOException Merged failed    * @throws PathConflictException the rebase failed due to a path conflict    */
DECL|method|rebaseCommit (final Repository git, final ObjectInserter inserter, final RevCommit original, final RevCommit base, final MergeUtil mergeUtil, final PersonIdent committerIdent)
specifier|private
name|ObjectId
name|rebaseCommit
parameter_list|(
specifier|final
name|Repository
name|git
parameter_list|,
specifier|final
name|ObjectInserter
name|inserter
parameter_list|,
specifier|final
name|RevCommit
name|original
parameter_list|,
specifier|final
name|RevCommit
name|base
parameter_list|,
specifier|final
name|MergeUtil
name|mergeUtil
parameter_list|,
specifier|final
name|PersonIdent
name|committerIdent
parameter_list|)
throws|throws
name|IOException
throws|,
name|PathConflictException
block|{
specifier|final
name|RevCommit
name|parentCommit
init|=
name|original
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|.
name|equals
argument_list|(
name|parentCommit
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Change is already up to date."
argument_list|)
throw|;
block|}
specifier|final
name|ThreeWayMerger
name|merger
init|=
name|mergeUtil
operator|.
name|newThreeWayMerger
argument_list|(
name|git
argument_list|,
name|inserter
argument_list|)
decl_stmt|;
name|merger
operator|.
name|setBase
argument_list|(
name|parentCommit
argument_list|)
expr_stmt|;
name|merger
operator|.
name|merge
argument_list|(
name|original
argument_list|,
name|base
argument_list|)
expr_stmt|;
if|if
condition|(
name|merger
operator|.
name|getResultTreeId
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|PathConflictException
argument_list|(
literal|"The change could not be rebased due to a path conflict during merge."
argument_list|)
throw|;
block|}
specifier|final
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setTreeId
argument_list|(
name|merger
operator|.
name|getResultTreeId
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setParentId
argument_list|(
name|base
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|original
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
name|original
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|committerIdent
argument_list|)
expr_stmt|;
specifier|final
name|ObjectId
name|objectId
init|=
name|inserter
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
decl_stmt|;
name|inserter
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|objectId
return|;
block|}
DECL|method|canRebase (RevisionResource r)
specifier|public
name|boolean
name|canRebase
parameter_list|(
name|RevisionResource
name|r
parameter_list|)
block|{
name|Repository
name|git
decl_stmt|;
try|try
block|{
name|git
operator|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|err
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|findBaseRevision
argument_list|(
name|r
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|db
argument_list|,
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|,
name|git
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|canDoRebase (final ReviewDb db, final Change change, final GitRepositoryManager gitManager, List<PatchSetAncestor> patchSetAncestors, List<PatchSet> depPatchSetList, List<Change> depChangeList)
specifier|public
specifier|static
name|boolean
name|canDoRebase
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|GitRepositoryManager
name|gitManager
parameter_list|,
name|List
argument_list|<
name|PatchSetAncestor
argument_list|>
name|patchSetAncestors
parameter_list|,
name|List
argument_list|<
name|PatchSet
argument_list|>
name|depPatchSetList
parameter_list|,
name|List
argument_list|<
name|Change
argument_list|>
name|depChangeList
parameter_list|)
throws|throws
name|OrmException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|Repository
name|git
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// If no exception is thrown, then we can do a rebase.
name|findBaseRevision
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|db
argument_list|,
name|change
operator|.
name|getDest
argument_list|()
argument_list|,
name|git
argument_list|,
name|patchSetAncestors
argument_list|,
name|depPatchSetList
argument_list|,
name|depChangeList
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

