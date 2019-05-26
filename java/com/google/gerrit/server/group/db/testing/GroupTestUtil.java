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
DECL|package|com.google.gerrit.server.group.db.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|db
operator|.
name|testing
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
name|server
operator|.
name|config
operator|.
name|AllUsersName
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
name|git
operator|.
name|GitRepositoryManager
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
name|junit
operator|.
name|TestRepository
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
name|PersonIdent
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
name|Ref
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
name|Repository
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

begin_comment
comment|/** Test utilities for low-level NoteDb groups. */
end_comment

begin_class
DECL|class|GroupTestUtil
specifier|public
class|class
name|GroupTestUtil
block|{
DECL|method|updateGroupFile ( GitRepositoryManager repoManager, AllUsersName allUsersName, PersonIdent serverIdent, String refName, String fileName, String content)
specifier|public
specifier|static
name|void
name|updateGroupFile
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|PersonIdent
name|serverIdent
parameter_list|,
name|String
name|refName
parameter_list|,
name|String
name|fileName
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|updateGroupFile
argument_list|(
name|repo
argument_list|,
name|serverIdent
argument_list|,
name|refName
argument_list|,
name|fileName
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|updateGroupFile ( Repository allUsersRepo, PersonIdent serverIdent, String refName, String fileName, String contents)
specifier|public
specifier|static
name|void
name|updateGroupFile
parameter_list|(
name|Repository
name|allUsersRepo
parameter_list|,
name|PersonIdent
name|serverIdent
parameter_list|,
name|String
name|refName
parameter_list|,
name|String
name|fileName
parameter_list|,
name|String
name|contents
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|allUsersRepo
argument_list|)
init|;
name|TestRepository
argument_list|<
name|Repository
argument_list|>
name|testRepository
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|allUsersRepo
argument_list|,
name|rw
argument_list|)
init|)
block|{
name|TestRepository
argument_list|<
name|Repository
argument_list|>
operator|.
name|CommitBuilder
name|builder
init|=
name|testRepository
operator|.
name|branch
argument_list|(
name|refName
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|add
argument_list|(
name|fileName
argument_list|,
name|contents
argument_list|)
operator|.
name|message
argument_list|(
literal|"update group file"
argument_list|)
operator|.
name|author
argument_list|(
name|serverIdent
argument_list|)
operator|.
name|committer
argument_list|(
name|serverIdent
argument_list|)
decl_stmt|;
name|Ref
name|ref
init|=
name|allUsersRepo
operator|.
name|exactRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|RevCommit
name|c
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|parent
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|builder
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|GroupTestUtil ()
specifier|private
name|GroupTestUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

