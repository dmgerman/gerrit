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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|extensions
operator|.
name|api
operator|.
name|lfs
operator|.
name|LfsDefinitions
operator|.
name|LFS_URL_WO_AUTH_REGEX
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
name|GitBasicAuthPolicy
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
name|servlet
operator|.
name|ServletModule
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_comment
comment|/** Configures filter for authenticating REST requests. */
end_comment

begin_class
DECL|class|GerritAuthModule
specifier|public
class|class
name|GerritAuthModule
extends|extends
name|ServletModule
block|{
DECL|field|NOT_AUTHORIZED_LFS_URL_REGEX
specifier|static
specifier|final
name|String
name|NOT_AUTHORIZED_LFS_URL_REGEX
init|=
literal|"^(?:(?!/a/))"
operator|+
name|LFS_URL_WO_AUTH_REGEX
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritAuthModule (AuthConfig authConfig)
name|GerritAuthModule
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Filter
argument_list|>
name|authFilter
init|=
name|retreiveAuthFilterFromConfig
argument_list|(
name|authConfig
argument_list|)
decl_stmt|;
name|filterRegex
argument_list|(
name|NOT_AUTHORIZED_LFS_URL_REGEX
argument_list|)
operator|.
name|through
argument_list|(
name|authFilter
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/a/*"
argument_list|)
operator|.
name|through
argument_list|(
name|authFilter
argument_list|)
expr_stmt|;
block|}
DECL|method|retreiveAuthFilterFromConfig (AuthConfig authConfig)
specifier|static
name|Class
argument_list|<
name|?
extends|extends
name|Filter
argument_list|>
name|retreiveAuthFilterFromConfig
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Filter
argument_list|>
name|authFilter
decl_stmt|;
if|if
condition|(
name|authConfig
operator|.
name|isTrustContainerAuth
argument_list|()
condition|)
block|{
name|authFilter
operator|=
name|ContainerAuthFilter
operator|.
name|class
expr_stmt|;
block|}
else|else
block|{
name|authFilter
operator|=
name|authConfig
operator|.
name|getGitBasicAuthPolicy
argument_list|()
operator|==
name|GitBasicAuthPolicy
operator|.
name|OAUTH
condition|?
name|ProjectOAuthFilter
operator|.
name|class
else|:
name|ProjectBasicAuthFilter
operator|.
name|class
expr_stmt|;
block|}
return|return
name|authFilter
return|;
block|}
block|}
end_class

end_unit

