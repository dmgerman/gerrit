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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
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
name|flogger
operator|.
name|LazyArgs
operator|.
name|lazy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|ImmutableList
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
name|LazyArg
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * Utility to compute the caller of a method.  *  *<p>In the logs we see for each entry from where it was triggered (class/method/line) but in case  * the logging is done in a utility method or inside of a module this doesn't tell us from where the  * action was actually triggered. To get this information we could included the stacktrace into the  * logs (by calling {@link  * com.google.common.flogger.LoggingApi#withStackTrace(com.google.common.flogger.StackSize)} but  * sometimes there are too many uninteresting stacks so that this would blow up the logs too much.  * In this case CallerFinder can be used to find the first interesting caller from the current  * stacktrace by specifying the class that interesting callers invoke as target.  *  *<p>Example:  *  *<p>Index queries are executed by the {@code query(List<String>, List<Predicate<T>>)} method in  * {@link com.google.gerrit.index.query.QueryProcessor}. At this place the index query is logged but  * from the log we want to see which code triggered this index query.  *  *<p>E.g. the stacktrace could look like this:  *  *<pre>  * GroupQueryProcessor(QueryProcessor<T>).query(List<String>, List<Predicate<T>>) line: 216  * GroupQueryProcessor(QueryProcessor<T>).query(List<Predicate<T>>) line: 188  * GroupQueryProcessor(QueryProcessor<T>).query(Predicate<T>) line: 171  * InternalGroupQuery(InternalQuery<T>).query(Predicate<T>) line: 81  * InternalGroupQuery.getOnlyGroup(Predicate<InternalGroup>, String) line: 67  * InternalGroupQuery.byName(NameKey) line: 50  * GroupCacheImpl$ByNameLoader.load(String) line: 166  * GroupCacheImpl$ByNameLoader.load(Object) line: 1  * LocalCache$LoadingValueReference<K,V>.loadFuture(K, CacheLoader<? super K,V>) line: 3527  * ...  *</pre>  *  *<p>The first interesting caller is {@code GroupCacheImpl$ByNameLoader.load(String) line: 166}. To  * find this caller from the stacktrace we could specify {@link  * com.google.gerrit.server.query.group.InternalGroupQuery} as a target since we know that all  * internal group queries go through this class:  *  *<pre>  * CallerFinder.builder()  *   .addTarget(InternalGroupQuery.class)  *   .build();  *</pre>  *  *<p>Since in some places {@link com.google.gerrit.server.query.group.GroupQueryProcessor} may also  * be used directly we can add it as a secondary target to catch these callers as well:  *  *<pre>  * CallerFinder.builder()  *   .addTarget(InternalGroupQuery.class)  *   .addTarget(GroupQueryProcessor.class)  *   .build();  *</pre>  *  *<p>However since {@link com.google.gerrit.index.query.QueryProcessor} is also responsible to  * execute other index queries (for changes, accounts, projects) we would need to add the classes  * for them as targets too. Since there are common base classes we can simply specify the base  * classes and request matching of subclasses:  *  *<pre>  * CallerFinder.builder()  *   .addTarget(InternalQuery.class)  *   .addTarget(QueryProcessor.class)  *   .matchSubClasses(true)  *   .build();  *</pre>  *  *<p>Another special case is if the entry point is always an inner class of a known interface. E.g.  * {@link com.google.gerrit.server.permissions.PermissionBackend} is the entry point for all  * permission checks but they are done through inner classes, e.g. {@link  * com.google.gerrit.server.permissions.PermissionBackend.ForProject}. In this case matching of  * inner classes must be enabled as well:  *  *<pre>  * CallerFinder.builder()  *   .addTarget(PermissionBackend.class)  *   .matchSubClasses(true)  *   .matchInnerClasses(true)  *   .build();  *</pre>  *  *<p>Finding the interesting caller requires specifying the entry point class as target. This may  * easily break when code is refactored and hence should be used only with care. It's recommended to  * use this only when the corresponding code is relatively stable and logging the caller information  * brings some significant benefit.  *  *<p>Based on {@link com.google.common.flogger.util.CallerFinder}.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|CallerFinder
specifier|public
specifier|abstract
class|class
name|CallerFinder
block|{
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_CallerFinder
operator|.
name|Builder
argument_list|()
operator|.
name|matchSubClasses
argument_list|(
literal|false
argument_list|)
operator|.
name|matchInnerClasses
argument_list|(
literal|false
argument_list|)
operator|.
name|skip
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**    * The target classes for which the caller should be found, in the order in which they should be    * checked.    *    * @return the target classes for which the caller should be found    */
DECL|method|targets ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|targets
parameter_list|()
function_decl|;
comment|/**    * Whether inner classes should be matched.    *    * @return whether inner classes should be matched    */
DECL|method|matchSubClasses ()
specifier|public
specifier|abstract
name|boolean
name|matchSubClasses
parameter_list|()
function_decl|;
comment|/**    * Whether sub classes of the target classes should be matched.    *    * @return whether sub classes of the target classes should be matched    */
DECL|method|matchInnerClasses ()
specifier|public
specifier|abstract
name|boolean
name|matchInnerClasses
parameter_list|()
function_decl|;
comment|/**    * The minimum number of calls known to have occurred between the first call to the target class    * and the call of {@link #findCaller()}. If in doubt, specify zero here to avoid accidentally    * skipping past the caller.    *    * @return the number of stack elements to skip when computing the caller    */
DECL|method|skip ()
specifier|public
specifier|abstract
name|int
name|skip
parameter_list|()
function_decl|;
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|targetsBuilder ()
specifier|abstract
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|targetsBuilder
parameter_list|()
function_decl|;
DECL|method|addTarget (Class<?> target)
specifier|public
name|Builder
name|addTarget
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|target
parameter_list|)
block|{
name|targetsBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|matchSubClasses (boolean matchSubClasses)
specifier|public
specifier|abstract
name|Builder
name|matchSubClasses
parameter_list|(
name|boolean
name|matchSubClasses
parameter_list|)
function_decl|;
DECL|method|matchInnerClasses (boolean matchInnerClasses)
specifier|public
specifier|abstract
name|Builder
name|matchInnerClasses
parameter_list|(
name|boolean
name|matchInnerClasses
parameter_list|)
function_decl|;
DECL|method|skip (int skip)
specifier|public
specifier|abstract
name|Builder
name|skip
parameter_list|(
name|int
name|skip
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|CallerFinder
name|build
parameter_list|()
function_decl|;
block|}
DECL|method|findCaller ()
specifier|public
name|LazyArg
argument_list|<
name|String
argument_list|>
name|findCaller
parameter_list|()
block|{
return|return
name|lazy
argument_list|(
parameter_list|()
lambda|->
name|targets
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|t
lambda|->
name|findCallerOf
argument_list|(
name|t
argument_list|,
name|skip
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|Optional
operator|::
name|isPresent
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|map
argument_list|(
name|Optional
operator|::
name|get
argument_list|)
operator|.
name|orElse
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|findCallerOf (Class<?> target, int skip)
specifier|private
name|Optional
argument_list|<
name|String
argument_list|>
name|findCallerOf
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|target
parameter_list|,
name|int
name|skip
parameter_list|)
block|{
comment|// Skip one additional stack frame because we create the Throwable inside this method, not at
comment|// the point that this method was invoked.
name|skip
operator|++
expr_stmt|;
name|StackTraceElement
index|[]
name|stack
init|=
operator|new
name|Throwable
argument_list|()
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
comment|// Note: To avoid having to reflect the getStackTraceDepth() method as well, we assume that we
comment|// will find the caller on the stack and simply catch an exception if we fail (which should
comment|// hardly ever happen).
name|boolean
name|foundCaller
init|=
literal|false
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|index
init|=
name|skip
init|;
condition|;
name|index
operator|++
control|)
block|{
name|StackTraceElement
name|element
init|=
name|stack
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|isCaller
argument_list|(
name|target
argument_list|,
name|element
operator|.
name|getClassName
argument_list|()
argument_list|,
name|matchSubClasses
argument_list|()
argument_list|)
condition|)
block|{
name|foundCaller
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|foundCaller
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|element
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// This should only happen if a) the caller was not found on the stack
comment|// (IndexOutOfBoundsException) b) a class that is mentioned in the stack was not found
comment|// (ClassNotFoundException), however we don't want anything to be thrown from here.
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
DECL|method|isCaller (Class<?> target, String className, boolean matchSubClasses)
specifier|private
name|boolean
name|isCaller
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|target
parameter_list|,
name|String
name|className
parameter_list|,
name|boolean
name|matchSubClasses
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|matchSubClasses
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
decl_stmt|;
while|while
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|isCaller
argument_list|(
name|target
argument_list|,
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|matchInnerClasses
argument_list|()
condition|)
block|{
name|int
name|i
init|=
name|className
operator|.
name|indexOf
argument_list|(
literal|'$'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|className
operator|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|target
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

