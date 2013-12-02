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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|gerrit
operator|.
name|common
operator|.
name|Version
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
name|GroupReference
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
name|LabelType
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
name|LabelValue
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
name|Permission
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
operator|.
name|Action
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
name|Project
operator|.
name|InheritableBoolean
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
name|config
operator|.
name|AllProjectsName
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
name|group
operator|.
name|SystemGroupBackend
import|;
end_import

begin_import
import|import static
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
operator|.
name|*
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/** Creates the {@code All-Projects} repository and initial ACLs. */
end_comment

begin_class
DECL|class|AllProjectsCreator
specifier|public
class|class
name|AllProjectsCreator
block|{
DECL|field|mgr
specifier|private
specifier|final
name|GitRepositoryManager
name|mgr
decl_stmt|;
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|serverUser
specifier|private
specifier|final
name|PersonIdent
name|serverUser
decl_stmt|;
DECL|field|admin
specifier|private
name|GroupReference
name|admin
decl_stmt|;
DECL|field|batch
specifier|private
name|GroupReference
name|batch
decl_stmt|;
DECL|field|anonymous
specifier|private
name|GroupReference
name|anonymous
decl_stmt|;
DECL|field|registered
specifier|private
name|GroupReference
name|registered
decl_stmt|;
DECL|field|owners
specifier|private
name|GroupReference
name|owners
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllProjectsCreator ( GitRepositoryManager mgr, AllProjectsName allProjectsName, @GerritPersonIdent PersonIdent serverUser)
name|AllProjectsCreator
parameter_list|(
name|GitRepositoryManager
name|mgr
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverUser
parameter_list|)
block|{
name|this
operator|.
name|mgr
operator|=
name|mgr
expr_stmt|;
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|serverUser
operator|=
name|serverUser
expr_stmt|;
name|this
operator|.
name|anonymous
operator|=
name|SystemGroupBackend
operator|.
name|getGroup
argument_list|(
name|ANONYMOUS_USERS
argument_list|)
expr_stmt|;
name|this
operator|.
name|registered
operator|=
name|SystemGroupBackend
operator|.
name|getGroup
argument_list|(
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|this
operator|.
name|owners
operator|=
name|SystemGroupBackend
operator|.
name|getGroup
argument_list|(
name|PROJECT_OWNERS
argument_list|)
expr_stmt|;
block|}
DECL|method|setAdministrators (GroupReference admin)
specifier|public
name|AllProjectsCreator
name|setAdministrators
parameter_list|(
name|GroupReference
name|admin
parameter_list|)
block|{
name|this
operator|.
name|admin
operator|=
name|admin
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setBatchUsers (GroupReference batch)
specifier|public
name|AllProjectsCreator
name|setBatchUsers
parameter_list|(
name|GroupReference
name|batch
parameter_list|)
block|{
name|this
operator|.
name|batch
operator|=
name|batch
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|create ()
specifier|public
name|void
name|create
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|Repository
name|git
init|=
literal|null
decl_stmt|;
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|openRepository
argument_list|(
name|allProjectsName
argument_list|)
expr_stmt|;
name|initAllProjects
argument_list|(
name|git
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|notFound
parameter_list|)
block|{
comment|// A repository may be missing if this project existed only to store
comment|// inheritable permissions. For example 'All-Projects'.
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|createRepository
argument_list|(
name|allProjectsName
argument_list|)
expr_stmt|;
name|initAllProjects
argument_list|(
name|git
argument_list|)
expr_stmt|;
name|RefUpdate
name|u
init|=
name|git
operator|.
name|updateRef
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
decl_stmt|;
name|u
operator|.
name|link
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|err
parameter_list|)
block|{
name|String
name|name
init|=
name|allProjectsName
operator|.
name|get
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create repository "
operator|+
name|name
argument_list|,
name|err
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|git
operator|!=
literal|null
condition|)
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|initAllProjects (Repository git)
specifier|private
name|void
name|initAllProjects
parameter_list|(
name|Repository
name|git
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allProjectsName
argument_list|,
name|git
argument_list|)
decl_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
literal|"Initialized Gerrit Code Review "
operator|+
name|Version
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|Project
name|p
init|=
name|config
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDescription
argument_list|(
literal|"Access inherited by all other projects."
argument_list|)
expr_stmt|;
name|p
operator|.
name|setRequireChangeID
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseContentMerge
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseSignedOffBy
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|AccessSection
name|cap
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|all
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|ALL
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|heads
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|HEADS
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|tags
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
literal|"refs/tags/*"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|meta
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|magic
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
literal|"refs/for/"
operator|+
name|AccessSection
operator|.
name|ALL
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|cap
argument_list|,
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|all
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|admin
argument_list|,
name|anonymous
argument_list|)
expr_stmt|;
if|if
condition|(
name|batch
operator|!=
literal|null
condition|)
block|{
name|Permission
name|priority
init|=
name|cap
operator|.
name|getPermission
argument_list|(
name|GlobalCapability
operator|.
name|PRIORITY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|PermissionRule
name|r
init|=
name|rule
argument_list|(
name|config
argument_list|,
name|batch
argument_list|)
decl_stmt|;
name|r
operator|.
name|setAction
argument_list|(
name|Action
operator|.
name|BATCH
argument_list|)
expr_stmt|;
name|priority
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|Permission
name|stream
init|=
name|cap
operator|.
name|getPermission
argument_list|(
name|GlobalCapability
operator|.
name|STREAM_EVENTS
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|stream
operator|.
name|add
argument_list|(
name|rule
argument_list|(
name|config
argument_list|,
name|batch
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|LabelType
name|cr
init|=
name|initCodeReviewLabel
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|cr
argument_list|,
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|cr
argument_list|,
operator|-
literal|2
argument_list|,
literal|2
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|FORGE_AUTHOR
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|FORGE_COMMITTER
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|EDIT_TOPIC_NAME
argument_list|,
literal|true
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|tags
argument_list|,
name|Permission
operator|.
name|PUSH_TAG
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|tags
argument_list|,
name|Permission
operator|.
name|PUSH_SIGNED_TAG
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|magic
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|magic
argument_list|,
name|Permission
operator|.
name|PUSH_MERGE
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|meta
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
literal|true
argument_list|)
operator|.
name|setExclusiveGroup
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|cr
argument_list|,
operator|-
literal|2
argument_list|,
literal|2
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, String permission, GroupReference... groupList)
specifier|private
name|void
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|String
name|permission
parameter_list|,
name|GroupReference
modifier|...
name|groupList
parameter_list|)
block|{
name|grant
argument_list|(
name|config
argument_list|,
name|section
argument_list|,
name|permission
argument_list|,
literal|false
argument_list|,
name|groupList
argument_list|)
expr_stmt|;
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, String permission, boolean force, GroupReference... groupList)
specifier|private
name|void
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|String
name|permission
parameter_list|,
name|boolean
name|force
parameter_list|,
name|GroupReference
modifier|...
name|groupList
parameter_list|)
block|{
name|Permission
name|p
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|permission
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupReference
name|group
range|:
name|groupList
control|)
block|{
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|PermissionRule
name|r
init|=
name|rule
argument_list|(
name|config
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|r
operator|.
name|setForce
argument_list|(
name|force
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, LabelType type, int min, int max, GroupReference... groupList)
specifier|private
name|void
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|LabelType
name|type
parameter_list|,
name|int
name|min
parameter_list|,
name|int
name|max
parameter_list|,
name|GroupReference
modifier|...
name|groupList
parameter_list|)
block|{
name|String
name|name
init|=
name|Permission
operator|.
name|LABEL
operator|+
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Permission
name|p
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupReference
name|group
range|:
name|groupList
control|)
block|{
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|PermissionRule
name|r
init|=
name|rule
argument_list|(
name|config
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|r
operator|.
name|setRange
argument_list|(
name|min
argument_list|,
name|max
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|rule (ProjectConfig config, GroupReference group)
specifier|private
name|PermissionRule
name|rule
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|GroupReference
name|group
parameter_list|)
block|{
return|return
operator|new
name|PermissionRule
argument_list|(
name|config
operator|.
name|resolve
argument_list|(
name|group
argument_list|)
argument_list|)
return|;
block|}
DECL|method|initCodeReviewLabel (ProjectConfig c)
specifier|public
specifier|static
name|LabelType
name|initCodeReviewLabel
parameter_list|(
name|ProjectConfig
name|c
parameter_list|)
block|{
name|LabelType
name|type
init|=
operator|new
name|LabelType
argument_list|(
literal|"Code-Review"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|,
literal|"Looks good to me, approved"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
literal|"I would prefer that you didn't submit this"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|2
argument_list|,
literal|"Do not submit"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|type
operator|.
name|setAbbreviation
argument_list|(
literal|"CR"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setCopyMinScore
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|c
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

