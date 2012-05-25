begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|plugins
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_class
DECL|class|SmallResource
specifier|final
class|class
name|SmallResource
extends|extends
name|Resource
block|{
DECL|field|data
specifier|private
specifier|final
name|byte
index|[]
name|data
decl_stmt|;
DECL|field|contentType
specifier|private
name|String
name|contentType
decl_stmt|;
DECL|field|characterEncoding
specifier|private
name|String
name|characterEncoding
decl_stmt|;
DECL|field|lastModified
specifier|private
name|long
name|lastModified
decl_stmt|;
DECL|method|SmallResource (byte[] data)
name|SmallResource
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
DECL|method|setLastModified (long when)
name|SmallResource
name|setLastModified
parameter_list|(
name|long
name|when
parameter_list|)
block|{
name|this
operator|.
name|lastModified
operator|=
name|when
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setContentType (String contentType)
name|SmallResource
name|setContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|contentType
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setCharacterEncoding (@ullable String enc)
name|SmallResource
name|setCharacterEncoding
parameter_list|(
annotation|@
name|Nullable
name|String
name|enc
parameter_list|)
block|{
name|this
operator|.
name|characterEncoding
operator|=
name|enc
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|weigh ()
name|int
name|weigh
parameter_list|()
block|{
return|return
name|contentType
operator|.
name|length
argument_list|()
operator|*
literal|2
operator|+
name|data
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|send (HttpServletRequest req, HttpServletResponse res)
name|void
name|send
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
literal|0
operator|<
name|lastModified
condition|)
block|{
name|res
operator|.
name|setDateHeader
argument_list|(
literal|"Last-Modified"
argument_list|,
name|lastModified
argument_list|)
expr_stmt|;
block|}
name|res
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
if|if
condition|(
name|characterEncoding
operator|!=
literal|null
condition|)
block|{
name|res
operator|.
name|setCharacterEncoding
argument_list|(
name|characterEncoding
argument_list|)
expr_stmt|;
block|}
name|res
operator|.
name|setContentLength
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|res
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

