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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|DynamicItem
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
name|CheckedExtensionImplFunction
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
name|ExtensionImplConsumer
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
name|ExtensionImplFunction
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

begin_comment
comment|/**  * Context to invoke an extension from a {@link DynamicItem}.  *  *<p>When the plugin extension is invoked a logging tag with the plugin name is set. This way any  * errors that are triggered by the plugin extension (even if they happen in Gerrit code which is  * called by the plugin extension) can be easily attributed to the plugin.  *  *<p>The run* methods execute an extension but don't deliver a result back to the caller.  * Exceptions can be caught and logged.  *  *<p>The call* methods execute an extension and deliver a result back to the caller.  *  *<p>Example if all exceptions should be caught and logged:  *  *<pre>  * fooPluginItemContext.run(foo -> foo.doFoo());  *</pre>  *  *<p>Example if all exceptions, but one, should be caught and logged:  *  *<pre>  * try {  *   fooPluginItemContext.run(foo -> foo.doFoo(), MyException.class);  * } catch (MyException e) {  *   // handle the exception  * }  *</pre>  *  *<p>Example if return values should be handled:  *  *<pre>  * Object result = fooPluginItemContext.call(foo -> foo.getFoo());  *</pre>  *  *<p>Example if return values and a single exception should be handled:  *  *<pre>  * Object result;  * try {  *   result = fooPluginItemContext.call(foo -> foo.getFoo(), MyException.class);  * } catch (MyException e) {  *   // handle the exception  * }  *</pre>  *  *<p>Example if several exceptions should be handled:  *  *<pre>  * try (TraceContext traceContext = PluginContext.newTrace(fooDynamicItem.getEntry())) {  *   fooDynamicItem.get().doFoo();  * } catch (MyException1 | MyException2 | MyException3 e) {  *   // handle the exception  * }  *</pre>  */
end_comment

begin_class
DECL|class|PluginItemContext
specifier|public
class|class
name|PluginItemContext
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|dynamicItem
annotation|@
name|Nullable
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|T
argument_list|>
name|dynamicItem
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|PluginItemContext (DynamicItem<T> dynamicItem)
specifier|public
name|PluginItemContext
parameter_list|(
name|DynamicItem
argument_list|<
name|T
argument_list|>
name|dynamicItem
parameter_list|)
block|{
name|this
operator|.
name|dynamicItem
operator|=
name|dynamicItem
expr_stmt|;
block|}
comment|/**    * Returns the name of the plugin that registered the extension.    *    * @return the plugin name, {@code null} if no implementation is registered for this extension    *     point    */
annotation|@
name|Nullable
DECL|method|getPluginName ()
specifier|public
name|String
name|getPluginName
parameter_list|()
block|{
return|return
name|dynamicItem
operator|.
name|getPluginName
argument_list|()
return|;
block|}
comment|/**    * Invokes the plugin extension of the item. All exceptions from the plugin extension are caught    * and logged.    *    *<p>The consumer gets the extension implementation provided that should be invoked.    *    *<p>No-op if no implementation is registered for this extension point.    *    * @param extensionImplConsumer consumer that invokes the extension    */
DECL|method|run (ExtensionImplConsumer<T> extensionImplConsumer)
specifier|public
name|void
name|run
parameter_list|(
name|ExtensionImplConsumer
argument_list|<
name|T
argument_list|>
name|extensionImplConsumer
parameter_list|)
block|{
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
init|=
name|dynamicItem
operator|.
name|getEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|extension
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|PluginContext
operator|.
name|runLogExceptions
argument_list|(
name|extension
argument_list|,
name|extensionImplConsumer
argument_list|)
expr_stmt|;
block|}
comment|/**    * Invokes the plugin extension of the item. All exceptions from the plugin extension are caught    * and logged.    *    *<p>The consumer gets the extension implementation provided that should be invoked.    *    *<p>No-op if no implementation is registered for this extension point.    *    * @param extensionImplConsumer consumer that invokes the extension    * @param exceptionClass type of the exceptions that should be thrown    * @throws X expected exception from the plugin extension    */
DECL|method|run ( ExtensionImplConsumer<T> extensionImplConsumer, Class<X> exceptionClass)
specifier|public
parameter_list|<
name|X
extends|extends
name|Exception
parameter_list|>
name|void
name|run
parameter_list|(
name|ExtensionImplConsumer
argument_list|<
name|T
argument_list|>
name|extensionImplConsumer
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
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
init|=
name|dynamicItem
operator|.
name|getEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|extension
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|PluginContext
operator|.
name|runLogExceptions
argument_list|(
name|extension
argument_list|,
name|extensionImplConsumer
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
block|}
comment|/**    * Calls the plugin extension of the item and returns the result from the plugin extension call.    *    *<p>The function gets the extension implementation provided that should be invoked.    *    *<p>Fails with {@link IllegalStateException} if no implementation is registered for the item.    *    * @param extensionImplFunction function that invokes the extension    * @return the result from the plugin extension    * @throws IllegalStateException if no implementation is registered for the item    */
DECL|method|call (ExtensionImplFunction<T, R> extensionImplFunction)
specifier|public
parameter_list|<
name|R
parameter_list|>
name|R
name|call
parameter_list|(
name|ExtensionImplFunction
argument_list|<
name|T
argument_list|,
name|R
argument_list|>
name|extensionImplFunction
parameter_list|)
block|{
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
init|=
name|dynamicItem
operator|.
name|getEntry
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|extension
operator|!=
literal|null
argument_list|)
expr_stmt|;
return|return
name|PluginContext
operator|.
name|call
argument_list|(
name|extension
argument_list|,
name|extensionImplFunction
argument_list|)
return|;
block|}
comment|/**    * Calls the plugin extension of the item and returns the result from the plugin extension call.    * Exceptions of the specified type are thrown and must be handled by the caller.    *    *<p>The function gets the extension implementation provided that should be invoked.    *    *<p>Fails with {@link IllegalStateException} if no implementation is registered for the item.    *    * @param checkedExtensionImplFunction function that invokes the extension    * @param exceptionClass type of the exceptions that should be thrown    * @return the result from the plugin extension    * @throws X expected exception from the plugin extension    * @throws IllegalStateException if no implementation is registered for the item    */
DECL|method|call ( CheckedExtensionImplFunction<T, R, X> checkedExtensionImplFunction, Class<X> exceptionClass)
specifier|public
parameter_list|<
name|R
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|R
name|call
parameter_list|(
name|CheckedExtensionImplFunction
argument_list|<
name|T
argument_list|,
name|R
argument_list|,
name|X
argument_list|>
name|checkedExtensionImplFunction
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
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
init|=
name|dynamicItem
operator|.
name|getEntry
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|extension
operator|!=
literal|null
argument_list|)
expr_stmt|;
return|return
name|PluginContext
operator|.
name|call
argument_list|(
name|extension
argument_list|,
name|checkedExtensionImplFunction
argument_list|,
name|exceptionClass
argument_list|)
return|;
block|}
block|}
end_class

end_unit

