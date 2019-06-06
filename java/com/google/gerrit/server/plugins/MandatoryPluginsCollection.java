begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
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
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
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
annotation|@
name|Singleton
DECL|class|MandatoryPluginsCollection
specifier|public
class|class
name|MandatoryPluginsCollection
block|{
DECL|field|members
specifier|private
specifier|final
name|CopyOnWriteArraySet
argument_list|<
name|String
argument_list|>
name|members
decl_stmt|;
annotation|@
name|Inject
DECL|method|MandatoryPluginsCollection (@erritServerConfig Config cfg)
name|MandatoryPluginsCollection
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|members
operator|=
name|Sets
operator|.
name|newCopyOnWriteArraySet
argument_list|()
expr_stmt|;
name|members
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"plugins"
argument_list|,
literal|null
argument_list|,
literal|"mandatory"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|contains (String name)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|members
operator|.
name|contains
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|asSet ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|asSet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|members
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|add (String name)
specifier|public
name|void
name|add
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|members
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

