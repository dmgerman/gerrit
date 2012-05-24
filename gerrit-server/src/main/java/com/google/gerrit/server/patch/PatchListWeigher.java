begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
comment|/** Approximates memory usage for PatchList in bytes of memory used. */
end_comment

begin_class
DECL|class|PatchListWeigher
specifier|public
class|class
name|PatchListWeigher
implements|implements
name|Weigher
argument_list|<
name|PatchListKey
argument_list|,
name|PatchList
argument_list|>
block|{
annotation|@
name|Override
DECL|method|weigh (PatchListKey key, PatchList value)
specifier|public
name|int
name|weigh
parameter_list|(
name|PatchListKey
name|key
parameter_list|,
name|PatchList
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
comment|// Size of PatchListKey, 64 bit JVM
operator|+
literal|16
operator|+
literal|3
operator|*
literal|8
operator|+
literal|3
operator|*
literal|4
operator|+
literal|20
decl_stmt|;
comment|// Size of PatchList, 64 bit JVM
for|for
control|(
name|PatchListEntry
name|e
range|:
name|value
operator|.
name|getPatches
argument_list|()
control|)
block|{
name|size
operator|+=
name|e
operator|.
name|weigh
argument_list|()
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
block|}
end_class

end_unit

