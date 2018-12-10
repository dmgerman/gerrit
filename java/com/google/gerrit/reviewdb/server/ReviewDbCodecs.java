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
DECL|package|com.google.gerrit.reviewdb.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetApproval
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
name|CodecFactory
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

begin_comment
comment|/** {@link ProtobufCodec} instances for ReviewDb types. */
end_comment

begin_class
DECL|class|ReviewDbCodecs
specifier|public
class|class
name|ReviewDbCodecs
block|{
DECL|field|APPROVAL_CODEC
specifier|public
specifier|static
specifier|final
name|ProtobufCodec
argument_list|<
name|PatchSetApproval
argument_list|>
name|APPROVAL_CODEC
init|=
name|CodecFactory
operator|.
name|encoder
argument_list|(
name|PatchSetApproval
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CHANGE_CODEC
specifier|public
specifier|static
specifier|final
name|ProtobufCodec
argument_list|<
name|Change
argument_list|>
name|CHANGE_CODEC
init|=
name|CodecFactory
operator|.
name|encoder
argument_list|(
name|Change
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ReviewDbCodecs ()
specifier|private
name|ReviewDbCodecs
parameter_list|()
block|{}
block|}
end_class

end_unit

