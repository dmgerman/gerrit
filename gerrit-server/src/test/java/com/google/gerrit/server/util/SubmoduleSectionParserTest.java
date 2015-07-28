begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createStrictMock
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
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|Branch
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
name|reviewdb
operator|.
name|client
operator|.
name|SubmoduleSubscription
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
name|GitRepositoryManager
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
name|LocalDiskRepositoryTestCase
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
name|BlobBasedConfig
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
name|net
operator|.
name|URI
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_class
DECL|class|SubmoduleSectionParserTest
specifier|public
class|class
name|SubmoduleSectionParserTest
extends|extends
name|LocalDiskRepositoryTestCase
block|{
DECL|field|THIS_SERVER
specifier|private
specifier|static
specifier|final
name|String
name|THIS_SERVER
init|=
literal|"localhost"
decl_stmt|;
DECL|field|repoManager
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|bbc
specifier|private
name|BlobBasedConfig
name|bbc
decl_stmt|;
annotation|@
name|Override
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|repoManager
operator|=
name|createStrictMock
argument_list|(
name|GitRepositoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|bbc
operator|=
name|createStrictMock
argument_list|(
name|BlobBasedConfig
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|doReplay ()
specifier|private
name|void
name|doReplay
parameter_list|()
block|{
name|replay
argument_list|(
name|repoManager
argument_list|,
name|bbc
argument_list|)
expr_stmt|;
block|}
DECL|method|doVerify ()
specifier|private
name|void
name|doVerify
parameter_list|()
block|{
name|verify
argument_list|(
name|repoManager
argument_list|,
name|bbc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmodulesParseWithCorrectSections ()
specifier|public
name|void
name|testSubmodulesParseWithCorrectSections
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|sectionsToReturn
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/a"
argument_list|,
literal|"a"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/b"
argument_list|,
literal|"b"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/test/c"
argument_list|,
literal|"c-path"
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|)
expr_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"d"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/d"
argument_list|,
literal|"d-parent/the-d-folder"
argument_list|,
literal|"refs/heads/test"
argument_list|)
argument_list|)
expr_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"e"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/e.git"
argument_list|,
literal|"e"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reposToBeFound
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|"test/c"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"d"
argument_list|,
literal|"d"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"e"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
specifier|final
name|Branch
operator|.
name|NameKey
name|superBranchNameKey
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"super-project"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|expectedSubscriptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"b"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"test/c"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"c-path"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"d"
argument_list|)
argument_list|,
literal|"refs/heads/test"
argument_list|)
argument_list|,
literal|"d-parent/the-d-folder"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"e"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"e"
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|superBranchNameKey
argument_list|,
name|sectionsToReturn
argument_list|,
name|reposToBeFound
argument_list|,
name|expectedSubscriptions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmodulesParseWithAnInvalidSection ()
specifier|public
name|void
name|testSubmodulesParseWithAnInvalidSection
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|sectionsToReturn
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/a"
argument_list|,
literal|"a"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
comment|// This one is invalid since "b" is not a recognized project
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/b"
argument_list|,
literal|"b"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/test/c"
argument_list|,
literal|"c-path"
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|)
expr_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"d"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/d"
argument_list|,
literal|"d-parent/the-d-folder"
argument_list|,
literal|"refs/heads/test"
argument_list|)
argument_list|)
expr_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"e"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/e.git"
argument_list|,
literal|"e"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
comment|// "b" will not be in this list
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reposToBeFound
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|"test/c"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"d"
argument_list|,
literal|"d"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"e"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
specifier|final
name|Branch
operator|.
name|NameKey
name|superBranchNameKey
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"super-project"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|expectedSubscriptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"test/c"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"c-path"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"d"
argument_list|)
argument_list|,
literal|"refs/heads/test"
argument_list|)
argument_list|,
literal|"d-parent/the-d-folder"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"e"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"e"
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|superBranchNameKey
argument_list|,
name|sectionsToReturn
argument_list|,
name|reposToBeFound
argument_list|,
name|expectedSubscriptions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmoduleSectionToOtherServer ()
specifier|public
name|void
name|testSubmoduleSectionToOtherServer
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|sectionsToReturn
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// The url is not to this server.
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://review.source.com/a"
argument_list|,
literal|"a"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"super-project"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
name|sectionsToReturn
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|SubmoduleSubscription
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProjectNotFound ()
specifier|public
name|void
name|testProjectNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|sectionsToReturn
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/a"
argument_list|,
literal|"a"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"super-project"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
name|sectionsToReturn
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|SubmoduleSubscription
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProjectWithSlashesNotFound ()
specifier|public
name|void
name|testProjectWithSlashesNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|sectionsToReturn
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"project"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/company/tools/project"
argument_list|,
literal|"project"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"super-project"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
name|sectionsToReturn
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|SubmoduleSubscription
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmodulesParseWithSubProjectFound ()
specifier|public
name|void
name|testSubmodulesParseWithSubProjectFound
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|sectionsToReturn
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
name|sectionsToReturn
operator|.
name|put
argument_list|(
literal|"a/b"
argument_list|,
operator|new
name|SubmoduleSection
argument_list|(
literal|"ssh://localhost/a/b"
argument_list|,
literal|"a/b"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reposToBeFound
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"a/b"
argument_list|,
literal|"a/b"
argument_list|)
expr_stmt|;
name|reposToBeFound
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|Branch
operator|.
name|NameKey
name|superBranchNameKey
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"super-project"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|expectedSubscriptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedSubscriptions
operator|.
name|add
argument_list|(
operator|new
name|SubmoduleSubscription
argument_list|(
name|superBranchNameKey
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"a/b"
argument_list|)
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|,
literal|"a/b"
argument_list|)
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|superBranchNameKey
argument_list|,
name|sectionsToReturn
argument_list|,
name|reposToBeFound
argument_list|,
name|expectedSubscriptions
argument_list|)
expr_stmt|;
block|}
DECL|method|execute (final Branch.NameKey superProjectBranch, final Map<String, SubmoduleSection> sectionsToReturn, final Map<String, String> reposToBeFound, final List<SubmoduleSubscription> expectedSubscriptions)
specifier|private
name|void
name|execute
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|superProjectBranch
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|sectionsToReturn
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reposToBeFound
parameter_list|,
specifier|final
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|expectedSubscriptions
parameter_list|)
throws|throws
name|Exception
block|{
name|expect
argument_list|(
name|bbc
operator|.
name|getSubsections
argument_list|(
literal|"submodule"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|sectionsToReturn
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|SubmoduleSection
argument_list|>
name|entry
range|:
name|sectionsToReturn
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|id
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|SubmoduleSection
name|section
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|bbc
operator|.
name|getString
argument_list|(
literal|"submodule"
argument_list|,
name|id
argument_list|,
literal|"url"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|section
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|bbc
operator|.
name|getString
argument_list|(
literal|"submodule"
argument_list|,
name|id
argument_list|,
literal|"path"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|section
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|bbc
operator|.
name|getString
argument_list|(
literal|"submodule"
argument_list|,
name|id
argument_list|,
literal|"branch"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|section
operator|.
name|getBranch
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|THIS_SERVER
operator|.
name|equals
argument_list|(
operator|new
name|URI
argument_list|(
name|section
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|projectNameCandidate
decl_stmt|;
specifier|final
name|String
name|urlExtractedPath
init|=
operator|new
name|URI
argument_list|(
name|section
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|int
name|fromIndex
init|=
name|urlExtractedPath
operator|.
name|length
argument_list|()
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|fromIndex
operator|>
literal|0
condition|)
block|{
name|fromIndex
operator|=
name|urlExtractedPath
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|,
name|fromIndex
operator|-
literal|1
argument_list|)
expr_stmt|;
name|projectNameCandidate
operator|=
name|urlExtractedPath
operator|.
name|substring
argument_list|(
name|fromIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectNameCandidate
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
condition|)
block|{
name|projectNameCandidate
operator|=
name|projectNameCandidate
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
comment|//
name|projectNameCandidate
operator|.
name|length
argument_list|()
operator|-
name|Constants
operator|.
name|DOT_GIT_EXT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reposToBeFound
operator|.
name|containsValue
argument_list|(
name|projectNameCandidate
argument_list|)
condition|)
block|{
name|expect
argument_list|(
name|repoManager
operator|.
name|list
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectNameCandidate
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|expect
argument_list|(
name|repoManager
operator|.
name|list
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|Collections
operator|.
expr|<
name|Project
operator|.
name|NameKey
operator|>
name|emptyList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|doReplay
argument_list|()
expr_stmt|;
specifier|final
name|SubmoduleSectionParser
name|ssp
init|=
operator|new
name|SubmoduleSectionParser
argument_list|(
name|bbc
argument_list|,
name|THIS_SERVER
argument_list|,
name|superProjectBranch
argument_list|,
name|repoManager
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|returnedSubscriptions
init|=
name|ssp
operator|.
name|parseAllSections
argument_list|()
decl_stmt|;
name|doVerify
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedSubscriptions
argument_list|,
name|returnedSubscriptions
argument_list|)
expr_stmt|;
block|}
DECL|class|SubmoduleSection
specifier|private
specifier|static
specifier|final
class|class
name|SubmoduleSection
block|{
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|branch
specifier|private
specifier|final
name|String
name|branch
decl_stmt|;
DECL|method|SubmoduleSection (final String url, final String path, final String branch)
specifier|public
name|SubmoduleSection
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|String
name|branch
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|getBranch ()
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|branch
return|;
block|}
block|}
block|}
end_class

end_unit

