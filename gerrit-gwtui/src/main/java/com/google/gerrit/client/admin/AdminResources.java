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
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|resources
operator|.
name|client
operator|.
name|ClientBundle
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|resources
operator|.
name|client
operator|.
name|ImageResource
import|;
end_import

begin_interface
DECL|interface|AdminResources
specifier|public
interface|interface
name|AdminResources
extends|extends
name|ClientBundle
block|{
DECL|field|I
specifier|public
specifier|static
specifier|final
name|AdminResources
name|I
init|=
name|GWT
operator|.
name|create
argument_list|(
name|AdminResources
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Source
argument_list|(
literal|"admin.css"
argument_list|)
DECL|method|css ()
name|AdminCss
name|css
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"editText.png"
argument_list|)
DECL|method|editText ()
specifier|public
name|ImageResource
name|editText
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"deleteNormal.png"
argument_list|)
DECL|method|deleteNormal ()
specifier|public
name|ImageResource
name|deleteNormal
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"deleteHover.png"
argument_list|)
DECL|method|deleteHover ()
specifier|public
name|ImageResource
name|deleteHover
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"undoNormal.png"
argument_list|)
DECL|method|undoNormal ()
specifier|public
name|ImageResource
name|undoNormal
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

