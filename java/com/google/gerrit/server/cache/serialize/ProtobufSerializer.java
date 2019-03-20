begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|proto
operator|.
name|Protos
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

begin_comment
comment|/** A CacheSerializer for Protobuf messages. */
end_comment

begin_class
DECL|class|ProtobufSerializer
specifier|public
class|class
name|ProtobufSerializer
parameter_list|<
name|T
extends|extends
name|MessageLite
parameter_list|>
implements|implements
name|CacheSerializer
argument_list|<
name|T
argument_list|>
block|{
DECL|field|parser
specifier|private
specifier|final
name|Parser
argument_list|<
name|T
argument_list|>
name|parser
decl_stmt|;
DECL|method|ProtobufSerializer (Parser<T> parser)
specifier|public
name|ProtobufSerializer
parameter_list|(
name|Parser
argument_list|<
name|T
argument_list|>
name|parser
parameter_list|)
block|{
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|serialize (T object)
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
name|Protos
operator|.
name|toByteArray
argument_list|(
name|object
argument_list|)
return|;
block|}
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
return|return
name|Protos
operator|.
name|parseUnchecked
argument_list|(
name|parser
argument_list|,
name|in
argument_list|)
return|;
block|}
block|}
end_class

end_unit

