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
import|import static
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
name|createAllIndexes
import|;
end_import

begin_import
import|import static
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
name|getConfig
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
name|testing
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
name|Before
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
annotation|@
name|ConfigSuite
operator|.
name|Default
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
DECL|method|elasticsearchV6 ()
specifier|public
specifier|static
name|Config
name|elasticsearchV6
parameter_list|()
block|{
return|return
name|getConfig
argument_list|(
name|ElasticVersion
operator|.
name|V6_8
argument_list|)
return|;
block|}
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|elasticsearchV7 ()
specifier|public
specifier|static
name|Config
name|elasticsearchV7
parameter_list|()
block|{
return|return
name|getConfig
argument_list|(
name|ElasticVersion
operator|.
name|V7_5
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
name|createAllIndexes
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|reindexFirstSinceElastic ()
specifier|public
name|void
name|reindexFirstSinceElastic
parameter_list|()
throws|throws
name|Exception
block|{
name|assertServerStartupFails
argument_list|()
expr_stmt|;
name|runGerrit
argument_list|(
literal|"reindex"
argument_list|,
literal|"-d"
argument_list|,
name|sitePaths
operator|.
name|site_path
operator|.
name|toString
argument_list|()
argument_list|,
literal|"--show-stack-trace"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

