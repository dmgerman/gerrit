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
name|PrimitiveSink
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
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_enum
DECL|enum|ObjectKeyTypeImpl
enum|enum
name|ObjectKeyTypeImpl
implements|implements
name|KeyType
argument_list|<
name|Object
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
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
name|Object
name|get
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|col
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getObject
argument_list|(
name|col
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|set (PreparedStatement ps, int col, Object key)
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
name|Object
name|key
parameter_list|)
throws|throws
name|SQLException
block|{
name|ps
operator|.
name|setObject
argument_list|(
name|col
argument_list|,
name|key
argument_list|,
name|Types
operator|.
name|JAVA_OBJECT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|funnel ()
specifier|public
name|Funnel
argument_list|<
name|Object
argument_list|>
name|funnel
parameter_list|()
block|{
return|return
operator|new
name|Funnel
argument_list|<
name|Object
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
name|Object
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
try|try
init|(
name|ObjectOutputStream
name|ser
init|=
operator|new
name|ObjectOutputStream
argument_list|(
operator|new
name|SinkOutputStream
argument_list|(
name|into
argument_list|)
argument_list|)
init|)
block|{
name|ser
operator|.
name|writeObject
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|ser
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot hash as Serializable"
argument_list|,
name|err
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
DECL|class|SinkOutputStream
specifier|private
specifier|static
class|class
name|SinkOutputStream
extends|extends
name|OutputStream
block|{
DECL|field|sink
specifier|private
specifier|final
name|PrimitiveSink
name|sink
decl_stmt|;
DECL|method|SinkOutputStream (PrimitiveSink sink)
name|SinkOutputStream
parameter_list|(
name|PrimitiveSink
name|sink
parameter_list|)
block|{
name|this
operator|.
name|sink
operator|=
name|sink
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (int b)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
block|{
name|sink
operator|.
name|putByte
argument_list|(
operator|(
name|byte
operator|)
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (byte[] b, int p, int n)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|p
parameter_list|,
name|int
name|n
parameter_list|)
block|{
name|sink
operator|.
name|putBytes
argument_list|(
name|b
argument_list|,
name|p
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_enum

end_unit

