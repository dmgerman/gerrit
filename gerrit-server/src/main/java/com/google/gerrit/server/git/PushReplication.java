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
name|gerrit
operator|.
name|reviewdb
operator|.
name|AccountGroup
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
name|ReviewDb
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
name|CurrentUser
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
name|ReplicationUser
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
name|ConfigUtil
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
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|AbstractModule
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
name|Injector
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
name|Singleton
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
name|FactoryProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelExec
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Session
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
name|FileBasedConfig
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
name|transport
operator|.
name|OpenSshConfig
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
name|transport
operator|.
name|RefSpec
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
name|transport
operator|.
name|RemoteConfig
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
name|transport
operator|.
name|SshConfigSessionFactory
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
name|transport
operator|.
name|SshSessionFactory
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
name|transport
operator|.
name|URIish
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
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|QuotedString
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/** Manages automatic replication to remote repositories. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PushReplication
specifier|public
class|class
name|PushReplication
implements|implements
name|ReplicationQueue
block|{
DECL|field|log
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PushReplication
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
comment|// Install our own factory which always runs in batch mode, as we
comment|// have no UI available for interactive prompting.
comment|//
name|SshSessionFactory
operator|.
name|setInstance
argument_list|(
operator|new
name|SshConfigSessionFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|(
name|OpenSshConfig
operator|.
name|Host
name|hc
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
comment|// Default configuration is batch mode.
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|field|injector
specifier|private
specifier|final
name|Injector
name|injector
decl_stmt|;
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|field|configs
specifier|private
specifier|final
name|List
argument_list|<
name|ReplicationConfig
argument_list|>
name|configs
decl_stmt|;
DECL|field|database
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|database
decl_stmt|;
DECL|field|replicationUserFactory
specifier|private
specifier|final
name|ReplicationUser
operator|.
name|Factory
name|replicationUserFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|PushReplication (final Injector i, final WorkQueue wq, final SitePaths site, final ReplicationUser.Factory ruf, final SchemaFactory<ReviewDb> db)
name|PushReplication
parameter_list|(
specifier|final
name|Injector
name|i
parameter_list|,
specifier|final
name|WorkQueue
name|wq
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|ReplicationUser
operator|.
name|Factory
name|ruf
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|injector
operator|=
name|i
expr_stmt|;
name|workQueue
operator|=
name|wq
expr_stmt|;
name|database
operator|=
name|db
expr_stmt|;
name|replicationUserFactory
operator|=
name|ruf
expr_stmt|;
name|configs
operator|=
name|allConfigs
argument_list|(
name|site
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|configs
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleFullSync (final Project.NameKey project, final String urlMatch)
specifier|public
name|void
name|scheduleFullSync
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|String
name|urlMatch
parameter_list|)
block|{
for|for
control|(
specifier|final
name|ReplicationConfig
name|cfg
range|:
name|configs
control|)
block|{
for|for
control|(
specifier|final
name|URIish
name|uri
range|:
name|cfg
operator|.
name|getURIs
argument_list|(
name|project
argument_list|,
name|urlMatch
argument_list|)
control|)
block|{
name|cfg
operator|.
name|schedule
argument_list|(
name|project
argument_list|,
name|PushOp
operator|.
name|MIRROR_ALL
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|scheduleUpdate (final Project.NameKey project, final String ref)
specifier|public
name|void
name|scheduleUpdate
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|String
name|ref
parameter_list|)
block|{
for|for
control|(
specifier|final
name|ReplicationConfig
name|cfg
range|:
name|configs
control|)
block|{
if|if
condition|(
name|cfg
operator|.
name|wouldPushRef
argument_list|(
name|ref
argument_list|)
condition|)
block|{
for|for
control|(
specifier|final
name|URIish
name|uri
range|:
name|cfg
operator|.
name|getURIs
argument_list|(
name|project
argument_list|,
literal|null
argument_list|)
control|)
block|{
name|cfg
operator|.
name|schedule
argument_list|(
name|project
argument_list|,
name|ref
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|replace (final String pat, final String key, final String val)
specifier|private
specifier|static
name|String
name|replace
parameter_list|(
specifier|final
name|String
name|pat
parameter_list|,
specifier|final
name|String
name|key
parameter_list|,
specifier|final
name|String
name|val
parameter_list|)
block|{
specifier|final
name|int
name|n
init|=
name|pat
operator|.
name|indexOf
argument_list|(
literal|"${"
operator|+
name|key
operator|+
literal|"}"
argument_list|)
decl_stmt|;
return|return
name|pat
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|n
argument_list|)
operator|+
name|val
operator|+
name|pat
operator|.
name|substring
argument_list|(
name|n
operator|+
literal|3
operator|+
name|key
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
DECL|method|allConfigs (final SitePaths site)
specifier|private
name|List
argument_list|<
name|ReplicationConfig
argument_list|>
name|allConfigs
parameter_list|(
specifier|final
name|SitePaths
name|site
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
specifier|final
name|FileBasedConfig
name|cfg
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|site
operator|.
name|replication_config
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cfg
operator|.
name|getFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No "
operator|+
name|cfg
operator|.
name|getFile
argument_list|()
operator|+
literal|"; not replicating"
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
if|if
condition|(
name|cfg
operator|.
name|getFile
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Empty "
operator|+
name|cfg
operator|.
name|getFile
argument_list|()
operator|+
literal|"; not replicating"
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
try|try
block|{
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"Config file "
operator|+
name|cfg
operator|.
name|getFile
argument_list|()
operator|+
literal|" is invalid: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot read "
operator|+
name|cfg
operator|.
name|getFile
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
name|List
argument_list|<
name|ReplicationConfig
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|ReplicationConfig
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|RemoteConfig
name|c
range|:
name|allRemotes
argument_list|(
name|cfg
argument_list|)
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getURIs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
for|for
control|(
specifier|final
name|URIish
name|u
range|:
name|c
operator|.
name|getURIs
argument_list|()
control|)
block|{
if|if
condition|(
name|u
operator|.
name|getPath
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|u
operator|.
name|getPath
argument_list|()
operator|.
name|contains
argument_list|(
literal|"${name}"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"remote."
operator|+
name|c
operator|.
name|getName
argument_list|()
operator|+
literal|".url"
operator|+
literal|" \""
operator|+
name|u
operator|+
literal|"\" lacks ${name} placeholder in "
operator|+
name|cfg
operator|.
name|getFile
argument_list|()
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|c
operator|.
name|getPushRefSpecs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|RefSpec
name|spec
init|=
operator|new
name|RefSpec
argument_list|()
decl_stmt|;
name|spec
operator|=
name|spec
operator|.
name|setSourceDestination
argument_list|(
literal|"refs/*"
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|spec
operator|=
name|spec
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|c
operator|.
name|addPushRefSpec
argument_list|(
name|spec
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|add
argument_list|(
operator|new
name|ReplicationConfig
argument_list|(
name|injector
argument_list|,
name|workQueue
argument_list|,
name|c
argument_list|,
name|cfg
argument_list|,
name|database
argument_list|,
name|replicationUserFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|r
argument_list|)
return|;
block|}
DECL|method|allRemotes (final FileBasedConfig cfg)
specifier|private
name|List
argument_list|<
name|RemoteConfig
argument_list|>
name|allRemotes
parameter_list|(
specifier|final
name|FileBasedConfig
name|cfg
parameter_list|)
throws|throws
name|ConfigInvalidException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|cfg
operator|.
name|getSubsections
argument_list|(
literal|"remote"
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|names
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|RemoteConfig
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|RemoteConfig
argument_list|>
argument_list|(
name|names
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|name
range|:
name|names
control|)
block|{
try|try
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|RemoteConfig
argument_list|(
name|cfg
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"remote "
operator|+
name|name
operator|+
literal|" has invalid URL in "
operator|+
name|cfg
operator|.
name|getFile
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|replicateNewProject (Project.NameKey projectName, String head)
specifier|public
name|void
name|replicateNewProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|head
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isEnabled
argument_list|()
condition|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|ReplicationConfig
argument_list|>
name|configIter
init|=
name|configs
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|configIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ReplicationConfig
name|rp
init|=
name|configIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|URIish
argument_list|>
name|uriList
init|=
name|rp
operator|.
name|getURIs
argument_list|(
name|projectName
argument_list|,
literal|"*"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|URIish
argument_list|>
name|uriIter
init|=
name|uriList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|uriIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|replicateProject
argument_list|(
name|uriIter
operator|.
name|next
argument_list|()
argument_list|,
name|head
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|replicateProject (final URIish replicateURI, final String head)
specifier|private
name|void
name|replicateProject
parameter_list|(
specifier|final
name|URIish
name|replicateURI
parameter_list|,
specifier|final
name|String
name|head
parameter_list|)
block|{
name|SshSessionFactory
name|sshFactory
init|=
name|SshSessionFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Session
name|sshSession
decl_stmt|;
name|String
name|projectPath
init|=
name|QuotedString
operator|.
name|BOURNE
operator|.
name|quote
argument_list|(
name|replicateURI
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|usingSSH
argument_list|(
name|replicateURI
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot create new project on remote site since the connection "
operator|+
literal|"method is not SSH: "
operator|+
name|replicateURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|OutputStream
name|errStream
init|=
name|createErrStream
argument_list|()
decl_stmt|;
name|String
name|cmd
init|=
literal|"mkdir -p "
operator|+
name|projectPath
operator|+
literal|"&& cd "
operator|+
name|projectPath
operator|+
literal|"&& git init --bare"
operator|+
literal|"&& git symbolic-ref HEAD "
operator|+
name|QuotedString
operator|.
name|BOURNE
operator|.
name|quote
argument_list|(
name|head
argument_list|)
decl_stmt|;
try|try
block|{
name|sshSession
operator|=
name|sshFactory
operator|.
name|getSession
argument_list|(
name|replicateURI
operator|.
name|getUser
argument_list|()
argument_list|,
name|replicateURI
operator|.
name|getPass
argument_list|()
argument_list|,
name|replicateURI
operator|.
name|getHost
argument_list|()
argument_list|,
name|replicateURI
operator|.
name|getPort
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
expr_stmt|;
name|sshSession
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Channel
name|channel
init|=
name|sshSession
operator|.
name|openChannel
argument_list|(
literal|"exec"
argument_list|)
decl_stmt|;
operator|(
operator|(
name|ChannelExec
operator|)
name|channel
operator|)
operator|.
name|setCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setInputStream
argument_list|(
literal|null
argument_list|)
expr_stmt|;
operator|(
operator|(
name|ChannelExec
operator|)
name|channel
operator|)
operator|.
name|setErrStream
argument_list|(
name|errStream
argument_list|)
expr_stmt|;
name|channel
operator|.
name|connect
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|channel
operator|.
name|isClosed
argument_list|()
condition|)
block|{
try|try
block|{
specifier|final
name|int
name|delay
init|=
literal|50
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{         }
block|}
name|channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|sshSession
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Communication error when trying to replicate to: "
operator|+
name|replicateURI
operator|.
name|toString
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Error reported: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Error in communication: "
operator|+
name|errStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createErrStream ()
specifier|private
name|OutputStream
name|createErrStream
parameter_list|()
block|{
return|return
operator|new
name|OutputStream
argument_list|()
block|{
specifier|private
name|StringBuilder
name|all
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|private
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|r
init|=
name|all
operator|.
name|toString
argument_list|()
decl_stmt|;
while|while
condition|(
name|r
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
name|r
operator|=
name|r
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|r
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b
operator|==
literal|'\r'
condition|)
block|{
return|return;
block|}
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|b
argument_list|)
expr_stmt|;
if|if
condition|(
name|b
operator|==
literal|'\n'
condition|)
block|{
name|all
operator|.
name|append
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
DECL|method|usingSSH (final URIish uri)
specifier|private
name|boolean
name|usingSSH
parameter_list|(
specifier|final
name|URIish
name|uri
parameter_list|)
block|{
specifier|final
name|String
name|scheme
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|uri
operator|.
name|isRemote
argument_list|()
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|scheme
operator|!=
literal|null
operator|&&
name|scheme
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"ssh"
argument_list|)
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|scheme
operator|==
literal|null
operator|&&
name|uri
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
condition|)
return|return
literal|true
return|;
return|return
literal|false
return|;
block|}
DECL|class|ReplicationConfig
specifier|static
class|class
name|ReplicationConfig
block|{
DECL|field|remote
specifier|private
specifier|final
name|RemoteConfig
name|remote
decl_stmt|;
DECL|field|delay
specifier|private
specifier|final
name|int
name|delay
decl_stmt|;
DECL|field|pool
specifier|private
specifier|final
name|WorkQueue
operator|.
name|Executor
name|pool
decl_stmt|;
DECL|field|pending
specifier|private
specifier|final
name|Map
argument_list|<
name|URIish
argument_list|,
name|PushOp
argument_list|>
name|pending
init|=
operator|new
name|HashMap
argument_list|<
name|URIish
argument_list|,
name|PushOp
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|opFactory
specifier|private
specifier|final
name|PushOp
operator|.
name|Factory
name|opFactory
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
DECL|method|ReplicationConfig (final Injector injector, final WorkQueue workQueue, final RemoteConfig rc, final Config cfg, SchemaFactory<ReviewDb> db, final ReplicationUser.Factory replicationUserFactory)
name|ReplicationConfig
parameter_list|(
specifier|final
name|Injector
name|injector
parameter_list|,
specifier|final
name|WorkQueue
name|workQueue
parameter_list|,
specifier|final
name|RemoteConfig
name|rc
parameter_list|,
specifier|final
name|Config
name|cfg
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
specifier|final
name|ReplicationUser
operator|.
name|Factory
name|replicationUserFactory
parameter_list|)
block|{
name|remote
operator|=
name|rc
expr_stmt|;
name|delay
operator|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|getInt
argument_list|(
name|rc
argument_list|,
name|cfg
argument_list|,
literal|"replicationdelay"
argument_list|,
literal|15
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|int
name|poolSize
init|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|getInt
argument_list|(
name|rc
argument_list|,
name|cfg
argument_list|,
literal|"threads"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|String
name|poolName
init|=
literal|"ReplicateTo-"
operator|+
name|rc
operator|.
name|getName
argument_list|()
decl_stmt|;
name|pool
operator|=
name|workQueue
operator|.
name|createQueue
argument_list|(
name|poolSize
argument_list|,
name|poolName
argument_list|)
expr_stmt|;
name|String
index|[]
name|authGroupNames
init|=
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"remote"
argument_list|,
name|rc
operator|.
name|getName
argument_list|()
argument_list|,
literal|"authGroup"
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|authGroups
decl_stmt|;
if|if
condition|(
name|authGroupNames
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|authGroups
operator|=
name|ConfigUtil
operator|.
name|groupsFor
argument_list|(
name|db
argument_list|,
name|authGroupNames
argument_list|,
comment|//
name|log
argument_list|,
literal|"Group \"{0}\" not in database, removing from authGroup"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|authGroups
operator|=
name|ReplicationUser
operator|.
name|EVERYTHING_VISIBLE
expr_stmt|;
block|}
specifier|final
name|ReplicationUser
name|remoteUser
init|=
name|replicationUserFactory
operator|.
name|create
argument_list|(
name|authGroups
argument_list|)
decl_stmt|;
name|projectControlFactory
operator|=
name|injector
operator|.
name|createChildInjector
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|CurrentUser
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|remoteUser
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|getInstance
argument_list|(
name|ProjectControl
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|opFactory
operator|=
name|injector
operator|.
name|createChildInjector
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|PushReplication
operator|.
name|ReplicationConfig
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|ReplicationConfig
operator|.
name|this
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|RemoteConfig
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|remote
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PushOp
operator|.
name|Factory
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FactoryProvider
operator|.
name|newFactory
argument_list|(
name|PushOp
operator|.
name|Factory
operator|.
name|class
argument_list|,
name|PushOp
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|getInstance
argument_list|(
name|PushOp
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getInt (final RemoteConfig rc, final Config cfg, final String name, final int defValue)
specifier|private
name|int
name|getInt
parameter_list|(
specifier|final
name|RemoteConfig
name|rc
parameter_list|,
specifier|final
name|Config
name|cfg
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|int
name|defValue
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getInt
argument_list|(
literal|"remote"
argument_list|,
name|rc
operator|.
name|getName
argument_list|()
argument_list|,
name|name
argument_list|,
name|defValue
argument_list|)
return|;
block|}
DECL|method|schedule (final Project.NameKey project, final String ref, final URIish uri)
name|void
name|schedule
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|String
name|ref
parameter_list|,
specifier|final
name|URIish
name|uri
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|controlFor
argument_list|(
name|project
argument_list|)
operator|.
name|isVisible
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e1
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Internal error: project "
operator|+
name|project
operator|+
literal|" not found during replication"
argument_list|)
expr_stmt|;
return|return;
block|}
synchronized|synchronized
init|(
name|pending
init|)
block|{
name|PushOp
name|e
init|=
name|pending
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|opFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|pool
operator|.
name|schedule
argument_list|(
name|e
argument_list|,
name|delay
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|pending
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|addRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|controlFor (final Project.NameKey project)
name|ProjectControl
name|controlFor
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|NoSuchProjectException
block|{
return|return
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|project
argument_list|)
return|;
block|}
DECL|method|notifyStarting (final PushOp op)
name|void
name|notifyStarting
parameter_list|(
specifier|final
name|PushOp
name|op
parameter_list|)
block|{
synchronized|synchronized
init|(
name|pending
init|)
block|{
name|pending
operator|.
name|remove
argument_list|(
name|op
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|wouldPushRef (final String ref)
name|boolean
name|wouldPushRef
parameter_list|(
specifier|final
name|String
name|ref
parameter_list|)
block|{
for|for
control|(
specifier|final
name|RefSpec
name|s
range|:
name|remote
operator|.
name|getPushRefSpecs
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|matchSource
argument_list|(
name|ref
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|getURIs (final Project.NameKey project, final String urlMatch)
name|List
argument_list|<
name|URIish
argument_list|>
name|getURIs
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|String
name|urlMatch
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|URIish
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|URIish
argument_list|>
argument_list|(
name|remote
operator|.
name|getURIs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|URIish
name|uri
range|:
name|remote
operator|.
name|getURIs
argument_list|()
control|)
block|{
if|if
condition|(
name|matches
argument_list|(
name|uri
argument_list|,
name|urlMatch
argument_list|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|.
name|setPath
argument_list|(
name|replace
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"name"
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
DECL|method|matches (URIish uri, final String urlMatch)
specifier|private
name|boolean
name|matches
parameter_list|(
name|URIish
name|uri
parameter_list|,
specifier|final
name|String
name|urlMatch
parameter_list|)
block|{
if|if
condition|(
name|urlMatch
operator|==
literal|null
operator|||
name|urlMatch
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|||
name|urlMatch
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|uri
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|urlMatch
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

