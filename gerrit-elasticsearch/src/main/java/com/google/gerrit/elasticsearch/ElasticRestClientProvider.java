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
name|gerrit
operator|.
name|extensions
operator|.
name|events
operator|.
name|LifecycleListener
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|Provider
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
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpHost
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
name|StatusLine
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
name|auth
operator|.
name|AuthScope
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
name|auth
operator|.
name|UsernamePasswordCredentials
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
name|CredentialsProvider
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
name|impl
operator|.
name|client
operator|.
name|BasicCredentialsProvider
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
name|impl
operator|.
name|nio
operator|.
name|client
operator|.
name|HttpAsyncClientBuilder
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

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|RestClient
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
name|RestClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ElasticRestClientProvider
class|class
name|ElasticRestClientProvider
implements|implements
name|Provider
argument_list|<
name|RestClient
argument_list|>
implements|,
name|LifecycleListener
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ElasticRestClientProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|hosts
specifier|private
specifier|final
name|HttpHost
index|[]
name|hosts
decl_stmt|;
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
specifier|final
name|String
name|password
decl_stmt|;
DECL|field|client
specifier|private
name|RestClient
name|client
decl_stmt|;
annotation|@
name|Inject
DECL|method|ElasticRestClientProvider (ElasticConfiguration cfg)
name|ElasticRestClientProvider
parameter_list|(
name|ElasticConfiguration
name|cfg
parameter_list|)
block|{
name|hosts
operator|=
name|cfg
operator|.
name|urls
operator|.
name|toArray
argument_list|(
operator|new
name|HttpHost
index|[
name|cfg
operator|.
name|urls
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
name|username
operator|=
name|cfg
operator|.
name|username
expr_stmt|;
name|password
operator|=
name|cfg
operator|.
name|password
expr_stmt|;
block|}
DECL|method|module ()
specifier|public
specifier|static
name|LifecycleModule
name|module
parameter_list|()
block|{
return|return
operator|new
name|LifecycleModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|ElasticRestClientProvider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|RestClient
name|get
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|build
argument_list|()
expr_stmt|;
name|ElasticVersion
name|version
init|=
name|getVersion
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Elasticsearch integration version {}"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|client
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore. We can't do anything about it.
block|}
block|}
block|}
DECL|class|FailedToGetVersion
specifier|public
specifier|static
class|class
name|FailedToGetVersion
extends|extends
name|ElasticException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"Failed to get Elasticsearch version"
decl_stmt|;
DECL|method|FailedToGetVersion (StatusLine status)
name|FailedToGetVersion
parameter_list|(
name|StatusLine
name|status
parameter_list|)
block|{
name|super
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s: %d %s"
argument_list|,
name|MESSAGE
argument_list|,
name|status
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|status
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|FailedToGetVersion (Throwable cause)
name|FailedToGetVersion
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getVersion ()
specifier|private
name|ElasticVersion
name|getVersion
parameter_list|()
throws|throws
name|ElasticException
block|{
try|try
block|{
name|Response
name|response
init|=
name|client
operator|.
name|performRequest
argument_list|(
literal|"GET"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|StatusLine
name|statusLine
init|=
name|response
operator|.
name|getStatusLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|statusLine
operator|.
name|getStatusCode
argument_list|()
operator|!=
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
throw|throw
operator|new
name|FailedToGetVersion
argument_list|(
name|statusLine
argument_list|)
throw|;
block|}
name|String
name|version
init|=
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
name|get
argument_list|(
literal|"version"
argument_list|)
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
literal|"number"
argument_list|)
operator|.
name|getAsString
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Connected to Elasticsearch version {}"
argument_list|,
name|version
argument_list|)
expr_stmt|;
return|return
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
name|version
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToGetVersion
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build ()
specifier|private
name|RestClient
name|build
parameter_list|()
block|{
name|RestClientBuilder
name|builder
init|=
name|RestClient
operator|.
name|builder
argument_list|(
name|hosts
argument_list|)
decl_stmt|;
name|setConfiguredCredentialsIfAny
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|setConfiguredCredentialsIfAny (RestClientBuilder builder)
specifier|private
name|void
name|setConfiguredCredentialsIfAny
parameter_list|(
name|RestClientBuilder
name|builder
parameter_list|)
block|{
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|CredentialsProvider
name|credentialsProvider
init|=
operator|new
name|BasicCredentialsProvider
argument_list|()
decl_stmt|;
name|credentialsProvider
operator|.
name|setCredentials
argument_list|(
name|AuthScope
operator|.
name|ANY
argument_list|,
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setHttpClientConfigCallback
argument_list|(
parameter_list|(
name|HttpAsyncClientBuilder
name|httpClientBuilder
parameter_list|)
lambda|->
name|httpClientBuilder
operator|.
name|setDefaultCredentialsProvider
argument_list|(
name|credentialsProvider
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

