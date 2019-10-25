begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|LabelApi
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
name|LabelDefinitionInfo
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
name|LabelDefinitionInput
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
name|LabelResource
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
name|GetLabel
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
name|SetLabel
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
DECL|class|LabelApiImpl
specifier|public
class|class
name|LabelApiImpl
implements|implements
name|LabelApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (LabelResource rsrc)
name|LabelApiImpl
name|create
parameter_list|(
name|LabelResource
name|rsrc
parameter_list|)
function_decl|;
block|}
DECL|field|getLabel
specifier|private
specifier|final
name|GetLabel
name|getLabel
decl_stmt|;
DECL|field|setLabel
specifier|private
specifier|final
name|SetLabel
name|setLabel
decl_stmt|;
DECL|field|rsrc
specifier|private
specifier|final
name|LabelResource
name|rsrc
decl_stmt|;
annotation|@
name|Inject
DECL|method|LabelApiImpl (GetLabel getLabel, SetLabel setLabel, @Assisted LabelResource rsrc)
name|LabelApiImpl
parameter_list|(
name|GetLabel
name|getLabel
parameter_list|,
name|SetLabel
name|setLabel
parameter_list|,
annotation|@
name|Assisted
name|LabelResource
name|rsrc
parameter_list|)
block|{
name|this
operator|.
name|getLabel
operator|=
name|getLabel
expr_stmt|;
name|this
operator|.
name|setLabel
operator|=
name|setLabel
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
name|LabelDefinitionInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|getLabel
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
literal|"Cannot get label"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|update (LabelDefinitionInput input)
specifier|public
name|LabelDefinitionInfo
name|update
parameter_list|(
name|LabelDefinitionInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|setLabel
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
name|input
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
literal|"Cannot update label"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

