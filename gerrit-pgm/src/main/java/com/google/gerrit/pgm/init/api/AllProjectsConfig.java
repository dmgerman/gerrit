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
name|git
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
name|git
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
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|VersionedMetaData
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
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|FileRepository
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
name|ObjectId
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
name|RefUpdate
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
name|Repository
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RepositoryCache
operator|.
name|FileKey
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
name|revwalk
operator|.
name|RevTree
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
name|revwalk
operator|.
name|RevWalk
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
name|util
operator|.
name|FS
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_class
DECL|class|AllProjectsConfig
specifier|public
class|class
name|AllProjectsConfig
extends|extends
name|VersionedMetaData
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
DECL|field|project
specifier|private
specifier|final
name|String
name|project
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|flags
specifier|private
specifier|final
name|InitFlags
name|flags
decl_stmt|;
DECL|field|cfg
specifier|private
name|Config
name|cfg
decl_stmt|;
DECL|field|revision
specifier|private
name|ObjectId
name|revision
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
name|this
operator|.
name|project
operator|=
name|allProjects
operator|.
name|get
argument_list|()
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|flags
operator|=
name|flags
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
DECL|method|getPath ()
specifier|private
name|File
name|getPath
parameter_list|()
block|{
name|Path
name|basePath
init|=
name|site
operator|.
name|resolve
argument_list|(
name|flags
operator|.
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"basePath"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"gerrit.basePath must be configured"
argument_list|)
throw|;
block|}
return|return
name|FileKey
operator|.
name|resolve
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
name|project
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
return|;
block|}
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
name|File
name|path
init|=
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|path
argument_list|)
init|)
block|{
name|load
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
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
name|revision
operator|=
name|getRevision
argument_list|()
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|save (String message)
specifier|public
name|void
name|save
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|save
argument_list|(
operator|new
name|PersonIdent
argument_list|(
literal|"Gerrit Initialization"
argument_list|,
literal|"init@gerrit"
argument_list|)
argument_list|,
name|message
argument_list|)
expr_stmt|;
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
DECL|method|save (PersonIdent ident, String msg)
specifier|private
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
block|{
name|File
name|path
init|=
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"All-Projects does not exist."
argument_list|)
throw|;
block|}
try|try
init|(
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|path
argument_list|)
init|)
block|{
name|inserter
operator|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
expr_stmt|;
name|reader
operator|=
name|repo
operator|.
name|newObjectReader
argument_list|()
expr_stmt|;
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
name|RevTree
name|srcTree
init|=
name|revision
operator|!=
literal|null
condition|?
name|rw
operator|.
name|parseTree
argument_list|(
name|revision
argument_list|)
else|:
literal|null
decl_stmt|;
name|newTree
operator|=
name|readTree
argument_list|(
name|srcTree
argument_list|)
expr_stmt|;
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
name|ObjectId
name|res
init|=
name|newTree
operator|.
name|writeTree
argument_list|(
name|inserter
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|.
name|equals
argument_list|(
name|srcTree
argument_list|)
condition|)
block|{
comment|// If there are no changes to the content, don't create the commit.
return|return;
block|}
name|CommitBuilder
name|commit
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|commit
operator|.
name|setAuthor
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setCommitter
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setTreeId
argument_list|(
name|res
argument_list|)
expr_stmt|;
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
name|commit
operator|.
name|addParentId
argument_list|(
name|revision
argument_list|)
expr_stmt|;
block|}
name|ObjectId
name|newRevision
init|=
name|inserter
operator|.
name|insert
argument_list|(
name|commit
argument_list|)
decl_stmt|;
name|updateRef
argument_list|(
name|repo
argument_list|,
name|ident
argument_list|,
name|newRevision
argument_list|,
literal|"commit: "
operator|+
name|msg
argument_list|)
expr_stmt|;
name|revision
operator|=
name|newRevision
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|inserter
operator|!=
literal|null
condition|)
block|{
name|inserter
operator|.
name|close
argument_list|()
expr_stmt|;
name|inserter
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|reader
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|// we need to invalidate the JGit cache if the group list is invalidated in
comment|// an unattended init step
name|RepositoryCache
operator|.
name|clear
argument_list|()
expr_stmt|;
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
DECL|method|updateRef (Repository repo, PersonIdent ident, ObjectId newRevision, String refLogMsg)
specifier|private
name|void
name|updateRef
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|PersonIdent
name|ident
parameter_list|,
name|ObjectId
name|newRevision
parameter_list|,
name|String
name|refLogMsg
parameter_list|)
throws|throws
name|IOException
block|{
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setRefLogIdent
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|newRevision
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|revision
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogMessage
argument_list|(
name|refLogMsg
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|RefUpdate
operator|.
name|Result
name|r
init|=
name|ru
operator|.
name|update
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|r
condition|)
block|{
case|case
name|FAST_FORWARD
case|:
case|case
name|NEW
case|:
case|case
name|NO_CHANGE
case|:
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to update "
operator|+
name|getRefName
argument_list|()
operator|+
literal|" of "
operator|+
name|project
operator|+
literal|": "
operator|+
name|r
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

