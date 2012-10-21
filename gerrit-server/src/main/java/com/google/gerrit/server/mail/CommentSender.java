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
name|server
operator|.
name|config
operator|.
name|AnonymousCowardName
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
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (Change change)
specifier|public
name|CommentSender
name|create
parameter_list|(
name|Change
name|change
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
annotation|@
name|Inject
DECL|method|CommentSender (EmailArguments ea, @AnonymousCowardName String anonymousCowardName, @Assisted Change c)
specifier|public
name|CommentSender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
annotation|@
name|Assisted
name|Change
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
name|anonymousCowardName
argument_list|,
name|c
argument_list|,
literal|"comment"
argument_list|)
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
argument_list|<
name|String
argument_list|>
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
name|String
index|[]
name|names
init|=
name|paths
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|paths
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|changeData
operator|.
name|setCurrentFilePaths
argument_list|(
name|names
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
name|ccAllApprovals
argument_list|()
expr_stmt|;
name|bccStarredBy
argument_list|()
expr_stmt|;
name|bccWatches
argument_list|(
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
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
specifier|final
name|Repository
name|repo
init|=
name|getRepository
argument_list|()
decl_stmt|;
try|try
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
name|patchList
operator|=
literal|null
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
specifier|final
name|int
name|lineNbr
init|=
name|c
operator|.
name|getLine
argument_list|()
decl_stmt|;
specifier|final
name|short
name|side
init|=
name|c
operator|.
name|getSide
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
name|cmts
operator|.
name|append
argument_list|(
literal|"....................................................\n"
argument_list|)
expr_stmt|;
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
literal|"Commit Message\n"
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
expr_stmt|;
name|cmts
operator|.
name|append
argument_list|(
name|pk
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|cmts
operator|.
name|append
argument_list|(
literal|"\n"
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
name|getFileName
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
comment|// Don't quote the line if we can't load it.
block|}
block|}
else|else
block|{
name|currentFileData
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|currentFileData
operator|!=
literal|null
condition|)
block|{
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
name|lines
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
name|lines
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
name|cmts
argument_list|,
name|currentFileData
argument_list|,
name|side
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
name|cmts
operator|.
name|append
argument_list|(
name|c
operator|.
name|getMessage
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|cmts
operator|.
name|append
argument_list|(
literal|"\n"
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
name|cmts
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
name|cmts
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|repo
operator|.
name|close
argument_list|()
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

