begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|AccountGroup
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
name|AccountGroupName
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
name|server
operator|.
name|Access
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
name|server
operator|.
name|OrmException
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
name|server
operator|.
name|PrimaryKey
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
name|server
operator|.
name|Query
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
name|server
operator|.
name|ResultSet
import|;
end_import

begin_interface
DECL|interface|AccountGroupNameAccess
specifier|public
interface|interface
name|AccountGroupNameAccess
extends|extends
name|Access
argument_list|<
name|AccountGroupName
argument_list|,
name|AccountGroup
operator|.
name|NameKey
argument_list|>
block|{
annotation|@
name|Override
annotation|@
name|PrimaryKey
argument_list|(
literal|"name"
argument_list|)
DECL|method|get (AccountGroup.NameKey name)
name|AccountGroupName
name|get
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"ORDER BY name"
argument_list|)
DECL|method|all ()
name|ResultSet
argument_list|<
name|AccountGroupName
argument_list|>
name|all
parameter_list|()
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE name.name>= ? AND name.name<= ? ORDER BY name LIMIT ?"
argument_list|)
DECL|method|suggestByName (String nameA, String nameB, int limit)
name|ResultSet
argument_list|<
name|AccountGroupName
argument_list|>
name|suggestByName
parameter_list|(
name|String
name|nameA
parameter_list|,
name|String
name|nameB
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

