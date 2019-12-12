begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
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
name|AccountFieldName
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
name|server
operator|.
name|IdentifiedUser
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
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalId
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
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
import|;
end_import

begin_comment
comment|/**  * Interface between Gerrit and an account system.  *  *<p>This interface provides the glue layer between the Gerrit and external account/authentication  * systems (eg. LDAP, OpenID).  */
end_comment

begin_interface
DECL|interface|Realm
specifier|public
interface|interface
name|Realm
block|{
comment|/** Can the end-user modify this field of their own account? */
DECL|method|allowsEdit (AccountFieldName field)
name|boolean
name|allowsEdit
parameter_list|(
name|AccountFieldName
name|field
parameter_list|)
function_decl|;
comment|/** Returns the account fields that the end-user can modify. */
DECL|method|getEditableFields ()
name|Set
argument_list|<
name|AccountFieldName
argument_list|>
name|getEditableFields
parameter_list|()
function_decl|;
DECL|method|authenticate (AuthRequest who)
name|AuthRequest
name|authenticate
parameter_list|(
name|AuthRequest
name|who
parameter_list|)
throws|throws
name|AccountException
function_decl|;
DECL|method|onCreateAccount (AuthRequest who, Account account)
name|void
name|onCreateAccount
parameter_list|(
name|AuthRequest
name|who
parameter_list|,
name|Account
name|account
parameter_list|)
function_decl|;
comment|/** @return true if the user has the given email address. */
DECL|method|hasEmailAddress (IdentifiedUser who, String email)
name|boolean
name|hasEmailAddress
parameter_list|(
name|IdentifiedUser
name|who
parameter_list|,
name|String
name|email
parameter_list|)
function_decl|;
comment|/** @return all known email addresses for the identified user. */
DECL|method|getEmailAddresses (IdentifiedUser who)
name|Set
argument_list|<
name|String
argument_list|>
name|getEmailAddresses
parameter_list|(
name|IdentifiedUser
name|who
parameter_list|)
function_decl|;
comment|/**    * Locate an account whose local username is the given account name.    *    *<p>Generally this only works for local realms, such as one backed by an LDAP directory, or    * where there is an {@link EmailExpander} configured that knows how to convert the accountName    * into an email address, and then locate the user by that email address.    */
DECL|method|lookup (String accountName)
name|Account
operator|.
name|Id
name|lookup
parameter_list|(
name|String
name|accountName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * @return true if the account is active.    * @throws NamingException    * @throws LoginException    * @throws AccountException    * @throws IOException    */
DECL|method|isActive (@uppressWarningsR) String username)
specifier|default
name|boolean
name|isActive
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|String
name|username
parameter_list|)
throws|throws
name|LoginException
throws|,
name|NamingException
throws|,
name|AccountException
throws|,
name|IOException
block|{
return|return
literal|true
return|;
block|}
comment|/** @return true if the account is backed by the realm, false otherwise. */
DECL|method|accountBelongsToRealm ( @uppressWarningsR) Collection<ExternalId> externalIds)
specifier|default
name|boolean
name|accountBelongsToRealm
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_interface

end_unit

