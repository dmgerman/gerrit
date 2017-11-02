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
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

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
name|common
operator|.
name|truth
operator|.
name|Truth8
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
name|common
operator|.
name|TimeUtil
operator|.
name|nowTs
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
name|notedb
operator|.
name|NoteDbChangeState
operator|.
name|PrimaryStorage
operator|.
name|NOTE_DB
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
name|notedb
operator|.
name|NoteDbChangeState
operator|.
name|PrimaryStorage
operator|.
name|REVIEW_DB
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
name|notedb
operator|.
name|NoteDbChangeState
operator|.
name|applyDelta
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
name|notedb
operator|.
name|NoteDbChangeState
operator|.
name|parse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
operator|.
name|zeroId
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
name|ImmutableMap
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
name|notedb
operator|.
name|NoteDbChangeState
operator|.
name|Delta
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
name|NoteDbChangeState
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
name|testing
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
name|testing
operator|.
name|TestChanges
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
name|TestTimeUtil
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
name|Optional
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
name|TimeUnit
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

begin_comment
comment|/** Unit tests for {@link NoteDbChangeState}. */
end_comment

begin_class
DECL|class|NoteDbChangeStateTest
specifier|public
class|class
name|NoteDbChangeStateTest
extends|extends
name|GerritBaseTests
block|{
DECL|field|SHA1
name|ObjectId
name|SHA1
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
decl_stmt|;
DECL|field|SHA2
name|ObjectId
name|SHA2
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"abcd1234abcd1234abcd1234abcd1234abcd1234"
argument_list|)
decl_stmt|;
DECL|field|SHA3
name|ObjectId
name|SHA3
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"badc0feebadc0feebadc0feebadc0feebadc0fee"
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
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
block|{
name|TestTimeUtil
operator|.
name|useSystemTime
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseReviewDbWithoutDrafts ()
specifier|public
name|void
name|parseReviewDbWithoutDrafts
parameter_list|()
block|{
name|NoteDbChangeState
name|state
init|=
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
name|SHA1
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeMetaId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getDraftIds
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|state
operator|=
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"R,"
operator|+
name|SHA1
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeMetaId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getDraftIds
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseReviewDbWithDrafts ()
specifier|public
name|void
name|parseReviewDbWithDrafts
parameter_list|()
block|{
name|String
name|str
init|=
name|SHA1
operator|.
name|name
argument_list|()
operator|+
literal|",2003="
operator|+
name|SHA2
operator|.
name|name
argument_list|()
operator|+
literal|",1001="
operator|+
name|SHA3
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|expected
init|=
name|SHA1
operator|.
name|name
argument_list|()
operator|+
literal|",1001="
operator|+
name|SHA3
operator|.
name|name
argument_list|()
operator|+
literal|",2003="
operator|+
name|SHA2
operator|.
name|name
argument_list|()
decl_stmt|;
name|NoteDbChangeState
name|state
init|=
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
name|str
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeMetaId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getDraftIds
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1001
argument_list|)
argument_list|,
name|SHA3
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|2003
argument_list|)
argument_list|,
name|SHA2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|state
operator|=
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"R,"
operator|+
name|str
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeMetaId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getDraftIds
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1001
argument_list|)
argument_list|,
name|SHA3
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|2003
argument_list|)
argument_list|,
name|SHA2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseReadOnlyUntil ()
specifier|public
name|void
name|parseReadOnlyUntil
parameter_list|()
block|{
name|Timestamp
name|ts
init|=
operator|new
name|Timestamp
argument_list|(
literal|12345
argument_list|)
decl_stmt|;
name|String
name|str
init|=
literal|"R=12345,"
operator|+
name|SHA1
operator|.
name|name
argument_list|()
decl_stmt|;
name|NoteDbChangeState
name|state
init|=
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
name|str
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|REVIEW_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeMetaId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ts
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|str
operator|=
literal|"N=12345"
expr_stmt|;
name|state
operator|=
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
name|str
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|NOTE_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getRefState
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ts
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|applyDeltaToNullWithNoNewMetaId ()
specifier|public
name|void
name|applyDeltaToNullWithNoNewMetaId
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|noMetaId
argument_list|()
argument_list|,
name|noDrafts
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|noMetaId
argument_list|()
argument_list|,
name|drafts
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1001
argument_list|)
argument_list|,
name|zeroId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|applyDeltaToMetaId ()
specifier|public
name|void
name|applyDeltaToMetaId
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|metaId
argument_list|(
name|SHA1
argument_list|)
argument_list|,
name|noDrafts
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|metaId
argument_list|(
name|SHA2
argument_list|)
argument_list|,
name|noDrafts
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA2
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// No-op delta.
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|noMetaId
argument_list|()
argument_list|,
name|noDrafts
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA2
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set to zero clears the field.
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|metaId
argument_list|(
name|zeroId
argument_list|()
argument_list|)
argument_list|,
name|noDrafts
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|applyDeltaToDrafts ()
specifier|public
name|void
name|applyDeltaToDrafts
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|metaId
argument_list|(
name|SHA1
argument_list|)
argument_list|,
name|drafts
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1001
argument_list|)
argument_list|,
name|SHA2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
operator|.
name|name
argument_list|()
operator|+
literal|",1001="
operator|+
name|SHA2
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|noMetaId
argument_list|()
argument_list|,
name|drafts
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|2003
argument_list|)
argument_list|,
name|SHA3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
operator|.
name|name
argument_list|()
operator|+
literal|",1001="
operator|+
name|SHA2
operator|.
name|name
argument_list|()
operator|+
literal|",2003="
operator|+
name|SHA3
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|noMetaId
argument_list|()
argument_list|,
name|drafts
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|2003
argument_list|)
argument_list|,
name|zeroId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA1
operator|.
name|name
argument_list|()
operator|+
literal|",1001="
operator|+
name|SHA2
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|metaId
argument_list|(
name|SHA3
argument_list|)
argument_list|,
name|noDrafts
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SHA3
operator|.
name|name
argument_list|()
operator|+
literal|",1001="
operator|+
name|SHA2
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|applyDeltaToReadOnly ()
specifier|public
name|void
name|applyDeltaToReadOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|Timestamp
name|ts
init|=
name|nowTs
argument_list|()
decl_stmt|;
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|NoteDbChangeState
name|state
init|=
operator|new
name|NoteDbChangeState
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|REVIEW_DB
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|RefState
operator|.
name|create
argument_list|(
name|SHA1
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|ts
operator|.
name|getTime
argument_list|()
operator|+
literal|10000
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|c
operator|.
name|setNoteDbState
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Delta
name|delta
init|=
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|metaId
argument_list|(
name|SHA2
argument_list|)
argument_list|,
name|noDrafts
argument_list|()
argument_list|)
decl_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|delta
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|NoteDbChangeState
operator|.
name|parse
argument_list|(
name|c
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|NoteDbChangeState
argument_list|(
name|state
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|RefState
operator|.
name|create
argument_list|(
name|SHA2
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseNoteDbPrimary ()
specifier|public
name|void
name|parseNoteDbPrimary
parameter_list|()
block|{
name|NoteDbChangeState
name|state
init|=
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"N"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|NOTE_DB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getRefState
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|getReadOnlyUntil
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|parseInvalidPrimaryStorage ()
specifier|public
name|void
name|parseInvalidPrimaryStorage
parameter_list|()
block|{
name|parse
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|applyDeltaToNoteDbPrimaryIsNoOp ()
specifier|public
name|void
name|applyDeltaToNoteDbPrimaryIsNoOp
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|c
operator|.
name|setNoteDbState
argument_list|(
literal|"N"
argument_list|)
expr_stmt|;
name|applyDelta
argument_list|(
name|c
argument_list|,
name|Delta
operator|.
name|create
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|metaId
argument_list|(
name|SHA1
argument_list|)
argument_list|,
name|drafts
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1001
argument_list|)
argument_list|,
name|SHA2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"N"
argument_list|)
expr_stmt|;
block|}
DECL|method|newChange ()
specifier|private
specifier|static
name|Change
name|newChange
parameter_list|()
block|{
return|return
name|TestChanges
operator|.
name|newChange
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project"
argument_list|)
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|12345
argument_list|)
argument_list|)
return|;
block|}
comment|// Static factory methods to avoid type arguments when using as method args.
DECL|method|noMetaId ()
specifier|private
specifier|static
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|noMetaId
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|metaId (ObjectId id)
specifier|private
specifier|static
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|metaId
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|noDrafts ()
specifier|private
specifier|static
name|ImmutableMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|noDrafts
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|drafts (Object... args)
specifier|private
specifier|static
name|ImmutableMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|drafts
parameter_list|(
name|Object
modifier|...
name|args
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|args
operator|.
name|length
operator|%
literal|2
operator|==
literal|0
argument_list|)
expr_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|b
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
operator|/
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|b
operator|.
name|put
argument_list|(
operator|(
name|Account
operator|.
name|Id
operator|)
name|args
index|[
literal|2
operator|*
name|i
index|]
argument_list|,
operator|(
name|ObjectId
operator|)
name|args
index|[
literal|2
operator|*
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit
