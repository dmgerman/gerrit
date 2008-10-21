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
DECL|package|com.google.codereview.rpc
package|package
name|com
operator|.
name|google
operator|.
name|codereview
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
name|protobuf
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|RpcCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|RpcChannel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|RpcController
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|Descriptors
operator|.
name|MethodDescriptor
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_class
DECL|class|MockRpcChannel
specifier|public
class|class
name|MockRpcChannel
implements|implements
name|RpcChannel
block|{
DECL|field|calls
specifier|private
name|LinkedList
argument_list|<
name|RpcChannel
argument_list|>
name|calls
init|=
operator|new
name|LinkedList
argument_list|<
name|RpcChannel
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|add (final RpcChannel c)
specifier|public
name|void
name|add
parameter_list|(
specifier|final
name|RpcChannel
name|c
parameter_list|)
block|{
name|calls
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|callMethod (final MethodDescriptor method, final RpcController controller, final Message request, final Message responsePrototype, final RpcCallback<Message> done)
specifier|public
name|void
name|callMethod
parameter_list|(
specifier|final
name|MethodDescriptor
name|method
parameter_list|,
specifier|final
name|RpcController
name|controller
parameter_list|,
specifier|final
name|Message
name|request
parameter_list|,
specifier|final
name|Message
name|responsePrototype
parameter_list|,
specifier|final
name|RpcCallback
argument_list|<
name|Message
argument_list|>
name|done
parameter_list|)
block|{
if|if
condition|(
name|calls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|TestCase
operator|.
name|fail
argument_list|(
literal|"Incorrect call for "
operator|+
name|method
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|RpcChannel
name|c
init|=
name|calls
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
name|c
operator|.
name|callMethod
argument_list|(
name|method
argument_list|,
name|controller
argument_list|,
name|request
argument_list|,
name|responsePrototype
argument_list|,
name|done
argument_list|)
expr_stmt|;
block|}
DECL|method|assertAllCallsMade ()
specifier|public
name|void
name|assertAllCallsMade
parameter_list|()
block|{
name|TestCase
operator|.
name|assertTrue
argument_list|(
name|calls
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

