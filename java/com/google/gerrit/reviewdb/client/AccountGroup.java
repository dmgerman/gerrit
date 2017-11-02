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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

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
name|Column
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
name|IntKey
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
name|StringKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** Named group of one or more accounts, typically used for access controls. */
end_comment

begin_class
DECL|class|AccountGroup
specifier|public
specifier|final
class|class
name|AccountGroup
block|{
comment|/**    * Time when the audit subsystem was implemented, used as the default value for {@link #createdOn}    * when one couldn't be determined from the audit log.    */
comment|// Can't use Instant here because GWT. This is verified against a readable time in the tests,
comment|// which don't need to compile under GWT.
DECL|field|AUDIT_CREATION_INSTANT_MS
specifier|private
specifier|static
specifier|final
name|long
name|AUDIT_CREATION_INSTANT_MS
init|=
literal|1244489460000L
decl_stmt|;
DECL|method|auditCreationInstantTs ()
specifier|public
specifier|static
name|Timestamp
name|auditCreationInstantTs
parameter_list|()
block|{
return|return
operator|new
name|Timestamp
argument_list|(
name|AUDIT_CREATION_INSTANT_MS
argument_list|)
return|;
block|}
comment|/** Group name key */
DECL|class|NameKey
specifier|public
specifier|static
class|class
name|NameKey
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|method|NameKey ()
specifier|protected
name|NameKey
parameter_list|()
block|{}
DECL|method|NameKey (String n)
specifier|public
name|NameKey
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|name
operator|=
name|newValue
expr_stmt|;
block|}
block|}
comment|/** Globally unique identifier. */
DECL|class|UUID
specifier|public
specifier|static
class|class
name|UUID
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|uuid
specifier|protected
name|String
name|uuid
decl_stmt|;
DECL|method|UUID ()
specifier|protected
name|UUID
parameter_list|()
block|{}
DECL|method|UUID (String n)
specifier|public
name|UUID
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|uuid
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|uuid
operator|=
name|newValue
expr_stmt|;
block|}
comment|/** Parse an AccountGroup.UUID out of a string representation. */
DECL|method|parse (String str)
specifier|public
specifier|static
name|UUID
name|parse
parameter_list|(
name|String
name|str
parameter_list|)
block|{
specifier|final
name|UUID
name|r
init|=
operator|new
name|UUID
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
comment|/** @return true if the UUID is for a group managed within Gerrit. */
DECL|method|isInternalGroup (AccountGroup.UUID uuid)
specifier|public
specifier|static
name|boolean
name|isInternalGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|uuid
operator|.
name|get
argument_list|()
operator|.
name|matches
argument_list|(
literal|"^[0-9a-f]{40}$"
argument_list|)
return|;
block|}
comment|/** Synthetic key to link to within the database */
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|IntKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|id
specifier|protected
name|int
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{}
DECL|method|Id (int id)
specifier|public
name|Id
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|set (int newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|int
name|newValue
parameter_list|)
block|{
name|id
operator|=
name|newValue
expr_stmt|;
block|}
comment|/** Parse an AccountGroup.Id out of a string representation. */
DECL|method|parse (String str)
specifier|public
specifier|static
name|Id
name|parse
parameter_list|(
name|String
name|str
parameter_list|)
block|{
specifier|final
name|Id
name|r
init|=
operator|new
name|Id
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
comment|/** Unique name of this group within the system. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|name
specifier|protected
name|NameKey
name|name
decl_stmt|;
comment|/** Unique identity, to link entities as {@link #name} can change. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|groupId
specifier|protected
name|Id
name|groupId
decl_stmt|;
comment|// DELETED: id = 3 (ownerGroupId)
comment|/** A textual description of the group's purpose. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|,
name|length
operator|=
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|description
specifier|protected
name|String
name|description
decl_stmt|;
comment|// DELETED: id = 5 (groupType)
comment|// DELETED: id = 6 (externalName)
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|7
argument_list|)
DECL|field|visibleToAll
specifier|protected
name|boolean
name|visibleToAll
decl_stmt|;
comment|// DELETED: id = 8 (emailOnlyAuthors)
comment|/** Globally unique identifier name for this group. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|9
argument_list|)
DECL|field|groupUUID
specifier|protected
name|UUID
name|groupUUID
decl_stmt|;
comment|/**    * Identity of the group whose members can manage this group.    *    *<p>This can be a self-reference to indicate the group's members manage itself.    */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|10
argument_list|)
DECL|field|ownerGroupUUID
specifier|protected
name|UUID
name|ownerGroupUUID
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|11
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|createdOn
specifier|protected
name|Timestamp
name|createdOn
decl_stmt|;
DECL|method|AccountGroup ()
specifier|protected
name|AccountGroup
parameter_list|()
block|{}
DECL|method|AccountGroup ( AccountGroup.NameKey newName, AccountGroup.Id newId, AccountGroup.UUID uuid, Timestamp createdOn)
specifier|public
name|AccountGroup
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|newName
parameter_list|,
name|AccountGroup
operator|.
name|Id
name|newId
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|,
name|Timestamp
name|createdOn
parameter_list|)
block|{
name|name
operator|=
name|newName
expr_stmt|;
name|groupId
operator|=
name|newId
expr_stmt|;
name|visibleToAll
operator|=
literal|false
expr_stmt|;
name|groupUUID
operator|=
name|uuid
expr_stmt|;
name|ownerGroupUUID
operator|=
name|groupUUID
expr_stmt|;
name|this
operator|.
name|createdOn
operator|=
name|createdOn
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getNameKey ()
specifier|public
name|AccountGroup
operator|.
name|NameKey
name|getNameKey
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setNameKey (AccountGroup.NameKey nameKey)
specifier|public
name|void
name|setNameKey
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|nameKey
parameter_list|)
block|{
name|name
operator|=
name|nameKey
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String d)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|d
parameter_list|)
block|{
name|description
operator|=
name|d
expr_stmt|;
block|}
DECL|method|getOwnerGroupUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getOwnerGroupUUID
parameter_list|()
block|{
return|return
name|ownerGroupUUID
return|;
block|}
DECL|method|setOwnerGroupUUID (AccountGroup.UUID uuid)
specifier|public
name|void
name|setOwnerGroupUUID
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|ownerGroupUUID
operator|=
name|uuid
expr_stmt|;
block|}
DECL|method|setVisibleToAll (boolean visibleToAll)
specifier|public
name|void
name|setVisibleToAll
parameter_list|(
name|boolean
name|visibleToAll
parameter_list|)
block|{
name|this
operator|.
name|visibleToAll
operator|=
name|visibleToAll
expr_stmt|;
block|}
DECL|method|isVisibleToAll ()
specifier|public
name|boolean
name|isVisibleToAll
parameter_list|()
block|{
return|return
name|visibleToAll
return|;
block|}
DECL|method|getGroupUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
block|{
return|return
name|groupUUID
return|;
block|}
DECL|method|setGroupUUID (AccountGroup.UUID uuid)
specifier|public
name|void
name|setGroupUUID
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|groupUUID
operator|=
name|uuid
expr_stmt|;
block|}
DECL|method|getCreatedOn ()
specifier|public
name|Timestamp
name|getCreatedOn
parameter_list|()
block|{
return|return
name|createdOn
operator|!=
literal|null
condition|?
name|createdOn
else|:
name|auditCreationInstantTs
argument_list|()
return|;
block|}
DECL|method|setCreatedOn (Timestamp createdOn)
specifier|public
name|void
name|setCreatedOn
parameter_list|(
name|Timestamp
name|createdOn
parameter_list|)
block|{
name|this
operator|.
name|createdOn
operator|=
name|createdOn
expr_stmt|;
block|}
block|}
end_class

end_unit
