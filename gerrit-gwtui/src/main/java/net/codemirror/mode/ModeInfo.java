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
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
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
name|client
operator|.
name|rpc
operator|.
name|NativeMap
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
name|client
operator|.
name|rpc
operator|.
name|Natives
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
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
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
name|core
operator|.
name|client
operator|.
name|JsArray
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
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
name|safehtml
operator|.
name|shared
operator|.
name|SafeUri
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Description of a CodeMirror language mode. */
end_comment

begin_class
DECL|class|ModeInfo
specifier|public
class|class
name|ModeInfo
extends|extends
name|JavaScriptObject
block|{
DECL|field|byMime
specifier|private
specifier|static
name|NativeMap
argument_list|<
name|ModeInfo
argument_list|>
name|byMime
decl_stmt|;
DECL|field|byExt
specifier|private
specifier|static
name|NativeMap
argument_list|<
name|ModeInfo
argument_list|>
name|byExt
decl_stmt|;
comment|/** Map of names such as "clike" to URI for code download. */
DECL|field|modeUris
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SafeUri
argument_list|>
name|modeUris
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|indexModes
argument_list|(
operator|new
name|DataResource
index|[]
block|{
name|Modes
operator|.
name|I
operator|.
name|apl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|asciiarmor
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|asn_1
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|asterisk
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|brainfuck
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|clike
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|clojure
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|cmake
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|cobol
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|coffeescript
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|commonlisp
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|crystal
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|css
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|cypher
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|d
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|dart
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|diff
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|django
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|dockerfile
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|dtd
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|dylan
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|ebnf
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|ecl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|eiffel
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|elm
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|erlang
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|factor
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|fcl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|forth
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|fortran
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|gas
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|gerrit_commit
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|gfm
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|gherkin
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|go
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|groovy
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|haml
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|handlebars
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|haskell_literate
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|haskell
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|haxe
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|htmlembedded
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|htmlmixed
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|http
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|idl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|javascript
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|jinja2
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|jsx
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|julia
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|livescript
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|lua
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|markdown
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|mathematica
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|mbox
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|mirc
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|mllike
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|modelica
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|mscgen
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|mumps
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|nginx
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|nsis
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|ntriples
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|octave
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|oz
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|pascal
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|pegjs
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|perl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|php
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|pig
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|powershell
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|properties
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|protobuf
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|pug
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|puppet
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|python
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|q
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|r
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|rpm
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|rst
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|ruby
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|rust
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|sas
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|sass
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|scheme
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|shell
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|smalltalk
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|smarty
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|solr
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|soy
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|sparql
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|spreadsheet
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|sql
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|stex
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|stylus
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|swift
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|tcl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|textile
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|tiddlywiki
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|tiki
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|toml
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|tornado
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|troff
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|ttcn_cfg
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|ttcn
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|turtle
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|twig
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|vb
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|vbscript
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|velocity
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|verilog
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|vhdl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|vue
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|webidl
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|xml
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|xquery
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|yacas
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|yaml_frontmatter
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|yaml
argument_list|()
block|,
name|Modes
operator|.
name|I
operator|.
name|z80
argument_list|()
block|,         }
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"application/x-httpd-php-open"
argument_list|,
literal|"application/x-httpd-php"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"application/x-javascript"
argument_list|,
literal|"application/javascript"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"application/x-shellscript"
argument_list|,
literal|"text/x-sh"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"application/x-tcl"
argument_list|,
literal|"text/x-tcl"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/typescript"
argument_list|,
literal|"application/typescript"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-c"
argument_list|,
literal|"text/x-csrc"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-c++hdr"
argument_list|,
literal|"text/x-c++src"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-chdr"
argument_list|,
literal|"text/x-csrc"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-h"
argument_list|,
literal|"text/x-csrc"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-ini"
argument_list|,
literal|"text/x-properties"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-java-source"
argument_list|,
literal|"text/x-java"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-php"
argument_list|,
literal|"application/x-httpd-php"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"text/x-scripttcl"
argument_list|,
literal|"text/x-tcl"
argument_list|)
expr_stmt|;
block|}
comment|/** All supported modes. */
DECL|method|all ()
specifier|public
specifier|static
specifier|native
name|JsArray
argument_list|<
name|ModeInfo
argument_list|>
name|all
parameter_list|()
comment|/*-{     return $wnd.CodeMirror.modeInfo   }-*/
function_decl|;
DECL|method|setAll (JsArray<ModeInfo> m)
specifier|private
specifier|static
specifier|native
name|void
name|setAll
parameter_list|(
name|JsArray
argument_list|<
name|ModeInfo
argument_list|>
name|m
parameter_list|)
comment|/*-{     $wnd.CodeMirror.modeInfo = m   }-*/
function_decl|;
comment|/** Look up mode by primary or alternate MIME types. */
DECL|method|findModeByMIME (String mime)
specifier|public
specifier|static
name|ModeInfo
name|findModeByMIME
parameter_list|(
name|String
name|mime
parameter_list|)
block|{
return|return
name|byMime
operator|.
name|get
argument_list|(
name|mime
argument_list|)
return|;
block|}
DECL|method|getModeScriptUri (String mode)
specifier|public
specifier|static
name|SafeUri
name|getModeScriptUri
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
return|return
name|modeUris
operator|.
name|get
argument_list|(
name|mode
argument_list|)
return|;
block|}
comment|/** Look up mode by MIME type or file extension from a path. */
DECL|method|findMode (String mime, String path)
specifier|public
specifier|static
name|ModeInfo
name|findMode
parameter_list|(
name|String
name|mime
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|ModeInfo
name|m
init|=
name|byMime
operator|.
name|get
argument_list|(
name|mime
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
return|return
name|m
return|;
block|}
name|int
name|s
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|int
name|d
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
operator|-
literal|1
operator|||
name|s
operator|>
name|d
condition|)
block|{
return|return
literal|null
return|;
comment|// punt on "foo.src/bar" type paths.
block|}
if|if
condition|(
name|byExt
operator|==
literal|null
condition|)
block|{
name|byExt
operator|=
name|NativeMap
operator|.
name|create
argument_list|()
expr_stmt|;
for|for
control|(
name|ModeInfo
name|mode
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|all
argument_list|()
argument_list|)
control|)
block|{
for|for
control|(
name|String
name|ext
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|mode
operator|.
name|ext
argument_list|()
argument_list|)
control|)
block|{
name|byExt
operator|.
name|put
argument_list|(
name|ext
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|byExt
operator|.
name|get
argument_list|(
name|path
operator|.
name|substring
argument_list|(
name|d
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
DECL|method|alias (String serverMime, String toMime)
specifier|private
specifier|static
name|void
name|alias
parameter_list|(
name|String
name|serverMime
parameter_list|,
name|String
name|toMime
parameter_list|)
block|{
name|ModeInfo
name|mode
init|=
name|byMime
operator|.
name|get
argument_list|(
name|toMime
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
name|byMime
operator|.
name|put
argument_list|(
name|serverMime
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|indexModes (DataResource[] all)
specifier|private
specifier|static
name|void
name|indexModes
parameter_list|(
name|DataResource
index|[]
name|all
parameter_list|)
block|{
for|for
control|(
name|DataResource
name|r
range|:
name|all
control|)
block|{
name|modeUris
operator|.
name|put
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|,
name|r
operator|.
name|getSafeUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JsArray
argument_list|<
name|ModeInfo
argument_list|>
name|modeList
init|=
name|all
argument_list|()
decl_stmt|;
name|modeList
operator|.
name|push
argument_list|(
name|gerrit_commit
argument_list|()
argument_list|)
expr_stmt|;
name|byMime
operator|=
name|NativeMap
operator|.
name|create
argument_list|()
expr_stmt|;
name|JsArray
argument_list|<
name|ModeInfo
argument_list|>
name|filtered
init|=
name|JsArray
operator|.
name|createArray
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
for|for
control|(
name|ModeInfo
name|m
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|modeList
argument_list|)
control|)
block|{
if|if
condition|(
name|modeUris
operator|.
name|containsKey
argument_list|(
name|m
operator|.
name|mode
argument_list|()
argument_list|)
condition|)
block|{
name|filtered
operator|.
name|push
argument_list|(
name|m
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|mimeType
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|m
operator|.
name|mimes
argument_list|()
argument_list|)
control|)
block|{
name|byMime
operator|.
name|put
argument_list|(
name|mimeType
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|byMime
operator|.
name|put
argument_list|(
name|m
operator|.
name|mode
argument_list|()
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
block|}
name|Natives
operator|.
name|asList
argument_list|(
name|filtered
argument_list|)
operator|.
name|sort
argument_list|(
name|comparing
argument_list|(
name|m
lambda|->
name|m
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setAll
argument_list|(
name|filtered
argument_list|)
expr_stmt|;
block|}
comment|/** Human readable name of the mode, such as "C++". */
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name }-*/
function_decl|;
comment|/** Internal CodeMirror name for {@code mode.js} file to load. */
DECL|method|mode ()
specifier|public
specifier|final
specifier|native
name|String
name|mode
parameter_list|()
comment|/*-{ return this.mode }-*/
function_decl|;
comment|/** Primary MIME type to activate this mode. */
DECL|method|mime ()
specifier|public
specifier|final
specifier|native
name|String
name|mime
parameter_list|()
comment|/*-{ return this.mime }-*/
function_decl|;
comment|/** Primary and additional MIME types that activate this mode. */
DECL|method|mimes ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|mimes
parameter_list|()
comment|/*-{ return this.mimes || [this.mime] }-*/
function_decl|;
DECL|method|ext ()
specifier|private
specifier|native
name|JsArrayString
name|ext
parameter_list|()
comment|/*-{ return this.ext || [] }-*/
function_decl|;
DECL|method|ModeInfo ()
specifier|protected
name|ModeInfo
parameter_list|()
block|{}
DECL|method|gerrit_commit ()
specifier|private
specifier|static
specifier|native
name|ModeInfo
name|gerrit_commit
parameter_list|()
comment|/*-{     return {name: "Git Commit Message",             mime: "text/x-gerrit-commit-message",             mode: "gerrit_commit"}   }-*/
function_decl|;
block|}
end_class

end_unit

