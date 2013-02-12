begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|HttpResponse
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
name|DefaultHttpClient
import|;
end_import

begin_class
DECL|class|RestSession
specifier|public
class|class
name|RestSession
block|{
DECL|field|account
specifier|private
specifier|final
name|TestAccount
name|account
decl_stmt|;
DECL|field|client
name|DefaultHttpClient
name|client
decl_stmt|;
DECL|method|RestSession (TestAccount account)
specifier|public
name|RestSession
parameter_list|(
name|TestAccount
name|account
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
block|}
DECL|method|get (String endPoint)
specifier|public
name|Reader
name|get
parameter_list|(
name|String
name|endPoint
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
literal|"http://localhost:8080/a"
operator|+
name|endPoint
argument_list|)
decl_stmt|;
name|HttpResponse
name|response
init|=
name|getClient
argument_list|()
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|Reader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
name|reader
operator|.
name|skip
argument_list|(
literal|4
argument_list|)
expr_stmt|;
return|return
name|reader
return|;
block|}
DECL|method|post (String endPoint)
specifier|public
name|Reader
name|post
parameter_list|(
name|String
name|endPoint
parameter_list|)
block|{
comment|// TODO
return|return
literal|null
return|;
block|}
DECL|method|getClient ()
specifier|private
name|DefaultHttpClient
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
name|client
operator|=
operator|new
name|DefaultHttpClient
argument_list|()
expr_stmt|;
name|client
operator|.
name|getCredentialsProvider
argument_list|()
operator|.
name|setCredentials
argument_list|(
operator|new
name|AuthScope
argument_list|(
literal|"localhost"
argument_list|,
literal|8080
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
block|}
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

