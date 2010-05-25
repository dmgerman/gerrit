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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|CurrentUser
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
name|config
operator|.
name|GerritServerConfig
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
name|git
operator|.
name|WorkQueue
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
import|;
end_import

begin_class
DECL|class|CommandExecutorProvider
class|class
name|CommandExecutorProvider
implements|implements
name|Provider
argument_list|<
name|WorkQueue
operator|.
name|Executor
argument_list|>
block|{
DECL|field|queues
specifier|private
specifier|final
name|QueueProvider
name|queues
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommandExecutorProvider (QueueProvider queues, CurrentUser user)
name|CommandExecutorProvider
parameter_list|(
name|QueueProvider
name|queues
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|queues
operator|=
name|queues
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|WorkQueue
operator|.
name|Executor
name|get
parameter_list|()
block|{
name|WorkQueue
operator|.
name|Executor
name|executor
decl_stmt|;
if|if
condition|(
name|user
operator|.
name|isBatchUser
argument_list|()
condition|)
block|{
name|executor
operator|=
name|queues
operator|.
name|getBatchQueue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|executor
operator|=
name|queues
operator|.
name|getInteractiveQueue
argument_list|()
expr_stmt|;
block|}
return|return
name|executor
return|;
block|}
block|}
end_class

end_unit

