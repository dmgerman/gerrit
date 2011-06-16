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
name|PermissionRange
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
name|reviewdb
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
name|rules
operator|.
name|PrologEnvironment
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
name|rules
operator|.
name|StoredValues
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
name|gwtorm
operator|.
name|client
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
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|compiler
operator|.
name|CompileException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|IntegerTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|PrologException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|StructureTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|VariableTerm
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
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/** Access control management for a user accessing a single change. */
end_comment

begin_class
DECL|class|ChangeControl
specifier|public
class|class
name|ChangeControl
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
name|ChangeControl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|GenericFactory
specifier|public
specifier|static
class|class
name|GenericFactory
block|{
DECL|field|projectControl
specifier|private
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControl
decl_stmt|;
annotation|@
name|Inject
DECL|method|GenericFactory (ProjectControl.GenericFactory p)
name|GenericFactory
parameter_list|(
name|ProjectControl
operator|.
name|GenericFactory
name|p
parameter_list|)
block|{
name|projectControl
operator|=
name|p
expr_stmt|;
block|}
DECL|method|controlFor (Change change, CurrentUser user)
specifier|public
name|ChangeControl
name|controlFor
parameter_list|(
name|Change
name|change
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|NoSuchChangeException
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|projectKey
init|=
name|change
operator|.
name|getProject
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|projectControl
operator|.
name|controlFor
argument_list|(
name|projectKey
argument_list|,
name|user
argument_list|)
operator|.
name|controlFor
argument_list|(
name|change
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
block|{
DECL|field|projectControl
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControl
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
annotation|@
name|Inject
DECL|method|Factory (final ProjectControl.Factory p, final Provider<ReviewDb> d)
name|Factory
parameter_list|(
specifier|final
name|ProjectControl
operator|.
name|Factory
name|p
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|d
parameter_list|)
block|{
name|projectControl
operator|=
name|p
expr_stmt|;
name|db
operator|=
name|d
expr_stmt|;
block|}
DECL|method|controlFor (final Change.Id id)
specifier|public
name|ChangeControl
name|controlFor
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|NoSuchChangeException
block|{
specifier|final
name|Change
name|change
decl_stmt|;
try|try
block|{
name|change
operator|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|id
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
name|NoSuchChangeException
argument_list|(
name|id
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|id
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|controlFor
argument_list|(
name|change
argument_list|)
return|;
block|}
DECL|method|controlFor (final Change change)
specifier|public
name|ChangeControl
name|controlFor
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|)
throws|throws
name|NoSuchChangeException
block|{
try|try
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|projectKey
init|=
name|change
operator|.
name|getProject
argument_list|()
decl_stmt|;
return|return
name|projectControl
operator|.
name|validateFor
argument_list|(
name|projectKey
argument_list|)
operator|.
name|controlFor
argument_list|(
name|change
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|validateFor (final Change.Id id)
specifier|public
name|ChangeControl
name|validateFor
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|NoSuchChangeException
block|{
return|return
name|validate
argument_list|(
name|controlFor
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|validateFor (final Change change)
specifier|public
name|ChangeControl
name|validateFor
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|)
throws|throws
name|NoSuchChangeException
block|{
return|return
name|validate
argument_list|(
name|controlFor
argument_list|(
name|change
argument_list|)
argument_list|)
return|;
block|}
DECL|method|validate (final ChangeControl c)
specifier|private
specifier|static
name|ChangeControl
name|validate
parameter_list|(
specifier|final
name|ChangeControl
name|c
parameter_list|)
throws|throws
name|NoSuchChangeException
block|{
if|if
condition|(
operator|!
name|c
operator|.
name|isVisible
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|c
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|c
return|;
block|}
block|}
DECL|field|refControl
specifier|private
specifier|final
name|RefControl
name|refControl
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|method|ChangeControl (final RefControl r, final Change c)
name|ChangeControl
parameter_list|(
specifier|final
name|RefControl
name|r
parameter_list|,
specifier|final
name|Change
name|c
parameter_list|)
block|{
name|this
operator|.
name|refControl
operator|=
name|r
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|c
expr_stmt|;
block|}
DECL|method|forUser (final CurrentUser who)
specifier|public
name|ChangeControl
name|forUser
parameter_list|(
specifier|final
name|CurrentUser
name|who
parameter_list|)
block|{
return|return
operator|new
name|ChangeControl
argument_list|(
name|getRefControl
argument_list|()
operator|.
name|forUser
argument_list|(
name|who
argument_list|)
argument_list|,
name|getChange
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRefControl ()
specifier|public
name|RefControl
name|getRefControl
parameter_list|()
block|{
return|return
name|refControl
return|;
block|}
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|getRefControl
argument_list|()
operator|.
name|getCurrentUser
argument_list|()
return|;
block|}
DECL|method|getProjectControl ()
specifier|public
name|ProjectControl
name|getProjectControl
parameter_list|()
block|{
return|return
name|getRefControl
argument_list|()
operator|.
name|getProjectControl
argument_list|()
return|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|getProjectControl
argument_list|()
operator|.
name|getProject
argument_list|()
return|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
return|;
block|}
comment|/** Can this user see this change? */
DECL|method|isVisible ()
specifier|public
name|boolean
name|isVisible
parameter_list|()
block|{
return|return
name|getRefControl
argument_list|()
operator|.
name|isVisible
argument_list|()
return|;
block|}
comment|/** Can this user abandon this change? */
DECL|method|canAbandon ()
specifier|public
name|boolean
name|canAbandon
parameter_list|()
block|{
return|return
name|isOwner
argument_list|()
comment|// owner (aka creator) of the change can abandon
operator|||
name|getRefControl
argument_list|()
operator|.
name|isOwner
argument_list|()
comment|// branch owner can abandon
operator|||
name|getProjectControl
argument_list|()
operator|.
name|isOwner
argument_list|()
comment|// project owner can abandon
operator|||
name|getCurrentUser
argument_list|()
operator|.
name|isAdministrator
argument_list|()
comment|// site administers are god
return|;
block|}
comment|/** Can this user restore this change? */
DECL|method|canRestore ()
specifier|public
name|boolean
name|canRestore
parameter_list|()
block|{
return|return
name|canAbandon
argument_list|()
return|;
comment|// Anyone who can abandon the change can restore it back
block|}
comment|/** All value ranges of any allowed label permission. */
DECL|method|getLabelRanges ()
specifier|public
name|List
argument_list|<
name|PermissionRange
argument_list|>
name|getLabelRanges
parameter_list|()
block|{
return|return
name|getRefControl
argument_list|()
operator|.
name|getLabelRanges
argument_list|()
return|;
block|}
comment|/** The range of permitted values associated with a label permission. */
DECL|method|getRange (String permission)
specifier|public
name|PermissionRange
name|getRange
parameter_list|(
name|String
name|permission
parameter_list|)
block|{
return|return
name|getRefControl
argument_list|()
operator|.
name|getRange
argument_list|(
name|permission
argument_list|)
return|;
block|}
comment|/** Can this user add a patch set to this change? */
DECL|method|canAddPatchSet ()
specifier|public
name|boolean
name|canAddPatchSet
parameter_list|()
block|{
return|return
name|getRefControl
argument_list|()
operator|.
name|canUpload
argument_list|()
return|;
block|}
comment|/** Is this user the owner of the change? */
DECL|method|isOwner ()
specifier|public
name|boolean
name|isOwner
parameter_list|()
block|{
if|if
condition|(
name|getCurrentUser
argument_list|()
operator|instanceof
name|IdentifiedUser
condition|)
block|{
specifier|final
name|IdentifiedUser
name|i
init|=
operator|(
name|IdentifiedUser
operator|)
name|getCurrentUser
argument_list|()
decl_stmt|;
return|return
name|i
operator|.
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** @return true if the user is allowed to remove this reviewer. */
DECL|method|canRemoveReviewer (PatchSetApproval approval)
specifier|public
name|boolean
name|canRemoveReviewer
parameter_list|(
name|PatchSetApproval
name|approval
parameter_list|)
block|{
if|if
condition|(
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
comment|// A user can always remove themselves.
comment|//
if|if
condition|(
name|getCurrentUser
argument_list|()
operator|instanceof
name|IdentifiedUser
condition|)
block|{
specifier|final
name|IdentifiedUser
name|i
init|=
operator|(
name|IdentifiedUser
operator|)
name|getCurrentUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|.
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|approval
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
comment|// can remove self
block|}
block|}
comment|// The change owner may remove any zero or positive score.
comment|//
if|if
condition|(
name|isOwner
argument_list|()
operator|&&
literal|0
operator|<=
name|approval
operator|.
name|getValue
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// The branch owner, project owner, site admin can remove anyone.
comment|//
if|if
condition|(
name|getRefControl
argument_list|()
operator|.
name|isOwner
argument_list|()
comment|// branch owner
operator|||
name|getProjectControl
argument_list|()
operator|.
name|isOwner
argument_list|()
comment|// project owner
operator|||
name|getCurrentUser
argument_list|()
operator|.
name|isAdministrator
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** @return {@link CanSubmitResult#OK}, or a result with an error message. */
DECL|method|canSubmit (final PatchSet.Id patchSetId)
specifier|public
name|CanSubmitResult
name|canSubmit
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
block|{
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isClosed
argument_list|()
condition|)
block|{
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Change "
operator|+
name|change
operator|.
name|getId
argument_list|()
operator|+
literal|" is closed"
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|patchSetId
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
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Patch set "
operator|+
name|patchSetId
operator|+
literal|" is not current"
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|getRefControl
argument_list|()
operator|.
name|canSubmit
argument_list|()
condition|)
block|{
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"User does not have permission to submit"
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|getCurrentUser
argument_list|()
operator|instanceof
name|IdentifiedUser
operator|)
condition|)
block|{
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"User is not signed-in"
argument_list|)
return|;
block|}
return|return
name|CanSubmitResult
operator|.
name|OK
return|;
block|}
comment|/** @return {@link CanSubmitResult#OK}, or a result with an error message. */
DECL|method|canSubmit (ReviewDb db, PatchSet.Id patchSetId)
specifier|public
name|CanSubmitResult
name|canSubmit
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
block|{
name|CanSubmitResult
name|result
init|=
name|canSubmit
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
name|CanSubmitResult
operator|.
name|OK
condition|)
block|{
return|return
name|result
return|;
block|}
name|PrologEnvironment
name|env
decl_stmt|;
try|try
block|{
name|env
operator|=
name|getProjectControl
argument_list|()
operator|.
name|getProjectState
argument_list|()
operator|.
name|newPrologEnvironment
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CompileException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"cannot consult rules.pl"
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Error reading submit rule"
argument_list|)
return|;
block|}
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|REVIEW_DB
argument_list|,
name|db
argument_list|)
expr_stmt|;
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|CHANGE
argument_list|,
name|change
argument_list|)
expr_stmt|;
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|PATCH_SET_ID
argument_list|,
name|patchSetId
argument_list|)
expr_stmt|;
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|CHANGE_CONTROL
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Term
name|submitRule
init|=
name|env
operator|.
name|once
argument_list|(
literal|"gerrit"
argument_list|,
literal|"locate_submit_rule"
argument_list|,
operator|new
name|VariableTerm
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|submitRule
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error in locate_submit_rule: no submit_rule found"
argument_list|)
expr_stmt|;
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Error in finding submit rule"
argument_list|)
return|;
block|}
name|List
argument_list|<
name|Term
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|Term
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|Term
index|[]
name|template
range|:
name|env
operator|.
name|all
argument_list|(
literal|"gerrit"
argument_list|,
literal|"can_submit"
argument_list|,
name|submitRule
argument_list|,
operator|new
name|VariableTerm
argument_list|()
argument_list|)
control|)
block|{
name|results
operator|.
name|add
argument_list|(
name|template
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PrologException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"PrologException calling "
operator|+
name|submitRule
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Error in submit rule"
argument_list|)
return|;
block|}
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// This should never occur. A well written submit rule will always produce
comment|// at least one result informing the caller of the labels that are
comment|// required for this change to be submittable. Each label will indicate
comment|// whether or not that is actually possible given the permissions.
name|log
operator|.
name|error
argument_list|(
literal|"Submit rule has no solution: "
operator|+
name|submitRule
argument_list|)
expr_stmt|;
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Error in submit rule (no solution possible)"
argument_list|)
return|;
block|}
comment|// The last result produced will be an "ok(P)" format if submit is possible.
comment|// This is always true because can_submit (called above) will cut away all
comment|// choice points once a solution is found.
name|Term
name|last
init|=
name|results
operator|.
name|get
argument_list|(
name|results
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|.
name|isStructure
argument_list|()
operator|&&
literal|1
operator|==
name|last
operator|.
name|arity
argument_list|()
operator|&&
literal|"ok"
operator|.
name|equals
argument_list|(
name|last
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
comment|// Term solution = last.arg(0);
return|return
name|CanSubmitResult
operator|.
name|OK
return|;
block|}
comment|// For now only process the first result. Later we can examine all of the
comment|// results and proposes different alternative paths to a submit solution.
name|Term
name|first
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|first
operator|.
name|isStructure
argument_list|()
operator|||
literal|1
operator|!=
name|first
operator|.
name|arity
argument_list|()
operator|||
operator|!
literal|"not_ready"
operator|.
name|equals
argument_list|(
name|first
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unexpected result from can_submit: "
operator|+
name|first
argument_list|)
expr_stmt|;
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Error in submit rule"
argument_list|)
return|;
block|}
name|Term
name|submitRecord
init|=
name|first
operator|.
name|arg
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|submitRecord
operator|.
name|isStructure
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid result from submit rule "
operator|+
name|submitRule
operator|+
literal|": "
operator|+
name|submitRecord
argument_list|)
expr_stmt|;
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Error in submit rule"
argument_list|)
return|;
block|}
for|for
control|(
name|Term
name|state
range|:
operator|(
operator|(
name|StructureTerm
operator|)
name|submitRecord
operator|)
operator|.
name|args
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|state
operator|.
name|isStructure
argument_list|()
operator|||
literal|2
operator|!=
name|state
operator|.
name|arity
argument_list|()
operator|||
operator|!
literal|"label"
operator|.
name|equals
argument_list|(
name|state
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid result from submit rule "
operator|+
name|submitRule
operator|+
literal|": "
operator|+
name|submitRecord
argument_list|)
expr_stmt|;
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Invalid submit rule result"
argument_list|)
return|;
block|}
name|String
name|label
init|=
name|state
operator|.
name|arg
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
name|Term
name|status
init|=
name|state
operator|.
name|arg
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"ok"
operator|.
name|equals
argument_list|(
name|status
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
elseif|else
if|if
condition|(
literal|"reject"
operator|.
name|equals
argument_list|(
name|status
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Submit blocked by "
operator|+
name|label
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"need"
operator|.
name|equals
argument_list|(
name|status
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|status
operator|.
name|isStructure
argument_list|()
operator|&&
name|status
operator|.
name|arg
argument_list|(
literal|0
argument_list|)
operator|.
name|isInteger
argument_list|()
condition|)
block|{
name|IntegerTerm
name|val
init|=
operator|(
name|IntegerTerm
operator|)
name|status
operator|.
name|arg
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
literal|1
operator|<
name|val
operator|.
name|intValue
argument_list|()
condition|)
block|{
name|label
operator|+=
literal|"+"
operator|+
name|val
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Requires "
operator|+
name|label
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"impossble"
operator|.
name|equals
argument_list|(
name|status
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Requires "
operator|+
name|label
operator|+
literal|" (check permissions)"
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|CanSubmitResult
argument_list|(
literal|"Invalid submit rule result"
argument_list|)
return|;
block|}
block|}
return|return
name|CanSubmitResult
operator|.
name|OK
return|;
block|}
block|}
end_class

end_unit

