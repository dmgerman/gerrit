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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Callable
import|;
end_import

begin_class
DECL|class|DiffSummaryLoader
specifier|public
class|class
name|DiffSummaryLoader
implements|implements
name|Callable
argument_list|<
name|DiffSummary
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (DiffSummaryKey key, Project.NameKey project)
name|DiffSummaryLoader
name|create
parameter_list|(
name|DiffSummaryKey
name|key
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
function_decl|;
block|}
DECL|field|patchListCache
specifier|private
specifier|final
name|PatchListCache
name|patchListCache
decl_stmt|;
DECL|field|key
specifier|private
specifier|final
name|DiffSummaryKey
name|key
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
annotation|@
name|Inject
DECL|method|DiffSummaryLoader (PatchListCache plc, @Assisted DiffSummaryKey k, @Assisted Project.NameKey p)
name|DiffSummaryLoader
parameter_list|(
name|PatchListCache
name|plc
parameter_list|,
annotation|@
name|Assisted
name|DiffSummaryKey
name|k
parameter_list|,
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
name|patchListCache
operator|=
name|plc
expr_stmt|;
name|key
operator|=
name|k
expr_stmt|;
name|project
operator|=
name|p
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|DiffSummary
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|PatchList
name|patchList
init|=
name|patchListCache
operator|.
name|get
argument_list|(
name|key
operator|.
name|toPatchListKey
argument_list|()
argument_list|,
name|project
argument_list|)
decl_stmt|;
return|return
name|toDiffSummary
argument_list|(
name|patchList
argument_list|)
return|;
block|}
DECL|method|toDiffSummary (PatchList patchList)
specifier|private
name|DiffSummary
name|toDiffSummary
parameter_list|(
name|PatchList
name|patchList
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|patchList
operator|.
name|getPatches
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PatchListEntry
name|e
range|:
name|patchList
operator|.
name|getPatches
argument_list|()
control|)
block|{
if|if
condition|(
name|Patch
operator|.
name|isMagic
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
switch|switch
condition|(
name|e
operator|.
name|getChangeType
argument_list|()
condition|)
block|{
case|case
name|ADDED
case|:
case|case
name|MODIFIED
case|:
case|case
name|DELETED
case|:
case|case
name|COPIED
case|:
case|case
name|REWRITE
case|:
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|RENAMED
case|:
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
operator|new
name|DiffSummary
argument_list|(
name|r
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|()
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
argument_list|,
name|patchList
operator|.
name|getInsertions
argument_list|()
argument_list|,
name|patchList
operator|.
name|getDeletions
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

