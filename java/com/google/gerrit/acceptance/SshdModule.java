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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|Provides
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|keyprovider
operator|.
name|KeyPairProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|keyprovider
operator|.
name|SimpleGeneratorHostKeyProvider
import|;
end_import

begin_class
DECL|class|SshdModule
specifier|public
class|class
name|SshdModule
extends|extends
name|AbstractModule
block|{
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|createHostKey ()
name|KeyPairProvider
name|createHostKey
parameter_list|()
block|{
return|return
name|getHostKeys
argument_list|()
return|;
block|}
DECL|field|keys
specifier|private
specifier|static
name|SimpleGeneratorHostKeyProvider
name|keys
decl_stmt|;
DECL|method|getHostKeys ()
specifier|private
specifier|static
specifier|synchronized
name|KeyPairProvider
name|getHostKeys
parameter_list|()
block|{
if|if
condition|(
name|keys
operator|==
literal|null
condition|)
block|{
name|keys
operator|=
operator|new
name|SimpleGeneratorHostKeyProvider
argument_list|()
expr_stmt|;
name|keys
operator|.
name|setAlgorithm
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|loadKeys
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|keys
return|;
block|}
block|}
end_class

end_unit

