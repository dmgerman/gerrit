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
name|java
operator|.
name|util
operator|.
name|HashMap
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
comment|/** Class for registering event types */
end_comment

begin_class
DECL|class|EventTypes
specifier|public
class|class
name|EventTypes
block|{
DECL|field|typesByString
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|typesByString
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|register
argument_list|(
name|AssigneeChangedEvent
operator|.
name|TYPE
argument_list|,
name|AssigneeChangedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ChangeAbandonedEvent
operator|.
name|TYPE
argument_list|,
name|ChangeAbandonedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ChangeMergedEvent
operator|.
name|TYPE
argument_list|,
name|ChangeMergedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ChangeRestoredEvent
operator|.
name|TYPE
argument_list|,
name|ChangeRestoredEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|CommentAddedEvent
operator|.
name|TYPE
argument_list|,
name|CommentAddedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|CommitReceivedEvent
operator|.
name|TYPE
argument_list|,
name|CommitReceivedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|HashtagsChangedEvent
operator|.
name|TYPE
argument_list|,
name|HashtagsChangedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|RefUpdatedEvent
operator|.
name|TYPE
argument_list|,
name|RefUpdatedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|RefReceivedEvent
operator|.
name|TYPE
argument_list|,
name|RefReceivedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ReviewerAddedEvent
operator|.
name|TYPE
argument_list|,
name|ReviewerAddedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ReviewerDeletedEvent
operator|.
name|TYPE
argument_list|,
name|ReviewerDeletedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|PatchSetCreatedEvent
operator|.
name|TYPE
argument_list|,
name|PatchSetCreatedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|TopicChangedEvent
operator|.
name|TYPE
argument_list|,
name|TopicChangedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ProjectCreatedEvent
operator|.
name|TYPE
argument_list|,
name|ProjectCreatedEvent
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**    * Register an event type and associated class.    *    * @param eventType The event type to register.    * @param eventClass The event class to register.    */
DECL|method|register (String eventType, Class<? extends Event> eventClass)
specifier|public
specifier|static
name|void
name|register
parameter_list|(
name|String
name|eventType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Event
argument_list|>
name|eventClass
parameter_list|)
block|{
name|typesByString
operator|.
name|put
argument_list|(
name|eventType
argument_list|,
name|eventClass
argument_list|)
expr_stmt|;
block|}
comment|/**    * Get the class for an event type.    *    * @param type The type.    * @return The event class, or null if no class is registered with the given type    */
DECL|method|getClass (String type)
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getClass
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|typesByString
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

