begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.group.db
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|db
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
name|flogger
operator|.
name|FluentLogger
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
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|entities
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
name|git
operator|.
name|DefaultQueueOp
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
name|WorkQueue
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
name|meta
operator|.
name|MetaDataUpdate
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
name|ProjectCache
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
name|List
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
name|Future
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
name|errors
operator|.
name|RepositoryNotFoundException
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

begin_class
DECL|class|RenameGroupOp
class|class
name|RenameGroupOp
extends|extends
name|DefaultQueueOp
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ( @ssistedR) PersonIdent author, @Assisted AccountGroup.UUID uuid, @Assisted(R) String oldName, @Assisted(R) String newName)
name|RenameGroupOp
name|create
parameter_list|(
annotation|@
name|Assisted
argument_list|(
literal|"author"
argument_list|)
name|PersonIdent
name|author
parameter_list|,
annotation|@
name|Assisted
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"oldName"
argument_list|)
name|String
name|oldName
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"newName"
argument_list|)
name|String
name|newName
parameter_list|)
function_decl|;
block|}
DECL|field|MAX_TRIES
specifier|private
specifier|static
specifier|final
name|int
name|MAX_TRIES
init|=
literal|10
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|projectConfigFactory
specifier|private
specifier|final
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
decl_stmt|;
DECL|field|author
specifier|private
specifier|final
name|PersonIdent
name|author
decl_stmt|;
DECL|field|uuid
specifier|private
specifier|final
name|AccountGroup
operator|.
name|UUID
name|uuid
decl_stmt|;
DECL|field|oldName
specifier|private
specifier|final
name|String
name|oldName
decl_stmt|;
DECL|field|newName
specifier|private
specifier|final
name|String
name|newName
decl_stmt|;
DECL|field|retryOn
specifier|private
specifier|final
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|retryOn
decl_stmt|;
DECL|field|tryingAgain
specifier|private
name|boolean
name|tryingAgain
decl_stmt|;
annotation|@
name|Inject
DECL|method|RenameGroupOp ( WorkQueue workQueue, ProjectCache projectCache, MetaDataUpdate.Server metaDataUpdateFactory, ProjectConfig.Factory projectConfigFactory, @Assisted(R) PersonIdent author, @Assisted AccountGroup.UUID uuid, @Assisted(R) String oldName, @Assisted(R) String newName)
specifier|public
name|RenameGroupOp
parameter_list|(
name|WorkQueue
name|workQueue
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
parameter_list|,
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"author"
argument_list|)
name|PersonIdent
name|author
parameter_list|,
annotation|@
name|Assisted
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"oldName"
argument_list|)
name|String
name|oldName
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"newName"
argument_list|)
name|String
name|newName
parameter_list|)
block|{
name|super
argument_list|(
name|workQueue
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|projectConfigFactory
operator|=
name|projectConfigFactory
expr_stmt|;
name|this
operator|.
name|author
operator|=
name|author
expr_stmt|;
name|this
operator|.
name|uuid
operator|=
name|uuid
expr_stmt|;
name|this
operator|.
name|oldName
operator|=
name|oldName
expr_stmt|;
name|this
operator|.
name|newName
operator|=
name|newName
expr_stmt|;
name|this
operator|.
name|retryOn
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|Iterable
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|names
init|=
name|tryingAgain
condition|?
name|retryOn
else|:
name|projectCache
operator|.
name|all
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|projectName
range|:
name|names
control|)
block|{
name|ProjectConfig
name|config
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|GroupReference
name|ref
init|=
name|config
operator|.
name|getGroup
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
operator|||
name|newName
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
try|try
init|(
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
init|)
block|{
name|rename
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|noProject
parameter_list|)
block|{
continue|continue;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|err
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot rename group %s in %s"
argument_list|,
name|oldName
argument_list|,
name|projectName
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If one or more projects did not update, wait 5 minutes and give it
comment|// another attempt. If it doesn't update after that, give up.
if|if
condition|(
operator|!
name|retryOn
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|tryingAgain
condition|)
block|{
name|tryingAgain
operator|=
literal|true
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|start
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
decl_stmt|;
block|}
block|}
DECL|method|rename (MetaDataUpdate md)
specifier|private
name|void
name|rename
parameter_list|(
name|MetaDataUpdate
name|md
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|attempts
init|=
literal|0
init|;
operator|!
name|success
operator|&&
name|attempts
operator|<
name|MAX_TRIES
condition|;
name|attempts
operator|++
control|)
block|{
name|ProjectConfig
name|config
init|=
name|projectConfigFactory
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
comment|// The group isn't referenced, or its name has been fixed already.
comment|//
name|GroupReference
name|ref
init|=
name|config
operator|.
name|getGroup
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
operator|||
name|newName
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|ref
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
literal|"Rename group "
operator|+
name|oldName
operator|+
literal|" to "
operator|+
name|newName
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
try|try
block|{
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Could not commit rename of group %s to %s in %s"
argument_list|,
name|oldName
argument_list|,
name|newName
argument_list|,
name|md
operator|.
name|getProjectName
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|25
comment|/* milliseconds */
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|wakeUp
parameter_list|)
block|{
continue|continue;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|success
condition|)
block|{
if|if
condition|(
name|tryingAgain
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Could not rename group %s to %s in %s"
argument_list|,
name|oldName
argument_list|,
name|newName
argument_list|,
name|md
operator|.
name|getProjectName
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retryOn
operator|.
name|add
argument_list|(
name|md
operator|.
name|getProjectName
argument_list|()
argument_list|)
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
literal|"Rename Group "
operator|+
name|oldName
return|;
block|}
block|}
end_class

end_unit

