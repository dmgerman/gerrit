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
DECL|package|com.google.gerrit.server.notedb.rebuild
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|rebuild
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|ChangeMessage
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
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
name|notedb
operator|.
name|ChangeUpdate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
DECL|class|StatusChangeEvent
class|class
name|StatusChangeEvent
extends|extends
name|Event
block|{
DECL|field|PATTERNS
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|Change
operator|.
name|Status
argument_list|,
name|Pattern
argument_list|>
name|PATTERNS
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^Abandoned(\n.*)*$"
argument_list|)
argument_list|,
name|Change
operator|.
name|Status
operator|.
name|MERGED
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^Change has been successfully"
operator|+
literal|" (merged|cherry-picked|rebased|pushed).*$"
argument_list|)
argument_list|,
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^Restored(\n.*)*$"
argument_list|)
argument_list|)
decl_stmt|;
DECL|method|parseFromMessage (ChangeMessage message, Change change, Change noteDbChange)
specifier|static
name|Optional
argument_list|<
name|StatusChangeEvent
argument_list|>
name|parseFromMessage
parameter_list|(
name|ChangeMessage
name|message
parameter_list|,
name|Change
name|change
parameter_list|,
name|Change
name|noteDbChange
parameter_list|)
block|{
name|String
name|msg
init|=
name|message
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Change
operator|.
name|Status
argument_list|,
name|Pattern
argument_list|>
name|e
range|:
name|PATTERNS
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|matcher
argument_list|(
name|msg
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|StatusChangeEvent
argument_list|(
name|message
argument_list|,
name|change
argument_list|,
name|noteDbChange
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|noteDbChange
specifier|private
specifier|final
name|Change
name|noteDbChange
decl_stmt|;
DECL|field|status
specifier|private
specifier|final
name|Change
operator|.
name|Status
name|status
decl_stmt|;
DECL|method|StatusChangeEvent (ChangeMessage message, Change change, Change noteDbChange, Change.Status status)
specifier|private
name|StatusChangeEvent
parameter_list|(
name|ChangeMessage
name|message
parameter_list|,
name|Change
name|change
parameter_list|,
name|Change
name|noteDbChange
parameter_list|,
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
name|this
argument_list|(
name|message
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|message
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|message
operator|.
name|getWrittenOn
argument_list|()
argument_list|,
name|change
argument_list|,
name|noteDbChange
argument_list|,
name|message
operator|.
name|getTag
argument_list|()
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
DECL|method|StatusChangeEvent (PatchSet.Id psId, Account.Id author, Timestamp when, Change change, Change noteDbChange, String tag, Change.Status status)
specifier|private
name|StatusChangeEvent
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|,
name|Timestamp
name|when
parameter_list|,
name|Change
name|change
parameter_list|,
name|Change
name|noteDbChange
parameter_list|,
name|String
name|tag
parameter_list|,
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
name|super
argument_list|(
name|psId
argument_list|,
name|author
argument_list|,
name|author
argument_list|,
name|when
argument_list|,
name|change
operator|.
name|getCreatedOn
argument_list|()
argument_list|,
name|tag
argument_list|)
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|noteDbChange
operator|=
name|noteDbChange
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|uniquePerUpdate ()
name|boolean
name|uniquePerUpdate
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|apply (ChangeUpdate update)
name|void
name|apply
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|)
throws|throws
name|OrmException
block|{
name|checkUpdate
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|update
operator|.
name|fixStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|noteDbChange
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|status
operator|==
name|Change
operator|.
name|Status
operator|.
name|MERGED
condition|)
block|{
name|update
operator|.
name|setSubmissionId
argument_list|(
name|change
operator|.
name|getSubmissionId
argument_list|()
argument_list|)
expr_stmt|;
name|noteDbChange
operator|.
name|setSubmissionId
argument_list|(
name|change
operator|.
name|getSubmissionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

