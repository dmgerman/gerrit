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
name|IntKey
import|;
end_import

begin_comment
comment|/** A single revision of a {@link Change}. */
end_comment

begin_class
DECL|class|PatchSet
specifier|public
specifier|final
class|class
name|PatchSet
block|{
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|IntKey
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
block|{
annotation|@
name|Column
DECL|field|changeId
specifier|protected
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
annotation|@
name|Column
DECL|field|patchSetId
specifier|protected
name|int
name|patchSetId
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{
name|changeId
operator|=
operator|new
name|Change
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Id (final Change.Id change, final int id)
specifier|public
name|Id
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|change
parameter_list|,
specifier|final
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|patchSetId
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Change
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|patchSetId
return|;
block|}
annotation|@
name|Override
DECL|method|set (int newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|int
name|newValue
parameter_list|)
block|{
name|patchSetId
operator|=
name|newValue
expr_stmt|;
block|}
comment|/** Parse a PatchSet.Id out of a string representation. */
DECL|method|parse (final String str)
specifier|public
specifier|static
name|Id
name|parse
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
specifier|final
name|Id
name|r
init|=
operator|new
name|Id
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
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
DECL|field|id
specifier|protected
name|Id
name|id
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|revision
specifier|protected
name|RevId
name|revision
decl_stmt|;
DECL|method|PatchSet ()
specifier|protected
name|PatchSet
parameter_list|()
block|{   }
DECL|method|PatchSet (final PatchSet.Id k)
specifier|public
name|PatchSet
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|k
parameter_list|)
block|{
name|id
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|getPatchSetId ()
specifier|public
name|int
name|getPatchSetId
parameter_list|()
block|{
return|return
name|id
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getRevision ()
specifier|public
name|RevId
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
DECL|method|setRevision (final RevId i)
specifier|public
name|void
name|setRevision
parameter_list|(
specifier|final
name|RevId
name|i
parameter_list|)
block|{
name|revision
operator|=
name|i
expr_stmt|;
block|}
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|"refs/changes/"
argument_list|)
expr_stmt|;
specifier|final
name|int
name|changeId
init|=
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
specifier|final
name|int
name|m
init|=
name|changeId
operator|%
literal|100
decl_stmt|;
if|if
condition|(
name|m
operator|<
literal|10
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

