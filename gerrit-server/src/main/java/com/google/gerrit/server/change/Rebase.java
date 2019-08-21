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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|conditions
operator|.
name|BooleanCondition
operator|.
name|and
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
name|Sets
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
name|TimeUtil
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
name|conditions
operator|.
name|BooleanCondition
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
name|change
operator|.
name|RebaseUtil
operator|.
name|Base
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
name|ChangePermission
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
name|update
operator|.
name|BatchUpdate
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
name|update
operator|.
name|RetryHelper
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
name|update
operator|.
name|RetryingRestModifyView
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
name|update
operator|.
name|UpdateException
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
name|ObjectReader
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

begin_class
annotation|@
name|Singleton
DECL|class|Rebase
specifier|public
class|class
name|Rebase
extends|extends
name|RetryingRestModifyView
argument_list|<
name|RevisionResource
argument_list|,
name|RebaseInput
argument_list|,
name|ChangeInfo
argument_list|>
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
DECL|field|OPTIONS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|ListChangesOption
argument_list|>
name|OPTIONS
init|=
name|Sets
operator|.
name|immutableEnumSet
argument_list|(
name|ListChangesOption
operator|.
name|CURRENT_REVISION
argument_list|,
name|ListChangesOption
operator|.
name|CURRENT_COMMIT
argument_list|)
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|rebaseFactory
specifier|private
specifier|final
name|RebaseChangeOp
operator|.
name|Factory
name|rebaseFactory
decl_stmt|;
DECL|field|rebaseUtil
specifier|private
specifier|final
name|RebaseUtil
name|rebaseUtil
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
operator|.
name|Factory
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
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|Rebase ( RetryHelper retryHelper, GitRepositoryManager repoManager, RebaseChangeOp.Factory rebaseFactory, RebaseUtil rebaseUtil, ChangeJson.Factory json, Provider<ReviewDb> dbProvider, PermissionBackend permissionBackend)
specifier|public
name|Rebase
parameter_list|(
name|RetryHelper
name|retryHelper
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|RebaseChangeOp
operator|.
name|Factory
name|rebaseFactory
parameter_list|,
name|RebaseUtil
name|rebaseUtil
parameter_list|,
name|ChangeJson
operator|.
name|Factory
name|json
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|)
block|{
name|super
argument_list|(
name|retryHelper
argument_list|)
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|rebaseFactory
operator|=
name|rebaseFactory
expr_stmt|;
name|this
operator|.
name|rebaseUtil
operator|=
name|rebaseUtil
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|applyImpl ( BatchUpdate.Factory updateFactory, RevisionResource rsrc, RebaseInput input)
specifier|protected
name|ChangeInfo
name|applyImpl
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|RevisionResource
name|rsrc
parameter_list|,
name|RebaseInput
name|input
parameter_list|)
throws|throws
name|EmailException
throws|,
name|OrmException
throws|,
name|UpdateException
throws|,
name|RestApiException
throws|,
name|IOException
throws|,
name|NoSuchChangeException
throws|,
name|PermissionBackendException
block|{
name|rsrc
operator|.
name|permissions
argument_list|()
operator|.
name|database
argument_list|(
name|dbProvider
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|REBASE
argument_list|)
expr_stmt|;
name|Change
name|change
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
init|;
name|ObjectInserter
name|oi
operator|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|;
name|ObjectReader
name|reader
operator|=
name|oi
operator|.
name|newReader
argument_list|()
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|;
name|BatchUpdate
name|bu
operator|=
name|updateFactory
operator|.
name|create
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
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
name|ChangeUtil
operator|.
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
name|hasOneParent
argument_list|(
name|rw
argument_list|,
name|rsrc
operator|.
name|getPatchSet
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
name|bu
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|oi
argument_list|)
expr_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|rebaseFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|,
name|findBaseRev
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|rsrc
argument_list|,
name|input
argument_list|)
argument_list|)
operator|.
name|setForceContentMerge
argument_list|(
literal|true
argument_list|)
operator|.
name|setFireRevisionCreated
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
return|return
name|json
operator|.
name|create
argument_list|(
name|OPTIONS
argument_list|)
operator|.
name|format
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|findBaseRev ( Repository repo, RevWalk rw, RevisionResource rsrc, RebaseInput input)
specifier|private
name|ObjectId
name|findBaseRev
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|RevisionResource
name|rsrc
parameter_list|,
name|RebaseInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|NoSuchChangeException
throws|,
name|AuthException
throws|,
name|PermissionBackendException
block|{
name|Branch
operator|.
name|NameKey
name|destRefKey
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|==
literal|null
operator|||
name|input
operator|.
name|base
operator|==
literal|null
condition|)
block|{
return|return
name|rebaseUtil
operator|.
name|findBaseRevision
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|,
name|destRefKey
argument_list|,
name|repo
argument_list|,
name|rw
argument_list|)
return|;
block|}
name|Change
name|change
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|String
name|str
init|=
name|input
operator|.
name|base
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|str
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
comment|// Remove existing dependency to other patch set.
name|Ref
name|destRef
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|destRefKey
operator|.
name|get
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
name|ResourceConflictException
argument_list|(
literal|"can't rebase onto tip of branch "
operator|+
name|destRefKey
operator|.
name|get
argument_list|()
operator|+
literal|"; branch doesn't exist"
argument_list|)
throw|;
block|}
return|return
name|destRef
operator|.
name|getObjectId
argument_list|()
return|;
block|}
name|Base
name|base
decl_stmt|;
try|try
block|{
name|base
operator|=
name|rebaseUtil
operator|.
name|parseBase
argument_list|(
name|rsrc
argument_list|,
name|str
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
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
name|str
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Base change not found: %s"
argument_list|,
name|input
operator|.
name|base
argument_list|)
argument_list|)
throw|;
block|}
name|PatchSet
operator|.
name|Id
name|baseId
init|=
name|base
operator|.
name|patchSet
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|baseId
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
literal|"cannot rebase change onto itself"
argument_list|)
throw|;
block|}
name|permissionBackend
operator|.
name|user
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
operator|.
name|database
argument_list|(
name|dbProvider
argument_list|)
operator|.
name|change
argument_list|(
name|base
operator|.
name|notes
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
name|Change
name|baseChange
init|=
name|base
operator|.
name|notes
argument_list|()
operator|.
name|getChange
argument_list|()
decl_stmt|;
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
literal|"base change is targeting wrong branch: "
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
name|isMergedInto
argument_list|(
name|rw
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|,
name|base
operator|.
name|patchSet
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
literal|" is a descendant of the current change - recursion not allowed"
argument_list|)
throw|;
block|}
return|return
name|ObjectId
operator|.
name|fromString
argument_list|(
name|base
operator|.
name|patchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isMergedInto (RevWalk rw, PatchSet base, PatchSet tip)
specifier|private
name|boolean
name|isMergedInto
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|PatchSet
name|base
parameter_list|,
name|PatchSet
name|tip
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectId
name|baseId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|base
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectId
name|tipId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|tip
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|rw
operator|.
name|isMergedInto
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|baseId
argument_list|)
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|tipId
argument_list|)
argument_list|)
return|;
block|}
DECL|method|hasOneParent (RevWalk rw, PatchSet ps)
specifier|private
name|boolean
name|hasOneParent
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|PatchSet
name|ps
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Prevent rebase of exotic changes (merge commit, no ancestor).
name|RevCommit
name|c
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|getParentCount
argument_list|()
operator|==
literal|1
return|;
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
name|patchSet
init|=
name|resource
operator|.
name|getPatchSet
argument_list|()
decl_stmt|;
name|Change
name|change
init|=
name|resource
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|dest
init|=
name|change
operator|.
name|getDest
argument_list|()
decl_stmt|;
name|boolean
name|visible
init|=
name|change
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
decl_stmt|;
name|boolean
name|enabled
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|visible
condition|)
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|dest
operator|.
name|getParentKey
argument_list|()
argument_list|)
init|;           RevWalk rw = new RevWalk(repo)
block|)
block|{
name|visible
operator|=
name|hasOneParent
argument_list|(
name|rw
argument_list|,
name|resource
operator|.
name|getPatchSet
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|visible
condition|)
block|{
name|enabled
operator|=
name|rebaseUtil
operator|.
name|canRebase
argument_list|(
name|patchSet
argument_list|,
name|dest
argument_list|,
name|repo
argument_list|,
name|rw
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to check if patch set can be rebased: "
operator|+
name|resource
operator|.
name|getPatchSet
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|visible
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|BooleanCondition
name|permissionCond
init|=
name|resource
operator|.
name|permissions
argument_list|()
operator|.
name|database
argument_list|(
name|dbProvider
argument_list|)
operator|.
name|testCond
argument_list|(
name|ChangePermission
operator|.
name|REBASE
argument_list|)
decl_stmt|;
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
name|and
argument_list|(
name|visible
argument_list|,
name|permissionCond
argument_list|)
argument_list|)
operator|.
name|setEnabled
argument_list|(
name|and
argument_list|(
name|enabled
argument_list|,
name|permissionCond
argument_list|)
argument_list|)
return|;
block|}
end_class

begin_class
DECL|class|CurrentRevision
specifier|public
specifier|static
class|class
name|CurrentRevision
extends|extends
name|RetryingRestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|RebaseInput
argument_list|,
name|ChangeInfo
argument_list|>
block|{
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|rebase
specifier|private
specifier|final
name|Rebase
name|rebase
decl_stmt|;
annotation|@
name|Inject
DECL|method|CurrentRevision (RetryHelper retryHelper, PatchSetUtil psUtil, Rebase rebase)
name|CurrentRevision
parameter_list|(
name|RetryHelper
name|retryHelper
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|Rebase
name|rebase
parameter_list|)
block|{
name|super
argument_list|(
name|retryHelper
argument_list|)
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|rebase
operator|=
name|rebase
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|applyImpl ( BatchUpdate.Factory updateFactory, ChangeResource rsrc, RebaseInput input)
specifier|protected
name|ChangeInfo
name|applyImpl
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ChangeResource
name|rsrc
parameter_list|,
name|RebaseInput
name|input
parameter_list|)
throws|throws
name|EmailException
throws|,
name|OrmException
throws|,
name|UpdateException
throws|,
name|RestApiException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
name|PatchSet
name|ps
init|=
name|psUtil
operator|.
name|current
argument_list|(
name|rebase
operator|.
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getNotes
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
return|return
name|rebase
operator|.
name|applyImpl
argument_list|(
name|updateFactory
argument_list|,
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
end_class

unit|}
end_unit

