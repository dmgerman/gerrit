begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|project
package|;
end_package

begin_import
import|import static
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
name|QueueProvider
operator|.
name|QueueType
operator|.
name|BATCH
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ListeningExecutorService
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
name|data
operator|.
name|GlobalCapability
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
name|extensions
operator|.
name|annotations
operator|.
name|RequiresCapability
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|IndexProjectInput
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
name|extensions
operator|.
name|common
operator|.
name|ProjectInfo
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
name|extensions
operator|.
name|restapi
operator|.
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|extensions
operator|.
name|restapi
operator|.
name|RestModifyView
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
name|index
operator|.
name|project
operator|.
name|ProjectIndexer
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
name|index
operator|.
name|IndexExecutor
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
name|permissions
operator|.
name|PermissionBackendException
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
name|project
operator|.
name|ProjectResource
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Future
import|;
end_import

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
argument_list|)
annotation|@
name|Singleton
DECL|class|Index
specifier|public
class|class
name|Index
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|IndexProjectInput
argument_list|>
block|{
DECL|field|indexer
specifier|private
specifier|final
name|ProjectIndexer
name|indexer
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ListeningExecutorService
name|executor
decl_stmt|;
DECL|field|listChildProjectsProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListChildProjects
argument_list|>
name|listChildProjectsProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|Index ( ProjectIndexer indexer, @IndexExecutor(BATCH) ListeningExecutorService executor, Provider<ListChildProjects> listChildProjectsProvider)
name|Index
parameter_list|(
name|ProjectIndexer
name|indexer
parameter_list|,
annotation|@
name|IndexExecutor
argument_list|(
name|BATCH
argument_list|)
name|ListeningExecutorService
name|executor
parameter_list|,
name|Provider
argument_list|<
name|ListChildProjects
argument_list|>
name|listChildProjectsProvider
parameter_list|)
block|{
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|listChildProjectsProvider
operator|=
name|listChildProjectsProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc, IndexProjectInput input)
specifier|public
name|Response
operator|.
name|Accepted
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|,
name|IndexProjectInput
name|input
parameter_list|)
throws|throws
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|RestApiException
block|{
name|String
name|response
init|=
literal|"Project "
operator|+
name|rsrc
operator|.
name|getName
argument_list|()
operator|+
literal|" submitted for reindexing"
decl_stmt|;
name|reindex
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|input
operator|.
name|async
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|input
operator|.
name|indexChildren
argument_list|)
condition|)
block|{
for|for
control|(
name|ProjectInfo
name|child
range|:
name|listChildProjectsProvider
operator|.
name|get
argument_list|()
operator|.
name|withRecursive
argument_list|(
literal|true
argument_list|)
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|)
operator|.
name|value
argument_list|()
control|)
block|{
name|reindex
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|child
operator|.
name|name
argument_list|)
argument_list|,
name|input
operator|.
name|async
argument_list|)
expr_stmt|;
block|}
name|response
operator|+=
literal|" (indexing children recursively)"
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|accepted
argument_list|(
name|response
argument_list|)
return|;
block|}
DECL|method|reindex (Project.NameKey project, Boolean async)
specifier|private
name|void
name|reindex
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Boolean
name|async
parameter_list|)
block|{
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|async
argument_list|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|executor
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
name|indexer
operator|.
name|index
argument_list|(
name|project
argument_list|)
argument_list|)
decl_stmt|;
block|}
else|else
block|{
name|indexer
operator|.
name|index
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

