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
name|JestClientFactory
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
name|config
operator|.
name|HttpClientConfig
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
name|config
operator|.
name|HttpClientConfig
operator|.
name|Builder
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|JestClientBuilder
class|class
name|JestClientBuilder
block|{
DECL|field|cfg
specifier|private
specifier|final
name|ElasticConfiguration
name|cfg
decl_stmt|;
annotation|@
name|Inject
DECL|method|JestClientBuilder (ElasticConfiguration cfg)
name|JestClientBuilder
parameter_list|(
name|ElasticConfiguration
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
DECL|method|build ()
name|JestHttpClient
name|build
parameter_list|()
block|{
name|JestClientFactory
name|factory
init|=
operator|new
name|JestClientFactory
argument_list|()
decl_stmt|;
name|Builder
name|builder
init|=
operator|new
name|HttpClientConfig
operator|.
name|Builder
argument_list|(
name|cfg
operator|.
name|urls
argument_list|)
operator|.
name|multiThreaded
argument_list|(
literal|true
argument_list|)
operator|.
name|discoveryEnabled
argument_list|(
literal|false
argument_list|)
operator|.
name|connTimeout
argument_list|(
operator|(
name|int
operator|)
name|cfg
operator|.
name|connectionTimeout
argument_list|)
operator|.
name|maxConnectionIdleTime
argument_list|(
name|cfg
operator|.
name|maxConnectionIdleTime
argument_list|,
name|cfg
operator|.
name|maxConnectionIdleUnit
argument_list|)
operator|.
name|maxTotalConnection
argument_list|(
name|cfg
operator|.
name|maxTotalConnection
argument_list|)
operator|.
name|readTimeout
argument_list|(
name|cfg
operator|.
name|readTimeout
argument_list|)
operator|.
name|requestCompressionEnabled
argument_list|(
name|cfg
operator|.
name|requestCompression
argument_list|)
operator|.
name|discoveryFrequency
argument_list|(
literal|1L
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfg
operator|.
name|username
operator|!=
literal|null
operator|&&
name|cfg
operator|.
name|password
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|defaultCredentials
argument_list|(
name|cfg
operator|.
name|username
argument_list|,
name|cfg
operator|.
name|password
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|setHttpClientConfig
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|JestHttpClient
operator|)
name|factory
operator|.
name|getObject
argument_list|()
return|;
block|}
block|}
end_class

end_unit

