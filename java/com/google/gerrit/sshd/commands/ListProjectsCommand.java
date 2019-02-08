begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|project
operator|.
name|ListProjects
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
name|gerrit
operator|.
name|util
operator|.
name|cli
operator|.
name|Options
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
literal|"ls-projects"
argument_list|,
name|description
operator|=
literal|"List projects visible to the caller"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|ListProjectsCommand
specifier|public
class|class
name|ListProjectsCommand
extends|extends
name|SshCommand
block|{
DECL|field|impl
annotation|@
name|Inject
annotation|@
name|Options
specifier|public
name|ListProjects
name|impl
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|impl
operator|.
name|getFormat
argument_list|()
operator|.
name|isJson
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|showBranch
init|=
name|impl
operator|.
name|getShowBranch
argument_list|()
decl_stmt|;
if|if
condition|(
name|impl
operator|.
name|isShowTree
argument_list|()
operator|&&
operator|(
name|showBranch
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|showBranch
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"--tree and --show-branch options are not compatible."
argument_list|)
throw|;
block|}
if|if
condition|(
name|impl
operator|.
name|isShowTree
argument_list|()
operator|&&
name|impl
operator|.
name|isShowDescription
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"--tree and --description options are not compatible."
argument_list|)
throw|;
block|}
block|}
name|impl
operator|.
name|displayToStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

