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
comment|/** Predicate to filter a field by matching integer value. */
end_comment

begin_class
DECL|class|IntPredicate
specifier|public
specifier|abstract
class|class
name|IntPredicate
parameter_list|<
name|T
parameter_list|>
extends|extends
name|OperatorPredicate
argument_list|<
name|T
argument_list|>
block|{
DECL|field|value
specifier|private
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|IntPredicate (final String name, final String value)
specifier|public
name|IntPredicate
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|IntPredicate (final String name, final int value)
specifier|public
name|IntPredicate
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|int
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|intValue ()
specifier|public
name|int
name|intValue
parameter_list|()
block|{
return|return
name|value
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
operator|+
name|value
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
name|IntPredicate
argument_list|<
name|?
argument_list|>
name|p
init|=
operator|(
name|IntPredicate
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
operator|&&
name|intValue
argument_list|()
operator|==
name|p
operator|.
name|intValue
argument_list|()
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

