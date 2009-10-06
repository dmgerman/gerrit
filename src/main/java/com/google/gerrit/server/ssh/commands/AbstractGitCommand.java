begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.ssh.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
operator|.
name|commands
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
name|client
operator|.
name|reviewdb
operator|.
name|Project
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
name|git
operator|.
name|GitRepositoryManager
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
name|ssh
operator|.
name|BaseCommand
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
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|AbstractGitCommand
specifier|abstract
class|class
name|AbstractGitCommand
extends|extends
name|BaseCommand
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|metaVar
operator|=
literal|"PROJECT.git"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"project name"
argument_list|)
DECL|field|projectControl
specifier|protected
name|ProjectControl
name|projectControl
decl_stmt|;
annotation|@
name|Inject
DECL|field|repoManager
specifier|protected
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|repo
specifier|protected
name|Repository
name|repo
decl_stmt|;
DECL|field|project
specifier|protected
name|Project
name|project
decl_stmt|;
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|startThread
argument_list|(
operator|new
name|CommandRunnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|parseCommandLine
argument_list|()
expr_stmt|;
name|AbstractGitCommand
operator|.
name|this
operator|.
name|service
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|service ()
specifier|private
name|void
name|service
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
block|{
name|project
operator|=
name|projectControl
operator|.
name|getProjectState
argument_list|()
operator|.
name|getProject
argument_list|()
expr_stmt|;
specifier|final
name|String
name|name
init|=
name|project
operator|.
name|getName
argument_list|()
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: '"
operator|+
name|name
operator|+
literal|"': not a git archive"
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|runImpl
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|runImpl ()
specifier|protected
specifier|abstract
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
function_decl|;
block|}
end_class

end_unit

