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
name|Column
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

begin_comment
comment|/**  * Information about a single user.  *<p>  * A user may have multiple identities they can use to login to Gerrit (see  * {@link AccountExternalId}), but in such cases they always map back to a  * single Account entity.  *<p>  * Entities "owned" by an Account (that is, their primary key contains the  * {@link Account.Id} key as part of their key structure):  *<ul>  *<li>{@link AccountAgreement}: any record of the user's acceptance of a  * predefined {@link ContributorAgreement}. Multiple records indicate  * potentially multiple agreements, especially if agreements must be retired and  * replaced with new agreements.</li>  *  *<li>{@link AccountExternalId}: OpenID identities and email addresses known to  * be registered to this user. Multiple records can exist when the user has more  * than one public identity, such as a work and a personal email address.</li>  *  *<li>{@link AccountGroupMember}: membership of the user in a specific human  * managed {@link AccountGroup}. Multiple records can exist when the user is a  * member of more than one group.</li>  *  *<li>{@link AccountProjectWatch}: user's email settings related to a specific  * {@link Project}. One record per project the user is interested in tracking.</li>  *  *<li>{@link AccountSshKey}: user's public SSH keys, for authentication through  * the internal SSH daemon. One record per SSH key uploaded by the user, keys  * are checked in random order until a match is found.</li>  *  *<li>{@link StarredChange}: user has starred the change, tracking  * notifications of updates on that change, or just book-marking it for faster  * future reference. One record per starred change.</li>  *  *<li>{@link AccountDiffPreference}: user's preferences for rendering side-to-side  * and unified diff</li>  *  *</ul>  */
end_comment

begin_class
DECL|class|Account
specifier|public
specifier|final
class|class
name|Account
block|{
DECL|enum|FieldName
specifier|public
specifier|static
enum|enum
name|FieldName
block|{
DECL|enumConstant|FULL_NAME
DECL|enumConstant|USER_NAME
DECL|enumConstant|REGISTER_NEW_EMAIL
name|FULL_NAME
block|,
name|USER_NAME
block|,
name|REGISTER_NEW_EMAIL
block|;   }
DECL|field|USER_NAME_PATTERN_FIRST
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME_PATTERN_FIRST
init|=
literal|"[a-zA-Z]"
decl_stmt|;
DECL|field|USER_NAME_PATTERN_REST
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME_PATTERN_REST
init|=
literal|"[a-zA-Z0-9._-]"
decl_stmt|;
DECL|field|USER_NAME_PATTERN_LAST
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME_PATTERN_LAST
init|=
literal|"[a-zA-Z0-9]"
decl_stmt|;
comment|/** Regular expression that {@link #userName} must match. */
DECL|field|USER_NAME_PATTERN
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME_PATTERN
init|=
literal|"^"
operator|+
comment|//
literal|"("
operator|+
comment|//
name|USER_NAME_PATTERN_FIRST
operator|+
comment|//
name|USER_NAME_PATTERN_REST
operator|+
literal|"*"
operator|+
comment|//
name|USER_NAME_PATTERN_LAST
operator|+
comment|//
literal|"|"
operator|+
comment|//
name|USER_NAME_PATTERN_FIRST
operator|+
comment|//
literal|")"
operator|+
comment|//
literal|"$"
decl_stmt|;
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
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|id
specifier|protected
name|int
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{     }
DECL|method|Id (final int id)
specifier|public
name|Id
parameter_list|(
specifier|final
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
DECL|method|parse (final String str)
specifier|public
specifier|static
name|Id
name|parse
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
specifier|final
name|Id
name|r
init|=
operator|new
name|Id
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|accountId
specifier|protected
name|Id
name|accountId
decl_stmt|;
comment|/** Date and time the user registered with the review server. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|registeredOn
specifier|protected
name|Timestamp
name|registeredOn
decl_stmt|;
comment|/** Full name of the user ("Given-name Surname" style). */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|fullName
specifier|protected
name|String
name|fullName
decl_stmt|;
comment|/** Email address the user prefers to be contacted through. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|preferredEmail
specifier|protected
name|String
name|preferredEmail
decl_stmt|;
comment|/** When did the user last give us contact information? Null if never. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|5
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|contactFiledOn
specifier|protected
name|Timestamp
name|contactFiledOn
decl_stmt|;
comment|/** This user's preferences */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|6
argument_list|,
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|generalPreferences
specifier|protected
name|AccountGeneralPreferences
name|generalPreferences
decl_stmt|;
comment|/** Is this user active */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|7
argument_list|)
DECL|field|inactive
specifier|protected
name|boolean
name|inactive
decl_stmt|;
comment|/**<i>computed</i> the username selected from the identities. */
DECL|field|userName
specifier|protected
name|String
name|userName
decl_stmt|;
DECL|method|Account ()
specifier|protected
name|Account
parameter_list|()
block|{   }
comment|/**    * Create a new account.    *    * @param newId unique id, see {@link ReviewDb#nextAccountId()}.    */
DECL|method|Account (final Account.Id newId)
specifier|public
name|Account
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|newId
parameter_list|)
block|{
name|accountId
operator|=
name|newId
expr_stmt|;
name|registeredOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|generalPreferences
operator|=
operator|new
name|AccountGeneralPreferences
argument_list|()
expr_stmt|;
name|generalPreferences
operator|.
name|resetToDefaults
argument_list|()
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
DECL|method|setFullName (final String name)
specifier|public
name|void
name|setFullName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|fullName
operator|=
name|name
expr_stmt|;
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
DECL|method|setPreferredEmail (final String addr)
specifier|public
name|void
name|setPreferredEmail
parameter_list|(
specifier|final
name|String
name|addr
parameter_list|)
block|{
name|preferredEmail
operator|=
name|addr
expr_stmt|;
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
DECL|method|getGeneralPreferences ()
specifier|public
name|AccountGeneralPreferences
name|getGeneralPreferences
parameter_list|()
block|{
return|return
name|generalPreferences
return|;
block|}
DECL|method|setGeneralPreferences (final AccountGeneralPreferences p)
specifier|public
name|void
name|setGeneralPreferences
parameter_list|(
specifier|final
name|AccountGeneralPreferences
name|p
parameter_list|)
block|{
name|generalPreferences
operator|=
name|p
expr_stmt|;
block|}
DECL|method|isContactFiled ()
specifier|public
name|boolean
name|isContactFiled
parameter_list|()
block|{
return|return
name|contactFiledOn
operator|!=
literal|null
return|;
block|}
DECL|method|getContactFiledOn ()
specifier|public
name|Timestamp
name|getContactFiledOn
parameter_list|()
block|{
return|return
name|contactFiledOn
return|;
block|}
DECL|method|setContactFiled ()
specifier|public
name|void
name|setContactFiled
parameter_list|()
block|{
name|contactFiledOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
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
comment|/** @return the computed user name for this account */
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
comment|/** Update the computed user name property. */
DECL|method|setUserName (final String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
specifier|final
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
block|}
end_class

end_unit

