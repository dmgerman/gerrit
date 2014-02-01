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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Splitter
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
name|GlobalCapability
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
name|ProjectCreationFailedException
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
name|RequiresCapability
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
name|RestApiException
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
name|TopLevelResource
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
name|AccountGroup
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
name|Project
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|server
operator|.
name|project
operator|.
name|CreateProject
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
name|NoSuchProjectException
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
name|ProjectControl
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
name|server
operator|.
name|project
operator|.
name|SuggestParentCandidates
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
name|sshd
operator|.
name|CommandMetaData
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
name|sshd
operator|.
name|SshCommand
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
name|server
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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

begin_comment
comment|/** Create a new project. **/
end_comment

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_PROJECT
argument_list|)
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"create-project"
argument_list|,
name|description
operator|=
literal|"Create a new project and associated Git repository"
argument_list|)
DECL|class|CreateProjectCommand
specifier|final
class|class
name|CreateProjectCommand
extends|extends
name|SshCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--name"
argument_list|,
name|aliases
operator|=
block|{
literal|"-n"
block|}
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|usage
operator|=
literal|"name of project to be created (deprecated option)"
argument_list|)
DECL|method|setProjectNameFromOption (String name)
name|void
name|setProjectNameFromOption
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|projectName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"NAME already supplied"
argument_list|)
throw|;
block|}
else|else
block|{
name|projectName
operator|=
name|name
expr_stmt|;
block|}
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--suggest-parents"
argument_list|,
name|aliases
operator|=
block|{
literal|"-S"
block|}
argument_list|,
name|usage
operator|=
literal|"suggest parent candidates, "
operator|+
literal|"if this option is used all other options and arguments are ignored"
argument_list|)
DECL|field|suggestParent
specifier|private
name|boolean
name|suggestParent
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--owner"
argument_list|,
name|aliases
operator|=
block|{
literal|"-o"
block|}
argument_list|,
name|usage
operator|=
literal|"owner(s) of project"
argument_list|)
DECL|field|ownerIds
specifier|private
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ownerIds
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--parent"
argument_list|,
name|aliases
operator|=
block|{
literal|"-p"
block|}
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|usage
operator|=
literal|"parent project"
argument_list|)
DECL|field|newParent
specifier|private
name|ProjectControl
name|newParent
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--permissions-only"
argument_list|,
name|usage
operator|=
literal|"create project for use only as parent"
argument_list|)
DECL|field|permissionsOnly
specifier|private
name|boolean
name|permissionsOnly
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--description"
argument_list|,
name|aliases
operator|=
block|{
literal|"-d"
block|}
argument_list|,
name|metaVar
operator|=
literal|"DESCRIPTION"
argument_list|,
name|usage
operator|=
literal|"description of project"
argument_list|)
DECL|field|projectDescription
specifier|private
name|String
name|projectDescription
init|=
literal|""
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--submit-type"
argument_list|,
name|aliases
operator|=
block|{
literal|"-t"
block|}
argument_list|,
name|usage
operator|=
literal|"project submit type"
argument_list|)
DECL|field|submitType
specifier|private
name|SubmitType
name|submitType
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--contributor-agreements"
argument_list|,
name|usage
operator|=
literal|"if contributor agreement is required"
argument_list|)
DECL|field|contributorAgreements
specifier|private
name|InheritableBoolean
name|contributorAgreements
init|=
name|InheritableBoolean
operator|.
name|INHERIT
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--signed-off-by"
argument_list|,
name|usage
operator|=
literal|"if signed-off-by is required"
argument_list|)
DECL|field|signedOffBy
specifier|private
name|InheritableBoolean
name|signedOffBy
init|=
name|InheritableBoolean
operator|.
name|INHERIT
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--content-merge"
argument_list|,
name|usage
operator|=
literal|"allow automatic conflict resolving within files"
argument_list|)
DECL|field|contentMerge
specifier|private
name|InheritableBoolean
name|contentMerge
init|=
name|InheritableBoolean
operator|.
name|INHERIT
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--change-id"
argument_list|,
name|usage
operator|=
literal|"if change-id is required"
argument_list|)
DECL|field|requireChangeID
specifier|private
name|InheritableBoolean
name|requireChangeID
init|=
name|InheritableBoolean
operator|.
name|INHERIT
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--use-contributor-agreements"
argument_list|,
name|aliases
operator|=
block|{
literal|"--ca"
block|}
argument_list|,
name|usage
operator|=
literal|"if contributor agreement is required"
argument_list|)
DECL|method|setUseContributorArgreements (boolean on)
name|void
name|setUseContributorArgreements
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|contributorAgreements
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--use-signed-off-by"
argument_list|,
name|aliases
operator|=
block|{
literal|"--so"
block|}
argument_list|,
name|usage
operator|=
literal|"if signed-off-by is required"
argument_list|)
DECL|method|setUseSignedOffBy (boolean on)
name|void
name|setUseSignedOffBy
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|signedOffBy
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--use-content-merge"
argument_list|,
name|usage
operator|=
literal|"allow automatic conflict resolving within files"
argument_list|)
DECL|method|setUseContentMerge (boolean on)
name|void
name|setUseContentMerge
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|contentMerge
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--require-change-id"
argument_list|,
name|aliases
operator|=
block|{
literal|"--id"
block|}
argument_list|,
name|usage
operator|=
literal|"if change-id is required"
argument_list|)
DECL|method|setRequireChangeId (boolean on)
name|void
name|setRequireChangeId
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|requireChangeID
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--branch"
argument_list|,
name|aliases
operator|=
block|{
literal|"-b"
block|}
argument_list|,
name|metaVar
operator|=
literal|"BRANCH"
argument_list|,
name|usage
operator|=
literal|"initial branch name\n"
operator|+
literal|"(default: master)"
argument_list|)
DECL|field|branch
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|branch
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--empty-commit"
argument_list|,
name|usage
operator|=
literal|"to create initial empty commit"
argument_list|)
DECL|field|createEmptyCommit
specifier|private
name|boolean
name|createEmptyCommit
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--max-object-size-limit"
argument_list|,
name|usage
operator|=
literal|"max Git object size for this project"
argument_list|)
DECL|field|maxObjectSizeLimit
specifier|private
name|String
name|maxObjectSizeLimit
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--plugin-config"
argument_list|,
name|usage
operator|=
literal|"plugin configuration parameter with format '<plugin-name>.<parameter-name>=<value>'"
argument_list|)
DECL|field|pluginConfigValues
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|pluginConfigValues
decl_stmt|;
DECL|field|projectName
specifier|private
name|String
name|projectName
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|usage
operator|=
literal|"name of project to be created"
argument_list|)
DECL|method|setProjectNameFromArgument (String name)
name|void
name|setProjectNameFromArgument
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|projectName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"--name already supplied"
argument_list|)
throw|;
block|}
else|else
block|{
name|projectName
operator|=
name|name
expr_stmt|;
block|}
block|}
annotation|@
name|Inject
DECL|field|createProjectFactory
specifier|private
name|Provider
argument_list|<
name|CreateProject
operator|.
name|Factory
argument_list|>
name|createProjectFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|suggestParentCandidatesFactory
specifier|private
name|SuggestParentCandidates
operator|.
name|Factory
name|suggestParentCandidatesFactory
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|UnloggedFailure
block|{
try|try
block|{
if|if
condition|(
operator|!
name|suggestParent
condition|)
block|{
if|if
condition|(
name|projectName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
literal|"fatal: Project name is required."
argument_list|)
throw|;
block|}
name|CreateProject
operator|.
name|Input
name|input
init|=
operator|new
name|CreateProject
operator|.
name|Input
argument_list|()
decl_stmt|;
name|input
operator|.
name|name
operator|=
name|projectName
expr_stmt|;
if|if
condition|(
name|ownerIds
operator|!=
literal|null
condition|)
block|{
name|input
operator|.
name|owners
operator|=
name|Lists
operator|.
name|transform
argument_list|(
name|ownerIds
argument_list|,
operator|new
name|Function
argument_list|<
name|AccountGroup
operator|.
name|UUID
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
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|uuid
operator|.
name|get
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newParent
operator|!=
literal|null
condition|)
block|{
name|input
operator|.
name|parent
operator|=
name|newParent
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|input
operator|.
name|permissionsOnly
operator|=
name|permissionsOnly
expr_stmt|;
name|input
operator|.
name|description
operator|=
name|projectDescription
expr_stmt|;
name|input
operator|.
name|submitType
operator|=
name|submitType
expr_stmt|;
name|input
operator|.
name|useContributorAgreements
operator|=
name|contributorAgreements
expr_stmt|;
name|input
operator|.
name|useSignedOffBy
operator|=
name|signedOffBy
expr_stmt|;
name|input
operator|.
name|useContentMerge
operator|=
name|contentMerge
expr_stmt|;
name|input
operator|.
name|requireChangeId
operator|=
name|requireChangeID
expr_stmt|;
name|input
operator|.
name|branches
operator|=
name|branch
expr_stmt|;
name|input
operator|.
name|createEmptyCommit
operator|=
name|createEmptyCommit
expr_stmt|;
name|input
operator|.
name|maxObjectSizeLimit
operator|=
name|maxObjectSizeLimit
expr_stmt|;
if|if
condition|(
name|pluginConfigValues
operator|!=
literal|null
condition|)
block|{
name|input
operator|.
name|pluginConfigValues
operator|=
name|parsePluginConfigValues
argument_list|(
name|pluginConfigValues
argument_list|)
expr_stmt|;
block|}
name|createProjectFactory
operator|.
name|get
argument_list|()
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|parentCandidates
init|=
name|suggestParentCandidatesFactory
operator|.
name|create
argument_list|()
operator|.
name|getNameKeys
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|parent
range|:
name|parentCandidates
control|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|parent
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RestApiException
decl||
name|ProjectCreationFailedException
decl||
name|IOException
decl||
name|NoSuchProjectException
decl||
name|OrmException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
literal|"fatal: "
operator|+
name|err
operator|.
name|getMessage
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
block|}
annotation|@
name|VisibleForTesting
DECL|method|parsePluginConfigValues ( List<String> pluginConfigValues)
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
name|parsePluginConfigValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|pluginConfigValues
parameter_list|)
throws|throws
name|UnloggedFailure
block|{
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
name|m
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pluginConfigValue
range|:
name|pluginConfigValues
control|)
block|{
name|String
index|[]
name|s
init|=
name|pluginConfigValue
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
name|String
index|[]
name|s2
init|=
name|s
index|[
literal|0
index|]
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
operator|!=
literal|2
operator|||
name|s2
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
literal|"Invalid plugin config value '"
operator|+
name|pluginConfigValue
operator|+
literal|"', expected format '<plugin-name>.<parameter-name>=<value>'"
operator|+
literal|" or '<plugin-name>.<parameter-name>=<value1,value2,...>'"
argument_list|)
throw|;
block|}
name|ConfigValue
name|value
init|=
operator|new
name|ConfigValue
argument_list|()
decl_stmt|;
name|String
name|v
init|=
name|s
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|v
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
name|value
operator|.
name|values
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|","
argument_list|)
operator|.
name|split
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|.
name|value
operator|=
name|v
expr_stmt|;
block|}
name|String
name|pluginName
init|=
name|s2
index|[
literal|0
index|]
decl_stmt|;
name|String
name|paramName
init|=
name|s2
index|[
literal|1
index|]
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
name|l
init|=
name|m
operator|.
name|get
argument_list|(
name|pluginName
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|l
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|pluginName
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|l
operator|.
name|put
argument_list|(
name|paramName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

