begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|FileRepository
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
name|Rule
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
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TemporaryFolder
import|;
end_import

begin_class
DECL|class|JGitConfigTest
specifier|public
class|class
name|JGitConfigTest
block|{
DECL|field|temporaryFolder
annotation|@
name|Rule
specifier|public
name|TemporaryFolder
name|temporaryFolder
init|=
operator|new
name|TemporaryFolder
argument_list|()
decl_stmt|;
DECL|field|site
specifier|private
name|SitePaths
name|site
decl_stmt|;
DECL|field|gitPath
specifier|private
name|Path
name|gitPath
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|site
operator|=
operator|new
name|SitePaths
argument_list|(
name|temporaryFolder
operator|.
name|newFolder
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|site
operator|.
name|etc_dir
argument_list|)
expr_stmt|;
name|gitPath
operator|=
name|Files
operator|.
name|createDirectories
argument_list|(
name|site
operator|.
name|resolve
argument_list|(
literal|"git"
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|site
operator|.
name|jgit_config
argument_list|,
literal|"[core]\n  trustFolderStat = false\n"
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|SystemReaderInstaller
argument_list|(
name|site
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|gitPath
operator|.
name|resolve
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|repo
operator|.
name|getConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"core"
argument_list|,
literal|null
argument_list|,
literal|"trustFolderStat"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
