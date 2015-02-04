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
DECL|method|clike ()
annotation|@
name|Source
argument_list|(
literal|"clike.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|clike
parameter_list|()
function_decl|;
DECL|method|clojure ()
annotation|@
name|Source
argument_list|(
literal|"clojure.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|clojure
parameter_list|()
function_decl|;
DECL|method|coffeescript ()
annotation|@
name|Source
argument_list|(
literal|"coffeescript.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|coffeescript
parameter_list|()
function_decl|;
DECL|method|commonlisp ()
annotation|@
name|Source
argument_list|(
literal|"commonlisp.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|commonlisp
parameter_list|()
function_decl|;
DECL|method|css ()
annotation|@
name|Source
argument_list|(
literal|"css.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|css
parameter_list|()
function_decl|;
DECL|method|d ()
annotation|@
name|Source
argument_list|(
literal|"d.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|d
parameter_list|()
function_decl|;
DECL|method|dart ()
annotation|@
name|Source
argument_list|(
literal|"dart.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|dart
parameter_list|()
function_decl|;
DECL|method|diff ()
annotation|@
name|Source
argument_list|(
literal|"diff.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|diff
parameter_list|()
function_decl|;
DECL|method|dockerfile ()
annotation|@
name|Source
argument_list|(
literal|"dockerfile.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|dockerfile
parameter_list|()
function_decl|;
DECL|method|dtd ()
annotation|@
name|Source
argument_list|(
literal|"dtd.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|dtd
parameter_list|()
function_decl|;
DECL|method|erlang ()
annotation|@
name|Source
argument_list|(
literal|"erlang.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|erlang
parameter_list|()
function_decl|;
DECL|method|gas ()
annotation|@
name|Source
argument_list|(
literal|"gas.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|gas
parameter_list|()
function_decl|;
DECL|method|gerrit_commit ()
annotation|@
name|Source
argument_list|(
literal|"gerrit/commit.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|gerrit_commit
parameter_list|()
function_decl|;
DECL|method|gfm ()
annotation|@
name|Source
argument_list|(
literal|"gfm.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|gfm
parameter_list|()
function_decl|;
DECL|method|go ()
annotation|@
name|Source
argument_list|(
literal|"go.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|go
parameter_list|()
function_decl|;
DECL|method|groovy ()
annotation|@
name|Source
argument_list|(
literal|"groovy.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|groovy
parameter_list|()
function_decl|;
DECL|method|haskell ()
annotation|@
name|Source
argument_list|(
literal|"haskell.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|haskell
parameter_list|()
function_decl|;
DECL|method|htmlmixed ()
annotation|@
name|Source
argument_list|(
literal|"htmlmixed.js"
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
literal|"javascript.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|javascript
parameter_list|()
function_decl|;
DECL|method|lua ()
annotation|@
name|Source
argument_list|(
literal|"lua.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|lua
parameter_list|()
function_decl|;
DECL|method|markdown ()
annotation|@
name|Source
argument_list|(
literal|"markdown.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|markdown
parameter_list|()
function_decl|;
DECL|method|perl ()
annotation|@
name|Source
argument_list|(
literal|"perl.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|perl
parameter_list|()
function_decl|;
DECL|method|php ()
annotation|@
name|Source
argument_list|(
literal|"php.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|php
parameter_list|()
function_decl|;
DECL|method|pig ()
annotation|@
name|Source
argument_list|(
literal|"pig.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|pig
parameter_list|()
function_decl|;
DECL|method|properties ()
annotation|@
name|Source
argument_list|(
literal|"properties.js"
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
literal|"python.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|python
parameter_list|()
function_decl|;
DECL|method|r ()
annotation|@
name|Source
argument_list|(
literal|"r.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|r
parameter_list|()
function_decl|;
DECL|method|rst ()
annotation|@
name|Source
argument_list|(
literal|"rst.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|rst
parameter_list|()
function_decl|;
DECL|method|ruby ()
annotation|@
name|Source
argument_list|(
literal|"ruby.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|ruby
parameter_list|()
function_decl|;
DECL|method|scheme ()
annotation|@
name|Source
argument_list|(
literal|"scheme.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|scheme
parameter_list|()
function_decl|;
DECL|method|shell ()
annotation|@
name|Source
argument_list|(
literal|"shell.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|shell
parameter_list|()
function_decl|;
DECL|method|smalltalk ()
annotation|@
name|Source
argument_list|(
literal|"smalltalk.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|smalltalk
parameter_list|()
function_decl|;
DECL|method|soy ()
annotation|@
name|Source
argument_list|(
literal|"soy.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|soy
parameter_list|()
function_decl|;
DECL|method|sql ()
annotation|@
name|Source
argument_list|(
literal|"sql.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|sql
parameter_list|()
function_decl|;
DECL|method|stex ()
annotation|@
name|Source
argument_list|(
literal|"stex.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|stex
parameter_list|()
function_decl|;
DECL|method|tcl ()
annotation|@
name|Source
argument_list|(
literal|"tcl.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|tcl
parameter_list|()
function_decl|;
DECL|method|velocity ()
annotation|@
name|Source
argument_list|(
literal|"velocity.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|velocity
parameter_list|()
function_decl|;
DECL|method|verilog ()
annotation|@
name|Source
argument_list|(
literal|"verilog.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|verilog
parameter_list|()
function_decl|;
DECL|method|xml ()
annotation|@
name|Source
argument_list|(
literal|"xml.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|xml
parameter_list|()
function_decl|;
DECL|method|yaml ()
annotation|@
name|Source
argument_list|(
literal|"yaml.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|yaml
parameter_list|()
function_decl|;
comment|// When adding a resource, update static initializer in ModeInfo.
block|}
end_interface

end_unit

