begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|schema
operator|.
name|sql
operator|.
name|DialectPostgreSQL
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
DECL|class|Schema_70
specifier|public
class|class
name|Schema_70
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_70 (Provider<Schema_69> prior)
specifier|protected
name|Schema_70
parameter_list|(
name|Provider
argument_list|<
name|Schema_69
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
name|Statement
name|stmt
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
name|stmt
operator|.
name|executeUpdate
argument_list|(
literal|"UPDATE tracking_ids SET tracking_key = tracking_id"
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|stmt
argument_list|,
literal|"DROP INDEX tracking_ids_byTrkId"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getDialect
argument_list|()
operator|instanceof
name|DialectPostgreSQL
condition|)
block|{
name|execute
argument_list|(
name|stmt
argument_list|,
literal|"ALTER TABLE tracking_ids DROP CONSTRAINT tracking_ids_pkey"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|execute
argument_list|(
name|stmt
argument_list|,
literal|"ALTER TABLE tracking_ids DROP PRIMARY KEY"
argument_list|)
expr_stmt|;
block|}
name|stmt
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE tracking_ids"
operator|+
literal|" ADD PRIMARY KEY (change_id, tracking_key, tracking_system)"
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
literal|"CREATE INDEX tracking_ids_byTrkKey"
operator|+
literal|" ON tracking_ids (tracking_key)"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|execute (Statement stmt, String command)
specifier|private
specifier|static
specifier|final
name|void
name|execute
parameter_list|(
name|Statement
name|stmt
parameter_list|,
name|String
name|command
parameter_list|)
block|{
try|try
block|{
name|stmt
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

