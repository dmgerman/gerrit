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
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_comment
comment|/**  * Result set that allows for asynchronous execution of the actual query. Callers should dispatch  * the query and call the constructor of this class with a supplier that fetches the result and  * blocks on it if necessary.  *  *<p>If the execution is synchronous or the results are known a priori, consider using {@link  * ListResultSet}.  */
end_comment

begin_class
DECL|class|LazyResultSet
specifier|public
class|class
name|LazyResultSet
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ResultSet
argument_list|<
name|T
argument_list|>
block|{
DECL|field|resultsCallback
specifier|private
specifier|final
name|Supplier
argument_list|<
name|ImmutableList
argument_list|<
name|T
argument_list|>
argument_list|>
name|resultsCallback
decl_stmt|;
DECL|field|resultsReturned
specifier|private
name|boolean
name|resultsReturned
init|=
literal|false
decl_stmt|;
DECL|method|LazyResultSet (Supplier<ImmutableList<T>> r)
specifier|public
name|LazyResultSet
parameter_list|(
name|Supplier
argument_list|<
name|ImmutableList
argument_list|<
name|T
argument_list|>
argument_list|>
name|r
parameter_list|)
block|{
name|resultsCallback
operator|=
name|requireNonNull
argument_list|(
name|r
argument_list|,
literal|"results can't be null"
argument_list|)
expr_stmt|;
block|}
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
return|return
name|toList
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
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
if|if
condition|(
name|resultsReturned
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Results already obtained"
argument_list|)
throw|;
block|}
name|resultsReturned
operator|=
literal|true
expr_stmt|;
return|return
name|resultsCallback
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{}
block|}
end_class

end_unit

