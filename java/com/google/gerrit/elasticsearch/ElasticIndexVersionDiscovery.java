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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|JsonParser
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
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|Response
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
name|ElasticRestClientProvider
name|client
decl_stmt|;
annotation|@
name|Inject
DECL|method|ElasticIndexVersionDiscovery (ElasticRestClientProvider client)
name|ElasticIndexVersionDiscovery
parameter_list|(
name|ElasticRestClientProvider
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
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
name|Response
name|response
init|=
name|client
operator|.
name|get
argument_list|()
operator|.
name|performRequest
argument_list|(
name|HttpGet
operator|.
name|METHOD_NAME
argument_list|,
name|name
operator|+
literal|"*/_aliases"
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
operator|==
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
return|return
operator|new
name|JsonParser
argument_list|()
operator|.
name|parse
argument_list|(
name|AbstractElasticIndex
operator|.
name|getContent
argument_list|(
name|response
argument_list|)
argument_list|)
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
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
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
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

