begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|gerrit
operator|.
name|common
operator|.
name|FooterConstants
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
name|BooleanProjectConfig
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
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
name|CommitMessageInput
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
name|CommitMessageUtil
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
name|errors
operator|.
name|ConfigInvalidException
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

begin_class
annotation|@
name|Singleton
DECL|class|PutMessage
specifier|public
class|class
name|PutMessage
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|CommitMessageInput
argument_list|>
block|{
DECL|field|updateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
DECL|field|repositoryManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repositoryManager
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|tz
specifier|private
specifier|final
name|TimeZone
name|tz
decl_stmt|;
DECL|field|psInserterFactory
specifier|private
specifier|final
name|PatchSetInserter
operator|.
name|Factory
name|psInserterFactory
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|notifyResolver
specifier|private
specifier|final
name|NotifyResolver
name|notifyResolver
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutMessage ( BatchUpdate.Factory updateFactory, GitRepositoryManager repositoryManager, Provider<CurrentUser> userProvider, PatchSetInserter.Factory psInserterFactory, PermissionBackend permissionBackend, @GerritPersonIdent PersonIdent gerritIdent, PatchSetUtil psUtil, NotifyResolver notifyResolver, ProjectCache projectCache)
name|PutMessage
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|GitRepositoryManager
name|repositoryManager
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|PatchSetInserter
operator|.
name|Factory
name|psInserterFactory
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|gerritIdent
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|NotifyResolver
name|notifyResolver
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
name|this
operator|.
name|repositoryManager
operator|=
name|repositoryManager
expr_stmt|;
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|psInserterFactory
operator|=
name|psInserterFactory
expr_stmt|;
name|this
operator|.
name|tz
operator|=
name|gerritIdent
operator|.
name|getTimeZone
argument_list|()
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|notifyResolver
operator|=
name|notifyResolver
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource resource, CommitMessageInput input)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|resource
parameter_list|,
name|CommitMessageInput
name|input
parameter_list|)
throws|throws
name|IOException
throws|,
name|RestApiException
throws|,
name|UpdateException
throws|,
name|PermissionBackendException
throws|,
name|ConfigInvalidException
block|{
name|PatchSet
name|ps
init|=
name|psUtil
operator|.
name|current
argument_list|(
name|resource
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
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"input cannot be null"
argument_list|)
throw|;
block|}
name|String
name|sanitizedCommitMessage
init|=
name|CommitMessageUtil
operator|.
name|checkAndSanitizeCommitMessage
argument_list|(
name|input
operator|.
name|message
argument_list|)
decl_stmt|;
name|ensureCanEditCommitMessage
argument_list|(
name|resource
operator|.
name|getNotes
argument_list|()
argument_list|)
expr_stmt|;
name|sanitizedCommitMessage
operator|=
name|ensureChangeIdIsCorrect
argument_list|(
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|resource
operator|.
name|getProject
argument_list|()
argument_list|)
operator|.
name|is
argument_list|(
name|BooleanProjectConfig
operator|.
name|REQUIRE_CHANGE_ID
argument_list|)
argument_list|,
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|sanitizedCommitMessage
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|repository
init|=
name|repositoryManager
operator|.
name|openRepository
argument_list|(
name|resource
operator|.
name|getProject
argument_list|()
argument_list|)
init|;
name|RevWalk
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|repository
argument_list|)
init|;
name|ObjectInserter
name|objectInserter
operator|=
name|repository
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|RevCommit
name|patchSetCommit
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|ps
operator|.
name|commitId
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|currentCommitMessage
init|=
name|patchSetCommit
operator|.
name|getFullMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|message
operator|.
name|equals
argument_list|(
name|currentCommitMessage
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"new and existing commit message are the same"
argument_list|)
throw|;
block|}
name|Timestamp
name|ts
init|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|userProvider
operator|.
name|get
argument_list|()
argument_list|,
name|ts
argument_list|)
init|)
block|{
comment|// Ensure that BatchUpdate will update the same repo
name|bu
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|,
operator|new
name|RevWalk
argument_list|(
name|objectInserter
operator|.
name|newReader
argument_list|()
argument_list|)
argument_list|,
name|objectInserter
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|ChangeUtil
operator|.
name|nextPatchSetId
argument_list|(
name|repository
argument_list|,
name|ps
operator|.
name|id
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectId
name|newCommit
init|=
name|createCommit
argument_list|(
name|objectInserter
argument_list|,
name|patchSetCommit
argument_list|,
name|sanitizedCommitMessage
argument_list|,
name|ts
argument_list|)
decl_stmt|;
name|PatchSetInserter
name|inserter
init|=
name|psInserterFactory
operator|.
name|create
argument_list|(
name|resource
operator|.
name|getNotes
argument_list|()
argument_list|,
name|psId
argument_list|,
name|newCommit
argument_list|)
decl_stmt|;
name|inserter
operator|.
name|setMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Patch Set %s: Commit message was updated."
argument_list|,
name|psId
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|inserter
operator|.
name|setDescription
argument_list|(
literal|"Edit commit message"
argument_list|)
expr_stmt|;
name|bu
operator|.
name|setNotify
argument_list|(
name|resolveNotify
argument_list|(
name|input
argument_list|,
name|resource
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|inserter
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
literal|"ok"
argument_list|)
return|;
block|}
DECL|method|resolveNotify (CommitMessageInput input, ChangeResource resource)
specifier|private
name|NotifyResolver
operator|.
name|Result
name|resolveNotify
parameter_list|(
name|CommitMessageInput
name|input
parameter_list|,
name|ChangeResource
name|resource
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|NotifyHandling
name|notifyHandling
init|=
name|input
operator|.
name|notify
decl_stmt|;
if|if
condition|(
name|notifyHandling
operator|==
literal|null
condition|)
block|{
name|notifyHandling
operator|=
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|isWorkInProgress
argument_list|()
condition|?
name|NotifyHandling
operator|.
name|OWNER
else|:
name|NotifyHandling
operator|.
name|ALL
expr_stmt|;
block|}
return|return
name|notifyResolver
operator|.
name|resolve
argument_list|(
name|notifyHandling
argument_list|,
name|input
operator|.
name|notifyDetails
argument_list|)
return|;
block|}
DECL|method|createCommit ( ObjectInserter objectInserter, RevCommit basePatchSetCommit, String commitMessage, Timestamp timestamp)
specifier|private
name|ObjectId
name|createCommit
parameter_list|(
name|ObjectInserter
name|objectInserter
parameter_list|,
name|RevCommit
name|basePatchSetCommit
parameter_list|,
name|String
name|commitMessage
parameter_list|,
name|Timestamp
name|timestamp
parameter_list|)
throws|throws
name|IOException
block|{
name|CommitBuilder
name|builder
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setTreeId
argument_list|(
name|basePatchSetCommit
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setParentIds
argument_list|(
name|basePatchSetCommit
operator|.
name|getParents
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setAuthor
argument_list|(
name|basePatchSetCommit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setCommitter
argument_list|(
name|userProvider
operator|.
name|get
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
operator|.
name|newCommitterIdent
argument_list|(
name|timestamp
argument_list|,
name|tz
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setMessage
argument_list|(
name|commitMessage
argument_list|)
expr_stmt|;
name|ObjectId
name|newCommitId
init|=
name|objectInserter
operator|.
name|insert
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|objectInserter
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|newCommitId
return|;
block|}
DECL|method|ensureCanEditCommitMessage (ChangeNotes changeNotes)
specifier|private
name|void
name|ensureCanEditCommitMessage
parameter_list|(
name|ChangeNotes
name|changeNotes
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
throws|,
name|IOException
throws|,
name|ResourceConflictException
block|{
if|if
condition|(
operator|!
name|userProvider
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
comment|// Not allowed to put message if the current patch set is locked.
name|psUtil
operator|.
name|checkPatchSetNotLocked
argument_list|(
name|changeNotes
argument_list|)
expr_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|userProvider
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|change
argument_list|(
name|changeNotes
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|ADD_PATCH_SET
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|changeNotes
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|.
name|checkStatePermitsWrite
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|denied
parameter_list|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"modifying commit message not permitted"
argument_list|,
name|denied
argument_list|)
throw|;
block|}
block|}
DECL|method|ensureChangeIdIsCorrect ( boolean requireChangeId, String currentChangeId, String newCommitMessage)
specifier|private
specifier|static
name|String
name|ensureChangeIdIsCorrect
parameter_list|(
name|boolean
name|requireChangeId
parameter_list|,
name|String
name|currentChangeId
parameter_list|,
name|String
name|newCommitMessage
parameter_list|)
throws|throws
name|ResourceConflictException
throws|,
name|BadRequestException
block|{
name|RevCommit
name|revCommit
init|=
name|RevCommit
operator|.
name|parse
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
literal|"tree "
operator|+
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|"\n\n"
operator|+
name|newCommitMessage
argument_list|)
argument_list|)
decl_stmt|;
comment|// Check that the commit message without footers is not empty
name|CommitMessageUtil
operator|.
name|checkAndSanitizeCommitMessage
argument_list|(
name|revCommit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|changeIdFooters
init|=
name|revCommit
operator|.
name|getFooterLines
argument_list|(
name|FooterConstants
operator|.
name|CHANGE_ID
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|changeIdFooters
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|changeIdFooters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
name|currentChangeId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"wrong Change-Id footer"
argument_list|)
throw|;
block|}
if|if
condition|(
name|requireChangeId
operator|&&
name|revCommit
operator|.
name|getFooterLines
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// sanitization always adds '\n' at the end.
name|newCommitMessage
operator|+=
literal|"\n"
expr_stmt|;
block|}
if|if
condition|(
name|requireChangeId
operator|&&
name|changeIdFooters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|newCommitMessage
operator|+=
name|FooterConstants
operator|.
name|CHANGE_ID
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|currentChangeId
operator|+
literal|"\n"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|changeIdFooters
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"multiple Change-Id footers"
argument_list|)
throw|;
block|}
return|return
name|newCommitMessage
return|;
block|}
block|}
end_class

end_unit

