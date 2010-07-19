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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|IdentifiedUser
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

begin_class
annotation|@
name|Singleton
DECL|class|SshCurrentUserProvider
class|class
name|SshCurrentUserProvider
implements|implements
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
block|{
DECL|field|session
specifier|private
specifier|final
name|Provider
argument_list|<
name|SshSession
argument_list|>
name|session
decl_stmt|;
DECL|field|identifiedProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshCurrentUserProvider (Provider<SshSession> s, Provider<IdentifiedUser> p)
name|SshCurrentUserProvider
parameter_list|(
name|Provider
argument_list|<
name|SshSession
argument_list|>
name|s
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|p
parameter_list|)
block|{
name|session
operator|=
name|s
expr_stmt|;
name|identifiedProvider
operator|=
name|p
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|CurrentUser
name|get
parameter_list|()
block|{
specifier|final
name|CurrentUser
name|user
init|=
name|session
operator|.
name|get
argument_list|()
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|instanceof
name|IdentifiedUser
condition|)
block|{
return|return
name|identifiedProvider
operator|.
name|get
argument_list|()
return|;
block|}
return|return
name|session
operator|.
name|get
argument_list|()
operator|.
name|getCurrentUser
argument_list|()
return|;
block|}
block|}
end_class

end_unit

