begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
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
name|common
operator|.
name|data
operator|.
name|ProjectDetail
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|project
operator|.
name|NoSuchProjectException
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
name|ProjectControl
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
name|ProjectState
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

begin_class
DECL|class|ProjectDetailFactory
class|class
name|ProjectDetailFactory
extends|extends
name|Handler
argument_list|<
name|ProjectDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (@ssisted Project.NameKey name)
name|ProjectDetailFactory
name|create
parameter_list|(
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
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
DECL|field|projectName
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectDetailFactory (final ProjectControl.Factory projectControlFactory, @Assisted final Project.NameKey name)
name|ProjectDetailFactory
parameter_list|(
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Project
operator|.
name|NameKey
name|name
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
name|projectName
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ProjectDetail
name|call
parameter_list|()
throws|throws
name|NoSuchProjectException
block|{
specifier|final
name|ProjectControl
name|pc
init|=
name|projectControlFactory
operator|.
name|validateFor
argument_list|(
name|projectName
argument_list|,
name|ProjectControl
operator|.
name|OWNER
operator||
name|ProjectControl
operator|.
name|VISIBLE
argument_list|)
decl_stmt|;
specifier|final
name|ProjectState
name|projectState
init|=
name|pc
operator|.
name|getProjectState
argument_list|()
decl_stmt|;
specifier|final
name|ProjectDetail
name|detail
init|=
operator|new
name|ProjectDetail
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setProject
argument_list|(
name|projectState
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|boolean
name|userIsOwner
init|=
name|pc
operator|.
name|isOwner
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|userIsOwnerAnyRef
init|=
name|pc
operator|.
name|isOwnerAnyRef
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setCanModifyAccess
argument_list|(
name|userIsOwnerAnyRef
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setCanModifyAgreements
argument_list|(
name|userIsOwner
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setCanModifyDescription
argument_list|(
name|userIsOwner
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setCanModifyMergeType
argument_list|(
name|userIsOwner
argument_list|)
expr_stmt|;
return|return
name|detail
return|;
block|}
block|}
end_class

end_unit

