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
DECL|package|com.google.gerrit.server.http
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|http
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
name|client
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|GerritServer
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
name|ChangeControl
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
name|NoSuchChangeException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
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
name|Provider
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
name|org
operator|.
name|spearce
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
name|spearce
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
name|spearce
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
name|spearce
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
name|spearce
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
name|spearce
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
name|spearce
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
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|NB
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_comment
comment|/**  * Exports a single version of a patch as a normal file download.  *<p>  * This can be relatively unsafe with Microsoft Internet Explorer 6.0 as the  * browser will (rather incorrectly) treat an HTML or JavaScript file its  * supposed to download as though it was served by this site, and will execute  * it with the site's own protection domain. This opens a massive security hole  * so we package the content into a zip file.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Singleton
DECL|class|CatServlet
specifier|public
class|class
name|CatServlet
extends|extends
name|HttpServlet
block|{
DECL|field|ZIP
specifier|private
specifier|static
specifier|final
name|MimeType
name|ZIP
init|=
operator|new
name|MimeType
argument_list|(
literal|"application/zip"
argument_list|)
decl_stmt|;
DECL|field|requestDb
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|requestDb
decl_stmt|;
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|rng
specifier|private
specifier|final
name|SecureRandom
name|rng
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|FileTypeRegistry
name|registry
decl_stmt|;
DECL|field|changeControl
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControl
decl_stmt|;
annotation|@
name|Inject
DECL|method|CatServlet (final GerritServer gs, final Provider<ReviewDb> sf, final FileTypeRegistry ftr, final ChangeControl.Factory ccf)
name|CatServlet
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
specifier|final
name|FileTypeRegistry
name|ftr
parameter_list|,
specifier|final
name|ChangeControl
operator|.
name|Factory
name|ccf
parameter_list|)
block|{
name|requestDb
operator|=
name|sf
expr_stmt|;
name|server
operator|=
name|gs
expr_stmt|;
name|rng
operator|=
operator|new
name|SecureRandom
argument_list|()
expr_stmt|;
name|registry
operator|=
name|ftr
expr_stmt|;
name|changeControl
operator|=
name|ccf
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (final HttpServletRequest req, final HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|keyStr
init|=
name|req
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|keyStr
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
name|keyStr
operator|=
name|keyStr
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
specifier|final
name|int
name|side
decl_stmt|;
block|{
specifier|final
name|int
name|c
init|=
name|keyStr
operator|.
name|lastIndexOf
argument_list|(
literal|'^'
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
name|side
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|side
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|keyStr
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|keyStr
operator|=
name|keyStr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
try|try
block|{
name|patchKey
operator|=
name|Patch
operator|.
name|Key
operator|.
name|parse
argument_list|(
name|keyStr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchKey
operator|.
name|getParentKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
specifier|final
name|Project
name|project
decl_stmt|;
specifier|final
name|PatchSet
name|patchSet
decl_stmt|;
specifier|final
name|Patch
name|patch
decl_stmt|;
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|requestDb
operator|.
name|get
argument_list|()
decl_stmt|;
specifier|final
name|ChangeControl
name|control
init|=
name|changeControl
operator|.
name|validateFor
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|project
operator|=
name|control
operator|.
name|getProject
argument_list|()
expr_stmt|;
name|patchSet
operator|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|patchKey
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|patch
operator|=
name|db
operator|.
name|patches
argument_list|()
operator|.
name|get
argument_list|(
name|patchKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|patchSet
operator|==
literal|null
operator|||
name|patch
operator|==
literal|null
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
literal|"Cannot query database"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|Repository
name|repo
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|server
operator|.
name|openRepository
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
literal|"Cannot open repository"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|byte
index|[]
name|blobData
decl_stmt|;
specifier|final
name|RevCommit
name|fromCommit
decl_stmt|;
specifier|final
name|String
name|suffix
decl_stmt|;
specifier|final
name|String
name|path
init|=
name|patch
operator|.
name|getFileName
argument_list|()
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
specifier|final
name|RevCommit
name|c
decl_stmt|;
specifier|final
name|TreeWalk
name|tw
decl_stmt|;
name|c
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|side
operator|==
literal|0
condition|)
block|{
name|fromCommit
operator|=
name|c
expr_stmt|;
name|suffix
operator|=
literal|"new"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|1
operator|<=
name|side
operator|&&
name|side
operator|-
literal|1
operator|<
name|c
operator|.
name|getParentCount
argument_list|()
condition|)
block|{
name|fromCommit
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|c
operator|.
name|getParent
argument_list|(
name|side
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
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
name|side
expr_stmt|;
block|}
block|}
else|else
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
name|tw
operator|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|repo
argument_list|,
name|path
argument_list|,
name|fromCommit
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|tw
operator|==
literal|null
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectType
argument_list|()
operator|==
name|Constants
operator|.
name|OBJ_BLOB
condition|)
block|{
name|blobData
operator|=
name|repo
operator|.
name|openBlob
argument_list|(
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|getCachedBytes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
literal|"Cannot read repository"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
literal|"Cannot read repository"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|final
name|long
name|when
init|=
name|fromCommit
operator|.
name|getCommitTime
argument_list|()
operator|*
literal|1000L
decl_stmt|;
name|MimeType
name|contentType
init|=
name|registry
operator|.
name|getMimeType
argument_list|(
name|path
argument_list|,
name|blobData
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|outData
decl_stmt|;
if|if
condition|(
name|registry
operator|.
name|isSafeInline
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
name|outData
operator|=
name|blobData
expr_stmt|;
block|}
else|else
block|{
comment|// The content may not be safe to transmit inline, as a browser might
comment|// interpret it as HTML or JavaScript hosted by this site. Such code
comment|// might then run in the site's security domain, and may be able to use
comment|// the user's cookies to perform unauthorized actions.
comment|//
comment|// Usually, wrapping the content into a ZIP file forces the browser to
comment|// save the content to the local system instead.
comment|//
specifier|final
name|ByteArrayOutputStream
name|zip
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|ZipOutputStream
name|zo
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|zip
argument_list|)
decl_stmt|;
specifier|final
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
name|rand
argument_list|(
name|req
argument_list|,
name|suffix
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|e
operator|.
name|setComment
argument_list|(
name|fromCommit
operator|.
name|name
argument_list|()
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
name|blobData
operator|.
name|length
argument_list|)
expr_stmt|;
name|e
operator|.
name|setTime
argument_list|(
name|when
argument_list|)
expr_stmt|;
name|zo
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|zo
operator|.
name|write
argument_list|(
name|blobData
argument_list|)
expr_stmt|;
name|zo
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|zo
operator|.
name|close
argument_list|()
expr_stmt|;
name|outData
operator|=
name|zip
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
name|contentType
operator|=
name|ZIP
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Content-Disposition"
argument_list|,
literal|"attachment; filename=\""
operator|+
name|safeFileName
argument_list|(
name|path
argument_list|,
name|suffix
argument_list|)
operator|+
literal|".zip"
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|rsp
operator|.
name|setContentType
argument_list|(
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setContentLength
argument_list|(
name|outData
operator|.
name|length
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setDateHeader
argument_list|(
literal|"Last-Modified"
argument_list|,
name|when
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setDateHeader
argument_list|(
literal|"Expires"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Cache-Control"
argument_list|,
literal|"no-cache, must-revalidate"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|outData
argument_list|)
expr_stmt|;
block|}
DECL|method|safeFileName (String fileName, final String suffix)
specifier|private
specifier|static
name|String
name|safeFileName
parameter_list|(
name|String
name|fileName
parameter_list|,
specifier|final
name|String
name|suffix
parameter_list|)
block|{
comment|// Convert a file path (e.g. "src/Init.c") to a safe file name with
comment|// no meta-characters that might be unsafe on any given platform.
comment|//
specifier|final
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
specifier|final
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
specifier|final
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
DECL|method|rand (final HttpServletRequest req, final String suffix)
specifier|private
name|String
name|rand
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|String
name|suffix
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
comment|// Produce a random suffix that is difficult (or nearly impossible)
comment|// for an attacker to guess in advance. This reduces the risk that
comment|// an attacker could upload a *.class file and have us send a ZIP
comment|// that can be invoked through an applet tag in the victim's browser.
comment|//
specifier|final
name|MessageDigest
name|md
init|=
name|Constants
operator|.
name|newMessageDigest
argument_list|()
decl_stmt|;
specifier|final
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
name|encodeInt32
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|req
operator|.
name|getRemotePort
argument_list|()
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|req
operator|.
name|getRemoteAddr
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|NB
operator|.
name|encodeInt64
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|rng
operator|.
name|nextBytes
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
literal|8
argument_list|)
expr_stmt|;
return|return
name|suffix
operator|+
literal|"-"
operator|+
name|ObjectId
operator|.
name|fromRaw
argument_list|(
name|md
operator|.
name|digest
argument_list|()
argument_list|)
operator|.
name|name
argument_list|()
return|;
block|}
block|}
end_class

end_unit

