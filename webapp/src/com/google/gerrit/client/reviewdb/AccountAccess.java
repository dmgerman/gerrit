begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Access
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|PrimaryKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|SecondaryKey
import|;
end_import

begin_comment
comment|/** Access interface for {@link Account}. */
end_comment

begin_interface
DECL|interface|AccountAccess
specifier|public
interface|interface
name|AccountAccess
extends|extends
name|Access
argument_list|<
name|Account
argument_list|,
name|Account
operator|.
name|OpenId
argument_list|>
block|{
comment|/** Locate an account by its OpenID provider supplied identifier string */
annotation|@
name|PrimaryKey
argument_list|(
literal|"openidIdentity"
argument_list|)
DECL|method|byOpenId (Account.OpenId key)
name|Account
name|byOpenId
parameter_list|(
name|Account
operator|.
name|OpenId
name|key
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/** Locate an account by our locally generated identity. */
annotation|@
name|SecondaryKey
argument_list|(
literal|"accountId"
argument_list|)
DECL|method|byId (Account.Id key)
name|Account
name|byId
parameter_list|(
name|Account
operator|.
name|Id
name|key
parameter_list|)
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

