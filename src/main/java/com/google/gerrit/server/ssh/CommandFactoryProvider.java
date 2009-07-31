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
name|CommandFactory
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
import|;
end_import

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
name|SshDaemonModule
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|injector
specifier|private
specifier|final
name|Injector
name|injector
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
name|injector
operator|=
name|i
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
name|GerritCommandFactory
argument_list|(
name|createMap
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createMap ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|AbstractCommand
argument_list|>
argument_list|>
name|createMap
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|AbstractCommand
argument_list|>
argument_list|>
name|m
decl_stmt|;
name|m
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|AbstractCommand
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|Binding
argument_list|<
name|?
argument_list|>
name|binding
range|:
name|allCommands
argument_list|()
control|)
block|{
specifier|final
name|Annotation
name|annotation
init|=
name|binding
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
operator|==
literal|null
operator|||
operator|!
operator|(
name|annotation
operator|instanceof
name|CommandName
operator|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"SSH command binding lacks @CommandName: "
operator|+
name|binding
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
specifier|final
name|CommandName
name|name
init|=
operator|(
name|CommandName
operator|)
name|annotation
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
operator|(
name|Provider
argument_list|<
name|AbstractCommand
argument_list|>
operator|)
name|binding
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|m
argument_list|)
return|;
block|}
DECL|method|allCommands ()
specifier|private
name|List
argument_list|<
name|Binding
argument_list|<
name|AbstractCommand
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
operator|new
name|TypeLiteral
argument_list|<
name|AbstractCommand
argument_list|>
argument_list|()
block|{}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

