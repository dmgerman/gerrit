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
DECL|package|com.google.gerrit.util.cli
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|cli
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|FactoryModuleBuilder
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
name|util
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|OptionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|Setter
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
name|Type
import|;
end_import

begin_comment
comment|/** Utilities to support creating OptionHandler instances. */
end_comment

begin_class
DECL|class|OptionHandlerUtil
specifier|public
class|class
name|OptionHandlerUtil
block|{
comment|/** Generate a key for an {@link OptionHandlerFactory} in Guice. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|keyFor (final Class<T> valueType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Key
argument_list|<
name|OptionHandlerFactory
argument_list|<
name|T
argument_list|>
argument_list|>
name|keyFor
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|valueType
parameter_list|)
block|{
specifier|final
name|Type
name|factoryType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|OptionHandlerFactory
operator|.
name|class
argument_list|,
name|valueType
argument_list|)
decl_stmt|;
return|return
operator|(
name|Key
argument_list|<
name|OptionHandlerFactory
argument_list|<
name|T
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|factoryType
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|handlerOf (Class<T> type)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Key
argument_list|<
name|OptionHandler
argument_list|<
name|T
argument_list|>
argument_list|>
name|handlerOf
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
specifier|final
name|Type
name|handlerType
init|=
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
literal|null
argument_list|,
name|OptionHandler
operator|.
name|class
argument_list|,
name|type
argument_list|)
decl_stmt|;
return|return
operator|(
name|Key
argument_list|<
name|OptionHandler
argument_list|<
name|T
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|handlerType
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|moduleFor (final Class<T> type, Class<? extends OptionHandler<T>> impl)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Module
name|moduleFor
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|OptionHandler
argument_list|<
name|T
argument_list|>
argument_list|>
name|impl
parameter_list|)
block|{
return|return
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|implement
argument_list|(
name|handlerOf
argument_list|(
name|type
argument_list|)
argument_list|,
name|impl
argument_list|)
operator|.
name|build
argument_list|(
name|keyFor
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
DECL|method|OptionHandlerUtil ()
specifier|private
name|OptionHandlerUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

