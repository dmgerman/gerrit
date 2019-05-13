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
name|extensions
operator|.
name|common
operator|.
name|Input
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
name|MethodNotAllowedException
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
name|extensions
operator|.
name|restapi
operator|.
name|RestModifyView
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
name|permissions
operator|.
name|GlobalPermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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

begin_class
annotation|@
name|Singleton
DECL|class|DisablePlugin
specifier|public
class|class
name|DisablePlugin
implements|implements
name|RestModifyView
argument_list|<
name|PluginResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|loader
specifier|private
specifier|final
name|PluginLoader
name|loader
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|mandatoryPluginsCollection
specifier|private
specifier|final
name|MandatoryPluginsCollection
name|mandatoryPluginsCollection
decl_stmt|;
annotation|@
name|Inject
DECL|method|DisablePlugin ( PluginLoader loader, PermissionBackend permissionBackend, MandatoryPluginsCollection mandatoryPluginsCollection)
name|DisablePlugin
parameter_list|(
name|PluginLoader
name|loader
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|MandatoryPluginsCollection
name|mandatoryPluginsCollection
parameter_list|)
block|{
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|mandatoryPluginsCollection
operator|=
name|mandatoryPluginsCollection
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (PluginResource resource, Input input)
specifier|public
name|PluginInfo
name|apply
parameter_list|(
name|PluginResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Could not check permission"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|loader
operator|.
name|checkRemoteAdminEnabled
argument_list|()
expr_stmt|;
name|String
name|name
init|=
name|resource
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|mandatoryPluginsCollection
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"Plugin "
operator|+
name|name
operator|+
literal|" is mandatory"
argument_list|)
throw|;
block|}
name|loader
operator|.
name|disablePlugins
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ListPlugins
operator|.
name|toPluginInfo
argument_list|(
name|loader
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

