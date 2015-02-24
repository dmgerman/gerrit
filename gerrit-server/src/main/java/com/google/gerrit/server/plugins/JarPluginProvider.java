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
name|gerrit
operator|.
name|server
operator|.
name|plugins
operator|.
name|PluginLoader
operator|.
name|asTemp
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
name|base
operator|.
name|MoreObjects
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
name|SitePaths
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
name|io
operator|.
name|FileInputStream
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
name|net
operator|.
name|MalformedURLException
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
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Date
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
name|jar
operator|.
name|JarFile
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

begin_class
DECL|class|JarPluginProvider
specifier|public
class|class
name|JarPluginProvider
implements|implements
name|ServerPluginProvider
block|{
DECL|field|PLUGIN_TMP_PREFIX
specifier|static
specifier|final
name|String
name|PLUGIN_TMP_PREFIX
init|=
literal|"plugin_"
decl_stmt|;
DECL|field|JAR_EXTENSION
specifier|static
specifier|final
name|String
name|JAR_EXTENSION
init|=
literal|".jar"
decl_stmt|;
DECL|field|log
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JarPluginProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|tmpDir
specifier|private
specifier|final
name|File
name|tmpDir
decl_stmt|;
annotation|@
name|Inject
DECL|method|JarPluginProvider (SitePaths sitePaths)
name|JarPluginProvider
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|)
block|{
comment|// TODO(dborowitz): Convert to NIO.
name|tmpDir
operator|=
name|sitePaths
operator|.
name|tmp_dir
operator|.
name|toFile
argument_list|()
expr_stmt|;
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
name|String
name|fileName
init|=
name|srcFile
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|fileName
operator|.
name|endsWith
argument_list|(
name|JAR_EXTENSION
argument_list|)
operator|||
name|fileName
operator|.
name|endsWith
argument_list|(
name|JAR_EXTENSION
operator|+
literal|".disabled"
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
try|try
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|getJarPluginName
argument_list|(
name|srcFile
argument_list|)
argument_list|,
name|PluginLoader
operator|.
name|nameOf
argument_list|(
name|srcFile
argument_list|)
argument_list|)
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
name|IllegalArgumentException
argument_list|(
literal|"Invalid plugin file "
operator|+
name|srcFile
operator|+
literal|": cannot get plugin name"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getJarPluginName (File srcFile)
specifier|public
specifier|static
name|String
name|getJarPluginName
parameter_list|(
name|File
name|srcFile
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|JarFile
name|jarFile
init|=
operator|new
name|JarFile
argument_list|(
name|srcFile
argument_list|)
init|)
block|{
return|return
name|jarFile
operator|.
name|getManifest
argument_list|()
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
literal|"Gerrit-PluginName"
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (File srcFile, FileSnapshot snapshot, PluginDescription description)
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
name|description
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
try|try
block|{
name|String
name|name
init|=
name|getPluginName
argument_list|(
name|srcFile
argument_list|)
decl_stmt|;
name|String
name|extension
init|=
name|getExtension
argument_list|(
name|srcFile
argument_list|)
decl_stmt|;
try|try
init|(
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|srcFile
argument_list|)
init|)
block|{
name|File
name|tmp
init|=
name|asTemp
argument_list|(
name|in
argument_list|,
name|tempNameFor
argument_list|(
name|name
argument_list|)
argument_list|,
name|extension
argument_list|,
name|tmpDir
argument_list|)
decl_stmt|;
return|return
name|loadJarPlugin
argument_list|(
name|name
argument_list|,
name|srcFile
argument_list|,
name|snapshot
argument_list|,
name|tmp
argument_list|,
name|description
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
literal|"Cannot load Jar plugin "
operator|+
name|srcFile
argument_list|,
name|e
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
DECL|method|getExtension (File file)
specifier|private
specifier|static
name|String
name|getExtension
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
name|getExtension
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getExtension (String name)
specifier|private
specifier|static
name|String
name|getExtension
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|ext
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
return|return
literal|0
operator|<
name|ext
condition|?
name|name
operator|.
name|substring
argument_list|(
name|ext
argument_list|)
else|:
literal|""
return|;
block|}
DECL|method|tempNameFor (String name)
specifier|private
specifier|static
name|String
name|tempNameFor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|SimpleDateFormat
name|fmt
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyMMdd_HHmm"
argument_list|)
decl_stmt|;
return|return
name|PLUGIN_TMP_PREFIX
operator|+
name|name
operator|+
literal|"_"
operator|+
name|fmt
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
operator|+
literal|"_"
return|;
block|}
DECL|method|storeInTemp (String pluginName, InputStream in, SitePaths sitePaths)
specifier|public
specifier|static
name|File
name|storeInTemp
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|sitePaths
operator|.
name|tmp_dir
argument_list|)
condition|)
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|sitePaths
operator|.
name|tmp_dir
argument_list|)
expr_stmt|;
block|}
return|return
name|asTemp
argument_list|(
name|in
argument_list|,
name|tempNameFor
argument_list|(
name|pluginName
argument_list|)
argument_list|,
literal|".jar"
argument_list|,
name|sitePaths
operator|.
name|tmp_dir
operator|.
name|toFile
argument_list|()
argument_list|)
return|;
block|}
DECL|method|loadJarPlugin (String name, File srcJar, FileSnapshot snapshot, File tmp, PluginDescription description)
specifier|private
name|ServerPlugin
name|loadJarPlugin
parameter_list|(
name|String
name|name
parameter_list|,
name|File
name|srcJar
parameter_list|,
name|FileSnapshot
name|snapshot
parameter_list|,
name|File
name|tmp
parameter_list|,
name|PluginDescription
name|description
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidPluginException
throws|,
name|MalformedURLException
block|{
name|JarFile
name|jarFile
init|=
operator|new
name|JarFile
argument_list|(
name|tmp
argument_list|)
decl_stmt|;
name|boolean
name|keep
init|=
literal|false
decl_stmt|;
try|try
block|{
name|Manifest
name|manifest
init|=
name|jarFile
operator|.
name|getManifest
argument_list|()
decl_stmt|;
name|Plugin
operator|.
name|ApiType
name|type
init|=
name|Plugin
operator|.
name|getApiType
argument_list|(
name|manifest
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|overlay
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"gerrit.plugin-classes"
argument_list|)
decl_stmt|;
if|if
condition|(
name|overlay
operator|!=
literal|null
condition|)
block|{
name|File
name|classes
init|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|overlay
argument_list|)
argument_list|,
name|name
argument_list|)
argument_list|,
literal|"main"
argument_list|)
decl_stmt|;
if|if
condition|(
name|classes
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"plugin %s: including %s"
argument_list|,
name|name
argument_list|,
name|classes
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|classes
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|urls
operator|.
name|add
argument_list|(
name|tmp
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|ClassLoader
name|pluginLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
operator|.
name|toArray
argument_list|(
operator|new
name|URL
index|[
name|urls
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|PluginLoader
operator|.
name|parentFor
argument_list|(
name|type
argument_list|)
argument_list|)
decl_stmt|;
name|JarScanner
name|jarScanner
init|=
name|createJarScanner
argument_list|(
name|srcJar
argument_list|)
decl_stmt|;
name|ServerPlugin
name|plugin
init|=
operator|new
name|ServerPlugin
argument_list|(
name|name
argument_list|,
name|description
operator|.
name|canonicalUrl
argument_list|,
name|description
operator|.
name|user
argument_list|,
name|srcJar
argument_list|,
name|snapshot
argument_list|,
name|jarScanner
argument_list|,
name|description
operator|.
name|dataDir
argument_list|,
name|pluginLoader
argument_list|)
decl_stmt|;
name|plugin
operator|.
name|setCleanupHandle
argument_list|(
operator|new
name|CleanupHandle
argument_list|(
name|tmp
argument_list|,
name|jarFile
argument_list|)
argument_list|)
expr_stmt|;
name|keep
operator|=
literal|true
expr_stmt|;
return|return
name|plugin
return|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|keep
condition|)
block|{
name|jarFile
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|createJarScanner (File srcJar)
specifier|private
name|JarScanner
name|createJarScanner
parameter_list|(
name|File
name|srcJar
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
try|try
block|{
return|return
operator|new
name|JarScanner
argument_list|(
name|srcJar
argument_list|)
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
name|InvalidPluginException
argument_list|(
literal|"Cannot scan plugin file "
operator|+
name|srcJar
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

