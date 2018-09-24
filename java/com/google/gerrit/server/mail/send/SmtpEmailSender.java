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
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|BaseEncoding
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Ints
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
name|common
operator|.
name|Version
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
name|errors
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigUtil
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
name|config
operator|.
name|GerritServerConfig
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
name|Encryption
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
name|util
operator|.
name|time
operator|.
name|TimeUtil
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
name|AbstractModule
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Collections
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
name|LinkedHashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadLocalRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|net
operator|.
name|smtp
operator|.
name|AuthSMTPClient
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
name|net
operator|.
name|smtp
operator|.
name|SMTPClient
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
name|net
operator|.
name|smtp
operator|.
name|SMTPReply
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|codec
operator|.
name|QuotedPrintableOutputStream
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
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/** Sends email via a nearby SMTP server. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|SmtpEmailSender
specifier|public
class|class
name|SmtpEmailSender
implements|implements
name|EmailSender
block|{
comment|/** The socket's connect timeout (0 = infinite timeout) */
DECL|field|DEFAULT_CONNECT_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CONNECT_TIMEOUT
init|=
literal|0
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|EmailSender
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SmtpEmailSender
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|enabled
specifier|private
specifier|final
name|boolean
name|enabled
decl_stmt|;
DECL|field|connectTimeout
specifier|private
specifier|final
name|int
name|connectTimeout
decl_stmt|;
DECL|field|smtpHost
specifier|private
name|String
name|smtpHost
decl_stmt|;
DECL|field|smtpPort
specifier|private
name|int
name|smtpPort
decl_stmt|;
DECL|field|smtpUser
specifier|private
name|String
name|smtpUser
decl_stmt|;
DECL|field|smtpPass
specifier|private
name|String
name|smtpPass
decl_stmt|;
DECL|field|smtpEncryption
specifier|private
name|Encryption
name|smtpEncryption
decl_stmt|;
DECL|field|sslVerify
specifier|private
name|boolean
name|sslVerify
decl_stmt|;
DECL|field|allowrcpt
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|allowrcpt
decl_stmt|;
DECL|field|importance
specifier|private
name|String
name|importance
decl_stmt|;
DECL|field|expiryDays
specifier|private
name|int
name|expiryDays
decl_stmt|;
annotation|@
name|Inject
DECL|method|SmtpEmailSender (@erritServerConfig Config cfg)
name|SmtpEmailSender
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|enabled
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"enable"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|connectTimeout
operator|=
name|Ints
operator|.
name|checkedCast
argument_list|(
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|cfg
argument_list|,
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"connectTimeout"
argument_list|,
name|DEFAULT_CONNECT_TIMEOUT
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|smtpHost
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpserver"
argument_list|)
expr_stmt|;
if|if
condition|(
name|smtpHost
operator|==
literal|null
condition|)
block|{
name|smtpHost
operator|=
literal|"127.0.0.1"
expr_stmt|;
block|}
name|smtpEncryption
operator|=
name|cfg
operator|.
name|getEnum
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpencryption"
argument_list|,
name|Encryption
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|sslVerify
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"sslverify"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|int
name|defaultPort
decl_stmt|;
switch|switch
condition|(
name|smtpEncryption
condition|)
block|{
case|case
name|SSL
case|:
name|defaultPort
operator|=
literal|465
expr_stmt|;
break|break;
case|case
name|NONE
case|:
case|case
name|TLS
case|:
default|default:
name|defaultPort
operator|=
literal|25
expr_stmt|;
break|break;
block|}
name|smtpPort
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpserverport"
argument_list|,
name|defaultPort
argument_list|)
expr_stmt|;
name|smtpUser
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpuser"
argument_list|)
expr_stmt|;
name|smtpPass
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtppass"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|rcpt
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|rcpt
argument_list|,
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"allowrcpt"
argument_list|)
argument_list|)
expr_stmt|;
name|allowrcpt
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|rcpt
argument_list|)
expr_stmt|;
name|importance
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"importance"
argument_list|)
expr_stmt|;
name|expiryDays
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"expiryDays"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
annotation|@
name|Override
DECL|method|canEmail (String address)
specifier|public
name|boolean
name|canEmail
parameter_list|(
name|String
name|address
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isEnabled
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|allowrcpt
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|allowrcpt
operator|.
name|contains
argument_list|(
name|address
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|domain
init|=
name|address
operator|.
name|substring
argument_list|(
name|address
operator|.
name|lastIndexOf
argument_list|(
literal|'@'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|allowrcpt
operator|.
name|contains
argument_list|(
name|domain
argument_list|)
operator|||
name|allowrcpt
operator|.
name|contains
argument_list|(
literal|"@"
operator|+
name|domain
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|send ( final Address from, Collection<Address> rcpt, final Map<String, EmailHeader> callerHeaders, String body)
specifier|public
name|void
name|send
parameter_list|(
specifier|final
name|Address
name|from
parameter_list|,
name|Collection
argument_list|<
name|Address
argument_list|>
name|rcpt
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|callerHeaders
parameter_list|,
name|String
name|body
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
name|callerHeaders
argument_list|,
name|body
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|send ( final Address from, Collection<Address> rcpt, final Map<String, EmailHeader> callerHeaders, String textBody, @Nullable String htmlBody)
specifier|public
name|void
name|send
parameter_list|(
specifier|final
name|Address
name|from
parameter_list|,
name|Collection
argument_list|<
name|Address
argument_list|>
name|rcpt
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|callerHeaders
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
if|if
condition|(
operator|!
name|isEnabled
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Sending email is disabled"
argument_list|)
throw|;
block|}
name|StringBuffer
name|rejected
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|SMTPClient
name|client
init|=
name|open
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|client
operator|.
name|setSender
argument_list|(
name|from
operator|.
name|getEmail
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Server "
operator|+
name|smtpHost
operator|+
literal|" rejected from address "
operator|+
name|from
operator|.
name|getEmail
argument_list|()
argument_list|)
throw|;
block|}
comment|/* Do not prevent the email from being sent to "good" users simply          * because some users get rejected.  If not, a single rejected          * project watcher could prevent email for most actions on a project          * from being sent to any user!  Instead, queue up the errors, and          * throw an exception after sending the email to get the rejected          * error(s) logged.          */
for|for
control|(
name|Address
name|addr
range|:
name|rcpt
control|)
block|{
if|if
condition|(
operator|!
name|client
operator|.
name|addRecipient
argument_list|(
name|addr
operator|.
name|getEmail
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|error
init|=
name|client
operator|.
name|getReplyString
argument_list|()
decl_stmt|;
name|rejected
operator|.
name|append
argument_list|(
literal|"Server "
argument_list|)
operator|.
name|append
argument_list|(
name|smtpHost
argument_list|)
operator|.
name|append
argument_list|(
literal|" rejected recipient "
argument_list|)
operator|.
name|append
argument_list|(
name|addr
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
try|try
init|(
name|Writer
name|messageDataWriter
init|=
name|client
operator|.
name|sendMessageData
argument_list|()
init|)
block|{
if|if
condition|(
name|messageDataWriter
operator|==
literal|null
condition|)
block|{
comment|/* Include rejected recipient error messages here to not lose that              * information. That piece of the puzzle is vital if zero recipients              * are accepted and the server consequently rejects the DATA command.              */
throw|throw
operator|new
name|EmailException
argument_list|(
name|rejected
operator|+
literal|"Server "
operator|+
name|smtpHost
operator|+
literal|" rejected DATA command: "
operator|+
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|)
throw|;
block|}
name|render
argument_list|(
name|messageDataWriter
argument_list|,
name|callerHeaders
argument_list|,
name|textBody
argument_list|,
name|htmlBody
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|client
operator|.
name|completePendingCommand
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Server "
operator|+
name|smtpHost
operator|+
literal|" rejected message body: "
operator|+
name|client
operator|.
name|getReplyString
argument_list|()
argument_list|)
throw|;
block|}
name|client
operator|.
name|logout
argument_list|()
expr_stmt|;
if|if
condition|(
name|rejected
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
name|rejected
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
finally|finally
block|{
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Cannot send outgoing email"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|render ( Writer out, Map<String, EmailHeader> callerHeaders, String textBody, @Nullable String htmlBody)
specifier|private
name|void
name|render
parameter_list|(
name|Writer
name|out
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|callerHeaders
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
name|IOException
throws|,
name|EmailException
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|hdrs
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|callerHeaders
argument_list|)
decl_stmt|;
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"MIME-Version"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"Content-Transfer-Encoding"
argument_list|,
literal|"8bit"
argument_list|)
expr_stmt|;
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"Content-Disposition"
argument_list|,
literal|"inline"
argument_list|)
expr_stmt|;
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"User-Agent"
argument_list|,
literal|"Gerrit/"
operator|+
name|Version
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|importance
operator|!=
literal|null
condition|)
block|{
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"Importance"
argument_list|,
name|importance
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expiryDays
operator|>
literal|0
condition|)
block|{
name|Date
name|expiry
init|=
operator|new
name|Date
argument_list|(
name|TimeUtil
operator|.
name|nowMs
argument_list|()
operator|+
name|expiryDays
operator|*
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
argument_list|)
decl_stmt|;
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"Expiry-Date"
argument_list|,
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss Z"
argument_list|)
operator|.
name|format
argument_list|(
name|expiry
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|encodedBody
decl_stmt|;
if|if
condition|(
name|htmlBody
operator|==
literal|null
condition|)
block|{
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"Content-Type"
argument_list|,
literal|"text/plain; charset=UTF-8"
argument_list|)
expr_stmt|;
name|encodedBody
operator|=
name|textBody
expr_stmt|;
block|}
else|else
block|{
name|String
name|boundary
init|=
name|generateMultipartBoundary
argument_list|(
name|textBody
argument_list|,
name|htmlBody
argument_list|)
decl_stmt|;
name|setMissingHeader
argument_list|(
name|hdrs
argument_list|,
literal|"Content-Type"
argument_list|,
literal|"multipart/alternative; boundary=\""
operator|+
name|boundary
operator|+
literal|"\"; charset=UTF-8"
argument_list|)
expr_stmt|;
name|encodedBody
operator|=
name|buildMultipartBody
argument_list|(
name|boundary
argument_list|,
name|textBody
argument_list|,
name|htmlBody
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|Writer
name|w
init|=
operator|new
name|BufferedWriter
argument_list|(
name|out
argument_list|)
init|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|h
range|:
name|hdrs
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|h
operator|.
name|getValue
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|w
operator|.
name|write
argument_list|(
name|h
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|h
operator|.
name|getValue
argument_list|()
operator|.
name|write
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
block|}
block|}
name|w
operator|.
name|write
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
name|encodedBody
argument_list|)
expr_stmt|;
name|w
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|generateMultipartBoundary (String textBody, String htmlBody)
specifier|public
specifier|static
name|String
name|generateMultipartBoundary
parameter_list|(
name|String
name|textBody
parameter_list|,
name|String
name|htmlBody
parameter_list|)
throws|throws
name|EmailException
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|8
index|]
decl_stmt|;
name|ThreadLocalRandom
name|rng
init|=
name|ThreadLocalRandom
operator|.
name|current
argument_list|()
decl_stmt|;
comment|// The probability of the boundary being valid is approximately
comment|// (2^64 - len(message)) / 2^64.
comment|//
comment|// The message is much shorter than 2^64 bytes, so if two tries don't
comment|// suffice, something is seriously wrong.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|rng
operator|.
name|nextBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|String
name|boundary
init|=
name|BaseEncoding
operator|.
name|base64
argument_list|()
operator|.
name|encode
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|String
name|encBoundary
init|=
literal|"--"
operator|+
name|boundary
decl_stmt|;
if|if
condition|(
name|textBody
operator|.
name|contains
argument_list|(
name|encBoundary
argument_list|)
operator|||
name|htmlBody
operator|.
name|contains
argument_list|(
name|encBoundary
argument_list|)
condition|)
block|{
continue|continue;
block|}
return|return
name|boundary
return|;
block|}
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"Gave up generating unique MIME boundary"
argument_list|)
throw|;
block|}
DECL|method|buildMultipartBody (String boundary, String textPart, String htmlPart)
specifier|protected
name|String
name|buildMultipartBody
parameter_list|(
name|String
name|boundary
parameter_list|,
name|String
name|textPart
parameter_list|,
name|String
name|htmlPart
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|encodedTextPart
init|=
name|quotedPrintableEncode
argument_list|(
name|textPart
argument_list|)
decl_stmt|;
name|String
name|encodedHtmlPart
init|=
name|quotedPrintableEncode
argument_list|(
name|htmlPart
argument_list|)
decl_stmt|;
comment|// Only declare quoted-printable encoding if there are characters that need to be encoded.
name|String
name|textTransferEncoding
init|=
name|textPart
operator|.
name|equals
argument_list|(
name|encodedTextPart
argument_list|)
condition|?
literal|"7bit"
else|:
literal|"quoted-printable"
decl_stmt|;
name|String
name|htmlTransferEncoding
init|=
name|htmlPart
operator|.
name|equals
argument_list|(
name|encodedHtmlPart
argument_list|)
condition|?
literal|"7bit"
else|:
literal|"quoted-printable"
decl_stmt|;
return|return
comment|// Output the text part:
literal|"--"
operator|+
name|boundary
operator|+
literal|"\r\n"
operator|+
literal|"Content-Type: text/plain; charset=UTF-8\r\n"
operator|+
literal|"Content-Transfer-Encoding: "
operator|+
name|textTransferEncoding
operator|+
literal|"\r\n"
operator|+
literal|"\r\n"
operator|+
name|encodedTextPart
operator|+
literal|"\r\n"
comment|// Output the HTML part:
operator|+
literal|"--"
operator|+
name|boundary
operator|+
literal|"\r\n"
operator|+
literal|"Content-Type: text/html; charset=UTF-8\r\n"
operator|+
literal|"Content-Transfer-Encoding: "
operator|+
name|htmlTransferEncoding
operator|+
literal|"\r\n"
operator|+
literal|"\r\n"
operator|+
name|encodedHtmlPart
operator|+
literal|"\r\n"
comment|// Output the closing boundary.
operator|+
literal|"--"
operator|+
name|boundary
operator|+
literal|"--\r\n"
return|;
block|}
DECL|method|quotedPrintableEncode (String input)
specifier|protected
name|String
name|quotedPrintableEncode
parameter_list|(
name|String
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|s
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|QuotedPrintableOutputStream
name|qp
init|=
operator|new
name|QuotedPrintableOutputStream
argument_list|(
name|s
argument_list|,
literal|false
argument_list|)
init|)
block|{
name|qp
operator|.
name|write
argument_list|(
name|input
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|setMissingHeader (Map<String, EmailHeader> hdrs, String name, String value)
specifier|private
specifier|static
name|void
name|setMissingHeader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|hdrs
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hdrs
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
operator|||
name|hdrs
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|hdrs
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
block|}
DECL|method|open ()
specifier|private
name|SMTPClient
name|open
parameter_list|()
throws|throws
name|EmailException
block|{
specifier|final
name|AuthSMTPClient
name|client
init|=
operator|new
name|AuthSMTPClient
argument_list|(
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|smtpEncryption
operator|==
name|Encryption
operator|.
name|SSL
condition|)
block|{
name|client
operator|.
name|enableSSL
argument_list|(
name|sslVerify
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|setConnectTimeout
argument_list|(
name|connectTimeout
argument_list|)
expr_stmt|;
try|try
block|{
name|client
operator|.
name|connect
argument_list|(
name|smtpHost
argument_list|,
name|smtpPort
argument_list|)
expr_stmt|;
name|int
name|replyCode
init|=
name|client
operator|.
name|getReplyCode
argument_list|()
decl_stmt|;
name|String
name|replyString
init|=
name|client
operator|.
name|getReplyString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|SMTPReply
operator|.
name|isPositiveCompletion
argument_list|(
name|replyCode
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"SMTP server rejected connection: %d: %s"
argument_list|,
name|replyCode
argument_list|,
name|replyString
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|client
operator|.
name|login
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"SMTP server rejected HELO/EHLO greeting: "
operator|+
name|replyString
argument_list|)
throw|;
block|}
if|if
condition|(
name|smtpEncryption
operator|==
name|Encryption
operator|.
name|TLS
condition|)
block|{
if|if
condition|(
operator|!
name|client
operator|.
name|startTLS
argument_list|(
name|smtpHost
argument_list|,
name|smtpPort
argument_list|,
name|sslVerify
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"SMTP server does not support TLS"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|client
operator|.
name|login
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"SMTP server rejected login: "
operator|+
name|replyString
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|smtpUser
operator|!=
literal|null
operator|&&
operator|!
name|client
operator|.
name|auth
argument_list|(
name|smtpUser
argument_list|,
name|smtpPass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|EmailException
argument_list|(
literal|"SMTP server rejected auth: "
operator|+
name|replyString
argument_list|)
throw|;
block|}
return|return
name|client
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|EmailException
name|e
parameter_list|)
block|{
if|if
condition|(
name|client
operator|.
name|isConnected
argument_list|()
condition|)
block|{
try|try
block|{
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
comment|// Ignored
block|}
block|}
if|if
condition|(
name|e
operator|instanceof
name|EmailException
condition|)
block|{
throw|throw
operator|(
name|EmailException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|EmailException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

