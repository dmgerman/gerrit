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

begin_comment
comment|/**  * Creates a CommandFactory using commands registered by {@link CommandModule}.  */
end_comment

begin_class
DECL|class|CommandFactoryProvider
class|class
name|CommandFactoryProvider
implements|implements
name|Provider
argument_list|<
name|CommandFactory
argument_list|>
block|{
DECL|field|SERVER
specifier|private
specifier|static
specifier|final
name|String
name|SERVER
init|=
literal|"Gerrit Code Review"
decl_stmt|;
DECL|field|dispatcher
specifier|private
specifier|final
name|DispatchCommandProvider
name|dispatcher
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommandFactoryProvider (final Injector i)
name|CommandFactoryProvider
parameter_list|(
specifier|final
name|Injector
name|i
parameter_list|)
block|{
name|dispatcher
operator|=
operator|new
name|DispatchCommandProvider
argument_list|(
name|i
argument_list|,
name|SERVER
argument_list|,
name|Commands
operator|.
name|ROOT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|CommandFactory
name|get
parameter_list|()
block|{
return|return
operator|new
name|CommandFactory
argument_list|()
block|{
specifier|public
name|Command
name|createCommand
parameter_list|(
specifier|final
name|String
name|requestCommand
parameter_list|)
block|{
specifier|final
name|DispatchCommand
name|c
init|=
name|dispatcher
operator|.
name|get
argument_list|()
decl_stmt|;
name|c
operator|.
name|setCommandLine
argument_list|(
name|requestCommand
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

