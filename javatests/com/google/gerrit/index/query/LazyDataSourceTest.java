begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|gerrit
operator|.
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|OrSource
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
name|Iterator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests that boolean data sources are lazy in that they don't call {@link ResultSet#toList()} or  * {@link ResultSet#toList()}. This is necessary because it allows Gerrit to send multiple queries  * to the index in parallel, have the results come in asynchronously and wait for them only when we  * call aforementioned methods on the {@link ResultSet}.  */
end_comment

begin_class
DECL|class|LazyDataSourceTest
specifier|public
class|class
name|LazyDataSourceTest
block|{
comment|/** Helper to avoid a mock which would be hard to create because of the type inference. */
DECL|class|LazyPredicate
specifier|static
class|class
name|LazyPredicate
extends|extends
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
implements|implements
name|ChangeDataSource
block|{
annotation|@
name|Override
DECL|method|getCardinality ()
specifier|public
name|int
name|getCardinality
parameter_list|()
block|{
return|return
literal|1
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
block|{
return|return
operator|new
name|FailingResultSet
argument_list|<>
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|readRaw ()
specifier|public
name|ResultSet
argument_list|<
name|FieldBundle
argument_list|>
name|readRaw
parameter_list|()
block|{
return|return
operator|new
name|FailingResultSet
argument_list|<>
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|copy (Collection<? extends Predicate<ChangeData>> children)
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not implemented"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not implemented"
argument_list|)
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not implemented"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not implemented"
argument_list|)
throw|;
block|}
block|}
comment|/** Implementation that throws {@link AssertionError} when accessing results. */
DECL|class|FailingResultSet
specifier|static
class|class
name|FailingResultSet
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ResultSet
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"called iterator() on the result set, but shouldn't have because the data source must be lazy"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|toList ()
specifier|public
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|toList
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"called toList() on the result set, but shouldn't have because the data source must be lazy"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// No-op
block|}
block|}
annotation|@
name|Test
DECL|method|andSourceIsLazy ()
specifier|public
name|void
name|andSourceIsLazy
parameter_list|()
block|{
name|AndSource
argument_list|<
name|ChangeData
argument_list|>
name|and
init|=
operator|new
name|AndSource
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|LazyPredicate
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|resultSet
init|=
name|and
operator|.
name|read
argument_list|()
decl_stmt|;
name|assertThrows
argument_list|(
name|AssertionError
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|resultSet
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|orSourceIsLazy ()
specifier|public
name|void
name|orSourceIsLazy
parameter_list|()
block|{
name|OrSource
name|or
init|=
operator|new
name|OrSource
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|LazyPredicate
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|resultSet
init|=
name|or
operator|.
name|read
argument_list|()
decl_stmt|;
name|assertThrows
argument_list|(
name|AssertionError
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|resultSet
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

