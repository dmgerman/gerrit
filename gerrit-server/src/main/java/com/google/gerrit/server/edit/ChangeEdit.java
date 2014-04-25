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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|RevId
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
name|IdentifiedUser
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
name|lib
operator|.
name|ObjectId
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
name|lib
operator|.
name|Ref
import|;
end_import

begin_comment
comment|/**  * A single user's edit for a change.  *<p>  * There is max. one edit per user per change. Edits are stored on refs:  * refs/users/UU/UUUU/edit-CCCC where UU/UUUU is sharded representation  * of user account and CCCC is change number.  */
end_comment

begin_class
DECL|class|ChangeEdit
specifier|public
class|class
name|ChangeEdit
block|{
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|Ref
name|ref
decl_stmt|;
DECL|method|ChangeEdit (IdentifiedUser user, Change change, Ref ref)
specifier|public
name|ChangeEdit
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|Change
name|change
parameter_list|,
name|Ref
name|ref
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|change
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|ref
operator|=
name|ref
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
DECL|method|getUser ()
specifier|public
name|IdentifiedUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getRef ()
specifier|public
name|Ref
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|getRevision ()
specifier|public
name|RevId
name|getRevision
parameter_list|()
block|{
return|return
operator|new
name|RevId
argument_list|(
name|ObjectId
operator|.
name|toString
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s/edit-%d"
argument_list|,
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
name|change
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

