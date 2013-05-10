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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
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
name|restapi
operator|.
name|RestResource
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

begin_interface
DECL|interface|UiCommand
specifier|public
interface|interface
name|UiCommand
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
extends|extends
name|RestView
argument_list|<
name|R
argument_list|>
block|{
DECL|enum|Place
specifier|public
specifier|static
enum|enum
name|Place
block|{
DECL|enumConstant|PATCHSET_ACTION_PANEL
name|PATCHSET_ACTION_PANEL
block|;   }
empty_stmt|;
DECL|method|getPlace ()
name|Place
name|getPlace
parameter_list|()
function_decl|;
DECL|method|getLabel (R resource)
name|String
name|getLabel
parameter_list|(
name|R
name|resource
parameter_list|)
function_decl|;
DECL|method|getTitle (R resource)
name|String
name|getTitle
parameter_list|(
name|R
name|resource
parameter_list|)
function_decl|;
DECL|method|isVisible (R resource)
name|boolean
name|isVisible
parameter_list|(
name|R
name|resource
parameter_list|)
function_decl|;
DECL|method|isEnabled (R resource)
name|boolean
name|isEnabled
parameter_list|(
name|R
name|resource
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

