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
name|account
operator|.
name|Util
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
name|rpc
operator|.
name|GerritCallback
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
name|AccountDiffPreference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|VoidResult
import|;
end_import

begin_class
DECL|class|ListenableAccountDiffPreference
specifier|public
class|class
name|ListenableAccountDiffPreference
extends|extends
name|ListenableOldValue
argument_list|<
name|AccountDiffPreference
argument_list|>
block|{
DECL|method|ListenableAccountDiffPreference ()
specifier|public
name|ListenableAccountDiffPreference
parameter_list|()
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|save (final GerritCallback<VoidResult> cb)
specifier|public
name|void
name|save
parameter_list|(
specifier|final
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|changeDiffPreferences
argument_list|(
name|get
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|VoidResult
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|setAccountDiffPreference
argument_list|(
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
operator|&&
name|Gerrit
operator|.
name|getAccountDiffPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|set
argument_list|(
name|Gerrit
operator|.
name|getAccountDiffPreference
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|set
argument_list|(
name|AccountDiffPreference
operator|.
name|createDefault
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

