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
comment|/** A {@code TreeModification} which deletes a file. */
end_comment

begin_class
DECL|class|DeleteFileModification
specifier|public
class|class
name|DeleteFileModification
implements|implements
name|TreeModification
block|{
DECL|field|filePath
specifier|private
specifier|final
name|String
name|filePath
decl_stmt|;
DECL|method|DeleteFileModification (String filePath)
specifier|public
name|DeleteFileModification
parameter_list|(
name|String
name|filePath
parameter_list|)
block|{
name|this
operator|.
name|filePath
operator|=
name|filePath
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPathEdits (Repository repository, RevCommit baseCommit)
specifier|public
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
block|{
name|DirCacheEditor
operator|.
name|DeletePath
name|deletePathEdit
init|=
operator|new
name|DirCacheEditor
operator|.
name|DeletePath
argument_list|(
name|filePath
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|deletePathEdit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getFilePath ()
specifier|public
name|String
name|getFilePath
parameter_list|()
block|{
return|return
name|filePath
return|;
block|}
block|}
end_class

end_unit

