begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
operator|.
name|SshInfo
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
name|ssh
operator|.
name|SshKeyCache
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
name|AbstractModule
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

begin_comment
comment|/**  * Pulls objects from the SSH injector over the HTTP injector.  *<p>  * This mess is only necessary because we build up two different injectors, in  * order to have different request scopes. But some HTTP RPCs can cause changes  * to the SSH side of the house, and thus needs access to it.  */
end_comment

begin_class
DECL|class|WebSshGlueModule
specifier|public
class|class
name|WebSshGlueModule
extends|extends
name|AbstractModule
block|{
DECL|field|sshInfoProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|SshInfo
argument_list|>
name|sshInfoProvider
decl_stmt|;
DECL|field|sshKeyCacheProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|SshKeyCache
argument_list|>
name|sshKeyCacheProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|WebSshGlueModule (Provider<SshInfo> sshInfoProvider, Provider<SshKeyCache> sshKeyCacheProvider)
name|WebSshGlueModule
parameter_list|(
name|Provider
argument_list|<
name|SshInfo
argument_list|>
name|sshInfoProvider
parameter_list|,
name|Provider
argument_list|<
name|SshKeyCache
argument_list|>
name|sshKeyCacheProvider
parameter_list|)
block|{
name|this
operator|.
name|sshInfoProvider
operator|=
name|sshInfoProvider
expr_stmt|;
name|this
operator|.
name|sshKeyCacheProvider
operator|=
name|sshKeyCacheProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|SshInfo
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|sshInfoProvider
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshKeyCache
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|sshKeyCacheProvider
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

