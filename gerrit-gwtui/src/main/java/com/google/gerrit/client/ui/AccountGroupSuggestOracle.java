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
name|RpcStatus
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
name|reviewdb
operator|.
name|AccountGroup
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
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|HighlightSuggestOracle
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
comment|/** Suggestion Oracle for AccountGroup entities. */
end_comment

begin_class
DECL|class|AccountGroupSuggestOracle
specifier|public
class|class
name|AccountGroupSuggestOracle
extends|extends
name|HighlightSuggestOracle
block|{
annotation|@
name|Override
DECL|method|onRequestSuggestions (final Request req, final Callback callback)
specifier|public
name|void
name|onRequestSuggestions
parameter_list|(
specifier|final
name|Request
name|req
parameter_list|,
specifier|final
name|Callback
name|callback
parameter_list|)
block|{
name|RpcStatus
operator|.
name|hide
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|SuggestUtil
operator|.
name|SVC
operator|.
name|suggestAccountGroup
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
name|List
argument_list|<
name|AccountGroup
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|result
parameter_list|)
block|{
specifier|final
name|ArrayList
argument_list|<
name|AccountGroupSuggestion
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroupSuggestion
argument_list|>
argument_list|(
name|result
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
name|p
range|:
name|result
control|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|AccountGroupSuggestion
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|callback
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
argument_list|)
expr_stmt|;
block|}
DECL|class|AccountGroupSuggestion
specifier|private
specifier|static
class|class
name|AccountGroupSuggestion
implements|implements
name|SuggestOracle
operator|.
name|Suggestion
block|{
DECL|field|info
specifier|private
specifier|final
name|AccountGroup
name|info
decl_stmt|;
DECL|method|AccountGroupSuggestion (final AccountGroup k)
name|AccountGroupSuggestion
parameter_list|(
specifier|final
name|AccountGroup
name|k
parameter_list|)
block|{
name|info
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getDisplayString ()
specifier|public
name|String
name|getDisplayString
parameter_list|()
block|{
return|return
name|info
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getReplacementString ()
specifier|public
name|String
name|getReplacementString
parameter_list|()
block|{
return|return
name|info
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

