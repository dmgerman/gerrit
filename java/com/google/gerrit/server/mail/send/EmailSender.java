begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
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
name|send
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
name|common
operator|.
name|Nullable
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
name|exceptions
operator|.
name|EmailException
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
name|mail
operator|.
name|Address
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
name|mail
operator|.
name|EmailHeader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Sends email messages to third parties. */
end_comment

begin_interface
DECL|interface|EmailSender
specifier|public
interface|interface
name|EmailSender
block|{
DECL|method|isEnabled ()
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
comment|/**    * Can the address receive messages from us?    *    * @param address the address to consider.    * @return true if this sender will deliver to the address.    */
DECL|method|canEmail (String address)
name|boolean
name|canEmail
parameter_list|(
name|String
name|address
parameter_list|)
function_decl|;
comment|/**    * Sends an email message. Messages always contain a text body, but messages can optionally    * include an additional HTML body. If both body types are present, {@code send} should construct    * a {@code multipart/alternative} message with an appropriately-selected boundary.    *    * @param from who the message is from.    * @param rcpt one or more address where the message will be delivered to. This list overrides any    *     To or CC headers in {@code headers}.    * @param headers message headers.    * @param textBody text to appear in the {@code text/plain} body of the message.    * @param htmlBody optional HTML code to appear in the {@code text/html} body of the message.    * @throws EmailException the message cannot be sent.    */
DECL|method|send ( Address from, Collection<Address> rcpt, Map<String, EmailHeader> headers, String textBody, @Nullable String htmlBody)
specifier|default
name|void
name|send
parameter_list|(
name|Address
name|from
parameter_list|,
name|Collection
argument_list|<
name|Address
argument_list|>
name|rcpt
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
parameter_list|,
name|String
name|textBody
parameter_list|,
annotation|@
name|Nullable
name|String
name|htmlBody
parameter_list|)
throws|throws
name|EmailException
block|{
name|send
argument_list|(
name|from
argument_list|,
name|rcpt
argument_list|,
name|headers
argument_list|,
name|textBody
argument_list|)
expr_stmt|;
block|}
comment|/**    * Sends an email message with a text body only (i.e. not HTML or multipart).    *    *<p>Authors of new implementations of this interface should not use this method to send a    * message because this method does not accept the HTML body. Instead, authors should use the    * above signature of {@code send}.    *    *<p>This version of the method is preserved for support of legacy implementations.    *    * @param from who the message is from.    * @param rcpt one or more address where the message will be delivered to. This list overrides any    *     To or CC headers in {@code headers}.    * @param headers message headers.    * @param body text to appear in the body of the message.    * @throws EmailException the message cannot be sent.    */
DECL|method|send (Address from, Collection<Address> rcpt, Map<String, EmailHeader> headers, String body)
name|void
name|send
parameter_list|(
name|Address
name|from
parameter_list|,
name|Collection
argument_list|<
name|Address
argument_list|>
name|rcpt
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
parameter_list|,
name|String
name|body
parameter_list|)
throws|throws
name|EmailException
function_decl|;
block|}
end_interface

end_unit

