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
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
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
name|primitives
operator|.
name|Ints
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
name|extensions
operator|.
name|events
operator|.
name|LifecycleListener
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicItem
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
name|LifecycleModule
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
name|Account
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
name|PatchSet
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
name|change
operator|.
name|AccountPatchReviewStore
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
name|GerritServerConfig
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
name|SitePaths
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
name|OrmDuplicateKeyException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|DriverManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|H2AccountPatchReviewStore
specifier|public
class|class
name|H2AccountPatchReviewStore
implements|implements
name|AccountPatchReviewStore
implements|,
name|LifecycleListener
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|H2AccountPatchReviewStore
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicItem
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|AccountPatchReviewStore
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|H2AccountPatchReviewStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|H2AccountPatchReviewStore
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|VisibleForTesting
DECL|class|InMemoryModule
specifier|public
specifier|static
class|class
name|InMemoryModule
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|H2AccountPatchReviewStore
name|inMemoryStore
init|=
operator|new
name|H2AccountPatchReviewStore
argument_list|()
decl_stmt|;
name|DynamicItem
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|AccountPatchReviewStore
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|inMemoryStore
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|toInstance
argument_list|(
name|inMemoryStore
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
annotation|@
name|Inject
DECL|method|H2AccountPatchReviewStore (@erritServerConfig Config cfg, SitePaths sitePaths)
name|H2AccountPatchReviewStore
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|H2
operator|.
name|appendUrlOptions
argument_list|(
name|cfg
argument_list|,
name|getUrl
argument_list|(
name|sitePaths
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getUrl (SitePaths sitePaths)
specifier|public
specifier|static
name|String
name|getUrl
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|)
block|{
return|return
name|H2
operator|.
name|createUrl
argument_list|(
name|sitePaths
operator|.
name|db_dir
operator|.
name|resolve
argument_list|(
literal|"account_patch_reviews"
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Creates an in-memory H2 database to store the reviewed flags.    * This should be used for tests only.    */
annotation|@
name|VisibleForTesting
DECL|method|H2AccountPatchReviewStore ()
specifier|private
name|H2AccountPatchReviewStore
parameter_list|()
block|{
comment|// DB_CLOSE_DELAY=-1: By default the content of an in-memory H2 database is
comment|// lost at the moment the last connection is closed. This option keeps the
comment|// content as long as the vm lives.
name|this
operator|.
name|url
operator|=
literal|"jdbc:h2:mem:account_patch_reviews;DB_CLOSE_DELAY=-1"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
try|try
block|{
name|createTableIfNotExists
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to create table to store account patch reviews"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createTableIfNotExists (String url)
specifier|public
specifier|static
name|void
name|createTableIfNotExists
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
init|(
name|Connection
name|con
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|)
init|;
name|Statement
name|stmt
operator|=
name|con
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|stmt
operator|.
name|executeUpdate
argument_list|(
literal|"CREATE TABLE IF NOT EXISTS ACCOUNT_PATCH_REVIEWS ("
operator|+
literal|"ACCOUNT_ID INTEGER DEFAULT 0 NOT NULL, "
operator|+
literal|"CHANGE_ID INTEGER DEFAULT 0 NOT NULL, "
operator|+
literal|"PATCH_SET_ID INTEGER DEFAULT 0 NOT NULL, "
operator|+
literal|"FILE_NAME VARCHAR(255) DEFAULT '' NOT NULL, "
operator|+
literal|"CONSTRAINT PRIMARY_KEY_ACCOUNT_PATCH_REVIEWS "
operator|+
literal|"PRIMARY KEY (ACCOUNT_ID, CHANGE_ID, PATCH_SET_ID, FILE_NAME)"
operator|+
literal|")"
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
name|convertError
argument_list|(
literal|"create"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|dropTableIfExists (String url)
specifier|public
specifier|static
name|void
name|dropTableIfExists
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
init|(
name|Connection
name|con
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|)
init|;
name|Statement
name|stmt
operator|=
name|con
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|stmt
operator|.
name|executeUpdate
argument_list|(
literal|"DROP TABLE IF EXISTS ACCOUNT_PATCH_REVIEWS"
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
name|convertError
argument_list|(
literal|"create"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{   }
annotation|@
name|Override
DECL|method|markReviewed (PatchSet.Id psId, Account.Id accountId, String path)
specifier|public
name|boolean
name|markReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
init|(
name|Connection
name|con
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|)
init|;
name|PreparedStatement
name|stmt
operator|=
name|con
operator|.
name|prepareStatement
argument_list|(
literal|"INSERT INTO ACCOUNT_PATCH_REVIEWS "
operator|+
literal|"(ACCOUNT_ID, CHANGE_ID, PATCH_SET_ID, FILE_NAME) VALUES "
operator|+
literal|"(?, ?, ?, ?)"
argument_list|)
init|)
block|{
name|stmt
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|3
argument_list|,
name|psId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setString
argument_list|(
literal|4
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|OrmException
name|ormException
init|=
name|convertError
argument_list|(
literal|"insert"
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|ormException
operator|instanceof
name|OrmDuplicateKeyException
condition|)
block|{
return|return
literal|false
return|;
block|}
throw|throw
name|ormException
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|markReviewed (PatchSet.Id psId, Account.Id accountId, Collection<String> paths)
specifier|public
name|void
name|markReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|paths
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|paths
operator|==
literal|null
operator|||
name|paths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
init|(
name|Connection
name|con
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|)
init|;
name|PreparedStatement
name|stmt
operator|=
name|con
operator|.
name|prepareStatement
argument_list|(
literal|"INSERT INTO ACCOUNT_PATCH_REVIEWS "
operator|+
literal|"(ACCOUNT_ID, CHANGE_ID, PATCH_SET_ID, FILE_NAME) VALUES "
operator|+
literal|"(?, ?, ?, ?)"
argument_list|)
init|)
block|{
for|for
control|(
name|String
name|path
range|:
name|paths
control|)
block|{
name|stmt
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|3
argument_list|,
name|psId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setString
argument_list|(
literal|4
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|addBatch
argument_list|()
expr_stmt|;
block|}
name|stmt
operator|.
name|executeBatch
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|OrmException
name|ormException
init|=
name|convertError
argument_list|(
literal|"insert"
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|ormException
operator|instanceof
name|OrmDuplicateKeyException
condition|)
block|{
return|return;
block|}
throw|throw
name|ormException
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|clearReviewed (PatchSet.Id psId, Account.Id accountId, String path)
specifier|public
name|void
name|clearReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
init|(
name|Connection
name|con
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|)
init|;
name|PreparedStatement
name|stmt
operator|=
name|con
operator|.
name|prepareStatement
argument_list|(
literal|"DELETE FROM ACCOUNT_PATCH_REVIEWS "
operator|+
literal|"WHERE ACCOUNT_ID = ? AND CHANGE_ID + ? AND "
operator|+
literal|"PATCH_SET_ID = ? AND FILE_NAME = ?"
argument_list|)
init|)
block|{
name|stmt
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|3
argument_list|,
name|psId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setString
argument_list|(
literal|4
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
name|convertError
argument_list|(
literal|"delete"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|clearReviewed (PatchSet.Id psId)
specifier|public
name|void
name|clearReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
init|(
name|Connection
name|con
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|)
init|;
name|PreparedStatement
name|stmt
operator|=
name|con
operator|.
name|prepareStatement
argument_list|(
literal|"DELETE FROM ACCOUNT_PATCH_REVIEWS "
operator|+
literal|"WHERE CHANGE_ID + ? AND PATCH_SET_ID = ?"
argument_list|)
init|)
block|{
name|stmt
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|psId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
name|convertError
argument_list|(
literal|"delete"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|findReviewed (PatchSet.Id psId, Account.Id accountId)
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|findReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
init|(
name|Connection
name|con
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|)
init|;
name|PreparedStatement
name|stmt
operator|=
name|con
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT FILE_NAME FROM ACCOUNT_PATCH_REVIEWS "
operator|+
literal|"WHERE ACCOUNT_ID = ? AND CHANGE_ID = ? AND PATCH_SET_ID = ?"
argument_list|)
init|)
block|{
name|stmt
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|3
argument_list|,
name|psId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|ResultSet
name|rs
init|=
name|stmt
operator|.
name|executeQuery
argument_list|()
init|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|files
operator|.
name|add
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"FILE_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|files
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
name|convertError
argument_list|(
literal|"select"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|convertError (String op, SQLException err)
specifier|public
specifier|static
name|OrmException
name|convertError
parameter_list|(
name|String
name|op
parameter_list|,
name|SQLException
name|err
parameter_list|)
block|{
switch|switch
condition|(
name|getSQLStateInt
argument_list|(
name|err
argument_list|)
condition|)
block|{
case|case
literal|23001
case|:
comment|// UNIQUE CONSTRAINT VIOLATION
case|case
literal|23505
case|:
comment|// DUPLICATE_KEY_1
return|return
operator|new
name|OrmDuplicateKeyException
argument_list|(
literal|"ACCOUNT_PATCH_REVIEWS"
argument_list|,
name|err
argument_list|)
return|;
default|default:
if|if
condition|(
name|err
operator|.
name|getCause
argument_list|()
operator|==
literal|null
operator|&&
name|err
operator|.
name|getNextException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|err
operator|.
name|initCause
argument_list|(
name|err
operator|.
name|getNextException
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|OrmException
argument_list|(
name|op
operator|+
literal|" failure on ACCOUNT_PATCH_REVIEWS"
argument_list|,
name|err
argument_list|)
return|;
block|}
block|}
DECL|method|getSQLState (SQLException err)
specifier|private
specifier|static
name|String
name|getSQLState
parameter_list|(
name|SQLException
name|err
parameter_list|)
block|{
name|String
name|ec
decl_stmt|;
name|SQLException
name|next
init|=
name|err
decl_stmt|;
do|do
block|{
name|ec
operator|=
name|next
operator|.
name|getSQLState
argument_list|()
expr_stmt|;
name|next
operator|=
name|next
operator|.
name|getNextException
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|ec
operator|==
literal|null
operator|&&
name|next
operator|!=
literal|null
condition|)
do|;
return|return
name|ec
return|;
block|}
DECL|method|getSQLStateInt (SQLException err)
specifier|private
specifier|static
name|int
name|getSQLStateInt
parameter_list|(
name|SQLException
name|err
parameter_list|)
block|{
name|String
name|s
init|=
name|getSQLState
argument_list|(
name|err
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|Integer
name|i
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|s
argument_list|)
decl_stmt|;
return|return
name|i
operator|!=
literal|null
condition|?
name|i
else|:
operator|-
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

