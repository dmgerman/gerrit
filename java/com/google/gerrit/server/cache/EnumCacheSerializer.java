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
name|common
operator|.
name|base
operator|.
name|Converter
import|;
end_import

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
name|Enums
import|;
end_import

begin_class
DECL|class|EnumCacheSerializer
specifier|public
class|class
name|EnumCacheSerializer
parameter_list|<
name|E
extends|extends
name|Enum
parameter_list|<
name|E
parameter_list|>
parameter_list|>
implements|implements
name|CacheSerializer
argument_list|<
name|E
argument_list|>
block|{
DECL|field|converter
specifier|private
specifier|final
name|Converter
argument_list|<
name|String
argument_list|,
name|E
argument_list|>
name|converter
decl_stmt|;
DECL|method|EnumCacheSerializer (Class<E> clazz)
specifier|public
name|EnumCacheSerializer
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|Enums
operator|.
name|stringConverter
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|serialize (E object)
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
name|E
name|object
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
name|checkNotNull
argument_list|(
name|object
argument_list|)
argument_list|)
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|deserialize (byte[] in)
specifier|public
name|E
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
name|convert
argument_list|(
operator|new
name|String
argument_list|(
name|checkNotNull
argument_list|(
name|in
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

