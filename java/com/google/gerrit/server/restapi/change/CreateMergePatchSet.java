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
DECL|package|com.google.gerrit.server.restapi.change
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
name|change
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
name|base
operator|.
name|MoreObjects
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
name|Strings
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
name|common
operator|.
name|MergeInput
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
name|MergePatchSetInput
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
name|MergeConflictException
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
name|GerritPersonIdent
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
name|ChangeFinder
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
name|ChangeJson
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
name|ChangeResource
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
name|NotifyResolver
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
name|git
operator|.
name|MergeUtil
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
name|ProjectCache
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
name|restapi
operator|.
name|project
operator|.
name|CommitsCollection
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
name|submit
operator|.
name|MergeIdenticalTreeException
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
name|gerrit
operator|.
name|server
operator|.
name|util
operator|.
name|time
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|PersonIdent
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
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|ChangeIdUtil
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|CreateMergePatchSet
specifier|public
class|class
name|CreateMergePatchSet
extends|extends
name|RetryingRestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|MergePatchSetInput
argument_list|,
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
block|{
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|commits
specifier|private
specifier|final
name|CommitsCollection
name|commits
decl_stmt|;
DECL|field|serverTimeZone
specifier|private
specifier|final
name|TimeZone
name|serverTimeZone
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
DECL|field|jsonFactory
specifier|private
specifier|final
name|ChangeJson
operator|.
name|Factory
name|jsonFactory
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|mergeUtilFactory
specifier|private
specifier|final
name|MergeUtil
operator|.
name|Factory
name|mergeUtilFactory
decl_stmt|;
DECL|field|patchSetInserterFactory
specifier|private
specifier|final
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|changeFinder
specifier|private
specifier|final
name|ChangeFinder
name|changeFinder
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateMergePatchSet ( GitRepositoryManager gitManager, CommitsCollection commits, @GerritPersonIdent PersonIdent myIdent, Provider<CurrentUser> user, ChangeJson.Factory json, PatchSetUtil psUtil, MergeUtil.Factory mergeUtilFactory, RetryHelper retryHelper, PatchSetInserter.Factory patchSetInserterFactory, ProjectCache projectCache, ChangeFinder changeFinder, PermissionBackend permissionBackend)
name|CreateMergePatchSet
parameter_list|(
name|GitRepositoryManager
name|gitManager
parameter_list|,
name|CommitsCollection
name|commits
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|myIdent
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|,
name|ChangeJson
operator|.
name|Factory
name|json
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|MergeUtil
operator|.
name|Factory
name|mergeUtilFactory
parameter_list|,
name|RetryHelper
name|retryHelper
parameter_list|,
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ChangeFinder
name|changeFinder
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
name|gitManager
operator|=
name|gitManager
expr_stmt|;
name|this
operator|.
name|commits
operator|=
name|commits
expr_stmt|;
name|this
operator|.
name|serverTimeZone
operator|=
name|myIdent
operator|.
name|getTimeZone
argument_list|()
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|jsonFactory
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|mergeUtilFactory
operator|=
name|mergeUtilFactory
expr_stmt|;
name|this
operator|.
name|patchSetInserterFactory
operator|=
name|patchSetInserterFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|changeFinder
operator|=
name|changeFinder
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
DECL|method|applyImpl ( BatchUpdate.Factory updateFactory, ChangeResource rsrc, MergePatchSetInput in)
specifier|protected
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
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
name|MergePatchSetInput
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|RestApiException
throws|,
name|UpdateException
throws|,
name|PermissionBackendException
block|{
comment|// Not allowed to create a new patch set if the current patch set is locked.
name|psUtil
operator|.
name|checkPatchSetNotLocked
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
expr_stmt|;
name|rsrc
operator|.
name|permissions
argument_list|()
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|ADD_PATCH_SET
argument_list|)
expr_stmt|;
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|projectState
operator|.
name|checkStatePermitsWrite
argument_list|()
expr_stmt|;
name|MergeInput
name|merge
init|=
name|in
operator|.
name|merge
decl_stmt|;
if|if
condition|(
name|merge
operator|==
literal|null
operator|||
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|merge
operator|.
name|source
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"merge.source must be non-empty"
argument_list|)
throw|;
block|}
name|in
operator|.
name|baseChange
operator|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|in
operator|.
name|baseChange
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|PatchSet
name|ps
init|=
name|psUtil
operator|.
name|current
argument_list|(
name|rsrc
operator|.
name|getNotes
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
name|Project
operator|.
name|NameKey
name|project
init|=
name|change
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|BranchNameKey
name|dest
init|=
name|change
operator|.
name|getDest
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|ObjectInserter
name|oi
operator|=
name|git
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
init|)
block|{
name|RevCommit
name|sourceCommit
init|=
name|MergeUtil
operator|.
name|resolveCommit
argument_list|(
name|git
argument_list|,
name|rw
argument_list|,
name|merge
operator|.
name|source
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|commits
operator|.
name|canRead
argument_list|(
name|projectState
argument_list|,
name|git
argument_list|,
name|sourceCommit
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"cannot find source commit: "
operator|+
name|merge
operator|.
name|source
operator|+
literal|" to merge."
argument_list|)
throw|;
block|}
name|RevCommit
name|currentPsCommit
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|groups
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|in
operator|.
name|inheritParent
operator|&&
operator|!
name|in
operator|.
name|baseChange
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PatchSet
name|basePS
init|=
name|findBasePatchSet
argument_list|(
name|in
operator|.
name|baseChange
argument_list|)
decl_stmt|;
name|currentPsCommit
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|basePS
operator|.
name|getCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|groups
operator|=
name|basePS
operator|.
name|getGroups
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|currentPsCommit
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ps
operator|.
name|getCommitId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Timestamp
name|now
init|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
decl_stmt|;
name|IdentifiedUser
name|me
init|=
name|user
operator|.
name|get
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
decl_stmt|;
name|PersonIdent
name|author
init|=
name|me
operator|.
name|newCommitterIdent
argument_list|(
name|now
argument_list|,
name|serverTimeZone
argument_list|)
decl_stmt|;
name|RevCommit
name|newCommit
init|=
name|createMergeCommit
argument_list|(
name|in
argument_list|,
name|projectState
argument_list|,
name|dest
argument_list|,
name|git
argument_list|,
name|oi
argument_list|,
name|rw
argument_list|,
name|currentPsCommit
argument_list|,
name|sourceCommit
argument_list|,
name|author
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
name|change
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|nextPsId
init|=
name|ChangeUtil
operator|.
name|nextPatchSetId
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSetInserter
name|psInserter
init|=
name|patchSetInserterFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|,
name|nextPsId
argument_list|,
name|newCommit
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|me
argument_list|,
name|now
argument_list|)
init|)
block|{
name|bu
operator|.
name|setRepository
argument_list|(
name|git
argument_list|,
name|rw
argument_list|,
name|oi
argument_list|)
expr_stmt|;
name|bu
operator|.
name|setNotify
argument_list|(
name|NotifyResolver
operator|.
name|Result
operator|.
name|none
argument_list|()
argument_list|)
expr_stmt|;
name|psInserter
operator|.
name|setMessage
argument_list|(
literal|"Uploaded patch set "
operator|+
name|nextPsId
operator|.
name|get
argument_list|()
operator|+
literal|"."
argument_list|)
operator|.
name|setCheckAddPatchSetPermission
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|groups
operator|!=
literal|null
condition|)
block|{
name|psInserter
operator|.
name|setGroups
argument_list|(
name|groups
argument_list|)
expr_stmt|;
block|}
name|bu
operator|.
name|addOp
argument_list|(
name|rsrc
operator|.
name|getId
argument_list|()
argument_list|,
name|psInserter
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|ChangeJson
name|json
init|=
name|jsonFactory
operator|.
name|create
argument_list|(
name|ListChangesOption
operator|.
name|CURRENT_REVISION
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|psInserter
operator|.
name|getChange
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|findBasePatchSet (String baseChange)
specifier|private
name|PatchSet
name|findBasePatchSet
parameter_list|(
name|String
name|baseChange
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|UnprocessableEntityException
block|{
name|List
argument_list|<
name|ChangeNotes
argument_list|>
name|notes
init|=
name|changeFinder
operator|.
name|find
argument_list|(
name|baseChange
argument_list|)
decl_stmt|;
if|if
condition|(
name|notes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"Base change not found: "
operator|+
name|baseChange
argument_list|)
throw|;
block|}
name|ChangeNotes
name|change
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|notes
argument_list|)
decl_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|change
argument_list|(
name|change
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"Read not permitted for "
operator|+
name|baseChange
argument_list|)
throw|;
block|}
return|return
name|psUtil
operator|.
name|current
argument_list|(
name|change
argument_list|)
return|;
block|}
DECL|method|createMergeCommit ( MergePatchSetInput in, ProjectState projectState, BranchNameKey dest, Repository git, ObjectInserter oi, RevWalk rw, RevCommit currentPsCommit, RevCommit sourceCommit, PersonIdent author, ObjectId changeId)
specifier|private
name|RevCommit
name|createMergeCommit
parameter_list|(
name|MergePatchSetInput
name|in
parameter_list|,
name|ProjectState
name|projectState
parameter_list|,
name|BranchNameKey
name|dest
parameter_list|,
name|Repository
name|git
parameter_list|,
name|ObjectInserter
name|oi
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|RevCommit
name|currentPsCommit
parameter_list|,
name|RevCommit
name|sourceCommit
parameter_list|,
name|PersonIdent
name|author
parameter_list|,
name|ObjectId
name|changeId
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|MergeIdenticalTreeException
throws|,
name|MergeConflictException
throws|,
name|IOException
block|{
name|ObjectId
name|parentCommit
decl_stmt|;
if|if
condition|(
name|in
operator|.
name|inheritParent
condition|)
block|{
comment|// inherit first parent from previous patch set
name|parentCommit
operator|=
name|currentPsCommit
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|in
operator|.
name|baseChange
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|parentCommit
operator|=
name|currentPsCommit
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// get the current branch tip of destination branch
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
name|dest
operator|.
name|branch
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|destRef
operator|!=
literal|null
condition|)
block|{
name|parentCommit
operator|=
name|destRef
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"cannot find destination branch"
argument_list|)
throw|;
block|}
block|}
name|RevCommit
name|mergeTip
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|parentCommit
argument_list|)
decl_stmt|;
name|String
name|commitMsg
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|in
operator|.
name|subject
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|commitMsg
operator|=
name|ChangeIdUtil
operator|.
name|insertId
argument_list|(
name|in
operator|.
name|subject
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// reuse previous patch set commit message
name|commitMsg
operator|=
name|currentPsCommit
operator|.
name|getFullMessage
argument_list|()
expr_stmt|;
block|}
name|String
name|mergeStrategy
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|in
operator|.
name|merge
operator|.
name|strategy
argument_list|)
argument_list|,
name|mergeUtilFactory
operator|.
name|create
argument_list|(
name|projectState
argument_list|)
operator|.
name|mergeStrategyName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|MergeUtil
operator|.
name|createMergeCommit
argument_list|(
name|oi
argument_list|,
name|git
operator|.
name|getConfig
argument_list|()
argument_list|,
name|mergeTip
argument_list|,
name|sourceCommit
argument_list|,
name|mergeStrategy
argument_list|,
name|author
argument_list|,
name|commitMsg
argument_list|,
name|rw
argument_list|)
return|;
block|}
block|}
end_class

end_unit

