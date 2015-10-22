begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
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
name|Nullable
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
name|AccessSection
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
name|GlobalCapability
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
name|PermissionRule
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
name|account
operator|.
name|GroupBackend
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
name|ChangeInserter
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
name|ChangesCollection
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
name|PostReviewers
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
name|AllProjectsNameProvider
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
name|MetaDataUpdate
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
name|ProjectConfig
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
name|ProjectControl
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
name|SetParent
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
name|List
import|;
end_import

begin_class
DECL|class|ReviewProjectAccess
specifier|public
class|class
name|ReviewProjectAccess
extends|extends
name|ProjectAccessHandler
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ( @ssistedR) Project.NameKey projectName, @Nullable @Assisted ObjectId base, @Assisted List<AccessSection> sectionList, @Nullable @Assisted(R) Project.NameKey parentProjectName, @Nullable @Assisted String message)
name|ReviewProjectAccess
name|create
parameter_list|(
annotation|@
name|Assisted
argument_list|(
literal|"projectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|ObjectId
name|base
parameter_list|,
annotation|@
name|Assisted
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sectionList
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
argument_list|(
literal|"parentProjectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|parentProjectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|String
name|message
parameter_list|)
function_decl|;
block|}
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|reviewersProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|PostReviewers
argument_list|>
name|reviewersProvider
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|changes
specifier|private
specifier|final
name|ChangesCollection
name|changes
decl_stmt|;
DECL|field|changeInserterFactory
specifier|private
specifier|final
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReviewProjectAccess (final ProjectControl.Factory projectControlFactory, GroupBackend groupBackend, MetaDataUpdate.User metaDataUpdateFactory, ReviewDb db, IdentifiedUser user, Provider<PostReviewers> reviewersProvider, ProjectCache projectCache, AllProjectsNameProvider allProjects, ChangesCollection changes, ChangeInserter.Factory changeInserterFactory, BatchUpdate.Factory updateFactory, Provider<SetParent> setParent, @Assisted(R) Project.NameKey projectName, @Nullable @Assisted ObjectId base, @Assisted List<AccessSection> sectionList, @Nullable @Assisted(R) Project.NameKey parentProjectName, @Nullable @Assisted String message)
name|ReviewProjectAccess
parameter_list|(
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
parameter_list|,
name|ReviewDb
name|db
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|,
name|Provider
argument_list|<
name|PostReviewers
argument_list|>
name|reviewersProvider
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|AllProjectsNameProvider
name|allProjects
parameter_list|,
name|ChangesCollection
name|changes
parameter_list|,
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|Provider
argument_list|<
name|SetParent
argument_list|>
name|setParent
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"projectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|ObjectId
name|base
parameter_list|,
annotation|@
name|Assisted
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sectionList
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
argument_list|(
literal|"parentProjectName"
argument_list|)
name|Project
operator|.
name|NameKey
name|parentProjectName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|projectControlFactory
argument_list|,
name|groupBackend
argument_list|,
name|metaDataUpdateFactory
argument_list|,
name|allProjects
argument_list|,
name|setParent
argument_list|,
name|projectName
argument_list|,
name|base
argument_list|,
name|sectionList
argument_list|,
name|parentProjectName
argument_list|,
name|message
argument_list|,
literal|false
argument_list|)
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
name|this
operator|.
name|reviewersProvider
operator|=
name|reviewersProvider
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|changes
operator|=
name|changes
expr_stmt|;
name|this
operator|.
name|changeInserterFactory
operator|=
name|changeInserterFactory
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateProjectConfig (ProjectControl ctl, ProjectConfig config, MetaDataUpdate md, boolean parentProjectUpdate)
specifier|protected
name|Change
operator|.
name|Id
name|updateProjectConfig
parameter_list|(
name|ProjectControl
name|ctl
parameter_list|,
name|ProjectConfig
name|config
parameter_list|,
name|MetaDataUpdate
name|md
parameter_list|,
name|boolean
name|parentProjectUpdate
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|md
operator|.
name|setInsertChangeId
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Change
operator|.
name|Id
name|changeId
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|db
operator|.
name|nextChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|RevCommit
name|commit
init|=
name|config
operator|.
name|commitToNewRef
argument_list|(
name|md
argument_list|,
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
name|Change
operator|.
name|INITIAL_PATCH_SET_ID
argument_list|)
operator|.
name|toRefName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|commit
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|base
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Change
name|change
init|=
operator|new
name|Change
argument_list|(
name|getChangeId
argument_list|(
name|commit
argument_list|)
argument_list|,
name|changeId
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|md
operator|.
name|getRepository
argument_list|()
argument_list|)
init|;
name|ObjectInserter
name|objInserter
operator|=
name|md
operator|.
name|getRepository
argument_list|()
operator|.
name|newObjectInserter
argument_list|()
init|;
name|BatchUpdate
name|bu
operator|=
name|updateFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|ctl
operator|.
name|getUser
argument_list|()
argument_list|,
name|change
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
init|)
block|{
name|bu
operator|.
name|setRepository
argument_list|(
name|md
operator|.
name|getRepository
argument_list|()
argument_list|,
name|rw
argument_list|,
name|objInserter
argument_list|)
expr_stmt|;
name|bu
operator|.
name|insertChange
argument_list|(
name|changeInserterFactory
operator|.
name|create
argument_list|(
name|ctl
operator|.
name|controlForRef
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|change
argument_list|,
name|commit
argument_list|)
operator|.
name|setValidatePolicy
argument_list|(
name|CommitValidators
operator|.
name|Policy
operator|.
name|NONE
argument_list|)
operator|.
name|setUpdateRef
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// Created by commitToNewRef.
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UpdateException
decl||
name|RestApiException
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
name|ChangeResource
name|rsrc
decl_stmt|;
try|try
block|{
name|rsrc
operator|=
name|changes
operator|.
name|parse
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
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
name|addProjectOwnersAsReviewers
argument_list|(
name|rsrc
argument_list|)
expr_stmt|;
if|if
condition|(
name|parentProjectUpdate
condition|)
block|{
name|addAdministratorsAsReviewers
argument_list|(
name|rsrc
argument_list|)
expr_stmt|;
block|}
return|return
name|changeId
return|;
block|}
DECL|method|getChangeId (RevCommit commit)
specifier|private
specifier|static
name|Change
operator|.
name|Key
name|getChangeId
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|idList
init|=
name|commit
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
name|commit
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|changeKey
return|;
block|}
DECL|method|addProjectOwnersAsReviewers (ChangeResource rsrc)
specifier|private
name|void
name|addProjectOwnersAsReviewers
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
block|{
specifier|final
name|String
name|projectOwners
init|=
name|groupBackend
operator|.
name|get
argument_list|(
name|SystemGroupBackend
operator|.
name|PROJECT_OWNERS
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
try|try
block|{
name|AddReviewerInput
name|input
init|=
operator|new
name|AddReviewerInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|reviewer
operator|=
name|projectOwners
expr_stmt|;
name|reviewersProvider
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|OrmException
decl||
name|RestApiException
name|e
parameter_list|)
block|{
comment|// one of the owner groups is not visible to the user and this it why it
comment|// can't be added as reviewer
block|}
block|}
DECL|method|addAdministratorsAsReviewers (ChangeResource rsrc)
specifier|private
name|void
name|addAdministratorsAsReviewers
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
block|{
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|adminRules
init|=
name|projectCache
operator|.
name|getAllProjects
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|)
operator|.
name|getPermission
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
operator|.
name|getRules
argument_list|()
decl_stmt|;
for|for
control|(
name|PermissionRule
name|r
range|:
name|adminRules
control|)
block|{
try|try
block|{
name|AddReviewerInput
name|input
init|=
operator|new
name|AddReviewerInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|reviewer
operator|=
name|r
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|reviewersProvider
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|OrmException
decl||
name|RestApiException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
end_class

end_unit

