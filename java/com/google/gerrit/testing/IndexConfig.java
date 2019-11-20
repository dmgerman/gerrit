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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
package|;
end_package

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

begin_class
DECL|class|IndexConfig
specifier|public
class|class
name|IndexConfig
block|{
DECL|method|create ()
specifier|public
specifier|static
name|Config
name|create
parameter_list|()
block|{
return|return
name|createFromExistingConfig
argument_list|(
operator|new
name|Config
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createFromExistingConfig (Config cfg)
specifier|public
specifier|static
name|Config
name|createFromExistingConfig
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|cfg
operator|.
name|setInt
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"maxPages"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
comment|// To avoid this flakiness indexMergeable is switched off for the tests as it incurs background
comment|// reindex calls.
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"change"
argument_list|,
literal|"indexMergeable"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"trackingid"
argument_list|,
literal|"query-bug"
argument_list|,
literal|"footer"
argument_list|,
literal|"Bug:"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"trackingid"
argument_list|,
literal|"query-bug"
argument_list|,
literal|"match"
argument_list|,
literal|"QUERY\\d{2,8}"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"trackingid"
argument_list|,
literal|"query-bug"
argument_list|,
literal|"system"
argument_list|,
literal|"querytests"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"trackingid"
argument_list|,
literal|"query-feature"
argument_list|,
literal|"footer"
argument_list|,
literal|"Feature"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"trackingid"
argument_list|,
literal|"query-feature"
argument_list|,
literal|"match"
argument_list|,
literal|"QUERY\\d{2,8}"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"trackingid"
argument_list|,
literal|"query-feature"
argument_list|,
literal|"system"
argument_list|,
literal|"querytests"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
DECL|method|createForLucene ()
specifier|public
specifier|static
name|Config
name|createForLucene
parameter_list|()
block|{
return|return
name|create
argument_list|()
return|;
block|}
DECL|method|createForElasticsearch ()
specifier|public
specifier|static
name|Config
name|createForElasticsearch
parameter_list|()
block|{
name|Config
name|cfg
init|=
name|create
argument_list|()
decl_stmt|;
comment|// For some reason enabling the staleness checker increases the flakiness of the Elasticsearch
comment|// tests. Hence disable the staleness checker.
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"autoReindexIfStale"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
block|}
end_class

end_unit

