begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|client
operator|.
name|Gerrit
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
name|client
operator|.
name|Link
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
name|client
operator|.
name|changes
operator|.
name|AccountDashboardScreen
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
name|client
operator|.
name|data
operator|.
name|AccountInfo
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
name|client
operator|.
name|ui
operator|.
name|DirectScreenLink
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
name|client
operator|.
name|ui
operator|.
name|Screen
import|;
end_import

begin_comment
comment|/** Link to any user's account dashboard. */
end_comment

begin_class
DECL|class|AccountDashboardLink
specifier|public
class|class
name|AccountDashboardLink
extends|extends
name|DirectScreenLink
block|{
DECL|method|name (final AccountInfo ai)
specifier|private
specifier|static
name|String
name|name
parameter_list|(
specifier|final
name|AccountInfo
name|ai
parameter_list|)
block|{
if|if
condition|(
name|ai
operator|.
name|getFullName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|ai
operator|.
name|getFullName
argument_list|()
return|;
block|}
if|if
condition|(
name|ai
operator|.
name|getPreferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|ai
operator|.
name|getPreferredEmail
argument_list|()
return|;
block|}
return|return
name|Gerrit
operator|.
name|C
operator|.
name|anonymousCoward
argument_list|()
return|;
block|}
DECL|field|account
specifier|private
name|AccountInfo
name|account
decl_stmt|;
DECL|method|AccountDashboardLink (final AccountInfo ai)
specifier|public
name|AccountDashboardLink
parameter_list|(
specifier|final
name|AccountInfo
name|ai
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|(
name|ai
argument_list|)
argument_list|,
name|ai
argument_list|)
expr_stmt|;
block|}
DECL|method|AccountDashboardLink (final String text, final AccountInfo ai)
specifier|public
name|AccountDashboardLink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|AccountInfo
name|ai
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|Link
operator|.
name|toAccountDashboard
argument_list|(
name|ai
argument_list|)
argument_list|)
expr_stmt|;
name|account
operator|=
name|ai
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createScreen ()
specifier|protected
name|Screen
name|createScreen
parameter_list|()
block|{
return|return
operator|new
name|AccountDashboardScreen
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

