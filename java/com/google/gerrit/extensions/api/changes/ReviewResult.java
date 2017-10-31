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
DECL|package|com.google.gerrit.extensions.api.changes
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
name|changes
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
name|Nullable
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

begin_comment
comment|/** Result object representing the outcome of a review request. */
end_comment

begin_class
DECL|class|ReviewResult
specifier|public
class|class
name|ReviewResult
block|{
comment|/**    * Map of labels to values after the review was posted. Null if any reviewer additions were    * rejected.    */
DECL|field|labels
annotation|@
name|Nullable
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|labels
decl_stmt|;
comment|/**    * Map of account or group identifier to outcome of adding as a reviewer. Null if no reviewer    * additions were requested.    */
DECL|field|reviewers
annotation|@
name|Nullable
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|AddReviewerResult
argument_list|>
name|reviewers
decl_stmt|;
comment|/**    * Boolean indicating whether the change was moved out of WIP by this review. Either true or null.    */
DECL|field|ready
annotation|@
name|Nullable
specifier|public
name|Boolean
name|ready
decl_stmt|;
comment|/** Error message for non-200 responses. */
DECL|field|error
annotation|@
name|Nullable
specifier|public
name|String
name|error
decl_stmt|;
block|}
end_class

end_unit

