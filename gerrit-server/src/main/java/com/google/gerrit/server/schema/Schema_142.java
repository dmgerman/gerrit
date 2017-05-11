begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalId
operator|.
name|SCHEME_USERNAME
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
name|Strings
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
name|server
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
name|server
operator|.
name|account
operator|.
name|HashedPassword
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
name|account
operator|.
name|externalids
operator|.
name|ExternalId
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
name|Statement
import|;
end_import

begin_class
DECL|class|Schema_142
specifier|public
class|class
name|Schema_142
extends|extends
name|SchemaVersion
block|{
DECL|field|MAX_BATCH_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|MAX_BATCH_SIZE
init|=
literal|1000
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_142 (Provider<Schema_141> prior)
name|Schema_142
parameter_list|(
name|Provider
argument_list|<
name|Schema_141
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
try|try
init|(
name|PreparedStatement
name|updateStmt
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
name|prepareStatement
argument_list|(
literal|"UPDATE account_external_ids "
operator|+
literal|"SET password = ? "
operator|+
literal|"WHERE external_id = ?"
argument_list|)
init|)
block|{
name|int
name|batchCount
init|=
literal|0
decl_stmt|;
try|try
init|(
name|Statement
name|stmt
init|=
name|newStatement
argument_list|(
name|db
argument_list|)
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT external_id, password FROM account_external_ids"
argument_list|)
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|externalId
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"external_id"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ExternalId
operator|.
name|Key
operator|.
name|parse
argument_list|(
name|externalId
argument_list|)
operator|.
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
operator|||
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|password
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|HashedPassword
name|hashed
init|=
name|HashedPassword
operator|.
name|fromPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|updateStmt
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|hashed
operator|.
name|encode
argument_list|()
argument_list|)
expr_stmt|;
name|updateStmt
operator|.
name|setString
argument_list|(
literal|2
argument_list|,
name|externalId
argument_list|)
expr_stmt|;
name|updateStmt
operator|.
name|addBatch
argument_list|()
expr_stmt|;
name|batchCount
operator|++
expr_stmt|;
if|if
condition|(
name|batchCount
operator|>=
name|MAX_BATCH_SIZE
condition|)
block|{
name|updateStmt
operator|.
name|executeBatch
argument_list|()
expr_stmt|;
name|batchCount
operator|=
literal|0
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|batchCount
operator|>
literal|0
condition|)
block|{
name|updateStmt
operator|.
name|executeBatch
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

