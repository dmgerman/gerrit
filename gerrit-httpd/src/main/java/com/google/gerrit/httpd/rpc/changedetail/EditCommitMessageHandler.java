begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.changedetail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|changedetail
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
name|common
operator|.
name|data
operator|.
name|ChangeDetail
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
name|errors
operator|.
name|EmailException
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
name|errors
operator|.
name|NoSuchEntityException
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|ChangeUtil
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
name|GerritPersonIdent
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
name|change
operator|.
name|PatchSetInserter
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
name|git
operator|.
name|validators
operator|.
name|CommitValidators
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
name|mail
operator|.
name|CommitMessageEditedSender
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
name|patch
operator|.
name|PatchSetInfoNotAvailableException
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
name|InvalidChangeOperationException
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
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|NoSuchProjectException
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
name|ssh
operator|.
name|NoSshInfo
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
name|assistedinject
operator|.
name|Assisted
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
name|IncorrectObjectTypeException
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
name|MissingObjectException
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_class
DECL|class|EditCommitMessageHandler
class|class
name|EditCommitMessageHandler
extends|extends
name|Handler
argument_list|<
name|ChangeDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (PatchSet.Id patchSetId, String message)
name|EditCommitMessageHandler
name|create
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|String
name|message
parameter_list|)
function_decl|;
block|}
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|IdentifiedUser
name|currentUser
decl_stmt|;
DECL|field|changeDetailFactory
specifier|private
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
decl_stmt|;
DECL|field|commitMessageEditedSenderFactory
specifier|private
specifier|final
name|CommitMessageEditedSender
operator|.
name|Factory
name|commitMessageEditedSenderFactory
decl_stmt|;
DECL|field|patchSetId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
annotation|@
name|Nullable
DECL|field|message
specifier|private
specifier|final
name|String
name|message
decl_stmt|;
DECL|field|commitValidatorsFactory
specifier|private
specifier|final
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|myIdent
specifier|private
specifier|final
name|PersonIdent
name|myIdent
decl_stmt|;
DECL|field|patchSetInserterFactory
specifier|private
specifier|final
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|EditCommitMessageHandler (final ChangeControl.Factory changeControlFactory, final ReviewDb db, final IdentifiedUser currentUser, final ChangeDetailFactory.Factory changeDetailFactory, final CommitMessageEditedSender.Factory commitMessageEditedSenderFactory, @Assisted final PatchSet.Id patchSetId, @Assisted @Nullable final String message, final CommitValidators.Factory commitValidatorsFactory, final GitRepositoryManager gitManager, @GerritPersonIdent final PersonIdent myIdent, final PatchSetInserter.Factory patchSetInserterFactory)
name|EditCommitMessageHandler
parameter_list|(
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|IdentifiedUser
name|currentUser
parameter_list|,
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
parameter_list|,
specifier|final
name|CommitMessageEditedSender
operator|.
name|Factory
name|commitMessageEditedSenderFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
specifier|final
name|String
name|message
parameter_list|,
specifier|final
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
parameter_list|,
specifier|final
name|GitRepositoryManager
name|gitManager
parameter_list|,
annotation|@
name|GerritPersonIdent
specifier|final
name|PersonIdent
name|myIdent
parameter_list|,
specifier|final
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
parameter_list|)
block|{
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
name|this
operator|.
name|changeDetailFactory
operator|=
name|changeDetailFactory
expr_stmt|;
name|this
operator|.
name|commitMessageEditedSenderFactory
operator|=
name|commitMessageEditedSenderFactory
expr_stmt|;
name|this
operator|.
name|patchSetId
operator|=
name|patchSetId
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|commitValidatorsFactory
operator|=
name|commitValidatorsFactory
expr_stmt|;
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
name|this
operator|.
name|myIdent
operator|=
name|myIdent
expr_stmt|;
name|this
operator|.
name|patchSetInserterFactory
operator|=
name|patchSetInserterFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ChangeDetail
name|call
parameter_list|()
throws|throws
name|NoSuchChangeException
throws|,
name|OrmException
throws|,
name|EmailException
throws|,
name|NoSuchEntityException
throws|,
name|PatchSetInfoNotAvailableException
throws|,
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
throws|,
name|InvalidChangeOperationException
throws|,
name|NoSuchProjectException
block|{
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
specifier|final
name|ChangeControl
name|control
init|=
name|changeControlFactory
operator|.
name|validateFor
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|canAddPatchSet
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|InvalidChangeOperationException
argument_list|(
literal|"Not allowed to add new Patch Sets to: "
operator|+
name|changeId
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|Repository
name|git
decl_stmt|;
try|try
block|{
name|git
operator|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|changeId
argument_list|)
operator|.
name|getProject
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
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|changeId
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|CommitValidators
name|commitValidators
init|=
name|commitValidatorsFactory
operator|.
name|create
argument_list|(
name|control
operator|.
name|getRefControl
argument_list|()
argument_list|,
operator|new
name|NoSshInfo
argument_list|()
argument_list|,
name|git
argument_list|)
decl_stmt|;
name|ChangeUtil
operator|.
name|editCommitMessage
argument_list|(
name|patchSetId
argument_list|,
name|control
operator|.
name|getRefControl
argument_list|()
argument_list|,
name|commitValidators
argument_list|,
name|currentUser
argument_list|,
name|message
argument_list|,
name|db
argument_list|,
name|commitMessageEditedSenderFactory
argument_list|,
name|git
argument_list|,
name|myIdent
argument_list|,
name|patchSetInserterFactory
argument_list|)
expr_stmt|;
return|return
name|changeDetailFactory
operator|.
name|create
argument_list|(
name|changeId
argument_list|)
operator|.
name|call
argument_list|()
return|;
block|}
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

