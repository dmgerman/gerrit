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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|GroupDescription
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
name|Response
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
name|RestModifyView
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
name|UnprocessableEntityException
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
name|AccountGroupById
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
name|AccountGroupByIdAud
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
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|AddIncludedGroups
operator|.
name|Input
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
name|util
operator|.
name|TimeUtil
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
name|Map
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|DeleteIncludedGroups
specifier|public
class|class
name|DeleteIncludedGroups
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|groupsCollection
specifier|private
specifier|final
name|GroupsCollection
name|groupsCollection
decl_stmt|;
DECL|field|groupIncludeCache
specifier|private
specifier|final
name|GroupIncludeCache
name|groupIncludeCache
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteIncludedGroups (GroupsCollection groupsCollection, GroupIncludeCache groupIncludeCache, Provider<ReviewDb> db, Provider<CurrentUser> self)
name|DeleteIncludedGroups
parameter_list|(
name|GroupsCollection
name|groupsCollection
parameter_list|,
name|GroupIncludeCache
name|groupIncludeCache
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|)
block|{
name|this
operator|.
name|groupsCollection
operator|=
name|groupsCollection
expr_stmt|;
name|this
operator|.
name|groupIncludeCache
operator|=
name|groupIncludeCache
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|GroupResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|MethodNotAllowedException
throws|,
name|UnprocessableEntityException
throws|,
name|OrmException
block|{
name|AccountGroup
name|internalGroup
init|=
name|resource
operator|.
name|toAccountGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|internalGroup
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|()
throw|;
block|}
name|input
operator|=
name|Input
operator|.
name|init
argument_list|(
name|input
argument_list|)
expr_stmt|;
specifier|final
name|GroupControl
name|control
init|=
name|resource
operator|.
name|getControl
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|AccountGroupById
argument_list|>
name|includedGroups
init|=
name|getIncludedGroups
argument_list|(
name|internalGroup
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountGroupById
argument_list|>
name|toRemove
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|includedGroup
range|:
name|input
operator|.
name|groups
control|)
block|{
name|GroupDescription
operator|.
name|Basic
name|d
init|=
name|groupsCollection
operator|.
name|parse
argument_list|(
name|includedGroup
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|canRemoveGroup
argument_list|(
name|d
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot delete group: %s"
argument_list|,
name|d
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|AccountGroupById
name|g
init|=
name|includedGroups
operator|.
name|remove
argument_list|(
name|d
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|!=
literal|null
condition|)
block|{
name|toRemove
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|toRemove
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|writeAudits
argument_list|(
name|toRemove
argument_list|)
expr_stmt|;
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupById
argument_list|()
operator|.
name|delete
argument_list|(
name|toRemove
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|AccountGroupById
name|g
range|:
name|toRemove
control|)
block|{
name|groupIncludeCache
operator|.
name|evictMemberIn
argument_list|(
name|g
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|groupIncludeCache
operator|.
name|evictMembersOf
argument_list|(
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
DECL|method|getIncludedGroups ( final AccountGroup.Id groupId)
specifier|private
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|AccountGroupById
argument_list|>
name|getIncludedGroups
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|AccountGroupById
argument_list|>
name|groups
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroupById
name|g
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupById
argument_list|()
operator|.
name|byGroup
argument_list|(
name|groupId
argument_list|)
control|)
block|{
name|groups
operator|.
name|put
argument_list|(
name|g
operator|.
name|getIncludeUUID
argument_list|()
argument_list|,
name|g
argument_list|)
expr_stmt|;
block|}
return|return
name|groups
return|;
block|}
DECL|method|writeAudits (final List<AccountGroupById> toBeRemoved)
specifier|private
name|void
name|writeAudits
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountGroupById
argument_list|>
name|toBeRemoved
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
operator|(
operator|(
name|IdentifiedUser
operator|)
name|self
operator|.
name|get
argument_list|()
operator|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountGroupByIdAud
argument_list|>
name|auditUpdates
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroupById
name|g
range|:
name|toBeRemoved
control|)
block|{
name|AccountGroupByIdAud
name|audit
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AccountGroupByIdAud
name|a
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupByIdAud
argument_list|()
operator|.
name|byGroupInclude
argument_list|(
name|g
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|g
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
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
expr_stmt|;
name|auditUpdates
operator|.
name|add
argument_list|(
name|audit
argument_list|)
expr_stmt|;
block|}
block|}
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupByIdAud
argument_list|()
operator|.
name|update
argument_list|(
name|auditUpdates
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Singleton
DECL|class|DeleteIncludedGroup
specifier|static
class|class
name|DeleteIncludedGroup
implements|implements
name|RestModifyView
argument_list|<
name|IncludedGroupResource
argument_list|,
name|DeleteIncludedGroup
operator|.
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{     }
DECL|field|delete
specifier|private
specifier|final
name|Provider
argument_list|<
name|DeleteIncludedGroups
argument_list|>
name|delete
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteIncludedGroup (final Provider<DeleteIncludedGroups> delete)
name|DeleteIncludedGroup
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|DeleteIncludedGroups
argument_list|>
name|delete
parameter_list|)
block|{
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (IncludedGroupResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|IncludedGroupResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|MethodNotAllowedException
throws|,
name|UnprocessableEntityException
throws|,
name|OrmException
block|{
name|AddIncludedGroups
operator|.
name|Input
name|in
init|=
operator|new
name|AddIncludedGroups
operator|.
name|Input
argument_list|()
decl_stmt|;
name|in
operator|.
name|groups
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|resource
operator|.
name|getMember
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|delete
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|resource
argument_list|,
name|in
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

