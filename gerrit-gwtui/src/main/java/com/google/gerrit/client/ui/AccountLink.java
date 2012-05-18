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
name|client
operator|.
name|Account
import|;
end_import

begin_comment
comment|/** Link to any user's account dashboard. */
end_comment

begin_class
DECL|class|AccountLink
specifier|public
class|class
name|AccountLink
extends|extends
name|InlineHyperlink
block|{
comment|/** Create a link after locating account details from an active cache. */
DECL|method|link (final AccountInfoCache cache, final Account.Id id)
specifier|public
specifier|static
name|AccountLink
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
name|AccountLink
argument_list|(
name|ai
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|AccountLink (final AccountInfo ai)
specifier|public
name|AccountLink
parameter_list|(
specifier|final
name|AccountInfo
name|ai
parameter_list|)
block|{
name|this
argument_list|(
name|owner
argument_list|(
name|ai
argument_list|)
argument_list|,
name|ai
argument_list|)
expr_stmt|;
block|}
DECL|method|AccountLink (final String text, final AccountInfo ai)
specifier|public
name|AccountLink
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
name|PageLinks
operator|.
name|toAccountQuery
argument_list|(
name|owner
argument_list|(
name|ai
argument_list|)
argument_list|)
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
DECL|method|owner (AccountInfo ai)
specifier|private
specifier|static
name|String
name|owner
parameter_list|(
name|AccountInfo
name|ai
parameter_list|)
block|{
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
elseif|else
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
return|return
literal|""
operator|+
name|ai
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|go ()
specifier|public
name|void
name|go
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|getTargetHistoryToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

