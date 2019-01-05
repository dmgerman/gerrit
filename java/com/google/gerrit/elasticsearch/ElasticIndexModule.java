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
DECL|package|com.google.gerrit.elasticsearch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
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
name|index
operator|.
name|project
operator|.
name|ProjectIndex
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
name|AbstractIndexModule
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
name|VersionManager
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
name|account
operator|.
name|AccountIndex
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
name|change
operator|.
name|ChangeIndex
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
name|group
operator|.
name|GroupIndex
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ElasticIndexModule
specifier|public
class|class
name|ElasticIndexModule
extends|extends
name|AbstractIndexModule
block|{
DECL|method|singleVersionWithExplicitVersions ( Map<String, Integer> versions, int threads, boolean slave)
specifier|public
specifier|static
name|ElasticIndexModule
name|singleVersionWithExplicitVersions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|versions
parameter_list|,
name|int
name|threads
parameter_list|,
name|boolean
name|slave
parameter_list|)
block|{
return|return
operator|new
name|ElasticIndexModule
argument_list|(
name|versions
argument_list|,
name|threads
argument_list|,
name|slave
argument_list|)
return|;
block|}
DECL|method|latestVersion (boolean slave)
specifier|public
specifier|static
name|ElasticIndexModule
name|latestVersion
parameter_list|(
name|boolean
name|slave
parameter_list|)
block|{
return|return
operator|new
name|ElasticIndexModule
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
name|slave
argument_list|)
return|;
block|}
DECL|method|ElasticIndexModule (Map<String, Integer> singleVersions, int threads, boolean slave)
specifier|private
name|ElasticIndexModule
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
parameter_list|,
name|int
name|threads
parameter_list|,
name|boolean
name|slave
parameter_list|)
block|{
name|super
argument_list|(
name|singleVersions
argument_list|,
name|threads
argument_list|,
name|slave
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|super
operator|.
name|configure
argument_list|()
expr_stmt|;
name|install
argument_list|(
name|ElasticRestClientProvider
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAccountIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|AccountIndex
argument_list|>
name|getAccountIndex
parameter_list|()
block|{
return|return
name|ElasticAccountIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getChangeIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|ChangeIndex
argument_list|>
name|getChangeIndex
parameter_list|()
block|{
return|return
name|ElasticChangeIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getGroupIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|GroupIndex
argument_list|>
name|getGroupIndex
parameter_list|()
block|{
return|return
name|ElasticGroupIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getProjectIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|ProjectIndex
argument_list|>
name|getProjectIndex
parameter_list|()
block|{
return|return
name|ElasticProjectIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getVersionManager ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|VersionManager
argument_list|>
name|getVersionManager
parameter_list|()
block|{
return|return
name|ElasticIndexVersionManager
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

