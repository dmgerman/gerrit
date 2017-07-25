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
DECL|package|com.google.gerrit.server.api.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
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
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|plugins
operator|.
name|Plugins
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
name|common
operator|.
name|PluginInfo
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
name|restapi
operator|.
name|RestApiException
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
name|plugins
operator|.
name|ListPlugins
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PluginsImpl
specifier|public
class|class
name|PluginsImpl
implements|implements
name|Plugins
block|{
DECL|field|listProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListPlugins
argument_list|>
name|listProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|PluginsImpl (Provider<ListPlugins> listProvider)
name|PluginsImpl
parameter_list|(
name|Provider
argument_list|<
name|ListPlugins
argument_list|>
name|listProvider
parameter_list|)
block|{
name|this
operator|.
name|listProvider
operator|=
name|listProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|ListRequest
name|list
parameter_list|()
block|{
return|return
operator|new
name|ListRequest
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PluginInfo
argument_list|>
name|getAsMap
parameter_list|()
throws|throws
name|RestApiException
block|{
name|ListPlugins
name|list
init|=
name|listProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|list
operator|.
name|setAll
argument_list|(
name|this
operator|.
name|getAll
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
operator|.
name|apply
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

