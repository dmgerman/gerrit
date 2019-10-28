begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.api.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|projects
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|ApiUtil
operator|.
name|asRestApiException
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
name|api
operator|.
name|projects
operator|.
name|ChildProjectApi
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
name|common
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
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|ChildProjectResource
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
name|restapi
operator|.
name|project
operator|.
name|GetChildProject
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
DECL|class|ChildProjectApiImpl
specifier|public
class|class
name|ChildProjectApiImpl
implements|implements
name|ChildProjectApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (ChildProjectResource rsrc)
name|ChildProjectApiImpl
name|create
parameter_list|(
name|ChildProjectResource
name|rsrc
parameter_list|)
function_decl|;
block|}
DECL|field|getChildProject
specifier|private
specifier|final
name|GetChildProject
name|getChildProject
decl_stmt|;
DECL|field|rsrc
specifier|private
specifier|final
name|ChildProjectResource
name|rsrc
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChildProjectApiImpl (GetChildProject getChildProject, @Assisted ChildProjectResource rsrc)
name|ChildProjectApiImpl
parameter_list|(
name|GetChildProject
name|getChildProject
parameter_list|,
annotation|@
name|Assisted
name|ChildProjectResource
name|rsrc
parameter_list|)
block|{
name|this
operator|.
name|getChildProject
operator|=
name|getChildProject
expr_stmt|;
name|this
operator|.
name|rsrc
operator|=
name|rsrc
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ProjectInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|get
argument_list|(
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (boolean recursive)
specifier|public
name|ProjectInfo
name|get
parameter_list|(
name|boolean
name|recursive
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|getChildProject
operator|.
name|setRecursive
argument_list|(
name|recursive
argument_list|)
expr_stmt|;
return|return
name|getChildProject
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot get child project"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

