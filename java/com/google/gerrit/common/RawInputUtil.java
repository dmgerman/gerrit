begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|RawInput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_class
DECL|class|RawInputUtil
specifier|public
class|class
name|RawInputUtil
block|{
DECL|method|create (String content)
specifier|public
specifier|static
name|RawInput
name|create
parameter_list|(
name|String
name|content
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|content
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create (byte[] bytes, String contentType)
specifier|public
specifier|static
name|RawInput
name|create
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|contentType
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|bytes
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
return|return
operator|new
name|RawInput
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|bytes
operator|.
name|length
return|;
block|}
block|}
return|;
block|}
DECL|method|create (byte[] bytes)
specifier|public
specifier|static
name|RawInput
name|create
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|bytes
argument_list|,
literal|"application/octet-stream"
argument_list|)
return|;
block|}
DECL|method|create (HttpServletRequest req)
specifier|public
specifier|static
name|RawInput
name|create
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
operator|new
name|RawInput
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|req
operator|.
name|getContentType
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|req
operator|.
name|getContentLength
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|req
operator|.
name|getInputStream
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

