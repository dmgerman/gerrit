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
name|collect
operator|.
name|Maps
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
name|RegistrationHandle
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
name|Binding
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
name|TypeLiteral
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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * Creates DispatchCommand using commands registered by {@link CommandModule}.  */
end_comment

begin_class
DECL|class|DispatchCommandProvider
specifier|public
class|class
name|DispatchCommandProvider
implements|implements
name|Provider
argument_list|<
name|DispatchCommand
argument_list|>
block|{
annotation|@
name|Inject
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Inject
DECL|field|factory
specifier|private
name|DispatchCommand
operator|.
name|Factory
name|factory
decl_stmt|;
DECL|field|dispatcherName
specifier|private
specifier|final
name|String
name|dispatcherName
decl_stmt|;
DECL|field|parent
specifier|private
specifier|final
name|CommandName
name|parent
decl_stmt|;
DECL|field|map
specifier|private
specifier|volatile
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|Command
argument_list|>
argument_list|>
name|map
decl_stmt|;
DECL|method|DispatchCommandProvider (final CommandName cn)
specifier|public
name|DispatchCommandProvider
parameter_list|(
specifier|final
name|CommandName
name|cn
parameter_list|)
block|{
name|this
argument_list|(
name|Commands
operator|.
name|nameOf
argument_list|(
name|cn
argument_list|)
argument_list|,
name|cn
argument_list|)
expr_stmt|;
block|}
DECL|method|DispatchCommandProvider (final String dispatcherName, final CommandName cn)
specifier|public
name|DispatchCommandProvider
parameter_list|(
specifier|final
name|String
name|dispatcherName
parameter_list|,
specifier|final
name|CommandName
name|cn
parameter_list|)
block|{
name|this
operator|.
name|dispatcherName
operator|=
name|dispatcherName
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|cn
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|DispatchCommand
name|get
parameter_list|()
block|{
return|return
name|factory
operator|.
name|create
argument_list|(
name|dispatcherName
argument_list|,
name|getMap
argument_list|()
argument_list|)
return|;
block|}
DECL|method|register (final CommandName name, final Provider<Command> cmd)
specifier|public
name|RegistrationHandle
name|register
parameter_list|(
specifier|final
name|CommandName
name|name
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|Command
argument_list|>
name|cmd
parameter_list|)
block|{
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|Command
argument_list|>
argument_list|>
name|m
init|=
name|getMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|putIfAbsent
argument_list|(
name|name
operator|.
name|value
argument_list|()
argument_list|,
name|cmd
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|name
operator|.
name|value
argument_list|()
operator|+
literal|" exists"
argument_list|)
throw|;
block|}
return|return
operator|new
name|RegistrationHandle
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|m
operator|.
name|remove
argument_list|(
name|name
operator|.
name|value
argument_list|()
argument_list|,
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|replace (final CommandName name, final Provider<Command> cmd)
specifier|public
name|RegistrationHandle
name|replace
parameter_list|(
specifier|final
name|CommandName
name|name
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|Command
argument_list|>
name|cmd
parameter_list|)
block|{
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|Command
argument_list|>
argument_list|>
name|m
init|=
name|getMap
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|name
operator|.
name|value
argument_list|()
argument_list|,
name|cmd
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegistrationHandle
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|m
operator|.
name|remove
argument_list|(
name|name
operator|.
name|value
argument_list|()
argument_list|,
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getMap ()
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|Command
argument_list|>
argument_list|>
name|getMap
parameter_list|()
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
name|createMap
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createMap ()
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|Command
argument_list|>
argument_list|>
name|createMap
parameter_list|()
block|{
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|Command
argument_list|>
argument_list|>
name|m
init|=
name|Maps
operator|.
name|newConcurrentMap
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Binding
argument_list|<
name|?
argument_list|>
name|b
range|:
name|allCommands
argument_list|()
control|)
block|{
specifier|final
name|Annotation
name|annotation
init|=
name|b
operator|.
name|getKey
argument_list|()
operator|.
name|getAnnotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|annotation
operator|instanceof
name|CommandName
condition|)
block|{
specifier|final
name|CommandName
name|n
init|=
operator|(
name|CommandName
operator|)
name|annotation
decl_stmt|;
if|if
condition|(
operator|!
name|Commands
operator|.
name|CMD_ROOT
operator|.
name|equals
argument_list|(
name|n
argument_list|)
operator|&&
name|Commands
operator|.
name|isChild
argument_list|(
name|parent
argument_list|,
name|n
argument_list|)
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|n
operator|.
name|value
argument_list|()
argument_list|,
operator|(
name|Provider
argument_list|<
name|Command
argument_list|>
operator|)
name|b
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|m
return|;
block|}
DECL|field|type
specifier|private
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|Command
argument_list|>
name|type
init|=
operator|new
name|TypeLiteral
argument_list|<
name|Command
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|method|allCommands ()
specifier|private
name|List
argument_list|<
name|Binding
argument_list|<
name|Command
argument_list|>
argument_list|>
name|allCommands
parameter_list|()
block|{
return|return
name|injector
operator|.
name|findBindingsByType
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

