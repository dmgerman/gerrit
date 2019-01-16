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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
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
comment|/** Negates the result of another predicate. */
end_comment

begin_class
DECL|class|NotPredicate
specifier|public
class|class
name|NotPredicate
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Predicate
argument_list|<
name|T
argument_list|>
implements|implements
name|Matchable
argument_list|<
name|T
argument_list|>
block|{
DECL|field|that
specifier|private
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|that
decl_stmt|;
DECL|method|NotPredicate (Predicate<T> that)
specifier|protected
name|NotPredicate
parameter_list|(
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Double negation unsupported"
argument_list|)
throw|;
block|}
name|this
operator|.
name|that
operator|=
name|that
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
name|singletonList
argument_list|(
name|that
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
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|getChild (int i)
specifier|public
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|getChild
parameter_list|(
name|int
name|i
parameter_list|)
block|{
if|if
condition|(
name|i
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
name|i
argument_list|)
throw|;
block|}
return|return
name|that
return|;
block|}
annotation|@
name|Override
DECL|method|copy (Collection<? extends Predicate<T>> children)
specifier|public
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
block|{
if|if
condition|(
name|children
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected exactly one child"
argument_list|)
throw|;
block|}
return|return
operator|new
name|NotPredicate
argument_list|<>
argument_list|(
name|children
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isMatchable ()
specifier|public
name|boolean
name|isMatchable
parameter_list|()
block|{
return|return
name|that
operator|.
name|isMatchable
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|match (T object)
specifier|public
name|boolean
name|match
parameter_list|(
name|T
name|object
parameter_list|)
block|{
name|checkState
argument_list|(
name|that
operator|.
name|isMatchable
argument_list|()
argument_list|,
literal|"match invoked, but child predicate %s doesn't implement %s"
argument_list|,
name|that
argument_list|,
name|Matchable
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|!
name|that
operator|.
name|asMatchable
argument_list|()
operator|.
name|match
argument_list|(
name|object
argument_list|)
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
name|that
operator|.
name|estimateCost
argument_list|()
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
operator|~
name|that
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
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
block|{
return|return
literal|false
return|;
block|}
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
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"-"
operator|+
name|that
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

