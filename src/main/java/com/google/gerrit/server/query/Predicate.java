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
comment|/**  * An abstract predicate tree for any form of query.  *<p>  * Implementations should be immutable, and therefore also be thread-safe. They  * also should ensure their immutable promise by defensively copying any  * structures which might be modified externally, but were passed into the  * object's constructor.  *<p>  * Predicates should support deep inspection whenever possible, so that generic  * algorithms can be written to operate against them. Predicates which contain  * other predicates should override {@link #getChildren()} to return the list of  * children nested within the predicate.  */
end_comment

begin_class
DECL|class|Predicate
specifier|public
specifier|abstract
class|class
name|Predicate
block|{
comment|/** Combine the passed predicates into a single AND node. */
DECL|method|and (final Predicate... that)
specifier|public
specifier|static
name|Predicate
name|and
parameter_list|(
specifier|final
name|Predicate
modifier|...
name|that
parameter_list|)
block|{
return|return
operator|new
name|AndPredicate
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Combine the passed predicates into a single AND node. */
DECL|method|and (final Collection<Predicate> that)
specifier|public
specifier|static
name|Predicate
name|and
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Predicate
argument_list|>
name|that
parameter_list|)
block|{
return|return
operator|new
name|AndPredicate
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Combine the passed predicates into a single OR node. */
DECL|method|or (final Predicate... that)
specifier|public
specifier|static
name|Predicate
name|or
parameter_list|(
specifier|final
name|Predicate
modifier|...
name|that
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Combine the passed predicates into a single OR node. */
DECL|method|or (final Collection<Predicate> that)
specifier|public
specifier|static
name|Predicate
name|or
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Predicate
argument_list|>
name|that
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|(
name|that
argument_list|)
return|;
block|}
comment|/** Invert the passed node; same as {@code that.not()}. */
DECL|method|not (final Predicate that)
specifier|public
specifier|static
name|Predicate
name|not
parameter_list|(
specifier|final
name|Predicate
name|that
parameter_list|)
block|{
return|return
name|that
operator|.
name|not
argument_list|()
return|;
block|}
comment|/** Get the children of this predicate, if any. */
DECL|method|getChildren ()
specifier|public
name|List
argument_list|<
name|Predicate
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
comment|/** Obtain the inverse of this predicate. */
DECL|method|not ()
specifier|public
name|Predicate
name|not
parameter_list|()
block|{
return|return
operator|new
name|NotPredicate
argument_list|(
name|this
argument_list|)
return|;
block|}
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

