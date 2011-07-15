begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|BufferingPrologControl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|PrologMachineCopy
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
name|util
operator|.
name|EnumSet
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
comment|/**  * Per-thread Prolog interpreter.  *<p>  * This class is not thread safe.  *<p>  * A single copy of the Prolog interpreter, for the current thread.  */
end_comment

begin_class
DECL|class|PrologEnvironment
specifier|public
class|class
name|PrologEnvironment
extends|extends
name|BufferingPrologControl
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
name|PrologEnvironment
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|MAX_ARITY
specifier|static
specifier|final
name|int
name|MAX_ARITY
init|=
literal|8
decl_stmt|;
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
comment|/**      * Construct a new Prolog interpreter.      *      * @param src the machine to template the new environment from.      * @return the new interpreter.      */
DECL|method|create (PrologMachineCopy src)
name|PrologEnvironment
name|create
parameter_list|(
name|PrologMachineCopy
name|src
parameter_list|)
function_decl|;
block|}
DECL|field|injector
specifier|private
specifier|final
name|Injector
name|injector
decl_stmt|;
DECL|field|storedValues
specifier|private
specifier|final
name|Map
argument_list|<
name|StoredValue
argument_list|<
name|Object
argument_list|>
argument_list|,
name|Object
argument_list|>
name|storedValues
decl_stmt|;
DECL|field|cleanup
specifier|private
name|List
argument_list|<
name|Runnable
argument_list|>
name|cleanup
decl_stmt|;
annotation|@
name|Inject
DECL|method|PrologEnvironment (Injector i, @Assisted PrologMachineCopy src)
name|PrologEnvironment
parameter_list|(
name|Injector
name|i
parameter_list|,
annotation|@
name|Assisted
name|PrologMachineCopy
name|src
parameter_list|)
block|{
name|super
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|injector
operator|=
name|i
expr_stmt|;
name|setMaxArity
argument_list|(
name|MAX_ARITY
argument_list|)
expr_stmt|;
name|setEnabled
argument_list|(
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Prolog
operator|.
name|Feature
operator|.
name|class
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|storedValues
operator|=
operator|new
name|HashMap
argument_list|<
name|StoredValue
argument_list|<
name|Object
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|cleanup
operator|=
operator|new
name|LinkedList
argument_list|<
name|Runnable
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/** Get the global Guice Injector that configured the environment. */
DECL|method|getInjector ()
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|injector
return|;
block|}
comment|/**    * Lookup a stored value in the interpreter's hash manager.    *    * @param<T> type of stored Java object.    * @param sv unique key.    * @return the value; null if not stored.    */
DECL|method|get (StoredValue<T> sv)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|StoredValue
argument_list|<
name|T
argument_list|>
name|sv
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|storedValues
operator|.
name|get
argument_list|(
name|sv
argument_list|)
return|;
block|}
comment|/**    * Set a stored value on the interpreter's hash manager.    *    * @param<T> type of stored Java object.    * @param sv unique key.    * @param obj the value to store under {@code sv}.    */
DECL|method|set (StoredValue<T> sv, T obj)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|set
parameter_list|(
name|StoredValue
argument_list|<
name|T
argument_list|>
name|sv
parameter_list|,
name|T
name|obj
parameter_list|)
block|{
name|storedValues
operator|.
name|put
argument_list|(
operator|(
name|StoredValue
argument_list|<
name|Object
argument_list|>
operator|)
name|sv
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
comment|/**    * Copy the stored values from another interpreter to this one.    * Also gets the cleanup from the child interpreter    */
DECL|method|copyStoredValues (PrologEnvironment child)
specifier|public
name|void
name|copyStoredValues
parameter_list|(
name|PrologEnvironment
name|child
parameter_list|)
block|{
name|storedValues
operator|.
name|putAll
argument_list|(
name|child
operator|.
name|storedValues
argument_list|)
expr_stmt|;
name|setCleanup
argument_list|(
name|child
operator|.
name|cleanup
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assign the environment a cleanup list (in order to use a centralized list)    * If this enivronment's list is non-empty, append its cleanup tasks to the    * assigning list.    */
DECL|method|setCleanup (List<Runnable> newCleanupList)
specifier|public
name|void
name|setCleanup
parameter_list|(
name|List
argument_list|<
name|Runnable
argument_list|>
name|newCleanupList
parameter_list|)
block|{
name|newCleanupList
operator|.
name|addAll
argument_list|(
name|cleanup
argument_list|)
expr_stmt|;
name|cleanup
operator|=
name|newCleanupList
expr_stmt|;
block|}
comment|/**    * Adds cleanup task to run when close() is called    * @param task is run when close() is called    */
DECL|method|addToCleanup (Runnable task)
specifier|public
name|void
name|addToCleanup
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
name|cleanup
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
comment|/**    * Release resources stored in interpreter's hash manager.    */
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
specifier|final
name|Iterator
argument_list|<
name|Runnable
argument_list|>
name|i
init|=
name|cleanup
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
try|try
block|{
name|i
operator|.
name|next
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to execute cleanup for PrologEnvironment"
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

