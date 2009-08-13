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
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|AbbreviatedObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_comment
comment|/** Predicate for a field of {@link ObjectId}. */
end_comment

begin_class
DECL|class|ObjectIdPredicate
specifier|public
specifier|final
class|class
name|ObjectIdPredicate
extends|extends
name|OperatorPredicate
block|{
DECL|field|id
specifier|private
specifier|final
name|AbbreviatedObjectId
name|id
decl_stmt|;
DECL|method|ObjectIdPredicate (final String name, final AbbreviatedObjectId id)
specifier|public
name|ObjectIdPredicate
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|AbbreviatedObjectId
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|id
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|isComplete ()
specifier|public
name|boolean
name|isComplete
parameter_list|()
block|{
return|return
name|id
operator|.
name|isComplete
argument_list|()
return|;
block|}
DECL|method|abbreviated ()
specifier|public
name|AbbreviatedObjectId
name|abbreviated
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|full ()
specifier|public
name|ObjectId
name|full
parameter_list|()
block|{
return|return
name|id
operator|.
name|toObjectId
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
name|getOperator
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
operator|+
name|id
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
operator|instanceof
name|ObjectIdPredicate
condition|)
block|{
specifier|final
name|ObjectIdPredicate
name|p
init|=
operator|(
name|ObjectIdPredicate
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
name|id
operator|.
name|equals
argument_list|(
name|p
operator|.
name|id
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
name|id
operator|.
name|name
argument_list|()
return|;
block|}
block|}
end_class

end_unit

