begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
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
name|entities
operator|.
name|Account
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
name|entities
operator|.
name|Project
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
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|AdvertiseRefsHook
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
name|AdvertiseRefsHookChain
import|;
end_import

begin_comment
comment|/**  * Helper to ensure that the chain for advertising refs is the same in tests and production code.  */
end_comment

begin_class
DECL|class|ReceiveCommitsAdvertiseRefsHookChain
specifier|public
class|class
name|ReceiveCommitsAdvertiseRefsHookChain
block|{
comment|/**    * Returns a single {@link AdvertiseRefsHook} that encompasses a chain of {@link    * AdvertiseRefsHook} to be used for advertising when processing a Git push.    */
DECL|method|create ( AllRefsWatcher allRefsWatcher, Provider<InternalChangeQuery> queryProvider, Project.NameKey projectName, Account.Id user)
specifier|public
specifier|static
name|AdvertiseRefsHook
name|create
parameter_list|(
name|AllRefsWatcher
name|allRefsWatcher
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Account
operator|.
name|Id
name|user
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|allRefsWatcher
argument_list|,
name|queryProvider
argument_list|,
name|projectName
argument_list|,
name|user
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**    * Returns a single {@link AdvertiseRefsHook} that encompasses a chain of {@link    * AdvertiseRefsHook} to be used for advertising when processing a Git push. Omits {@link    * HackPushNegotiateHook} as that does not advertise refs on it's own but adds {@code .have} based    * on history which is not relevant for the tests we have.    */
annotation|@
name|VisibleForTesting
DECL|method|createForTest ( Provider<InternalChangeQuery> queryProvider, Project.NameKey projectName, Account.Id user)
specifier|public
specifier|static
name|AdvertiseRefsHook
name|createForTest
parameter_list|(
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Account
operator|.
name|Id
name|user
parameter_list|)
block|{
return|return
name|create
argument_list|(
operator|new
name|AllRefsWatcher
argument_list|()
argument_list|,
name|queryProvider
argument_list|,
name|projectName
argument_list|,
name|user
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|create ( AllRefsWatcher allRefsWatcher, Provider<InternalChangeQuery> queryProvider, Project.NameKey projectName, Account.Id user, boolean skipHackPushNegotiateHook)
specifier|private
specifier|static
name|AdvertiseRefsHook
name|create
parameter_list|(
name|AllRefsWatcher
name|allRefsWatcher
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Account
operator|.
name|Id
name|user
parameter_list|,
name|boolean
name|skipHackPushNegotiateHook
parameter_list|)
block|{
name|List
argument_list|<
name|AdvertiseRefsHook
argument_list|>
name|advHooks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|advHooks
operator|.
name|add
argument_list|(
name|allRefsWatcher
argument_list|)
expr_stmt|;
name|advHooks
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommitsAdvertiseRefsHook
argument_list|(
name|queryProvider
argument_list|,
name|projectName
argument_list|,
name|user
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|skipHackPushNegotiateHook
condition|)
block|{
name|advHooks
operator|.
name|add
argument_list|(
operator|new
name|HackPushNegotiateHook
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|AdvertiseRefsHookChain
operator|.
name|newChain
argument_list|(
name|advHooks
argument_list|)
return|;
block|}
block|}
end_class

end_unit

