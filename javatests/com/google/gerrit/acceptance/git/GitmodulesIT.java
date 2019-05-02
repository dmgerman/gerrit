begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
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
name|gerrit
operator|.
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|errors
operator|.
name|TransportException
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
name|Config
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
name|RefSpec
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
DECL|class|GitmodulesIT
specifier|public
class|class
name|GitmodulesIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|invalidSubmoduleURLIsRejected ()
specifier|public
name|void
name|invalidSubmoduleURLIsRejected
parameter_list|()
throws|throws
name|Exception
block|{
name|pushGitmodules
argument_list|(
literal|"name"
argument_list|,
literal|"-invalid-url"
argument_list|,
literal|"path"
argument_list|,
literal|"Invalid submodule URL"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidSubmodulePathIsRejected ()
specifier|public
name|void
name|invalidSubmodulePathIsRejected
parameter_list|()
throws|throws
name|Exception
block|{
name|pushGitmodules
argument_list|(
literal|"name"
argument_list|,
literal|"http://somewhere"
argument_list|,
literal|"-invalid-path"
argument_list|,
literal|"Invalid submodule path"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidSubmoduleNameIsRejected ()
specifier|public
name|void
name|invalidSubmoduleNameIsRejected
parameter_list|()
throws|throws
name|Exception
block|{
name|pushGitmodules
argument_list|(
literal|"-invalid-name"
argument_list|,
literal|"http://somewhere"
argument_list|,
literal|"path"
argument_list|,
literal|"Invalid submodule name"
argument_list|)
expr_stmt|;
block|}
DECL|method|pushGitmodules (String name, String url, String path, String expectedErrorMessage)
specifier|private
name|void
name|pushGitmodules
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|expectedErrorMessage
parameter_list|)
throws|throws
name|Exception
block|{
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|config
operator|.
name|setString
argument_list|(
literal|"submodule"
argument_list|,
name|name
argument_list|,
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|config
operator|.
name|setString
argument_list|(
literal|"submodule"
argument_list|,
name|name
argument_list|,
literal|"path"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|repo
init|=
name|cloneProject
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|repo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"subject: adding new subscription"
argument_list|)
operator|.
name|add
argument_list|(
literal|".gitmodules"
argument_list|,
name|config
operator|.
name|toText
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|TransportException
name|thrown
init|=
name|assertThrows
argument_list|(
name|TransportException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|repo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/for/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
name|expectedErrorMessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

