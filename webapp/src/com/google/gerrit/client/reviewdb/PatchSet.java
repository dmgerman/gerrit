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
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|PatchSet (final PatchSet.Id k, final RevId rev)
specifier|public
name|PatchSet
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|k
parameter_list|,
specifier|final
name|RevId
name|rev
parameter_list|)
block|{
name|this
argument_list|(
name|k
argument_list|)
expr_stmt|;
name|revision
operator|=
name|rev
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getId ()
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|key
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
block|}
end_class

end_unit

