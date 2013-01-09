begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|rpc
operator|.
name|GerritCallback
import|;
end_import

begin_comment
comment|/**  * GerritCallback to be used on user interfaces that allow filtering to handle  * RPC's that request filtering. The user may change the filter quickly so that  * a response may be outdated when the client receives it. In this case the  * response must be ignored because the responses to RCP's may come out-of-order  * and an outdated response would overwrite the correct result which was  * received before.  */
end_comment

begin_class
DECL|class|IgnoreOutdatedFilterResultsCallbackWrapper
specifier|public
class|class
name|IgnoreOutdatedFilterResultsCallbackWrapper
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GerritCallback
argument_list|<
name|T
argument_list|>
block|{
DECL|field|filteredUI
specifier|private
specifier|final
name|FilteredUserInterface
name|filteredUI
decl_stmt|;
DECL|field|myFilter
specifier|private
specifier|final
name|String
name|myFilter
decl_stmt|;
DECL|field|cb
specifier|private
specifier|final
name|GerritCallback
argument_list|<
name|T
argument_list|>
name|cb
decl_stmt|;
DECL|method|IgnoreOutdatedFilterResultsCallbackWrapper ( final FilteredUserInterface filteredUI, final GerritCallback<T> cb)
specifier|public
name|IgnoreOutdatedFilterResultsCallbackWrapper
parameter_list|(
specifier|final
name|FilteredUserInterface
name|filteredUI
parameter_list|,
specifier|final
name|GerritCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|this
operator|.
name|filteredUI
operator|=
name|filteredUI
expr_stmt|;
name|this
operator|.
name|myFilter
operator|=
name|filteredUI
operator|.
name|getCurrentFilter
argument_list|()
expr_stmt|;
name|this
operator|.
name|cb
operator|=
name|cb
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuccess (final T result)
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|T
name|result
parameter_list|)
block|{
if|if
condition|(
operator|(
name|myFilter
operator|==
literal|null
operator|&&
name|filteredUI
operator|.
name|getCurrentFilter
argument_list|()
operator|==
literal|null
operator|)
operator|||
operator|(
name|myFilter
operator|!=
literal|null
operator|&&
name|myFilter
operator|.
name|equals
argument_list|(
name|filteredUI
operator|.
name|getCurrentFilter
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
comment|// Else ignore the result, the user has already changed the filter
comment|// and the result is not relevant anymore. If multiple RPC's are
comment|// fired the results may come back out-of-order and a non-relevant
comment|// result could overwrite the correct result if not ignored.
block|}
block|}
end_class

end_unit

