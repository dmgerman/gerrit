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
DECL|package|com.google.gerrit.proto
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|proto
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|ByteString
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
name|MessageLite
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
name|Parser
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

begin_comment
comment|/** Static utilities for dealing with protobuf-based objects. */
end_comment

begin_class
DECL|class|Protos
specifier|public
class|class
name|Protos
block|{
comment|/**    * Serializes a proto to a byte array.    *    *<p>Guarantees deterministic serialization. No matter whether the use case cares about    * determinism or not, always use this method in preference to {@link MessageLite#toByteArray()},    * which is not guaranteed deterministic.    *    * @param message the proto message to serialize.    * @return a byte array with the message contents.    */
DECL|method|toByteArray (MessageLite message)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|MessageLite
name|message
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|message
operator|.
name|getSerializedSize
argument_list|()
index|]
decl_stmt|;
name|CodedOutputStream
name|cout
init|=
name|CodedOutputStream
operator|.
name|newInstance
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|cout
operator|.
name|useDeterministicSerialization
argument_list|()
expr_stmt|;
try|try
block|{
name|message
operator|.
name|writeTo
argument_list|(
name|cout
argument_list|)
expr_stmt|;
name|cout
operator|.
name|checkNoSpaceLeft
argument_list|()
expr_stmt|;
return|return
name|bytes
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
name|IllegalStateException
argument_list|(
literal|"exception writing to byte array"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Serializes a proto to a {@code ByteString}.    *    *<p>Guarantees deterministic serialization. No matter whether the use case cares about    * determinism or not, always use this method in preference to {@link MessageLite#toByteString()},    * which is not guaranteed deterministic.    *    * @param message the proto message to serialize    * @return a {@code ByteString} with the message contents    */
DECL|method|toByteString (MessageLite message)
specifier|public
specifier|static
name|ByteString
name|toByteString
parameter_list|(
name|MessageLite
name|message
parameter_list|)
block|{
try|try
init|(
name|ByteString
operator|.
name|Output
name|bout
init|=
name|ByteString
operator|.
name|newOutput
argument_list|(
name|message
operator|.
name|getSerializedSize
argument_list|()
argument_list|)
init|)
block|{
name|CodedOutputStream
name|outputStream
init|=
name|CodedOutputStream
operator|.
name|newInstance
argument_list|(
name|bout
argument_list|)
decl_stmt|;
name|outputStream
operator|.
name|useDeterministicSerialization
argument_list|()
expr_stmt|;
name|message
operator|.
name|writeTo
argument_list|(
name|outputStream
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|bout
operator|.
name|toByteString
argument_list|()
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
name|IllegalStateException
argument_list|(
literal|"exception writing to ByteString"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Parses a byte array to a protobuf message.    *    * @param parser parser for the proto type.    * @param in byte array with the message contents.    * @return parsed proto.    */
DECL|method|parseUnchecked (Parser<M> parser, byte[] in)
specifier|public
specifier|static
parameter_list|<
name|M
extends|extends
name|MessageLite
parameter_list|>
name|M
name|parseUnchecked
parameter_list|(
name|Parser
argument_list|<
name|M
argument_list|>
name|parser
parameter_list|,
name|byte
index|[]
name|in
parameter_list|)
block|{
try|try
block|{
return|return
name|parser
operator|.
name|parseFrom
argument_list|(
name|in
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
name|IllegalArgumentException
argument_list|(
literal|"exception parsing byte array to proto"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Parses a specific segment of a byte array to a protobuf message.    *    * @param parser parser for the proto type    * @param in byte array with the message contents    * @param offset offset in the byte array to start reading from    * @param length amount of read bytes    * @return parsed proto    */
DECL|method|parseUnchecked ( Parser<M> parser, byte[] in, int offset, int length)
specifier|public
specifier|static
parameter_list|<
name|M
extends|extends
name|MessageLite
parameter_list|>
name|M
name|parseUnchecked
parameter_list|(
name|Parser
argument_list|<
name|M
argument_list|>
name|parser
parameter_list|,
name|byte
index|[]
name|in
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
try|try
block|{
return|return
name|parser
operator|.
name|parseFrom
argument_list|(
name|in
argument_list|,
name|offset
argument_list|,
name|length
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
name|IllegalArgumentException
argument_list|(
literal|"exception parsing byte array to proto"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Parses a {@code ByteString} to a protobuf message.    *    * @param parser parser for the proto type    * @param byteString {@code ByteString} with the message contents    * @return parsed proto    */
DECL|method|parseUnchecked (Parser<M> parser, ByteString byteString)
specifier|public
specifier|static
parameter_list|<
name|M
extends|extends
name|MessageLite
parameter_list|>
name|M
name|parseUnchecked
parameter_list|(
name|Parser
argument_list|<
name|M
argument_list|>
name|parser
parameter_list|,
name|ByteString
name|byteString
parameter_list|)
block|{
try|try
block|{
return|return
name|parser
operator|.
name|parseFrom
argument_list|(
name|byteString
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
name|IllegalArgumentException
argument_list|(
literal|"exception parsing ByteString to proto"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|Protos ()
specifier|private
name|Protos
parameter_list|()
block|{}
block|}
end_class

end_unit

