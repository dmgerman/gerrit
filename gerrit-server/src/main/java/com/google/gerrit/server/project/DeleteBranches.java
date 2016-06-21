begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
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
name|gerrit
operator|.
name|common
operator|.
name|ChangeHooks
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
name|projects
operator|.
name|DeleteBranchesInput
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
name|restapi
operator|.
name|ResourceConflictException
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
name|restapi
operator|.
name|Response
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
name|restapi
operator|.
name|RestModifyView
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|Provider
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
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteBranches
class|class
name|DeleteBranches
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|DeleteBranchesInput
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DeleteBranches
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|identifiedUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
DECL|field|referenceUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|referenceUpdated
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteBranches (Provider<IdentifiedUser> identifiedUser, GitRepositoryManager repoManager, Provider<InternalChangeQuery> queryProvider, GitReferenceUpdated referenceUpdated, ChangeHooks hooks)
name|DeleteBranches
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|GitReferenceUpdated
name|referenceUpdated
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|)
block|{
name|this
operator|.
name|identifiedUser
operator|=
name|identifiedUser
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
name|this
operator|.
name|referenceUpdated
operator|=
name|referenceUpdated
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource project, DeleteBranchesInput input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|DeleteBranchesInput
name|input
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|ResourceConflictException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|DeleteBranchesInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|branches
operator|==
literal|null
condition|)
block|{
name|input
operator|.
name|branches
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|Repository
name|r
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|)
block|{
name|BatchRefUpdate
name|batchUpdate
init|=
name|r
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|branch
range|:
name|input
operator|.
name|branches
control|)
block|{
name|batchUpdate
operator|.
name|addCommand
argument_list|(
name|createDeleteCommand
argument_list|(
name|project
argument_list|,
name|r
argument_list|,
name|branch
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|r
argument_list|)
init|)
block|{
name|batchUpdate
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
block|}
name|StringBuilder
name|errorMessages
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|ReceiveCommand
name|command
range|:
name|batchUpdate
operator|.
name|getCommands
argument_list|()
control|)
block|{
if|if
condition|(
name|command
operator|.
name|getResult
argument_list|()
operator|==
name|Result
operator|.
name|OK
condition|)
block|{
name|postDeletion
argument_list|(
name|project
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendAndLogErrorMessage
argument_list|(
name|errorMessages
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|errorMessages
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|errorMessages
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
DECL|method|createDeleteCommand (ProjectResource project, Repository r, String branch)
specifier|private
name|ReceiveCommand
name|createDeleteCommand
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|Repository
name|r
parameter_list|,
name|String
name|branch
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|Ref
name|ref
init|=
name|r
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRef
argument_list|(
name|branch
argument_list|)
decl_stmt|;
name|ReceiveCommand
name|command
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
name|command
operator|=
operator|new
name|ReceiveCommand
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|branch
argument_list|)
expr_stmt|;
name|command
operator|.
name|setResult
argument_list|(
name|Result
operator|.
name|REJECTED_OTHER_REASON
argument_list|,
literal|"it doesn't exist or you do not have permission to delete it"
argument_list|)
expr_stmt|;
return|return
name|command
return|;
block|}
name|command
operator|=
operator|new
name|ReceiveCommand
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Branch
operator|.
name|NameKey
name|branchKey
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|project
operator|.
name|getControl
argument_list|()
operator|.
name|controlForRef
argument_list|(
name|branchKey
argument_list|)
operator|.
name|canDelete
argument_list|()
condition|)
block|{
name|command
operator|.
name|setResult
argument_list|(
name|Result
operator|.
name|REJECTED_OTHER_REASON
argument_list|,
literal|"it doesn't exist or you do not have permission to delete it"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|setLimit
argument_list|(
literal|1
argument_list|)
operator|.
name|byBranchOpen
argument_list|(
name|branchKey
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|command
operator|.
name|setResult
argument_list|(
name|Result
operator|.
name|REJECTED_OTHER_REASON
argument_list|,
literal|"it has open changes"
argument_list|)
expr_stmt|;
block|}
return|return
name|command
return|;
block|}
DECL|method|appendAndLogErrorMessage (StringBuilder errorMessages, ReceiveCommand cmd)
specifier|private
name|void
name|appendAndLogErrorMessage
parameter_list|(
name|StringBuilder
name|errorMessages
parameter_list|,
name|ReceiveCommand
name|cmd
parameter_list|)
block|{
name|String
name|msg
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
condition|)
block|{
case|case
name|REJECTED_CURRENT_BRANCH
case|:
name|msg
operator|=
name|format
argument_list|(
literal|"Cannot delete %s: it is the current branch"
argument_list|,
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REJECTED_OTHER_REASON
case|:
name|msg
operator|=
name|format
argument_list|(
literal|"Cannot delete %s: %s"
argument_list|,
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|,
name|cmd
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|LOCK_FAILURE
case|:
case|case
name|NOT_ATTEMPTED
case|:
case|case
name|OK
case|:
case|case
name|REJECTED_MISSING_OBJECT
case|:
case|case
name|REJECTED_NOCREATE
case|:
case|case
name|REJECTED_NODELETE
case|:
case|case
name|REJECTED_NONFASTFORWARD
case|:
default|default:
name|msg
operator|=
name|format
argument_list|(
literal|"Cannot delete %s: %s"
argument_list|,
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|,
name|cmd
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
name|log
operator|.
name|error
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|errorMessages
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|errorMessages
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|postDeletion (ProjectResource project, ReceiveCommand cmd)
specifier|private
name|void
name|postDeletion
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|ReceiveCommand
name|cmd
parameter_list|)
block|{
name|referenceUpdated
operator|.
name|fire
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|cmd
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
name|Branch
operator|.
name|NameKey
name|branchKey
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
name|hooks
operator|.
name|doRefUpdatedHook
argument_list|(
name|branchKey
argument_list|,
name|cmd
operator|.
name|getOldId
argument_list|()
argument_list|,
name|cmd
operator|.
name|getNewId
argument_list|()
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

