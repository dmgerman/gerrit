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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
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
name|client
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
name|server
operator|.
name|config
operator|.
name|SystemConfigProvider
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
name|client
operator|.
name|SchemaFactory
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
name|Database
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
name|SimpleDataSource
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
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_comment
comment|/**  * An in-memory test instance of {@link ReviewDb} database.  *<p>  * Test classes should create one instance of this class for each unique test  * database they want to use. When the tests needing this instance are complete,  * ensure that {@link #drop(TestDatabase)} is called to free the resources so  * the JVM running the unit tests doesn't run out of heap space.  */
end_comment

begin_class
DECL|class|TestDatabase
specifier|public
class|class
name|TestDatabase
implements|implements
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
block|{
DECL|field|dbCnt
specifier|private
specifier|static
name|int
name|dbCnt
decl_stmt|;
DECL|method|newDataSource ()
specifier|private
specifier|static
specifier|synchronized
name|DataSource
name|newDataSource
parameter_list|()
throws|throws
name|SQLException
block|{
specifier|final
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"driver"
argument_list|,
name|org
operator|.
name|h2
operator|.
name|Driver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"url"
argument_list|,
literal|"jdbc:h2:mem:"
operator|+
literal|"Test_"
operator|+
operator|(
operator|++
name|dbCnt
operator|)
argument_list|)
expr_stmt|;
specifier|final
name|DataSource
name|dataSource
init|=
operator|new
name|SimpleDataSource
argument_list|(
name|p
argument_list|)
decl_stmt|;
return|return
name|dataSource
return|;
block|}
comment|/** Drop the database from memory; does nothing if the instance was null. */
DECL|method|drop (final TestDatabase db)
specifier|public
specifier|static
name|void
name|drop
parameter_list|(
specifier|final
name|TestDatabase
name|db
parameter_list|)
block|{
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|drop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|field|openHandle
specifier|private
name|Connection
name|openHandle
decl_stmt|;
DECL|field|database
specifier|private
name|Database
argument_list|<
name|ReviewDb
argument_list|>
name|database
decl_stmt|;
DECL|method|TestDatabase ()
specifier|public
name|TestDatabase
parameter_list|()
throws|throws
name|OrmException
block|{
try|try
block|{
specifier|final
name|DataSource
name|dataSource
init|=
name|newDataSource
argument_list|()
decl_stmt|;
comment|// Open one connection. This will peg the database into memory
comment|// until someone calls drop on us, allowing subsequent connections
comment|// opened against the same URL to go to the same set of tables.
comment|//
name|openHandle
operator|=
name|dataSource
operator|.
name|getConnection
argument_list|()
expr_stmt|;
comment|// Build the access layer around the connection factory.
comment|//
name|database
operator|=
operator|new
name|Database
argument_list|<
name|ReviewDb
argument_list|>
argument_list|(
name|dataSource
argument_list|,
name|ReviewDb
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getDatabase ()
specifier|public
name|Database
argument_list|<
name|ReviewDb
argument_list|>
name|getDatabase
parameter_list|()
block|{
return|return
name|database
return|;
block|}
annotation|@
name|Override
DECL|method|open ()
specifier|public
name|ReviewDb
name|open
parameter_list|()
throws|throws
name|OrmException
block|{
return|return
name|getDatabase
argument_list|()
operator|.
name|open
argument_list|()
return|;
block|}
comment|/** Ensure the database schema has been created and initialized. */
DECL|method|create ()
specifier|public
name|TestDatabase
name|create
parameter_list|()
block|{
operator|new
name|SystemConfigProvider
argument_list|(
name|this
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Drop this database from memory so it no longer exists. */
DECL|method|drop ()
specifier|public
name|void
name|drop
parameter_list|()
block|{
if|if
condition|(
name|openHandle
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|openHandle
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"WARNING: Cannot close database connection"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
name|openHandle
operator|=
literal|null
expr_stmt|;
name|database
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

