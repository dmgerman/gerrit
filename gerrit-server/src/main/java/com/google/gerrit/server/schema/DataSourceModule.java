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
name|name
operator|.
name|Names
import|;
end_import

begin_class
DECL|class|DataSourceModule
specifier|public
class|class
name|DataSourceModule
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
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"db2"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|DB2
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"h2"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|H2
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"jdbc"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|JDBC
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"mysql"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MySql
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"oracle"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|Oracle
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"postgresql"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|PostgreSQL
operator|.
name|class
argument_list|)
expr_stmt|;
comment|/*      * DatabaseMetaData.getDatabaseProductName() returns "sap db" for MaxDB.      * For auto-detection of the DB type (com.google.gerrit.pgm.util.SiteProgram#getDbType)      * we have to map "sap db" additionally to "maxdb", which is used for explicit configuration.      */
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"maxdb"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MaxDb
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Names
operator|.
name|named
argument_list|(
literal|"sap db"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MaxDb
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

