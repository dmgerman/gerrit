begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
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
name|extensions
operator|.
name|common
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
name|extensions
operator|.
name|events
operator|.
name|AgreementSignupListener
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
name|account
operator|.
name|AccountState
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
name|plugincontext
operator|.
name|PluginSetContext
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
name|Singleton
import|;
end_import

begin_comment
comment|/** Helper class to fire an event when a user has signed a contributor agreement. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|AgreementSignup
specifier|public
class|class
name|AgreementSignup
block|{
DECL|field|listeners
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|AgreementSignupListener
argument_list|>
name|listeners
decl_stmt|;
DECL|field|util
specifier|private
specifier|final
name|EventUtil
name|util
decl_stmt|;
annotation|@
name|Inject
DECL|method|AgreementSignup (PluginSetContext<AgreementSignupListener> listeners, EventUtil util)
name|AgreementSignup
parameter_list|(
name|PluginSetContext
argument_list|<
name|AgreementSignupListener
argument_list|>
name|listeners
parameter_list|,
name|EventUtil
name|util
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
name|this
operator|.
name|util
operator|=
name|util
expr_stmt|;
block|}
DECL|method|fire (AccountState accountState, String agreementName)
specifier|public
name|void
name|fire
parameter_list|(
name|AccountState
name|accountState
parameter_list|,
name|String
name|agreementName
parameter_list|)
block|{
if|if
condition|(
name|listeners
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Event
name|event
init|=
operator|new
name|Event
argument_list|(
name|util
operator|.
name|accountInfo
argument_list|(
name|accountState
argument_list|)
argument_list|,
name|agreementName
argument_list|)
decl_stmt|;
name|listeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onAgreementSignup
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Event to be fired when a user has signed a contributor agreement. */
DECL|class|Event
specifier|private
specifier|static
class|class
name|Event
extends|extends
name|AbstractNoNotifyEvent
implements|implements
name|AgreementSignupListener
operator|.
name|Event
block|{
DECL|field|account
specifier|private
specifier|final
name|AccountInfo
name|account
decl_stmt|;
DECL|field|agreementName
specifier|private
specifier|final
name|String
name|agreementName
decl_stmt|;
DECL|method|Event (AccountInfo account, String agreementName)
name|Event
parameter_list|(
name|AccountInfo
name|account
parameter_list|,
name|String
name|agreementName
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
name|this
operator|.
name|agreementName
operator|=
name|agreementName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAccount ()
specifier|public
name|AccountInfo
name|getAccount
parameter_list|()
block|{
return|return
name|account
return|;
block|}
annotation|@
name|Override
DECL|method|getAgreementName ()
specifier|public
name|String
name|getAgreementName
parameter_list|()
block|{
return|return
name|agreementName
return|;
block|}
block|}
block|}
end_class

end_unit

