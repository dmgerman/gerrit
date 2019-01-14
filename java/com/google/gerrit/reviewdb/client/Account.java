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
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|IntKey
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
comment|/**  * Information about a single user.  *  *<p>A user may have multiple identities they can use to login to Gerrit (see ExternalId), but in  * such cases they always map back to a single Account entity.  *  *<p>Entities "owned" by an Account (that is, their primary key contains the {@link Account.Id} key  * as part of their key structure):  *  *<ul>  *<li>ExternalId: OpenID identities and email addresses known to be registered to this user.  *       Multiple records can exist when the user has more than one public identity, such as a work  *       and a personal email address.  *<li>{@link AccountGroupMember}: membership of the user in a specific human managed {@link  *       AccountGroup}. Multiple records can exist when the user is a member of more than one group.  *<li>AccountSshKey: user's public SSH keys, for authentication through the internal SSH daemon.  *       One record per SSH key uploaded by the user, keys are checked in random order until a match  *       is found.  *<li>{@link DiffPreferencesInfo}: user's preferences for rendering side-to-side and unified diff  *</ul>  */
end_comment

begin_class
DECL|class|Account
specifier|public
specifier|final
class|class
name|Account
block|{
comment|/** Key local to Gerrit to identify a user. */
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|IntKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|id
specifier|protected
name|int
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{}
DECL|method|Id (int id)
specifier|public
name|Id
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|set (int newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|int
name|newValue
parameter_list|)
block|{
name|id
operator|=
name|newValue
expr_stmt|;
block|}
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
try|try
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|Id
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|str
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
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
operator|new
name|Account
operator|.
name|Id
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
operator|new
name|Account
operator|.
name|Id
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
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|id
argument_list|)
else|:
literal|null
return|;
block|}
block|}
DECL|field|accountId
specifier|private
name|Id
name|accountId
decl_stmt|;
comment|/** Date and time the user registered with the review server. */
DECL|field|registeredOn
specifier|private
name|Timestamp
name|registeredOn
decl_stmt|;
comment|/** Full name of the user ("Given-name Surname" style). */
DECL|field|fullName
specifier|private
name|String
name|fullName
decl_stmt|;
comment|/** Email address the user prefers to be contacted through. */
DECL|field|preferredEmail
specifier|private
name|String
name|preferredEmail
decl_stmt|;
comment|/**    * Is this user inactive? This is used to avoid showing some users (eg. former employees) in    * auto-suggest.    */
DECL|field|inactive
specifier|private
name|boolean
name|inactive
decl_stmt|;
comment|/** The user-settable status of this account (e.g. busy, OOO, available) */
DECL|field|status
specifier|private
name|String
name|status
decl_stmt|;
comment|/** ID of the user branch from which the account was read. */
DECL|field|metaId
specifier|private
name|String
name|metaId
decl_stmt|;
DECL|method|Account ()
specifier|protected
name|Account
parameter_list|()
block|{}
comment|/**    * Create a new account.    *    * @param newId unique id, see {@link com.google.gerrit.server.notedb.Sequences#nextAccountId()}.    * @param registeredOn when the account was registered.    */
DECL|method|Account (Account.Id newId, Timestamp registeredOn)
specifier|public
name|Account
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
name|this
operator|.
name|accountId
operator|=
name|newId
expr_stmt|;
name|this
operator|.
name|registeredOn
operator|=
name|registeredOn
expr_stmt|;
block|}
comment|/** Get local id of this account, to link with in other entities */
DECL|method|getId ()
specifier|public
name|Account
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
comment|/** Get the full name of the user ("Given-name Surname" style). */
DECL|method|getFullName ()
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
comment|/** Set the full name of the user ("Given-name Surname" style). */
DECL|method|setFullName (String name)
specifier|public
name|void
name|setFullName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|fullName
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|fullName
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/** Email address the user prefers to be contacted through. */
DECL|method|getPreferredEmail ()
specifier|public
name|String
name|getPreferredEmail
parameter_list|()
block|{
return|return
name|preferredEmail
return|;
block|}
comment|/** Set the email address the user prefers to be contacted through. */
DECL|method|setPreferredEmail (String addr)
specifier|public
name|void
name|setPreferredEmail
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
name|preferredEmail
operator|=
name|addr
expr_stmt|;
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
operator|!=
literal|null
condition|)
block|{
return|return
name|fullName
return|;
block|}
if|if
condition|(
name|preferredEmail
operator|!=
literal|null
condition|)
block|{
return|return
name|preferredEmail
return|;
block|}
return|return
name|getName
argument_list|(
name|accountId
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
operator|!=
literal|null
condition|?
name|fullName
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
name|accountId
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
comment|/** Get the date and time the user first registered. */
DECL|method|getRegisteredOn ()
specifier|public
name|Timestamp
name|getRegisteredOn
parameter_list|()
block|{
return|return
name|registeredOn
return|;
block|}
DECL|method|getMetaId ()
specifier|public
name|String
name|getMetaId
parameter_list|()
block|{
return|return
name|metaId
return|;
block|}
DECL|method|setMetaId (String metaId)
specifier|public
name|void
name|setMetaId
parameter_list|(
name|String
name|metaId
parameter_list|)
block|{
name|this
operator|.
name|metaId
operator|=
name|metaId
expr_stmt|;
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
return|;
block|}
DECL|method|setActive (boolean active)
specifier|public
name|void
name|setActive
parameter_list|(
name|boolean
name|active
parameter_list|)
block|{
name|inactive
operator|=
operator|!
name|active
expr_stmt|;
block|}
DECL|method|getStatus ()
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
DECL|method|setStatus (String status)
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|Account
operator|&&
operator|(
operator|(
name|Account
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

