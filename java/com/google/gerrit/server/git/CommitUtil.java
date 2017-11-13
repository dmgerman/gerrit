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
name|extensions
operator|.
name|common
operator|.
name|CommitInfo
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
name|CommonConverters
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

begin_comment
comment|/** Static utilities for working with {@link RevCommit}s. */
end_comment

begin_class
DECL|class|CommitUtil
specifier|public
class|class
name|CommitUtil
block|{
DECL|method|toCommitInfo (RevCommit commit)
specifier|public
specifier|static
name|CommitInfo
name|toCommitInfo
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
block|{
name|CommitInfo
name|info
init|=
operator|new
name|CommitInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|commit
operator|=
name|commit
operator|.
name|getName
argument_list|()
expr_stmt|;
name|info
operator|.
name|author
operator|=
name|CommonConverters
operator|.
name|toGitPerson
argument_list|(
name|commit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|committer
operator|=
name|CommonConverters
operator|.
name|toGitPerson
argument_list|(
name|commit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|subject
operator|=
name|commit
operator|.
name|getShortMessage
argument_list|()
expr_stmt|;
name|info
operator|.
name|message
operator|=
name|commit
operator|.
name|getFullMessage
argument_list|()
expr_stmt|;
name|info
operator|.
name|parents
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|commit
operator|.
name|getParentCount
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|commit
operator|.
name|getParentCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|RevCommit
name|p
init|=
name|commit
operator|.
name|getParent
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|CommitInfo
name|parentInfo
init|=
operator|new
name|CommitInfo
argument_list|()
decl_stmt|;
name|parentInfo
operator|.
name|commit
operator|=
name|p
operator|.
name|getName
argument_list|()
expr_stmt|;
name|parentInfo
operator|.
name|subject
operator|=
name|p
operator|.
name|getShortMessage
argument_list|()
expr_stmt|;
name|info
operator|.
name|parents
operator|.
name|add
argument_list|(
name|parentInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
DECL|method|CommitUtil ()
specifier|private
name|CommitUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

