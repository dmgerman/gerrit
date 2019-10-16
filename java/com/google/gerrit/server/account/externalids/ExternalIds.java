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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
operator|.
name|toImmutableSet
import|;
end_import

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
name|ImmutableSet
import|;
end_import

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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|Optional
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
name|errors
operator|.
name|ConfigInvalidException
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
comment|/**  * Class to access external IDs.  *  *<p>The external IDs are either read from NoteDb or retrieved from the cache.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ExternalIds
specifier|public
class|class
name|ExternalIds
block|{
DECL|field|externalIdReader
specifier|private
specifier|final
name|ExternalIdReader
name|externalIdReader
decl_stmt|;
DECL|field|externalIdCache
specifier|private
specifier|final
name|ExternalIdCache
name|externalIdCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ExternalIds (ExternalIdReader externalIdReader, ExternalIdCache externalIdCache)
specifier|public
name|ExternalIds
parameter_list|(
name|ExternalIdReader
name|externalIdReader
parameter_list|,
name|ExternalIdCache
name|externalIdCache
parameter_list|)
block|{
name|this
operator|.
name|externalIdReader
operator|=
name|externalIdReader
expr_stmt|;
name|this
operator|.
name|externalIdCache
operator|=
name|externalIdCache
expr_stmt|;
block|}
comment|/** Returns all external IDs. */
DECL|method|all ()
specifier|public
name|ImmutableSet
argument_list|<
name|ExternalId
argument_list|>
name|all
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
name|externalIdReader
operator|.
name|all
argument_list|()
return|;
block|}
comment|/** Returns all external IDs from the specified revision of the refs/meta/external-ids branch. */
DECL|method|all (ObjectId rev)
specifier|public
name|ImmutableSet
argument_list|<
name|ExternalId
argument_list|>
name|all
parameter_list|(
name|ObjectId
name|rev
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
name|externalIdReader
operator|.
name|all
argument_list|(
name|rev
argument_list|)
return|;
block|}
comment|/** Returns the specified external ID. */
DECL|method|get (ExternalId.Key key)
specifier|public
name|Optional
argument_list|<
name|ExternalId
argument_list|>
name|get
parameter_list|(
name|ExternalId
operator|.
name|Key
name|key
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
name|externalIdReader
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/** Returns the specified external ID from the given revision. */
DECL|method|get (ExternalId.Key key, ObjectId rev)
specifier|public
name|Optional
argument_list|<
name|ExternalId
argument_list|>
name|get
parameter_list|(
name|ExternalId
operator|.
name|Key
name|key
parameter_list|,
name|ObjectId
name|rev
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
name|externalIdReader
operator|.
name|get
argument_list|(
name|key
argument_list|,
name|rev
argument_list|)
return|;
block|}
comment|/** Returns the external IDs of the specified account. */
DECL|method|byAccount (Account.Id accountId)
specifier|public
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
block|{
return|return
name|externalIdCache
operator|.
name|byAccount
argument_list|(
name|accountId
argument_list|)
return|;
block|}
comment|/** Returns the external IDs of the specified account that have the given scheme. */
DECL|method|byAccount (Account.Id accountId, String scheme)
specifier|public
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
name|String
name|scheme
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|byAccount
argument_list|(
name|accountId
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|e
operator|.
name|key
argument_list|()
operator|.
name|isScheme
argument_list|(
name|scheme
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
return|;
block|}
comment|/** Returns the external IDs of the specified account. */
DECL|method|byAccount (Account.Id accountId, ObjectId rev)
specifier|public
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
block|{
return|return
name|externalIdCache
operator|.
name|byAccount
argument_list|(
name|accountId
argument_list|,
name|rev
argument_list|)
return|;
block|}
comment|/** Returns the external IDs of the specified account that have the given scheme. */
DECL|method|byAccount (Account.Id accountId, String scheme, ObjectId rev)
specifier|public
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
name|String
name|scheme
parameter_list|,
name|ObjectId
name|rev
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|byAccount
argument_list|(
name|accountId
argument_list|,
name|rev
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|e
operator|.
name|key
argument_list|()
operator|.
name|isScheme
argument_list|(
name|scheme
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
return|;
block|}
comment|/** Returns all external IDs by account. */
DECL|method|allByAccount ()
specifier|public
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
block|{
return|return
name|externalIdCache
operator|.
name|allByAccount
argument_list|()
return|;
block|}
comment|/**    * Returns the external ID with the given email.    *    *<p>Each email should belong to a single external ID only. This means if more than one external    * ID is returned there is an inconsistency in the external IDs.    *    *<p>The external IDs are retrieved from the external ID cache. Each access to the external ID    * cache requires reading the SHA1 of the refs/meta/external-ids branch. If external IDs for    * multiple emails are needed it is more efficient to use {@link #byEmails(String...)} as this    * method reads the SHA1 of the refs/meta/external-ids branch only once (and not once per email).    *    * @see #byEmails(String...)    */
DECL|method|byEmail (String email)
specifier|public
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
name|externalIdCache
operator|.
name|byEmail
argument_list|(
name|email
argument_list|)
return|;
block|}
comment|/**    * Returns the external IDs for the given emails.    *    *<p>Each email should belong to a single external ID only. This means if more than one external    * ID for an email is returned there is an inconsistency in the external IDs.    *    *<p>The external IDs are retrieved from the external ID cache. Each access to the external ID    * cache requires reading the SHA1 of the refs/meta/external-ids branch. If external IDs for    * multiple emails are needed it is more efficient to use this method instead of {@link    * #byEmail(String)} as this method reads the SHA1 of the refs/meta/external-ids branch only once    * (and not once per email).    *    * @see #byEmail(String)    */
DECL|method|byEmails (String... emails)
specifier|public
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
block|{
return|return
name|externalIdCache
operator|.
name|byEmails
argument_list|(
name|emails
argument_list|)
return|;
block|}
comment|/** Returns all external IDs by email. */
DECL|method|allByEmail ()
specifier|public
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
block|{
return|return
name|externalIdCache
operator|.
name|allByEmail
argument_list|()
return|;
block|}
block|}
end_class

end_unit

