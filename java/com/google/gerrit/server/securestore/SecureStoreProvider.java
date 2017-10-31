begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|Strings
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
name|Nullable
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
name|Provider
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

begin_class
annotation|@
name|Singleton
DECL|class|SecureStoreProvider
specifier|public
class|class
name|SecureStoreProvider
implements|implements
name|Provider
argument_list|<
name|SecureStore
argument_list|>
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
name|SecureStoreProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|libdir
specifier|private
specifier|final
name|Path
name|libdir
decl_stmt|;
DECL|field|injector
specifier|private
specifier|final
name|Injector
name|injector
decl_stmt|;
DECL|field|className
specifier|private
specifier|final
name|String
name|className
decl_stmt|;
annotation|@
name|Inject
DECL|method|SecureStoreProvider ( Injector injector, SitePaths sitePaths, @Nullable @SecureStoreClassName String className)
specifier|protected
name|SecureStoreProvider
parameter_list|(
name|Injector
name|injector
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|SecureStoreClassName
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
name|this
operator|.
name|libdir
operator|=
name|sitePaths
operator|.
name|lib_dir
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
specifier|synchronized
name|SecureStore
name|get
parameter_list|()
block|{
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|getSecureStoreImpl
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getSecureStoreImpl ()
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|SecureStore
argument_list|>
name|getSecureStoreImpl
parameter_list|()
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|DefaultSecureStore
operator|.
name|class
return|;
block|}
name|SiteLibraryLoaderUtil
operator|.
name|loadSiteLib
argument_list|(
name|libdir
argument_list|)
expr_stmt|;
try|try
block|{
return|return
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
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load secure store class: %s"
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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

