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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
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
name|Nullable
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
name|NotImplementedException
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

begin_interface
DECL|interface|LabelApi
specifier|public
interface|interface
name|LabelApi
block|{
DECL|method|create (LabelDefinitionInput input)
name|LabelApi
name|create
parameter_list|(
name|LabelDefinitionInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|get ()
name|LabelDefinitionInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|update (LabelDefinitionInput input)
name|LabelDefinitionInfo
name|update
parameter_list|(
name|LabelDefinitionInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|delete ()
specifier|default
name|void
name|delete
parameter_list|()
throws|throws
name|RestApiException
block|{
name|delete
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (@ullable String commitMessage)
name|void
name|delete
parameter_list|(
annotation|@
name|Nullable
name|String
name|commitMessage
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|LabelApi
block|{
annotation|@
name|Override
DECL|method|create (LabelDefinitionInput input)
specifier|public
name|LabelApi
name|create
parameter_list|(
name|LabelDefinitionInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|delete (@ullable String commitMessage)
specifier|public
name|void
name|delete
parameter_list|(
annotation|@
name|Nullable
name|String
name|commitMessage
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit

