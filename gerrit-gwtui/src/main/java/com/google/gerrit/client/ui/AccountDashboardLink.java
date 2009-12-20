begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|FormatUtil
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
name|common
operator|.
name|PageLinks
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
name|common
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
name|common
operator|.
name|data
operator|.
name|AccountInfoCache
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
name|reviewdb
operator|.
name|Account
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
comment|/** Create a link after locating account details from an active cache. */
DECL|method|link (final AccountInfoCache cache, final Account.Id id)
specifier|public
specifier|static
name|AccountDashboardLink
name|link
parameter_list|(
specifier|final
name|AccountInfoCache
name|cache
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
specifier|final
name|AccountInfo
name|ai
init|=
name|cache
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|ai
operator|!=
literal|null
condition|?
operator|new
name|AccountDashboardLink
argument_list|(
name|ai
argument_list|)
else|:
literal|null
return|;
block|}
DECL|field|accountId
specifier|private
name|Account
operator|.
name|Id
name|accountId
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
name|FormatUtil
operator|.
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
name|this
argument_list|(
name|text
argument_list|,
name|ai
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
name|FormatUtil
operator|.
name|nameEmail
argument_list|(
name|ai
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|AccountDashboardLink (final String text, final Account.Id ai)
specifier|public
name|AccountDashboardLink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|ai
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|PageLinks
operator|.
name|toAccountDashboard
argument_list|(
name|ai
argument_list|)
argument_list|)
expr_stmt|;
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|accountName
argument_list|()
argument_list|)
expr_stmt|;
name|accountId
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
name|accountId
argument_list|)
return|;
block|}
block|}
end_class

end_unit

