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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|reviewdb
operator|.
name|Account
import|;
end_import

begin_comment
comment|/** Constructs an address to send email from. */
end_comment

begin_interface
DECL|interface|FromAddressGenerator
specifier|public
interface|interface
name|FromAddressGenerator
block|{
DECL|method|isGenericAddress (Account.Id fromId)
specifier|public
name|boolean
name|isGenericAddress
parameter_list|(
name|Account
operator|.
name|Id
name|fromId
parameter_list|)
function_decl|;
DECL|method|from (Account.Id fromId)
specifier|public
name|Address
name|from
parameter_list|(
name|Account
operator|.
name|Id
name|fromId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

