begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|entities
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
name|entities
operator|.
name|Comment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|GsonBuilder
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
name|time
operator|.
name|ZonedDateTime
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

begin_class
DECL|class|CommentTimestampAdapterTest
specifier|public
class|class
name|CommentTimestampAdapterTest
block|{
comment|/** Arbitrary time outside of a DST transition, as an ISO instant. */
DECL|field|NON_DST_STR
specifier|private
specifier|static
specifier|final
name|String
name|NON_DST_STR
init|=
literal|"2017-02-07T10:20:30.123Z"
decl_stmt|;
comment|/** Arbitrary time outside of a DST transition, as a reasonable Java 8 representation. */
DECL|field|NON_DST
specifier|private
specifier|static
specifier|final
name|ZonedDateTime
name|NON_DST
init|=
name|ZonedDateTime
operator|.
name|parse
argument_list|(
name|NON_DST_STR
argument_list|)
decl_stmt|;
comment|/** {@link #NON_DST_STR} truncated to seconds. */
DECL|field|NON_DST_STR_TRUNC
specifier|private
specifier|static
specifier|final
name|String
name|NON_DST_STR_TRUNC
init|=
literal|"2017-02-07T10:20:30Z"
decl_stmt|;
comment|/** Arbitrary time outside of a DST transition, as an unreasonable Timestamp representation. */
DECL|field|NON_DST_TS
specifier|private
specifier|static
specifier|final
name|Timestamp
name|NON_DST_TS
init|=
name|Timestamp
operator|.
name|from
argument_list|(
name|NON_DST
operator|.
name|toInstant
argument_list|()
argument_list|)
decl_stmt|;
comment|/** {@link #NON_DST_TS} truncated to seconds. */
DECL|field|NON_DST_TS_TRUNC
specifier|private
specifier|static
specifier|final
name|Timestamp
name|NON_DST_TS_TRUNC
init|=
name|Timestamp
operator|.
name|from
argument_list|(
name|ZonedDateTime
operator|.
name|parse
argument_list|(
name|NON_DST_STR_TRUNC
argument_list|)
operator|.
name|toInstant
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * Real live ms since epoch timestamp of a comment that was posted during the PDT to PST    * transition in November 2013.    */
DECL|field|MID_DST_MS
specifier|private
specifier|static
specifier|final
name|long
name|MID_DST_MS
init|=
literal|1383466224175L
decl_stmt|;
comment|/**    * Ambiguous string representation of {@link #MID_DST_MS} that was actually stored in NoteDb for    * this comment.    */
DECL|field|MID_DST_STR
specifier|private
specifier|static
specifier|final
name|String
name|MID_DST_STR
init|=
literal|"Nov 3, 2013 1:10:24 AM"
decl_stmt|;
DECL|field|systemTimeZone
specifier|private
name|TimeZone
name|systemTimeZone
decl_stmt|;
DECL|field|legacyGson
specifier|private
name|Gson
name|legacyGson
decl_stmt|;
DECL|field|gson
specifier|private
name|Gson
name|gson
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|systemTimeZone
operator|=
name|TimeZone
operator|.
name|getDefault
argument_list|()
expr_stmt|;
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"America/Los_Angeles"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Match ChangeNoteUtil#gson as of 4e1f02db913d91f2988f559048e513e6093a1bce
name|legacyGson
operator|=
operator|new
name|GsonBuilder
argument_list|()
operator|.
name|setPrettyPrinting
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|gson
operator|=
name|ChangeNoteJson
operator|.
name|newGson
argument_list|()
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
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|systemTimeZone
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|legacyGsonBehavesAsExpectedDuringDstTransition ()
specifier|public
name|void
name|legacyGsonBehavesAsExpectedDuringDstTransition
parameter_list|()
block|{
name|long
name|oneHourMs
init|=
name|TimeUnit
operator|.
name|HOURS
operator|.
name|toMillis
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|beforeJson
init|=
literal|"\"Nov 3, 2013 12:10:24 AM\""
decl_stmt|;
name|Timestamp
name|beforeTs
init|=
operator|new
name|Timestamp
argument_list|(
name|MID_DST_MS
operator|-
name|oneHourMs
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|legacyGson
operator|.
name|toJson
argument_list|(
name|beforeTs
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|beforeJson
argument_list|)
expr_stmt|;
name|String
name|ambiguousJson
init|=
literal|'"'
operator|+
name|MID_DST_STR
operator|+
literal|'"'
decl_stmt|;
name|Timestamp
name|duringTs
init|=
operator|new
name|Timestamp
argument_list|(
name|MID_DST_MS
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|legacyGson
operator|.
name|toJson
argument_list|(
name|duringTs
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ambiguousJson
argument_list|)
expr_stmt|;
name|Timestamp
name|afterTs
init|=
operator|new
name|Timestamp
argument_list|(
name|MID_DST_MS
operator|+
name|oneHourMs
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|legacyGson
operator|.
name|toJson
argument_list|(
name|afterTs
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ambiguousJson
argument_list|)
expr_stmt|;
name|Timestamp
name|beforeTsTruncated
init|=
operator|new
name|Timestamp
argument_list|(
name|beforeTs
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
operator|*
literal|1000
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|legacyGson
operator|.
name|fromJson
argument_list|(
name|beforeJson
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|beforeTsTruncated
argument_list|)
expr_stmt|;
comment|// Gson just picks one, and it happens to be the one after the PST transition.
name|Timestamp
name|afterTsTruncated
init|=
operator|new
name|Timestamp
argument_list|(
name|afterTs
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
operator|*
literal|1000
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|legacyGson
operator|.
name|fromJson
argument_list|(
name|ambiguousJson
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|afterTsTruncated
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|legacyAdapterViaZonedDateTime ()
specifier|public
name|void
name|legacyAdapterViaZonedDateTime
parameter_list|()
block|{
name|assertThat
argument_list|(
name|legacyGson
operator|.
name|toJson
argument_list|(
name|NON_DST_TS
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"\"Feb 7, 2017 2:20:30 AM\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|legacyAdapterCanParseOutputOfNewAdapter ()
specifier|public
name|void
name|legacyAdapterCanParseOutputOfNewAdapter
parameter_list|()
block|{
name|String
name|instantJson
init|=
name|gson
operator|.
name|toJson
argument_list|(
name|NON_DST_TS
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|instantJson
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|'"'
operator|+
name|NON_DST_STR_TRUNC
operator|+
literal|'"'
argument_list|)
expr_stmt|;
name|Timestamp
name|result
init|=
name|legacyGson
operator|.
name|fromJson
argument_list|(
name|instantJson
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|NON_DST_TS_TRUNC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|newAdapterCanParseOutputOfLegacyAdapter ()
specifier|public
name|void
name|newAdapterCanParseOutputOfLegacyAdapter
parameter_list|()
block|{
name|String
name|legacyJson
init|=
name|legacyGson
operator|.
name|toJson
argument_list|(
name|NON_DST_TS
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|legacyJson
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"\"Feb 7, 2017 2:20:30 AM\""
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gson
operator|.
name|fromJson
argument_list|(
name|legacyJson
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|NON_DST_TS
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|newAdapterDisagreesWithLegacyAdapterDuringDstTransition ()
specifier|public
name|void
name|newAdapterDisagreesWithLegacyAdapterDuringDstTransition
parameter_list|()
block|{
name|String
name|duringJson
init|=
name|legacyGson
operator|.
name|toJson
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|MID_DST_MS
argument_list|)
argument_list|)
decl_stmt|;
name|Timestamp
name|duringTs
init|=
name|legacyGson
operator|.
name|fromJson
argument_list|(
name|duringJson
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// This is unfortunate, but it's just documenting the current behavior, there is no real good
comment|// solution here. The goal is that all these changes will be rebuilt with proper UTC instant
comment|// strings shortly after the new adapter is live.
name|Timestamp
name|newDuringTs
init|=
name|gson
operator|.
name|fromJson
argument_list|(
name|duringJson
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|newDuringTs
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|duringTs
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newDuringTs
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|duringTs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|newAdapterRoundTrip ()
specifier|public
name|void
name|newAdapterRoundTrip
parameter_list|()
block|{
name|String
name|json
init|=
name|gson
operator|.
name|toJson
argument_list|(
name|NON_DST_TS
argument_list|)
decl_stmt|;
comment|// Round-trip lossily truncates ms, but that's ok.
name|assertThat
argument_list|(
name|json
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|'"'
operator|+
name|NON_DST_STR_TRUNC
operator|+
literal|'"'
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gson
operator|.
name|fromJson
argument_list|(
name|json
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|NON_DST_TS_TRUNC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nullSafety ()
specifier|public
name|void
name|nullSafety
parameter_list|()
block|{
name|assertThat
argument_list|(
name|gson
operator|.
name|toJson
argument_list|(
literal|null
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gson
operator|.
name|fromJson
argument_list|(
literal|"null"
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|newAdapterRoundTripOfWholeComment ()
specifier|public
name|void
name|newAdapterRoundTripOfWholeComment
parameter_list|()
block|{
name|Comment
name|c
init|=
operator|new
name|Comment
argument_list|(
operator|new
name|Comment
operator|.
name|Key
argument_list|(
literal|"uuid"
argument_list|,
literal|"filename"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|100
argument_list|)
argument_list|,
name|NON_DST_TS
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|,
literal|"message"
argument_list|,
literal|"serverId"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|c
operator|.
name|lineNbr
operator|=
literal|1
expr_stmt|;
name|c
operator|.
name|setCommitId
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|gson
operator|.
name|toJson
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|json
argument_list|)
operator|.
name|contains
argument_list|(
literal|"\"writtenOn\": \""
operator|+
name|NON_DST_STR_TRUNC
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|Comment
name|result
init|=
name|gson
operator|.
name|fromJson
argument_list|(
name|json
argument_list|,
name|Comment
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Round-trip lossily truncates ms, but that's ok.
name|assertThat
argument_list|(
name|result
operator|.
name|writtenOn
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|NON_DST_TS_TRUNC
argument_list|)
expr_stmt|;
name|result
operator|.
name|writtenOn
operator|=
name|NON_DST_TS
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

