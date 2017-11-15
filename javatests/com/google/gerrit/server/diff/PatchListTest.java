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
DECL|package|com.google.gerrit.server.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
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
name|reviewdb
operator|.
name|client
operator|.
name|Patch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
DECL|class|PatchListTest
specifier|public
class|class
name|PatchListTest
block|{
annotation|@
name|Test
DECL|method|fileOrder ()
specifier|public
name|void
name|fileOrder
parameter_list|()
block|{
name|String
index|[]
name|names
init|=
block|{
literal|"zzz"
block|,
literal|"def/g"
block|,
literal|"/!xxx"
block|,
literal|"abc"
block|,
name|Patch
operator|.
name|MERGE_LIST
block|,
literal|"qrx"
block|,
name|Patch
operator|.
name|COMMIT_MSG
block|,     }
decl_stmt|;
name|String
index|[]
name|want
init|=
block|{
name|Patch
operator|.
name|COMMIT_MSG
block|,
name|Patch
operator|.
name|MERGE_LIST
block|,
literal|"/!xxx"
block|,
literal|"abc"
block|,
literal|"def/g"
block|,
literal|"qrx"
block|,
literal|"zzz"
block|,     }
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|names
argument_list|,
literal|0
argument_list|,
name|names
operator|.
name|length
argument_list|,
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
return|return
name|PatchList
operator|.
name|comparePaths
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|names
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|want
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|fileOrderNoMerge ()
specifier|public
name|void
name|fileOrderNoMerge
parameter_list|()
block|{
name|String
index|[]
name|names
init|=
block|{
literal|"zzz"
block|,
literal|"def/g"
block|,
literal|"/!xxx"
block|,
literal|"abc"
block|,
literal|"qrx"
block|,
name|Patch
operator|.
name|COMMIT_MSG
block|,     }
decl_stmt|;
name|String
index|[]
name|want
init|=
block|{
name|Patch
operator|.
name|COMMIT_MSG
block|,
literal|"/!xxx"
block|,
literal|"abc"
block|,
literal|"def/g"
block|,
literal|"qrx"
block|,
literal|"zzz"
block|,     }
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|names
argument_list|,
literal|0
argument_list|,
name|names
operator|.
name|length
argument_list|,
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
return|return
name|PatchList
operator|.
name|comparePaths
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|names
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|want
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

