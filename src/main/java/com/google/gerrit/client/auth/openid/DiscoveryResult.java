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
DECL|package|com.google.gerrit.client.auth.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
DECL|field|validProvider
specifier|public
name|boolean
name|validProvider
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
DECL|method|DiscoveryResult (final boolean valid, final String redirect, final Map<String, String> args)
specifier|public
name|DiscoveryResult
parameter_list|(
specifier|final
name|boolean
name|valid
parameter_list|,
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
name|validProvider
operator|=
name|valid
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
DECL|method|DiscoveryResult (final boolean fail)
specifier|public
name|DiscoveryResult
parameter_list|(
specifier|final
name|boolean
name|fail
parameter_list|)
block|{
name|this
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

