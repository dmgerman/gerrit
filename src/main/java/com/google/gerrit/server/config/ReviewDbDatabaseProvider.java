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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|Database
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
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
name|Named
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
comment|/** Provides the {@code Database<ReviewDb>} database handle. */
end_comment

begin_class
DECL|class|ReviewDbDatabaseProvider
specifier|final
class|class
name|ReviewDbDatabaseProvider
implements|implements
name|Provider
argument_list|<
name|Database
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
block|{
DECL|field|datasource
specifier|private
specifier|final
name|DataSource
name|datasource
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReviewDbDatabaseProvider (@amedR) final DataSource ds)
name|ReviewDbDatabaseProvider
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"ReviewDb"
argument_list|)
specifier|final
name|DataSource
name|ds
parameter_list|)
block|{
name|datasource
operator|=
name|ds
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Database
argument_list|<
name|ReviewDb
argument_list|>
name|get
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|Database
argument_list|<
name|ReviewDb
argument_list|>
argument_list|(
name|datasource
argument_list|,
name|ReviewDb
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot create ReviewDb"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

