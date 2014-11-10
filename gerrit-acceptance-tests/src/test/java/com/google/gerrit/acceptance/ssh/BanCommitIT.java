begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|assert_
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|add
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|createCommit
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|pushHead
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
name|GitUtil
operator|.
name|Commit
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|PushResult
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|BanCommitIT
specifier|public
class|class
name|BanCommitIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|banCommit ()
specifier|public
name|void
name|banCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|add
argument_list|(
name|git
argument_list|,
literal|"a.txt"
argument_list|,
literal|"some content"
argument_list|)
expr_stmt|;
name|Commit
name|c
init|=
name|createCommit
argument_list|(
name|git
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"subject"
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|sshSession
operator|.
name|exec
argument_list|(
literal|"gerrit ban-commit "
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|" "
operator|+
name|c
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assert_
argument_list|()
operator|.
name|withFailureMessage
argument_list|(
name|sshSession
operator|.
name|getError
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|sshSession
operator|.
name|hasError
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
operator|.
name|doesNotContain
argument_list|(
literal|"error"
argument_list|)
expr_stmt|;
name|PushResult
name|pushResult
init|=
name|pushHead
argument_list|(
name|git
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|pushResult
operator|.
name|getRemoteUpdate
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"contains banned commit"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

