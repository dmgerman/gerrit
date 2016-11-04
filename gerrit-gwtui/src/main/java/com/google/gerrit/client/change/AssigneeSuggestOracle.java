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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
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
name|account
operator|.
name|AccountApi
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
name|info
operator|.
name|AccountInfo
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
name|GerritCallback
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
name|Natives
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
name|ui
operator|.
name|AccountSuggestOracle
operator|.
name|AccountSuggestion
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
name|ui
operator|.
name|SuggestAfterTypingNCharsOracle
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
name|Collections
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
comment|/** REST API based suggestion Oracle for assignee */
end_comment

begin_class
DECL|class|AssigneeSuggestOracle
specifier|public
class|class
name|AssigneeSuggestOracle
extends|extends
name|SuggestAfterTypingNCharsOracle
block|{
annotation|@
name|Override
DECL|method|_onRequestSuggestions (Request req, Callback cb)
specifier|protected
name|void
name|_onRequestSuggestions
parameter_list|(
name|Request
name|req
parameter_list|,
name|Callback
name|cb
parameter_list|)
block|{
name|AccountApi
operator|.
name|suggest
argument_list|(
name|req
operator|.
name|getQuery
argument_list|()
argument_list|,
name|req
operator|.
name|getLimit
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|JsArray
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JsArray
argument_list|<
name|AccountInfo
argument_list|>
name|result
parameter_list|)
block|{
name|List
argument_list|<
name|AccountSuggestion
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|result
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountInfo
name|reviewer
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|result
argument_list|)
control|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|AccountSuggestion
argument_list|(
name|reviewer
argument_list|,
name|req
operator|.
name|getQuery
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|cb
operator|.
name|onSuggestionsReady
argument_list|(
name|req
argument_list|,
operator|new
name|Response
argument_list|(
name|r
argument_list|)
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
name|err
parameter_list|)
block|{
name|List
argument_list|<
name|Suggestion
argument_list|>
name|r
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|cb
operator|.
name|onSuggestionsReady
argument_list|(
name|req
argument_list|,
operator|new
name|Response
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

