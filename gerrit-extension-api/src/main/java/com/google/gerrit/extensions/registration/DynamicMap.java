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
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|ConcurrentHashMap
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
comment|/**  * A map of members that can be modified as plugins reload.  *<p>  * Maps index their members by plugin name and export name.  *<p>  * DynamicMaps are always mapped as singletons in Guice. Maps store Providers  * internally, and resolve the provider to an instance on demand. This enables  * registrations to decide between singleton and non-singleton members.  */
end_comment

begin_class
DECL|class|DynamicMap
specifier|public
specifier|abstract
class|class
name|DynamicMap
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Declare a singleton {@code DynamicMap<T>} with a binder.    *<p>    * Maps must be defined in a Guice module before they can be bound:    *    *<pre>    * DynamicMap.mapOf(binder(), Interface.class);    * bind(Interface.class)    *   .annotatedWith(Exports.named(&quot;foo&quot;))    *   .to(Impl.class);    *</pre>    *    * @param binder a new binder created in the module.    * @param member type of value in the map.    */
DECL|method|mapOf (Binder binder, Class<T> member)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|mapOf
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
name|mapOf
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
comment|/**    * Declare a singleton {@code DynamicMap<T>} with a binder.    *<p>    * Maps must be defined in a Guice module before they can be bound:    *    *<pre>    * DynamicMap.mapOf(binder(), new TypeLiteral<Thing<Bar>>(){});    * bind(new TypeLiteral<Thing<Bar>>() {})    *   .annotatedWith(Exports.named(&quot;foo&quot;))    *   .to(Impl.class);    *</pre>    *    * @param binder a new binder created in the module.    * @param member type of value in the map.    */
DECL|method|mapOf (Binder binder, TypeLiteral<T> member)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|mapOf
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Key
argument_list|<
name|DynamicMap
argument_list|<
name|T
argument_list|>
argument_list|>
name|key
init|=
operator|(
name|Key
argument_list|<
name|DynamicMap
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
name|DynamicMap
operator|.
name|class
argument_list|,
name|member
operator|.
name|getType
argument_list|()
argument_list|)
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
name|DynamicMapProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|member
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
DECL|field|items
specifier|final
name|ConcurrentMap
argument_list|<
name|NamePair
argument_list|,
name|Provider
argument_list|<
name|T
argument_list|>
argument_list|>
name|items
decl_stmt|;
DECL|method|DynamicMap ()
name|DynamicMap
parameter_list|()
block|{
name|items
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|NamePair
argument_list|,
name|Provider
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|(
literal|16
comment|/* initial size */
argument_list|,
literal|0.75f
comment|/* load factor */
argument_list|,
literal|1
comment|/* concurrency level of 1, load/unload is single threaded */
argument_list|)
expr_stmt|;
block|}
comment|/**    * Lookup an implementation by name.    *    * @param pluginName local name of the plugin providing the item.    * @param exportName name the plugin exports the item as.    * @return the implementation. Null if the plugin is not running, or if the    *         plugin does not export this name.    * @throws ProvisionException if the registered provider is unable to obtain    *         an instance of the requested implementation.    */
DECL|method|get (String pluginName, String exportName)
specifier|public
name|T
name|get
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|exportName
parameter_list|)
block|{
name|Provider
argument_list|<
name|T
argument_list|>
name|p
init|=
name|items
operator|.
name|get
argument_list|(
operator|new
name|NamePair
argument_list|(
name|pluginName
argument_list|,
name|exportName
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|p
operator|!=
literal|null
condition|?
name|p
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**    * Get the names of all running plugins supplying this type.    *    * @return sorted set of active plugins that supply at least one item.    */
DECL|method|plugins ()
specifier|public
name|SortedSet
argument_list|<
name|String
argument_list|>
name|plugins
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|r
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|NamePair
name|p
range|:
name|items
operator|.
name|keySet
argument_list|()
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|p
operator|.
name|pluginName
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableSortedSet
argument_list|(
name|r
argument_list|)
return|;
block|}
comment|/**    * Get the items exported by a single plugin.    *    * @param pluginName name of the plugin.    * @return items exported by a plugin, keyed by the export name.    */
DECL|method|byPlugin (String pluginName)
specifier|public
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|T
argument_list|>
argument_list|>
name|byPlugin
parameter_list|(
name|String
name|pluginName
parameter_list|)
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|T
argument_list|>
argument_list|>
name|r
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|NamePair
argument_list|,
name|Provider
argument_list|<
name|T
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
if|if
condition|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|pluginName
operator|.
name|equals
argument_list|(
name|pluginName
argument_list|)
condition|)
block|{
name|r
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|exportName
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableSortedMap
argument_list|(
name|r
argument_list|)
return|;
block|}
DECL|class|NamePair
specifier|static
class|class
name|NamePair
block|{
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|exportName
specifier|private
specifier|final
name|String
name|exportName
decl_stmt|;
DECL|method|NamePair (String pn, String en)
name|NamePair
parameter_list|(
name|String
name|pn
parameter_list|,
name|String
name|en
parameter_list|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|pn
expr_stmt|;
name|this
operator|.
name|exportName
operator|=
name|en
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|pluginName
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
operator|+
name|exportName
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|NamePair
condition|)
block|{
name|NamePair
name|np
init|=
operator|(
name|NamePair
operator|)
name|other
decl_stmt|;
return|return
name|pluginName
operator|.
name|equals
argument_list|(
name|np
operator|.
name|pluginName
argument_list|)
operator|&&
name|exportName
operator|.
name|equals
argument_list|(
name|np
operator|.
name|exportName
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

