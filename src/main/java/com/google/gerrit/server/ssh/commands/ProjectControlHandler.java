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
DECL|package|com.google.gerrit.server.ssh.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
operator|.
name|commands
package|;
end_package

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
name|reviewdb
operator|.
name|Project
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
name|server
operator|.
name|project
operator|.
name|NoSuchProjectException
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
name|server
operator|.
name|project
operator|.
name|ProjectControl
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
DECL|class|ProjectControlHandler
specifier|public
class|class
name|ProjectControlHandler
extends|extends
name|OptionHandler
argument_list|<
name|ProjectControl
argument_list|>
block|{
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Inject
DECL|method|ProjectControlHandler ( final ProjectControl.Factory projectControlFactory, @Assisted final CmdLineParser parser, @Assisted final OptionDef option, @Assisted final Setter setter)
specifier|public
name|ProjectControlHandler
parameter_list|(
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
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
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parseArguments (final Parameters params)
specifier|public
specifier|final
name|int
name|parseArguments
parameter_list|(
specifier|final
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
name|String
name|projectName
init|=
name|token
decl_stmt|;
if|if
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the trailing ".git" suffix, which we never keep
comment|// in our database, but clients might mistakenly provide anyway.
comment|//
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|projectName
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectName
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the leading "/" if supplied by an absolute path.
comment|// We don't have a file system hierarchy, just a flat namespace in
comment|// the database's Project entities. We never encode these with a
comment|// leading '/' but users might accidentally include them in Git URLs.
comment|//
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ProjectControl
name|control
decl_stmt|;
try|try
block|{
name|control
operator|=
name|projectControlFactory
operator|.
name|validateFor
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
literal|"'"
operator|+
name|token
operator|+
literal|"': not a Gerrit project"
argument_list|)
throw|;
block|}
name|setter
operator|.
name|addValue
argument_list|(
name|control
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
literal|"PROJECT"
return|;
block|}
block|}
end_class

end_unit

