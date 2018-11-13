begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|ImmutableSet
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
name|collect
operator|.
name|Sets
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
name|HashMap
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
DECL|class|Schema_105
specifier|public
class|class
name|Schema_105
extends|extends
name|ReviewDbSchemaVersion
block|{
DECL|field|TABLE
specifier|private
specifier|static
specifier|final
name|String
name|TABLE
init|=
literal|"changes"
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_105 (Provider<Schema_104> prior)
name|Schema_105
parameter_list|(
name|Provider
argument_list|<
name|Schema_104
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
name|Map
argument_list|<
name|String
argument_list|,
name|OrmException
argument_list|>
name|errors
init|=
operator|new
name|HashMap
argument_list|<>
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
for|for
control|(
name|String
name|index
range|:
name|listChangesIndexes
argument_list|(
name|schema
argument_list|)
control|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Dropping index "
operator|+
name|index
operator|+
literal|" on table "
operator|+
name|TABLE
argument_list|)
expr_stmt|;
try|try
block|{
name|dialect
operator|.
name|dropIndex
argument_list|(
name|e
argument_list|,
name|TABLE
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
name|errors
operator|.
name|put
argument_list|(
name|index
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|String
name|index
range|:
name|listChangesIndexes
argument_list|(
name|schema
argument_list|)
control|)
block|{
name|String
name|msg
init|=
literal|"Failed to drop index "
operator|+
name|index
decl_stmt|;
name|OrmException
name|err
init|=
name|errors
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|err
operator|!=
literal|null
condition|)
block|{
name|msg
operator|+=
literal|": "
operator|+
name|err
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
name|ui
operator|.
name|message
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|listChangesIndexes (JdbcSchema schema)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|listChangesIndexes
parameter_list|(
name|JdbcSchema
name|schema
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// List of all changes indexes ever created or dropped, found with the
comment|// following command:
comment|//   find g* -name \*.sql | xargs git log -i -p -S' index changes_' | grep -io ' index
comment|// changes_\w*' | cut -d' ' -f3 | tr A-Z a-z | sort -u
comment|// Used rather than listIndexes as we're not sure whether it might include
comment|// primary key indexes.
name|Set
argument_list|<
name|String
argument_list|>
name|allChanges
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"changes_allclosed"
argument_list|,
literal|"changes_allopen"
argument_list|,
literal|"changes_bybranchclosed"
argument_list|,
literal|"changes_byownerclosed"
argument_list|,
literal|"changes_byowneropen"
argument_list|,
literal|"changes_byproject"
argument_list|,
literal|"changes_byprojectopen"
argument_list|,
literal|"changes_key"
argument_list|,
literal|"changes_submitted"
argument_list|)
decl_stmt|;
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|schema
operator|.
name|getDialect
argument_list|()
operator|.
name|listIndexes
argument_list|(
name|schema
operator|.
name|getConnection
argument_list|()
argument_list|,
name|TABLE
argument_list|)
argument_list|,
name|allChanges
argument_list|)
return|;
block|}
block|}
end_class

end_unit

