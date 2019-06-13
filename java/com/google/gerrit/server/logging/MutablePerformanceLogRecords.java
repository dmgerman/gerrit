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
name|base
operator|.
name|MoreObjects
import|;
end_import

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
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * Thread-safe store for performance log records.  *  *<p>This class is intended to keep track of performance log records in {@link LoggingContext}. It  * needs to be thread-safe because it gets shared between threads when the logging context is copied  * to another thread (see {@link LoggingContextAwareRunnable} and {@link  * LoggingContextAwareCallable}. In this case the logging contexts of both threads share the same  * instance of this class. This is important since performance log records are processed only at the  * end of a request and performance log records that are created in another thread should not get  * lost.  */
end_comment

begin_class
DECL|class|MutablePerformanceLogRecords
specifier|public
class|class
name|MutablePerformanceLogRecords
block|{
DECL|field|performanceLogRecords
specifier|private
specifier|final
name|ArrayList
argument_list|<
name|PerformanceLogRecord
argument_list|>
name|performanceLogRecords
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|add (PerformanceLogRecord record)
specifier|public
specifier|synchronized
name|void
name|add
parameter_list|(
name|PerformanceLogRecord
name|record
parameter_list|)
block|{
name|performanceLogRecords
operator|.
name|add
argument_list|(
name|record
argument_list|)
expr_stmt|;
block|}
DECL|method|set (List<PerformanceLogRecord> records)
specifier|public
specifier|synchronized
name|void
name|set
parameter_list|(
name|List
argument_list|<
name|PerformanceLogRecord
argument_list|>
name|records
parameter_list|)
block|{
name|performanceLogRecords
operator|.
name|clear
argument_list|()
expr_stmt|;
name|performanceLogRecords
operator|.
name|addAll
argument_list|(
name|records
argument_list|)
expr_stmt|;
block|}
DECL|method|list ()
specifier|public
specifier|synchronized
name|ImmutableList
argument_list|<
name|PerformanceLogRecord
argument_list|>
name|list
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|performanceLogRecords
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"performanceLogRecords"
argument_list|,
name|performanceLogRecords
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

