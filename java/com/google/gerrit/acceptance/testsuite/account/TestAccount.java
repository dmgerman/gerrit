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
DECL|package|com.google.gerrit.acceptance.testsuite.account
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
name|account
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
DECL|class|TestAccount
specifier|public
specifier|abstract
class|class
name|TestAccount
block|{
DECL|method|accountId ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|accountId
parameter_list|()
function_decl|;
DECL|method|fullname ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|fullname
parameter_list|()
function_decl|;
DECL|method|preferredEmail ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|preferredEmail
parameter_list|()
function_decl|;
DECL|method|username ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|username
parameter_list|()
function_decl|;
DECL|method|active ()
specifier|public
specifier|abstract
name|boolean
name|active
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
name|AutoValue_TestAccount
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
DECL|method|accountId (Account.Id accountId)
specifier|abstract
name|Builder
name|accountId
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
DECL|method|fullname (Optional<String> fullname)
specifier|abstract
name|Builder
name|fullname
parameter_list|(
name|Optional
argument_list|<
name|String
argument_list|>
name|fullname
parameter_list|)
function_decl|;
DECL|method|preferredEmail (Optional<String> fullname)
specifier|abstract
name|Builder
name|preferredEmail
parameter_list|(
name|Optional
argument_list|<
name|String
argument_list|>
name|fullname
parameter_list|)
function_decl|;
DECL|method|username (Optional<String> username)
specifier|abstract
name|Builder
name|username
parameter_list|(
name|Optional
argument_list|<
name|String
argument_list|>
name|username
parameter_list|)
function_decl|;
DECL|method|active (boolean active)
specifier|abstract
name|Builder
name|active
parameter_list|(
name|boolean
name|active
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|abstract
name|TestAccount
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

