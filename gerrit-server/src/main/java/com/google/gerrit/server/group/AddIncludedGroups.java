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
name|DefaultInput
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
name|ResourceNotFoundException
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
name|BadRequestHandler
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
DECL|class|AddIncludedGroups
specifier|public
class|class
name|AddIncludedGroups
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{
annotation|@
name|DefaultInput
DECL|field|_oneGroup
name|String
name|_oneGroup
decl_stmt|;
DECL|field|groups
name|List
argument_list|<
name|String
argument_list|>
name|groups
decl_stmt|;
DECL|method|init (Input in)
specifier|static
name|Input
name|init
parameter_list|(
name|Input
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|in
operator|=
operator|new
name|Input
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|groups
operator|==
literal|null
condition|)
block|{
name|in
operator|.
name|groups
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|in
operator|.
name|_oneGroup
argument_list|)
condition|)
block|{
name|in
operator|.
name|groups
operator|.
name|add
argument_list|(
name|in
operator|.
name|_oneGroup
argument_list|)
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
block|}
DECL|field|groupsCollection
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupsCollection
argument_list|>
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
name|ReviewDb
name|db
decl_stmt|;
annotation|@
name|Inject
DECL|method|AddIncludedGroups (Provider<GroupsCollection> groupsCollection, GroupIncludeCache groupIncludeCache, ReviewDb db)
specifier|public
name|AddIncludedGroups
parameter_list|(
name|Provider
argument_list|<
name|GroupsCollection
argument_list|>
name|groupsCollection
parameter_list|,
name|GroupIncludeCache
name|groupIncludeCache
parameter_list|,
name|ReviewDb
name|db
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
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource, Input input)
specifier|public
name|List
argument_list|<
name|GroupInfo
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
name|MethodNotAllowedException
throws|,
name|AuthException
throws|,
name|BadRequestException
throws|,
name|OrmException
block|{
name|AccountGroup
name|group
init|=
name|resource
operator|.
name|toAccountGroup
argument_list|()
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
name|GroupControl
name|control
init|=
name|resource
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|AccountGroupIncludeByUuid
argument_list|>
name|newIncludedGroups
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AccountGroupIncludeByUuidAudit
argument_list|>
name|newIncludedGroupsAudits
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
name|BadRequestHandler
name|badRequest
init|=
operator|new
name|BadRequestHandler
argument_list|(
literal|"adding groups"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
name|Account
operator|.
name|Id
name|me
init|=
operator|(
operator|(
name|IdentifiedUser
operator|)
name|control
operator|.
name|getCurrentUser
argument_list|()
operator|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|includedGroup
range|:
name|input
operator|.
name|groups
control|)
block|{
try|try
block|{
name|GroupResource
name|includedGroupResource
init|=
name|groupsCollection
operator|.
name|get
argument_list|()
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
name|canAddGroup
argument_list|(
name|includedGroupResource
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
literal|"Cannot add group: %s"
argument_list|,
name|includedGroupResource
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|newIncludedGroups
operator|.
name|containsKey
argument_list|(
name|includedGroupResource
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
condition|)
block|{
name|AccountGroupIncludeByUuid
operator|.
name|Key
name|agiKey
init|=
operator|new
name|AccountGroupIncludeByUuid
operator|.
name|Key
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|,
name|includedGroupResource
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
decl_stmt|;
name|AccountGroupIncludeByUuid
name|agi
init|=
name|db
operator|.
name|accountGroupIncludesByUuid
argument_list|()
operator|.
name|get
argument_list|(
name|agiKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|agi
operator|==
literal|null
condition|)
block|{
name|agi
operator|=
operator|new
name|AccountGroupIncludeByUuid
argument_list|(
name|agiKey
argument_list|)
expr_stmt|;
name|newIncludedGroups
operator|.
name|put
argument_list|(
name|includedGroupResource
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|agi
argument_list|)
expr_stmt|;
name|newIncludedGroupsAudits
operator|.
name|add
argument_list|(
operator|new
name|AccountGroupIncludeByUuidAudit
argument_list|(
name|agi
argument_list|,
name|me
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|add
argument_list|(
operator|new
name|GroupInfo
argument_list|(
name|includedGroupResource
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
name|badRequest
operator|.
name|addError
argument_list|(
operator|new
name|NoSuchGroupException
argument_list|(
name|includedGroup
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|badRequest
operator|.
name|failOnError
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|newIncludedGroups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|db
operator|.
name|accountGroupIncludesByUuidAudit
argument_list|()
operator|.
name|insert
argument_list|(
name|newIncludedGroupsAudits
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupIncludesByUuid
argument_list|()
operator|.
name|insert
argument_list|(
name|newIncludedGroups
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroupIncludeByUuid
name|agi
range|:
name|newIncludedGroups
operator|.
name|values
argument_list|()
control|)
block|{
name|groupIncludeCache
operator|.
name|evictMemberIn
argument_list|(
name|agi
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
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|class|PutIncludedGroup
specifier|static
class|class
name|PutIncludedGroup
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|PutIncludedGroup
operator|.
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{     }
DECL|field|put
specifier|private
specifier|final
name|Provider
argument_list|<
name|AddIncludedGroups
argument_list|>
name|put
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|method|PutIncludedGroup (Provider<AddIncludedGroups> put, String id)
name|PutIncludedGroup
parameter_list|(
name|Provider
argument_list|<
name|AddIncludedGroups
argument_list|>
name|put
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|put
operator|=
name|put
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource, Input input)
specifier|public
name|GroupInfo
name|apply
parameter_list|(
name|GroupResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|MethodNotAllowedException
throws|,
name|AuthException
throws|,
name|BadRequestException
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
name|id
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|list
init|=
name|put
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
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
DECL|class|UpdateIncludedGroup
specifier|static
class|class
name|UpdateIncludedGroup
implements|implements
name|RestModifyView
argument_list|<
name|IncludedGroupResource
argument_list|,
name|PutIncludedGroup
operator|.
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{     }
DECL|field|get
specifier|private
specifier|final
name|Provider
argument_list|<
name|GetIncludedGroup
argument_list|>
name|get
decl_stmt|;
annotation|@
name|Inject
DECL|method|UpdateIncludedGroup (Provider<GetIncludedGroup> get)
name|UpdateIncludedGroup
parameter_list|(
name|Provider
argument_list|<
name|GetIncludedGroup
argument_list|>
name|get
parameter_list|)
block|{
name|this
operator|.
name|get
operator|=
name|get
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (IncludedGroupResource resource, PutIncludedGroup.Input input)
specifier|public
name|Object
name|apply
parameter_list|(
name|IncludedGroupResource
name|resource
parameter_list|,
name|PutIncludedGroup
operator|.
name|Input
name|input
parameter_list|)
block|{
comment|// Do nothing, the group is already included.
return|return
name|get
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|resource
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

