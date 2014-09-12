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
name|Map
import|;
end_import

begin_class
DECL|class|ChangeInfo
specifier|public
class|class
name|ChangeInfo
block|{
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
DECL|field|starred
specifier|public
name|Boolean
name|starred
decl_stmt|;
DECL|field|reviewed
specifier|public
name|Boolean
name|reviewed
decl_stmt|;
DECL|field|mergeable
specifier|public
name|Boolean
name|mergeable
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
DECL|field|owner
specifier|public
name|AccountInfo
name|owner
decl_stmt|;
DECL|field|currentRevision
specifier|public
name|String
name|currentRevision
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
DECL|field|messages
specifier|public
name|Collection
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
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
DECL|field|baseChange
specifier|public
name|String
name|baseChange
decl_stmt|;
DECL|field|_number
specifier|public
name|int
name|_number
decl_stmt|;
block|}
end_class

end_unit

