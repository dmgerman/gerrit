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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|sshd
operator|.
name|CommandMetaData
operator|.
name|Mode
operator|.
name|MASTER_OR_SLAVE
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
name|base
operator|.
name|Function
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
name|base
operator|.
name|Joiner
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
name|Lists
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|project
operator|.
name|BanCommit
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
name|project
operator|.
name|BanCommit
operator|.
name|BanResultInfo
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
name|project
operator|.
name|ProjectControl
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
name|project
operator|.
name|ProjectResource
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
name|sshd
operator|.
name|CommandMetaData
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
name|sshd
operator|.
name|SshCommand
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
name|Inject
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

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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

begin_class
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"ban-commit"
argument_list|,
name|description
operator|=
literal|"Ban a commit from a project's repository"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|BanCommitCommand
specifier|public
class|class
name|BanCommitCommand
extends|extends
name|SshCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--reason"
argument_list|,
name|aliases
operator|=
block|{
literal|"-r"
block|}
argument_list|,
name|metaVar
operator|=
literal|"REASON"
argument_list|,
name|usage
operator|=
literal|"reason for banning the commit"
argument_list|)
DECL|field|reason
specifier|private
name|String
name|reason
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"PROJECT"
argument_list|,
name|usage
operator|=
literal|"name of the project for which the commit should be banned"
argument_list|)
DECL|field|projectControl
specifier|private
name|ProjectControl
name|projectControl
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"COMMIT"
argument_list|,
name|usage
operator|=
literal|"commit(s) that should be banned"
argument_list|)
DECL|field|commitsToBan
specifier|private
name|List
argument_list|<
name|ObjectId
argument_list|>
name|commitsToBan
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
DECL|field|banCommit
specifier|private
name|BanCommit
name|banCommit
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|Failure
block|{
try|try
block|{
name|BanCommit
operator|.
name|Input
name|input
init|=
name|BanCommit
operator|.
name|Input
operator|.
name|fromCommits
argument_list|(
name|Lists
operator|.
name|transform
argument_list|(
name|commitsToBan
argument_list|,
operator|new
name|Function
argument_list|<
name|ObjectId
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|ObjectId
name|oid
parameter_list|)
block|{
return|return
name|oid
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|input
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
name|BanResultInfo
name|r
init|=
name|banCommit
operator|.
name|apply
argument_list|(
operator|new
name|ProjectResource
argument_list|(
name|projectControl
argument_list|)
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|printCommits
argument_list|(
name|r
operator|.
name|newlyBanned
argument_list|,
literal|"The following commits were banned"
argument_list|)
expr_stmt|;
name|printCommits
argument_list|(
name|r
operator|.
name|alreadyBanned
argument_list|,
literal|"The following commits were already banned"
argument_list|)
expr_stmt|;
name|printCommits
argument_list|(
name|r
operator|.
name|ignored
argument_list|,
literal|"The following ids do not represent commits and were ignored"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RestApiException
decl||
name|IOException
decl||
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
name|die
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|printCommits (List<String> commits, String message)
specifier|private
name|void
name|printCommits
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|commits
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|commits
operator|!=
literal|null
operator|&&
operator|!
name|commits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|message
operator|+
literal|":\n"
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|",\n"
argument_list|)
operator|.
name|join
argument_list|(
name|commits
argument_list|)
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

