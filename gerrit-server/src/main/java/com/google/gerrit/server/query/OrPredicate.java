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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/** Requires one predicate to be true. */
end_comment

begin_class
DECL|class|OrPredicate
specifier|public
class|class
name|OrPredicate
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Predicate
argument_list|<
name|T
argument_list|>
block|{
DECL|field|children
specifier|private
specifier|final
name|List
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|children
decl_stmt|;
DECL|field|cost
specifier|private
specifier|final
name|int
name|cost
decl_stmt|;
annotation|@
name|SafeVarargs
DECL|method|OrPredicate (final Predicate<T>... that)
specifier|protected
name|OrPredicate
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
name|this
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|that
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|OrPredicate (final Collection<? extends Predicate<T>> that)
specifier|protected
name|OrPredicate
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
name|List
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|t
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|that
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|c
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|p
range|:
name|that
control|)
block|{
if|if
condition|(
name|getClass
argument_list|()
operator|==
name|p
operator|.
name|getClass
argument_list|()
condition|)
block|{
for|for
control|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|gp
range|:
name|p
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|t
operator|.
name|add
argument_list|(
name|gp
argument_list|)
expr_stmt|;
name|c
operator|+=
name|gp
operator|.
name|getCost
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|t
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|c
operator|+=
name|p
operator|.
name|getCost
argument_list|()
expr_stmt|;
block|}
block|}
name|children
operator|=
name|t
expr_stmt|;
name|cost
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getChildren ()
specifier|public
specifier|final
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
name|unmodifiableList
argument_list|(
name|children
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getChildCount ()
specifier|public
specifier|final
name|int
name|getChildCount
parameter_list|()
block|{
return|return
name|children
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getChild (final int i)
specifier|public
specifier|final
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
name|children
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|copy (final Collection<? extends Predicate<T>> children)
specifier|public
name|Predicate
argument_list|<
name|T
argument_list|>
name|copy
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
name|children
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|<>
argument_list|(
name|children
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|match (final T object)
specifier|public
name|boolean
name|match
parameter_list|(
specifier|final
name|T
name|object
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|c
range|:
name|children
control|)
block|{
if|if
condition|(
name|c
operator|.
name|match
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
name|cost
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getChild
argument_list|(
literal|0
argument_list|)
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
operator|+
name|getChild
argument_list|(
literal|1
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (final Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|==
literal|null
condition|)
return|return
literal|false
return|;
return|return
name|getClass
argument_list|()
operator|==
name|other
operator|.
name|getClass
argument_list|()
operator|&&
name|getChildren
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Predicate
argument_list|<
name|?
argument_list|>
operator|)
name|other
operator|)
operator|.
name|getChildren
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|!=
literal|0
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|" OR "
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|getChild
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

