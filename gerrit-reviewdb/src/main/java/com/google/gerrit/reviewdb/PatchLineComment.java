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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
package|;
end_package

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

begin_comment
comment|/** A comment left by a user on a specific line of a {@link Patch}. */
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
DECL|method|Key (final Patch.Key p, final String uuid)
specifier|public
name|Key
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|p
parameter_list|,
specifier|final
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
specifier|protected
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
block|}
DECL|field|STATUS_DRAFT
specifier|protected
specifier|static
specifier|final
name|char
name|STATUS_DRAFT
init|=
literal|'d'
decl_stmt|;
DECL|field|STATUS_PUBLISHED
specifier|protected
specifier|static
specifier|final
name|char
name|STATUS_PUBLISHED
init|=
literal|'P'
decl_stmt|;
DECL|enum|Status
specifier|public
specifier|static
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
DECL|method|Status (final char c)
specifier|private
name|Status
parameter_list|(
specifier|final
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
DECL|method|forCode (final char c)
specifier|public
specifier|static
name|Status
name|forCode
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
for|for
control|(
specifier|final
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
comment|/**    * The parent of this comment, or null if this is the first comment on this    * line    */
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

begin_constructor
DECL|method|PatchLineComment ()
specifier|protected
name|PatchLineComment
parameter_list|()
block|{   }
end_constructor

begin_constructor
DECL|method|PatchLineComment (final PatchLineComment.Key id, final int line, final Account.Id a, String parentUuid)
specifier|public
name|PatchLineComment
parameter_list|(
specifier|final
name|PatchLineComment
operator|.
name|Key
name|id
parameter_list|,
specifier|final
name|int
name|line
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|a
parameter_list|,
name|String
name|parentUuid
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
name|this
operator|.
name|parentUuid
operator|=
name|parentUuid
expr_stmt|;
name|setStatus
argument_list|(
name|Status
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
name|updated
argument_list|()
expr_stmt|;
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
DECL|method|setStatus (final Status s)
specifier|public
name|void
name|setStatus
parameter_list|(
specifier|final
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
DECL|method|setSide (final short s)
specifier|public
name|void
name|setSide
parameter_list|(
specifier|final
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
DECL|method|setMessage (final String s)
specifier|public
name|void
name|setMessage
parameter_list|(
specifier|final
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
DECL|method|updated ()
specifier|public
name|void
name|updated
parameter_list|()
block|{
name|writtenOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
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

unit|}
end_unit

