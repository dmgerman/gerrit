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
DECL|package|com.google.gerrit.acceptance.rest
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|PluginResource
operator|.
name|PLUGIN_KIND
import|;
end_import

begin_import
import|import static
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
name|ConfigResource
operator|.
name|CONFIG_KIND
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
name|annotations
operator|.
name|Exports
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
name|config
operator|.
name|CapabilityDefinition
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
name|DynamicMap
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
name|RestApiModule
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
name|AbstractModule
import|;
end_import

begin_class
DECL|class|TestPluginModule
specifier|public
class|class
name|TestPluginModule
extends|extends
name|AbstractModule
block|{
DECL|field|PLUGIN_CAPABILITY
specifier|public
specifier|static
specifier|final
name|String
name|PLUGIN_CAPABILITY
init|=
literal|"printHello"
decl_stmt|;
DECL|field|PLUGIN_COLLECTION
specifier|public
specifier|static
specifier|final
name|String
name|PLUGIN_COLLECTION
init|=
literal|"foo"
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|CapabilityDefinition
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Exports
operator|.
name|named
argument_list|(
name|PLUGIN_CAPABILITY
argument_list|)
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|CapabilityDefinition
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Print Hello"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|RestApiModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|PLUGIN_KIND
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|CONFIG_KIND
argument_list|,
name|PLUGIN_COLLECTION
argument_list|)
operator|.
name|to
argument_list|(
name|PluginCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PLUGIN_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetTestPlugin
operator|.
name|class
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|PLUGIN_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|CreateTestPlugin
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

