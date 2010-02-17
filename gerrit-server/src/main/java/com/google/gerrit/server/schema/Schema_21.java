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
name|Project
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
name|Collections
import|;
end_import

begin_class
DECL|class|Schema_21
class|class
name|Schema_21
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_21 (Provider<Schema_20> prior)
name|Schema_21
parameter_list|(
name|Provider
argument_list|<
name|Schema_20
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
DECL|method|migrateData (ReviewDb db)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
name|JdbcSchema
name|jdbc
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
name|SystemConfig
name|sc
init|=
name|db
operator|.
name|systemConfig
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|SystemConfig
operator|.
name|Key
argument_list|()
argument_list|)
decl_stmt|;
name|Statement
name|s
init|=
name|jdbc
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
name|r
decl_stmt|;
name|r
operator|=
name|s
operator|.
name|executeQuery
argument_list|(
literal|"SELECT name FROM projects WHERE project_id = 0"
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|r
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot read old wild project"
argument_list|)
throw|;
block|}
name|sc
operator|.
name|wildProjectName
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|jdbc
operator|.
name|getDialect
argument_list|()
operator|instanceof
name|DialectMySQL
condition|)
block|{
name|s
operator|.
name|execute
argument_list|(
literal|"DROP FUNCTION nextval_project_id"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|jdbc
operator|.
name|getDialect
argument_list|()
operator|instanceof
name|DialectH2
condition|)
block|{
name|s
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE projects DROP CONSTRAINT"
operator|+
literal|" IF EXISTS CONSTRAINT_F3"
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|sc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

