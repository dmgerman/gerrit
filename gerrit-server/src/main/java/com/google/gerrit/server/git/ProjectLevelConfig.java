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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|ProjectState
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|CommitBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/** Configuration file in the projects refs/meta/config branch. */
end_comment

begin_class
DECL|class|ProjectLevelConfig
specifier|public
class|class
name|ProjectLevelConfig
extends|extends
name|VersionedMetaData
block|{
DECL|field|fileName
specifier|private
specifier|final
name|String
name|fileName
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|ProjectState
name|project
decl_stmt|;
DECL|field|cfg
specifier|private
name|Config
name|cfg
decl_stmt|;
DECL|method|ProjectLevelConfig (String fileName, ProjectState project)
specifier|public
name|ProjectLevelConfig
parameter_list|(
name|String
name|fileName
parameter_list|,
name|ProjectState
name|project
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|protected
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|RefNames
operator|.
name|REFS_CONFIG
return|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|cfg
operator|=
name|readConfig
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|Config
name|get
parameter_list|()
block|{
if|if
condition|(
name|cfg
operator|==
literal|null
condition|)
block|{
name|cfg
operator|=
operator|new
name|Config
argument_list|()
expr_stmt|;
block|}
return|return
name|cfg
return|;
block|}
DECL|method|getWithInheritance ()
specifier|public
name|Config
name|getWithInheritance
parameter_list|()
block|{
name|Config
name|cfgWithInheritance
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
try|try
block|{
name|cfgWithInheritance
operator|.
name|fromText
argument_list|(
name|get
argument_list|()
operator|.
name|toText
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
comment|// cannot happen
block|}
name|ProjectState
name|parent
init|=
name|Iterables
operator|.
name|getFirst
argument_list|(
name|project
operator|.
name|parents
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|Config
name|parentCfg
init|=
name|parent
operator|.
name|getConfig
argument_list|(
name|fileName
argument_list|)
operator|.
name|getWithInheritance
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|section
range|:
name|parentCfg
operator|.
name|getSections
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|allNames
init|=
name|get
argument_list|()
operator|.
name|getNames
argument_list|(
name|section
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|parentCfg
operator|.
name|getNames
argument_list|(
name|section
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|allNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|cfgWithInheritance
operator|.
name|setStringList
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|parentCfg
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|subsection
range|:
name|parentCfg
operator|.
name|getSubsections
argument_list|(
name|section
argument_list|)
control|)
block|{
name|allNames
operator|=
name|get
argument_list|()
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|parentCfg
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|allNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|cfgWithInheritance
operator|.
name|setStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|parentCfg
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|cfgWithInheritance
return|;
block|}
annotation|@
name|Override
DECL|method|onSave (CommitBuilder commit)
specifier|protected
name|boolean
name|onSave
parameter_list|(
name|CommitBuilder
name|commit
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|commit
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|commit
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|commit
operator|.
name|setMessage
argument_list|(
literal|"Updated configuration\n"
argument_list|)
expr_stmt|;
block|}
name|saveConfig
argument_list|(
name|fileName
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

