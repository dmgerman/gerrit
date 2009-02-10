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

begin_interface
DECL|interface|ChangeAccess
specifier|public
interface|interface
name|ChangeAccess
extends|extends
name|Access
argument_list|<
name|Change
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
block|{
annotation|@
name|PrimaryKey
argument_list|(
literal|"changeId"
argument_list|)
DECL|method|get (Change.Id id)
name|Change
name|get
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE owner = ? AND open = true ORDER BY createdOn, changeId"
argument_list|)
DECL|method|byOwnerOpen (Account.Id id)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|byOwnerOpen
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE owner = ? AND open = false ORDER BY lastUpdatedOn DESC LIMIT 5"
argument_list|)
DECL|method|byOwnerClosed (Account.Id id)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|byOwnerClosed
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE dest = ? AND status = '"
operator|+
name|Change
operator|.
name|STATUS_SUBMITTED
operator|+
literal|"' ORDER BY lastUpdatedOn"
argument_list|)
DECL|method|submitted (Branch.NameKey dest)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|submitted
parameter_list|(
name|Branch
operator|.
name|NameKey
name|dest
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE status = '"
operator|+
name|Change
operator|.
name|STATUS_SUBMITTED
operator|+
literal|"'"
argument_list|)
DECL|method|allSubmitted ()
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|allSubmitted
parameter_list|()
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE open = true AND sortKey> ? ORDER BY sortKey LIMIT ?"
argument_list|)
DECL|method|allOpenPrev (String sortKey, int limit)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|allOpenPrev
parameter_list|(
name|String
name|sortKey
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
literal|"WHERE open = true AND sortKey< ? ORDER BY sortKey DESC LIMIT ?"
argument_list|)
DECL|method|allOpenNext (String sortKey, int limit)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|allOpenNext
parameter_list|(
name|String
name|sortKey
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
literal|"WHERE open = true AND dest.projectName = ? AND sortKey> ?"
operator|+
literal|" ORDER BY sortKey LIMIT ?"
argument_list|)
DECL|method|byProjectOpenPrev (Project.NameKey p, String sortKey, int limit)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|byProjectOpenPrev
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|,
name|String
name|sortKey
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
literal|"WHERE open = true AND dest.projectName = ? AND sortKey< ?"
operator|+
literal|" ORDER BY sortKey DESC LIMIT ?"
argument_list|)
DECL|method|byProjectOpenNext (Project.NameKey p, String sortKey, int limit)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|byProjectOpenNext
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|,
name|String
name|sortKey
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
literal|"WHERE open = false AND status = ? AND sortKey> ? ORDER BY sortKey LIMIT ?"
argument_list|)
DECL|method|allClosedPrev (char status, String sortKey, int limit)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|allClosedPrev
parameter_list|(
name|char
name|status
parameter_list|,
name|String
name|sortKey
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
literal|"WHERE open = false AND status = ? AND sortKey< ? ORDER BY sortKey DESC LIMIT ?"
argument_list|)
DECL|method|allClosedNext (char status, String sortKey, int limit)
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|allClosedNext
parameter_list|(
name|char
name|status
parameter_list|,
name|String
name|sortKey
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

