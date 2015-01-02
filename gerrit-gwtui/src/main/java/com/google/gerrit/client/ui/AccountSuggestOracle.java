begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|gerrit
operator|.
name|client
operator|.
name|FormatUtil
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
name|account
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
name|ui
operator|.
name|SuggestOracle
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
name|List
import|;
end_import

begin_comment
comment|/** Suggestion Oracle for Account entities. */
end_comment

begin_class
DECL|class|AccountSuggestOracle
specifier|public
class|class
name|AccountSuggestOracle
extends|extends
name|SuggestAfterTypingNCharsOracle
block|{
annotation|@
name|Override
DECL|method|_onRequestSuggestions (final Request req, final Callback cb)
specifier|public
name|void
name|_onRequestSuggestions
parameter_list|(
specifier|final
name|Request
name|req
parameter_list|,
specifier|final
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
name|in
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
name|in
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountInfo
name|p
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|in
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
name|p
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
block|}
argument_list|)
expr_stmt|;
block|}
DECL|class|AccountSuggestion
specifier|private
specifier|static
class|class
name|AccountSuggestion
implements|implements
name|SuggestOracle
operator|.
name|Suggestion
block|{
DECL|field|info
specifier|private
specifier|final
name|AccountInfo
name|info
decl_stmt|;
DECL|method|AccountSuggestion (final AccountInfo k)
name|AccountSuggestion
parameter_list|(
specifier|final
name|AccountInfo
name|k
parameter_list|)
block|{
name|info
operator|=
name|k
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDisplayString ()
specifier|public
name|String
name|getDisplayString
parameter_list|()
block|{
return|return
name|FormatUtil
operator|.
name|nameEmail
argument_list|(
name|info
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getReplacementString ()
specifier|public
name|String
name|getReplacementString
parameter_list|()
block|{
return|return
name|FormatUtil
operator|.
name|nameEmail
argument_list|(
name|info
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

