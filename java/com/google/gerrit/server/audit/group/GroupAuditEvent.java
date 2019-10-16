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

begin_comment
comment|/** An audit event for groups. */
end_comment

begin_interface
DECL|interface|GroupAuditEvent
specifier|public
interface|interface
name|GroupAuditEvent
block|{
comment|/**    * Gets the acting user who is updating the group.    *    * @return the {@link com.google.gerrit.entities.Account.Id} of the acting user.    */
DECL|method|getActor ()
name|Account
operator|.
name|Id
name|getActor
parameter_list|()
function_decl|;
comment|/**    * Gets the {@link com.google.gerrit.entities.AccountGroup.UUID} of the updated group.    *    * @return the {@link com.google.gerrit.entities.AccountGroup.UUID} of the updated group.    */
DECL|method|getUpdatedGroup ()
name|AccountGroup
operator|.
name|UUID
name|getUpdatedGroup
parameter_list|()
function_decl|;
comment|/**    * Gets the {@link Timestamp} of the action.    *    * @return the {@link Timestamp} of the action.    */
DECL|method|getTimestamp ()
name|Timestamp
name|getTimestamp
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

