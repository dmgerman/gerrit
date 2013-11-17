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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|Maps
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
name|LabelType
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
name|LabelValue
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
name|ReviewResult
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
name|ReviewResult
operator|.
name|Error
operator|.
name|Type
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
name|GerritApi
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
name|changes
operator|.
name|AbandonInput
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
name|changes
operator|.
name|RestoreInput
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
name|changes
operator|.
name|ReviewInput
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
name|AuthException
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|PatchSet
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
name|RevId
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
name|server
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
name|server
operator|.
name|changedetail
operator|.
name|PublishDraft
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
name|AllProjectsName
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
name|InvalidChangeOperationException
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
name|NoSuchChangeException
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
name|gerrit
operator|.
name|util
operator|.
name|cli
operator|.
name|CmdLineParser
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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
name|ArrayList
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
name|Set
import|;
end_import

begin_class
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"review"
argument_list|,
name|description
operator|=
literal|"Verify, approve and/or submit one or more patch sets"
argument_list|)
DECL|class|ReviewCommand
specifier|public
class|class
name|ReviewCommand
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
name|ReviewCommand
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|newCmdLineParser (Object options)
specifier|protected
specifier|final
name|CmdLineParser
name|newCmdLineParser
parameter_list|(
name|Object
name|options
parameter_list|)
block|{
specifier|final
name|CmdLineParser
name|parser
init|=
name|super
operator|.
name|newCmdLineParser
argument_list|(
name|options
argument_list|)
decl_stmt|;
for|for
control|(
name|ApproveOption
name|c
range|:
name|optionList
control|)
block|{
name|parser
operator|.
name|addOption
argument_list|(
name|c
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|parser
return|;
block|}
DECL|field|patchSets
specifier|private
specifier|final
name|Set
argument_list|<
name|PatchSet
argument_list|>
name|patchSets
init|=
operator|new
name|HashSet
argument_list|<
name|PatchSet
argument_list|>
argument_list|()
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
name|multiValued
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"{COMMIT | CHANGE,PATCHSET}"
argument_list|,
name|usage
operator|=
literal|"list of commits or patch sets to review"
argument_list|)
DECL|method|addPatchSetId (final String token)
name|void
name|addPatchSetId
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
try|try
block|{
name|patchSets
operator|.
name|add
argument_list|(
name|parsePatchSet
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnloggedFailure
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
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
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"database error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--project"
argument_list|,
name|aliases
operator|=
literal|"-p"
argument_list|,
name|usage
operator|=
literal|"project containing the specified patch set(s)"
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
literal|"--message"
argument_list|,
name|aliases
operator|=
literal|"-m"
argument_list|,
name|usage
operator|=
literal|"cover message to publish on change(s)"
argument_list|,
name|metaVar
operator|=
literal|"MESSAGE"
argument_list|)
DECL|field|changeComment
specifier|private
name|String
name|changeComment
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--abandon"
argument_list|,
name|usage
operator|=
literal|"abandon the specified change(s)"
argument_list|)
DECL|field|abandonChange
specifier|private
name|boolean
name|abandonChange
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--restore"
argument_list|,
name|usage
operator|=
literal|"restore the specified abandoned change(s)"
argument_list|)
DECL|field|restoreChange
specifier|private
name|boolean
name|restoreChange
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--submit"
argument_list|,
name|aliases
operator|=
literal|"-s"
argument_list|,
name|usage
operator|=
literal|"submit the specified patch set(s)"
argument_list|)
DECL|field|submitChange
specifier|private
name|boolean
name|submitChange
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--publish"
argument_list|,
name|usage
operator|=
literal|"publish the specified draft patch set(s)"
argument_list|)
DECL|field|publishPatchSet
specifier|private
name|boolean
name|publishPatchSet
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--delete"
argument_list|,
name|usage
operator|=
literal|"delete the specified draft patch set(s)"
argument_list|)
DECL|field|deleteDraftPatchSet
specifier|private
name|boolean
name|deleteDraftPatchSet
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--label"
argument_list|,
name|aliases
operator|=
literal|"-l"
argument_list|,
name|usage
operator|=
literal|"custom label(s) to assign"
argument_list|,
name|metaVar
operator|=
literal|"LABEL=VALUE"
argument_list|)
DECL|method|addLabel (final String token)
name|void
name|addLabel
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|'='
argument_list|)
operator|.
name|split
argument_list|(
name|token
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid custom label "
operator|+
name|token
argument_list|)
throw|;
block|}
name|short
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|Short
operator|.
name|parseShort
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid custom label value "
operator|+
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
throw|;
block|}
name|customLabels
operator|.
name|put
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
annotation|@
name|Inject
DECL|field|projectControlFactory
specifier|private
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|allProjects
specifier|private
name|AllProjectsName
name|allProjects
decl_stmt|;
annotation|@
name|Inject
DECL|field|gApi
specifier|private
name|Provider
argument_list|<
name|GerritApi
argument_list|>
name|gApi
decl_stmt|;
annotation|@
name|Inject
DECL|field|publishDraftFactory
specifier|private
name|PublishDraft
operator|.
name|Factory
name|publishDraftFactory
decl_stmt|;
DECL|field|optionList
specifier|private
name|List
argument_list|<
name|ApproveOption
argument_list|>
name|optionList
decl_stmt|;
DECL|field|customLabels
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|customLabels
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
if|if
condition|(
name|abandonChange
condition|)
block|{
if|if
condition|(
name|restoreChange
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"abandon and restore actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|submitChange
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"abandon and submit actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|publishPatchSet
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"abandon and publish actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|deleteDraftPatchSet
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"abandon and delete actions are mutually exclusive"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|publishPatchSet
condition|)
block|{
if|if
condition|(
name|restoreChange
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"publish and restore actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|submitChange
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"publish and submit actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|deleteDraftPatchSet
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"publish and delete actions are mutually exclusive"
argument_list|)
throw|;
block|}
block|}
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
for|for
control|(
specifier|final
name|PatchSet
name|patchSet
range|:
name|patchSets
control|)
block|{
try|try
block|{
name|approveOne
argument_list|(
name|patchSet
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnloggedFailure
name|e
parameter_list|)
block|{
name|ok
operator|=
literal|false
expr_stmt|;
name|writeError
argument_list|(
literal|"error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
name|ok
operator|=
literal|false
expr_stmt|;
name|writeError
argument_list|(
literal|"no such change "
operator|+
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ok
operator|=
literal|false
expr_stmt|;
name|writeError
argument_list|(
literal|"fatal: internal server error while approving "
operator|+
name|patchSet
operator|.
name|getId
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|log
operator|.
name|error
argument_list|(
literal|"internal error while approving "
operator|+
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|ok
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
literal|"one or more approvals failed;"
operator|+
literal|" review output above"
argument_list|)
throw|;
block|}
block|}
DECL|method|applyReview (PatchSet patchSet, final ReviewInput review)
specifier|private
name|void
name|applyReview
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|,
specifier|final
name|ReviewInput
name|review
parameter_list|)
throws|throws
name|Exception
block|{
name|gApi
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|review
argument_list|(
name|review
argument_list|)
expr_stmt|;
block|}
DECL|method|approveOne (final PatchSet patchSet)
specifier|private
name|void
name|approveOne
parameter_list|(
specifier|final
name|PatchSet
name|patchSet
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|changeComment
operator|==
literal|null
condition|)
block|{
name|changeComment
operator|=
literal|""
expr_stmt|;
block|}
name|ReviewInput
name|review
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|review
operator|.
name|message
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|changeComment
argument_list|)
expr_stmt|;
name|review
operator|.
name|labels
operator|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
expr_stmt|;
name|review
operator|.
name|drafts
operator|=
name|ReviewInput
operator|.
name|DraftHandling
operator|.
name|PUBLISH
expr_stmt|;
name|review
operator|.
name|strictLabels
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|ApproveOption
name|ao
range|:
name|optionList
control|)
block|{
name|Short
name|v
init|=
name|ao
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|review
operator|.
name|labels
operator|.
name|put
argument_list|(
name|ao
operator|.
name|getLabelName
argument_list|()
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
name|review
operator|.
name|labels
operator|.
name|putAll
argument_list|(
name|customLabels
argument_list|)
expr_stmt|;
comment|// If review labels are being applied, the comment will be included
comment|// on the review note. We don't need to add it again on the abandon
comment|// or restore comment.
if|if
condition|(
operator|!
name|review
operator|.
name|labels
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|(
name|abandonChange
operator|||
name|restoreChange
operator|)
condition|)
block|{
name|changeComment
operator|=
literal|null
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|abandonChange
condition|)
block|{
name|AbandonInput
name|input
init|=
operator|new
name|AbandonInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|message
operator|=
name|changeComment
expr_stmt|;
name|applyReview
argument_list|(
name|patchSet
argument_list|,
name|review
argument_list|)
expr_stmt|;
try|try
block|{
name|gApi
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|abandon
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|writeError
argument_list|(
literal|"error: "
operator|+
name|parseError
argument_list|(
name|Type
operator|.
name|ABANDON_NOT_PERMITTED
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|writeError
argument_list|(
literal|"error: "
operator|+
name|parseError
argument_list|(
name|Type
operator|.
name|CHANGE_IS_CLOSED
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|restoreChange
condition|)
block|{
name|RestoreInput
name|input
init|=
operator|new
name|RestoreInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|message
operator|=
name|changeComment
expr_stmt|;
try|try
block|{
name|gApi
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|restore
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|applyReview
argument_list|(
name|patchSet
argument_list|,
name|review
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|writeError
argument_list|(
literal|"error: "
operator|+
name|parseError
argument_list|(
name|Type
operator|.
name|RESTORE_NOT_PERMITTED
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|writeError
argument_list|(
literal|"error: "
operator|+
name|parseError
argument_list|(
name|Type
operator|.
name|CHANGE_NOT_ABANDONED
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|applyReview
argument_list|(
name|patchSet
argument_list|,
name|review
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|submitChange
condition|)
block|{
name|gApi
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|publishPatchSet
condition|)
block|{
specifier|final
name|ReviewResult
name|result
init|=
name|publishDraftFactory
operator|.
name|create
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
decl_stmt|;
name|handleReviewResultErrors
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|deleteDraftPatchSet
condition|)
block|{
name|gApi
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidChangeOperationException
name|e
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|BadRequestException
name|e
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RestApiException
name|e
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|handleReviewResultErrors (final ReviewResult result)
specifier|private
name|void
name|handleReviewResultErrors
parameter_list|(
specifier|final
name|ReviewResult
name|result
parameter_list|)
block|{
for|for
control|(
name|ReviewResult
operator|.
name|Error
name|resultError
range|:
name|result
operator|.
name|getErrors
argument_list|()
control|)
block|{
name|String
name|errMsg
init|=
literal|"error: (change "
operator|+
name|result
operator|.
name|getChangeId
argument_list|()
operator|+
literal|") "
decl_stmt|;
name|errMsg
operator|+=
name|parseError
argument_list|(
name|resultError
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|resultError
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|errMsg
operator|+=
literal|": "
operator|+
name|resultError
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
name|writeError
argument_list|(
name|errMsg
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|parseError (Type type)
specifier|private
name|String
name|parseError
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|ABANDON_NOT_PERMITTED
case|:
return|return
literal|"not permitted to abandon change"
return|;
case|case
name|RESTORE_NOT_PERMITTED
case|:
return|return
literal|"not permitted to restore change"
return|;
case|case
name|SUBMIT_NOT_PERMITTED
case|:
return|return
literal|"not permitted to submit change"
return|;
case|case
name|SUBMIT_NOT_READY
case|:
return|return
literal|"approvals or dependencies lacking"
return|;
case|case
name|CHANGE_IS_CLOSED
case|:
return|return
literal|"change is closed"
return|;
case|case
name|CHANGE_NOT_ABANDONED
case|:
return|return
literal|"change is not abandoned"
return|;
case|case
name|PUBLISH_NOT_PERMITTED
case|:
return|return
literal|"not permitted to publish change"
return|;
case|case
name|DELETE_NOT_PERMITTED
case|:
return|return
literal|"not permitted to delete change/patch set"
return|;
case|case
name|RULE_ERROR
case|:
return|return
literal|"rule error"
return|;
case|case
name|NOT_A_DRAFT
case|:
return|return
literal|"change/patch set is not a draft"
return|;
case|case
name|GIT_ERROR
case|:
return|return
literal|"error writing change to git repository"
return|;
case|case
name|DEST_BRANCH_NOT_FOUND
case|:
return|return
literal|"destination branch not found"
return|;
default|default:
return|return
literal|"failure in review"
return|;
block|}
block|}
DECL|method|parsePatchSet (final String patchIdentity)
specifier|private
name|PatchSet
name|parsePatchSet
parameter_list|(
specifier|final
name|String
name|patchIdentity
parameter_list|)
throws|throws
name|UnloggedFailure
throws|,
name|OrmException
block|{
comment|// By commit?
comment|//
if|if
condition|(
name|patchIdentity
operator|.
name|matches
argument_list|(
literal|"^([0-9a-fA-F]{4,"
operator|+
name|RevId
operator|.
name|LEN
operator|+
literal|"})$"
argument_list|)
condition|)
block|{
specifier|final
name|RevId
name|id
init|=
operator|new
name|RevId
argument_list|(
name|patchIdentity
argument_list|)
decl_stmt|;
specifier|final
name|ResultSet
argument_list|<
name|PatchSet
argument_list|>
name|patches
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|isComplete
argument_list|()
condition|)
block|{
name|patches
operator|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byRevision
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|patches
operator|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byRevisionRange
argument_list|(
name|id
argument_list|,
name|id
operator|.
name|max
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Set
argument_list|<
name|PatchSet
argument_list|>
name|matches
init|=
operator|new
name|HashSet
argument_list|<
name|PatchSet
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|PatchSet
name|ps
range|:
name|patches
control|)
block|{
specifier|final
name|Change
name|change
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|inProject
argument_list|(
name|change
argument_list|)
condition|)
block|{
name|matches
operator|.
name|add
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
block|}
switch|switch
condition|(
name|matches
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
name|matches
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
case|case
literal|0
case|:
throw|throw
name|error
argument_list|(
literal|"\""
operator|+
name|patchIdentity
operator|+
literal|"\" no such patch set"
argument_list|)
throw|;
default|default:
throw|throw
name|error
argument_list|(
literal|"\""
operator|+
name|patchIdentity
operator|+
literal|"\" matches multiple patch sets"
argument_list|)
throw|;
block|}
block|}
comment|// By older style change,patchset?
comment|//
if|if
condition|(
name|patchIdentity
operator|.
name|matches
argument_list|(
literal|"^[1-9][0-9]*,[1-9][0-9]*$"
argument_list|)
condition|)
block|{
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
try|try
block|{
name|patchSetId
operator|=
name|PatchSet
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|patchIdentity
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
literal|"\""
operator|+
name|patchIdentity
operator|+
literal|"\" is not a valid patch set"
argument_list|)
throw|;
block|}
specifier|final
name|PatchSet
name|patchSet
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|patchSet
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"\""
operator|+
name|patchIdentity
operator|+
literal|"\" no such patch set"
argument_list|)
throw|;
block|}
if|if
condition|(
name|projectControl
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Change
name|change
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|patchSetId
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|inProject
argument_list|(
name|change
argument_list|)
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"change "
operator|+
name|change
operator|.
name|getId
argument_list|()
operator|+
literal|" not in project "
operator|+
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|patchSet
return|;
block|}
throw|throw
name|error
argument_list|(
literal|"\""
operator|+
name|patchIdentity
operator|+
literal|"\" is not a valid patch set"
argument_list|)
throw|;
block|}
DECL|method|inProject (final Change change)
specifier|private
name|boolean
name|inProject
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|)
block|{
if|if
condition|(
name|projectControl
operator|==
literal|null
condition|)
block|{
comment|// No --project option, so they want every project.
return|return
literal|true
return|;
block|}
return|return
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|parseCommandLine ()
specifier|protected
name|void
name|parseCommandLine
parameter_list|()
throws|throws
name|UnloggedFailure
block|{
name|optionList
operator|=
operator|new
name|ArrayList
argument_list|<
name|ApproveOption
argument_list|>
argument_list|()
expr_stmt|;
name|customLabels
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
name|ProjectControl
name|allProjectsControl
decl_stmt|;
try|try
block|{
name|allProjectsControl
operator|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|allProjects
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|"missing "
operator|+
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
for|for
control|(
name|LabelType
name|type
range|:
name|allProjectsControl
operator|.
name|getLabelTypes
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
control|)
block|{
name|String
name|usage
decl_stmt|;
name|usage
operator|=
literal|"score for "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|"\n"
expr_stmt|;
for|for
control|(
name|LabelValue
name|v
range|:
name|type
operator|.
name|getValues
argument_list|()
control|)
block|{
name|usage
operator|+=
name|v
operator|.
name|format
argument_list|()
operator|+
literal|"\n"
expr_stmt|;
block|}
specifier|final
name|String
name|name
init|=
literal|"--"
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|optionList
operator|.
name|add
argument_list|(
operator|new
name|ApproveOption
argument_list|(
name|name
argument_list|,
name|usage
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|parseCommandLine
argument_list|()
expr_stmt|;
block|}
DECL|method|writeError (final String msg)
specifier|private
name|void
name|writeError
parameter_list|(
specifier|final
name|String
name|msg
parameter_list|)
block|{
try|try
block|{
name|err
operator|.
name|write
argument_list|(
name|msg
operator|.
name|getBytes
argument_list|(
name|ENC
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{     }
block|}
DECL|method|error (final String msg)
specifier|private
specifier|static
name|UnloggedFailure
name|error
parameter_list|(
specifier|final
name|String
name|msg
parameter_list|)
block|{
return|return
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
name|msg
argument_list|)
return|;
block|}
block|}
end_class

end_unit

