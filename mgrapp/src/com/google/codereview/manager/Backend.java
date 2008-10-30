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
DECL|package|com.google.codereview.manager
package|package
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|Admin
operator|.
name|AdminService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|Build
operator|.
name|BuildService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|BundleStore
operator|.
name|BundleStoreService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|Change
operator|.
name|ChangeService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|Merge
operator|.
name|MergeService
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
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
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
name|ScheduledExecutorService
import|;
end_import

begin_comment
comment|/**  * Configuration and state related to a single Gerrit backend process.  */
end_comment

begin_class
DECL|class|Backend
specifier|public
class|class
name|Backend
block|{
DECL|field|repoCache
specifier|private
specifier|final
name|RepositoryCache
name|repoCache
decl_stmt|;
DECL|field|rpc
specifier|private
specifier|final
name|RpcChannel
name|rpc
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ScheduledExecutorService
name|executor
decl_stmt|;
DECL|field|mergeIdent
specifier|private
specifier|final
name|PersonIdent
name|mergeIdent
decl_stmt|;
DECL|field|adminSvc
specifier|private
specifier|final
name|AdminService
name|adminSvc
decl_stmt|;
DECL|field|buildSvc
specifier|private
specifier|final
name|BuildService
name|buildSvc
decl_stmt|;
DECL|field|bundleStoreSvc
specifier|private
specifier|final
name|BundleStoreService
name|bundleStoreSvc
decl_stmt|;
DECL|field|changeSvc
specifier|private
specifier|final
name|ChangeService
name|changeSvc
decl_stmt|;
DECL|field|mergeSvc
specifier|private
specifier|final
name|MergeService
name|mergeSvc
decl_stmt|;
DECL|method|Backend (final RepositoryCache cache, final RpcChannel api, final ScheduledExecutorService threadPool, final PersonIdent performMergsAs)
specifier|public
name|Backend
parameter_list|(
specifier|final
name|RepositoryCache
name|cache
parameter_list|,
specifier|final
name|RpcChannel
name|api
parameter_list|,
specifier|final
name|ScheduledExecutorService
name|threadPool
parameter_list|,
specifier|final
name|PersonIdent
name|performMergsAs
parameter_list|)
block|{
name|repoCache
operator|=
name|cache
expr_stmt|;
name|rpc
operator|=
name|api
expr_stmt|;
name|executor
operator|=
name|threadPool
expr_stmt|;
name|mergeIdent
operator|=
name|performMergsAs
expr_stmt|;
name|adminSvc
operator|=
name|AdminService
operator|.
name|newStub
argument_list|(
name|rpc
argument_list|)
expr_stmt|;
name|buildSvc
operator|=
name|BuildService
operator|.
name|newStub
argument_list|(
name|rpc
argument_list|)
expr_stmt|;
name|bundleStoreSvc
operator|=
name|BundleStoreService
operator|.
name|newStub
argument_list|(
name|rpc
argument_list|)
expr_stmt|;
name|changeSvc
operator|=
name|ChangeService
operator|.
name|newStub
argument_list|(
name|rpc
argument_list|)
expr_stmt|;
name|mergeSvc
operator|=
name|MergeService
operator|.
name|newStub
argument_list|(
name|rpc
argument_list|)
expr_stmt|;
block|}
DECL|method|getRepositoryCache ()
specifier|public
name|RepositoryCache
name|getRepositoryCache
parameter_list|()
block|{
return|return
name|repoCache
return|;
block|}
DECL|method|getRpcChannel ()
specifier|public
name|RpcChannel
name|getRpcChannel
parameter_list|()
block|{
return|return
name|rpc
return|;
block|}
DECL|method|getMergeIdentity ()
specifier|public
name|PersonIdent
name|getMergeIdentity
parameter_list|()
block|{
return|return
name|mergeIdent
return|;
block|}
comment|/**    * @return a copy of {@link #getMergeIdentity()} modified to use the current    *         system clock as the time, in the GMT/UTC time zone.    */
DECL|method|newMergeIdentity ()
specifier|public
name|PersonIdent
name|newMergeIdentity
parameter_list|()
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|getMergeIdentity
argument_list|()
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**    * Schedule a task for execution on a background thread.    *     * @param task runnable to perform the task. The task will be executed once,    *        as soon as a thread is available.    */
DECL|method|asyncExec (final Runnable task)
specifier|public
name|void
name|asyncExec
parameter_list|(
specifier|final
name|Runnable
name|task
parameter_list|)
block|{
name|executor
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
DECL|method|getAdminService ()
specifier|public
name|AdminService
name|getAdminService
parameter_list|()
block|{
return|return
name|adminSvc
return|;
block|}
DECL|method|getBuildService ()
specifier|public
name|BuildService
name|getBuildService
parameter_list|()
block|{
return|return
name|buildSvc
return|;
block|}
DECL|method|getBundleStoreService ()
specifier|public
name|BundleStoreService
name|getBundleStoreService
parameter_list|()
block|{
return|return
name|bundleStoreSvc
return|;
block|}
DECL|method|getChangeService ()
specifier|public
name|ChangeService
name|getChangeService
parameter_list|()
block|{
return|return
name|changeSvc
return|;
block|}
DECL|method|getMergeService ()
specifier|public
name|MergeService
name|getMergeService
parameter_list|()
block|{
return|return
name|mergeSvc
return|;
block|}
block|}
end_class

end_unit

