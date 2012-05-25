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
name|sql
operator|.
name|Statement
import|;
end_import

begin_class
DECL|class|Schema_68
specifier|public
class|class
name|Schema_68
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_68 (Provider<Schema_67> prior)
name|Schema_68
parameter_list|(
name|Provider
argument_list|<
name|Schema_67
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
DECL|method|migrateData (final ReviewDb db, final UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
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
name|stmt
operator|.
name|execute
argument_list|(
literal|"CREATE INDEX submodule_subscription_access_bySubscription"
operator|+
literal|" ON submodule_subscriptions (submodule_project_name, submodule_branch_name)"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// the index creation might have failed because the index exists already,
comment|// in this case the exception can be safely ignored,
comment|// but there are also other possible reasons for an exception here that
comment|// should not be ignored,
comment|// -> ask the user whether to ignore this exception or not
name|ui
operator|.
name|message
argument_list|(
literal|"warning: Cannot create index for submodule subscriptions"
argument_list|)
expr_stmt|;
name|ui
operator|.
name|message
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ui
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"you may ignore this warning when running in interactive mode"
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
else|else
block|{
specifier|final
name|boolean
name|answer
init|=
name|ui
operator|.
name|yesno
argument_list|(
literal|false
argument_list|,
literal|"Ignore warning and proceed with schema upgrade"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|answer
condition|)
block|{
throw|throw
name|e
throw|;
block|}
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
block|}
block|}
end_class

end_unit

