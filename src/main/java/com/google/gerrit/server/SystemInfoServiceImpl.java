begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|SshHostKey
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
name|SystemInfoService
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
name|ContributorAgreement
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
name|jcraft
operator|.
name|jsch
operator|.
name|HostKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSch
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|util
operator|.
name|Buffer
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
name|security
operator|.
name|PublicKey
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_class
DECL|class|SystemInfoServiceImpl
specifier|public
class|class
name|SystemInfoServiceImpl
implements|implements
name|SystemInfoService
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
name|SystemInfoServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|JSCH
specifier|private
specifier|static
specifier|final
name|JSch
name|JSCH
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
DECL|method|loadGerritConfig (final AsyncCallback<GerritConfig> callback)
specifier|public
name|void
name|loadGerritConfig
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|GerritConfig
argument_list|>
name|callback
parameter_list|)
block|{
name|callback
operator|.
name|onSuccess
argument_list|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|contributorAgreements ( final AsyncCallback<List<ContributorAgreement>> callback)
specifier|public
name|void
name|contributorAgreements
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|ContributorAgreement
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|Common
operator|.
name|getSchemaFactory
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
name|callback
operator|.
name|onSuccess
argument_list|(
name|db
operator|.
name|contributorAgreements
argument_list|()
operator|.
name|active
argument_list|()
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|daemonHostKeys (final AsyncCallback<List<SshHostKey>> callback)
specifier|public
name|void
name|daemonHostKeys
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|SshHostKey
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
specifier|final
name|String
name|hostIdent
init|=
name|hostIdent
argument_list|()
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|PublicKey
argument_list|>
name|keys
init|=
name|GerritSshDaemon
operator|.
name|getHostKeys
argument_list|()
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|SshHostKey
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|SshHostKey
argument_list|>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|PublicKey
name|pub
range|:
name|keys
control|)
block|{
try|try
block|{
specifier|final
name|HostKey
name|hk
init|=
name|toHostKey
argument_list|(
name|hostIdent
argument_list|,
name|pub
argument_list|)
decl_stmt|;
name|r
operator|.
name|add
argument_list|(
operator|new
name|SshHostKey
argument_list|(
name|hk
operator|.
name|getHost
argument_list|()
argument_list|,
name|hk
operator|.
name|getType
argument_list|()
operator|+
literal|" "
operator|+
name|hk
operator|.
name|getKey
argument_list|()
argument_list|,
name|hk
operator|.
name|getFingerPrint
argument_list|(
name|JSCH
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid host key"
argument_list|,
name|e
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
name|callback
operator|.
name|onSuccess
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|toHostKey (final String hostIdent, final PublicKey pub)
specifier|private
name|HostKey
name|toHostKey
parameter_list|(
specifier|final
name|String
name|hostIdent
parameter_list|,
specifier|final
name|PublicKey
name|pub
parameter_list|)
throws|throws
name|JSchException
block|{
specifier|final
name|Buffer
name|buf
init|=
operator|new
name|Buffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|putPublicKey
argument_list|(
name|pub
argument_list|)
expr_stmt|;
specifier|final
name|byte
index|[]
name|keyBin
init|=
name|buf
operator|.
name|getCompactData
argument_list|()
decl_stmt|;
return|return
operator|new
name|HostKey
argument_list|(
name|hostIdent
argument_list|,
name|keyBin
argument_list|)
return|;
block|}
DECL|method|hostIdent ()
specifier|private
name|String
name|hostIdent
parameter_list|()
block|{
specifier|final
name|HttpServletRequest
name|req
init|=
name|GerritJsonServlet
operator|.
name|getCurrentCall
argument_list|()
operator|.
name|getHttpServletRequest
argument_list|()
decl_stmt|;
specifier|final
name|String
name|serverName
init|=
name|req
operator|.
name|getServerName
argument_list|()
decl_stmt|;
specifier|final
name|int
name|serverPort
init|=
name|GerritSshDaemon
operator|.
name|getSshdPort
argument_list|()
decl_stmt|;
return|return
name|serverPort
operator|==
literal|22
condition|?
name|serverName
else|:
literal|"["
operator|+
name|serverName
operator|+
literal|"]:"
operator|+
name|serverPort
return|;
block|}
block|}
end_class

end_unit

