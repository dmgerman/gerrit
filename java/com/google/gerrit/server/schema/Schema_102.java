begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
DECL|class|Schema_102
specifier|public
class|class
name|Schema_102
extends|extends
name|ReviewDbSchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_102 (Provider<Schema_101> prior)
name|Schema_102
parameter_list|(
name|Provider
argument_list|<
name|Schema_101
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
name|JdbcSchema
name|schema
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
name|SqlDialect
name|dialect
init|=
name|schema
operator|.
name|getDialect
argument_list|()
decl_stmt|;
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
comment|// Drop left over indexes that were missed to be removed in schema 84.
comment|// See "Delete SQL index support" commit for more details:
comment|// d4ae3a16d5e1464574bd04f429a63eb9c02b3b43
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^changes_(allOpen|allClosed|byBranchClosed)$"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
name|String
name|table
init|=
literal|"changes"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|listIndexes
init|=
name|dialect
operator|.
name|listIndexes
argument_list|(
name|schema
operator|.
name|getConnection
argument_list|()
argument_list|,
name|table
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|index
range|:
name|listIndexes
control|)
block|{
if|if
condition|(
name|pattern
operator|.
name|matcher
argument_list|(
name|index
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|dialect
operator|.
name|dropIndex
argument_list|(
name|e
argument_list|,
name|table
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
name|dialect
operator|.
name|dropIndex
argument_list|(
name|e
argument_list|,
name|table
argument_list|,
literal|"changes_byProjectOpen"
argument_list|)
expr_stmt|;
if|if
condition|(
name|dialect
operator|instanceof
name|DialectPostgreSQL
condition|)
block|{
name|e
operator|.
name|execute
argument_list|(
literal|"CREATE INDEX changes_byProjectOpen"
operator|+
literal|" ON "
operator|+
name|table
operator|+
literal|" (dest_project_name, last_updated_on)"
operator|+
literal|" WHERE open = 'Y'"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|e
operator|.
name|execute
argument_list|(
literal|"CREATE INDEX changes_byProjectOpen"
operator|+
literal|" ON "
operator|+
name|table
operator|+
literal|" (open, dest_project_name, last_updated_on)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

