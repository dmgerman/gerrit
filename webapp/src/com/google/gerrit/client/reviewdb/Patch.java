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
comment|/** Number of published comments on this patch. */
annotation|@
name|Column
DECL|field|nbrComments
specifier|protected
name|int
name|nbrComments
decl_stmt|;
comment|/** Content which expands out to the old version of the file. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|preImage
specifier|protected
name|DeltaContent
operator|.
name|Key
name|preImage
decl_stmt|;
comment|/** Unified style diff between {@link #preImage} and {@link #postImage}. */
annotation|@
name|Column
DECL|field|patch
specifier|protected
name|DeltaContent
operator|.
name|Key
name|patch
decl_stmt|;
comment|/**    * Content which expands out to the new version of the file.    *<p>    * Note that in many cases this is identical to {@link #patch}, as very often    * the patch itself can be used to reconstruct the postImage.    */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|postImage
specifier|protected
name|DeltaContent
operator|.
name|Key
name|postImage
decl_stmt|;
DECL|method|Patch ()
specifier|protected
name|Patch
parameter_list|()
block|{   }
DECL|method|Patch (final Patch.Id newId, final ChangeType type)
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
name|type
parameter_list|)
block|{
name|key
operator|=
name|newId
expr_stmt|;
name|setChangeType
argument_list|(
name|type
argument_list|)
expr_stmt|;
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
DECL|method|getPreImage ()
specifier|public
name|DeltaContent
operator|.
name|Key
name|getPreImage
parameter_list|()
block|{
return|return
name|preImage
return|;
block|}
DECL|method|setPreImage (final DeltaContent.Key p)
specifier|public
name|void
name|setPreImage
parameter_list|(
specifier|final
name|DeltaContent
operator|.
name|Key
name|p
parameter_list|)
block|{
name|preImage
operator|=
name|p
expr_stmt|;
block|}
DECL|method|getPatch ()
specifier|public
name|DeltaContent
operator|.
name|Key
name|getPatch
parameter_list|()
block|{
return|return
name|patch
return|;
block|}
DECL|method|setPatch (final DeltaContent.Key p)
specifier|public
name|void
name|setPatch
parameter_list|(
specifier|final
name|DeltaContent
operator|.
name|Key
name|p
parameter_list|)
block|{
name|patch
operator|=
name|p
expr_stmt|;
block|}
DECL|method|getPostImage ()
specifier|public
name|DeltaContent
operator|.
name|Key
name|getPostImage
parameter_list|()
block|{
return|return
name|postImage
return|;
block|}
DECL|method|setPostImage (final DeltaContent.Key p)
specifier|public
name|void
name|setPostImage
parameter_list|(
specifier|final
name|DeltaContent
operator|.
name|Key
name|p
parameter_list|)
block|{
name|postImage
operator|=
name|p
expr_stmt|;
block|}
block|}
end_class

end_unit

