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
name|extensions
operator|.
name|client
operator|.
name|InheritableBoolean
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
name|client
operator|.
name|SubmitType
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
name|reviewdb
operator|.
name|client
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
name|List
import|;
end_import

begin_class
DECL|class|CreateProjectArgs
specifier|public
class|class
name|CreateProjectArgs
block|{
DECL|field|projectName
specifier|private
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
DECL|field|ownerIds
specifier|public
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ownerIds
decl_stmt|;
DECL|field|newParent
specifier|public
name|Project
operator|.
name|NameKey
name|newParent
decl_stmt|;
DECL|field|projectDescription
specifier|public
name|String
name|projectDescription
decl_stmt|;
DECL|field|submitType
specifier|public
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|contributorAgreements
specifier|public
name|InheritableBoolean
name|contributorAgreements
decl_stmt|;
DECL|field|signedOffBy
specifier|public
name|InheritableBoolean
name|signedOffBy
decl_stmt|;
DECL|field|permissionsOnly
specifier|public
name|boolean
name|permissionsOnly
decl_stmt|;
DECL|field|branch
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|branch
decl_stmt|;
DECL|field|contentMerge
specifier|public
name|InheritableBoolean
name|contentMerge
decl_stmt|;
DECL|field|newChangeForAllNotInTarget
specifier|public
name|InheritableBoolean
name|newChangeForAllNotInTarget
decl_stmt|;
DECL|field|changeIdRequired
specifier|public
name|InheritableBoolean
name|changeIdRequired
decl_stmt|;
DECL|field|createEmptyCommit
specifier|public
name|boolean
name|createEmptyCommit
decl_stmt|;
DECL|field|maxObjectSizeLimit
specifier|public
name|String
name|maxObjectSizeLimit
decl_stmt|;
DECL|method|CreateProjectArgs ()
specifier|public
name|CreateProjectArgs
parameter_list|()
block|{
name|contributorAgreements
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|signedOffBy
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|contentMerge
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|changeIdRequired
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|newChangeForAllNotInTarget
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|submitType
operator|=
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
expr_stmt|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|projectName
return|;
block|}
DECL|method|getProjectName ()
specifier|public
name|String
name|getProjectName
parameter_list|()
block|{
return|return
name|projectName
operator|!=
literal|null
condition|?
name|projectName
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|setProjectName (String n)
specifier|public
name|void
name|setProjectName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|projectName
operator|=
name|n
operator|!=
literal|null
condition|?
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|n
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
DECL|method|setProjectName (Project.NameKey n)
specifier|public
name|void
name|setProjectName
parameter_list|(
name|Project
operator|.
name|NameKey
name|n
parameter_list|)
block|{
name|projectName
operator|=
name|n
expr_stmt|;
block|}
block|}
end_class

end_unit
