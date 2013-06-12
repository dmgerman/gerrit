begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
comment|// limitations under the License.package com.google.gerrit.server.git;
end_comment

begin_package
DECL|package|com.google.gerrit.solr
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|solr
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
name|index
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
name|IndexModule
import|;
end_import

begin_class
DECL|class|SolrIndexModule
specifier|public
class|class
name|SolrIndexModule
extends|extends
name|LifecycleModule
block|{
DECL|field|checkVersion
specifier|private
specifier|final
name|boolean
name|checkVersion
decl_stmt|;
DECL|field|threads
specifier|private
specifier|final
name|int
name|threads
decl_stmt|;
DECL|method|SolrIndexModule ()
specifier|public
name|SolrIndexModule
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|SolrIndexModule (boolean checkVersion, int threads)
specifier|public
name|SolrIndexModule
parameter_list|(
name|boolean
name|checkVersion
parameter_list|,
name|int
name|threads
parameter_list|)
block|{
name|this
operator|.
name|checkVersion
operator|=
name|checkVersion
expr_stmt|;
name|this
operator|.
name|threads
operator|=
name|threads
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
name|install
argument_list|(
operator|new
name|IndexModule
argument_list|(
name|threads
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ChangeIndex
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SolrChangeIndex
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|SolrChangeIndex
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|checkVersion
condition|)
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|IndexVersionCheck
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

