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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|ResourceConflictException
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
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|diff
operator|.
name|DiffFormatter
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
name|AbbreviatedObjectId
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
name|PersonIdent
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
name|filter
operator|.
name|PathFilter
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

begin_class
DECL|class|GetPatch
specifier|public
class|class
name|GetPatch
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|FILE_NOT_FOUND
specifier|private
specifier|final
name|String
name|FILE_NOT_FOUND
init|=
literal|"File not found: %s."
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--zip"
argument_list|)
DECL|field|zip
specifier|private
name|boolean
name|zip
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--download"
argument_list|)
DECL|field|download
specifier|private
name|boolean
name|download
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--path"
argument_list|)
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetPatch (GitRepositoryManager repoManager)
name|GetPatch
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
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|ResourceNotFoundException
block|{
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
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|close
init|=
literal|true
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
name|RevCommit
index|[]
name|parents
init|=
name|commit
operator|.
name|getParents
argument_list|()
decl_stmt|;
if|if
condition|(
name|parents
operator|.
name|length
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Revision has more than 1 parent."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|parents
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Revision has no parent."
argument_list|)
throw|;
block|}
specifier|final
name|RevCommit
name|base
init|=
name|parents
index|[
literal|0
index|]
decl_stmt|;
name|rw
operator|.
name|parseBody
argument_list|(
name|base
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|zip
condition|)
block|{
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|ZipEntry
name|e
init|=
operator|new
name|ZipEntry
argument_list|(
name|fileName
argument_list|(
name|rw
argument_list|,
name|commit
argument_list|)
argument_list|)
decl_stmt|;
name|e
operator|.
name|setTime
argument_list|(
name|commit
operator|.
name|getCommitTime
argument_list|()
operator|*
literal|1000L
argument_list|)
expr_stmt|;
name|zos
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|format
argument_list|(
name|zos
argument_list|)
expr_stmt|;
name|zos
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|zos
operator|.
name|finish
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|format
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|format
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Only add header if no path is specified
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|formatEmailHeader
argument_list|(
name|commit
argument_list|)
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|DiffFormatter
name|fmt
init|=
operator|new
name|DiffFormatter
argument_list|(
name|out
argument_list|)
init|)
block|{
name|fmt
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|fmt
operator|.
name|setPathFilter
argument_list|(
name|PathFilter
operator|.
name|create
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|fmt
operator|.
name|format
argument_list|(
name|base
operator|.
name|getTree
argument_list|()
argument_list|,
name|commit
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|flush
argument_list|()
expr_stmt|;
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
name|close
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
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|bin
operator|.
name|asString
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|FILE_NOT_FOUND
argument_list|,
name|path
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|zip
condition|)
block|{
name|bin
operator|.
name|disableGzip
argument_list|()
operator|.
name|setContentType
argument_list|(
literal|"application/zip"
argument_list|)
operator|.
name|setAttachmentName
argument_list|(
name|fileName
argument_list|(
name|rw
argument_list|,
name|commit
argument_list|)
operator|+
literal|".zip"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bin
operator|.
name|base64
argument_list|()
operator|.
name|setContentType
argument_list|(
literal|"application/mbox"
argument_list|)
operator|.
name|setAttachmentName
argument_list|(
name|download
condition|?
name|fileName
argument_list|(
name|rw
argument_list|,
name|commit
argument_list|)
operator|+
literal|".base64"
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
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
name|close
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
DECL|method|setPath (String path)
specifier|public
name|GetPatch
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|formatEmailHeader (RevCommit commit)
specifier|private
specifier|static
name|String
name|formatEmailHeader
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|PersonIdent
name|author
init|=
name|commit
operator|.
name|getAuthorIdent
argument_list|()
decl_stmt|;
name|String
name|subject
init|=
name|commit
operator|.
name|getShortMessage
argument_list|()
decl_stmt|;
name|String
name|msg
init|=
name|commit
operator|.
name|getFullMessage
argument_list|()
operator|.
name|substring
argument_list|(
name|subject
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|startsWith
argument_list|(
literal|"\n\n"
argument_list|)
condition|)
block|{
name|msg
operator|=
name|msg
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"From "
argument_list|)
operator|.
name|append
argument_list|(
name|commit
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
literal|"Mon Sep 17 00:00:00 2001\n"
argument_list|)
comment|// Fixed timestamp to match output of C Git's format-patch
operator|.
name|append
argument_list|(
literal|"From: "
argument_list|)
operator|.
name|append
argument_list|(
name|author
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|author
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Date: "
argument_list|)
operator|.
name|append
argument_list|(
name|formatDate
argument_list|(
name|author
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|append
argument_list|(
literal|"Subject: [PATCH] "
argument_list|)
operator|.
name|append
argument_list|(
name|subject
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|msg
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|append
argument_list|(
literal|"---\n\n"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|formatDate (PersonIdent author)
specifier|private
specifier|static
name|String
name|formatDate
parameter_list|(
name|PersonIdent
name|author
parameter_list|)
block|{
name|SimpleDateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss Z"
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|df
operator|.
name|setCalendar
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|(
name|author
operator|.
name|getTimeZone
argument_list|()
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|df
operator|.
name|format
argument_list|(
name|author
operator|.
name|getWhen
argument_list|()
argument_list|)
return|;
block|}
DECL|method|fileName (RevWalk rw, RevCommit commit)
specifier|private
specifier|static
name|String
name|fileName
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|RevCommit
name|commit
parameter_list|)
throws|throws
name|IOException
block|{
name|AbbreviatedObjectId
name|id
init|=
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
decl_stmt|;
return|return
name|id
operator|.
name|name
argument_list|()
operator|+
literal|".diff"
return|;
block|}
block|}
end_class

end_unit
