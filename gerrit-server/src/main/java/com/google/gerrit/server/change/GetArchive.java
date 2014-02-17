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
name|RestReadView
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
name|inject
operator|.
name|Inject
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
name|api
operator|.
name|ArchiveCommand
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|GetArchive
class|class
name|GetArchive
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|field|formats
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ArchiveFormat
argument_list|>
name|formats
init|=
name|ArchiveFormat
operator|.
name|init
argument_list|()
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--format"
argument_list|)
DECL|field|format
specifier|private
name|String
name|format
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetArchive (GitRepositoryManager repoManager)
name|GetArchive
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc)
specifier|public
name|BinaryResult
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|IOException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|format
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"format is not specified"
argument_list|)
throw|;
block|}
specifier|final
name|ArchiveFormat
name|f
init|=
name|formats
operator|.
name|get
argument_list|(
literal|"."
operator|+
name|format
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"unknown archive format"
argument_list|)
throw|;
block|}
name|boolean
name|close
init|=
literal|true
decl_stmt|;
specifier|final
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|BinaryResult
name|bin
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
name|out
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
operator|new
name|ArchiveCommand
argument_list|(
name|repo
argument_list|)
operator|.
name|setFormat
argument_list|(
name|f
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|setTree
argument_list|(
name|commit
operator|.
name|getTree
argument_list|()
argument_list|)
operator|.
name|setOutputStream
argument_list|(
name|out
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GitAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|bin
operator|.
name|disableGzip
argument_list|()
operator|.
name|setContentType
argument_list|(
name|f
operator|.
name|getMimeType
argument_list|()
argument_list|)
operator|.
name|setAttachmentName
argument_list|(
name|name
argument_list|(
name|f
argument_list|,
name|rw
argument_list|,
name|commit
argument_list|)
argument_list|)
expr_stmt|;
name|close
operator|=
literal|false
expr_stmt|;
return|return
name|bin
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|close
condition|)
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|close
condition|)
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|name (ArchiveFormat format, RevWalk rw, RevCommit commit)
specifier|private
specifier|static
name|String
name|name
parameter_list|(
name|ArchiveFormat
name|format
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|RevCommit
name|commit
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s%s"
argument_list|,
name|rw
operator|.
name|getObjectReader
argument_list|()
operator|.
name|abbreviate
argument_list|(
name|commit
argument_list|,
literal|7
argument_list|)
operator|.
name|name
argument_list|()
argument_list|,
name|format
operator|.
name|getDefaultSuffix
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

