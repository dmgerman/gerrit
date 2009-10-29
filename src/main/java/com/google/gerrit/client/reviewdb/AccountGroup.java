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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
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
name|length
operator|=
literal|40
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
block|{     }
DECL|method|NameKey (final String n)
specifier|public
name|NameKey
parameter_list|(
specifier|final
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
comment|/** Distinguished name, within organization directory server. */
DECL|class|ExternalNameKey
specifier|public
specifier|static
class|class
name|ExternalNameKey
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
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|method|ExternalNameKey ()
specifier|protected
name|ExternalNameKey
parameter_list|()
block|{     }
DECL|method|ExternalNameKey (final String n)
specifier|public
name|ExternalNameKey
parameter_list|(
specifier|final
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
DECL|field|id
specifier|protected
name|int
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{     }
DECL|method|Id (final int id)
specifier|public
name|Id
parameter_list|(
specifier|final
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
DECL|method|parse (final String str)
specifier|public
specifier|static
name|Id
name|parse
parameter_list|(
specifier|final
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
DECL|field|name
specifier|protected
name|NameKey
name|name
decl_stmt|;
comment|/** Unique identity, to link entities as {@link #name} can change. */
annotation|@
name|Column
DECL|field|groupId
specifier|protected
name|Id
name|groupId
decl_stmt|;
comment|/**    * Identity of the group whose members can manage this group.    *<p>    * This can be a self-reference to indicate the group's members manage itself.    */
annotation|@
name|Column
DECL|field|ownerGroupId
specifier|protected
name|Id
name|ownerGroupId
decl_stmt|;
comment|/** A textual description of the group's purpose. */
annotation|@
name|Column
argument_list|(
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
comment|/** Is the membership managed by some external means? */
annotation|@
name|Column
DECL|field|automaticMembership
specifier|protected
name|boolean
name|automaticMembership
decl_stmt|;
comment|/** Distinguished name in the directory server. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|externalName
specifier|protected
name|ExternalNameKey
name|externalName
decl_stmt|;
DECL|method|AccountGroup ()
specifier|protected
name|AccountGroup
parameter_list|()
block|{   }
DECL|method|AccountGroup (final AccountGroup.NameKey newName, final AccountGroup.Id newId)
specifier|public
name|AccountGroup
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|NameKey
name|newName
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Id
name|newId
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
name|ownerGroupId
operator|=
name|groupId
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
DECL|method|setNameKey (final AccountGroup.NameKey nameKey)
specifier|public
name|void
name|setNameKey
parameter_list|(
specifier|final
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
DECL|method|setDescription (final String d)
specifier|public
name|void
name|setDescription
parameter_list|(
specifier|final
name|String
name|d
parameter_list|)
block|{
name|description
operator|=
name|d
expr_stmt|;
block|}
DECL|method|getOwnerGroupId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getOwnerGroupId
parameter_list|()
block|{
return|return
name|ownerGroupId
return|;
block|}
DECL|method|setOwnerGroupId (final AccountGroup.Id id)
specifier|public
name|void
name|setOwnerGroupId
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
block|{
name|ownerGroupId
operator|=
name|id
expr_stmt|;
block|}
DECL|method|isAutomaticMembership ()
specifier|public
name|boolean
name|isAutomaticMembership
parameter_list|()
block|{
return|return
name|automaticMembership
operator|||
name|externalName
operator|!=
literal|null
return|;
block|}
DECL|method|setAutomaticMembership (final boolean auto)
specifier|public
name|void
name|setAutomaticMembership
parameter_list|(
specifier|final
name|boolean
name|auto
parameter_list|)
block|{
name|automaticMembership
operator|=
name|auto
expr_stmt|;
block|}
DECL|method|getExternalNameKey ()
specifier|public
name|ExternalNameKey
name|getExternalNameKey
parameter_list|()
block|{
return|return
name|externalName
return|;
block|}
DECL|method|setExternalNameKey (final ExternalNameKey k)
specifier|public
name|void
name|setExternalNameKey
parameter_list|(
specifier|final
name|ExternalNameKey
name|k
parameter_list|)
block|{
name|externalName
operator|=
name|k
expr_stmt|;
block|}
block|}
end_class

end_unit

