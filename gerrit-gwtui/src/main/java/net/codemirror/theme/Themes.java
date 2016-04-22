begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|net.codemirror.theme
package|package
name|net
operator|.
name|codemirror
operator|.
name|theme
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
name|ExternalTextResource
import|;
end_import

begin_interface
DECL|interface|Themes
specifier|public
interface|interface
name|Themes
extends|extends
name|ClientBundle
block|{
DECL|field|I
name|Themes
name|I
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Themes
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|day_3024 ()
annotation|@
name|Source
argument_list|(
literal|"3024-day.css"
argument_list|)
name|ExternalTextResource
name|day_3024
parameter_list|()
function_decl|;
DECL|method|night_3024 ()
annotation|@
name|Source
argument_list|(
literal|"3024-night.css"
argument_list|)
name|ExternalTextResource
name|night_3024
parameter_list|()
function_decl|;
DECL|method|abcdef ()
annotation|@
name|Source
argument_list|(
literal|"abcdef.css"
argument_list|)
name|ExternalTextResource
name|abcdef
parameter_list|()
function_decl|;
DECL|method|ambiance ()
annotation|@
name|Source
argument_list|(
literal|"ambiance.css"
argument_list|)
name|ExternalTextResource
name|ambiance
parameter_list|()
function_decl|;
DECL|method|base16_dark ()
annotation|@
name|Source
argument_list|(
literal|"base16-dark.css"
argument_list|)
name|ExternalTextResource
name|base16_dark
parameter_list|()
function_decl|;
DECL|method|base16_light ()
annotation|@
name|Source
argument_list|(
literal|"base16-light.css"
argument_list|)
name|ExternalTextResource
name|base16_light
parameter_list|()
function_decl|;
DECL|method|bespin ()
annotation|@
name|Source
argument_list|(
literal|"bespin.css"
argument_list|)
name|ExternalTextResource
name|bespin
parameter_list|()
function_decl|;
DECL|method|blackboard ()
annotation|@
name|Source
argument_list|(
literal|"blackboard.css"
argument_list|)
name|ExternalTextResource
name|blackboard
parameter_list|()
function_decl|;
DECL|method|cobalt ()
annotation|@
name|Source
argument_list|(
literal|"cobalt.css"
argument_list|)
name|ExternalTextResource
name|cobalt
parameter_list|()
function_decl|;
DECL|method|colorforth ()
annotation|@
name|Source
argument_list|(
literal|"colorforth.css"
argument_list|)
name|ExternalTextResource
name|colorforth
parameter_list|()
function_decl|;
DECL|method|dracula ()
annotation|@
name|Source
argument_list|(
literal|"dracula.css"
argument_list|)
name|ExternalTextResource
name|dracula
parameter_list|()
function_decl|;
DECL|method|eclipse ()
annotation|@
name|Source
argument_list|(
literal|"eclipse.css"
argument_list|)
name|ExternalTextResource
name|eclipse
parameter_list|()
function_decl|;
DECL|method|elegant ()
annotation|@
name|Source
argument_list|(
literal|"elegant.css"
argument_list|)
name|ExternalTextResource
name|elegant
parameter_list|()
function_decl|;
DECL|method|erlang_dark ()
annotation|@
name|Source
argument_list|(
literal|"erlang-dark.css"
argument_list|)
name|ExternalTextResource
name|erlang_dark
parameter_list|()
function_decl|;
DECL|method|hopscotch ()
annotation|@
name|Source
argument_list|(
literal|"hopscotch.css"
argument_list|)
name|ExternalTextResource
name|hopscotch
parameter_list|()
function_decl|;
DECL|method|icecoder ()
annotation|@
name|Source
argument_list|(
literal|"icecoder.css"
argument_list|)
name|ExternalTextResource
name|icecoder
parameter_list|()
function_decl|;
DECL|method|isotope ()
annotation|@
name|Source
argument_list|(
literal|"isotope.css"
argument_list|)
name|ExternalTextResource
name|isotope
parameter_list|()
function_decl|;
DECL|method|lesser_dark ()
annotation|@
name|Source
argument_list|(
literal|"lesser-dark.css"
argument_list|)
name|ExternalTextResource
name|lesser_dark
parameter_list|()
function_decl|;
DECL|method|liquibyte ()
annotation|@
name|Source
argument_list|(
literal|"liquibyte.css"
argument_list|)
name|ExternalTextResource
name|liquibyte
parameter_list|()
function_decl|;
DECL|method|material ()
annotation|@
name|Source
argument_list|(
literal|"material.css"
argument_list|)
name|ExternalTextResource
name|material
parameter_list|()
function_decl|;
DECL|method|mbo ()
annotation|@
name|Source
argument_list|(
literal|"mbo.css"
argument_list|)
name|ExternalTextResource
name|mbo
parameter_list|()
function_decl|;
DECL|method|mdn_like ()
annotation|@
name|Source
argument_list|(
literal|"mdn-like.css"
argument_list|)
name|ExternalTextResource
name|mdn_like
parameter_list|()
function_decl|;
DECL|method|midnight ()
annotation|@
name|Source
argument_list|(
literal|"midnight.css"
argument_list|)
name|ExternalTextResource
name|midnight
parameter_list|()
function_decl|;
DECL|method|monokai ()
annotation|@
name|Source
argument_list|(
literal|"monokai.css"
argument_list|)
name|ExternalTextResource
name|monokai
parameter_list|()
function_decl|;
DECL|method|neat ()
annotation|@
name|Source
argument_list|(
literal|"neat.css"
argument_list|)
name|ExternalTextResource
name|neat
parameter_list|()
function_decl|;
DECL|method|neo ()
annotation|@
name|Source
argument_list|(
literal|"neo.css"
argument_list|)
name|ExternalTextResource
name|neo
parameter_list|()
function_decl|;
DECL|method|night ()
annotation|@
name|Source
argument_list|(
literal|"night.css"
argument_list|)
name|ExternalTextResource
name|night
parameter_list|()
function_decl|;
DECL|method|paraiso_dark ()
annotation|@
name|Source
argument_list|(
literal|"paraiso-dark.css"
argument_list|)
name|ExternalTextResource
name|paraiso_dark
parameter_list|()
function_decl|;
DECL|method|paraiso_light ()
annotation|@
name|Source
argument_list|(
literal|"paraiso-light.css"
argument_list|)
name|ExternalTextResource
name|paraiso_light
parameter_list|()
function_decl|;
DECL|method|pastel_on_dark ()
annotation|@
name|Source
argument_list|(
literal|"pastel-on-dark.css"
argument_list|)
name|ExternalTextResource
name|pastel_on_dark
parameter_list|()
function_decl|;
DECL|method|railscasts ()
annotation|@
name|Source
argument_list|(
literal|"railscasts.css"
argument_list|)
name|ExternalTextResource
name|railscasts
parameter_list|()
function_decl|;
DECL|method|rubyblue ()
annotation|@
name|Source
argument_list|(
literal|"rubyblue.css"
argument_list|)
name|ExternalTextResource
name|rubyblue
parameter_list|()
function_decl|;
DECL|method|seti ()
annotation|@
name|Source
argument_list|(
literal|"seti.css"
argument_list|)
name|ExternalTextResource
name|seti
parameter_list|()
function_decl|;
DECL|method|solarized ()
annotation|@
name|Source
argument_list|(
literal|"solarized.css"
argument_list|)
name|ExternalTextResource
name|solarized
parameter_list|()
function_decl|;
DECL|method|the_matrix ()
annotation|@
name|Source
argument_list|(
literal|"the-matrix.css"
argument_list|)
name|ExternalTextResource
name|the_matrix
parameter_list|()
function_decl|;
DECL|method|tomorrow_night_bright ()
annotation|@
name|Source
argument_list|(
literal|"tomorrow-night-bright.css"
argument_list|)
name|ExternalTextResource
name|tomorrow_night_bright
parameter_list|()
function_decl|;
DECL|method|tomorrow_night_eighties ()
annotation|@
name|Source
argument_list|(
literal|"tomorrow-night-eighties.css"
argument_list|)
name|ExternalTextResource
name|tomorrow_night_eighties
parameter_list|()
function_decl|;
DECL|method|ttcn ()
annotation|@
name|Source
argument_list|(
literal|"ttcn.css"
argument_list|)
name|ExternalTextResource
name|ttcn
parameter_list|()
function_decl|;
DECL|method|twilight ()
annotation|@
name|Source
argument_list|(
literal|"twilight.css"
argument_list|)
name|ExternalTextResource
name|twilight
parameter_list|()
function_decl|;
DECL|method|vibrant_ink ()
annotation|@
name|Source
argument_list|(
literal|"vibrant-ink.css"
argument_list|)
name|ExternalTextResource
name|vibrant_ink
parameter_list|()
function_decl|;
DECL|method|xq_dark ()
annotation|@
name|Source
argument_list|(
literal|"xq-dark.css"
argument_list|)
name|ExternalTextResource
name|xq_dark
parameter_list|()
function_decl|;
DECL|method|xq_light ()
annotation|@
name|Source
argument_list|(
literal|"xq-light.css"
argument_list|)
name|ExternalTextResource
name|xq_light
parameter_list|()
function_decl|;
DECL|method|yeti ()
annotation|@
name|Source
argument_list|(
literal|"yeti.css"
argument_list|)
name|ExternalTextResource
name|yeti
parameter_list|()
function_decl|;
DECL|method|zenburn ()
annotation|@
name|Source
argument_list|(
literal|"zenburn.css"
argument_list|)
name|ExternalTextResource
name|zenburn
parameter_list|()
function_decl|;
comment|// When adding a resource, update:
comment|// - static initializer in ThemeLoader
comment|// - enum value in com.google.gerrit.extensions.common.Theme
block|}
end_interface

end_unit

