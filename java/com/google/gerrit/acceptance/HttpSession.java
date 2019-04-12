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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|fluent
operator|.
name|Executor
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
name|fluent
operator|.
name|Request
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

begin_class
DECL|class|HttpSession
specifier|public
class|class
name|HttpSession
block|{
DECL|field|account
specifier|protected
name|TestAccount
name|account
decl_stmt|;
DECL|field|url
specifier|protected
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
DECL|method|HttpSession (GerritServer server, @Nullable TestAccount account)
specifier|public
name|HttpSession
parameter_list|(
name|GerritServer
name|server
parameter_list|,
annotation|@
name|Nullable
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
name|HttpClient
name|noRedirectClient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|disableRedirectHandling
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|this
operator|.
name|executor
operator|=
name|Executor
operator|.
name|newInstance
argument_list|(
name|noRedirectClient
argument_list|)
expr_stmt|;
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
if|if
condition|(
name|account
operator|!=
literal|null
condition|)
block|{
name|executor
operator|.
name|auth
argument_list|(
operator|new
name|HttpHost
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
name|account
operator|.
name|username
argument_list|()
argument_list|,
name|account
operator|.
name|httpPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|url ()
specifier|public
name|String
name|url
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|execute (Request request)
specifier|public
name|RestResponse
name|execute
parameter_list|(
name|Request
name|request
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|RestResponse
argument_list|(
name|executor
operator|.
name|execute
argument_list|(
name|request
argument_list|)
operator|.
name|returnResponse
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

