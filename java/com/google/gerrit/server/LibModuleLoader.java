begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Key
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
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

begin_comment
comment|/** Loads configured Guice modules from {@code gerrit.installModule}. */
end_comment

begin_class
DECL|class|LibModuleLoader
specifier|public
class|class
name|LibModuleLoader
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
DECL|method|loadModules (Injector parent, LibModuleType moduleType)
specifier|public
specifier|static
name|List
argument_list|<
name|Module
argument_list|>
name|loadModules
parameter_list|(
name|Injector
name|parent
parameter_list|,
name|LibModuleType
name|moduleType
parameter_list|)
block|{
name|Config
name|cfg
init|=
name|getConfig
argument_list|(
name|parent
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|stream
argument_list|(
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"install"
operator|+
name|moduleType
operator|.
name|getConfigKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|m
lambda|->
name|createModule
argument_list|(
name|parent
argument_list|,
name|m
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getConfig (Injector i)
specifier|private
specifier|static
name|Config
name|getConfig
parameter_list|(
name|Injector
name|i
parameter_list|)
block|{
return|return
name|i
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|Config
operator|.
name|class
argument_list|,
name|GerritServerConfig
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createModule (Injector injector, String className)
specifier|private
specifier|static
name|Module
name|createModule
parameter_list|(
name|Injector
name|injector
parameter_list|,
name|String
name|className
parameter_list|)
block|{
name|Module
name|m
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|loadModule
argument_list|(
name|className
argument_list|)
argument_list|)
decl_stmt|;
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Installed module %s"
argument_list|,
name|className
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|loadModule (String className)
specifier|private
specifier|static
name|Class
argument_list|<
name|Module
argument_list|>
name|loadModule
parameter_list|(
name|String
name|className
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|Class
argument_list|<
name|Module
argument_list|>
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
decl||
name|LinkageError
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Cannot load LibModule "
operator|+
name|className
decl_stmt|;
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
name|msg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

