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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|spearce
operator|.
name|jgit
operator|.
name|errors
operator|.
name|NoRemoteRepositoryException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|errors
operator|.
name|NotSupportedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|errors
operator|.
name|TransportException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|NullProgressMonitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Ref
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|spearce
operator|.
name|jgit
operator|.
name|transport
operator|.
name|FetchConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|transport
operator|.
name|PushResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|spearce
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
name|spearce
operator|.
name|jgit
operator|.
name|transport
operator|.
name|RemoteRefUpdate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|transport
operator|.
name|Transport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_comment
comment|/**  * A push to remote operation started by {@link ReplicationQueue}.  *<p>  * Instance members are protected by the lock within PushQueue. Callers must  * take that lock to ensure they are working with a current view of the object.  */
end_comment

begin_class
DECL|class|PushOp
class|class
name|PushOp
implements|implements
name|Runnable
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (String d, URIish u)
name|PushOp
name|create
parameter_list|(
name|String
name|d
parameter_list|,
name|URIish
name|u
parameter_list|)
function_decl|;
block|}
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|PushReplication
operator|.
name|log
decl_stmt|;
DECL|field|MIRROR_ALL
specifier|static
specifier|final
name|String
name|MIRROR_ALL
init|=
literal|"..all.."
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|pool
specifier|private
specifier|final
name|PushReplication
operator|.
name|ReplicationConfig
name|pool
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|RemoteConfig
name|config
decl_stmt|;
DECL|field|delta
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|delta
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|projectName
specifier|private
specifier|final
name|String
name|projectName
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|URIish
name|uri
decl_stmt|;
DECL|field|mirror
specifier|private
name|boolean
name|mirror
decl_stmt|;
DECL|field|db
specifier|private
name|Repository
name|db
decl_stmt|;
annotation|@
name|Inject
DECL|method|PushOp (final GitRepositoryManager grm, final PushReplication.ReplicationConfig p, final RemoteConfig c, @Assisted final String d, @Assisted final URIish u)
name|PushOp
parameter_list|(
specifier|final
name|GitRepositoryManager
name|grm
parameter_list|,
specifier|final
name|PushReplication
operator|.
name|ReplicationConfig
name|p
parameter_list|,
specifier|final
name|RemoteConfig
name|c
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|String
name|d
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|URIish
name|u
parameter_list|)
block|{
name|repoManager
operator|=
name|grm
expr_stmt|;
name|pool
operator|=
name|p
expr_stmt|;
name|config
operator|=
name|c
expr_stmt|;
name|projectName
operator|=
name|d
expr_stmt|;
name|uri
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getURI ()
name|URIish
name|getURI
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|addRef (final String ref)
name|void
name|addRef
parameter_list|(
specifier|final
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|MIRROR_ALL
operator|.
name|equals
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|delta
operator|.
name|clear
argument_list|()
expr_stmt|;
name|mirror
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|mirror
condition|)
block|{
name|delta
operator|.
name|add
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|// Lock the queue, and remove ourselves, so we can't be modified once
comment|// we start replication (instead a new instance, with the same URI, is
comment|// created and scheduled for a future point in time.)
comment|//
name|pool
operator|.
name|notifyStarting
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|db
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|runImpl
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot replicate "
operator|+
name|projectName
operator|+
literal|"; "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoRemoteRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot replicate to "
operator|+
name|uri
operator|+
literal|"; repository not found"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NotSupportedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot replicate to "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransportException
name|e
parameter_list|)
block|{
specifier|final
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|instanceof
name|JSchException
operator|&&
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"UnknownHostKey:"
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot replicate to "
operator|+
name|uri
operator|+
literal|": "
operator|+
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot replicate to "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot replicate to "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unexpected error during replication to "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unexpected error during replication to "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|(
name|mirror
condition|?
literal|"mirror "
else|:
literal|"push "
operator|)
operator|+
name|uri
return|;
block|}
DECL|method|runImpl ()
specifier|private
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Transport
name|tn
init|=
name|Transport
operator|.
name|open
argument_list|(
name|db
argument_list|,
name|uri
argument_list|)
decl_stmt|;
specifier|final
name|PushResult
name|res
decl_stmt|;
try|try
block|{
name|res
operator|=
name|pushVia
argument_list|(
name|tn
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|tn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e2
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unexpected error while closing "
operator|+
name|uri
argument_list|,
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
specifier|final
name|RemoteRefUpdate
name|u
range|:
name|res
operator|.
name|getRemoteUpdates
argument_list|()
control|)
block|{
switch|switch
condition|(
name|u
operator|.
name|getStatus
argument_list|()
condition|)
block|{
case|case
name|OK
case|:
case|case
name|UP_TO_DATE
case|:
case|case
name|NON_EXISTING
case|:
break|break;
case|case
name|NOT_ATTEMPTED
case|:
case|case
name|AWAITING_REPORT
case|:
case|case
name|REJECTED_NODELETE
case|:
case|case
name|REJECTED_NONFASTFORWARD
case|:
case|case
name|REJECTED_REMOTE_CHANGED
case|:
name|log
operator|.
name|error
argument_list|(
literal|"Failed replicate of "
operator|+
name|u
operator|.
name|getRemoteName
argument_list|()
operator|+
literal|" to "
operator|+
name|uri
operator|+
literal|": status "
operator|+
name|u
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REJECTED_OTHER_REASON
case|:
name|log
operator|.
name|error
argument_list|(
literal|"Failed replicate of "
operator|+
name|u
operator|.
name|getRemoteName
argument_list|()
operator|+
literal|" to "
operator|+
name|uri
operator|+
literal|", reason: "
operator|+
name|u
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
DECL|method|pushVia (final Transport tn)
specifier|private
name|PushResult
name|pushVia
parameter_list|(
specifier|final
name|Transport
name|tn
parameter_list|)
throws|throws
name|IOException
throws|,
name|NotSupportedException
throws|,
name|TransportException
block|{
name|tn
operator|.
name|applyConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|RemoteRefUpdate
argument_list|>
name|todo
init|=
name|generateUpdates
argument_list|(
name|tn
argument_list|)
decl_stmt|;
if|if
condition|(
name|todo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// If we have no commands selected, we have nothing to do.
comment|// Calling JGit at this point would just redo the work we
comment|// already did, and come up with the same answer. Instead
comment|// send back an empty result.
comment|//
return|return
operator|new
name|PushResult
argument_list|()
return|;
block|}
return|return
name|tn
operator|.
name|push
argument_list|(
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|,
name|todo
argument_list|)
return|;
block|}
DECL|method|generateUpdates (final Transport tn)
specifier|private
name|List
argument_list|<
name|RemoteRefUpdate
argument_list|>
name|generateUpdates
parameter_list|(
specifier|final
name|Transport
name|tn
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|List
argument_list|<
name|RemoteRefUpdate
argument_list|>
name|cmds
init|=
operator|new
name|ArrayList
argument_list|<
name|RemoteRefUpdate
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|local
init|=
name|db
operator|.
name|getAllRefs
argument_list|()
decl_stmt|;
if|if
condition|(
name|mirror
condition|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|remote
init|=
name|listRemote
argument_list|(
name|tn
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Ref
name|src
range|:
name|local
operator|.
name|values
argument_list|()
control|)
block|{
specifier|final
name|RefSpec
name|spec
init|=
name|matchSrc
argument_list|(
name|src
operator|.
name|getOrigName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|spec
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Ref
name|dst
init|=
name|remote
operator|.
name|get
argument_list|(
name|spec
operator|.
name|getDestination
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dst
operator|==
literal|null
operator|||
operator|!
name|src
operator|.
name|getObjectId
argument_list|()
operator|.
name|equals
argument_list|(
name|dst
operator|.
name|getObjectId
argument_list|()
argument_list|)
condition|)
block|{
comment|// Doesn't exist yet, or isn't the same value, request to push.
comment|//
name|send
argument_list|(
name|cmds
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
specifier|final
name|Ref
name|ref
range|:
name|remote
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|isHEAD
argument_list|(
name|ref
argument_list|)
condition|)
block|{
specifier|final
name|RefSpec
name|spec
init|=
name|matchDst
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|spec
operator|!=
literal|null
operator|&&
operator|!
name|local
operator|.
name|containsKey
argument_list|(
name|spec
operator|.
name|getSource
argument_list|()
argument_list|)
condition|)
block|{
comment|// No longer on local side, request removal.
comment|//
name|delete
argument_list|(
name|cmds
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
for|for
control|(
specifier|final
name|String
name|src
range|:
name|delta
control|)
block|{
specifier|final
name|RefSpec
name|spec
init|=
name|matchSrc
argument_list|(
name|src
argument_list|)
decl_stmt|;
if|if
condition|(
name|spec
operator|!=
literal|null
condition|)
block|{
comment|// If the ref still exists locally, send it, otherwise delete it.
comment|//
if|if
condition|(
name|local
operator|.
name|containsKey
argument_list|(
name|src
argument_list|)
condition|)
block|{
name|send
argument_list|(
name|cmds
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|delete
argument_list|(
name|cmds
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|cmds
return|;
block|}
DECL|method|listRemote (final Transport tn)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|listRemote
parameter_list|(
specifier|final
name|Transport
name|tn
parameter_list|)
throws|throws
name|NotSupportedException
throws|,
name|TransportException
block|{
specifier|final
name|FetchConnection
name|fc
init|=
name|tn
operator|.
name|openFetch
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|fc
operator|.
name|getRefsMap
argument_list|()
return|;
block|}
finally|finally
block|{
name|fc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|matchSrc (final String ref)
specifier|private
name|RefSpec
name|matchSrc
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
name|config
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
name|s
operator|.
name|expandFromSource
argument_list|(
name|ref
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|matchDst (final String ref)
specifier|private
name|RefSpec
name|matchDst
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
name|config
operator|.
name|getPushRefSpecs
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|matchDestination
argument_list|(
name|ref
argument_list|)
condition|)
block|{
return|return
name|s
operator|.
name|expandFromDestination
argument_list|(
name|ref
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|send (final List<RemoteRefUpdate> cmds, final RefSpec spec)
specifier|private
name|void
name|send
parameter_list|(
specifier|final
name|List
argument_list|<
name|RemoteRefUpdate
argument_list|>
name|cmds
parameter_list|,
specifier|final
name|RefSpec
name|spec
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|String
name|src
init|=
name|spec
operator|.
name|getSource
argument_list|()
decl_stmt|;
specifier|final
name|String
name|dst
init|=
name|spec
operator|.
name|getDestination
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|force
init|=
name|spec
operator|.
name|isForceUpdate
argument_list|()
decl_stmt|;
name|cmds
operator|.
name|add
argument_list|(
operator|new
name|RemoteRefUpdate
argument_list|(
name|db
argument_list|,
name|src
argument_list|,
name|dst
argument_list|,
name|force
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (final List<RemoteRefUpdate> cmds, final RefSpec spec)
specifier|private
name|void
name|delete
parameter_list|(
specifier|final
name|List
argument_list|<
name|RemoteRefUpdate
argument_list|>
name|cmds
parameter_list|,
specifier|final
name|RefSpec
name|spec
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|String
name|dst
init|=
name|spec
operator|.
name|getDestination
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|force
init|=
name|spec
operator|.
name|isForceUpdate
argument_list|()
decl_stmt|;
name|cmds
operator|.
name|add
argument_list|(
operator|new
name|RemoteRefUpdate
argument_list|(
name|db
argument_list|,
literal|null
argument_list|,
name|dst
argument_list|,
name|force
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isHEAD (final Ref ref)
specifier|private
specifier|static
name|boolean
name|isHEAD
parameter_list|(
specifier|final
name|Ref
name|ref
parameter_list|)
block|{
return|return
name|Constants
operator|.
name|HEAD
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getOrigName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

