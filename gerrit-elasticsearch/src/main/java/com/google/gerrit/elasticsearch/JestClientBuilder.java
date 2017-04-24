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
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
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
name|http
operator|.
name|JestHttpClient
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|JestClientBuilder
class|class
name|JestClientBuilder
block|{
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|refresh
specifier|final
name|boolean
name|refresh
decl_stmt|;
annotation|@
name|Inject
DECL|method|JestClientBuilder (@erritServerConfig Config cfg)
name|JestClientBuilder
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|String
name|protocol
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"protocol"
argument_list|)
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|String
name|hostname
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"hostname"
argument_list|)
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|String
name|port
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|cfg
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"port"
argument_list|,
literal|9200
argument_list|)
argument_list|)
decl_stmt|;
comment|// By default Elasticsearch has a 1s delay before changes are available in
comment|// the index.  Setting refresh(true) on calls to the index makes the index
comment|// refresh immediately.
comment|//
comment|// Discovery should be disabled during test mode to prevent spurious
comment|// connection failures caused by the client starting up and being ready
comment|// before the test node.
comment|//
comment|// This setting should only be set to true during testing, and is not
comment|// documented.
name|this
operator|.
name|refresh
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"elasticsearch"
argument_list|,
literal|"test"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|buildUrl
argument_list|(
name|protocol
argument_list|,
name|hostname
argument_list|,
name|port
argument_list|)
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
name|factory
operator|.
name|setHttpClientConfig
argument_list|(
operator|new
name|HttpClientConfig
operator|.
name|Builder
argument_list|(
name|url
argument_list|)
operator|.
name|multiThreaded
argument_list|(
literal|true
argument_list|)
operator|.
name|discoveryEnabled
argument_list|(
operator|!
name|refresh
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
DECL|method|buildUrl (String protocol, String hostname, String port)
specifier|private
name|String
name|buildUrl
parameter_list|(
name|String
name|protocol
parameter_list|,
name|String
name|hostname
parameter_list|,
name|String
name|port
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|protocol
argument_list|,
name|hostname
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
argument_list|,
literal|""
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
decl||
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot build url to Elasticsearch from values: protocol="
operator|+
name|protocol
operator|+
literal|" hostname="
operator|+
name|hostname
operator|+
literal|" port="
operator|+
name|port
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

