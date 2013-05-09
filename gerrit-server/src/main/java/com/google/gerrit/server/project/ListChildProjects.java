begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|project
operator|.
name|ProjectJson
operator|.
name|ProjectInfo
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|ListChildProjects
specifier|public
class|class
name|ListChildProjects
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|allProjects
specifier|private
specifier|final
name|AllProjectsName
name|allProjects
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ProjectJson
name|json
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListChildProjects (ProjectCache projectCache, AllProjectsName allProjects, ProjectJson json)
name|ListChildProjects
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|,
name|AllProjectsName
name|allProjects
parameter_list|,
name|ProjectJson
name|json
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|allProjects
operator|=
name|allProjects
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc)
specifier|public
name|List
argument_list|<
name|ProjectInfo
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|)
block|{
name|List
argument_list|<
name|ProjectInfo
argument_list|>
name|childProjects
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|projectName
range|:
name|projectCache
operator|.
name|all
argument_list|()
control|)
block|{
name|ProjectState
name|e
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
comment|// If we can't get it from the cache, pretend it's not present.
continue|continue;
block|}
if|if
condition|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|(
name|allProjects
argument_list|)
argument_list|)
condition|)
block|{
name|childProjects
operator|.
name|add
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|e
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|childProjects
return|;
block|}
block|}
end_class

end_unit

