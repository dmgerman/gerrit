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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ConfigInfo
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
name|projects
operator|.
name|ConfigInfo
operator|.
name|InheritedBooleanInfo
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
name|projects
operator|.
name|ConfigInput
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_comment
comment|/** Provides transformations to get and set BooleanProjectConfigs from the API. */
end_comment

begin_class
DECL|class|BooleanProjectConfigTransformations
specifier|public
class|class
name|BooleanProjectConfigTransformations
block|{
DECL|field|MAPPER
specifier|private
specifier|static
name|ImmutableMap
argument_list|<
name|BooleanProjectConfig
argument_list|,
name|Mapper
argument_list|>
name|MAPPER
init|=
name|ImmutableMap
operator|.
expr|<
name|BooleanProjectConfig
decl_stmt|,
name|Mapper
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_CONTRIBUTOR_AGREEMENTS
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|useContributorAgreements
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|useContributorAgreements
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_SIGNED_OFF_BY
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|useSignedOffBy
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|useSignedOffBy
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_CONTENT_MERGE
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|useContentMerge
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|useContentMerge
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|REQUIRE_CHANGE_ID
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|requireChangeId
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|requireChangeId
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|CREATE_NEW_CHANGE_FOR_ALL_NOT_IN_TARGET
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|createNewChangeForAllNotInTarget
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|createNewChangeForAllNotInTarget
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|ENABLE_SIGNED_PUSH
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|enableSignedPush
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|enableSignedPush
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|REQUIRE_SIGNED_PUSH
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|requireSignedPush
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|requireSignedPush
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|REJECT_IMPLICIT_MERGES
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|rejectImplicitMerges
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|rejectImplicitMerges
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|PRIVATE_BY_DEFAULT
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|privateByDefault
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|privateByDefault
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|ENABLE_REVIEWER_BY_EMAIL
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|enableReviewerByEmail
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|enableReviewerByEmail
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|MATCH_AUTHOR_TO_COMMITTER_DATE
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|matchAuthorToCommitterDate
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|matchAuthorToCommitterDate
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|REJECT_EMPTY_COMMIT
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|rejectEmptyCommit
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|rejectEmptyCommit
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|BooleanProjectConfig
operator|.
name|WORK_IN_PROGRESS_BY_DEFAULT
argument_list|,
operator|new
name|Mapper
argument_list|(
name|i
lambda|->
name|i
operator|.
name|workInProgressByDefault
argument_list|,
parameter_list|(
name|i
parameter_list|,
name|v
parameter_list|)
lambda|->
name|i
operator|.
name|workInProgressByDefault
operator|=
name|v
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
static|static
block|{
comment|// Verify that each BooleanProjectConfig has to/from API mappers in
comment|// BooleanProjectConfigTransformations
if|if
condition|(
operator|!
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|MAPPER
operator|.
name|keySet
argument_list|()
argument_list|,
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|BooleanProjectConfig
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"All values of BooleanProjectConfig must have transformations associated with them"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ToApi
specifier|private
interface|interface
name|ToApi
block|{
DECL|method|apply (ConfigInfo info, InheritedBooleanInfo val)
name|void
name|apply
parameter_list|(
name|ConfigInfo
name|info
parameter_list|,
name|InheritedBooleanInfo
name|val
parameter_list|)
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|FromApi
specifier|private
interface|interface
name|FromApi
block|{
DECL|method|apply (ConfigInput input)
name|InheritableBoolean
name|apply
parameter_list|(
name|ConfigInput
name|input
parameter_list|)
function_decl|;
block|}
DECL|method|set (BooleanProjectConfig cfg, ConfigInfo info, InheritedBooleanInfo val)
specifier|public
specifier|static
name|void
name|set
parameter_list|(
name|BooleanProjectConfig
name|cfg
parameter_list|,
name|ConfigInfo
name|info
parameter_list|,
name|InheritedBooleanInfo
name|val
parameter_list|)
block|{
name|MAPPER
operator|.
name|get
argument_list|(
name|cfg
argument_list|)
operator|.
name|set
argument_list|(
name|info
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
DECL|method|get (BooleanProjectConfig cfg, ConfigInput input)
specifier|public
specifier|static
name|InheritableBoolean
name|get
parameter_list|(
name|BooleanProjectConfig
name|cfg
parameter_list|,
name|ConfigInput
name|input
parameter_list|)
block|{
return|return
name|MAPPER
operator|.
name|get
argument_list|(
name|cfg
argument_list|)
operator|.
name|get
argument_list|(
name|input
argument_list|)
return|;
block|}
DECL|class|Mapper
specifier|private
specifier|static
class|class
name|Mapper
block|{
DECL|field|fromApi
specifier|private
specifier|final
name|FromApi
name|fromApi
decl_stmt|;
DECL|field|toApi
specifier|private
specifier|final
name|ToApi
name|toApi
decl_stmt|;
DECL|method|Mapper (FromApi fromApi, ToApi toApi)
specifier|private
name|Mapper
parameter_list|(
name|FromApi
name|fromApi
parameter_list|,
name|ToApi
name|toApi
parameter_list|)
block|{
name|this
operator|.
name|fromApi
operator|=
name|fromApi
expr_stmt|;
name|this
operator|.
name|toApi
operator|=
name|toApi
expr_stmt|;
block|}
DECL|method|set (ConfigInfo info, InheritedBooleanInfo val)
specifier|public
name|void
name|set
parameter_list|(
name|ConfigInfo
name|info
parameter_list|,
name|InheritedBooleanInfo
name|val
parameter_list|)
block|{
name|toApi
operator|.
name|apply
argument_list|(
name|info
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
DECL|method|get (ConfigInput input)
specifier|public
name|InheritableBoolean
name|get
parameter_list|(
name|ConfigInput
name|input
parameter_list|)
block|{
return|return
name|fromApi
operator|.
name|apply
argument_list|(
name|input
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

