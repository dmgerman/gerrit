begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
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
name|entities
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
name|entities
operator|.
name|AccountGroup
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|TestGroup
specifier|public
specifier|abstract
class|class
name|TestGroup
block|{
DECL|method|groupUuid ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|()
function_decl|;
DECL|method|groupId ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|()
function_decl|;
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|nameKey
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|nameKey ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|NameKey
name|nameKey
parameter_list|()
function_decl|;
DECL|method|description ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|description
parameter_list|()
function_decl|;
DECL|method|ownerGroupUuid ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|ownerGroupUuid
parameter_list|()
function_decl|;
DECL|method|visibleToAll ()
specifier|public
specifier|abstract
name|boolean
name|visibleToAll
parameter_list|()
function_decl|;
DECL|method|createdOn ()
specifier|public
specifier|abstract
name|Timestamp
name|createdOn
parameter_list|()
function_decl|;
DECL|method|members ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|members
parameter_list|()
function_decl|;
DECL|method|subgroups ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|subgroups
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_TestGroup
operator|.
name|Builder
argument_list|()
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
DECL|method|groupUuid (AccountGroup.UUID groupUuid)
specifier|public
specifier|abstract
name|Builder
name|groupUuid
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
function_decl|;
DECL|method|groupId (AccountGroup.Id id)
specifier|public
specifier|abstract
name|Builder
name|groupId
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
DECL|method|nameKey (AccountGroup.NameKey name)
specifier|public
specifier|abstract
name|Builder
name|nameKey
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|name
parameter_list|)
function_decl|;
DECL|method|description (String description)
specifier|public
specifier|abstract
name|Builder
name|description
parameter_list|(
name|String
name|description
parameter_list|)
function_decl|;
DECL|method|description (Optional<String> description)
specifier|public
specifier|abstract
name|Builder
name|description
parameter_list|(
name|Optional
argument_list|<
name|String
argument_list|>
name|description
parameter_list|)
function_decl|;
DECL|method|ownerGroupUuid (AccountGroup.UUID ownerGroupUuid)
specifier|public
specifier|abstract
name|Builder
name|ownerGroupUuid
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|ownerGroupUuid
parameter_list|)
function_decl|;
DECL|method|visibleToAll (boolean visibleToAll)
specifier|public
specifier|abstract
name|Builder
name|visibleToAll
parameter_list|(
name|boolean
name|visibleToAll
parameter_list|)
function_decl|;
DECL|method|createdOn (Timestamp createdOn)
specifier|public
specifier|abstract
name|Builder
name|createdOn
parameter_list|(
name|Timestamp
name|createdOn
parameter_list|)
function_decl|;
DECL|method|members (ImmutableSet<Account.Id> members)
specifier|public
specifier|abstract
name|Builder
name|members
parameter_list|(
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|members
parameter_list|)
function_decl|;
DECL|method|subgroups (ImmutableSet<AccountGroup.UUID> subgroups)
specifier|public
specifier|abstract
name|Builder
name|subgroups
parameter_list|(
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|subgroups
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|abstract
name|TestGroup
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

