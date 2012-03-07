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
name|CompoundKey
import|;
end_import

begin_comment
comment|/**  * An entity that keeps track of what user reviewed what patches.  */
end_comment

begin_class
DECL|class|AccountPatchReview
specifier|public
specifier|final
class|class
name|AccountPatchReview
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|CompoundKey
argument_list|<
name|Account
operator|.
name|Id
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
argument_list|)
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
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
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|accountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|()
expr_stmt|;
name|patchKey
operator|=
operator|new
name|Patch
operator|.
name|Key
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Patch.Key p, final Account.Id a)
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
name|Account
operator|.
name|Id
name|a
parameter_list|)
block|{
name|patchKey
operator|=
name|p
expr_stmt|;
name|accountId
operator|=
name|a
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Account
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
DECL|method|getPatchKey ()
specifier|public
name|Patch
operator|.
name|Key
name|getPatchKey
parameter_list|()
block|{
return|return
name|patchKey
return|;
block|}
annotation|@
name|Override
DECL|method|members ()
specifier|public
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
name|members
parameter_list|()
block|{
return|return
operator|new
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|patchKey
block|}
empty_stmt|;
block|}
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
DECL|field|key
specifier|protected
name|AccountPatchReview
operator|.
name|Key
name|key
decl_stmt|;
DECL|method|AccountPatchReview ()
specifier|protected
name|AccountPatchReview
parameter_list|()
block|{   }
DECL|method|AccountPatchReview (final Patch.Key k, final Account.Id a)
specifier|public
name|AccountPatchReview
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|k
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|a
parameter_list|)
block|{
name|key
operator|=
operator|new
name|AccountPatchReview
operator|.
name|Key
argument_list|(
name|k
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|AccountPatchReview
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
block|}
end_class

end_unit

