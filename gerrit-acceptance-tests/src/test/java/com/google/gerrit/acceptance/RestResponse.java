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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
operator|.
name|RestApiServlet
operator|.
name|JSON_MAGIC
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

begin_class
DECL|class|RestResponse
specifier|public
class|class
name|RestResponse
block|{
DECL|field|response
specifier|private
name|HttpResponse
name|response
decl_stmt|;
DECL|field|reader
specifier|private
name|Reader
name|reader
decl_stmt|;
DECL|method|RestResponse (HttpResponse response)
name|RestResponse
parameter_list|(
name|HttpResponse
name|response
parameter_list|)
block|{
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
block|}
DECL|method|getReader ()
specifier|public
name|Reader
name|getReader
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|IOException
block|{
if|if
condition|(
name|reader
operator|==
literal|null
operator|&&
name|response
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reader
operator|=
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
expr_stmt|;
name|reader
operator|.
name|skip
argument_list|(
name|JSON_MAGIC
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
return|return
name|reader
return|;
block|}
DECL|method|consume ()
specifier|public
name|void
name|consume
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|IOException
block|{
name|Reader
name|reader
init|=
name|getReader
argument_list|()
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|reader
operator|.
name|read
argument_list|()
operator|!=
operator|-
literal|1
condition|)
empty_stmt|;
block|}
block|}
DECL|method|getStatusCode ()
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

