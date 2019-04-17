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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Converter
import|;
end_import

begin_comment
comment|/**  * Interface for serializing/deserializing a type to/from a persistent cache.  *  *<p>Implementations are null-hostile and will throw exceptions from {@link #serialize} when passed  * null values, unless otherwise specified.  */
end_comment

begin_interface
DECL|interface|CacheSerializer
specifier|public
interface|interface
name|CacheSerializer
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Convert a serializer of one type to another type using a {@link Converter}.    *    * @param delegate underlying serializer.    * @param converter converter between an arbitrary type {@code T} and {@code delegate}'s type.    * @return serializer of type {@code T}.    */
DECL|method|convert (CacheSerializer<D> delegate, Converter<T, D> converter)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|D
parameter_list|>
name|CacheSerializer
argument_list|<
name|T
argument_list|>
name|convert
parameter_list|(
name|CacheSerializer
argument_list|<
name|D
argument_list|>
name|delegate
parameter_list|,
name|Converter
argument_list|<
name|T
argument_list|,
name|D
argument_list|>
name|converter
parameter_list|)
block|{
return|return
operator|new
name|CacheSerializer
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
name|T
name|object
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|serialize
argument_list|(
name|converter
operator|.
name|convert
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|deserialize
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
return|return
name|converter
operator|.
name|reverse
argument_list|()
operator|.
name|convert
argument_list|(
name|delegate
operator|.
name|deserialize
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Serializes the object to a new byte array.    *    * @param object object to serialize.    * @return serialized byte array representation.    * @throws RuntimeException for malformed input, for example null or an otherwise unsupported    *     value.    */
DECL|method|serialize (T object)
name|byte
index|[]
name|serialize
parameter_list|(
name|T
name|object
parameter_list|)
function_decl|;
comment|/**    * Deserializes a single object from the given byte array.    *    * @param in serialized byte array representation.    * @throws RuntimeException for malformed input, for example null or an otherwise corrupt    *     serialized representation.    */
DECL|method|deserialize (byte[] in)
name|T
name|deserialize
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

