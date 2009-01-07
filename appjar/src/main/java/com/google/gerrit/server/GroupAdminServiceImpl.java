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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|admin
operator|.
name|AccountGroupDetail
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
name|admin
operator|.
name|GroupAdminService
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
name|data
operator|.
name|AccountInfoCacheFactory
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountGroupMember
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
name|client
operator|.
name|rpc
operator|.
name|BaseServiceImplementation
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
name|rpc
operator|.
name|Common
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
name|rpc
operator|.
name|NameAlreadyUsedException
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
name|rpc
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
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
name|gwtorm
operator|.
name|client
operator|.
name|Transaction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
DECL|class|GroupAdminServiceImpl
specifier|public
class|class
name|GroupAdminServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|GroupAdminService
block|{
DECL|method|ownedGroups (final AsyncCallback<List<AccountGroup>> callback)
specifier|public
name|void
name|ownedGroups
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountGroup
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|List
argument_list|<
name|AccountGroup
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|result
decl_stmt|;
if|if
condition|(
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|isAdministrator
argument_list|(
name|Common
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|myOwnedGroups
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|result
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AccountGroup
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|AccountGroup
name|a
parameter_list|,
specifier|final
name|AccountGroup
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createGroup (final String newName, final AsyncCallback<AccountGroup.Id> callback)
specifier|public
name|void
name|createGroup
parameter_list|(
specifier|final
name|String
name|newName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|()
block|{
specifier|public
name|AccountGroup
operator|.
name|Id
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|AccountGroup
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NameAlreadyUsedException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|AccountGroup
name|group
init|=
operator|new
name|AccountGroup
argument_list|(
name|nameKey
argument_list|,
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|db
operator|.
name|nextAccountGroupId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|group
operator|.
name|setNameKey
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
name|group
operator|.
name|setDescription
argument_list|(
literal|""
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroupMember
name|m
init|=
operator|new
name|AccountGroupMember
argument_list|(
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|Common
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Transaction
name|txn
init|=
name|db
operator|.
name|beginTransaction
argument_list|()
decl_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|group
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|txn
operator|.
name|commit
argument_list|()
expr_stmt|;
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|notifyGroupAdd
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|group
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|groupDetail (final AccountGroup.Id groupId, final AsyncCallback<AccountGroupDetail> callback)
specifier|public
name|void
name|groupDetail
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|AccountGroupDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|AccountGroupDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|AccountGroupDetail
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
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
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|AccountGroupDetail
name|d
init|=
operator|new
name|AccountGroupDetail
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|auto
init|=
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|isAutoGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|d
operator|.
name|load
argument_list|(
name|db
argument_list|,
operator|new
name|AccountInfoCacheFactory
argument_list|(
name|db
argument_list|)
argument_list|,
name|group
argument_list|,
name|auto
argument_list|)
expr_stmt|;
return|return
name|d
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|changeGroupDescription (final AccountGroup.Id groupId, final String description, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|changeGroupDescription
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|String
name|description
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|VoidResult
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
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
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
name|group
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|changeGroupOwner (final AccountGroup.Id groupId, final String newOwnerName, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|changeGroupOwner
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|String
name|newOwnerName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|VoidResult
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
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
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|AccountGroup
name|owner
init|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|newOwnerName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|owner
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
name|group
operator|.
name|setOwnerGroupId
argument_list|(
name|owner
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|renameGroup (final AccountGroup.Id groupId, final String newName, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|renameGroup
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|String
name|newName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|VoidResult
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
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
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|AccountGroup
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|nameKey
operator|.
name|equals
argument_list|(
name|group
operator|.
name|getNameKey
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NameAlreadyUsedException
argument_list|()
argument_list|)
throw|;
block|}
name|group
operator|.
name|setNameKey
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|addGroupMember (final AccountGroup.Id groupId, final String nameOrEmail, final AsyncCallback<AccountGroupDetail> callback)
specifier|public
name|void
name|addGroupMember
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|String
name|nameOrEmail
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|AccountGroupDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|AccountGroupDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|AccountGroupDetail
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
if|if
condition|(
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|isAutoGroup
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NameAlreadyUsedException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|Account
name|a
init|=
name|findAccount
argument_list|(
name|db
argument_list|,
name|nameOrEmail
argument_list|)
decl_stmt|;
specifier|final
name|AccountGroupMember
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|a
operator|.
name|getId
argument_list|()
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|AccountGroupDetail
argument_list|()
return|;
block|}
specifier|final
name|AccountGroupMember
name|m
init|=
operator|new
name|AccountGroupMember
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|notifyGroupAdd
argument_list|(
name|m
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroupDetail
name|d
init|=
operator|new
name|AccountGroupDetail
argument_list|()
decl_stmt|;
name|d
operator|.
name|loadOneMember
argument_list|(
name|db
argument_list|,
name|a
argument_list|,
name|m
argument_list|)
expr_stmt|;
return|return
name|d
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteGroupMembers (final Set<AccountGroupMember.Key> keys, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|deleteGroupMembers
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountGroupMember
operator|.
name|Key
argument_list|>
name|keys
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|VoidResult
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|owned
init|=
name|ids
argument_list|(
name|myOwnedGroups
argument_list|(
name|db
argument_list|)
argument_list|)
decl_stmt|;
name|Boolean
name|amAdmin
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroupMember
operator|.
name|Key
name|k
range|:
name|keys
control|)
block|{
if|if
condition|(
operator|!
name|owned
operator|.
name|contains
argument_list|(
name|k
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|amAdmin
operator|==
literal|null
condition|)
block|{
name|amAdmin
operator|=
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|isAdministrator
argument_list|(
name|Common
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|amAdmin
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
for|for
control|(
specifier|final
name|AccountGroupMember
operator|.
name|Key
name|k
range|:
name|keys
control|)
block|{
specifier|final
name|AccountGroupMember
name|m
init|=
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|delete
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|notifyGroupDelete
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|assertAmGroupOwner (final ReviewDb db, final AccountGroup.Id groupId)
specifier|private
name|void
name|assertAmGroupOwner
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
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
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|Common
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|isInGroup
argument_list|(
name|me
argument_list|,
name|group
operator|.
name|getOwnerGroupId
argument_list|()
argument_list|)
operator|&&
operator|!
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|isAdministrator
argument_list|(
name|me
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|ids ( final Collection<AccountGroup> groupList)
specifier|private
specifier|static
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|ids
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|AccountGroup
argument_list|>
name|groupList
parameter_list|)
block|{
specifier|final
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|r
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
name|group
range|:
name|groupList
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|myOwnedGroups (final ReviewDb db)
specifier|private
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|myOwnedGroups
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|Common
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|own
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroup
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
range|:
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|getGroups
argument_list|(
name|me
argument_list|)
control|)
block|{
for|for
control|(
specifier|final
name|AccountGroup
name|g
range|:
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|ownedByGroup
argument_list|(
name|groupId
argument_list|)
control|)
block|{
name|own
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|own
return|;
block|}
DECL|method|findAccount (final ReviewDb db, final String nameOrEmail)
specifier|private
specifier|static
name|Account
name|findAccount
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|String
name|nameOrEmail
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|Account
name|r
init|=
name|Account
operator|.
name|find
argument_list|(
name|db
argument_list|,
name|nameOrEmail
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

