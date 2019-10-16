begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|entities
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
name|server
operator|.
name|config
operator|.
name|AllProjectsName
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
name|util
operator|.
name|TreeFormatter
operator|.
name|TreeNode
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/** Node of a Project in a tree formatted by {@link ListProjects}. */
end_comment

begin_class
DECL|class|ProjectNode
specifier|public
class|class
name|ProjectNode
implements|implements
name|TreeNode
implements|,
name|Comparable
argument_list|<
name|ProjectNode
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Project project, boolean isVisible)
name|ProjectNode
name|create
parameter_list|(
name|Project
name|project
parameter_list|,
name|boolean
name|isVisible
parameter_list|)
function_decl|;
block|}
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
name|project
decl_stmt|;
DECL|field|isVisible
specifier|private
specifier|final
name|boolean
name|isVisible
decl_stmt|;
DECL|field|children
specifier|private
specifier|final
name|SortedSet
argument_list|<
name|ProjectNode
argument_list|>
name|children
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectNode ( final AllProjectsName allProjectsName, @Assisted final Project project, @Assisted final boolean isVisible)
specifier|protected
name|ProjectNode
parameter_list|(
specifier|final
name|AllProjectsName
name|allProjectsName
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Project
name|project
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|boolean
name|isVisible
parameter_list|)
block|{
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|isVisible
operator|=
name|isVisible
expr_stmt|;
block|}
comment|/**    * Returns the project parent name.    *    * @return Project parent name, {@code null} for the 'All-Projects' root project    */
DECL|method|getParentName ()
name|Project
operator|.
name|NameKey
name|getParentName
parameter_list|()
block|{
return|return
name|project
operator|.
name|getParent
argument_list|(
name|allProjectsName
argument_list|)
return|;
block|}
DECL|method|isAllProjects ()
name|boolean
name|isAllProjects
parameter_list|()
block|{
return|return
name|allProjectsName
operator|.
name|equals
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getProject ()
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
annotation|@
name|Override
DECL|method|getDisplayName ()
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
return|return
name|project
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isVisible ()
specifier|public
name|boolean
name|isVisible
parameter_list|()
block|{
return|return
name|isVisible
return|;
block|}
annotation|@
name|Override
DECL|method|getChildren ()
specifier|public
name|SortedSet
argument_list|<
name|?
extends|extends
name|ProjectNode
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|children
return|;
block|}
DECL|method|addChild (ProjectNode child)
name|void
name|addChild
parameter_list|(
name|ProjectNode
name|child
parameter_list|)
block|{
name|children
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compareTo (ProjectNode o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|ProjectNode
name|o
parameter_list|)
block|{
return|return
name|project
operator|.
name|getNameKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o
operator|.
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

