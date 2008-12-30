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
name|ResultSet
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
name|List
import|;
end_import

begin_comment
comment|/** Preferences and information about a single user. */
end_comment

begin_class
DECL|class|Account
specifier|public
specifier|final
class|class
name|Account
block|{
comment|/** Default number of lines of context. */
DECL|field|DEFAULT_CONTEXT
specifier|public
specifier|static
specifier|final
name|short
name|DEFAULT_CONTEXT
init|=
literal|10
decl_stmt|;
comment|/** Typical valid choices for the default context setting. */
DECL|field|CONTEXT_CHOICES
specifier|public
specifier|static
specifier|final
name|short
index|[]
name|CONTEXT_CHOICES
init|=
block|{
literal|3
block|,
literal|10
block|,
literal|25
block|,
literal|50
block|,
literal|75
block|,
literal|100
block|}
decl_stmt|;
comment|/**    * Locate exactly one account matching the name or name/email string.    *     * @param db open database handle to use for the query.    * @param nameOrEmail a string of the format    *        "Full Name&lt;email@example&gt;", or just the preferred email    *        address ("email@example"), or a full name.    * @return the single account that matches; null if no account matches or    *         there are multiple candidates.    */
DECL|method|find (final ReviewDb db, final String nameOrEmail)
specifier|public
specifier|static
name|Account
name|find
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|String
name|nameOrEmail
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|int
name|lt
init|=
name|nameOrEmail
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
decl_stmt|;
specifier|final
name|int
name|gt
init|=
name|nameOrEmail
operator|.
name|indexOf
argument_list|(
literal|'>'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lt
operator|>=
literal|0
operator|&&
name|gt
operator|>
name|lt
condition|)
block|{
specifier|final
name|String
name|email
init|=
name|nameOrEmail
operator|.
name|substring
argument_list|(
name|lt
operator|+
literal|1
argument_list|,
name|gt
argument_list|)
decl_stmt|;
return|return
name|one
argument_list|(
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|byPreferredEmail
argument_list|(
name|email
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|nameOrEmail
operator|.
name|contains
argument_list|(
literal|"@"
argument_list|)
condition|)
block|{
return|return
name|one
argument_list|(
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|byPreferredEmail
argument_list|(
name|nameOrEmail
argument_list|)
argument_list|)
return|;
block|}
return|return
name|one
argument_list|(
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|suggestByFullName
argument_list|(
name|nameOrEmail
argument_list|,
name|nameOrEmail
argument_list|,
literal|2
argument_list|)
argument_list|)
return|;
block|}
DECL|method|one (final ResultSet<Account> rs)
specifier|private
specifier|static
name|Account
name|one
parameter_list|(
specifier|final
name|ResultSet
argument_list|<
name|Account
argument_list|>
name|rs
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|Account
argument_list|>
name|r
init|=
name|rs
operator|.
name|toList
argument_list|()
decl_stmt|;
return|return
name|r
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
name|r
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
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
annotation|@
name|Column
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
DECL|field|accountId
specifier|protected
name|Id
name|accountId
decl_stmt|;
comment|/** Date and time the user registered with the review server. */
annotation|@
name|Column
DECL|field|registeredOn
specifier|protected
name|Timestamp
name|registeredOn
decl_stmt|;
comment|/** Full name of the user ("Given-name Surname" style). */
annotation|@
name|Column
argument_list|(
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
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|preferredEmail
specifier|protected
name|String
name|preferredEmail
decl_stmt|;
comment|/** Default number of lines of context when viewing a patch. */
annotation|@
name|Column
DECL|field|defaultContext
specifier|protected
name|short
name|defaultContext
decl_stmt|;
comment|/** Non-Internet based contact details for the account's owner. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|contact
specifier|protected
name|ContactInformation
name|contact
decl_stmt|;
DECL|method|Account ()
specifier|protected
name|Account
parameter_list|()
block|{   }
comment|/**    * Create a new account.    *     * @param newId unique id, see {@link ReviewDb#nextAccountId()}.    */
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
name|defaultContext
operator|=
name|DEFAULT_CONTEXT
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
comment|/** Get the default number of lines of context when viewing a patch. */
DECL|method|getDefaultContext ()
specifier|public
name|short
name|getDefaultContext
parameter_list|()
block|{
return|return
name|defaultContext
return|;
block|}
comment|/** Set the number of lines of context when viewing a patch. */
DECL|method|setDefaultContext (final short s)
specifier|public
name|void
name|setDefaultContext
parameter_list|(
specifier|final
name|short
name|s
parameter_list|)
block|{
name|defaultContext
operator|=
name|s
expr_stmt|;
block|}
DECL|method|getContactInformation ()
specifier|public
name|ContactInformation
name|getContactInformation
parameter_list|()
block|{
return|return
name|contact
return|;
block|}
DECL|method|setContactInformation (final ContactInformation i)
specifier|public
name|void
name|setContactInformation
parameter_list|(
specifier|final
name|ContactInformation
name|i
parameter_list|)
block|{
name|contact
operator|=
name|i
expr_stmt|;
block|}
block|}
end_class

end_unit

