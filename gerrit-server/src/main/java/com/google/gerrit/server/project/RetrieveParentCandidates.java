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
comment|// limitations under the License
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
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
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
name|Set
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

begin_class
DECL|class|RetrieveParentCandidates
specifier|public
class|class
name|RetrieveParentCandidates
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|RetrieveParentCandidates
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|allProject
specifier|private
specifier|final
name|AllProjectsName
name|allProject
decl_stmt|;
annotation|@
name|Inject
DECL|method|RetrieveParentCandidates (final ProjectControl.Factory projectControlFactory, final ProjectCache projectCache, final AllProjectsName allProject)
name|RetrieveParentCandidates
parameter_list|(
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
specifier|final
name|ProjectCache
name|projectCache
parameter_list|,
specifier|final
name|AllProjectsName
name|allProject
parameter_list|)
block|{
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|allProject
operator|=
name|allProject
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|get
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|r
init|=
operator|new
name|TreeSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projectCache
operator|.
name|all
argument_list|()
control|)
block|{
try|try
block|{
specifier|final
name|ProjectControl
name|project
init|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|p
argument_list|)
decl_stmt|;
specifier|final
name|Project
operator|.
name|NameKey
name|parent
init|=
name|project
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|ProjectControl
name|c
init|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|isVisible
argument_list|()
operator|||
name|c
operator|.
name|isOwner
argument_list|()
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
continue|continue;
block|}
block|}
name|r
operator|.
name|add
argument_list|(
name|allProject
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

