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
DECL|package|com.google.gerrit.server.index.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|gerrit
operator|.
name|entities
operator|.
name|Account
import|;
end_import

begin_comment
comment|/** Interface for indexing a Gerrit account. */
end_comment

begin_interface
DECL|interface|AccountIndexer
specifier|public
interface|interface
name|AccountIndexer
block|{
comment|/**    * Synchronously index an account.    *    * @param id account id to index.    */
DECL|method|index (Account.Id id)
name|void
name|index
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
comment|/**    * Synchronously reindex an account if it is stale.    *    * @param id account id to index.    * @return whether the account was reindexed    */
DECL|method|reindexIfStale (Account.Id id)
name|boolean
name|reindexIfStale
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

