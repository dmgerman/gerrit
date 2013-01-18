begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.account
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
name|common
operator|.
name|data
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
name|common
operator|.
name|data
operator|.
name|GroupDetail
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
name|GroupOptions
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
name|GroupReference
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
name|common
operator|.
name|errors
operator|.
name|NoSuchGroupException
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
name|AccountGroupIncludeByUuid
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
name|AccountGroupIncludeByUuidAudit
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
name|account
operator|.
name|GroupBackend
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
name|GroupBackends
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
name|GroupCache
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
name|GroupControl
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
name|GroupIncludeCache
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
name|common
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
name|common
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
name|HashSet
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
class|class
name|GroupAdminServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|GroupAdminService
block|{
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|groupIncludeCache
specifier|private
specifier|final
name|GroupIncludeCache
name|groupIncludeCache
decl_stmt|;
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|createGroupFactory
specifier|private
specifier|final
name|CreateGroup
operator|.
name|Factory
name|createGroupFactory
decl_stmt|;
DECL|field|renameGroupFactory
specifier|private
specifier|final
name|RenameGroup
operator|.
name|Factory
name|renameGroupFactory
decl_stmt|;
DECL|field|groupDetailFactory
specifier|private
specifier|final
name|GroupDetailHandler
operator|.
name|Factory
name|groupDetailFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupAdminServiceImpl (final Provider<ReviewDb> schema, final Provider<IdentifiedUser> currentUser, final GroupIncludeCache groupIncludeCache, final GroupCache groupCache, final GroupBackend groupBackend, final GroupControl.Factory groupControlFactory, final CreateGroup.Factory createGroupFactory, final RenameGroup.Factory renameGroupFactory, final GroupDetailHandler.Factory groupDetailFactory)
name|GroupAdminServiceImpl
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|currentUser
parameter_list|,
specifier|final
name|GroupIncludeCache
name|groupIncludeCache
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|GroupBackend
name|groupBackend
parameter_list|,
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
specifier|final
name|CreateGroup
operator|.
name|Factory
name|createGroupFactory
parameter_list|,
specifier|final
name|RenameGroup
operator|.
name|Factory
name|renameGroupFactory
parameter_list|,
specifier|final
name|GroupDetailHandler
operator|.
name|Factory
name|groupDetailFactory
parameter_list|)
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|currentUser
argument_list|)
expr_stmt|;
name|this
operator|.
name|groupIncludeCache
operator|=
name|groupIncludeCache
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|createGroupFactory
operator|=
name|createGroupFactory
expr_stmt|;
name|this
operator|.
name|renameGroupFactory
operator|=
name|renameGroupFactory
expr_stmt|;
name|this
operator|.
name|groupDetailFactory
operator|=
name|groupDetailFactory
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
name|createGroupFactory
operator|.
name|create
argument_list|(
name|newName
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|groupDetail (AccountGroup.Id groupId, AccountGroup.UUID groupUUID, AsyncCallback<GroupDetail> callback)
specifier|public
name|void
name|groupDetail
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|,
name|AsyncCallback
argument_list|<
name|GroupDetail
argument_list|>
name|callback
parameter_list|)
block|{
if|if
condition|(
name|groupId
operator|==
literal|null
operator|&&
name|groupUUID
operator|!=
literal|null
condition|)
block|{
name|AccountGroup
name|g
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupUUID
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|!=
literal|null
condition|)
block|{
name|groupId
operator|=
name|g
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
block|}
name|groupDetailFactory
operator|.
name|create
argument_list|(
name|groupId
argument_list|)
operator|.
name|to
argument_list|(
name|callback
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
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|group
argument_list|)
expr_stmt|;
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
name|groupCache
operator|.
name|evict
argument_list|(
name|group
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
DECL|method|changeGroupOptions (final AccountGroup.Id groupId, final GroupOptions groupOptions, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|changeGroupOptions
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|GroupOptions
name|groupOptions
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
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|group
operator|.
name|setVisibleToAll
argument_list|(
name|groupOptions
operator|.
name|isVisibleToAll
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
name|groupCache
operator|.
name|evict
argument_list|(
name|group
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
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|GroupReference
name|owner
init|=
name|GroupBackends
operator|.
name|findExactSuggestion
argument_list|(
name|groupBackend
argument_list|,
name|newOwnerName
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
name|setOwnerGroupUUID
argument_list|(
name|owner
operator|.
name|getUUID
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
name|groupCache
operator|.
name|evict
argument_list|(
name|group
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
DECL|method|renameGroup (final AccountGroup.Id groupId, final String newName, final AsyncCallback<GroupDetail> callback)
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
name|GroupDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|renameGroupFactory
operator|.
name|create
argument_list|(
name|groupId
argument_list|,
name|newName
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|changeGroupType (final AccountGroup.Id groupId, final AccountGroup.Type newType, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|changeGroupType
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Type
name|newType
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
name|assertAmGroupOwner
argument_list|(
name|db
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|group
operator|.
name|setType
argument_list|(
name|newType
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
name|groupCache
operator|.
name|evict
argument_list|(
name|group
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
DECL|method|addGroupInclude (final AccountGroup.Id groupId, final AccountGroup.UUID incGroupUUID, final String incGroupName, final AsyncCallback<GroupDetail> callback)
specifier|public
name|void
name|addGroupInclude
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|UUID
name|incGroupUUID
parameter_list|,
specifier|final
name|String
name|incGroupName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|GroupDetail
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
name|GroupDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|GroupDetail
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
throws|,
name|NoSuchGroupException
block|{
specifier|final
name|GroupControl
name|control
init|=
name|groupControlFactory
operator|.
name|validateFor
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupCache
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
operator|.
name|getType
argument_list|()
operator|!=
name|AccountGroup
operator|.
name|Type
operator|.
name|INTERNAL
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
if|if
condition|(
name|incGroupUUID
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchGroupException
argument_list|(
name|incGroupName
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|control
operator|.
name|canAddGroup
argument_list|(
name|incGroupUUID
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
specifier|final
name|AccountGroupIncludeByUuid
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupIncludeByUuid
operator|.
name|Key
argument_list|(
name|groupId
argument_list|,
name|incGroupUUID
argument_list|)
decl_stmt|;
name|AccountGroupIncludeByUuid
name|m
init|=
name|db
operator|.
name|accountGroupIncludesByUuid
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
operator|new
name|AccountGroupIncludeByUuid
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupIncludesByUuidAudit
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|AccountGroupIncludeByUuidAudit
argument_list|(
name|m
argument_list|,
name|getAccountId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupIncludesByUuid
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
name|groupIncludeCache
operator|.
name|evictInclude
argument_list|(
name|incGroupUUID
argument_list|)
expr_stmt|;
block|}
return|return
name|groupDetailFactory
operator|.
name|create
argument_list|(
name|groupId
argument_list|)
operator|.
name|call
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteGroupIncludes (final AccountGroup.Id groupId, final Set<AccountGroupIncludeByUuid.Key> keys, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|deleteGroupIncludes
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
specifier|final
name|Set
argument_list|<
name|AccountGroupIncludeByUuid
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
name|NoSuchGroupException
throws|,
name|Failure
block|{
specifier|final
name|GroupControl
name|control
init|=
name|groupControlFactory
operator|.
name|validateFor
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupCache
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
operator|.
name|getType
argument_list|()
operator|!=
name|AccountGroup
operator|.
name|Type
operator|.
name|INTERNAL
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
for|for
control|(
specifier|final
name|AccountGroupIncludeByUuid
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
name|groupId
operator|.
name|equals
argument_list|(
name|k
operator|.
name|getGroupId
argument_list|()
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
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupsToEvict
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroupIncludeByUuid
operator|.
name|Key
name|k
range|:
name|keys
control|)
block|{
specifier|final
name|AccountGroupIncludeByUuid
name|m
init|=
name|db
operator|.
name|accountGroupIncludesByUuid
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
if|if
condition|(
operator|!
name|control
operator|.
name|canRemoveGroup
argument_list|(
name|m
operator|.
name|getIncludeUUID
argument_list|()
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
name|AccountGroupIncludeByUuidAudit
name|audit
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AccountGroupIncludeByUuidAudit
name|a
range|:
name|db
operator|.
name|accountGroupIncludesByUuidAudit
argument_list|()
operator|.
name|byGroupInclude
argument_list|(
name|m
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|m
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|a
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|audit
operator|=
name|a
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|audit
operator|!=
literal|null
condition|)
block|{
name|audit
operator|.
name|removed
argument_list|(
name|me
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupIncludesByUuidAudit
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|audit
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountGroupIncludesByUuid
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
name|groupsToEvict
operator|.
name|add
argument_list|(
name|k
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|uuid
range|:
name|groupsToEvict
control|)
block|{
name|groupIncludeCache
operator|.
name|evictInclude
argument_list|(
name|uuid
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
DECL|method|assertAmGroupOwner (final ReviewDb db, final AccountGroup group)
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
name|group
parameter_list|)
throws|throws
name|Failure
block|{
try|try
block|{
if|if
condition|(
operator|!
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isOwner
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchGroupException
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchGroupException
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

