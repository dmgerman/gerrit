begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.plugincontext
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugincontext
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Iterators
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
name|DynamicMap
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
name|Extension
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
name|plugincontext
operator|.
name|PluginContext
operator|.
name|ExtensionConsumer
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
name|plugincontext
operator|.
name|PluginContext
operator|.
name|PluginMetrics
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
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * Context to invoke extensions from a {@link DynamicMap}.  *  *<p>When a plugin extension is invoked a logging tag with the plugin name is set. This way any  * errors that are triggered by the plugin extension (even if they happen in Gerrit code which is  * called by the plugin extension) can be easily attributed to the plugin.  *  *<p>Example if all exceptions should be caught and logged:  *  *<pre>  * Map<String, Object> results = new HashMap<>();  * fooPluginMapContext.runEach(  *     extension -> results.put(extension.getExportName(), extension.get().getFoo());  *</pre>  *  *<p>Example if all exceptions, but one, should be caught and logged:  *  *<pre>  * Map<String, Object> results = new HashMap<>();  * try {  *   fooPluginMapContext.runEach(  *       extension -> results.put(extension.getExportName(), extension.get().getFoo(),  *       MyException.class);  * } catch (MyException e) {  *   // handle the exception  * }  *</pre>  *  *<p>Example if return values should be handled:  *  *<pre>  * Map<String, Object> results = new HashMap<>();  * for (PluginMapEntryContext<Foo> c : fooPluginMapContext) {  *   if (c.call(extension -> extension.get().handles(x))) {  *     c.run(extension -> results.put(extension.getExportName(), extension.get().getFoo());  *   }  * }  *</pre>  *  *<p>Example if return values and a single exception should be handled:  *  *<pre>  * Map<String, Object> results = new HashMap<>();  * try {  *   for (PluginMapEntryContext<Foo> c : fooPluginMapContext) {  *     if (c.call(extension -> extension.handles(x), MyException.class)) {  *       c.run(extension -> results.put(extension.getExportName(), extension.get().getFoo(),  *           MyException.class);  *     }  *   }  * } catch (MyException e) {  *   // handle the exception  * }  *</pre>  *  *<p>Example if several exceptions should be handled:  *  *<pre>  * for (Extension<Foo> fooExtension : fooDynamicMap) {  *   try (TraceContext traceContext = PluginContext.newTrace(fooExtension)) {  *     fooExtension.get().doFoo();  *   } catch (MyException1 | MyException2 | MyException3 e) {  *     // handle the exception  *   }  * }  *</pre>  */
end_comment

begin_class
DECL|class|PluginMapContext
specifier|public
class|class
name|PluginMapContext
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterable
argument_list|<
name|PluginMapEntryContext
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
DECL|field|dynamicMap
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|T
argument_list|>
name|dynamicMap
decl_stmt|;
DECL|field|pluginMetrics
specifier|private
specifier|final
name|PluginMetrics
name|pluginMetrics
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|PluginMapContext (DynamicMap<T> dynamicMap, PluginMetrics pluginMetrics)
specifier|public
name|PluginMapContext
parameter_list|(
name|DynamicMap
argument_list|<
name|T
argument_list|>
name|dynamicMap
parameter_list|,
name|PluginMetrics
name|pluginMetrics
parameter_list|)
block|{
name|this
operator|.
name|dynamicMap
operator|=
name|dynamicMap
expr_stmt|;
name|this
operator|.
name|pluginMetrics
operator|=
name|pluginMetrics
expr_stmt|;
block|}
comment|/**    * Iterator that provides contexts for invoking the extensions in this map.    *    *<p>This is useful if:    *    *<ul>    *<li>invoking of each extension returns a result that should be handled    *<li>a sequence of invocations should be done on each extension    *</ul>    */
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|PluginMapEntryContext
argument_list|<
name|T
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|transform
argument_list|(
name|dynamicMap
operator|.
name|iterator
argument_list|()
argument_list|,
name|e
lambda|->
operator|new
name|PluginMapEntryContext
argument_list|<>
argument_list|(
name|e
argument_list|,
name|pluginMetrics
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Checks if no implementations for this extension point have been registered.    *    * @return {@code true} if no implementations for this extension point have been registered,    *     otherwise {@code false}    */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
operator|!
name|dynamicMap
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
comment|/**    * Returns a sorted list of the plugins that have registered implementations for this extension    * point.    *    * @return sorted list of the plugins that have registered implementations for this extension    *     point    */
DECL|method|plugins ()
specifier|public
name|SortedSet
argument_list|<
name|String
argument_list|>
name|plugins
parameter_list|()
block|{
return|return
name|dynamicMap
operator|.
name|plugins
argument_list|()
return|;
block|}
comment|/**    * Invokes each extension in the map. All exceptions from the plugin extensions are caught and    * logged.    *    *<p>The consumer get the {@link Extension} provided that should be invoked. The extension    * provides access to the plugin name and the export name.    *    *<p>All extension in the map are invoked, even if invoking some of the extensions failed.    *    * @param extensionConsumer consumer that invokes the extension    */
DECL|method|runEach (ExtensionConsumer<Extension<T>> extensionConsumer)
specifier|public
name|void
name|runEach
parameter_list|(
name|ExtensionConsumer
argument_list|<
name|Extension
argument_list|<
name|T
argument_list|>
argument_list|>
name|extensionConsumer
parameter_list|)
block|{
name|dynamicMap
operator|.
name|forEach
argument_list|(
name|p
lambda|->
name|PluginContext
operator|.
name|runLogExceptions
argument_list|(
name|pluginMetrics
argument_list|,
name|p
argument_list|,
name|extensionConsumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Invokes each extension in the map. All exceptions from the plugin extensions except exceptions    * of the specified type are caught and logged.    *    *<p>The consumer get the {@link Extension} provided that should be invoked. The extension    * provides access to the plugin name and the export name.    *    *<p>All extension in the map are invoked, even if invoking some of the extensions failed.    *    * @param extensionConsumer consumer that invokes the extension    * @param exceptionClass type of the exceptions that should be thrown    * @throws X expected exception from the plugin extension    */
DECL|method|runEach ( ExtensionConsumer<Extension<T>> extensionConsumer, Class<X> exceptionClass)
specifier|public
parameter_list|<
name|X
extends|extends
name|Exception
parameter_list|>
name|void
name|runEach
parameter_list|(
name|ExtensionConsumer
argument_list|<
name|Extension
argument_list|<
name|T
argument_list|>
argument_list|>
name|extensionConsumer
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionClass
parameter_list|)
throws|throws
name|X
block|{
for|for
control|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
range|:
name|dynamicMap
control|)
block|{
name|PluginContext
operator|.
name|runLogExceptions
argument_list|(
name|pluginMetrics
argument_list|,
name|extension
argument_list|,
name|extensionConsumer
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

