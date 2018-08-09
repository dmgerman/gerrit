begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
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
name|ImmutableSetMultimap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
import|;
end_import

begin_comment
comment|/**  * ThreadFactory that copies the logging context of the current thread to any new thread that is  * created by this ThreadFactory.  */
end_comment

begin_class
DECL|class|LoggingContextAwareThreadFactory
specifier|public
class|class
name|LoggingContextAwareThreadFactory
implements|implements
name|ThreadFactory
block|{
DECL|field|parentThreadFactory
specifier|private
specifier|final
name|ThreadFactory
name|parentThreadFactory
decl_stmt|;
DECL|method|LoggingContextAwareThreadFactory ()
specifier|public
name|LoggingContextAwareThreadFactory
parameter_list|()
block|{
name|this
operator|.
name|parentThreadFactory
operator|=
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
expr_stmt|;
block|}
DECL|method|LoggingContextAwareThreadFactory (ThreadFactory parentThreadFactory)
specifier|public
name|LoggingContextAwareThreadFactory
parameter_list|(
name|ThreadFactory
name|parentThreadFactory
parameter_list|)
block|{
name|this
operator|.
name|parentThreadFactory
operator|=
name|parentThreadFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newThread (Runnable r)
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|callingThread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tags
init|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|getTagsAsMap
argument_list|()
decl_stmt|;
name|boolean
name|forceLogging
init|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|isLoggingForced
argument_list|()
decl_stmt|;
return|return
name|parentThreadFactory
operator|.
name|newThread
argument_list|(
parameter_list|()
lambda|->
block|{
if|if
condition|(
name|callingThread
operator|.
name|equals
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
condition|)
block|{
comment|// propagation of logging context is not needed
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// propagate logging context
name|LoggingContext
name|loggingCtx
init|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|loggingCtx
operator|.
name|setTags
argument_list|(
name|tags
argument_list|)
expr_stmt|;
name|loggingCtx
operator|.
name|forceLogging
argument_list|(
name|forceLogging
argument_list|)
expr_stmt|;
try|try
block|{
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|loggingCtx
operator|.
name|clearTags
argument_list|()
expr_stmt|;
name|loggingCtx
operator|.
name|forceLogging
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

