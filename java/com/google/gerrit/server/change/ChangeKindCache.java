begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|ChangeKind
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|Config
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_comment
comment|/**  * Cache of {@link ChangeKind} per commit.  *  *<p>This is immutable conditioned on the merge strategy (unless the JGit strategy implementation  * changes, which might invalidate old entries).  */
end_comment

begin_interface
DECL|interface|ChangeKindCache
specifier|public
interface|interface
name|ChangeKindCache
block|{
DECL|method|getChangeKind ( Project.NameKey project, @Nullable RevWalk rw, @Nullable Config repoConfig, ObjectId prior, ObjectId next)
name|ChangeKind
name|getChangeKind
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
annotation|@
name|Nullable
name|RevWalk
name|rw
parameter_list|,
annotation|@
name|Nullable
name|Config
name|repoConfig
parameter_list|,
name|ObjectId
name|prior
parameter_list|,
name|ObjectId
name|next
parameter_list|)
function_decl|;
DECL|method|getChangeKind (Change change, PatchSet patch)
name|ChangeKind
name|getChangeKind
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchSet
name|patch
parameter_list|)
function_decl|;
DECL|method|getChangeKind ( @ullable RevWalk rw, @Nullable Config repoConfig, ChangeData cd, PatchSet patch)
name|ChangeKind
name|getChangeKind
parameter_list|(
annotation|@
name|Nullable
name|RevWalk
name|rw
parameter_list|,
annotation|@
name|Nullable
name|Config
name|repoConfig
parameter_list|,
name|ChangeData
name|cd
parameter_list|,
name|PatchSet
name|patch
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

