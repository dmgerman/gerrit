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
DECL|package|com.google.gerrit.client.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|api
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
name|gerrit
operator|.
name|client
operator|.
name|changes
operator|.
name|ChangeInfo
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
name|changes
operator|.
name|ChangeInfo
operator|.
name|ActionInfo
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
name|common
operator|.
name|PageLinks
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
name|client
operator|.
name|Change
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
name|JavaScriptObject
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

begin_class
DECL|class|DefaultActions
class|class
name|DefaultActions
block|{
DECL|method|invoke (ChangeInfo change, ActionInfo action, RestApi api)
specifier|static
name|void
name|invoke
parameter_list|(
name|ChangeInfo
name|change
parameter_list|,
name|ActionInfo
name|action
parameter_list|,
name|RestApi
name|api
parameter_list|)
block|{
specifier|final
name|Change
operator|.
name|Id
name|id
init|=
name|change
operator|.
name|legacy_id
argument_list|()
decl_stmt|;
name|AsyncCallback
argument_list|<
name|JavaScriptObject
argument_list|>
name|cb
init|=
operator|new
name|GerritCallback
argument_list|<
name|JavaScriptObject
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JavaScriptObject
name|msg
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange2
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
if|if
condition|(
literal|"PUT"
operator|.
name|equalsIgnoreCase
argument_list|(
name|action
operator|.
name|method
argument_list|()
argument_list|)
condition|)
block|{
name|api
operator|.
name|put
argument_list|(
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"DELETE"
operator|.
name|equalsIgnoreCase
argument_list|(
name|action
operator|.
name|method
argument_list|()
argument_list|)
condition|)
block|{
name|api
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|api
operator|.
name|post
argument_list|(
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|DefaultActions ()
specifier|private
name|DefaultActions
parameter_list|()
block|{   }
block|}
end_class

end_unit

