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
DECL|package|com.google.gerrit.server.quota
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|quota
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
name|entities
operator|.
name|Change
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
name|Project
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
name|AnonymousUser
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
name|CurrentUser
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
DECL|class|QuotaRequestContext
specifier|public
specifier|abstract
class|class
name|QuotaRequestContext
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
name|AutoValue_QuotaRequestContext
operator|.
name|Builder
argument_list|()
operator|.
name|user
argument_list|(
operator|new
name|AnonymousUser
argument_list|()
argument_list|)
return|;
block|}
DECL|method|user ()
specifier|public
specifier|abstract
name|CurrentUser
name|user
parameter_list|()
function_decl|;
DECL|method|project ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|project
parameter_list|()
function_decl|;
DECL|method|change ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|change
parameter_list|()
function_decl|;
DECL|method|account ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|account
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
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
DECL|method|user (CurrentUser user)
specifier|public
specifier|abstract
name|QuotaRequestContext
operator|.
name|Builder
name|user
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
function_decl|;
DECL|method|account (Account.Id account)
specifier|public
specifier|abstract
name|QuotaRequestContext
operator|.
name|Builder
name|account
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
function_decl|;
DECL|method|project (Project.NameKey project)
specifier|public
specifier|abstract
name|QuotaRequestContext
operator|.
name|Builder
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
function_decl|;
DECL|method|change (Change.Id change)
specifier|public
specifier|abstract
name|QuotaRequestContext
operator|.
name|Builder
name|change
parameter_list|(
name|Change
operator|.
name|Id
name|change
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|QuotaRequestContext
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

