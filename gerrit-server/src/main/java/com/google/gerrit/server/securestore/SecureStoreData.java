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
DECL|package|com.google.gerrit.server.securestore
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|securestore
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
name|Objects
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

begin_class
DECL|class|SecureStoreData
specifier|public
class|class
name|SecureStoreData
block|{
DECL|field|pluginFile
specifier|public
specifier|final
name|File
name|pluginFile
decl_stmt|;
DECL|field|storeName
specifier|public
specifier|final
name|String
name|storeName
decl_stmt|;
DECL|field|className
specifier|public
specifier|final
name|String
name|className
decl_stmt|;
DECL|method|SecureStoreData (String pluginName, String className, File jarFile, String storeName)
specifier|public
name|SecureStoreData
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|className
parameter_list|,
name|File
name|jarFile
parameter_list|,
name|String
name|storeName
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
name|this
operator|.
name|pluginFile
operator|=
name|jarFile
expr_stmt|;
name|this
operator|.
name|storeName
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s"
argument_list|,
name|pluginName
argument_list|,
name|storeName
argument_list|)
expr_stmt|;
block|}
DECL|method|getStoreName ()
specifier|public
name|String
name|getStoreName
parameter_list|()
block|{
return|return
name|storeName
return|;
block|}
DECL|method|load ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|SecureStore
argument_list|>
name|load
parameter_list|()
block|{
return|return
name|load
argument_list|(
name|pluginFile
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|load (File pluginFile)
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|SecureStore
argument_list|>
name|load
parameter_list|(
name|File
name|pluginFile
parameter_list|)
block|{
try|try
block|{
name|URL
index|[]
name|pluginJarUrls
init|=
operator|new
name|URL
index|[]
block|{
name|pluginFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
block|}
decl_stmt|;
name|ClassLoader
name|currentCL
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
specifier|final
name|URLClassLoader
name|newClassLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|pluginJarUrls
argument_list|,
name|currentCL
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|newClassLoader
argument_list|)
expr_stmt|;
return|return
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|SecureStore
argument_list|>
operator|)
name|newClassLoader
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SecureStoreException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load secure store implementation for %s"
argument_list|,
name|storeName
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"storeName"
argument_list|,
name|storeName
argument_list|)
operator|.
name|add
argument_list|(
literal|"className"
argument_list|,
name|className
argument_list|)
operator|.
name|add
argument_list|(
literal|"file"
argument_list|,
name|pluginFile
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|SecureStoreData
condition|)
block|{
name|SecureStoreData
name|o
init|=
operator|(
name|SecureStoreData
operator|)
name|obj
decl_stmt|;
return|return
name|storeName
operator|.
name|equals
argument_list|(
name|o
operator|.
name|storeName
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|storeName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

