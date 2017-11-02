begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|systemstatus
operator|.
name|ServerInformation
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

begin_class
DECL|class|PluginModule
specifier|public
class|class
name|PluginModule
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
name|bind
argument_list|(
name|ServerInformationImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ServerInformation
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerInformationImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PluginCleanerTask
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PluginGuiceEnvironment
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PluginLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CopyConfigModule
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|PluginLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicSet
operator|.
name|setOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|ServerPluginProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicSet
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|ServerPluginProvider
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|JarPluginProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|UniversalServerPluginProvider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
