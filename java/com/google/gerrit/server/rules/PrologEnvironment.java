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
DECL|package|com.google.gerrit.server.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|server
operator|.
name|AnonymousUser
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
name|IdentifiedUser
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
name|PatchSetUtil
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
name|account
operator|.
name|Emails
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
name|config
operator|.
name|GerritServerConfig
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
name|config
operator|.
name|PluginConfigFactory
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
name|git
operator|.
name|GitRepositoryManager
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
name|patch
operator|.
name|PatchListCache
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
name|patch
operator|.
name|PatchSetInfoFactory
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
name|permissions
operator|.
name|PermissionBackend
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
name|project
operator|.
name|ProjectCache
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
name|Singleton
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
name|Predicate
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
name|PredicateEncoder
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/**  * Per-thread Prolog interpreter.  *  *<p>This class is not thread safe.  *  *<p>A single copy of the Prolog interpreter, for the current thread.  */
end_comment

begin_class
DECL|class|PrologEnvironment
specifier|public
class|class
name|PrologEnvironment
extends|extends
name|BufferingPrologControl
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
DECL|interface|Factory
specifier|public
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
DECL|field|args
specifier|private
specifier|final
name|Args
name|args
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
DECL|method|PrologEnvironment (Args a, @Assisted PrologMachineCopy src)
name|PrologEnvironment
parameter_list|(
name|Args
name|a
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
name|args
operator|=
name|a
expr_stmt|;
name|storedValues
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|cleanup
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|getArgs ()
specifier|public
name|Args
name|getArgs
parameter_list|()
block|{
return|return
name|args
return|;
block|}
annotation|@
name|Override
DECL|method|setPredicate (Predicate goal)
specifier|public
name|void
name|setPredicate
parameter_list|(
name|Predicate
name|goal
parameter_list|)
block|{
name|super
operator|.
name|setPredicate
argument_list|(
name|goal
argument_list|)
expr_stmt|;
name|int
name|reductionLimit
init|=
name|args
operator|.
name|reductionLimit
argument_list|(
name|goal
argument_list|)
decl_stmt|;
name|setReductionLimit
argument_list|(
name|reductionLimit
argument_list|)
expr_stmt|;
block|}
comment|/**    * Lookup a stored value in the interpreter's hash manager.    *    * @param<T> type of stored Java object.    * @param sv unique key.    * @return the value; null if not stored.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
comment|/**    * Copy the stored values from another interpreter to this one. Also gets the cleanup from the    * child interpreter    */
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
comment|/**    * Assign the environment a cleanup list (in order to use a centralized list) If this    * enivronment's list is non-empty, append its cleanup tasks to the assigning list.    */
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
comment|/**    * Adds cleanup task to run when close() is called    *    * @param task is run when close() is called    */
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
comment|/** Release resources stored in interpreter's hash manager. */
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
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
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to execute cleanup for PrologEnvironment"
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
annotation|@
name|Singleton
DECL|class|Args
specifier|public
specifier|static
class|class
name|Args
block|{
DECL|field|CONSULT_STREAM_2
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|Predicate
argument_list|>
name|CONSULT_STREAM_2
decl_stmt|;
static|static
block|{
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|Predicate
argument_list|>
name|c
init|=
operator|(
name|Class
argument_list|<
name|Predicate
argument_list|>
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|PredicateEncoder
operator|.
name|encode
argument_list|(
name|Prolog
operator|.
name|BUILTIN
argument_list|,
literal|"consult_stream"
argument_list|,
literal|2
argument_list|)
argument_list|,
literal|false
argument_list|,
name|RulesCache
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|CONSULT_STREAM_2
operator|=
name|c
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|LinkageError
argument_list|(
literal|"cannot find predicate consult_stream"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|repositoryManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repositoryManager
decl_stmt|;
DECL|field|pluginConfigFactory
specifier|private
specifier|final
name|PluginConfigFactory
name|pluginConfigFactory
decl_stmt|;
DECL|field|patchListCache
specifier|private
specifier|final
name|PatchListCache
name|patchListCache
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|anonymousUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|AnonymousUser
argument_list|>
name|anonymousUser
decl_stmt|;
DECL|field|reductionLimit
specifier|private
specifier|final
name|int
name|reductionLimit
decl_stmt|;
DECL|field|compileLimit
specifier|private
specifier|final
name|int
name|compileLimit
decl_stmt|;
DECL|field|patchsetUtil
specifier|private
specifier|final
name|PatchSetUtil
name|patchsetUtil
decl_stmt|;
DECL|field|emails
specifier|private
name|Emails
name|emails
decl_stmt|;
annotation|@
name|Inject
DECL|method|Args ( ProjectCache projectCache, PermissionBackend permissionBackend, GitRepositoryManager repositoryManager, PluginConfigFactory pluginConfigFactory, PatchListCache patchListCache, PatchSetInfoFactory patchSetInfoFactory, IdentifiedUser.GenericFactory userFactory, Provider<AnonymousUser> anonymousUser, @GerritServerConfig Config config, PatchSetUtil patchsetUtil, Emails emails)
name|Args
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GitRepositoryManager
name|repositoryManager
parameter_list|,
name|PluginConfigFactory
name|pluginConfigFactory
parameter_list|,
name|PatchListCache
name|patchListCache
parameter_list|,
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|Provider
argument_list|<
name|AnonymousUser
argument_list|>
name|anonymousUser
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|PatchSetUtil
name|patchsetUtil
parameter_list|,
name|Emails
name|emails
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|repositoryManager
operator|=
name|repositoryManager
expr_stmt|;
name|this
operator|.
name|pluginConfigFactory
operator|=
name|pluginConfigFactory
expr_stmt|;
name|this
operator|.
name|patchListCache
operator|=
name|patchListCache
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|anonymousUser
operator|=
name|anonymousUser
expr_stmt|;
name|this
operator|.
name|patchsetUtil
operator|=
name|patchsetUtil
expr_stmt|;
name|this
operator|.
name|emails
operator|=
name|emails
expr_stmt|;
name|this
operator|.
name|reductionLimit
operator|=
name|RuleUtil
operator|.
name|reductionLimit
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|compileLimit
operator|=
name|RuleUtil
operator|.
name|compileReductionLimit
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"reductionLimit: %d, compileLimit: %d"
argument_list|,
name|reductionLimit
argument_list|,
name|compileLimit
argument_list|)
expr_stmt|;
block|}
DECL|method|reductionLimit (Predicate goal)
specifier|private
name|int
name|reductionLimit
parameter_list|(
name|Predicate
name|goal
parameter_list|)
block|{
if|if
condition|(
name|goal
operator|.
name|getClass
argument_list|()
operator|==
name|CONSULT_STREAM_2
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"predicate class is CONSULT_STREAM_2: override reductionLimit with compileLimit (%d)"
argument_list|,
name|compileLimit
argument_list|)
expr_stmt|;
return|return
name|compileLimit
return|;
block|}
return|return
name|reductionLimit
return|;
block|}
DECL|method|getProjectCache ()
specifier|public
name|ProjectCache
name|getProjectCache
parameter_list|()
block|{
return|return
name|projectCache
return|;
block|}
DECL|method|getPermissionBackend ()
specifier|public
name|PermissionBackend
name|getPermissionBackend
parameter_list|()
block|{
return|return
name|permissionBackend
return|;
block|}
DECL|method|getGitRepositoryManager ()
specifier|public
name|GitRepositoryManager
name|getGitRepositoryManager
parameter_list|()
block|{
return|return
name|repositoryManager
return|;
block|}
DECL|method|getPluginConfigFactory ()
specifier|public
name|PluginConfigFactory
name|getPluginConfigFactory
parameter_list|()
block|{
return|return
name|pluginConfigFactory
return|;
block|}
DECL|method|getPatchListCache ()
specifier|public
name|PatchListCache
name|getPatchListCache
parameter_list|()
block|{
return|return
name|patchListCache
return|;
block|}
DECL|method|getPatchSetInfoFactory ()
specifier|public
name|PatchSetInfoFactory
name|getPatchSetInfoFactory
parameter_list|()
block|{
return|return
name|patchSetInfoFactory
return|;
block|}
DECL|method|getUserFactory ()
specifier|public
name|IdentifiedUser
operator|.
name|GenericFactory
name|getUserFactory
parameter_list|()
block|{
return|return
name|userFactory
return|;
block|}
DECL|method|getAnonymousUser ()
specifier|public
name|AnonymousUser
name|getAnonymousUser
parameter_list|()
block|{
return|return
name|anonymousUser
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getPatchsetUtil ()
specifier|public
name|PatchSetUtil
name|getPatchsetUtil
parameter_list|()
block|{
return|return
name|patchsetUtil
return|;
block|}
DECL|method|getEmails ()
specifier|public
name|Emails
name|getEmails
parameter_list|()
block|{
return|return
name|emails
return|;
block|}
block|}
block|}
end_class

end_unit

