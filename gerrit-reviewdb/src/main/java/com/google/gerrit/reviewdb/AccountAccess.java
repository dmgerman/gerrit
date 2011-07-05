begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|Query
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
name|ResultSet
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
name|Id
argument_list|>
block|{
comment|/** Locate an account by our locally generated identity. */
annotation|@
name|PrimaryKey
argument_list|(
literal|"accountId"
argument_list|)
DECL|method|get (Account.Id key)
name|Account
name|get
parameter_list|(
name|Account
operator|.
name|Id
name|key
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE preferredEmail = ? LIMIT 2"
argument_list|)
DECL|method|byPreferredEmail (String email)
name|ResultSet
argument_list|<
name|Account
argument_list|>
name|byPreferredEmail
parameter_list|(
name|String
name|email
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE fullName = ? LIMIT 2"
argument_list|)
DECL|method|byFullName (String name)
name|ResultSet
argument_list|<
name|Account
argument_list|>
name|byFullName
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE fullName>= ? AND fullName<= ? ORDER BY fullName LIMIT ?"
argument_list|)
DECL|method|suggestByFullName (String nameA, String nameB, int limit)
name|ResultSet
argument_list|<
name|Account
argument_list|>
name|suggestByFullName
parameter_list|(
name|String
name|nameA
parameter_list|,
name|String
name|nameB
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE preferredEmail>= ? AND preferredEmail<= ? ORDER BY preferredEmail LIMIT ?"
argument_list|)
DECL|method|suggestByPreferredEmail (String nameA, String nameB, int limit)
name|ResultSet
argument_list|<
name|Account
argument_list|>
name|suggestByPreferredEmail
parameter_list|(
name|String
name|nameA
parameter_list|,
name|String
name|nameB
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"LIMIT 1"
argument_list|)
DECL|method|anyAccounts ()
name|ResultSet
argument_list|<
name|Account
argument_list|>
name|anyAccounts
parameter_list|()
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"ORDER BY accountId LIMIT ?"
argument_list|)
DECL|method|firstNById (int n)
name|ResultSet
argument_list|<
name|Account
argument_list|>
name|firstNById
parameter_list|(
name|int
name|n
parameter_list|)
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

