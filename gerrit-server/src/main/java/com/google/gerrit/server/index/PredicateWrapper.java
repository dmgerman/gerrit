begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
comment|// limitations under the License.package com.google.gerrit.server.git;
end_comment

begin_package
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|QueryParseException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeDataSource
import|;
end_import

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
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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

begin_comment
comment|/**  * Wrapper combining an {@link IndexPredicate} together with a  * {@link ChangeDataSource} that returns matching results from the index.  *<p>  * Appropriate to return as the rootmost predicate that can be processed using  * the secondary index; such predicates must also implement  * {@link ChangeDataSource} to be chosen by the query processor.  */
end_comment

begin_class
DECL|class|PredicateWrapper
specifier|public
class|class
name|PredicateWrapper
extends|extends
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
implements|implements
name|ChangeDataSource
block|{
DECL|field|pred
specifier|private
specifier|final
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
name|pred
decl_stmt|;
DECL|field|source
specifier|private
specifier|final
name|ChangeDataSource
name|source
decl_stmt|;
DECL|method|PredicateWrapper (ChangeIndex index, IndexPredicate<ChangeData> pred)
specifier|public
name|PredicateWrapper
parameter_list|(
name|ChangeIndex
name|index
parameter_list|,
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
name|pred
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|this
operator|.
name|pred
operator|=
name|pred
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|index
operator|.
name|getSource
argument_list|(
name|pred
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCardinality ()
specifier|public
name|int
name|getCardinality
parameter_list|()
block|{
return|return
name|source
operator|.
name|getCardinality
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
return|return
name|source
operator|.
name|hasChange
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|read
parameter_list|()
throws|throws
name|OrmException
block|{
return|return
name|source
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|copy ( Collection<? extends Predicate<ChangeData>> children)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|copy
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|children
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|pred
operator|.
name|match
argument_list|(
name|cd
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
name|pred
operator|.
name|getCost
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
name|pred
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
return|return
name|other
operator|!=
literal|null
operator|&&
name|getClass
argument_list|()
operator|==
name|other
operator|.
name|getClass
argument_list|()
operator|&&
name|pred
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|PredicateWrapper
operator|)
name|other
operator|)
operator|.
name|pred
argument_list|)
return|;
block|}
block|}
end_class

end_unit

