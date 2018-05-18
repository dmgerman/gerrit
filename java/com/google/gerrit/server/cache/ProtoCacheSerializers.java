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
DECL|package|com.google.gerrit.server.cache
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|OBJECT_ID_LENGTH
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|protobuf
operator|.
name|ProtobufCodec
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_comment
comment|/** Static utilities for writing protobuf-based {@link CacheSerializer} implementations. */
end_comment

begin_class
DECL|class|ProtoCacheSerializers
specifier|public
class|class
name|ProtoCacheSerializers
block|{
comment|/**    * Serializes a proto to a byte array.    *    *<p>Guarantees deterministic serialization and thus is suitable for use in persistent caches.    * Should be used in preference to {@link MessageLite#toByteArray()}, which is not guaranteed    * deterministic.    *    * @param message the proto message to serialize.    * @return a byte array with the message contents.    */
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
comment|/**    * Serializes an object to a {@link ByteString} using a protobuf codec.    *    *<p>Guarantees deterministic serialization and thus is suitable for use in persistent caches.    * Should be used in preference to {@link ProtobufCodec#encodeToByteString(Object)}, which is not    * guaranteed deterministic.    *    * @param object the object to serialize.    * @param codec codec for serializing.    * @return a {@code ByteString} with the message contents.    */
DECL|method|toByteString (T object, ProtobufCodec<T> codec)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ByteString
name|toByteString
parameter_list|(
name|T
name|object
parameter_list|,
name|ProtobufCodec
argument_list|<
name|T
argument_list|>
name|codec
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
argument_list|()
init|)
block|{
name|CodedOutputStream
name|cout
init|=
name|CodedOutputStream
operator|.
name|newInstance
argument_list|(
name|bout
argument_list|)
decl_stmt|;
name|cout
operator|.
name|useDeterministicSerialization
argument_list|()
expr_stmt|;
name|codec
operator|.
name|encode
argument_list|(
name|object
argument_list|,
name|cout
argument_list|)
expr_stmt|;
name|cout
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
comment|/**    * Helper for serializing {@link ObjectId} instances to/from protobuf fields.    *    *<p>Reuse a single instance's {@link #toByteString(ObjectId)} and {@link    * #fromByteString(ByteString)} within a single {@link CacheSerializer#serialize} or {@link    * CacheSerializer#deserialize} method body to minimize allocation of temporary buffers.    *    *<p><strong>Note:</strong> This class is not threadsafe. Instances must not be stored in {@link    * CacheSerializer} fields if the serializer instances will be used from multiple threads.    */
DECL|class|ObjectIdConverter
specifier|public
specifier|static
class|class
name|ObjectIdConverter
block|{
DECL|method|create ()
specifier|public
specifier|static
name|ObjectIdConverter
name|create
parameter_list|()
block|{
return|return
operator|new
name|ObjectIdConverter
argument_list|()
return|;
block|}
DECL|field|buf
specifier|private
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|OBJECT_ID_LENGTH
index|]
decl_stmt|;
DECL|method|ObjectIdConverter ()
specifier|private
name|ObjectIdConverter
parameter_list|()
block|{}
DECL|method|toByteString (ObjectId id)
specifier|public
name|ByteString
name|toByteString
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|id
operator|.
name|copyRawTo
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|ByteString
operator|.
name|copyFrom
argument_list|(
name|buf
argument_list|)
return|;
block|}
DECL|method|fromByteString (ByteString in)
specifier|public
name|ObjectId
name|fromByteString
parameter_list|(
name|ByteString
name|in
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|in
operator|.
name|size
argument_list|()
operator|==
name|OBJECT_ID_LENGTH
argument_list|,
literal|"expected ByteString of length %s: %s"
argument_list|,
name|OBJECT_ID_LENGTH
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|copyTo
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|ObjectId
operator|.
name|fromRaw
argument_list|(
name|buf
argument_list|)
return|;
block|}
block|}
DECL|method|ProtoCacheSerializers ()
specifier|private
name|ProtoCacheSerializers
parameter_list|()
block|{}
block|}
end_class

end_unit

