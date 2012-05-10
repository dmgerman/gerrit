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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|Preconditions
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
name|annotations
operator|.
name|PluginName
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
name|sshd
operator|.
name|CommandName
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
name|sshd
operator|.
name|Commands
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
name|sshd
operator|.
name|DispatchCommandProvider
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
name|AbstractModule
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
name|binder
operator|.
name|LinkedBindingBuilder
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
name|Command
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
DECL|class|PluginCommandModule
specifier|public
specifier|abstract
class|class
name|PluginCommandModule
extends|extends
name|AbstractModule
block|{
DECL|field|command
specifier|private
name|CommandName
name|command
decl_stmt|;
annotation|@
name|Inject
DECL|method|setPluginName (@luginName String name)
name|void
name|setPluginName
parameter_list|(
annotation|@
name|PluginName
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|Commands
operator|.
name|named
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
specifier|final
name|void
name|configure
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
name|command
operator|!=
literal|null
argument_list|,
literal|"@PluginName must be provided"
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|command
argument_list|)
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|DispatchCommandProvider
argument_list|(
name|command
argument_list|)
argument_list|)
expr_stmt|;
name|configureCommands
argument_list|()
expr_stmt|;
block|}
DECL|method|configureCommands ()
specifier|protected
specifier|abstract
name|void
name|configureCommands
parameter_list|()
function_decl|;
DECL|method|command (String subCmd)
specifier|protected
name|LinkedBindingBuilder
argument_list|<
name|Command
argument_list|>
name|command
parameter_list|(
name|String
name|subCmd
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|command
argument_list|,
name|subCmd
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

