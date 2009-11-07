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

begin_comment
comment|/** Line of development within a {@link Project}. */
end_comment

begin_class
DECL|class|Branch
specifier|public
specifier|final
class|class
name|Branch
block|{
DECL|field|R_HEADS
specifier|public
specifier|static
specifier|final
name|String
name|R_HEADS
init|=
literal|"refs/heads/"
decl_stmt|;
DECL|field|R_REFS
specifier|public
specifier|static
specifier|final
name|String
name|R_REFS
init|=
literal|"refs/"
decl_stmt|;
comment|/** Branch name key */
DECL|class|NameKey
specifier|public
specifier|static
class|class
name|NameKey
extends|extends
name|StringKey
argument_list|<
name|Project
operator|.
name|NameKey
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
DECL|field|projectName
specifier|protected
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
annotation|@
name|Column
DECL|field|branchName
specifier|protected
name|String
name|branchName
decl_stmt|;
DECL|method|NameKey ()
specifier|protected
name|NameKey
parameter_list|()
block|{
name|projectName
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|()
expr_stmt|;
block|}
DECL|method|NameKey (final Project.NameKey proj, final String n)
specifier|public
name|NameKey
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
specifier|final
name|String
name|n
parameter_list|)
block|{
name|projectName
operator|=
name|proj
expr_stmt|;
name|branchName
operator|=
name|n
expr_stmt|;
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
name|branchName
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
name|branchName
operator|=
name|newValue
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getParentKey
parameter_list|()
block|{
return|return
name|projectName
return|;
block|}
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
specifier|final
name|String
name|n
init|=
name|get
argument_list|()
decl_stmt|;
comment|// Git style branches will tend to start with "refs/heads/".
comment|//
if|if
condition|(
name|n
operator|.
name|startsWith
argument_list|(
name|R_HEADS
argument_list|)
condition|)
block|{
return|return
name|n
operator|.
name|substring
argument_list|(
name|R_HEADS
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|n
return|;
block|}
block|}
DECL|field|name
specifier|protected
name|NameKey
name|name
decl_stmt|;
DECL|field|revision
specifier|protected
name|RevId
name|revision
decl_stmt|;
DECL|method|Branch ()
specifier|protected
name|Branch
parameter_list|()
block|{   }
DECL|method|Branch (final Branch.NameKey newName)
specifier|public
name|Branch
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|newName
parameter_list|)
block|{
name|name
operator|=
name|newName
expr_stmt|;
block|}
DECL|method|getNameKey ()
specifier|public
name|Branch
operator|.
name|NameKey
name|getNameKey
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
name|name
operator|.
name|getShortName
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
DECL|method|setRevision (final RevId id)
specifier|public
name|void
name|setRevision
parameter_list|(
specifier|final
name|RevId
name|id
parameter_list|)
block|{
name|revision
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

