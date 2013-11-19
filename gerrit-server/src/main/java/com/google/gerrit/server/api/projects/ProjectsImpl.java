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
name|ProjectApi
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
name|Projects
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
name|extensions
operator|.
name|restapi
operator|.
name|UnprocessableEntityException
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
name|ProjectsCollection
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
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|ProjectsImpl
class|class
name|ProjectsImpl
implements|implements
name|Projects
block|{
DECL|field|projects
specifier|private
specifier|final
name|ProjectsCollection
name|projects
decl_stmt|;
DECL|field|api
specifier|private
specifier|final
name|ProjectApiImpl
operator|.
name|Factory
name|api
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectsImpl (ProjectsCollection projects, ProjectApiImpl.Factory api)
name|ProjectsImpl
parameter_list|(
name|ProjectsCollection
name|projects
parameter_list|,
name|ProjectApiImpl
operator|.
name|Factory
name|api
parameter_list|)
block|{
name|this
operator|.
name|projects
operator|=
name|projects
expr_stmt|;
name|this
operator|.
name|api
operator|=
name|api
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|name (String name)
specifier|public
name|ProjectApi
name|name
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|api
operator|.
name|create
argument_list|(
name|projects
operator|.
name|parse
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot retrieve project"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnprocessableEntityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot retrieve project"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

