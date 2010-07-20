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
DECL|package|com.google.gerrit.server.mail
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|UserIdentity
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
name|mail
operator|.
name|EmailHeader
operator|.
name|AddressList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|app
operator|.
name|Velocity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|ResourceNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|VelocityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|SystemReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
comment|/** Sends an email to one or more interested parties. */
end_comment

begin_class
DECL|class|OutgoingEmail
specifier|public
specifier|abstract
class|class
name|OutgoingEmail
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OutgoingEmail
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|HDR_TO
specifier|private
specifier|static
specifier|final
name|String
name|HDR_TO
init|=
literal|"To"
decl_stmt|;
DECL|field|HDR_CC
specifier|private
specifier|static
specifier|final
name|String
name|HDR_CC
init|=
literal|"CC"
decl_stmt|;
DECL|field|messageClass
specifier|protected
name|String
name|messageClass
decl_stmt|;
DECL|field|rcptTo
specifier|private
specifier|final
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|rcptTo
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|headers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
decl_stmt|;
DECL|field|smtpRcptTo
specifier|private
specifier|final
name|List
argument_list|<
name|Address
argument_list|>
name|smtpRcptTo
init|=
operator|new
name|ArrayList
argument_list|<
name|Address
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|smtpFromAddress
specifier|private
name|Address
name|smtpFromAddress
decl_stmt|;
DECL|field|body
specifier|private
name|StringBuilder
name|body
decl_stmt|;
DECL|field|velocityContext
specifier|protected
name|VelocityContext
name|velocityContext
decl_stmt|;
DECL|field|args
specifier|protected
specifier|final
name|EmailArguments
name|args
decl_stmt|;
DECL|field|fromId
specifier|protected
name|Account
operator|.
name|Id
name|fromId
decl_stmt|;
DECL|method|OutgoingEmail (EmailArguments ea, final String mc)
specifier|protected
name|OutgoingEmail
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
specifier|final
name|String
name|mc
parameter_list|)
block|{
name|args
operator|=
name|ea
expr_stmt|;
name|messageClass
operator|=
name|mc
expr_stmt|;
name|headers
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|setFrom (final Account.Id id)
specifier|public
name|void
name|setFrom
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|fromId
operator|=
name|id
expr_stmt|;
block|}
comment|/**    * Format and enqueue the message for delivery.    *    * @throws EmailException    */
DECL|method|send ()
specifier|public
name|void
name|send
parameter_list|()
throws|throws
name|EmailException
block|{
if|if
condition|(
operator|!
name|args
operator|.
name|emailSender
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
comment|// Server has explicitly disabled email sending.
comment|//
return|return;
block|}
name|init
argument_list|()
expr_stmt|;
name|format
argument_list|()
expr_stmt|;
if|if
condition|(
name|shouldSendMessage
argument_list|()
condition|)
block|{
if|if
condition|(
name|fromId
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Account
name|fromUser
init|=
name|args
operator|.
name|accountCache
operator|.
name|get
argument_list|(
name|fromId
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromUser
operator|.
name|getGeneralPreferences
argument_list|()
operator|.
name|isCopySelfOnEmails
argument_list|()
condition|)
block|{
comment|// If we are impersonating a user, make sure they receive a CC of
comment|// this message so they can always review and audit what we sent
comment|// on their behalf to others.
comment|//
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|fromId
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|rcptTo
operator|.
name|remove
argument_list|(
name|fromId
argument_list|)
condition|)
block|{
comment|// If they don't want a copy, but we queued one up anyway,
comment|// drop them from the recipient lists.
comment|//
specifier|final
name|String
name|fromEmail
init|=
name|fromUser
operator|.
name|getPreferredEmail
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Address
argument_list|>
name|i
init|=
name|smtpRcptTo
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
name|i
operator|.
name|next
argument_list|()
operator|.
name|email
operator|.
name|equals
argument_list|(
name|fromEmail
argument_list|)
condition|)
block|{
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
for|for
control|(
name|EmailHeader
name|hdr
range|:
name|headers
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|hdr
operator|instanceof
name|AddressList
condition|)
block|{
operator|(
operator|(
name|AddressList
operator|)
name|hdr
operator|)
operator|.
name|remove
argument_list|(
name|fromEmail
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|smtpRcptTo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
block|}
name|args
operator|.
name|emailSender
operator|.
name|send
argument_list|(
name|smtpFromAddress
argument_list|,
name|smtpRcptTo
argument_list|,
name|headers
argument_list|,
name|body
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Format the message body by calling {@link #appendText(String)}. */
DECL|method|format ()
specifier|protected
specifier|abstract
name|void
name|format
parameter_list|()
throws|throws
name|EmailException
function_decl|;
comment|/** Setup the message headers and envelope (TO, CC, BCC). */
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|setupVelocityContext
argument_list|()
expr_stmt|;
name|smtpFromAddress
operator|=
name|args
operator|.
name|fromAddressGenerator
operator|.
name|from
argument_list|(
name|fromId
argument_list|)
expr_stmt|;
name|setHeader
argument_list|(
literal|"Date"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"From"
argument_list|,
operator|new
name|EmailHeader
operator|.
name|AddressList
argument_list|(
name|smtpFromAddress
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HDR_TO
argument_list|,
operator|new
name|EmailHeader
operator|.
name|AddressList
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HDR_CC
argument_list|,
operator|new
name|EmailHeader
operator|.
name|AddressList
argument_list|()
argument_list|)
expr_stmt|;
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|fromId
operator|!=
literal|null
condition|)
block|{
comment|// If we have a user that this message is supposedly caused by
comment|// but the From header on the email does not match the user as
comment|// it is a generic header for this Gerrit server, include the
comment|// Reply-To header with the current user's email address.
comment|//
specifier|final
name|Address
name|a
init|=
name|toAddress
argument_list|(
name|fromId
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
operator|&&
operator|!
name|smtpFromAddress
operator|.
name|email
operator|.
name|equals
argument_list|(
name|a
operator|.
name|email
argument_list|)
condition|)
block|{
name|setHeader
argument_list|(
literal|"Reply-To"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
block|}
name|setHeader
argument_list|(
literal|"X-Gerrit-MessageType"
argument_list|,
name|messageClass
argument_list|)
expr_stmt|;
name|body
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
if|if
condition|(
name|fromId
operator|!=
literal|null
operator|&&
name|args
operator|.
name|fromAddressGenerator
operator|.
name|isGenericAddress
argument_list|(
name|fromId
argument_list|)
condition|)
block|{
name|appendText
argument_list|(
name|getFromLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getFromLine ()
specifier|protected
name|String
name|getFromLine
parameter_list|()
block|{
specifier|final
name|Account
name|account
init|=
name|args
operator|.
name|accountCache
operator|.
name|get
argument_list|(
name|fromId
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|account
operator|.
name|getFullName
argument_list|()
decl_stmt|;
specifier|final
name|String
name|email
init|=
name|account
operator|.
name|getPreferredEmail
argument_list|()
decl_stmt|;
name|StringBuilder
name|f
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|name
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|email
operator|!=
literal|null
operator|&&
operator|!
name|email
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|f
operator|.
name|append
argument_list|(
literal|"From"
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|f
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|email
operator|!=
literal|null
operator|&&
operator|!
name|email
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|f
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|email
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
name|f
operator|.
name|append
argument_list|(
literal|":\n\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|f
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getGerritHost ()
specifier|public
name|String
name|getGerritHost
parameter_list|()
block|{
if|if
condition|(
name|getGerritUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|getGerritUrl
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// Try something else.
block|}
block|}
comment|// Fall back onto whatever the local operating system thinks
comment|// this server is called. We hopefully didn't get here as a
comment|// good admin would have configured the canonical url.
comment|//
return|return
name|SystemReader
operator|.
name|getInstance
argument_list|()
operator|.
name|getHostname
argument_list|()
return|;
block|}
DECL|method|getSettingsUrl ()
specifier|public
name|String
name|getSettingsUrl
parameter_list|()
block|{
if|if
condition|(
name|getGerritUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|getGerritUrl
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|"settings"
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getGerritUrl ()
specifier|public
name|String
name|getGerritUrl
parameter_list|()
block|{
return|return
name|args
operator|.
name|urlProvider
operator|.
name|get
argument_list|()
return|;
block|}
comment|/** Set a header in the outgoing message using a template. */
DECL|method|setVHeader (final String name, final String value)
specifier|protected
name|void
name|setVHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
throws|throws
name|EmailException
block|{
name|setHeader
argument_list|(
name|name
argument_list|,
name|velocify
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Set a header in the outgoing message. */
DECL|method|setHeader (final String name, final String value)
specifier|protected
name|void
name|setHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|EmailHeader
operator|.
name|String
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setHeader (final String name, final Date date)
specifier|protected
name|void
name|setHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Date
name|date
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|EmailHeader
operator|.
name|Date
argument_list|(
name|date
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Append text to the outgoing email body. */
DECL|method|appendText (final String text)
specifier|protected
name|void
name|appendText
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
name|body
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Lookup a human readable name for an account, usually the "full name". */
DECL|method|getNameFor (final Account.Id accountId)
specifier|protected
name|String
name|getNameFor
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
if|if
condition|(
name|accountId
operator|==
literal|null
condition|)
block|{
return|return
literal|"Anonymous Coward"
return|;
block|}
specifier|final
name|Account
name|userAccount
init|=
name|args
operator|.
name|accountCache
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|userAccount
operator|.
name|getFullName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|userAccount
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|"Anonymous Coward #"
operator|+
name|accountId
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
DECL|method|getNameEmailFor (Account.Id accountId)
specifier|public
name|String
name|getNameEmailFor
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|AccountState
name|who
init|=
name|args
operator|.
name|accountCache
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|who
operator|.
name|getAccount
argument_list|()
operator|.
name|getFullName
argument_list|()
decl_stmt|;
name|String
name|email
init|=
name|who
operator|.
name|getAccount
argument_list|()
operator|.
name|getPreferredEmail
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|email
operator|!=
literal|null
condition|)
block|{
return|return
name|name
operator|+
literal|"<"
operator|+
name|email
operator|+
literal|">"
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
elseif|else
if|if
condition|(
name|email
operator|!=
literal|null
condition|)
block|{
return|return
name|email
return|;
block|}
else|else
comment|/* (name == null&& email == null) */
block|{
return|return
literal|"Anonymous Coward #"
operator|+
name|accountId
return|;
block|}
block|}
DECL|method|shouldSendMessage ()
specifier|protected
name|boolean
name|shouldSendMessage
parameter_list|()
block|{
if|if
condition|(
name|body
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// If we have no message body, don't send.
comment|//
return|return
literal|false
return|;
block|}
if|if
condition|(
name|smtpRcptTo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// If we have nobody to send this message to, then all of our
comment|// selection filters previously for this type of message were
comment|// unable to match a destination. Don't bother sending it.
comment|//
return|return
literal|false
return|;
block|}
if|if
condition|(
name|rcptTo
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|rcptTo
operator|.
name|contains
argument_list|(
name|fromId
argument_list|)
condition|)
block|{
comment|// If the only recipient is also the sender, don't bother.
comment|//
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/** Schedule this message for delivery to the listed accounts. */
DECL|method|add (final RecipientType rt, final Collection<Account.Id> list)
specifier|protected
name|void
name|add
parameter_list|(
specifier|final
name|RecipientType
name|rt
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|list
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Account
operator|.
name|Id
name|id
range|:
name|list
control|)
block|{
name|add
argument_list|(
name|rt
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|add (final RecipientType rt, final UserIdentity who)
specifier|protected
name|void
name|add
parameter_list|(
specifier|final
name|RecipientType
name|rt
parameter_list|,
specifier|final
name|UserIdentity
name|who
parameter_list|)
block|{
if|if
condition|(
name|who
operator|!=
literal|null
operator|&&
name|who
operator|.
name|getAccount
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|add
argument_list|(
name|rt
argument_list|,
name|who
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Schedule delivery of this message to the given account. */
DECL|method|add (final RecipientType rt, final Account.Id to)
specifier|protected
name|void
name|add
parameter_list|(
specifier|final
name|RecipientType
name|rt
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|to
parameter_list|)
block|{
if|if
condition|(
operator|!
name|rcptTo
operator|.
name|contains
argument_list|(
name|to
argument_list|)
operator|&&
name|isVisibleTo
argument_list|(
name|to
argument_list|)
condition|)
block|{
name|rcptTo
operator|.
name|add
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|rt
argument_list|,
name|toAddress
argument_list|(
name|to
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isVisibleTo (final Account.Id to)
specifier|protected
name|boolean
name|isVisibleTo
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|to
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/** Schedule delivery of this message to the given account. */
DECL|method|add (final RecipientType rt, final Address addr)
specifier|protected
name|void
name|add
parameter_list|(
specifier|final
name|RecipientType
name|rt
parameter_list|,
specifier|final
name|Address
name|addr
parameter_list|)
block|{
if|if
condition|(
name|addr
operator|!=
literal|null
operator|&&
name|addr
operator|.
name|email
operator|!=
literal|null
operator|&&
name|addr
operator|.
name|email
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|emailSender
operator|.
name|canEmail
argument_list|(
name|addr
operator|.
name|email
argument_list|)
condition|)
block|{
name|smtpRcptTo
operator|.
name|add
argument_list|(
name|addr
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|rt
condition|)
block|{
case|case
name|TO
case|:
operator|(
operator|(
name|EmailHeader
operator|.
name|AddressList
operator|)
name|headers
operator|.
name|get
argument_list|(
name|HDR_TO
argument_list|)
operator|)
operator|.
name|add
argument_list|(
name|addr
argument_list|)
expr_stmt|;
break|break;
case|case
name|CC
case|:
operator|(
operator|(
name|EmailHeader
operator|.
name|AddressList
operator|)
name|headers
operator|.
name|get
argument_list|(
name|HDR_CC
argument_list|)
operator|)
operator|.
name|add
argument_list|(
name|addr
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Not emailing "
operator|+
name|addr
operator|.
name|email
operator|+
literal|" (prohibited by allowrcpt)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|toAddress (final Account.Id id)
specifier|private
name|Address
name|toAddress
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
specifier|final
name|Account
name|a
init|=
name|args
operator|.
name|accountCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
specifier|final
name|String
name|e
init|=
name|a
operator|.
name|getPreferredEmail
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|a
operator|.
name|isActive
argument_list|()
operator|||
name|e
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|Address
argument_list|(
name|a
operator|.
name|getFullName
argument_list|()
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|setupVelocityContext ()
specifier|protected
name|void
name|setupVelocityContext
parameter_list|()
block|{
name|velocityContext
operator|=
operator|new
name|VelocityContext
argument_list|()
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"email"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"messageClass"
argument_list|,
name|messageClass
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"StringUtils"
argument_list|,
name|StringUtils
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|velocify (String tpl)
specifier|protected
name|String
name|velocify
parameter_list|(
name|String
name|tpl
parameter_list|)
throws|throws
name|EmailException
block|{
try|try
block|{
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|Velocity
operator|.
name|evaluate
argument_list|(
name|velocityContext
argument_list|,
name|w
argument_list|,
literal|"OutgoingEmail"
argument_list|,
name|tpl
argument_list|)
expr_stmt|;
return|return
name|w
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Velocity template "
operator|+
name|tpl
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|velocifyFile (String name)
specifier|protected
name|String
name|velocifyFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|EmailException
block|{
try|try
block|{
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|Velocity
operator|.
name|mergeTemplate
argument_list|(
name|name
argument_list|,
name|velocityContext
argument_list|,
name|w
argument_list|)
expr_stmt|;
return|return
name|w
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
try|try
block|{
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|String
name|pkg
init|=
literal|"com/google/gerrit/server/mail/"
decl_stmt|;
name|Velocity
operator|.
name|mergeTemplate
argument_list|(
name|pkg
operator|+
name|name
argument_list|,
name|velocityContext
argument_list|,
name|w
argument_list|)
expr_stmt|;
return|return
name|w
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e2
parameter_list|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Velocity WAR template"
operator|+
name|name
operator|+
literal|".\n"
argument_list|,
name|e2
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Velocity template "
operator|+
name|name
operator|+
literal|".\n"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

