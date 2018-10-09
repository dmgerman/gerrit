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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|PatchSetApproval
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
name|CurrentUser
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
name|notedb
operator|.
name|ChangeNotes
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
name|ChangePermission
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
name|PermissionBackendException
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|RemoveReviewerControl
specifier|public
class|class
name|RemoveReviewerControl
block|{
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|RemoveReviewerControl ( PermissionBackend permissionBackend, Provider<ReviewDb> dbProvider, ProjectControl.GenericFactory projectControlFactory)
name|RemoveReviewerControl
parameter_list|(
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
parameter_list|)
block|{
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
block|}
comment|/**    * Checks if removing the given reviewer and patch set approval is OK.    *    * @throws AuthException if this user is not allowed to remove this approval.    */
DECL|method|checkRemoveReviewer ( ChangeNotes notes, CurrentUser currentUser, PatchSetApproval approval)
specifier|public
name|void
name|checkRemoveReviewer
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|currentUser
parameter_list|,
name|PatchSetApproval
name|approval
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|AuthException
throws|,
name|NoSuchProjectException
throws|,
name|IOException
block|{
name|checkRemoveReviewer
argument_list|(
name|notes
argument_list|,
name|currentUser
argument_list|,
name|approval
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|approval
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Checks if removing the given reviewer is OK. Does not check if removing any approvals the    * reviewer might have given is OK.    *    * @throws AuthException if this user is not allowed to remove this approval.    */
DECL|method|checkRemoveReviewer (ChangeNotes notes, CurrentUser currentUser, Account.Id reviewer)
specifier|public
name|void
name|checkRemoveReviewer
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|currentUser
parameter_list|,
name|Account
operator|.
name|Id
name|reviewer
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|AuthException
throws|,
name|NoSuchProjectException
throws|,
name|IOException
block|{
name|checkRemoveReviewer
argument_list|(
name|notes
argument_list|,
name|currentUser
argument_list|,
name|reviewer
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/** @return true if the user is allowed to remove this reviewer. */
DECL|method|testRemoveReviewer ( ChangeData cd, CurrentUser currentUser, Account.Id reviewer, int value)
specifier|public
name|boolean
name|testRemoveReviewer
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|CurrentUser
name|currentUser
parameter_list|,
name|Account
operator|.
name|Id
name|reviewer
parameter_list|,
name|int
name|value
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|NoSuchProjectException
throws|,
name|OrmException
throws|,
name|IOException
block|{
if|if
condition|(
name|canRemoveReviewerWithoutPermissionCheck
argument_list|(
name|cd
operator|.
name|change
argument_list|()
argument_list|,
name|currentUser
argument_list|,
name|reviewer
argument_list|,
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|permissionBackend
operator|.
name|user
argument_list|(
name|currentUser
argument_list|)
operator|.
name|change
argument_list|(
name|cd
argument_list|)
operator|.
name|database
argument_list|(
name|dbProvider
argument_list|)
operator|.
name|test
argument_list|(
name|ChangePermission
operator|.
name|REMOVE_REVIEWER
argument_list|)
return|;
block|}
DECL|method|checkRemoveReviewer ( ChangeNotes notes, CurrentUser currentUser, Account.Id reviewer, int val)
specifier|private
name|void
name|checkRemoveReviewer
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|currentUser
parameter_list|,
name|Account
operator|.
name|Id
name|reviewer
parameter_list|,
name|int
name|val
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|NoSuchProjectException
throws|,
name|AuthException
throws|,
name|IOException
block|{
if|if
condition|(
name|canRemoveReviewerWithoutPermissionCheck
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
argument_list|,
name|currentUser
argument_list|,
name|reviewer
argument_list|,
name|val
argument_list|)
condition|)
block|{
return|return;
block|}
name|permissionBackend
operator|.
name|user
argument_list|(
name|currentUser
argument_list|)
operator|.
name|change
argument_list|(
name|notes
argument_list|)
operator|.
name|database
argument_list|(
name|dbProvider
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|REMOVE_REVIEWER
argument_list|)
expr_stmt|;
block|}
DECL|method|canRemoveReviewerWithoutPermissionCheck ( Change change, CurrentUser currentUser, Account.Id reviewer, int value)
specifier|private
name|boolean
name|canRemoveReviewerWithoutPermissionCheck
parameter_list|(
name|Change
name|change
parameter_list|,
name|CurrentUser
name|currentUser
parameter_list|,
name|Account
operator|.
name|Id
name|reviewer
parameter_list|,
name|int
name|value
parameter_list|)
throws|throws
name|NoSuchProjectException
throws|,
name|IOException
block|{
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|equals
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|MERGED
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|currentUser
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|Account
operator|.
name|Id
name|aId
init|=
name|currentUser
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
if|if
condition|(
name|aId
operator|.
name|equals
argument_list|(
name|reviewer
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
comment|// A user can always remove themselves.
block|}
elseif|else
if|if
condition|(
name|aId
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
operator|&&
literal|0
operator|<=
name|value
condition|)
block|{
return|return
literal|true
return|;
comment|// The change owner may remove any zero or positive score.
block|}
block|}
comment|// Users with the remove reviewer permission, the branch owner, project
comment|// owner and site admin can remove anyone
name|ProjectControl
name|ctl
init|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|currentUser
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctl
operator|.
name|controlForRef
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
operator|.
name|isOwner
argument_list|()
comment|// branch owner
operator|||
name|ctl
operator|.
name|isOwner
argument_list|()
comment|// project owner
operator|||
name|ctl
operator|.
name|isAdmin
argument_list|()
condition|)
block|{
comment|// project admin
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

