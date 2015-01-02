begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
package|;
end_package

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
name|ui
operator|.
name|SuggestOracle
import|;
end_import

begin_comment
comment|/**  * Delegates to a slow SuggestOracle, such as a remote server API.  *<p>  * A response is only supplied to the UI if no requests were made after the  * oracle begin that request.  *<p>  * When a request is made while the delegate is still processing a prior request  * all intermediate requests are discarded and the most recent request is  * queued. The pending request's response is discarded and the most recent  * request is started.  */
end_comment

begin_class
DECL|class|RemoteSuggestOracle
specifier|public
class|class
name|RemoteSuggestOracle
extends|extends
name|SuggestOracle
block|{
DECL|field|oracle
specifier|private
specifier|final
name|SuggestOracle
name|oracle
decl_stmt|;
DECL|field|query
specifier|private
name|Query
name|query
decl_stmt|;
DECL|field|last
specifier|private
name|String
name|last
decl_stmt|;
DECL|method|RemoteSuggestOracle (SuggestOracle src)
specifier|public
name|RemoteSuggestOracle
parameter_list|(
name|SuggestOracle
name|src
parameter_list|)
block|{
name|oracle
operator|=
name|src
expr_stmt|;
block|}
DECL|method|getLast ()
specifier|public
name|String
name|getLast
parameter_list|()
block|{
return|return
name|last
return|;
block|}
annotation|@
name|Override
DECL|method|requestSuggestions (Request req, Callback cb)
specifier|public
name|void
name|requestSuggestions
parameter_list|(
name|Request
name|req
parameter_list|,
name|Callback
name|cb
parameter_list|)
block|{
name|Query
name|q
init|=
operator|new
name|Query
argument_list|(
name|req
argument_list|,
name|cb
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|q
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|query
operator|=
name|q
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isDisplayStringHTML ()
specifier|public
name|boolean
name|isDisplayStringHTML
parameter_list|()
block|{
return|return
name|oracle
operator|.
name|isDisplayStringHTML
argument_list|()
return|;
block|}
DECL|class|Query
specifier|private
class|class
name|Query
implements|implements
name|Callback
block|{
DECL|field|request
specifier|final
name|Request
name|request
decl_stmt|;
DECL|field|callback
specifier|final
name|Callback
name|callback
decl_stmt|;
DECL|method|Query (Request req, Callback cb)
name|Query
parameter_list|(
name|Request
name|req
parameter_list|,
name|Callback
name|cb
parameter_list|)
block|{
name|request
operator|=
name|req
expr_stmt|;
name|callback
operator|=
name|cb
expr_stmt|;
block|}
DECL|method|start ()
name|void
name|start
parameter_list|()
block|{
name|oracle
operator|.
name|requestSuggestions
argument_list|(
name|request
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuggestionsReady (Request req, Response res)
specifier|public
name|void
name|onSuggestionsReady
parameter_list|(
name|Request
name|req
parameter_list|,
name|Response
name|res
parameter_list|)
block|{
if|if
condition|(
name|query
operator|==
name|this
condition|)
block|{
comment|// No new request was started while this query was running.
comment|// Propose this request's response as the suggestions.
name|query
operator|=
literal|null
expr_stmt|;
name|last
operator|=
name|request
operator|.
name|getQuery
argument_list|()
expr_stmt|;
name|callback
operator|.
name|onSuggestionsReady
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Another query came in while this one was running. Skip
comment|// this response and start the most recent query.
name|query
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

