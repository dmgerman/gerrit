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
name|Nullable
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
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_class
annotation|@
name|AutoValue
DECL|class|InternalGroup
specifier|public
specifier|abstract
class|class
name|InternalGroup
implements|implements
name|Serializable
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
DECL|method|create ( AccountGroup accountGroup, ImmutableSet<Account.Id> members, ImmutableSet<AccountGroup.UUID> subgroups)
specifier|public
specifier|static
name|InternalGroup
name|create
parameter_list|(
name|AccountGroup
name|accountGroup
parameter_list|,
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|members
parameter_list|,
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|subgroups
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_InternalGroup
argument_list|(
name|accountGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|accountGroup
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|accountGroup
operator|.
name|getDescription
argument_list|()
argument_list|,
name|accountGroup
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|,
name|accountGroup
operator|.
name|isVisibleToAll
argument_list|()
argument_list|,
name|accountGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|accountGroup
operator|.
name|getCreatedOn
argument_list|()
argument_list|,
name|members
argument_list|,
name|subgroups
argument_list|)
return|;
block|}
DECL|method|getId ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|Id
name|getId
parameter_list|()
function_decl|;
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getNameKey
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getNameKey ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|NameKey
name|getNameKey
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|getDescription ()
specifier|public
specifier|abstract
name|String
name|getDescription
parameter_list|()
function_decl|;
DECL|method|getOwnerGroupUUID ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|getOwnerGroupUUID
parameter_list|()
function_decl|;
DECL|method|isVisibleToAll ()
specifier|public
specifier|abstract
name|boolean
name|isVisibleToAll
parameter_list|()
function_decl|;
DECL|method|getGroupUUID ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
function_decl|;
DECL|method|getCreatedOn ()
specifier|public
specifier|abstract
name|Timestamp
name|getCreatedOn
parameter_list|()
function_decl|;
DECL|method|getMembers ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|getMembers
parameter_list|()
function_decl|;
DECL|method|getSubgroups ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getSubgroups
parameter_list|()
function_decl|;
block|}
end_class

end_unit

