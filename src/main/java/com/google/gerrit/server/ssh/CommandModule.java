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
name|CommandFactory
operator|.
name|Command
import|;
end_import

begin_comment
comment|/** Module to register commands in the SSH daemon. */
end_comment

begin_class
DECL|class|CommandModule
specifier|public
specifier|abstract
class|class
name|CommandModule
extends|extends
name|AbstractModule
block|{
comment|/**    * Configure a command to be invoked by name.    *    * @param name the name of the command the client will provide in order to    *        call the command.    * @return a binding that must be bound to a non-singleton provider for a    *         {@link Command} object.    */
DECL|method|command (final String name)
specifier|protected
name|LinkedBindingBuilder
argument_list|<
name|Command
argument_list|>
name|command
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Configure a command to be invoked by name.    *    * @param name the name of the command the client will provide in order to    *        call the command.    * @return a binding that must be bound to a non-singleton provider for a    *         {@link Command} object.    */
DECL|method|command (final CommandName name)
specifier|protected
name|LinkedBindingBuilder
argument_list|<
name|Command
argument_list|>
name|command
parameter_list|(
specifier|final
name|CommandName
name|name
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Configure a command to be invoked by name.    *    *@param parent context of the parent command, that this command is a    *        subcommand of.    * @param name the name of the command the client will provide in order to    *        call the command.    * @return a binding that must be bound to a non-singleton provider for a    *         {@link Command} object.    */
DECL|method|command (final CommandName parent, final String name)
specifier|protected
name|LinkedBindingBuilder
argument_list|<
name|Command
argument_list|>
name|command
parameter_list|(
specifier|final
name|CommandName
name|parent
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Alias one command to another.    *    * @param from the new command name that when called will actually delegate to    *        {@code to}'s implementation.    * @param to name of an already registered command that will perform the    *        action when {@code from} is invoked by a client.    */
DECL|method|alias (final String from, final String to)
specifier|protected
name|void
name|alias
parameter_list|(
specifier|final
name|String
name|from
parameter_list|,
specifier|final
name|String
name|to
parameter_list|)
block|{
name|bind
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|from
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|to
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

