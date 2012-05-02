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
name|ImmutableSet
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
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|account
operator|.
name|GroupBackends
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
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|GroupSetProvider
specifier|public
specifier|abstract
class|class
name|GroupSetProvider
implements|implements
name|Provider
argument_list|<
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
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
name|GroupSetProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|groupIds
specifier|protected
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupSetProvider (GroupBackend groupBackend, @GerritServerConfig Config config, String section, String subsection, String name)
specifier|protected
name|GroupSetProvider
parameter_list|(
name|GroupBackend
name|groupBackend
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|groupNames
init|=
name|config
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|n
range|:
name|groupNames
control|)
block|{
name|GroupReference
name|g
init|=
name|GroupBackends
operator|.
name|findBestSuggestion
argument_list|(
name|groupBackend
argument_list|,
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Group \"{0}\" not in database, skipping."
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|add
argument_list|(
name|g
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|groupIds
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|groupIds
return|;
block|}
block|}
end_class

end_unit

