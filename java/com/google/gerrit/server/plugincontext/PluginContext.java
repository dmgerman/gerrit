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
name|checkNotNull
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
name|Throwables
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
name|flogger
operator|.
name|FluentLogger
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
name|DynamicSet
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
name|logging
operator|.
name|TraceContext
import|;
end_import

begin_comment
comment|/**  * Context for invoking plugin extensions.  *  *<p>Invoking a plugin extension through a PluginContext sets a logging tag with the plugin name is  * set. This way any errors that are triggered by the plugin extension (even if they happen in  * Gerrit code which is called by the plugin extension) can be easily attributed to the plugin.  *  *<p>If possible plugin extensions should be invoked through:  *  *<ul>  *<li>{@link PluginItemContext} for extensions from {@link DynamicItem}  *<li>{@link PluginSetContext} for extensions from {@link DynamicSet}  *<li>{@link PluginMapContext} for extensions from {@link DynamicMap}  *</ul>  *  *<p>A plugin context can be manually opened by invoking the newTrace methods. This should only be  * needed if an extension throws multiple exceptions that need to be handled:  *  *<pre>  * public interface Foo {  *   void doFoo() throws Exception1, Exception2, Exception3;  * }  *  * ...  *  * for (Extension<Foo> fooExtension : fooDynamicMap) {  *   try (TraceContext traceContext = PluginContext.newTrace(fooExtension)) {  *     fooExtension.get().doFoo();  *   }  * }  *</pre>  *  *<p>This class hosts static methods with generic functionality to invoke plugin extensions with a  * trace context that are commonly used by {@link PluginItemContext}, {@link PluginSetContext} and  * {@link PluginMapContext}.  *  *<p>The run* methods execute an extension but don't deliver a result back to the caller.  * Exceptions can be caught and logged.  *  *<p>The call* methods execute an extension and deliver a result back to the caller.  */
end_comment

