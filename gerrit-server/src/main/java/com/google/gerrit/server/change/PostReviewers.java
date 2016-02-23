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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|ImmutableMap
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
name|Lists
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
name|Maps
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
name|GroupDescription
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
name|NoSuchGroupException
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
name|AddReviewerInput
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
name|AccountGroup
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
name|ApprovalsUtil
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
name|account
operator|.
name|AccountCache
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
name|account
operator|.
name|AccountLoader
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
name|account
operator|.
name|AccountsCollection
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
name|account
operator|.
name|GroupMembers
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
name|ReviewerJson
operator|.
name|PostResult
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
name|ReviewerJson
operator|.
name|ReviewerInfo
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
name|git
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
name|git
operator|.
name|BatchUpdate
operator|.
name|ChangeContext
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
name|BatchUpdate
operator|.
name|Context
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
name|group
operator|.
name|GroupsCollection
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
name|group
operator|.
name|SystemGroupBackend
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
name|mail
operator|.
name|AddReviewerSender
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
name|NoSuchProjectException
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
name|Config
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
name|text
operator|.
name|MessageFormat
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PostReviewers
specifier|public
class|class
name|PostReviewers
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|AddReviewerInput
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
name|PostReviewers
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_MAX_REVIEWERS_WITHOUT_CHECK
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_MAX_REVIEWERS_WITHOUT_CHECK
init|=
literal|10
decl_stmt|;
DECL|field|DEFAULT_MAX_REVIEWERS
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_MAX_REVIEWERS
init|=
literal|20
decl_stmt|;
DECL|field|accounts
specifier|private
specifier|final
name|AccountsCollection
name|accounts
decl_stmt|;
DECL|field|reviewerFactory
specifier|private
specifier|final
name|ReviewerResource
operator|.
name|Factory
name|reviewerFactory
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|addReviewerSenderFactory
specifier|private
specifier|final
name|AddReviewerSender
operator|.
name|Factory
name|addReviewerSenderFactory
decl_stmt|;
DECL|field|groupsCollection
specifier|private
specifier|final
name|GroupsCollection
name|groupsCollection
decl_stmt|;
DECL|field|groupMembersFactory
specifier|private
specifier|final
name|GroupMembers
operator|.
name|Factory
name|groupMembersFactory
decl_stmt|;
DECL|field|accountLoaderFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
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
DECL|field|batchUpdateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|user
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ReviewerJson
name|json
decl_stmt|;
annotation|@
name|Inject
DECL|method|PostReviewers (AccountsCollection accounts, ReviewerResource.Factory reviewerFactory, ApprovalsUtil approvalsUtil, PatchSetUtil psUtil, AddReviewerSender.Factory addReviewerSenderFactory, GroupsCollection groupsCollection, GroupMembers.Factory groupMembersFactory, AccountLoader.Factory accountLoaderFactory, Provider<ReviewDb> db, BatchUpdate.Factory batchUpdateFactory, Provider<IdentifiedUser> user, IdentifiedUser.GenericFactory identifiedUserFactory, @GerritServerConfig Config cfg, ChangeHooks hooks, AccountCache accountCache, ReviewerJson json)
name|PostReviewers
parameter_list|(
name|AccountsCollection
name|accounts
parameter_list|,
name|ReviewerResource
operator|.
name|Factory
name|reviewerFactory
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|AddReviewerSender
operator|.
name|Factory
name|addReviewerSenderFactory
parameter_list|,
name|GroupsCollection
name|groupsCollection
parameter_list|,
name|GroupMembers
operator|.
name|Factory
name|groupMembersFactory
parameter_list|,
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|user
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|ReviewerJson
name|json
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|reviewerFactory
operator|=
name|reviewerFactory
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|addReviewerSenderFactory
operator|=
name|addReviewerSenderFactory
expr_stmt|;
name|this
operator|.
name|groupsCollection
operator|=
name|groupsCollection
expr_stmt|;
name|this
operator|.
name|groupMembersFactory
operator|=
name|groupMembersFactory
expr_stmt|;
name|this
operator|.
name|accountLoaderFactory
operator|=
name|accountLoaderFactory
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|batchUpdateFactory
operator|=
name|batchUpdateFactory
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, AddReviewerInput input)
specifier|public
name|PostResult
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|AddReviewerInput
name|input
parameter_list|)
throws|throws
name|UpdateException
throws|,
name|OrmException
throws|,
name|RestApiException
throws|,
name|IOException
block|{
if|if
condition|(
name|input
operator|.
name|reviewer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"missing reviewer field"
argument_list|)
throw|;
block|}
try|try
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
name|accounts
operator|.
name|parse
argument_list|(
name|input
operator|.
name|reviewer
argument_list|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
return|return
name|putAccount
argument_list|(
name|reviewerFactory
operator|.
name|create
argument_list|(
name|rsrc
argument_list|,
name|accountId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnprocessableEntityException
name|e
parameter_list|)
block|{
try|try
block|{
return|return
name|putGroup
argument_list|(
name|rsrc
argument_list|,
name|input
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnprocessableEntityException
name|e2
parameter_list|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
name|ChangeMessages
operator|.
name|get
argument_list|()
operator|.
name|reviewerNotFound
argument_list|,
name|input
operator|.
name|reviewer
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|putAccount (ReviewerResource rsrc)
specifier|private
name|PostResult
name|putAccount
parameter_list|(
name|ReviewerResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
throws|,
name|UpdateException
throws|,
name|RestApiException
block|{
name|Account
name|member
init|=
name|rsrc
operator|.
name|getReviewerUser
argument_list|()
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|ChangeControl
name|control
init|=
name|rsrc
operator|.
name|getReviewerControl
argument_list|()
decl_stmt|;
name|PostResult
name|result
init|=
operator|new
name|PostResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|isValidReviewer
argument_list|(
name|member
argument_list|,
name|control
argument_list|)
condition|)
block|{
name|addReviewers
argument_list|(
name|rsrc
operator|.
name|getChangeResource
argument_list|()
argument_list|,
name|result
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|(
name|member
operator|.
name|getId
argument_list|()
argument_list|,
name|control
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|putGroup (ChangeResource rsrc, AddReviewerInput input)
specifier|private
name|PostResult
name|putGroup
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|AddReviewerInput
name|input
parameter_list|)
throws|throws
name|UpdateException
throws|,
name|RestApiException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|GroupDescription
operator|.
name|Basic
name|group
init|=
name|groupsCollection
operator|.
name|parseInternal
argument_list|(
name|input
operator|.
name|reviewer
argument_list|)
decl_stmt|;
name|PostResult
name|result
init|=
operator|new
name|PostResult
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isLegalReviewerGroup
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|error
operator|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|ChangeMessages
operator|.
name|get
argument_list|()
operator|.
name|groupIsNotAllowed
argument_list|,
name|group
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ChangeControl
argument_list|>
name|reviewers
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|ChangeControl
name|control
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Account
argument_list|>
name|members
decl_stmt|;
try|try
block|{
name|members
operator|=
name|groupMembersFactory
operator|.
name|create
argument_list|(
name|control
operator|.
name|getUser
argument_list|()
argument_list|)
operator|.
name|listAccounts
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|control
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
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
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
comment|// if maxAllowed is set to 0, it is allowed to add any number of
comment|// reviewers
name|int
name|maxAllowed
init|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"addreviewer"
argument_list|,
literal|"maxAllowed"
argument_list|,
name|DEFAULT_MAX_REVIEWERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxAllowed
operator|>
literal|0
operator|&&
name|members
operator|.
name|size
argument_list|()
operator|>
name|maxAllowed
condition|)
block|{
name|result
operator|.
name|error
operator|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|ChangeMessages
operator|.
name|get
argument_list|()
operator|.
name|groupHasTooManyMembers
argument_list|,
name|group
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|// if maxWithoutCheck is set to 0, we never ask for confirmation
name|int
name|maxWithoutConfirmation
init|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"addreviewer"
argument_list|,
literal|"maxWithoutConfirmation"
argument_list|,
name|DEFAULT_MAX_REVIEWERS_WITHOUT_CHECK
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|input
operator|.
name|confirmed
argument_list|()
operator|&&
name|maxWithoutConfirmation
operator|>
literal|0
operator|&&
name|members
operator|.
name|size
argument_list|()
operator|>
name|maxWithoutConfirmation
condition|)
block|{
name|result
operator|.
name|confirm
operator|=
literal|true
expr_stmt|;
name|result
operator|.
name|error
operator|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|ChangeMessages
operator|.
name|get
argument_list|()
operator|.
name|groupManyMembersConfirmation
argument_list|,
name|group
operator|.
name|getName
argument_list|()
argument_list|,
name|members
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
for|for
control|(
name|Account
name|member
range|:
name|members
control|)
block|{
if|if
condition|(
name|isValidReviewer
argument_list|(
name|member
argument_list|,
name|control
argument_list|)
condition|)
block|{
name|reviewers
operator|.
name|put
argument_list|(
name|member
operator|.
name|getId
argument_list|()
argument_list|,
name|control
argument_list|)
expr_stmt|;
block|}
block|}
name|addReviewers
argument_list|(
name|rsrc
argument_list|,
name|result
argument_list|,
name|reviewers
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|isValidReviewer (Account member, ChangeControl control)
specifier|private
name|boolean
name|isValidReviewer
parameter_list|(
name|Account
name|member
parameter_list|,
name|ChangeControl
name|control
parameter_list|)
block|{
if|if
condition|(
name|member
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|IdentifiedUser
name|user
init|=
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|member
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
comment|// Does not account for draft status as a user might want to let a
comment|// reviewer see a draft.
return|return
name|control
operator|.
name|forUser
argument_list|(
name|user
argument_list|)
operator|.
name|isRefVisible
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|addReviewers ( ChangeResource rsrc, PostResult result, Map<Account.Id, ChangeControl> reviewers)
specifier|private
name|void
name|addReviewers
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|PostResult
name|result
parameter_list|,
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ChangeControl
argument_list|>
name|reviewers
parameter_list|)
throws|throws
name|OrmException
throws|,
name|RestApiException
throws|,
name|UpdateException
block|{
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
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
name|Op
name|op
init|=
operator|new
name|Op
argument_list|(
name|rsrc
argument_list|,
name|reviewers
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
name|op
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
name|result
operator|.
name|reviewers
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|op
operator|.
name|added
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|op
operator|.
name|added
control|)
block|{
comment|// New reviewers have value 0, don't bother normalizing.
name|result
operator|.
name|reviewers
operator|.
name|add
argument_list|(
name|json
operator|.
name|format
argument_list|(
operator|new
name|ReviewerInfo
argument_list|(
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
name|reviewers
operator|.
name|get
argument_list|(
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|psa
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// We don't do this inside Op, since the accounts are in a different
comment|// table.
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
operator|.
name|fill
argument_list|(
name|result
operator|.
name|reviewers
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Op
specifier|private
class|class
name|Op
extends|extends
name|BatchUpdate
operator|.
name|Op
block|{
DECL|field|rsrc
specifier|private
specifier|final
name|ChangeResource
name|rsrc
decl_stmt|;
DECL|field|reviewers
specifier|private
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ChangeControl
argument_list|>
name|reviewers
decl_stmt|;
DECL|field|added
specifier|private
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|added
decl_stmt|;
DECL|field|patchSet
specifier|private
name|PatchSet
name|patchSet
decl_stmt|;
DECL|method|Op (ChangeResource rsrc, Map<Account.Id, ChangeControl> reviewers)
name|Op
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ChangeControl
argument_list|>
name|reviewers
parameter_list|)
block|{
name|this
operator|.
name|rsrc
operator|=
name|rsrc
expr_stmt|;
name|this
operator|.
name|reviewers
operator|=
name|reviewers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateChange (ChangeContext ctx)
specifier|public
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|added
operator|=
name|approvalsUtil
operator|.
name|addReviewers
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|ctx
operator|.
name|getNotes
argument_list|()
argument_list|,
name|ctx
operator|.
name|getUpdate
argument_list|(
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
argument_list|,
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|,
name|reviewers
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|added
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|patchSet
operator|=
name|psUtil
operator|.
name|current
argument_list|(
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
expr_stmt|;
block|}
return|return
operator|!
name|added
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|postUpdate (Context ctx)
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Context
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|emailReviewers
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|,
name|added
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|added
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|added
control|)
block|{
name|Account
name|account
init|=
name|accountCache
operator|.
name|get
argument_list|(
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|hooks
operator|.
name|doReviewerAddedHook
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|,
name|account
argument_list|,
name|patchSet
argument_list|,
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|emailReviewers (Change change, List<PatchSetApproval> added)
specifier|private
name|void
name|emailReviewers
parameter_list|(
name|Change
name|change
parameter_list|,
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|added
parameter_list|)
block|{
if|if
condition|(
name|added
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// Email the reviewers
comment|//
comment|// The user knows they added themselves, don't bother emailing them.
name|List
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|toMail
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|added
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Account
operator|.
name|Id
name|userId
init|=
name|user
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|added
control|)
block|{
if|if
condition|(
operator|!
name|psa
operator|.
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|userId
argument_list|)
condition|)
block|{
name|toMail
operator|.
name|add
argument_list|(
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|toMail
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|AddReviewerSender
name|cm
init|=
name|addReviewerSenderFactory
operator|.
name|create
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
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|userId
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addReviewers
argument_list|(
name|toMail
argument_list|)
expr_stmt|;
name|cm
operator|.
name|send
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email to new reviewers of change "
operator|+
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isLegalReviewerGroup (AccountGroup.UUID groupUUID)
specifier|public
specifier|static
name|boolean
name|isLegalReviewerGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|)
block|{
return|return
operator|!
name|SystemGroupBackend
operator|.
name|isSystemGroup
argument_list|(
name|groupUUID
argument_list|)
return|;
block|}
block|}
end_class

end_unit

