begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.receive
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|receive
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
name|plugincontext
operator|.
name|PluginSetContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|PostReceiveHook
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ReceivePack
import|;
end_import

begin_class
DECL|class|LazyPostReceiveHookChain
class|class
name|LazyPostReceiveHookChain
implements|implements
name|PostReceiveHook
block|{
DECL|field|hooks
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|PostReceiveHook
argument_list|>
name|hooks
decl_stmt|;
annotation|@
name|Inject
DECL|method|LazyPostReceiveHookChain (PluginSetContext<PostReceiveHook> hooks)
name|LazyPostReceiveHookChain
parameter_list|(
name|PluginSetContext
argument_list|<
name|PostReceiveHook
argument_list|>
name|hooks
parameter_list|)
block|{
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onPostReceive (ReceivePack rp, Collection<ReceiveCommand> commands)
specifier|public
name|void
name|onPostReceive
parameter_list|(
name|ReceivePack
name|rp
parameter_list|,
name|Collection
argument_list|<
name|ReceiveCommand
argument_list|>
name|commands
parameter_list|)
block|{
name|hooks
operator|.
name|runEach
argument_list|(
name|h
lambda|->
name|h
operator|.
name|onPostReceive
argument_list|(
name|rp
argument_list|,
name|commands
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

