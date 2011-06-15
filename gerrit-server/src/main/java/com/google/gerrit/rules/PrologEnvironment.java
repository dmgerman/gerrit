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
name|PrologClassLoader
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
name|SystemException
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
name|Term
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
DECL|field|PACKAGE_LIST
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|PACKAGE_LIST
init|=
block|{
name|Prolog
operator|.
name|BUILTIN
block|,
literal|"gerrit"
block|,     }
decl_stmt|;
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
comment|/**      * Construct a new Prolog interpreter.      *      * @param cl ClassLoader to dynamically load predicates from.      * @return the new interpreter.      */
DECL|method|create (ClassLoader cl)
name|PrologEnvironment
name|create
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
function_decl|;
block|}
DECL|field|injector
specifier|private
specifier|final
name|Injector
name|injector
decl_stmt|;
DECL|field|intialized
specifier|private
name|boolean
name|intialized
decl_stmt|;
annotation|@
name|Inject
DECL|method|PrologEnvironment (Injector i, @Assisted ClassLoader newCL)
name|PrologEnvironment
parameter_list|(
name|Injector
name|i
parameter_list|,
annotation|@
name|Assisted
name|ClassLoader
name|newCL
parameter_list|)
block|{
name|injector
operator|=
name|i
expr_stmt|;
name|setPrologClassLoader
argument_list|(
operator|new
name|PrologClassLoader
argument_list|(
name|newCL
argument_list|)
argument_list|)
expr_stmt|;
name|setMaxArity
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|setMaxDatabaseSize
argument_list|(
literal|64
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
name|sv
operator|.
name|getOrNull
argument_list|(
name|engine
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
name|sv
operator|.
name|set
argument_list|(
name|engine
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setPredicate (String pkg, String functor, Term... args)
specifier|public
name|void
name|setPredicate
parameter_list|(
name|String
name|pkg
parameter_list|,
name|String
name|functor
parameter_list|,
name|Term
modifier|...
name|args
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
name|super
operator|.
name|setPredicate
argument_list|(
name|pkg
argument_list|,
name|functor
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
DECL|method|init ()
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
operator|!
name|intialized
condition|)
block|{
name|intialized
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|initialize
argument_list|(
name|PACKAGE_LIST
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SystemException
argument_list|(
literal|"Prolog initialization failed"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

