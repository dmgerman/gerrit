begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/** Provides {@link SshdListenAddressl} from {@code sshd.listenAddress}. */
end_comment

begin_class
DECL|class|SshdListenAddressProvider
specifier|public
class|class
name|SshdListenAddressProvider
implements|implements
name|Provider
argument_list|<
name|String
argument_list|>
block|{
DECL|field|sshAddress
specifier|private
specifier|final
name|String
name|sshAddress
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshdListenAddressProvider (@erritServerConfig final Config config)
specifier|public
name|SshdListenAddressProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|config
parameter_list|)
block|{
name|String
name|sshdListenAddress
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|,
literal|"listenAddress"
argument_list|)
decl_stmt|;
name|String
name|sshdAdvertisedAddress
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|,
literal|"advertisedAddress"
argument_list|)
decl_stmt|;
comment|/*      * If advertised address is specified it should take precedence over the      * "normal" listening address.      */
if|if
condition|(
name|sshdAdvertisedAddress
operator|!=
literal|null
operator|&&
operator|!
name|sshdAdvertisedAddress
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sshAddress
operator|=
name|sshdAdvertisedAddress
expr_stmt|;
block|}
else|else
block|{
name|sshAddress
operator|=
name|sshdListenAddress
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|sshAddress
return|;
block|}
block|}
end_class

end_unit

