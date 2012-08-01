begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|CssResource
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
DECL|interface|GerritResources
specifier|public
interface|interface
name|GerritResources
extends|extends
name|ClientBundle
block|{
annotation|@
name|Source
argument_list|(
literal|"gerrit.css"
argument_list|)
DECL|method|css ()
name|GerritCss
name|css
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"gwt_override.css"
argument_list|)
DECL|method|gwt_override ()
name|CssResource
name|gwt_override
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"arrowRight.gif"
argument_list|)
DECL|method|arrowRight ()
specifier|public
name|ImageResource
name|arrowRight
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"starOpen.gif"
argument_list|)
DECL|method|starOpen ()
specifier|public
name|ImageResource
name|starOpen
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"starFilled.gif"
argument_list|)
DECL|method|starFilled ()
specifier|public
name|ImageResource
name|starFilled
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"greenCheck.png"
argument_list|)
DECL|method|greenCheck ()
specifier|public
name|ImageResource
name|greenCheck
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"redNot.png"
argument_list|)
DECL|method|redNot ()
specifier|public
name|ImageResource
name|redNot
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"downloadIcon.png"
argument_list|)
DECL|method|downloadIcon ()
specifier|public
name|ImageResource
name|downloadIcon
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

