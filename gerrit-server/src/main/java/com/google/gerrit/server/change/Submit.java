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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|common
operator|.
name|data
operator|.
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
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
name|base
operator|.
name|Optional
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
name|base
operator|.
name|Predicate
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
name|Iterables
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|extensions
operator|.
name|webui
operator|.
name|UiAction
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
name|PatchSetApproval
operator|.
name|LabelId
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
name|ProjectUtil
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
name|Submit
operator|.
name|Input
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
name|MergeQueue
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
name|gwtorm
operator|.
name|server
operator|.
name|AtomicUpdate
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

begin_class
DECL|class|Submit
specifier|public
class|class
name|Submit
implements|implements
name|RestModifyView
argument_list|<
name|RevisionResource
argument_list|,
name|Input
argument_list|>
implements|,
name|UiAction
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|waitForMerge
specifier|public
name|boolean
name|waitForMerge
decl_stmt|;
block|}
DECL|enum|Status
specifier|public
enum|enum
name|Status
block|{
DECL|enumConstant|SUBMITTED
DECL|enumConstant|MERGED
name|SUBMITTED
block|,
name|MERGED
block|;   }
DECL|class|Output
specifier|public
specifier|static
class|class
name|Output
block|{
DECL|field|status
specifier|public
name|Status
name|status
decl_stmt|;
DECL|field|change
specifier|transient
name|Change
name|change
decl_stmt|;
DECL|method|Output (Status s, Change c)
specifier|private
name|Output
parameter_list|(
name|Status
name|s
parameter_list|,
name|Change
name|c
parameter_list|)
block|{
name|status
operator|=
name|s
expr_stmt|;
name|change
operator|=
name|c
expr_stmt|;
block|}
block|}
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|mergeQueue
specifier|private
specifier|final
name|MergeQueue
name|mergeQueue
decl_stmt|;
DECL|field|indexer
specifier|private
specifier|final
name|ChangeIndexer
name|indexer
decl_stmt|;
annotation|@
name|Inject
DECL|method|Submit (Provider<ReviewDb> dbProvider, GitRepositoryManager repoManager, MergeQueue mergeQueue, ChangeIndexer indexer)
name|Submit
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|MergeQueue
name|mergeQueue
parameter_list|,
name|ChangeIndexer
name|indexer
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|mergeQueue
operator|=
name|mergeQueue
expr_stmt|;
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc, Input input)
specifier|public
name|Output
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceConflictException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|ChangeControl
name|control
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|IdentifiedUser
name|caller
init|=
operator|(
name|IdentifiedUser
operator|)
name|control
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
name|Change
name|change
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|canSubmit
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"submit not permitted"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is "
operator|+
name|status
argument_list|(
name|change
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|ProjectUtil
operator|.
name|branchExists
argument_list|(
name|repoManager
argument_list|,
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"destination branch \"%s\" not found."
argument_list|,
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
condition|)
block|{
comment|// TODO Allow submitting non-current revision by changing the current.
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"revision %s is not current revision"
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|checkSubmitRule
argument_list|(
name|rsrc
argument_list|)
expr_stmt|;
name|change
operator|=
name|submit
argument_list|(
name|rsrc
argument_list|,
name|caller
argument_list|)
expr_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is "
operator|+
name|status
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|waitForMerge
condition|)
block|{
name|mergeQueue
operator|.
name|merge
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|=
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mergeQueue
operator|.
name|schedule
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is deleted"
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|change
operator|.
name|getStatus
argument_list|()
condition|)
block|{
case|case
name|SUBMITTED
case|:
return|return
operator|new
name|Output
argument_list|(
name|Status
operator|.
name|SUBMITTED
argument_list|,
name|change
argument_list|)
return|;
case|case
name|MERGED
case|:
return|return
operator|new
name|Output
argument_list|(
name|Status
operator|.
name|MERGED
argument_list|,
name|change
argument_list|)
return|;
case|case
name|NEW
case|:
name|ChangeMessage
name|msg
init|=
name|getConflictMessage
argument_list|(
name|rsrc
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|msg
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
default|default:
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is "
operator|+
name|status
argument_list|(
name|change
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getDescription (RevisionResource resource)
specifier|public
name|UiAction
operator|.
name|Description
name|getDescription
parameter_list|(
name|RevisionResource
name|resource
parameter_list|)
block|{
name|PatchSet
operator|.
name|Id
name|current
init|=
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setTitle
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Submit Revision %d"
argument_list|,
name|resource
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setVisible
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
operator|&&
name|resource
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|current
argument_list|)
operator|&&
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|canSubmit
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * If the merge was attempted and it failed the system usually writes a    * comment as a ChangeMessage and sets status to NEW. Find the relevant    * message and return it.    */
DECL|method|getConflictMessage (RevisionResource rsrc)
specifier|public
name|ChangeMessage
name|getConflictMessage
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Timestamp
name|before
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getLastUpdatedOn
argument_list|()
decl_stmt|;
name|ChangeMessage
name|msg
init|=
name|Iterables
operator|.
name|getFirst
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|Lists
operator|.
name|reverse
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|changeMessages
argument_list|()
operator|.
name|byChange
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Predicate
argument_list|<
name|ChangeMessage
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|ChangeMessage
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|getAuthor
argument_list|()
operator|==
literal|null
operator|&&
name|input
operator|.
name|getWrittenOn
argument_list|()
operator|.
name|getTime
argument_list|()
operator|>=
name|before
operator|.
name|getTime
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|msg
return|;
block|}
DECL|method|submit (RevisionResource rsrc, IdentifiedUser caller)
specifier|public
name|Change
name|submit
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|IdentifiedUser
name|caller
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Timestamp
name|timestamp
init|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|Change
name|change
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|ReviewDb
name|db
init|=
name|dbProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|db
operator|.
name|changes
argument_list|()
operator|.
name|beginTransaction
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|approve
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|,
name|caller
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
name|change
operator|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|atomicUpdate
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|AtomicUpdate
argument_list|<
name|Change
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Change
name|update
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|change
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|SUBMITTED
argument_list|)
expr_stmt|;
name|change
operator|.
name|setLastUpdatedOn
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|ChangeUtil
operator|.
name|computeSortKey
argument_list|(
name|change
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|db
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
name|indexer
operator|.
name|index
argument_list|(
name|change
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
DECL|method|approve (PatchSet rev, IdentifiedUser caller, Timestamp timestamp)
specifier|private
name|void
name|approve
parameter_list|(
name|PatchSet
name|rev
parameter_list|,
name|IdentifiedUser
name|caller
parameter_list|,
name|Timestamp
name|timestamp
parameter_list|)
throws|throws
name|OrmException
block|{
name|PatchSetApproval
name|submit
init|=
name|Iterables
operator|.
name|getFirst
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byPatchSetUser
argument_list|(
name|rev
operator|.
name|getId
argument_list|()
argument_list|,
name|caller
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Predicate
argument_list|<
name|PatchSetApproval
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|PatchSetApproval
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|isSubmit
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|submit
operator|==
literal|null
condition|)
block|{
name|submit
operator|=
operator|new
name|PatchSetApproval
argument_list|(
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
name|rev
operator|.
name|getId
argument_list|()
argument_list|,
name|caller
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|LabelId
operator|.
name|SUBMIT
argument_list|)
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
block|}
name|submit
operator|.
name|setValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|submit
operator|.
name|setGranted
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|upsert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|submit
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|checkSubmitRule (RevisionResource rsrc)
specifier|private
name|void
name|checkSubmitRule
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|ResourceConflictException
block|{
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|results
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|canSubmit
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|SubmitRecord
argument_list|>
name|ok
init|=
name|findOkRecord
argument_list|(
name|results
argument_list|)
decl_stmt|;
if|if
condition|(
name|ok
operator|.
name|isPresent
argument_list|()
condition|)
block|{
comment|// Rules supplied a valid solution.
return|return;
block|}
elseif|else
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"ChangeControl.canSubmit returned empty list for %s in %s"
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
for|for
control|(
name|SubmitRecord
name|record
range|:
name|results
control|)
block|{
switch|switch
condition|(
name|record
operator|.
name|status
condition|)
block|{
case|case
name|CLOSED
case|:
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is closed"
argument_list|)
throw|;
case|case
name|RULE_ERROR
case|:
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"rule error: %s"
argument_list|,
name|record
operator|.
name|errorMessage
argument_list|)
argument_list|)
throw|;
case|case
name|NOT_READY
case|:
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|SubmitRecord
operator|.
name|Label
name|lbl
range|:
name|record
operator|.
name|labels
control|)
block|{
switch|switch
condition|(
name|lbl
operator|.
name|status
condition|)
block|{
case|case
name|OK
case|:
case|case
name|MAY
case|:
continue|continue;
case|case
name|REJECT
case|:
if|if
condition|(
name|msg
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
name|msg
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"blocked by "
operator|+
name|lbl
operator|.
name|label
argument_list|)
expr_stmt|;
continue|continue;
case|case
name|NEED
case|:
if|if
condition|(
name|msg
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
name|msg
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"needs "
operator|+
name|lbl
operator|.
name|label
argument_list|)
expr_stmt|;
continue|continue;
case|case
name|IMPOSSIBLE
case|:
if|if
condition|(
name|msg
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
name|msg
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"needs "
operator|+
name|lbl
operator|.
name|label
operator|+
literal|" (check project access)"
argument_list|)
expr_stmt|;
continue|continue;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unsupported SubmitRecord.Label %s for %s in %s"
argument_list|,
name|lbl
operator|.
name|toString
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unsupported SubmitRecord %s for %s in %s"
argument_list|,
name|record
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|findOkRecord (Collection<SubmitRecord> in)
specifier|private
specifier|static
name|Optional
argument_list|<
name|SubmitRecord
argument_list|>
name|findOkRecord
parameter_list|(
name|Collection
argument_list|<
name|SubmitRecord
argument_list|>
name|in
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|tryFind
argument_list|(
name|in
argument_list|,
operator|new
name|Predicate
argument_list|<
name|SubmitRecord
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|SubmitRecord
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|status
operator|==
name|OK
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|status (Change change)
specifier|static
name|String
name|status
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
return|return
name|change
operator|!=
literal|null
condition|?
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
else|:
literal|"deleted"
return|;
block|}
DECL|class|CurrentRevision
specifier|public
specifier|static
class|class
name|CurrentRevision
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|submit
specifier|private
specifier|final
name|Submit
name|submit
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
name|json
decl_stmt|;
annotation|@
name|Inject
DECL|method|CurrentRevision (Provider<ReviewDb> dbProvider, Submit submit, ChangeJson json)
name|CurrentRevision
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|Submit
name|submit
parameter_list|,
name|ChangeJson
name|json
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|submit
operator|=
name|submit
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, Input input)
specifier|public
name|Object
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceConflictException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|PatchSet
name|ps
init|=
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ps
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"current revision is missing"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|isPatchVisible
argument_list|(
name|ps
argument_list|,
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"current revision not accessible"
argument_list|)
throw|;
block|}
name|Output
name|out
init|=
name|submit
operator|.
name|apply
argument_list|(
operator|new
name|RevisionResource
argument_list|(
name|rsrc
argument_list|,
name|ps
argument_list|)
argument_list|,
name|input
argument_list|)
decl_stmt|;
return|return
name|json
operator|.
name|format
argument_list|(
name|out
operator|.
name|change
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

