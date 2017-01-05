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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|lifecycle
operator|.
name|LifecycleManager
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
name|client
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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|group
operator|.
name|GroupIndexCollection
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
name|schema
operator|.
name|SchemaCreator
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
name|schema
operator|.
name|SchemaVersion
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
name|SchemaFactory
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
name|Guice
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
name|Injector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
comment|/**  * An in-memory test instance of {@link ReviewDb} database.  *<p>  * Test classes should create one instance of this class for each unique test  * database they want to use. When the tests needing this instance are complete,  * ensure that {@link #drop(InMemoryDatabase)} is called to free the resources so  * the JVM running the unit tests doesn't run out of heap space.  */
end_comment

begin_class
DECL|class|InMemoryDatabase
specifier|public
class|class
name|InMemoryDatabase
implements|implements
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
block|{
DECL|method|newDatabase (LifecycleManager lifecycle)
specifier|public
specifier|static
name|InMemoryDatabase
name|newDatabase
parameter_list|(
name|LifecycleManager
name|lifecycle
parameter_list|)
block|{
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|InMemoryModule
argument_list|()
argument_list|)
decl_stmt|;
name|lifecycle
operator|.
name|add
argument_list|(
name|injector
argument_list|)
expr_stmt|;
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|InMemoryDatabase
operator|.
name|class
argument_list|)
return|;
block|}
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
return|return
operator|new
name|SimpleDataSource
argument_list|(
name|p
argument_list|)
return|;
block|}
comment|/** Drop the database from memory; does nothing if the instance was null. */
DECL|method|drop (final InMemoryDatabase db)
specifier|public
specifier|static
name|void
name|drop
parameter_list|(
specifier|final
name|InMemoryDatabase
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
DECL|field|schemaCreator
specifier|private
specifier|final
name|SchemaCreator
name|schemaCreator
decl_stmt|;
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
DECL|field|created
specifier|private
name|boolean
name|created
decl_stmt|;
annotation|@
name|Inject
DECL|method|InMemoryDatabase (Injector injector)
name|InMemoryDatabase
parameter_list|(
name|Injector
name|injector
parameter_list|)
throws|throws
name|OrmException
block|{
comment|// Don't inject SchemaCreator directly.
comment|// SchemaCreator needs to get GroupIndexCollection injected, but
comment|// GroupIndexCollection was not bound yet. Creating a child injector with a
comment|// binding for GroupIndexCollection to create an instance of SchemaCreator
comment|// prevents that Guice creates a just-in-time binding for
comment|// GroupIndexCollection in the root injector. If a binding for
comment|// GroupIndexCollection is created in the root injector then IndexModule
comment|// fails to create this binding later, because it already exists.
name|this
argument_list|(
name|injector
operator|.
name|createChildInjector
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|GroupIndexCollection
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|getInstance
argument_list|(
name|SchemaCreator
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|InMemoryDatabase (SchemaCreator schemaCreator)
name|InMemoryDatabase
parameter_list|(
name|SchemaCreator
name|schemaCreator
parameter_list|)
throws|throws
name|OrmException
block|{
name|this
operator|.
name|schemaCreator
operator|=
name|schemaCreator
expr_stmt|;
try|try
block|{
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
argument_list|<>
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
name|InMemoryDatabase
name|create
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|created
condition|)
block|{
name|created
operator|=
literal|true
expr_stmt|;
try|try
init|(
name|ReviewDb
name|c
init|=
name|open
argument_list|()
init|)
block|{
name|schemaCreator
operator|.
name|create
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot create in-memory database"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
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
DECL|method|getSystemConfig ()
specifier|public
name|SystemConfig
name|getSystemConfig
parameter_list|()
throws|throws
name|OrmException
block|{
try|try
init|(
name|ReviewDb
name|c
init|=
name|open
argument_list|()
init|)
block|{
return|return
name|c
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
return|;
block|}
block|}
DECL|method|getSchemaVersion ()
specifier|public
name|CurrentSchemaVersion
name|getSchemaVersion
parameter_list|()
throws|throws
name|OrmException
block|{
try|try
init|(
name|ReviewDb
name|c
init|=
name|open
argument_list|()
init|)
block|{
return|return
name|c
operator|.
name|schemaVersion
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|CurrentSchemaVersion
operator|.
name|Key
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|assertSchemaVersion ()
specifier|public
name|void
name|assertSchemaVersion
parameter_list|()
throws|throws
name|OrmException
block|{
name|assertThat
argument_list|(
name|getSchemaVersion
argument_list|()
operator|.
name|versionNbr
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SchemaVersion
operator|.
name|getBinaryVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

