begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|client
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
name|client
operator|.
name|ProjectState
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
name|client
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
name|project
operator|.
name|ProjectCache
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

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"set-project"
argument_list|,
name|description
operator|=
literal|"Change a project's settings"
argument_list|)
DECL|class|SetProjectCommand
specifier|final
class|class
name|SetProjectCommand
extends|extends
name|SshCommand
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
name|SetProjectCommand
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|usage
operator|=
literal|"name of the project"
argument_list|)
DECL|field|projectControl
specifier|private
name|ProjectControl
name|projectControl
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
literal|"project submit type\n"
operator|+
literal|"(default: MERGE_IF_NECESSARY)"
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
DECL|method|setUseContributorArgreements (@uppressWarningsR) boolean on)
name|void
name|setUseContributorArgreements
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
literal|"--no-contributor-agreements"
argument_list|,
name|aliases
operator|=
block|{
literal|"--nca"
block|}
argument_list|,
name|usage
operator|=
literal|"if contributor agreement is not required"
argument_list|)
DECL|method|setNoContributorArgreements (@uppressWarningsR) boolean on)
name|void
name|setNoContributorArgreements
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|boolean
name|on
parameter_list|)
block|{
name|contributorAgreements
operator|=
name|InheritableBoolean
operator|.
name|FALSE
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
DECL|method|setUseSignedOffBy (@uppressWarningsR) boolean on)
name|void
name|setUseSignedOffBy
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
literal|"--no-signed-off-by"
argument_list|,
name|aliases
operator|=
block|{
literal|"--nso"
block|}
argument_list|,
name|usage
operator|=
literal|"if signed-off-by is not required"
argument_list|)
DECL|method|setNoSignedOffBy (@uppressWarningsR) boolean on)
name|void
name|setNoSignedOffBy
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|boolean
name|on
parameter_list|)
block|{
name|signedOffBy
operator|=
name|InheritableBoolean
operator|.
name|FALSE
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
DECL|method|setUseContentMerge (@uppressWarningsR) boolean on)
name|void
name|setUseContentMerge
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
literal|"--no-content-merge"
argument_list|,
name|usage
operator|=
literal|"don't allow automatic conflict resolving within files"
argument_list|)
DECL|method|setNoContentMerge (@uppressWarningsR) boolean on)
name|void
name|setNoContentMerge
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|boolean
name|on
parameter_list|)
block|{
name|contentMerge
operator|=
name|InheritableBoolean
operator|.
name|FALSE
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
DECL|method|setRequireChangeId (@uppressWarningsR) boolean on)
name|void
name|setRequireChangeId
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
literal|"--no-change-id"
argument_list|,
name|aliases
operator|=
block|{
literal|"--nid"
block|}
argument_list|,
name|usage
operator|=
literal|"if change-id is not required"
argument_list|)
DECL|method|setNoChangeId (@uppressWarningsR) boolean on)
name|void
name|setNoChangeId
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|boolean
name|on
parameter_list|)
block|{
name|requireChangeID
operator|=
name|InheritableBoolean
operator|.
name|FALSE
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--project-state"
argument_list|,
name|aliases
operator|=
block|{
literal|"--ps"
block|}
argument_list|,
name|usage
operator|=
literal|"project's visibility state"
argument_list|)
DECL|field|state
specifier|private
name|ProjectState
name|state
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
name|Inject
DECL|field|metaDataUpdateFactory
specifier|private
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|projectCache
specifier|private
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|Failure
block|{
name|Project
name|ctlProject
init|=
name|projectControl
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|nameKey
init|=
name|ctlProject
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|ctlProject
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|StringBuilder
name|err
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
init|(
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|nameKey
argument_list|)
init|)
block|{
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|Project
name|project
init|=
name|config
operator|.
name|getProject
argument_list|()
decl_stmt|;
if|if
condition|(
name|requireChangeID
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setRequireChangeID
argument_list|(
name|requireChangeID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|submitType
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setSubmitType
argument_list|(
name|submitType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentMerge
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setUseContentMerge
argument_list|(
name|contentMerge
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contributorAgreements
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setUseContributorAgreements
argument_list|(
name|contributorAgreements
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signedOffBy
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setUseSignedOffBy
argument_list|(
name|signedOffBy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectDescription
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setDescription
argument_list|(
name|projectDescription
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setState
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maxObjectSizeLimit
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setMaxObjectSizeLimit
argument_list|(
name|maxObjectSizeLimit
argument_list|)
expr_stmt|;
block|}
name|md
operator|.
name|setMessage
argument_list|(
literal|"Project settings updated"
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|notFound
parameter_list|)
block|{
name|err
operator|.
name|append
argument_list|(
literal|"Project "
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|" not found\n"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
specifier|final
name|String
name|msg
init|=
literal|"Cannot update project "
operator|+
name|name
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|err
operator|.
name|append
argument_list|(
literal|"error: "
argument_list|)
operator|.
name|append
argument_list|(
name|msg
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|projectCache
operator|.
name|evict
argument_list|(
name|ctlProject
argument_list|)
expr_stmt|;
if|if
condition|(
name|err
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
while|while
condition|(
name|err
operator|.
name|charAt
argument_list|(
name|err
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'\n'
condition|)
block|{
name|err
operator|.
name|setLength
argument_list|(
name|err
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
throw|throw
name|die
argument_list|(
name|err
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

