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
name|permissions
operator|.
name|PermissionBackend
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|command
operator|.
name|Command
import|;
end_import

begin_comment
comment|/** Resolves an alias to another command. */
end_comment

begin_class
DECL|class|AliasCommandProvider
specifier|public
class|class
name|AliasCommandProvider
implements|implements
name|Provider
argument_list|<
name|Command
argument_list|>
block|{
DECL|field|command
specifier|private
specifier|final
name|CommandName
name|command
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|CommandName
argument_list|(
name|Commands
operator|.
name|ROOT
argument_list|)
DECL|field|root
specifier|private
name|DispatchCommandProvider
name|root
decl_stmt|;
DECL|field|permissionBackend
annotation|@
name|Inject
specifier|private
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|method|AliasCommandProvider (CommandName command)
specifier|public
name|AliasCommandProvider
parameter_list|(
name|CommandName
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Command
name|get
parameter_list|()
block|{
return|return
operator|new
name|AliasCommand
argument_list|(
name|root
argument_list|,
name|permissionBackend
argument_list|,
name|command
argument_list|)
return|;
block|}
block|}
end_class

end_unit

