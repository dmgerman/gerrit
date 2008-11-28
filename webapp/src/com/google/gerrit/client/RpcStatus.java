begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|FlowPanel
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
name|InlineLabel
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
name|Label
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
name|Panel
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
name|client
operator|.
name|RpcStatusListener
import|;
end_import

begin_class
DECL|class|RpcStatus
class|class
name|RpcStatus
implements|implements
name|RpcStatusListener
block|{
DECL|field|loading
specifier|private
specifier|final
name|Label
name|loading
decl_stmt|;
DECL|field|activeCalls
specifier|private
name|int
name|activeCalls
decl_stmt|;
DECL|method|RpcStatus (final Panel p)
name|RpcStatus
parameter_list|(
specifier|final
name|Panel
name|p
parameter_list|)
block|{
specifier|final
name|FlowPanel
name|r
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|r
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-RpcStatusPanel"
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|loading
operator|=
operator|new
name|InlineLabel
argument_list|()
expr_stmt|;
name|loading
operator|.
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|rpcStatusLoading
argument_list|()
argument_list|)
expr_stmt|;
name|loading
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-RpcStatus"
argument_list|)
expr_stmt|;
name|loading
operator|.
name|addStyleDependentName
argument_list|(
literal|"Loading"
argument_list|)
expr_stmt|;
name|loading
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|loading
argument_list|)
expr_stmt|;
block|}
DECL|method|onCallStart ()
specifier|public
name|void
name|onCallStart
parameter_list|()
block|{
if|if
condition|(
operator|++
name|activeCalls
operator|==
literal|1
condition|)
block|{
name|loading
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onCallEnd ()
specifier|public
name|void
name|onCallEnd
parameter_list|()
block|{
if|if
condition|(
operator|--
name|activeCalls
operator|==
literal|0
condition|)
block|{
name|loading
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

