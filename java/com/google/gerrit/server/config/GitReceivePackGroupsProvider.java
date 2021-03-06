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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|GroupBackend
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
name|group
operator|.
name|SystemGroupBackend
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
name|util
operator|.
name|ServerRequestContext
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
DECL|class|GitReceivePackGroupsProvider
specifier|public
class|class
name|GitReceivePackGroupsProvider
extends|extends
name|GroupSetProvider
block|{
annotation|@
name|Inject
DECL|method|GitReceivePackGroupsProvider ( GroupBackend gb, @GerritServerConfig Config config, ThreadLocalRequestContext threadContext, ServerRequestContext serverCtx)
specifier|public
name|GitReceivePackGroupsProvider
parameter_list|(
name|GroupBackend
name|gb
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|ThreadLocalRequestContext
name|threadContext
parameter_list|,
name|ServerRequestContext
name|serverCtx
parameter_list|)
block|{
name|super
argument_list|(
name|gb
argument_list|,
name|threadContext
argument_list|,
name|serverCtx
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|config
operator|.
name|getStringList
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"allowGroup"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// If no group was set, default to "registered users"
comment|//
if|if
condition|(
name|groupIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|groupIds
operator|=
name|Collections
operator|.
name|singleton
argument_list|(
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

