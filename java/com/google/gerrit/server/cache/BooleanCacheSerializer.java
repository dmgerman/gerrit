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
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|TextFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_enum
DECL|enum|BooleanCacheSerializer
specifier|public
enum|enum
name|BooleanCacheSerializer
implements|implements
name|CacheSerializer
argument_list|<
name|Boolean
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
DECL|field|TRUE
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TRUE
init|=
name|Boolean
operator|.
name|toString
argument_list|(
literal|true
argument_list|)
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
decl_stmt|;
DECL|field|FALSE
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|FALSE
init|=
name|Boolean
operator|.
name|toString
argument_list|(
literal|false
argument_list|)
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|serialize (Boolean object)
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
name|Boolean
name|object
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
name|checkNotNull
argument_list|(
name|object
argument_list|)
condition|?
name|TRUE
else|:
name|FALSE
decl_stmt|;
return|return
name|Arrays
operator|.
name|copyOf
argument_list|(
name|bytes
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|deserialize (byte[] in)
specifier|public
name|Boolean
name|deserialize
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|in
argument_list|,
name|TRUE
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
elseif|else
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|in
argument_list|,
name|FALSE
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid Boolean value: "
operator|+
name|TextFormat
operator|.
name|escapeBytes
argument_list|(
name|in
argument_list|)
argument_list|)
throw|;
block|}
block|}
end_enum

end_unit

