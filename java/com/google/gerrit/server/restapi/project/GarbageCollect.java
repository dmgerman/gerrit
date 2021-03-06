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
DECL|package|com.google.gerrit.server.restapi.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|project
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|GarbageCollectionResult
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
name|GlobalCapability
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
name|extensions
operator|.
name|annotations
operator|.
name|RequiresCapability
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicItem
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
name|extensions
operator|.
name|restapi
operator|.
name|BinaryResult
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
name|extensions
operator|.
name|restapi
operator|.
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|RestModifyView
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
name|extensions
operator|.
name|webui
operator|.
name|UiAction
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
name|UrlFormatter
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
name|GarbageCollection
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
name|LocalDiskRepositoryManager
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
name|ioutil
operator|.
name|HexFormat
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
name|ProjectResource
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
name|restapi
operator|.
name|project
operator|.
name|GarbageCollect
operator|.
name|Input
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|Optional
import|;
end_import

begin_comment
comment|/** REST endpoint that executes GC on a project. */
end_comment

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|RUN_GC
argument_list|)
annotation|@
name|Singleton
DECL|class|GarbageCollect
specifier|public
class|class
name|GarbageCollect
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|Input
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|showProgress
specifier|public
name|boolean
name|showProgress
decl_stmt|;
DECL|field|aggressive
specifier|public
name|boolean
name|aggressive
decl_stmt|;
DECL|field|async
specifier|public
name|boolean
name|async
decl_stmt|;
block|}
DECL|field|canGC
specifier|private
specifier|final
name|boolean
name|canGC
decl_stmt|;
DECL|field|garbageCollectionFactory
specifier|private
specifier|final
name|GarbageCollection
operator|.
name|Factory
name|garbageCollectionFactory
decl_stmt|;
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|field|urlFormatter
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|UrlFormatter
argument_list|>
name|urlFormatter
decl_stmt|;
annotation|@
name|Inject
DECL|method|GarbageCollect ( GitRepositoryManager repoManager, GarbageCollection.Factory garbageCollectionFactory, WorkQueue workQueue, DynamicItem<UrlFormatter> urlFormatter)
name|GarbageCollect
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|GarbageCollection
operator|.
name|Factory
name|garbageCollectionFactory
parameter_list|,
name|WorkQueue
name|workQueue
parameter_list|,
name|DynamicItem
argument_list|<
name|UrlFormatter
argument_list|>
name|urlFormatter
parameter_list|)
block|{
name|this
operator|.
name|workQueue
operator|=
name|workQueue
expr_stmt|;
name|this
operator|.
name|urlFormatter
operator|=
name|urlFormatter
expr_stmt|;
name|this
operator|.
name|canGC
operator|=
name|repoManager
operator|instanceof
name|LocalDiskRepositoryManager
expr_stmt|;
name|this
operator|.
name|garbageCollectionFactory
operator|=
name|garbageCollectionFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|rsrc
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|async
condition|)
block|{
return|return
name|applyAsync
argument_list|(
name|project
argument_list|,
name|input
argument_list|)
return|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|applySync
argument_list|(
name|project
argument_list|,
name|input
argument_list|)
argument_list|)
return|;
block|}
DECL|method|applyAsync (Project.NameKey project, Input input)
specifier|private
name|Response
operator|.
name|Accepted
name|applyAsync
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Input
name|input
parameter_list|)
block|{
name|Runnable
name|job
init|=
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
name|runGC
argument_list|(
name|project
argument_list|,
name|input
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Run "
operator|+
operator|(
name|input
operator|.
name|aggressive
condition|?
literal|"aggressive "
else|:
literal|""
operator|)
operator|+
literal|"garbage collection on project "
operator|+
name|project
operator|.
name|get
argument_list|()
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|WorkQueue
operator|.
name|Task
argument_list|<
name|Void
argument_list|>
name|task
init|=
operator|(
name|WorkQueue
operator|.
name|Task
argument_list|<
name|Void
argument_list|>
operator|)
name|workQueue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|submit
argument_list|(
name|job
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|url
init|=
name|urlFormatter
operator|.
name|get
argument_list|()
operator|.
name|getRestUrl
argument_list|(
literal|"a/config/server/tasks/"
operator|+
name|HexFormat
operator|.
name|fromInt
argument_list|(
name|task
operator|.
name|getTaskId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// We're in a HTTP handler, so must be present.
name|checkState
argument_list|(
name|url
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|accepted
argument_list|(
name|url
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
DECL|method|applySync (Project.NameKey project, Input input)
specifier|private
name|BinaryResult
name|applySync
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Input
name|input
parameter_list|)
block|{
return|return
operator|new
name|BinaryResult
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|println
parameter_list|()
block|{
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|PrintWriter
name|progressWriter
init|=
name|input
operator|.
name|showProgress
condition|?
name|writer
else|:
literal|null
decl_stmt|;
name|GarbageCollectionResult
name|result
init|=
name|runGC
argument_list|(
name|project
argument_list|,
name|input
argument_list|,
name|progressWriter
argument_list|)
decl_stmt|;
name|String
name|msg
init|=
literal|"Garbage collection completed successfully."
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|hasErrors
argument_list|()
condition|)
block|{
for|for
control|(
name|GarbageCollectionResult
operator|.
name|Error
name|e
range|:
name|result
operator|.
name|getErrors
argument_list|()
control|)
block|{
switch|switch
condition|(
name|e
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|REPOSITORY_NOT_FOUND
case|:
name|msg
operator|=
literal|"Error: project \""
operator|+
name|e
operator|.
name|getProjectName
argument_list|()
operator|+
literal|"\" not found."
expr_stmt|;
break|break;
case|case
name|GC_ALREADY_SCHEDULED
case|:
name|msg
operator|=
literal|"Error: garbage collection for project \""
operator|+
name|e
operator|.
name|getProjectName
argument_list|()
operator|+
literal|"\" was already scheduled."
expr_stmt|;
break|break;
case|case
name|GC_FAILED
case|:
name|msg
operator|=
literal|"Error: garbage collection for project \""
operator|+
name|e
operator|.
name|getProjectName
argument_list|()
operator|+
literal|"\" failed."
expr_stmt|;
break|break;
default|default:
name|msg
operator|=
literal|"Error: garbage collection for project \""
operator|+
name|e
operator|.
name|getProjectName
argument_list|()
operator|+
literal|"\" failed: "
operator|+
name|e
operator|.
name|getType
argument_list|()
operator|+
literal|"."
expr_stmt|;
block|}
block|}
block|}
name|writer
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|setCharacterEncoding
argument_list|(
name|UTF_8
argument_list|)
operator|.
name|disableGzip
argument_list|()
return|;
block|}
DECL|method|runGC (Project.NameKey project, Input input, PrintWriter progressWriter)
name|GarbageCollectionResult
name|runGC
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Input
name|input
parameter_list|,
name|PrintWriter
name|progressWriter
parameter_list|)
block|{
return|return
name|garbageCollectionFactory
operator|.
name|create
argument_list|()
operator|.
name|run
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|project
argument_list|)
argument_list|,
name|input
operator|.
name|aggressive
argument_list|,
name|progressWriter
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getDescription (ProjectResource rsrc)
specifier|public
name|UiAction
operator|.
name|Description
name|getDescription
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|)
block|{
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Run GC"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Triggers the Git Garbage Collection for this project."
argument_list|)
operator|.
name|setVisible
argument_list|(
name|canGC
argument_list|)
return|;
block|}
block|}
end_class

end_unit

