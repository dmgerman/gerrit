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
DECL|method|apl ()
annotation|@
name|Source
argument_list|(
literal|"apl.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|apl
parameter_list|()
function_decl|;
DECL|method|asciiarmor ()
annotation|@
name|Source
argument_list|(
literal|"asciiarmor.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|asciiarmor
parameter_list|()
function_decl|;
DECL|method|asn_1 ()
annotation|@
name|Source
argument_list|(
literal|"asn.1.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|asn_1
parameter_list|()
function_decl|;
DECL|method|asterisk ()
annotation|@
name|Source
argument_list|(
literal|"asterisk.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|asterisk
parameter_list|()
function_decl|;
DECL|method|brainfuck ()
annotation|@
name|Source
argument_list|(
literal|"brainfuck.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|brainfuck
parameter_list|()
function_decl|;
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
DECL|method|cmake ()
annotation|@
name|Source
argument_list|(
literal|"cmake.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|cmake
parameter_list|()
function_decl|;
DECL|method|cobol ()
annotation|@
name|Source
argument_list|(
literal|"cobol.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|cobol
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
DECL|method|crystal ()
annotation|@
name|Source
argument_list|(
literal|"crystal.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|crystal
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
DECL|method|cypher ()
annotation|@
name|Source
argument_list|(
literal|"cypher.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|cypher
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
DECL|method|django ()
annotation|@
name|Source
argument_list|(
literal|"django.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|django
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
DECL|method|dylan ()
annotation|@
name|Source
argument_list|(
literal|"dylan.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|dylan
parameter_list|()
function_decl|;
DECL|method|ebnf ()
annotation|@
name|Source
argument_list|(
literal|"ebnf.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|ebnf
parameter_list|()
function_decl|;
DECL|method|ecl ()
annotation|@
name|Source
argument_list|(
literal|"ecl.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|ecl
parameter_list|()
function_decl|;
DECL|method|eiffel ()
annotation|@
name|Source
argument_list|(
literal|"eiffel.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|eiffel
parameter_list|()
function_decl|;
DECL|method|elm ()
annotation|@
name|Source
argument_list|(
literal|"elm.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|elm
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
DECL|method|factor ()
annotation|@
name|Source
argument_list|(
literal|"factor.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|factor
parameter_list|()
function_decl|;
DECL|method|fcl ()
annotation|@
name|Source
argument_list|(
literal|"fcl.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|fcl
parameter_list|()
function_decl|;
DECL|method|forth ()
annotation|@
name|Source
argument_list|(
literal|"forth.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|forth
parameter_list|()
function_decl|;
DECL|method|fortran ()
annotation|@
name|Source
argument_list|(
literal|"fortran.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|fortran
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
DECL|method|gherkin ()
annotation|@
name|Source
argument_list|(
literal|"gherkin.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|gherkin
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
DECL|method|haml ()
annotation|@
name|Source
argument_list|(
literal|"haml.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|haml
parameter_list|()
function_decl|;
DECL|method|handlebars ()
annotation|@
name|Source
argument_list|(
literal|"handlebars.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|handlebars
parameter_list|()
function_decl|;
DECL|method|haskell_literate ()
annotation|@
name|Source
argument_list|(
literal|"haskell-literate.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|haskell_literate
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
DECL|method|haxe ()
annotation|@
name|Source
argument_list|(
literal|"haxe.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|haxe
parameter_list|()
function_decl|;
DECL|method|htmlembedded ()
annotation|@
name|Source
argument_list|(
literal|"htmlembedded.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|htmlembedded
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
DECL|method|http ()
annotation|@
name|Source
argument_list|(
literal|"http.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|http
parameter_list|()
function_decl|;
DECL|method|idl ()
annotation|@
name|Source
argument_list|(
literal|"idl.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|idl
parameter_list|()
function_decl|;
DECL|method|jade ()
annotation|@
name|Source
argument_list|(
literal|"jade.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|jade
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
DECL|method|jinja2 ()
annotation|@
name|Source
argument_list|(
literal|"jinja2.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|jinja2
parameter_list|()
function_decl|;
DECL|method|jsx ()
annotation|@
name|Source
argument_list|(
literal|"jsx.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|jsx
parameter_list|()
function_decl|;
DECL|method|julia ()
annotation|@
name|Source
argument_list|(
literal|"julia.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|julia
parameter_list|()
function_decl|;
DECL|method|livescript ()
annotation|@
name|Source
argument_list|(
literal|"livescript.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|livescript
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
DECL|method|mathematica ()
annotation|@
name|Source
argument_list|(
literal|"mathematica.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|mathematica
parameter_list|()
function_decl|;
DECL|method|mirc ()
annotation|@
name|Source
argument_list|(
literal|"mirc.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|mirc
parameter_list|()
function_decl|;
DECL|method|mllike ()
annotation|@
name|Source
argument_list|(
literal|"mllike.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|mllike
parameter_list|()
function_decl|;
DECL|method|modelica ()
annotation|@
name|Source
argument_list|(
literal|"modelica.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|modelica
parameter_list|()
function_decl|;
DECL|method|mscgen ()
annotation|@
name|Source
argument_list|(
literal|"mscgen.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|mscgen
parameter_list|()
function_decl|;
DECL|method|mumps ()
annotation|@
name|Source
argument_list|(
literal|"mumps.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|mumps
parameter_list|()
function_decl|;
DECL|method|nginx ()
annotation|@
name|Source
argument_list|(
literal|"nginx.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|nginx
parameter_list|()
function_decl|;
DECL|method|nsis ()
annotation|@
name|Source
argument_list|(
literal|"nsis.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|nsis
parameter_list|()
function_decl|;
DECL|method|ntriples ()
annotation|@
name|Source
argument_list|(
literal|"ntriples.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|ntriples
parameter_list|()
function_decl|;
DECL|method|octave ()
annotation|@
name|Source
argument_list|(
literal|"octave.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|octave
parameter_list|()
function_decl|;
DECL|method|oz ()
annotation|@
name|Source
argument_list|(
literal|"oz.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|oz
parameter_list|()
function_decl|;
DECL|method|pascal ()
annotation|@
name|Source
argument_list|(
literal|"pascal.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|pascal
parameter_list|()
function_decl|;
DECL|method|pegjs ()
annotation|@
name|Source
argument_list|(
literal|"pegjs.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|pegjs
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
DECL|method|protobuf ()
annotation|@
name|Source
argument_list|(
literal|"protobuf.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|protobuf
parameter_list|()
function_decl|;
DECL|method|puppet ()
annotation|@
name|Source
argument_list|(
literal|"puppet.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|puppet
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
DECL|method|q ()
annotation|@
name|Source
argument_list|(
literal|"q.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|q
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
DECL|method|rpm ()
annotation|@
name|Source
argument_list|(
literal|"rpm.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|rpm
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
DECL|method|rust ()
annotation|@
name|Source
argument_list|(
literal|"rust.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|rust
parameter_list|()
function_decl|;
DECL|method|sass ()
annotation|@
name|Source
argument_list|(
literal|"sass.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|sass
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
DECL|method|sieve ()
annotation|@
name|Source
argument_list|(
literal|"sieve.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|sieve
parameter_list|()
function_decl|;
DECL|method|slim ()
annotation|@
name|Source
argument_list|(
literal|"slim.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|slim
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
DECL|method|smarty ()
annotation|@
name|Source
argument_list|(
literal|"smarty.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|smarty
parameter_list|()
function_decl|;
DECL|method|solr ()
annotation|@
name|Source
argument_list|(
literal|"solr.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|solr
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
DECL|method|sparql ()
annotation|@
name|Source
argument_list|(
literal|"sparql.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|sparql
parameter_list|()
function_decl|;
DECL|method|spreadsheet ()
annotation|@
name|Source
argument_list|(
literal|"spreadsheet.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|spreadsheet
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
DECL|method|stylus ()
annotation|@
name|Source
argument_list|(
literal|"stylus.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|stylus
parameter_list|()
function_decl|;
DECL|method|swift ()
annotation|@
name|Source
argument_list|(
literal|"swift.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|swift
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
DECL|method|textile ()
annotation|@
name|Source
argument_list|(
literal|"textile.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|textile
parameter_list|()
function_decl|;
DECL|method|tiddlywiki ()
annotation|@
name|Source
argument_list|(
literal|"tiddlywiki.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|tiddlywiki
parameter_list|()
function_decl|;
DECL|method|tiki ()
annotation|@
name|Source
argument_list|(
literal|"tiki.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|tiki
parameter_list|()
function_decl|;
DECL|method|toml ()
annotation|@
name|Source
argument_list|(
literal|"toml.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|toml
parameter_list|()
function_decl|;
DECL|method|tornado ()
annotation|@
name|Source
argument_list|(
literal|"tornado.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|tornado
parameter_list|()
function_decl|;
DECL|method|troff ()
annotation|@
name|Source
argument_list|(
literal|"troff.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|troff
parameter_list|()
function_decl|;
DECL|method|ttcn_cfg ()
annotation|@
name|Source
argument_list|(
literal|"ttcn-cfg.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|ttcn_cfg
parameter_list|()
function_decl|;
DECL|method|ttcn ()
annotation|@
name|Source
argument_list|(
literal|"ttcn.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|ttcn
parameter_list|()
function_decl|;
DECL|method|turtle ()
annotation|@
name|Source
argument_list|(
literal|"turtle.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|turtle
parameter_list|()
function_decl|;
DECL|method|twig ()
annotation|@
name|Source
argument_list|(
literal|"twig.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|twig
parameter_list|()
function_decl|;
DECL|method|vb ()
annotation|@
name|Source
argument_list|(
literal|"vb.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|vb
parameter_list|()
function_decl|;
DECL|method|vbscript ()
annotation|@
name|Source
argument_list|(
literal|"vbscript.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|vbscript
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
DECL|method|vhdl ()
annotation|@
name|Source
argument_list|(
literal|"vhdl.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|vhdl
parameter_list|()
function_decl|;
DECL|method|vue ()
annotation|@
name|Source
argument_list|(
literal|"vue.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|vue
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
DECL|method|xquery ()
annotation|@
name|Source
argument_list|(
literal|"xquery.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|xquery
parameter_list|()
function_decl|;
DECL|method|yaml_frontmatter ()
annotation|@
name|Source
argument_list|(
literal|"yaml-frontmatter.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|yaml_frontmatter
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
DECL|method|z80 ()
annotation|@
name|Source
argument_list|(
literal|"z80.js"
argument_list|)
annotation|@
name|DoNotEmbed
name|DataResource
name|z80
parameter_list|()
function_decl|;
comment|// When adding a resource, update static initializer in ModeInfo.
block|}
end_interface

end_unit

