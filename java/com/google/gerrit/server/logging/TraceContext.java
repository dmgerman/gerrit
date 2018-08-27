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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|HashBasedTable
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
name|Table
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
name|util
operator|.
name|RequestId
import|;
end_import

begin_class
DECL|class|TraceContext
specifier|public
class|class
name|TraceContext
implements|implements
name|AutoCloseable
block|{
DECL|field|DISABLED
specifier|public
specifier|static
specifier|final
name|TraceContext
name|DISABLED
init|=
operator|new
name|TraceContext
argument_list|()
decl_stmt|;
DECL|method|open ()
specifier|public
specifier|static
name|TraceContext
name|open
parameter_list|()
block|{
return|return
operator|new
name|TraceContext
argument_list|()
return|;
block|}
comment|// Table<TAG_NAME, TAG_VALUE, REMOVE_ON_CLOSE>
DECL|field|tags
specifier|private
specifier|final
name|Table
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|Boolean
argument_list|>
name|tags
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|stopForceLoggingOnClose
specifier|private
name|boolean
name|stopForceLoggingOnClose
decl_stmt|;
DECL|method|TraceContext ()
specifier|private
name|TraceContext
parameter_list|()
block|{}
DECL|method|addTag (RequestId.Type requestId, Object tagValue)
specifier|public
name|TraceContext
name|addTag
parameter_list|(
name|RequestId
operator|.
name|Type
name|requestId
parameter_list|,
name|Object
name|tagValue
parameter_list|)
block|{
return|return
name|addTag
argument_list|(
name|checkNotNull
argument_list|(
name|requestId
argument_list|,
literal|"request ID is required"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|,
name|tagValue
argument_list|)
return|;
block|}
DECL|method|addTag (String tagName, Object tagValue)
specifier|public
name|TraceContext
name|addTag
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Object
name|tagValue
parameter_list|)
block|{
name|String
name|name
init|=
name|checkNotNull
argument_list|(
name|tagName
argument_list|,
literal|"tag name is required"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|checkNotNull
argument_list|(
name|tagValue
argument_list|,
literal|"tag value is required"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|tags
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|addTag
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|forceLogging ()
specifier|public
name|TraceContext
name|forceLogging
parameter_list|()
block|{
if|if
condition|(
name|stopForceLoggingOnClose
condition|)
block|{
return|return
name|this
return|;
block|}
name|stopForceLoggingOnClose
operator|=
operator|!
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|forceLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
name|Table
operator|.
name|Cell
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|Boolean
argument_list|>
name|cell
range|:
name|tags
operator|.
name|cellSet
argument_list|()
control|)
block|{
if|if
condition|(
name|cell
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|removeTag
argument_list|(
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|stopForceLoggingOnClose
condition|)
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|forceLogging
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

