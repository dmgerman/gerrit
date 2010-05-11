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
DECL|package|com.google.gerrit.common.auth.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|auth
operator|.
name|openid
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|DiscoveryResult
specifier|public
specifier|final
class|class
name|DiscoveryResult
block|{
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
comment|/** Provider was discovered and {@code providerUrl} is valid. */
DECL|enumConstant|VALID
name|VALID
block|,
comment|/** The identifier is not allowed to be used, by site configuration. */
DECL|enumConstant|NOT_ALLOWED
name|NOT_ALLOWED
block|,
comment|/** Identifier isn't for an OpenID provider. */
DECL|enumConstant|NO_PROVIDER
name|NO_PROVIDER
block|,
comment|/** The provider was discovered, but something else failed. */
DECL|enumConstant|ERROR
name|ERROR
block|;   }
DECL|field|status
specifier|public
name|Status
name|status
decl_stmt|;
DECL|field|providerUrl
specifier|public
name|String
name|providerUrl
decl_stmt|;
DECL|field|providerArgs
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|providerArgs
decl_stmt|;
DECL|method|DiscoveryResult ()
specifier|protected
name|DiscoveryResult
parameter_list|()
block|{   }
DECL|method|DiscoveryResult (final String redirect, final Map<String, String> args)
specifier|public
name|DiscoveryResult
parameter_list|(
specifier|final
name|String
name|redirect
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|args
parameter_list|)
block|{
name|status
operator|=
name|Status
operator|.
name|VALID
expr_stmt|;
name|providerUrl
operator|=
name|redirect
expr_stmt|;
name|providerArgs
operator|=
name|args
expr_stmt|;
block|}
DECL|method|DiscoveryResult (final Status s)
specifier|public
name|DiscoveryResult
parameter_list|(
specifier|final
name|Status
name|s
parameter_list|)
block|{
name|status
operator|=
name|s
expr_stmt|;
block|}
block|}
end_class

end_unit

