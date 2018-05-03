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

begin_interface
DECL|interface|KeyType
interface|interface
name|KeyType
parameter_list|<
name|K
parameter_list|>
block|{
DECL|method|columnType ()
name|String
name|columnType
parameter_list|()
function_decl|;
DECL|method|get (ResultSet rs, int col)
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
function_decl|;
DECL|method|set (PreparedStatement ps, int col, K key)
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
function_decl|;
DECL|method|funnel ()
name|Funnel
argument_list|<
name|K
argument_list|>
name|funnel
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

