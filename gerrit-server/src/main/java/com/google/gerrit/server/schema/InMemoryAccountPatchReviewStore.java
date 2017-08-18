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

begin_class
DECL|class|InMemoryAccountPatchReviewStore
specifier|public
class|class
name|InMemoryAccountPatchReviewStore
extends|extends
name|JdbcAccountPatchReviewStore
block|{
annotation|@
name|VisibleForTesting
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
name|InMemoryAccountPatchReviewStore
name|inMemoryStore
init|=
operator|new
name|InMemoryAccountPatchReviewStore
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
comment|/**    * Creates an in-memory H2 database to store the reviewed flags. This should be used for tests    * only.    */
annotation|@
name|VisibleForTesting
DECL|method|InMemoryAccountPatchReviewStore ()
specifier|private
name|InMemoryAccountPatchReviewStore
parameter_list|()
block|{
name|super
argument_list|(
name|newDataSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|newDataSource ()
specifier|private
specifier|static
specifier|synchronized
name|DataSource
name|newDataSource
parameter_list|()
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
literal|"org.h2.Driver"
argument_list|)
expr_stmt|;
comment|// DB_CLOSE_DELAY=-1: By default the content of an in-memory H2 database is lost at the moment
comment|// the last connection is closed. This option keeps the content as long as the vm lives.
name|p
operator|.
name|setProperty
argument_list|(
literal|"url"
argument_list|,
literal|"jdbc:h2:mem:account_patch_reviews;DB_CLOSE_DELAY=-1"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|SimpleDataSource
argument_list|(
name|p
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create test datasource"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

