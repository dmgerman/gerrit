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
name|ChangeHookRunner
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
name|ApprovalsUtil
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
name|ReplicationQueue
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
name|server
operator|.
name|mail
operator|.
name|RebasedPatchSetSender
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
name|PatchSetInfoFactory
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
name|lib
operator|.
name|PersonIdent
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

begin_class
DECL|class|RebaseChange
class|class
name|RebaseChange
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
DECL|method|create (PatchSet.Id patchSetId)
name|RebaseChange
name|create
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
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
DECL|field|rebasedPatchSetSenderFactory
specifier|private
specifier|final
name|RebasedPatchSetSender
operator|.
name|Factory
name|rebasedPatchSetSenderFactory
decl_stmt|;
DECL|field|changeDetailFactory
specifier|private
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
decl_stmt|;
DECL|field|replication
specifier|private
specifier|final
name|ReplicationQueue
name|replication
decl_stmt|;
DECL|field|patchSetId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHookRunner
name|hooks
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|myIdent
specifier|private
specifier|final
name|PersonIdent
name|myIdent
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|RebaseChange (final ChangeControl.Factory changeControlFactory, final ReviewDb db, final IdentifiedUser currentUser, final RebasedPatchSetSender.Factory rebasedPatchSetSenderFactory, final ChangeDetailFactory.Factory changeDetailFactory, @Assisted final PatchSet.Id patchSetId, final ChangeHookRunner hooks, final GitRepositoryManager gitManager, final PatchSetInfoFactory patchSetInfoFactory, final ReplicationQueue replication, @GerritPersonIdent final PersonIdent myIdent, final ApprovalsUtil approvalsUtil)
name|RebaseChange
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
name|RebasedPatchSetSender
operator|.
name|Factory
name|rebasedPatchSetSenderFactory
parameter_list|,
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
specifier|final
name|ChangeHookRunner
name|hooks
parameter_list|,
specifier|final
name|GitRepositoryManager
name|gitManager
parameter_list|,
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
specifier|final
name|ReplicationQueue
name|replication
parameter_list|,
annotation|@
name|GerritPersonIdent
specifier|final
name|PersonIdent
name|myIdent
parameter_list|,
specifier|final
name|ApprovalsUtil
name|approvalsUtil
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
name|rebasedPatchSetSenderFactory
operator|=
name|rebasedPatchSetSenderFactory
expr_stmt|;
name|this
operator|.
name|changeDetailFactory
operator|=
name|changeDetailFactory
expr_stmt|;
name|this
operator|.
name|patchSetId
operator|=
name|patchSetId
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|replication
operator|=
name|replication
expr_stmt|;
name|this
operator|.
name|myIdent
operator|=
name|myIdent
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
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
block|{
name|ChangeUtil
operator|.
name|rebaseChange
argument_list|(
name|patchSetId
argument_list|,
name|currentUser
argument_list|,
name|db
argument_list|,
name|rebasedPatchSetSenderFactory
argument_list|,
name|hooks
argument_list|,
name|gitManager
argument_list|,
name|patchSetInfoFactory
argument_list|,
name|replication
argument_list|,
name|myIdent
argument_list|,
name|changeControlFactory
argument_list|,
name|approvalsUtil
argument_list|)
expr_stmt|;
return|return
name|changeDetailFactory
operator|.
name|create
argument_list|(
name|patchSetId
operator|.
name|getParentKey
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
return|;
block|}
block|}
end_class

end_unit

