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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_DRAFT_COMMENTS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_STARRED_CHANGES
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_USERS
import|;
end_import

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
name|common
operator|.
name|primitives
operator|.
name|Ints
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
name|common
operator|.
name|Nullable
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
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * Information about a single user.  *  *<p>A user may have multiple identities they can use to login to Gerrit (see ExternalId), but in  * such cases they always map back to a single Account entity.  *  *<p>Entities "owned" by an Account (that is, their primary key contains the {@link Account.Id} key  * as part of their key structure):  *  *<ul>  *<li>ExternalId: OpenID identities and email addresses known to be registered to this user.  *       Multiple records can exist when the user has more than one public identity, such as a work  *       and a personal email address.  *<li>AccountSshKey: user's public SSH keys, for authentication through the internal SSH daemon.  *       One record per SSH key uploaded by the user, keys are checked in random order until a match  *       is found.  *<li>{@link DiffPreferencesInfo}: user's preferences for rendering side-to-side and unified diff  *</ul>  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|Account
specifier|public
specifier|abstract
class|class
name|Account
block|{
DECL|method|id (int id)
specifier|public
specifier|static
name|Id
name|id
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_Account_Id
argument_list|(
name|id
argument_list|)
return|;
block|}
comment|/** Key local to Gerrit to identify a user. */
annotation|@
name|AutoValue
DECL|class|Id
specifier|public
specifier|abstract
specifier|static
class|class
name|Id
implements|implements
name|Comparable
argument_list|<
name|Id
argument_list|>
block|{
comment|/** Parse an Account.Id out of a string representation. */
DECL|method|tryParse (String str)
specifier|public
specifier|static
name|Optional
argument_list|<
name|Id
argument_list|>
name|tryParse
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|Ints
operator|.
name|tryParse
argument_list|(
name|str
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Account
operator|::
name|id
argument_list|)
return|;
block|}
DECL|method|fromRef (String name)
specifier|public
specifier|static
name|Id
name|fromRef
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|REFS_USERS
argument_list|)
condition|)
block|{
return|return
name|fromRefPart
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|REFS_USERS
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|REFS_DRAFT_COMMENTS
argument_list|)
condition|)
block|{
return|return
name|parseAfterShardedRefPart
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|REFS_DRAFT_COMMENTS
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|REFS_STARRED_CHANGES
argument_list|)
condition|)
block|{
return|return
name|parseAfterShardedRefPart
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|REFS_STARRED_CHANGES
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Parse an Account.Id out of a part of a ref-name.      *      * @param name a ref name with the following syntax: {@code "34/1234..."}. We assume that the      *     caller has trimmed any prefix.      */
DECL|method|fromRefPart (String name)
specifier|public
specifier|static
name|Id
name|fromRefPart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Integer
name|id
init|=
name|RefNames
operator|.
name|parseShardedRefPart
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|id
operator|!=
literal|null
condition|?
name|Account
operator|.
name|id
argument_list|(
name|id
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|parseAfterShardedRefPart (String name)
specifier|public
specifier|static
name|Id
name|parseAfterShardedRefPart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Integer
name|id
init|=
name|RefNames
operator|.
name|parseAfterShardedRefPart
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|id
operator|!=
literal|null
condition|?
name|Account
operator|.
name|id
argument_list|(
name|id
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Parse an Account.Id out of the last part of a ref name.      *      *<p>The input is a ref name of the form {@code ".../1234"}, where the suffix is a non-sharded      * account ID. Ref names using a sharded ID should use {@link #fromRefPart(String)} instead for      * greater safety.      *      * @param name ref name      * @return account ID, or null if not numeric.      */
DECL|method|fromRefSuffix (String name)
specifier|public
specifier|static
name|Id
name|fromRefSuffix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Integer
name|id
init|=
name|RefNames
operator|.
name|parseRefSuffix
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|id
operator|!=
literal|null
condition|?
name|Account
operator|.
name|id
argument_list|(
name|id
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|id ()
specifier|abstract
name|int
name|id
parameter_list|()
function_decl|;
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|id
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (Id o)
specifier|public
specifier|final
name|int
name|compareTo
parameter_list|(
name|Id
name|o
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|compare
argument_list|(
name|id
argument_list|()
argument_list|,
name|o
operator|.
name|id
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|id ()
specifier|public
specifier|abstract
name|Id
name|id
parameter_list|()
function_decl|;
comment|/** Date and time the user registered with the review server. */
DECL|method|registeredOn ()
specifier|public
specifier|abstract
name|Timestamp
name|registeredOn
parameter_list|()
function_decl|;
comment|/** Full name of the user ("Given-name Surname" style). */
annotation|@
name|Nullable
DECL|method|fullName ()
specifier|public
specifier|abstract
name|String
name|fullName
parameter_list|()
function_decl|;
comment|/** Email address the user prefers to be contacted through. */
annotation|@
name|Nullable
DECL|method|preferredEmail ()
specifier|public
specifier|abstract
name|String
name|preferredEmail
parameter_list|()
function_decl|;
comment|/**    * Is this user inactive? This is used to avoid showing some users (eg. former employees) in    * auto-suggest.    */
DECL|method|inactive ()
specifier|public
specifier|abstract
name|boolean
name|inactive
parameter_list|()
function_decl|;
comment|/** The user-settable status of this account (e.g. busy, OOO, available) */
annotation|@
name|Nullable
DECL|method|status ()
specifier|public
specifier|abstract
name|String
name|status
parameter_list|()
function_decl|;
comment|/** ID of the user branch from which the account was read. */
annotation|@
name|Nullable
DECL|method|metaId ()
specifier|public
specifier|abstract
name|String
name|metaId
parameter_list|()
function_decl|;
comment|/**    * Create a new account.    *    * @param newId unique id, see {@link com.google.gerrit.server.notedb.Sequences#nextAccountId()}.    * @param registeredOn when the account was registered.    */
DECL|method|builder (Account.Id newId, Timestamp registeredOn)
specifier|public
specifier|static
name|Account
operator|.
name|Builder
name|builder
parameter_list|(
name|Account
operator|.
name|Id
name|newId
parameter_list|,
name|Timestamp
name|registeredOn
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_Account
operator|.
name|Builder
argument_list|()
operator|.
name|setInactive
argument_list|(
literal|false
argument_list|)
operator|.
name|setId
argument_list|(
name|newId
argument_list|)
operator|.
name|setRegisteredOn
argument_list|(
name|registeredOn
argument_list|)
return|;
block|}
comment|/**    * Formats an account name.    *    *<p>The return value goes into NoteDb commits and audit logs, so it should not be changed.    *    *<p>This method deliberately does not use {@code Anonymous Coward} because it can be changed    * using a {@code gerrit.config} option which is a problem for NoteDb commits that still refer to    * a previously defined value.    *    * @return the fullname, if present, otherwise the preferred email, if present, as a last resort a    *     generic string containing the accountId.    */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|fullName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|fullName
argument_list|()
return|;
block|}
if|if
condition|(
name|preferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|preferredEmail
argument_list|()
return|;
block|}
return|return
name|getName
argument_list|(
name|id
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getName (Account.Id accountId)
specifier|public
specifier|static
name|String
name|getName
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
literal|"GerritAccount #"
operator|+
name|accountId
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * Get the name and email address.    *    *<p>Example output:    *    *<ul>    *<li>{@code A U. Thor&lt;author@example.com&gt;}: full populated    *<li>{@code A U. Thor (12)}: missing email address    *<li>{@code Anonymous Coward&lt;author@example.com&gt;}: missing name    *<li>{@code Anonymous Coward (12)}: missing name and email address    *</ul>    */
DECL|method|getNameEmail (String anonymousCowardName)
specifier|public
name|String
name|getNameEmail
parameter_list|(
name|String
name|anonymousCowardName
parameter_list|)
block|{
name|String
name|name
init|=
name|fullName
argument_list|()
operator|!=
literal|null
condition|?
name|fullName
argument_list|()
else|:
name|anonymousCowardName
decl_stmt|;
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|preferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|preferredEmail
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
operator|!
name|inactive
argument_list|()
return|;
block|}
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
DECL|method|id ()
specifier|public
specifier|abstract
name|Id
name|id
parameter_list|()
function_decl|;
DECL|method|setId (Id id)
specifier|abstract
name|Builder
name|setId
parameter_list|(
name|Id
name|id
parameter_list|)
function_decl|;
DECL|method|registeredOn ()
specifier|public
specifier|abstract
name|Timestamp
name|registeredOn
parameter_list|()
function_decl|;
DECL|method|setRegisteredOn (Timestamp registeredOn)
specifier|abstract
name|Builder
name|setRegisteredOn
parameter_list|(
name|Timestamp
name|registeredOn
parameter_list|)
function_decl|;
annotation|@
name|Nullable
DECL|method|fullName ()
specifier|public
specifier|abstract
name|String
name|fullName
parameter_list|()
function_decl|;
DECL|method|setFullName (String fullName)
specifier|public
specifier|abstract
name|Builder
name|setFullName
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
annotation|@
name|Nullable
DECL|method|preferredEmail ()
specifier|public
specifier|abstract
name|String
name|preferredEmail
parameter_list|()
function_decl|;
DECL|method|setPreferredEmail (String preferredEmail)
specifier|public
specifier|abstract
name|Builder
name|setPreferredEmail
parameter_list|(
name|String
name|preferredEmail
parameter_list|)
function_decl|;
DECL|method|inactive ()
specifier|public
specifier|abstract
name|boolean
name|inactive
parameter_list|()
function_decl|;
DECL|method|setInactive (boolean inactive)
specifier|public
specifier|abstract
name|Builder
name|setInactive
parameter_list|(
name|boolean
name|inactive
parameter_list|)
function_decl|;
DECL|method|setActive (boolean active)
specifier|public
name|Builder
name|setActive
parameter_list|(
name|boolean
name|active
parameter_list|)
block|{
return|return
name|setInactive
argument_list|(
operator|!
name|active
argument_list|)
return|;
block|}
annotation|@
name|Nullable
DECL|method|status ()
specifier|public
specifier|abstract
name|String
name|status
parameter_list|()
function_decl|;
DECL|method|setStatus (String status)
specifier|public
specifier|abstract
name|Builder
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
function_decl|;
annotation|@
name|Nullable
DECL|method|metaId ()
specifier|public
specifier|abstract
name|String
name|metaId
parameter_list|()
function_decl|;
DECL|method|setMetaId (@ullable String metaId)
specifier|public
specifier|abstract
name|Builder
name|setMetaId
parameter_list|(
annotation|@
name|Nullable
name|String
name|metaId
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|Account
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

