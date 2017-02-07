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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|ImmutableMap
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
name|Export
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
name|Plugin
operator|.
name|ApiType
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
name|Module
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_comment
comment|/**  * Base plugin scanner for a set of pre-loaded classes.  *  *<p>Utility base class for simplifying the development of Server plugin scanner based on a set of  * externally pre-loaded classes.  *  *<p>Extending this class you can implement very easily a PluginContentScanner from a set of  * pre-loaded Java Classes and an API Type. The convention used by this class is: - there is at most  * one Guice module per Gerrit module type (SysModule, HttpModule, SshModule) - plugin is set to be  * restartable in Gerrit Plugin MANIFEST - only Export and Listen annotated classes can be  * self-discovered  */
end_comment

begin_class
DECL|class|AbstractPreloadedPluginScanner
specifier|public
specifier|abstract
class|class
name|AbstractPreloadedPluginScanner
implements|implements
name|PluginContentScanner
block|{
DECL|field|pluginName
specifier|protected
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|pluginVersion
specifier|protected
specifier|final
name|String
name|pluginVersion
decl_stmt|;
DECL|field|preloadedClasses
specifier|protected
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|preloadedClasses
decl_stmt|;
DECL|field|apiType
specifier|protected
specifier|final
name|ApiType
name|apiType
decl_stmt|;
DECL|field|sshModuleClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|sshModuleClass
decl_stmt|;
DECL|field|httpModuleClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|httpModuleClass
decl_stmt|;
DECL|field|sysModuleClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|sysModuleClass
decl_stmt|;
DECL|method|AbstractPreloadedPluginScanner ( String pluginName, String pluginVersion, Set<Class<?>> preloadedClasses, Plugin.ApiType apiType)
specifier|public
name|AbstractPreloadedPluginScanner
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|pluginVersion
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|preloadedClasses
parameter_list|,
name|Plugin
operator|.
name|ApiType
name|apiType
parameter_list|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
name|this
operator|.
name|pluginVersion
operator|=
name|pluginVersion
expr_stmt|;
name|this
operator|.
name|preloadedClasses
operator|=
name|preloadedClasses
expr_stmt|;
name|this
operator|.
name|apiType
operator|=
name|apiType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getManifest ()
specifier|public
name|Manifest
name|getManifest
parameter_list|()
throws|throws
name|IOException
block|{
name|scanGuiceModules
argument_list|(
name|preloadedClasses
argument_list|)
expr_stmt|;
name|StringBuilder
name|manifestString
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"PluginName: "
operator|+
name|pluginName
operator|+
literal|"\n"
operator|+
literal|"Implementation-Version: "
operator|+
name|pluginVersion
operator|+
literal|"\n"
operator|+
literal|"Gerrit-ReloadMode: restart\n"
operator|+
literal|"Gerrit-ApiType: "
operator|+
name|apiType
operator|+
literal|"\n"
argument_list|)
decl_stmt|;
name|appendIfNotNull
argument_list|(
name|manifestString
argument_list|,
literal|"Gerrit-SshModule: "
argument_list|,
name|sshModuleClass
argument_list|)
expr_stmt|;
name|appendIfNotNull
argument_list|(
name|manifestString
argument_list|,
literal|"Gerrit-HttpModule: "
argument_list|,
name|httpModuleClass
argument_list|)
expr_stmt|;
name|appendIfNotNull
argument_list|(
name|manifestString
argument_list|,
literal|"Gerrit-Module: "
argument_list|,
name|sysModuleClass
argument_list|)
expr_stmt|;
return|return
operator|new
name|Manifest
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|manifestString
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scan ( String pluginName, Iterable<Class<? extends Annotation>> annotations)
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Iterable
argument_list|<
name|ExtensionMetaData
argument_list|>
argument_list|>
name|scan
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Iterable
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Iterable
argument_list|<
name|ExtensionMetaData
argument_list|>
argument_list|>
name|result
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
name|Set
argument_list|<
name|ExtensionMetaData
argument_list|>
name|classMetaDataSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|annotation
argument_list|,
name|classMetaDataSet
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|preloadedClasses
control|)
block|{
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|clazz
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|annotation
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|classMetaDataSet
operator|.
name|add
argument_list|(
operator|new
name|ExtensionMetaData
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|getExportAnnotationValue
argument_list|(
name|clazz
argument_list|,
name|annotation
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|appendIfNotNull (StringBuilder string, String header, Class<?> guiceModuleClass)
specifier|private
name|void
name|appendIfNotNull
parameter_list|(
name|StringBuilder
name|string
parameter_list|,
name|String
name|header
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|guiceModuleClass
parameter_list|)
block|{
if|if
condition|(
name|guiceModuleClass
operator|!=
literal|null
condition|)
block|{
name|string
operator|.
name|append
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|string
operator|.
name|append
argument_list|(
name|guiceModuleClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|string
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|scanGuiceModules (Set<Class<?>> classes)
specifier|private
name|void
name|scanGuiceModules
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|sysModuleBaseClass
init|=
name|Module
operator|.
name|class
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|httpModuleBaseClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.google.inject.servlet.ServletModule"
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sshModuleBaseClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.google.gerrit.sshd.CommandModule"
argument_list|)
decl_stmt|;
name|sshModuleClass
operator|=
literal|null
expr_stmt|;
name|httpModuleClass
operator|=
literal|null
expr_stmt|;
name|sysModuleClass
operator|=
literal|null
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isLocalClass
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|sshModuleBaseClass
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|sshModuleClass
operator|=
name|getUniqueGuiceModule
argument_list|(
name|sshModuleBaseClass
argument_list|,
name|sshModuleClass
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|httpModuleBaseClass
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|httpModuleClass
operator|=
name|getUniqueGuiceModule
argument_list|(
name|httpModuleBaseClass
argument_list|,
name|httpModuleClass
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sysModuleBaseClass
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|sysModuleClass
operator|=
name|getUniqueGuiceModule
argument_list|(
name|sysModuleBaseClass
argument_list|,
name|sysModuleClass
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot find base Gerrit classes for Guice Plugin Modules"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getUniqueGuiceModule ( Class<?> guiceModuleBaseClass, Class<?> existingGuiceModuleName, Class<?> newGuiceModuleClass)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|getUniqueGuiceModule
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|guiceModuleBaseClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|existingGuiceModuleName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|newGuiceModuleClass
parameter_list|)
block|{
name|checkState
argument_list|(
name|existingGuiceModuleName
operator|==
literal|null
argument_list|,
literal|"Multiple %s implementations: %s, %s"
argument_list|,
name|guiceModuleBaseClass
argument_list|,
name|existingGuiceModuleName
argument_list|,
name|newGuiceModuleClass
argument_list|)
expr_stmt|;
return|return
name|newGuiceModuleClass
return|;
block|}
DECL|method|getExportAnnotationValue ( Class<?> scriptClass, Class<? extends Annotation> annotation)
specifier|private
name|String
name|getExportAnnotationValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|scriptClass
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|)
block|{
return|return
name|annotation
operator|==
name|Export
operator|.
name|class
condition|?
name|scriptClass
operator|.
name|getAnnotation
argument_list|(
name|Export
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
else|:
literal|""
return|;
block|}
block|}
end_class

end_unit

