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
DECL|package|com.google.gerrit.elasticsearch.bulk
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|bulk
package|;
end_package

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

begin_class
DECL|class|BulkRequest
specifier|public
specifier|abstract
class|class
name|BulkRequest
block|{
DECL|field|requests
specifier|private
specifier|final
name|List
argument_list|<
name|BulkRequest
argument_list|>
name|requests
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|BulkRequest ()
specifier|protected
name|BulkRequest
parameter_list|()
block|{
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|add (BulkRequest request)
specifier|public
name|BulkRequest
name|add
parameter_list|(
name|BulkRequest
name|request
parameter_list|)
block|{
name|requests
operator|.
name|add
argument_list|(
name|request
argument_list|)
expr_stmt|;
return|return
name|this
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
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|BulkRequest
name|request
range|:
name|requests
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getRequest ()
specifier|protected
specifier|abstract
name|String
name|getRequest
parameter_list|()
function_decl|;
block|}
end_class

end_unit

