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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
operator|.
name|AutoRegisterUtil
operator|.
name|calculateBindAnnotation
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
name|common
operator|.
name|collect
operator|.
name|LinkedListMultimap
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
name|Maps
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
name|annotations
operator|.
name|Export
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
name|server
operator|.
name|plugins
operator|.
name|InvalidPluginException
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
name|server
operator|.
name|plugins
operator|.
name|ModuleGenerator
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
name|Module
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
name|Map
import|;
end_import

begin_class
DECL|class|SshAutoRegisterModuleGenerator
class|class
name|SshAutoRegisterModuleGenerator
extends|extends
name|AbstractModule
implements|implements
name|ModuleGenerator
block|{
DECL|field|commands
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|Command
argument_list|>
argument_list|>
name|commands
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Multimap
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|listeners
init|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|command
specifier|private
name|CommandName
name|command
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|Command
argument_list|>
argument_list|>
name|e
range|:
name|commands
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|bind
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|command
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|e
range|:
name|listeners
operator|.
name|entries
argument_list|()
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|TypeLiteral
argument_list|<
name|Object
argument_list|>
name|type
init|=
operator|(
name|TypeLiteral
argument_list|<
name|Object
argument_list|>
operator|)
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|Object
argument_list|>
name|impl
init|=
operator|(
name|Class
argument_list|<
name|Object
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Annotation
name|n
init|=
name|calculateBindAnnotation
argument_list|(
name|impl
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|type
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|n
argument_list|)
operator|.
name|to
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setPluginName (String name)
specifier|public
name|void
name|setPluginName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|export (Export export, Class<?> type)
specifier|public
name|void
name|export
parameter_list|(
name|Export
name|export
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
name|command
operator|!=
literal|null
argument_list|,
literal|"pluginName must be provided"
argument_list|)
expr_stmt|;
if|if
condition|(
name|Command
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|Command
argument_list|>
name|old
init|=
name|commands
operator|.
name|get
argument_list|(
name|export
operator|.
name|value
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"@Export(\"%s\") has duplicate bindings:\n  %s\n  %s"
argument_list|,
name|export
operator|.
name|value
argument_list|()
argument_list|,
name|old
operator|.
name|getName
argument_list|()
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|commands
operator|.
name|put
argument_list|(
name|export
operator|.
name|value
argument_list|()
argument_list|,
operator|(
name|Class
argument_list|<
name|Command
argument_list|>
operator|)
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Class %s with @Export(\"%s\") must extend %s or implement %s"
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|export
operator|.
name|value
argument_list|()
argument_list|,
name|SshCommand
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Command
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|listen (TypeLiteral<?> tl, Class<?> clazz)
specifier|public
name|void
name|listen
parameter_list|(
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|tl
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|listeners
operator|.
name|put
argument_list|(
name|tl
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|Module
name|create
parameter_list|()
throws|throws
name|InvalidPluginException
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
name|command
operator|!=
literal|null
argument_list|,
literal|"pluginName must be provided"
argument_list|)
expr_stmt|;
return|return
operator|!
name|commands
operator|.
name|isEmpty
argument_list|()
condition|?
name|this
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

