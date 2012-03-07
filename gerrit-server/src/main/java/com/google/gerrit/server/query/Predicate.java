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
DECL|package|com.google.gerrit.server.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|List
import|;
end_import

begin_comment
comment|/**  * An abstract predicate tree for any form of query.  *<p>  * Implementations should be immutable, such that the meaning of a predicate  * never changes once constructed. They should ensure their immutable promise by  * defensively copying any structures which might be modified externally, but  * was passed into the object's constructor.  *<p>  * However, implementations<i>may</i> retain non-thread-safe caches internally,  * to speed up evaluation operations within the context of one thread's  * evaluation of the predicate. As a result, callers should assume predicates  * are not thread-safe, but that two predicate graphs produce the same results  * given the same inputs if they are {@link #equals(Object)}.  *<p>  * Predicates should support deep inspection whenever possible, so that generic  * algorithms can be written to operate against them. Predicates which contain  * other predicates should override {@link #getChildren()} to return the list of  * children nested within the predicate.  *  * @type<T> type of object the predicate can evaluate in memory.  */
end_comment

begin_class
DECL|class|Predicate
specifier|public
specifier|abstract
class|class
name|Predicate
parameter_list|<
name|T
parameter_list|>
block|{
comment|/** Combine the passed predicates into a single AND node. */
DECL|method|and (final Predicate<T>... that)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
modifier|...
name|that
parameter_list|)
block|{
return|return
operator|new
name|AndPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Combine the passed predicates into a single AND node. */
DECL|method|and ( final Collection<? extends Predicate<T>> that)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|that
parameter_list|)
block|{
return|return
operator|new
name|AndPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Combine the passed predicates into a single OR node. */
DECL|method|or (final Predicate<T>... that)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
modifier|...
name|that
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Combine the passed predicates into a single OR node. */
DECL|method|or ( final Collection<? extends Predicate<T>> that)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|that
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Invert the passed node. */
DECL|method|not (final Predicate<T> that)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|not
parameter_list|(
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|that
parameter_list|)
block|{
if|if
condition|(
name|that
operator|instanceof
name|NotPredicate
condition|)
block|{
comment|// Negate of a negate is the original predicate.
comment|//
return|return
name|that
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
operator|new
name|NotPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Get the children of this predicate, if any. */
DECL|method|getChildren ()
specifier|public
name|List
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|/** Same as {@code getChildren().size()} */
DECL|method|getChildCount ()
specifier|public
name|int
name|getChildCount
parameter_list|()
block|{
return|return
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
comment|/** Same as {@code getChildren().get(i)} */
DECL|method|getChild (final int i)
specifier|public
name|Predicate
argument_list|<
name|T
argument_list|>
name|getChild
parameter_list|(
specifier|final
name|int
name|i
parameter_list|)
block|{
return|return
name|getChildren
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
block|}
comment|/** Create a copy of this predicate, with new children. */
DECL|method|copy (Collection<? extends Predicate<T>> children)
specifier|public
specifier|abstract
name|Predicate
argument_list|<
name|T
argument_list|>
name|copy
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|children
parameter_list|)
function_decl|;
comment|/**    * Does this predicate match this object?    *    * @throws OrmException    */
DECL|method|match (T object)
specifier|public
specifier|abstract
name|boolean
name|match
parameter_list|(
name|T
name|object
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/** @return a cost estimate to run this predicate, higher figures cost more. */
DECL|method|getCost ()
specifier|public
specifier|abstract
name|int
name|getCost
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|abstract
name|int
name|hashCode
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
specifier|abstract
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
function_decl|;
block|}
end_class

end_unit

