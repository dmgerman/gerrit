begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|server
operator|.
name|notedb
operator|.
name|ReviewerStateInternal
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
comment|/** Change to a reviewer's status. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|ReviewerStatusUpdate
specifier|public
specifier|abstract
class|class
name|ReviewerStatusUpdate
block|{
DECL|method|create ( Timestamp ts, Account.Id updatedBy, Account.Id reviewer, ReviewerStateInternal state)
specifier|public
specifier|static
name|ReviewerStatusUpdate
name|create
parameter_list|(
name|Timestamp
name|ts
parameter_list|,
name|Account
operator|.
name|Id
name|updatedBy
parameter_list|,
name|Account
operator|.
name|Id
name|reviewer
parameter_list|,
name|ReviewerStateInternal
name|state
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ReviewerStatusUpdate
argument_list|(
name|ts
argument_list|,
name|updatedBy
argument_list|,
name|reviewer
argument_list|,
name|state
argument_list|)
return|;
block|}
DECL|method|date ()
specifier|public
specifier|abstract
name|Timestamp
name|date
parameter_list|()
function_decl|;
DECL|method|updatedBy ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|updatedBy
parameter_list|()
function_decl|;
DECL|method|reviewer ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|reviewer
parameter_list|()
function_decl|;
DECL|method|state ()
specifier|public
specifier|abstract
name|ReviewerStateInternal
name|state
parameter_list|()
function_decl|;
block|}
end_class

end_unit

