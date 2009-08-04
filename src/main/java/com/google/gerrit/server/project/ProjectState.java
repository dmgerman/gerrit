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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|reviewdb
operator|.
name|AccountGroup
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
name|ApprovalCategory
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
name|Project
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
name|ProjectRight
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
name|gerrit
operator|.
name|server
operator|.
name|CurrentUser
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Cached information on a project. */
end_comment

begin_class
DECL|class|ProjectState
specifier|public
class|class
name|ProjectState
block|{
DECL|field|projectCache
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
name|project
decl_stmt|;
DECL|field|rights
specifier|private
specifier|final
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|rights
decl_stmt|;
DECL|field|owners
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|owners
decl_stmt|;
DECL|method|ProjectState (final ProjectCache pc, final ReviewDb db, final Project p)
specifier|protected
name|ProjectState
parameter_list|(
specifier|final
name|ProjectCache
name|pc
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Project
name|p
parameter_list|)
throws|throws
name|OrmException
block|{
name|projectCache
operator|=
name|pc
expr_stmt|;
name|project
operator|=
name|p
expr_stmt|;
name|rights
operator|=
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|db
operator|.
name|projectRights
argument_list|()
operator|.
name|byProject
argument_list|(
name|project
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groups
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ProjectRight
name|right
range|:
name|rights
control|)
block|{
if|if
condition|(
name|ApprovalCategory
operator|.
name|OWN
operator|.
name|equals
argument_list|(
name|right
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
operator|&&
name|right
operator|.
name|getMaxValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|groups
operator|.
name|add
argument_list|(
name|right
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|owners
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|groups
argument_list|)
expr_stmt|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
DECL|method|getRights ()
specifier|public
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|getRights
parameter_list|()
block|{
return|return
name|rights
return|;
block|}
DECL|method|getOwners ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getOwners
parameter_list|()
block|{
return|return
name|owners
return|;
block|}
DECL|method|controlForAnonymousUser ()
specifier|public
name|ProjectControl
name|controlForAnonymousUser
parameter_list|()
block|{
return|return
name|controlFor
argument_list|(
name|projectCache
operator|.
name|anonymousUser
argument_list|)
return|;
block|}
DECL|method|controlFor (final CurrentUser user)
specifier|public
name|ProjectControl
name|controlFor
parameter_list|(
specifier|final
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
operator|new
name|ProjectControl
argument_list|(
name|user
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

