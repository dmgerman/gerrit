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
name|TagApi
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
name|TagInfo
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
name|IdString
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
name|ListTags
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
name|ProjectResource
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
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|TagApiImpl
specifier|public
class|class
name|TagApiImpl
implements|implements
name|TagApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (ProjectResource project, String ref)
name|TagApiImpl
name|create
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
function_decl|;
block|}
DECL|field|listTags
specifier|private
specifier|final
name|ListTags
name|listTags
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|ProjectResource
name|project
decl_stmt|;
annotation|@
name|Inject
DECL|method|TagApiImpl (ListTags listTags, @Assisted ProjectResource project, @Assisted String ref)
name|TagApiImpl
parameter_list|(
name|ListTags
name|listTags
parameter_list|,
annotation|@
name|Assisted
name|ProjectResource
name|project
parameter_list|,
annotation|@
name|Assisted
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|listTags
operator|=
name|listTags
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|TagInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|listTags
operator|.
name|get
argument_list|(
name|project
argument_list|,
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|ref
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

