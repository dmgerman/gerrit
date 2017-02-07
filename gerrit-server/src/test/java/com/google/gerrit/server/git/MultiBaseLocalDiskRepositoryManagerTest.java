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
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createNiceMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|reset
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
name|RepositoryConfig
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
name|testutil
operator|.
name|GerritBaseTests
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

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
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
name|After
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
DECL|class|MultiBaseLocalDiskRepositoryManagerTest
specifier|public
class|class
name|MultiBaseLocalDiskRepositoryManagerTest
extends|extends
name|GerritBaseTests
block|{
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
name|MultiBaseLocalDiskRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|configMock
specifier|private
name|RepositoryConfig
name|configMock
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
name|configMock
operator|=
name|createNiceMock
argument_list|(
name|RepositoryConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configMock
operator|.
name|getAllBasePaths
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Path
argument_list|>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|configMock
argument_list|)
expr_stmt|;
name|repoManager
operator|=
operator|new
name|MultiBaseLocalDiskRepositoryManager
argument_list|(
name|site
argument_list|,
name|cfg
argument_list|,
name|configMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|TempFileUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|defaultRepositoryLocation ()
specifier|public
name|void
name|defaultRepositoryLocation
parameter_list|()
throws|throws
name|RepositoryCaseMismatchException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
name|Project
operator|.
name|NameKey
name|someProjectKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"someProject"
argument_list|)
decl_stmt|;
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|someProjectKey
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|(
name|someProjectKey
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|someProjectKey
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|(
name|someProjectKey
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|(
name|someProjectKey
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|(
name|someProjectKey
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|repoList
init|=
name|repoManager
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|repoList
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repoList
operator|.
name|toArray
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
index|[
name|repoList
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
index|[]
block|{
name|someProjectKey
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|alternateRepositoryLocation ()
specifier|public
name|void
name|alternateRepositoryLocation
parameter_list|()
throws|throws
name|IOException
block|{
name|Path
name|alternateBasePath
init|=
name|TempFileUtil
operator|.
name|createTempDirectory
argument_list|()
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|someProjectKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"someProject"
argument_list|)
decl_stmt|;
name|reset
argument_list|(
name|configMock
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configMock
operator|.
name|getBasePath
argument_list|(
name|someProjectKey
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|alternateBasePath
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|configMock
operator|.
name|getAllBasePaths
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|alternateBasePath
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|configMock
argument_list|)
expr_stmt|;
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|someProjectKey
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|alternateBasePath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|someProjectKey
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|alternateBasePath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|(
name|someProjectKey
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|alternateBasePath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|repoList
init|=
name|repoManager
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|repoList
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repoList
operator|.
name|toArray
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
index|[
name|repoList
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
index|[]
block|{
name|someProjectKey
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listReturnRepoFromProperLocation ()
specifier|public
name|void
name|listReturnRepoFromProperLocation
parameter_list|()
throws|throws
name|IOException
block|{
name|Project
operator|.
name|NameKey
name|basePathProject
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"basePathProject"
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|altPathProject
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"altPathProject"
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|misplacedProject1
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"misplacedProject1"
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|misplacedProject2
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"misplacedProject2"
argument_list|)
decl_stmt|;
name|Path
name|alternateBasePath
init|=
name|TempFileUtil
operator|.
name|createTempDirectory
argument_list|()
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|reset
argument_list|(
name|configMock
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configMock
operator|.
name|getBasePath
argument_list|(
name|altPathProject
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|alternateBasePath
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|configMock
operator|.
name|getBasePath
argument_list|(
name|misplacedProject2
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|alternateBasePath
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|configMock
operator|.
name|getAllBasePaths
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|alternateBasePath
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|configMock
argument_list|)
expr_stmt|;
name|repoManager
operator|.
name|createRepository
argument_list|(
name|basePathProject
argument_list|)
expr_stmt|;
name|repoManager
operator|.
name|createRepository
argument_list|(
name|altPathProject
argument_list|)
expr_stmt|;
comment|// create the misplaced ones without the repomanager otherwise they would
comment|// end up at the proper place.
name|createRepository
argument_list|(
name|repoManager
operator|.
name|getBasePath
argument_list|(
name|basePathProject
argument_list|)
argument_list|,
name|misplacedProject2
argument_list|)
expr_stmt|;
name|createRepository
argument_list|(
name|alternateBasePath
argument_list|,
name|misplacedProject1
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|repoList
init|=
name|repoManager
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|repoList
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repoList
operator|.
name|toArray
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
index|[
name|repoList
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
index|[]
block|{
name|altPathProject
block|,
name|basePathProject
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createRepository (Path directory, Project.NameKey projectName)
specifier|private
name|void
name|createRepository
parameter_list|(
name|Path
name|directory
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|n
init|=
name|projectName
operator|.
name|get
argument_list|()
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
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|testRelativeAlternateLocation ()
specifier|public
name|void
name|testRelativeAlternateLocation
parameter_list|()
block|{
name|configMock
operator|=
name|createNiceMock
argument_list|(
name|RepositoryConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configMock
operator|.
name|getAllBasePaths
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"repos"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|configMock
argument_list|)
expr_stmt|;
name|repoManager
operator|=
operator|new
name|MultiBaseLocalDiskRepositoryManager
argument_list|(
name|site
argument_list|,
name|cfg
argument_list|,
name|configMock
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

