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
name|io
operator|.
name|ByteStreams
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_class
DECL|class|DelegatingClassLoader
specifier|public
class|class
name|DelegatingClassLoader
extends|extends
name|ClassLoader
block|{
DECL|field|target
specifier|private
specifier|final
name|ClassLoader
name|target
decl_stmt|;
DECL|method|DelegatingClassLoader (ClassLoader parent, ClassLoader target)
specifier|public
name|DelegatingClassLoader
parameter_list|(
name|ClassLoader
name|parent
parameter_list|,
name|ClassLoader
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|findClass (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|String
name|path
init|=
name|name
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
try|try
init|(
name|InputStream
name|resource
init|=
name|target
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
init|)
block|{
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|byte
index|[]
name|bytes
init|=
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|resource
argument_list|)
decl_stmt|;
return|return
name|defineClass
argument_list|(
name|name
argument_list|,
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// throws ClassNotFoundException later
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// throws ClassNotFoundException later
block|}
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getResource (String name)
specifier|public
name|URL
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|URL
name|rtn
init|=
name|getParent
argument_list|()
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|rtn
operator|==
literal|null
condition|)
block|{
name|rtn
operator|=
name|target
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|rtn
return|;
block|}
annotation|@
name|Override
DECL|method|getResources (String name)
specifier|public
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|rtn
init|=
name|getParent
argument_list|()
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|rtn
operator|==
literal|null
condition|)
block|{
name|rtn
operator|=
name|target
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|rtn
return|;
block|}
annotation|@
name|Override
DECL|method|getResourceAsStream (String name)
specifier|public
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|InputStream
name|rtn
init|=
name|getParent
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|rtn
operator|==
literal|null
condition|)
block|{
name|rtn
operator|=
name|target
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|rtn
return|;
block|}
block|}
end_class

end_unit

