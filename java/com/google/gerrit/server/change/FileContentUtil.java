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
name|base
operator|.
name|Strings
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
name|hash
operator|.
name|Hasher
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
name|hash
operator|.
name|Hashing
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
name|common
operator|.
name|data
operator|.
name|PatchScript
operator|.
name|FileMode
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
name|extensions
operator|.
name|restapi
operator|.
name|BadRequestException
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
name|BinaryResult
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
name|ResourceNotFoundException
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
name|git
operator|.
name|GitRepositoryManager
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
name|mime
operator|.
name|FileTypeRegistry
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
name|project
operator|.
name|ProjectState
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
name|util
operator|.
name|time
operator|.
name|TimeUtil
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
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeType
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipOutputStream
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
name|LargeObjectException
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
name|RepositoryNotFoundException
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
name|Constants
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
name|ObjectLoader
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|NB
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|FileContentUtil
specifier|public
class|class
name|FileContentUtil
block|{
DECL|field|TEXT_X_GERRIT_COMMIT_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_X_GERRIT_COMMIT_MESSAGE
init|=
literal|"text/x-gerrit-commit-message"
decl_stmt|;
DECL|field|TEXT_X_GERRIT_MERGE_LIST
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_X_GERRIT_MERGE_LIST
init|=
literal|"text/x-gerrit-merge-list"
decl_stmt|;
DECL|field|X_GIT_SYMLINK
specifier|private
specifier|static
specifier|final
name|String
name|X_GIT_SYMLINK
init|=
literal|"x-git/symlink"
decl_stmt|;
DECL|field|X_GIT_GITLINK
specifier|private
specifier|static
specifier|final
name|String
name|X_GIT_GITLINK
init|=
literal|"x-git/gitlink"
decl_stmt|;
DECL|field|MAX_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|MAX_SIZE
init|=
literal|5
operator|<<
literal|20
decl_stmt|;
DECL|field|ZIP_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|ZIP_TYPE
init|=
literal|"application/zip"
decl_stmt|;
DECL|field|rng
specifier|private
specifier|static
specifier|final
name|SecureRandom
name|rng
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|FileTypeRegistry
name|registry
decl_stmt|;
annotation|@
name|Inject
DECL|method|FileContentUtil (GitRepositoryManager repoManager, FileTypeRegistry ftr)
name|FileContentUtil
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|FileTypeRegistry
name|ftr
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|ftr
expr_stmt|;
block|}
comment|/**    * Get the content of a file at a specific commit or one of it's parent commits.    *    * @param project A {@code Project} that this request refers to.    * @param revstr An {@code ObjectId} specifying the commit.    * @param path A string specifying the filepath.    * @param parent A 1-based parent index to get the content from instead. Null if the content    *     should be obtained from {@code revstr} instead.    * @return Content of the file as {@code BinaryResult}.    * @throws ResourceNotFoundException    * @throws IOException    */
DECL|method|getContent ( ProjectState project, ObjectId revstr, String path, @Nullable Integer parent)
specifier|public
name|BinaryResult
name|getContent
parameter_list|(
name|ProjectState
name|project
parameter_list|,
name|ObjectId
name|revstr
parameter_list|,
name|String
name|path
parameter_list|,
annotation|@
name|Nullable
name|Integer
name|parent
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ResourceNotFoundException
throws|,
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|RevCommit
name|revCommit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|revstr
argument_list|)
decl_stmt|;
if|if
condition|(
name|revCommit
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"commit not found"
argument_list|)
throw|;
block|}
if|if
condition|(
name|parent
operator|>
name|revCommit
operator|.
name|getParentCount
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"invalid parent"
argument_list|)
throw|;
block|}
name|revstr
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|revstr
argument_list|)
operator|.
name|getParent
argument_list|(
name|Integer
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|parent
operator|-
literal|1
argument_list|)
argument_list|)
operator|.
name|toObjectId
argument_list|()
expr_stmt|;
block|}
return|return
name|getContent
argument_list|(
name|repo
argument_list|,
name|project
argument_list|,
name|revstr
argument_list|,
name|path
argument_list|)
return|;
block|}
block|}
DECL|method|getContent ( Repository repo, ProjectState project, ObjectId revstr, String path)
specifier|public
name|BinaryResult
name|getContent
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ProjectState
name|project
parameter_list|,
name|ObjectId
name|revstr
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceNotFoundException
throws|,
name|BadRequestException
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|revstr
argument_list|)
decl_stmt|;
try|try
init|(
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|path
argument_list|,
name|commit
operator|.
name|getTree
argument_list|()
argument_list|)
init|)
block|{
if|if
condition|(
name|tw
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|FileMode
name|mode
init|=
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ObjectId
name|id
init|=
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|==
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|FileMode
operator|.
name|GITLINK
condition|)
block|{
return|return
name|BinaryResult
operator|.
name|create
argument_list|(
name|id
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|setContentType
argument_list|(
name|X_GIT_GITLINK
argument_list|)
operator|.
name|base64
argument_list|()
return|;
block|}
if|if
condition|(
name|mode
operator|==
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|FileMode
operator|.
name|TREE
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"cannot retrieve content of directories"
argument_list|)
throw|;
block|}
name|ObjectLoader
name|obj
init|=
name|repo
operator|.
name|open
argument_list|(
name|id
argument_list|,
name|OBJ_BLOB
argument_list|)
decl_stmt|;
name|byte
index|[]
name|raw
decl_stmt|;
try|try
block|{
name|raw
operator|=
name|obj
operator|.
name|getCachedBytes
argument_list|(
name|MAX_SIZE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LargeObjectException
name|e
parameter_list|)
block|{
name|raw
operator|=
literal|null
expr_stmt|;
block|}
name|String
name|type
decl_stmt|;
if|if
condition|(
name|mode
operator|==
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|FileMode
operator|.
name|SYMLINK
condition|)
block|{
name|type
operator|=
name|X_GIT_SYMLINK
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|registry
operator|.
name|getMimeType
argument_list|(
name|path
argument_list|,
name|raw
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|type
operator|=
name|resolveContentType
argument_list|(
name|project
argument_list|,
name|path
argument_list|,
name|FileMode
operator|.
name|FILE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|asBinaryResult
argument_list|(
name|raw
argument_list|,
name|obj
argument_list|)
operator|.
name|setContentType
argument_list|(
name|type
argument_list|)
operator|.
name|base64
argument_list|()
return|;
block|}
block|}
block|}
DECL|method|asBinaryResult (byte[] raw, ObjectLoader obj)
specifier|private
specifier|static
name|BinaryResult
name|asBinaryResult
parameter_list|(
name|byte
index|[]
name|raw
parameter_list|,
name|ObjectLoader
name|obj
parameter_list|)
block|{
if|if
condition|(
name|raw
operator|!=
literal|null
condition|)
block|{
return|return
name|BinaryResult
operator|.
name|create
argument_list|(
name|raw
argument_list|)
return|;
block|}
name|BinaryResult
name|result
init|=
operator|new
name|BinaryResult
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|obj
operator|.
name|copyTo
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|result
operator|.
name|setContentLength
argument_list|(
name|obj
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|downloadContent ( ProjectState project, ObjectId revstr, String path, @Nullable Integer parent)
specifier|public
name|BinaryResult
name|downloadContent
parameter_list|(
name|ProjectState
name|project
parameter_list|,
name|ObjectId
name|revstr
parameter_list|,
name|String
name|path
parameter_list|,
annotation|@
name|Nullable
name|Integer
name|parent
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|String
name|suffix
init|=
literal|"new"
decl_stmt|;
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|revstr
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|commit
operator|.
name|getParentCount
argument_list|()
operator|==
literal|1
condition|)
block|{
name|suffix
operator|=
literal|"old"
expr_stmt|;
block|}
else|else
block|{
name|suffix
operator|=
literal|"old"
operator|+
name|parent
expr_stmt|;
block|}
name|commit
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|commit
operator|.
name|getParent
argument_list|(
name|parent
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|path
argument_list|,
name|commit
operator|.
name|getTree
argument_list|()
argument_list|)
init|)
block|{
if|if
condition|(
name|tw
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
name|int
name|mode
init|=
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectType
argument_list|()
decl_stmt|;
if|if
condition|(
name|mode
operator|!=
name|Constants
operator|.
name|OBJ_BLOB
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
name|ObjectId
name|id
init|=
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ObjectLoader
name|obj
init|=
name|repo
operator|.
name|open
argument_list|(
name|id
argument_list|,
name|OBJ_BLOB
argument_list|)
decl_stmt|;
name|byte
index|[]
name|raw
decl_stmt|;
try|try
block|{
name|raw
operator|=
name|obj
operator|.
name|getCachedBytes
argument_list|(
name|MAX_SIZE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LargeObjectException
name|e
parameter_list|)
block|{
name|raw
operator|=
literal|null
expr_stmt|;
block|}
name|MimeType
name|contentType
init|=
name|registry
operator|.
name|getMimeType
argument_list|(
name|path
argument_list|,
name|raw
argument_list|)
decl_stmt|;
return|return
name|registry
operator|.
name|isSafeInline
argument_list|(
name|contentType
argument_list|)
condition|?
name|wrapBlob
argument_list|(
name|path
argument_list|,
name|obj
argument_list|,
name|raw
argument_list|,
name|contentType
argument_list|,
name|suffix
argument_list|)
else|:
name|zipBlob
argument_list|(
name|path
argument_list|,
name|obj
argument_list|,
name|commit
argument_list|,
name|suffix
argument_list|)
return|;
block|}
block|}
block|}
DECL|method|wrapBlob ( String path, final ObjectLoader obj, byte[] raw, MimeType contentType, @Nullable String suffix)
specifier|private
name|BinaryResult
name|wrapBlob
parameter_list|(
name|String
name|path
parameter_list|,
specifier|final
name|ObjectLoader
name|obj
parameter_list|,
name|byte
index|[]
name|raw
parameter_list|,
name|MimeType
name|contentType
parameter_list|,
annotation|@
name|Nullable
name|String
name|suffix
parameter_list|)
block|{
return|return
name|asBinaryResult
argument_list|(
name|raw
argument_list|,
name|obj
argument_list|)
operator|.
name|setContentType
argument_list|(
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|setAttachmentName
argument_list|(
name|safeFileName
argument_list|(
name|path
argument_list|,
name|suffix
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
DECL|method|zipBlob ( final String path, ObjectLoader obj, RevCommit commit, @Nullable final String suffix)
specifier|private
name|BinaryResult
name|zipBlob
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
name|ObjectLoader
name|obj
parameter_list|,
name|RevCommit
name|commit
parameter_list|,
annotation|@
name|Nullable
specifier|final
name|String
name|suffix
parameter_list|)
block|{
specifier|final
name|String
name|commitName
init|=
name|commit
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|long
name|when
init|=
name|commit
operator|.
name|getCommitTime
argument_list|()
operator|*
literal|1000L
decl_stmt|;
return|return
operator|new
name|BinaryResult
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ZipOutputStream
name|zipOut
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|os
argument_list|)
init|)
block|{
name|String
name|decoration
init|=
name|randSuffix
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
name|decoration
operator|=
name|suffix
operator|+
literal|'-'
operator|+
name|decoration
expr_stmt|;
block|}
name|ZipEntry
name|e
init|=
operator|new
name|ZipEntry
argument_list|(
name|safeFileName
argument_list|(
name|path
argument_list|,
name|decoration
argument_list|)
argument_list|)
decl_stmt|;
name|e
operator|.
name|setComment
argument_list|(
name|commitName
operator|+
literal|":"
operator|+
name|path
argument_list|)
expr_stmt|;
name|e
operator|.
name|setSize
argument_list|(
name|obj
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|setTime
argument_list|(
name|when
argument_list|)
expr_stmt|;
name|zipOut
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|obj
operator|.
name|copyTo
argument_list|(
name|zipOut
argument_list|)
expr_stmt|;
name|zipOut
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
block|}
block|}
block|}
operator|.
name|setContentType
argument_list|(
name|ZIP_TYPE
argument_list|)
operator|.
name|setAttachmentName
argument_list|(
name|safeFileName
argument_list|(
name|path
argument_list|,
name|suffix
argument_list|)
operator|+
literal|".zip"
argument_list|)
operator|.
name|disableGzip
argument_list|()
return|;
block|}
DECL|method|safeFileName (String fileName, @Nullable String suffix)
specifier|private
specifier|static
name|String
name|safeFileName
parameter_list|(
name|String
name|fileName
parameter_list|,
annotation|@
name|Nullable
name|String
name|suffix
parameter_list|)
block|{
comment|// Convert a file path (e.g. "src/Init.c") to a safe file name with
comment|// no meta-characters that might be unsafe on any given platform.
comment|//
name|int
name|slash
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|slash
operator|>=
literal|0
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|slash
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|(
name|fileName
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fileName
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|char
name|c
init|=
name|fileName
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'_'
operator|||
name|c
operator|==
literal|'-'
operator|||
name|c
operator|==
literal|'.'
operator|||
name|c
operator|==
literal|'@'
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|'0'
operator|<=
name|c
operator|&&
name|c
operator|<=
literal|'9'
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|'A'
operator|<=
name|c
operator|&&
name|c
operator|<=
literal|'Z'
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|'a'
operator|<=
name|c
operator|&&
name|c
operator|<=
literal|'z'
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|' '
operator|||
name|c
operator|==
literal|'\n'
operator|||
name|c
operator|==
literal|'\r'
operator|||
name|c
operator|==
literal|'\t'
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
block|}
block|}
name|fileName
operator|=
name|r
operator|.
name|toString
argument_list|()
expr_stmt|;
name|int
name|ext
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|suffix
operator|==
literal|null
condition|)
block|{
return|return
name|fileName
return|;
block|}
elseif|else
if|if
condition|(
name|ext
operator|<=
literal|0
condition|)
block|{
return|return
name|fileName
operator|+
literal|"_"
operator|+
name|suffix
return|;
block|}
else|else
block|{
return|return
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ext
argument_list|)
operator|+
literal|"_"
operator|+
name|suffix
operator|+
name|fileName
operator|.
name|substring
argument_list|(
name|ext
argument_list|)
return|;
block|}
block|}
DECL|method|randSuffix ()
specifier|private
specifier|static
name|String
name|randSuffix
parameter_list|()
block|{
comment|// Produce a random suffix that is difficult (or nearly impossible)
comment|// for an attacker to guess in advance. This reduces the risk that
comment|// an attacker could upload a *.class file and have us send a ZIP
comment|// that can be invoked through an applet tag in the victim's browser.
comment|//
name|Hasher
name|h
init|=
name|Hashing
operator|.
name|murmur3_128
argument_list|()
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|8
index|]
decl_stmt|;
name|NB
operator|.
name|encodeInt64
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|TimeUtil
operator|.
name|nowMs
argument_list|()
argument_list|)
expr_stmt|;
name|h
operator|.
name|putBytes
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|rng
operator|.
name|nextBytes
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|h
operator|.
name|putBytes
argument_list|(
name|buf
argument_list|)
expr_stmt|;
return|return
name|h
operator|.
name|hash
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|resolveContentType ( ProjectState project, String path, FileMode fileMode, String mimeType)
specifier|public
specifier|static
name|String
name|resolveContentType
parameter_list|(
name|ProjectState
name|project
parameter_list|,
name|String
name|path
parameter_list|,
name|FileMode
name|fileMode
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
switch|switch
condition|(
name|fileMode
condition|)
block|{
case|case
name|FILE
case|:
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|TEXT_X_GERRIT_COMMIT_MESSAGE
return|;
block|}
if|if
condition|(
name|Patch
operator|.
name|MERGE_LIST
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|TEXT_X_GERRIT_MERGE_LIST
return|;
block|}
if|if
condition|(
name|project
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProjectState
name|p
range|:
name|project
operator|.
name|tree
argument_list|()
control|)
block|{
name|String
name|t
init|=
name|p
operator|.
name|getConfig
argument_list|()
operator|.
name|getMimeTypes
argument_list|()
operator|.
name|getMimeType
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
return|return
name|t
return|;
block|}
block|}
block|}
return|return
name|mimeType
return|;
case|case
name|GITLINK
case|:
return|return
name|X_GIT_GITLINK
return|;
case|case
name|SYMLINK
case|:
return|return
name|X_GIT_SYMLINK
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"file mode: "
operator|+
name|fileMode
argument_list|)
throw|;
block|}
block|}
DECL|method|openRepository (ProjectState project)
specifier|private
name|Repository
name|openRepository
parameter_list|(
name|ProjectState
name|project
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
return|return
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

