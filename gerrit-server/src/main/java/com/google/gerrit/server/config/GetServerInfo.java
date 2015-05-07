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
DECL|field|downloadConfig
specifier|private
specifier|final
name|DownloadConfig
name|downloadConfig
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
annotation|@
name|Inject
DECL|method|GetServerInfo ( @erritServerConfig Config config, AuthConfig authConfig, Realm realm, DownloadConfig downloadConfig, DynamicMap<DownloadScheme> downloadSchemes, DynamicMap<DownloadCommand> downloadCommands, AllProjectsName allProjectsName, AllUsersName allUsersName)
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
name|DownloadConfig
name|downloadConfig
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
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|AllUsersName
name|allUsersName
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
name|downloadConfig
operator|=
name|downloadConfig
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
operator|new
name|AuthInfo
argument_list|(
name|authConfig
argument_list|,
name|realm
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
operator|new
name|DownloadInfo
argument_list|(
name|downloadConfig
argument_list|,
name|downloadSchemes
argument_list|,
name|downloadCommands
argument_list|)
expr_stmt|;
name|info
operator|.
name|gerrit
operator|=
operator|new
name|GerritInfo
argument_list|(
name|allProjectsName
argument_list|,
name|allUsersName
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
DECL|method|AuthInfo (AuthConfig cfg, Realm realm)
specifier|public
name|AuthInfo
parameter_list|(
name|AuthConfig
name|cfg
parameter_list|,
name|Realm
name|realm
parameter_list|)
block|{
name|authType
operator|=
name|cfg
operator|.
name|getAuthType
argument_list|()
expr_stmt|;
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
block|}
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
name|ArchiveFormat
argument_list|>
name|archives
decl_stmt|;
DECL|method|DownloadInfo (DownloadConfig downloadConfig, DynamicMap<DownloadScheme> downloadSchemes, DynamicMap<DownloadCommand> downloadCommands)
specifier|public
name|DownloadInfo
parameter_list|(
name|DownloadConfig
name|downloadConfig
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
parameter_list|)
block|{
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
condition|)
block|{
name|schemes
operator|.
name|put
argument_list|(
name|e
operator|.
name|getExportName
argument_list|()
argument_list|,
operator|new
name|DownloadSchemeInfo
argument_list|(
name|scheme
argument_list|,
name|downloadCommands
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|archives
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|downloadConfig
operator|.
name|getArchiveFormats
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
DECL|method|DownloadSchemeInfo (DownloadScheme scheme, DynamicMap<DownloadCommand> downloadCommands)
specifier|public
name|DownloadSchemeInfo
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
name|url
operator|=
name|scheme
operator|.
name|getUrl
argument_list|(
literal|"${project}"
argument_list|)
expr_stmt|;
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
block|}
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
DECL|method|GerritInfo (AllProjectsName allProjectsName, AllUsersName allUsersName)
specifier|public
name|GerritInfo
parameter_list|(
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|)
block|{
name|allProjects
operator|=
name|allProjectsName
operator|.
name|get
argument_list|()
expr_stmt|;
name|allUsers
operator|=
name|allUsersName
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

