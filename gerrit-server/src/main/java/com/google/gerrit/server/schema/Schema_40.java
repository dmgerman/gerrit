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
name|AccountProjectWatch
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
name|gwtorm
operator|.
name|schema
operator|.
name|sql
operator|.
name|DialectH2
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
name|DialectMySQL
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
name|schema
operator|.
name|sql
operator|.
name|SqlDialect
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
DECL|class|Schema_40
specifier|public
class|class
name|Schema_40
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_40 (Provider<Schema_39> prior)
name|Schema_40
parameter_list|(
name|Provider
argument_list|<
name|Schema_39
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
comment|// Set to "*" the filter field of the previously watched projects
comment|//
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
name|execute
argument_list|(
literal|"UPDATE account_project_watches"
comment|//
operator|+
literal|" SET filter = '"
operator|+
name|AccountProjectWatch
operator|.
name|FILTER_ALL
operator|+
literal|"'"
comment|//
operator|+
literal|" WHERE filter IS NULL OR filter = ''"
argument_list|)
expr_stmt|;
comment|// Set the new primary key
comment|//
specifier|final
name|SqlDialect
name|dialect
init|=
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getDialect
argument_list|()
decl_stmt|;
if|if
condition|(
name|dialect
operator|instanceof
name|DialectPostgreSQL
condition|)
block|{
name|stmt
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE account_project_watches "
operator|+
literal|"DROP CONSTRAINT account_project_watches_pkey"
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE account_project_watches "
operator|+
literal|"ADD PRIMARY KEY (account_id, project_name, filter)"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|dialect
operator|instanceof
name|DialectH2
operator|)
operator|||
operator|(
name|dialect
operator|instanceof
name|DialectMySQL
operator|)
condition|)
block|{
name|stmt
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE account_project_watches DROP PRIMARY KEY"
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE account_project_watches "
operator|+
literal|"ADD PRIMARY KEY (account_id, project_name, filter)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Unsupported dialect "
operator|+
name|dialect
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit

