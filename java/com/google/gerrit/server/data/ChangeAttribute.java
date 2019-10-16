begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|data
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
name|entities
operator|.
name|Change
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
name|PluginDefinedInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|annotations
operator|.
name|SerializedName
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
DECL|class|ChangeAttribute
specifier|public
class|class
name|ChangeAttribute
block|{
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
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|number
specifier|public
name|int
name|number
decl_stmt|;
DECL|field|subject
specifier|public
name|String
name|subject
decl_stmt|;
DECL|field|owner
specifier|public
name|AccountAttribute
name|owner
decl_stmt|;
DECL|field|assignee
specifier|public
name|AccountAttribute
name|assignee
decl_stmt|;
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|commitMessage
specifier|public
name|String
name|commitMessage
decl_stmt|;
DECL|field|hashtags
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|hashtags
decl_stmt|;
DECL|field|createdOn
specifier|public
name|Long
name|createdOn
decl_stmt|;
DECL|field|lastUpdated
specifier|public
name|Long
name|lastUpdated
decl_stmt|;
DECL|field|open
specifier|public
name|Boolean
name|open
decl_stmt|;
DECL|field|status
specifier|public
name|Change
operator|.
name|Status
name|status
decl_stmt|;
DECL|field|comments
specifier|public
name|List
argument_list|<
name|MessageAttribute
argument_list|>
name|comments
decl_stmt|;
DECL|field|wip
specifier|public
name|Boolean
name|wip
decl_stmt|;
annotation|@
name|SerializedName
argument_list|(
literal|"private"
argument_list|)
DECL|field|isPrivate
specifier|public
name|Boolean
name|isPrivate
decl_stmt|;
DECL|field|trackingIds
specifier|public
name|List
argument_list|<
name|TrackingIdAttribute
argument_list|>
name|trackingIds
decl_stmt|;
DECL|field|currentPatchSet
specifier|public
name|PatchSetAttribute
name|currentPatchSet
decl_stmt|;
DECL|field|patchSets
specifier|public
name|List
argument_list|<
name|PatchSetAttribute
argument_list|>
name|patchSets
decl_stmt|;
DECL|field|dependsOn
specifier|public
name|List
argument_list|<
name|DependencyAttribute
argument_list|>
name|dependsOn
decl_stmt|;
DECL|field|neededBy
specifier|public
name|List
argument_list|<
name|DependencyAttribute
argument_list|>
name|neededBy
decl_stmt|;
DECL|field|submitRecords
specifier|public
name|List
argument_list|<
name|SubmitRecordAttribute
argument_list|>
name|submitRecords
decl_stmt|;
DECL|field|allReviewers
specifier|public
name|List
argument_list|<
name|AccountAttribute
argument_list|>
name|allReviewers
decl_stmt|;
DECL|field|plugins
specifier|public
name|List
argument_list|<
name|PluginDefinedInfo
argument_list|>
name|plugins
decl_stmt|;
block|}
end_class

end_unit

