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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
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
name|AccountExternalId
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
name|reviewdb
operator|.
name|AccountExternalId
operator|.
name|Key
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
name|DialectH2
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

begin_class
DECL|class|Schema_22
class|class
name|Schema_22
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_22 (Provider<Schema_21> prior)
name|Schema_22
parameter_list|(
name|Provider
argument_list|<
name|Schema_21
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
DECL|method|migrateData (ReviewDb db)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
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
name|ResultSet
name|results
init|=
name|s
operator|.
name|executeQuery
argument_list|(
comment|//
literal|"SELECT account_id, ssh_user_name"
operator|+
literal|" FROM accounts"
comment|//
operator|+
literal|" WHERE ssh_user_name IS NOT NULL"
operator|+
literal|" AND ssh_user_name<> ''"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountExternalId
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|results
operator|.
name|next
argument_list|()
condition|)
block|{
specifier|final
name|int
name|accountId
init|=
name|results
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|String
name|userName
init|=
name|results
operator|.
name|getString
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|account
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
specifier|final
name|AccountExternalId
operator|.
name|Key
name|key
init|=
name|toKey
argument_list|(
name|userName
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
operator|new
name|AccountExternalId
argument_list|(
name|account
argument_list|,
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|ids
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getDialect
argument_list|()
operator|instanceof
name|DialectH2
condition|)
block|{
name|s
operator|.
name|execute
argument_list|(
literal|"ALTER TABLE accounts DROP CONSTRAINT"
operator|+
literal|" IF EXISTS CONSTRAINT_AF"
argument_list|)
expr_stmt|;
block|}
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
DECL|method|toKey (final String userName)
specifier|private
name|Key
name|toKey
parameter_list|(
specifier|final
name|String
name|userName
parameter_list|)
block|{
return|return
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|SCHEME_USERNAME
argument_list|,
name|userName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

