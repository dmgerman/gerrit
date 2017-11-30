begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|annotations
operator|.
name|VisibleForTesting
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
name|LockFailureException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|internal
operator|.
name|JGitText
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
name|BatchRefUpdate
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
name|NullProgressMonitor
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
name|RevWalk
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
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_comment
comment|/** Static utilities for working with JGit's ref update APIs. */
end_comment

begin_class
DECL|class|RefUpdateUtil
specifier|public
class|class
name|RefUpdateUtil
block|{
comment|/**    * Execute a batch ref update, throwing a checked exception if not all updates succeeded.    *    *<p>Creates a new {@link RevWalk} used only for this operation.    *    * @param bru batch update; should already have been executed.    * @param repo repository that created {@code bru}.    * @throws LockFailureException if the transaction was aborted due to lock failure; see {@link    *     #checkResults(BatchRefUpdate)} for details.    * @throws IOException if any result was not {@code OK}.    */
DECL|method|executeChecked (BatchRefUpdate bru, Repository repo)
specifier|public
specifier|static
name|void
name|executeChecked
parameter_list|(
name|BatchRefUpdate
name|bru
parameter_list|,
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|executeChecked
argument_list|(
name|bru
argument_list|,
name|rw
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Execute a batch ref update, throwing a checked exception if not all updates succeeded.    *    * @param bru batch update; should already have been executed.    * @param rw walk for executing the update.    * @throws LockFailureException if the transaction was aborted due to lock failure; see {@link    *     #checkResults(BatchRefUpdate)} for details.    * @throws IOException if any result was not {@code OK}.    */
DECL|method|executeChecked (BatchRefUpdate bru, RevWalk rw)
specifier|public
specifier|static
name|void
name|executeChecked
parameter_list|(
name|BatchRefUpdate
name|bru
parameter_list|,
name|RevWalk
name|rw
parameter_list|)
throws|throws
name|IOException
block|{
name|bru
operator|.
name|execute
argument_list|(
name|rw
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|checkResults
argument_list|(
name|bru
argument_list|)
expr_stmt|;
block|}
comment|/**    * Check results of all commands in the update batch, reducing to a single exception if there was    * a failure.    *    *<p>Throws {@link LockFailureException} if at least one command failed with {@code    * LOCK_FAILURE}, and the entire transaction was aborted, i.e. any non-{@code LOCK_FAILURE}    * results, if there were any, failed with "transaction aborted".    *    *<p>In particular, if the underlying ref database does not {@link    * org.eclipse.jgit.lib.RefDatabase#performsAtomicTransactions() perform atomic transactions},    * then a combination of {@code LOCK_FAILURE} on one ref and {@code OK} or another result on other    * refs will<em>not</em> throw {@code LockFailureException}.    *    * @param bru batch update; should already have been executed.    * @throws LockFailureException if the transaction was aborted due to lock failure.    * @throws IOException if any result was not {@code OK}.    */
annotation|@
name|VisibleForTesting
DECL|method|checkResults (BatchRefUpdate bru)
specifier|static
name|void
name|checkResults
parameter_list|(
name|BatchRefUpdate
name|bru
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|bru
operator|.
name|getCommands
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|int
name|lockFailure
init|=
literal|0
decl_stmt|;
name|int
name|aborted
init|=
literal|0
decl_stmt|;
name|int
name|failure
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ReceiveCommand
name|cmd
range|:
name|bru
operator|.
name|getCommands
argument_list|()
control|)
block|{
if|if
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
operator|!=
name|ReceiveCommand
operator|.
name|Result
operator|.
name|OK
condition|)
block|{
name|failure
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
operator|==
name|ReceiveCommand
operator|.
name|Result
operator|.
name|LOCK_FAILURE
condition|)
block|{
name|lockFailure
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
operator|==
name|ReceiveCommand
operator|.
name|Result
operator|.
name|REJECTED_OTHER_REASON
operator|&&
name|JGitText
operator|.
name|get
argument_list|()
operator|.
name|transactionAborted
operator|.
name|equals
argument_list|(
name|cmd
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|aborted
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|lockFailure
operator|+
name|aborted
operator|==
name|bru
operator|.
name|getCommands
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|LockFailureException
argument_list|(
literal|"Update aborted with one or more lock failures: "
operator|+
name|bru
argument_list|,
name|bru
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|failure
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Update failed: "
operator|+
name|bru
argument_list|)
throw|;
block|}
block|}
comment|/**    * Delete a single ref, throwing a checked exception on failure.    *    *<p>Does not require that the ref have any particular old value. Succeeds as a no-op if the ref    * did not exist.    *    * @param repo repository.    * @param refName ref name to delete.    * @throws LockFailureException if a low-level lock failure (e.g. compare-and-swap failure)    *     occurs.    * @throws IOException if an error occurred.    */
DECL|method|deleteChecked (Repository repo, String refName)
specifier|public
specifier|static
name|void
name|deleteChecked
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|String
name|refName
parameter_list|)
throws|throws
name|IOException
block|{
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|ru
operator|.
name|delete
argument_list|()
condition|)
block|{
case|case
name|FORCED
case|:
comment|// Ref was deleted.
return|return;
case|case
name|NEW
case|:
comment|// Ref didn't exist (yes, really).
return|return;
case|case
name|LOCK_FAILURE
case|:
throw|throw
operator|new
name|LockFailureException
argument_list|(
literal|"Failed to delete "
operator|+
name|refName
operator|+
literal|": "
operator|+
name|ru
operator|.
name|getResult
argument_list|()
argument_list|,
name|ru
argument_list|)
throw|;
comment|// Not really failures, but should not be the result of a deletion, so the best option is to
comment|// throw.
case|case
name|NO_CHANGE
case|:
case|case
name|FAST_FORWARD
case|:
case|case
name|RENAMED
case|:
case|case
name|NOT_ATTEMPTED
case|:
case|case
name|IO_FAILURE
case|:
case|case
name|REJECTED
case|:
case|case
name|REJECTED_CURRENT_BRANCH
case|:
case|case
name|REJECTED_MISSING_OBJECT
case|:
case|case
name|REJECTED_OTHER_REASON
case|:
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to delete "
operator|+
name|refName
operator|+
literal|": "
operator|+
name|ru
operator|.
name|getResult
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|RefUpdateUtil ()
specifier|private
name|RefUpdateUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

