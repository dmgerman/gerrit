begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rpc
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
name|server
operator|.
name|http
operator|.
name|RpcServletModule
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
name|server
operator|.
name|rpc
operator|.
name|account
operator|.
name|AccountModule
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
name|server
operator|.
name|rpc
operator|.
name|changedetail
operator|.
name|ChangeModule
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
name|server
operator|.
name|rpc
operator|.
name|patch
operator|.
name|PatchModule
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
name|server
operator|.
name|rpc
operator|.
name|project
operator|.
name|ProjectModule
import|;
end_import

begin_comment
comment|/** Registers servlets to answer RPCs from client UI. */
end_comment

begin_class
DECL|class|UiRpcModule
specifier|public
class|class
name|UiRpcModule
extends|extends
name|RpcServletModule
block|{
DECL|field|PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"/gerrit/rpc/"
decl_stmt|;
DECL|method|UiRpcModule ()
specifier|public
name|UiRpcModule
parameter_list|()
block|{
name|super
argument_list|(
name|PREFIX
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|rpc
argument_list|(
name|GroupAdminServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|ChangeListServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|SuggestServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|rpc
argument_list|(
name|SystemInfoServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|AccountModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|ChangeModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|PatchModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|ProjectModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

