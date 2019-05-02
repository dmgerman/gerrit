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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|Collection
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
DECL|class|SubmitRecordTest
specifier|public
class|class
name|SubmitRecordTest
block|{
DECL|field|OK_RECORD
specifier|private
specifier|static
specifier|final
name|SubmitRecord
name|OK_RECORD
decl_stmt|;
DECL|field|FORCED_RECORD
specifier|private
specifier|static
specifier|final
name|SubmitRecord
name|FORCED_RECORD
decl_stmt|;
DECL|field|NOT_READY_RECORD
specifier|private
specifier|static
specifier|final
name|SubmitRecord
name|NOT_READY_RECORD
decl_stmt|;
static|static
block|{
name|OK_RECORD
operator|=
operator|new
name|SubmitRecord
argument_list|()
expr_stmt|;
name|OK_RECORD
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
expr_stmt|;
name|FORCED_RECORD
operator|=
operator|new
name|SubmitRecord
argument_list|()
expr_stmt|;
name|FORCED_RECORD
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|FORCED
expr_stmt|;
name|NOT_READY_RECORD
operator|=
operator|new
name|SubmitRecord
argument_list|()
expr_stmt|;
name|NOT_READY_RECORD
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|NOT_READY
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|okIfAllOkay ()
specifier|public
name|void
name|okIfAllOkay
parameter_list|()
block|{
name|Collection
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|submitRecords
operator|.
name|add
argument_list|(
name|OK_RECORD
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|SubmitRecord
operator|.
name|allRecordsOK
argument_list|(
name|submitRecords
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|okWhenEmpty ()
specifier|public
name|void
name|okWhenEmpty
parameter_list|()
block|{
name|Collection
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|SubmitRecord
operator|.
name|allRecordsOK
argument_list|(
name|submitRecords
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|okWhenForced ()
specifier|public
name|void
name|okWhenForced
parameter_list|()
block|{
name|Collection
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|submitRecords
operator|.
name|add
argument_list|(
name|FORCED_RECORD
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|SubmitRecord
operator|.
name|allRecordsOK
argument_list|(
name|submitRecords
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|emptyResultIfInvalid ()
specifier|public
name|void
name|emptyResultIfInvalid
parameter_list|()
block|{
name|Collection
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|submitRecords
operator|.
name|add
argument_list|(
name|NOT_READY_RECORD
argument_list|)
expr_stmt|;
name|submitRecords
operator|.
name|add
argument_list|(
name|OK_RECORD
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|SubmitRecord
operator|.
name|allRecordsOK
argument_list|(
name|submitRecords
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

