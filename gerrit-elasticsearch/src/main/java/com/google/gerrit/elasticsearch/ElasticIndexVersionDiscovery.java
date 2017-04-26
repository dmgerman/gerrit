begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.elasticsearch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonElement
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|client
operator|.
name|JestResult
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|client
operator|.
name|http
operator|.
name|JestHttpClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|indices
operator|.
name|aliases
operator|.
name|GetAliases
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ElasticIndexVersionDiscovery
class|class
name|ElasticIndexVersionDiscovery
block|{
DECL|field|client
specifier|private
specifier|final
name|JestHttpClient
name|client
decl_stmt|;
annotation|@
name|Inject
DECL|method|ElasticIndexVersionDiscovery (JestClientBuilder clientBuilder)
name|ElasticIndexVersionDiscovery
parameter_list|(
name|JestClientBuilder
name|clientBuilder
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|clientBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|discover (String prefix, String indexName)
name|List
argument_list|<
name|String
argument_list|>
name|discover
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|indexName
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|prefix
operator|+
name|indexName
operator|+
literal|"_"
decl_stmt|;
name|JestResult
name|result
init|=
name|client
operator|.
name|execute
argument_list|(
operator|new
name|GetAliases
operator|.
name|Builder
argument_list|()
operator|.
name|addIndex
argument_list|(
name|name
operator|+
literal|"*"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isSucceeded
argument_list|()
condition|)
block|{
name|JsonObject
name|object
init|=
name|result
operator|.
name|getJsonObject
argument_list|()
operator|.
name|getAsJsonObject
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|object
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|JsonElement
argument_list|>
name|entry
range|:
name|object
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|versions
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|replace
argument_list|(
name|name
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|versions
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

