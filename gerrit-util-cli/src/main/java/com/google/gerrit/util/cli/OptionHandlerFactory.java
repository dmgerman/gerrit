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
DECL|package|com.google.gerrit.util.cli
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|cli
package|;
end_package

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|OptionDef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|OptionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|Setter
import|;
end_import

begin_comment
comment|/** Creates an args4j OptionHandler through a Guice Injector. */
end_comment

begin_interface
DECL|interface|OptionHandlerFactory
specifier|public
interface|interface
name|OptionHandlerFactory
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|create (org.kohsuke.args4j.CmdLineParser cmdLineParser, OptionDef optionDef, Setter<T> setter)
name|OptionHandler
argument_list|<
name|T
argument_list|>
name|create
parameter_list|(
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineParser
name|cmdLineParser
parameter_list|,
name|OptionDef
name|optionDef
parameter_list|,
name|Setter
argument_list|<
name|T
argument_list|>
name|setter
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

