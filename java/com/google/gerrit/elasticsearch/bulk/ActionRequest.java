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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|ElasticQueryAdapter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonObject
import|;
end_import

begin_class
DECL|class|ActionRequest
specifier|abstract
class|class
name|ActionRequest
extends|extends
name|BulkRequest
block|{
DECL|field|action
specifier|private
specifier|final
name|String
name|action
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|String
name|index
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|String
name|type
decl_stmt|;
DECL|field|adapter
specifier|private
specifier|final
name|ElasticQueryAdapter
name|adapter
decl_stmt|;
DECL|method|ActionRequest ( String action, String id, String index, String type, ElasticQueryAdapter adapter)
specifier|protected
name|ActionRequest
parameter_list|(
name|String
name|action
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|index
parameter_list|,
name|String
name|type
parameter_list|,
name|ElasticQueryAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRequest ()
specifier|protected
name|String
name|getRequest
parameter_list|()
block|{
name|JsonObject
name|properties
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|properties
operator|.
name|addProperty
argument_list|(
literal|"_id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addProperty
argument_list|(
literal|"_index"
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|setType
argument_list|(
name|properties
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|JsonObject
name|jsonAction
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|jsonAction
operator|.
name|add
argument_list|(
name|action
argument_list|,
name|properties
argument_list|)
expr_stmt|;
return|return
name|jsonAction
operator|.
name|toString
argument_list|()
operator|+
name|System
operator|.
name|lineSeparator
argument_list|()
return|;
block|}
block|}
end_class

end_unit

