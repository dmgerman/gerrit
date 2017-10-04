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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|RequiresCapability
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
name|InstallPluginInput
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
name|BadRequestException
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
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|TopLevelResource
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipException
import|;
end_import

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
DECL|class|InstallPlugin
specifier|public
class|class
name|InstallPlugin
implements|implements
name|RestModifyView
argument_list|<
name|TopLevelResource
argument_list|,
name|InstallPluginInput
argument_list|>
block|{
DECL|field|loader
specifier|private
specifier|final
name|PluginLoader
name|loader
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|created
specifier|private
name|boolean
name|created
decl_stmt|;
annotation|@
name|Inject
DECL|method|InstallPlugin (PluginLoader loader)
name|InstallPlugin
parameter_list|(
name|PluginLoader
name|loader
parameter_list|)
block|{
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
block|}
DECL|method|setName (String name)
specifier|public
name|InstallPlugin
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setCreated (boolean created)
specifier|public
name|InstallPlugin
name|setCreated
parameter_list|(
name|boolean
name|created
parameter_list|)
block|{
name|this
operator|.
name|created
operator|=
name|created
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource resource, InstallPluginInput input)
specifier|public
name|Response
argument_list|<
name|PluginInfo
argument_list|>
name|apply
parameter_list|(
name|TopLevelResource
name|resource
parameter_list|,
name|InstallPluginInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
block|{
name|loader
operator|.
name|checkRemoteAdminEnabled
argument_list|()
expr_stmt|;
try|try
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|openStream
argument_list|(
name|input
argument_list|)
init|)
block|{
name|String
name|pluginName
init|=
name|loader
operator|.
name|installPluginFromStream
argument_list|(
name|name
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|PluginInfo
name|info
init|=
name|ListPlugins
operator|.
name|toPluginInfo
argument_list|(
name|loader
operator|.
name|get
argument_list|(
name|pluginName
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|created
condition|?
name|Response
operator|.
name|created
argument_list|(
name|info
argument_list|)
else|:
name|Response
operator|.
name|ok
argument_list|(
name|info
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|PluginInstallException
name|e
parameter_list|)
block|{
name|StringWriter
name|buf
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|buf
operator|.
name|write
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"cannot install %s"
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ZipException
condition|)
block|{
name|buf
operator|.
name|write
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|write
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|write
argument_list|(
literal|":\n"
argument_list|)
expr_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|pw
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|openStream (InstallPluginInput input)
specifier|private
name|InputStream
name|openStream
parameter_list|(
name|InstallPluginInput
name|input
parameter_list|)
throws|throws
name|IOException
throws|,
name|BadRequestException
block|{
if|if
condition|(
name|input
operator|.
name|raw
operator|!=
literal|null
condition|)
block|{
return|return
name|input
operator|.
name|raw
operator|.
name|getInputStream
argument_list|()
return|;
block|}
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|input
operator|.
name|url
argument_list|)
operator|.
name|openStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
DECL|class|Overwrite
specifier|static
class|class
name|Overwrite
implements|implements
name|RestModifyView
argument_list|<
name|PluginResource
argument_list|,
name|InstallPluginInput
argument_list|>
block|{
DECL|field|install
specifier|private
specifier|final
name|Provider
argument_list|<
name|InstallPlugin
argument_list|>
name|install
decl_stmt|;
annotation|@
name|Inject
DECL|method|Overwrite (Provider<InstallPlugin> install)
name|Overwrite
parameter_list|(
name|Provider
argument_list|<
name|InstallPlugin
argument_list|>
name|install
parameter_list|)
block|{
name|this
operator|.
name|install
operator|=
name|install
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (PluginResource resource, InstallPluginInput input)
specifier|public
name|Response
argument_list|<
name|PluginInfo
argument_list|>
name|apply
parameter_list|(
name|PluginResource
name|resource
parameter_list|,
name|InstallPluginInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
block|{
return|return
name|install
operator|.
name|get
argument_list|()
operator|.
name|setName
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|input
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

