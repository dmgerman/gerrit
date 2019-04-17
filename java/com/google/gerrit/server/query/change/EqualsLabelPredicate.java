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
DECL|package|com.google.gerrit.server.query.change
package|package
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
name|LabelType
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
name|LabelTypes
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
name|index
operator|.
name|change
operator|.
name|ChangeField
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
name|project
operator|.
name|ProjectCache
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
name|ProjectState
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
DECL|class|EqualsLabelPredicate
specifier|public
class|class
name|EqualsLabelPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|field|projectCache
specifier|protected
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|permissionBackend
specifier|protected
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|userFactory
specifier|protected
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|label
specifier|protected
specifier|final
name|String
name|label
decl_stmt|;
DECL|field|expVal
specifier|protected
specifier|final
name|int
name|expVal
decl_stmt|;
DECL|field|account
specifier|protected
specifier|final
name|Account
operator|.
name|Id
name|account
decl_stmt|;
DECL|field|group
specifier|protected
specifier|final
name|AccountGroup
operator|.
name|UUID
name|group
decl_stmt|;
DECL|method|EqualsLabelPredicate ( LabelPredicate.Args args, String label, int expVal, Account.Id account)
specifier|public
name|EqualsLabelPredicate
parameter_list|(
name|LabelPredicate
operator|.
name|Args
name|args
parameter_list|,
name|String
name|label
parameter_list|,
name|int
name|expVal
parameter_list|,
name|Account
operator|.
name|Id
name|account
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|LABEL
argument_list|,
name|ChangeField
operator|.
name|formatLabel
argument_list|(
name|label
argument_list|,
name|expVal
argument_list|,
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|args
operator|.
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|args
operator|.
name|projectCache
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|args
operator|.
name|userFactory
expr_stmt|;
name|this
operator|.
name|group
operator|=
name|args
operator|.
name|group
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
name|this
operator|.
name|expVal
operator|=
name|expVal
expr_stmt|;
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|object
parameter_list|)
block|{
name|Change
name|c
init|=
name|object
operator|.
name|change
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
comment|// The change has disappeared.
comment|//
return|return
literal|false
return|;
block|}
name|ProjectState
name|project
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|c
operator|.
name|getDest
argument_list|()
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
comment|// The project has disappeared.
comment|//
return|return
literal|false
return|;
block|}
name|LabelType
name|labelType
init|=
name|type
argument_list|(
name|project
operator|.
name|getLabelTypes
argument_list|()
argument_list|,
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
name|labelType
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
comment|// Label is not defined by this project.
block|}
name|boolean
name|hasVote
init|=
literal|false
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|p
range|:
name|object
operator|.
name|currentApprovals
argument_list|()
control|)
block|{
if|if
condition|(
name|labelType
operator|.
name|matches
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|hasVote
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|match
argument_list|(
name|object
argument_list|,
name|p
operator|.
name|getValue
argument_list|()
argument_list|,
name|p
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|hasVote
operator|&&
name|expVal
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|type (LabelTypes types, String toFind)
specifier|protected
specifier|static
name|LabelType
name|type
parameter_list|(
name|LabelTypes
name|types
parameter_list|,
name|String
name|toFind
parameter_list|)
block|{
if|if
condition|(
name|types
operator|.
name|byLabel
argument_list|(
name|toFind
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|types
operator|.
name|byLabel
argument_list|(
name|toFind
argument_list|)
return|;
block|}
for|for
control|(
name|LabelType
name|lt
range|:
name|types
operator|.
name|getLabelTypes
argument_list|()
control|)
block|{
if|if
condition|(
name|toFind
operator|.
name|equalsIgnoreCase
argument_list|(
name|lt
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|lt
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|match (ChangeData cd, short value, Account.Id approver)
specifier|protected
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|short
name|value
parameter_list|,
name|Account
operator|.
name|Id
name|approver
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
name|expVal
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|account
operator|!=
literal|null
operator|&&
operator|!
name|account
operator|.
name|equals
argument_list|(
name|approver
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|IdentifiedUser
name|reviewer
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|approver
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
operator|&&
operator|!
name|reviewer
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|contains
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Check the user has 'READ' permission.
try|try
block|{
name|PermissionBackend
operator|.
name|ForChange
name|perm
init|=
name|permissionBackend
operator|.
name|absentUser
argument_list|(
name|approver
argument_list|)
operator|.
name|change
argument_list|(
name|cd
argument_list|)
decl_stmt|;
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|cd
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
operator|||
operator|!
name|projectState
operator|.
name|statePermitsRead
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|perm
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
decl||
name|IOException
decl||
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
operator|+
operator|(
name|group
operator|==
literal|null
condition|?
literal|0
else|:
literal|1
operator|)
return|;
block|}
block|}
end_class

end_unit

