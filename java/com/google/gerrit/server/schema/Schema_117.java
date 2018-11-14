begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|Connection
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
name|Set
import|;
end_import

begin_class
DECL|class|Schema_117
specifier|public
class|class
name|Schema_117
extends|extends
name|ReviewDbSchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_117 (Provider<Schema_116> prior)
name|Schema_117
parameter_list|(
name|Provider
argument_list|<
name|Schema_116
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
name|schema
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
name|Connection
name|connection
init|=
name|schema
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|String
name|tableName
init|=
literal|"patch_sets"
decl_stmt|;
name|String
name|oldColumnName
init|=
literal|"push_certficate"
decl_stmt|;
name|String
name|newColumnName
init|=
literal|"push_certificate"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|columns
init|=
name|schema
operator|.
name|getDialect
argument_list|()
operator|.
name|listColumns
argument_list|(
name|connection
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
if|if
condition|(
name|columns
operator|.
name|contains
argument_list|(
name|oldColumnName
argument_list|)
condition|)
block|{
name|renameColumn
argument_list|(
name|db
argument_list|,
name|tableName
argument_list|,
name|oldColumnName
argument_list|,
name|newColumnName
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|Statement
name|stmt
init|=
name|schema
operator|.
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|stmt
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE "
operator|+
name|tableName
operator|+
literal|" MODIFY "
operator|+
name|newColumnName
operator|+
literal|" clob"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// Ignore.  Type may have already been modified manually.
block|}
block|}
block|}
end_class

end_unit

