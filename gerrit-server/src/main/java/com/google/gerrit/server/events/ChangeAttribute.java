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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|String
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
DECL|field|sortKey
specifier|public
name|String
name|sortKey
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
block|}
end_class

end_unit

