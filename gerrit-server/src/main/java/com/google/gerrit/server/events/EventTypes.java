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
name|registerClass
argument_list|(
operator|new
name|ChangeAbandonedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|ChangeMergedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|ChangeRestoredEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|CommentAddedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|CommitReceivedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|DraftPublishedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|HashtagsChangedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|MergeFailedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|RefUpdatedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|RefReceivedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|ReviewerAddedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|PatchSetCreatedEvent
argument_list|()
argument_list|)
expr_stmt|;
name|registerClass
argument_list|(
operator|new
name|TopicChangedEvent
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Register an event.    *    *  @param event The event to register.    *  registered.    **/
DECL|method|registerClass (Event event)
specifier|public
specifier|static
name|void
name|registerClass
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|String
name|type
init|=
name|event
operator|.
name|getType
argument_list|()
decl_stmt|;
name|typesByString
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|event
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Get the class for an event type.    *    * @param type The type.    * @return The event class, or null if no class is registered with the    * given type    **/
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

