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
comment|/** Ancestors of a {@link PatchSet} that the PatchSet depends upon. */
end_comment

begin_class
DECL|class|PatchSetAncestor
specifier|public
specifier|final
class|class
name|PatchSetAncestor
block|{
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|IntKey
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
DECL|field|position
specifier|protected
name|int
name|position
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
DECL|method|Id (final PatchSet.Id psId, final int pos)
specifier|public
name|Id
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
specifier|final
name|int
name|pos
parameter_list|)
block|{
name|this
operator|.
name|patchSetId
operator|=
name|psId
expr_stmt|;
name|this
operator|.
name|position
operator|=
name|pos
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
name|int
name|get
parameter_list|()
block|{
return|return
name|position
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
name|position
operator|=
name|newValue
expr_stmt|;
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
annotation|@
name|Column
DECL|field|ancestorRevision
specifier|protected
name|RevId
name|ancestorRevision
decl_stmt|;
DECL|method|PatchSetAncestor ()
specifier|protected
name|PatchSetAncestor
parameter_list|()
block|{   }
DECL|method|PatchSetAncestor (final PatchSetAncestor.Id k)
specifier|public
name|PatchSetAncestor
parameter_list|(
specifier|final
name|PatchSetAncestor
operator|.
name|Id
name|k
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|PatchSetAncestor
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getPatchSet ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getPatchSet
parameter_list|()
block|{
return|return
name|key
operator|.
name|patchSetId
return|;
block|}
DECL|method|getPosition ()
specifier|public
name|int
name|getPosition
parameter_list|()
block|{
return|return
name|key
operator|.
name|position
return|;
block|}
DECL|method|getAncestorRevision ()
specifier|public
name|RevId
name|getAncestorRevision
parameter_list|()
block|{
return|return
name|ancestorRevision
return|;
block|}
DECL|method|setAncestorRevision (final RevId id)
specifier|public
name|void
name|setAncestorRevision
parameter_list|(
specifier|final
name|RevId
name|id
parameter_list|)
block|{
name|ancestorRevision
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

