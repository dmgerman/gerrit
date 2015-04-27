begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|reviewdb
operator|.
name|client
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
name|notedb
operator|.
name|NotesMigration
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
name|testutil
operator|.
name|TempFileUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|KeyUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|StandardKeyEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMockSupport
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
name|Constants
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
name|RepositoryCache
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
name|RepositoryCache
operator|.
name|FileKey
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
name|Test
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
name|file
operator|.
name|Path
import|;
end_import

begin_class
DECL|class|LocalDiskRepositoryManagerTest
specifier|public
class|class
name|LocalDiskRepositoryManagerTest
extends|extends
name|EasyMockSupport
block|{
static|static
block|{
name|KeyUtil
operator|.
name|setEncoderImpl
argument_list|(
operator|new
name|StandardKeyEncoder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|cfg
specifier|private
name|Config
name|cfg
decl_stmt|;
DECL|field|site
specifier|private
name|SitePaths
name|site
decl_stmt|;
DECL|field|repoManager
specifier|private
name|LocalDiskRepositoryManager
name|repoManager
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
name|site
operator|=
operator|new
name|SitePaths
argument_list|(
name|TempFileUtil
operator|.
name|createTempDirectory
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|site
operator|.
name|resolve
argument_list|(
literal|"git"
argument_list|)
operator|.
name|toFile
argument_list|()
operator|.
name|mkdir
argument_list|()
expr_stmt|;
name|cfg
operator|=
operator|new
name|Config
argument_list|()
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"basePath"
argument_list|,
literal|"git"
argument_list|)
expr_stmt|;
name|repoManager
operator|=
operator|new
name|LocalDiskRepositoryManager
argument_list|(
name|site
argument_list|,
name|cfg
argument_list|,
name|createNiceMock
argument_list|(
name|NotesMigration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|testThatNullBasePathThrowsAnException ()
specifier|public
name|void
name|testThatNullBasePathThrowsAnException
parameter_list|()
block|{
operator|new
name|LocalDiskRepositoryManager
argument_list|(
name|site
argument_list|,
operator|new
name|Config
argument_list|()
argument_list|,
name|createNiceMock
argument_list|(
name|NotesMigration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProjectCreation ()
specifier|public
name|void
name|testProjectCreation
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
operator|.
name|NameKey
name|projectA
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"projectA"
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|projectA
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|repo
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectA
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|repo
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|repoManager
operator|.
name|list
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|projectA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithEmptyName ()
specifier|public
name|void
name|testProjectCreationWithEmptyName
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithTrailingSlash ()
specifier|public
name|void
name|testProjectCreationWithTrailingSlash
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"projectA/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithBackSlash ()
specifier|public
name|void
name|testProjectCreationWithBackSlash
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a\\projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationAbsolutePath ()
specifier|public
name|void
name|testProjectCreationAbsolutePath
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"/projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationStartingWithDotDot ()
specifier|public
name|void
name|testProjectCreationStartingWithDotDot
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"../projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationContainsDotDot ()
specifier|public
name|void
name|testProjectCreationContainsDotDot
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a/../projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationDotPathSegment ()
specifier|public
name|void
name|testProjectCreationDotPathSegment
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a/./projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithTwoSlashes ()
specifier|public
name|void
name|testProjectCreationWithTwoSlashes
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a//projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithPathSegmentEndingByDotGit ()
specifier|public
name|void
name|testProjectCreationWithPathSegmentEndingByDotGit
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a/b.git/projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithQuestionMark ()
specifier|public
name|void
name|testProjectCreationWithQuestionMark
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project?A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithPercentageSign ()
specifier|public
name|void
name|testProjectCreationWithPercentageSign
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project%A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithWidlcard ()
specifier|public
name|void
name|testProjectCreationWithWidlcard
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project*A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithColon ()
specifier|public
name|void
name|testProjectCreationWithColon
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project:A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithLessThatSign ()
specifier|public
name|void
name|testProjectCreationWithLessThatSign
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project<A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithGreaterThatSign ()
specifier|public
name|void
name|testProjectCreationWithGreaterThatSign
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project>A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithPipe ()
specifier|public
name|void
name|testProjectCreationWithPipe
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project|A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithDollarSign ()
specifier|public
name|void
name|testProjectCreationWithDollarSign
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project$A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testProjectCreationWithCarriageReturn ()
specifier|public
name|void
name|testProjectCreationWithCarriageReturn
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|createRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project\\rA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOpenRepositoryCreatedDirectlyOnDisk ()
specifier|public
name|void
name|testOpenRepositoryCreatedDirectlyOnDisk
parameter_list|()
throws|throws
name|Exception
block|{
name|createRepository
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|()
argument_list|,
literal|"projectA"
argument_list|)
expr_stmt|;
name|Project
operator|.
name|NameKey
name|projectA
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"projectA"
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectA
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|repo
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|repoManager
operator|.
name|list
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|projectA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testOpenRepositoryInvalidName ()
specifier|public
name|void
name|testOpenRepositoryInvalidName
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|openRepository
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project%?|<>A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testList ()
specifier|public
name|void
name|testList
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
operator|.
name|NameKey
name|projectA
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"projectA"
argument_list|)
decl_stmt|;
name|createRepository
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|()
argument_list|,
name|projectA
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Project
operator|.
name|NameKey
name|projectB
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"path/projectB"
argument_list|)
decl_stmt|;
name|createRepository
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|()
argument_list|,
name|projectB
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Project
operator|.
name|NameKey
name|projectC
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"anotherPath/path/projectC"
argument_list|)
decl_stmt|;
name|createRepository
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|()
argument_list|,
name|projectC
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// create an invalid git repo named only .git
name|repoManager
operator|.
name|getBasePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|".git"
argument_list|)
operator|.
name|toFile
argument_list|()
operator|.
name|mkdir
argument_list|()
expr_stmt|;
comment|// create an invalid repo name
name|createRepository
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|()
argument_list|,
literal|"project?A"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|repoManager
operator|.
name|list
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|projectA
argument_list|,
name|projectB
argument_list|,
name|projectC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetSetProjectDescription ()
specifier|public
name|void
name|testGetSetProjectDescription
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
operator|.
name|NameKey
name|projectA
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"projectA"
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|projectA
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|repo
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|repoManager
operator|.
name|getProjectDescription
argument_list|(
name|projectA
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|repoManager
operator|.
name|setProjectDescription
argument_list|(
name|projectA
argument_list|,
literal|"projectA description"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repoManager
operator|.
name|getProjectDescription
argument_list|(
name|projectA
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"projectA description"
argument_list|)
expr_stmt|;
name|repoManager
operator|.
name|setProjectDescription
argument_list|(
name|projectA
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repoManager
operator|.
name|getProjectDescription
argument_list|(
name|projectA
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryNotFoundException
operator|.
name|class
argument_list|)
DECL|method|testGetProjectDescriptionFromUnexistingRepository ()
specifier|public
name|void
name|testGetProjectDescriptionFromUnexistingRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|repoManager
operator|.
name|getProjectDescription
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"projectA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createRepository (Path directory, String projectName)
specifier|private
name|void
name|createRepository
parameter_list|(
name|Path
name|directory
parameter_list|,
name|String
name|projectName
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|n
init|=
name|projectName
operator|+
name|Constants
operator|.
name|DOT_GIT_EXT
decl_stmt|;
name|FileKey
name|loc
init|=
name|FileKey
operator|.
name|exact
argument_list|(
name|directory
operator|.
name|resolve
argument_list|(
name|n
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|db
init|=
name|RepositoryCache
operator|.
name|open
argument_list|(
name|loc
argument_list|,
literal|false
argument_list|)
init|)
block|{
name|db
operator|.
name|create
argument_list|(
literal|true
comment|/* bare */
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

