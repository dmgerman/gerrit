begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
package|;
end_package

begin_class
DECL|class|ChangeCosts
specifier|public
class|class
name|ChangeCosts
block|{
DECL|field|IDS_MEMORY
specifier|public
specifier|static
specifier|final
name|int
name|IDS_MEMORY
init|=
literal|1
decl_stmt|;
DECL|field|CHANGES_SCAN
specifier|public
specifier|static
specifier|final
name|int
name|CHANGES_SCAN
init|=
literal|2
decl_stmt|;
DECL|field|TR_SCAN
specifier|public
specifier|static
specifier|final
name|int
name|TR_SCAN
init|=
literal|20
decl_stmt|;
DECL|field|APPROVALS_SCAN
specifier|public
specifier|static
specifier|final
name|int
name|APPROVALS_SCAN
init|=
literal|30
decl_stmt|;
DECL|field|PATCH_SETS_SCAN
specifier|public
specifier|static
specifier|final
name|int
name|PATCH_SETS_SCAN
init|=
literal|30
decl_stmt|;
comment|/** Estimated matches for a Change-Id string. */
DECL|field|CARD_KEY
specifier|public
specifier|static
specifier|final
name|int
name|CARD_KEY
init|=
literal|5
decl_stmt|;
comment|/** Estimated matches for a commit SHA-1 string. */
DECL|field|CARD_COMMIT
specifier|public
specifier|static
specifier|final
name|int
name|CARD_COMMIT
init|=
literal|5
decl_stmt|;
comment|/** Estimated matches for a tracking/bug id string. */
DECL|field|CARD_TRACKING_IDS
specifier|public
specifier|static
specifier|final
name|int
name|CARD_TRACKING_IDS
init|=
literal|5
decl_stmt|;
DECL|method|cost (int cost, int cardinality)
specifier|public
specifier|static
name|int
name|cost
parameter_list|(
name|int
name|cost
parameter_list|,
name|int
name|cardinality
parameter_list|)
block|{
return|return
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|cost
argument_list|)
operator|*
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|cardinality
argument_list|)
return|;
block|}
DECL|method|ChangeCosts ()
specifier|private
name|ChangeCosts
parameter_list|()
block|{   }
block|}
end_class

end_unit

