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
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|common
operator|.
name|primitives
operator|.
name|Ints
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
name|RestApiException
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
name|UnprocessableEntityException
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
name|notedb
operator|.
name|ChangeNotes
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

begin_comment
comment|/** Utility methods related to rebasing changes. */
end_comment

begin_class
DECL|class|RebaseUtil
specifier|public
class|class
name|RebaseUtil
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
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
DECL|field|notesFactory
specifier|private
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|RebaseUtil ( Provider<InternalChangeQuery> queryProvider, ChangeNotes.Factory notesFactory, PatchSetUtil psUtil)
name|RebaseUtil
parameter_list|(
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|)
block|{
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
name|this
operator|.
name|notesFactory
operator|=
name|notesFactory
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
block|}
DECL|method|canRebase (PatchSet patchSet, Branch.NameKey dest, Repository git, RevWalk rw)
specifier|public
name|boolean
name|canRebase
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|,
name|Branch
operator|.
name|NameKey
name|dest
parameter_list|,
name|Repository
name|git
parameter_list|,
name|RevWalk
name|rw
parameter_list|)
block|{
try|try
block|{
name|findBaseRevision
argument_list|(
name|patchSet
argument_list|,
name|dest
argument_list|,
name|git
argument_list|,
name|rw
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|RestApiException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|StorageException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Error checking if patch set %s on %s can be rebased"
argument_list|,
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|,
name|dest
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|AutoValue
DECL|class|Base
specifier|public
specifier|abstract
specifier|static
class|class
name|Base
block|{
DECL|method|create (ChangeNotes notes, PatchSet ps)
specifier|private
specifier|static
name|Base
name|create
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
name|ps
parameter_list|)
block|{
if|if
condition|(
name|notes
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|AutoValue_RebaseUtil_Base
argument_list|(
name|notes
argument_list|,
name|ps
argument_list|)
return|;
block|}
DECL|method|notes ()
specifier|public
specifier|abstract
name|ChangeNotes
name|notes
parameter_list|()
function_decl|;
DECL|method|patchSet ()
specifier|public
specifier|abstract
name|PatchSet
name|patchSet
parameter_list|()
function_decl|;
block|}
DECL|method|parseBase (RevisionResource rsrc, String base)
specifier|public
name|Base
name|parseBase
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|String
name|base
parameter_list|)
block|{
comment|// Try parsing the base as a ref string.
name|PatchSet
operator|.
name|Id
name|basePatchSetId
init|=
name|PatchSet
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|base
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePatchSetId
operator|!=
literal|null
condition|)
block|{
name|Change
operator|.
name|Id
name|baseChangeId
init|=
name|basePatchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|ChangeNotes
name|baseNotes
init|=
name|notesFor
argument_list|(
name|rsrc
argument_list|,
name|baseChangeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseNotes
operator|!=
literal|null
condition|)
block|{
return|return
name|Base
operator|.
name|create
argument_list|(
name|notesFor
argument_list|(
name|rsrc
argument_list|,
name|basePatchSetId
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|,
name|psUtil
operator|.
name|get
argument_list|(
name|baseNotes
argument_list|,
name|basePatchSetId
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|// Try parsing base as a change number (assume current patch set).
name|Integer
name|baseChangeId
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|base
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseChangeId
operator|!=
literal|null
condition|)
block|{
name|ChangeNotes
name|baseNotes
init|=
name|notesFor
argument_list|(
name|rsrc
argument_list|,
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|baseChangeId
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseNotes
operator|!=
literal|null
condition|)
block|{
return|return
name|Base
operator|.
name|create
argument_list|(
name|baseNotes
argument_list|,
name|psUtil
operator|.
name|current
argument_list|(
name|baseNotes
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|// Try parsing as SHA-1.
name|Base
name|ret
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byProjectCommit
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|base
argument_list|)
control|)
block|{
for|for
control|(
name|PatchSet
name|ps
range|:
name|cd
operator|.
name|patchSets
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|matches
argument_list|(
name|base
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|ret
operator|==
literal|null
operator|||
name|ret
operator|.
name|patchSet
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|<
name|ps
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
condition|)
block|{
name|ret
operator|=
name|Base
operator|.
name|create
argument_list|(
name|cd
operator|.
name|notes
argument_list|()
argument_list|,
name|ps
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|ret
return|;
block|}
DECL|method|notesFor (RevisionResource rsrc, Change.Id id)
specifier|private
name|ChangeNotes
name|notesFor
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|rsrc
operator|.
name|getNotes
argument_list|()
return|;
block|}
return|return
name|notesFactory
operator|.
name|createChecked
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
comment|/**    * Find the commit onto which a patch set should be rebased.    *    *<p>This is defined as the latest patch set of the change corresponding to this commit's parent,    * or the destination branch tip in the case where the parent's change is merged.    *    * @param patchSet patch set for which the new base commit should be found.    * @param destBranch the destination branch.    * @param git the repository.    * @param rw the RevWalk.    * @return the commit onto which the patch set should be rebased.    * @throws RestApiException if rebase is not possible.    * @throws IOException if accessing the repository fails.    */
DECL|method|findBaseRevision ( PatchSet patchSet, Branch.NameKey destBranch, Repository git, RevWalk rw)
specifier|public
name|ObjectId
name|findBaseRevision
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|,
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
name|Repository
name|git
parameter_list|,
name|RevWalk
name|rw
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
block|{
name|String
name|baseRev
init|=
literal|null
decl_stmt|;
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|commit
operator|.
name|getParentCount
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"Cannot rebase a change with multiple parents."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|commit
operator|.
name|getParentCount
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"Cannot rebase a change without any parents (is this the initial commit?)."
argument_list|)
throw|;
block|}
name|RevId
name|parentRev
init|=
operator|new
name|RevId
argument_list|(
name|commit
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|CHANGES
label|:
for|for
control|(
name|ChangeData
name|cd
range|:
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byBranchCommit
argument_list|(
name|destBranch
argument_list|,
name|parentRev
operator|.
name|get
argument_list|()
argument_list|)
control|)
block|{
for|for
control|(
name|PatchSet
name|depPatchSet
range|:
name|cd
operator|.
name|patchSets
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|depPatchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|parentRev
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Change
name|depChange
init|=
name|cd
operator|.
name|change
argument_list|()
decl_stmt|;
if|if
condition|(
name|depChange
operator|.
name|isAbandoned
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot rebase a change with an abandoned parent: "
operator|+
name|depChange
operator|.
name|getKey
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|depChange
operator|.
name|isNew
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
name|ResourceConflictException
argument_list|(
literal|"Change is already based on the latest patch set of the dependent change."
argument_list|)
throw|;
block|}
name|baseRev
operator|=
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
expr_stmt|;
block|}
break|break
name|CHANGES
break|;
block|}
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
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|destBranch
operator|.
name|branch
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
name|UnprocessableEntityException
argument_list|(
literal|"The destination branch does not exist: "
operator|+
name|destBranch
operator|.
name|branch
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
name|parentRev
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Change is already up to date."
argument_list|)
throw|;
block|}
block|}
return|return
name|ObjectId
operator|.
name|fromString
argument_list|(
name|baseRev
argument_list|)
return|;
block|}
block|}
end_class

end_unit

