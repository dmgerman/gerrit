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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|client
operator|.
name|changes
operator|.
name|ChangeScreen
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchSet
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchSetInfo
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|OrmException
import|;
end_import

begin_comment
comment|/** Detail necessary to display{@link ChangeScreen}. */
end_comment

begin_class
DECL|class|ChangeDetail
specifier|public
class|class
name|ChangeDetail
block|{
DECL|field|change
specifier|protected
name|Change
name|change
decl_stmt|;
DECL|field|currentPatchSet
specifier|protected
name|PatchSet
name|currentPatchSet
decl_stmt|;
DECL|field|currentPatchSetInfo
specifier|protected
name|PatchSetInfo
name|currentPatchSetInfo
decl_stmt|;
DECL|method|ChangeDetail ()
specifier|public
name|ChangeDetail
parameter_list|()
block|{   }
DECL|method|load (final Change c, final ReviewDb db)
specifier|public
name|void
name|load
parameter_list|(
specifier|final
name|Change
name|c
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
name|change
operator|=
name|c
expr_stmt|;
specifier|final
name|PatchSet
operator|.
name|Id
name|ps
init|=
name|change
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
if|if
condition|(
name|ps
operator|!=
literal|null
condition|)
block|{
name|currentPatchSet
operator|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|ps
argument_list|)
expr_stmt|;
name|currentPatchSetInfo
operator|=
name|db
operator|.
name|patchSetInfo
argument_list|()
operator|.
name|get
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getCurrentPatchSet ()
specifier|public
name|PatchSet
name|getCurrentPatchSet
parameter_list|()
block|{
return|return
name|currentPatchSet
return|;
block|}
DECL|method|getCurrentPatchSetInfo ()
specifier|public
name|PatchSetInfo
name|getCurrentPatchSetInfo
parameter_list|()
block|{
return|return
name|currentPatchSetInfo
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|currentPatchSetInfo
operator|!=
literal|null
condition|?
name|currentPatchSetInfo
operator|.
name|getMessage
argument_list|()
else|:
literal|""
return|;
block|}
block|}
end_class

end_unit

