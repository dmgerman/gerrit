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
DECL|package|com.google.gerrit.git.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
operator|.
name|testing
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
name|assertAbout
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|FailureMetadata
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
name|truth
operator|.
name|Subject
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
name|truth
operator|.
name|Truth
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
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_comment
comment|/** Subject over JGit {@link RevCommit}s. */
end_comment

begin_class
DECL|class|CommitSubject
specifier|public
class|class
name|CommitSubject
extends|extends
name|Subject
argument_list|<
name|CommitSubject
argument_list|,
name|RevCommit
argument_list|>
block|{
comment|/**    * Constructs a new subject.    *    * @param commit the commit.    * @return a new subject over the commit.    */
DECL|method|assertThat (RevCommit commit)
specifier|public
specifier|static
name|CommitSubject
name|assertThat
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|CommitSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|commit
argument_list|)
return|;
block|}
comment|/**    * Performs some common assertions over a single commit.    *    * @param commit the commit.    * @param expectedCommitMessage exact expected commit message.    * @param expectedCommitTimestamp expected commit timestamp, to the tolerance specified in {@link    *     #hasCommitTimestamp(Timestamp)}.    * @param expectedSha1 expected commit SHA-1.    */
DECL|method|assertCommit ( RevCommit commit, String expectedCommitMessage, Timestamp expectedCommitTimestamp, ObjectId expectedSha1)
specifier|public
specifier|static
name|void
name|assertCommit
parameter_list|(
name|RevCommit
name|commit
parameter_list|,
name|String
name|expectedCommitMessage
parameter_list|,
name|Timestamp
name|expectedCommitTimestamp
parameter_list|,
name|ObjectId
name|expectedSha1
parameter_list|)
block|{
name|CommitSubject
name|commitSubject
init|=
name|assertThat
argument_list|(
name|commit
argument_list|)
decl_stmt|;
name|commitSubject
operator|.
name|hasCommitMessage
argument_list|(
name|expectedCommitMessage
argument_list|)
expr_stmt|;
name|commitSubject
operator|.
name|hasCommitTimestamp
argument_list|(
name|expectedCommitTimestamp
argument_list|)
expr_stmt|;
name|commitSubject
operator|.
name|hasSha1
argument_list|(
name|expectedSha1
argument_list|)
expr_stmt|;
block|}
DECL|method|CommitSubject (FailureMetadata metadata, RevCommit actual)
specifier|private
name|CommitSubject
parameter_list|(
name|FailureMetadata
name|metadata
parameter_list|,
name|RevCommit
name|actual
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that the commit has the given commit message.    *    * @param expectedCommitMessage exact expected commit message.    */
DECL|method|hasCommitMessage (String expectedCommitMessage)
specifier|public
name|void
name|hasCommitMessage
parameter_list|(
name|String
name|expectedCommitMessage
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|RevCommit
name|commit
init|=
name|actual
argument_list|()
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|commit
operator|.
name|getFullMessage
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"commit message"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedCommitMessage
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that the commit has the given commit message, up to skew of at most 1 second.    *    * @param expectedCommitTimestamp expected commit timestamp.    */
DECL|method|hasCommitTimestamp (Timestamp expectedCommitTimestamp)
specifier|public
name|void
name|hasCommitTimestamp
parameter_list|(
name|Timestamp
name|expectedCommitTimestamp
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|RevCommit
name|commit
init|=
name|actual
argument_list|()
decl_stmt|;
name|long
name|timestampDiffMs
init|=
name|Math
operator|.
name|abs
argument_list|(
name|commit
operator|.
name|getCommitTime
argument_list|()
operator|*
literal|1000L
operator|-
name|expectedCommitTimestamp
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|timestampDiffMs
argument_list|)
operator|.
name|named
argument_list|(
literal|"commit timestamp diff"
argument_list|)
operator|.
name|isAtMost
argument_list|(
name|SECONDS
operator|.
name|toMillis
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that the commit has the given SHA-1.    *    * @param expectedSha1 expected commit SHA-1.    */
DECL|method|hasSha1 (ObjectId expectedSha1)
specifier|public
name|void
name|hasSha1
parameter_list|(
name|ObjectId
name|expectedSha1
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|RevCommit
name|commit
init|=
name|actual
argument_list|()
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|commit
argument_list|)
operator|.
name|named
argument_list|(
literal|"SHA1"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedSha1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

