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
DECL|package|com.google.gerrit.acceptance.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|pgm
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
name|elasticsearch
operator|.
name|ElasticContainer
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
name|elasticsearch
operator|.
name|ElasticTestUtils
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
name|elasticsearch
operator|.
name|ElasticTestUtils
operator|.
name|ElasticNodeInfo
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
name|elasticsearch
operator|.
name|ElasticVersion
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
name|testutil
operator|.
name|ConfigSuite
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
name|Injector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_class
DECL|class|ElasticReindexIT
specifier|public
class|class
name|ElasticReindexIT
extends|extends
name|AbstractReindexTests
block|{
DECL|field|container
specifier|private
specifier|static
name|ElasticContainer
argument_list|<
name|?
argument_list|>
name|container
decl_stmt|;
DECL|method|getConfig (ElasticVersion version)
specifier|private
specifier|static
name|Config
name|getConfig
parameter_list|(
name|ElasticVersion
name|version
parameter_list|)
block|{
name|ElasticNodeInfo
name|elasticNodeInfo
decl_stmt|;
name|container
operator|=
name|ElasticContainer
operator|.
name|createAndStart
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|elasticNodeInfo
operator|=
operator|new
name|ElasticNodeInfo
argument_list|(
name|container
operator|.
name|getHttpHost
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|indicesPrefix
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|ElasticTestUtils
operator|.
name|configure
argument_list|(
name|cfg
argument_list|,
name|elasticNodeInfo
operator|.
name|port
argument_list|,
name|indicesPrefix
argument_list|,
name|version
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|elasticsearchV2 ()
specifier|public
specifier|static
name|Config
name|elasticsearchV2
parameter_list|()
block|{
return|return
name|getConfig
argument_list|(
name|ElasticVersion
operator|.
name|V2_4
argument_list|)
return|;
block|}
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|elasticsearchV5 ()
specifier|public
specifier|static
name|Config
name|elasticsearchV5
parameter_list|()
block|{
return|return
name|getConfig
argument_list|(
name|ElasticVersion
operator|.
name|V5_6
argument_list|)
return|;
block|}
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|elasticsearchV6_2 ()
specifier|public
specifier|static
name|Config
name|elasticsearchV6_2
parameter_list|()
block|{
return|return
name|getConfig
argument_list|(
name|ElasticVersion
operator|.
name|V6_2
argument_list|)
return|;
block|}
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|elasticsearchV6_3 ()
specifier|public
specifier|static
name|Config
name|elasticsearchV6_3
parameter_list|()
block|{
return|return
name|getConfig
argument_list|(
name|ElasticVersion
operator|.
name|V6_3
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|configureIndex (Injector injector)
specifier|public
name|void
name|configureIndex
parameter_list|(
name|Injector
name|injector
parameter_list|)
throws|throws
name|Exception
block|{
name|ElasticTestUtils
operator|.
name|createAllIndexes
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stopElasticServer ()
specifier|public
name|void
name|stopElasticServer
parameter_list|()
block|{
if|if
condition|(
name|container
operator|!=
literal|null
condition|)
block|{
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
name|container
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

