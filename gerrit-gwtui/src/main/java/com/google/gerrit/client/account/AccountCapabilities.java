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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
import|;
end_import

begin_comment
comment|/** Capabilities the caller has from {@code /accounts/self/capabilities}.  */
end_comment

begin_class
DECL|class|AccountCapabilities
specifier|public
class|class
name|AccountCapabilities
extends|extends
name|JavaScriptObject
block|{
DECL|method|all (AsyncCallback<AccountCapabilities> cb, String... filter)
specifier|public
specifier|static
name|void
name|all
parameter_list|(
name|AsyncCallback
argument_list|<
name|AccountCapabilities
argument_list|>
name|cb
parameter_list|,
name|String
modifier|...
name|filter
parameter_list|)
block|{
name|RestApi
name|api
init|=
operator|new
name|RestApi
argument_list|(
literal|"/accounts/self/capabilities"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|filter
control|)
block|{
name|api
operator|.
name|addParameter
argument_list|(
literal|"q"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
name|api
operator|.
name|send
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|AccountCapabilities ()
specifier|protected
name|AccountCapabilities
parameter_list|()
block|{   }
DECL|method|canPerform (String name)
specifier|public
specifier|final
specifier|native
name|boolean
name|canPerform
parameter_list|(
name|String
name|name
parameter_list|)
comment|/*-{ return this[name] ? true : false; }-*/
function_decl|;
block|}
end_class

end_unit

