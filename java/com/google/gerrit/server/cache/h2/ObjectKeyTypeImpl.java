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
DECL|package|com.google.gerrit.server.cache.h2
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
name|h2
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Funnel
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
name|hash
operator|.
name|Funnels
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
name|hash
operator|.
name|PrimitiveSink
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
name|server
operator|.
name|cache
operator|.
name|serialize
operator|.
name|CacheSerializer
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
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_class
DECL|class|ObjectKeyTypeImpl
class|class
name|ObjectKeyTypeImpl
parameter_list|<
name|K
parameter_list|>
implements|implements
name|KeyType
argument_list|<
name|K
argument_list|>
block|{
DECL|field|serializer
specifier|private
specifier|final
name|CacheSerializer
argument_list|<
name|K
argument_list|>
name|serializer
decl_stmt|;
DECL|method|ObjectKeyTypeImpl (CacheSerializer<K> serializer)
name|ObjectKeyTypeImpl
parameter_list|(
name|CacheSerializer
argument_list|<
name|K
argument_list|>
name|serializer
parameter_list|)
block|{
name|this
operator|.
name|serializer
operator|=
name|serializer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|columnType ()
specifier|public
name|String
name|columnType
parameter_list|()
block|{
return|return
literal|"OTHER"
return|;
block|}
annotation|@
name|Override
DECL|method|get (ResultSet rs, int col)
specifier|public
name|K
name|get
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|col
parameter_list|)
throws|throws
name|IOException
throws|,
name|SQLException
block|{
return|return
name|serializer
operator|.
name|deserialize
argument_list|(
name|rs
operator|.
name|getBytes
argument_list|(
name|col
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|set (PreparedStatement ps, int col, K key)
specifier|public
name|void
name|set
parameter_list|(
name|PreparedStatement
name|ps
parameter_list|,
name|int
name|col
parameter_list|,
name|K
name|key
parameter_list|)
throws|throws
name|IOException
throws|,
name|SQLException
block|{
name|ps
operator|.
name|setBytes
argument_list|(
name|col
argument_list|,
name|serializer
operator|.
name|serialize
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|funnel ()
specifier|public
name|Funnel
argument_list|<
name|K
argument_list|>
name|funnel
parameter_list|()
block|{
return|return
operator|new
name|Funnel
argument_list|<
name|K
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|funnel
parameter_list|(
name|K
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
operator|.
name|funnel
argument_list|(
name|serializer
operator|.
name|serialize
argument_list|(
name|from
argument_list|)
argument_list|,
name|into
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

