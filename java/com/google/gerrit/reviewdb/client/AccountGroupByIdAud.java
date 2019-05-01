begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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

begin_comment
comment|/** Inclusion of an {@link AccountGroup} in another {@link AccountGroup}. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|AccountGroupByIdAud
specifier|public
specifier|abstract
class|class
name|AccountGroupByIdAud
block|{
DECL|method|key (AccountGroup.Id groupId, AccountGroup.UUID includeUuid, Timestamp addedOn)
specifier|public
specifier|static
name|Key
name|key
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|includeUuid
parameter_list|,
name|Timestamp
name|addedOn
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_AccountGroupByIdAud_Key
argument_list|(
name|groupId
argument_list|,
name|includeUuid
argument_list|,
name|addedOn
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
DECL|class|Key
specifier|public
specifier|abstract
specifier|static
class|class
name|Key
block|{
DECL|method|groupId ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|()
function_decl|;
DECL|method|includeUuid ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|includeUuid
parameter_list|()
function_decl|;
DECL|method|addedOn ()
specifier|public
specifier|abstract
name|Timestamp
name|addedOn
parameter_list|()
function_decl|;
block|}
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_AccountGroupByIdAud
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
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|key (Key key)
specifier|public
specifier|abstract
name|Builder
name|key
parameter_list|(
name|Key
name|key
parameter_list|)
function_decl|;
DECL|method|addedBy (Account.Id addedBy)
specifier|public
specifier|abstract
name|Builder
name|addedBy
parameter_list|(
name|Account
operator|.
name|Id
name|addedBy
parameter_list|)
function_decl|;
DECL|method|removedBy (Account.Id removedBy)
specifier|abstract
name|Builder
name|removedBy
parameter_list|(
name|Account
operator|.
name|Id
name|removedBy
parameter_list|)
function_decl|;
DECL|method|removedOn (Timestamp removedOn)
specifier|abstract
name|Builder
name|removedOn
parameter_list|(
name|Timestamp
name|removedOn
parameter_list|)
function_decl|;
DECL|method|removed (Account.Id removedBy, Timestamp removedOn)
specifier|public
name|Builder
name|removed
parameter_list|(
name|Account
operator|.
name|Id
name|removedBy
parameter_list|,
name|Timestamp
name|removedOn
parameter_list|)
block|{
return|return
name|removedBy
argument_list|(
name|removedBy
argument_list|)
operator|.
name|removedOn
argument_list|(
name|removedOn
argument_list|)
return|;
block|}
DECL|method|build ()
specifier|public
specifier|abstract
name|AccountGroupByIdAud
name|build
parameter_list|()
function_decl|;
block|}
DECL|method|getKey ()
specifier|public
specifier|abstract
name|AccountGroupByIdAud
operator|.
name|Key
name|getKey
parameter_list|()
function_decl|;
DECL|method|getAddedBy ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|getAddedBy
parameter_list|()
function_decl|;
DECL|method|getRemovedBy ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|getRemovedBy
parameter_list|()
function_decl|;
DECL|method|getRemovedOn ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Timestamp
argument_list|>
name|getRemovedOn
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|getGroupId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getGroupId
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|.
name|groupId
argument_list|()
return|;
block|}
DECL|method|getAddedOn ()
specifier|public
name|Timestamp
name|getAddedOn
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|.
name|addedOn
argument_list|()
return|;
block|}
DECL|method|getIncludeUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getIncludeUUID
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|.
name|includeUuid
argument_list|()
return|;
block|}
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
operator|!
name|getRemovedOn
argument_list|()
operator|.
name|isPresent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

