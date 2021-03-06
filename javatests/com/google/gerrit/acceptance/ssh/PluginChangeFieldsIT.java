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
DECL|package|com.google.gerrit.acceptance.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|ssh
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

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
name|joining
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
name|ImmutableListMultimap
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
name|io
operator|.
name|CharStreams
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
name|acceptance
operator|.
name|AbstractPluginFieldsTest
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
name|acceptance
operator|.
name|UseSsh
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
name|Nullable
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
name|entities
operator|.
name|Change
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
name|query
operator|.
name|change
operator|.
name|OutputStreamQuery
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|UseSsh
DECL|class|PluginChangeFieldsIT
specifier|public
class|class
name|PluginChangeFieldsIT
extends|extends
name|AbstractPluginFieldsTest
block|{
comment|// No tests for getting a single change over SSH, since the only API is the query API.
DECL|field|GSON
specifier|private
specifier|static
specifier|final
name|Gson
name|GSON
init|=
name|OutputStreamQuery
operator|.
name|GSON
decl_stmt|;
annotation|@
name|Test
DECL|method|queryChangeWithNullAttribute ()
specifier|public
name|void
name|queryChangeWithNullAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|getChangeWithNullAttribute
argument_list|(
name|id
lambda|->
name|pluginInfoFromSingletonList
argument_list|(
name|adminSshSession
operator|.
name|exec
argument_list|(
name|changeQueryCmd
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|queryChangeWithSimpleAttribute ()
specifier|public
name|void
name|queryChangeWithSimpleAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|getChangeWithSimpleAttribute
argument_list|(
name|id
lambda|->
name|pluginInfoFromSingletonList
argument_list|(
name|adminSshSession
operator|.
name|exec
argument_list|(
name|changeQueryCmd
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|queryChangeWithOption ()
specifier|public
name|void
name|queryChangeWithOption
parameter_list|()
throws|throws
name|Exception
block|{
name|getChangeWithOption
argument_list|(
name|id
lambda|->
name|pluginInfoFromSingletonList
argument_list|(
name|adminSshSession
operator|.
name|exec
argument_list|(
name|changeQueryCmd
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
argument_list|,
parameter_list|(
name|id
parameter_list|,
name|opts
parameter_list|)
lambda|->
name|pluginInfoFromSingletonList
argument_list|(
name|adminSshSession
operator|.
name|exec
argument_list|(
name|changeQueryCmd
argument_list|(
name|id
argument_list|,
name|opts
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|changeQueryCmd (Change.Id id)
specifier|private
name|String
name|changeQueryCmd
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|changeQueryCmd
argument_list|(
name|id
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
DECL|method|changeQueryCmd (Change.Id id, ImmutableListMultimap<String, String> pluginOptions)
specifier|private
name|String
name|changeQueryCmd
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|ImmutableListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pluginOptions
parameter_list|)
block|{
return|return
literal|"gerrit query --format json "
operator|+
name|pluginOptions
operator|.
name|entries
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|e
lambda|->
name|Stream
operator|.
name|of
argument_list|(
literal|"--"
operator|+
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|" "
argument_list|)
argument_list|)
operator|+
literal|" "
operator|+
name|id
return|;
block|}
annotation|@
name|Nullable
DECL|method|pluginInfoFromSingletonList (String sshOutput)
specifier|private
specifier|static
name|List
argument_list|<
name|MyInfo
argument_list|>
name|pluginInfoFromSingletonList
parameter_list|(
name|String
name|sshOutput
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|changeAttrs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|CharStreams
operator|.
name|readLines
argument_list|(
operator|new
name|StringReader
argument_list|(
name|sshOutput
argument_list|)
argument_list|)
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|changeAttr
init|=
name|GSON
operator|.
name|fromJson
argument_list|(
name|line
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"stats"
operator|.
name|equals
argument_list|(
name|changeAttr
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
argument_list|)
condition|)
block|{
name|changeAttrs
operator|.
name|add
argument_list|(
name|changeAttr
argument_list|)
expr_stmt|;
block|}
block|}
name|assertThat
argument_list|(
name|changeAttrs
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|decodeRawPluginsList
argument_list|(
name|GSON
argument_list|,
name|changeAttrs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|"plugins"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

