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
DECL|package|com.google.gerrit.lifecycle
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|lifecycle
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
name|collect
operator|.
name|Lists
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Tracks and executes registered {@link LifecycleListener}s. */
end_comment

begin_class
DECL|class|LifecycleManager
specifier|public
class|class
name|LifecycleManager
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
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|Provider
argument_list|<
name|LifecycleListener
argument_list|>
argument_list|>
name|listeners
init|=
name|newList
argument_list|()
decl_stmt|;
DECL|field|handles
specifier|private
specifier|final
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|handles
init|=
name|newList
argument_list|()
decl_stmt|;
comment|/** Index of the last listener to start successfully; -1 when not started. */
DECL|field|startedIndex
specifier|private
name|int
name|startedIndex
init|=
operator|-
literal|1
decl_stmt|;
comment|/**    * Add a handle that must be cleared during stop.    *    * @param handle the handle to add.    */
DECL|method|add (RegistrationHandle handle)
specifier|public
name|void
name|add
parameter_list|(
name|RegistrationHandle
name|handle
parameter_list|)
block|{
name|handles
operator|.
name|add
argument_list|(
name|handle
argument_list|)
expr_stmt|;
block|}
comment|/**    * Add a single listener.    *    * @param listener the listener to add.    */
DECL|method|add (LifecycleListener listener)
specifier|public
name|void
name|add
parameter_list|(
name|LifecycleListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Add a single listener.    *    * @param listener the listener to add.    */
DECL|method|add (Provider<LifecycleListener> listener)
specifier|public
name|void
name|add
parameter_list|(
name|Provider
argument_list|<
name|LifecycleListener
argument_list|>
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**    * Add all {@link LifecycleListener}s registered in the Injector.    *    * @param injector the injector to add.    */
DECL|method|add (Injector injector)
specifier|public
name|void
name|add
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|checkState
argument_list|(
name|startedIndex
operator|<
literal|0
argument_list|,
literal|"Already started"
argument_list|)
expr_stmt|;
for|for
control|(
name|Binding
argument_list|<
name|LifecycleListener
argument_list|>
name|binding
range|:
name|get
argument_list|(
name|injector
argument_list|)
control|)
block|{
name|add
argument_list|(
name|binding
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Add all {@link LifecycleListener}s registered in the Injectors.    *    * @param injectors the injectors to add.    */
DECL|method|add (Injector... injectors)
specifier|public
name|void
name|add
parameter_list|(
name|Injector
modifier|...
name|injectors
parameter_list|)
block|{
for|for
control|(
name|Injector
name|i
range|:
name|injectors
control|)
block|{
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Start all listeners, in the order they were registered. */
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
name|startedIndex
operator|+
literal|1
init|;
name|i
operator|<
name|listeners
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|LifecycleListener
name|listener
init|=
name|listeners
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|startedIndex
operator|=
name|i
expr_stmt|;
name|listener
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** Stop all listeners, in the reverse order they were registered. */
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
name|handles
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
literal|0
operator|<=
name|i
condition|;
name|i
operator|--
control|)
block|{
name|handles
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
name|handles
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|startedIndex
init|;
literal|0
operator|<=
name|i
condition|;
name|i
operator|--
control|)
block|{
name|LifecycleListener
name|obj
init|=
name|listeners
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
name|obj
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to stop %s"
argument_list|,
name|obj
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|startedIndex
operator|=
name|i
operator|-
literal|1
expr_stmt|;
block|}
block|}
DECL|method|get (Injector i)
specifier|private
specifier|static
name|List
argument_list|<
name|Binding
argument_list|<
name|LifecycleListener
argument_list|>
argument_list|>
name|get
parameter_list|(
name|Injector
name|i
parameter_list|)
block|{
return|return
name|i
operator|.
name|findBindingsByType
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|LifecycleListener
argument_list|>
argument_list|()
block|{}
argument_list|)
return|;
block|}
DECL|method|newList ()
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|newList
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|4
argument_list|)
return|;
block|}
block|}
end_class

end_unit

