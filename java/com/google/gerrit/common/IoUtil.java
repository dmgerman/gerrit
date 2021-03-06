begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|Sets
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
name|OutputStream
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
name|InvocationTargetException
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
name|Method
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
name|Path
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
DECL|class|IoUtil
specifier|public
specifier|final
class|class
name|IoUtil
block|{
DECL|method|copyWithThread (InputStream src, OutputStream dst)
specifier|public
specifier|static
name|void
name|copyWithThread
parameter_list|(
name|InputStream
name|src
parameter_list|,
name|OutputStream
name|dst
parameter_list|)
block|{
operator|new
name|Thread
argument_list|(
literal|"IoUtil-Copy"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
literal|0
operator|<
operator|(
name|n
operator|=
name|src
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
condition|)
block|{
name|dst
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|src
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|loadJARs (Collection<Path> jars)
specifier|public
specifier|static
name|void
name|loadJARs
parameter_list|(
name|Collection
argument_list|<
name|Path
argument_list|>
name|jars
parameter_list|)
block|{
if|if
condition|(
name|jars
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|ClassLoader
name|cl
init|=
name|IoUtil
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|cl
operator|instanceof
name|URLClassLoader
operator|)
condition|)
block|{
throw|throw
name|noAddURL
argument_list|(
literal|"Not loaded by URLClassLoader"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
comment|// Leave open so classes can be loaded.
name|URLClassLoader
name|urlClassLoader
init|=
operator|(
name|URLClassLoader
operator|)
name|cl
decl_stmt|;
name|Method
name|addURL
decl_stmt|;
try|try
block|{
name|addURL
operator|=
name|URLClassLoader
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"addURL"
argument_list|,
name|URL
operator|.
name|class
argument_list|)
expr_stmt|;
name|addURL
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
decl||
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
name|noAddURL
argument_list|(
literal|"Method addURL not available"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|URL
argument_list|>
name|have
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|urlClassLoader
operator|.
name|getURLs
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Path
name|path
range|:
name|jars
control|)
block|{
try|try
block|{
name|URL
name|url
init|=
name|path
operator|.
name|toUri
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
if|if
condition|(
name|have
operator|.
name|add
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|addURL
operator|.
name|invoke
argument_list|(
name|cl
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
decl||
name|IllegalArgumentException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
name|noAddURL
argument_list|(
literal|"addURL "
operator|+
name|path
operator|+
literal|" failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|noAddURL
argument_list|(
literal|"addURL "
operator|+
name|path
operator|+
literal|" failed"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|loadJARs (Path jar)
specifier|public
specifier|static
name|void
name|loadJARs
parameter_list|(
name|Path
name|jar
parameter_list|)
block|{
name|loadJARs
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|jar
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|noAddURL (String m, Throwable why)
specifier|private
specifier|static
name|UnsupportedOperationException
name|noAddURL
parameter_list|(
name|String
name|m
parameter_list|,
name|Throwable
name|why
parameter_list|)
block|{
name|String
name|prefix
init|=
literal|"Cannot extend classpath: "
decl_stmt|;
return|return
operator|new
name|UnsupportedOperationException
argument_list|(
name|prefix
operator|+
name|m
argument_list|,
name|why
argument_list|)
return|;
block|}
DECL|method|IoUtil ()
specifier|private
name|IoUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

