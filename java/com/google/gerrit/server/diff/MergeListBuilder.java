begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
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
name|ImmutableList
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_class
DECL|class|MergeListBuilder
specifier|public
class|class
name|MergeListBuilder
block|{
DECL|method|build (RevWalk rw, RevCommit merge, int uninterestingParent)
specifier|public
specifier|static
name|List
argument_list|<
name|RevCommit
argument_list|>
name|build
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|RevCommit
name|merge
parameter_list|,
name|int
name|uninterestingParent
parameter_list|)
throws|throws
name|IOException
block|{
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
name|rw
operator|.
name|parseBody
argument_list|(
name|merge
argument_list|)
expr_stmt|;
if|if
condition|(
name|merge
operator|.
name|getParentCount
argument_list|()
operator|<
literal|2
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
for|for
control|(
name|int
name|parent
init|=
literal|0
init|;
name|parent
operator|<
name|merge
operator|.
name|getParentCount
argument_list|()
condition|;
name|parent
operator|++
control|)
block|{
name|RevCommit
name|parentCommit
init|=
name|merge
operator|.
name|getParent
argument_list|(
name|parent
argument_list|)
decl_stmt|;
name|rw
operator|.
name|parseBody
argument_list|(
name|parentCommit
argument_list|)
expr_stmt|;
if|if
condition|(
name|parent
operator|==
name|uninterestingParent
operator|-
literal|1
condition|)
block|{
name|rw
operator|.
name|markUninteresting
argument_list|(
name|parentCommit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rw
operator|.
name|markStart
argument_list|(
name|parentCommit
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|RevCommit
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|RevCommit
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|rw
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

