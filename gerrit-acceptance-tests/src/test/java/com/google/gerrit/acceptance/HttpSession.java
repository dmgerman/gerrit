begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

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
name|CharMatcher
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
name|HttpClient
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
name|client
operator|.
name|HttpClientBuilder
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
name|net
operator|.
name|URI
import|;
end_import

begin_class
DECL|class|HttpSession
specifier|public
class|class
name|HttpSession
block|{
DECL|field|url
specifier|protected
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|account
specifier|private
specifier|final
name|TestAccount
name|account
decl_stmt|;
DECL|field|client
specifier|private
name|HttpClient
name|client
decl_stmt|;
DECL|method|HttpSession (GerritServer server, TestAccount account)
specifier|public
name|HttpSession
parameter_list|(
name|GerritServer
name|server
parameter_list|,
name|TestAccount
name|account
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'/'
argument_list|)
operator|.
name|trimTrailingFrom
argument_list|(
name|server
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
block|}
DECL|method|get (String path)
specifier|public
name|HttpResponse
name|get
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
name|url
operator|+
name|path
argument_list|)
decl_stmt|;
return|return
operator|new
name|HttpResponse
argument_list|(
name|getClient
argument_list|()
operator|.
name|execute
argument_list|(
name|get
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getClient ()
specifier|protected
name|HttpClient
name|getClient
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|BasicCredentialsProvider
name|creds
init|=
operator|new
name|BasicCredentialsProvider
argument_list|()
decl_stmt|;
name|creds
operator|.
name|setCredentials
argument_list|(
operator|new
name|AuthScope
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|,
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|account
operator|.
name|username
argument_list|,
name|account
operator|.
name|httpPassword
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|setDefaultCredentialsProvider
argument_list|(
name|creds
argument_list|)
operator|.
name|setMaxConnPerRoute
argument_list|(
literal|512
argument_list|)
operator|.
name|setMaxConnTotal
argument_list|(
literal|1024
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

