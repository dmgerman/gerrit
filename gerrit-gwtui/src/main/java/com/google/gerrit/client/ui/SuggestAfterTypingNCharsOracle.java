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
name|Gerrit
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

begin_comment
comment|/**  * Suggest oracle that only provides suggestions if the user has typed at least  * as many characters as configured by 'suggest.from'. If 'suggest.from' is set  * to 0, suggestions will always be provided.  */
end_comment

begin_class
DECL|class|SuggestAfterTypingNCharsOracle
specifier|public
specifier|abstract
class|class
name|SuggestAfterTypingNCharsOracle
extends|extends
name|HighlightSuggestOracle
block|{
annotation|@
name|Override
DECL|method|onRequestSuggestions (final Request request, final Callback done)
specifier|protected
name|void
name|onRequestSuggestions
parameter_list|(
specifier|final
name|Request
name|request
parameter_list|,
specifier|final
name|Callback
name|done
parameter_list|)
block|{
specifier|final
name|int
name|suggestFrom
init|=
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getSuggestFrom
argument_list|()
decl_stmt|;
if|if
condition|(
name|suggestFrom
operator|==
literal|0
operator|||
name|request
operator|.
name|getQuery
argument_list|()
operator|.
name|length
argument_list|()
operator|>=
name|suggestFrom
condition|)
block|{
name|_onRequestSuggestions
argument_list|(
name|request
argument_list|,
name|done
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|_onRequestSuggestions (Request request, Callback done)
specifier|protected
specifier|abstract
name|void
name|_onRequestSuggestions
parameter_list|(
name|Request
name|request
parameter_list|,
name|Callback
name|done
parameter_list|)
function_decl|;
block|}
end_class

end_unit

