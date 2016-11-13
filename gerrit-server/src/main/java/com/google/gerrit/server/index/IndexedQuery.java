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
name|common
operator|.
name|base
operator|.
name|MoreObjects
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
name|DataSource
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
name|Paginated
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
comment|/**  * Wrapper combining an {@link IndexPredicate} together with a {@link DataSource} that returns  * matching results from the index.  *  *<p>Appropriate to return as the rootmost predicate that can be processed using the secondary  * index; such predicates must also implement {@link DataSource} to be chosen by the query  * processor.  *  * @param<I> The type of the IDs by which the entities are stored in the index.  * @param<T> The type of the entities that are stored in the index.  */
end_comment

begin_class
DECL|class|IndexedQuery
specifier|public
class|class
name|IndexedQuery
parameter_list|<
name|I
parameter_list|,
name|T
parameter_list|>
extends|extends
name|Predicate
argument_list|<
name|T
argument_list|>
implements|implements
name|DataSource
argument_list|<
name|T
argument_list|>
implements|,
name|Paginated
argument_list|<
name|T
argument_list|>
block|{
DECL|field|index
specifier|protected
specifier|final
name|Index
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|index
decl_stmt|;
DECL|field|opts
specifier|private
name|QueryOptions
name|opts
decl_stmt|;
DECL|field|pred
specifier|private
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|pred
decl_stmt|;
DECL|field|source
specifier|protected
name|DataSource
argument_list|<
name|T
argument_list|>
name|source
decl_stmt|;
DECL|method|IndexedQuery (Index<I, T> index, Predicate<T> pred, QueryOptions opts)
specifier|public
name|IndexedQuery
parameter_list|(
name|Index
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|index
parameter_list|,
name|Predicate
argument_list|<
name|T
argument_list|>
name|pred
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|opts
operator|=
name|opts
expr_stmt|;
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
argument_list|,
name|this
operator|.
name|opts
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getChildCount ()
specifier|public
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
operator|==
literal|0
condition|)
block|{
return|return
name|pred
return|;
block|}
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
name|i
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getChildren ()
specifier|public
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
name|ImmutableList
operator|.
name|of
argument_list|(
name|pred
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getOptions ()
specifier|public
name|QueryOptions
name|getOptions
parameter_list|()
block|{
return|return
name|opts
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
name|source
operator|!=
literal|null
condition|?
name|source
operator|.
name|getCardinality
argument_list|()
else|:
name|opts
operator|.
name|limit
argument_list|()
return|;
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
return|return
name|source
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|restart (int start)
specifier|public
name|ResultSet
argument_list|<
name|T
argument_list|>
name|restart
parameter_list|(
name|int
name|start
parameter_list|)
throws|throws
name|OrmException
block|{
name|opts
operator|=
name|opts
operator|.
name|withStart
argument_list|(
name|start
argument_list|)
expr_stmt|;
try|try
block|{
name|source
operator|=
name|index
operator|.
name|getSource
argument_list|(
name|pred
argument_list|,
name|opts
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
comment|// Don't need to show this exception to the user; the only thing that
comment|// changed about pred was its start, and any other QPEs that might happen
comment|// should have already thrown from the constructor.
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// Don't convert start to a limit, since the caller of this method (see
comment|// AndSource) has calculated the actual number to skip.
return|return
name|read
argument_list|()
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
return|return
name|this
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
if|if
condition|(
name|other
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|other
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|IndexedQuery
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|o
init|=
operator|(
name|IndexedQuery
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|other
decl_stmt|;
return|return
name|pred
operator|.
name|equals
argument_list|(
name|o
operator|.
name|pred
argument_list|)
operator|&&
name|opts
operator|.
name|equals
argument_list|(
name|o
operator|.
name|opts
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
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
literal|"index"
argument_list|)
operator|.
name|add
argument_list|(
literal|"p"
argument_list|,
name|pred
argument_list|)
operator|.
name|add
argument_list|(
literal|"opts"
argument_list|,
name|opts
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

