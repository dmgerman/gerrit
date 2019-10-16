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
DECL|package|com.google.gerrit.server.audit.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|audit
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

begin_class
annotation|@
name|AutoValue
DECL|class|GroupSubgroupAuditEvent
specifier|public
specifier|abstract
class|class
name|GroupSubgroupAuditEvent
implements|implements
name|GroupAuditEvent
block|{
DECL|method|create ( Account.Id actor, AccountGroup.UUID updatedGroup, ImmutableSet<AccountGroup.UUID> modifiedSubgroups, Timestamp timestamp)
specifier|public
specifier|static
name|GroupSubgroupAuditEvent
name|create
parameter_list|(
name|Account
operator|.
name|Id
name|actor
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|updatedGroup
parameter_list|,
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|modifiedSubgroups
parameter_list|,
name|Timestamp
name|timestamp
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_GroupSubgroupAuditEvent
argument_list|(
name|actor
argument_list|,
name|updatedGroup
argument_list|,
name|modifiedSubgroups
argument_list|,
name|timestamp
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getActor ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|getActor
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|getUpdatedGroup ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|getUpdatedGroup
parameter_list|()
function_decl|;
comment|/** Gets the added or deleted subgroups of the updated group. */
DECL|method|getModifiedSubgroups ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getModifiedSubgroups
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|getTimestamp ()
specifier|public
specifier|abstract
name|Timestamp
name|getTimestamp
parameter_list|()
function_decl|;
block|}
end_class

end_unit

