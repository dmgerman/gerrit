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
name|RestApi
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
name|extensions
operator|.
name|client
operator|.
name|ListChangesOption
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JsArray
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|JsArray
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
comment|// If changing default options, also update in
comment|// ChangeIT#defaultSearchDoesNotTouchDatabase().
DECL|field|OPTIONS
specifier|private
specifier|static
specifier|final
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|OPTIONS
init|=
name|EnumSet
operator|.
name|of
argument_list|(
name|ListChangesOption
operator|.
name|LABELS
argument_list|,
name|ListChangesOption
operator|.
name|DETAILED_ACCOUNTS
argument_list|)
decl_stmt|;
comment|/** Run multiple queries in a single remote invocation. */
DECL|method|queryMultiple ( final AsyncCallback<JsArray<ChangeList>> callback, EnumSet<ListChangesOption> options, String... queries)
specifier|public
specifier|static
name|void
name|queryMultiple
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|JsArray
argument_list|<
name|ChangeList
argument_list|>
argument_list|>
name|callback
parameter_list|,
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|options
parameter_list|,
name|String
modifier|...
name|queries
parameter_list|)
block|{
if|if
condition|(
name|queries
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|RestApi
name|call
init|=
operator|new
name|RestApi
argument_list|(
name|URI
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|q
range|:
name|queries
control|)
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
name|q
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|OPTIONS
operator|.
name|addAll
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|addOptions
argument_list|(
name|call
argument_list|,
name|OPTIONS
argument_list|)
expr_stmt|;
if|if
condition|(
name|queries
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|// Server unwraps a single query, so wrap it back in an array for the
comment|// callback.
name|call
operator|.
name|get
argument_list|(
operator|new
name|AsyncCallback
argument_list|<
name|ChangeList
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeList
name|result
parameter_list|)
block|{
name|JsArray
argument_list|<
name|ChangeList
argument_list|>
name|wrapped
init|=
name|JsArray
operator|.
name|createArray
argument_list|(
literal|1
argument_list|)
operator|.
name|cast
argument_list|()
decl_stmt|;
name|wrapped
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onSuccess
argument_list|(
name|wrapped
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|call
operator|.
name|get
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|query (String query, EnumSet<ListChangesOption> options, AsyncCallback<ChangeList> callback)
specifier|public
specifier|static
name|void
name|query
parameter_list|(
name|String
name|query
parameter_list|,
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|options
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
name|addOptions
argument_list|(
name|call
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|call
operator|.
name|get
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|next (String query, int start, int limit, AsyncCallback<ChangeList> callback)
specifier|public
specifier|static
name|void
name|next
parameter_list|(
name|String
name|query
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|limit
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
name|addOptions
argument_list|(
name|call
argument_list|,
name|OPTIONS
argument_list|)
expr_stmt|;
if|if
condition|(
name|start
operator|!=
literal|0
condition|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"S"
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
name|call
operator|.
name|get
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|addOptions (RestApi call, EnumSet<ListChangesOption> s)
specifier|public
specifier|static
name|void
name|addOptions
parameter_list|(
name|RestApi
name|call
parameter_list|,
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|s
parameter_list|)
block|{
name|call
operator|.
name|addParameterRaw
argument_list|(
literal|"O"
argument_list|,
name|Integer
operator|.
name|toHexString
argument_list|(
name|ListChangesOption
operator|.
name|toBits
argument_list|(
name|s
argument_list|)
argument_list|)
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

