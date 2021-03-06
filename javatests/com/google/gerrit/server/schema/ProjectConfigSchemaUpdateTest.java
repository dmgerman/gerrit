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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|ImmutableList
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
name|server
operator|.
name|config
operator|.
name|AllProjectsName
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|meta
operator|.
name|MetaDataUpdate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|lib
operator|.
name|StoredConfig
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
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
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
name|util
operator|.
name|FS
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
DECL|class|ProjectConfigSchemaUpdateTest
specifier|public
class|class
name|ProjectConfigSchemaUpdateTest
block|{
DECL|field|ALL_PROJECTS
specifier|private
specifier|static
specifier|final
name|String
name|ALL_PROJECTS
init|=
literal|"All-The-Projects"
decl_stmt|;
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
DECL|field|sitePaths
specifier|private
name|SitePaths
name|sitePaths
decl_stmt|;
DECL|field|factory
specifier|private
name|ProjectConfigSchemaUpdate
operator|.
name|Factory
name|factory
decl_stmt|;
DECL|field|allProjectsRepoFile
specifier|private
name|File
name|allProjectsRepoFile
decl_stmt|;
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
name|sitePaths
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
name|sitePaths
operator|.
name|etc_dir
argument_list|)
expr_stmt|;
name|Path
name|gitPath
init|=
name|sitePaths
operator|.
name|resolve
argument_list|(
literal|"git"
argument_list|)
decl_stmt|;
name|StoredConfig
name|gerritConfig
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|sitePaths
operator|.
name|gerrit_config
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
name|gerritConfig
operator|.
name|load
argument_list|()
expr_stmt|;
name|gerritConfig
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"basePath"
argument_list|,
name|gitPath
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|gerritConfig
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"allProjects"
argument_list|,
name|ALL_PROJECTS
argument_list|)
expr_stmt|;
name|gerritConfig
operator|.
name|save
argument_list|()
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|sitePaths
operator|.
name|resolve
argument_list|(
literal|"git"
argument_list|)
argument_list|)
expr_stmt|;
name|allProjectsRepoFile
operator|=
name|gitPath
operator|.
name|resolve
argument_list|(
literal|"All-The-Projects.git"
argument_list|)
operator|.
name|toFile
argument_list|()
expr_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|allProjectsRepoFile
argument_list|)
init|)
block|{
name|repo
operator|.
name|create
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|factory
operator|=
operator|new
name|ProjectConfigSchemaUpdate
operator|.
name|Factory
argument_list|(
name|sitePaths
argument_list|,
operator|new
name|AllProjectsName
argument_list|(
name|ALL_PROJECTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noBaseConfig ()
specifier|public
name|void
name|noBaseConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|getConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|allProjectsRepoFile
argument_list|)
init|;
name|TestRepository
argument_list|<
name|Repository
argument_list|>
name|tr
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|tr
operator|.
name|branch
argument_list|(
literal|"refs/meta/config"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|add
argument_list|(
literal|"project.config"
argument_list|,
literal|"[foo]\nbar = baz"
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|baseConfig ()
specifier|public
name|void
name|baseConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|getConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|Path
name|baseConfigPath
init|=
name|sitePaths
operator|.
name|etc_dir
operator|.
name|resolve
argument_list|(
name|ALL_PROJECTS
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"project.config"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|baseConfigPath
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|baseConfigPath
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"[foo]"
argument_list|,
literal|"bar = base"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"base"
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|allProjectsRepoFile
argument_list|)
init|;
name|TestRepository
argument_list|<
name|Repository
argument_list|>
name|tr
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|tr
operator|.
name|branch
argument_list|(
literal|"refs/meta/config"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|add
argument_list|(
literal|"project.config"
argument_list|,
literal|"[foo]\nbar = baz"
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|private
name|Config
name|getConfig
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|allProjectsRepoFile
argument_list|)
init|)
block|{
return|return
name|factory
operator|.
name|read
argument_list|(
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|Project
operator|.
name|nameKey
argument_list|(
name|ALL_PROJECTS
argument_list|)
argument_list|,
name|repo
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|getConfig
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

