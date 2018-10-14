begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
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
name|extensions
operator|.
name|client
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
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|SubmitType
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

begin_class
DECL|class|ConfigInput
specifier|public
class|class
name|ConfigInput
block|{
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|useContributorAgreements
specifier|public
name|InheritableBoolean
name|useContributorAgreements
decl_stmt|;
DECL|field|useContentMerge
specifier|public
name|InheritableBoolean
name|useContentMerge
decl_stmt|;
DECL|field|useSignedOffBy
specifier|public
name|InheritableBoolean
name|useSignedOffBy
decl_stmt|;
DECL|field|createNewChangeForAllNotInTarget
specifier|public
name|InheritableBoolean
name|createNewChangeForAllNotInTarget
decl_stmt|;
DECL|field|requireChangeId
specifier|public
name|InheritableBoolean
name|requireChangeId
decl_stmt|;
DECL|field|enableSignedPush
specifier|public
name|InheritableBoolean
name|enableSignedPush
decl_stmt|;
DECL|field|requireSignedPush
specifier|public
name|InheritableBoolean
name|requireSignedPush
decl_stmt|;
DECL|field|rejectImplicitMerges
specifier|public
name|InheritableBoolean
name|rejectImplicitMerges
decl_stmt|;
DECL|field|privateByDefault
specifier|public
name|InheritableBoolean
name|privateByDefault
decl_stmt|;
DECL|field|workInProgressByDefault
specifier|public
name|InheritableBoolean
name|workInProgressByDefault
decl_stmt|;
DECL|field|enableReviewerByEmail
specifier|public
name|InheritableBoolean
name|enableReviewerByEmail
decl_stmt|;
DECL|field|matchAuthorToCommitterDate
specifier|public
name|InheritableBoolean
name|matchAuthorToCommitterDate
decl_stmt|;
DECL|field|rejectEmptyCommit
specifier|public
name|InheritableBoolean
name|rejectEmptyCommit
decl_stmt|;
DECL|field|maxObjectSizeLimit
specifier|public
name|String
name|maxObjectSizeLimit
decl_stmt|;
DECL|field|submitType
specifier|public
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|state
specifier|public
name|ProjectState
name|state
decl_stmt|;
DECL|field|pluginConfigValues
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
argument_list|>
name|pluginConfigValues
decl_stmt|;
DECL|field|commentLinks
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|CommentLinkInput
argument_list|>
name|commentLinks
decl_stmt|;
block|}
end_class

end_unit

