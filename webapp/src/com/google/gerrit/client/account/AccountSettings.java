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
name|Screen
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
name|reviewdb
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|AccountSettings
specifier|public
class|class
name|AccountSettings
extends|extends
name|Screen
block|{
DECL|method|AccountSettings ()
specifier|public
name|AccountSettings
parameter_list|()
block|{
name|super
argument_list|(
name|Util
operator|.
name|C
operator|.
name|accountSettingsHeading
argument_list|()
argument_list|)
expr_stmt|;
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|myAccount
argument_list|(
operator|new
name|AsyncCallback
argument_list|<
name|Account
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|GWT
operator|.
name|log
argument_list|(
literal|"myAccount failed"
argument_list|,
name|caught
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Account
name|result
parameter_list|)
block|{
name|GWT
operator|.
name|log
argument_list|(
literal|"yay, i am "
operator|+
name|result
operator|.
name|getPreferredEmail
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|GWT
operator|.
name|log
argument_list|(
literal|"created on "
operator|+
name|result
operator|.
name|getRegisteredOn
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

