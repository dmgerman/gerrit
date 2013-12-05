begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|server
operator|.
name|project
operator|.
name|Util
operator|.
name|category
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
name|project
operator|.
name|Util
operator|.
name|value
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|DAYS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MINUTES
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|common
operator|.
name|collect
operator|.
name|Iterables
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
name|common
operator|.
name|data
operator|.
name|LabelTypes
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
name|Account
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
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
name|PatchSetApproval
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
name|PatchSetInfo
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
name|ChangeUtil
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
name|MetaDataUpdate
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
name|util
operator|.
name|TimeUtil
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
name|InMemoryRepositoryManager
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
name|ConfigInvalidException
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
name|dfs
operator|.
name|InMemoryRepository
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
name|PersonIdent
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTimeUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTimeUtils
operator|.
name|MillisProvider
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|TimeZone
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_class
DECL|class|ChangeNotesTest
specifier|public
class|class
name|ChangeNotesTest
block|{
DECL|field|TZ
specifier|private
specifier|static
specifier|final
name|TimeZone
name|TZ
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"America/Los_Angeles"
argument_list|)
decl_stmt|;
DECL|field|LABEL_TYPES
specifier|private
name|LabelTypes
name|LABEL_TYPES
init|=
operator|new
name|LabelTypes
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|category
argument_list|(
literal|"Verified"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Verified"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Fails"
argument_list|)
argument_list|)
argument_list|,
name|category
argument_list|(
literal|"Code-Review"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Looks Good To Me"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Do Not Submit"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|repoManager
specifier|private
name|InMemoryRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|repo
specifier|private
name|InMemoryRepository
name|repo
decl_stmt|;
DECL|field|clockStepMs
specifier|private
specifier|volatile
name|long
name|clockStepMs
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
name|project
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"test-project"
argument_list|)
expr_stmt|;
name|repoManager
operator|=
operator|new
name|InMemoryRepositoryManager
argument_list|()
expr_stmt|;
name|repo
operator|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setMillisProvider ()
specifier|public
name|void
name|setMillisProvider
parameter_list|()
block|{
name|clockStepMs
operator|=
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
specifier|final
name|AtomicLong
name|clockMs
init|=
operator|new
name|AtomicLong
argument_list|(
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|ChangeUtil
operator|.
name|SORT_KEY_EPOCH_MINS
argument_list|,
name|MINUTES
argument_list|)
operator|+
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|60
argument_list|,
name|DAYS
argument_list|)
argument_list|)
decl_stmt|;
name|DateTimeUtils
operator|.
name|setCurrentMillisProvider
argument_list|(
operator|new
name|MillisProvider
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|long
name|getMillis
parameter_list|()
block|{
return|return
name|clockMs
operator|.
name|getAndAdd
argument_list|(
name|clockStepMs
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|resetMillisProvider ()
specifier|public
name|void
name|resetMillisProvider
parameter_list|()
block|{
name|DateTimeUtils
operator|.
name|setCurrentMillisSystem
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|approvalsCommitFormat ()
specifier|public
name|void
name|approvalsCommitFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Verified"
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/changes/01/1/meta"
argument_list|,
name|update
operator|.
name|getRefName
argument_list|()
argument_list|)
expr_stmt|;
name|RevWalk
name|walk
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|RevCommit
name|commit
init|=
name|walk
operator|.
name|parseCommit
argument_list|(
name|update
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
name|walk
operator|.
name|parseBody
argument_list|(
name|commit
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Label: Verified=+1\n"
operator|+
literal|"Label: Code-Review=-1\n"
argument_list|,
name|commit
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
name|PersonIdent
name|author
init|=
name|commit
operator|.
name|getAuthorIdent
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Example User"
argument_list|,
name|author
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user@example.com"
argument_list|,
name|author
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
name|c
operator|.
name|getCreatedOn
argument_list|()
operator|.
name|getTime
argument_list|()
operator|+
literal|1000
argument_list|)
argument_list|,
name|author
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT-8:00"
argument_list|)
argument_list|,
name|author
operator|.
name|getTimeZone
argument_list|()
argument_list|)
expr_stmt|;
name|PersonIdent
name|committer
init|=
name|commit
operator|.
name|getCommitterIdent
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Example User"
argument_list|,
name|committer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5@gerrit"
argument_list|,
name|committer
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|author
operator|.
name|getWhen
argument_list|()
argument_list|,
name|committer
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|author
operator|.
name|getTimeZone
argument_list|()
argument_list|,
name|committer
operator|.
name|getTimeZone
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|walk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|approvalsOnePatchSet ()
specifier|public
name|void
name|approvalsOnePatchSet
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Verified"
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|ChangeNotes
name|notes
init|=
name|newNotes
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|notes
operator|.
name|getApprovals
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|psas
init|=
name|notes
operator|.
name|getApprovals
argument_list|()
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|psas
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Verified"
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|truncate
argument_list|(
name|after
argument_list|(
name|c
argument_list|,
literal|1000
argument_list|)
argument_list|)
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getGranted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Code-Review"
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getGranted
argument_list|()
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getGranted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|approvalsMultiplePatchSets ()
specifier|public
name|void
name|approvalsMultiplePatchSets
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|ps1
init|=
name|c
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|incrementPatchSet
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|update
operator|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|ps2
init|=
name|c
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|ChangeNotes
name|notes
init|=
name|newNotes
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|psas
init|=
name|notes
operator|.
name|getApprovals
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|notes
operator|.
name|getApprovals
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|PatchSetApproval
name|psa1
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|psas
operator|.
name|get
argument_list|(
name|ps1
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ps1
argument_list|,
name|psa1
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|psa1
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Code-Review"
argument_list|,
name|psa1
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
name|psa1
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|truncate
argument_list|(
name|after
argument_list|(
name|c
argument_list|,
literal|1000
argument_list|)
argument_list|)
argument_list|,
name|psa1
operator|.
name|getGranted
argument_list|()
argument_list|)
expr_stmt|;
name|PatchSetApproval
name|psa2
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|psas
operator|.
name|get
argument_list|(
name|ps2
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ps2
argument_list|,
name|psa2
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|psa2
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Code-Review"
argument_list|,
name|psa2
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
operator|+
literal|1
argument_list|,
name|psa2
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|truncate
argument_list|(
name|after
argument_list|(
name|c
argument_list|,
literal|2000
argument_list|)
argument_list|)
argument_list|,
name|psa2
operator|.
name|getGranted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|approvalsMultipleApprovals ()
specifier|public
name|void
name|approvalsMultipleApprovals
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|ChangeNotes
name|notes
init|=
name|newNotes
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|PatchSetApproval
name|psa
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|notes
operator|.
name|getApprovals
argument_list|()
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Code-Review"
argument_list|,
name|psa
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
name|psa
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|notes
operator|=
name|newNotes
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|psa
operator|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|notes
operator|.
name|getApprovals
argument_list|()
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Code-Review"
argument_list|,
name|psa
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
name|psa
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|approvalsMultipleUsers ()
specifier|public
name|void
name|approvalsMultipleUsers
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|update
operator|=
name|newUpdate
argument_list|(
name|c
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|ChangeNotes
name|notes
init|=
name|newNotes
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|notes
operator|.
name|getApprovals
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|psas
init|=
name|notes
operator|.
name|getApprovals
argument_list|()
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|psas
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Code-Review"
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|truncate
argument_list|(
name|after
argument_list|(
name|c
argument_list|,
literal|1000
argument_list|)
argument_list|)
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getGranted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAccountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Code-Review"
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|truncate
argument_list|(
name|after
argument_list|(
name|c
argument_list|,
literal|2000
argument_list|)
argument_list|)
argument_list|,
name|psas
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getGranted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|newChange (int accountId)
specifier|private
name|Change
name|newChange
parameter_list|(
name|int
name|accountId
parameter_list|)
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Change
name|c
init|=
operator|new
name|Change
argument_list|(
operator|new
name|Change
operator|.
name|Key
argument_list|(
literal|"Iabcd1234abcd1234abcd1234abcd1234abcd1234"
argument_list|)
argument_list|,
name|changeId
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|accountId
argument_list|)
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
literal|"master"
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|incrementPatchSet
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|newUpdate (Change c, Account.Id owner)
specifier|private
name|ChangeUpdate
name|newUpdate
parameter_list|(
name|Change
name|c
parameter_list|,
name|Account
operator|.
name|Id
name|owner
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
return|return
operator|new
name|ChangeUpdate
argument_list|(
name|repoManager
argument_list|,
name|LABEL_TYPES
argument_list|,
name|c
argument_list|,
name|owner
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|newNotes (Change c)
specifier|private
name|ChangeNotes
name|newNotes
parameter_list|(
name|Change
name|c
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
return|return
operator|new
name|ChangeNotes
argument_list|(
name|repo
argument_list|,
name|c
argument_list|)
return|;
block|}
DECL|method|incrementPatchSet (Change change)
specifier|private
specifier|static
name|void
name|incrementPatchSet
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
name|PatchSet
operator|.
name|Id
name|curr
init|=
name|change
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|PatchSetInfo
name|ps
init|=
operator|new
name|PatchSetInfo
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|curr
operator|!=
literal|null
condition|?
name|curr
operator|.
name|get
argument_list|()
operator|+
literal|1
else|:
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ps
operator|.
name|setSubject
argument_list|(
literal|"Change subject"
argument_list|)
expr_stmt|;
name|change
operator|.
name|setCurrentPatchSet
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
DECL|method|truncate (Timestamp ts)
specifier|private
specifier|static
name|Timestamp
name|truncate
parameter_list|(
name|Timestamp
name|ts
parameter_list|)
block|{
return|return
operator|new
name|Timestamp
argument_list|(
operator|(
name|ts
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
operator|)
operator|*
literal|1000
argument_list|)
return|;
block|}
DECL|method|after (Change c, long millis)
specifier|private
specifier|static
name|Timestamp
name|after
parameter_list|(
name|Change
name|c
parameter_list|,
name|long
name|millis
parameter_list|)
block|{
return|return
operator|new
name|Timestamp
argument_list|(
name|c
operator|.
name|getCreatedOn
argument_list|()
operator|.
name|getTime
argument_list|()
operator|+
name|millis
argument_list|)
return|;
block|}
DECL|method|commit (ChangeUpdate update)
specifier|private
name|RevCommit
name|commit
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|)
throws|throws
name|IOException
block|{
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|project
argument_list|,
name|repo
argument_list|)
decl_stmt|;
name|Timestamp
name|ts
init|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
decl_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
operator|new
name|PersonIdent
argument_list|(
literal|"Example User"
argument_list|,
literal|"user@example.com"
argument_list|,
name|ts
argument_list|,
name|TZ
argument_list|)
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
operator|new
name|PersonIdent
argument_list|(
literal|"Gerrit Test"
argument_list|,
literal|"notthis@email.com"
argument_list|,
name|ts
argument_list|,
name|TZ
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|update
operator|.
name|commit
argument_list|(
name|md
argument_list|)
return|;
block|}
block|}
end_class

end_unit

