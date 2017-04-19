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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInfo
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
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInfo
operator|.
name|CheckAccountExternalIdsResultInfo
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
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInput
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
name|BadRequestException
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
name|RestModifyView
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
name|ExternalIdsConsistencyChecker
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
name|io
operator|.
name|IOException
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|CheckConsistency
specifier|public
class|class
name|CheckConsistency
implements|implements
name|RestModifyView
argument_list|<
name|ConfigResource
argument_list|,
name|ConsistencyCheckInput
argument_list|>
block|{
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|externalIdsConsistencyChecker
specifier|private
specifier|final
name|ExternalIdsConsistencyChecker
name|externalIdsConsistencyChecker
decl_stmt|;
annotation|@
name|Inject
DECL|method|CheckConsistency ( Provider<IdentifiedUser> currentUser, ExternalIdsConsistencyChecker externalIdsConsistencyChecker)
name|CheckConsistency
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|currentUser
parameter_list|,
name|ExternalIdsConsistencyChecker
name|externalIdsConsistencyChecker
parameter_list|)
block|{
name|this
operator|.
name|userProvider
operator|=
name|currentUser
expr_stmt|;
name|this
operator|.
name|externalIdsConsistencyChecker
operator|=
name|externalIdsConsistencyChecker
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource resource, ConsistencyCheckInput input)
specifier|public
name|ConsistencyCheckInfo
name|apply
parameter_list|(
name|ConfigResource
name|resource
parameter_list|,
name|ConsistencyCheckInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
block|{
name|IdentifiedUser
name|user
init|=
name|userProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|user
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAccessDatabase
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to run consistency checks"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|==
literal|null
operator|||
name|input
operator|.
name|checkAccountExternalIds
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"input required"
argument_list|)
throw|;
block|}
name|ConsistencyCheckInfo
name|consistencyCheckInfo
init|=
operator|new
name|ConsistencyCheckInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|checkAccountExternalIds
operator|!=
literal|null
condition|)
block|{
name|consistencyCheckInfo
operator|.
name|checkAccountExternalIdsResult
operator|=
operator|new
name|CheckAccountExternalIdsResultInfo
argument_list|(
name|externalIdsConsistencyChecker
operator|.
name|check
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|consistencyCheckInfo
return|;
block|}
block|}
end_class

end_unit

