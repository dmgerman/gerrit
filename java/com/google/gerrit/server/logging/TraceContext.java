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
DECL|field|tagName
specifier|private
specifier|final
name|String
name|tagName
decl_stmt|;
DECL|field|tagValue
specifier|private
specifier|final
name|String
name|tagValue
decl_stmt|;
DECL|field|removeOnClose
specifier|private
specifier|final
name|boolean
name|removeOnClose
decl_stmt|;
DECL|method|TraceContext (RequestId.Type requestType, Object tagValue)
specifier|public
name|TraceContext
parameter_list|(
name|RequestId
operator|.
name|Type
name|requestType
parameter_list|,
name|Object
name|tagValue
parameter_list|)
block|{
name|this
argument_list|(
name|checkNotNull
argument_list|(
name|requestType
argument_list|,
literal|"request type is required"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|,
name|tagValue
argument_list|)
expr_stmt|;
block|}
DECL|method|TraceContext (String tagName, Object tagValue)
specifier|public
name|TraceContext
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Object
name|tagValue
parameter_list|)
block|{
name|this
operator|.
name|tagName
operator|=
name|checkNotNull
argument_list|(
name|tagName
argument_list|,
literal|"tag name is required"
argument_list|)
expr_stmt|;
name|this
operator|.
name|tagValue
operator|=
name|checkNotNull
argument_list|(
name|tagValue
argument_list|,
literal|"tag value is required"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|removeOnClose
operator|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|addTag
argument_list|(
name|this
operator|.
name|tagName
argument_list|,
name|this
operator|.
name|tagValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|removeOnClose
condition|)
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|removeTag
argument_list|(
name|tagName
argument_list|,
name|tagValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

