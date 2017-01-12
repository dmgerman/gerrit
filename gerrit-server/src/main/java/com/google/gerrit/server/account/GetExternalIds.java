begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
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
name|ImmutableList
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
name|Lists
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
name|common
operator|.
name|AccountExternalIdInfo
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|RestApiException
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
name|restapi
operator|.
name|RestReadView
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountExternalId
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|AuthConfig
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
name|Provider
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
name|Collections
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

begin_class
annotation|@
name|Singleton
DECL|class|GetExternalIds
specifier|public
class|class
name|GetExternalIds
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|externalIdCache
specifier|private
specifier|final
name|ExternalIdCache
name|externalIdCache
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetExternalIds (ExternalIdCache externalIdCache, Provider<CurrentUser> self, AuthConfig authConfig)
name|GetExternalIds
parameter_list|(
name|ExternalIdCache
name|externalIdCache
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
operator|.
name|externalIdCache
operator|=
name|externalIdCache
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource resource)
specifier|public
name|List
argument_list|<
name|AccountExternalIdInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|resource
parameter_list|)
throws|throws
name|RestApiException
block|{
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|resource
operator|.
name|getUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to get external IDs"
argument_list|)
throw|;
block|}
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
init|=
name|externalIdCache
operator|.
name|byAccount
argument_list|(
name|resource
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|List
argument_list|<
name|AccountExternalIdInfo
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|ids
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountExternalId
name|id
range|:
name|ids
control|)
block|{
name|AccountExternalIdInfo
name|info
init|=
operator|new
name|AccountExternalIdInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|identity
operator|=
name|id
operator|.
name|getExternalId
argument_list|()
expr_stmt|;
name|info
operator|.
name|emailAddress
operator|=
name|id
operator|.
name|getEmailAddress
argument_list|()
expr_stmt|;
name|info
operator|.
name|trusted
operator|=
name|authConfig
operator|.
name|isIdentityTrustable
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
comment|// The identity can be deleted only if its not the one used to
comment|// establish this web session, and if only if an identity was
comment|// actually used to establish this web session.
if|if
condition|(
name|id
operator|.
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
name|info
operator|.
name|canDelete
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|CurrentUser
operator|.
name|PropertyKey
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|k
init|=
name|CurrentUser
operator|.
name|PropertyKey
operator|.
name|create
argument_list|()
decl_stmt|;
name|AccountExternalId
operator|.
name|Key
name|last
init|=
name|resource
operator|.
name|getUser
argument_list|()
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
name|info
operator|.
name|canDelete
operator|=
operator|(
name|last
operator|!=
literal|null
operator|)
operator|&&
operator|(
operator|!
name|last
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|info
operator|.
name|identity
argument_list|)
operator|)
expr_stmt|;
block|}
name|result
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

