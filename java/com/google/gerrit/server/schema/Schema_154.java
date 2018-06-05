begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toMap
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
name|ImmutableMap
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
name|flogger
operator|.
name|FluentLogger
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
name|GerritPersonIdent
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
name|account
operator|.
name|AccountConfig
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
name|AllUsersName
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|git
operator|.
name|GitRepositoryManager
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
name|git
operator|.
name|meta
operator|.
name|MetaDataUpdate
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
name|HashSet
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
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
name|ProgressMonitor
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
name|Repository
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
name|TextProgressMonitor
import|;
end_import

begin_comment
comment|/** Migrate accounts to NoteDb. */
end_comment

begin_class
DECL|class|Schema_154
specifier|public
class|class
name|Schema_154
extends|extends
name|SchemaVersion
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|TABLE
specifier|private
specifier|static
specifier|final
name|String
name|TABLE
init|=
literal|"accounts"
decl_stmt|;
DECL|field|ACCOUNT_FIELDS_MAP
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|AccountSetter
argument_list|>
name|ACCOUNT_FIELDS_MAP
init|=
name|ImmutableMap
operator|.
expr|<
name|String
decl_stmt|,
name|AccountSetter
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
literal|"full_name"
argument_list|,
parameter_list|(
name|a
parameter_list|,
name|rs
parameter_list|,
name|field
parameter_list|)
lambda|->
name|a
operator|.
name|setFullName
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|field
argument_list|)
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
literal|"preferred_email"
argument_list|,
parameter_list|(
name|a
parameter_list|,
name|rs
parameter_list|,
name|field
parameter_list|)
lambda|->
name|a
operator|.
name|setPreferredEmail
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|field
argument_list|)
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
literal|"status"
argument_list|,
parameter_list|(
name|a
parameter_list|,
name|rs
parameter_list|,
name|field
parameter_list|)
lambda|->
name|a
operator|.
name|setStatus
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|field
argument_list|)
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
literal|"inactive"
argument_list|,
parameter_list|(
name|a
parameter_list|,
name|rs
parameter_list|,
name|field
parameter_list|)
lambda|->
name|a
operator|.
name|setActive
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|field
argument_list|)
operator|.
name|equals
argument_list|(
literal|"N"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_154 ( Provider<Schema_153> prior, GitRepositoryManager repoManager, AllUsersName allUsersName, @GerritPersonIdent Provider<PersonIdent> serverIdent)
name|Schema_154
parameter_list|(
name|Provider
argument_list|<
name|Schema_153
argument_list|>
name|prior
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
annotation|@
name|GerritPersonIdent
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
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
try|try
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|ProgressMonitor
name|pm
init|=
operator|new
name|TextProgressMonitor
argument_list|()
decl_stmt|;
name|pm
operator|.
name|beginTask
argument_list|(
literal|"Collecting accounts"
argument_list|,
name|ProgressMonitor
operator|.
name|UNKNOWN
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Account
argument_list|>
name|accounts
init|=
name|scanAccounts
argument_list|(
name|db
argument_list|,
name|pm
argument_list|)
decl_stmt|;
name|pm
operator|.
name|endTask
argument_list|()
expr_stmt|;
name|pm
operator|.
name|beginTask
argument_list|(
literal|"Migrating accounts to NoteDb"
argument_list|,
name|accounts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Account
name|account
range|:
name|accounts
control|)
block|{
name|updateAccountInNoteDb
argument_list|(
name|repo
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|pm
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|pm
operator|.
name|endTask
argument_list|()
expr_stmt|;
block|}
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
literal|"Migrating accounts to NoteDb failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|scanAccounts (ReviewDb db, ProgressMonitor pm)
specifier|private
name|Set
argument_list|<
name|Account
argument_list|>
name|scanAccounts
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ProgressMonitor
name|pm
parameter_list|)
throws|throws
name|SQLException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|AccountSetter
argument_list|>
name|fields
init|=
name|getFields
argument_list|(
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Only account_id and registered_on fields are migrated for accounts"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|queryFields
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|queryFields
operator|.
name|add
argument_list|(
literal|"account_id"
argument_list|)
expr_stmt|;
name|queryFields
operator|.
name|add
argument_list|(
literal|"registered_on"
argument_list|)
expr_stmt|;
name|queryFields
operator|.
name|addAll
argument_list|(
name|fields
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|query
init|=
literal|"SELECT "
operator|+
name|String
operator|.
name|join
argument_list|(
literal|", "
argument_list|,
name|queryFields
argument_list|)
operator|+
name|String
operator|.
name|format
argument_list|(
literal|" FROM %s"
argument_list|,
name|TABLE
argument_list|)
decl_stmt|;
try|try
init|(
name|Statement
name|stmt
init|=
name|newStatement
argument_list|(
name|db
argument_list|)
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
name|query
argument_list|)
init|)
block|{
name|Set
argument_list|<
name|Account
argument_list|>
name|s
init|=
operator|new
name|HashSet
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
name|Account
name|a
init|=
operator|new
name|Account
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
name|rs
operator|.
name|getTimestamp
argument_list|(
literal|2
argument_list|)
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
name|AccountSetter
argument_list|>
name|field
range|:
name|fields
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|field
operator|.
name|getValue
argument_list|()
operator|.
name|set
argument_list|(
name|a
argument_list|,
name|rs
argument_list|,
name|field
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|s
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|pm
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
block|}
DECL|method|getFields (ReviewDb db)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AccountSetter
argument_list|>
name|getFields
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
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
name|TABLE
argument_list|)
decl_stmt|;
return|return
name|ACCOUNT_FIELDS_MAP
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|columns
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getKey
argument_list|,
name|Map
operator|.
name|Entry
operator|::
name|getValue
argument_list|)
argument_list|)
return|;
block|}
DECL|method|updateAccountInNoteDb (Repository allUsersRepo, Account account)
specifier|private
name|void
name|updateAccountInNoteDb
parameter_list|(
name|Repository
name|allUsersRepo
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allUsersName
argument_list|,
name|allUsersRepo
argument_list|)
decl_stmt|;
name|PersonIdent
name|ident
init|=
name|serverIdent
operator|.
name|get
argument_list|()
decl_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|ident
argument_list|)
expr_stmt|;
operator|new
name|AccountConfig
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|,
name|allUsersRepo
argument_list|)
operator|.
name|load
argument_list|()
operator|.
name|setAccount
argument_list|(
name|account
argument_list|)
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|AccountSetter
specifier|private
interface|interface
name|AccountSetter
block|{
DECL|method|set (Account a, ResultSet rs, String field)
name|void
name|set
parameter_list|(
name|Account
name|a
parameter_list|,
name|ResultSet
name|rs
parameter_list|,
name|String
name|field
parameter_list|)
throws|throws
name|SQLException
function_decl|;
block|}
block|}
end_class

end_unit

