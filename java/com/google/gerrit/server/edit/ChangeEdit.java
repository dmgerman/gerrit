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
DECL|package|com.google.gerrit.server.edit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|edit
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|entities
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
name|entities
operator|.
name|PatchSet
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
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_comment
comment|/**  * A single user's edit for a change.  *  *<p>There is max. one edit per user per change. Edits are stored on refs:  * refs/users/UU/UUUU/edit-CCCC/P where UU/UUUU is sharded representation of user account, CCCC is  * change number and P is the patch set number it is based on.  */
end_comment

begin_class
DECL|class|ChangeEdit
specifier|public
class|class
name|ChangeEdit
block|{
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|editRefName
specifier|private
specifier|final
name|String
name|editRefName
decl_stmt|;
DECL|field|editCommit
specifier|private
specifier|final
name|RevCommit
name|editCommit
decl_stmt|;
DECL|field|basePatchSet
specifier|private
specifier|final
name|PatchSet
name|basePatchSet
decl_stmt|;
DECL|method|ChangeEdit ( Change change, String editRefName, RevCommit editCommit, PatchSet basePatchSet)
specifier|public
name|ChangeEdit
parameter_list|(
name|Change
name|change
parameter_list|,
name|String
name|editRefName
parameter_list|,
name|RevCommit
name|editCommit
parameter_list|,
name|PatchSet
name|basePatchSet
parameter_list|)
block|{
name|this
operator|.
name|change
operator|=
name|requireNonNull
argument_list|(
name|change
argument_list|)
expr_stmt|;
name|this
operator|.
name|editRefName
operator|=
name|requireNonNull
argument_list|(
name|editRefName
argument_list|)
expr_stmt|;
name|this
operator|.
name|editCommit
operator|=
name|requireNonNull
argument_list|(
name|editCommit
argument_list|)
expr_stmt|;
name|this
operator|.
name|basePatchSet
operator|=
name|requireNonNull
argument_list|(
name|basePatchSet
argument_list|)
expr_stmt|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
return|;
block|}
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|editRefName
return|;
block|}
DECL|method|getEditCommit ()
specifier|public
name|RevCommit
name|getEditCommit
parameter_list|()
block|{
return|return
name|editCommit
return|;
block|}
DECL|method|getBasePatchSet ()
specifier|public
name|PatchSet
name|getBasePatchSet
parameter_list|()
block|{
return|return
name|basePatchSet
return|;
block|}
block|}
end_class

end_unit

