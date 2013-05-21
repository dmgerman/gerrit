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
DECL|package|net.codemirror.mode
package|package
name|net
operator|.
name|codemirror
operator|.
name|mode
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
name|DataResource
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
name|DataResource
operator|.
name|DoNotEmbed
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
DECL|interface|Modes
specifier|public
interface|interface
name|Modes
extends|extends
name|ClientBundle
block|{
DECL|field|I
specifier|public
specifier|static
specifier|final
name|Modes
name|I
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Modes
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|mode_map ()
annotation|@
name|Source
argument_list|(
literal|"mode_map"
argument_list|)
name|TextResource
name|mode_map
parameter_list|()
function_decl|;
DECL|method|clike ()
annotation|@
name|Source
argument_list|(
literal|"clike/clike.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|clike
parameter_list|()
function_decl|;
DECL|method|css ()
annotation|@
name|Source
argument_list|(
literal|"css/css.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|css
parameter_list|()
function_decl|;
DECL|method|go ()
annotation|@
name|Source
argument_list|(
literal|"go/go.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|go
parameter_list|()
function_decl|;
DECL|method|htmlmixed ()
annotation|@
name|Source
argument_list|(
literal|"htmlmixed/htmlmixed.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|htmlmixed
parameter_list|()
function_decl|;
DECL|method|javascript ()
annotation|@
name|Source
argument_list|(
literal|"javascript/javascript.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|javascript
parameter_list|()
function_decl|;
DECL|method|properties ()
annotation|@
name|Source
argument_list|(
literal|"properties/properties.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|properties
parameter_list|()
function_decl|;
DECL|method|python ()
annotation|@
name|Source
argument_list|(
literal|"python/python.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|python
parameter_list|()
function_decl|;
DECL|method|shell ()
annotation|@
name|Source
argument_list|(
literal|"shell/shell.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|shell
parameter_list|()
function_decl|;
DECL|method|sql ()
annotation|@
name|Source
argument_list|(
literal|"sql/sql.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|sql
parameter_list|()
function_decl|;
DECL|method|velocity ()
annotation|@
name|Source
argument_list|(
literal|"velocity/velocity.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|velocity
parameter_list|()
function_decl|;
DECL|method|xml ()
annotation|@
name|Source
argument_list|(
literal|"xml/xml.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|xml
parameter_list|()
function_decl|;
comment|// When adding a resource, update static initializer in ModeInjector.
block|}
end_interface

end_unit

