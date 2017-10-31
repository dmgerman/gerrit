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
DECL|package|com.google.gwtexpui.globalkey.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
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
name|CssResource
import|;
end_import

begin_interface
DECL|interface|KeyCss
specifier|public
interface|interface
name|KeyCss
extends|extends
name|CssResource
block|{
DECL|method|helpPopup ()
name|String
name|helpPopup
parameter_list|()
function_decl|;
DECL|method|helpHeader ()
name|String
name|helpHeader
parameter_list|()
function_decl|;
DECL|method|helpHeaderGlue ()
name|String
name|helpHeaderGlue
parameter_list|()
function_decl|;
DECL|method|helpTable ()
name|String
name|helpTable
parameter_list|()
function_decl|;
DECL|method|helpTableGlue ()
name|String
name|helpTableGlue
parameter_list|()
function_decl|;
DECL|method|helpGroup ()
name|String
name|helpGroup
parameter_list|()
function_decl|;
DECL|method|helpKeyStroke ()
name|String
name|helpKeyStroke
parameter_list|()
function_decl|;
DECL|method|helpSeparator ()
name|String
name|helpSeparator
parameter_list|()
function_decl|;
DECL|method|helpKey ()
name|String
name|helpKey
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

