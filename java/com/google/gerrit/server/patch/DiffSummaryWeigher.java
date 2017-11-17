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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|Weigher
import|;
end_import

begin_comment
comment|/** Computes memory usage for {@link DiffSummary} in bytes of memory used. */
end_comment

begin_class
DECL|class|DiffSummaryWeigher
specifier|public
class|class
name|DiffSummaryWeigher
implements|implements
name|Weigher
argument_list|<
name|DiffSummaryKey
argument_list|,
name|DiffSummary
argument_list|>
block|{
annotation|@
name|Override
DECL|method|weigh (DiffSummaryKey key, DiffSummary value)
specifier|public
name|int
name|weigh
parameter_list|(
name|DiffSummaryKey
name|key
parameter_list|,
name|DiffSummary
name|value
parameter_list|)
block|{
name|int
name|size
init|=
literal|16
operator|+
literal|4
operator|*
literal|8
operator|+
literal|2
operator|*
literal|36
comment|// Size of DiffSummaryKey, 64 bit JVM
operator|+
literal|16
operator|+
literal|8
operator|+
literal|2
operator|*
literal|4
comment|// Size of DiffSummary
operator|+
literal|16
operator|+
literal|8
decl_stmt|;
comment|// String[]
for|for
control|(
name|String
name|p
range|:
name|value
operator|.
name|getPaths
argument_list|()
control|)
block|{
name|size
operator|+=
literal|16
operator|+
literal|8
operator|+
literal|4
operator|*
literal|4
comment|// String
operator|+
literal|16
operator|+
literal|8
operator|+
name|p
operator|.
name|length
argument_list|()
operator|*
literal|2
expr_stmt|;
comment|// char[]
block|}
return|return
name|size
return|;
block|}
block|}
end_class

end_unit

