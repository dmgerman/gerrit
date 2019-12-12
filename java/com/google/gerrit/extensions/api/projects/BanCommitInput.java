begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Commits that will forbidden to be uploaded. */
end_comment

begin_class
DECL|class|BanCommitInput
specifier|public
class|class
name|BanCommitInput
block|{
DECL|field|commits
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|commits
decl_stmt|;
DECL|field|reason
specifier|public
name|String
name|reason
decl_stmt|;
DECL|method|fromCommits (String firstCommit, String... moreCommits)
specifier|public
specifier|static
name|BanCommitInput
name|fromCommits
parameter_list|(
name|String
name|firstCommit
parameter_list|,
name|String
modifier|...
name|moreCommits
parameter_list|)
block|{
return|return
name|fromCommits
argument_list|(
name|Lists
operator|.
name|asList
argument_list|(
name|firstCommit
argument_list|,
name|moreCommits
argument_list|)
argument_list|)
return|;
block|}
DECL|method|fromCommits (List<String> commits)
specifier|public
specifier|static
name|BanCommitInput
name|fromCommits
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|commits
parameter_list|)
block|{
name|BanCommitInput
name|in
init|=
operator|new
name|BanCommitInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|commits
operator|=
name|commits
expr_stmt|;
return|return
name|in
return|;
block|}
block|}
end_class

end_unit

