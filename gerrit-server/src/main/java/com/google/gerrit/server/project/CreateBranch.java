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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefDatabase
operator|.
name|ALL
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
name|common
operator|.
name|errors
operator|.
name|InvalidRevisionException
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
name|BranchInfo
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
name|BadRequestException
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
name|DefaultInput
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
name|project
operator|.
name|CreateBranch
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
name|util
operator|.
name|MagicBranch
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
name|assistedinject
operator|.
name|Assisted
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
name|IncorrectObjectTypeException
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
name|MissingObjectException
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
name|RevisionSyntaxException
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
name|Constants
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
name|ObjectWalk
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
name|RevObject
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
DECL|class|CreateBranch
specifier|public
class|class
name|CreateBranch
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|Input
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
name|CreateBranch
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|ref
specifier|public
name|String
name|ref
decl_stmt|;
annotation|@
name|DefaultInput
DECL|field|revision
specifier|public
name|String
name|revision
decl_stmt|;
block|}
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (String ref)
name|CreateBranch
name|create
parameter_list|(
name|String
name|ref
parameter_list|)
function_decl|;
block|}
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
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
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
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateBranch (Provider<IdentifiedUser> identifiedUser, GitRepositoryManager repoManager, Provider<ReviewDb> db, GitReferenceUpdated referenceUpdated, ChangeHooks hooks, @Assisted String ref)
name|CreateBranch
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
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|GitReferenceUpdated
name|referenceUpdated
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|,
annotation|@
name|Assisted
name|String
name|ref
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
name|db
operator|=
name|db
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
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc, Input input)
specifier|public
name|BranchInfo
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|AuthException
throws|,
name|ResourceConflictException
throws|,
name|IOException
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
name|Input
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|ref
operator|!=
literal|null
operator|&&
operator|!
name|ref
operator|.
name|equals
argument_list|(
name|input
operator|.
name|ref
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"ref must match URL"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|revision
operator|==
literal|null
condition|)
block|{
name|input
operator|.
name|revision
operator|=
name|Constants
operator|.
name|HEAD
expr_stmt|;
block|}
while|while
condition|(
name|ref
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|ref
operator|=
name|ref
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ref
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_REFS
argument_list|)
condition|)
block|{
name|ref
operator|=
name|Constants
operator|.
name|R_HEADS
operator|+
name|ref
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Repository
operator|.
name|isValidRefName
argument_list|(
name|ref
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"invalid branch name \""
operator|+
name|ref
operator|+
literal|"\""
argument_list|)
throw|;
block|}
if|if
condition|(
name|MagicBranch
operator|.
name|isMagicBranch
argument_list|(
name|ref
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"not allowed to create branches under \""
operator|+
name|MagicBranch
operator|.
name|getMagicRefNamePrefix
argument_list|(
name|ref
argument_list|)
operator|+
literal|"\""
argument_list|)
throw|;
block|}
specifier|final
name|Branch
operator|.
name|NameKey
name|name
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|ref
argument_list|)
decl_stmt|;
specifier|final
name|RefControl
name|refControl
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|controlForRef
argument_list|(
name|name
argument_list|)
decl_stmt|;
specifier|final
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|ObjectId
name|revid
init|=
name|parseBaseRevision
argument_list|(
name|repo
argument_list|,
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|input
operator|.
name|revision
argument_list|)
decl_stmt|;
specifier|final
name|RevWalk
name|rw
init|=
name|verifyConnected
argument_list|(
name|repo
argument_list|,
name|revid
argument_list|)
decl_stmt|;
name|RevObject
name|object
init|=
name|rw
operator|.
name|parseAny
argument_list|(
name|revid
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_HEADS
argument_list|)
condition|)
block|{
comment|// Ensure that what we start the branch from is a commit. If we
comment|// were given a tag, deference to the commit instead.
comment|//
try|try
block|{
name|object
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IncorrectObjectTypeException
name|notCommit
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"\""
operator|+
name|input
operator|.
name|revision
operator|+
literal|"\" not a commit"
argument_list|)
throw|;
block|}
block|}
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|refControl
operator|.
name|canCreate
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|rw
argument_list|,
name|object
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Cannot create \""
operator|+
name|ref
operator|+
literal|"\""
argument_list|)
throw|;
block|}
try|try
block|{
specifier|final
name|RefUpdate
name|u
init|=
name|repo
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
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setNewObjectId
argument_list|(
name|object
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setRefLogIdent
argument_list|(
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|newRefLogIdent
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setRefLogMessage
argument_list|(
literal|"created via REST from "
operator|+
name|input
operator|.
name|revision
argument_list|,
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|RefUpdate
operator|.
name|Result
name|result
init|=
name|u
operator|.
name|update
argument_list|(
name|rw
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|FAST_FORWARD
case|:
case|case
name|NEW
case|:
case|case
name|NO_CHANGE
case|:
name|referenceUpdated
operator|.
name|fire
argument_list|(
name|name
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|u
argument_list|)
expr_stmt|;
name|hooks
operator|.
name|doRefUpdatedHook
argument_list|(
name|name
argument_list|,
name|u
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
break|break;
case|case
name|LOCK_FAILURE
case|:
if|if
condition|(
name|repo
operator|.
name|getRef
argument_list|(
name|ref
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"branch \""
operator|+
name|ref
operator|+
literal|"\" already exists"
argument_list|)
throw|;
block|}
name|String
name|refPrefix
init|=
name|getRefPrefix
argument_list|(
name|ref
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|Constants
operator|.
name|R_HEADS
operator|.
name|equals
argument_list|(
name|refPrefix
argument_list|)
condition|)
block|{
if|if
condition|(
name|repo
operator|.
name|getRef
argument_list|(
name|refPrefix
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot create branch \""
operator|+
name|ref
operator|+
literal|"\" since it conflicts with branch \""
operator|+
name|refPrefix
operator|+
literal|"\"."
argument_list|)
throw|;
block|}
name|refPrefix
operator|=
name|getRefPrefix
argument_list|(
name|refPrefix
argument_list|)
expr_stmt|;
block|}
comment|//$FALL-THROUGH$
default|default:
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|result
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|BranchInfo
name|info
init|=
operator|new
name|BranchInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|info
operator|.
name|revision
operator|=
name|revid
operator|.
name|getName
argument_list|()
expr_stmt|;
name|info
operator|.
name|canDelete
operator|=
name|refControl
operator|.
name|canDelete
argument_list|()
condition|?
literal|true
else|:
literal|null
expr_stmt|;
return|return
name|info
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot create branch \""
operator|+
name|name
operator|+
literal|"\""
argument_list|,
name|err
argument_list|)
expr_stmt|;
throw|throw
name|err
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidRevisionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"invalid revision \""
operator|+
name|input
operator|.
name|revision
operator|+
literal|"\""
argument_list|)
throw|;
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
DECL|method|getRefPrefix (final String refName)
specifier|private
specifier|static
name|String
name|getRefPrefix
parameter_list|(
specifier|final
name|String
name|refName
parameter_list|)
block|{
specifier|final
name|int
name|i
init|=
name|refName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>
name|Constants
operator|.
name|R_HEADS
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
return|return
name|refName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
return|;
block|}
return|return
name|Constants
operator|.
name|R_HEADS
return|;
block|}
DECL|method|parseBaseRevision (Repository repo, Project.NameKey projectName, String baseRevision)
specifier|private
name|ObjectId
name|parseBaseRevision
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|baseRevision
parameter_list|)
throws|throws
name|InvalidRevisionException
block|{
try|try
block|{
specifier|final
name|ObjectId
name|revid
init|=
name|repo
operator|.
name|resolve
argument_list|(
name|baseRevision
argument_list|)
decl_stmt|;
if|if
condition|(
name|revid
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidRevisionException
argument_list|()
throw|;
block|}
return|return
name|revid
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot resolve \""
operator|+
name|baseRevision
operator|+
literal|"\" in project \""
operator|+
name|projectName
operator|.
name|get
argument_list|()
operator|+
literal|"\""
argument_list|,
name|err
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|InvalidRevisionException
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|RevisionSyntaxException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid revision syntax \""
operator|+
name|baseRevision
operator|+
literal|"\""
argument_list|,
name|err
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|InvalidRevisionException
argument_list|()
throw|;
block|}
block|}
DECL|method|verifyConnected (final Repository repo, final ObjectId revid)
specifier|private
name|RevWalk
name|verifyConnected
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|ObjectId
name|revid
parameter_list|)
throws|throws
name|InvalidRevisionException
block|{
try|try
block|{
specifier|final
name|ObjectWalk
name|rw
init|=
operator|new
name|ObjectWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|rw
operator|.
name|markStart
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|revid
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IncorrectObjectTypeException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidRevisionException
argument_list|()
throw|;
block|}
for|for
control|(
specifier|final
name|Ref
name|r
range|:
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|ALL
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|rw
operator|.
name|markUninteresting
argument_list|(
name|rw
operator|.
name|parseAny
argument_list|(
name|r
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MissingObjectException
name|err
parameter_list|)
block|{
continue|continue;
block|}
block|}
name|rw
operator|.
name|checkConnectivity
argument_list|()
expr_stmt|;
return|return
name|rw
return|;
block|}
catch|catch
parameter_list|(
name|IncorrectObjectTypeException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidRevisionException
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|MissingObjectException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidRevisionException
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Repository \""
operator|+
name|repo
operator|.
name|getDirectory
argument_list|()
operator|+
literal|"\" may be corrupt; suggest running git fsck"
argument_list|,
name|err
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|InvalidRevisionException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

