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
DECL|package|com.google.gerrit.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
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
name|LinkedHashMultimap
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
name|Multimap
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Loads the classes for Prolog predicates.  */
end_comment

begin_class
DECL|class|PredicateClassLoader
specifier|public
class|class
name|PredicateClassLoader
extends|extends
name|ClassLoader
block|{
DECL|field|packageClassLoaderMap
specifier|private
specifier|final
name|Multimap
argument_list|<
name|String
argument_list|,
name|ClassLoader
argument_list|>
name|packageClassLoaderMap
init|=
name|LinkedHashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|method|PredicateClassLoader ( final DynamicSet<PredicateProvider> predicateProviders, final ClassLoader parent)
specifier|public
name|PredicateClassLoader
parameter_list|(
specifier|final
name|DynamicSet
argument_list|<
name|PredicateProvider
argument_list|>
name|predicateProviders
parameter_list|,
specifier|final
name|ClassLoader
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
for|for
control|(
name|PredicateProvider
name|predicateProvider
range|:
name|predicateProviders
control|)
block|{
for|for
control|(
name|String
name|pkg
range|:
name|predicateProvider
operator|.
name|getPackages
argument_list|()
control|)
block|{
name|packageClassLoaderMap
operator|.
name|put
argument_list|(
name|pkg
argument_list|,
name|predicateProvider
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|findClass (final String className)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
specifier|final
name|String
name|className
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
specifier|final
name|Collection
argument_list|<
name|ClassLoader
argument_list|>
name|classLoaders
init|=
name|packageClassLoaderMap
operator|.
name|get
argument_list|(
name|getPackageName
argument_list|(
name|className
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|ClassLoader
name|cl
range|:
name|classLoaders
control|)
block|{
try|try
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|,
literal|true
argument_list|,
name|cl
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|className
argument_list|)
throw|;
block|}
DECL|method|getPackageName (String className)
specifier|private
specifier|static
name|String
name|getPackageName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
specifier|final
name|int
name|pos
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|<
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
return|;
block|}
block|}
end_class

end_unit

