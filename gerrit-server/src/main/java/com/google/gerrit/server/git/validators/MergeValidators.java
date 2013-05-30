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
DECL|method|MergeValidators (DynamicSet<MergeValidationListener> mergeValidationListeners)
name|MergeValidators
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

