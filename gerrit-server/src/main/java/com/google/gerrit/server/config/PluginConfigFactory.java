begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|class|PluginConfigFactory
specifier|public
class|class
name|PluginConfigFactory
block|{
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
annotation|@
name|Inject
DECL|method|PluginConfigFactory (@erritServerConfig Config cfg)
name|PluginConfigFactory
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
DECL|method|get (String pluginName)
specifier|public
name|PluginConfig
name|get
parameter_list|(
name|String
name|pluginName
parameter_list|)
block|{
return|return
operator|new
name|PluginConfig
argument_list|(
name|pluginName
argument_list|,
name|cfg
argument_list|)
return|;
block|}
block|}
end_class

end_unit

