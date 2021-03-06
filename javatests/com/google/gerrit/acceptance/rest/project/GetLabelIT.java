begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|NoHttpd
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
name|acceptance
operator|.
name|testsuite
operator|.
name|request
operator|.
name|RequestScopeOperations
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
name|LabelFunction
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
name|extensions
operator|.
name|common
operator|.
name|LabelDefinitionInfo
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
name|ResourceNotFoundException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|GetLabelIT
specifier|public
class|class
name|GetLabelIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|requestScopeOperations
annotation|@
name|Inject
specifier|private
name|RequestScopeOperations
name|requestScopeOperations
decl_stmt|;
annotation|@
name|Test
DECL|method|anonymous ()
specifier|public
name|void
name|anonymous
parameter_list|()
throws|throws
name|Exception
block|{
name|requestScopeOperations
operator|.
name|setApiUserAnonymous
argument_list|()
expr_stmt|;
name|AuthException
name|thrown
init|=
name|assertThrows
argument_list|(
name|AuthException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"Code-Review"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Authentication required"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|notAllowed ()
specifier|public
name|void
name|notAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|AuthException
name|thrown
init|=
name|assertThrows
argument_list|(
name|AuthException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"Code-Review"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"read refs/meta/config not permitted"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|notFound ()
specifier|public
name|void
name|notFound
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceNotFoundException
name|thrown
init|=
name|assertThrows
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"Foo-Review"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Not found: Foo-Review"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|allProjectsCodeReviewLabel ()
specifier|public
name|void
name|allProjectsCodeReviewLabel
parameter_list|()
throws|throws
name|Exception
block|{
name|LabelDefinitionInfo
name|codeReviewLabel
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"Code-Review"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|LabelAssert
operator|.
name|assertCodeReviewLabel
argument_list|(
name|codeReviewLabel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|labelWithDefaultValue ()
specifier|public
name|void
name|labelWithDefaultValue
parameter_list|()
throws|throws
name|Exception
block|{
name|configLabel
argument_list|(
literal|"foo"
argument_list|,
name|LabelFunction
operator|.
name|NO_OP
argument_list|)
expr_stmt|;
comment|// set default value
try|try
init|(
name|ProjectConfigUpdate
name|u
init|=
name|updateProject
argument_list|(
name|project
argument_list|)
init|)
block|{
name|LabelType
name|labelType
init|=
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|labelType
operator|.
name|setDefaultValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|labelType
operator|.
name|getName
argument_list|()
argument_list|,
name|labelType
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
name|LabelDefinitionInfo
name|fooLabel
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|defaultValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|labelLimitedToBranches ()
specifier|public
name|void
name|labelLimitedToBranches
parameter_list|()
throws|throws
name|Exception
block|{
name|configLabel
argument_list|(
literal|"foo"
argument_list|,
name|LabelFunction
operator|.
name|NO_OP
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"refs/heads/master"
argument_list|,
literal|"^refs/heads/stable-.*"
argument_list|)
argument_list|)
expr_stmt|;
name|LabelDefinitionInfo
name|fooLabel
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|branches
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"refs/heads/master"
argument_list|,
literal|"^refs/heads/stable-.*"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|labelWithoutRules ()
specifier|public
name|void
name|labelWithoutRules
parameter_list|()
throws|throws
name|Exception
block|{
name|configLabel
argument_list|(
literal|"foo"
argument_list|,
name|LabelFunction
operator|.
name|NO_OP
argument_list|)
expr_stmt|;
comment|// unset rules which are enabled by default
try|try
init|(
name|ProjectConfigUpdate
name|u
init|=
name|updateProject
argument_list|(
name|project
argument_list|)
init|)
block|{
name|LabelType
name|labelType
init|=
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|labelType
operator|.
name|setCanOverride
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setCopyAllScoresIfNoChange
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setAllowPostSubmit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|labelType
operator|.
name|getName
argument_list|()
argument_list|,
name|labelType
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
name|LabelDefinitionInfo
name|fooLabel
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|canOverride
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAnyScore
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyMinScore
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyMaxScore
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresIfNoChange
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresIfNoCodeChange
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresOnTrivialRebase
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresOnMergeFirstParentUpdate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|allowPostSubmit
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|ignoreSelfApproval
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|labelWithAllRules ()
specifier|public
name|void
name|labelWithAllRules
parameter_list|()
throws|throws
name|Exception
block|{
name|configLabel
argument_list|(
literal|"foo"
argument_list|,
name|LabelFunction
operator|.
name|NO_OP
argument_list|)
expr_stmt|;
comment|// set rules which are not enabled by default
try|try
init|(
name|ProjectConfigUpdate
name|u
init|=
name|updateProject
argument_list|(
name|project
argument_list|)
init|)
block|{
name|LabelType
name|labelType
init|=
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|labelType
operator|.
name|setCopyAnyScore
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setCopyMinScore
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setCopyMaxScore
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setCopyAllScoresIfNoCodeChange
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setCopyAllScoresOnTrivialRebase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setCopyAllScoresOnMergeFirstParentUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelType
operator|.
name|setIgnoreSelfApproval
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|labelType
operator|.
name|getName
argument_list|()
argument_list|,
name|labelType
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
name|LabelDefinitionInfo
name|fooLabel
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|label
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|canOverride
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAnyScore
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyMinScore
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyMaxScore
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresIfNoChange
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresIfNoCodeChange
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresOnTrivialRebase
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|copyAllScoresOnMergeFirstParentUpdate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|allowPostSubmit
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|fooLabel
operator|.
name|ignoreSelfApproval
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

