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
DECL|package|com.google.gerrit.server.args4j
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|args4j
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|cli
operator|.
name|Localizable
operator|.
name|localizable
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
name|entities
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
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
name|CmdLineException
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
name|CmdLineParser
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
name|Parameters
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

begin_class
DECL|class|PatchSetIdHandler
specifier|public
class|class
name|PatchSetIdHandler
extends|extends
name|OptionHandler
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
block|{
annotation|@
name|Inject
DECL|method|PatchSetIdHandler ( @ssisted final CmdLineParser parser, @Assisted final OptionDef option, @Assisted final Setter<PatchSet.Id> setter)
specifier|public
name|PatchSetIdHandler
parameter_list|(
annotation|@
name|Assisted
specifier|final
name|CmdLineParser
name|parser
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|OptionDef
name|option
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Setter
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|setter
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|option
argument_list|,
name|setter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parseArguments (Parameters params)
specifier|public
specifier|final
name|int
name|parseArguments
parameter_list|(
name|Parameters
name|params
parameter_list|)
throws|throws
name|CmdLineException
block|{
specifier|final
name|String
name|token
init|=
name|params
operator|.
name|getParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|PatchSet
operator|.
name|Id
name|id
decl_stmt|;
try|try
block|{
name|id
operator|=
name|PatchSet
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
name|localizable
argument_list|(
literal|"\"%s\" is not a valid patch set"
argument_list|)
argument_list|,
name|token
argument_list|)
throw|;
block|}
name|setter
operator|.
name|addValue
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|getDefaultMetaVariable ()
specifier|public
specifier|final
name|String
name|getDefaultMetaVariable
parameter_list|()
block|{
return|return
literal|"CHANGE,PATCHSET"
return|;
block|}
block|}
end_class

end_unit

