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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|AtomicLongMap
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
name|extensions
operator|.
name|common
operator|.
name|ChangeInfo
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
name|extensions
operator|.
name|events
operator|.
name|ChangeIndexedListener
import|;
end_import

begin_class
DECL|class|ChangeIndexedCounter
specifier|public
class|class
name|ChangeIndexedCounter
implements|implements
name|ChangeIndexedListener
block|{
DECL|field|countsByChange
specifier|private
specifier|final
name|AtomicLongMap
argument_list|<
name|Integer
argument_list|>
name|countsByChange
init|=
name|AtomicLongMap
operator|.
name|create
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|onChangeIndexed (String projectName, int id)
specifier|public
name|void
name|onChangeIndexed
parameter_list|(
name|String
name|projectName
parameter_list|,
name|int
name|id
parameter_list|)
block|{
name|countsByChange
operator|.
name|incrementAndGet
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onChangeDeleted (int id)
specifier|public
name|void
name|onChangeDeleted
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|countsByChange
operator|.
name|incrementAndGet
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|countsByChange
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getCount (ChangeInfo info)
specifier|public
name|long
name|getCount
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
return|return
name|countsByChange
operator|.
name|get
argument_list|(
name|info
operator|.
name|_number
argument_list|)
return|;
block|}
DECL|method|assertReindexOf (ChangeInfo info)
specifier|public
name|void
name|assertReindexOf
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
name|assertReindexOf
argument_list|(
name|info
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReindexOf (ChangeInfo info, long expectedCount)
specifier|public
name|void
name|assertReindexOf
parameter_list|(
name|ChangeInfo
name|info
parameter_list|,
name|long
name|expectedCount
parameter_list|)
block|{
name|assertThat
argument_list|(
name|countsByChange
operator|.
name|asMap
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|info
operator|.
name|_number
argument_list|,
name|expectedCount
argument_list|)
expr_stmt|;
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

