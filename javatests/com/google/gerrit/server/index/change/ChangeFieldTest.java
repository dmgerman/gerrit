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
name|HashBasedTable
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
name|Table
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
name|SubmitRecord
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
name|SubmitRequirement
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
name|server
operator|.
name|ReviewerSet
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
name|ReviewerStateInternal
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
name|time
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
name|Collections
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
name|concurrent
operator|.
name|TimeUnit
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
DECL|class|ChangeFieldTest
specifier|public
class|class
name|ChangeFieldTest
extends|extends
name|GerritBaseTests
block|{
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
DECL|method|reviewerFieldValues ()
specifier|public
name|void
name|reviewerFieldValues
parameter_list|()
block|{
name|Table
argument_list|<
name|ReviewerStateInternal
argument_list|,
name|Account
operator|.
name|Id
argument_list|,
name|Timestamp
argument_list|>
name|t
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
name|Timestamp
name|t1
init|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
decl_stmt|;
name|t
operator|.
name|put
argument_list|(
name|ReviewerStateInternal
operator|.
name|REVIEWER
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|1
argument_list|)
argument_list|,
name|t1
argument_list|)
expr_stmt|;
name|Timestamp
name|t2
init|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
decl_stmt|;
name|t
operator|.
name|put
argument_list|(
name|ReviewerStateInternal
operator|.
name|CC
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|2
argument_list|)
argument_list|,
name|t2
argument_list|)
expr_stmt|;
name|ReviewerSet
name|reviewers
init|=
name|ReviewerSet
operator|.
name|fromTable
argument_list|(
name|t
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|ChangeField
operator|.
name|getReviewerFieldValues
argument_list|(
name|reviewers
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|values
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"REVIEWER,1"
argument_list|,
literal|"REVIEWER,1,"
operator|+
name|t1
operator|.
name|getTime
argument_list|()
argument_list|,
literal|"CC,2"
argument_list|,
literal|"CC,2,"
operator|+
name|t2
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ChangeField
operator|.
name|parseReviewerFieldValues
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
name|values
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|reviewers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatSubmitRecordValues ()
specifier|public
name|void
name|formatSubmitRecordValues
parameter_list|()
block|{
name|assertThat
argument_list|(
name|ChangeField
operator|.
name|formatSubmitRecordValues
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|record
argument_list|(
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|MAY
argument_list|,
literal|"Label-1"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|OK
argument_list|,
literal|"Label-2"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"OK"
argument_list|,
literal|"MAY,label-1"
argument_list|,
literal|"OK,label-2"
argument_list|,
literal|"OK,label-2,0"
argument_list|,
literal|"OK,label-2,1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|storedSubmitRecords ()
specifier|public
name|void
name|storedSubmitRecords
parameter_list|()
block|{
name|assertStoredRecordRoundTrip
argument_list|(
name|record
argument_list|(
name|SubmitRecord
operator|.
name|Status
operator|.
name|CLOSED
argument_list|)
argument_list|)
expr_stmt|;
name|SubmitRecord
name|r
init|=
name|record
argument_list|(
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|MAY
argument_list|,
literal|"Label-1"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|OK
argument_list|,
literal|"Label-2"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertStoredRecordRoundTrip
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|storedSubmitRecordsWithRequirement ()
specifier|public
name|void
name|storedSubmitRecordsWithRequirement
parameter_list|()
block|{
name|SubmitRecord
name|r
init|=
name|record
argument_list|(
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|MAY
argument_list|,
literal|"Label-1"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|OK
argument_list|,
literal|"Label-2"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|SubmitRequirement
name|sr
init|=
name|SubmitRequirement
operator|.
name|builder
argument_list|()
operator|.
name|setType
argument_list|(
literal|"short_type"
argument_list|)
operator|.
name|setFallbackText
argument_list|(
literal|"Fallback text may contain special symbols like<> \\ / ; :"
argument_list|)
operator|.
name|addCustomValue
argument_list|(
literal|"custom_data"
argument_list|,
literal|"my value"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|r
operator|.
name|requirements
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|assertStoredRecordRoundTrip
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|storedSubmitRequirementWithoutCustomData ()
specifier|public
name|void
name|storedSubmitRequirementWithoutCustomData
parameter_list|()
block|{
name|SubmitRecord
name|r
init|=
name|record
argument_list|(
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|MAY
argument_list|,
literal|"Label-1"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|label
argument_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|OK
argument_list|,
literal|"Label-2"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
comment|// Doesn't have any custom data value
name|SubmitRequirement
name|sr
init|=
name|SubmitRequirement
operator|.
name|builder
argument_list|()
operator|.
name|setFallbackText
argument_list|(
literal|"short_type"
argument_list|)
operator|.
name|setType
argument_list|(
literal|"ci_status"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|r
operator|.
name|requirements
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|assertStoredRecordRoundTrip
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|record (SubmitRecord.Status status, SubmitRecord.Label... labels)
specifier|private
specifier|static
name|SubmitRecord
name|record
parameter_list|(
name|SubmitRecord
operator|.
name|Status
name|status
parameter_list|,
name|SubmitRecord
operator|.
name|Label
modifier|...
name|labels
parameter_list|)
block|{
name|SubmitRecord
name|r
init|=
operator|new
name|SubmitRecord
argument_list|()
decl_stmt|;
name|r
operator|.
name|status
operator|=
name|status
expr_stmt|;
if|if
condition|(
name|labels
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|r
operator|.
name|labels
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|labels
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|label ( SubmitRecord.Label.Status status, String label, Integer appliedBy)
specifier|private
specifier|static
name|SubmitRecord
operator|.
name|Label
name|label
parameter_list|(
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
name|status
parameter_list|,
name|String
name|label
parameter_list|,
name|Integer
name|appliedBy
parameter_list|)
block|{
name|SubmitRecord
operator|.
name|Label
name|l
init|=
operator|new
name|SubmitRecord
operator|.
name|Label
argument_list|()
decl_stmt|;
name|l
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|l
operator|.
name|label
operator|=
name|label
expr_stmt|;
if|if
condition|(
name|appliedBy
operator|!=
literal|null
condition|)
block|{
name|l
operator|.
name|appliedBy
operator|=
name|Account
operator|.
name|id
argument_list|(
name|appliedBy
argument_list|)
expr_stmt|;
block|}
return|return
name|l
return|;
block|}
DECL|method|assertStoredRecordRoundTrip (SubmitRecord... records)
specifier|private
specifier|static
name|void
name|assertStoredRecordRoundTrip
parameter_list|(
name|SubmitRecord
modifier|...
name|records
parameter_list|)
block|{
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|recordList
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|records
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|stored
init|=
name|ChangeField
operator|.
name|storedSubmitRecords
argument_list|(
name|recordList
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|s
lambda|->
operator|new
name|String
argument_list|(
name|s
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ChangeField
operator|.
name|parseSubmitRecords
argument_list|(
name|stored
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"JSON %s"
operator|+
name|stored
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|recordList
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

