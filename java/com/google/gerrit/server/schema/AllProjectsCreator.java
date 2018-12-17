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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_SEQUENCES
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
name|ANONYMOUS_USERS
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
name|PROJECT_OWNERS
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
name|REGISTERED_USERS
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
name|schema
operator|.
name|AclUtil
operator|.
name|grant
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
name|schema
operator|.
name|AclUtil
operator|.
name|rule
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
name|extensions
operator|.
name|client
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
name|reviewdb
operator|.
name|client
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
name|Sequences
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
name|UsedAt
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
name|meta
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
name|notedb
operator|.
name|NoteDbSchemaVersionManager
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
name|RepoSequence
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
name|ProjectConfig
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
name|BatchRefUpdate
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
name|NullProgressMonitor
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

begin_comment
comment|/** Creates the {@code All-Projects} repository and initial ACLs. */
end_comment

begin_class
DECL|class|AllProjectsCreator
specifier|public
class|class
name|AllProjectsCreator
block|{
DECL|field|repositoryManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repositoryManager
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
DECL|field|versionManager
specifier|private
specifier|final
name|NoteDbSchemaVersionManager
name|versionManager
decl_stmt|;
DECL|field|projectConfigFactory
specifier|private
specifier|final
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
decl_stmt|;
DECL|field|anonymous
specifier|private
specifier|final
name|GroupReference
name|anonymous
decl_stmt|;
DECL|field|registered
specifier|private
specifier|final
name|GroupReference
name|registered
decl_stmt|;
DECL|field|owners
specifier|private
specifier|final
name|GroupReference
name|owners
decl_stmt|;
DECL|field|admin
annotation|@
name|Nullable
specifier|private
name|GroupReference
name|admin
decl_stmt|;
DECL|field|batch
annotation|@
name|Nullable
specifier|private
name|GroupReference
name|batch
decl_stmt|;
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|field|firstChangeId
specifier|private
name|int
name|firstChangeId
init|=
name|Sequences
operator|.
name|FIRST_CHANGE_ID
decl_stmt|;
DECL|field|codeReviewLabel
specifier|private
name|LabelType
name|codeReviewLabel
decl_stmt|;
DECL|field|additionalLabelType
specifier|private
name|List
argument_list|<
name|LabelType
argument_list|>
name|additionalLabelType
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllProjectsCreator ( GitRepositoryManager repositoryManager, AllProjectsName allProjectsName, @GerritPersonIdent PersonIdent serverUser, NoteDbSchemaVersionManager versionManager, SystemGroupBackend systemGroupBackend, ProjectConfig.Factory projectConfigFactory)
name|AllProjectsCreator
parameter_list|(
name|GitRepositoryManager
name|repositoryManager
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverUser
parameter_list|,
name|NoteDbSchemaVersionManager
name|versionManager
parameter_list|,
name|SystemGroupBackend
name|systemGroupBackend
parameter_list|,
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
parameter_list|)
block|{
name|this
operator|.
name|repositoryManager
operator|=
name|repositoryManager
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
name|versionManager
operator|=
name|versionManager
expr_stmt|;
name|this
operator|.
name|projectConfigFactory
operator|=
name|projectConfigFactory
expr_stmt|;
name|this
operator|.
name|anonymous
operator|=
name|systemGroupBackend
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
name|systemGroupBackend
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
name|systemGroupBackend
operator|.
name|getGroup
argument_list|(
name|PROJECT_OWNERS
argument_list|)
expr_stmt|;
name|this
operator|.
name|codeReviewLabel
operator|=
name|getDefaultCodeReviewLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|additionalLabelType
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/** If called, grant default permissions to this admin group */
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
comment|/** If called, grant stream-events permission and set appropriate priority for this group */
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
DECL|method|setCommitMessage (String message)
specifier|public
name|AllProjectsCreator
name|setCommitMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|setFirstChangeIdForNoteDb (int id)
specifier|public
name|AllProjectsCreator
name|setFirstChangeIdForNoteDb
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|id
operator|>
literal|0
argument_list|,
literal|"id must be positive: %s"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|firstChangeId
operator|=
name|id
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** If called, the provided "Code-Review" label will be used rather than the default. */
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|setCodeReviewLabel (LabelType labelType)
specifier|public
name|AllProjectsCreator
name|setCodeReviewLabel
parameter_list|(
name|LabelType
name|labelType
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|labelType
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Code-Review"
argument_list|)
argument_list|,
literal|"label should have 'Code-Review' as its name"
argument_list|)
expr_stmt|;
name|this
operator|.
name|codeReviewLabel
operator|=
name|labelType
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|addAdditionalLabel (LabelType labelType)
specifier|public
name|AllProjectsCreator
name|addAdditionalLabel
parameter_list|(
name|LabelType
name|labelType
parameter_list|)
block|{
name|additionalLabelType
operator|.
name|add
argument_list|(
name|labelType
argument_list|)
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
throws|,
name|OrmException
block|{
try|try
init|(
name|Repository
name|git
init|=
name|repositoryManager
operator|.
name|openRepository
argument_list|(
name|allProjectsName
argument_list|)
init|)
block|{
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
init|(
name|Repository
name|git
init|=
name|repositoryManager
operator|.
name|createRepository
argument_list|(
name|allProjectsName
argument_list|)
init|)
block|{
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
name|RefNames
operator|.
name|REFS_CONFIG
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
throws|,
name|OrmException
block|{
name|BatchRefUpdate
name|bru
init|=
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
try|try
init|(
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
argument_list|,
name|bru
argument_list|)
init|)
block|{
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
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|message
argument_list|)
argument_list|,
literal|"Initialized Gerrit Code Review "
operator|+
name|Version
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ProjectConfig
name|config
init|=
name|projectConfigFactory
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
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|REQUIRE_CHANGE_ID
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_CONTENT_MERGE
argument_list|,
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_CONTRIBUTOR_AGREEMENTS
argument_list|,
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_SIGNED_OFF_BY
argument_list|,
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|ENABLE_SIGNED_PUSH
argument_list|,
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
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|refsFor
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
literal|"refs/for/*"
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
name|grant
argument_list|(
name|config
argument_list|,
name|refsFor
argument_list|,
name|Permission
operator|.
name|ADD_PATCH_SET
argument_list|,
name|registered
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
name|initLabels
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|codeReviewLabel
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
name|codeReviewLabel
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
name|tags
argument_list|,
name|Permission
operator|.
name|CREATE_TAG
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
name|CREATE_SIGNED_TAG
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
name|codeReviewLabel
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
name|commitToNewRef
argument_list|(
name|md
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
expr_stmt|;
name|initSequences
argument_list|(
name|git
argument_list|,
name|bru
argument_list|)
expr_stmt|;
comment|// init schema
name|versionManager
operator|.
name|init
argument_list|()
expr_stmt|;
name|execute
argument_list|(
name|git
argument_list|,
name|bru
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|getDefaultCodeReviewLabel ()
specifier|public
specifier|static
name|LabelType
name|getDefaultCodeReviewLabel
parameter_list|()
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
literal|"I would prefer this is not merged as is"
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
literal|"This shall not be merged"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|type
operator|.
name|setCopyMinScore
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|type
operator|.
name|setCopyAllScoresOnTrivialRebase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
DECL|method|initLabels (ProjectConfig projectConfig)
specifier|private
name|void
name|initLabels
parameter_list|(
name|ProjectConfig
name|projectConfig
parameter_list|)
block|{
name|projectConfig
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|codeReviewLabel
operator|.
name|getName
argument_list|()
argument_list|,
name|codeReviewLabel
argument_list|)
expr_stmt|;
name|additionalLabelType
operator|.
name|forEach
argument_list|(
name|t
lambda|->
name|projectConfig
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|t
operator|.
name|getName
argument_list|()
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|initSequences (Repository git, BatchRefUpdate bru)
specifier|private
name|void
name|initSequences
parameter_list|(
name|Repository
name|git
parameter_list|,
name|BatchRefUpdate
name|bru
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|git
operator|.
name|exactRef
argument_list|(
name|REFS_SEQUENCES
operator|+
name|Sequences
operator|.
name|NAME_CHANGES
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// Can't easily reuse the inserter from MetaDataUpdate, but this shouldn't slow down site
comment|// initialization unduly.
try|try
init|(
name|ObjectInserter
name|ins
init|=
name|git
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|bru
operator|.
name|addCommand
argument_list|(
name|RepoSequence
operator|.
name|storeNew
argument_list|(
name|ins
argument_list|,
name|Sequences
operator|.
name|NAME_CHANGES
argument_list|,
name|firstChangeId
argument_list|)
argument_list|)
expr_stmt|;
name|ins
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|execute (Repository git, BatchRefUpdate bru)
specifier|private
name|void
name|execute
parameter_list|(
name|Repository
name|git
parameter_list|,
name|BatchRefUpdate
name|bru
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|bru
operator|.
name|execute
argument_list|(
name|rw
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ReceiveCommand
name|cmd
range|:
name|bru
operator|.
name|getCommands
argument_list|()
control|)
block|{
if|if
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
operator|!=
name|ReceiveCommand
operator|.
name|Result
operator|.
name|OK
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to initialize "
operator|+
name|allProjectsName
operator|+
literal|" refs:\n"
operator|+
name|bru
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

