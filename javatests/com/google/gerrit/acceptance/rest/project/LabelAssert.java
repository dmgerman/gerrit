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
name|server
operator|.
name|config
operator|.
name|AllProjectsNameProvider
import|;
end_import

begin_class
DECL|class|LabelAssert
specifier|public
class|class
name|LabelAssert
block|{
DECL|method|assertCodeReviewLabel (LabelDefinitionInfo codeReviewLabel)
specifier|public
specifier|static
name|void
name|assertCodeReviewLabel
parameter_list|(
name|LabelDefinitionInfo
name|codeReviewLabel
parameter_list|)
block|{
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Code-Review"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|projectName
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|AllProjectsNameProvider
operator|.
name|DEFAULT
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|function
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|LabelFunction
operator|.
name|MAX_WITH_BLOCK
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|values
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"+2"
argument_list|,
literal|"Looks good to me, approved"
argument_list|,
literal|"+1"
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|,
literal|" 0"
argument_list|,
literal|"No score"
argument_list|,
literal|"-1"
argument_list|,
literal|"I would prefer this is not merged as is"
argument_list|,
literal|"-2"
argument_list|,
literal|"This shall not be merged"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|defaultValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|branches
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|canOverride
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|copyAnyScore
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|copyMinScore
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|copyMaxScore
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|copyAllScoresIfNoChange
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|copyAllScoresIfNoCodeChange
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|copyAllScoresOnTrivialRebase
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|copyAllScoresOnMergeFirstParentUpdate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|allowPostSubmit
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|codeReviewLabel
operator|.
name|ignoreSelfApproval
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
DECL|method|LabelAssert ()
specifier|private
name|LabelAssert
parameter_list|()
block|{}
block|}
end_class

end_unit

