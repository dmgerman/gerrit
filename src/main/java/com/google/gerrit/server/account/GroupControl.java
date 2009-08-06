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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountGroup
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

begin_comment
comment|/** Access control management for a group of accounts managed in Gerrit. */
end_comment

begin_class
DECL|class|GroupControl
specifier|public
class|class
name|GroupControl
block|{
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
block|{
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|Factory (final GroupCache gc, final Provider<CurrentUser> cu)
name|Factory
parameter_list|(
specifier|final
name|GroupCache
name|gc
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|cu
parameter_list|)
block|{
name|groupCache
operator|=
name|gc
expr_stmt|;
name|user
operator|=
name|cu
expr_stmt|;
block|}
DECL|method|controlFor (final AccountGroup.Id groupId)
specifier|public
name|GroupControl
name|controlFor
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|NoSuchGroupException
block|{
specifier|final
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupId
argument_list|)
throw|;
block|}
return|return
operator|new
name|GroupControl
argument_list|(
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|group
argument_list|)
return|;
block|}
DECL|method|validateFor (final AccountGroup.Id groupId)
specifier|public
name|GroupControl
name|validateFor
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|NoSuchGroupException
block|{
specifier|final
name|GroupControl
name|c
init|=
name|controlFor
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|c
operator|.
name|isVisible
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupId
argument_list|)
throw|;
block|}
return|return
name|c
return|;
block|}
block|}
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|group
specifier|private
specifier|final
name|AccountGroup
name|group
decl_stmt|;
DECL|method|GroupControl (final CurrentUser who, final AccountGroup gc)
name|GroupControl
parameter_list|(
specifier|final
name|CurrentUser
name|who
parameter_list|,
specifier|final
name|AccountGroup
name|gc
parameter_list|)
block|{
name|user
operator|=
name|who
expr_stmt|;
name|group
operator|=
name|gc
expr_stmt|;
block|}
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getAccountGroup ()
specifier|public
name|AccountGroup
name|getAccountGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
comment|/** Can this user see this group exists? */
DECL|method|isVisible ()
specifier|public
name|boolean
name|isVisible
parameter_list|()
block|{
return|return
name|isOwner
argument_list|()
return|;
block|}
DECL|method|isOwner ()
specifier|public
name|boolean
name|isOwner
parameter_list|()
block|{
specifier|final
name|AccountGroup
operator|.
name|Id
name|owner
init|=
name|group
operator|.
name|getOwnerGroupId
argument_list|()
decl_stmt|;
return|return
name|getCurrentUser
argument_list|()
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|contains
argument_list|(
name|owner
argument_list|)
operator|||
name|getCurrentUser
argument_list|()
operator|.
name|isAdministrator
argument_list|()
return|;
block|}
DECL|method|canAdd (final Account.Id id)
specifier|public
name|boolean
name|canAdd
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|isOwner
argument_list|()
return|;
block|}
DECL|method|canRemove (final Account.Id id)
specifier|public
name|boolean
name|canRemove
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|isOwner
argument_list|()
return|;
block|}
DECL|method|canSee (Account.Id id)
specifier|public
name|boolean
name|canSee
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|isOwner
argument_list|()
return|;
block|}
block|}
end_class

end_unit

