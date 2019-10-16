begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.quota
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|quota
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
name|entities
operator|.
name|Change
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
name|Project
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
name|CurrentUser
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
name|ImplementedBy
import|;
end_import

begin_comment
comment|/**  * Backend interface to perform quota requests on. By default, this interface is backed by {@link  * DefaultQuotaBackend} which calls all plugins that implement {@link QuotaEnforcer}. A different  * implementation might be bound in tests. Plugins are not supposed to implement this interface, but  * bind a {@link QuotaEnforcer} implementation instead.  *  *<p>All quota requests require a quota group and a user. Enriching them with a top-level entity  * {@code Change, Project, Account} is optional but should be done if the request is targeted.  *  *<p>Example usage:  *  *<pre>  *   quotaBackend.currentUser().project(projectName).requestToken("/projects/create").throwOnError();  *   quotaBackend.user(user).requestToken("/restapi/config/put").throwOnError();  *   QuotaResponse.Aggregated result = quotaBackend.currentUser().account(accountId).requestToken("/restapi/accounts/emails/validate");  *   QuotaResponse.Aggregated result = quotaBackend.currentUser().project(projectName).requestTokens("/projects/git/upload", numBytesInPush);  *</pre>  *  *<p>All quota groups must be documented in {@code quota.txt} and detail the metadata that is  * provided (i.e. the parameters used to scope down the quota request).  */
end_comment

begin_interface
annotation|@
name|ImplementedBy
argument_list|(
name|DefaultQuotaBackend
operator|.
name|class
argument_list|)
DECL|interface|QuotaBackend
specifier|public
interface|interface
name|QuotaBackend
block|{
comment|/** Constructs a request for the current user. */
DECL|method|currentUser ()
name|WithUser
name|currentUser
parameter_list|()
function_decl|;
comment|/**    * See {@link #currentUser()}. Use this method only if you can't guarantee that the request is for    * the current user (e.g. impersonation).    */
DECL|method|user (CurrentUser user)
name|WithUser
name|user
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
function_decl|;
comment|/**    * An interface capable of issuing quota requests. Scope can be futher reduced by providing a    * top-level entity.    */
DECL|interface|WithUser
interface|interface
name|WithUser
extends|extends
name|WithResource
block|{
comment|/** Scope the request down to an account. */
DECL|method|account (Account.Id account)
name|WithResource
name|account
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
function_decl|;
comment|/** Scope the request down to a project. */
DECL|method|project (Project.NameKey project)
name|WithResource
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
function_decl|;
comment|/** Scope the request down to a change. */
DECL|method|change (Change.Id change, Project.NameKey project)
name|WithResource
name|change
parameter_list|(
name|Change
operator|.
name|Id
name|change
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
function_decl|;
block|}
comment|/** An interface capable of issuing quota requests. */
DECL|interface|WithResource
interface|interface
name|WithResource
block|{
comment|/** Issues a single quota request for {@code 1} token. */
DECL|method|requestToken (String quotaGroup)
specifier|default
name|QuotaResponse
operator|.
name|Aggregated
name|requestToken
parameter_list|(
name|String
name|quotaGroup
parameter_list|)
block|{
return|return
name|requestTokens
argument_list|(
name|quotaGroup
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/** Issues a single quota request for {@code numTokens} tokens. */
DECL|method|requestTokens (String quotaGroup, long numTokens)
name|QuotaResponse
operator|.
name|Aggregated
name|requestTokens
parameter_list|(
name|String
name|quotaGroup
parameter_list|,
name|long
name|numTokens
parameter_list|)
function_decl|;
comment|/**      * Issues a single quota request for {@code numTokens} tokens but signals the implementations      * not to deduct any quota yet. Can be used to do pre-flight requests where necessary      */
DECL|method|dryRun (String quotaGroup, long tokens)
name|QuotaResponse
operator|.
name|Aggregated
name|dryRun
parameter_list|(
name|String
name|quotaGroup
parameter_list|,
name|long
name|tokens
parameter_list|)
function_decl|;
comment|/**      * Requests a minimum number of tokens available in implementations. This is a pre-flight check      * for the exceptional case when the requested number of tokens is not known in advance but      * boundary can be specified. For instance, when the commit is received its size is not known      * until the transfer happens however one can specify how many bytes can be accepted to meet the      * repository size quota.      *      *<p>By definition, this is not an allocating request, therefore, it should be followed by the      * call to {@link #requestTokens(String, long)} when the size gets determined so that quota      * could be properly adjusted. It is in developer discretion to ensure that it gets called.      * There might be a case when particular quota gets temporarily overbooked when multiple      * requests are performed but the following calls to {@link #requestTokens(String, long)} will      * fail at the moment when a quota is exhausted. It is not a subject of quota backend to reclaim      * tokens that were used due to overbooking.      */
DECL|method|availableTokens (String quotaGroup)
name|QuotaResponse
operator|.
name|Aggregated
name|availableTokens
parameter_list|(
name|String
name|quotaGroup
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

