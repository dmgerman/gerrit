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
name|inject
operator|.
name|Binder
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
name|ProvisionException
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
name|Scopes
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
name|binder
operator|.
name|LinkedBindingBuilder
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
name|Providers
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * A single item that can be modified as plugins reload.  *  *<p>DynamicItems are always mapped as singletons in Guice. Items store a Provider internally, and  * resolve the provider to an instance on demand. This enables registrations to decide between  * singleton and non-singleton members. If multiple plugins try to provide the same Provider, an  * exception is thrown.  */
end_comment

begin_class
DECL|class|DynamicItem
specifier|public
class|class
name|DynamicItem
parameter_list|<
name|T
parameter_list|>
block|{
comment|/** Pair of provider implementation and plugin providing it. */
DECL|class|NamedProvider
specifier|static
class|class
name|NamedProvider
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|impl
specifier|final
name|Provider
argument_list|<
name|T
argument_list|>
name|impl
decl_stmt|;
DECL|field|pluginName
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|method|NamedProvider (Provider<T> provider, String pluginName)
name|NamedProvider
parameter_list|(
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
parameter_list|,
name|String
name|pluginName
parameter_list|)
block|{
name|this
operator|.
name|impl
operator|=
name|provider
expr_stmt|;
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
block|}
block|}
comment|/**    * Declare a singleton {@code DynamicItem<T>} with a binder.    *    *<p>Items must be defined in a Guice module before they can be bound:    *    *<pre>    *   DynamicItem.itemOf(binder(), Interface.class);    *   DynamicItem.bind(binder(), Interface.class).to(Impl.class);    *</pre>    *    * @param binder a new binder created in the module.    * @param member type of entry to store.    */
DECL|method|itemOf (Binder binder, Class<T> member)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|itemOf
parameter_list|(
name|Binder
name|binder
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|member
parameter_list|)
block|{
name|itemOf
argument_list|(
name|binder
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Declare a singleton {@code DynamicItem<T>} with a binder.    *    *<p>Items must be defined in a Guice module before they can be bound:    *    *<pre>    *   DynamicSet.itemOf(binder(), new TypeLiteral&lt;Thing&lt;Foo&gt;&gt;() {});    *</pre>    *    * @param binder a new binder created in the module.    * @param member type of entry to store.    */
DECL|method|itemOf (Binder binder, TypeLiteral<T> member)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|itemOf
parameter_list|(
name|Binder
name|binder
parameter_list|,
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|member
parameter_list|)
block|{
name|Key
argument_list|<
name|DynamicItem
argument_list|<
name|T
argument_list|>
argument_list|>
name|key
init|=
name|keyFor
argument_list|(
name|member
argument_list|)
decl_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|key
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|DynamicItemProvider
argument_list|<>
argument_list|(
name|member
argument_list|,
name|key
argument_list|)
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
block|}
comment|/**    * Construct a single {@code DynamicItem<T>} with a fixed value.    *    *<p>Primarily useful for passing {@code DynamicItem}s to constructors in tests.    *    * @param member type of item.    * @param item item to store.    */
DECL|method|itemOf (Class<T> member, T item)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|DynamicItem
argument_list|<
name|T
argument_list|>
name|itemOf
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|member
parameter_list|,
name|T
name|item
parameter_list|)
block|{
return|return
operator|new
name|DynamicItem
argument_list|<>
argument_list|(
name|keyFor
argument_list|(
name|TypeLiteral
operator|.
name|get
argument_list|(
name|member
argument_list|)
argument_list|)
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|item
argument_list|)
argument_list|,
literal|"gerrit"
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|keyFor (TypeLiteral<T> member)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Key
argument_list|<
name|DynamicItem
argument_list|<
name|T
argument_list|>
argument_list|>
name|keyFor
parameter_list|(
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|member
parameter_list|)
block|{
return|return
operator|(
name|Key
argument_list|<
name|DynamicItem
argument_list|<
name|T
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|DynamicItem
operator|.
name|class
argument_list|,
name|member
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Bind one implementation as the item using a unique annotation.    *    * @param binder a new binder created in the module.    * @param type type of entry to store.    * @return a binder to continue configuring the new item.    */
DECL|method|bind (Binder binder, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|LinkedBindingBuilder
argument_list|<
name|T
argument_list|>
name|bind
parameter_list|(
name|Binder
name|binder
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|bind
argument_list|(
name|binder
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Bind one implementation as the item.    *    * @param binder a new binder created in the module.    * @param type type of entry to store.    * @return a binder to continue configuring the new item.    */
DECL|method|bind (Binder binder, TypeLiteral<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|LinkedBindingBuilder
argument_list|<
name|T
argument_list|>
name|bind
parameter_list|(
name|Binder
name|binder
parameter_list|,
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|binder
operator|.
name|bind
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|field|key
specifier|private
specifier|final
name|Key
argument_list|<
name|DynamicItem
argument_list|<
name|T
argument_list|>
argument_list|>
name|key
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|NamedProvider
argument_list|<
name|T
argument_list|>
argument_list|>
name|ref
decl_stmt|;
DECL|method|DynamicItem (Key<DynamicItem<T>> key, Provider<T> provider, String pluginName)
name|DynamicItem
parameter_list|(
name|Key
argument_list|<
name|DynamicItem
argument_list|<
name|T
argument_list|>
argument_list|>
name|key
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
parameter_list|,
name|String
name|pluginName
parameter_list|)
block|{
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|in
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|in
operator|=
operator|new
name|NamedProvider
argument_list|<>
argument_list|(
name|provider
argument_list|,
name|pluginName
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|ref
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
comment|/**    * Get the configured item, or null.    *    * @return the configured item instance; null if no implementation has been bound to the item.    *     This is common if no plugin registered an implementation for the type.    */
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|item
init|=
name|ref
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|item
operator|!=
literal|null
condition|?
name|item
operator|.
name|impl
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**    * Set the element to provide.    *    * @param item the item to use. Must not be null.    * @param pluginName the name of the plugin providing the item.    * @return handle to remove the item at a later point in time.    */
DECL|method|set (T item, String pluginName)
specifier|public
name|RegistrationHandle
name|set
parameter_list|(
name|T
name|item
parameter_list|,
name|String
name|pluginName
parameter_list|)
block|{
return|return
name|set
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
name|item
argument_list|)
argument_list|,
name|pluginName
argument_list|)
return|;
block|}
comment|/**    * Set the element to provide.    *    * @param impl the item to add to the collection. Must not be null.    * @param pluginName name of the source providing the implementation.    * @return handle to remove the item at a later point in time.    */
DECL|method|set (Provider<T> impl, String pluginName)
specifier|public
name|RegistrationHandle
name|set
parameter_list|(
name|Provider
argument_list|<
name|T
argument_list|>
name|impl
parameter_list|,
name|String
name|pluginName
parameter_list|)
block|{
specifier|final
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|item
init|=
operator|new
name|NamedProvider
argument_list|<>
argument_list|(
name|impl
argument_list|,
name|pluginName
argument_list|)
decl_stmt|;
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|old
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|!
name|ref
operator|.
name|compareAndSet
argument_list|(
name|old
argument_list|,
name|item
argument_list|)
condition|)
block|{
name|old
operator|=
name|ref
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
operator|&&
operator|!
literal|"gerrit"
operator|.
name|equals
argument_list|(
name|old
operator|.
name|pluginName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s already provided by %s, ignoring plugin %s"
argument_list|,
name|key
operator|.
name|getTypeLiteral
argument_list|()
argument_list|,
name|old
operator|.
name|pluginName
argument_list|,
name|pluginName
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|final
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|defaultItem
init|=
name|old
decl_stmt|;
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
name|ref
operator|.
name|compareAndSet
argument_list|(
name|item
argument_list|,
name|defaultItem
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**    * Set the element that may be hot-replaceable in the future.    *    * @param key unique description from the item's Guice binding. This can be later obtained from    *     the registration handle to facilitate matching with the new equivalent instance during a    *     hot reload.    * @param impl the item to set as our value right now. Must not be null.    * @param pluginName the name of the plugin providing the item.    * @return a handle that can remove this item later, or hot-swap the item.    */
DECL|method|set (Key<T> key, Provider<T> impl, String pluginName)
specifier|public
name|ReloadableRegistrationHandle
argument_list|<
name|T
argument_list|>
name|set
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|impl
parameter_list|,
name|String
name|pluginName
parameter_list|)
block|{
specifier|final
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|item
init|=
operator|new
name|NamedProvider
argument_list|<>
argument_list|(
name|impl
argument_list|,
name|pluginName
argument_list|)
decl_stmt|;
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|old
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|!
name|ref
operator|.
name|compareAndSet
argument_list|(
name|old
argument_list|,
name|item
argument_list|)
condition|)
block|{
name|old
operator|=
name|ref
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
operator|&&
operator|!
literal|"gerrit"
operator|.
name|equals
argument_list|(
name|old
operator|.
name|pluginName
argument_list|)
operator|&&
operator|!
name|pluginName
operator|.
name|equals
argument_list|(
name|old
operator|.
name|pluginName
argument_list|)
condition|)
block|{
comment|// We allow to replace:
comment|// 1. Gerrit core items, e.g. websession cache
comment|//    can be replaced by plugin implementation
comment|// 2. Reload of current plugin
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s already provided by %s, ignoring plugin %s"
argument_list|,
name|this
operator|.
name|key
operator|.
name|getTypeLiteral
argument_list|()
argument_list|,
name|old
operator|.
name|pluginName
argument_list|,
name|pluginName
argument_list|)
argument_list|)
throw|;
block|}
block|}
return|return
operator|new
name|ReloadableHandle
argument_list|(
name|key
argument_list|,
name|item
argument_list|,
name|old
argument_list|)
return|;
block|}
DECL|class|ReloadableHandle
specifier|private
class|class
name|ReloadableHandle
implements|implements
name|ReloadableRegistrationHandle
argument_list|<
name|T
argument_list|>
block|{
DECL|field|handleKey
specifier|private
specifier|final
name|Key
argument_list|<
name|T
argument_list|>
name|handleKey
decl_stmt|;
DECL|field|item
specifier|private
specifier|final
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|item
decl_stmt|;
DECL|field|defaultItem
specifier|private
specifier|final
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|defaultItem
decl_stmt|;
DECL|method|ReloadableHandle (Key<T> handleKey, NamedProvider<T> item, NamedProvider<T> defaultItem)
name|ReloadableHandle
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|handleKey
parameter_list|,
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|item
parameter_list|,
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|defaultItem
parameter_list|)
block|{
name|this
operator|.
name|handleKey
operator|=
name|handleKey
expr_stmt|;
name|this
operator|.
name|item
operator|=
name|item
expr_stmt|;
name|this
operator|.
name|defaultItem
operator|=
name|defaultItem
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getKey ()
specifier|public
name|Key
argument_list|<
name|T
argument_list|>
name|getKey
parameter_list|()
block|{
return|return
name|handleKey
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|ref
operator|.
name|compareAndSet
argument_list|(
name|item
argument_list|,
name|defaultItem
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|replace (Key<T> newKey, Provider<T> newItem)
specifier|public
name|ReloadableHandle
name|replace
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|newKey
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|newItem
parameter_list|)
block|{
name|NamedProvider
argument_list|<
name|T
argument_list|>
name|n
init|=
operator|new
name|NamedProvider
argument_list|<>
argument_list|(
name|newItem
argument_list|,
name|item
operator|.
name|pluginName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|compareAndSet
argument_list|(
name|item
argument_list|,
name|n
argument_list|)
condition|)
block|{
return|return
operator|new
name|ReloadableHandle
argument_list|(
name|newKey
argument_list|,
name|n
argument_list|,
name|defaultItem
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit
