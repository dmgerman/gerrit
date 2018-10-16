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
DECL|package|com.google.gerrit.server.cache.serialize
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|serialize
package|;
end_package

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
name|protobuf
operator|.
name|CodedInputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|CodedOutputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|TextFormat
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_enum
DECL|enum|IntegerCacheSerializer
specifier|public
enum|enum
name|IntegerCacheSerializer
implements|implements
name|CacheSerializer
argument_list|<
name|Integer
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
comment|// Same as com.google.protobuf.WireFormat#MAX_VARINT_SIZE. Note that negative values take up more
comment|// than MAX_VARINT32_SIZE space.
DECL|field|MAX_VARINT_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|MAX_VARINT_SIZE
init|=
literal|10
decl_stmt|;
annotation|@
name|Override
DECL|method|serialize (Integer object)
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
name|Integer
name|object
parameter_list|)
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|MAX_VARINT_SIZE
index|]
decl_stmt|;
name|CodedOutputStream
name|cout
init|=
name|CodedOutputStream
operator|.
name|newInstance
argument_list|(
name|buf
argument_list|)
decl_stmt|;
try|try
block|{
name|cout
operator|.
name|writeInt32NoTag
argument_list|(
name|requireNonNull
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
name|cout
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Failed to serialize int"
argument_list|)
throw|;
block|}
name|int
name|n
init|=
name|cout
operator|.
name|getTotalBytesWritten
argument_list|()
decl_stmt|;
return|return
name|n
operator|==
name|buf
operator|.
name|length
condition|?
name|buf
else|:
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|deserialize (byte[] in)
specifier|public
name|Integer
name|deserialize
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
name|CodedInputStream
name|cin
init|=
name|CodedInputStream
operator|.
name|newInstance
argument_list|(
name|requireNonNull
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|ret
decl_stmt|;
try|try
block|{
name|ret
operator|=
name|cin
operator|.
name|readRawVarint32
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to deserialize int"
argument_list|)
throw|;
block|}
name|int
name|n
init|=
name|cin
operator|.
name|getTotalBytesRead
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|!=
name|in
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Extra bytes in int representation: "
operator|+
name|TextFormat
operator|.
name|escapeBytes
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|in
argument_list|,
name|n
argument_list|,
name|in
operator|.
name|length
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_enum

end_unit

