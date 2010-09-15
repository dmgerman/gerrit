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
operator|.
name|Callback
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
name|ui
operator|.
name|SuggestOracle
operator|.
name|Request
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
name|ui
operator|.
name|SuggestOracle
operator|.
name|Response
import|;
end_import

begin_comment
comment|/** This class will proxy SuggestOracle requests to another SuggestOracle  *  while keeping track of the latest request.  Any repsonse that belongs  *  to a request which is not the latest request will be dropped to prevent  *  invalid deliveries.  */
end_comment

begin_class
DECL|class|RPCSuggestOracle
specifier|public
class|class
name|RPCSuggestOracle
extends|extends
name|SuggestOracle
block|{
DECL|field|oracle
specifier|private
name|SuggestOracle
name|oracle
decl_stmt|;
DECL|field|request
specifier|private
name|SuggestOracle
operator|.
name|Request
name|request
decl_stmt|;
DECL|field|callback
specifier|private
name|SuggestOracle
operator|.
name|Callback
name|callback
decl_stmt|;
DECL|field|myCallback
specifier|private
name|SuggestOracle
operator|.
name|Callback
name|myCallback
init|=
operator|new
name|SuggestOracle
operator|.
name|Callback
argument_list|()
block|{
specifier|public
name|void
name|onSuggestionsReady
parameter_list|(
name|SuggestOracle
operator|.
name|Request
name|req
parameter_list|,
name|SuggestOracle
operator|.
name|Response
name|response
parameter_list|)
block|{
if|if
condition|(
name|request
operator|==
name|req
condition|)
block|{
name|callback
operator|.
name|onSuggestionsReady
argument_list|(
name|req
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|request
operator|=
literal|null
expr_stmt|;
name|callback
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
DECL|method|RPCSuggestOracle (SuggestOracle ora)
specifier|public
name|RPCSuggestOracle
parameter_list|(
name|SuggestOracle
name|ora
parameter_list|)
block|{
name|oracle
operator|=
name|ora
expr_stmt|;
block|}
DECL|method|requestSuggestions (SuggestOracle.Request req, SuggestOracle.Callback cb)
specifier|public
name|void
name|requestSuggestions
parameter_list|(
name|SuggestOracle
operator|.
name|Request
name|req
parameter_list|,
name|SuggestOracle
operator|.
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
name|oracle
operator|.
name|requestSuggestions
argument_list|(
name|req
argument_list|,
name|myCallback
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

