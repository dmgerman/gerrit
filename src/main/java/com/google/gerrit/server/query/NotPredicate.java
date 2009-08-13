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
specifier|final
class|class
name|NotPredicate
extends|extends
name|Predicate
block|{
DECL|field|that
specifier|private
specifier|final
name|Predicate
name|that
decl_stmt|;
DECL|method|NotPredicate (final Predicate that)
specifier|public
name|NotPredicate
parameter_list|(
specifier|final
name|Predicate
name|that
parameter_list|)
block|{
name|this
operator|.
name|that
operator|=
name|that
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|not ()
specifier|public
name|Predicate
name|not
parameter_list|()
block|{
return|return
name|that
return|;
block|}
annotation|@
name|Override
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
name|singletonList
argument_list|(
name|that
argument_list|)
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
return|return
name|other
operator|instanceof
name|NotPredicate
operator|&&
name|getChildren
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Predicate
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

