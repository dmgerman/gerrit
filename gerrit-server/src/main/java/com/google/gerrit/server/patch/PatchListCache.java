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
name|reviewdb
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
name|reviewdb
operator|.
name|PatchSet
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
name|diff
operator|.
name|Edit
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
DECL|method|get (PatchListKey key)
specifier|public
name|PatchList
name|get
parameter_list|(
name|PatchListKey
name|key
parameter_list|)
function_decl|;
DECL|method|get (Change change, PatchSet patchSet)
specifier|public
name|PatchList
name|get
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|)
function_decl|;
DECL|method|getIntraLineDiff (ObjectId aId, Text aText, ObjectId bId, Text bText, List<Edit> edits)
specifier|public
name|IntraLineDiff
name|getIntraLineDiff
parameter_list|(
name|ObjectId
name|aId
parameter_list|,
name|Text
name|aText
parameter_list|,
name|ObjectId
name|bId
parameter_list|,
name|Text
name|bText
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

