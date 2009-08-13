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
comment|/** Predicate to filter a field by matching value. */
end_comment

begin_class
DECL|class|OperatorPredicate
specifier|public
class|class
name|OperatorPredicate
extends|extends
name|Predicate
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|OperatorPredicate (final String name, final String value)
specifier|public
name|OperatorPredicate
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
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getOperator ()
specifier|public
name|String
name|getOperator
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getValue ()
specifier|public
name|String
name|getValue
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
name|getValue
argument_list|()
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
name|OperatorPredicate
name|p
init|=
operator|(
name|OperatorPredicate
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
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getValue
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
specifier|final
name|String
name|val
init|=
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|QueryParser
operator|.
name|isSingleWord
argument_list|(
name|val
argument_list|)
condition|)
block|{
return|return
name|getOperator
argument_list|()
operator|+
literal|":"
operator|+
name|val
return|;
block|}
else|else
block|{
return|return
name|getOperator
argument_list|()
operator|+
literal|":\""
operator|+
name|val
operator|+
literal|"\""
return|;
block|}
block|}
block|}
end_class

end_unit

