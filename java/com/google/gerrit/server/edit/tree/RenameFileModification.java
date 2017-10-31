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
name|Arrays
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|treewalk
operator|.
name|TreeWalk
import|;
end_import

begin_comment
comment|/** A {@code TreeModification} which renames a file or moves it to a different path. */
end_comment

begin_class
DECL|class|RenameFileModification
specifier|public
class|class
name|RenameFileModification
implements|implements
name|TreeModification
block|{
DECL|field|currentFilePath
specifier|private
specifier|final
name|String
name|currentFilePath
decl_stmt|;
DECL|field|newFilePath
specifier|private
specifier|final
name|String
name|newFilePath
decl_stmt|;
DECL|method|RenameFileModification (String currentFilePath, String newFilePath)
specifier|public
name|RenameFileModification
parameter_list|(
name|String
name|currentFilePath
parameter_list|,
name|String
name|newFilePath
parameter_list|)
block|{
name|this
operator|.
name|currentFilePath
operator|=
name|currentFilePath
expr_stmt|;
name|this
operator|.
name|newFilePath
operator|=
name|newFilePath
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
throws|throws
name|IOException
block|{
try|try
init|(
name|RevWalk
name|revWalk
init|=
operator|new
name|RevWalk
argument_list|(
name|repository
argument_list|)
init|)
block|{
name|revWalk
operator|.
name|parseHeaders
argument_list|(
name|baseCommit
argument_list|)
expr_stmt|;
try|try
init|(
name|TreeWalk
name|treeWalk
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|revWalk
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|currentFilePath
argument_list|,
name|baseCommit
operator|.
name|getTree
argument_list|()
argument_list|)
init|)
block|{
if|if
condition|(
name|treeWalk
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
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
name|currentFilePath
argument_list|)
decl_stmt|;
name|AddPath
name|addPathEdit
init|=
operator|new
name|AddPath
argument_list|(
name|newFilePath
argument_list|,
name|treeWalk
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
argument_list|,
name|treeWalk
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|deletePathEdit
argument_list|,
name|addPathEdit
argument_list|)
return|;
block|}
block|}
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
name|newFilePath
return|;
block|}
block|}
end_class

end_unit

