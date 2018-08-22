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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_comment
comment|/**  * Serializer that uses default Java serialization.  *  *<p>Unlike most {@link CacheSerializer} implementations, serializing null is supported.  *  * @param<T> type to serialize. Must implement {@code Serializable}, but due to implementation  *     details this is only checked at runtime.  */
end_comment

begin_class
DECL|class|JavaCacheSerializer
specifier|public
class|class
name|JavaCacheSerializer
parameter_list|<
name|T
parameter_list|>
implements|implements
name|CacheSerializer
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|serialize (@ullable T object)
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
annotation|@
name|Nullable
name|T
name|object
parameter_list|)
block|{
try|try
init|(
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|ObjectOutputStream
name|oout
operator|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bout
argument_list|)
init|)
block|{
name|oout
operator|.
name|writeObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|oout
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|bout
operator|.
name|toByteArray
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
name|IllegalArgumentException
argument_list|(
literal|"Failed to serialize object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|deserialize (byte[] in)
specifier|public
name|T
name|deserialize
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
name|Object
name|object
decl_stmt|;
try|try
init|(
name|ByteArrayInputStream
name|bin
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|in
argument_list|)
init|;
name|ObjectInputStream
name|oin
operator|=
operator|new
name|ObjectInputStream
argument_list|(
name|bin
argument_list|)
init|)
block|{
name|object
operator|=
name|oin
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to deserialize object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|(
name|T
operator|)
name|object
return|;
block|}
block|}
end_class

end_unit

