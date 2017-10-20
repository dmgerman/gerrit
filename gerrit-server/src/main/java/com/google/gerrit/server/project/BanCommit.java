begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|collect
operator|.
name|Lists
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
name|BanCommitInput
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
name|UnprocessableEntityException
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
name|BanCommitResult
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
name|BanCommit
operator|.
name|BanResultInfo
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
name|update
operator|.
name|BatchUpdate
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
name|update
operator|.
name|RetryHelper
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
name|update
operator|.
name|RetryingRestModifyView
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
name|update
operator|.
name|UpdateException
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
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|BanCommit
specifier|public
class|class
name|BanCommit
extends|extends
name|RetryingRestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|BanCommitInput
argument_list|,
name|BanResultInfo
argument_list|>
block|{
DECL|field|banCommit
specifier|private
specifier|final
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
name|BanCommit
name|banCommit
decl_stmt|;
annotation|@
name|Inject
DECL|method|BanCommit (RetryHelper retryHelper, com.google.gerrit.server.git.BanCommit banCommit)
name|BanCommit
parameter_list|(
name|RetryHelper
name|retryHelper
parameter_list|,
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
name|BanCommit
name|banCommit
parameter_list|)
block|{
name|super
argument_list|(
name|retryHelper
argument_list|)
expr_stmt|;
name|this
operator|.
name|banCommit
operator|=
name|banCommit
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|applyImpl ( BatchUpdate.Factory updateFactory, ProjectResource rsrc, BanCommitInput input)
specifier|protected
name|BanResultInfo
name|applyImpl
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ProjectResource
name|rsrc
parameter_list|,
name|BanCommitInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
name|BanResultInfo
name|r
init|=
operator|new
name|BanResultInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|!=
literal|null
operator|&&
name|input
operator|.
name|commits
operator|!=
literal|null
operator|&&
operator|!
name|input
operator|.
name|commits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|ObjectId
argument_list|>
name|commitsToBan
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|input
operator|.
name|commits
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|c
range|:
name|input
operator|.
name|commits
control|)
block|{
try|try
block|{
name|commitsToBan
operator|.
name|add
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|BanCommitResult
name|result
init|=
name|banCommit
operator|.
name|ban
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|commitsToBan
argument_list|,
name|input
operator|.
name|reason
argument_list|)
decl_stmt|;
name|r
operator|.
name|newlyBanned
operator|=
name|transformCommits
argument_list|(
name|result
operator|.
name|getNewlyBannedCommits
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|alreadyBanned
operator|=
name|transformCommits
argument_list|(
name|result
operator|.
name|getAlreadyBannedCommits
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|ignored
operator|=
name|transformCommits
argument_list|(
name|result
operator|.
name|getIgnoredObjectIds
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|transformCommits (List<ObjectId> commits)
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|transformCommits
parameter_list|(
name|List
argument_list|<
name|ObjectId
argument_list|>
name|commits
parameter_list|)
block|{
if|if
condition|(
name|commits
operator|==
literal|null
operator|||
name|commits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Lists
operator|.
name|transform
argument_list|(
name|commits
argument_list|,
name|ObjectId
operator|::
name|getName
argument_list|)
return|;
block|}
DECL|class|BanResultInfo
specifier|public
specifier|static
class|class
name|BanResultInfo
block|{
DECL|field|newlyBanned
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|newlyBanned
decl_stmt|;
DECL|field|alreadyBanned
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|alreadyBanned
decl_stmt|;
DECL|field|ignored
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|ignored
decl_stmt|;
block|}
block|}
end_class

end_unit

