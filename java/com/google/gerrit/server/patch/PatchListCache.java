begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|entities
operator|.
name|Change
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
name|PatchSet
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_comment
comment|/** Provides a cached list of {@link PatchListEntry}. */
end_comment

begin_interface
DECL|interface|PatchListCache
specifier|public
interface|interface
name|PatchListCache
block|{
DECL|method|get (PatchListKey key, Project.NameKey project)
name|PatchList
name|get
parameter_list|(
name|PatchListKey
name|key
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|PatchListNotAvailableException
function_decl|;
DECL|method|get (Change change, PatchSet patchSet)
name|PatchList
name|get
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|)
throws|throws
name|PatchListNotAvailableException
function_decl|;
DECL|method|getOldId (Change change, PatchSet patchSet, Integer parentNum)
name|ObjectId
name|getOldId
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|Integer
name|parentNum
parameter_list|)
throws|throws
name|PatchListNotAvailableException
function_decl|;
DECL|method|getIntraLineDiff (IntraLineDiffKey key, IntraLineDiffArgs args)
name|IntraLineDiff
name|getIntraLineDiff
parameter_list|(
name|IntraLineDiffKey
name|key
parameter_list|,
name|IntraLineDiffArgs
name|args
parameter_list|)
function_decl|;
DECL|method|getDiffSummary (DiffSummaryKey key, Project.NameKey project)
name|DiffSummary
name|getDiffSummary
parameter_list|(
name|DiffSummaryKey
name|key
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|PatchListNotAvailableException
function_decl|;
block|}
end_interface

end_unit

