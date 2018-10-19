begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.change
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
name|change
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|AuthException
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
name|IdString
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
name|ResourceConflictException
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
name|ResourceNotFoundException
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
name|RestApiException
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
name|RestCollection
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
name|RestView
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
name|TopLevelResource
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
name|server
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
name|change
operator|.
name|ChangeFinder
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
name|change
operator|.
name|ChangeResource
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
name|notedb
operator|.
name|ChangeNotes
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|ProjectState
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
name|server
operator|.
name|OrmException
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
name|Provider
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
name|util
operator|.
name|List
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ChangesCollection
specifier|public
class|class
name|ChangesCollection
implements|implements
name|RestCollection
argument_list|<
name|TopLevelResource
argument_list|,
name|ChangeResource
argument_list|>
block|{
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|queryFactory
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryChanges
argument_list|>
name|queryFactory
decl_stmt|;
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ChangeResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|changeFinder
specifier|private
specifier|final
name|ChangeFinder
name|changeFinder
decl_stmt|;
DECL|field|changeResourceFactory
specifier|private
specifier|final
name|ChangeResource
operator|.
name|Factory
name|changeResourceFactory
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangesCollection ( Provider<ReviewDb> db, Provider<CurrentUser> user, Provider<QueryChanges> queryFactory, DynamicMap<RestView<ChangeResource>> views, ChangeFinder changeFinder, ChangeResource.Factory changeResourceFactory, PermissionBackend permissionBackend, ProjectCache projectCache)
specifier|public
name|ChangesCollection
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|,
name|Provider
argument_list|<
name|QueryChanges
argument_list|>
name|queryFactory
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ChangeResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|ChangeFinder
name|changeFinder
parameter_list|,
name|ChangeResource
operator|.
name|Factory
name|changeResourceFactory
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|queryFactory
operator|=
name|queryFactory
expr_stmt|;
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|changeFinder
operator|=
name|changeFinder
expr_stmt|;
name|this
operator|.
name|changeResourceFactory
operator|=
name|changeResourceFactory
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|QueryChanges
name|list
parameter_list|()
block|{
return|return
name|queryFactory
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ChangeResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|parse (TopLevelResource root, IdString id)
specifier|public
name|ChangeResource
name|parse
parameter_list|(
name|TopLevelResource
name|root
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|OrmException
throws|,
name|PermissionBackendException
throws|,
name|IOException
block|{
name|List
argument_list|<
name|ChangeNotes
argument_list|>
name|notes
init|=
name|changeFinder
operator|.
name|find
argument_list|(
name|id
operator|.
name|encoded
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|notes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|notes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"Multiple changes found for "
operator|+
name|id
argument_list|)
throw|;
block|}
name|ChangeNotes
name|change
init|=
name|notes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|canRead
argument_list|(
name|change
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
name|checkProjectStatePermitsRead
argument_list|(
name|change
operator|.
name|getProjectName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|changeResourceFactory
operator|.
name|create
argument_list|(
name|change
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parse (Change.Id id)
specifier|public
name|ChangeResource
name|parse
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|ResourceConflictException
throws|,
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|PermissionBackendException
throws|,
name|IOException
block|{
name|List
argument_list|<
name|ChangeNotes
argument_list|>
name|notes
init|=
name|changeFinder
operator|.
name|find
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|notes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|toIdString
argument_list|(
name|id
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|notes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"Multiple changes found for "
operator|+
name|id
argument_list|)
throw|;
block|}
name|ChangeNotes
name|change
init|=
name|notes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|canRead
argument_list|(
name|change
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|toIdString
argument_list|(
name|id
argument_list|)
argument_list|)
throw|;
block|}
name|checkProjectStatePermitsRead
argument_list|(
name|change
operator|.
name|getProjectName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|changeResourceFactory
operator|.
name|create
argument_list|(
name|change
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toIdString (Change.Id id)
specifier|private
specifier|static
name|IdString
name|toIdString
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|id
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parse (ChangeNotes notes, CurrentUser user)
specifier|public
name|ChangeResource
name|parse
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
name|changeResourceFactory
operator|.
name|create
argument_list|(
name|notes
argument_list|,
name|user
argument_list|)
return|;
block|}
DECL|method|canRead (ChangeNotes notes)
specifier|private
name|boolean
name|canRead
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|IOException
block|{
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|change
argument_list|(
name|notes
argument_list|)
operator|.
name|database
argument_list|(
name|db
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|projectState
operator|.
name|statePermitsRead
argument_list|()
return|;
block|}
DECL|method|checkProjectStatePermitsRead (Project.NameKey project)
specifier|private
name|void
name|checkProjectStatePermitsRead
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"project not found: "
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
name|projectState
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

