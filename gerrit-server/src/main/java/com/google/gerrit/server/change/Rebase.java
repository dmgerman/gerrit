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
name|gerrit
operator|.
name|common
operator|.
name|errors
operator|.
name|EmailException
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
name|changes
operator|.
name|RebaseInput
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
name|client
operator|.
name|ListChangesOption
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
name|common
operator|.
name|ChangeInfo
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
name|ResourceNotFoundException
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
name|Change
operator|.
name|Status
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
name|PatchSetAncestor
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
name|changedetail
operator|.
name|RebaseChange
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|Rebase
specifier|public
class|class
name|Rebase
implements|implements
name|RestModifyView
argument_list|<
name|RevisionResource
argument_list|,
name|RebaseInput
argument_list|>
implements|,
name|UiAction
argument_list|<
name|RevisionResource
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
name|Rebase
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|rebaseChange
specifier|private
specifier|final
name|Provider
argument_list|<
name|RebaseChange
argument_list|>
name|rebaseChange
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
name|json
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|Rebase (Provider<RebaseChange> rebaseChange, ChangeJson json, Provider<ReviewDb> dbProvider)
specifier|public
name|Rebase
parameter_list|(
name|Provider
argument_list|<
name|RebaseChange
argument_list|>
name|rebaseChange
parameter_list|,
name|ChangeJson
name|json
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|)
block|{
name|this
operator|.
name|rebaseChange
operator|=
name|rebaseChange
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|CURRENT_REVISION
argument_list|)
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|CURRENT_COMMIT
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc, RebaseInput input)
specifier|public
name|ChangeInfo
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|RebaseInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|EmailException
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
name|canRebase
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"rebase not permitted"
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
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|hasOneParent
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"cannot rebase merge commits or commit with no ancestor"
argument_list|)
throw|;
block|}
name|String
name|baseRev
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|input
operator|!=
literal|null
operator|&&
name|input
operator|.
name|base
operator|!=
literal|null
condition|)
block|{
name|String
name|base
init|=
name|input
operator|.
name|base
operator|.
name|trim
argument_list|()
decl_stmt|;
do|do
block|{
if|if
condition|(
name|base
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
comment|// remove existing dependency to other patch set
name|baseRev
operator|=
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
break|break;
block|}
name|ReviewDb
name|db
init|=
name|dbProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|PatchSet
name|basePatchSet
init|=
name|parseBase
argument_list|(
name|base
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePatchSet
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"base revision is missing: "
operator|+
name|base
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
name|basePatchSet
argument_list|,
name|db
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"base revision not accessible: "
operator|+
name|base
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|change
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|basePatchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"cannot depend on self"
argument_list|)
throw|;
block|}
name|Change
name|baseChange
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|basePatchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseChange
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|baseChange
operator|.
name|getProject
argument_list|()
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"base change is in wrong project: "
operator|+
name|baseChange
operator|.
name|getProject
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|baseChange
operator|.
name|getDest
argument_list|()
operator|.
name|equals
argument_list|(
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
literal|"base change is targetting wrong branch: "
operator|+
name|baseChange
operator|.
name|getDest
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|baseChange
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|ABANDONED
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"base change is abandoned: "
operator|+
name|baseChange
operator|.
name|getKey
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|isDescendantOf
argument_list|(
name|baseChange
operator|.
name|getId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"base change "
operator|+
name|baseChange
operator|.
name|getKey
argument_list|()
operator|+
literal|" is a descendant of the current "
operator|+
literal|" change - recursion not allowed"
argument_list|)
throw|;
block|}
name|baseRev
operator|=
name|basePatchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
do|while
condition|(
literal|false
condition|)
do|;
comment|// just wanted to use the break statement
block|}
try|try
block|{
name|rebaseChange
operator|.
name|get
argument_list|()
operator|.
name|rebase
argument_list|(
name|change
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
name|getUser
argument_list|()
argument_list|,
name|baseRev
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidChangeOperationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|change
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|json
operator|.
name|format
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isDescendantOf (Change.Id child, RevId ancestor)
specifier|private
name|boolean
name|isDescendantOf
parameter_list|(
name|Change
operator|.
name|Id
name|child
parameter_list|,
name|RevId
name|ancestor
parameter_list|)
throws|throws
name|OrmException
block|{
name|ReviewDb
name|db
init|=
name|dbProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|ArrayList
argument_list|<
name|RevId
argument_list|>
name|parents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|parents
operator|.
name|add
argument_list|(
name|ancestor
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|parents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|RevId
name|parent
init|=
name|parents
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// get direct descendants of change
for|for
control|(
name|PatchSetAncestor
name|desc
range|:
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|descendantsOf
argument_list|(
name|parent
argument_list|)
control|)
block|{
name|PatchSet
name|descPatchSet
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|desc
operator|.
name|getPatchSet
argument_list|()
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Id
name|descChangeId
init|=
name|descPatchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|equals
argument_list|(
name|descChangeId
argument_list|)
condition|)
block|{
name|PatchSet
operator|.
name|Id
name|descCurrentPatchSetId
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|descChangeId
argument_list|)
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
comment|// it's only bad if the descendant patch set is current
return|return
name|descPatchSet
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|descCurrentPatchSetId
argument_list|)
return|;
block|}
else|else
block|{
comment|// process indirect descendants as well
name|parents
operator|.
name|add
argument_list|(
name|descPatchSet
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|parseBase (final String base)
specifier|private
name|PatchSet
name|parseBase
parameter_list|(
specifier|final
name|String
name|base
parameter_list|)
throws|throws
name|OrmException
block|{
name|ReviewDb
name|db
init|=
name|dbProvider
operator|.
name|get
argument_list|()
decl_stmt|;
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
comment|// try parsing the base as a ref string
return|return
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|basePatchSetId
argument_list|)
return|;
block|}
comment|// try parsing base as a change number (assume current patch set)
name|PatchSet
name|basePatchSet
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Change
operator|.
name|Id
name|baseChangeId
init|=
name|Change
operator|.
name|Id
operator|.
name|parse
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
for|for
control|(
name|PatchSet
name|ps
range|:
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byChange
argument_list|(
name|baseChangeId
argument_list|)
control|)
block|{
if|if
condition|(
name|basePatchSet
operator|==
literal|null
operator|||
name|basePatchSet
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
name|basePatchSet
operator|=
name|ps
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// probably a SHA1
block|}
comment|// try parsing as SHA1
if|if
condition|(
name|basePatchSet
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|PatchSet
name|ps
range|:
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byRevision
argument_list|(
operator|new
name|RevId
argument_list|(
name|base
argument_list|)
argument_list|)
control|)
block|{
if|if
condition|(
name|basePatchSet
operator|==
literal|null
operator|||
name|basePatchSet
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
name|basePatchSet
operator|=
name|ps
expr_stmt|;
block|}
block|}
block|}
return|return
name|basePatchSet
return|;
block|}
DECL|method|hasOneParent (final PatchSet.Id patchSetId)
specifier|private
name|boolean
name|hasOneParent
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
block|{
try|try
block|{
comment|// prevent rebase of exotic changes (merge commit, no ancestor).
return|return
operator|(
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|ancestorsOf
argument_list|(
name|patchSetId
argument_list|)
operator|.
name|toList
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to get ancestors of patch set "
operator|+
name|patchSetId
operator|.
name|toRefName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
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
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Rebase"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Rebase onto tip of branch or parent change"
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
name|isCurrent
argument_list|()
operator|&&
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|canRebase
argument_list|()
operator|&&
name|hasOneParent
argument_list|(
name|resource
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
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
name|RebaseInput
argument_list|>
block|{
DECL|field|rebase
specifier|private
specifier|final
name|Rebase
name|rebase
decl_stmt|;
annotation|@
name|Inject
DECL|method|CurrentRevision (Rebase rebase)
name|CurrentRevision
parameter_list|(
name|Rebase
name|rebase
parameter_list|)
block|{
name|this
operator|.
name|rebase
operator|=
name|rebase
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, RebaseInput input)
specifier|public
name|ChangeInfo
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|RebaseInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|EmailException
throws|,
name|OrmException
block|{
name|PatchSet
name|ps
init|=
name|rebase
operator|.
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
name|rebase
operator|.
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
return|return
name|rebase
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
return|;
block|}
block|}
block|}
end_class

end_unit

