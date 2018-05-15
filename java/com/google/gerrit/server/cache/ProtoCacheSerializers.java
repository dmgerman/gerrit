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
DECL|method|ProtoCacheSerializers ()
specifier|private
name|ProtoCacheSerializers
parameter_list|()
block|{}
block|}
end_class

end_unit

