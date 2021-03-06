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
DECL|package|com.google.gerrit.server.restapi.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|project
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
name|collect
operator|.
name|ImmutableSet
operator|.
name|toImmutableSet
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
name|entities
operator|.
name|RefNames
operator|.
name|isConfigRef
import|;
end_import

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
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|R_REFS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|R_TAGS
import|;
end_import

begin_import
import|import static
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
name|Type
operator|.
name|DELETE
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
name|ImmutableSet
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
name|flogger
operator|.
name|FluentLogger
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
name|entities
operator|.
name|Project
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
name|exceptions
operator|.
name|StorageException
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
name|git
operator|.
name|LockFailureException
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|permissions
operator|.
name|RefPermission
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
name|project
operator|.
name|RefValidationHelper
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteRef
specifier|public
class|class
name|DeleteRef
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
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
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|referenceUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|referenceUpdated
decl_stmt|;
DECL|field|refDeletionValidator
specifier|private
specifier|final
name|RefValidationHelper
name|refDeletionValidator
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
annotation|@
name|Inject
DECL|method|DeleteRef ( Provider<IdentifiedUser> identifiedUser, PermissionBackend permissionBackend, GitRepositoryManager repoManager, GitReferenceUpdated referenceUpdated, RefValidationHelper.Factory refDeletionValidatorFactory, Provider<InternalChangeQuery> queryProvider)
name|DeleteRef
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|GitReferenceUpdated
name|referenceUpdated
parameter_list|,
name|RefValidationHelper
operator|.
name|Factory
name|refDeletionValidatorFactory
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
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
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|referenceUpdated
operator|=
name|referenceUpdated
expr_stmt|;
name|this
operator|.
name|refDeletionValidator
operator|=
name|refDeletionValidatorFactory
operator|.
name|create
argument_list|(
name|DELETE
argument_list|)
expr_stmt|;
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
block|}
comment|/**    * Deletes a single ref from the repository.    *    * @param projectState the {@code ProjectState} of the project containing the target ref.    * @param ref the ref to be deleted.    * @throws IOException    * @throws ResourceConflictException    */
DECL|method|deleteSingleRef (ProjectState projectState, String ref)
specifier|public
name|void
name|deleteSingleRef
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceConflictException
throws|,
name|AuthException
throws|,
name|PermissionBackendException
block|{
name|deleteSingleRef
argument_list|(
name|projectState
argument_list|,
name|ref
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * Deletes a single ref from the repository.    *    * @param projectState the {@code ProjectState} of the project containing the target ref.    * @param ref the ref to be deleted.    * @param prefix the prefix of the ref.    * @throws IOException    * @throws ResourceConflictException    */
DECL|method|deleteSingleRef (ProjectState projectState, String ref, @Nullable String prefix)
specifier|public
name|void
name|deleteSingleRef
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|String
name|ref
parameter_list|,
annotation|@
name|Nullable
name|String
name|prefix
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceConflictException
throws|,
name|AuthException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
operator|!
name|ref
operator|.
name|startsWith
argument_list|(
name|R_REFS
argument_list|)
condition|)
block|{
name|ref
operator|=
name|prefix
operator|+
name|ref
expr_stmt|;
block|}
name|projectState
operator|.
name|checkStatePermitsWrite
argument_list|()
expr_stmt|;
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|project
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|ref
argument_list|(
name|ref
argument_list|)
operator|.
name|check
argument_list|(
name|RefPermission
operator|.
name|DELETE
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|repository
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|)
block|{
name|RefUpdate
operator|.
name|Result
name|result
decl_stmt|;
name|RefUpdate
name|u
init|=
name|repository
operator|.
name|updateRef
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|u
operator|.
name|setExpectedOldObjectId
argument_list|(
name|repository
operator|.
name|exactRef
argument_list|(
name|ref
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setNewObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|refDeletionValidator
operator|.
name|validateRefOperation
argument_list|(
name|projectState
operator|.
name|getName
argument_list|()
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
argument_list|,
name|u
argument_list|)
expr_stmt|;
name|result
operator|=
name|u
operator|.
name|delete
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|NEW
case|:
case|case
name|NO_CHANGE
case|:
case|case
name|FAST_FORWARD
case|:
case|case
name|FORCED
case|:
name|referenceUpdated
operator|.
name|fire
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|u
argument_list|,
name|ReceiveCommand
operator|.
name|Type
operator|.
name|DELETE
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REJECTED_CURRENT_BRANCH
case|:
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Cannot delete current branch %s: %s"
argument_list|,
name|ref
argument_list|,
name|result
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"cannot delete current branch"
argument_list|)
throw|;
case|case
name|LOCK_FAILURE
case|:
throw|throw
operator|new
name|LockFailureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot delete %s"
argument_list|,
name|ref
argument_list|)
argument_list|,
name|u
argument_list|)
throw|;
case|case
name|IO_FAILURE
case|:
case|case
name|NOT_ATTEMPTED
case|:
case|case
name|REJECTED
case|:
case|case
name|RENAMED
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
name|StorageException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot delete %s: %s"
argument_list|,
name|ref
argument_list|,
name|result
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * Deletes a set of refs from the repository.    *    * @param projectState the {@code ProjectState} of the project whose refs are to be deleted.    * @param refsToDelete the refs to be deleted.    * @param prefix the prefix of the refs.    * @throws IOException    * @throws ResourceConflictException    * @throws PermissionBackendException    */
DECL|method|deleteMultipleRefs ( ProjectState projectState, ImmutableSet<String> refsToDelete, String prefix)
specifier|public
name|void
name|deleteMultipleRefs
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|refsToDelete
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceConflictException
throws|,
name|PermissionBackendException
throws|,
name|AuthException
block|{
if|if
condition|(
name|refsToDelete
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|refsToDelete
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|deleteSingleRef
argument_list|(
name|projectState
argument_list|,
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|refsToDelete
argument_list|)
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
init|(
name|Repository
name|repository
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|)
block|{
name|BatchRefUpdate
name|batchUpdate
init|=
name|repository
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
name|batchUpdate
operator|.
name|setAtomic
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|refs
init|=
name|prefix
operator|==
literal|null
condition|?
name|refsToDelete
else|:
name|refsToDelete
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ref
lambda|->
name|ref
operator|.
name|startsWith
argument_list|(
name|R_REFS
argument_list|)
condition|?
name|ref
else|:
name|prefix
operator|+
name|ref
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|ref
range|:
name|refs
control|)
block|{
name|batchUpdate
operator|.
name|addCommand
argument_list|(
name|createDeleteCommand
argument_list|(
name|projectState
argument_list|,
name|repository
argument_list|,
name|ref
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
name|repository
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
name|projectState
operator|.
name|getNameKey
argument_list|()
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
block|}
DECL|method|createDeleteCommand ( ProjectState projectState, Repository r, String refName)
specifier|private
name|ReceiveCommand
name|createDeleteCommand
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|Repository
name|r
parameter_list|,
name|String
name|refName
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceConflictException
throws|,
name|PermissionBackendException
block|{
name|Ref
name|ref
init|=
name|r
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|refName
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
name|refName
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
if|if
condition|(
name|isConfigRef
argument_list|(
name|refName
argument_list|)
condition|)
block|{
comment|// Never allow to delete the meta config branch.
name|command
operator|.
name|setResult
argument_list|(
name|Result
operator|.
name|REJECTED_OTHER_REASON
argument_list|,
literal|"not allowed to delete branch "
operator|+
name|refName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|project
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|ref
argument_list|(
name|refName
argument_list|)
operator|.
name|check
argument_list|(
name|RefPermission
operator|.
name|DELETE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|denied
parameter_list|)
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
block|}
if|if
condition|(
operator|!
name|projectState
operator|.
name|statePermitsWrite
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
literal|"project state does not permit write"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|refName
operator|.
name|startsWith
argument_list|(
name|R_TAGS
argument_list|)
condition|)
block|{
name|BranchNameKey
name|branchKey
init|=
name|BranchNameKey
operator|.
name|create
argument_list|(
name|projectState
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
block|}
name|RefUpdate
name|u
init|=
name|r
operator|.
name|updateRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|u
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|u
operator|.
name|setExpectedOldObjectId
argument_list|(
name|r
operator|.
name|exactRef
argument_list|(
name|refName
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setNewObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|refDeletionValidator
operator|.
name|validateRefOperation
argument_list|(
name|projectState
operator|.
name|getName
argument_list|()
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
argument_list|,
name|u
argument_list|)
expr_stmt|;
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
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
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
DECL|method|postDeletion (Project.NameKey project, ReceiveCommand cmd)
specifier|private
name|void
name|postDeletion
parameter_list|(
name|Project
operator|.
name|NameKey
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
argument_list|,
name|cmd
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

