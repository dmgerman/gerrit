begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|List
import|;
end_import

begin_comment
comment|/**  * Result set for queries that run synchronously or for cases where the result is already known and  * we just need to pipe it back through our interfaces.  *  *<p>If your implementation benefits from asynchronous execution (i.e. dispatching a query and  * awaiting results only when {@link ResultSet#toList()} is called, consider using {@link  * LazyResultSet}.  */
end_comment

begin_class
DECL|class|ListResultSet
specifier|public
class|class
name|ListResultSet
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ResultSet
argument_list|<
name|T
argument_list|>
block|{
DECL|field|results
specifier|private
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|results
decl_stmt|;
DECL|method|ListResultSet (List<T> r)
specifier|public
name|ListResultSet
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|r
parameter_list|)
block|{
name|results
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|requireNonNull
argument_list|(
name|r
argument_list|,
literal|"results can't be null"
argument_list|)
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
name|results
operator|==
literal|null
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
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|r
init|=
name|results
decl_stmt|;
name|results
operator|=
literal|null
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|results
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

