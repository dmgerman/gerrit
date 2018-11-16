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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|Joiner
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
name|Strings
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
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|IoUtil
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
name|SiteLibraryLoaderUtil
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
name|pgm
operator|.
name|util
operator|.
name|SiteProgram
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
name|gerrit
operator|.
name|server
operator|.
name|plugins
operator|.
name|JarScanner
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
name|securestore
operator|.
name|DefaultSecureStore
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
name|securestore
operator|.
name|SecureStore
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
name|securestore
operator|.
name|SecureStore
operator|.
name|EntryKey
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
name|Injector
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
name|nio
operator|.
name|file
operator|.
name|Path
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
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|zip
operator|.
name|ZipEntry
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
name|errors
operator|.
name|ConfigInvalidException
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
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
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
name|util
operator|.
name|FS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|SwitchSecureStore
specifier|public
class|class
name|SwitchSecureStore
extends|extends
name|SiteProgram
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|method|getSecureStoreClassFromGerritConfig (SitePaths sitePaths)
specifier|private
specifier|static
name|String
name|getSecureStoreClassFromGerritConfig
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|)
block|{
name|FileBasedConfig
name|cfg
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|sitePaths
operator|.
name|gerrit_config
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
try|try
block|{
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot read gerrit.config file"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"secureStoreClass"
argument_list|)
return|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--new-secure-store-lib"
argument_list|,
name|usage
operator|=
literal|"Path to new SecureStore implementation"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|newSecureStoreLib
specifier|private
name|String
name|newSecureStoreLib
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|SitePaths
name|sitePaths
init|=
operator|new
name|SitePaths
argument_list|(
name|getSitePath
argument_list|()
argument_list|)
decl_stmt|;
name|Path
name|newSecureStorePath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|newSecureStoreLib
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|newSecureStorePath
argument_list|)
condition|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"File %s doesn't exist"
argument_list|,
name|newSecureStorePath
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
name|String
name|newSecureStore
init|=
name|getNewSecureStoreClassName
argument_list|(
name|newSecureStorePath
argument_list|)
decl_stmt|;
name|String
name|currentSecureStoreName
init|=
name|getCurrentSecureStoreClassName
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentSecureStoreName
operator|.
name|equals
argument_list|(
name|newSecureStore
argument_list|)
condition|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Old and new SecureStore implementation names "
operator|+
literal|"are the same. Migration will not work"
argument_list|)
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
name|IoUtil
operator|.
name|loadJARs
argument_list|(
name|newSecureStorePath
argument_list|)
expr_stmt|;
name|SiteLibraryLoaderUtil
operator|.
name|loadSiteLib
argument_list|(
name|sitePaths
operator|.
name|lib_dir
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Current secureStoreClass property (%s) will be replaced with %s"
argument_list|,
name|currentSecureStoreName
argument_list|,
name|newSecureStore
argument_list|)
expr_stmt|;
name|Injector
name|dbInjector
init|=
name|createDbInjector
argument_list|()
decl_stmt|;
name|SecureStore
name|currentStore
init|=
name|getSecureStore
argument_list|(
name|currentSecureStoreName
argument_list|,
name|dbInjector
argument_list|)
decl_stmt|;
name|SecureStore
name|newStore
init|=
name|getSecureStore
argument_list|(
name|newSecureStore
argument_list|,
name|dbInjector
argument_list|)
decl_stmt|;
name|migrateProperties
argument_list|(
name|currentStore
argument_list|,
name|newStore
argument_list|)
expr_stmt|;
name|removeOldLib
argument_list|(
name|sitePaths
argument_list|,
name|currentSecureStoreName
argument_list|)
expr_stmt|;
name|copyNewLib
argument_list|(
name|sitePaths
argument_list|,
name|newSecureStorePath
argument_list|)
expr_stmt|;
name|updateGerritConfig
argument_list|(
name|sitePaths
argument_list|,
name|newSecureStore
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
DECL|method|migrateProperties (SecureStore currentStore, SecureStore newStore)
specifier|private
name|void
name|migrateProperties
parameter_list|(
name|SecureStore
name|currentStore
parameter_list|,
name|SecureStore
name|newStore
parameter_list|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Migrate entries"
argument_list|)
expr_stmt|;
for|for
control|(
name|EntryKey
name|key
range|:
name|currentStore
operator|.
name|list
argument_list|()
control|)
block|{
name|String
index|[]
name|value
init|=
name|currentStore
operator|.
name|getList
argument_list|(
name|key
operator|.
name|section
argument_list|,
name|key
operator|.
name|subsection
argument_list|,
name|key
operator|.
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|newStore
operator|.
name|setList
argument_list|(
name|key
operator|.
name|section
argument_list|,
name|key
operator|.
name|subsection
argument_list|,
name|key
operator|.
name|name
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Cannot migrate entry for %s"
argument_list|,
name|key
operator|.
name|section
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|subsection
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
name|msg
operator|+
name|String
operator|.
name|format
argument_list|(
literal|".%s"
argument_list|,
name|key
operator|.
name|subsection
argument_list|)
expr_stmt|;
block|}
name|msg
operator|=
name|msg
operator|+
name|String
operator|.
name|format
argument_list|(
literal|".%s"
argument_list|,
name|key
operator|.
name|name
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|removeOldLib (SitePaths sitePaths, String currentSecureStoreName)
specifier|private
name|void
name|removeOldLib
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
name|String
name|currentSecureStoreName
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|oldSecureStore
init|=
name|findJarWithSecureStore
argument_list|(
name|sitePaths
argument_list|,
name|currentSecureStoreName
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldSecureStore
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Removing old SecureStore (%s) from lib/ directory"
argument_list|,
name|oldSecureStore
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Files
operator|.
name|delete
argument_list|(
name|oldSecureStore
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot remove %s"
argument_list|,
name|oldSecureStore
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Cannot find jar with old SecureStore (%s) in lib/ directory"
argument_list|,
name|currentSecureStoreName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copyNewLib (SitePaths sitePaths, Path newSecureStorePath)
specifier|private
name|void
name|copyNewLib
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
name|Path
name|newSecureStorePath
parameter_list|)
throws|throws
name|IOException
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Copy new SecureStore (%s) into lib/ directory"
argument_list|,
name|newSecureStorePath
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|newSecureStorePath
argument_list|,
name|sitePaths
operator|.
name|lib_dir
operator|.
name|resolve
argument_list|(
name|newSecureStorePath
operator|.
name|getFileName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|updateGerritConfig (SitePaths sitePaths, String newSecureStore)
specifier|private
name|void
name|updateGerritConfig
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
name|String
name|newSecureStore
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Set gerrit.secureStoreClass property of gerrit.config to %s"
argument_list|,
name|newSecureStore
argument_list|)
expr_stmt|;
name|FileBasedConfig
name|config
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|sitePaths
operator|.
name|gerrit_config
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
name|config
operator|.
name|load
argument_list|()
expr_stmt|;
name|config
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"secureStoreClass"
argument_list|,
name|newSecureStore
argument_list|)
expr_stmt|;
name|config
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
DECL|method|getNewSecureStoreClassName (Path secureStore)
specifier|private
name|String
name|getNewSecureStoreClassName
parameter_list|(
name|Path
name|secureStore
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|JarScanner
name|scanner
init|=
operator|new
name|JarScanner
argument_list|(
name|secureStore
argument_list|)
init|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|newSecureStores
init|=
name|scanner
operator|.
name|findSubClassesOf
argument_list|(
name|SecureStore
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newSecureStores
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot find implementation of SecureStore interface in %s"
argument_list|,
name|secureStore
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|newSecureStores
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Found too many implementations of SecureStore:\n%s\nin %s"
argument_list|,
name|Joiner
operator|.
name|on
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|join
argument_list|(
name|newSecureStores
argument_list|)
argument_list|,
name|secureStore
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|newSecureStores
argument_list|)
return|;
block|}
block|}
DECL|method|getCurrentSecureStoreClassName (SitePaths sitePaths)
specifier|private
name|String
name|getCurrentSecureStoreClassName
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|)
block|{
name|String
name|current
init|=
name|getSecureStoreClassFromGerritConfig
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|current
argument_list|)
condition|)
block|{
return|return
name|current
return|;
block|}
return|return
name|DefaultSecureStore
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getSecureStore (String className, Injector injector)
specifier|private
name|SecureStore
name|getSecureStore
parameter_list|(
name|String
name|className
parameter_list|,
name|Injector
name|injector
parameter_list|)
block|{
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|?
extends|extends
name|SecureStore
argument_list|>
name|clazz
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|SecureStore
argument_list|>
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
decl_stmt|;
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|clazz
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load SecureStore implementation: %s"
argument_list|,
name|className
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|findJarWithSecureStore (SitePaths sitePaths, String secureStoreClass)
specifier|private
name|Path
name|findJarWithSecureStore
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
name|String
name|secureStoreClass
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Path
argument_list|>
name|jars
init|=
name|SiteLibraryLoaderUtil
operator|.
name|listJars
argument_list|(
name|sitePaths
operator|.
name|lib_dir
argument_list|)
decl_stmt|;
name|String
name|secureStoreClassPath
init|=
name|secureStoreClass
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
decl_stmt|;
for|for
control|(
name|Path
name|jar
range|:
name|jars
control|)
block|{
try|try
init|(
name|JarFile
name|jarFile
init|=
operator|new
name|JarFile
argument_list|(
name|jar
operator|.
name|toFile
argument_list|()
argument_list|)
init|)
block|{
name|ZipEntry
name|entry
init|=
name|jarFile
operator|.
name|getEntry
argument_list|(
name|secureStoreClassPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
return|return
name|jar
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

