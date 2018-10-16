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
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|client
operator|.
name|IntKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_class
DECL|class|IntKeyCacheSerializer
specifier|public
class|class
name|IntKeyCacheSerializer
parameter_list|<
name|K
extends|extends
name|IntKey
parameter_list|<
name|?
parameter_list|>
parameter_list|>
implements|implements
name|CacheSerializer
argument_list|<
name|K
argument_list|>
block|{
DECL|field|factory
specifier|private
specifier|final
name|Function
argument_list|<
name|Integer
argument_list|,
name|K
argument_list|>
name|factory
decl_stmt|;
DECL|method|IntKeyCacheSerializer (Function<Integer, K> factory)
specifier|public
name|IntKeyCacheSerializer
parameter_list|(
name|Function
argument_list|<
name|Integer
argument_list|,
name|K
argument_list|>
name|factory
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|requireNonNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|serialize (K object)
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
name|K
name|object
parameter_list|)
block|{
return|return
name|IntegerCacheSerializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|object
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|deserialize (byte[] in)
specifier|public
name|K
name|deserialize
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
return|return
name|factory
operator|.
name|apply
argument_list|(
name|IntegerCacheSerializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

