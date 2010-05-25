begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|AccountGroupName
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
name|ReviewDb
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
name|SystemConfig
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
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_class
DECL|class|Schema_33
specifier|public
class|class
name|Schema_33
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_33 (Provider<Schema_32> prior)
name|Schema_33
parameter_list|(
name|Provider
argument_list|<
name|Schema_32
argument_list|>
name|prior
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|migrateData (ReviewDb db, UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
block|{
name|SystemConfig
name|config
init|=
name|db
operator|.
name|systemConfig
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|AccountGroup
name|batchUsers
init|=
operator|new
name|AccountGroup
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Non-Interactive Users"
argument_list|)
argument_list|,
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|db
operator|.
name|nextAccountGroupId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|batchUsers
operator|.
name|setDescription
argument_list|(
literal|"Users who perform batch actions on Gerrit"
argument_list|)
expr_stmt|;
name|batchUsers
operator|.
name|setOwnerGroupId
argument_list|(
name|config
operator|.
name|adminGroupId
argument_list|)
expr_stmt|;
name|batchUsers
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|INTERNAL
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|batchUsers
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|AccountGroupName
argument_list|(
name|batchUsers
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|batchUsersGroupId
operator|=
name|batchUsers
operator|.
name|getId
argument_list|()
expr_stmt|;
name|db
operator|.
name|systemConfig
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

