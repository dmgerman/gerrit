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
comment|// limitations under the License.package com.google.gerrit.server.git;
end_comment

begin_package
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|annotations
operator|.
name|VisibleForTesting
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
name|collect
operator|.
name|Lists
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
name|Inject
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/** Dynamic pointers to the index versions used for searching and writing. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|IndexCollection
specifier|public
class|class
name|IndexCollection
block|{
DECL|field|writeIndexes
specifier|private
specifier|final
name|CopyOnWriteArrayList
argument_list|<
name|ChangeIndex
argument_list|>
name|writeIndexes
decl_stmt|;
DECL|field|searchIndex
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|ChangeIndex
argument_list|>
name|searchIndex
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|VisibleForTesting
DECL|method|IndexCollection ()
specifier|public
name|IndexCollection
parameter_list|()
block|{
name|this
operator|.
name|writeIndexes
operator|=
name|Lists
operator|.
name|newCopyOnWriteArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|searchIndex
operator|=
operator|new
name|AtomicReference
argument_list|<
name|ChangeIndex
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**    * @return the current search index version, or null if the secondary index is    *     disabled.    */
annotation|@
name|Nullable
DECL|method|getSearchIndex ()
specifier|public
name|ChangeIndex
name|getSearchIndex
parameter_list|()
block|{
return|return
name|searchIndex
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|setSearchIndex (ChangeIndex index)
specifier|public
name|void
name|setSearchIndex
parameter_list|(
name|ChangeIndex
name|index
parameter_list|)
block|{
name|searchIndex
operator|.
name|set
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
DECL|method|getWriteIndexes ()
specifier|public
name|Collection
argument_list|<
name|ChangeIndex
argument_list|>
name|getWriteIndexes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|writeIndexes
argument_list|)
return|;
block|}
DECL|method|addWriteIndex (ChangeIndex index)
specifier|public
specifier|synchronized
name|void
name|addWriteIndex
parameter_list|(
name|ChangeIndex
name|index
parameter_list|)
block|{
name|int
name|version
init|=
name|index
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeIndex
name|i
range|:
name|writeIndexes
control|)
block|{
if|if
condition|(
name|i
operator|.
name|getSchema
argument_list|()
operator|.
name|getVersion
argument_list|()
operator|==
name|version
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Write index version "
operator|+
name|version
operator|+
literal|" already in list"
argument_list|)
throw|;
block|}
block|}
name|writeIndexes
operator|.
name|add
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

