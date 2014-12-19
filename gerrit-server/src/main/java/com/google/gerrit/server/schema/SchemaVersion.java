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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|CurrentSchemaVersion
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
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcExecutor
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
name|gwtorm
operator|.
name|server
operator|.
name|StatementExecutor
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
name|AbstractModule
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** A version of the database schema. */
end_comment

begin_class
DECL|class|SchemaVersion
specifier|public
specifier|abstract
class|class
name|SchemaVersion
block|{
comment|/** The current schema version. */
DECL|field|C
specifier|public
specifier|static
specifier|final
name|Class
argument_list|<
name|Schema_102
argument_list|>
name|C
init|=
name|Schema_102
operator|.
name|class
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|SchemaVersion
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Current
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|C
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|prior
specifier|private
specifier|final
name|Provider
argument_list|<
name|?
extends|extends
name|SchemaVersion
argument_list|>
name|prior
decl_stmt|;
DECL|field|versionNbr
specifier|private
specifier|final
name|int
name|versionNbr
decl_stmt|;
DECL|method|SchemaVersion (final Provider<? extends SchemaVersion> prior)
specifier|protected
name|SchemaVersion
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|?
extends|extends
name|SchemaVersion
argument_list|>
name|prior
parameter_list|)
block|{
name|this
operator|.
name|prior
operator|=
name|prior
expr_stmt|;
name|this
operator|.
name|versionNbr
operator|=
name|guessVersion
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|guessVersion (Class<?> c)
specifier|public
specifier|static
name|int
name|guessVersion
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|String
name|n
init|=
name|c
operator|.
name|getName
argument_list|()
decl_stmt|;
name|n
operator|=
name|n
operator|.
name|substring
argument_list|(
name|n
operator|.
name|lastIndexOf
argument_list|(
literal|'_'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
while|while
condition|(
name|n
operator|.
name|startsWith
argument_list|(
literal|"0"
argument_list|)
condition|)
name|n
operator|=
name|n
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|n
argument_list|)
return|;
block|}
DECL|method|SchemaVersion (final Provider<? extends SchemaVersion> prior, final int versionNbr)
specifier|protected
name|SchemaVersion
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|?
extends|extends
name|SchemaVersion
argument_list|>
name|prior
parameter_list|,
specifier|final
name|int
name|versionNbr
parameter_list|)
block|{
name|this
operator|.
name|prior
operator|=
name|prior
expr_stmt|;
name|this
operator|.
name|versionNbr
operator|=
name|versionNbr
expr_stmt|;
block|}
comment|/** @return the {@link CurrentSchemaVersion#versionNbr} this step targets. */
DECL|method|getVersionNbr ()
specifier|public
specifier|final
name|int
name|getVersionNbr
parameter_list|()
block|{
return|return
name|versionNbr
return|;
block|}
DECL|method|check (UpdateUI ui, CurrentSchemaVersion curr, ReviewDb db)
specifier|public
specifier|final
name|void
name|check
parameter_list|(
name|UpdateUI
name|ui
parameter_list|,
name|CurrentSchemaVersion
name|curr
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
if|if
condition|(
name|curr
operator|.
name|versionNbr
operator|==
name|versionNbr
condition|)
block|{
comment|// Nothing to do, we are at the correct schema.
block|}
elseif|else
if|if
condition|(
name|curr
operator|.
name|versionNbr
operator|>
name|versionNbr
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot downgrade database schema from version "
operator|+
name|curr
operator|.
name|versionNbr
operator|+
literal|" to "
operator|+
name|versionNbr
operator|+
literal|"."
argument_list|)
throw|;
block|}
else|else
block|{
name|upgradeFrom
argument_list|(
name|ui
argument_list|,
name|curr
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Runs check on the prior schema version, and then upgrades. */
DECL|method|upgradeFrom (UpdateUI ui, CurrentSchemaVersion curr, ReviewDb db)
specifier|private
name|void
name|upgradeFrom
parameter_list|(
name|UpdateUI
name|ui
parameter_list|,
name|CurrentSchemaVersion
name|curr
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
name|List
argument_list|<
name|SchemaVersion
argument_list|>
name|pending
init|=
name|pending
argument_list|(
name|curr
operator|.
name|versionNbr
argument_list|)
decl_stmt|;
name|updateSchema
argument_list|(
name|pending
argument_list|,
name|ui
argument_list|,
name|db
argument_list|)
expr_stmt|;
name|migrateData
argument_list|(
name|pending
argument_list|,
name|ui
argument_list|,
name|curr
argument_list|,
name|db
argument_list|)
expr_stmt|;
name|JdbcSchema
name|s
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
name|JdbcExecutor
name|e
init|=
operator|new
name|JdbcExecutor
argument_list|(
name|s
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|pruneList
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|s
operator|.
name|pruneSchema
argument_list|(
operator|new
name|StatementExecutor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|pruneList
operator|.
name|add
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|pruneList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ui
operator|.
name|pruneSchema
argument_list|(
name|e
argument_list|,
name|pruneList
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|e
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|pending (int curr)
specifier|private
name|List
argument_list|<
name|SchemaVersion
argument_list|>
name|pending
parameter_list|(
name|int
name|curr
parameter_list|)
block|{
name|List
argument_list|<
name|SchemaVersion
argument_list|>
name|r
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|versionNbr
operator|-
name|curr
argument_list|)
decl_stmt|;
for|for
control|(
name|SchemaVersion
name|v
init|=
name|this
init|;
name|curr
operator|<
name|v
operator|.
name|getVersionNbr
argument_list|()
condition|;
name|v
operator|=
name|v
operator|.
name|prior
operator|.
name|get
argument_list|()
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|r
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|method|updateSchema (List<SchemaVersion> pending, UpdateUI ui, ReviewDb db)
specifier|private
name|void
name|updateSchema
parameter_list|(
name|List
argument_list|<
name|SchemaVersion
argument_list|>
name|pending
parameter_list|,
name|UpdateUI
name|ui
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
for|for
control|(
name|SchemaVersion
name|v
range|:
name|pending
control|)
block|{
name|ui
operator|.
name|message
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Upgrading schema to %d ..."
argument_list|,
name|v
operator|.
name|getVersionNbr
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|v
operator|.
name|preUpdateSchema
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
name|JdbcSchema
name|s
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
name|JdbcExecutor
name|e
init|=
operator|new
name|JdbcExecutor
argument_list|(
name|s
argument_list|)
decl_stmt|;
try|try
block|{
name|s
operator|.
name|updateSchema
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|e
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Invoked before updateSchema adds new columns/tables.    *    * @param db open database handle.    * @throws OrmException if a Gerrit-specific exception occurred.    * @throws SQLException if an underlying SQL exception occurred.    */
DECL|method|preUpdateSchema (ReviewDb db)
specifier|protected
name|void
name|preUpdateSchema
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{   }
DECL|method|migrateData (List<SchemaVersion> pending, UpdateUI ui, CurrentSchemaVersion curr, ReviewDb db)
specifier|private
name|void
name|migrateData
parameter_list|(
name|List
argument_list|<
name|SchemaVersion
argument_list|>
name|pending
parameter_list|,
name|UpdateUI
name|ui
parameter_list|,
name|CurrentSchemaVersion
name|curr
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
for|for
control|(
name|SchemaVersion
name|v
range|:
name|pending
control|)
block|{
name|ui
operator|.
name|message
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Migrating data to schema %d ..."
argument_list|,
name|v
operator|.
name|getVersionNbr
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|v
operator|.
name|migrateData
argument_list|(
name|db
argument_list|,
name|ui
argument_list|)
expr_stmt|;
name|v
operator|.
name|finish
argument_list|(
name|curr
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Invoked between updateSchema (adds new columns/tables) and pruneSchema    * (removes deleted columns/tables).    *    * @param db open database handle.    * @param ui interface for interacting with the user.    * @throws OrmException if a Gerrit-specific exception occurred.    * @throws SQLException if an underlying SQL exception occurred.    */
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
block|{   }
comment|/** Mark the current schema version. */
DECL|method|finish (CurrentSchemaVersion curr, ReviewDb db)
specifier|protected
name|void
name|finish
parameter_list|(
name|CurrentSchemaVersion
name|curr
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
name|curr
operator|.
name|versionNbr
operator|=
name|versionNbr
expr_stmt|;
name|db
operator|.
name|schemaVersion
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|curr
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Rename an existing table. */
DECL|method|renameTable (ReviewDb db, String from, String to)
specifier|protected
name|void
name|renameTable
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|JdbcSchema
name|s
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
specifier|final
name|JdbcExecutor
name|e
init|=
operator|new
name|JdbcExecutor
argument_list|(
name|s
argument_list|)
decl_stmt|;
try|try
block|{
name|s
operator|.
name|renameTable
argument_list|(
name|e
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|e
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** Rename an existing column. */
DECL|method|renameColumn (ReviewDb db, String table, String from, String to)
specifier|protected
name|void
name|renameColumn
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|String
name|table
parameter_list|,
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|JdbcSchema
name|s
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
specifier|final
name|JdbcExecutor
name|e
init|=
operator|new
name|JdbcExecutor
argument_list|(
name|s
argument_list|)
decl_stmt|;
try|try
block|{
name|s
operator|.
name|renameField
argument_list|(
name|e
argument_list|,
name|table
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|e
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** Execute an SQL statement. */
DECL|method|execute (ReviewDb db, String sql)
specifier|protected
name|void
name|execute
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
name|Statement
name|s
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
name|s
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

