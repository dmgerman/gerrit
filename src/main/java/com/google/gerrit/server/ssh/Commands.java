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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|CommandFactory
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

begin_comment
comment|/** Utilities to support {@link CommandName} construction. */
end_comment

begin_class
DECL|class|Commands
specifier|public
class|class
name|Commands
block|{
DECL|method|key (final String name)
specifier|public
specifier|static
name|Key
argument_list|<
name|CommandFactory
operator|.
name|Command
argument_list|>
name|key
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|Key
operator|.
name|get
argument_list|(
name|CommandFactory
operator|.
name|Command
operator|.
name|class
argument_list|,
name|named
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/** Create a CommandName annotation for the supplied name. */
DECL|method|named (final String name)
specifier|public
specifier|static
name|CommandName
name|named
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|CommandName
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|()
block|{
return|return
name|CommandName
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|value
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|CommandName
operator|&&
name|value
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|CommandName
operator|)
name|obj
operator|)
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CommandName["
operator|+
name|value
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
DECL|method|Commands ()
specifier|private
name|Commands
parameter_list|()
block|{   }
block|}
end_class

end_unit

