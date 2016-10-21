begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.receive
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|receive
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
name|mail
operator|.
name|EmailSettings
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

begin_class
annotation|@
name|Singleton
DECL|class|ImapMailReceiver
specifier|public
class|class
name|ImapMailReceiver
extends|extends
name|MailReceiver
block|{
annotation|@
name|Inject
DECL|method|ImapMailReceiver (EmailSettings mailSettings)
specifier|public
name|ImapMailReceiver
parameter_list|(
name|EmailSettings
name|mailSettings
parameter_list|)
block|{
name|super
argument_list|(
name|mailSettings
argument_list|)
expr_stmt|;
block|}
comment|/**    * handleEmails will open a connection to the mail server, remove emails    * where deletion is pending, read new email and close the connection.    */
annotation|@
name|Override
DECL|method|handleEmails ()
specifier|protected
specifier|synchronized
name|void
name|handleEmails
parameter_list|()
block|{
comment|// TODO(hiesel) Implement.
block|}
block|}
end_class

end_unit

