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
DECL|package|com.google.gerrit.server.git
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|RepositoryConfig
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

begin_comment
comment|/**  * Module to install {@link MultiBaseLocalDiskRepositoryManager} rather than {@link  * LocalDiskRepositoryManager} if needed.  */
end_comment

begin_class
DECL|class|GitRepositoryManagerModule
specifier|public
class|class
name|GitRepositoryManagerModule
extends|extends
name|LifecycleModule
block|{
DECL|field|repoConfig
specifier|private
specifier|final
name|RepositoryConfig
name|repoConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitRepositoryManagerModule (RepositoryConfig repoConfig)
specifier|public
name|GitRepositoryManagerModule
parameter_list|(
name|RepositoryConfig
name|repoConfig
parameter_list|)
block|{
name|this
operator|.
name|repoConfig
operator|=
name|repoConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
if|if
condition|(
name|repoConfig
operator|.
name|getAllBasePaths
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|install
argument_list|(
operator|new
name|LocalDiskRepositoryManager
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|install
argument_list|(
operator|new
name|MultiBaseLocalDiskRepositoryManager
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

