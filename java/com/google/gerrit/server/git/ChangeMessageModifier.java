begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
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
name|entities
operator|.
name|BranchNameKey
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
name|annotations
operator|.
name|ExtensionPoint
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

begin_comment
comment|/**  * Allows to modify the commit message for new commits generated by Rebase Always submit strategy.  *  *<p>Invoked by Gerrit when all information about new commit is already known such as parent(s),  * tree hash, etc, but commit's message can still be modified.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ChangeMessageModifier
specifier|public
interface|interface
name|ChangeMessageModifier
block|{
comment|/**    * Implementation must return non-Null commit message.    *    *<p>mergeTip and original commit are guaranteed to have their body parsed, meaning that their    * commit messages and footers can be accessed.    *    * @param newCommitMessage the new commit message that was result of either    *<ul>    *<li>{@link MergeUtil#createDetailedCommitMessage} called before    *<li>other extensions or plugins implementing the same point and called before.    *</ul>    *    * @param original the commit of the change being submitted.<b>Note that its commit message may    *     be different than newCommitMessage argument.</b>    * @param mergeTip the current HEAD of the destination branch, which will be a parent of a new    *     commit being generated    * @param destination the branch onto which the change is being submitted    * @return a new not null commit message.    */
DECL|method|onSubmit ( String newCommitMessage, RevCommit original, RevCommit mergeTip, BranchNameKey destination)
name|String
name|onSubmit
parameter_list|(
name|String
name|newCommitMessage
parameter_list|,
name|RevCommit
name|original
parameter_list|,
name|RevCommit
name|mergeTip
parameter_list|,
name|BranchNameKey
name|destination
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

