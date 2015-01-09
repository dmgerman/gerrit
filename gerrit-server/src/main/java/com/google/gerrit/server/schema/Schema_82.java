begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|ImmutableMap
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
name|SqlDialect
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|Schema_82
specifier|public
class|class
name|Schema_82
extends|extends
name|SchemaVersion
block|{
DECL|field|tables
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tables
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"account_group_includes_by_uuid"
argument_list|,
literal|"account_group_by_id"
argument_list|,
literal|"account_group_includes_by_uuid_audit"
argument_list|,
literal|"account_group_by_id_aud"
argument_list|)
decl_stmt|;
DECL|field|indexes
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Index
argument_list|>
name|indexes
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"account_project_watches_byProject"
argument_list|,
operator|new
name|Index
argument_list|(
literal|"account_project_watches"
argument_list|,
literal|"account_project_watches_byP"
argument_list|)
argument_list|,
literal|"patch_set_approvals_closedByUser"
argument_list|,
operator|new
name|Index
argument_list|(
literal|"patch_set_approvals"
argument_list|,
literal|"patch_set_approvals_closedByU"
argument_list|)
argument_list|,
literal|"submodule_subscription_access_bySubscription"
argument_list|,
operator|new
name|Index
argument_list|(
literal|"submodule_subscriptions"
argument_list|,
literal|"submodule_subscr_acc_byS"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_82 (Provider<Schema_81> prior)
name|Schema_82
parameter_list|(
name|Provider
argument_list|<
name|Schema_81
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
block|{
name|JdbcSchema
name|s
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
try|try
init|(
name|JdbcExecutor
name|e
init|=
operator|new
name|JdbcExecutor
argument_list|(
name|s
argument_list|)
init|)
block|{
name|renameTables
argument_list|(
name|db
argument_list|,
name|s
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|renameColumn
argument_list|(
name|db
argument_list|,
name|s
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|renameIndexes
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
DECL|method|renameTables (final ReviewDb db, final JdbcSchema s, final JdbcExecutor e)
specifier|private
name|void
name|renameTables
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|JdbcSchema
name|s
parameter_list|,
specifier|final
name|JdbcExecutor
name|e
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
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
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|existingTables
init|=
name|dialect
operator|.
name|listTables
argument_list|(
name|s
operator|.
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|tables
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// Does source table exist?
if|if
condition|(
name|existingTables
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
comment|// Does target table exist?
if|if
condition|(
operator|!
name|existingTables
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|s
operator|.
name|renameTable
argument_list|(
name|e
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|renameColumn (final ReviewDb db, final JdbcSchema s, final JdbcExecutor e)
specifier|private
name|void
name|renameColumn
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|JdbcSchema
name|s
parameter_list|,
specifier|final
name|JdbcExecutor
name|e
parameter_list|)
throws|throws
name|SQLException
throws|,
name|OrmException
block|{
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
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|existingColumns
init|=
name|dialect
operator|.
name|listColumns
argument_list|(
name|s
operator|.
name|getConnection
argument_list|()
argument_list|,
literal|"accounts"
argument_list|)
decl_stmt|;
comment|// Does source column exist?
if|if
condition|(
operator|!
name|existingColumns
operator|.
name|contains
argument_list|(
literal|"show_username_in_review_category"
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// Does target column exist?
if|if
condition|(
name|existingColumns
operator|.
name|contains
argument_list|(
literal|"show_user_in_review"
argument_list|)
condition|)
block|{
return|return;
block|}
name|s
operator|.
name|renameColumn
argument_list|(
name|e
argument_list|,
literal|"accounts"
argument_list|,
literal|"show_username_in_review_category"
argument_list|,
literal|"show_user_in_review"
argument_list|)
expr_stmt|;
comment|// MySQL loose check constraint during the column renaming.
comment|// Well it doesn't implemented anyway,
comment|// check constraints are get parsed but do nothing
if|if
condition|(
name|dialect
operator|instanceof
name|DialectMySQL
condition|)
block|{
try|try
init|(
name|Statement
name|stmt
init|=
name|newStatement
argument_list|(
name|db
argument_list|)
init|)
block|{
name|addCheckConstraint
argument_list|(
name|stmt
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|renameIndexes (ReviewDb db)
specifier|private
name|void
name|renameIndexes
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
block|{
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
comment|// Use a new executor so we can ignore errors.
try|try
init|(
name|StatementExecutor
name|e
init|=
name|newExecutor
argument_list|(
name|db
argument_list|)
init|)
block|{
comment|// MySQL doesn't have alter index stmt, drop& create
if|if
condition|(
name|dialect
operator|instanceof
name|DialectMySQL
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Index
argument_list|>
name|entry
range|:
name|indexes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|dialect
operator|.
name|dropIndex
argument_list|(
name|e
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|table
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|execute
argument_list|(
literal|"CREATE INDEX account_project_watches_byP ON "
operator|+
literal|"account_project_watches (project_name)"
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
literal|"CREATE INDEX patch_set_approvals_closedByU ON "
operator|+
literal|"patch_set_approvals (change_open, account_id, change_sort_key)"
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
literal|"CREATE INDEX submodule_subscr_acc_bys ON "
operator|+
literal|"submodule_subscriptions (submodule_project_name, "
operator|+
literal|"submodule_branch_name)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Index
argument_list|>
name|entry
range|:
name|indexes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|e
operator|.
name|execute
argument_list|(
literal|"ALTER INDEX "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" RENAME TO "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|index
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
comment|// We don't care; better, we could check if index was already renamed, but
comment|// gwtorm didn't expose this functionality at the time this schema upgrade
comment|// was written.
block|}
block|}
DECL|method|addCheckConstraint (Statement stmt)
specifier|private
name|void
name|addCheckConstraint
parameter_list|(
name|Statement
name|stmt
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// add check constraint for the destination column
name|stmt
operator|.
name|executeUpdate
argument_list|(
literal|"ALTER TABLE accounts ADD CONSTRAINT "
operator|+
literal|"show_user_in_review_check CHECK "
operator|+
literal|"(show_user_in_review IN('Y', 'N'))"
argument_list|)
expr_stmt|;
block|}
DECL|class|Index
specifier|static
class|class
name|Index
block|{
DECL|field|table
name|String
name|table
decl_stmt|;
DECL|field|index
name|String
name|index
decl_stmt|;
DECL|method|Index (String tableName, String indexName)
name|Index
parameter_list|(
name|String
name|tableName
parameter_list|,
name|String
name|indexName
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|tableName
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|indexName
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

