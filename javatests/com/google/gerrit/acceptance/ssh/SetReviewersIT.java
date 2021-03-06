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
DECL|package|com.google.gerrit.acceptance.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|ssh
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

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
name|ImmutableSet
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|NoHttpd
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
name|acceptance
operator|.
name|PushOneCommit
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
name|acceptance
operator|.
name|SshSession
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
name|acceptance
operator|.
name|UseSsh
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
name|entities
operator|.
name|Account
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|UseSsh
annotation|@
name|NoHttpd
DECL|class|SetReviewersIT
specifier|public
class|class
name|SetReviewersIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|change
name|PushOneCommit
operator|.
name|Result
name|change
decl_stmt|;
DECL|field|session
name|SshSession
name|session
decl_stmt|;
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|asAdmin ()
specifier|public
specifier|static
name|Config
name|asAdmin
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"SetReviewersIT"
argument_list|,
literal|null
argument_list|,
literal|"asAdmin"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|change
operator|=
name|createChange
argument_list|()
expr_stmt|;
name|session
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"SetReviewersIT"
argument_list|,
literal|null
argument_list|,
literal|"asAdmin"
argument_list|,
literal|false
argument_list|)
condition|?
name|adminSshSession
else|:
name|userSshSession
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|byCommitHash ()
specifier|public
name|void
name|byCommitHash
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|change
operator|.
name|getCommit
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|"\\s+"
argument_list|)
index|[
literal|1
index|]
decl_stmt|;
name|addReviewer
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|removeReviewer
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|byChangeID ()
specifier|public
name|void
name|byChangeID
parameter_list|()
throws|throws
name|Exception
block|{
name|addReviewer
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|removeReviewer
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setReviewer (boolean add, String id)
specifier|private
name|void
name|setReviewer
parameter_list|(
name|boolean
name|add
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|session
operator|.
name|exec
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"gerrit set-reviewers -%s %s %s"
argument_list|,
name|add
condition|?
literal|"a"
else|:
literal|"r"
argument_list|,
name|user
operator|.
name|email
argument_list|()
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|assertSuccess
argument_list|()
expr_stmt|;
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewers
init|=
name|change
operator|.
name|getChange
argument_list|()
operator|.
name|getReviewers
argument_list|()
operator|.
name|all
argument_list|()
decl_stmt|;
if|if
condition|(
name|add
condition|)
block|{
name|assertThat
argument_list|(
name|reviewers
argument_list|)
operator|.
name|contains
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|reviewers
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addReviewer (String id)
specifier|private
name|void
name|addReviewer
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|setReviewer
argument_list|(
literal|true
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|removeReviewer (String id)
specifier|private
name|void
name|removeReviewer
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|setReviewer
argument_list|(
literal|false
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

