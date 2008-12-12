begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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

begin_comment
comment|/** A single modified file in a {@link PatchSet}. */
end_comment

begin_class
DECL|class|Patch
specifier|public
specifier|final
class|class
name|Patch
block|{
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|StringKey
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
block|{
annotation|@
name|Column
argument_list|(
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|patchSetId
specifier|protected
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
annotation|@
name|Column
DECL|field|fileName
specifier|protected
name|String
name|fileName
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{
name|patchSetId
operator|=
operator|new
name|PatchSet
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Id (final PatchSet.Id ps, final String name)
specifier|public
name|Id
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|ps
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|patchSetId
operator|=
name|ps
expr_stmt|;
name|this
operator|.
name|fileName
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|patchSetId
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
name|fileName
return|;
block|}
block|}
DECL|enum|ChangeType
specifier|public
specifier|static
enum|enum
name|ChangeType
block|{
DECL|enumConstant|ADD
name|ADD
argument_list|(
literal|'A'
argument_list|)
block|,
DECL|enumConstant|MODIFIED
name|MODIFIED
argument_list|(
literal|'M'
argument_list|)
block|,
DECL|enumConstant|DELETED
name|DELETED
argument_list|(
literal|'D'
argument_list|)
block|,
DECL|enumConstant|RENAMED
name|RENAMED
argument_list|(
literal|'R'
argument_list|)
block|,
DECL|enumConstant|COPIED
name|COPIED
argument_list|(
literal|'C'
argument_list|)
block|;
DECL|field|code
specifier|private
specifier|final
name|char
name|code
decl_stmt|;
DECL|method|ChangeType (final char c)
specifier|private
name|ChangeType
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
name|ChangeType
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
name|ChangeType
name|s
range|:
name|ChangeType
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
DECL|enum|PatchType
specifier|public
specifier|static
enum|enum
name|PatchType
block|{
DECL|enumConstant|UNIFIED
name|UNIFIED
argument_list|(
literal|'U'
argument_list|)
block|,
DECL|enumConstant|BINARY
name|BINARY
argument_list|(
literal|'B'
argument_list|)
block|,
DECL|enumConstant|N_WAY
name|N_WAY
argument_list|(
literal|'N'
argument_list|)
block|;
DECL|field|code
specifier|private
specifier|final
name|char
name|code
decl_stmt|;
DECL|method|PatchType (final char c)
specifier|private
name|PatchType
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
name|PatchType
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
name|PatchType
name|s
range|:
name|PatchType
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
annotation|@
name|Column
argument_list|(
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Id
name|key
decl_stmt|;
comment|/** What sort of change is this to the path; see {@link ChangeType}. */
annotation|@
name|Column
DECL|field|changeType
specifier|protected
name|char
name|changeType
decl_stmt|;
comment|/** What type of patch is this; see {@link PatchType}. */
annotation|@
name|Column
DECL|field|patchType
specifier|protected
name|char
name|patchType
decl_stmt|;
comment|/** Number of published comments on this patch. */
annotation|@
name|Column
DECL|field|nbrComments
specifier|protected
name|int
name|nbrComments
decl_stmt|;
comment|/**    * Original if {@link #changeType} is {@link ChangeType#COPIED} or    * {@link ChangeType#RENAMED}.    */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|sourceFileName
specifier|protected
name|String
name|sourceFileName
decl_stmt|;
DECL|method|Patch ()
specifier|protected
name|Patch
parameter_list|()
block|{   }
DECL|method|Patch (final Patch.Id newId, final ChangeType ct, final PatchType pt)
specifier|public
name|Patch
parameter_list|(
specifier|final
name|Patch
operator|.
name|Id
name|newId
parameter_list|,
specifier|final
name|ChangeType
name|ct
parameter_list|,
specifier|final
name|PatchType
name|pt
parameter_list|)
block|{
name|key
operator|=
name|newId
expr_stmt|;
name|setChangeType
argument_list|(
name|ct
argument_list|)
expr_stmt|;
name|setPatchType
argument_list|(
name|pt
argument_list|)
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|Patch
operator|.
name|Id
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getCommentCount ()
specifier|public
name|int
name|getCommentCount
parameter_list|()
block|{
return|return
name|nbrComments
return|;
block|}
DECL|method|getChangeType ()
specifier|public
name|ChangeType
name|getChangeType
parameter_list|()
block|{
return|return
name|ChangeType
operator|.
name|forCode
argument_list|(
name|changeType
argument_list|)
return|;
block|}
DECL|method|setChangeType (final ChangeType type)
specifier|public
name|void
name|setChangeType
parameter_list|(
specifier|final
name|ChangeType
name|type
parameter_list|)
block|{
name|changeType
operator|=
name|type
operator|.
name|getCode
argument_list|()
expr_stmt|;
block|}
DECL|method|getPatchType ()
specifier|public
name|PatchType
name|getPatchType
parameter_list|()
block|{
return|return
name|PatchType
operator|.
name|forCode
argument_list|(
name|patchType
argument_list|)
return|;
block|}
DECL|method|setPatchType (final PatchType type)
specifier|public
name|void
name|setPatchType
parameter_list|(
specifier|final
name|PatchType
name|type
parameter_list|)
block|{
name|patchType
operator|=
name|type
operator|.
name|getCode
argument_list|()
expr_stmt|;
block|}
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|key
operator|.
name|fileName
return|;
block|}
DECL|method|getSourceFileName ()
specifier|public
name|String
name|getSourceFileName
parameter_list|()
block|{
return|return
name|sourceFileName
return|;
block|}
DECL|method|setSourceFileName (final String n)
specifier|public
name|void
name|setSourceFileName
parameter_list|(
specifier|final
name|String
name|n
parameter_list|)
block|{
name|sourceFileName
operator|=
name|n
expr_stmt|;
block|}
block|}
end_class

end_unit

