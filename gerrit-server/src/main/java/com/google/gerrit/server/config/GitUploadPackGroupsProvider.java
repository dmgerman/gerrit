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
comment|// limitations under the License
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
DECL|class|GitUploadPackGroupsProvider
specifier|public
class|class
name|GitUploadPackGroupsProvider
extends|extends
name|GroupSetProvider
block|{
annotation|@
name|Inject
DECL|method|GitUploadPackGroupsProvider (@erritServerConfig Config config, SchemaFactory<ReviewDb> db)
specifier|public
name|GitUploadPackGroupsProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
block|{
name|super
argument_list|(
name|config
argument_list|,
name|db
argument_list|,
literal|"upload"
argument_list|,
literal|null
argument_list|,
literal|"allowGroup"
argument_list|)
expr_stmt|;
comment|// If no group was set, default to "registered users" and "anonymous"
comment|//
if|if
condition|(
name|groupIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|all
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|()
decl_stmt|;
name|all
operator|.
name|add
argument_list|(
name|AccountGroup
operator|.
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|all
operator|.
name|add
argument_list|(
name|AccountGroup
operator|.
name|ANONYMOUS_USERS
argument_list|)
expr_stmt|;
name|groupIds
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|all
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

