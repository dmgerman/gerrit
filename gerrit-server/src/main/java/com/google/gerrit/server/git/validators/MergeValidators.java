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
DECL|package|com.google.gerrit.server.git.validators
package|package
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
name|Lists
import|;
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
name|registration
operator|.
name|DynamicSet
import|;
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
name|git
operator|.
name|CodeReviewCommit
import|;
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
name|CommitMergeStatus
import|;
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
name|lib
operator|.
name|Repository
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
DECL|class|MergeValidators
specifier|public
class|class
name|MergeValidators
block|{
DECL|field|mergeValidationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|MergeValidationListener
argument_list|>
name|mergeValidationListeners
decl_stmt|;
DECL|field|projectConfigValidatorFactory
specifier|private
specifier|final
name|ProjectConfigValidator
operator|.
name|Factory
name|projectConfigValidatorFactory
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|MergeValidators
name|create
parameter_list|()
function_decl|;
block|}
annotation|@
name|Inject
DECL|method|MergeValidators (DynamicSet<MergeValidationListener> mergeValidationListeners, ProjectConfigValidator.Factory projectConfigValidatorFactory)
name|MergeValidators
parameter_list|(
name|DynamicSet
argument_list|<
name|MergeValidationListener
argument_list|>
name|mergeValidationListeners
parameter_list|,
name|ProjectConfigValidator
operator|.
name|Factory
name|projectConfigValidatorFactory
parameter_list|)
block|{
name|this
operator|.
name|mergeValidationListeners
operator|=
name|mergeValidationListeners
expr_stmt|;
name|this
operator|.
name|projectConfigValidatorFactory
operator|=
name|projectConfigValidatorFactory
expr_stmt|;
block|}
DECL|method|validatePreMerge (Repository repo, CodeReviewCommit commit, ProjectState destProject, Branch.NameKey destBranch, PatchSet.Id patchSetId)
specifier|public
name|void
name|validatePreMerge
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|CodeReviewCommit
name|commit
parameter_list|,
name|ProjectState
name|destProject
parameter_list|,
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
throws|throws
name|MergeValidationException
block|{
name|List
argument_list|<
name|MergeValidationListener
argument_list|>
name|validators
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
name|validators
operator|.
name|add
argument_list|(
operator|new
name|PluginMergeValidationListener
argument_list|(
name|mergeValidationListeners
argument_list|)
argument_list|)
expr_stmt|;
name|validators
operator|.
name|add
argument_list|(
name|projectConfigValidatorFactory
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|MergeValidationListener
name|validator
range|:
name|validators
control|)
block|{
name|validator
operator|.
name|onPreMerge
argument_list|(
name|repo
argument_list|,
name|commit
argument_list|,
name|destProject
argument_list|,
name|destBranch
argument_list|,
name|patchSetId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ProjectConfigValidator
specifier|public
specifier|static
class|class
name|ProjectConfigValidator
implements|implements
name|MergeValidationListener
block|{
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|ProjectConfigValidator
name|create
parameter_list|()
function_decl|;
block|}
annotation|@
name|Inject
DECL|method|ProjectConfigValidator (AllProjectsName allProjectsName, ReviewDb db, ProjectCache projectCache, IdentifiedUser.GenericFactory iuf, ApprovalsUtil approvalsUtil)
specifier|public
name|ProjectConfigValidator
parameter_list|(
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|ReviewDb
name|db
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|iuf
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|)
block|{
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|iuf
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onPreMerge (final Repository repo, final CodeReviewCommit commit, final ProjectState destProject, final Branch.NameKey destBranch, final PatchSet.Id patchSetId)
specifier|public
name|void
name|onPreMerge
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|CodeReviewCommit
name|commit
parameter_list|,
specifier|final
name|ProjectState
name|destProject
parameter_list|,
specifier|final
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
throws|throws
name|MergeValidationException
block|{
if|if
condition|(
name|RefNames
operator|.
name|REFS_CONFIG
operator|.
name|equals
argument_list|(
name|destBranch
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|newParent
decl_stmt|;
try|try
block|{
name|ProjectConfig
name|cfg
init|=
operator|new
name|ProjectConfig
argument_list|(
name|destProject
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|load
argument_list|(
name|repo
argument_list|,
name|commit
argument_list|)
expr_stmt|;
name|newParent
operator|=
name|cfg
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|(
name|allProjectsName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MergeValidationException
argument_list|(
name|CommitMergeStatus
operator|.
name|INVALID_PROJECT_CONFIGURATION
argument_list|)
throw|;
block|}
specifier|final
name|Project
operator|.
name|NameKey
name|oldParent
init|=
name|destProject
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|(
name|allProjectsName
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldParent
operator|==
literal|null
condition|)
block|{
comment|// update of the 'All-Projects' project
if|if
condition|(
name|newParent
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|MergeValidationException
argument_list|(
name|CommitMergeStatus
operator|.
name|INVALID_PROJECT_CONFIGURATION_ROOT_PROJECT_CANNOT_HAVE_PARENT
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|oldParent
operator|.
name|equals
argument_list|(
name|newParent
argument_list|)
condition|)
block|{
name|PatchSetApproval
name|psa
init|=
name|approvalsUtil
operator|.
name|getSubmitter
argument_list|(
name|db
argument_list|,
name|patchSetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|psa
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MergeValidationException
argument_list|(
name|CommitMergeStatus
operator|.
name|SETTING_PARENT_PROJECT_ONLY_ALLOWED_BY_ADMIN
argument_list|)
throw|;
block|}
specifier|final
name|IdentifiedUser
name|submitter
init|=
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|submitter
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MergeValidationException
argument_list|(
name|CommitMergeStatus
operator|.
name|SETTING_PARENT_PROJECT_ONLY_ALLOWED_BY_ADMIN
argument_list|)
throw|;
block|}
if|if
condition|(
name|projectCache
operator|.
name|get
argument_list|(
name|newParent
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MergeValidationException
argument_list|(
name|CommitMergeStatus
operator|.
name|INVALID_PROJECT_CONFIGURATION_PARENT_PROJECT_NOT_FOUND
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
block|}
comment|/** Execute merge validation plug-ins */
DECL|class|PluginMergeValidationListener
specifier|public
specifier|static
class|class
name|PluginMergeValidationListener
implements|implements
name|MergeValidationListener
block|{
DECL|field|mergeValidationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|MergeValidationListener
argument_list|>
name|mergeValidationListeners
decl_stmt|;
DECL|method|PluginMergeValidationListener ( DynamicSet<MergeValidationListener> mergeValidationListeners)
specifier|public
name|PluginMergeValidationListener
parameter_list|(
name|DynamicSet
argument_list|<
name|MergeValidationListener
argument_list|>
name|mergeValidationListeners
parameter_list|)
block|{
name|this
operator|.
name|mergeValidationListeners
operator|=
name|mergeValidationListeners
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onPreMerge (Repository repo, CodeReviewCommit commit, ProjectState destProject, Branch.NameKey destBranch, PatchSet.Id patchSetId)
specifier|public
name|void
name|onPreMerge
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|CodeReviewCommit
name|commit
parameter_list|,
name|ProjectState
name|destProject
parameter_list|,
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
throws|throws
name|MergeValidationException
block|{
for|for
control|(
name|MergeValidationListener
name|validator
range|:
name|mergeValidationListeners
control|)
block|{
name|validator
operator|.
name|onPreMerge
argument_list|(
name|repo
argument_list|,
name|commit
argument_list|,
name|destProject
argument_list|,
name|destBranch
argument_list|,
name|patchSetId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

