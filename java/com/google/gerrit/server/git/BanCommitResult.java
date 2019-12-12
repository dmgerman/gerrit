begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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

begin_comment
comment|/** The outcome of the {@link com.google.gerrit.server.git.BanCommit} operation. */
end_comment

begin_class
DECL|class|BanCommitResult
specifier|public
class|class
name|BanCommitResult
block|{
DECL|field|newlyBannedCommits
specifier|private
specifier|final
name|List
argument_list|<
name|ObjectId
argument_list|>
name|newlyBannedCommits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
DECL|field|alreadyBannedCommits
specifier|private
specifier|final
name|List
argument_list|<
name|ObjectId
argument_list|>
name|alreadyBannedCommits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
DECL|field|ignoredObjectIds
specifier|private
specifier|final
name|List
argument_list|<
name|ObjectId
argument_list|>
name|ignoredObjectIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
DECL|method|commitBanned (ObjectId commitId)
specifier|public
name|void
name|commitBanned
parameter_list|(
name|ObjectId
name|commitId
parameter_list|)
block|{
name|newlyBannedCommits
operator|.
name|add
argument_list|(
name|commitId
argument_list|)
expr_stmt|;
block|}
DECL|method|commitAlreadyBanned (ObjectId commitId)
specifier|public
name|void
name|commitAlreadyBanned
parameter_list|(
name|ObjectId
name|commitId
parameter_list|)
block|{
name|alreadyBannedCommits
operator|.
name|add
argument_list|(
name|commitId
argument_list|)
expr_stmt|;
block|}
DECL|method|notACommit (ObjectId id)
specifier|public
name|void
name|notACommit
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|ignoredObjectIds
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|getNewlyBannedCommits ()
specifier|public
name|List
argument_list|<
name|ObjectId
argument_list|>
name|getNewlyBannedCommits
parameter_list|()
block|{
return|return
name|newlyBannedCommits
return|;
block|}
DECL|method|getAlreadyBannedCommits ()
specifier|public
name|List
argument_list|<
name|ObjectId
argument_list|>
name|getAlreadyBannedCommits
parameter_list|()
block|{
return|return
name|alreadyBannedCommits
return|;
block|}
DECL|method|getIgnoredObjectIds ()
specifier|public
name|List
argument_list|<
name|ObjectId
argument_list|>
name|getIgnoredObjectIds
parameter_list|()
block|{
return|return
name|ignoredObjectIds
return|;
block|}
block|}
end_class

end_unit

