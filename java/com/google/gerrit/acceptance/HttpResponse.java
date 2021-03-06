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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|collect
operator|.
name|ImmutableList
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

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Header
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
name|util
operator|.
name|IO
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
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_class
DECL|class|HttpResponse
specifier|public
class|class
name|HttpResponse
block|{
DECL|field|response
specifier|protected
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
name|response
decl_stmt|;
DECL|field|reader
specifier|protected
name|Reader
name|reader
decl_stmt|;
DECL|method|HttpResponse (org.apache.http.HttpResponse response)
name|HttpResponse
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|http
operator|.
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
argument_list|,
name|UTF_8
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
block|{}
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
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|getHeader
argument_list|(
literal|"X-FYI-Content-Type"
argument_list|)
return|;
block|}
DECL|method|getHeader (String name)
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Header
name|hdr
init|=
name|response
operator|.
name|getFirstHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|hdr
operator|!=
literal|null
condition|?
name|hdr
operator|.
name|getValue
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|getHeaders (String name)
specifier|public
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|getHeaders
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Header
operator|::
name|getValue
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|hasContent ()
specifier|public
name|boolean
name|hasContent
parameter_list|()
block|{
name|requireNonNull
argument_list|(
name|response
argument_list|,
literal|"Response is not initialized."
argument_list|)
expr_stmt|;
return|return
name|response
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
return|;
block|}
DECL|method|getEntityContent ()
specifier|public
name|String
name|getEntityContent
parameter_list|()
throws|throws
name|IOException
block|{
name|requireNonNull
argument_list|(
name|response
argument_list|,
literal|"Response is not initialized."
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|,
literal|"Response.Entity is not initialized."
argument_list|)
expr_stmt|;
name|ByteBuffer
name|buf
init|=
name|IO
operator|.
name|readWholeStream
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|,
literal|1024
argument_list|)
decl_stmt|;
return|return
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|buf
operator|.
name|array
argument_list|()
argument_list|,
name|buf
operator|.
name|arrayOffset
argument_list|()
argument_list|,
name|buf
operator|.
name|limit
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
end_class

end_unit

