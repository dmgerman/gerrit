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
DECL|package|com.google.gerrit.extensions.registration
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
package|;
end_package

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
name|events
operator|.
name|LifecycleListener
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
name|TypeLiteral
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
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**<b>DO NOT USE</b> */
end_comment

begin_class
DECL|class|PrivateInternals_DynamicTypes
specifier|public
class|class
name|PrivateInternals_DynamicTypes
block|{
DECL|method|dynamicItemsOf (Injector src)
specifier|public
specifier|static
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicItem
argument_list|<
name|?
argument_list|>
argument_list|>
name|dynamicItemsOf
parameter_list|(
name|Injector
name|src
parameter_list|)
block|{
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicItem
argument_list|<
name|?
argument_list|>
argument_list|>
name|m
init|=
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
name|e
range|:
name|src
operator|.
name|getBindings
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|type
init|=
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|getTypeLiteral
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getRawType
argument_list|()
operator|==
name|DynamicItem
operator|.
name|class
condition|)
block|{
name|ParameterizedType
name|p
init|=
operator|(
name|ParameterizedType
operator|)
name|type
operator|.
name|getType
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|TypeLiteral
operator|.
name|get
argument_list|(
name|p
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
argument_list|,
operator|(
name|DynamicItem
argument_list|<
name|?
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|m
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
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
DECL|method|dynamicSetsOf (Injector src)
specifier|public
specifier|static
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicSet
argument_list|<
name|?
argument_list|>
argument_list|>
name|dynamicSetsOf
parameter_list|(
name|Injector
name|src
parameter_list|)
block|{
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicSet
argument_list|<
name|?
argument_list|>
argument_list|>
name|m
init|=
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
name|e
range|:
name|src
operator|.
name|getBindings
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|type
init|=
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|getTypeLiteral
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getRawType
argument_list|()
operator|==
name|DynamicSet
operator|.
name|class
condition|)
block|{
name|ParameterizedType
name|p
init|=
operator|(
name|ParameterizedType
operator|)
name|type
operator|.
name|getType
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|TypeLiteral
operator|.
name|get
argument_list|(
name|p
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
argument_list|,
operator|(
name|DynamicSet
argument_list|<
name|?
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|m
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
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
DECL|method|dynamicMapsOf (Injector src)
specifier|public
specifier|static
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicMap
argument_list|<
name|?
argument_list|>
argument_list|>
name|dynamicMapsOf
parameter_list|(
name|Injector
name|src
parameter_list|)
block|{
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicMap
argument_list|<
name|?
argument_list|>
argument_list|>
name|m
init|=
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
name|e
range|:
name|src
operator|.
name|getBindings
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|type
init|=
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|getTypeLiteral
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getRawType
argument_list|()
operator|==
name|DynamicMap
operator|.
name|class
condition|)
block|{
name|ParameterizedType
name|p
init|=
operator|(
name|ParameterizedType
operator|)
name|type
operator|.
name|getType
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|TypeLiteral
operator|.
name|get
argument_list|(
name|p
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
argument_list|,
operator|(
name|DynamicMap
argument_list|<
name|?
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|m
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
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
DECL|method|attachItems ( Injector src, Map<TypeLiteral<?>, DynamicItem<?>> items, String pluginName)
specifier|public
specifier|static
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|attachItems
parameter_list|(
name|Injector
name|src
parameter_list|,
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicItem
argument_list|<
name|?
argument_list|>
argument_list|>
name|items
parameter_list|,
name|String
name|pluginName
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
operator|||
name|items
operator|==
literal|null
operator|||
name|items
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|handles
init|=
operator|new
name|ArrayList
argument_list|<
name|RegistrationHandle
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
try|try
block|{
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
name|DynamicItem
argument_list|<
name|?
argument_list|>
argument_list|>
name|e
range|:
name|items
operator|.
name|entrySet
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
name|DynamicItem
argument_list|<
name|Object
argument_list|>
name|item
init|=
operator|(
name|DynamicItem
argument_list|<
name|Object
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Binding
argument_list|<
name|Object
argument_list|>
name|b
range|:
name|bindings
argument_list|(
name|src
argument_list|,
name|type
argument_list|)
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getKey
argument_list|()
operator|.
name|getAnnotation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|handles
operator|.
name|add
argument_list|(
name|item
operator|.
name|set
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|,
name|b
operator|.
name|getProvider
argument_list|()
argument_list|,
name|pluginName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|remove
argument_list|(
name|handles
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|remove
argument_list|(
name|handles
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
name|handles
return|;
block|}
DECL|method|attachSets ( Injector src, Map<TypeLiteral<?>, DynamicSet<?>> sets)
specifier|public
specifier|static
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|attachSets
parameter_list|(
name|Injector
name|src
parameter_list|,
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicSet
argument_list|<
name|?
argument_list|>
argument_list|>
name|sets
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
operator|||
name|sets
operator|==
literal|null
operator|||
name|sets
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|handles
init|=
operator|new
name|ArrayList
argument_list|<
name|RegistrationHandle
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
try|try
block|{
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
name|DynamicSet
argument_list|<
name|?
argument_list|>
argument_list|>
name|e
range|:
name|sets
operator|.
name|entrySet
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
name|DynamicSet
argument_list|<
name|Object
argument_list|>
name|set
init|=
operator|(
name|DynamicSet
argument_list|<
name|Object
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Binding
argument_list|<
name|Object
argument_list|>
name|b
range|:
name|bindings
argument_list|(
name|src
argument_list|,
name|type
argument_list|)
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getKey
argument_list|()
operator|.
name|getAnnotation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|handles
operator|.
name|add
argument_list|(
name|set
operator|.
name|add
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|,
name|b
operator|.
name|getProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|remove
argument_list|(
name|handles
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|remove
argument_list|(
name|handles
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
name|handles
return|;
block|}
DECL|method|attachMaps ( Injector src, String groupName, Map<TypeLiteral<?>, DynamicMap<?>> maps)
specifier|public
specifier|static
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|attachMaps
parameter_list|(
name|Injector
name|src
parameter_list|,
name|String
name|groupName
parameter_list|,
name|Map
argument_list|<
name|TypeLiteral
argument_list|<
name|?
argument_list|>
argument_list|,
name|DynamicMap
argument_list|<
name|?
argument_list|>
argument_list|>
name|maps
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
operator|||
name|maps
operator|==
literal|null
operator|||
name|maps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|handles
init|=
operator|new
name|ArrayList
argument_list|<
name|RegistrationHandle
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
try|try
block|{
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
name|DynamicMap
argument_list|<
name|?
argument_list|>
argument_list|>
name|e
range|:
name|maps
operator|.
name|entrySet
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
name|PrivateInternals_DynamicMapImpl
argument_list|<
name|Object
argument_list|>
name|set
init|=
operator|(
name|PrivateInternals_DynamicMapImpl
argument_list|<
name|Object
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Binding
argument_list|<
name|Object
argument_list|>
name|b
range|:
name|bindings
argument_list|(
name|src
argument_list|,
name|type
argument_list|)
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getKey
argument_list|()
operator|.
name|getAnnotation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|handles
operator|.
name|add
argument_list|(
name|set
operator|.
name|put
argument_list|(
name|groupName
argument_list|,
name|b
operator|.
name|getKey
argument_list|()
argument_list|,
name|b
operator|.
name|getProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|remove
argument_list|(
name|handles
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|remove
argument_list|(
name|handles
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
name|handles
return|;
block|}
DECL|method|registerInParentInjectors ()
specifier|public
specifier|static
name|LifecycleListener
name|registerInParentInjectors
parameter_list|()
block|{
return|return
operator|new
name|LifecycleListener
argument_list|()
block|{
specifier|private
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|handles
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|Injector
name|self
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{
name|handles
operator|=
operator|new
name|ArrayList
argument_list|<
name|RegistrationHandle
argument_list|>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|Injector
name|parent
init|=
name|self
operator|.
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|handles
operator|.
name|addAll
argument_list|(
name|attachSets
argument_list|(
name|self
argument_list|,
name|dynamicSetsOf
argument_list|(
name|parent
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|handles
operator|.
name|addAll
argument_list|(
name|attachMaps
argument_list|(
name|self
argument_list|,
literal|"gerrit"
argument_list|,
name|dynamicMapsOf
argument_list|(
name|parent
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|parent
operator|=
name|parent
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|handles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|handles
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|remove
argument_list|(
name|handles
argument_list|)
expr_stmt|;
name|handles
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|remove (List<RegistrationHandle> handles)
specifier|private
specifier|static
name|void
name|remove
parameter_list|(
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|handles
parameter_list|)
block|{
if|if
condition|(
name|handles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RegistrationHandle
name|handle
range|:
name|handles
control|)
block|{
name|handle
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|newHashMap ()
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newHashMap
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
DECL|method|bindings (Injector src, TypeLiteral<T> type)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|Binding
argument_list|<
name|T
argument_list|>
argument_list|>
name|bindings
parameter_list|(
name|Injector
name|src
parameter_list|,
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|src
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

