begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

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
name|CommentsUtil
operator|.
name|COMMENT_INFO_ORDER
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|Streams
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
name|Nullable
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
name|Comment
operator|.
name|Range
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
name|Side
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
name|CommentInfo
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
name|FixReplacementInfo
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
name|FixSuggestionInfo
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
name|RobotCommentInfo
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
name|Url
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
name|Comment
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
name|FixReplacement
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
name|FixSuggestion
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
name|RobotComment
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
name|AccountLoader
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
name|permissions
operator|.
name|PermissionBackendException
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
name|TreeMap
import|;
end_import

begin_class
DECL|class|CommentJson
specifier|public
class|class
name|CommentJson
block|{
DECL|field|accountLoaderFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
decl_stmt|;
DECL|field|fillAccounts
specifier|private
name|boolean
name|fillAccounts
init|=
literal|true
decl_stmt|;
DECL|field|fillPatchSet
specifier|private
name|boolean
name|fillPatchSet
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommentJson (AccountLoader.Factory accountLoaderFactory)
name|CommentJson
parameter_list|(
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|)
block|{
name|this
operator|.
name|accountLoaderFactory
operator|=
name|accountLoaderFactory
expr_stmt|;
block|}
DECL|method|setFillAccounts (boolean fillAccounts)
name|CommentJson
name|setFillAccounts
parameter_list|(
name|boolean
name|fillAccounts
parameter_list|)
block|{
name|this
operator|.
name|fillAccounts
operator|=
name|fillAccounts
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setFillPatchSet (boolean fillPatchSet)
name|CommentJson
name|setFillPatchSet
parameter_list|(
name|boolean
name|fillPatchSet
parameter_list|)
block|{
name|this
operator|.
name|fillPatchSet
operator|=
name|fillPatchSet
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|newCommentFormatter ()
specifier|public
name|CommentFormatter
name|newCommentFormatter
parameter_list|()
block|{
return|return
operator|new
name|CommentFormatter
argument_list|()
return|;
block|}
DECL|method|newRobotCommentFormatter ()
specifier|public
name|RobotCommentFormatter
name|newRobotCommentFormatter
parameter_list|()
block|{
return|return
operator|new
name|RobotCommentFormatter
argument_list|()
return|;
block|}
DECL|class|BaseCommentFormatter
specifier|private
specifier|abstract
class|class
name|BaseCommentFormatter
parameter_list|<
name|F
extends|extends
name|Comment
parameter_list|,
name|T
extends|extends
name|CommentInfo
parameter_list|>
block|{
DECL|method|format (F comment)
specifier|public
name|T
name|format
parameter_list|(
name|F
name|comment
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|AccountLoader
name|loader
init|=
name|fillAccounts
condition|?
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
else|:
literal|null
decl_stmt|;
name|T
name|info
init|=
name|toInfo
argument_list|(
name|comment
argument_list|,
name|loader
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|loader
operator|.
name|fill
argument_list|()
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
DECL|method|format (Iterable<F> comments)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
name|format
parameter_list|(
name|Iterable
argument_list|<
name|F
argument_list|>
name|comments
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|AccountLoader
name|loader
init|=
name|fillAccounts
condition|?
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
else|:
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
name|out
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|F
name|c
range|:
name|comments
control|)
block|{
name|T
name|o
init|=
name|toInfo
argument_list|(
name|c
argument_list|,
name|loader
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
name|out
operator|.
name|get
argument_list|(
name|o
operator|.
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|out
operator|.
name|put
argument_list|(
name|o
operator|.
name|path
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
name|o
operator|.
name|path
operator|=
literal|null
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|values
argument_list|()
operator|.
name|forEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|sort
argument_list|(
name|COMMENT_INFO_ORDER
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|loader
operator|.
name|fill
argument_list|()
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
DECL|method|formatAsList (Iterable<F> comments)
specifier|public
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|formatAsList
parameter_list|(
name|Iterable
argument_list|<
name|F
argument_list|>
name|comments
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|AccountLoader
name|loader
init|=
name|fillAccounts
condition|?
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
else|:
literal|null
decl_stmt|;
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|out
init|=
name|Streams
operator|.
name|stream
argument_list|(
name|comments
argument_list|)
operator|.
name|map
argument_list|(
name|c
lambda|->
name|toInfo
argument_list|(
name|c
argument_list|,
name|loader
argument_list|)
argument_list|)
operator|.
name|sorted
argument_list|(
name|COMMENT_INFO_ORDER
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|loader
operator|.
name|fill
argument_list|()
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
DECL|method|toInfo (F comment, AccountLoader loader)
specifier|protected
specifier|abstract
name|T
name|toInfo
parameter_list|(
name|F
name|comment
parameter_list|,
name|AccountLoader
name|loader
parameter_list|)
function_decl|;
DECL|method|fillCommentInfo (Comment c, CommentInfo r, AccountLoader loader)
specifier|protected
name|void
name|fillCommentInfo
parameter_list|(
name|Comment
name|c
parameter_list|,
name|CommentInfo
name|r
parameter_list|,
name|AccountLoader
name|loader
parameter_list|)
block|{
if|if
condition|(
name|fillPatchSet
condition|)
block|{
name|r
operator|.
name|patchSet
operator|=
name|c
operator|.
name|key
operator|.
name|patchSetId
expr_stmt|;
block|}
name|r
operator|.
name|id
operator|=
name|Url
operator|.
name|encode
argument_list|(
name|c
operator|.
name|key
operator|.
name|uuid
argument_list|)
expr_stmt|;
name|r
operator|.
name|path
operator|=
name|c
operator|.
name|key
operator|.
name|filename
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|side
operator|<=
literal|0
condition|)
block|{
name|r
operator|.
name|side
operator|=
name|Side
operator|.
name|PARENT
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|side
operator|<
literal|0
condition|)
block|{
name|r
operator|.
name|parent
operator|=
operator|-
name|c
operator|.
name|side
expr_stmt|;
block|}
block|}
if|if
condition|(
name|c
operator|.
name|lineNbr
operator|>
literal|0
condition|)
block|{
name|r
operator|.
name|line
operator|=
name|c
operator|.
name|lineNbr
expr_stmt|;
block|}
name|r
operator|.
name|inReplyTo
operator|=
name|Url
operator|.
name|encode
argument_list|(
name|c
operator|.
name|parentUuid
argument_list|)
expr_stmt|;
name|r
operator|.
name|message
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|c
operator|.
name|message
argument_list|)
expr_stmt|;
name|r
operator|.
name|updated
operator|=
name|c
operator|.
name|writtenOn
expr_stmt|;
name|r
operator|.
name|range
operator|=
name|toRange
argument_list|(
name|c
operator|.
name|range
argument_list|)
expr_stmt|;
name|r
operator|.
name|tag
operator|=
name|c
operator|.
name|tag
expr_stmt|;
name|r
operator|.
name|unresolved
operator|=
name|c
operator|.
name|unresolved
expr_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|author
operator|=
name|loader
operator|.
name|get
argument_list|(
name|c
operator|.
name|author
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|toRange (Comment.Range commentRange)
specifier|protected
name|Range
name|toRange
parameter_list|(
name|Comment
operator|.
name|Range
name|commentRange
parameter_list|)
block|{
name|Range
name|range
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|commentRange
operator|!=
literal|null
condition|)
block|{
name|range
operator|=
operator|new
name|Range
argument_list|()
expr_stmt|;
name|range
operator|.
name|startLine
operator|=
name|commentRange
operator|.
name|startLine
expr_stmt|;
name|range
operator|.
name|startCharacter
operator|=
name|commentRange
operator|.
name|startChar
expr_stmt|;
name|range
operator|.
name|endLine
operator|=
name|commentRange
operator|.
name|endLine
expr_stmt|;
name|range
operator|.
name|endCharacter
operator|=
name|commentRange
operator|.
name|endChar
expr_stmt|;
block|}
return|return
name|range
return|;
block|}
block|}
DECL|class|CommentFormatter
specifier|public
class|class
name|CommentFormatter
extends|extends
name|BaseCommentFormatter
argument_list|<
name|Comment
argument_list|,
name|CommentInfo
argument_list|>
block|{
annotation|@
name|Override
DECL|method|toInfo (Comment c, AccountLoader loader)
specifier|protected
name|CommentInfo
name|toInfo
parameter_list|(
name|Comment
name|c
parameter_list|,
name|AccountLoader
name|loader
parameter_list|)
block|{
name|CommentInfo
name|ci
init|=
operator|new
name|CommentInfo
argument_list|()
decl_stmt|;
name|fillCommentInfo
argument_list|(
name|c
argument_list|,
name|ci
argument_list|,
name|loader
argument_list|)
expr_stmt|;
return|return
name|ci
return|;
block|}
DECL|method|CommentFormatter ()
specifier|private
name|CommentFormatter
parameter_list|()
block|{}
block|}
DECL|class|RobotCommentFormatter
class|class
name|RobotCommentFormatter
extends|extends
name|BaseCommentFormatter
argument_list|<
name|RobotComment
argument_list|,
name|RobotCommentInfo
argument_list|>
block|{
annotation|@
name|Override
DECL|method|toInfo (RobotComment c, AccountLoader loader)
specifier|protected
name|RobotCommentInfo
name|toInfo
parameter_list|(
name|RobotComment
name|c
parameter_list|,
name|AccountLoader
name|loader
parameter_list|)
block|{
name|RobotCommentInfo
name|rci
init|=
operator|new
name|RobotCommentInfo
argument_list|()
decl_stmt|;
name|rci
operator|.
name|robotId
operator|=
name|c
operator|.
name|robotId
expr_stmt|;
name|rci
operator|.
name|robotRunId
operator|=
name|c
operator|.
name|robotRunId
expr_stmt|;
name|rci
operator|.
name|url
operator|=
name|c
operator|.
name|url
expr_stmt|;
name|rci
operator|.
name|properties
operator|=
name|c
operator|.
name|properties
expr_stmt|;
name|rci
operator|.
name|fixSuggestions
operator|=
name|toFixSuggestionInfos
argument_list|(
name|c
operator|.
name|fixSuggestions
argument_list|)
expr_stmt|;
name|fillCommentInfo
argument_list|(
name|c
argument_list|,
name|rci
argument_list|,
name|loader
argument_list|)
expr_stmt|;
return|return
name|rci
return|;
block|}
DECL|method|toFixSuggestionInfos ( @ullable List<FixSuggestion> fixSuggestions)
specifier|private
name|List
argument_list|<
name|FixSuggestionInfo
argument_list|>
name|toFixSuggestionInfos
parameter_list|(
annotation|@
name|Nullable
name|List
argument_list|<
name|FixSuggestion
argument_list|>
name|fixSuggestions
parameter_list|)
block|{
if|if
condition|(
name|fixSuggestions
operator|==
literal|null
operator|||
name|fixSuggestions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|fixSuggestions
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|this
operator|::
name|toFixSuggestionInfo
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toFixSuggestionInfo (FixSuggestion fixSuggestion)
specifier|private
name|FixSuggestionInfo
name|toFixSuggestionInfo
parameter_list|(
name|FixSuggestion
name|fixSuggestion
parameter_list|)
block|{
name|FixSuggestionInfo
name|fixSuggestionInfo
init|=
operator|new
name|FixSuggestionInfo
argument_list|()
decl_stmt|;
name|fixSuggestionInfo
operator|.
name|fixId
operator|=
name|fixSuggestion
operator|.
name|fixId
expr_stmt|;
name|fixSuggestionInfo
operator|.
name|description
operator|=
name|fixSuggestion
operator|.
name|description
expr_stmt|;
name|fixSuggestionInfo
operator|.
name|replacements
operator|=
name|fixSuggestion
operator|.
name|replacements
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|this
operator|::
name|toFixReplacementInfo
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|fixSuggestionInfo
return|;
block|}
DECL|method|toFixReplacementInfo (FixReplacement fixReplacement)
specifier|private
name|FixReplacementInfo
name|toFixReplacementInfo
parameter_list|(
name|FixReplacement
name|fixReplacement
parameter_list|)
block|{
name|FixReplacementInfo
name|fixReplacementInfo
init|=
operator|new
name|FixReplacementInfo
argument_list|()
decl_stmt|;
name|fixReplacementInfo
operator|.
name|path
operator|=
name|fixReplacement
operator|.
name|path
expr_stmt|;
name|fixReplacementInfo
operator|.
name|range
operator|=
name|toRange
argument_list|(
name|fixReplacement
operator|.
name|range
argument_list|)
expr_stmt|;
name|fixReplacementInfo
operator|.
name|replacement
operator|=
name|fixReplacement
operator|.
name|replacement
expr_stmt|;
return|return
name|fixReplacementInfo
return|;
block|}
DECL|method|RobotCommentFormatter ()
specifier|private
name|RobotCommentFormatter
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit

