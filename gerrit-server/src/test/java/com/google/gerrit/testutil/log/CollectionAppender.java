begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.testutil.log
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
operator|.
name|log
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
name|Lists
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|AppenderSkeleton
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|spi
operator|.
name|LoggingEvent
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
name|LinkedList
import|;
end_import

begin_comment
comment|/**  * Log4j appender that logs into a list  */
end_comment

begin_class
DECL|class|CollectionAppender
specifier|public
class|class
name|CollectionAppender
extends|extends
name|AppenderSkeleton
block|{
DECL|field|events
specifier|private
name|Collection
argument_list|<
name|LoggingEvent
argument_list|>
name|events
decl_stmt|;
DECL|method|CollectionAppender ()
specifier|public
name|CollectionAppender
parameter_list|()
block|{
name|events
operator|=
operator|new
name|LinkedList
argument_list|<
name|LoggingEvent
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|CollectionAppender (Collection<LoggingEvent> events)
specifier|public
name|CollectionAppender
parameter_list|(
name|Collection
argument_list|<
name|LoggingEvent
argument_list|>
name|events
parameter_list|)
block|{
name|this
operator|.
name|events
operator|=
name|events
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|requiresLayout ()
specifier|public
name|boolean
name|requiresLayout
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|append (LoggingEvent event)
specifier|protected
name|void
name|append
parameter_list|(
name|LoggingEvent
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|events
operator|.
name|add
argument_list|(
name|event
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not append event "
operator|+
name|event
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{   }
DECL|method|getLoggedEvents ()
specifier|public
name|Collection
argument_list|<
name|LoggingEvent
argument_list|>
name|getLoggedEvents
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newLinkedList
argument_list|(
name|events
argument_list|)
return|;
block|}
block|}
end_class

end_unit

