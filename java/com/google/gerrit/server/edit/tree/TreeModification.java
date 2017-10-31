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
DECL|package|com.google.gerrit.server.edit.tree
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|edit
operator|.
name|tree
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|dircache
operator|.
name|DirCacheEditor
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
name|Repository
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
name|RevCommit
import|;
end_import

begin_comment
comment|/** A specific modification of a Git tree. */
end_comment

begin_interface
DECL|interface|TreeModification
specifier|public
interface|interface
name|TreeModification
block|{
comment|/**    * Returns a list of {@code PathEdit}s which are necessary in order to achieve the desired    * modification of the Git tree. The order of the {@code PathEdit}s can be crucial and hence    * shouldn't be changed.    *    * @param repository the affected Git repository    * @param baseCommit the commit to whose tree this modification is applied    * @return an ordered list of necessary {@code PathEdit}s    * @throws IOException if problems arise when accessing the repository    */
DECL|method|getPathEdits (Repository repository, RevCommit baseCommit)
name|List
argument_list|<
name|DirCacheEditor
operator|.
name|PathEdit
argument_list|>
name|getPathEdits
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|RevCommit
name|baseCommit
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Indicates a file path which is affected by this {@code TreeModification}. If the modification    * refers to several file paths (e.g. renaming a file), returning either of them is appropriate as    * long as the returned value is deterministic.    *    * @return an affected file path    */
annotation|@
name|VisibleForTesting
DECL|method|getFilePath ()
name|String
name|getFilePath
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

