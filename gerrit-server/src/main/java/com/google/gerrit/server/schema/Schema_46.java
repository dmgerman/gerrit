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
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcSchema
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
name|sql
operator|.
name|Connection
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
name|Statement
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
DECL|class|Schema_46
specifier|public
class|class
name|Schema_46
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_46 (final Provider<Schema_45> prior)
name|Schema_46
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|Schema_45
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
name|SQLException
throws|,
name|OrmException
block|{
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
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
decl_stmt|;
comment|// update system_config
specifier|final
name|Connection
name|connection
init|=
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getConnection
argument_list|()
decl_stmt|;
specifier|final
name|Statement
name|stmt
init|=
name|connection
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
literal|"UPDATE system_config SET OWNER_GROUP_ID = "
operator|+
name|groupId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ResultSet
name|resultSet
init|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT ADMIN_GROUP_ID FROM system_config"
argument_list|)
decl_stmt|;
name|resultSet
operator|.
name|next
argument_list|()
expr_stmt|;
specifier|final
name|int
name|adminGroupId
init|=
name|resultSet
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// create 'Project Owners' group
name|AccountGroup
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Project Owners"
argument_list|)
decl_stmt|;
name|AccountGroup
name|group
init|=
operator|new
name|AccountGroup
argument_list|(
name|nameKey
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
name|group
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|SYSTEM
argument_list|)
expr_stmt|;
name|group
operator|.
name|setOwnerGroupId
argument_list|(
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|adminGroupId
argument_list|)
argument_list|)
expr_stmt|;
name|group
operator|.
name|setDescription
argument_list|(
literal|"Any owner of the project"
argument_list|)
expr_stmt|;
name|AccountGroupName
name|gn
init|=
operator|new
name|AccountGroupName
argument_list|(
name|group
argument_list|)
decl_stmt|;
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
name|gn
argument_list|)
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
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

