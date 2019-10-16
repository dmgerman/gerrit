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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|server
operator|.
name|account
operator|.
name|GroupMembership
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|servlet
operator|.
name|RequestScoped
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
name|function
operator|.
name|Consumer
import|;
end_import

begin_comment
comment|/**  * Information about the currently logged in user.  *  *<p>This is a {@link RequestScoped} property managed by Guice.  *  * @see AnonymousUser  * @see IdentifiedUser  */
end_comment

begin_class
DECL|class|CurrentUser
specifier|public
specifier|abstract
class|class
name|CurrentUser
block|{
comment|/** Unique key for plugin/extension specific data on a CurrentUser. */
DECL|class|PropertyKey
specifier|public
specifier|static
specifier|final
class|class
name|PropertyKey
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|PropertyKey
argument_list|<
name|T
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|PropertyKey
argument_list|<>
argument_list|()
return|;
block|}
DECL|method|PropertyKey ()
specifier|private
name|PropertyKey
parameter_list|()
block|{}
block|}
DECL|field|accessPath
specifier|private
name|AccessPath
name|accessPath
init|=
name|AccessPath
operator|.
name|UNKNOWN
decl_stmt|;
DECL|field|lastLoginExternalIdPropertyKey
specifier|private
name|PropertyKey
argument_list|<
name|ExternalId
operator|.
name|Key
argument_list|>
name|lastLoginExternalIdPropertyKey
init|=
name|PropertyKey
operator|.
name|create
argument_list|()
decl_stmt|;
comment|/** How this user is accessing the Gerrit Code Review application. */
DECL|method|getAccessPath ()
specifier|public
specifier|final
name|AccessPath
name|getAccessPath
parameter_list|()
block|{
return|return
name|accessPath
return|;
block|}
DECL|method|setAccessPath (AccessPath path)
specifier|public
name|void
name|setAccessPath
parameter_list|(
name|AccessPath
name|path
parameter_list|)
block|{
name|accessPath
operator|=
name|path
expr_stmt|;
block|}
comment|/**    * Identity of the authenticated user.    *    *<p>In the normal case where a user authenticates as themselves {@code getRealUser() == this}.    *    *<p>If {@code X-Gerrit-RunAs} or {@code suexec} was used this method returns the identity of the    * account that has permission to act on behalf of this user.    */
DECL|method|getRealUser ()
specifier|public
name|CurrentUser
name|getRealUser
parameter_list|()
block|{
return|return
name|this
return|;
block|}
DECL|method|isImpersonating ()
specifier|public
name|boolean
name|isImpersonating
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**    * If the {@link #getRealUser()} has an account ID associated with it, call the given setter with    * that ID.    */
DECL|method|updateRealAccountId (Consumer<Account.Id> setter)
specifier|public
name|void
name|updateRealAccountId
parameter_list|(
name|Consumer
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|setter
parameter_list|)
block|{
if|if
condition|(
name|getRealUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|setter
operator|.
name|accept
argument_list|(
name|getRealUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Get the set of groups the user is currently a member of.    *    *<p>The returned set may be a subset of the user's actual groups; if the user's account is    * currently deemed to be untrusted then the effective group set is only the anonymous and    * registered user groups. To enable additional groups (and gain their granted permissions) the    * user must update their account to use only trusted authentication providers.    *    * @return active groups for this user.    */
DECL|method|getEffectiveGroups ()
specifier|public
specifier|abstract
name|GroupMembership
name|getEffectiveGroups
parameter_list|()
function_decl|;
comment|/**    * Returns a unique identifier for this user that is intended to be used as a cache key. Returned    * object should to implement {@code equals()} and {@code hashCode()} for effective caching.    */
DECL|method|getCacheKey ()
specifier|public
specifier|abstract
name|Object
name|getCacheKey
parameter_list|()
function_decl|;
comment|/** Unique name of the user on this server, if one has been assigned. */
DECL|method|getUserName ()
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|getUserName
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/** @return unique name of the user for logging, never {@code null} */
DECL|method|getLoggableName ()
specifier|public
name|String
name|getLoggableName
parameter_list|()
block|{
return|return
name|getUserName
argument_list|()
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
return|;
block|}
comment|/** Check if user is the IdentifiedUser */
DECL|method|isIdentifiedUser ()
specifier|public
name|boolean
name|isIdentifiedUser
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** Cast to IdentifiedUser if possible. */
DECL|method|asIdentifiedUser ()
specifier|public
name|IdentifiedUser
name|asIdentifiedUser
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" is not an IdentifiedUser"
argument_list|)
throw|;
block|}
comment|/**    * Return account ID if {@link #isIdentifiedUser} is true.    *    * @throws UnsupportedOperationException if the user is not logged in.    */
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" is not an IdentifiedUser"
argument_list|)
throw|;
block|}
comment|/** Check if the CurrentUser is an InternalUser. */
DECL|method|isInternalUser ()
specifier|public
name|boolean
name|isInternalUser
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**    * Lookup a previously stored property.    *    * @param key unique property key.    * @return previously stored value, or {@code Optional#empty()}.    */
DECL|method|get (PropertyKey<T> key)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|get
parameter_list|(
name|PropertyKey
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/**    * Store a property for later retrieval.    *    * @param key unique property key.    * @param value value to store; or {@code null} to clear the value.    */
DECL|method|put (PropertyKey<T> key, @Nullable T value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|put
parameter_list|(
name|PropertyKey
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
annotation|@
name|Nullable
name|T
name|value
parameter_list|)
block|{}
DECL|method|setLastLoginExternalIdKey (ExternalId.Key externalIdKey)
specifier|public
name|void
name|setLastLoginExternalIdKey
parameter_list|(
name|ExternalId
operator|.
name|Key
name|externalIdKey
parameter_list|)
block|{
name|put
argument_list|(
name|lastLoginExternalIdPropertyKey
argument_list|,
name|externalIdKey
argument_list|)
expr_stmt|;
block|}
DECL|method|getLastLoginExternalIdKey ()
specifier|public
name|Optional
argument_list|<
name|ExternalId
operator|.
name|Key
argument_list|>
name|getLastLoginExternalIdKey
parameter_list|()
block|{
return|return
name|get
argument_list|(
name|lastLoginExternalIdPropertyKey
argument_list|)
return|;
block|}
comment|/**    * Checks if the current user has the same account id of another.    *    *<p>Provide a generic interface for allowing subclasses to define whether two accounts represent    * the same account id.    *    * @param other user to compare    * @return true if the two users have the same account id    */
DECL|method|hasSameAccountId (CurrentUser other)
specifier|public
name|boolean
name|hasSameAccountId
parameter_list|(
name|CurrentUser
name|other
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

