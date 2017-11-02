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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|OBJ_BLOB
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
name|io
operator|.
name|ByteStreams
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
name|restapi
operator|.
name|RawInput
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
name|io
operator|.
name|InputStream
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
name|errors
operator|.
name|InvalidObjectIdException
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
name|ObjectInserter
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/** A {@code TreeModification} which changes the content of a file. */
end_comment

begin_class
DECL|class|ChangeFileContentModification
specifier|public
class|class
name|ChangeFileContentModification
implements|implements
name|TreeModification
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ChangeFileContentModification
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|filePath
specifier|private
specifier|final
name|String
name|filePath
decl_stmt|;
DECL|field|newContent
specifier|private
specifier|final
name|RawInput
name|newContent
decl_stmt|;
DECL|method|ChangeFileContentModification (String filePath, RawInput newContent)
specifier|public
name|ChangeFileContentModification
parameter_list|(
name|String
name|filePath
parameter_list|,
name|RawInput
name|newContent
parameter_list|)
block|{
name|this
operator|.
name|filePath
operator|=
name|filePath
expr_stmt|;
name|this
operator|.
name|newContent
operator|=
name|checkNotNull
argument_list|(
name|newContent
argument_list|,
literal|"new content required"
argument_list|)
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
name|PathEdit
name|changeContentEdit
init|=
operator|new
name|ChangeContent
argument_list|(
name|filePath
argument_list|,
name|newContent
argument_list|,
name|repository
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|changeContentEdit
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
annotation|@
name|VisibleForTesting
DECL|method|getNewContent ()
name|RawInput
name|getNewContent
parameter_list|()
block|{
return|return
name|newContent
return|;
block|}
comment|/** A {@code PathEdit} which changes the contents of a file. */
DECL|class|ChangeContent
specifier|private
specifier|static
class|class
name|ChangeContent
extends|extends
name|DirCacheEditor
operator|.
name|PathEdit
block|{
DECL|field|newContent
specifier|private
specifier|final
name|RawInput
name|newContent
decl_stmt|;
DECL|field|repository
specifier|private
specifier|final
name|Repository
name|repository
decl_stmt|;
DECL|method|ChangeContent (String filePath, RawInput newContent, Repository repository)
name|ChangeContent
parameter_list|(
name|String
name|filePath
parameter_list|,
name|RawInput
name|newContent
parameter_list|,
name|Repository
name|repository
parameter_list|)
block|{
name|super
argument_list|(
name|filePath
argument_list|)
expr_stmt|;
name|this
operator|.
name|newContent
operator|=
name|newContent
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
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
try|try
block|{
if|if
condition|(
name|dirCacheEntry
operator|.
name|getFileMode
argument_list|()
operator|==
name|FileMode
operator|.
name|GITLINK
condition|)
block|{
name|dirCacheEntry
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|dirCacheEntry
operator|.
name|setLastModified
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ObjectId
name|newObjectId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|getNewContentBytes
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|dirCacheEntry
operator|.
name|setObjectId
argument_list|(
name|newObjectId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|dirCacheEntry
operator|.
name|getRawMode
argument_list|()
operator|==
literal|0
condition|)
block|{
name|dirCacheEntry
operator|.
name|setFileMode
argument_list|(
name|FileMode
operator|.
name|REGULAR_FILE
argument_list|)
expr_stmt|;
block|}
name|ObjectId
name|newBlobObjectId
init|=
name|createNewBlobAndGetItsId
argument_list|()
decl_stmt|;
name|dirCacheEntry
operator|.
name|setObjectId
argument_list|(
name|newBlobObjectId
argument_list|)
expr_stmt|;
block|}
comment|// Previously, these two exceptions were swallowed. To improve the
comment|// situation, we log them now. However, we should think of a better
comment|// approach.
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|String
name|message
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Could not change the content of %s"
argument_list|,
name|dirCacheEntry
operator|.
name|getPathString
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidObjectIdException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid object id in submodule link"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createNewBlobAndGetItsId ()
specifier|private
name|ObjectId
name|createNewBlobAndGetItsId
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|ObjectInserter
name|objectInserter
init|=
name|repository
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|ObjectId
name|blobObjectId
init|=
name|createNewBlobAndGetItsId
argument_list|(
name|objectInserter
argument_list|)
decl_stmt|;
name|objectInserter
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|blobObjectId
return|;
block|}
block|}
DECL|method|createNewBlobAndGetItsId (ObjectInserter objectInserter)
specifier|private
name|ObjectId
name|createNewBlobAndGetItsId
parameter_list|(
name|ObjectInserter
name|objectInserter
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|contentLength
init|=
name|newContent
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentLength
operator|<
literal|0
condition|)
block|{
return|return
name|objectInserter
operator|.
name|insert
argument_list|(
name|OBJ_BLOB
argument_list|,
name|getNewContentBytes
argument_list|()
argument_list|)
return|;
block|}
name|InputStream
name|contentInputStream
init|=
name|newContent
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
return|return
name|objectInserter
operator|.
name|insert
argument_list|(
name|OBJ_BLOB
argument_list|,
name|contentLength
argument_list|,
name|contentInputStream
argument_list|)
return|;
block|}
DECL|method|getNewContentBytes ()
specifier|private
name|byte
index|[]
name|getNewContentBytes
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|newContent
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
