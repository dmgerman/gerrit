begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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

begin_comment
comment|/**  * The record of an operation for which the execution time was measured.  *  *<p>Metadata to provide additional context can be included by providing a {@link Metadata}  * instance.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|PerformanceLogRecord
specifier|public
specifier|abstract
class|class
name|PerformanceLogRecord
block|{
comment|/**    * Creates a performance log record without meta data.    *    * @param operation the name of operation the is was performed    * @param durationMs the execution time in milliseconds    * @return the performance log record    */
DECL|method|create (String operation, long durationMs)
specifier|public
specifier|static
name|PerformanceLogRecord
name|create
parameter_list|(
name|String
name|operation
parameter_list|,
name|long
name|durationMs
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_PerformanceLogRecord
argument_list|(
name|operation
argument_list|,
name|durationMs
argument_list|,
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates a performance log record with meta data.    *    * @param operation the name of operation the is was performed    * @param durationMs the execution time in milliseconds    * @param metadata metadata    * @return the performance log record    */
DECL|method|create (String operation, long durationMs, Metadata metadata)
specifier|public
specifier|static
name|PerformanceLogRecord
name|create
parameter_list|(
name|String
name|operation
parameter_list|,
name|long
name|durationMs
parameter_list|,
name|Metadata
name|metadata
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_PerformanceLogRecord
argument_list|(
name|operation
argument_list|,
name|durationMs
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|metadata
argument_list|)
argument_list|)
return|;
block|}
DECL|method|operation ()
specifier|public
specifier|abstract
name|String
name|operation
parameter_list|()
function_decl|;
DECL|method|durationMs ()
specifier|public
specifier|abstract
name|long
name|durationMs
parameter_list|()
function_decl|;
DECL|method|metadata ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Metadata
argument_list|>
name|metadata
parameter_list|()
function_decl|;
DECL|method|writeTo (PerformanceLogger performanceLogger)
name|void
name|writeTo
parameter_list|(
name|PerformanceLogger
name|performanceLogger
parameter_list|)
block|{
if|if
condition|(
name|metadata
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|performanceLogger
operator|.
name|log
argument_list|(
name|operation
argument_list|()
argument_list|,
name|durationMs
argument_list|()
argument_list|,
name|metadata
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|performanceLogger
operator|.
name|log
argument_list|(
name|operation
argument_list|()
argument_list|,
name|durationMs
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

