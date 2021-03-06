begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
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
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|StalenessChecker
operator|.
name|refsAreStale
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
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|ImmutableListMultimap
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
name|ImmutableSetMultimap
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
name|ListMultimap
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
name|Change
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
name|index
operator|.
name|RefState
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|StalenessChecker
operator|.
name|RefStatePattern
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
name|testing
operator|.
name|InMemoryRepositoryManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|ObjectId
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
name|Test
import|;
end_import

begin_class
DECL|class|StalenessCheckerTest
specifier|public
class|class
name|StalenessCheckerTest
block|{
DECL|field|SHA1
specifier|private
specifier|static
specifier|final
name|String
name|SHA1
init|=
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
decl_stmt|;
DECL|field|SHA2
specifier|private
specifier|static
specifier|final
name|String
name|SHA2
init|=
literal|"badc0feebadc0feebadc0feebadc0feebadc0fee"
decl_stmt|;
DECL|field|P1
specifier|private
specifier|static
specifier|final
name|Project
operator|.
name|NameKey
name|P1
init|=
name|Project
operator|.
name|nameKey
argument_list|(
literal|"project1"
argument_list|)
decl_stmt|;
DECL|field|P2
specifier|private
specifier|static
specifier|final
name|Project
operator|.
name|NameKey
name|P2
init|=
name|Project
operator|.
name|nameKey
argument_list|(
literal|"project2"
argument_list|)
decl_stmt|;
DECL|field|C
specifier|private
specifier|static
specifier|final
name|Change
operator|.
name|Id
name|C
init|=
name|Change
operator|.
name|id
argument_list|(
literal|1234
argument_list|)
decl_stmt|;
DECL|field|repoManager
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|r1
specifier|private
name|Repository
name|r1
decl_stmt|;
DECL|field|r2
specifier|private
name|Repository
name|r2
decl_stmt|;
DECL|field|tr1
specifier|private
name|TestRepository
argument_list|<
name|Repository
argument_list|>
name|tr1
decl_stmt|;
DECL|field|tr2
specifier|private
name|TestRepository
argument_list|<
name|Repository
argument_list|>
name|tr2
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
name|repoManager
operator|=
operator|new
name|InMemoryRepositoryManager
argument_list|()
expr_stmt|;
name|r1
operator|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|P1
argument_list|)
expr_stmt|;
name|tr1
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|r2
operator|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|P2
argument_list|)
expr_stmt|;
name|tr2
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|r2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseStates ()
specifier|public
name|void
name|parseStates
parameter_list|()
block|{
name|assertInvalidState
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertInvalidState
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertInvalidState
argument_list|(
literal|"project1:refs/heads/foo"
argument_list|)
expr_stmt|;
name|assertInvalidState
argument_list|(
literal|"project1:refs/heads/foo:notasha"
argument_list|)
expr_stmt|;
name|assertInvalidState
argument_list|(
literal|"project1:refs/heads/foo:"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RefState
operator|.
name|parseStates
argument_list|(
name|byteArrays
argument_list|(
name|P1
operator|+
literal|":refs/heads/foo:"
operator|+
name|SHA1
argument_list|,
name|P1
operator|+
literal|":refs/heads/bar:"
operator|+
name|SHA2
argument_list|,
name|P2
operator|+
literal|":refs/heads/baz:"
operator|+
name|SHA1
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
literal|"refs/heads/foo"
argument_list|,
name|SHA1
argument_list|)
argument_list|,
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
literal|"refs/heads/bar"
argument_list|,
name|SHA2
argument_list|)
argument_list|,
name|P2
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
literal|"refs/heads/baz"
argument_list|,
name|SHA1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertInvalidState (String state)
specifier|private
specifier|static
name|void
name|assertInvalidState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|assertThrows
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|RefState
operator|.
name|parseStates
argument_list|(
name|byteArrays
argument_list|(
name|state
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refStateToByteArray ()
specifier|public
name|void
name|refStateToByteArray
parameter_list|()
block|{
name|assertThat
argument_list|(
operator|new
name|String
argument_list|(
name|RefState
operator|.
name|create
argument_list|(
literal|"refs/heads/foo"
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
name|SHA1
argument_list|)
argument_list|)
operator|.
name|toByteArray
argument_list|(
name|P1
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|P1
operator|+
literal|":refs/heads/foo:"
operator|+
name|SHA1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|new
name|String
argument_list|(
name|RefState
operator|.
name|create
argument_list|(
literal|"refs/heads/foo"
argument_list|,
operator|(
name|ObjectId
operator|)
literal|null
argument_list|)
operator|.
name|toByteArray
argument_list|(
name|P1
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|P1
operator|+
literal|":refs/heads/foo:"
operator|+
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parsePatterns ()
specifier|public
name|void
name|parsePatterns
parameter_list|()
block|{
name|assertInvalidPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertInvalidPattern
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertInvalidPattern
argument_list|(
literal|"project:"
argument_list|)
expr_stmt|;
name|assertInvalidPattern
argument_list|(
literal|"project:refs/heads/foo"
argument_list|)
expr_stmt|;
name|assertInvalidPattern
argument_list|(
literal|"project:refs/he*ds/bar"
argument_list|)
expr_stmt|;
name|assertInvalidPattern
argument_list|(
literal|"project:refs/(he)*ds/bar"
argument_list|)
expr_stmt|;
name|assertInvalidPattern
argument_list|(
literal|"project:invalidrefname"
argument_list|)
expr_stmt|;
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefStatePattern
argument_list|>
name|r
init|=
name|StalenessChecker
operator|.
name|parsePatterns
argument_list|(
name|byteArrays
argument_list|(
name|P1
operator|+
literal|":refs/heads/*"
argument_list|,
name|P2
operator|+
literal|":refs/heads/foo/*/bar"
argument_list|,
name|P2
operator|+
literal|":refs/heads/foo/*-baz/*/quux"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|P1
argument_list|,
name|P2
argument_list|)
expr_stmt|;
name|RefStatePattern
name|p
init|=
name|r
operator|.
name|get
argument_list|(
name|P1
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|pattern
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|prefix
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|regex
argument_list|()
operator|.
name|pattern
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"^\\Qrefs/heads/\\E.*\\Q\\E$"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"xrefs/heads/foo"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/tags/foo"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|p
operator|=
name|r
operator|.
name|get
argument_list|(
name|P2
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|pattern
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/foo/*/bar"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|prefix
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/foo/"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|regex
argument_list|()
operator|.
name|pattern
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"^\\Qrefs/heads/foo/\\E.*\\Q/bar\\E$"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo//bar"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo/x/bar"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo/x/y/bar"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo/x/baz"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|p
operator|=
name|r
operator|.
name|get
argument_list|(
name|P2
argument_list|)
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|pattern
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/foo/*-baz/*/quux"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|prefix
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/foo/"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|regex
argument_list|()
operator|.
name|pattern
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"^\\Qrefs/heads/foo/\\E.*\\Q-baz/\\E.*\\Q/quux\\E$"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo/-baz//quux"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo/x-baz/x/quux"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo/x/y-baz/x/y/quux"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|match
argument_list|(
literal|"refs/heads/foo/x-baz/x/y"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refStatePatternToByteArray ()
specifier|public
name|void
name|refStatePatternToByteArray
parameter_list|()
block|{
name|assertThat
argument_list|(
operator|new
name|String
argument_list|(
name|RefStatePattern
operator|.
name|create
argument_list|(
literal|"refs/*"
argument_list|)
operator|.
name|toByteArray
argument_list|(
name|P1
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|P1
operator|+
literal|":refs/*"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertInvalidPattern (String state)
specifier|private
specifier|static
name|void
name|assertInvalidPattern
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|assertThrows
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|StalenessChecker
operator|.
name|parsePatterns
argument_list|(
name|byteArrays
argument_list|(
name|state
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isStaleRefStatesOnly ()
specifier|public
name|void
name|isStaleRefStatesOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ref1
init|=
literal|"refs/heads/foo"
decl_stmt|;
name|ObjectId
name|id1
init|=
name|tr1
operator|.
name|update
argument_list|(
name|ref1
argument_list|,
name|tr1
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 1"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|ref2
init|=
literal|"refs/heads/bar"
decl_stmt|;
name|ObjectId
name|id2
init|=
name|tr2
operator|.
name|update
argument_list|(
name|ref2
argument_list|,
name|tr2
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 2"
argument_list|)
argument_list|)
decl_stmt|;
comment|// Not stale.
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|P2
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref2
argument_list|,
name|id2
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// Wrong ref value.
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|SHA1
argument_list|)
argument_list|,
name|P2
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref2
argument_list|,
name|id2
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// Swapped repos.
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id2
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|P2
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref2
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// Two refs in same repo, not stale.
name|String
name|ref3
init|=
literal|"refs/heads/baz"
decl_stmt|;
name|ObjectId
name|id3
init|=
name|tr1
operator|.
name|update
argument_list|(
name|ref3
argument_list|,
name|tr1
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 3"
argument_list|)
argument_list|)
decl_stmt|;
name|tr1
operator|.
name|update
argument_list|(
name|ref3
argument_list|,
name|id3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref3
argument_list|,
name|id3
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// Ignore ref not mentioned.
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// One ref wrong.
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref3
argument_list|,
name|SHA1
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isStaleWithRefStatePatterns ()
specifier|public
name|void
name|isStaleWithRefStatePatterns
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ref1
init|=
literal|"refs/heads/foo"
decl_stmt|;
name|ObjectId
name|id1
init|=
name|tr1
operator|.
name|update
argument_list|(
name|ref1
argument_list|,
name|tr1
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 1"
argument_list|)
argument_list|)
decl_stmt|;
comment|// ref1 is only ref matching pattern.
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefStatePattern
operator|.
name|create
argument_list|(
literal|"refs/heads/*"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// Now ref2 matches pattern, so stale unless ref2 is present in state map.
name|String
name|ref2
init|=
literal|"refs/heads/bar"
decl_stmt|;
name|ObjectId
name|id2
init|=
name|tr1
operator|.
name|update
argument_list|(
name|ref2
argument_list|,
name|tr1
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 2"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefStatePattern
operator|.
name|create
argument_list|(
literal|"refs/heads/*"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref2
argument_list|,
name|id2
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefStatePattern
operator|.
name|create
argument_list|(
literal|"refs/heads/*"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isStaleWithNonPrefixPattern ()
specifier|public
name|void
name|isStaleWithNonPrefixPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ref1
init|=
literal|"refs/heads/foo"
decl_stmt|;
name|ObjectId
name|id1
init|=
name|tr1
operator|.
name|update
argument_list|(
name|ref1
argument_list|,
name|tr1
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 1"
argument_list|)
argument_list|)
decl_stmt|;
name|tr1
operator|.
name|update
argument_list|(
literal|"refs/heads/bar"
argument_list|,
name|tr1
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// ref1 is only ref matching pattern.
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefStatePattern
operator|.
name|create
argument_list|(
literal|"refs/*/foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// Now ref2 matches pattern, so stale unless ref2 is present in state map.
name|String
name|ref3
init|=
literal|"refs/other/foo"
decl_stmt|;
name|ObjectId
name|id3
init|=
name|tr1
operator|.
name|update
argument_list|(
name|ref3
argument_list|,
name|tr1
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"commit 3"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefStatePattern
operator|.
name|create
argument_list|(
literal|"refs/*/foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|C
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref1
argument_list|,
name|id1
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|P1
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|ref3
argument_list|,
name|id3
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|P1
argument_list|,
name|RefStatePattern
operator|.
name|create
argument_list|(
literal|"refs/*/foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isStale
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
DECL|method|byteArrays (String... strs)
specifier|private
specifier|static
name|Iterable
argument_list|<
name|byte
index|[]
argument_list|>
name|byteArrays
parameter_list|(
name|String
modifier|...
name|strs
parameter_list|)
block|{
return|return
name|Stream
operator|.
name|of
argument_list|(
name|strs
argument_list|)
operator|.
name|map
argument_list|(
name|s
lambda|->
name|s
operator|!=
literal|null
condition|?
name|s
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
else|:
literal|null
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