begin_class
DECL|class|PluginContext
specifier|public
class|class
name|PluginContext
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
annotation|@
name|FunctionalInterface
DECL|interface|ExtensionImplConsumer
specifier|public
interface|interface
name|ExtensionImplConsumer
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|run (T t)
name|void
name|run
parameter_list|(
name|T
name|t
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ExtensionImplFunction
specifier|public
interface|interface
name|ExtensionImplFunction
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|>
block|{
DECL|method|call (T input)
name|R
name|call
parameter_list|(
name|T
name|input
parameter_list|)
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|CheckedExtensionImplFunction
specifier|public
interface|interface
name|CheckedExtensionImplFunction
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
block|{
DECL|method|call (T input)
name|R
name|call
parameter_list|(
name|T
name|input
parameter_list|)
throws|throws
name|X
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ExtensionConsumer
specifier|public
interface|interface
name|ExtensionConsumer
parameter_list|<
name|T
extends|extends
name|Extension
parameter_list|<
name|?
parameter_list|>
parameter_list|>
block|{
DECL|method|run (T extension)
name|void
name|run
parameter_list|(
name|T
name|extension
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ExtensionFunction
specifier|public
interface|interface
name|ExtensionFunction
parameter_list|<
name|T
extends|extends
name|Extension
parameter_list|<
name|?
parameter_list|>
parameter_list|,
name|R
parameter_list|>
block|{
DECL|method|call (T extension)
name|R
name|call
parameter_list|(
name|T
name|extension
parameter_list|)
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|CheckedExtensionFunction
specifier|public
interface|interface
name|CheckedExtensionFunction
parameter_list|<
name|T
extends|extends
name|Extension
parameter_list|<
name|?
parameter_list|>
parameter_list|,
name|R
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
block|{
DECL|method|call (T extension)
name|R
name|call
parameter_list|(
name|T
name|extension
parameter_list|)
throws|throws
name|X
function_decl|;
block|}
comment|/**    * Opens a new trace context for invoking a plugin extension.    *    * @param dynamicItem dynamic item that holds the extension implementation that is being invoked    *     from within the trace context    * @return the created trace context    */
DECL|method|newTrace (DynamicItem<T> dynamicItem)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|TraceContext
name|newTrace
parameter_list|(
name|DynamicItem
argument_list|<
name|T
argument_list|>
name|dynamicItem
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
return|return
name|TraceContext
operator|.
name|open
argument_list|()
return|;
block|}
return|return
name|newTrace
argument_list|(
name|extension
argument_list|)
return|;
block|}
comment|/**    * Opens a new trace context for invoking a plugin extension.    *    * @param extension extension that is being invoked from within the trace context    * @return the created trace context    */
DECL|method|newTrace (Extension<T> extension)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|TraceContext
name|newTrace
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|)
block|{
return|return
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addPluginTag
argument_list|(
name|checkNotNull
argument_list|(
name|extension
argument_list|)
operator|.
name|getPluginName
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Runs a plugin extension. All exceptions from the plugin extension are caught and logged.    *    *<p>The consumer gets the extension implementation provided that should be invoked.    *    * @param extension extension that is being invoked    * @param extensionImplConsumer the consumer that invokes the extension    */
DECL|method|runLogExceptions ( Extension<T> extension, ExtensionImplConsumer<T> extensionImplConsumer)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|runLogExceptions
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
name|ExtensionImplConsumer
argument_list|<
name|T
argument_list|>
name|extensionImplConsumer
parameter_list|)
block|{
name|T
name|extensionImpl
init|=
name|extension
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|extensionImpl
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
name|extensionImplConsumer
operator|.
name|run
argument_list|(
name|extensionImpl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failure in %s of plugin %s"
argument_list|,
name|extensionImpl
operator|.
name|getClass
argument_list|()
argument_list|,
name|extension
operator|.
name|getPluginName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Runs a plugin extension. All exceptions from the plugin extension are caught and logged.    *    *<p>The consumer get the {@link Extension} provided that should be invoked. The extension    * provides access to the plugin name and the export name.    *    * @param extension extension that is being invoked    * @param extensionConsumer the consumer that invokes the extension    */
DECL|method|runLogExceptions ( Extension<T> extension, ExtensionConsumer<Extension<T>> extensionConsumer)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|runLogExceptions
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
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
name|T
name|extensionImpl
init|=
name|extension
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|extensionImpl
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
name|extensionConsumer
operator|.
name|run
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failure in %s of plugin %s"
argument_list|,
name|extensionImpl
operator|.
name|getClass
argument_list|()
argument_list|,
name|extension
operator|.
name|getPluginName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Runs a plugin extension. All exceptions from the plugin extension except exceptions of the    * specified type are caught and logged. Exceptions of the specified type are thrown and must be    * handled by the caller.    *    *<p>The consumer gets the extension implementation provided that should be invoked.    *    * @param extension extension that is being invoked    * @param extensionImplConsumer the consumer that invokes the extension    * @param exceptionClass type of the exceptions that should be thrown    * @throws X expected exception from the plugin extension    */
DECL|method|runLogExceptions ( Extension<T> extension, ExtensionImplConsumer<T> extensionImplConsumer, Class<X> exceptionClass)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|void
name|runLogExceptions
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
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
name|T
name|extensionImpl
init|=
name|extension
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|extensionImpl
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
name|extensionImplConsumer
operator|.
name|run
argument_list|(
name|extensionImpl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failure in %s of plugin invoke%s"
argument_list|,
name|extensionImpl
operator|.
name|getClass
argument_list|()
argument_list|,
name|extension
operator|.
name|getPluginName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Runs a plugin extension. All exceptions from the plugin extension except exceptions of the    * specified type are caught and logged. Exceptions of the specified type are thrown and must be    * handled by the caller.    *    *<p>The consumer get the {@link Extension} provided that should be invoked. The extension    * provides access to the plugin name and the export name.    *    * @param extension extension that is being invoked    * @param extensionConsumer the consumer that invokes the extension    * @param exceptionClass type of the exceptions that should be thrown    * @throws X expected exception from the plugin extension    */
DECL|method|runLogExceptions ( Extension<T> extension, ExtensionConsumer<Extension<T>> extensionConsumer, Class<X> exceptionClass)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|void
name|runLogExceptions
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
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
name|T
name|extensionImpl
init|=
name|extension
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|extensionImpl
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
name|extensionConsumer
operator|.
name|run
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failure in %s of plugin %s"
argument_list|,
name|extensionImpl
operator|.
name|getClass
argument_list|()
argument_list|,
name|extension
operator|.
name|getPluginName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Calls a plugin extension and returns the result from the plugin extension call.    *    *<p>The function gets the extension implementation provided that should be invoked.    *    * @param extension extension that is being invoked    * @param extensionImplFunction function that invokes the extension    * @return the result from the plugin extension    */
DECL|method|call (Extension<T> extension, ExtensionImplFunction<T, R> extensionImplFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|>
name|R
name|call
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
name|ExtensionImplFunction
argument_list|<
name|T
argument_list|,
name|R
argument_list|>
name|extensionImplFunction
parameter_list|)
block|{
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
return|return
name|extensionImplFunction
operator|.
name|call
argument_list|(
name|extension
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Calls a plugin extension and returns the result from the plugin extension call. Exceptions of    * the specified type are thrown and must be handled by the caller.    *    *<p>The function gets the extension implementation provided that should be invoked.    *    * @param extension extension that is being invoked    * @param checkedExtensionImplFunction function that invokes the extension    * @param exceptionClass type of the exceptions that should be thrown    * @return the result from the plugin extension    * @throws X expected exception from the plugin extension    */
DECL|method|call ( Extension<T> extension, CheckedExtensionImplFunction<T, R, X> checkedExtensionImplFunction, Class<X> exceptionClass)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|R
name|call
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
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
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
try|try
block|{
return|return
name|checkedExtensionImplFunction
operator|.
name|call
argument_list|(
name|extension
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// The only exception that can be thrown is X, but we cannot catch X since it is a generic
comment|// type.
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unexpected exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * Calls a plugin extension and returns the result from the plugin extension call.    *    *<p>The function get the {@link Extension} provided that should be invoked. The extension    * provides access to the plugin name and the export name.    *    * @param extension extension that is being invoked    * @param extensionFunction function that invokes the extension    * @return the result from the plugin extension    */
DECL|method|call ( Extension<T> extension, ExtensionFunction<Extension<T>, R> extensionFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|>
name|R
name|call
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
name|ExtensionFunction
argument_list|<
name|Extension
argument_list|<
name|T
argument_list|>
argument_list|,
name|R
argument_list|>
name|extensionFunction
parameter_list|)
block|{
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
return|return
name|extensionFunction
operator|.
name|call
argument_list|(
name|extension
argument_list|)
return|;
block|}
block|}
comment|/**    * Calls a plugin extension and returns the result from the plugin extension call. Exceptions of    * the specified type are thrown and must be handled by the caller.    *    *<p>The function get the {@link Extension} provided that should be invoked. The extension    * provides access to the plugin name and the export name.    *    * @param extension extension that is being invoked    * @param checkedExtensionFunction function that invokes the extension    * @param exceptionClass type of the exceptions that should be thrown    * @return the result from the plugin extension    * @throws X expected exception from the plugin extension    */
DECL|method|call ( Extension<T> extension, CheckedExtensionFunction<Extension<T>, R, X> checkedExtensionFunction, Class<X> exceptionClass)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|R
name|call
parameter_list|(
name|Extension
argument_list|<
name|T
argument_list|>
name|extension
parameter_list|,
name|CheckedExtensionFunction
argument_list|<
name|Extension
argument_list|<
name|T
argument_list|>
argument_list|,
name|R
argument_list|,
name|X
argument_list|>
name|checkedExtensionFunction
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
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newTrace
argument_list|(
name|extension
argument_list|)
init|)
block|{
try|try
block|{
return|return
name|checkedExtensionFunction
operator|.
name|call
argument_list|(
name|extension
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// The only exception that can be thrown is X, but we cannot catch X since it is a generic
comment|// type.
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unexpected exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

