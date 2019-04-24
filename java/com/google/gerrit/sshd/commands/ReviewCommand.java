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
import|import static
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
name|Localizable
operator|.
name|localizable
import|;
end_import

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
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|flogger
operator|.
name|FluentLogger
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
name|CharStreams
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
name|exceptions
operator|.
name|StorageException
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
name|ChangeApi
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
name|MoveInput
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
name|NotifyHandling
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
name|api
operator|.
name|changes
operator|.
name|RevisionApi
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
name|json
operator|.
name|OutputFormat
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
name|server
operator|.
name|util
operator|.
name|LabelVote
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
name|gerrit
operator|.
name|util
operator|.
name|cli
operator|.
name|OptionUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonSyntaxException
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|AnnotatedElement
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
name|Optional
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
name|TreeMap
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
name|CmdLineException
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
name|kohsuke
operator|.
name|args4j
operator|.
name|OptionDef
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
name|spi
operator|.
name|FieldSetter
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
name|spi
operator|.
name|OneArgumentOptionHandler
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
name|spi
operator|.
name|Setter
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
literal|"Apply reviews to one or more patch sets"
argument_list|)
DECL|class|ReviewCommand
specifier|public
class|class
name|ReviewCommand
extends|extends
name|SshCommand
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
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
name|optionMap
operator|.
name|forEach
argument_list|(
parameter_list|(
name|o
parameter_list|,
name|s
parameter_list|)
lambda|->
name|parser
operator|.
name|addOption
argument_list|(
name|s
argument_list|,
name|o
argument_list|)
argument_list|)
expr_stmt|;
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
argument_list|<>
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
DECL|method|addPatchSetId (String token)
name|void
name|addPatchSetId
parameter_list|(
name|String
name|token
parameter_list|)
block|{
try|try
block|{
name|PatchSet
name|ps
init|=
name|psParser
operator|.
name|parsePatchSet
argument_list|(
name|token
argument_list|,
name|projectState
argument_list|,
name|branch
argument_list|)
decl_stmt|;
name|patchSets
operator|.
name|add
argument_list|(
name|ps
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
name|StorageException
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
DECL|field|projectState
specifier|private
name|ProjectState
name|projectState
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--branch"
argument_list|,
name|aliases
operator|=
literal|"-b"
argument_list|,
name|usage
operator|=
literal|"branch containing the specified patch set(s)"
argument_list|)
DECL|field|branch
specifier|private
name|String
name|branch
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
literal|"--notify"
argument_list|,
name|aliases
operator|=
literal|"-n"
argument_list|,
name|usage
operator|=
literal|"Who to send email notifications to after the review is stored."
argument_list|,
name|metaVar
operator|=
literal|"NOTIFYHANDLING"
argument_list|)
DECL|field|notify
specifier|private
name|NotifyHandling
name|notify
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
literal|"--rebase"
argument_list|,
name|usage
operator|=
literal|"rebase the specified change(s)"
argument_list|)
DECL|field|rebaseChange
specifier|private
name|boolean
name|rebaseChange
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--move"
argument_list|,
name|usage
operator|=
literal|"move the specified change(s)"
argument_list|,
name|metaVar
operator|=
literal|"BRANCH"
argument_list|)
DECL|field|moveToBranch
specifier|private
name|String
name|moveToBranch
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
literal|"--json"
argument_list|,
name|aliases
operator|=
literal|"-j"
argument_list|,
name|usage
operator|=
literal|"read review input json from stdin"
argument_list|)
DECL|field|json
specifier|private
name|boolean
name|json
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--tag"
argument_list|,
name|aliases
operator|=
literal|"-t"
argument_list|,
name|usage
operator|=
literal|"applies a tag to the given review"
argument_list|,
name|metaVar
operator|=
literal|"TAG"
argument_list|)
DECL|field|changeTag
specifier|private
name|String
name|changeTag
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
DECL|method|addLabel (String token)
name|void
name|addLabel
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|LabelVote
name|v
init|=
name|LabelVote
operator|.
name|parseWithEquals
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|LabelType
operator|.
name|checkName
argument_list|(
name|v
operator|.
name|label
argument_list|()
argument_list|)
expr_stmt|;
comment|// Disallow SUBM.
name|customLabels
operator|.
name|put
argument_list|(
name|v
operator|.
name|label
argument_list|()
argument_list|,
name|v
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|projectCache
annotation|@
name|Inject
specifier|private
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|allProjects
annotation|@
name|Inject
specifier|private
name|AllProjectsName
name|allProjects
decl_stmt|;
DECL|field|gApi
annotation|@
name|Inject
specifier|private
name|GerritApi
name|gApi
decl_stmt|;
DECL|field|psParser
annotation|@
name|Inject
specifier|private
name|PatchSetParser
name|psParser
decl_stmt|;
DECL|field|optionMap
specifier|private
name|Map
argument_list|<
name|Option
argument_list|,
name|LabelSetter
argument_list|>
name|optionMap
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
name|die
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
name|die
argument_list|(
literal|"abandon and submit actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|rebaseChange
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"abandon and rebase actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|moveToBranch
operator|!=
literal|null
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"abandon and move actions are mutually exclusive"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|json
condition|)
block|{
if|if
condition|(
name|restoreChange
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"json and restore actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|submitChange
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"json and submit actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|abandonChange
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"json and abandon actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|changeComment
operator|!=
literal|null
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"json and message are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|rebaseChange
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"json and rebase actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|moveToBranch
operator|!=
literal|null
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"json and move actions are mutually exclusive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|changeTag
operator|!=
literal|null
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"json and tag actions are mutually exclusive"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|rebaseChange
condition|)
block|{
if|if
condition|(
name|submitChange
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"rebase and submit actions are mutually exclusive"
argument_list|)
throw|;
block|}
block|}
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
name|ReviewInput
name|input
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|json
condition|)
block|{
name|input
operator|=
name|reviewFromJson
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|PatchSet
name|patchSet
range|:
name|patchSets
control|)
block|{
try|try
block|{
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
name|applyReview
argument_list|(
name|patchSet
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reviewPatchSet
argument_list|(
name|patchSet
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RestApiException
decl||
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
literal|"error"
argument_list|,
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
literal|"error"
argument_list|,
literal|"no such change "
operator|+
name|patchSet
operator|.
name|getId
argument_list|()
operator|.
name|changeId
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
literal|"fatal"
argument_list|,
literal|"internal server error while reviewing "
operator|+
name|patchSet
operator|.
name|getId
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"internal error while reviewing %s"
argument_list|,
name|patchSet
operator|.
name|getId
argument_list|()
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
name|die
argument_list|(
literal|"one or more reviews failed; review output above"
argument_list|)
throw|;
block|}
block|}
DECL|method|applyReview (PatchSet patchSet, ReviewInput review)
specifier|private
name|void
name|applyReview
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|,
name|ReviewInput
name|review
parameter_list|)
throws|throws
name|RestApiException
block|{
name|gApi
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
name|changeId
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
name|getCommitId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|review
argument_list|(
name|review
argument_list|)
expr_stmt|;
block|}
DECL|method|reviewFromJson ()
specifier|private
name|ReviewInput
name|reviewFromJson
parameter_list|()
throws|throws
name|UnloggedFailure
block|{
try|try
init|(
name|InputStreamReader
name|r
init|=
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|UTF_8
argument_list|)
init|)
block|{
return|return
name|OutputFormat
operator|.
name|JSON
operator|.
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|CharStreams
operator|.
name|toString
argument_list|(
name|r
argument_list|)
argument_list|,
name|ReviewInput
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|JsonSyntaxException
name|e
parameter_list|)
block|{
name|writeError
argument_list|(
literal|"error"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|'\n'
argument_list|)
expr_stmt|;
throw|throw
name|die
argument_list|(
literal|"internal error while reading review input"
argument_list|)
throw|;
block|}
block|}
DECL|method|reviewPatchSet (PatchSet patchSet)
specifier|private
name|void
name|reviewPatchSet
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|)
throws|throws
name|Exception
block|{
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
name|tag
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|changeTag
argument_list|)
expr_stmt|;
name|review
operator|.
name|notify
operator|=
name|notify
expr_stmt|;
name|review
operator|.
name|labels
operator|=
operator|new
name|TreeMap
argument_list|<>
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
for|for
control|(
name|LabelSetter
name|setter
range|:
name|optionMap
operator|.
name|values
argument_list|()
control|)
block|{
name|setter
operator|.
name|getValue
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|v
lambda|->
name|review
operator|.
name|labels
operator|.
name|put
argument_list|(
name|setter
operator|.
name|getLabelName
argument_list|()
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
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
comment|// We don't need to add the review comment when abandoning/restoring.
if|if
condition|(
name|abandonChange
operator|||
name|restoreChange
operator|||
name|moveToBranch
operator|!=
literal|null
condition|)
block|{
name|review
operator|.
name|message
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
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|changeComment
argument_list|)
expr_stmt|;
name|applyReview
argument_list|(
name|patchSet
argument_list|,
name|review
argument_list|)
expr_stmt|;
name|changeApi
argument_list|(
name|patchSet
argument_list|)
operator|.
name|abandon
argument_list|(
name|input
argument_list|)
expr_stmt|;
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
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|changeComment
argument_list|)
expr_stmt|;
name|changeApi
argument_list|(
name|patchSet
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
name|moveToBranch
operator|!=
literal|null
condition|)
block|{
name|MoveInput
name|moveInput
init|=
operator|new
name|MoveInput
argument_list|()
decl_stmt|;
name|moveInput
operator|.
name|destinationBranch
operator|=
name|moveToBranch
expr_stmt|;
name|moveInput
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
name|changeApi
argument_list|(
name|patchSet
argument_list|)
operator|.
name|move
argument_list|(
name|moveInput
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rebaseChange
condition|)
block|{
name|revisionApi
argument_list|(
name|patchSet
argument_list|)
operator|.
name|rebase
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|submitChange
condition|)
block|{
name|revisionApi
argument_list|(
name|patchSet
argument_list|)
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalStateException
decl||
name|RestApiException
name|e
parameter_list|)
block|{
throw|throw
name|die
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|changeApi (PatchSet patchSet)
specifier|private
name|ChangeApi
name|changeApi
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|gApi
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
name|changeId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|revisionApi (PatchSet patchSet)
specifier|private
name|RevisionApi
name|revisionApi
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|changeApi
argument_list|(
name|patchSet
argument_list|)
operator|.
name|revision
argument_list|(
name|patchSet
operator|.
name|getCommitId
argument_list|()
operator|.
name|name
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
name|optionMap
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|customLabels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|ProjectState
name|allProjectsState
decl_stmt|;
try|try
block|{
name|allProjectsState
operator|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|allProjects
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|die
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
name|allProjectsState
operator|.
name|getLabelTypes
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
control|)
block|{
name|StringBuilder
name|usage
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"score for "
argument_list|)
operator|.
name|append
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
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
operator|.
name|append
argument_list|(
name|v
operator|.
name|format
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|optionMap
operator|.
name|put
argument_list|(
name|newApproveOption
argument_list|(
name|type
argument_list|,
name|usage
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
operator|new
name|LabelSetter
argument_list|(
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
DECL|method|asOptionName (LabelType type)
specifier|private
specifier|static
name|String
name|asOptionName
parameter_list|(
name|LabelType
name|type
parameter_list|)
block|{
return|return
literal|"--"
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
DECL|method|newApproveOption (LabelType type, String usage)
specifier|private
specifier|static
name|Option
name|newApproveOption
parameter_list|(
name|LabelType
name|type
parameter_list|,
name|String
name|usage
parameter_list|)
block|{
return|return
name|OptionUtil
operator|.
name|newOption
argument_list|(
name|asOptionName
argument_list|(
name|type
argument_list|)
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|usage
argument_list|,
literal|"N"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|LabelHandler
operator|.
name|class
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
return|;
block|}
DECL|class|LabelSetter
specifier|private
specifier|static
class|class
name|LabelSetter
implements|implements
name|Setter
argument_list|<
name|Short
argument_list|>
block|{
DECL|field|type
specifier|private
specifier|final
name|LabelType
name|type
decl_stmt|;
DECL|field|value
specifier|private
name|Optional
argument_list|<
name|Short
argument_list|>
name|value
decl_stmt|;
DECL|method|LabelSetter (LabelType type)
name|LabelSetter
parameter_list|(
name|LabelType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|requireNonNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
block|}
DECL|method|getValue ()
name|Optional
argument_list|<
name|Short
argument_list|>
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|getLabelType ()
name|LabelType
name|getLabelType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getLabelName ()
name|String
name|getLabelName
parameter_list|()
block|{
return|return
name|type
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addValue (Short value)
specifier|public
name|void
name|addValue
parameter_list|(
name|Short
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|Short
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|Short
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|isMultiValued ()
specifier|public
name|boolean
name|isMultiValued
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|asFieldSetter ()
specifier|public
name|FieldSetter
name|asFieldSetter
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|asAnnotatedElement ()
specifier|public
name|AnnotatedElement
name|asAnnotatedElement
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
DECL|class|LabelHandler
specifier|public
specifier|static
class|class
name|LabelHandler
extends|extends
name|OneArgumentOptionHandler
argument_list|<
name|Short
argument_list|>
block|{
DECL|field|type
specifier|private
specifier|final
name|LabelType
name|type
decl_stmt|;
DECL|method|LabelHandler ( org.kohsuke.args4j.CmdLineParser parser, OptionDef option, Setter<Short> setter)
specifier|public
name|LabelHandler
parameter_list|(
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineParser
name|parser
parameter_list|,
name|OptionDef
name|option
parameter_list|,
name|Setter
argument_list|<
name|Short
argument_list|>
name|setter
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|option
argument_list|,
name|setter
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
operator|(
operator|(
name|LabelSetter
operator|)
name|setter
operator|)
operator|.
name|getLabelType
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parse (String token)
specifier|protected
name|Short
name|parse
parameter_list|(
name|String
name|token
parameter_list|)
throws|throws
name|NumberFormatException
throws|,
name|CmdLineException
block|{
name|String
name|argument
init|=
name|token
decl_stmt|;
if|if
condition|(
name|argument
operator|.
name|startsWith
argument_list|(
literal|"+"
argument_list|)
condition|)
block|{
name|argument
operator|=
name|argument
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|short
name|value
init|=
name|Short
operator|.
name|parseShort
argument_list|(
name|argument
argument_list|)
decl_stmt|;
name|LabelValue
name|min
init|=
name|type
operator|.
name|getMin
argument_list|()
decl_stmt|;
name|LabelValue
name|max
init|=
name|type
operator|.
name|getMax
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
argument_list|<
name|min
operator|.
name|getValue
operator|(
operator|)
operator|||
name|value
argument_list|>
name|max
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|String
name|e
init|=
literal|"\""
operator|+
name|token
operator|+
literal|"\" must be in range "
operator|+
name|min
operator|.
name|formatValue
argument_list|()
operator|+
literal|".."
operator|+
name|max
operator|.
name|formatValue
argument_list|()
operator|+
literal|" for \""
operator|+
name|asOptionName
argument_list|(
name|type
argument_list|)
operator|+
literal|"\""
decl_stmt|;
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
name|localizable
argument_list|(
name|e
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

