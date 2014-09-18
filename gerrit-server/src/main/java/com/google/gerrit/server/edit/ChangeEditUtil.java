begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.edit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|edit
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
name|checkArgument
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|RefNames
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Utility functions to manipulate change edits.  *<p>  * This class contains methods to retrieve, publish and delete edits.  * For changing edits see {@link ChangeEditModifier}.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeEditUtil
specifier|public
class|class
name|ChangeEditUtil
block|{
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|patchSetInserterFactory
specifier|private
specifier|final
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
decl_stmt|;
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
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeEditUtil (GitRepositoryManager gitManager, PatchSetInserter.Factory patchSetInserterFactory, ChangeControl.GenericFactory changeControlFactory, Provider<ReviewDb> db, Provider<CurrentUser> user)
name|ChangeEditUtil
parameter_list|(
name|GitRepositoryManager
name|gitManager
parameter_list|,
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
parameter_list|,
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|)
block|{
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
name|this
operator|.
name|patchSetInserterFactory
operator|=
name|patchSetInserterFactory
expr_stmt|;
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
name|user
operator|=
name|user
expr_stmt|;
block|}
comment|/**    * Retrieve edits for a change and user. Max. one change edit can    * exist per user and change.    *    * @param change    * @return edit for this change for this user, if present.    * @throws AuthException    * @throws IOException    */
DECL|method|byChange (Change change)
specifier|public
name|Optional
argument_list|<
name|ChangeEdit
argument_list|>
name|byChange
parameter_list|(
name|Change
name|change
parameter_list|)
throws|throws
name|AuthException
throws|,
name|IOException
throws|,
name|InvalidChangeOperationException
block|{
if|if
condition|(
operator|!
name|user
operator|.
name|get
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
name|Repository
name|repo
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
name|IdentifiedUser
name|me
init|=
operator|(
name|IdentifiedUser
operator|)
name|user
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|editRefPrefix
init|=
name|editRefPrefix
argument_list|(
name|me
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|editRefPrefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
comment|// TODO(davido): Rather than failing when we encounter the corrupt state
comment|// where there is more than one ref, we could silently delete all but the
comment|// current one.
name|Ref
name|ref
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|refs
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSet
name|basePs
init|=
name|getBasePatchSet
argument_list|(
name|change
argument_list|,
name|ref
argument_list|)
decl_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|ChangeEdit
argument_list|(
name|me
argument_list|,
name|change
argument_list|,
name|ref
argument_list|,
name|commit
argument_list|,
name|basePs
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Promote change edit to patch set, by squashing the edit into    * its parent.    *    * @param edit change edit to publish    * @throws AuthException    * @throws NoSuchChangeException    * @throws IOException    * @throws InvalidChangeOperationException    * @throws OrmException    * @throws ResourceConflictException    */
DECL|method|publish (ChangeEdit edit)
specifier|public
name|void
name|publish
parameter_list|(
name|ChangeEdit
name|edit
parameter_list|)
throws|throws
name|AuthException
throws|,
name|NoSuchChangeException
throws|,
name|IOException
throws|,
name|InvalidChangeOperationException
throws|,
name|OrmException
throws|,
name|ResourceConflictException
block|{
name|Change
name|change
init|=
name|edit
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|Repository
name|repo
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
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|ObjectInserter
name|inserter
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
decl_stmt|;
try|try
block|{
name|PatchSet
name|basePatchSet
init|=
name|edit
operator|.
name|getBasePatchSet
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|basePatchSet
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
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"only edit for current patch set can be published"
argument_list|)
throw|;
block|}
name|insertPatchSet
argument_list|(
name|edit
argument_list|,
name|change
argument_list|,
name|repo
argument_list|,
name|rw
argument_list|,
name|basePatchSet
argument_list|,
name|squashEdit
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|inserter
argument_list|,
name|edit
operator|.
name|getEditCommit
argument_list|()
argument_list|,
name|basePatchSet
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|inserter
operator|.
name|release
argument_list|()
expr_stmt|;
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
comment|// TODO(davido): This should happen in the same BatchRefUpdate.
name|deleteRef
argument_list|(
name|repo
argument_list|,
name|edit
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Delete change edit.    *    * @param edit change edit to delete    * @throws IOException    */
DECL|method|delete (ChangeEdit edit)
specifier|public
name|void
name|delete
parameter_list|(
name|ChangeEdit
name|edit
parameter_list|)
throws|throws
name|IOException
block|{
name|Change
name|change
init|=
name|edit
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|Repository
name|repo
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
name|deleteRef
argument_list|(
name|repo
argument_list|,
name|edit
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getBasePatchSet (Change change, Ref ref)
specifier|private
name|PatchSet
name|getBasePatchSet
parameter_list|(
name|Change
name|change
parameter_list|,
name|Ref
name|ref
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidChangeOperationException
block|{
try|try
block|{
name|int
name|pos
init|=
name|ref
operator|.
name|getName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|pos
operator|>
literal|0
argument_list|,
literal|"invalid edit ref: %s"
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|psId
init|=
name|ref
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|psId
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Returns reference for this change edit with sharded user and change number:    * refs/users/UU/UUUU/edit-CCCC/P.    *    * @param accountId accout id    * @param changeId change number    * @param psId patch set number    * @return reference for this change edit    */
DECL|method|editRefName (Account.Id accountId, Change.Id changeId, PatchSet.Id psId)
specifier|static
name|String
name|editRefName
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
return|return
name|editRefPrefix
argument_list|(
name|accountId
argument_list|,
name|changeId
argument_list|)
operator|+
name|psId
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * Returns reference prefix for this change edit with sharded user and    * change number: refs/users/UU/UUUU/edit-CCCC/.    *    * @param accountId accout id    * @param changeId change number    * @return reference prefix for this change edit    */
DECL|method|editRefPrefix (Account.Id accountId, Change.Id changeId)
specifier|static
name|String
name|editRefPrefix
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s/edit-%d/"
argument_list|,
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|accountId
argument_list|)
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|squashEdit (Repository repo, RevWalk rw, ObjectInserter inserter, RevCommit edit, PatchSet basePatchSet)
specifier|private
name|RevCommit
name|squashEdit
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|,
name|RevCommit
name|edit
parameter_list|,
name|PatchSet
name|basePatchSet
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceConflictException
block|{
name|RevCommit
name|parent
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|basePatchSet
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
name|parent
operator|.
name|getTree
argument_list|()
operator|.
name|equals
argument_list|(
name|edit
operator|.
name|getTree
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"identical tree"
argument_list|)
throw|;
block|}
return|return
name|writeSquashedCommit
argument_list|(
name|rw
argument_list|,
name|inserter
argument_list|,
name|parent
argument_list|,
name|edit
argument_list|)
return|;
block|}
DECL|method|insertPatchSet (ChangeEdit edit, Change change, Repository repo, RevWalk rw, PatchSet basePatchSet, RevCommit squashed)
specifier|private
name|void
name|insertPatchSet
parameter_list|(
name|ChangeEdit
name|edit
parameter_list|,
name|Change
name|change
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|PatchSet
name|basePatchSet
parameter_list|,
name|RevCommit
name|squashed
parameter_list|)
throws|throws
name|NoSuchChangeException
throws|,
name|InvalidChangeOperationException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|PatchSet
name|ps
init|=
operator|new
name|PatchSet
argument_list|(
name|ChangeUtil
operator|.
name|nextPatchSetId
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ps
operator|.
name|setRevision
argument_list|(
operator|new
name|RevId
argument_list|(
name|ObjectId
operator|.
name|toString
argument_list|(
name|squashed
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setUploader
argument_list|(
name|edit
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setCreatedOn
argument_list|(
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
expr_stmt|;
name|PatchSetInserter
name|insr
init|=
name|patchSetInserterFactory
operator|.
name|create
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|changeControlFactory
operator|.
name|controlFor
argument_list|(
name|change
argument_list|,
name|edit
operator|.
name|getUser
argument_list|()
argument_list|)
argument_list|,
name|squashed
argument_list|)
decl_stmt|;
name|insr
operator|.
name|setPatchSet
argument_list|(
name|ps
argument_list|)
operator|.
name|setMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Patch Set %d: Published edit on patch set %d"
argument_list|,
name|ps
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|basePatchSet
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|insert
argument_list|()
expr_stmt|;
block|}
DECL|method|deleteRef (Repository repo, ChangeEdit edit)
specifier|private
specifier|static
name|void
name|deleteRef
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ChangeEdit
name|edit
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|refName
init|=
name|edit
operator|.
name|getRefName
argument_list|()
decl_stmt|;
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|refName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|edit
operator|.
name|getRef
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|RefUpdate
operator|.
name|Result
name|result
init|=
name|ru
operator|.
name|delete
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|FORCED
case|:
case|case
name|NEW
case|:
case|case
name|NO_CHANGE
case|:
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to delete ref %s: %s"
argument_list|,
name|refName
argument_list|,
name|result
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|writeSquashedCommit (RevWalk rw, ObjectInserter inserter, RevCommit parent, RevCommit edit)
specifier|private
specifier|static
name|RevCommit
name|writeSquashedCommit
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|,
name|RevCommit
name|parent
parameter_list|,
name|RevCommit
name|edit
parameter_list|)
throws|throws
name|IOException
block|{
name|CommitBuilder
name|mergeCommit
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parent
operator|.
name|getParentCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|mergeCommit
operator|.
name|addParentId
argument_list|(
name|parent
operator|.
name|getParent
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mergeCommit
operator|.
name|setAuthor
argument_list|(
name|parent
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|mergeCommit
operator|.
name|setMessage
argument_list|(
name|parent
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
name|mergeCommit
operator|.
name|setCommitter
argument_list|(
name|edit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|mergeCommit
operator|.
name|setTreeId
argument_list|(
name|edit
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|rw
operator|.
name|parseCommit
argument_list|(
name|commit
argument_list|(
name|inserter
argument_list|,
name|mergeCommit
argument_list|)
argument_list|)
return|;
block|}
DECL|method|commit (ObjectInserter inserter, CommitBuilder mergeCommit)
specifier|private
specifier|static
name|ObjectId
name|commit
parameter_list|(
name|ObjectInserter
name|inserter
parameter_list|,
name|CommitBuilder
name|mergeCommit
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectId
name|id
init|=
name|inserter
operator|.
name|insert
argument_list|(
name|mergeCommit
argument_list|)
decl_stmt|;
name|inserter
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

