begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
comment|/** Membership of an {@link Account} in an {@link AccountGroup}. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|AccountGroupMemberAudit
specifier|public
specifier|abstract
class|class
name|AccountGroupMemberAudit
block|{
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_AccountGroupMemberAudit
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
DECL|method|groupId (AccountGroup.Id groupId)
specifier|public
specifier|abstract
name|Builder
name|groupId
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
function_decl|;
DECL|method|memberId (Account.Id accountId)
specifier|public
specifier|abstract
name|Builder
name|memberId
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
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
DECL|method|addedBy ()
specifier|abstract
name|Account
operator|.
name|Id
name|addedBy
parameter_list|()
function_decl|;
DECL|method|addedOn (Timestamp addedOn)
specifier|public
specifier|abstract
name|Builder
name|addedOn
parameter_list|(
name|Timestamp
name|addedOn
parameter_list|)
function_decl|;
DECL|method|addedOn ()
specifier|abstract
name|Timestamp
name|addedOn
parameter_list|()
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
DECL|method|removedLegacy ()
specifier|public
name|Builder
name|removedLegacy
parameter_list|()
block|{
return|return
name|removed
argument_list|(
name|addedBy
argument_list|()
argument_list|,
name|addedOn
argument_list|()
argument_list|)
return|;
block|}
DECL|method|build ()
specifier|public
specifier|abstract
name|AccountGroupMemberAudit
name|build
parameter_list|()
function_decl|;
block|}
DECL|method|groupId ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|()
function_decl|;
DECL|method|memberId ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|memberId
parameter_list|()
function_decl|;
DECL|method|addedBy ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|addedBy
parameter_list|()
function_decl|;
DECL|method|addedOn ()
specifier|public
specifier|abstract
name|Timestamp
name|addedOn
parameter_list|()
function_decl|;
DECL|method|removedBy ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|removedBy
parameter_list|()
function_decl|;
DECL|method|removedOn ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Timestamp
argument_list|>
name|removedOn
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
operator|!
name|removedOn
argument_list|()
operator|.
name|isPresent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

