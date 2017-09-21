begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.validators
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|validators
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|server
operator|.
name|mail
operator|.
name|send
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Listener to provide validation on outgoing email notification. */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|OutgoingEmailValidationListener
specifier|public
interface|interface
name|OutgoingEmailValidationListener
block|{
comment|/** Arguments supplied to validateOutgoingEmail. */
DECL|class|Args
class|class
name|Args
block|{
comment|// in arguments
DECL|field|messageClass
specifier|public
name|String
name|messageClass
decl_stmt|;
DECL|field|htmlBody
annotation|@
name|Nullable
specifier|public
name|String
name|htmlBody
decl_stmt|;
comment|// in/out arguments
DECL|field|smtpFromAddress
specifier|public
name|Address
name|smtpFromAddress
decl_stmt|;
DECL|field|smtpRcptTo
specifier|public
name|Set
argument_list|<
name|Address
argument_list|>
name|smtpRcptTo
decl_stmt|;
DECL|field|body
specifier|public
name|String
name|body
decl_stmt|;
comment|// The text body of the email.
DECL|field|headers
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
decl_stmt|;
block|}
comment|/**    * Outgoing e-mail validation.    *    *<p>Invoked by Gerrit just before an e-mail is sent, after all e-mail templates have been    * applied.    *    *<p>Plugins may modify the following fields in args: - smtpFromAddress - smtpRcptTo - body -    * headers    *    * @param args E-mail properties. Some are mutable.    * @throws ValidationException if validation fails.    */
DECL|method|validateOutgoingEmail (OutgoingEmailValidationListener.Args args)
name|void
name|validateOutgoingEmail
parameter_list|(
name|OutgoingEmailValidationListener
operator|.
name|Args
name|args
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
block|}
end_interface

end_unit

