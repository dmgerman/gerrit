begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

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
name|util
operator|.
name|EnumSet
import|;
end_import

begin_comment
comment|/** Enum that can be expressed as a bitset in query parameters. */
end_comment

begin_interface
DECL|interface|ListOption
specifier|public
interface|interface
name|ListOption
block|{
DECL|method|getValue ()
name|int
name|getValue
parameter_list|()
function_decl|;
DECL|method|fromBits (Class<T> clazz, int v)
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
operator|&
name|ListOption
parameter_list|>
name|EnumSet
argument_list|<
name|T
argument_list|>
name|fromBits
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|int
name|v
parameter_list|)
block|{
name|EnumSet
argument_list|<
name|T
argument_list|>
name|r
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|T
index|[]
name|values
decl_stmt|;
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
index|[]
name|tmp
init|=
operator|(
name|T
index|[]
operator|)
name|clazz
operator|.
name|getMethod
argument_list|(
literal|"values"
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|values
operator|=
name|tmp
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
decl||
name|NoSuchMethodException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
for|for
control|(
name|T
name|o
range|:
name|values
control|)
block|{
if|if
condition|(
operator|(
name|v
operator|&
operator|(
literal|1
operator|<<
name|o
operator|.
name|getValue
argument_list|()
operator|)
operator|)
operator|!=
literal|0
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|v
operator|&=
operator|~
operator|(
literal|1
operator|<<
name|o
operator|.
name|getValue
argument_list|()
operator|)
expr_stmt|;
block|}
if|if
condition|(
name|v
operator|==
literal|0
condition|)
block|{
return|return
name|r
return|;
block|}
block|}
if|if
condition|(
name|v
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|Integer
operator|.
name|toHexString
argument_list|(
name|v
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|r
return|;
block|}
block|}
end_interface

end_unit

