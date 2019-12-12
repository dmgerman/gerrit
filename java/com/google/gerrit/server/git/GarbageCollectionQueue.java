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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|entities
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|HashSet
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

begin_comment
comment|/** A thread-safe list of projects scheduled for GC. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GarbageCollectionQueue
specifier|public
class|class
name|GarbageCollectionQueue
block|{
DECL|field|projectsScheduledForGc
specifier|private
specifier|final
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projectsScheduledForGc
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|addAll (Collection<Project.NameKey> projects)
specifier|public
specifier|synchronized
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projects
parameter_list|)
block|{
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|added
init|=
name|Sets
operator|.
name|newLinkedHashSetWithExpectedSize
argument_list|(
name|projects
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projects
control|)
block|{
if|if
condition|(
name|projectsScheduledForGc
operator|.
name|add
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|added
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|added
return|;
block|}
DECL|method|gcFinished (Project.NameKey project)
specifier|public
specifier|synchronized
name|void
name|gcFinished
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|projectsScheduledForGc
operator|.
name|remove
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

