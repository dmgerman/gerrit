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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|Schema_23
class|class
name|Schema_23
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_23 (Provider<Schema_22> prior)
name|Schema_23
parameter_list|(
name|Provider
argument_list|<
name|Schema_22
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
throws|,
name|SQLException
block|{
name|Collection
argument_list|<
name|AccountGroupName
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroupName
argument_list|>
argument_list|()
decl_stmt|;
name|Statement
name|queryStmt
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
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|ResultSet
name|results
init|=
name|queryStmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT group_id, name FROM account_groups"
argument_list|)
decl_stmt|;
while|while
condition|(
name|results
operator|.
name|next
argument_list|()
condition|)
block|{
specifier|final
name|int
name|id
init|=
name|results
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|results
operator|.
name|getString
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|final
name|AccountGroup
operator|.
name|Id
name|group
init|=
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|id
argument_list|)
decl_stmt|;
specifier|final
name|AccountGroup
operator|.
name|NameKey
name|key
init|=
name|toKey
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
operator|new
name|AccountGroupName
argument_list|(
name|key
argument_list|,
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|queryStmt
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
DECL|method|toKey (final String name)
specifier|private
name|AccountGroup
operator|.
name|NameKey
name|toKey
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

