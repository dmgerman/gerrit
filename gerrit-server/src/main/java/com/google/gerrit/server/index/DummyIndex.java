begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|server
operator|.
name|query
operator|.
name|Predicate
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
name|query
operator|.
name|QueryParseException
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|query
operator|.
name|change
operator|.
name|ChangeDataSource
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

begin_class
DECL|class|DummyIndex
specifier|public
class|class
name|DummyIndex
implements|implements
name|ChangeIndex
block|{
annotation|@
name|Override
DECL|method|getSchema ()
specifier|public
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|getSchema
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{   }
annotation|@
name|Override
DECL|method|replace (ChangeData cd)
specifier|public
name|void
name|replace
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{   }
annotation|@
name|Override
DECL|method|delete (ChangeData cd)
specifier|public
name|void
name|delete
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{   }
annotation|@
name|Override
DECL|method|deleteAll ()
specifier|public
name|void
name|deleteAll
parameter_list|()
throws|throws
name|IOException
block|{   }
annotation|@
name|Override
DECL|method|getSource (Predicate<ChangeData> p, int start, int limit)
specifier|public
name|ChangeDataSource
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|QueryParseException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|markReady (boolean ready)
specifier|public
name|void
name|markReady
parameter_list|(
name|boolean
name|ready
parameter_list|)
throws|throws
name|IOException
block|{   }
block|}
end_class

end_unit

