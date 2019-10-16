begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.index.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|project
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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
DECL|class|ProjectData
specifier|public
class|class
name|ProjectData
block|{
DECL|field|project
specifier|private
specifier|final
name|Project
name|project
decl_stmt|;
DECL|field|parent
specifier|private
specifier|final
name|Optional
argument_list|<
name|ProjectData
argument_list|>
name|parent
decl_stmt|;
DECL|method|ProjectData (Project project, Optional<ProjectData> parent)
specifier|public
name|ProjectData
parameter_list|(
name|Project
name|project
parameter_list|,
name|Optional
argument_list|<
name|ProjectData
argument_list|>
name|parent
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
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
DECL|method|getParent ()
specifier|public
name|Optional
argument_list|<
name|ProjectData
argument_list|>
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|/** Returns all {@link ProjectData} in the hierarchy starting with the current one. */
DECL|method|tree ()
specifier|public
name|ImmutableList
argument_list|<
name|ProjectData
argument_list|>
name|tree
parameter_list|()
block|{
name|List
argument_list|<
name|ProjectData
argument_list|>
name|parents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|ProjectData
argument_list|>
name|curr
init|=
name|Optional
operator|.
name|of
argument_list|(
name|this
argument_list|)
decl_stmt|;
while|while
condition|(
name|curr
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|parents
operator|.
name|add
argument_list|(
name|curr
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|curr
operator|=
name|curr
operator|.
name|get
argument_list|()
operator|.
name|parent
expr_stmt|;
block|}
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|parents
argument_list|)
return|;
block|}
DECL|method|getParentNames ()
specifier|public
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|getParentNames
parameter_list|()
block|{
return|return
name|tree
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|skip
argument_list|(
literal|1
argument_list|)
operator|.
name|map
argument_list|(
name|p
lambda|->
name|p
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|MoreObjects
operator|.
name|ToStringHelper
name|h
init|=
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|h
operator|.
name|addValue
argument_list|(
name|project
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|h
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

