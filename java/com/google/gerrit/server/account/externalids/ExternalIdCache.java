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
DECL|package|com.google.gerrit.server.account.externalids
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|externalids
package|;
end_package

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
name|SetMultimap
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_comment
comment|/**  * Caches external IDs of all accounts.  *  *<p>On each cache access the SHA1 of the refs/meta/external-ids branch is read to verify that the  * cache is up to date.  *  *<p>All returned collections are unmodifiable.  */
end_comment

begin_interface
DECL|interface|ExternalIdCache
interface|interface
name|ExternalIdCache
block|{
DECL|method|onReplace ( ObjectId oldNotesRev, ObjectId newNotesRev, Collection<ExternalId> toRemove, Collection<ExternalId> toAdd)
name|void
name|onReplace
parameter_list|(
name|ObjectId
name|oldNotesRev
parameter_list|,
name|ObjectId
name|newNotesRev
parameter_list|,
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|toRemove
parameter_list|,
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|toAdd
parameter_list|)
function_decl|;
DECL|method|byAccount (Account.Id accountId)
name|Set
argument_list|<
name|ExternalId
argument_list|>
name|byAccount
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|byAccount (Account.Id accountId, ObjectId rev)
name|Set
argument_list|<
name|ExternalId
argument_list|>
name|byAccount
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|ObjectId
name|rev
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|allByAccount ()
name|SetMultimap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ExternalId
argument_list|>
name|allByAccount
parameter_list|()
throws|throws
name|IOException
function_decl|;
DECL|method|byEmails (String... emails)
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|ExternalId
argument_list|>
name|byEmails
parameter_list|(
name|String
modifier|...
name|emails
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|allByEmail ()
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|ExternalId
argument_list|>
name|allByEmail
parameter_list|()
throws|throws
name|IOException
function_decl|;
DECL|method|byEmail (String email)
specifier|default
name|Set
argument_list|<
name|ExternalId
argument_list|>
name|byEmail
parameter_list|(
name|String
name|email
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|byEmails
argument_list|(
name|email
argument_list|)
operator|.
name|get
argument_list|(
name|email
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

