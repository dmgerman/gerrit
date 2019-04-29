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
DECL|package|com.google.gerrit.reviewdb.converter
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|converter
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
name|Entities
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
comment|/**  * Proto converter for {@code ObjectId}s.  *  *<p>This converter uses the hex representation of object IDs embedded in a wrapper proto type,  * rather than a more parsimonious implementation (e.g. a raw byte array), for two reasons:  *  *<ul>  *<li>Hex strings are easier to read and work with when reading and writing protos in text  *       formats, for example in test failure messages, or when using command-line tools.  *<li>This maintains backwards wire compatibility with a pre-NoteDb implementation.  *</ul>  */
end_comment

begin_enum
DECL|enum|ObjectIdProtoConverter
specifier|public
enum|enum
name|ObjectIdProtoConverter
implements|implements
name|ProtoConverter
argument_list|<
name|Entities
operator|.
name|ObjectId
argument_list|,
name|ObjectId
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|toProto (ObjectId objectId)
specifier|public
name|Entities
operator|.
name|ObjectId
name|toProto
parameter_list|(
name|ObjectId
name|objectId
parameter_list|)
block|{
return|return
name|Entities
operator|.
name|ObjectId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setName
argument_list|(
name|objectId
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|fromProto (Entities.ObjectId proto)
specifier|public
name|ObjectId
name|fromProto
parameter_list|(
name|Entities
operator|.
name|ObjectId
name|proto
parameter_list|)
block|{
return|return
name|ObjectId
operator|.
name|fromString
argument_list|(
name|proto
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getParser ()
specifier|public
name|Parser
argument_list|<
name|Entities
operator|.
name|ObjectId
argument_list|>
name|getParser
parameter_list|()
block|{
return|return
name|Entities
operator|.
name|ObjectId
operator|.
name|parser
argument_list|()
return|;
block|}
block|}
end_enum

end_unit

