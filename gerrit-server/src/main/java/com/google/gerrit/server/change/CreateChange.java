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
name|data
operator|.
name|Capable
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
name|ChangeStatus
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
name|MethodNotAllowedException
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
name|TopLevelResource
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
name|config
operator|.
name|GerritServerConfig
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
name|events
operator|.
name|CommitReceivedEvent
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
name|validators
operator|.
name|CommitValidationException
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
name|validators
operator|.
name|CommitValidators
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
name|ProjectResource
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
name|ProjectsCollection
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
name|RefControl
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
name|ssh
operator|.
name|NoSshInfo
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
name|Config
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
name|util
operator|.
name|ChangeIdUtil
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
name|io
operator|.
name|UnsupportedEncodingException
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

begin_class
annotation|@
name|Singleton
DECL|class|CreateChange
specifier|public
class|class
name|CreateChange
implements|implements
name|RestModifyView
argument_list|<
name|TopLevelResource
argument_list|,
name|ChangeInfo
argument_list|>
block|{
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|serverTimeZone
specifier|private
specifier|final
name|TimeZone
name|serverTimeZone
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
DECL|field|projectsCollection
specifier|private
specifier|final
name|ProjectsCollection
name|projectsCollection
decl_stmt|;
DECL|field|commitValidatorsFactory
specifier|private
specifier|final
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
decl_stmt|;
DECL|field|changeInserterFactory
specifier|private
specifier|final
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
name|json
decl_stmt|;
DECL|field|changeUtil
specifier|private
specifier|final
name|ChangeUtil
name|changeUtil
decl_stmt|;
DECL|field|allowDrafts
specifier|private
specifier|final
name|boolean
name|allowDrafts
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateChange (Provider<ReviewDb> db, GitRepositoryManager gitManager, @GerritPersonIdent PersonIdent myIdent, Provider<CurrentUser> userProvider, ProjectsCollection projectsCollection, CommitValidators.Factory commitValidatorsFactory, ChangeInserter.Factory changeInserterFactory, ChangeJson json, ChangeUtil changeUtil, @GerritServerConfig Config config)
name|CreateChange
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|GitRepositoryManager
name|gitManager
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
name|userProvider
parameter_list|,
name|ProjectsCollection
name|projectsCollection
parameter_list|,
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
parameter_list|,
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
parameter_list|,
name|ChangeJson
name|json
parameter_list|,
name|ChangeUtil
name|changeUtil
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|gitManager
operator|=
name|gitManager
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
name|userProvider
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|projectsCollection
operator|=
name|projectsCollection
expr_stmt|;
name|this
operator|.
name|commitValidatorsFactory
operator|=
name|commitValidatorsFactory
expr_stmt|;
name|this
operator|.
name|changeInserterFactory
operator|=
name|changeInserterFactory
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|changeUtil
operator|=
name|changeUtil
expr_stmt|;
name|this
operator|.
name|allowDrafts
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"change"
argument_list|,
literal|"allowDrafts"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource parent, ChangeInfo input)
specifier|public
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|TopLevelResource
name|parent
parameter_list|,
name|ChangeInfo
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|OrmException
throws|,
name|BadRequestException
throws|,
name|UnprocessableEntityException
throws|,
name|IOException
throws|,
name|InvalidChangeOperationException
throws|,
name|ResourceNotFoundException
throws|,
name|MethodNotAllowedException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|project
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"project must be non-empty"
argument_list|)
throw|;
block|}
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|branch
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"branch must be non-empty"
argument_list|)
throw|;
block|}
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|subject
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"commit message must be non-empty"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|status
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|input
operator|.
name|status
operator|!=
name|ChangeStatus
operator|.
name|NEW
operator|&&
name|input
operator|.
name|status
operator|!=
name|ChangeStatus
operator|.
name|DRAFT
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"unsupported change status"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|allowDrafts
operator|&&
name|input
operator|.
name|status
operator|==
name|ChangeStatus
operator|.
name|DRAFT
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"draft workflow is disabled"
argument_list|)
throw|;
block|}
block|}
name|String
name|refName
init|=
name|RefNames
operator|.
name|fullName
argument_list|(
name|input
operator|.
name|branch
argument_list|)
decl_stmt|;
name|ProjectResource
name|rsrc
init|=
name|projectsCollection
operator|.
name|parse
argument_list|(
name|input
operator|.
name|project
argument_list|)
decl_stmt|;
name|Capable
name|r
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|canPushToAtLeastOneRef
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|!=
name|Capable
operator|.
name|OK
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
name|r
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
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
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|refControl
operator|.
name|canUpload
argument_list|()
operator|||
operator|!
name|refControl
operator|.
name|canRead
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"cannot upload review"
argument_list|)
throw|;
block|}
name|Project
operator|.
name|NameKey
name|project
init|=
name|rsrc
operator|.
name|getNameKey
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
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|ObjectId
name|parentCommit
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|groups
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|baseChange
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Change
argument_list|>
name|changes
init|=
name|changeUtil
operator|.
name|findChanges
argument_list|(
name|input
operator|.
name|baseChange
argument_list|)
decl_stmt|;
if|if
condition|(
name|changes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
literal|"Base change not found: "
operator|+
name|input
operator|.
name|baseChange
argument_list|)
throw|;
block|}
name|Change
name|change
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|changes
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|controlFor
argument_list|(
name|change
argument_list|)
operator|.
name|isVisible
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
literal|"Base change not found: "
operator|+
name|input
operator|.
name|baseChange
argument_list|)
throw|;
block|}
name|PatchSet
name|ps
init|=
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
name|change
operator|.
name|currentPatchSetId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|parentCommit
operator|=
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
expr_stmt|;
name|groups
operator|=
name|ps
operator|.
name|getGroups
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Ref
name|destRef
init|=
name|git
operator|.
name|getRef
argument_list|(
name|refName
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
name|String
operator|.
name|format
argument_list|(
literal|"Branch %s does not exist."
argument_list|,
name|refName
argument_list|)
argument_list|)
throw|;
block|}
name|parentCommit
operator|=
name|destRef
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
name|groups
operator|=
literal|null
expr_stmt|;
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
operator|(
name|IdentifiedUser
operator|)
name|userProvider
operator|.
name|get
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
name|ObjectId
name|id
init|=
name|ChangeIdUtil
operator|.
name|computeChangeId
argument_list|(
name|mergeTip
operator|.
name|getTree
argument_list|()
argument_list|,
name|mergeTip
argument_list|,
name|author
argument_list|,
name|author
argument_list|,
name|input
operator|.
name|subject
argument_list|)
decl_stmt|;
name|String
name|commitMessage
init|=
name|ChangeIdUtil
operator|.
name|insertId
argument_list|(
name|input
operator|.
name|subject
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|RevCommit
name|c
init|=
name|newCommit
argument_list|(
name|git
argument_list|,
name|rw
argument_list|,
name|author
argument_list|,
name|mergeTip
argument_list|,
name|commitMessage
argument_list|)
decl_stmt|;
name|Change
name|change
init|=
operator|new
name|Change
argument_list|(
name|getChangeId
argument_list|(
name|id
argument_list|,
name|c
argument_list|)
argument_list|,
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|db
operator|.
name|get
argument_list|()
operator|.
name|nextChangeId
argument_list|()
argument_list|)
argument_list|,
name|me
operator|.
name|getAccountId
argument_list|()
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
name|refName
argument_list|)
argument_list|,
name|now
argument_list|)
decl_stmt|;
name|ChangeInserter
name|ins
init|=
name|changeInserterFactory
operator|.
name|create
argument_list|(
name|refControl
operator|.
name|getProjectControl
argument_list|()
argument_list|,
name|change
argument_list|,
name|c
argument_list|)
decl_stmt|;
name|validateCommit
argument_list|(
name|git
argument_list|,
name|refControl
argument_list|,
name|c
argument_list|,
name|me
argument_list|,
name|ins
argument_list|)
expr_stmt|;
name|updateRef
argument_list|(
name|git
argument_list|,
name|rw
argument_list|,
name|c
argument_list|,
name|change
argument_list|,
name|ins
operator|.
name|getPatchSet
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setTopic
argument_list|(
name|input
operator|.
name|topic
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setDraft
argument_list|(
name|input
operator|.
name|status
operator|!=
literal|null
operator|&&
name|input
operator|.
name|status
operator|==
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setGroups
argument_list|(
name|groups
argument_list|)
expr_stmt|;
name|ins
operator|.
name|insert
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|created
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|validateCommit (Repository git, RefControl refControl, RevCommit c, IdentifiedUser me, ChangeInserter ins)
specifier|private
name|void
name|validateCommit
parameter_list|(
name|Repository
name|git
parameter_list|,
name|RefControl
name|refControl
parameter_list|,
name|RevCommit
name|c
parameter_list|,
name|IdentifiedUser
name|me
parameter_list|,
name|ChangeInserter
name|ins
parameter_list|)
throws|throws
name|InvalidChangeOperationException
block|{
name|PatchSet
name|newPatchSet
init|=
name|ins
operator|.
name|getPatchSet
argument_list|()
decl_stmt|;
name|CommitValidators
name|commitValidators
init|=
name|commitValidatorsFactory
operator|.
name|create
argument_list|(
name|refControl
argument_list|,
operator|new
name|NoSshInfo
argument_list|()
argument_list|,
name|git
argument_list|)
decl_stmt|;
name|CommitReceivedEvent
name|commitReceivedEvent
init|=
operator|new
name|CommitReceivedEvent
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|newPatchSet
operator|.
name|getRefName
argument_list|()
argument_list|)
argument_list|,
name|refControl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|refControl
operator|.
name|getRefName
argument_list|()
argument_list|,
name|c
argument_list|,
name|me
argument_list|)
decl_stmt|;
try|try
block|{
name|commitValidators
operator|.
name|validateForGerritCommits
argument_list|(
name|commitReceivedEvent
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CommitValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|updateRef (Repository git, RevWalk rw, RevCommit c, Change change, PatchSet newPatchSet)
specifier|private
specifier|static
name|void
name|updateRef
parameter_list|(
name|Repository
name|git
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|RevCommit
name|c
parameter_list|,
name|Change
name|change
parameter_list|,
name|PatchSet
name|newPatchSet
parameter_list|)
throws|throws
name|IOException
block|{
name|RefUpdate
name|ru
init|=
name|git
operator|.
name|updateRef
argument_list|(
name|newPatchSet
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|ru
operator|.
name|disableRefLog
argument_list|()
expr_stmt|;
if|if
condition|(
name|ru
operator|.
name|update
argument_list|(
name|rw
argument_list|)
operator|!=
name|RefUpdate
operator|.
name|Result
operator|.
name|NEW
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to create ref %s in %s: %s"
argument_list|,
name|newPatchSet
operator|.
name|getRefName
argument_list|()
argument_list|,
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|ru
operator|.
name|getResult
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|getChangeId (ObjectId id, RevCommit emptyCommit)
specifier|private
specifier|static
name|Change
operator|.
name|Key
name|getChangeId
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|RevCommit
name|emptyCommit
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|idList
init|=
name|emptyCommit
operator|.
name|getFooterLines
argument_list|(
name|FooterConstants
operator|.
name|CHANGE_ID
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Key
name|changeKey
init|=
operator|!
name|idList
operator|.
name|isEmpty
argument_list|()
condition|?
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|idList
operator|.
name|get
argument_list|(
name|idList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
else|:
operator|new
name|Change
operator|.
name|Key
argument_list|(
literal|"I"
operator|+
name|id
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|changeKey
return|;
block|}
DECL|method|newCommit (Repository git, RevWalk rw, PersonIdent authorIdent, RevCommit mergeTip, String commitMessage)
specifier|private
specifier|static
name|RevCommit
name|newCommit
parameter_list|(
name|Repository
name|git
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|PersonIdent
name|authorIdent
parameter_list|,
name|RevCommit
name|mergeTip
parameter_list|,
name|String
name|commitMessage
parameter_list|)
throws|throws
name|IOException
block|{
name|RevCommit
name|emptyCommit
decl_stmt|;
try|try
init|(
name|ObjectInserter
name|oi
init|=
name|git
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|CommitBuilder
name|commit
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|commit
operator|.
name|setTreeId
argument_list|(
name|mergeTip
operator|.
name|getTree
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setParentId
argument_list|(
name|mergeTip
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setAuthor
argument_list|(
name|authorIdent
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setCommitter
argument_list|(
name|authorIdent
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setMessage
argument_list|(
name|commitMessage
argument_list|)
expr_stmt|;
name|emptyCommit
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|insert
argument_list|(
name|oi
argument_list|,
name|commit
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|emptyCommit
return|;
block|}
DECL|method|insert (ObjectInserter inserter, CommitBuilder commit)
specifier|private
specifier|static
name|ObjectId
name|insert
parameter_list|(
name|ObjectInserter
name|inserter
parameter_list|,
name|CommitBuilder
name|commit
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedEncodingException
block|{
name|ObjectId
name|id
init|=
name|inserter
operator|.
name|insert
argument_list|(
name|commit
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

