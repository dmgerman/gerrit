begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|api
operator|.
name|changes
operator|.
name|SubmitInput
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
name|MethodNotAllowedException
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
name|PreconditionFailedException
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
name|RestApiException
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|server
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
name|IdentifiedUser
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
name|MergeOp
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
name|MergeOpRepoManager
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
name|MergeOpRepoManager
operator|.
name|OpenRepo
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
name|gwtorm
operator|.
name|server
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|archivers
operator|.
name|ArchiveOutputStream
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
name|NullProgressMonitor
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
name|transport
operator|.
name|BundleWriter
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
name|transport
operator|.
name|ReceiveCommand
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PreviewSubmit
specifier|public
class|class
name|PreviewSubmit
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|mergeOpProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|MergeOp
argument_list|>
name|mergeOpProvider
decl_stmt|;
DECL|field|allowedFormats
specifier|private
specifier|final
name|AllowedFormats
name|allowedFormats
decl_stmt|;
DECL|field|format
specifier|private
name|String
name|format
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--format"
argument_list|)
DECL|method|setFormat (String f)
specifier|public
name|void
name|setFormat
parameter_list|(
name|String
name|f
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|f
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|PreviewSubmit (Provider<ReviewDb> dbProvider, Provider<MergeOp> mergeOpProvider, AllowedFormats allowedFormats)
name|PreviewSubmit
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|Provider
argument_list|<
name|MergeOp
argument_list|>
name|mergeOpProvider
parameter_list|,
name|AllowedFormats
name|allowedFormats
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|mergeOpProvider
operator|=
name|mergeOpProvider
expr_stmt|;
name|this
operator|.
name|allowedFormats
operator|=
name|allowedFormats
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
name|RestApiException
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
name|ArchiveFormat
name|f
init|=
name|allowedFormats
operator|.
name|extensions
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
name|Change
name|change
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PreconditionFailedException
argument_list|(
literal|"change is "
operator|+
name|Submit
operator|.
name|status
argument_list|(
name|change
argument_list|)
argument_list|)
throw|;
block|}
name|ChangeControl
name|control
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|getUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"Anonymous users cannot submit"
argument_list|)
throw|;
block|}
try|try
init|(
name|BinaryResult
name|b
init|=
name|getBundles
argument_list|(
name|rsrc
argument_list|,
name|f
argument_list|)
init|)
block|{
name|b
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
literal|"submit-preview-"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"."
operator|+
name|format
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Error generating submit preview"
argument_list|)
throw|;
block|}
block|}
DECL|method|getBundles (RevisionResource rsrc, final ArchiveFormat f)
specifier|private
name|BinaryResult
name|getBundles
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
specifier|final
name|ArchiveFormat
name|f
parameter_list|)
throws|throws
name|OrmException
throws|,
name|RestApiException
block|{
name|ReviewDb
name|db
init|=
name|dbProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|ChangeControl
name|control
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|IdentifiedUser
name|caller
init|=
name|control
operator|.
name|getUser
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
decl_stmt|;
name|Change
name|change
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|BinaryResult
name|bin
decl_stmt|;
try|try
init|(
name|MergeOp
name|op
init|=
name|mergeOpProvider
operator|.
name|get
argument_list|()
init|)
block|{
name|op
operator|.
name|merge
argument_list|(
name|db
argument_list|,
name|change
argument_list|,
name|caller
argument_list|,
literal|false
argument_list|,
operator|new
name|SubmitInput
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|MergeOpRepoManager
name|orm
init|=
name|op
operator|.
name|getMergeOpRepoManager
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projects
init|=
name|op
operator|.
name|getAllProjects
argument_list|()
decl_stmt|;
name|bin
operator|=
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
name|ArchiveOutputStream
name|aos
init|=
name|f
operator|.
name|createArchiveOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projects
control|)
block|{
name|OpenRepo
name|or
init|=
name|orm
operator|.
name|getRepo
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|BundleWriter
name|bw
init|=
operator|new
name|BundleWriter
argument_list|(
name|or
operator|.
name|getRepo
argument_list|()
argument_list|)
decl_stmt|;
name|bw
operator|.
name|setObjectCountCallback
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|bw
operator|.
name|setPackConfig
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ReceiveCommand
argument_list|>
name|refs
init|=
name|or
operator|.
name|getUpdate
argument_list|()
operator|.
name|getRefUpdates
argument_list|()
decl_stmt|;
for|for
control|(
name|ReceiveCommand
name|r
range|:
name|refs
control|)
block|{
name|bw
operator|.
name|include
argument_list|(
name|r
operator|.
name|getRefName
argument_list|()
argument_list|,
name|r
operator|.
name|getNewId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|r
operator|.
name|getOldId
argument_list|()
operator|.
name|equals
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
condition|)
block|{
name|bw
operator|.
name|assume
argument_list|(
name|or
operator|.
name|getCodeReviewRevWalk
argument_list|()
operator|.
name|parseCommit
argument_list|(
name|r
operator|.
name|getOldId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// This naming scheme cannot produce directory/file conflicts
comment|// as no projects contains ".git/":
name|aos
operator|.
name|putArchiveEntry
argument_list|(
name|f
operator|.
name|prepareArchiveEntry
argument_list|(
name|p
operator|.
name|get
argument_list|()
operator|+
literal|".git"
argument_list|)
argument_list|)
expr_stmt|;
name|bw
operator|.
name|writeBundle
argument_list|(
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|,
name|aos
argument_list|)
expr_stmt|;
name|aos
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
block|}
name|aos
operator|.
name|finish
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
return|return
name|bin
return|;
block|}
block|}
end_class

end_unit

