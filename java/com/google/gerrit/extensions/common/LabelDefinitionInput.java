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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

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

begin_class
DECL|class|LabelDefinitionInput
specifier|public
class|class
name|LabelDefinitionInput
extends|extends
name|InputWithCommitMessage
block|{
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|function
specifier|public
name|String
name|function
decl_stmt|;
DECL|field|values
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
decl_stmt|;
DECL|field|defaultValue
specifier|public
name|Short
name|defaultValue
decl_stmt|;
DECL|field|branches
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|field|canOverride
specifier|public
name|Boolean
name|canOverride
decl_stmt|;
DECL|field|copyAnyScore
specifier|public
name|Boolean
name|copyAnyScore
decl_stmt|;
DECL|field|copyMinScore
specifier|public
name|Boolean
name|copyMinScore
decl_stmt|;
DECL|field|copyMaxScore
specifier|public
name|Boolean
name|copyMaxScore
decl_stmt|;
DECL|field|copyAllScoresIfNoChange
specifier|public
name|Boolean
name|copyAllScoresIfNoChange
decl_stmt|;
DECL|field|copyAllScoresIfNoCodeChange
specifier|public
name|Boolean
name|copyAllScoresIfNoCodeChange
decl_stmt|;
DECL|field|copyAllScoresOnTrivialRebase
specifier|public
name|Boolean
name|copyAllScoresOnTrivialRebase
decl_stmt|;
DECL|field|copyAllScoresOnMergeFirstParentUpdate
specifier|public
name|Boolean
name|copyAllScoresOnMergeFirstParentUpdate
decl_stmt|;
DECL|field|allowPostSubmit
specifier|public
name|Boolean
name|allowPostSubmit
decl_stmt|;
DECL|field|ignoreSelfApproval
specifier|public
name|Boolean
name|ignoreSelfApproval
decl_stmt|;
block|}
end_class

end_unit

