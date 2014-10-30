begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|CharMatcher
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
name|Joiner
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
name|Objects
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
name|Strings
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
name|ChangeHooks
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
name|api
operator|.
name|projects
operator|.
name|ProjectInput
operator|.
name|ConfigValue
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
name|common
operator|.
name|InheritableBoolean
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
name|common
operator|.
name|SubmitType
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
name|BadRequestException
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
name|ResourceConflictException
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
name|ResourceNotFoundException
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
name|RestModifyView
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
name|RestView
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
name|Branch
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
name|Project
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
name|RefNames
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
name|CurrentUser
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
name|IdentifiedUser
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
name|AllProjectsNameProvider
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
name|PluginConfig
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
name|PluginConfigFactory
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
name|ProjectConfigEntry
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
name|git
operator|.
name|GitRepositoryManager
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
name|git
operator|.
name|MetaDataUpdate
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
name|git
operator|.
name|ProjectConfig
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
name|git
operator|.
name|TransferConfig
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
name|project
operator|.
name|PutConfig
operator|.
name|Input
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
name|Singleton
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
name|errors
operator|.
name|ConfigInvalidException
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|ObjectId
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Map
operator|.
name|Entry
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PutConfig
specifier|public
class|class
name|PutConfig
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|Input
argument_list|>
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
name|PutConfig
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|useContributorAgreements
specifier|public
name|InheritableBoolean
name|useContributorAgreements
decl_stmt|;
DECL|field|useContentMerge
specifier|public
name|InheritableBoolean
name|useContentMerge
decl_stmt|;
DECL|field|useSignedOffBy
specifier|public
name|InheritableBoolean
name|useSignedOffBy
decl_stmt|;
DECL|field|createNewChangeForAllNotInTarget
specifier|public
name|InheritableBoolean
name|createNewChangeForAllNotInTarget
decl_stmt|;
DECL|field|requireChangeId
specifier|public
name|InheritableBoolean
name|requireChangeId
decl_stmt|;
DECL|field|maxObjectSizeLimit
specifier|public
name|String
name|maxObjectSizeLimit
decl_stmt|;
DECL|field|submitType
specifier|public
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|state
specifier|public
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ProjectState
name|state
decl_stmt|;
DECL|field|pluginConfigValues
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
argument_list|>
name|pluginConfigValues
decl_stmt|;
block|}
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|gitMgr
specifier|private
specifier|final
name|GitRepositoryManager
name|gitMgr
decl_stmt|;
DECL|field|projectStateFactory
specifier|private
specifier|final
name|ProjectState
operator|.
name|Factory
name|projectStateFactory
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|TransferConfig
name|config
decl_stmt|;
DECL|field|pluginConfigEntries
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|ProjectConfigEntry
argument_list|>
name|pluginConfigEntries
decl_stmt|;
DECL|field|cfgFactory
specifier|private
specifier|final
name|PluginConfigFactory
name|cfgFactory
decl_stmt|;
DECL|field|allProjects
specifier|private
specifier|final
name|AllProjectsNameProvider
name|allProjects
decl_stmt|;
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ProjectResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutConfig (MetaDataUpdate.User metaDataUpdateFactory, ProjectCache projectCache, GitRepositoryManager gitMgr, ProjectState.Factory projectStateFactory, TransferConfig config, DynamicMap<ProjectConfigEntry> pluginConfigEntries, PluginConfigFactory cfgFactory, AllProjectsNameProvider allProjects, DynamicMap<RestView<ProjectResource>> views, ChangeHooks hooks, Provider<CurrentUser> currentUser)
name|PutConfig
parameter_list|(
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|GitRepositoryManager
name|gitMgr
parameter_list|,
name|ProjectState
operator|.
name|Factory
name|projectStateFactory
parameter_list|,
name|TransferConfig
name|config
parameter_list|,
name|DynamicMap
argument_list|<
name|ProjectConfigEntry
argument_list|>
name|pluginConfigEntries
parameter_list|,
name|PluginConfigFactory
name|cfgFactory
parameter_list|,
name|AllProjectsNameProvider
name|allProjects
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ProjectResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
parameter_list|)
block|{
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|gitMgr
operator|=
name|gitMgr
expr_stmt|;
name|this
operator|.
name|projectStateFactory
operator|=
name|projectStateFactory
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|pluginConfigEntries
operator|=
name|pluginConfigEntries
expr_stmt|;
name|this
operator|.
name|cfgFactory
operator|=
name|cfgFactory
expr_stmt|;
name|this
operator|.
name|allProjects
operator|=
name|allProjects
expr_stmt|;
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc, Input input)
specifier|public
name|ConfigInfo
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
block|{
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|rsrc
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|isOwner
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"config is required"
argument_list|)
throw|;
block|}
specifier|final
name|MetaDataUpdate
name|md
decl_stmt|;
try|try
block|{
name|md
operator|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|notFound
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|ProjectConfig
name|projectConfig
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|Project
name|p
init|=
name|projectConfig
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDescription
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|description
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|input
operator|.
name|useContributorAgreements
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setUseContributorAgreements
argument_list|(
name|input
operator|.
name|useContributorAgreements
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|useContentMerge
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setUseContentMerge
argument_list|(
name|input
operator|.
name|useContentMerge
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|useSignedOffBy
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setUseSignedOffBy
argument_list|(
name|input
operator|.
name|useSignedOffBy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|createNewChangeForAllNotInTarget
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setCreateNewChangeForAllNotInTarget
argument_list|(
name|input
operator|.
name|createNewChangeForAllNotInTarget
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|requireChangeId
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setRequireChangeID
argument_list|(
name|input
operator|.
name|requireChangeId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|maxObjectSizeLimit
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setMaxObjectSizeLimit
argument_list|(
name|input
operator|.
name|maxObjectSizeLimit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|submitType
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setSubmitType
argument_list|(
name|input
operator|.
name|submitType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|state
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setState
argument_list|(
name|input
operator|.
name|state
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|pluginConfigValues
operator|!=
literal|null
condition|)
block|{
name|setPluginConfigValues
argument_list|(
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|projectConfig
argument_list|,
name|input
operator|.
name|pluginConfigValues
argument_list|)
expr_stmt|;
block|}
name|md
operator|.
name|setMessage
argument_list|(
literal|"Modified project settings\n"
argument_list|)
expr_stmt|;
try|try
block|{
name|ObjectId
name|baseRev
init|=
name|projectConfig
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|ObjectId
name|commitRev
init|=
name|projectConfig
operator|.
name|commit
argument_list|(
name|md
argument_list|)
decl_stmt|;
comment|// Only fire hook if project was actually changed.
if|if
condition|(
operator|!
name|Objects
operator|.
name|equal
argument_list|(
name|baseRev
argument_list|,
name|commitRev
argument_list|)
condition|)
block|{
name|IdentifiedUser
name|user
init|=
operator|(
name|IdentifiedUser
operator|)
name|currentUser
operator|.
name|get
argument_list|()
decl_stmt|;
name|hooks
operator|.
name|doRefUpdatedHook
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
argument_list|,
name|baseRev
argument_list|,
name|commitRev
argument_list|,
name|user
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|projectCache
operator|.
name|evict
argument_list|(
name|projectConfig
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|gitMgr
operator|.
name|setProjectDescription
argument_list|(
name|projectName
argument_list|,
name|p
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ConfigInvalidException
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot update "
operator|+
name|projectName
operator|+
literal|": "
operator|+
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot update "
operator|+
name|projectName
argument_list|)
throw|;
block|}
block|}
name|ProjectState
name|state
init|=
name|projectStateFactory
operator|.
name|create
argument_list|(
name|projectConfig
argument_list|)
decl_stmt|;
return|return
operator|new
name|ConfigInfo
argument_list|(
name|state
operator|.
name|controlFor
argument_list|(
name|currentUser
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|config
argument_list|,
name|pluginConfigEntries
argument_list|,
name|cfgFactory
argument_list|,
name|allProjects
argument_list|,
name|views
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot read project "
operator|+
name|projectName
argument_list|,
name|err
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot update project "
operator|+
name|projectName
argument_list|,
name|err
argument_list|)
throw|;
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setPluginConfigValues (ProjectState projectState, ProjectConfig projectConfig, Map<String, Map<String, ConfigValue>> pluginConfigValues)
specifier|private
name|void
name|setPluginConfigValues
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|ProjectConfig
name|projectConfig
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
argument_list|>
name|pluginConfigValues
parameter_list|)
throws|throws
name|BadRequestException
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
argument_list|>
name|e
range|:
name|pluginConfigValues
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|pluginName
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|PluginConfig
name|cfg
init|=
name|projectConfig
operator|.
name|getPluginConfig
argument_list|(
name|pluginName
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
name|v
range|:
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ProjectConfigEntry
name|projectConfigEntry
init|=
name|pluginConfigEntries
operator|.
name|get
argument_list|(
name|pluginName
argument_list|,
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectConfigEntry
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isValidParameterName
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Parameter name '%s' must match '^[a-zA-Z0-9]+[a-zA-Z0-9-]*$'"
argument_list|,
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|String
name|oldValue
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|v
operator|.
name|getValue
argument_list|()
operator|.
name|value
decl_stmt|;
if|if
condition|(
name|projectConfigEntry
operator|.
name|getType
argument_list|()
operator|==
name|ProjectConfigEntry
operator|.
name|Type
operator|.
name|ARRAY
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|l
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|cfg
operator|.
name|getStringList
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|oldValue
operator|=
name|Joiner
operator|.
name|on
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|join
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|value
operator|=
name|Joiner
operator|.
name|on
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|join
argument_list|(
name|v
operator|.
name|getValue
argument_list|()
operator|.
name|values
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|value
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|value
operator|.
name|equals
argument_list|(
name|oldValue
argument_list|)
condition|)
block|{
name|validateProjectConfigEntryIsEditable
argument_list|(
name|projectConfigEntry
argument_list|,
name|projectState
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|pluginName
argument_list|)
expr_stmt|;
try|try
block|{
switch|switch
condition|(
name|projectConfigEntry
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|BOOLEAN
case|:
name|boolean
name|newBooleanValue
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|,
name|newBooleanValue
argument_list|)
expr_stmt|;
break|break;
case|case
name|INT
case|:
name|int
name|newIntValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|,
name|newIntValue
argument_list|)
expr_stmt|;
break|break;
case|case
name|LONG
case|:
name|long
name|newLongValue
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setLong
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|,
name|newLongValue
argument_list|)
expr_stmt|;
break|break;
case|case
name|LIST
case|:
if|if
condition|(
operator|!
name|projectConfigEntry
operator|.
name|getPermittedValues
argument_list|()
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The value '%s' is not permitted for parameter '%s' of plugin '"
operator|+
name|pluginName
operator|+
literal|"'"
argument_list|,
name|value
argument_list|,
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
case|case
name|STRING
case|:
name|cfg
operator|.
name|setString
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|ARRAY
case|:
name|cfg
operator|.
name|setStringList
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|,
name|v
operator|.
name|getValue
argument_list|()
operator|.
name|values
argument_list|)
expr_stmt|;
break|break;
default|default:
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The type '%s' of parameter '%s' is not supported."
argument_list|,
name|projectConfigEntry
operator|.
name|getType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The value '%s' of config parameter '%s' of plugin '%s' is invalid: %s"
argument_list|,
name|v
operator|.
name|getValue
argument_list|()
argument_list|,
name|v
operator|.
name|getKey
argument_list|()
argument_list|,
name|pluginName
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|validateProjectConfigEntryIsEditable
argument_list|(
name|projectConfigEntry
argument_list|,
name|projectState
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|pluginName
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|unset
argument_list|(
name|v
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The config parameter '%s' of plugin '%s' does not exist."
argument_list|,
name|v
operator|.
name|getKey
argument_list|()
argument_list|,
name|pluginName
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
DECL|method|validateProjectConfigEntryIsEditable ( ProjectConfigEntry projectConfigEntry, ProjectState projectState, String parameterName, String pluginName)
specifier|private
specifier|static
name|void
name|validateProjectConfigEntryIsEditable
parameter_list|(
name|ProjectConfigEntry
name|projectConfigEntry
parameter_list|,
name|ProjectState
name|projectState
parameter_list|,
name|String
name|parameterName
parameter_list|,
name|String
name|pluginName
parameter_list|)
throws|throws
name|BadRequestException
block|{
if|if
condition|(
operator|!
name|projectConfigEntry
operator|.
name|isEditable
argument_list|(
name|projectState
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Not allowed to set parameter '%s' of plugin '%s' on project '%s'."
argument_list|,
name|parameterName
argument_list|,
name|pluginName
argument_list|,
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|isValidParameterName (String name)
specifier|private
specifier|static
name|boolean
name|isValidParameterName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|CharMatcher
operator|.
name|JAVA_LETTER_OR_DIGIT
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'-'
argument_list|)
argument_list|)
operator|.
name|matchesAllOf
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
name|name
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

