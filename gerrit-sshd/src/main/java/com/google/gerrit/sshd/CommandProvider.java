begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|inject
operator|.
name|Provider
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
name|Command
import|;
end_import

begin_class
DECL|class|CommandProvider
specifier|final
class|class
name|CommandProvider
block|{
DECL|field|provider
specifier|private
specifier|final
name|Provider
argument_list|<
name|Command
argument_list|>
name|provider
decl_stmt|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|method|CommandProvider (final Provider<Command> p, final String d)
name|CommandProvider
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|Command
argument_list|>
name|p
parameter_list|,
specifier|final
name|String
name|d
parameter_list|)
block|{
name|this
operator|.
name|provider
operator|=
name|p
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|d
expr_stmt|;
block|}
DECL|method|getProvider ()
specifier|public
name|Provider
argument_list|<
name|Command
argument_list|>
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
block|}
end_class

end_unit

