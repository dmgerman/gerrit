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
DECL|package|com.google.gerrit.server.git.validators
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
name|validators
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|project
operator|.
name|ProjectState
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

begin_comment
comment|/**  * Listener to provide validation of commits before merging.  *  *<p>Invoked by Gerrit before a commit is merged.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|MergeValidationListener
specifier|public
interface|interface
name|MergeValidationListener
block|{
comment|/**    * Validate a commit before it is merged.    *    * @param repo the repository    * @param commit commit details    * @param destProject the destination project    * @param destBranch the destination branch    * @param changeId the change ID    * @param patchSetId the patch set ID    * @param caller the user who initiated the merge request    * @throws MergeValidationException if the commit fails to validate    */
DECL|method|onPreMerge ( Repository repo, CodeReviewCommit commit, ProjectState destProject, Branch.NameKey destBranch, Change.Id changeId, PatchSet.Id patchSetId, IdentifiedUser caller)
name|void
name|onPreMerge
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|CodeReviewCommit
name|commit
parameter_list|,
name|ProjectState
name|destProject
parameter_list|,
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|IdentifiedUser
name|caller
parameter_list|)
throws|throws
name|MergeValidationException
function_decl|;
block|}
end_interface

end_unit

