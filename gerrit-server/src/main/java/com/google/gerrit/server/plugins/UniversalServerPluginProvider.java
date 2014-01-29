begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|base
operator|.
name|Predicate
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
name|Iterables
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
name|DynamicSet
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|FileSnapshot
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
name|io
operator|.
name|File
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

begin_class
annotation|@
name|Singleton
DECL|class|UniversalServerPluginProvider
class|class
name|UniversalServerPluginProvider
implements|implements
name|ServerPluginProvider
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
name|UniversalServerPluginProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|serverPluginProviders
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ServerPluginProvider
argument_list|>
name|serverPluginProviders
decl_stmt|;
annotation|@
name|Inject
DECL|method|UniversalServerPluginProvider (DynamicSet<ServerPluginProvider> sf)
name|UniversalServerPluginProvider
parameter_list|(
name|DynamicSet
argument_list|<
name|ServerPluginProvider
argument_list|>
name|sf
parameter_list|)
block|{
name|this
operator|.
name|serverPluginProviders
operator|=
name|sf
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (File srcFile, FileSnapshot snapshot, PluginDescription pluginDescription)
specifier|public
name|ServerPlugin
name|get
parameter_list|(
name|File
name|srcFile
parameter_list|,
name|FileSnapshot
name|snapshot
parameter_list|,
name|PluginDescription
name|pluginDescription
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
return|return
name|providerOf
argument_list|(
name|srcFile
argument_list|)
operator|.
name|get
argument_list|(
name|srcFile
argument_list|,
name|snapshot
argument_list|,
name|pluginDescription
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getPluginName (File srcFile)
specifier|public
name|String
name|getPluginName
parameter_list|(
name|File
name|srcFile
parameter_list|)
block|{
return|return
name|providerOf
argument_list|(
name|srcFile
argument_list|)
operator|.
name|getPluginName
argument_list|(
name|srcFile
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|handles (File srcFile)
specifier|public
name|boolean
name|handles
parameter_list|(
name|File
name|srcFile
parameter_list|)
block|{
name|List
argument_list|<
name|ServerPluginProvider
argument_list|>
name|providers
init|=
name|providersForHandlingPlugin
argument_list|(
name|srcFile
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|providers
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
literal|true
return|;
case|case
literal|0
case|:
return|return
literal|false
return|;
default|default:
throw|throw
operator|new
name|MultipleProvidersForPluginException
argument_list|(
name|srcFile
argument_list|,
name|providers
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getProviderPluginName ()
specifier|public
name|String
name|getProviderPluginName
parameter_list|()
block|{
return|return
literal|"gerrit"
return|;
block|}
DECL|method|providerOf (File srcFile)
specifier|private
name|ServerPluginProvider
name|providerOf
parameter_list|(
name|File
name|srcFile
parameter_list|)
block|{
name|List
argument_list|<
name|ServerPluginProvider
argument_list|>
name|providers
init|=
name|providersForHandlingPlugin
argument_list|(
name|srcFile
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|providers
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
name|providers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
case|case
literal|0
case|:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No ServerPluginProvider found/loaded to handle plugin file "
operator|+
name|srcFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|MultipleProvidersForPluginException
argument_list|(
name|srcFile
argument_list|,
name|providers
argument_list|)
throw|;
block|}
block|}
DECL|method|providersForHandlingPlugin ( final File srcFile)
specifier|private
name|List
argument_list|<
name|ServerPluginProvider
argument_list|>
name|providersForHandlingPlugin
parameter_list|(
specifier|final
name|File
name|srcFile
parameter_list|)
block|{
name|List
argument_list|<
name|ServerPluginProvider
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ServerPluginProvider
name|serverPluginProvider
range|:
name|serverPluginProviders
control|)
block|{
name|boolean
name|handles
init|=
name|serverPluginProvider
operator|.
name|handles
argument_list|(
name|srcFile
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"File {} handled by {} ? => {}"
argument_list|,
name|srcFile
argument_list|,
name|serverPluginProvider
operator|.
name|getProviderPluginName
argument_list|()
argument_list|,
name|handles
argument_list|)
expr_stmt|;
if|if
condition|(
name|handles
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|serverPluginProvider
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|providers
return|;
block|}
block|}
end_class

end_unit

