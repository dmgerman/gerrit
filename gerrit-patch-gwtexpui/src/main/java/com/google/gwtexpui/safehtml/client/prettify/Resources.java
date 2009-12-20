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
DECL|package|com.google.gwtexpui.safehtml.client.prettify
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|prettify
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
name|TextResource
import|;
end_import

begin_interface
DECL|interface|Resources
specifier|public
interface|interface
name|Resources
extends|extends
name|ClientBundle
block|{
DECL|field|I
specifier|public
specifier|static
specifier|final
name|Resources
name|I
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Resources
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Source
argument_list|(
literal|"prettify.css"
argument_list|)
DECL|method|css ()
name|CssResource
name|css
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"prettify.js"
argument_list|)
DECL|method|core ()
name|TextResource
name|core
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-apollo.js"
argument_list|)
DECL|method|lang_apollo ()
name|TextResource
name|lang_apollo
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-css.js"
argument_list|)
DECL|method|lang_css ()
name|TextResource
name|lang_css
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-hs.js"
argument_list|)
DECL|method|lang_hs ()
name|TextResource
name|lang_hs
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-lisp.js"
argument_list|)
DECL|method|lang_lisp ()
name|TextResource
name|lang_lisp
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-lua.js"
argument_list|)
DECL|method|lang_lua ()
name|TextResource
name|lang_lua
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-ml.js"
argument_list|)
DECL|method|lang_ml ()
name|TextResource
name|lang_ml
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-proto.js"
argument_list|)
DECL|method|lang_proto ()
name|TextResource
name|lang_proto
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-sql.js"
argument_list|)
DECL|method|lang_sql ()
name|TextResource
name|lang_sql
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-vb.js"
argument_list|)
DECL|method|lang_vb ()
name|TextResource
name|lang_vb
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"lang-wiki.js"
argument_list|)
DECL|method|lang_wiki ()
name|TextResource
name|lang_wiki
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

