begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.codereview.manager.unpack
package|package
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|unpack
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|CompletePatchset
operator|.
name|CompletePatchsetRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|CompletePatchset
operator|.
name|CompletePatchsetResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|UploadPatchsetFile
operator|.
name|UploadPatchsetFileRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|internal
operator|.
name|UploadPatchsetFile
operator|.
name|UploadPatchsetFileResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|Backend
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|manager
operator|.
name|StopProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|rpc
operator|.
name|SimpleController
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|ByteString
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|RpcCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|ObjectLoader
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|util
operator|.
name|zip
operator|.
name|Deflater
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
name|DeflaterOutputStream
import|;
end_import

begin_class
DECL|class|PatchSetUploader
class|class
name|PatchSetUploader
implements|implements
name|Runnable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PatchSetUploader
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|MAX_DATA_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|MAX_DATA_SIZE
init|=
literal|1022
operator|*
literal|1024
decl_stmt|;
comment|// bytes
DECL|field|EMPTY_BLOB_ID
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_BLOB_ID
decl_stmt|;
DECL|field|EMPTY_DEFLATE
specifier|private
specifier|static
name|ByteString
name|EMPTY_DEFLATE
decl_stmt|;
static|static
block|{
specifier|final
name|MessageDigest
name|md
init|=
name|Constants
operator|.
name|newMessageDigest
argument_list|()
decl_stmt|;
name|md
operator|.
name|update
argument_list|(
name|Constants
operator|.
name|encodeASCII
argument_list|(
literal|"blob 0\0"
argument_list|)
argument_list|)
expr_stmt|;
name|EMPTY_BLOB_ID
operator|=
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
expr_stmt|;
block|}
DECL|field|server
specifier|private
specifier|final
name|Backend
name|server
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Repository
name|db
decl_stmt|;
DECL|field|commit
specifier|private
specifier|final
name|RevCommit
name|commit
decl_stmt|;
DECL|field|commitName
specifier|private
specifier|final
name|String
name|commitName
decl_stmt|;
DECL|field|patchsetKey
specifier|private
specifier|final
name|String
name|patchsetKey
decl_stmt|;
DECL|field|patchDeflater
specifier|private
name|Deflater
name|patchDeflater
decl_stmt|;
DECL|field|compressedFilenames
specifier|private
name|ByteString
operator|.
name|Output
name|compressedFilenames
decl_stmt|;
DECL|field|filenameOut
specifier|private
name|Writer
name|filenameOut
decl_stmt|;
DECL|method|PatchSetUploader (final Backend be, final Repository sourceRepo, final RevCommit sourceCommit, final String destPatchsetKey)
name|PatchSetUploader
parameter_list|(
specifier|final
name|Backend
name|be
parameter_list|,
specifier|final
name|Repository
name|sourceRepo
parameter_list|,
specifier|final
name|RevCommit
name|sourceCommit
parameter_list|,
specifier|final
name|String
name|destPatchsetKey
parameter_list|)
block|{
name|server
operator|=
name|be
expr_stmt|;
name|db
operator|=
name|sourceRepo
expr_stmt|;
name|commit
operator|=
name|sourceCommit
expr_stmt|;
name|commitName
operator|=
name|commit
operator|.
name|getId
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
name|patchsetKey
operator|=
name|destPatchsetKey
expr_stmt|;
block|}
DECL|method|logkey ()
specifier|private
name|String
name|logkey
parameter_list|()
block|{
return|return
name|db
operator|.
name|getDirectory
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" "
operator|+
name|commitName
return|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" begin"
argument_list|)
expr_stmt|;
try|try
block|{
name|runImpl
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fatal
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" failure"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fatal
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" failure"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|runImpl ()
specifier|private
name|void
name|runImpl
parameter_list|()
block|{
try|try
block|{
name|compressedFilenames
operator|=
name|ByteString
operator|.
name|newOutput
argument_list|()
expr_stmt|;
name|filenameOut
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|DeflaterOutputStream
argument_list|(
name|compressedFilenames
argument_list|,
operator|new
name|Deflater
argument_list|(
name|Deflater
operator|.
name|BEST_COMPRESSION
argument_list|)
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" cannot initialize filename compression"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
name|patchDeflater
operator|=
operator|new
name|Deflater
argument_list|(
name|Deflater
operator|.
name|BEST_COMPRESSION
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|DiffReader
name|dr
init|=
operator|new
name|DiffReader
argument_list|(
name|db
argument_list|,
name|commit
argument_list|)
decl_stmt|;
try|try
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
name|FileDiff
name|file
decl_stmt|;
while|while
condition|(
operator|(
name|file
operator|=
name|dr
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|storeOneDiff
argument_list|(
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|filenameOut
operator|.
name|write
argument_list|(
literal|'\0'
argument_list|)
expr_stmt|;
block|}
name|filenameOut
operator|.
name|write
argument_list|(
name|file
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|dr
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|filenameOut
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StopProcessingException
name|halt
parameter_list|)
block|{
return|return;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" diff failed"
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return;
block|}
finally|finally
block|{
name|patchDeflater
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
specifier|final
name|CompletePatchsetRequest
operator|.
name|Builder
name|req
decl_stmt|;
name|req
operator|=
name|CompletePatchsetRequest
operator|.
name|newBuilder
argument_list|()
expr_stmt|;
name|req
operator|.
name|setPatchsetKey
argument_list|(
name|patchsetKey
argument_list|)
expr_stmt|;
name|req
operator|.
name|setCompressedFilenames
argument_list|(
name|compressedFilenames
operator|.
name|toByteString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SimpleController
name|ctrl
init|=
operator|new
name|SimpleController
argument_list|()
decl_stmt|;
name|server
operator|.
name|getChangeService
argument_list|()
operator|.
name|completePatchset
argument_list|(
name|ctrl
argument_list|,
name|req
operator|.
name|build
argument_list|()
argument_list|,
operator|new
name|RpcCallback
argument_list|<
name|CompletePatchsetResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|(
specifier|final
name|CompletePatchsetResponse
name|rsp
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" complete"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctrl
operator|.
name|failed
argument_list|()
condition|)
block|{
specifier|final
name|String
name|why
init|=
name|ctrl
operator|.
name|errorText
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|error
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" completing failed: "
operator|+
name|why
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeOneDiff (final FileDiff diff)
specifier|private
name|void
name|storeOneDiff
parameter_list|(
specifier|final
name|FileDiff
name|diff
parameter_list|)
throws|throws
name|StopProcessingException
block|{
specifier|final
name|UploadPatchsetFileRequest
name|req
init|=
name|toFileRequest
argument_list|(
name|diff
argument_list|)
decl_stmt|;
specifier|final
name|SimpleController
name|ctrl
init|=
operator|new
name|SimpleController
argument_list|()
decl_stmt|;
name|server
operator|.
name|getChangeService
argument_list|()
operator|.
name|uploadPatchsetFile
argument_list|(
name|ctrl
argument_list|,
name|req
argument_list|,
operator|new
name|RpcCallback
argument_list|<
name|UploadPatchsetFileResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|(
specifier|final
name|UploadPatchsetFileResponse
name|rsp
parameter_list|)
block|{
specifier|final
name|UploadPatchsetFileResponse
operator|.
name|CodeType
name|sc
init|=
name|rsp
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
specifier|final
name|String
name|fn
init|=
name|req
operator|.
name|getFileName
argument_list|()
decl_stmt|;
specifier|final
name|String
name|pk
init|=
name|req
operator|.
name|getPatchsetKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|sc
operator|==
name|UploadPatchsetFileResponse
operator|.
name|CodeType
operator|.
name|CREATED
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" uploaded "
operator|+
name|fn
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sc
operator|==
name|UploadPatchsetFileResponse
operator|.
name|CodeType
operator|.
name|CLOSED
condition|)
block|{
name|ctrl
operator|.
name|setFailed
argument_list|(
literal|"patchset closed "
operator|+
name|pk
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sc
operator|==
name|UploadPatchsetFileResponse
operator|.
name|CodeType
operator|.
name|UNKNOWN_PATCHSET
condition|)
block|{
name|ctrl
operator|.
name|setFailed
argument_list|(
literal|"patchset unknown "
operator|+
name|pk
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sc
operator|==
name|UploadPatchsetFileResponse
operator|.
name|CodeType
operator|.
name|PATCHING_ERROR
condition|)
block|{
name|ctrl
operator|.
name|setFailed
argument_list|(
literal|"server cannot apply patch"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ctrl
operator|.
name|setFailed
argument_list|(
literal|"Unknown status "
operator|+
name|sc
operator|.
name|name
argument_list|()
operator|+
literal|" "
operator|+
name|pk
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctrl
operator|.
name|failed
argument_list|()
condition|)
block|{
specifier|final
name|String
name|fn
init|=
name|req
operator|.
name|getFileName
argument_list|()
decl_stmt|;
specifier|final
name|String
name|why
init|=
name|ctrl
operator|.
name|errorText
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|error
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" uploading "
operator|+
name|fn
operator|+
literal|" failed: "
operator|+
name|why
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|StopProcessingException
argument_list|(
name|why
argument_list|)
throw|;
block|}
block|}
DECL|method|toFileRequest (final FileDiff diff)
specifier|private
name|UploadPatchsetFileRequest
name|toFileRequest
parameter_list|(
specifier|final
name|FileDiff
name|diff
parameter_list|)
block|{
specifier|final
name|UploadPatchsetFileRequest
operator|.
name|Builder
name|req
decl_stmt|;
name|req
operator|=
name|UploadPatchsetFileRequest
operator|.
name|newBuilder
argument_list|()
expr_stmt|;
name|req
operator|.
name|setPatchsetKey
argument_list|(
name|patchsetKey
argument_list|)
expr_stmt|;
name|req
operator|.
name|setFileName
argument_list|(
name|diff
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
name|req
operator|.
name|setStatus
argument_list|(
name|diff
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|diff
operator|.
name|isBinary
argument_list|()
operator|&&
operator|!
name|diff
operator|.
name|isTruncated
argument_list|()
condition|)
block|{
specifier|final
name|ObjectId
name|baseId
init|=
name|diff
operator|.
name|getBaseId
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseId
operator|==
literal|null
operator|||
name|ObjectId
operator|.
name|equals
argument_list|(
name|baseId
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
condition|)
block|{
name|req
operator|.
name|setBaseId
argument_list|(
name|EMPTY_BLOB_ID
argument_list|)
expr_stmt|;
name|req
operator|.
name|setBaseZ
argument_list|(
name|getEmptyDeflatedBlob
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
specifier|final
name|ObjectLoader
name|ldr
init|=
name|db
operator|.
name|openBlob
argument_list|(
name|baseId
argument_list|)
decl_stmt|;
if|if
condition|(
name|ldr
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fatal
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" missing "
operator|+
name|baseId
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|StopProcessingException
argument_list|(
literal|"No "
operator|+
name|baseId
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|byte
index|[]
name|base
init|=
name|ldr
operator|.
name|getCachedBytes
argument_list|()
decl_stmt|;
if|if
condition|(
name|base
operator|.
name|length
operator|+
name|diff
operator|.
name|getPatchSize
argument_list|()
operator|>
name|MAX_DATA_SIZE
condition|)
block|{
name|diff
operator|.
name|truncatePatch
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|req
operator|.
name|setBaseId
argument_list|(
name|baseId
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|req
operator|.
name|setBaseZ
argument_list|(
name|deflate
argument_list|(
name|base
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|LOG
operator|.
name|fatal
argument_list|(
name|logkey
argument_list|()
operator|+
literal|" cannot read base "
operator|+
name|baseId
operator|.
name|name
argument_list|()
argument_list|,
name|err
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|StopProcessingException
argument_list|(
literal|"No "
operator|+
name|baseId
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|diff
operator|.
name|isBinary
argument_list|()
operator|&&
operator|!
name|diff
operator|.
name|isTruncated
argument_list|()
condition|)
block|{
specifier|final
name|ObjectId
name|finalId
init|=
name|diff
operator|.
name|getFinalId
argument_list|()
decl_stmt|;
if|if
condition|(
name|finalId
operator|==
literal|null
operator|||
name|ObjectId
operator|.
name|equals
argument_list|(
name|finalId
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
condition|)
block|{
name|req
operator|.
name|setFinalId
argument_list|(
name|EMPTY_BLOB_ID
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|req
operator|.
name|setFinalId
argument_list|(
name|finalId
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|byte
index|[]
name|rawpatch
init|=
name|diff
operator|.
name|getPatch
argument_list|()
decl_stmt|;
name|req
operator|.
name|setPatchZ
argument_list|(
name|deflate
argument_list|(
name|rawpatch
argument_list|)
argument_list|)
expr_stmt|;
name|req
operator|.
name|setPatchId
argument_list|(
name|hashOf
argument_list|(
name|rawpatch
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|req
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|getEmptyDeflatedBlob ()
specifier|private
name|ByteString
name|getEmptyDeflatedBlob
parameter_list|()
block|{
synchronized|synchronized
init|(
name|PatchSetUploader
operator|.
name|class
init|)
block|{
if|if
condition|(
name|EMPTY_DEFLATE
operator|==
literal|null
condition|)
block|{
name|EMPTY_DEFLATE
operator|=
name|deflate
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|EMPTY_DEFLATE
return|;
block|}
block|}
DECL|method|deflate (final byte[] buf)
specifier|private
name|ByteString
name|deflate
parameter_list|(
specifier|final
name|byte
index|[]
name|buf
parameter_list|)
block|{
specifier|final
name|ByteString
operator|.
name|Output
name|r
init|=
name|ByteString
operator|.
name|newOutput
argument_list|()
decl_stmt|;
specifier|final
name|DeflaterOutputStream
name|out
decl_stmt|;
name|out
operator|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|r
argument_list|,
name|patchDeflater
argument_list|)
expr_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
comment|// This should not happen.
throw|throw
operator|new
name|StopProcessingException
argument_list|(
literal|"Unexpected IO error"
argument_list|,
name|err
argument_list|)
throw|;
block|}
finally|finally
block|{
name|patchDeflater
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
return|return
name|r
operator|.
name|toByteString
argument_list|()
return|;
block|}
DECL|method|hashOf (final byte[] in)
specifier|private
specifier|static
name|String
name|hashOf
parameter_list|(
specifier|final
name|byte
index|[]
name|in
parameter_list|)
block|{
specifier|final
name|MessageDigest
name|md
init|=
name|Constants
operator|.
name|newMessageDigest
argument_list|()
decl_stmt|;
name|md
operator|.
name|update
argument_list|(
name|in
argument_list|,
literal|0
argument_list|,
name|in
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
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

