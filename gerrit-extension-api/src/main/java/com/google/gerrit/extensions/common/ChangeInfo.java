begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|ChangeStatus
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
name|ReviewerState
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ChangeInfo
specifier|public
class|class
name|ChangeInfo
block|{
comment|// ActionJson#copy(List, ChangeInfo) must be adapted if new fields are added that are not
comment|// protected by any ListChangesOption.
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|branch
specifier|public
name|String
name|branch
decl_stmt|;
DECL|field|topic
specifier|public
name|String
name|topic
decl_stmt|;
DECL|field|assignee
specifier|public
name|AccountInfo
name|assignee
decl_stmt|;
DECL|field|hashtags
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|hashtags
decl_stmt|;
DECL|field|changeId
specifier|public
name|String
name|changeId
decl_stmt|;
DECL|field|subject
specifier|public
name|String
name|subject
decl_stmt|;
DECL|field|status
specifier|public
name|ChangeStatus
name|status
decl_stmt|;
DECL|field|created
specifier|public
name|Timestamp
name|created
decl_stmt|;
DECL|field|updated
specifier|public
name|Timestamp
name|updated
decl_stmt|;
DECL|field|submitted
specifier|public
name|Timestamp
name|submitted
decl_stmt|;
DECL|field|submitter
specifier|public
name|AccountInfo
name|submitter
decl_stmt|;
DECL|field|starred
specifier|public
name|Boolean
name|starred
decl_stmt|;
DECL|field|stars
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|stars
decl_stmt|;
DECL|field|reviewed
specifier|public
name|Boolean
name|reviewed
decl_stmt|;
DECL|field|submitType
specifier|public
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|mergeable
specifier|public
name|Boolean
name|mergeable
decl_stmt|;
DECL|field|submittable
specifier|public
name|Boolean
name|submittable
decl_stmt|;
DECL|field|insertions
specifier|public
name|Integer
name|insertions
decl_stmt|;
DECL|field|deletions
specifier|public
name|Integer
name|deletions
decl_stmt|;
DECL|field|unresolvedCommentCount
specifier|public
name|Integer
name|unresolvedCommentCount
decl_stmt|;
DECL|field|isPrivate
specifier|public
name|Boolean
name|isPrivate
decl_stmt|;
DECL|field|workInProgress
specifier|public
name|Boolean
name|workInProgress
decl_stmt|;
DECL|field|hasReviewStarted
specifier|public
name|Boolean
name|hasReviewStarted
decl_stmt|;
DECL|field|revertOf
specifier|public
name|Integer
name|revertOf
decl_stmt|;
DECL|field|_number
specifier|public
name|int
name|_number
decl_stmt|;
DECL|field|owner
specifier|public
name|AccountInfo
name|owner
decl_stmt|;
DECL|field|actions
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|actions
decl_stmt|;
DECL|field|labels
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|LabelInfo
argument_list|>
name|labels
decl_stmt|;
DECL|field|permittedLabels
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|permittedLabels
decl_stmt|;
DECL|field|removableReviewers
specifier|public
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
name|removableReviewers
decl_stmt|;
DECL|field|reviewers
specifier|public
name|Map
argument_list|<
name|ReviewerState
argument_list|,
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|reviewers
decl_stmt|;
DECL|field|pendingReviewers
specifier|public
name|Map
argument_list|<
name|ReviewerState
argument_list|,
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|pendingReviewers
decl_stmt|;
DECL|field|reviewerUpdates
specifier|public
name|Collection
argument_list|<
name|ReviewerUpdateInfo
argument_list|>
name|reviewerUpdates
decl_stmt|;
DECL|field|messages
specifier|public
name|Collection
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
decl_stmt|;
DECL|field|currentRevision
specifier|public
name|String
name|currentRevision
decl_stmt|;
DECL|field|revisions
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RevisionInfo
argument_list|>
name|revisions
decl_stmt|;
DECL|field|_moreChanges
specifier|public
name|Boolean
name|_moreChanges
decl_stmt|;
DECL|field|problems
specifier|public
name|List
argument_list|<
name|ProblemInfo
argument_list|>
name|problems
decl_stmt|;
DECL|field|plugins
specifier|public
name|List
argument_list|<
name|PluginDefinedInfo
argument_list|>
name|plugins
decl_stmt|;
DECL|field|trackingIds
specifier|public
name|Collection
argument_list|<
name|TrackingIdInfo
argument_list|>
name|trackingIds
decl_stmt|;
block|}
end_class

end_unit

