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

begin_comment
comment|/**  * Predicate only for use in rewrite rule patterns.  *<p>  * May<b>only</b> be used when nested immediately within a  * {@link VariablePredicate}. Within the QueryRewriter this predicate matches  * any other operator whose name matches this predicate's operator name.  *  * @see QueryRewriter  */
end_comment

begin_class
DECL|class|WildPatternPredicate
specifier|public
specifier|final
class|class
name|WildPatternPredicate
parameter_list|<
name|T
parameter_list|>
extends|extends
name|OperatorPredicate
argument_list|<
name|T
argument_list|>
block|{
DECL|method|WildPatternPredicate (final String name)
specifier|public
name|WildPatternPredicate
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
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
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot match "
operator|+
name|toString
argument_list|()
argument_list|)
throw|;
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
literal|0
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
name|getOperator
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
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
name|getClass
argument_list|()
operator|==
name|other
operator|.
name|getClass
argument_list|()
condition|)
block|{
specifier|final
name|WildPatternPredicate
argument_list|<
name|?
argument_list|>
name|p
init|=
operator|(
name|WildPatternPredicate
argument_list|<
name|?
argument_list|>
operator|)
name|other
decl_stmt|;
return|return
name|getOperator
argument_list|()
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getOperator
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
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
return|return
name|getOperator
argument_list|()
operator|+
literal|":"
operator|+
name|getValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

