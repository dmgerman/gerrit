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
name|git
operator|.
name|ObjectIds
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

begin_enum
DECL|enum|ObjectIdCacheSerializer
specifier|public
enum|enum
name|ObjectIdCacheSerializer
implements|implements
name|CacheSerializer
argument_list|<
name|ObjectId
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|serialize (ObjectId object)
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
name|ObjectId
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
name|ObjectIds
operator|.
name|LEN
index|]
decl_stmt|;
name|object
operator|.
name|copyRawTo
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
annotation|@
name|Override
DECL|method|deserialize (byte[] in)
specifier|public
name|ObjectId
name|deserialize
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|==
literal|null
operator|||
name|in
operator|.
name|length
operator|!=
name|ObjectIds
operator|.
name|LEN
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to deserialize ObjectId"
argument_list|)
throw|;
block|}
return|return
name|ObjectId
operator|.
name|fromRaw
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

