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
DECL|package|com.google.gerrit.server.submit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|submit
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toSet
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|Optional
import|;
end_import

begin_comment
comment|/**  * Status codes set on {@link com.google.gerrit.server.git.CodeReviewCommit}s by {@link  * SubmitStrategy} implementations.  */
end_comment

begin_enum
DECL|enum|CommitMergeStatus
specifier|public
enum|enum
name|CommitMergeStatus
block|{
DECL|enumConstant|CLEAN_MERGE
name|CLEAN_MERGE
argument_list|(
literal|"Change has been successfully merged"
argument_list|)
block|,
DECL|enumConstant|CLEAN_PICK
name|CLEAN_PICK
argument_list|(
literal|"Change has been successfully cherry-picked"
argument_list|)
block|,
DECL|enumConstant|CLEAN_REBASE
name|CLEAN_REBASE
argument_list|(
literal|"Change has been successfully rebased and submitted"
argument_list|)
block|,
DECL|enumConstant|ALREADY_MERGED
name|ALREADY_MERGED
argument_list|(
literal|""
argument_list|)
block|,
DECL|enumConstant|PATH_CONFLICT
name|PATH_CONFLICT
argument_list|(
literal|"Change could not be merged due to a path conflict.\n"
operator|+
literal|"\n"
operator|+
literal|"Please rebase the change locally and upload the rebased commit for review."
argument_list|)
block|,
DECL|enumConstant|REBASE_MERGE_CONFLICT
name|REBASE_MERGE_CONFLICT
argument_list|(
literal|"Change could not be merged due to a conflict.\n"
operator|+
literal|"\n"
operator|+
literal|"Please rebase the change locally and upload the rebased commit for review."
argument_list|)
block|,
DECL|enumConstant|SKIPPED_IDENTICAL_TREE
name|SKIPPED_IDENTICAL_TREE
argument_list|(
literal|"Marking change merged without cherry-picking to branch, as the resulting commit would be empty."
argument_list|)
block|,
DECL|enumConstant|MISSING_DEPENDENCY
name|MISSING_DEPENDENCY
argument_list|(
literal|"Depends on change that was not submitted."
argument_list|)
block|,
DECL|enumConstant|MANUAL_RECURSIVE_MERGE
name|MANUAL_RECURSIVE_MERGE
argument_list|(
literal|"The change requires a local merge to resolve.\n"
operator|+
literal|"\n"
operator|+
literal|"Please merge (or rebase) the change locally and upload the resolution for review."
argument_list|)
block|,
DECL|enumConstant|CANNOT_CHERRY_PICK_ROOT
name|CANNOT_CHERRY_PICK_ROOT
argument_list|(
literal|"Cannot cherry-pick an initial commit onto an existing branch.\n"
operator|+
literal|"\n"
operator|+
literal|"Please merge the change locally and upload the merge commit for review."
argument_list|)
block|,
DECL|enumConstant|CANNOT_REBASE_ROOT
name|CANNOT_REBASE_ROOT
argument_list|(
literal|"Cannot rebase an initial commit onto an existing branch.\n"
operator|+
literal|"\n"
operator|+
literal|"Please merge the change locally and upload the merge commit for review."
argument_list|)
block|,
DECL|enumConstant|NOT_FAST_FORWARD
name|NOT_FAST_FORWARD
argument_list|(
literal|"Project policy requires all submissions to be a fast-forward.\n"
operator|+
literal|"\n"
operator|+
literal|"Please rebase the change locally and upload again for review."
argument_list|)
block|,
DECL|enumConstant|EMPTY_COMMIT
name|EMPTY_COMMIT
argument_list|(
literal|"Change could not be merged because the commit is empty.\n"
operator|+
literal|"\n"
operator|+
literal|"Project policy requires all commits to contain modifications to at least one file."
argument_list|)
block|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|method|CommitMergeStatus (String description)
name|CommitMergeStatus
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|createMissingDependencyMessage ( @ullable CurrentUser caller, Provider<InternalChangeQuery> queryProvider, String commit, String otherCommit)
specifier|public
specifier|static
name|String
name|createMissingDependencyMessage
parameter_list|(
annotation|@
name|Nullable
name|CurrentUser
name|caller
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|String
name|commit
parameter_list|,
name|String
name|otherCommit
parameter_list|)
block|{
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changes
init|=
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|enforceVisibility
argument_list|(
literal|true
argument_list|)
operator|.
name|byCommit
argument_list|(
name|otherCommit
argument_list|)
decl_stmt|;
if|if
condition|(
name|changes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Commit %s depends on commit %s which cannot be merged."
operator|+
literal|" Is the change of this commit not visible to '%s' or was it deleted?"
argument_list|,
name|commit
argument_list|,
name|otherCommit
argument_list|,
name|caller
operator|!=
literal|null
condition|?
name|caller
operator|.
name|getLoggableName
argument_list|()
else|:
literal|"<user-not-available>"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|changes
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|ChangeData
name|cd
init|=
name|changes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|cd
operator|.
name|currentPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|otherCommit
argument_list|)
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Commit %s depends on commit %s of change %d which cannot be merged."
argument_list|,
name|commit
argument_list|,
name|otherCommit
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
name|Optional
argument_list|<
name|PatchSet
argument_list|>
name|patchSet
init|=
name|cd
operator|.
name|patchSets
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|ps
lambda|->
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|otherCommit
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
decl_stmt|;
if|if
condition|(
name|patchSet
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Commit %s depends on commit %s, which is outdated patch set %d of change %d."
operator|+
literal|" The latest patch set is %d."
argument_list|,
name|commit
argument_list|,
name|otherCommit
argument_list|,
name|patchSet
operator|.
name|get
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|cd
operator|.
name|currentPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
comment|// should not happen, fall-back to default message
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Commit %s depends on commit %s of change %d which cannot be merged."
argument_list|,
name|commit
argument_list|,
name|otherCommit
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Commit %s depends on commit %s of changes %s which cannot be merged."
argument_list|,
name|commit
argument_list|,
name|otherCommit
argument_list|,
name|changes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|cd
lambda|->
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_enum

end_unit

