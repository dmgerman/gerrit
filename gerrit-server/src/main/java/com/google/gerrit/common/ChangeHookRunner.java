begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|Account
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
name|ApprovalCategory
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
name|ApprovalCategoryValue
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
name|Change
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
name|PatchSet
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
name|IdentifiedUser
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
name|GerritServerConfig
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
name|GitRepositoryManager
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
name|ProjectControl
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
name|Singleton
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
name|Repository
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
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
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
name|Map
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
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * This class implements hooks for certain gerrit events.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeHookRunner
specifier|public
class|class
name|ChangeHookRunner
block|{
comment|/** A logger for this class. */
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
name|ChangeHookRunner
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|ChangeEvent
specifier|public
specifier|static
specifier|abstract
class|class
name|ChangeEvent
block|{     }
DECL|class|ApprovalAttribute
specifier|public
specifier|static
class|class
name|ApprovalAttribute
block|{
DECL|field|type
specifier|public
name|String
name|type
decl_stmt|;
DECL|field|value
specifier|public
name|String
name|value
decl_stmt|;
block|}
DECL|class|AuthorAttribute
specifier|public
specifier|static
class|class
name|AuthorAttribute
block|{
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|email
specifier|public
name|String
name|email
decl_stmt|;
block|}
DECL|class|CommentAddedEvent
specifier|public
specifier|static
class|class
name|CommentAddedEvent
extends|extends
name|ChangeEvent
block|{
DECL|field|type
specifier|public
specifier|final
name|String
name|type
init|=
literal|"comment-added"
decl_stmt|;
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|branch
specifier|public
name|String
name|branch
decl_stmt|;
DECL|field|change
specifier|public
name|String
name|change
decl_stmt|;
DECL|field|revision
specifier|public
name|String
name|revision
decl_stmt|;
DECL|field|author
specifier|public
name|AuthorAttribute
name|author
decl_stmt|;
DECL|field|approvals
specifier|public
name|ApprovalAttribute
index|[]
name|approvals
decl_stmt|;
DECL|field|comment
specifier|public
name|String
name|comment
decl_stmt|;
block|}
DECL|class|ChangeMergedEvent
specifier|public
specifier|static
class|class
name|ChangeMergedEvent
extends|extends
name|ChangeEvent
block|{
DECL|field|type
specifier|public
specifier|final
name|String
name|type
init|=
literal|"change-merged"
decl_stmt|;
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|branch
specifier|public
name|String
name|branch
decl_stmt|;
DECL|field|change
specifier|public
name|String
name|change
decl_stmt|;
DECL|field|patchSet
specifier|public
name|String
name|patchSet
decl_stmt|;
DECL|field|submitter
specifier|public
name|AuthorAttribute
name|submitter
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
block|}
DECL|class|ChangeAbandonedEvent
specifier|public
specifier|static
class|class
name|ChangeAbandonedEvent
extends|extends
name|ChangeEvent
block|{
DECL|field|type
specifier|public
specifier|final
name|String
name|type
init|=
literal|"change-abandoned"
decl_stmt|;
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|branch
specifier|public
name|String
name|branch
decl_stmt|;
DECL|field|change
specifier|public
name|String
name|change
decl_stmt|;
DECL|field|author
specifier|public
name|AuthorAttribute
name|author
decl_stmt|;
DECL|field|reason
specifier|public
name|String
name|reason
decl_stmt|;
block|}
DECL|class|PatchSetCreatedEvent
specifier|public
specifier|static
class|class
name|PatchSetCreatedEvent
extends|extends
name|ChangeEvent
block|{
DECL|field|type
specifier|public
specifier|final
name|String
name|type
init|=
literal|"patchset-created"
decl_stmt|;
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|branch
specifier|public
name|String
name|branch
decl_stmt|;
DECL|field|change
specifier|public
name|String
name|change
decl_stmt|;
DECL|field|commit
specifier|public
name|String
name|commit
decl_stmt|;
DECL|field|patchSet
specifier|public
name|String
name|patchSet
decl_stmt|;
block|}
DECL|class|ChangeListenerHolder
specifier|private
specifier|static
class|class
name|ChangeListenerHolder
block|{
DECL|field|listener
specifier|final
name|ChangeListener
name|listener
decl_stmt|;
DECL|field|user
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|method|ChangeListenerHolder (ChangeListener l, IdentifiedUser u)
name|ChangeListenerHolder
parameter_list|(
name|ChangeListener
name|l
parameter_list|,
name|IdentifiedUser
name|u
parameter_list|)
block|{
name|listener
operator|=
name|l
expr_stmt|;
name|user
operator|=
name|u
expr_stmt|;
block|}
block|}
comment|/** Listeners to receive changes as they happen. */
DECL|field|listeners
specifier|private
specifier|final
name|Map
argument_list|<
name|ChangeListener
argument_list|,
name|ChangeListenerHolder
argument_list|>
name|listeners
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|ChangeListener
argument_list|,
name|ChangeListenerHolder
argument_list|>
argument_list|()
decl_stmt|;
comment|/** Filename of the new patchset hook. */
DECL|field|patchsetCreatedHook
specifier|private
specifier|final
name|File
name|patchsetCreatedHook
decl_stmt|;
comment|/** Filename of the new comments hook. */
DECL|field|commentAddedHook
specifier|private
specifier|final
name|File
name|commentAddedHook
decl_stmt|;
comment|/** Filename of the change merged hook. */
DECL|field|changeMergedHook
specifier|private
specifier|final
name|File
name|changeMergedHook
decl_stmt|;
comment|/** Filename of the change abandoned hook. */
DECL|field|changeAbandonedHook
specifier|private
specifier|final
name|File
name|changeAbandonedHook
decl_stmt|;
comment|/** Repository Manager. */
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
comment|/** Queue of hooks that need to run. */
DECL|field|hookQueue
specifier|private
specifier|final
name|WorkQueue
operator|.
name|Executor
name|hookQueue
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
comment|/**      * Create a new ChangeHookRunner.      *      * @param queue Queue to use when processing hooks.      * @param repoManager The repository manager.      * @param config Config file to use.      * @param sitePath The sitepath of this gerrit install.      * @param projectCache the project cache instance for the server.      */
annotation|@
name|Inject
DECL|method|ChangeHookRunner (final WorkQueue queue, final GitRepositoryManager repoManager, @GerritServerConfig final Config config, final SitePaths sitePath, final ProjectCache projectCache)
specifier|public
name|ChangeHookRunner
parameter_list|(
specifier|final
name|WorkQueue
name|queue
parameter_list|,
specifier|final
name|GitRepositoryManager
name|repoManager
parameter_list|,
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|config
parameter_list|,
specifier|final
name|SitePaths
name|sitePath
parameter_list|,
specifier|final
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|hookQueue
operator|=
name|queue
operator|.
name|createQueue
argument_list|(
literal|1
argument_list|,
literal|"hook"
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
specifier|final
name|File
name|hooksPath
init|=
name|sitePath
operator|.
name|resolve
argument_list|(
name|getValue
argument_list|(
name|config
argument_list|,
literal|"hooks"
argument_list|,
literal|"path"
argument_list|,
name|sitePath
operator|.
name|hooks_dir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|patchsetCreatedHook
operator|=
name|sitePath
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
name|hooksPath
argument_list|,
name|getValue
argument_list|(
name|config
argument_list|,
literal|"hooks"
argument_list|,
literal|"patchsetCreatedHook"
argument_list|,
literal|"patchset-created"
argument_list|)
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|commentAddedHook
operator|=
name|sitePath
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
name|hooksPath
argument_list|,
name|getValue
argument_list|(
name|config
argument_list|,
literal|"hooks"
argument_list|,
literal|"commentAddedHook"
argument_list|,
literal|"comment-added"
argument_list|)
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|changeMergedHook
operator|=
name|sitePath
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
name|hooksPath
argument_list|,
name|getValue
argument_list|(
name|config
argument_list|,
literal|"hooks"
argument_list|,
literal|"changeMergedHook"
argument_list|,
literal|"change-merged"
argument_list|)
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|changeAbandonedHook
operator|=
name|sitePath
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
name|hooksPath
argument_list|,
name|getValue
argument_list|(
name|config
argument_list|,
literal|"hooks"
argument_list|,
literal|"changeAbandonedHook"
argument_list|,
literal|"change-abandoned"
argument_list|)
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|addChangeListener (ChangeListener listener, IdentifiedUser user)
specifier|public
name|void
name|addChangeListener
parameter_list|(
name|ChangeListener
name|listener
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|listeners
operator|.
name|put
argument_list|(
name|listener
argument_list|,
operator|new
name|ChangeListenerHolder
argument_list|(
name|listener
argument_list|,
name|user
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|removeChangeListener (ChangeListener listener)
specifier|public
name|void
name|removeChangeListener
parameter_list|(
name|ChangeListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Helper Method for getting values from the config.      *      * @param config Config file to get value from.      * @param section Section to look in.      * @param setting Setting to get.      * @param fallback Fallback value.      * @return Setting value if found, else fallback.      */
DECL|method|getValue (final Config config, final String section, final String setting, final String fallback)
specifier|private
name|String
name|getValue
parameter_list|(
specifier|final
name|Config
name|config
parameter_list|,
specifier|final
name|String
name|section
parameter_list|,
specifier|final
name|String
name|setting
parameter_list|,
specifier|final
name|String
name|fallback
parameter_list|)
block|{
specifier|final
name|String
name|result
init|=
name|config
operator|.
name|getString
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|setting
argument_list|)
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|fallback
else|:
name|result
return|;
block|}
comment|/**      * Get the Repository for the given change, or null on error.      *      * @param change Change to get repo for,      * @return Repository or null.      */
DECL|method|getRepo (final Change change)
specifier|private
name|Repository
name|getRepo
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|)
block|{
try|try
block|{
return|return
name|repoManager
operator|.
name|openRepository
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Fire the Patchset Created Hook.      *      * @param change The change itself.      * @param patchSet The Patchset that was created.      */
DECL|method|doPatchsetCreatedHook (final Change change, final PatchSet patchSet)
specifier|public
name|void
name|doPatchsetCreatedHook
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|PatchSet
name|patchSet
parameter_list|)
block|{
specifier|final
name|PatchSetCreatedEvent
name|event
init|=
operator|new
name|PatchSetCreatedEvent
argument_list|()
decl_stmt|;
name|event
operator|.
name|project
operator|=
name|change
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|branch
operator|=
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getShortName
argument_list|()
expr_stmt|;
name|event
operator|.
name|change
operator|=
name|change
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|commit
operator|=
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|patchSet
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|patchSet
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|fireEvent
argument_list|(
name|change
argument_list|,
name|event
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
name|patchsetCreatedHook
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--change"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|change
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--project"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|project
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--branch"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|branch
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--commit"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|commit
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--patchset"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|patchSet
argument_list|)
expr_stmt|;
name|runHook
argument_list|(
name|getRepo
argument_list|(
name|change
argument_list|)
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fire the Comment Added Hook.      *      * @param change The change itself.      * @param patchSet The patchset this comment is related to.      * @param account The gerrit user who commited the change.      * @param comment The comment given.      * @param approvals Map of Approval Categories and Scores      */
DECL|method|doCommentAddedHook (final Change change, final Account account, final PatchSet patchSet, final String comment, final Map<ApprovalCategory.Id, ApprovalCategoryValue.Id> approvals)
specifier|public
name|void
name|doCommentAddedHook
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|Account
name|account
parameter_list|,
specifier|final
name|PatchSet
name|patchSet
parameter_list|,
specifier|final
name|String
name|comment
parameter_list|,
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|approvals
parameter_list|)
block|{
specifier|final
name|CommentAddedEvent
name|event
init|=
operator|new
name|CommentAddedEvent
argument_list|()
decl_stmt|;
name|event
operator|.
name|project
operator|=
name|change
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|branch
operator|=
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getShortName
argument_list|()
expr_stmt|;
name|event
operator|.
name|change
operator|=
name|change
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|author
operator|=
name|getAuthorAttribute
argument_list|(
name|account
argument_list|)
expr_stmt|;
name|event
operator|.
name|revision
operator|=
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|comment
operator|=
name|comment
expr_stmt|;
if|if
condition|(
name|approvals
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|event
operator|.
name|approvals
operator|=
operator|new
name|ApprovalAttribute
index|[
name|approvals
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|approval
range|:
name|approvals
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ApprovalAttribute
name|a
init|=
operator|new
name|ApprovalAttribute
argument_list|()
decl_stmt|;
name|a
operator|.
name|type
operator|=
name|approval
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|a
operator|.
name|value
operator|=
name|Short
operator|.
name|toString
argument_list|(
name|approval
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|approvals
index|[
name|i
operator|++
index|]
operator|=
name|a
expr_stmt|;
block|}
block|}
name|fireEvent
argument_list|(
name|change
argument_list|,
name|event
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
name|commentAddedHook
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--change"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|change
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--project"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|project
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--branch"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|branch
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--author"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|getDisplayName
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--commit"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|revision
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--comment"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|comment
operator|==
literal|null
condition|?
literal|""
else|:
name|comment
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|approval
range|:
name|approvals
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"--"
operator|+
name|approval
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|Short
operator|.
name|toString
argument_list|(
name|approval
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|runHook
argument_list|(
name|getRepo
argument_list|(
name|change
argument_list|)
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fire the Change Merged Hook.      *      * @param change The change itself.      * @param account The gerrit user who commited the change.      * @param patchSet The patchset that was merged.      */
DECL|method|doChangeMergedHook (final Change change, final Account account, final PatchSet patchSet)
specifier|public
name|void
name|doChangeMergedHook
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|Account
name|account
parameter_list|,
specifier|final
name|PatchSet
name|patchSet
parameter_list|)
block|{
specifier|final
name|ChangeMergedEvent
name|event
init|=
operator|new
name|ChangeMergedEvent
argument_list|()
decl_stmt|;
name|event
operator|.
name|project
operator|=
name|change
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|branch
operator|=
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getShortName
argument_list|()
expr_stmt|;
name|event
operator|.
name|change
operator|=
name|change
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|submitter
operator|=
name|getAuthorAttribute
argument_list|(
name|account
argument_list|)
expr_stmt|;
name|event
operator|.
name|patchSet
operator|=
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|description
operator|=
name|change
operator|.
name|getSubject
argument_list|()
expr_stmt|;
name|fireEvent
argument_list|(
name|change
argument_list|,
name|event
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
name|changeMergedHook
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--change"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|change
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--project"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|project
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--branch"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|branch
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--submitter"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|getDisplayName
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--commit"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|patchSet
argument_list|)
expr_stmt|;
name|runHook
argument_list|(
name|getRepo
argument_list|(
name|change
argument_list|)
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fire the Change Abandoned Hook.      *      * @param change The change itself.      * @param account The gerrit user who abandoned the change.      * @param reason Reason for abandoning the change.      */
DECL|method|doChangeAbandonedHook (final Change change, final Account account, final String reason)
specifier|public
name|void
name|doChangeAbandonedHook
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|Account
name|account
parameter_list|,
specifier|final
name|String
name|reason
parameter_list|)
block|{
specifier|final
name|ChangeAbandonedEvent
name|event
init|=
operator|new
name|ChangeAbandonedEvent
argument_list|()
decl_stmt|;
name|event
operator|.
name|project
operator|=
name|change
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|branch
operator|=
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getShortName
argument_list|()
expr_stmt|;
name|event
operator|.
name|change
operator|=
name|change
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|event
operator|.
name|author
operator|=
name|getAuthorAttribute
argument_list|(
name|account
argument_list|)
expr_stmt|;
name|event
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
name|fireEvent
argument_list|(
name|change
argument_list|,
name|event
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
name|changeAbandonedHook
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--change"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|change
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--project"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|project
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--branch"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|event
operator|.
name|branch
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--abandoner"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|getDisplayName
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"--reason"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|reason
operator|==
literal|null
condition|?
literal|""
else|:
name|reason
argument_list|)
expr_stmt|;
name|runHook
argument_list|(
name|getRepo
argument_list|(
name|change
argument_list|)
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
DECL|method|fireEvent (final Change change, final ChangeEvent event)
specifier|private
name|void
name|fireEvent
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|ChangeEvent
name|event
parameter_list|)
block|{
for|for
control|(
name|ChangeListenerHolder
name|holder
range|:
name|listeners
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|isVisibleTo
argument_list|(
name|change
argument_list|,
name|holder
operator|.
name|user
argument_list|)
condition|)
block|{
name|holder
operator|.
name|listener
operator|.
name|onChangeEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isVisibleTo (Change change, IdentifiedUser user)
specifier|private
name|boolean
name|isVisibleTo
parameter_list|(
name|Change
name|change
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
block|{
specifier|final
name|ProjectState
name|pe
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pe
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|ProjectControl
name|pc
init|=
name|pe
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
decl_stmt|;
return|return
name|pc
operator|.
name|controlFor
argument_list|(
name|change
argument_list|)
operator|.
name|isVisible
argument_list|()
return|;
block|}
comment|/**      * Create an AuthorAttribute for the given account suitable for serialization to JSON.      *      * @param account      * @return object suitable for serialization to JSON      */
DECL|method|getAuthorAttribute (final Account account)
specifier|private
name|AuthorAttribute
name|getAuthorAttribute
parameter_list|(
specifier|final
name|Account
name|account
parameter_list|)
block|{
name|AuthorAttribute
name|author
init|=
operator|new
name|AuthorAttribute
argument_list|()
decl_stmt|;
name|author
operator|.
name|name
operator|=
name|account
operator|.
name|getFullName
argument_list|()
expr_stmt|;
name|author
operator|.
name|email
operator|=
name|account
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
return|return
name|author
return|;
block|}
comment|/**      * Get the display name for the given account.      *      * @param account Account to get name for.      * @return Name for this account.      */
DECL|method|getDisplayName (final Account account)
specifier|private
name|String
name|getDisplayName
parameter_list|(
specifier|final
name|Account
name|account
parameter_list|)
block|{
if|if
condition|(
name|account
operator|!=
literal|null
condition|)
block|{
name|String
name|result
init|=
operator|(
name|account
operator|.
name|getFullName
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|"Anonymous Coward"
else|:
name|account
operator|.
name|getFullName
argument_list|()
decl_stmt|;
if|if
condition|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|+=
literal|" ("
operator|+
name|account
operator|.
name|getPreferredEmail
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
return|return
literal|"Anonymous Coward"
return|;
block|}
comment|/**      * Run a hook.      *      * @param repo Repo to run the hook for.      * @param args Arguments to use to run the hook.      */
DECL|method|runHook (final Repository repo, final List<String> args)
specifier|private
specifier|synchronized
name|void
name|runHook
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
parameter_list|)
block|{
if|if
condition|(
name|repo
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"No repo found for hook."
argument_list|)
expr_stmt|;
return|return;
block|}
name|hookQueue
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|new
name|File
argument_list|(
name|args
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
specifier|final
name|ProcessBuilder
name|pb
init|=
operator|new
name|ProcessBuilder
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|pb
operator|.
name|redirectErrorStream
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|pb
operator|.
name|directory
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|env
init|=
name|pb
operator|.
name|environment
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"GIT_DIR"
argument_list|,
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Process
name|ps
init|=
name|pb
operator|.
name|start
argument_list|()
decl_stmt|;
name|ps
operator|.
name|getOutputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|ps
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"hook output: "
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|br
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{                             }
name|ps
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unexpected error during hook execution"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

