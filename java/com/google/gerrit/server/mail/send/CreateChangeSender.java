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
DECL|package|com.google.gerrit.server.mail.send
package|package
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
name|send
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
name|flogger
operator|.
name|FluentLogger
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|Account
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
name|server
operator|.
name|account
operator|.
name|ProjectWatches
operator|.
name|NotifyType
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
name|send
operator|.
name|ProjectWatch
operator|.
name|Watchers
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|RefPermission
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|StreamSupport
import|;
end_import

begin_comment
comment|/** Notify interested parties of a brand new change. */
end_comment

begin_class
DECL|class|CreateChangeSender
specifier|public
class|class
name|CreateChangeSender
extends|extends
name|NewChangeSender
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Project.NameKey project, Change.Id id)
name|CreateChangeSender
name|create
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
block|}
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateChangeSender ( EmailArguments ea, PermissionBackend permissionBackend, @Assisted Project.NameKey project, @Assisted Change.Id id)
specifier|public
name|CreateChangeSender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
annotation|@
name|Assisted
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
name|super
argument_list|(
name|ea
argument_list|,
name|newChangeData
argument_list|(
name|ea
argument_list|,
name|project
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
try|try
block|{
comment|// Upgrade watching owners from CC and BCC to TO.
name|Watchers
name|matching
init|=
name|getWatchers
argument_list|(
name|NotifyType
operator|.
name|NEW_CHANGES
argument_list|,
operator|!
name|change
operator|.
name|isWorkInProgress
argument_list|()
operator|&&
operator|!
name|change
operator|.
name|isPrivate
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO(hiesel): Remove special handling for owners
name|StreamSupport
operator|.
name|stream
argument_list|(
name|matching
operator|.
name|all
argument_list|()
operator|.
name|accounts
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|filter
argument_list|(
name|this
operator|::
name|isOwnerOfProjectOrBranch
argument_list|)
operator|.
name|forEach
argument_list|(
name|acc
lambda|->
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|acc
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add everyone else. Owners added above will not be duplicated.
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|matching
operator|.
name|to
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|matching
operator|.
name|cc
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|BCC
argument_list|,
name|matching
operator|.
name|bcc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
comment|// Just don't CC everyone. Better to send a partial message to those
comment|// we already have queued up then to fail deliver entirely to people
comment|// who have a lower interest in the change.
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot notify watchers for new change"
argument_list|)
expr_stmt|;
block|}
name|includeWatchers
argument_list|(
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|,
operator|!
name|change
operator|.
name|isWorkInProgress
argument_list|()
operator|&&
operator|!
name|change
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|isOwnerOfProjectOrBranch (Account.Id userId)
specifier|private
name|boolean
name|isOwnerOfProjectOrBranch
parameter_list|(
name|Account
operator|.
name|Id
name|userId
parameter_list|)
block|{
return|return
name|permissionBackend
operator|.
name|absentUser
argument_list|(
name|userId
argument_list|)
operator|.
name|ref
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
operator|.
name|testOrFalse
argument_list|(
name|RefPermission
operator|.
name|WRITE_CONFIG
argument_list|)
return|;
block|}
block|}
end_class

end_unit

