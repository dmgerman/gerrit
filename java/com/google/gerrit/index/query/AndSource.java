begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|checkArgument
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
name|base
operator|.
name|Throwables
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
name|FluentIterable
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
name|common
operator|.
name|collect
operator|.
name|Iterables
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
name|ListResultSet
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
name|OrmRuntimeException
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
name|ArrayList
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
name|Comparator
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

begin_class
DECL|class|AndSource
specifier|public
class|class
name|AndSource
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AndPredicate
argument_list|<
name|T
argument_list|>
implements|implements
name|DataSource
argument_list|<
name|T
argument_list|>
implements|,
name|Comparator
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
DECL|field|source
specifier|protected
specifier|final
name|DataSource
argument_list|<
name|T
argument_list|>
name|source
decl_stmt|;
DECL|field|isVisibleToPredicate
specifier|private
specifier|final
name|IsVisibleToPredicate
argument_list|<
name|T
argument_list|>
name|isVisibleToPredicate
decl_stmt|;
DECL|field|start
specifier|private
specifier|final
name|int
name|start
decl_stmt|;
DECL|field|cardinality
specifier|private
specifier|final
name|int
name|cardinality
decl_stmt|;
DECL|method|AndSource (Collection<? extends Predicate<T>> that)
specifier|public
name|AndSource
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
name|that
parameter_list|)
block|{
name|this
argument_list|(
name|that
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|AndSource (Predicate<T> that, IsVisibleToPredicate<T> isVisibleToPredicate)
specifier|public
name|AndSource
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|that
parameter_list|,
name|IsVisibleToPredicate
argument_list|<
name|T
argument_list|>
name|isVisibleToPredicate
parameter_list|)
block|{
name|this
argument_list|(
name|that
argument_list|,
name|isVisibleToPredicate
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|AndSource (Predicate<T> that, IsVisibleToPredicate<T> isVisibleToPredicate, int start)
specifier|public
name|AndSource
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|that
parameter_list|,
name|IsVisibleToPredicate
argument_list|<
name|T
argument_list|>
name|isVisibleToPredicate
parameter_list|,
name|int
name|start
parameter_list|)
block|{
name|this
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|that
argument_list|)
argument_list|,
name|isVisibleToPredicate
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
DECL|method|AndSource ( Collection<? extends Predicate<T>> that, IsVisibleToPredicate<T> isVisibleToPredicate, int start)
specifier|public
name|AndSource
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
name|that
parameter_list|,
name|IsVisibleToPredicate
argument_list|<
name|T
argument_list|>
name|isVisibleToPredicate
parameter_list|,
name|int
name|start
parameter_list|)
block|{
name|super
argument_list|(
name|that
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|start
operator|>=
literal|0
argument_list|,
literal|"negative start: %s"
argument_list|,
name|start
argument_list|)
expr_stmt|;
name|this
operator|.
name|isVisibleToPredicate
operator|=
name|isVisibleToPredicate
expr_stmt|;
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
name|int
name|c
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|DataSource
argument_list|<
name|T
argument_list|>
name|s
init|=
literal|null
decl_stmt|;
name|int
name|minCost
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
for|for
control|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|p
range|:
name|sort
argument_list|(
name|getChildren
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|p
operator|instanceof
name|DataSource
condition|)
block|{
name|c
operator|=
name|Math
operator|.
name|min
argument_list|(
name|c
argument_list|,
operator|(
operator|(
name|DataSource
argument_list|<
name|?
argument_list|>
operator|)
name|p
operator|)
operator|.
name|getCardinality
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|cost
init|=
name|p
operator|.
name|estimateCost
argument_list|()
decl_stmt|;
if|if
condition|(
name|cost
operator|<
name|minCost
condition|)
block|{
name|s
operator|=
name|toDataSource
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|minCost
operator|=
name|cost
expr_stmt|;
block|}
block|}
block|}
name|this
operator|.
name|source
operator|=
name|s
expr_stmt|;
name|this
operator|.
name|cardinality
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|ResultSet
argument_list|<
name|T
argument_list|>
name|read
parameter_list|()
throws|throws
name|OrmException
block|{
try|try
block|{
return|return
name|readImpl
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OrmRuntimeException
name|err
parameter_list|)
block|{
if|if
condition|(
name|err
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|err
operator|.
name|getCause
argument_list|()
argument_list|,
name|OrmException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|OrmException
argument_list|(
name|err
argument_list|)
throw|;
block|}
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
throws|throws
name|OrmException
block|{
comment|// TOOD(hiesel): Implement
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not implemented"
argument_list|)
throw|;
block|}
DECL|method|readImpl ()
specifier|private
name|ResultSet
argument_list|<
name|T
argument_list|>
name|readImpl
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"No DataSource: "
operator|+
name|this
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|T
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|T
name|last
init|=
literal|null
decl_stmt|;
name|int
name|nextStart
init|=
literal|0
decl_stmt|;
name|boolean
name|skipped
init|=
literal|false
decl_stmt|;
for|for
control|(
name|T
name|data
range|:
name|buffer
argument_list|(
name|source
operator|.
name|read
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|isMatchable
argument_list|()
operator|||
name|match
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|skipped
operator|=
literal|true
expr_stmt|;
block|}
name|last
operator|=
name|data
expr_stmt|;
name|nextStart
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|skipped
operator|&&
name|last
operator|!=
literal|null
operator|&&
name|source
operator|instanceof
name|Paginated
condition|)
block|{
comment|// If our source is a paginated source and we skipped at
comment|// least one of its results, we may not have filled the full
comment|// limit the caller wants.  Restart the source and continue.
comment|//
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Paginated
argument_list|<
name|T
argument_list|>
name|p
init|=
operator|(
name|Paginated
argument_list|<
name|T
argument_list|>
operator|)
name|source
decl_stmt|;
while|while
condition|(
name|skipped
operator|&&
name|r
operator|.
name|size
argument_list|()
operator|<
name|p
operator|.
name|getOptions
argument_list|()
operator|.
name|limit
argument_list|()
operator|+
name|start
condition|)
block|{
name|skipped
operator|=
literal|false
expr_stmt|;
name|ResultSet
argument_list|<
name|T
argument_list|>
name|next
init|=
name|p
operator|.
name|restart
argument_list|(
name|nextStart
argument_list|)
decl_stmt|;
for|for
control|(
name|T
name|data
range|:
name|buffer
argument_list|(
name|next
argument_list|)
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|skipped
operator|=
literal|true
expr_stmt|;
block|}
name|nextStart
operator|++
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|start
operator|>=
name|r
operator|.
name|size
argument_list|()
condition|)
block|{
name|r
operator|=
name|ImmutableList
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|start
operator|>
literal|0
condition|)
block|{
name|r
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|r
operator|.
name|subList
argument_list|(
name|start
argument_list|,
name|r
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ListResultSet
argument_list|<>
argument_list|(
name|r
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
name|isVisibleToPredicate
operator|!=
literal|null
operator|||
name|super
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
throws|throws
name|OrmException
block|{
if|if
condition|(
name|isVisibleToPredicate
operator|!=
literal|null
operator|&&
operator|!
name|isVisibleToPredicate
operator|.
name|match
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|super
operator|.
name|isMatchable
argument_list|()
operator|&&
operator|!
name|super
operator|.
name|match
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|buffer (ResultSet<T> scanner)
specifier|private
name|Iterable
argument_list|<
name|T
argument_list|>
name|buffer
parameter_list|(
name|ResultSet
argument_list|<
name|T
argument_list|>
name|scanner
parameter_list|)
block|{
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|Iterables
operator|.
name|partition
argument_list|(
name|scanner
argument_list|,
literal|50
argument_list|)
argument_list|)
operator|.
name|transformAndConcat
argument_list|(
name|this
operator|::
name|transformBuffer
argument_list|)
return|;
block|}
DECL|method|transformBuffer (List<T> buffer)
specifier|protected
name|List
argument_list|<
name|T
argument_list|>
name|transformBuffer
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|buffer
parameter_list|)
throws|throws
name|OrmRuntimeException
block|{
return|return
name|buffer
return|;
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
name|cardinality
return|;
block|}
DECL|method|sort (Collection<? extends Predicate<T>> that)
specifier|private
name|List
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|sort
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
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|that
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|r
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Override
DECL|method|compare (Predicate<T> a, Predicate<T> b)
specifier|public
name|int
name|compare
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|a
parameter_list|,
name|Predicate
argument_list|<
name|T
argument_list|>
name|b
parameter_list|)
block|{
name|int
name|ai
init|=
name|a
operator|instanceof
name|DataSource
condition|?
literal|0
else|:
literal|1
decl_stmt|;
name|int
name|bi
init|=
name|b
operator|instanceof
name|DataSource
condition|?
literal|0
else|:
literal|1
decl_stmt|;
name|int
name|cmp
init|=
name|ai
operator|-
name|bi
decl_stmt|;
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
name|cmp
operator|=
name|a
operator|.
name|estimateCost
argument_list|()
operator|-
name|b
operator|.
name|estimateCost
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cmp
operator|==
literal|0
operator|&&
name|a
operator|instanceof
name|DataSource
operator|&&
name|b
operator|instanceof
name|DataSource
condition|)
block|{
name|DataSource
argument_list|<
name|?
argument_list|>
name|as
init|=
operator|(
name|DataSource
argument_list|<
name|?
argument_list|>
operator|)
name|a
decl_stmt|;
name|DataSource
argument_list|<
name|?
argument_list|>
name|bs
init|=
operator|(
name|DataSource
argument_list|<
name|?
argument_list|>
operator|)
name|b
decl_stmt|;
name|cmp
operator|=
name|as
operator|.
name|getCardinality
argument_list|()
operator|-
name|bs
operator|.
name|getCardinality
argument_list|()
expr_stmt|;
block|}
return|return
name|cmp
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|toDataSource (Predicate<T> pred)
specifier|private
name|DataSource
argument_list|<
name|T
argument_list|>
name|toDataSource
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|pred
parameter_list|)
block|{
return|return
operator|(
name|DataSource
argument_list|<
name|T
argument_list|>
operator|)
name|pred
return|;
block|}
block|}
end_class

end_unit

