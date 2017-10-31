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
name|dircache
operator|.
name|DirCacheEntry
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
name|FileMode
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
comment|/**  * A {@code PathEdit} which adds a file path to the index. This operation is the counterpart to  * {@link org.eclipse.jgit.dircache.DirCacheEditor.DeletePath}.  */
end_comment

begin_class
DECL|class|AddPath
class|class
name|AddPath
extends|extends
name|DirCacheEditor
operator|.
name|PathEdit
block|{
DECL|field|fileMode
specifier|private
specifier|final
name|FileMode
name|fileMode
decl_stmt|;
DECL|field|objectId
specifier|private
specifier|final
name|ObjectId
name|objectId
decl_stmt|;
DECL|method|AddPath (String filePath, FileMode fileMode, ObjectId objectId)
name|AddPath
parameter_list|(
name|String
name|filePath
parameter_list|,
name|FileMode
name|fileMode
parameter_list|,
name|ObjectId
name|objectId
parameter_list|)
block|{
name|super
argument_list|(
name|filePath
argument_list|)
expr_stmt|;
name|this
operator|.
name|fileMode
operator|=
name|fileMode
expr_stmt|;
name|this
operator|.
name|objectId
operator|=
name|objectId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (DirCacheEntry dirCacheEntry)
specifier|public
name|void
name|apply
parameter_list|(
name|DirCacheEntry
name|dirCacheEntry
parameter_list|)
block|{
name|dirCacheEntry
operator|.
name|setFileMode
argument_list|(
name|fileMode
argument_list|)
expr_stmt|;
name|dirCacheEntry
operator|.
name|setObjectId
argument_list|(
name|objectId
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

