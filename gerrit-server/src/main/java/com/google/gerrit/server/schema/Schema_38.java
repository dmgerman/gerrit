begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|AccountDiffPreference
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
name|List
import|;
end_import

begin_class
DECL|class|Schema_38
specifier|public
class|class
name|Schema_38
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_38 (Provider<Schema_37> prior)
name|Schema_38
parameter_list|(
name|Provider
argument_list|<
name|Schema_37
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
comment|/**    * Migrate the account.default_context column to account_diff_preferences.context column.    *<p>    * Other fields in account_diff_preferences will be filled in with their defaults as    * defined in the {@link AccountDiffPreference#createDefault(com.google.gerrit.reviewdb.Account.Id)}    */
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
name|List
argument_list|<
name|AccountDiffPreference
argument_list|>
name|newPrefs
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountDiffPreference
argument_list|>
argument_list|()
decl_stmt|;
name|Statement
name|stmt
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
name|result
init|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT account_id, default_context"
operator|+
literal|" FROM accounts WHERE default_context<> 10"
argument_list|)
decl_stmt|;
while|while
condition|(
name|result
operator|.
name|next
argument_list|()
condition|)
block|{
name|int
name|accountId
init|=
name|result
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|short
name|defaultContext
init|=
name|result
operator|.
name|getShort
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|AccountDiffPreference
name|diffPref
init|=
name|AccountDiffPreference
operator|.
name|createDefault
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|accountId
argument_list|)
argument_list|)
decl_stmt|;
name|diffPref
operator|.
name|setContext
argument_list|(
name|defaultContext
argument_list|)
expr_stmt|;
name|newPrefs
operator|.
name|add
argument_list|(
name|diffPref
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|db
operator|.
name|accountDiffPreferences
argument_list|()
operator|.
name|insert
argument_list|(
name|newPrefs
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

