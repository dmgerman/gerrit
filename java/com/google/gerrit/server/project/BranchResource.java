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
name|gerrit
operator|.
name|entities
operator|.
name|BranchNameKey
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
name|RestView
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
name|inject
operator|.
name|TypeLiteral
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

begin_class
DECL|class|BranchResource
specifier|public
class|class
name|BranchResource
extends|extends
name|RefResource
block|{
DECL|field|BRANCH_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|BranchResource
argument_list|>
argument_list|>
name|BRANCH_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|BranchResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|refName
specifier|private
specifier|final
name|String
name|refName
decl_stmt|;
DECL|field|revision
specifier|private
specifier|final
name|String
name|revision
decl_stmt|;
DECL|method|BranchResource (ProjectState projectState, CurrentUser user, Ref ref)
specifier|public
name|BranchResource
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Ref
name|ref
parameter_list|)
block|{
name|super
argument_list|(
name|projectState
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|this
operator|.
name|refName
operator|=
name|ref
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|?
name|ref
operator|.
name|getObjectId
argument_list|()
operator|.
name|name
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
DECL|method|getBranchKey ()
specifier|public
name|BranchNameKey
name|getBranchKey
parameter_list|()
block|{
return|return
name|BranchNameKey
operator|.
name|create
argument_list|(
name|getNameKey
argument_list|()
argument_list|,
name|refName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|refName
return|;
block|}
annotation|@
name|Override
DECL|method|getRevision ()
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
block|}
end_class

end_unit

