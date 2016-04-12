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
DECL|package|com.google.gerrit.server.mail
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
name|server
operator|.
name|PatchLineCommentsUtil
operator|.
name|getCommentPsId
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
name|Ordering
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ReviewInput
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountProjectWatch
operator|.
name|NotifyType
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
name|CommentRange
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
name|Patch
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
name|PatchLineComment
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
name|PatchLineCommentsUtil
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
name|patch
operator|.
name|PatchFile
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
name|patch
operator|.
name|PatchList
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
name|patch
operator|.
name|PatchListNotAvailableException
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
name|KeyUtil
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
name|assistedinject
operator|.
name|Assisted
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
name|Repository
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
name|Collections
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
name|Set
import|;
end_import

begin_comment
comment|/** Send comments, after the author of them hit used Publish Comments in the UI. */
end_comment

begin_class
DECL|class|CommentSender
specifier|public
class|class
name|CommentSender
extends|extends
name|ReplyToChangeSender
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
name|CommentSender
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Project.NameKey project, Change.Id id)
name|CommentSender
name|create
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
block|}
DECL|field|inlineComments
specifier|private
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|inlineComments
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
DECL|field|plcUtil
specifier|private
specifier|final
name|PatchLineCommentsUtil
name|plcUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommentSender (EmailArguments ea, PatchLineCommentsUtil plcUtil, @Assisted Project.NameKey project, @Assisted Change.Id id)
specifier|public
name|CommentSender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|PatchLineCommentsUtil
name|plcUtil
parameter_list|,
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
annotation|@
name|Assisted
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
name|super
argument_list|(
name|ea
argument_list|,
literal|"comment"
argument_list|,
name|newChangeData
argument_list|(
name|ea
argument_list|,
name|project
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|plcUtil
operator|=
name|plcUtil
expr_stmt|;
block|}
DECL|method|setPatchLineComments (final List<PatchLineComment> plc)
specifier|public
name|void
name|setPatchLineComments
parameter_list|(
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|plc
parameter_list|)
throws|throws
name|OrmException
block|{
name|inlineComments
operator|=
name|plc
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|paths
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchLineComment
name|c
range|:
name|plc
control|)
block|{
name|Patch
operator|.
name|Key
name|p
init|=
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getFileName
argument_list|()
argument_list|)
condition|)
block|{
name|paths
operator|.
name|add
argument_list|(
name|p
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|changeData
operator|.
name|setCurrentFilePaths
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|sortedCopy
argument_list|(
name|paths
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
if|if
condition|(
name|notify
operator|.
name|compareTo
argument_list|(
name|NotifyHandling
operator|.
name|OWNER_REVIEWERS
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|ccAllApprovals
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|notify
operator|.
name|compareTo
argument_list|(
name|NotifyHandling
operator|.
name|ALL
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|bccStarredBy
argument_list|()
expr_stmt|;
name|includeWatchers
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|formatChange ()
specifier|public
name|void
name|formatChange
parameter_list|()
throws|throws
name|EmailException
block|{
name|appendText
argument_list|(
name|velocifyFile
argument_list|(
literal|"Comment.vm"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|formatFooter ()
specifier|public
name|void
name|formatFooter
parameter_list|()
throws|throws
name|EmailException
block|{
name|appendText
argument_list|(
name|velocifyFile
argument_list|(
literal|"CommentFooter.vm"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|hasInlineComments ()
specifier|public
name|boolean
name|hasInlineComments
parameter_list|()
block|{
return|return
operator|!
name|inlineComments
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|getInlineComments ()
specifier|public
name|String
name|getInlineComments
parameter_list|()
block|{
return|return
name|getInlineComments
argument_list|(
literal|1
argument_list|)
return|;
block|}
DECL|method|getInlineComments (int lines)
specifier|public
name|String
name|getInlineComments
parameter_list|(
name|int
name|lines
parameter_list|)
block|{
name|StringBuilder
name|cmts
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|getRepository
argument_list|()
init|)
block|{
name|PatchList
name|patchList
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|patchList
operator|=
name|getPatchList
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatchListNotAvailableException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to get patch list"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|Patch
operator|.
name|Key
name|currentFileKey
init|=
literal|null
decl_stmt|;
name|PatchFile
name|currentFileData
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|PatchLineComment
name|c
range|:
name|inlineComments
control|)
block|{
specifier|final
name|Patch
operator|.
name|Key
name|pk
init|=
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|pk
operator|.
name|equals
argument_list|(
name|currentFileKey
argument_list|)
condition|)
block|{
name|String
name|link
init|=
name|makeLink
argument_list|(
name|pk
argument_list|)
decl_stmt|;
if|if
condition|(
name|link
operator|!=
literal|null
condition|)
block|{
name|cmts
operator|.
name|append
argument_list|(
name|link
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|pk
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|cmts
operator|.
name|append
argument_list|(
literal|"Commit Message:\n\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cmts
operator|.
name|append
argument_list|(
literal|"File "
argument_list|)
operator|.
name|append
argument_list|(
name|pk
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":\n\n"
argument_list|)
expr_stmt|;
block|}
name|currentFileKey
operator|=
name|pk
expr_stmt|;
if|if
condition|(
name|patchList
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|currentFileData
operator|=
operator|new
name|PatchFile
argument_list|(
name|repo
argument_list|,
name|patchList
argument_list|,
name|pk
operator|.
name|get
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
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load %s from %s in %s"
argument_list|,
name|pk
operator|.
name|getFileName
argument_list|()
argument_list|,
name|patchList
operator|.
name|getNewId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|currentFileData
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|currentFileData
operator|!=
literal|null
condition|)
block|{
name|appendComment
argument_list|(
name|cmts
argument_list|,
name|lines
argument_list|,
name|currentFileData
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
name|cmts
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cmts
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|appendComment (StringBuilder out, int contextLines, PatchFile currentFileData, PatchLineComment comment)
specifier|private
name|void
name|appendComment
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|int
name|contextLines
parameter_list|,
name|PatchFile
name|currentFileData
parameter_list|,
name|PatchLineComment
name|comment
parameter_list|)
block|{
name|short
name|side
init|=
name|comment
operator|.
name|getSide
argument_list|()
decl_stmt|;
name|CommentRange
name|range
init|=
name|comment
operator|.
name|getRange
argument_list|()
decl_stmt|;
if|if
condition|(
name|range
operator|!=
literal|null
condition|)
block|{
name|String
name|prefix
init|=
literal|"PS"
operator|+
name|getCommentPsId
argument_list|(
name|comment
argument_list|)
operator|.
name|get
argument_list|()
operator|+
literal|", Line "
operator|+
name|range
operator|.
name|getStartLine
argument_list|()
operator|+
literal|": "
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
name|range
operator|.
name|getStartLine
argument_list|()
init|;
name|n
operator|<=
name|range
operator|.
name|getEndLine
argument_list|()
condition|;
name|n
operator|++
control|)
block|{
name|out
operator|.
name|append
argument_list|(
name|n
operator|==
name|range
operator|.
name|getStartLine
argument_list|()
condition|?
name|prefix
else|:
name|Strings
operator|.
name|padStart
argument_list|(
literal|": "
argument_list|,
name|prefix
operator|.
name|length
argument_list|()
argument_list|,
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|s
init|=
name|currentFileData
operator|.
name|getLine
argument_list|(
name|side
argument_list|,
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|==
name|range
operator|.
name|getStartLine
argument_list|()
operator|&&
name|n
operator|==
name|range
operator|.
name|getEndLine
argument_list|()
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|range
operator|.
name|getStartCharacter
argument_list|()
argument_list|,
name|s
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|range
operator|.
name|getEndCharacter
argument_list|()
argument_list|,
name|s
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|==
name|range
operator|.
name|getStartLine
argument_list|()
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|range
operator|.
name|getStartCharacter
argument_list|()
argument_list|,
name|s
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|==
name|range
operator|.
name|getEndLine
argument_list|()
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|range
operator|.
name|getEndCharacter
argument_list|()
argument_list|,
name|s
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// Don't quote the line if we can't safely convert it.
block|}
name|out
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|appendQuotedParent
argument_list|(
name|out
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|comment
operator|.
name|getMessage
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|lineNbr
init|=
name|comment
operator|.
name|getLine
argument_list|()
decl_stmt|;
name|int
name|maxLines
decl_stmt|;
try|try
block|{
name|maxLines
operator|=
name|currentFileData
operator|.
name|getLineCount
argument_list|(
name|side
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|maxLines
operator|=
name|lineNbr
expr_stmt|;
block|}
specifier|final
name|int
name|startLine
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|lineNbr
operator|-
name|contextLines
operator|+
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|int
name|stopLine
init|=
name|Math
operator|.
name|min
argument_list|(
name|maxLines
argument_list|,
name|lineNbr
operator|+
name|contextLines
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|line
init|=
name|startLine
init|;
name|line
operator|<=
name|lineNbr
condition|;
operator|++
name|line
control|)
block|{
name|appendFileLine
argument_list|(
name|out
argument_list|,
name|currentFileData
argument_list|,
name|side
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
name|appendQuotedParent
argument_list|(
name|out
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|comment
operator|.
name|getMessage
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|line
init|=
name|lineNbr
operator|+
literal|1
init|;
name|line
operator|<
name|stopLine
condition|;
operator|++
name|line
control|)
block|{
name|appendFileLine
argument_list|(
name|out
argument_list|,
name|currentFileData
argument_list|,
name|side
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|appendFileLine (StringBuilder cmts, PatchFile fileData, short side, int line)
specifier|private
name|void
name|appendFileLine
parameter_list|(
name|StringBuilder
name|cmts
parameter_list|,
name|PatchFile
name|fileData
parameter_list|,
name|short
name|side
parameter_list|,
name|int
name|line
parameter_list|)
block|{
name|cmts
operator|.
name|append
argument_list|(
literal|"Line "
operator|+
name|line
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|String
name|lineStr
init|=
name|fileData
operator|.
name|getLine
argument_list|(
name|side
argument_list|,
name|line
argument_list|)
decl_stmt|;
name|cmts
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|cmts
operator|.
name|append
argument_list|(
name|lineStr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// Don't quote the line if we can't safely convert it.
block|}
name|cmts
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|appendQuotedParent (StringBuilder out, PatchLineComment child)
specifier|private
name|void
name|appendQuotedParent
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|PatchLineComment
name|child
parameter_list|)
block|{
if|if
condition|(
name|child
operator|.
name|getParentUuid
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Optional
argument_list|<
name|PatchLineComment
argument_list|>
name|parent
decl_stmt|;
name|PatchLineComment
operator|.
name|Key
name|key
init|=
operator|new
name|PatchLineComment
operator|.
name|Key
argument_list|(
name|child
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|child
operator|.
name|getParentUuid
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|parent
operator|=
name|plcUtil
operator|.
name|get
argument_list|(
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|changeData
operator|.
name|notes
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not find the parent of this comment: "
operator|+
name|child
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|parent
operator|=
name|Optional
operator|.
name|absent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parent
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
name|parent
operator|.
name|get
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|length
argument_list|()
operator|>
literal|75
condition|)
block|{
name|msg
operator|=
name|msg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|75
argument_list|)
expr_stmt|;
block|}
name|int
name|lf
init|=
name|msg
operator|.
name|indexOf
argument_list|(
literal|'\n'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lf
operator|>
literal|0
condition|)
block|{
name|msg
operator|=
name|msg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lf
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
literal|"> "
argument_list|)
operator|.
name|append
argument_list|(
name|msg
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Makes a link back to the given patch set and file.
DECL|method|makeLink (Patch.Key patch)
specifier|private
name|String
name|makeLink
parameter_list|(
name|Patch
operator|.
name|Key
name|patch
parameter_list|)
block|{
name|String
name|url
init|=
name|getGerritUrl
argument_list|()
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
name|PatchSet
operator|.
name|Id
name|ps
init|=
name|patch
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|c
init|=
name|ps
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|url
argument_list|)
operator|.
name|append
argument_list|(
literal|"#/c/"
argument_list|)
operator|.
name|append
argument_list|(
name|c
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|ps
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|KeyUtil
operator|.
name|encode
argument_list|(
name|patch
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getRepository ()
specifier|private
name|Repository
name|getRepository
parameter_list|()
block|{
try|try
block|{
return|return
name|args
operator|.
name|server
operator|.
name|openRepository
argument_list|(
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

