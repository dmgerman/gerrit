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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Month
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
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
DECL|class|AccountGroupTest
specifier|public
class|class
name|AccountGroupTest
block|{
annotation|@
name|Test
DECL|method|auditCreationInstant ()
specifier|public
name|void
name|auditCreationInstant
parameter_list|()
block|{
name|Instant
name|instant
init|=
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2009
argument_list|,
name|Month
operator|.
name|JUNE
argument_list|,
literal|8
argument_list|,
literal|19
argument_list|,
literal|31
argument_list|)
operator|.
name|toInstant
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|AccountGroup
operator|.
name|auditCreationInstantTs
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|Timestamp
operator|.
name|from
argument_list|(
name|instant
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

