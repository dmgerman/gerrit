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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
import|;
end_import

begin_comment
comment|/**  * Allows plugins to enforce different types of quota.  *  *<p>Enforcing quotas can be helpful in many scenarios. For example:  *  *<ul>  *<li>Reducing the number of QPS a user can send to Gerrit on the REST API  *<li>Limiting the size of a repository (project)  *<li>Limiting the number of changes in a repository  *<li>Limiting the number of actions that have the potential for spam, abuse or flooding if not  *       limited  *</ul>  *  * This endpoint gives plugins the capability to enforce any of these limits. The server will ask  * all plugins that registered this endpoint and collect all results. In case {@link  * #requestTokens(String, QuotaRequestContext, long)} was called and one or more plugins returned an  * erroneous result, the server will call {@link #refill(String, QuotaRequestContext, long)} on all  * plugins with the same parameters. Plugins that deducted tokens in the {@link  * #requestTokens(String, QuotaRequestContext, long)} call can refill them so that users don't get  * charged any quota for failed requests.  *  *<p>Not all implementations will need to deduct quota on {@link #requestTokens(String,  * QuotaRequestContext, long)}}. Implementations that work on top of instance-attributes, such as  * the number of projects per instance can choose not to keep any state and always check how many  * existing projects there are and if adding the inquired number would exceed the limit. In this  * case, {@link #requestTokens(String, QuotaRequestContext, long)} and {@link #dryRun(String,  * QuotaRequestContext, long)} share the same implementation and {@link #refill(String,  * QuotaRequestContext, long)} is a no-op.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|QuotaEnforcer
specifier|public
interface|interface
name|QuotaEnforcer
block|{
comment|/**    * Checks if there is at least {@code numTokens} quota to fulfil the request. Bucket-based    * implementations can deduct the inquired number of tokens from the bucket.    */
DECL|method|requestTokens (String quotaGroup, QuotaRequestContext ctx, long numTokens)
name|QuotaResponse
name|requestTokens
parameter_list|(
name|String
name|quotaGroup
parameter_list|,
name|QuotaRequestContext
name|ctx
parameter_list|,
name|long
name|numTokens
parameter_list|)
function_decl|;
comment|/**    * Checks if there is at least {@code numTokens} quota to fulfil the request. This is a pre-flight    * request, implementations should not deduct tokens from a bucket, yet.    */
DECL|method|dryRun (String quotaGroup, QuotaRequestContext ctx, long numTokens)
name|QuotaResponse
name|dryRun
parameter_list|(
name|String
name|quotaGroup
parameter_list|,
name|QuotaRequestContext
name|ctx
parameter_list|,
name|long
name|numTokens
parameter_list|)
function_decl|;
comment|/**    * A previously requested and deducted quota has to be refilled (if possible) because the request    * failed other quota checks. Implementations can choose to leave this a no-op in case they are    * the first line of defence (e.g. always deduct HTTP quota even if the request failed for other    * quota issues so that the user gets throttled).    *    *<p>Will not be called if the {@link #requestTokens(String, QuotaRequestContext, long)} call    * returned {@link QuotaResponse.Status#NO_OP}.    */
DECL|method|refill (String quotaGroup, QuotaRequestContext ctx, long numTokens)
name|void
name|refill
parameter_list|(
name|String
name|quotaGroup
parameter_list|,
name|QuotaRequestContext
name|ctx
parameter_list|,
name|long
name|numTokens
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

