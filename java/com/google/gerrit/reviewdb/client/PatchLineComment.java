begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|gwtorm
operator|.
name|client
operator|.
name|Column
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
name|StringKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * A comment left by a user on a specific line of a {@link Patch}.  *  *<p>New APIs should not expose this class.  *  * @see Comment  */
end_comment

begin_class
DECL|class|PatchLineComment
specifier|public
specifier|final
class|class
name|PatchLineComment
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|StringKey
argument_list|<
name|Patch
operator|.
name|Key
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|from (Change.Id changeId, Comment.Key key)
specifier|public
specifier|static
name|Key
name|from
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Comment
operator|.
name|Key
name|key
parameter_list|)
block|{
return|return
operator|new
name|Key
argument_list|(
operator|new
name|Patch
operator|.
name|Key
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
name|key
operator|.
name|patchSetId
argument_list|)
argument_list|,
name|key
operator|.
name|filename
argument_list|)
argument_list|,
name|key
operator|.
name|uuid
argument_list|)
return|;
block|}
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|patchKey
specifier|protected
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|,
name|length
operator|=
literal|40
argument_list|)
DECL|field|uuid
specifier|protected
name|String
name|uuid
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|patchKey
operator|=
operator|new
name|Patch
operator|.
name|Key
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (Patch.Key p, String uuid)
specifier|public
name|Key
parameter_list|(
name|Patch
operator|.
name|Key
name|p
parameter_list|,
name|String
name|uuid
parameter_list|)
block|{
name|this
operator|.
name|patchKey
operator|=
name|p
expr_stmt|;
name|this
operator|.
name|uuid
operator|=
name|uuid
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Patch
operator|.
name|Key
name|getParentKey
parameter_list|()
block|{
return|return
name|patchKey
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|public
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|uuid
operator|=
name|newValue
expr_stmt|;
block|}
DECL|method|asCommentKey ()
specifier|public
name|Comment
operator|.
name|Key
name|asCommentKey
parameter_list|()
block|{
return|return
operator|new
name|Comment
operator|.
name|Key
argument_list|(
name|get
argument_list|()
argument_list|,
name|getParentKey
argument_list|()
operator|.
name|getFileName
argument_list|()
argument_list|,
name|getParentKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|field|STATUS_DRAFT
specifier|public
specifier|static
specifier|final
name|char
name|STATUS_DRAFT
init|=
literal|'d'
decl_stmt|;
DECL|field|STATUS_PUBLISHED
specifier|public
specifier|static
specifier|final
name|char
name|STATUS_PUBLISHED
init|=
literal|'P'
decl_stmt|;
DECL|enum|Status
specifier|public
enum|enum
name|Status
block|{
DECL|enumConstant|DRAFT
name|DRAFT
parameter_list|(
name|STATUS_DRAFT
parameter_list|)
operator|,
DECL|enumConstant|PUBLISHED
constructor|PUBLISHED(STATUS_PUBLISHED
block|)
enum|;
DECL|field|code
specifier|private
specifier|final
name|char
name|code
decl_stmt|;
DECL|method|Status (char c)
name|Status
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|code
operator|=
name|c
expr_stmt|;
block|}
DECL|method|getCode ()
specifier|public
name|char
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
DECL|method|forCode (char c)
specifier|public
specifier|static
name|Status
name|forCode
parameter_list|(
name|char
name|c
parameter_list|)
block|{
for|for
control|(
name|Status
name|s
range|:
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|code
operator|==
name|c
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

begin_function
DECL|method|from ( Change.Id changeId, PatchLineComment.Status status, Comment c)
specifier|public
specifier|static
name|PatchLineComment
name|from
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|PatchLineComment
operator|.
name|Status
name|status
parameter_list|,
name|Comment
name|c
parameter_list|)
block|{
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
operator|new
name|Patch
operator|.
name|Key
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
name|c
operator|.
name|key
operator|.
name|patchSetId
argument_list|)
argument_list|,
name|c
operator|.
name|key
operator|.
name|filename
argument_list|)
argument_list|,
name|c
operator|.
name|key
operator|.
name|uuid
argument_list|)
decl_stmt|;
name|PatchLineComment
name|plc
init|=
operator|new
name|PatchLineComment
argument_list|(
name|key
argument_list|,
name|c
operator|.
name|lineNbr
argument_list|,
name|c
operator|.
name|author
operator|.
name|getId
argument_list|()
argument_list|,
name|c
operator|.
name|parentUuid
argument_list|,
name|c
operator|.
name|writtenOn
argument_list|)
decl_stmt|;
name|plc
operator|.
name|setSide
argument_list|(
name|c
operator|.
name|side
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setMessage
argument_list|(
name|c
operator|.
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|range
operator|!=
literal|null
condition|)
block|{
name|Comment
operator|.
name|Range
name|r
init|=
name|c
operator|.
name|range
decl_stmt|;
name|plc
operator|.
name|setRange
argument_list|(
operator|new
name|CommentRange
argument_list|(
name|r
operator|.
name|startLine
argument_list|,
name|r
operator|.
name|startChar
argument_list|,
name|r
operator|.
name|endLine
argument_list|,
name|r
operator|.
name|endChar
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|plc
operator|.
name|setTag
argument_list|(
name|c
operator|.
name|tag
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setRevId
argument_list|(
operator|new
name|RevId
argument_list|(
name|c
operator|.
name|revId
argument_list|)
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setRealAuthor
argument_list|(
name|c
operator|.
name|getRealAuthor
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|plc
operator|.
name|setUnresolved
argument_list|(
name|c
operator|.
name|unresolved
argument_list|)
expr_stmt|;
return|return
name|plc
return|;
block|}
end_function

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Line number this comment applies to; it should display after the line. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|lineNbr
specifier|protected
name|int
name|lineNbr
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Who wrote this comment. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|,
name|name
operator|=
literal|"author_id"
argument_list|)
DECL|field|author
specifier|protected
name|Account
operator|.
name|Id
name|author
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** When this comment was drafted. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|)
DECL|field|writtenOn
specifier|protected
name|Timestamp
name|writtenOn
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Current publication state of the comment; see {@link Status}. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|5
argument_list|)
DECL|field|status
specifier|protected
name|char
name|status
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Which file is this comment; 0 is ancestor, 1 is new version. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|6
argument_list|)
DECL|field|side
specifier|protected
name|short
name|side
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** The text left by the user. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|7
argument_list|,
name|notNull
operator|=
literal|false
argument_list|,
name|length
operator|=
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
DECL|field|message
specifier|protected
name|String
name|message
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** The parent of this comment, or null if this is the first comment on this line */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|8
argument_list|,
name|length
operator|=
literal|40
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|parentUuid
specifier|protected
name|String
name|parentUuid
decl_stmt|;
end_decl_stmt

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|9
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|range
specifier|protected
name|CommentRange
name|range
decl_stmt|;
end_decl_stmt

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|10
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|tag
specifier|protected
name|String
name|tag
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** Real user that added this comment on behalf of the user recorded in {@link #author}. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|11
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|realAuthor
specifier|protected
name|Account
operator|.
name|Id
name|realAuthor
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** True if this comment requires further action. */
end_comment

begin_decl_stmt
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|12
argument_list|)
DECL|field|unresolved
specifier|protected
name|boolean
name|unresolved
decl_stmt|;
end_decl_stmt

begin_comment
comment|/** The RevId for the commit to which this comment is referring. */
end_comment

begin_decl_stmt
DECL|field|revId
specifier|protected
name|RevId
name|revId
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|PatchLineComment ()
specifier|protected
name|PatchLineComment
parameter_list|()
block|{}
end_constructor

begin_constructor
DECL|method|PatchLineComment ( PatchLineComment.Key id, int line, Account.Id a, String parentUuid, Timestamp when)
specifier|public
name|PatchLineComment
parameter_list|(
name|PatchLineComment
operator|.
name|Key
name|id
parameter_list|,
name|int
name|line
parameter_list|,
name|Account
operator|.
name|Id
name|a
parameter_list|,
name|String
name|parentUuid
parameter_list|,
name|Timestamp
name|when
parameter_list|)
block|{
name|key
operator|=
name|id
expr_stmt|;
name|lineNbr
operator|=
name|line
expr_stmt|;
name|author
operator|=
name|a
expr_stmt|;
name|setParentUuid
argument_list|(
name|parentUuid
argument_list|)
expr_stmt|;
name|setStatus
argument_list|(
name|Status
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
name|setWrittenOn
argument_list|(
name|when
argument_list|)
expr_stmt|;
block|}
end_constructor

begin_constructor
DECL|method|PatchLineComment (PatchLineComment o)
specifier|public
name|PatchLineComment
parameter_list|(
name|PatchLineComment
name|o
parameter_list|)
block|{
name|key
operator|=
name|o
operator|.
name|key
expr_stmt|;
name|lineNbr
operator|=
name|o
operator|.
name|lineNbr
expr_stmt|;
name|author
operator|=
name|o
operator|.
name|author
expr_stmt|;
name|realAuthor
operator|=
name|o
operator|.
name|realAuthor
expr_stmt|;
name|writtenOn
operator|=
name|o
operator|.
name|writtenOn
expr_stmt|;
name|status
operator|=
name|o
operator|.
name|status
expr_stmt|;
name|side
operator|=
name|o
operator|.
name|side
expr_stmt|;
name|message
operator|=
name|o
operator|.
name|message
expr_stmt|;
name|parentUuid
operator|=
name|o
operator|.
name|parentUuid
expr_stmt|;
name|revId
operator|=
name|o
operator|.
name|revId
expr_stmt|;
if|if
condition|(
name|o
operator|.
name|range
operator|!=
literal|null
condition|)
block|{
name|range
operator|=
operator|new
name|CommentRange
argument_list|(
name|o
operator|.
name|range
operator|.
name|getStartLine
argument_list|()
argument_list|,
name|o
operator|.
name|range
operator|.
name|getStartCharacter
argument_list|()
argument_list|,
name|o
operator|.
name|range
operator|.
name|getEndLine
argument_list|()
argument_list|,
name|o
operator|.
name|range
operator|.
name|getEndCharacter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_constructor

begin_function
DECL|method|getKey ()
specifier|public
name|PatchLineComment
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
end_function

begin_function
DECL|method|getPatchSetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getPatchSetId
parameter_list|()
block|{
return|return
name|key
operator|.
name|getParentKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|getLine ()
specifier|public
name|int
name|getLine
parameter_list|()
block|{
return|return
name|lineNbr
return|;
block|}
end_function

begin_function
DECL|method|setLine (int line)
specifier|public
name|void
name|setLine
parameter_list|(
name|int
name|line
parameter_list|)
block|{
name|lineNbr
operator|=
name|line
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getAuthor ()
specifier|public
name|Account
operator|.
name|Id
name|getAuthor
parameter_list|()
block|{
return|return
name|author
return|;
block|}
end_function

begin_function
DECL|method|getRealAuthor ()
specifier|public
name|Account
operator|.
name|Id
name|getRealAuthor
parameter_list|()
block|{
return|return
name|realAuthor
operator|!=
literal|null
condition|?
name|realAuthor
else|:
name|getAuthor
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|setRealAuthor (Account.Id id)
specifier|public
name|void
name|setRealAuthor
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
comment|// Use null for same real author, as before the column was added.
name|realAuthor
operator|=
name|Objects
operator|.
name|equals
argument_list|(
name|getAuthor
argument_list|()
argument_list|,
name|id
argument_list|)
condition|?
literal|null
else|:
name|id
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getWrittenOn ()
specifier|public
name|Timestamp
name|getWrittenOn
parameter_list|()
block|{
return|return
name|writtenOn
return|;
block|}
end_function

begin_function
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|Status
operator|.
name|forCode
argument_list|(
name|status
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|setStatus (Status s)
specifier|public
name|void
name|setStatus
parameter_list|(
name|Status
name|s
parameter_list|)
block|{
name|status
operator|=
name|s
operator|.
name|getCode
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getSide ()
specifier|public
name|short
name|getSide
parameter_list|()
block|{
return|return
name|side
return|;
block|}
end_function

begin_function
DECL|method|setSide (short s)
specifier|public
name|void
name|setSide
parameter_list|(
name|short
name|s
parameter_list|)
block|{
name|side
operator|=
name|s
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
end_function

begin_function
DECL|method|setMessage (String s)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|message
operator|=
name|s
expr_stmt|;
block|}
end_function

begin_function
DECL|method|setWrittenOn (Timestamp ts)
specifier|public
name|void
name|setWrittenOn
parameter_list|(
name|Timestamp
name|ts
parameter_list|)
block|{
name|writtenOn
operator|=
name|ts
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getParentUuid ()
specifier|public
name|String
name|getParentUuid
parameter_list|()
block|{
return|return
name|parentUuid
return|;
block|}
end_function

begin_function
DECL|method|setParentUuid (String inReplyTo)
specifier|public
name|void
name|setParentUuid
parameter_list|(
name|String
name|inReplyTo
parameter_list|)
block|{
name|parentUuid
operator|=
name|inReplyTo
expr_stmt|;
block|}
end_function

begin_function
DECL|method|setRange (Range r)
specifier|public
name|void
name|setRange
parameter_list|(
name|Range
name|r
parameter_list|)
block|{
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|range
operator|=
operator|new
name|CommentRange
argument_list|(
name|r
operator|.
name|startLine
argument_list|,
name|r
operator|.
name|startCharacter
argument_list|,
name|r
operator|.
name|endLine
argument_list|,
name|r
operator|.
name|endCharacter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|range
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|setRange (CommentRange r)
specifier|public
name|void
name|setRange
parameter_list|(
name|CommentRange
name|r
parameter_list|)
block|{
name|range
operator|=
name|r
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getRange ()
specifier|public
name|CommentRange
name|getRange
parameter_list|()
block|{
return|return
name|range
return|;
block|}
end_function

begin_function
DECL|method|setRevId (RevId rev)
specifier|public
name|void
name|setRevId
parameter_list|(
name|RevId
name|rev
parameter_list|)
block|{
name|revId
operator|=
name|rev
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getRevId ()
specifier|public
name|RevId
name|getRevId
parameter_list|()
block|{
return|return
name|revId
return|;
block|}
end_function

begin_function
DECL|method|setTag (String tag)
specifier|public
name|void
name|setTag
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
name|this
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getTag ()
specifier|public
name|String
name|getTag
parameter_list|()
block|{
return|return
name|tag
return|;
block|}
end_function

begin_function
DECL|method|setUnresolved (Boolean unresolved)
specifier|public
name|void
name|setUnresolved
parameter_list|(
name|Boolean
name|unresolved
parameter_list|)
block|{
name|this
operator|.
name|unresolved
operator|=
name|unresolved
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getUnresolved ()
specifier|public
name|Boolean
name|getUnresolved
parameter_list|()
block|{
return|return
name|unresolved
return|;
block|}
end_function

begin_function
DECL|method|asComment (String serverId)
specifier|public
name|Comment
name|asComment
parameter_list|(
name|String
name|serverId
parameter_list|)
block|{
name|Comment
name|c
init|=
operator|new
name|Comment
argument_list|(
name|key
operator|.
name|asCommentKey
argument_list|()
argument_list|,
name|author
argument_list|,
name|writtenOn
argument_list|,
name|side
argument_list|,
name|message
argument_list|,
name|serverId
argument_list|,
name|unresolved
argument_list|)
decl_stmt|;
name|c
operator|.
name|setRevId
argument_list|(
name|revId
argument_list|)
expr_stmt|;
name|c
operator|.
name|setRange
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|c
operator|.
name|lineNbr
operator|=
name|lineNbr
expr_stmt|;
name|c
operator|.
name|parentUuid
operator|=
name|parentUuid
expr_stmt|;
name|c
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
name|c
operator|.
name|setRealAuthor
argument_list|(
name|getRealAuthor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|PatchLineComment
condition|)
block|{
name|PatchLineComment
name|c
init|=
operator|(
name|PatchLineComment
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|key
argument_list|,
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|lineNbr
argument_list|,
name|c
operator|.
name|getLine
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|author
argument_list|,
name|c
operator|.
name|getAuthor
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|writtenOn
argument_list|,
name|c
operator|.
name|getWrittenOn
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|status
argument_list|,
name|c
operator|.
name|getStatus
argument_list|()
operator|.
name|getCode
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|side
argument_list|,
name|c
operator|.
name|getSide
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|message
argument_list|,
name|c
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|parentUuid
argument_list|,
name|c
operator|.
name|getParentUuid
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|range
argument_list|,
name|c
operator|.
name|getRange
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|revId
argument_list|,
name|c
operator|.
name|getRevId
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|tag
argument_list|,
name|c
operator|.
name|getTag
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|unresolved
argument_list|,
name|c
operator|.
name|getUnresolved
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|key
operator|.
name|hashCode
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"PatchLineComment{"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"key="
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"lineNbr="
argument_list|)
operator|.
name|append
argument_list|(
name|lineNbr
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"author="
argument_list|)
operator|.
name|append
argument_list|(
name|author
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"realAuthor="
argument_list|)
operator|.
name|append
argument_list|(
name|realAuthor
operator|!=
literal|null
condition|?
name|realAuthor
operator|.
name|get
argument_list|()
else|:
literal|""
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"writtenOn="
argument_list|)
operator|.
name|append
argument_list|(
name|writtenOn
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"status="
argument_list|)
operator|.
name|append
argument_list|(
name|status
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"side="
argument_list|)
operator|.
name|append
argument_list|(
name|side
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"message="
argument_list|)
operator|.
name|append
argument_list|(
name|Objects
operator|.
name|toString
argument_list|(
name|message
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"parentUuid="
argument_list|)
operator|.
name|append
argument_list|(
name|Objects
operator|.
name|toString
argument_list|(
name|parentUuid
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"range="
argument_list|)
operator|.
name|append
argument_list|(
name|Objects
operator|.
name|toString
argument_list|(
name|range
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"revId="
argument_list|)
operator|.
name|append
argument_list|(
name|revId
operator|!=
literal|null
condition|?
name|revId
operator|.
name|get
argument_list|()
else|:
literal|""
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"tag="
argument_list|)
operator|.
name|append
argument_list|(
name|Objects
operator|.
name|toString
argument_list|(
name|tag
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"unresolved="
argument_list|)
operator|.
name|append
argument_list|(
name|unresolved
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

