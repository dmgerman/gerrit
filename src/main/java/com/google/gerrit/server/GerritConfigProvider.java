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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|data
operator|.
name|ApprovalType
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
name|data
operator|.
name|GerritConfig
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
name|data
operator|.
name|GitwebLink
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
name|ApprovalCategory
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
name|ReviewDb
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
name|Common
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
name|AuthConfig
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
name|EmailSender
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
name|ssh
operator|.
name|GerritSshDaemon
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|Provider
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
name|ProvisionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Inet6Address
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_class
DECL|class|GerritConfigProvider
class|class
name|GerritConfigProvider
implements|implements
name|Provider
argument_list|<
name|GerritConfig
argument_list|>
block|{
DECL|method|isIPv6 (final InetAddress ip)
specifier|private
specifier|static
name|boolean
name|isIPv6
parameter_list|(
specifier|final
name|InetAddress
name|ip
parameter_list|)
block|{
return|return
name|ip
operator|instanceof
name|Inet6Address
operator|&&
name|ip
operator|.
name|getHostName
argument_list|()
operator|.
name|equals
argument_list|(
name|ip
operator|.
name|getHostAddress
argument_list|()
argument_list|)
return|;
block|}
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|sshd
specifier|private
name|GerritSshDaemon
name|sshd
decl_stmt|;
DECL|field|emailSender
specifier|private
name|EmailSender
name|emailSender
decl_stmt|;
DECL|field|contactStore
specifier|private
name|ContactStore
name|contactStore
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritConfigProvider (@erritServerConfig final Config gsc, final AuthConfig ac, final GerritServer gs, final SchemaFactory<ReviewDb> sf)
name|GerritConfigProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|gsc
parameter_list|,
specifier|final
name|AuthConfig
name|ac
parameter_list|,
specifier|final
name|GerritServer
name|gs
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|)
block|{
name|cfg
operator|=
name|gsc
expr_stmt|;
name|authConfig
operator|=
name|ac
expr_stmt|;
name|server
operator|=
name|gs
expr_stmt|;
name|schema
operator|=
name|sf
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setGerritSshDaemon (final GerritSshDaemon d)
name|void
name|setGerritSshDaemon
parameter_list|(
specifier|final
name|GerritSshDaemon
name|d
parameter_list|)
block|{
name|sshd
operator|=
name|d
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setEmailSender (final EmailSender d)
name|void
name|setEmailSender
parameter_list|(
specifier|final
name|EmailSender
name|d
parameter_list|)
block|{
name|emailSender
operator|=
name|d
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setContactStore (final ContactStore d)
name|void
name|setContactStore
parameter_list|(
specifier|final
name|ContactStore
name|d
parameter_list|)
block|{
name|contactStore
operator|=
name|d
expr_stmt|;
block|}
DECL|method|create ()
specifier|private
name|GerritConfig
name|create
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|GerritConfig
name|config
init|=
operator|new
name|GerritConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setCanonicalUrl
argument_list|(
name|server
operator|.
name|getCanonicalURL
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUseContributorAgreements
argument_list|(
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"contributoragreements"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setGitDaemonUrl
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"canonicalgiturl"
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUseRepoDownload
argument_list|(
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"repo"
argument_list|,
literal|null
argument_list|,
literal|"showdownloadcommand"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUseContactInfo
argument_list|(
name|contactStore
operator|!=
literal|null
operator|&&
name|contactStore
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setAllowRegisterNewEmail
argument_list|(
name|emailSender
operator|!=
literal|null
operator|&&
name|emailSender
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLoginType
argument_list|(
name|authConfig
operator|.
name|getLoginType
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|gitwebUrl
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gitweb"
argument_list|,
literal|null
argument_list|,
literal|"url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|gitwebUrl
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|setGitwebLink
argument_list|(
operator|new
name|GitwebLink
argument_list|(
name|gitwebUrl
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
specifier|final
name|ApprovalCategory
name|c
range|:
name|db
operator|.
name|approvalCategories
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
name|config
operator|.
name|add
argument_list|(
operator|new
name|ApprovalType
argument_list|(
name|c
argument_list|,
name|db
operator|.
name|approvalCategoryValues
argument_list|()
operator|.
name|byCategory
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|final
name|InetSocketAddress
name|addr
init|=
name|sshd
operator|!=
literal|null
condition|?
name|sshd
operator|.
name|getAddress
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|addr
operator|!=
literal|null
condition|)
block|{
specifier|final
name|InetAddress
name|ip
init|=
name|addr
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|String
name|host
decl_stmt|;
if|if
condition|(
name|ip
operator|!=
literal|null
operator|&&
name|ip
operator|.
name|isAnyLocalAddress
argument_list|()
condition|)
block|{
name|host
operator|=
literal|""
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isIPv6
argument_list|(
name|ip
argument_list|)
condition|)
block|{
name|host
operator|=
literal|"["
operator|+
name|addr
operator|.
name|getHostName
argument_list|()
operator|+
literal|"]"
expr_stmt|;
block|}
else|else
block|{
name|host
operator|=
name|addr
operator|.
name|getHostName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|addr
operator|.
name|getPort
argument_list|()
operator|!=
literal|22
condition|)
block|{
name|host
operator|+=
literal|":"
operator|+
name|addr
operator|.
name|getPort
argument_list|()
expr_stmt|;
block|}
name|config
operator|.
name|setSshdAddress
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
name|Common
operator|.
name|setGerritConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|GerritConfig
name|get
parameter_list|()
block|{
try|try
block|{
return|return
name|create
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot construct GerritConfig"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

