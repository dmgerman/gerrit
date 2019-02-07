begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

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
name|Nullable
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

begin_class
DECL|class|ApprovalInfo
specifier|public
class|class
name|ApprovalInfo
extends|extends
name|AccountInfo
block|{
DECL|field|tag
specifier|public
name|String
name|tag
decl_stmt|;
DECL|field|value
specifier|public
name|Integer
name|value
decl_stmt|;
DECL|field|date
specifier|public
name|Timestamp
name|date
decl_stmt|;
DECL|field|postSubmit
specifier|public
name|Boolean
name|postSubmit
decl_stmt|;
DECL|field|permittedVotingRange
specifier|public
name|VotingRangeInfo
name|permittedVotingRange
decl_stmt|;
DECL|method|ApprovalInfo (Integer id)
specifier|public
name|ApprovalInfo
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|ApprovalInfo ( Integer id, Integer value, @Nullable VotingRangeInfo permittedVotingRange, @Nullable String tag, Timestamp date)
specifier|public
name|ApprovalInfo
parameter_list|(
name|Integer
name|id
parameter_list|,
name|Integer
name|value
parameter_list|,
annotation|@
name|Nullable
name|VotingRangeInfo
name|permittedVotingRange
parameter_list|,
annotation|@
name|Nullable
name|String
name|tag
parameter_list|,
name|Timestamp
name|date
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|permittedVotingRange
operator|=
name|permittedVotingRange
expr_stmt|;
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
name|this
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
block|}
block|}
end_class

end_unit

