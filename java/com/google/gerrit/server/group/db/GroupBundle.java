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
DECL|package|com.google.gerrit.server.group.db
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
operator|.
name|db
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
operator|.
name|toImmutableSet
import|;
end_import

begin_import
import|import static
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
name|ReviewDbUtil
operator|.
name|checkColumns
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|ImmutableSet
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
name|TimeUtil
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
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroupMemberAudit
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
name|group
operator|.
name|InternalGroup
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
name|ConfigInvalidException
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

begin_comment
comment|/**  * A bundle of all entities rooted at a single {@link AccountGroup} entity.  *  *<p>Used primarily during the migration process. Most callers should prefer {@link InternalGroup}  * instead.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|GroupBundle
specifier|public
specifier|abstract
class|class
name|GroupBundle
block|{
static|static
block|{
comment|// Initialization-time checks that the column set hasn't changed since the
comment|// last time this file was updated.
name|checkColumns
argument_list|(
name|AccountGroup
operator|.
name|NameKey
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroup
operator|.
name|UUID
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroup
operator|.
name|Id
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroup
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|,
literal|7
argument_list|,
literal|9
argument_list|,
literal|10
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupById
operator|.
name|Key
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupById
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupByIdAud
operator|.
name|Key
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupByIdAud
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupMember
operator|.
name|Key
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupMember
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupMemberAudit
operator|.
name|Key
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|checkColumns
argument_list|(
name|AccountGroupMemberAudit
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Singleton
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
block|{
DECL|field|auditLogReader
specifier|private
specifier|final
name|AuditLogReader
name|auditLogReader
decl_stmt|;
annotation|@
name|Inject
DECL|method|Factory (AuditLogReader auditLogReader)
name|Factory
parameter_list|(
name|AuditLogReader
name|auditLogReader
parameter_list|)
block|{
name|this
operator|.
name|auditLogReader
operator|=
name|auditLogReader
expr_stmt|;
block|}
DECL|method|fromReviewDb (ReviewDb db, AccountGroup.Id id)
specifier|public
name|GroupBundle
name|fromReviewDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
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
name|id
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
name|OrmException
argument_list|(
literal|"Group "
operator|+
name|id
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
name|create
argument_list|(
name|group
argument_list|,
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|byGroup
argument_list|(
name|id
argument_list|)
argument_list|,
name|db
operator|.
name|accountGroupMembersAudit
argument_list|()
operator|.
name|byGroup
argument_list|(
name|id
argument_list|)
argument_list|,
name|db
operator|.
name|accountGroupById
argument_list|()
operator|.
name|byGroup
argument_list|(
name|id
argument_list|)
argument_list|,
name|db
operator|.
name|accountGroupByIdAud
argument_list|()
operator|.
name|byGroup
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|fromNoteDb (Repository repo, AccountGroup.UUID uuid)
specifier|public
name|GroupBundle
name|fromNoteDb
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|GroupConfig
name|groupConfig
init|=
name|GroupConfig
operator|.
name|loadForGroup
argument_list|(
name|repo
argument_list|,
name|uuid
argument_list|)
decl_stmt|;
name|InternalGroup
name|internalGroup
init|=
name|groupConfig
operator|.
name|getLoadedGroup
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
name|internalGroup
operator|.
name|getId
argument_list|()
decl_stmt|;
name|AccountGroup
name|accountGroup
init|=
operator|new
name|AccountGroup
argument_list|(
name|internalGroup
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|internalGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|internalGroup
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
decl_stmt|;
name|accountGroup
operator|.
name|setDescription
argument_list|(
name|internalGroup
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|accountGroup
operator|.
name|setOwnerGroupUUID
argument_list|(
name|internalGroup
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
name|accountGroup
operator|.
name|setVisibleToAll
argument_list|(
name|internalGroup
operator|.
name|isVisibleToAll
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|create
argument_list|(
name|accountGroup
argument_list|,
name|internalGroup
operator|.
name|getMembers
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|accountId
lambda|->
operator|new
name|AccountGroupMember
argument_list|(
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|accountId
argument_list|,
name|groupId
argument_list|)
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
argument_list|,
name|auditLogReader
operator|.
name|getMembersAudit
argument_list|(
name|uuid
argument_list|)
argument_list|,
name|internalGroup
operator|.
name|getSubgroups
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|subgroupUuid
lambda|->
operator|new
name|AccountGroupById
argument_list|(
operator|new
name|AccountGroupById
operator|.
name|Key
argument_list|(
name|groupId
argument_list|,
name|subgroupUuid
argument_list|)
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
argument_list|,
name|auditLogReader
operator|.
name|getSubgroupsAudit
argument_list|(
name|uuid
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|create ( AccountGroup group, Iterable<AccountGroupMember> members, Iterable<AccountGroupMemberAudit> memberAudit, Iterable<AccountGroupById> byId, Iterable<AccountGroupByIdAud> byIdAudit)
specifier|public
specifier|static
name|GroupBundle
name|create
parameter_list|(
name|AccountGroup
name|group
parameter_list|,
name|Iterable
argument_list|<
name|AccountGroupMember
argument_list|>
name|members
parameter_list|,
name|Iterable
argument_list|<
name|AccountGroupMemberAudit
argument_list|>
name|memberAudit
parameter_list|,
name|Iterable
argument_list|<
name|AccountGroupById
argument_list|>
name|byId
parameter_list|,
name|Iterable
argument_list|<
name|AccountGroupByIdAud
argument_list|>
name|byIdAudit
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_GroupBundle
operator|.
name|Builder
argument_list|()
operator|.
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|members
argument_list|(
name|members
argument_list|)
operator|.
name|memberAudit
argument_list|(
name|memberAudit
argument_list|)
operator|.
name|byId
argument_list|(
name|byId
argument_list|)
operator|.
name|byIdAudit
argument_list|(
name|byIdAudit
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|builder ()
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_GroupBundle
operator|.
name|Builder
argument_list|()
operator|.
name|members
argument_list|()
operator|.
name|memberAudit
argument_list|()
operator|.
name|byId
argument_list|()
operator|.
name|byIdAudit
argument_list|()
return|;
block|}
DECL|method|id ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|()
block|{
return|return
name|group
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|uuid ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|()
block|{
return|return
name|group
argument_list|()
operator|.
name|getGroupUUID
argument_list|()
return|;
block|}
DECL|method|group ()
specifier|public
specifier|abstract
name|AccountGroup
name|group
parameter_list|()
function_decl|;
DECL|method|members ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|AccountGroupMember
argument_list|>
name|members
parameter_list|()
function_decl|;
DECL|method|memberAudit ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|AccountGroupMemberAudit
argument_list|>
name|memberAudit
parameter_list|()
function_decl|;
DECL|method|byId ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|AccountGroupById
argument_list|>
name|byId
parameter_list|()
function_decl|;
DECL|method|byIdAudit ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|AccountGroupByIdAud
argument_list|>
name|byIdAudit
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|roundToSecond ()
specifier|public
name|GroupBundle
name|roundToSecond
parameter_list|()
block|{
name|AccountGroup
name|newGroup
init|=
operator|new
name|AccountGroup
argument_list|(
name|group
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|newGroup
operator|.
name|getCreatedOn
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|newGroup
operator|.
name|setCreatedOn
argument_list|(
name|TimeUtil
operator|.
name|roundToSecond
argument_list|(
name|newGroup
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|toBuilder
argument_list|()
operator|.
name|group
argument_list|(
name|newGroup
argument_list|)
operator|.
name|memberAudit
argument_list|(
name|memberAudit
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|GroupBundle
operator|::
name|roundToSecond
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|byIdAudit
argument_list|(
name|byIdAudit
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|GroupBundle
operator|::
name|roundToSecond
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|roundToSecond (AccountGroupMemberAudit a)
specifier|private
specifier|static
name|AccountGroupMemberAudit
name|roundToSecond
parameter_list|(
name|AccountGroupMemberAudit
name|a
parameter_list|)
block|{
name|AccountGroupMemberAudit
name|result
init|=
operator|new
name|AccountGroupMemberAudit
argument_list|(
operator|new
name|AccountGroupMemberAudit
operator|.
name|Key
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|a
operator|.
name|getKey
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|roundToSecond
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
operator|.
name|getAddedOn
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|a
operator|.
name|getAddedBy
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|.
name|getRemovedOn
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|removed
argument_list|(
name|a
operator|.
name|getRemovedBy
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|roundToSecond
argument_list|(
name|a
operator|.
name|getRemovedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|roundToSecond (AccountGroupByIdAud a)
specifier|private
specifier|static
name|AccountGroupByIdAud
name|roundToSecond
parameter_list|(
name|AccountGroupByIdAud
name|a
parameter_list|)
block|{
name|AccountGroupByIdAud
name|result
init|=
operator|new
name|AccountGroupByIdAud
argument_list|(
operator|new
name|AccountGroupByIdAud
operator|.
name|Key
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|a
operator|.
name|getKey
argument_list|()
operator|.
name|getIncludeUUID
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|roundToSecond
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
operator|.
name|getAddedOn
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|a
operator|.
name|getAddedBy
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|.
name|getRemovedOn
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|removed
argument_list|(
name|a
operator|.
name|getRemovedBy
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|roundToSecond
argument_list|(
name|a
operator|.
name|getRemovedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|toInternalGroup ()
specifier|public
name|InternalGroup
name|toInternalGroup
parameter_list|()
block|{
return|return
name|InternalGroup
operator|.
name|create
argument_list|(
name|group
argument_list|()
argument_list|,
name|members
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|AccountGroupMember
operator|::
name|getAccountId
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
argument_list|,
name|byId
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|AccountGroupById
operator|::
name|getIncludeUUID
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|group (AccountGroup group)
specifier|abstract
name|Builder
name|group
parameter_list|(
name|AccountGroup
name|group
parameter_list|)
function_decl|;
DECL|method|members (AccountGroupMember... member)
specifier|abstract
name|Builder
name|members
parameter_list|(
name|AccountGroupMember
modifier|...
name|member
parameter_list|)
function_decl|;
DECL|method|members (Iterable<AccountGroupMember> member)
specifier|abstract
name|Builder
name|members
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroupMember
argument_list|>
name|member
parameter_list|)
function_decl|;
DECL|method|memberAudit (AccountGroupMemberAudit... audit)
specifier|abstract
name|Builder
name|memberAudit
parameter_list|(
name|AccountGroupMemberAudit
modifier|...
name|audit
parameter_list|)
function_decl|;
DECL|method|memberAudit (Iterable<AccountGroupMemberAudit> audit)
specifier|abstract
name|Builder
name|memberAudit
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroupMemberAudit
argument_list|>
name|audit
parameter_list|)
function_decl|;
DECL|method|byId (AccountGroupById... byId)
specifier|abstract
name|Builder
name|byId
parameter_list|(
name|AccountGroupById
modifier|...
name|byId
parameter_list|)
function_decl|;
DECL|method|byId (Iterable<AccountGroupById> byId)
specifier|abstract
name|Builder
name|byId
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroupById
argument_list|>
name|byId
parameter_list|)
function_decl|;
DECL|method|byIdAudit (AccountGroupByIdAud... audit)
specifier|abstract
name|Builder
name|byIdAudit
parameter_list|(
name|AccountGroupByIdAud
modifier|...
name|audit
parameter_list|)
function_decl|;
DECL|method|byIdAudit (Iterable<AccountGroupByIdAud> audit)
specifier|abstract
name|Builder
name|byIdAudit
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroupByIdAud
argument_list|>
name|audit
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|abstract
name|GroupBundle
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

