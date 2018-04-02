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
DECL|package|com.google.gerrit.pgm.init.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|api
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
name|reviewdb
operator|.
name|client
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
name|config
operator|.
name|SitePaths
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
name|GroupList
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
name|ProjectConfig
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|PersonIdent
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
name|RepositoryCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|AllProjectsConfig
specifier|public
class|class
name|AllProjectsConfig
extends|extends
name|VersionedMetaDataOnInit
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AllProjectsConfig
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cfg
specifier|private
name|Config
name|cfg
decl_stmt|;
DECL|field|groupList
specifier|private
name|GroupList
name|groupList
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllProjectsConfig (AllProjectsNameOnInitProvider allProjects, SitePaths site, InitFlags flags)
name|AllProjectsConfig
parameter_list|(
name|AllProjectsNameOnInitProvider
name|allProjects
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|InitFlags
name|flags
parameter_list|)
block|{
name|super
argument_list|(
name|flags
argument_list|,
name|site
argument_list|,
name|allProjects
operator|.
name|get
argument_list|()
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|Config
name|getConfig
parameter_list|()
block|{
return|return
name|cfg
return|;
block|}
DECL|method|getGroups ()
specifier|public
name|GroupList
name|getGroups
parameter_list|()
block|{
return|return
name|groupList
return|;
block|}
annotation|@
name|Override
DECL|method|load ()
specifier|public
name|AllProjectsConfig
name|load
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|super
operator|.
name|load
argument_list|()
expr_stmt|;
return|return
name|this
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
name|groupList
operator|=
name|readGroupList
argument_list|()
expr_stmt|;
name|cfg
operator|=
name|readConfig
argument_list|(
name|ProjectConfig
operator|.
name|PROJECT_CONFIG
argument_list|)
expr_stmt|;
block|}
DECL|method|readGroupList ()
specifier|private
name|GroupList
name|readGroupList
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|GroupList
operator|.
name|parse
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|project
argument_list|)
argument_list|,
name|readUTF8
argument_list|(
name|GroupList
operator|.
name|FILE_NAME
argument_list|)
argument_list|,
name|GroupList
operator|.
name|createLoggerSink
argument_list|(
name|GroupList
operator|.
name|FILE_NAME
argument_list|,
name|log
argument_list|)
argument_list|)
return|;
block|}
DECL|method|save (String pluginName, String message)
specifier|public
name|void
name|save
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|message
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|save
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|pluginName
argument_list|,
name|pluginName
operator|+
literal|"@gerrit"
argument_list|)
argument_list|,
literal|"Update from plugin "
operator|+
name|pluginName
operator|+
literal|": "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|save (PersonIdent ident, String msg)
specifier|protected
name|void
name|save
parameter_list|(
name|PersonIdent
name|ident
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|super
operator|.
name|save
argument_list|(
name|ident
argument_list|,
name|msg
argument_list|)
expr_stmt|;
comment|// we need to invalidate the JGit cache if the group list is invalidated in
comment|// an unattended init step
name|RepositoryCache
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|saveConfig
argument_list|(
name|ProjectConfig
operator|.
name|PROJECT_CONFIG
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|saveGroupList
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|saveGroupList ()
specifier|private
name|void
name|saveGroupList
parameter_list|()
throws|throws
name|IOException
block|{
name|saveUTF8
argument_list|(
name|GroupList
operator|.
name|FILE_NAME
argument_list|,
name|groupList
operator|.
name|asText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

