begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|client
operator|.
name|rpc
operator|.
name|NativeList
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
name|client
operator|.
name|rpc
operator|.
name|RestApi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
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
name|client
operator|.
name|KeyUtil
import|;
end_import

begin_comment
comment|/** List of changes available from {@code /changes/}. */
end_comment

begin_class
DECL|class|ChangeList
specifier|public
class|class
name|ChangeList
extends|extends
name|NativeList
argument_list|<
name|ChangeInfo
argument_list|>
block|{
DECL|field|URI
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"/changes/"
decl_stmt|;
DECL|method|prev (String query, int limit, String sortkey, AsyncCallback<ChangeList> callback)
specifier|public
specifier|static
name|void
name|prev
parameter_list|(
name|String
name|query
parameter_list|,
name|int
name|limit
parameter_list|,
name|String
name|sortkey
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeList
argument_list|>
name|callback
parameter_list|)
block|{
name|RestApi
name|call
init|=
name|newQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"n"
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|PagedSingleListScreen
operator|.
name|MIN_SORTKEY
operator|.
name|equals
argument_list|(
name|sortkey
argument_list|)
condition|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"P"
argument_list|,
name|sortkey
argument_list|)
expr_stmt|;
block|}
name|call
operator|.
name|send
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|next (String query, int limit, String sortkey, AsyncCallback<ChangeList> callback)
specifier|public
specifier|static
name|void
name|next
parameter_list|(
name|String
name|query
parameter_list|,
name|int
name|limit
parameter_list|,
name|String
name|sortkey
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeList
argument_list|>
name|callback
parameter_list|)
block|{
name|RestApi
name|call
init|=
name|newQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"n"
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|PagedSingleListScreen
operator|.
name|MAX_SORTKEY
operator|.
name|equals
argument_list|(
name|sortkey
argument_list|)
condition|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"N"
argument_list|,
name|sortkey
argument_list|)
expr_stmt|;
block|}
name|call
operator|.
name|send
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|newQuery (String query)
specifier|private
specifier|static
name|RestApi
name|newQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|RestApi
name|call
init|=
operator|new
name|RestApi
argument_list|(
name|URI
argument_list|)
decl_stmt|;
comment|// The server default is ?q=status:open so don't repeat it.
if|if
condition|(
operator|!
literal|"status:open"
operator|.
name|equals
argument_list|(
name|query
argument_list|)
operator|&&
operator|!
literal|"is:open"
operator|.
name|equals
argument_list|(
name|query
argument_list|)
condition|)
block|{
name|call
operator|.
name|addParameterRaw
argument_list|(
literal|"q"
argument_list|,
name|KeyUtil
operator|.
name|encode
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|call
return|;
block|}
DECL|method|ChangeList ()
specifier|protected
name|ChangeList
parameter_list|()
block|{   }
block|}
end_class

end_unit

