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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|OutputStream
import|;
end_import

begin_class
DECL|class|LimitedByteArrayOutputStream
class|class
name|LimitedByteArrayOutputStream
extends|extends
name|OutputStream
block|{
DECL|field|maxSize
specifier|private
specifier|final
name|int
name|maxSize
decl_stmt|;
DECL|field|buffer
specifier|private
specifier|final
name|ByteArrayOutputStream
name|buffer
decl_stmt|;
comment|/**    * Constructs a LimitedByteArrayOutputStream, which stores output in memory up to a certain    * specified size. When the output exceeds the specified size a LimitExceededException is thrown.    *    * @param max the maximum size in bytes which may be stored.    * @param initial the initial size. It must be smaller than the max size.    */
DECL|method|LimitedByteArrayOutputStream (int max, int initial)
name|LimitedByteArrayOutputStream
parameter_list|(
name|int
name|max
parameter_list|,
name|int
name|initial
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|initial
operator|<=
name|max
argument_list|)
expr_stmt|;
name|maxSize
operator|=
name|max
expr_stmt|;
name|buffer
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|initial
argument_list|)
expr_stmt|;
block|}
DECL|method|checkOversize (int additionalSize)
specifier|private
name|void
name|checkOversize
parameter_list|(
name|int
name|additionalSize
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|buffer
operator|.
name|size
argument_list|()
operator|+
name|additionalSize
operator|>
name|maxSize
condition|)
block|{
throw|throw
operator|new
name|LimitExceededException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|write (int b)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|checkOversize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (byte[] b, int off, int len)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|checkOversize
argument_list|(
name|len
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
comment|/** @return a newly allocated byte array with contents of the buffer. */
DECL|method|toByteArray ()
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
return|return
name|buffer
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|class|LimitExceededException
specifier|static
class|class
name|LimitExceededException
extends|extends
name|IOException
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
block|}
block|}
end_class

end_unit

