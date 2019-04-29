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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
operator|.
name|ObjectIds
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
comment|/**  * Helper for serializing {@link ObjectId} instances to/from protobuf fields.  *  *<p>Reuse a single instance's {@link #toByteString(ObjectId)} and {@link  * #fromByteString(ByteString)} within a single {@link CacheSerializer#serialize} or {@link  * CacheSerializer#deserialize} method body to minimize allocation of temporary buffers.  *  *<p><strong>Note:</strong> This class is not threadsafe. Instances must not be stored in {@link  * CacheSerializer} fields if the serializer instances will be used from multiple threads.  */
end_comment

begin_class
DECL|class|ObjectIdConverter
specifier|public
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
name|ObjectIds
operator|.
name|LEN
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
name|ObjectIds
operator|.
name|LEN
argument_list|,
literal|"expected ByteString of length %s: %s"
argument_list|,
name|ObjectIds
operator|.
name|LEN
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
end_class

end_unit

