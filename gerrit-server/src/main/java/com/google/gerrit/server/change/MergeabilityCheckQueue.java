begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|collect
operator|.
name|Sets
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|MergeabilityCheckQueue
class|class
name|MergeabilityCheckQueue
block|{
DECL|field|pending
specifier|private
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|pending
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
DECL|field|forcePending
specifier|private
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|forcePending
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
DECL|method|addAll (Collection<Change> changes, boolean force)
specifier|synchronized
name|Set
argument_list|<
name|Change
argument_list|>
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|Change
argument_list|>
name|changes
parameter_list|,
name|boolean
name|force
parameter_list|)
block|{
name|Set
argument_list|<
name|Change
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newLinkedHashSetWithExpectedSize
argument_list|(
name|changes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Change
name|c
range|:
name|changes
control|)
block|{
if|if
condition|(
name|force
condition|?
name|forcePending
operator|.
name|add
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
else|:
name|pending
operator|.
name|add
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
DECL|method|updatingMergeabilityFlag (Change change, boolean force)
specifier|synchronized
name|void
name|updatingMergeabilityFlag
parameter_list|(
name|Change
name|change
parameter_list|,
name|boolean
name|force
parameter_list|)
block|{
if|if
condition|(
name|force
condition|)
block|{
name|forcePending
operator|.
name|remove
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|pending
operator|.
name|remove
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

