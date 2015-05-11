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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Function
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
name|base
operator|.
name|Optional
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
name|collect
operator|.
name|Iterables
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
name|collect
operator|.
name|Lists
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
name|data
operator|.
name|GitWebType
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
name|config
operator|.
name|DownloadCommand
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
name|config
operator|.
name|DownloadScheme
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
name|registration
operator|.
name|DynamicMap
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
name|restapi
operator|.
name|RestReadView
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
name|client
operator|.
name|AuthType
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
name|Realm
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
name|change
operator|.
name|ArchiveFormat
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
name|change
operator|.
name|GetArchive
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
name|HashMap
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

begin_class
DECL|class|GetServerInfo
specifier|public
class|class
name|GetServerInfo
implements|implements
name|RestReadView
argument_list|<
name|ConfigResource
argument_list|>
block|{
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|downloadSchemes
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
decl_stmt|;
DECL|field|downloadCommands
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
decl_stmt|;
DECL|field|archiveFormats
specifier|private
specifier|final
name|GetArchive
operator|.
name|AllowedFormats
name|archiveFormats
decl_stmt|;
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|anonymousCowardName
specifier|private
specifier|final
name|String
name|anonymousCowardName
decl_stmt|;
DECL|field|gitWebConfig
specifier|private
specifier|final
name|GitWebConfig
name|gitWebConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetServerInfo ( @erritServerConfig Config config, AuthConfig authConfig, Realm realm, DynamicMap<DownloadScheme> downloadSchemes, DynamicMap<DownloadCommand> downloadCommands, GetArchive.AllowedFormats archiveFormats, AllProjectsName allProjectsName, AllUsersName allUsersName, @AnonymousCowardName String anonymousCowardName, GitWebConfig gitWebConfig)
specifier|public
name|GetServerInfo
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
name|Realm
name|realm
parameter_list|,
name|DynamicMap
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
parameter_list|,
name|DynamicMap
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
parameter_list|,
name|GetArchive
operator|.
name|AllowedFormats
name|archiveFormats
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
name|GitWebConfig
name|gitWebConfig
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
name|this
operator|.
name|downloadSchemes
operator|=
name|downloadSchemes
expr_stmt|;
name|this
operator|.
name|downloadCommands
operator|=
name|downloadCommands
expr_stmt|;
name|this
operator|.
name|archiveFormats
operator|=
name|archiveFormats
expr_stmt|;
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
name|this
operator|.
name|gitWebConfig
operator|=
name|gitWebConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource rsrc)
specifier|public
name|ServerInfo
name|apply
parameter_list|(
name|ConfigResource
name|rsrc
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|ServerInfo
name|info
init|=
operator|new
name|ServerInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|auth
operator|=
name|getAuthInfo
argument_list|(
name|authConfig
argument_list|,
name|realm
argument_list|)
expr_stmt|;
name|info
operator|.
name|change
operator|=
name|getChangeInfo
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|info
operator|.
name|contactStore
operator|=
name|getContactStoreInfo
argument_list|()
expr_stmt|;
name|info
operator|.
name|download
operator|=
name|getDownloadInfo
argument_list|(
name|downloadSchemes
argument_list|,
name|downloadCommands
argument_list|,
name|archiveFormats
argument_list|)
expr_stmt|;
name|info
operator|.
name|gerrit
operator|=
name|getGerritInfo
argument_list|(
name|config
argument_list|,
name|allProjectsName
argument_list|,
name|allUsersName
argument_list|)
expr_stmt|;
name|info
operator|.
name|gitWeb
operator|=
name|getGitWebInfo
argument_list|(
name|gitWebConfig
argument_list|)
expr_stmt|;
name|info
operator|.
name|suggest
operator|=
name|getSuggestInfo
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|info
operator|.
name|user
operator|=
name|getUserInfo
argument_list|(
name|anonymousCowardName
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|getAuthInfo (AuthConfig cfg, Realm realm)
specifier|private
name|AuthInfo
name|getAuthInfo
parameter_list|(
name|AuthConfig
name|cfg
parameter_list|,
name|Realm
name|realm
parameter_list|)
block|{
name|AuthInfo
name|info
init|=
operator|new
name|AuthInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|authType
operator|=
name|cfg
operator|.
name|getAuthType
argument_list|()
expr_stmt|;
name|info
operator|.
name|useContributorAgreements
operator|=
name|toBoolean
argument_list|(
name|cfg
operator|.
name|isUseContributorAgreements
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|editableAccountFields
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|realm
operator|.
name|getEditableFields
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|switchAccountUrl
operator|=
name|cfg
operator|.
name|getSwitchAccountUrl
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|info
operator|.
name|authType
condition|)
block|{
case|case
name|LDAP
case|:
case|case
name|LDAP_BIND
case|:
name|info
operator|.
name|registerUrl
operator|=
name|cfg
operator|.
name|getRegisterUrl
argument_list|()
expr_stmt|;
name|info
operator|.
name|registerText
operator|=
name|cfg
operator|.
name|getRegisterText
argument_list|()
expr_stmt|;
name|info
operator|.
name|editFullNameUrl
operator|=
name|cfg
operator|.
name|getEditFullNameUrl
argument_list|()
expr_stmt|;
name|info
operator|.
name|isGitBasicAuth
operator|=
name|toBoolean
argument_list|(
name|cfg
operator|.
name|isGitBasicAuth
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|CUSTOM_EXTENSION
case|:
name|info
operator|.
name|registerUrl
operator|=
name|cfg
operator|.
name|getRegisterUrl
argument_list|()
expr_stmt|;
name|info
operator|.
name|registerText
operator|=
name|cfg
operator|.
name|getRegisterText
argument_list|()
expr_stmt|;
name|info
operator|.
name|editFullNameUrl
operator|=
name|cfg
operator|.
name|getEditFullNameUrl
argument_list|()
expr_stmt|;
name|info
operator|.
name|httpPasswordUrl
operator|=
name|cfg
operator|.
name|getHttpPasswordUrl
argument_list|()
expr_stmt|;
break|break;
case|case
name|HTTP
case|:
case|case
name|HTTP_LDAP
case|:
name|info
operator|.
name|loginUrl
operator|=
name|cfg
operator|.
name|getLoginUrl
argument_list|()
expr_stmt|;
name|info
operator|.
name|loginText
operator|=
name|cfg
operator|.
name|getLoginText
argument_list|()
expr_stmt|;
break|break;
case|case
name|CLIENT_SSL_CERT_LDAP
case|:
case|case
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
case|:
case|case
name|OAUTH
case|:
case|case
name|OPENID
case|:
case|case
name|OPENID_SSO
case|:
break|break;
block|}
return|return
name|info
return|;
block|}
DECL|method|getChangeInfo (Config cfg)
specifier|private
name|ChangeConfigInfo
name|getChangeInfo
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|ChangeConfigInfo
name|info
init|=
operator|new
name|ChangeConfigInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|allowDrafts
operator|=
name|toBoolean
argument_list|(
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"change"
argument_list|,
literal|"allowDrafts"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|largeChange
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"change"
argument_list|,
literal|"largeChange"
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|info
operator|.
name|replyTooltip
operator|=
name|Optional
operator|.
name|fromNullable
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"replyTooltip"
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
literal|"Reply and score"
argument_list|)
operator|+
literal|" (Shortcut: a)"
expr_stmt|;
name|info
operator|.
name|replyLabel
operator|=
name|Optional
operator|.
name|fromNullable
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"replyLabel"
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
literal|"Reply"
argument_list|)
operator|+
literal|"\u2026"
expr_stmt|;
name|info
operator|.
name|updateDelay
operator|=
operator|(
name|int
operator|)
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|cfg
argument_list|,
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"updateDelay"
argument_list|,
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|getContactStoreInfo ()
specifier|private
name|ContactStoreInfo
name|getContactStoreInfo
parameter_list|()
block|{
name|String
name|url
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"contactstore"
argument_list|,
literal|null
argument_list|,
literal|"url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ContactStoreInfo
name|contactStore
init|=
operator|new
name|ContactStoreInfo
argument_list|()
decl_stmt|;
name|contactStore
operator|.
name|url
operator|=
name|url
expr_stmt|;
return|return
name|contactStore
return|;
block|}
DECL|method|getDownloadInfo (DynamicMap<DownloadScheme> downloadSchemes, DynamicMap<DownloadCommand> downloadCommands, GetArchive.AllowedFormats archiveFormats)
specifier|private
name|DownloadInfo
name|getDownloadInfo
parameter_list|(
name|DynamicMap
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
parameter_list|,
name|DynamicMap
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
parameter_list|,
name|GetArchive
operator|.
name|AllowedFormats
name|archiveFormats
parameter_list|)
block|{
name|DownloadInfo
name|info
init|=
operator|new
name|DownloadInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|schemes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|DynamicMap
operator|.
name|Entry
argument_list|<
name|DownloadScheme
argument_list|>
name|e
range|:
name|downloadSchemes
control|)
block|{
name|DownloadScheme
name|scheme
init|=
name|e
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheme
operator|.
name|isEnabled
argument_list|()
operator|&&
name|scheme
operator|.
name|getUrl
argument_list|(
literal|"${project}"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|schemes
operator|.
name|put
argument_list|(
name|e
operator|.
name|getExportName
argument_list|()
argument_list|,
name|getDownloadSchemeInfo
argument_list|(
name|scheme
argument_list|,
name|downloadCommands
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|info
operator|.
name|archives
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|archiveFormats
operator|.
name|getAllowed
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|ArchiveFormat
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|ArchiveFormat
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|getShortName
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|getDownloadSchemeInfo (DownloadScheme scheme, DynamicMap<DownloadCommand> downloadCommands)
specifier|private
name|DownloadSchemeInfo
name|getDownloadSchemeInfo
parameter_list|(
name|DownloadScheme
name|scheme
parameter_list|,
name|DynamicMap
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
parameter_list|)
block|{
name|DownloadSchemeInfo
name|info
init|=
operator|new
name|DownloadSchemeInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|url
operator|=
name|scheme
operator|.
name|getUrl
argument_list|(
literal|"${project}"
argument_list|)
expr_stmt|;
name|info
operator|.
name|isAuthRequired
operator|=
name|toBoolean
argument_list|(
name|scheme
operator|.
name|isAuthRequired
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|isAuthSupported
operator|=
name|toBoolean
argument_list|(
name|scheme
operator|.
name|isAuthSupported
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|commands
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|DynamicMap
operator|.
name|Entry
argument_list|<
name|DownloadCommand
argument_list|>
name|e
range|:
name|downloadCommands
control|)
block|{
name|String
name|commandName
init|=
name|e
operator|.
name|getExportName
argument_list|()
decl_stmt|;
name|DownloadCommand
name|command
init|=
name|e
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|c
init|=
name|command
operator|.
name|getCommand
argument_list|(
name|scheme
argument_list|,
literal|"${project}"
argument_list|,
literal|"${ref}"
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|commands
operator|.
name|put
argument_list|(
name|commandName
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|info
return|;
block|}
DECL|method|getGerritInfo (Config cfg, AllProjectsName allProjectsName, AllUsersName allUsersName)
specifier|private
name|GerritInfo
name|getGerritInfo
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|)
block|{
name|GerritInfo
name|info
init|=
operator|new
name|GerritInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|allProjects
operator|=
name|allProjectsName
operator|.
name|get
argument_list|()
expr_stmt|;
name|info
operator|.
name|allUsers
operator|=
name|allUsersName
operator|.
name|get
argument_list|()
expr_stmt|;
name|info
operator|.
name|reportBugUrl
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"reportBugUrl"
argument_list|)
expr_stmt|;
name|info
operator|.
name|reportBugText
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"reportBugText"
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|getGitWebInfo (GitWebConfig cfg)
specifier|private
name|GitWebInfo
name|getGitWebInfo
parameter_list|(
name|GitWebConfig
name|cfg
parameter_list|)
block|{
if|if
condition|(
name|cfg
operator|.
name|getUrl
argument_list|()
operator|==
literal|null
operator|||
name|cfg
operator|.
name|getGitWebType
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|GitWebInfo
name|info
init|=
operator|new
name|GitWebInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|url
operator|=
name|cfg
operator|.
name|getUrl
argument_list|()
expr_stmt|;
name|info
operator|.
name|type
operator|=
name|cfg
operator|.
name|getGitWebType
argument_list|()
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|getSuggestInfo (Config cfg)
specifier|private
name|SuggestInfo
name|getSuggestInfo
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|SuggestInfo
name|info
init|=
operator|new
name|SuggestInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|from
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"suggest"
argument_list|,
literal|"from"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|getUserInfo (String anonymousCowardName)
specifier|private
name|UserConfigInfo
name|getUserInfo
parameter_list|(
name|String
name|anonymousCowardName
parameter_list|)
block|{
name|UserConfigInfo
name|info
init|=
operator|new
name|UserConfigInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|toBoolean (boolean v)
specifier|private
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|boolean
name|v
parameter_list|)
block|{
return|return
name|v
condition|?
name|v
else|:
literal|null
return|;
block|}
DECL|class|ServerInfo
specifier|public
specifier|static
class|class
name|ServerInfo
block|{
DECL|field|auth
specifier|public
name|AuthInfo
name|auth
decl_stmt|;
DECL|field|change
specifier|public
name|ChangeConfigInfo
name|change
decl_stmt|;
DECL|field|contactStore
specifier|public
name|ContactStoreInfo
name|contactStore
decl_stmt|;
DECL|field|download
specifier|public
name|DownloadInfo
name|download
decl_stmt|;
DECL|field|gerrit
specifier|public
name|GerritInfo
name|gerrit
decl_stmt|;
DECL|field|gitWeb
specifier|public
name|GitWebInfo
name|gitWeb
decl_stmt|;
DECL|field|suggest
specifier|public
name|SuggestInfo
name|suggest
decl_stmt|;
DECL|field|user
specifier|public
name|UserConfigInfo
name|user
decl_stmt|;
block|}
DECL|class|AuthInfo
specifier|public
specifier|static
class|class
name|AuthInfo
block|{
DECL|field|authType
specifier|public
name|AuthType
name|authType
decl_stmt|;
DECL|field|useContributorAgreements
specifier|public
name|Boolean
name|useContributorAgreements
decl_stmt|;
DECL|field|editableAccountFields
specifier|public
name|List
argument_list|<
name|Account
operator|.
name|FieldName
argument_list|>
name|editableAccountFields
decl_stmt|;
DECL|field|loginUrl
specifier|public
name|String
name|loginUrl
decl_stmt|;
DECL|field|loginText
specifier|public
name|String
name|loginText
decl_stmt|;
DECL|field|switchAccountUrl
specifier|public
name|String
name|switchAccountUrl
decl_stmt|;
DECL|field|registerUrl
specifier|public
name|String
name|registerUrl
decl_stmt|;
DECL|field|registerText
specifier|public
name|String
name|registerText
decl_stmt|;
DECL|field|editFullNameUrl
specifier|public
name|String
name|editFullNameUrl
decl_stmt|;
DECL|field|httpPasswordUrl
specifier|public
name|String
name|httpPasswordUrl
decl_stmt|;
DECL|field|isGitBasicAuth
specifier|public
name|Boolean
name|isGitBasicAuth
decl_stmt|;
block|}
DECL|class|ChangeConfigInfo
specifier|public
specifier|static
class|class
name|ChangeConfigInfo
block|{
DECL|field|allowDrafts
specifier|public
name|Boolean
name|allowDrafts
decl_stmt|;
DECL|field|largeChange
specifier|public
name|int
name|largeChange
decl_stmt|;
DECL|field|replyLabel
specifier|public
name|String
name|replyLabel
decl_stmt|;
DECL|field|replyTooltip
specifier|public
name|String
name|replyTooltip
decl_stmt|;
DECL|field|updateDelay
specifier|public
name|int
name|updateDelay
decl_stmt|;
block|}
DECL|class|ContactStoreInfo
specifier|public
specifier|static
class|class
name|ContactStoreInfo
block|{
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
block|}
DECL|class|DownloadInfo
specifier|public
specifier|static
class|class
name|DownloadInfo
block|{
DECL|field|schemes
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DownloadSchemeInfo
argument_list|>
name|schemes
decl_stmt|;
DECL|field|archives
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|archives
decl_stmt|;
block|}
DECL|class|DownloadSchemeInfo
specifier|public
specifier|static
class|class
name|DownloadSchemeInfo
block|{
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|isAuthRequired
specifier|public
name|Boolean
name|isAuthRequired
decl_stmt|;
DECL|field|isAuthSupported
specifier|public
name|Boolean
name|isAuthSupported
decl_stmt|;
DECL|field|commands
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|commands
decl_stmt|;
block|}
DECL|class|GerritInfo
specifier|public
specifier|static
class|class
name|GerritInfo
block|{
DECL|field|allProjects
specifier|public
name|String
name|allProjects
decl_stmt|;
DECL|field|allUsers
specifier|public
name|String
name|allUsers
decl_stmt|;
DECL|field|reportBugUrl
specifier|public
name|String
name|reportBugUrl
decl_stmt|;
DECL|field|reportBugText
specifier|public
name|String
name|reportBugText
decl_stmt|;
block|}
DECL|class|GitWebInfo
specifier|public
specifier|static
class|class
name|GitWebInfo
block|{
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|type
specifier|public
name|GitWebType
name|type
decl_stmt|;
block|}
DECL|class|SuggestInfo
specifier|public
specifier|static
class|class
name|SuggestInfo
block|{
DECL|field|from
specifier|public
name|int
name|from
decl_stmt|;
block|}
DECL|class|UserConfigInfo
specifier|public
specifier|static
class|class
name|UserConfigInfo
block|{
DECL|field|anonymousCowardName
specifier|public
name|String
name|anonymousCowardName
decl_stmt|;
block|}
block|}
end_class

end_unit

